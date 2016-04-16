package com.inetec.ichange.service.monitor.init;

import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.monitor.config.source.ConfigSourceService;
import com.inetec.ichange.service.monitor.config.source.ConfigSourceServiceImpl;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.utils.Configuration;
import com.inetec.ichange.service.monitor.utils.ServiceUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 */
public class InitService extends Thread {
    private static final Logger logger = Logger.getLogger(InitService.class);
    private static final String ExternalXml = System.getProperty("ichange.home")+"/repository/external/config.xml";
    private String ip;
    private int port;
    private boolean isRun = false;
    public BlockingQueue<String> query;
    private String type;

    public void init(String _channel){
        try {
            Configuration config = new Configuration(ExternalXml);
            List<Channel> list = config.getChannels();
            for (Channel channel : list) {
                if(channel.getChannelValue().equals(_channel)){
                    ip = channel.gettIp();
                    port = Integer.parseInt(channel.gettPort());
                }
            }
            query = new LinkedBlockingQueue<String>(1000);
        } catch (Ex ex) {
            logger.error("加载配置文件传输通道出错",ex);
        }
        this.type = _channel;
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
        try {
            InetSocketAddress target = new InetSocketAddress(ip,port);
            logger.info("发送 通道测试消息 到"+ip+"的端口"+port);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date());
            byte[] buf = new String(date).getBytes();
            long index = 0;
            long time = System.currentTimeMillis();
            List<byte[]> list = new ArrayList<byte[]>();

            int len = buf.length;
            UdpHeader udpHeader = new UdpHeader();
            udpHeader.setCommand("inittestreceive");
            udpHeader.setTime(time);
            udpHeader.setType(type);
            udpHeader.setFileName("date");
            udpHeader.setFileSize(buf.length);
            udpHeader.setId(String.valueOf(index++));
            udpHeader.setLen(len);

            byte[] buff = UdpHeader.addHead(udpHeader, buf);
            list.add(buff);
            ServiceUtils.callUdpService(target, list);

        } catch (SocketException e) {
            logger.error("UDP 发送 通道测试消息 错误",e);
        } catch (FileNotFoundException e) {
            logger.error("UDP 发送 通道测试消息 错误:找不到配置文件",e);
        } catch (IOException e) {
            logger.error("UDP 发送 通道测试消息 错误",e);
        }
    }
    public static void main(String[] args){
        InitService auditService = new InitService();
        auditService.init("1");
        auditService.start();
    }
}
