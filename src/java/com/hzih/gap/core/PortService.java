package com.hzih.gap.core;

import com.hzih.gap.GapType;
import com.hzih.gap.domain.Tunnel;
import com.hzih.gap.utils.Configuration;
import com.hzih.gap.utils.StringContext;
import com.inetec.common.exception.Ex;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-29
 * Time: 下午1:46
 * To change this template use File | Settings | File Templates.
 */
public class PortService implements Runnable{
    private static final String path = StringContext.systemPath +"/repository/app.xml";
    private static Logger logger = Logger.getLogger(PortService.class);
    private LinkedList<PortEntity> portEntities;
    
    private boolean isRun;
//    public static Map<String,List<TcpProcess>> map = new HashMap();

    public PortService(LinkedList<PortEntity>  portEntities){
        this.portEntities = portEntities;
//        map.clear();
    }

    public PortService(){}

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

//    public Map<String,String> ipmap = new HashMap<>();
    @Override
    public void run() {
        isRun = true;
        //To change body of implemented methods use File | Settings | File Templates.
        InitProcess clear = new InitProcess();
        clear.initClear();
        clear.start();
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(portEntities.size()>0){
            logger.warn("portEntities size is "+portEntities.size());
            for(int i = 0; i < portEntities.size() ; i++){
                PortEntity portEntity = portEntities.get(i);
                if(portEntity.getType().equals("tcp")){
                    tcpProcess(portEntity);
                    try {
                        Configuration configuration = new Configuration(path);
                        configuration.updateState(new String[]{portEntity.getId()});
                    } catch (Ex ex) {
                        ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                else{
                    udpProcess(portEntity);
                    try {
                        Configuration configuration = new Configuration(path);
                        configuration.updateState(new String[]{portEntity.getId()});
                    } catch (Ex ex) {
                        ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
    }

    /**
     * 更新工作时间
     * @param worktime
     *//*
    private void updateQuartzJob(String worktime){
        Configuration configuration = null;
        try {
            configuration = new Configuration(path);
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        configuration.updateQuartzJob(worktime);
    }*/

    /**
     * 开启tcp入口
     * @param portEntity
     */
    public void tcpProcess(PortEntity portEntity){
        /**
         * 是前置还是后置网匝
         */
        boolean gap_back = GapType.checkGaptypeIsBack();
        if(gap_back){
            gapBackTcp(portEntity);
        }
        else {
            gapPreTcp(portEntity);
        }
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * 清除tcp入口
     * @param portEntity
     */
    public void clearTcpProcess(PortEntity portEntity){
        /**
         * 是前置还是后置网匝
         */
        boolean gap_back = GapType.checkGaptypeIsBack();
        if(gap_back){
            clearGapBackTcp(portEntity);
        }
        else {
            clearGapPreTcp(portEntity);
        }
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * 开户udp入口
     * @param portEntity
     */
    public void udpProcess(PortEntity portEntity){
        /**
         * 判断是前置还是后置网匝
         */
        boolean gap_back = GapType.checkGaptypeIsBack();
        if(gap_back){
            //后置
            gapBackUdp(portEntity);
        }
        else {
            //前置
            gapPreUdp(portEntity);
        }
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * 清除udp入口
     * @param portEntity
     */
    public void clearUdpProcess(PortEntity portEntity){
        /**
         * 判断是前置还是后置网匝
         */
        boolean gap_back = GapType.checkGaptypeIsBack();
        if(gap_back){
            //后置
            clearGapBackUdp(portEntity);
        }
        else {
            //前置
            clearGapPreUdp(portEntity);
        }
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * 开启tcp
     * @param portEntity
     */
    public void gapBackTcp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //后置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initTcpPort(clientip, serverip, port);

        }
        else if (portEntity.getTunneltype().equals("2")){
            //后置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            initTcpPort(clientip, serverip, port);
        }
        else {
             //后置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initTcpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            initTcpPort(clientip2, serverip2, port);
        }

    }

    /**
     *
     * @param portEntity
     */
    public void clearGapBackTcp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //后置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearTcpPort(clientip, serverip, port);

        }
        else if (portEntity.getTunneltype().equals("2")){
            //后置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            clearTcpPort(clientip, serverip, port);
        }
        else {
            //后置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearTcpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            clearTcpPort(clientip2, serverip2, port);
        }

    }

    /**
     * 开启tcp
     * @param portEntity
     */
    public void gapPreTcp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //前置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            initTcpPort(clientip, serverip, port);
        }
        else if (portEntity.getTunneltype().equals("2")){
            //前置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initTcpPort(clientip, serverip, port);
        }
        else {
            //前置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initTcpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            initTcpPort(clientip2, serverip2, port);
        }
    }

    /**
     *
     * @param portEntity
     */
    public void clearGapPreTcp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //前置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            clearTcpPort(clientip, serverip, port);
        }
        else if (portEntity.getTunneltype().equals("2")){
            //前置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearTcpPort(clientip, serverip, port);
        }
        else {
            //前置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearTcpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            clearTcpPort(clientip2, serverip2, port);
        }
    }

    /**
     *  开启udp
     * @param portEntity
     */
    public void gapBackUdp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //后置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initUdpPort(clientip, serverip, port);
        }
        else if (portEntity.getTunneltype().equals("2")){
            //后置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            initUdpPort(clientip, serverip, port);
        }
        else {
            //后置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initUdpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            initUdpPort(clientip2, serverip2, port);
        }
    }

