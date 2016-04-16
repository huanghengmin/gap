package com.hzih.gap.web.action.port;

import com.hzih.gap.core.InitProcess;
import com.hzih.gap.core.PortEntity;
import com.hzih.gap.core.PortService;
import com.hzih.gap.domain.Tcp;
import com.hzih.gap.service.LogService;
import com.hzih.gap.service.impl.MonitorServiceImpl;
import com.hzih.gap.utils.Configuration;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.web.action.ActionBase;
import com.inetec.common.exception.Ex;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-7
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class PortAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(PortAction.class);
    private LogService logService;
    private static final String path = StringContext.systemPath +"/repository/app.xml";
    private static final String quartz_path = StringContext.systemPath +"/repository/quartz_job.xml";

    private MonitorServiceImpl monitorService;

    public String getTask() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "";
        try {
            json = read();
           // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
           // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String save() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String[] data = ServletRequestUtils.getStringParameters(request, "dataArray");
        String msg = "";
        try {
            boolean flag = save(data);

            // updateQuartzJob(data[0].split("&")[3]);
            msg = "client端保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "client端保存失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String add() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String id = ServletRequestUtils.getStringParameter(request,"id");
        String port = ServletRequestUtils.getStringParameter(request,"port");
        String worktime = ServletRequestUtils.getStringParameter(request,"worktime");
        String run = ServletRequestUtils.getStringParameter(request,"run");
        String type = ServletRequestUtils.getStringParameter(request,"type");
        String tunnel = ServletRequestUtils.getStringParameter(request,"tunnel");
        String tunneltype = ServletRequestUtils.getStringParameter(request,"tunneltype");
        PortEntity portEntity = new PortEntity();
        portEntity.setId(id);
        portEntity.setPort(port);
        portEntity.setWorktime(worktime);
        portEntity.setRun(run);
        portEntity.setType(type);
        portEntity.setTunnel(tunnel);
        portEntity.setTunneltype(tunneltype);
        String msg = "";
       // if(checkPort(port)) {
        try {
            msg = add(portEntity);
            PortService portService = new PortService();
            if("tcp".equals(portEntity.getType())){
//                if("0".equals(portEntity.getRunstate())) {
                    portService.tcpProcess(portEntity);
//                }
            }
            else if("udp".equals(portEntity.getType())){
//                if("0".equals(portEntity.getRunstate())){
                    portService.udpProcess(portEntity);
//                }
            }

            updateRunStateEntity(portEntity);
            // updateQuartzJob(data[0].split("&")[3]);
            // msg = "端口保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "端口保存失败";
        }
        /*else{
            msg = "端口保存失败,端口重复";
        }*/
       /* String msg = "";
        try {
            boolean flag = save(data);
            // updateQuartzJob(data[0].split("&")[3]);
            msg = "client端保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "client端保存失败";
        }*/
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String update() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String id = ServletRequestUtils.getStringParameter(request,"id");
        String port = ServletRequestUtils.getStringParameter(request,"port");
        String worktime = ServletRequestUtils.getStringParameter(request,"worktime");
        String run = ServletRequestUtils.getStringParameter(request,"run");
        String type = ServletRequestUtils.getStringParameter(request,"type");
        String tunnel = ServletRequestUtils.getStringParameter(request,"tunnel");
        String tunneltype = ServletRequestUtils.getStringParameter(request,"tunneltype");


        String oldid = ServletRequestUtils.getStringParameter(request,"oldid");
        String oldport = ServletRequestUtils.getStringParameter(request,"oldport");
        String oldworktime = ServletRequestUtils.getStringParameter(request,"oldworktime");
        String oldrun = ServletRequestUtils.getStringParameter(request,"oldrun");
        String oldtype = ServletRequestUtils.getStringParameter(request,"oldtype");
        String oldtunnel = ServletRequestUtils.getStringParameter(request,"oldtunnel");
        String oldtunneltype = ServletRequestUtils.getStringParameter(request,"oldtunneltype");
        String oldrunstate = ServletRequestUtils.getStringParameter(request,"oldrunstate");
        PortEntity oldPortEntity = new PortEntity();
        oldPortEntity.setId(oldid);
        oldPortEntity.setPort(oldport);
        oldPortEntity.setWorktime(oldworktime);
        oldPortEntity.setRun(oldrun);
        oldPortEntity.setType(oldtype);
        oldPortEntity.setTunnel(oldtunnel);
        oldPortEntity.setTunneltype(oldtunneltype);
        oldPortEntity.setRunstate(oldrunstate);

        PortService oldportService = new PortService();
        if("tcp".equals(oldPortEntity.getType())){
//            if("1".equals(oldPortEntity.getRunstate())) {
                oldportService.clearTcpProcess(oldPortEntity);
//            }
        }
        else if("udp".equals(oldPortEntity.getType())){
//            if("1".equals(oldPortEntity.getRunstate())){
                oldportService.clearUdpProcess(oldPortEntity);
//            }
        }

//        updateRunStateClearEntity(oldPortEntity);

        PortEntity portEntity = new PortEntity();
        portEntity.setId(id);
        portEntity.setPort(port);
        portEntity.setWorktime(worktime);
        portEntity.setRun(run);
        portEntity.setType(type);
        portEntity.setTunnel(tunnel);
        portEntity.setTunneltype(tunneltype);
        String msg = "";
        // if(checkPort(port)) {
        try {
            msg = add(portEntity);
            PortService portService = new PortService();
            if("tcp".equals(portEntity.getType())){
//                if("0".equals(portEntity.getRunstate())) {
                    portService.tcpProcess(portEntity);
//                }
            }
            else if("udp".equals(portEntity.getType())){
//                if("0".equals(portEntity.getRunstate())){
                    portService.udpProcess(portEntity);
//                }
            }

            updateRunStateEntity(portEntity);

            // updateQuartzJob(data[0].split("&")[3]);
            // msg = "端口保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "端口保存失败";
        }
        /*else{
            msg = "端口保存失败,端口重复";
        }*/
        /* String msg = "";
        try {
            boolean flag = save(data);
            // updateQuartzJob(data[0].split("&")[3]);
            msg = "client端保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "client端保存失败";
        }*/
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String delete() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String[] data = ServletRequestUtils.getStringParameters(request,"idArray");
        String msg = "";
        try {
            msg = stop(data);
            boolean flag = _delete(data);
            if(flag) {
                msg = "删除成功";
            }
           // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
          //  logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "删除失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String startPort() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String[] data = ServletRequestUtils.getStringParameters(request,"idArray");
        String msg = "";
        try {
            stop(data);
            msg = start(data);
            //msg = "启动成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
            //  logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "启动失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String clearPort() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String[] data = ServletRequestUtils.getStringParameters(request,"idArray");
        String msg = "";
        try {
//            msg = clear();
            msg = stop(data);
           // msg = "清除成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
            //  logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "清除失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    private String read(){
        //Basic.class.getResourceAsStream("/config_db.xml")
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.getPorts();
        }
        catch (Ex ex){
            result = ex.getMessage();
        }
        return result;
    }

    public boolean save(String[] data){
        for(int i = 0 ;i< data.length ; i++){
            String[] tcps = data[i].split("&");
            Tcp tcp = new Tcp();
            tcp.setTask(tcps[0]);
            tcp.setIp(tcps[1]);
            tcp.setPort(tcps[2]);
            String worktime = "";
            if(tcps[3].equals("")){
                worktime = "00:00-23:59";
            }
            else {
                worktime = tcps[3];
            }
            tcp.setStart(worktime.substring(0,worktime.indexOf("-")));
            tcp.setEnd(worktime.substring(worktime.indexOf("-")+1));
            if(tcps[4].equals("是")){
                tcp.setRun("true");
            }
            else{
                tcp.setRun("false");
            }
            tcp.setDescribe(tcps[5]);
            if(tcps[7].equals("通道一")){
                tcp.setTunnel("1");
            }
            else{
                tcp.setTunnel("2");
            }
            if(tcps[6].equals("id=undefined")){
                add(tcp);
            }
            else {
                tcp.setId(tcps[6].substring(tcps[6].indexOf("=")+1));
                update(tcp);
            }
        }
        return true;
    }

    /**
     * 删除数据
     * @param data
     * @return
     */
    public boolean _delete(String[] data){
        for(int i = 0 ;i< data.length ; i++){
            PortEntity portEntity = new PortEntity();
            portEntity.setId(data[i]);
            _delete(portEntity);
        }
        return true;
    }


    private String add(Tcp tcp){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.addTcpClient(tcp);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }

        return result;
    }

    private boolean checkPort(String port){
        Configuration configuration = null;
        try {
            configuration = new Configuration(path);
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return configuration.checkPort(port);
    }

    private String add(PortEntity portEntity){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.addPort(portEntity);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }

        return result;
    }


    private String update(Tcp tcp){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.updateTcpClient(tcp);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }
        return result;
    }

    /**
     * 更新工作时间
     * @param worktime
     */
    private void updateQuartzJob(String worktime){
        Configuration configuration = null;
        try {
            configuration = new Configuration(quartz_path);
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        configuration.updateQuartzJob(worktime);
    }

    /**
     *
     * @param portEntity
     * @return
     */
    private String _delete(PortEntity portEntity){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.deletePort(portEntity);
            PortService service = new PortService();
            if("tcp".equals(portEntity.getType())){
//                if("1".equals(portEntity.getRunstate())) {
                    service.clearTcpProcess(portEntity);
//                }
            }
            else if("udp".equals(portEntity.getType())){
//                if("1".equals(portEntity.getRunstate())){
                    service.clearUdpProcess(portEntity);
//                }
            }

        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "删除失败";
        }
        return result;
    }

    /**
     * 开启服务
     * @param data
     * @return
     */
    private String start(String[] data){
        String result = "";
        for(int i = 0 ;i< data.length ; i++){
            try{
                Configuration configuration = new Configuration(path);
                PortEntity portEntity = configuration.getPortEntity(data[i]);
                PortService portService = new PortService();
                if("tcp".equals(portEntity.getType())){
                    if("0".equals(portEntity.getRunstate())) {
                        portService.tcpProcess(portEntity);
                    }
                }
                else if("udp".equals(portEntity.getType())){
                    if("0".equals(portEntity.getRunstate())){
                        portService.udpProcess(portEntity);
                    }
                }
            }
            catch (Ex ex){
                logger.info(ex.getMessage());
                result = "启用失败";
            }
        }

        result = updateRunState(data);
        return result;
    }


    /**
     * 停止规则
     * @param data
     * @return
     */
    private String stop(String[] data){
        String result = "";
        for(int i = 0 ;i< data.length ; i++){
            try{
                Configuration configuration = new Configuration(path);

                PortEntity portEntity = configuration.getPortEntity(data[i]);
                PortService portService = new PortService();
                if("tcp".equals(portEntity.getType())){
//                    if("1".equals(portEntity.getRunstate())) {
                        portService.clearTcpProcess(portEntity);
//                    }
                }
                else if("udp".equals(portEntity.getType())){
//                    if("1".equals(portEntity.getRunstate())){
                        portService.clearUdpProcess(portEntity);
//                    }
                }
            }
            catch (Ex ex){
                logger.info(ex.getMessage());
                result = "清除失败";
            }
        }

        result = updateRunStateClear(data);
        return result;
    }

    /**
     * 清除所有规则
     * @return
     */
    private String clear(){
        InitProcess initProcess = new InitProcess();
        initProcess.initClear();
        initProcess.start();
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.clear();
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "清除失败";
        }
        return result;
    }

    /**
     * 更新状态为正在远行
     * @param data
     * @return
     */
    private String updateRunState(String[] data){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.updateState(data);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "操作失败";
        }
        return result;
    }

    /**
     * 更新状态为未运行
     * @param data
     * @return
     */
    private String updateRunStateClear(String[] data){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.updateStateClear(data);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "操作失败";
        }
        return result;
    }



    /**
     * 更新状态为未运行
     * @param entity
     * @return
     */
    private String updateRunStateClearEntity(PortEntity entity){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.updateStateClearPortEntity(entity);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "操作失败";
        }
        return result;
    }


    /**
     * 更新状态为未运行
     * @param entity
     * @return
     */
    private String updateRunStateEntity(PortEntity entity){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.updateStatePortEntity(entity);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "操作失败";
        }
        return result;
    }





    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public MonitorServiceImpl getMonitorService() {
        return monitorService;
    }

    public void setMonitorService(MonitorServiceImpl monitorService) {
        this.monitorService = monitorService;
    }
}
