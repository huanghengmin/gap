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
 * 服务管理
 * @author 钱晓盼
 *
 */
public class ServerStatAction extends ActionSupport{

	private static final long serialVersionUID = -1267078981037327633L;
	private Logger log = Logger.getLogger(ServerStatAction.class);
	private String json;
	
	public String getJson() {
		return json;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	
	public String execute() throws Exception {
		log.info("读取服务状态开始!");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionBase base = new ActionBase();
		String result =	base.actionBegin(request);
		this.json = readServerStat();
		
		log.info("读取服务状态成功!");
		base.actionEnd(response, json, result);
		return "success";
	}

	@SuppressWarnings("deprecation")
	private String readServerStat() {
		String result = "";
		String[] flag = new String[3];
		OSInfo osInfo = OSInfo.getOSInfo();
		if (osInfo.isLinux()) {
            String[] servers = {"ssh","webmin","heartbeat"};
            String service = null;
            for (int i = 0; i < servers.length; i++) {
            	Proc proc = new Proc();
            	proc.setDebug(true);
            	proc.setChildText("service");
            	service = "service "+servers[i]+" status";            
            	proc.exec(service);            
            	result = proc.getOutput();
            	log.info(result);
            	proc.stop();
            	log.info(result);
            	if(result.indexOf("running")>0){
            		flag[i] = "open";//close
            	}else{
            		flag[i] = "close";
            	}
            }            	
        }else{
        	for (int i = 0; i < flag.length; i++) {
        		flag[i] = "操作系统不对,"+System.getProperty("os.name")+" "+System.getProperty("os.version"); 
			}
        }

		return "{'success':true,'total':3,rows:[" +
				"{'server':'ssh服务','flag':'"+flag[0]+"'}," +
				"{'server':'webmin服务','flag':'"+flag[1]+"'}," +
				"{'server':'heartbeat服务','flag':'"+flag[2]+"'},]}";
	}

}
