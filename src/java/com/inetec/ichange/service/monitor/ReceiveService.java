package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.utils.ServiceMonitorFactory;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-29
 * Time: 下午4:59
 * 接收UDP发送的数据.组成一定大小的包
 */
public class ReceiveService extends Thread{
    private static final Logger logger = Logger.getLogger(ReceiveService.class);
    private boolean run = false;
    public BlockingQueue<IoBuffer> query;
    private byte[] tempByte;
    public static ConcurrentHashMap initMap = new ConcurrentHashMap();

    public void init(){
        query = new LinkedBlockingQueue<IoBuffer>(1000);
    }

    public byte[] pollQuery(){
        try {
            IoBuffer buffer = query.take();
            byte[] buff = buffer.array();
            return buff;
        } catch (InterruptedException e) {

        }
        return null;
    }

    public boolean isQueryEmpty(){
        return querySize()==0;
    }

    public int querySize() {
        return query.size();
    }

    public byte[] getTempByte() {
        return tempByte;
    }

    public void setTempByte(byte[] tempByte) {
        this.tempByte = tempByte;
    }

    public void process(){
        byte[] buf = pollQuery();
        if(buf!=null){
            byte[] heads = new byte[200];
            for(int i=0;i<200;i++){
                heads[i] = buf[i];
            }
            String str = new String(heads);
            String header = str.substring(0, str.indexOf("}", 0) + 1);
            int len = UdpHeader.getSize(header); //包总大小
            byte[] bodys = new byte[len];
            for(int i=0;i<len;i++){
                bodys[i] = buf[i+200];
            }
            DataAttributes result = new DataAttributes();
            result.setProperty("header",header);
            if(UdpHeader.getCommand(header).equalsIgnoreCase("devicemonitorrunreceive")){
                String body = new String(bodys);
                result.setProperty("body",body);
                commandProcess(header,result);
            } else if(UdpHeader.getCommand(header).equalsIgnoreCase("devicemonitorrunreceive")) {
                result.setResultData(bodys);
                commandProcess(header,result);
            } else if(UdpHeader.getCommand(header).equalsIgnoreCase("inittestreceive")) {
                String sendtime = new String(bodys);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String receivetime = sdf.format(new Date());
                String channel = UdpHeader.getType(header);
                if(initMap.containsKey("s"+channel)){
                    initMap.remove("s"+channel);
                }
                if(initMap.containsKey("r"+channel)){
                    initMap.remove("r"+channel);
                }
                initMap.put("s"+channel,sendtime);
                initMap.put("r"+channel,receivetime);
            } else if(UdpHeader.getCommand(header).equalsIgnoreCase("configreceive")) {
                logger.info("接收到目标端发送的配置文件信息");
                result.setResultData(bodys);
                commandProcess(header,result);//
            }
        }
    }

    private void commandProcess(String header,DataAttributes result) {
        String command = UdpHeader.getCommand(header);
        IServiceCommandProcess serviceCommonProcess = null;
        try {
            serviceCommonProcess = ServiceMonitorFactory.createServiceCommon(command);
            result = serviceCommonProcess.process("", result);
        } catch (Ex ex) {
            logger.error("udp 接收转发到指定线程处理",ex);
        } catch (IOException e) {
            logger.error("udp 接收转发到指定线程处理",e);
        }
        if(result.getStatus().equals(Status.S_Success)) {
            //TODO UDP接收成功
        }
    }

    public void run() {
        this.run = true;
        while (run){
//            setTempByte(pollQuery());
            process();
        }
    }

    public void close(){
        this.run = false;
    }

    public boolean isRunning(){
        return run;
    }

}
