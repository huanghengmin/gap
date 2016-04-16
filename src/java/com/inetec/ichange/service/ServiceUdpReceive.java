package com.inetec.ichange.service;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.monitor.ReceiveService;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.udp.UdpServer;
import com.inetec.ichange.service.monitor.utils.ConfigUtils;
import com.inetec.ichange.service.monitor.utils.ServiceMonitorFactory;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-31
 * Time: 下午2:24
 * To change this template use File | Settings | File Templates.
 */
public class ServiceUdpReceive extends Thread {
    private static final Logger logger = Logger.getLogger(ServiceUdpReceive.class);
    private static ExecutorService pool;
    private List<Integer> ports = new ArrayList<Integer>();
    private boolean run = false;

    public void init(List<Integer> ports){
        this.ports = ports;
        pool = Executors.newFixedThreadPool(3);
        for (int port : ports){
            Runnable command =  new ListenerRun(port);
            pool.execute(command);
        }
    }

    public void run(){
        run = true;
        while (run){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean running(){
        return run;
    }

    public void start(){
        this.run = true;
    }

    public void close(){
        this.run = false;
        pool.shutdown();
        pool.isTerminated();
    }

    public void restart() {
        if(Service.isRunServiceUdpReceive) {
            Service.serviceUdpReceive.close();
        }
        List<Integer> list = ConfigUtils.getPorts();
        Service.serviceUdpReceive.init(list);
        Service.serviceUdpReceive.start();
        Service.serviceUdpReceive.run();
        Service.isRunServiceUdpReceive = true;
    }

    private static class ListenerRun implements Runnable {
        private static Map<Integer,ReceiveService> services = new HashMap<Integer, ReceiveService>();
        private static Map<Integer,Boolean> isRunningReceiveServices = new HashMap<Integer, Boolean>();
        private int port;
        public ListenerRun(int port) {
            this.port = port;
            runReceiveService();
        }

        @Override
        public void run() {
            /*DatagramSocket ds = null;//开始监视端口
            try {
                ds = new DatagramSocket(port);
                logger.info("启动端口"+port+"的监听...");
                for (;;){
                    byte[] buf = new byte[ServiceUtil.UDPPACKETSIZE];
                    DatagramPacket dp = new DatagramPacket(buf, buf.length);//创建接收数据报的实例
                    ds.receive(dp);//阻塞,直到收到数据报后将数据装入IP中
                    ListenerRun.services.get(port).addList(buf);
                }
            } catch (SocketException e) {
                logger.error("监听"+port+"失败",e);
            } catch (IOException e) {
                logger.error("监听"+port+"失败:"+e.getMessage());
            }*/
            try {
                new UdpServer(port,ListenerRun.services.get(port));
            } catch (IOException e) {
                logger.error("接收端"+port+"处理数据出错"+e.getMessage());
            }

        }

        private void runReceiveService() {
            if(ListenerRun.isRunningReceiveServices.get(port) != null &&
                    ListenerRun.isRunningReceiveServices.get(port)) {
                return;
            }
            ReceiveService service = new ReceiveService();
            service.init();
            service.start();
            ListenerRun.services.put(port,service);
            ListenerRun.isRunningReceiveServices.put(port,true);
        }
    }

}
