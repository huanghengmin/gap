package com.inetec.ichange.service.monitor.business;

import com.inetec.common.logs.LogHelper;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.alarm.AlarmService;
import com.inetec.ichange.service.monitor.databean.AlertDataBean;
import com.inetec.ichange.service.monitor.databean.BusinessDataBean;
import com.inetec.ichange.service.monitor.databean.dao.BusinessLogDao;
import com.inetec.ichange.service.monitor.databean.entity.BusinessLog;
import com.inetec.ichange.service.monitor.syslog.format.ILogFormat;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 边界接入业务日志处理
 */

public class BusinessLogImp implements Runnable {
    private static final Logger m_log = Logger.getLogger(BusinessLogImp.class);
    private boolean isRun = false;
    private byte[] buff;
    private ByteArrayOutputStream buffs = new ByteArrayOutputStream();
    private BusinessLogDao dao = new BusinessLogDao();

    public boolean isRun() {

        return isRun;
    }

    public BusinessLogImp() {

    }

    public void processLog(LogHelper[] log) {
        for (int i=0;i<log.length;i++){
            BusinessLog blog = getBusinessLogByLogHelper(log[i]);
            if(!blog.getBusiness_type().equals("proxy")){
                try {
                    if(!dao.isSavedBusinessLog(blog)){
                        dao.saveBusinessLog(blog);
                    }
                } catch (Exception e) {
                    m_log.error("查找数据库失败",e);
                }
            }
        }

    }

    public void run() {
        isRun = true;
        while (isRun) {
            try {
                if (buff != null) {
                    businessLog();
                }
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                //okay
            }
        }
    }

    public void businessLog() {
        try {
            LogHelper[] logs = LogHelper.getLogHelper(buff);
            for (int i = 0; i < logs.length; i++) {
                BusinessLog businessLog = getBusinessLogByLogHelper(logs[i]);
                if(!businessLog.getBusiness_type().equals("proxy")){
                    if(!dao.isSavedBusinessLog(businessLog)){
                        //日志入库
                        dao.saveBusinessLog(businessLog);
                        //业务状态统计
                    }
                }
            }

        } catch (RuntimeException r) {
            m_log.warn("log parser erro!", r);
        } catch (Exception e) {
            m_log.warn("业务日志入库失败!", e);
        }
        buff = null;
    }

    public void close() {
        isRun = false;
    }

    public BusinessLog getBusinessLogByLogHelper(LogHelper log) {

        BusinessLog blog = new BusinessLog();
        blog.setBusiness_name(log.getAppName());
        blog.setBusiness_type(log.getAppType());
        blog.setLevel(log.getLevel()==null?"info":log.getLevel());
        if(log.getAppType().equals("file")){
            blog.setFile_name(log.getFilename());
            blog.setJson_id(Integer.parseInt(log.getPk_id()));
            blog.setAudit_count(Integer.parseInt(log.getFlux()));
            blog.setDest_ip(log.getDest_ip());
            blog.setDest_port(log.getDest_port());
            blog.setSource_port(log.getSource_port());
        } else if(log.getAppType().equals("db")){
            if(log.getOperType().equals("external")){
                blog.setSource_jdbc(log.getDbName());
            } else if(log.getOperType().equals("internal")) {
                blog.setDest_jdbc(log.getDbName());
            }
            blog.setFile_name(log.getTableName());
            blog.setAudit_count(Integer.parseInt(log.getRecordCount()));
        }  else if(log.getAppType().equals("proxy")) {
            blog.setCount(Integer.parseInt(log.getRecordCount()));
        }
        blog.setSource_ip(log.getSource_ip());

        blog.setLog_time(Timestamp.valueOf(log.getDate()));
        blog.setStatus(Integer.parseInt(log.getStatusCode()));
        blog.setPlugin(log.getOperType());

        BusinessDataBean bean = BusinessPlatformService.dataset.getBusinessDataBeanByID(log.getAppName());
        bean.setRecord_count(bean.getRecord_count() + Integer.parseInt(log.getRecordCount()));
//        m_log.info("业务日志类型" + log.getLevel());

        if (log.getLevel().equalsIgnoreCase("warn")) {
            bean.setAlert_count(bean.getAlert_count() + 1);
            //todo:添加报警处理
            bean.setStatus(BusinessDataBean.I_Status_Alert);
            Service.alert.process(AlarmService.AlertType_Business, getAlertBean(blog));
            Service.alert.process(AlarmService.AlertType_Security, getAlertBean(blog));
        }
        if (log.getLevel().equalsIgnoreCase("error")) {
            bean.setAlert_count(bean.getAlert_count() + 1);
            //todo:添加报警处理
            Service.alert.process(AlarmService.AlertType_Business, getAlertBean(blog));
            Service.alert.process(AlarmService.AlertType_Security, getAlertBean(blog));
            bean.setStatus(BusinessDataBean.I_Status_Error);

        }
        bean.setConnect_count(Integer.parseInt(log.getCurconnect()));
        if (log.getFlux() == null || log.getFlux().equalsIgnoreCase("null") || log.getFlux().equalsIgnoreCase("")) {
            log.setFlux("0");
        }
        bean.setRecord_count(bean.getRecord_count() + Integer.parseInt(log.getRecordCount()));
        bean.setConnect_count(bean.getConnect_count() + Integer.parseInt(log.getCurconnect()));
        if (log.getFlux() == null || log.getFlux().equalsIgnoreCase("null") || log.getFlux().equalsIgnoreCase("")) {
            log.setFlux("0");
        }
        float dataFow = Float.valueOf(log.getFlux());

        String str = new String(""+dataFow/(1024*1024));
        if(str.indexOf("E-")>-1) {
            dataFow = (float)0.01;
        } else {
            int len = str.toString().substring(str.toString().indexOf(".")+1).length();
            if(len>2){
                int three = Integer.parseInt(str.substring(str.indexOf(".") + 3,str.indexOf(".") + 4));
                dataFow = Float.parseFloat(str.substring(0,str.indexOf(".") + 3));
                if(three>=5){
                    dataFow += (float)0.01;
                }
            } else {
                dataFow = Float.valueOf(str);
            }
        }
        bean.setXt_dataflow(bean.getXt_dataflow() + dataFow);
        BusinessPlatformService.dataset.returnBusinessDataBean(log.getAppName(), bean);
        return blog;
    }


    public static void main(String arg[]) throws Exception {
        BusinessLogImp process = new BusinessLogImp();
        new Thread(process).start();
        while (true) {
            Thread.sleep(1000);
        }
    }

    public AlertDataBean getAlertBean(BusinessLog log) {
        AlertDataBean result = new AlertDataBean();
        if (log.getDest_ip() != null && !log.getDest_ip().equalsIgnoreCase(""))
            result.setIp(log.getDest_ip());
        if (log.getSource_ip() != null && !log.getSource_ip().equalsIgnoreCase(""))
            result.setIp(log.getSource_ip());
        result.setName(log.getBusiness_name());
        result.setAlert_time(log.getLog_time());
//        result.setUsername(log.getUser_name());
//        result.setAlert_info(log.getAlert_info());
//        result.setCode(alert.getAlert_type_code());
        result.setIsread("N");
        result.setObjType("business");
        return result;

    }

    public void processLogByte(byte[] bytes) {
        this.buff = bytes;
    }
}
