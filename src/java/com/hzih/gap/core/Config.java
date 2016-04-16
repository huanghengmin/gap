package com.hzih.gap.core;

import com.hzih.gap.domain.Tunnel;
import com.hzih.gap.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-17
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class Config  {
    private static final String path = StringContext.systemPath +"/repository/app.xml";
    //private static final String path = "F:/stp/repository/app.xml";
    private static Logger logger = Logger.getLogger(Config.class);
    private Document document;
    TunnelProcess tunnelProcess;
    /*public static Map<String,List<TcpEntity>> tcp_start_map = new HashMap();
    public static Map<String,List<TcpEntity>> tcp_end_map = new HashMap();
    public static Map<String,List<UdpEntity>> udp_start_map = new HashMap();
    public static Map<String,List<UdpEntity>> udp_end_map = new HashMap();*/
    public static ArrayList<String> localip = new ArrayList<>();

    LinkedList<UdpEntity> udp_list = new LinkedList();

    LinkedList<TcpEntity> tcp_list = new LinkedList();

    LinkedList<PortEntity> port_list = new LinkedList();


    public Config(){
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        tunnelProcess = new TunnelProcess();
       // init();
    }

    public LinkedList<TcpEntity> getTcpClientEntitys(){
        Element root = document.getRootElement();
        Element tcp = root.element("tcp");
        List<Element> clients = tcp.elements("client");
        for(Element client : clients){
            TcpEntity tcpEntity = new TcpEntity();
            String id = client.attribute("id").getValue();
            String ctask = client.element("task").getText();
            String clientIp = client.element("ip").getText();
            String clientPort = client.element("port").getText();
            String start = client.element("start").getText();
            String end = client.element("end").getText();
            String run = client.element("run").getText();
            String tunnel = client.element("tunnel").getText();
            String describe = client.element("describe").getText();
            tcpEntity.setId(id);
            tcpEntity.setTask(ctask);
            Tunnel tunnel1 = tunnelProcess.getTunnel(tunnel);
            tcpEntity.setServerIp(tunnel1.getTargetIp());
            tcpEntity.setServerPort(clientPort);
            tcpEntity.setClientIp(clientIp);
            tcpEntity.setClientPort(clientPort);
            tcpEntity.setStart(start);
            tcpEntity.setEnd(end);
            if(run.endsWith("是") || run.equals("true")){
                tcpEntity.setRun(true);
            }
            else{
                tcpEntity.setRun(false);
            }
            tcpEntity.setDescribe(describe);
            tcp_list.add(tcpEntity);
        }
        return tcp_list;
    }
    public LinkedList<TcpEntity> getTcpServerEntitys(){
        Element root = document.getRootElement();
        Element tcp = root.element("tcp");
        List<Element> servers = tcp.elements("server");
        for(Element server : servers){
            TcpEntity tcpEntity = new TcpEntity();
            String id = server.attribute("id").getValue();
            String stask = server.element("task").getText();
            String serverIp = server.element("ip").getText();
            String serverPort = server.element("port").getText();
            String tunnel = server.element("tunnel").getText();
            tcpEntity.setId(id);
            tcpEntity.setTask(stask);
            Tunnel tunnel1 = tunnelProcess.getTunnel(tunnel);
            tcpEntity.setServerIp(serverIp);
            tcpEntity.setServerPort(serverPort);
            tcpEntity.setClientIp(tunnel1.getLocalIp());
            tcpEntity.setClientPort(serverPort);
            tcpEntity.setStart("00:00");
            tcpEntity.setEnd("23:59");
            tcpEntity.setRun(true);
            tcp_list.add(tcpEntity);
        }
        return tcp_list;
    }


    public LinkedList<UdpEntity> getUdpClientEntitys(){
        Element root = document.getRootElement();
        Element tcp = root.element("udp");
        List<Element> clients = tcp.elements("client");
        for(Element client : clients){
            UdpEntity udpEntity = new UdpEntity();
            String id = client.attribute("id").getValue();
            String ctask = client.element("task").getText();
            String clientIp = client.element("ip").getText();
            String clientPort = client.element("port").getText();
            String start = client.element("start").getText();
            String end = client.element("end").getText();
            String run = client.element("run").getText();
            String tunnel = client.element("tunnel").getText();
            String describe = client.element("describe").getText();
            udpEntity.setId(id);
            udpEntity.setTask(ctask);
            Tunnel tunnel1 = tunnelProcess.getTunnel(tunnel);
            udpEntity.setServerIp(tunnel1.getTargetIp());
            udpEntity.setServerPort(clientPort);
            udpEntity.setClientIp(clientIp);
            udpEntity.setClientPort(clientPort);
            udpEntity.setStart(start);
            udpEntity.setEnd(end);
            if(run.endsWith("是") || run.equals("true")){
                udpEntity.setRun(true);
            }
            else{
                udpEntity.setRun(false);
            }
            udpEntity.setDescribe(describe);
            udp_list.add(udpEntity);
        }
        return udp_list;
    }

    public LinkedList<PortEntity> getPortEntitys(){
        Element root = document.getRootElement();
        Element ports = root.element("ports");
        List<Element> port = ports.elements("port");
        for(Element p : port){
            PortEntity portEntity = new PortEntity();
            String id = p.attribute("id").getValue();
            String _port = p.element("port").getText();
            String worktime = p.element("worktime").getText();
            String run = p.element("run").getText();
            String type = p.element("type").getText();
            String tunneltype = p.element("tunneltype").getText();
            String tunnel = p.element("tunnel").getText();
            portEntity.setId(id);
            portEntity.setTunnel(tunnel);
            portEntity.setTunneltype(tunneltype);
            portEntity.setWorktime(worktime);
            portEntity.setPort(_port);
            portEntity.setRun(run);
            portEntity.setType(type);
            port_list.add(portEntity);
        }
        return port_list;
    }

    public LinkedList<UdpEntity> getUdpServerEntitys(){
        Element root = document.getRootElement();
        Element tcp = root.element("udp");
        List<Element> servers = tcp.elements("server");
        for(Element server : servers){
            UdpEntity tcpEntity = new UdpEntity();
            String id = server.attribute("id").getValue();
            String stask = server.element("task").getText();
            String serverIp = server.element("ip").getText();
            String serverPort = server.element("port").getText();
            String tunnel = server.element("tunnel").getText();
            tcpEntity.setId(id);
            tcpEntity.setTask(stask);
            Tunnel tunnel1 = tunnelProcess.getTunnel(tunnel);
            tcpEntity.setServerIp(serverIp);
            tcpEntity.setServerPort(serverPort);
            tcpEntity.setClientIp(tunnel1.getLocalIp());
            tcpEntity.setClientPort(serverPort);
            tcpEntity.setStart("00:00");
            tcpEntity.setEnd("23:59");
            tcpEntity.setRun(true);
            udp_list.add(tcpEntity);
        }
        return udp_list;
    }

    public void setLocalip(){
        LocalAddress localAddress = new LocalAddress();
        localip = localAddress.getLocalIp();
    }

    public static String getLocalip(String ip){
        String lip = "";
        String ip_header = ip.substring(0,ip.lastIndexOf(".")+1);
        for(int i = 0 ; i < localip.size() ; i++){
            String _ip = localip.get(i);
            if(_ip.startsWith(ip_header)){
                lip = _ip;
            }
        }
        return lip;
    }

    public void init(){
        getTcpClientEntitys();
        getTcpServerEntitys();
        getUdpClientEntitys();
        getUdpServerEntitys();
    }

    public LinkedList<TcpEntity> getTcpEntitys(){
        return tcp_list;
    }
    public LinkedList<UdpEntity> getUdpEntitys(){
        return udp_list;
    }

    public static void main(String[] arg0){
        try {
            ServerSocket serverSocket = new ServerSocket(8200);
            Socket socket = serverSocket.accept();
            byte[] data = new byte[1024];
            InputStream outputStream = socket.getInputStream();
            int len = outputStream.read(data);
            System.out.println(new String(data,0,len));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
