package com.inetec.ichange.service.monitor.audit.source;

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
 * Date: 12-8-15
 * Time: 下午3:23
 * 审计文件传输服务
 */
public class AuditSourceBusinessServiceImpl implements AuditSourceService {
    private static final Logger logger = Logger.getLogger(AuditSourceService.class);

    private String fileName;
    private String ip;
    private int port;
    @Override
    public void init(String fileName, String ip, int port) {
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void send() {
        try {
            InetSocketAddress target = new InetSocketAddress(ip,port);
            File file = new File(fileName);
            InputStream in = new FileInputStream(file);
            byte[] buf = new byte[ServiceUtil.UDPPACKETSIZE -200];
            int len = 0;
            long index = 0;
            long time = System.currentTimeMillis();
            List<byte[]> list = new ArrayList<byte[]>();
            while ((len = in.read(buf))>-1) {
                if(len < ServiceUtil.UDPPACKETSIZE-200){
                    buf = IOUtils.copyArray(buf, len);
                }
                UdpHeader udpHeader = new UdpHeader();
                udpHeader.setCommand("auditbusiness");
                udpHeader.setType("file");
                udpHeader.setTime(time);
                udpHeader.setFileName(file.getName());
                udpHeader.setFileSize(file.length());
                udpHeader.setId(String.valueOf(index++));
                udpHeader.setLen(len);
                byte[] buff = UdpHeader.addHead(udpHeader, buf);
                list.add(buff);
            }
            ServiceUtils.callUdpService(target, list);
            in.close();
            logger.info("发出业务审计日志文件:"+fileName);
            file.delete();
        } catch (SocketException e) {
            logger.error("创建连接错误",e);
        } catch (FileNotFoundException e) {
            logger.error(fileName+"文件找不到");
        } catch (IOException e) {
            logger.error("流操作错误",e);
        }
    }

    @Override
    public void close() {

    }

}
