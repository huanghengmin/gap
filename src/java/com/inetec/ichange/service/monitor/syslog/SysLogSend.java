package com.inetec.ichange.service.monitor.syslog;

import com.inetec.ichange.service.Service;
import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogIF;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-27
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class SysLogSend {
    private static final Logger logger = Logger.getLogger(SysLogSend.class);
    public static void sysLog(String log) {
        if(Service.sysLogSendService.isRunning()){
            if(log!=null){
                Service.sysLogSendService.sysLogSend(log, "UTF-8");
            } else {
                logger.warn("SysLog服务收到的是空数据");
            }
        }else {
            logger.info("日志发出服务没有启动:发出"+log+"失败");
        }
    }
/*    public static void main(String[] args){

        service.init();
        service.start();
        service.sysLogSend("121212","UTF-8");
        service.sysLogSend("121212","UTF-8");
        service.sysLogSend("121212","UTF-8");
//        SysLogSend.sysLog("olololololo");
//
//        SysLogSend.sysLog("121313");

    }
    public static SysLogSendService service = new SysLogSendService();*/
}
