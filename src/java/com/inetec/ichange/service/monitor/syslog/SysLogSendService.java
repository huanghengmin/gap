package com.inetec.ichange.service.monitor.syslog;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.monitor.databean.IpPort;
import com.inetec.ichange.service.monitor.utils.Configuration;
import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogConfigIF;
import org.productivity.java.syslog4j.SyslogIF;
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig;
import org.productivity.java.syslog4j.server.SyslogServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-27
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class SysLogSendService extends Thread{
    private static final Logger logger = Logger.getLogger(SysLogSendService.class);
    private static final String xml = System.getProperty("ichange.home") + "/repository/external/config.xml";
    public static String log;
    private String charset;
    private boolean isRunning = false;

    public void init(){

    }

    public void sysLogSend(String log,String charset) {
        this.log = log;
        this.charset = charset;
        work();
    }

    public void run(){
        isRunning = true;
        while (isRunning){
//            if(!isWorking){
//                work();
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void work() {
        int index = 0;
        List<IpPort> sysLogs = null;
        try {
            Configuration config = new Configuration(xml);
            sysLogs = config.getSysLogClient();
        } catch (Ex ex) {
            logger.error("获取日志接收服务器地址端口出错",ex);
        }
        for(IpPort ipPort : sysLogs){
            SyslogConfigIF config = new UDPNetSyslogConfig();
            config.setHost(ipPort.getIp());
            config.setCharSet(charset);
            config.setPort(ipPort.getPort());
            String protocol = ipPort.toString()+"["+String.valueOf(index++)+"]";
            SyslogIF syLog = null;
            try{
                syLog = Syslog.getInstance(protocol);
            } catch (Exception e){
                syLog = null;
            }
            if(syLog==null){
                syLog = Syslog.createInstance(protocol,config);
            }
            syLog.info(log);
            syLog.flush();
            syLog.shutdown();
        }
        log = null;
    }


    public boolean isRunning(){
        return isRunning;
    }

    public void close(){
        isRunning = false;
    }

    public static String getLog() {
        return log;
    }

    public static void setLog(String log) {
        SysLogSendService.log = log;
    }


}
