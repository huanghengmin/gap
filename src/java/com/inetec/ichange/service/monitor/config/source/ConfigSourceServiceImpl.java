package com.inetec.ichange.service.monitor.config.source;

import com.inetec.common.io.IOUtils;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.utils.ServiceUtils;
import com.inetec.ichange.service.utils.ServiceUtil;
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
public class ConfigSourceServiceImpl implements ConfigSourceService {
     private static final Logger logger = Logger.getLogger(ConfigSourceServiceImpl.class);

    private String config;
    private String ip;
    private int port;
    @Override
    public void init(String config, String ip, int port) {
        this.config = config;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void send() {
        try {
            InetSocketAddress target = new InetSocketAddress(ip,port);
            logger.info("发送配置文件到"+ip+"的端口"+port);
            File file = new File(config);
            InputStream in = new FileInputStream(file);
            byte[] buf = new byte[ServiceUtil.UDPPACKETSIZE-200];
            int len = 0;
            long index = 0;
            long time = System.currentTimeMillis();
            List<byte[]> list = new ArrayList<byte[]>();
            while ((len = in.read(buf))>-1) {
                if(len < ServiceUtil.UDPPACKETSIZE-200){
                    buf = IOUtils.copyArray(buf, len);
                }
                UdpHeader udpHeader = new UdpHeader();
                udpHeader.setCommand("configreceive");
                udpHeader.setTime(time);
                udpHeader.setType("file");
                udpHeader.setFileName(file.getName());
                udpHeader.setFileSize(file.length());
                udpHeader.setId(String.valueOf(index++));
                udpHeader.setLen(len);
                byte[] buff = UdpHeader.addHead(udpHeader, buf);
                list.add(buff);
            }
            ServiceUtils.callUdpService(target, list);
            in.close();
        } catch (SocketException e) {
            logger.error("UDP 发送配置文件错误",e);
        } catch (FileNotFoundException e) {
            logger.error("UDP 发送配置文件错误:找不到配置文件",e);
        } catch (IOException e) {
            logger.error("UDP 发送配置文件错误",e);
        }
    }

    @Override
    public void close() {

    }
}
