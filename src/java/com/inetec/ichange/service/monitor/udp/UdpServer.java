package com.inetec.ichange.service.monitor.udp;


import com.inetec.ichange.service.monitor.ReceiveService;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-12-9
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public class UdpServer {
    private static final Logger logger = Logger.getLogger(UdpServer.class);

    public UdpServer(int port, ReceiveService receiveService) throws IOException {

        NioDatagramAcceptor acceptor = new NioDatagramAcceptor();//创建一个UDP的接收器
        acceptor.setHandler(new UdpServerHandler(receiveService));//设置接收器的处理程序

        DatagramSessionConfig dcfg = acceptor.getSessionConfig();//建立连接的配置文件
        dcfg.setReuseAddress(true);//设置每一个非主监听连接的端口可以重用
        dcfg.setBroadcast(false);
        dcfg.setReceiveBufferSize(6553600);
        dcfg.setSendBufferSize(6553600);
        dcfg.setMaxReadBufferSize(6553600);
        dcfg.setMinReadBufferSize(65536);
        dcfg.setTrafficClass(0x04|0x08); //高性能和可靠性

        acceptor.bind(new InetSocketAddress(port));//绑定端口
        logger.info("启动端口"+port+"的UDP监听...");
    }
}
