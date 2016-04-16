package com.hzih.gap.web.action.net;

import com.hzih.gap.domain.Ipmac;
import com.hzih.gap.service.IpmacService;
import com.hzih.gap.service.LogService;
import com.hzih.gap.web.SessionUtils;
import com.hzih.gap.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-6
 * Time: 下午5:48
 * To change this template use File | Settings | File Templates.
 */
public class IpmacBondAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(IpmacBondAction.class);
    private LogService logService;
    private IpmacService ipmacService;

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
    public String bond() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String[] data = ServletRequestUtils.getStringParameters(request, "ipArray");
        String msg = "";
        try {
            boolean flag = save(data);
            msg = "ip与mac绑定成功";
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "ip与mac绑定失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }
    public String getIpmac() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int start = Integer.parseInt(request.getParameter("start"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        String json = "";
        try {
            json = ipmacService.getBondIpmac(start,limit);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }
    public String delete() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String[] data = ServletRequestUtils.getStringParameters(request, "idArray");
        String msg = "";
        try {
            boolean flag = delete(data);
            msg = "ip与mac绑定成功";
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址成功 ");
        } catch (Exception e) {
            logger.error("配置管理", e);
            logService.newLog("ERROR", SessionUtils.getAccount(request).getUserName(), "配置管理","用户获取管理服务、集控采集数据接口设定IP地址失败 ");
            msg = "ip与mac绑定失败";
        }
        String json  = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response, json, result);
        return null;
    }


    public IpmacService getIpmacService() {
        return ipmacService;
    }

    public void setIpmacService(IpmacService ipmacService) {
        this.ipmacService = ipmacService;
    }

    public boolean save(String[] data){
        for(int i = 0 ;i< data.length ; i++){
            String[] ipmacs = data[i].split("&");
            Ipmac ipmac = new Ipmac();
            ipmac.setIp(ipmacs[0]);
            ipmac.setMac(ipmacs[1]);
            if(ipmacs[2].equals("是")){
                ipmac.setProbe(0);
            }
            else if (ipmacs[2].equals("否")){
                ipmac.setProbe(1);
            }
            else if(ipmacs[2].equals("0")){
                ipmac.setProbe(0);
            }
            else if(ipmacs[2].equals("1")){
                ipmac.setProbe(1);
            }
            if(ipmacs[3].equals("id=undefined")){
                ipmacService.bond(ipmac);
            }
            else {
                ipmac.setId(Integer.parseInt(ipmacs[3].substring(ipmacs[3].lastIndexOf("=")+1)));
                ipmacService.update(ipmac);
            }
        }
        return true;
    }
    public boolean delete(String[] data){
        for(int i = 0 ;i< data.length ; i++){
            String id = data[i];
            Ipmac ipmac = new Ipmac();
            ipmac.setId(Integer.parseInt(id));
            ipmacService.delete(ipmac);
        }
        return true;
    }
}
