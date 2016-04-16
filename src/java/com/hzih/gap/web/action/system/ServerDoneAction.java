package com.hzih.gap.web.action.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hzih.gap.web.action.ActionBase;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 服务管理-开、关
 * @author 钱晓盼
 *
 */
public class ServerDoneAction extends ActionSupport{

	private static final long serialVersionUID = -1267078981037327633L;
	private Logger log = Logger.getLogger(ServerDoneAction.class);
	private String json;
	private String server;
	private String type;
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJson() {
		return json;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	
	public String execute() throws Exception {
		log.info(type+"服务开始!");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		this.json = serverDone();
		log.info(type+"服务成功!");
		base.actionEnd(response, json, result);
		return "success";
	}

	private String serverDone() {
		String msg = "";
		if(type.equals("open")){
			msg = openServer();
		}else if(type.equals("close")){
			msg = closeServer();
		}
		return "{'success':true,'msg':'"+msg+"'}";
	}

	@SuppressWarnings("deprecation")
	private String openServer() {
		String result = "";
		OSInfo osInfo = OSInfo.getOSInfo();
		if (osInfo.isLinux()) {
            Proc proc = new Proc();
            proc.setDebug(true);
            proc.setChildText("service");
            String service = "service "+server+" start";
//            service ssh start webmin hartbeat
            proc.exec(service);
            String[] str = proc.getOutput().split("\n");
            if(str.length>1){
            	result = str[0];
            }else{
            	if(result==null || result.equals("")){
            		result = server+"服务开启成功";
            	}else{
            		result = proc.getOutput();
            	}
            }
            proc.stop();
        }else {
        	result = "操作系统系统不对!";
        }
        return result;
		
	}
	
	@SuppressWarnings("deprecation")
	private String closeServer(){
		String result = "";
		OSInfo osInfo = OSInfo.getOSInfo();
		if (osInfo.isLinux()) {
            Proc proc = new Proc();
            proc.setDebug(true);
            proc.setChildText("service");
            String service = "service "+server+" stop";
            proc.exec(service);
            String[] str = proc.getOutput().split("\n");   
            if(str.length>1){
            	result = str[0];
            }else{
            	if(result==null || result.equals("")){
            		result = server+"服务关闭成功";
            	}else{
            		result = proc.getOutput();
            	}
            }
            proc.stop();
        }else {
        	result = "操作系统系统不对!";
        }
        return result;
	}
}
