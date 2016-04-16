package com.inetec.ichange.service;

import com.inetec.common.config.stp.ConfigParser;
import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.config.stp.nodes.IChange;
import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.service.monitor.audit.AuditBusinessService;
import com.inetec.ichange.service.monitor.audit.AuditEquipmentService;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.EChange;
import com.inetec.ichange.service.monitor.alarm.AlarmService;
import com.inetec.ichange.service.monitor.business.BusinessPlatformService;
import com.inetec.ichange.service.monitor.databean.*;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
import com.inetec.ichange.service.monitor.device.DeviceService;
import com.inetec.ichange.service.monitor.device.PingTelnetService;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.monitor.syslog.SysLogSendService;
import com.inetec.ichange.service.monitor.utils.BusinessDataBeanSet;
import com.inetec.ichange.service.monitor.utils.ConfigUtils;
import com.inetec.ichange.service.monitor.utils.ServiceMonitorFactory;
import com.inetec.ichange.service.utils.*;
import com.inetec.ichange.service.utils.syslog.SyslogServer;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-15
 * Time: 20:17:53
 * To change this template use File | Settings | File Templates.
 */
public final class Service  {
    public static Category s_log = null;
    public static SipTypeStatusSet siptypeSet = null;

    public Service() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Monitor  Service Page</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=white>");
        writer.println("<table border=\"0\">");
        writer.println("<tr>");
        writer.println("<td>");
        writer.println("<h1>Fertec Service  Status Page</h1>");
        writer.println("<P>Service is running.<P><BR>");
        writer.println("</td>");
        writer.println("</tr>");
        writer.println("</table>");
        writer.println("</body>");
        writer.println("</html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            if (s_log == null)
                s_log = Category.getInstance(Service.class);
            String reqtype = request.getParameter(ServiceUtil.HDR_ServiceRequestType);
            if (reqtype == null) {
                reqtype = request.getHeader(ServiceUtil.HDR_ServiceRequestType);
            }
            if (reqtype == null)
                throw new Ex().set(EChange.E_UNKNOWN, new Message("Service Request  commond is null."));

            if (reqtype.equalsIgnoreCase(ServiceUtil.STR_REQTP_ServiceDataPost)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else if (reqtype.equalsIgnoreCase(ServiceUtil.STR_REQTP_ServiceControlPost)) {
                if (s_log.isDebugEnabled())
                    s_log.debug("Data Control Request received.");
                String commandBody = request.getParameter(ServiceUtil.Str_MonitorCommond);
                if (commandBody == null) {
                    commandBody = request.getHeader(ServiceUtil.Str_MonitorCommond);
                }
                if (commandBody == null) {
                    commandBody = "";
                }
                if (commandBody.equalsIgnoreCase("")) {
                    s_log.debug("Data Control bad Request commandBody:" + commandBody);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

                DataAttributes fp = receiveServiceControl(request);
                byte[] data = null;
                if(fp!=null){
                    if (fp.getValue("Command") == null) {
                        if (fp.getStatus().isSuccess()) {
                            String responsecode = fp.getProperty(ServiceUtil.Str_ResponseProcessStatus);
                            if (responsecode != null) {
                                int status = Integer.parseInt(responsecode);
                                response.setStatus(status);
                                if (fp.isResultData()) {
                                    data = DataAttributes.readInputStream(fp.getResultData());
                                    response.setContentLength(data.length);
                                    response.getOutputStream().write(data);
                                }
                            }
                        } else {
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        if (fp.getResultData() != null)
                            data = DataAttributes.readInputStream(fp.getResultData());
                        else
                            data = fp.getContent();
                        response.setContentLength(data.length);
                        response.getOutputStream().write(data);
                    }
                    response.flushBuffer();
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    s_log.info("接收信息结束!");
                    return;
                }
            } else {

                s_log.error("The Request Type test '" + reqtype + "' is unrecognizable.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "<HTML><BODY>The Request Type  '" + reqtype +
                                "' is unrecognizable.</Body></HTML>");
            }
        } catch (Ex Ex) {    // Server error; report to client.

            s_log.info("Could not process the request from " + Ex.getMessage());

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    , " - Could not process the request from " +
                    ": " + Ex.getMessage());

            s_log.error("" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR +
                    " - Could not process the request: ", Ex);

        } catch (RuntimeException Ex) {
            s_log.error("Run-time exception is caught:- ", Ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    , " - Could not process the request from " +
                    ": " + Ex.getMessage());
        }
    }

    public void init(String path, String fileName) throws ServletException {
        if (s_log == null)
            s_log = Category.getInstance(Service.class);
        System.setProperty("privatenetwork", String.valueOf(ConfigUtils.getPrivated()).toUpperCase());

        try {
            if(s_log==null){
                s_log = Category.getInstance(Service.class);
            }

            //NdcProvider.push(ChangeServerImp.Str_DataCollectionService);
            m_fileseparator = System.getProperty("file.separator");
            //License.validator();
            //License.start();
            if (path == null) {
                throw new Ex().set(EChange.E_UNKNOWN, new Message("The required property 'stp.home' is not set."));
            }

            PropertyConfigurator.configure(path + m_fileseparator + "etc" + m_fileseparator + "log4j.properties");
            String privateNetwork = System.getProperties().getProperty("privatenetwork");
            if(privateNetwork.equalsIgnoreCase("false")){  // 源端 -- 非可信服务
                s_log.info("外网服务 -- 启动开始,等待1分钟...");
                Thread.sleep(1000*60);
            }else {
                s_log.info("内网服务 -- 启动开始...");
            }
//            s_log.info("加载数据库服务 开启开始....");
//            loadDataBase();
//            s_log.info("加载数据库服务 开启成功....");
            s_log.info("SYSLOG服务 开启成功....");
            runSysLogService();
            s_log.info("SYSLOG服务 开启成功....");
            s_log.info("SNMP服务 开启开始....");
            runSnmpMonitorService();
            s_log.info("SNMP服务 开启成功....");
            s_log.info("设备扫描服务 开启开始...");
            runPingTelnetService();
            s_log.info("设备扫描服务 开启成功...");
            s_log.info("报警服务 开启开始....");
            runAlertService();
            s_log.info("报警服务 开启成功....");
            if(privateNetwork.equalsIgnoreCase("false")){  // 源端 -- 非可信服务
                s_log.info("发送设备监控信息服务 开启开始....");
                runDeviceService();
                s_log.info("发送设备监控信息服务 开启成功....");
                s_log.info("发送业务审计信息服务 开启开始....");
                runAuditBusinessService();
                s_log.info("发送业务审计信息服务 开启成功....");
                s_log.info("发送设备审计信息服务 开启开始....");
                runAuditEquipmentService();
                s_log.info("发送设备审计信息服务 开启成功....");

                s_log.info("外网服务 -- 启动成功...");
            } else {
                s_log.info("平台日志接收服务 开启开始....");
                runSysLogServer();
                s_log.info("平台日志接收服务 开启成功....");
                s_log.info("监听服务 开启开始....");
                runServiceUdpReceive();
                s_log.info("监听服务 开启成功....");
                s_log.info("业务监控服务 开启开始....");
                runBusinessService();
                s_log.info("业务监控服务 开启成功....");

                s_log.info("内网服务 -- 启动成功...");
            }

            if (s_log.isDebugEnabled())
                s_log.debug("Servlet Monitor Server is starting.");

            try {
                FileInputStream is = new FileInputStream(path + m_fileseparator + "etc" + m_fileseparator + "main.properties");
                Properties props = new Properties();
                props.load(new BufferedInputStream(is));
                Properties sysProps = System.getProperties();
                sysProps.putAll(props);

            } catch (IOException Ex) {
                s_log.error("Failed to read the property file " + path +
                        "/etc/main.properties", Ex);
                throw new Ex().set(EChange.E_UNKNOWN, new Message("Failed to read the property file{0}/etc/main.properties", path));
            }

            m_configName = fileName;

            if (m_configName == null) {
                new Ex().set(EChange.E_UNKNOWN, new Message("The required Config File  is not set."));
            }
            String ichangeHomePath = System.getProperty("ichange.home");
            if (ichangeHomePath == null || ichangeHomePath == "") {
                throw new Ex().set(E.E_InvalidArgument, new Message("没有设置环境变量ichange.home"));
            }
            if(privateNetwork.equalsIgnoreCase("false")){
                m_configPath = ichangeHomePath + m_fileseparator + "repository"+m_fileseparator+"external" + m_fileseparator;
            } else {
                m_configPath = ichangeHomePath + m_fileseparator + "repository" + m_fileseparator;
            }
            String strConfigFile = m_configPath + m_configName;
            //String strConfigRuleFile = m_configPath + configRuleFile;
            if (s_log.isDebugEnabled())
                s_log.debug("config file name:" + strConfigFile);

            //try {


            //config(strConfigFile);
            clearTempFile();


            //} catch (Ex ex) {
            //========================
            //add the NDC information
            //========================
            //NdcProvider.push(ChangeServerImp.Str_DataCollectionService);
            // s_log.error("init ServiceConfig failed " + ex.getMessage());
            // throw new Ex().set(EChange.E_UNKNOWN, ex, new Message("Failed to ServiceConfig initialize"));
            //}
            if (s_log.isDebugEnabled())
                s_log.debug("ServiceConfig initialized.");


        } catch (Ex Ex) {
            s_log.error("Init/Config failed.", Ex);
        } catch (RuntimeException Ex) {
            s_log.error("RuntimeException was caught during servlet initialization.", Ex);
            throw Ex;
        } catch (InterruptedException e) {
        }

    }

    private void runPingTelnetService() {
        if(Service.isPingTelnetService) {
            return;
        }
        Service.pingTelnetService.init();
        Service.pingTelnetService.start();
        Service.isPingTelnetService = true;
    }

    private void runSysLogServer() {
        if(Service.isRunSysLogSserver) {
            return;
        }
        Service.syslogServer.init();
        Service.syslogServer.start();
        Service.isRunSysLogSserver = true;
    }

    public void config(String config) throws Ex {
        ConfigParser configParser = new ConfigParser(config);
        IChange ichange = configParser.getRoot();
        Channel channel = ichange.getChannel("1");
//        ChannelInfo channelInfo1 = new ChannelInfo();
//        channelInfo1.set(channel, ichange.getPrivatePassword(), ichange.getPrivateKey());
//        channelInfo1.setPlatFormUrl();
//        m_dispPlatform = new CommandDisposer(channelInfo1, true);
//        ChannelInfo channelInfo = new ChannelInfo();
//        channelInfo.set(channel, ichange.getPrivatePassword(), ichange.getPrivateKey());
//        m_config = config;
//        m_dispService = new CommandDisposer(channelInfo, false);
//        m_externalFilename = m_configPath + "external" + m_fileseparator + m_configName;
        m_bPrivate = channel.getPrivated();
        System.setProperty("privatenetwork", String.valueOf(m_bPrivate));
        /*if (m_bPrivate) {
            m_serInitThread = new ServiceInitThread(config, m_externalFilename, true);
            try {
                IChangeUtils changeUtils = ichange.getIchangeUtils();
                String port = ichange.getIchangeUtils().getLogServerPort();

                if (port != null && !port.equals("")) {
                    logService.init(new Integer(port).intValue());
                } else {
                    logService.init();
                }

                //add siptype status
                initTypeStatus(ichange.getAllTypes());
                //okay
                logService.config(changeUtils.getLogServerUser(), changeUtils.getLogServerPassword(),
                        changeUtils.getLogsMaxSize(), changeUtils.getLogsMaxDay());
            } catch (IOException e) {
                throw new Ex().set(E.E_IOException, e, new Message("Log Service init error."));
            }
            //???????????.
            m_times = ichange.getIchangeUtils().getRestartTime();
            if (m_times != null && !m_times.equals("")) {
                m_restart = new ReStartTimerTask(m_times);
                m_restart.init();
            }

        } else {
            m_serInitThread = new ServiceInitThread(config, m_externalFilename, false);

        }
        if (m_restart != null) {
            m_restart.start();
        }
        m_serInitThread.start();*/
//        ServiceStatusThread serviceStatus = new ServiceStatusThread(m_bPrivate);
//        serviceStatus.start();


    }

    /*private void initTypeStatus(Type[] types) {
        if (types != null) {
            for (int i = 0; i < types.length; i++) {
                *//*if (types[i].getAppType() == null) {
                    types[i].setAppType("");
                    s_log.warn("config type app type is null.(" + types[i].getTypeName() + ")");
                }*//*
                if (types[i].getAppType().equals(Type.s_app_sipproxy)) {
                    SipTypeStatus sipchange = new SipTypeStatus();
                    sipchange.setName(types[i].getTypeName());
                    sipchange.setDesc(types[i].getDescription());
                    int max = Integer.parseInt(types[i].getPlugin().getSourceVideo().getPoolMax()) - Integer.parseInt(types[i].getPlugin().getSourceVideo().getPoolMin());
                    sipchange.setMax_connect(max);
                    if (types[i].isActive()) {
                        sipchange.setStatus(0);
                        sipchange.setAppstatus(0);
                    } else {
                        sipchange.setStatus(1);
                        sipchange.setAppstatus(1);
                    }
                    if (siptypeSet == null) {
                        siptypeSet = new SipTypeStatusSet();
                    }
                    siptypeSet.setTypeStatus(sipchange);
                }
            }
        } else {
            s_log.warn("config Type is null.");
        }
    }*/

    public void clearTempFile() {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempDirs = new File(tempDir);
        if (tempDirs.exists()) {
            File[] tempFiles = tempDirs.listFiles();
            for (int i = 0; i < tempFiles.length; i++) {
                tempFiles[i].delete();
            }
        }
    }

    public DataAttributes receiveServiceControl(HttpServletRequest req) throws Ex {
        DataAttributes result = new DataAttributes();
        String common = req.getParameter(ServiceUtil.Str_MonitorCommond);
        if (common == null || common == "") {
            common = req.getHeader(ServiceUtil.Str_MonitorCommond);
        }
        if (common == null || common == "") {
            throw new Ex().set(E.E_ObjectNotFound, new Message("Commond is null or empty."));
        }


        IServiceCommandProcess serviceCommonProcess = ServiceMonitorFactory.createServiceCommon(common);
        Enumeration enumeration = req.getParameterNames();
        Enumeration reqheader = req.getHeaderNames();

        while (enumeration.hasMoreElements()) {
            String hdr = (String) enumeration.nextElement();
            result.putValue(hdr.toLowerCase(), req.getParameter(hdr));
            //s_log.info("request parameter:" + hdr + " value:" + req.getParameter(hdr));
        }
        while (reqheader.hasMoreElements()) {
            String hdr = (String) reqheader.nextElement();
            result.putValue(hdr.toLowerCase(), req.getHeader(hdr));
            //s_log.info("request hdr:" + hdr + " value:" + req.getHeader(hdr));
        }

        try {
            if (req.getContentLength() > 0)
                result.setResultData(DataAttributes.readInputStream(req.getInputStream()));
            result = serviceCommonProcess.process("", result);
        } catch (IOException e) {
            throw new Ex().set(E.E_IOException, new Message("Request get Stream error."));
        }
        return result;

    }

    public DataAttributes reciveServiceData(HttpServletRequest req) throws Ex {
        DataAttributes result = new DataAttributes();
        String common = req.getHeader(ServiceUtil.HDR_ServiceDataType);
        if (common == null || common == "") {
            throw new Ex().set(E.E_ObjectNotFound, new Message("Commond is null or empty."));
        }
        result.putValue(ServiceUtil.STR_ChannelPrivate, String.valueOf(m_bPrivate));
        IServiceCommandProcess serviceCommonProcess = ServiceCommonFactory.createServiceCommon(common);
        try {
            result = serviceCommonProcess.process(req.getInputStream(), result);
        } catch (IOException e) {
            throw new Ex().set(E.E_IOException, new Message("Request get Stream error."));
        }
        return result;
    }

    private void runSnmpMonitorService() {
        if (Service.isRunSnmpMonitorService) {
            return;
        }
        snmpservice.init();
        new Thread(snmpservice).start();
        Service.isRunSnmpMonitorService = true;

    }

    private void runSysLogService() {
        if (Service.isRunSysLogService) {
            return;
        }
        sysLogSendService.init();
        sysLogSendService.start();
        Service.isRunSysLogService = true;

    }

    /**
     * 业务运行监控、目标端业务审计日志入库
     */
    private void runBusinessService() {
        if (Service.isRunBusinessPlatformMonitorService) {
            return;
        }
        List<BusinessDataBean> list = ConfigUtils.getRunBusinesses();
//        if(list.size()>0){
            businessPlatformService.init(list);
            new Thread(businessPlatformService).start();
            try {
                alert.init();
            } catch (Ex ex) {
                s_log.warn("alert init error.", ex);
            }
            Service.isRunBusinessPlatformMonitorService = true;
//        } else {
//            s_log.warn("业务不存在!");
//        }
    }

    /**
     * 启动并监听审计端口用于udp传输
     */
    private void runServiceUdpReceive(){
        if(Service.isRunServiceUdpReceive){
           return;
        }
        List<Integer> list = ConfigUtils.getPorts();
        serviceUdpReceive.init(list);
        serviceUdpReceive.start();
        Service.isRunServiceUdpReceive = true;
    }

    /**
     * 定时,定量,定数发送业务审计日志到目标端
     */
    private void runAuditBusinessService() {
        if(Service.isRunAuditBusinessService)    {
            return;
        }
        auditBusinessService.init();
        auditBusinessService.start();
        Service.isRunAuditBusinessService = true;
    }

    /**
     * 定时,定量,定数发送设备审计日志到目标端
     */
    private void runAuditEquipmentService() {
        if(Service.isRunAuditEquipmentService)    {
            return;
        }
        auditEquipmentService.init();
        auditEquipmentService.start();
        Service.isRunAuditEquipmentService = true;
    }

    private void runAlertService(){
        if(Service.isRunAlertAlarmService){
            return;
        }
        try {
            alert.init();
            alert.start();
            Service.isRunAlertAlarmService = true;
        } catch (Ex ex) {
            s_log.error("日志报警服务启动错误",ex);
        }
    }

    /**
     * 加载数据库 stp
     */
    private void loadDataBase() {
        if(Service.isLoadDataBase){
            return;
        }
        EquipmentDao dao = new EquipmentDao();
        dao.findById(1);
        Service.isLoadDataBase = true;
    }

    private void runDeviceService() {
        if(Service.isRunDeviceService){
            return;
        }
        deviceService.init();
        deviceService.start();
        Service.isRunDeviceService = true;
    }

    public static boolean m_bPrivate = false;
    public static boolean m_public = false;
    public static boolean m_publicConfigured = false;
    public static boolean m_bPrivateStart = false;
    public static boolean m_publicStart = false;

    public static CommandDisposer m_dispService = null;
    public static CommandDisposer m_dispPlatform = null;
    public static int I_SleepTime = 30 * 1000;
    public static String m_configPath = null;
    public static String m_fileseparator = null;
    public static String m_configName = null;
    public static String m_config = null;
    public static String m_externalFilename = null;
    public static boolean m_configred = false;
    public static boolean m_platformConfigred = false;
    public static ServiceInitThread m_serInitThread = null;
    public static byte[] m_typeStatus = null;

    public static String m_times = null;
    public static ReStartTimerTask m_restart = null;
    public static BusinessDataBeanSet busDataSet = new BusinessDataBeanSet();
    //public static DeviceDataBeanSet deviceDataSet = new DeviceDataBeanSet();
    public static boolean isRunSnmpMonitorService = false;
    public static boolean isRunSysLogService = false;
    public static boolean isRunSysLogSserver = false;
    public static boolean isPingTelnetService = false;
    public static boolean isRunBusinessPlatformMonitorService = false;
    public static boolean isRunAuditBusinessService = false;
    public static boolean isRunAuditEquipmentService = false;
    public static boolean isLoadDataBase = false;
    public static boolean isRunAlertAlarmService = false;
    public static boolean isRunServiceUdpReceive = false;
    public static boolean isRunDeviceService = false;
    public static SnmpMonitorService snmpservice = new SnmpMonitorService();
    public static BusinessPlatformService businessPlatformService = new BusinessPlatformService();
    public static SysLogSendService sysLogSendService = new SysLogSendService();
    public static SyslogServer syslogServer = new SyslogServer();   //log receive : (from platform.war)
    public static PingTelnetService pingTelnetService = new PingTelnetService();
    public static AlarmService alert = new AlarmService();
    public static AuditBusinessService auditBusinessService = new AuditBusinessService();
    public static AuditEquipmentService auditEquipmentService = new AuditEquipmentService();
    public static ServiceUdpReceive serviceUdpReceive = new ServiceUdpReceive();
    public static DeviceService deviceService = new DeviceService();

}
