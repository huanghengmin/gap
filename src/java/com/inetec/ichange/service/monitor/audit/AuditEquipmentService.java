package com.inetec.ichange.service.monitor.audit;

import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.monitor.audit.source.AuditSourceEquipmentServiceImpl;
import com.inetec.ichange.service.monitor.audit.source.AuditSourceService;
import com.inetec.ichange.service.monitor.utils.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 */
public class AuditEquipmentService extends Thread {
    private static final Logger logger = Logger.getLogger(AuditEquipmentService.class);
    private static final String ExternalAudit = System.getProperty("ichange.home")+"/audit/equipment/audit.log";
    private static final String ExternalXml = System.getProperty("ichange.home")+"/repository/external/config.xml";
    private static final String ExternalAuditForSendPath = System.getProperty("ichange.home")+"/audit/equipment/send";
    private static final int MB = 1024*1000;
    private String ip;
    private int port;
    private int count;
    private int size;
    private boolean isRun = false;

    public void init(){
        try {
            Configuration config = new Configuration(ExternalXml);
            List<Channel> list = config.getChannels();
            for (Channel channel : list) {
                if(channel.getChannelValue().equals("1")){
                    ip = channel.gettIp();
                    port = Integer.parseInt(channel.getAuditPort());
                    count = Integer.parseInt(channel.getCount());
                    size = Integer.parseInt(channel.getSize());
                }
            }
        } catch (Ex ex) {
            logger.error("加载设备日志审计通道出错",ex);
        }
    }

    public void run(){
        isRun = true;
        while (isRun){
            work();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void work() {
        checkTime(ExternalAudit);
        File file = new File(ExternalAuditForSendPath);
        File[] files = file.listFiles();
        if(files!=null&&files.length>0){
            String sendAudit = ExternalAuditForSendPath+"/"+files[0].getName();
            AuditSourceService auditSourceService = new AuditSourceEquipmentServiceImpl();
            auditSourceService.init(sendAudit,ip,port);
            auditSourceService.send();
        }
    }

    public void close() {
        isRun = false;
    }

    public boolean isRunning() {
        return isRun;
    }

    /**
     * 1. 一天 的时间结束 后的*秒  --- * 代表取值频率
     * 2.剪切审计文件到待发送目录
     * @param externalAudit
     */
    private void checkTime(String externalAudit) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if(hour==0&&min==0&&second<5){
            logger.info("业务审计日志发送时间到达");
            String newFileName = "audit_"+System.currentTimeMillis()+".log";
            File sourceFile = new File(ExternalAudit);
            try {
                FileUtils.copyFile(sourceFile, new File(ExternalAuditForSendPath + "/" + newFileName));
                sourceFile.delete();
            } catch (IOException e) {
                logger.error("I/O操作错误",e);
            }
            try {
                sourceFile.createNewFile();
            } catch (IOException e) {
                logger.error("创建设备审计信息文件错误", e);
            }
        }
    }

    public static void main(String[] args){
        AuditEquipmentService auditService = new AuditEquipmentService();
        auditService.init();
        auditService.start();
    }
}
