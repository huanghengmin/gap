package com.hzih.gap.web.action.system;

import com.hzih.gap.core.LocalAddress;
import com.hzih.gap.core.PingProcess;
import com.hzih.gap.domain.Tunnel;
import com.hzih.gap.service.LogService;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.web.action.ActionBase;
import com.inetec.common.exception.Ex;
import com.inetec.common.util.OSInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hzih.gap.utils.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-10
 * Time: 下午2:10
 * To change this template use File | Settings | File Templates.
 */
public class TunnelConfig extends ActionSupport {
    private static final Logger logger = Logger.getLogger(TunnelConfig.class);

    private LogService logService;

    private static final String path = StringContext.systemPath +"/repository/app.xml";

    public String test() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String localip1 = ServletRequestUtils.getStringParameter(request,"localip1");
        String targetIp1 = ServletRequestUtils.getStringParameter(request,"targetIp1");
        String localip2 = ServletRequestUtils.getStringParameter(request,"localip2");
        String targetIp2 = ServletRequestUtils.getStringParameter(request,"targetIp2");
        String msg = "";
        try {
            if((localip1 !="" && localip1 !=null) && (targetIp1 !="" && targetIp1 !=null)){
                LocalAddress localAddress = new LocalAddress();
                if(!localAddress.getLocalIp().contains(localip1)){
                    msg = "本机ip不存在";
                }
                else{
                    PingProcess pingProcess = new PingProcess();

                    String command = " ping "+targetIp1+" -i 0.0001 -c 4";

                    msg = getResult(pingProcess.pingResult(command));


                }
            }
            if((localip2 !="" && localip2 !=null) && (targetIp2 !="" && targetIp2 !=null)){
                LocalAddress localAddress = new LocalAddress();
                if(!localAddress.getLocalIp().contains(localip2)){
                    msg = "本机ip不存在";
                }
                else{
                    PingProcess pingProcess = new PingProcess();

                    String command = " ping "+targetIp2+" -i 0.0001 -c 4";

                    msg = getResult(pingProcess.pingResult(command));


                }
            }

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

    public String save() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String localip1 = ServletRequestUtils.getStringParameter(request,"localip1");
        String targetIp1 = ServletRequestUtils.getStringParameter(request,"targetIp1");
        String localip2 = ServletRequestUtils.getStringParameter(request,"localip2");
        String targetIp2 = ServletRequestUtils.getStringParameter(request,"targetIp2");
        String msg = "";
        try {
            if((localip1 !="" || localip1 !=null) && (targetIp1 !="" || targetIp1 !=null)){
                LocalAddress localAddress = new LocalAddress();
                if(!localAddress.getLocalIp().contains(localip1)){
                    msg = "本机ip不存在";
                }
                else{
                    Tunnel tunnel = new Tunnel();
                    tunnel.setId("1");
                    tunnel.setName("通道一");
                    tunnel.setLocalIp(localip1);
                    tunnel.setTargetIp(targetIp1);
                    save(tunnel);
                }
            }
            if((localip2 !="" || localip2 !=null) && (targetIp2 !="" || targetIp2 !=null)){
                LocalAddress localAddress = new LocalAddress();
                if(!localAddress.getLocalIp().contains(localip2)){
                    msg = "本机ip不存在";
                }
                else{
                    Tunnel tunnel = new Tunnel();
                    tunnel.setId("2");
                    tunnel.setName("通道二");
                    tunnel.setLocalIp(localip2);
                    tunnel.setTargetIp(targetIp2);
                    save(tunnel);
                }
            }
            msg = "通道保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "通道保存失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String SaveType() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String type = ServletRequestUtils.getStringParameter(request,"type");
        String msg = "";
        try{

            saveType(type);
            msg = "通道保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "通道保存失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String SaveService() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
       /* String localip1 = ServletRequestUtils.getStringParameter(request,"gapip1");
        String targetIp1 = ServletRequestUtils.getStringParameter(request,"videoip1");
        String localip2 = ServletRequestUtils.getStringParameter(request,"gapip2");
        String targetIp2 = ServletRequestUtils.getStringParameter(request,"videoip2");*/
        String[] data = ServletRequestUtils.getStringParameters(request, "dataArray");
        String msg = "";
        try {
            for(int i = 0 ; i < data.length ; i++){
                if(data[i].contains("&")){
                    Tunnel tunnel = new Tunnel();
                    tunnel.setId(data[i].split("&")[3]);
                    tunnel.setLocalIp(data[i].split("&")[0]);
                    tunnel.setGapip(data[i].split("&")[1]);
                    tunnel.setVideoip(data[i].split("&")[2]);
                    save(tunnel);
                }
            }
            msg = "通道保存成功";
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "通道保存失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public String getTunnels() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = "";
        try {
            msg = getTunnel();
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "{success:true,total:"+0+",rows:[]}";
        }
        actionBase.actionEnd(response, msg, result);
        return null;
    }
    public String getServices() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String msg = "";
        try {
            msg = getService();
            // logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置tcp客户端", e);
            // logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "{success:true,total:"+0+",rows:[]}";
        }
        actionBase.actionEnd(response, msg, result);
        return null;
    }

    private String save(Tunnel tunnel){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.saveTunnel(tunnel);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }
        return result;
    }

    private String getTunnel(){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.getTunnel();
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }
        return result;
    }

    private String saveType(String type){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.saveType(type);
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }
        return result;
    }
    

    private String getService(){

        String result = "";
        try{
            Configuration configuration = new Configuration(path);
            result = configuration.getService();
        }
        catch (Ex ex){
            logger.info(ex.getMessage());
            result = "保存失败";
        }
        return result;
    }


    private String getResult(String pingStr) {
        String result = "";
        OSInfo osInfo = OSInfo.getOSInfo();
        if(osInfo.isLinux()){
            String[] pings = pingStr.split("\n");
            for (int i = 0; i < pings.length; i++) {
                if(i<pings.length - 1){
                    result += pings[i].trim()+"<br>";
                }else{
                    result += pings[i].trim();
                }
            }
        }else if(osInfo.isWin()){
            String[] pings = pingStr.split("\r\n");
            for (int i = 0; i < pings.length; i++) {
                if(i<pings.length - 1){
                    result += pings[i].trim()+"<br>";
                }else{
                    result += pings[i].trim();
                }
            }
        }
        if(( result.indexOf("ttl")>-1 && result.indexOf("time")>-1 )
                ||(result.indexOf("bytes")>-1 && result.indexOf("time")>-1
                && result.indexOf("TTL")>-1)){
            result += "<br><font color=\"green\">PING测试成功!</font>";
        } else {
            result += "<br><font color=\"red\">PING测试失败!</font>";
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
