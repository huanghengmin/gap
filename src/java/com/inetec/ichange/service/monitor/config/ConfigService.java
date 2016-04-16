package com.inetec.ichange.service.monitor.config;

import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.exception.Ex;
import com.inetec.common.io.IOUtils;
import com.inetec.ichange.service.monitor.audit.source.AuditSourceBusinessServiceImpl;
import com.inetec.ichange.service.monitor.audit.source.AuditSourceService;
import com.inetec.ichange.service.monitor.config.source.ConfigSourceService;
import com.inetec.ichange.service.monitor.config.source.ConfigSourceServiceImpl;
import com.inetec.ichange.service.monitor.utils.Configuration;
import com.inetec.ichange.service.monitor.utils.FileUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 */
public class ConfigService extends Thread {
    private static final Logger logger = Logger.getLogger(ConfigService.class);
    private static final String ExternalXml = System.getProperty("ichange.home")+"/repository/external/config.xml";
    private String ip;
    private int port;
    private boolean isRun = false;
    public BlockingQueue<String> query;

    public void init(){
        try {
            Configuration config = new Configuration(ExternalXml);
            List<Channel> list = config.getChannels();
            for (Channel channel : list) {
                if(channel.getChannelValue().equals("1")){
                    ip = channel.gettIp();
                    port = Integer.parseInt(channel.gettPort());
                }
            }
            query = new LinkedBlockingQueue<String>(1000);
        } catch (Ex ex) {
            logger.error("加载配置文件传输通道出错",ex);
        }
    }

    public String pollQuery() {
        try {
            return query.take();
        } catch (InterruptedException e) {

        }
        return null;
    }

    public void run(){
        isRun = true;
        while (isRun){
            String commad = pollQuery();
            send();
        }
    }

    public void close(){
        isRun = false;
    }

    public boolean isRunning() {
        return isRun;
    }

    public void send(){
        ConfigSourceService configSourceService = new ConfigSourceServiceImpl();
        configSourceService.init(ExternalXml, ip, port);
        configSourceService.send();
    }
    public static void main(String[] args){
        ConfigService auditService = new ConfigService();
        auditService.init();
        auditService.start();
    }
}
