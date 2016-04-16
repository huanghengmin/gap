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
 * Time: 上午11:18
 * To change this template use File | Settings | File Templates.
 */
public class UdpService implements Runnable{
    private static Logger logger = Logger.getLogger(UdpService.class);
    private LinkedList<UdpEntity> udpEntities;
    private boolean isRun;
    public static Map<String,List<UdpProcess>> map = new HashMap();
    public UdpService(LinkedList<UdpEntity>  udpEntities){
        this.udpEntities = udpEntities;
        map.clear();
    }
    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        if(udpEntities.size()>0){
            for(int i = 0; i < udpEntities.size() ; i++){
                UdpEntity udpEntity = udpEntities.get(i);
                String clientIp = udpEntity.getClientIp();
                String clientPort = udpEntity.getClientPort();
                String serverIp = udpEntity.getServerIp();
                String serverPort = udpEntity.getServerPort();
                if(clientPort.contains("-") && serverPort.contains("-")){
                    int client_start_port = Integer.parseInt(clientPort.split("-")[0]);
                    int client_end_port = Integer.parseInt(clientPort.split("-")[1]);
                    int client_port_nums = client_end_port-client_start_port;
                    int server_start_port = Integer.parseInt(serverPort.split("-")[0]);
                    int server_end_port = Integer.parseInt(serverPort.split("-")[1]);
                    int server_port_nums = server_end_port-server_start_port;
                    if(client_port_nums == server_port_nums){
                        ArrayList<Integer> server_ports = new ArrayList<>();
                        ArrayList<Integer> client_ports = new ArrayList<>();
                        for(int j = server_start_port ; j <= server_end_port ; j++){
                            server_ports.add(j);
                        }
                        for(int j = client_start_port ; j <= client_end_port; j++){
                            client_ports.add(j);
                        }
                        for(int j = 0 ; j < client_ports.size() ; j++){
                            UdpProcess videoProcess = new UdpProcess();
                            videoProcess.init(clientIp,client_ports.get(j),clientIp,client_ports.get(j),serverIp,server_ports.get(j));
                            videoProcess.start();
                           // updateQuartzJob(udpEntity.getStart()+"-"+udpEntity.getEnd());
                            if(map.containsKey(udpEntity.getId())){
                                List<UdpProcess> list = map.get(udpEntity.getId());
                                list.add(videoProcess);
                                map.put(udpEntity.getId(),list);
                            }
                            else{
                                List<UdpProcess> list = new ArrayList<>();
                                list.add(videoProcess);
                                map.put(udpEntity.getId(),list);
                            }
                        }
                    }
                    else{
                        logger.error("配置Tcp信令通道，客户端端口数与服务端端口数不等");
                    }
                }
                else{
                    UdpProcess videoProcess = new UdpProcess();
                    videoProcess.init(clientIp,Integer.parseInt(clientPort),clientIp,Integer.parseInt(clientPort),serverIp,Integer.parseInt(serverPort));
                    videoProcess.start();
                    //updateQuartzJob(udpEntity.getStart()+"-"+udpEntity.getEnd());
                    if(map.containsKey(udpEntity.getId())){
                        List<UdpProcess> list = map.get(udpEntity.getId());
                        list.add(videoProcess);
                        map.put(udpEntity.getId(),list);
                    }
                    else{
                        List<UdpProcess> list = new ArrayList<>();
                        list.add(videoProcess);
                        map.put(udpEntity.getId(),list);
                    }
                }
            }
        }
    }
   /* private void updateQuartzJob(String worktime){

        Configuration configuration = null;
        try {
            configuration = new Configuration(path);
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        configuration.updateQuartzJob(worktime);
    }*/
}