    /**
     *
     * @param portEntity
     */
    public void clearGapBackUdp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //后置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearUdpPort(clientip, serverip, port);
        }
        else if (portEntity.getTunneltype().equals("2")){
            //后置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            clearUdpPort(clientip, serverip, port);
        }
        else {
            //后置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearUdpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            clearUdpPort(clientip2, serverip2, port);
        }
    }

    /**
     * 开启udp
     * @param portEntity
     */
    public void gapPreUdp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //前置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            initUdpPort(clientip, serverip, port);
        }
        else if (portEntity.getTunneltype().equals("2")){
            //前置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initUdpPort(clientip, serverip, port);
        }
        else {
            //前置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            initUdpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            initUdpPort(clientip2, serverip2, port);
        }
    }

    /**
     *
     * @param portEntity
     */
    public void clearGapPreUdp(PortEntity portEntity){
        if(portEntity.getTunneltype().equals("1")){
            //前置机 前置后置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getGapip();
            String serverip = tunnel.getTargetIp();
            String port = portEntity.getPort();
            clearUdpPort(clientip, serverip, port);
        }
        else if (portEntity.getTunneltype().equals("2")){
            //前置机 后置前置通道
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearUdpPort(clientip, serverip, port);
        }
        else {
            //前置机 双向
            Tunnel tunnel =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip = tunnel.getLocalIp();
            String serverip = tunnel.getVideoip();
            String port = portEntity.getPort();
            clearUdpPort(clientip, serverip, port);
            Tunnel tunnel2 =TunnelProcess.getTunnel(portEntity.getTunnel());
            String clientip2 = tunnel2.getGapip();
            String serverip2 = tunnel2.getTargetIp();
            clearUdpPort(clientip2, serverip2, port);
        }
    }

    /**
     * 开启tcpport
     * @param clientIp
     * @param serverIp
     * @param port
     */
    public void initTcpPort(String clientIp, String serverIp, String port){
        TcpProcess videoProcess = new TcpProcess();
        if(port.contains("-")){
            videoProcess.initPrerouting(clientIp,port.replace("-",":"),serverIp,port);
        }
        else{
            videoProcess.initPrerouting(clientIp,port,serverIp,port);
        }
        videoProcess.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String localip = Config.getLocalip(serverIp);
        TcpProcess tcpProcess = new TcpProcess();
        tcpProcess.initPostrouting(serverIp,localip);
        tcpProcess.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * 清除tcp port
     * @param clientIp
     * @param serverIp
     * @param port
     */
    public void clearTcpPort(String clientIp, String serverIp, String port){
        TcpProcess videoProcess = new TcpProcess();
        if(port.contains("-")){
            videoProcess.clearPrerouting(clientIp,port.replace("-",":"),serverIp,port);
        }
        else{
            videoProcess.clearPrerouting(clientIp,port,serverIp,port);
        }
        videoProcess.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String localip = Config.getLocalip(serverIp);
        TcpProcess tcpProcess = new TcpProcess();
        tcpProcess.clearPostrouting(serverIp,localip);
        tcpProcess.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * 开启udpport
     * @param clientIp
     * @param serverIp
     * @param port
     */
    public void initUdpPort(String clientIp, String serverIp, String port){
        NewUdpProcess udpProcess = new NewUdpProcess();
        if(port.contains("-")){
            udpProcess.initPrerouting(clientIp,port.replace("-",":"),serverIp,port);
        }
        else{
            udpProcess.initPrerouting(clientIp,port,serverIp,port);
        }
        udpProcess.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String localip = Config.getLocalip(serverIp);
        NewUdpProcess udpProcess2 = new NewUdpProcess();
        udpProcess2.initPostrouting(serverIp,localip);
        udpProcess2.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * 清除udpport
     * @param clientIp
     * @param serverIp
     * @param port
     */
    public void clearUdpPort(String clientIp, String serverIp, String port){
        NewUdpProcess udpProcess = new NewUdpProcess();
        if(port.contains("-")){
            udpProcess.clearPrerouting(clientIp,port.replace("-",":"),serverIp,port);
        }
        else{
            udpProcess.clearPrerouting(clientIp,port,serverIp,port);
        }
        udpProcess.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String localip = Config.getLocalip(serverIp);
        NewUdpProcess udpProcess2 = new NewUdpProcess();
        udpProcess2.clearPostrouting(serverIp,localip);
        udpProcess2.start();
        try {
            Thread.sleep(1000*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
