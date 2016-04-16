package com.hzih.gap.core;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-17
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */
public class Main implements Runnable{
    private static Logger logger = Logger.getLogger(Main.class);
    public static boolean isRun = false;
    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        isRun = true;

        Config config = new Config();


        config.setLocalip();
        /*LinkedList<TcpEntity> tcpEntities = config.getTcpEntitys();
        LinkedList<UdpEntity> udpEntities = config.getUdpEntitys();
        TcpService tcpService = new TcpService(tcpEntities);
        Thread tcp_thread = new Thread(tcpService);
        tcp_thread.start();
        UdpService udpService = new UdpService(udpEntities);
        Thread udp_thread = new Thread(udpService);
        udp_thread.start();*/

        IxgbeProcess ixgbeProcess = new IxgbeProcess();

        Thread ixgbe = new Thread(ixgbeProcess);

        ixgbe.start();

        LinkedList<PortEntity> portEntities = config.getPortEntitys();

        PortService portService = new PortService(portEntities);

        Thread port_thread = new Thread(portService);

        port_thread.start();

    }
    
    public static void main(String[] arg0){

        Config config = new Config();


        config.setLocalip();
        /*LinkedList<TcpEntity> tcpEntities = config.getTcpEntitys();
        LinkedList<UdpEntity> udpEntities = config.getUdpEntitys();
        TcpService tcpService = new TcpService(tcpEntities);
        Thread tcp_thread = new Thread(tcpService);
        tcp_thread.start();
        UdpService udpService = new UdpService(udpEntities);
        Thread udp_thread = new Thread(udpService);
        udp_thread.start();*/

        LinkedList<PortEntity> portEntities = config.getPortEntitys();

        PortService portService = new PortService(portEntities);

        Thread port_thread = new Thread(portService);

        port_thread.start();

    }
}
