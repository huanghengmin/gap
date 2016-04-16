package com.hzih.gap.core;

import com.hzih.gap.utils.Configuration;
import com.hzih.gap.utils.StringContext;
import com.inetec.common.exception.Ex;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-17
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
public class TcpService implements Runnable{
    private static Logger logger = Logger.getLogger(TcpService.class);
    private LinkedList<TcpEntity>  tcpEntities;
    private boolean isRun;
    public static Map<String,List<TcpProcess>> map = new HashMap();
    public TcpService(LinkedList<TcpEntity>  tcpEntities){
        this.tcpEntities = tcpEntities;
        map.clear();
    }
    public Map<String,String> ipmap = new HashMap<>();
    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        InitProcess clear = new InitProcess();
        clear.initClear();
        clear.start();
        try {
            Thread.sleep(1000*60*1);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(tcpEntities.size()>0){
            logger.warn("tcpEntities size is "+tcpEntities.size());
            for(int i = 0; i < tcpEntities.size() ; i++){
                TcpEntity tcpEntity = tcpEntities.get(i);
                String clientIp = tcpEntity.getClientIp();
                String clientPort = tcpEntity.getClientPort();
                String serverIp = tcpEntity.getServerIp();
                String serverPort = tcpEntity.getServerPort();
                TcpProcess videoProcess = new TcpProcess();
                videoProcess.initPrerouting(clientIp,clientPort,serverIp,serverPort);
                videoProcess.start();
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                String localip = Config.getLocalip(serverIp);
                TcpProcess tcpProcess = new TcpProcess();
                tcpProcess.initPostrouting(serverIp,localip);
                tcpProcess.start();
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                //ipmap.put(serverIp,localip);

                /*if(!ipmap.containsKey(serverIp)){
                    String localip = Config.getLocalip(serverIp);
                    TcpProcess tcpProcess = new TcpProcess();
                    tcpProcess.initPostrouting(serverIp,localip);
                    tcpProcess.start();
                    ipmap.put(serverIp,localip);
                }
                else{
                    String localip1 = ipmap.get(serverIp);
                    String localip = Config.getLocalip(serverIp);
                    if(!localip1.equals(localip)){
                        TcpProcess tcpProcess = new TcpProcess();
                        tcpProcess.initPostrouting(serverIp,localip);
                        tcpProcess.start();
                        ipmap.put(serverIp,localip);
                    }
                }*/

            }
        }
    }
    /*private void updateQuartzJob(String worktime){

        Configuration configuration = null;
        try {
            configuration = new Configuration(path);
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        configuration.updateQuartzJob(worktime);
    }*/
}
