package com.inetec.ichange.service.monitor.device.source;

import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.utils.ServiceUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-20
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public class DeviceSourceServiceImpl implements DeviceSourceService {
     private static final Logger logger = Logger.getLogger(DeviceSourceServiceImpl.class);

    private String device;
    private String deviceRun;
    private String deviceAlert;
    private String ip;
    private int port;
    private String equName;
    private String equId;
    @Override
    public void init(String device, String deviceRun,String deviceAlert,String equName,String equId, String ip, int port) {
        this.device = device;
        this.deviceRun = deviceRun;
        this.deviceAlert = deviceAlert;
        this.ip = ip;
        this.port = port;
        this.equName = equName;
        this.equId = equId;
    }

    @Override
    public void send() {
        if(device!=null){
            byte[] buf = device.getBytes();
            UdpHeader udpHeader = new UdpHeader();
            udpHeader.setCommand("devicemonitorrunreceive");
            udpHeader.setStringName(equName);
            udpHeader.setStringId(equId);
            udpHeader.setType("device");
            udpHeader.setLen(buf.length);
            udpSend(udpHeader,buf);
        }

        if(deviceRun!=null){
            byte[] buf = deviceRun.getBytes();
            UdpHeader udpHeader = new UdpHeader();
            udpHeader.setCommand("devicemonitorrunreceive");
            udpHeader.setStringName(equName);
            udpHeader.setStringId(equId);
            udpHeader.setType("deviceRun");
            udpHeader.setLen(buf.length);
            udpSend(udpHeader,buf);
        }

        if(deviceAlert!=null){
            byte[] buf = deviceAlert.getBytes();
            UdpHeader udpHeader = new UdpHeader();
            udpHeader.setCommand("devicemonitorrunreceive");
            udpHeader.setStringName(equName);
            udpHeader.setStringId(equId);
            udpHeader.setType("deviceAlert");
            udpHeader.setLen(buf.length);
            udpSend(udpHeader,buf);
        }

    }

    private void udpSend(UdpHeader udpHeader, byte[] buf) {
        try {
            InetSocketAddress target = new InetSocketAddress(ip,port);
            byte[] buff = UdpHeader.addHead(udpHeader, buf);
            List<byte[]> list = new ArrayList<byte[]>();
            list.add(buff);
            ServiceUtils.callUdpService(target, list);
        } catch (SocketException e) {
            logger.error("UDP连接建立出错", e);
        } catch (FileNotFoundException e) {
            logger.error("文件没有找到", e);
        } catch (IOException e) {
            logger.error("发送错误", e);
        }
    }

    @Override
    public void close() {

    }
}
