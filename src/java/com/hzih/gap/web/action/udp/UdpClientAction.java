package com.hzih.gap.web.action.udp;

import com.hzih.gap.domain.Tcp;
import com.hzih.gap.domain.Udp;
import com.hzih.gap.service.LogService;
import com.hzih.gap.utils.Configuration;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.web.SessionUtils;
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
public class UdpClientAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(UdpClientAction.class);
    private LogService logService;
    private static final String path = StringContext.systemPath +"/repository/app.xml";
    private static final String quartz_path = StringContext.systemPath +"/repository/quartz_job.xml";
    public String getTask() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = "";
        try {
            json = read();
            //logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("获取udp客户端数据出错", e);
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
            //logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("保存udp客户端数据出错", e);
           // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "client端保存失败";
        }
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
            boolean flag = delete(data);
            msg = "删除成功";
           // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("删除udp客户端数据出错", e);
            //logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "删除失败";
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
            result = configuration.getClientTask("udp");
        }
        catch (Ex ex){
            result = ex.getMessage();
        }
        return result;
    }
    public boolean save(String[] data){
        for(int i = 0 ;i< data.length ; i++){
            String[] udps = data[i].split("&");
            Udp udp = new Udp();
            udp.setTask(udps[0]);
            udp.setIp(udps[1]);
            udp.setPort(udps[2]);
            String worktime = "";
            if(udps[3].equals("")){
                worktime = "00:00-23:59";
            }
            else {
                worktime = udps[3];
            }
            udp.setStart(worktime.substring(0,worktime.indexOf("-")));
            udp.setEnd(worktime.substring(worktime.indexOf("-")+1));
            if(udps[4].equals("是")){
                udp.setRun("true");
            }
            else{
                udp.setRun("false");
            }
            udp.setDescribe(udps[5]);
            if(udps[7].equals("通道一")){
                udp.setTunnel("1");
            }
            else{
                udp.setTunnel("2");
            }
            if(udps[6].equals("id=undefined")){
                add(udp);
            }
            else {
                udp.setId(udps[6].substring(udps[6].indexOf("=")+1));
                update(udp);
            }
        }
        return true;
    }
    public boolean delete(String[] data){
        for(int i = 0 ;i< data.length ; i++){
            Udp udp = new Udp();
            udp.setId(data[i]);
            delete(udp);
        }
        return true;
    }
    private String add(Udp udp){
        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.addUdpClient(udp);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }

        return result;
    }
    private String update(Udp udp){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.updateUdpClient(udp);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }
        return result;
    }
    private void updateQuartzJob(String worktime){

        Configuration configuration = null;
        try {
            configuration = new Configuration(quartz_path);
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        configuration.updateQuartzJob(worktime);
    }

    private String delete(Udp udp){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.deleteUdpClient(udp);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "删除失败";
        }
        return result;
    }
    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }


}
