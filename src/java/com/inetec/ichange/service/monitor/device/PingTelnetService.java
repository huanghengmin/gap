package com.inetec.ichange.service.monitor.device;

import com.inetec.common.exception.Ex;
import com.inetec.common.net.Ping;
import com.inetec.common.net.Telnet;
import com.inetec.common.util.OSInfo;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 13-2-26
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 */
public class PingTelnetService extends Thread {
    private final static Logger logger = Logger.getLogger(PingTelnetService.class);
    public ConcurrentHashMap ping = new ConcurrentHashMap();
    public ConcurrentHashMap telnet = new ConcurrentHashMap();
    public ConcurrentHashMap pingExt = new ConcurrentHashMap();
    public ConcurrentHashMap telnetExt = new ConcurrentHashMap();
    private List<Equipment> equipments = new ArrayList<Equipment>();
    private boolean isRunning = false;

    public void init() {
        EquipmentDao dao = new EquipmentDao();
        equipments = dao.list();
        for (Equipment equ : equipments) {
            ping.put(equ.getId(),false);
            telnet.put(equ.getId(),false);
        }
    }

    public void run() {
        isRunning = true;
        while (isRunning){
            for (Equipment equ : equipments) {
                String pingStr = null;
                try {
                    pingStr = Ping.exec(equ.getIp(), 1);
                    boolean isPing = getResult(pingStr);
                    if(ping.containsKey(equ.getId())){
                        ping.remove(equ.getId());
                    }
                    ping.put(equ.getId(),isPing);
                } catch (Ex ex) {
                    logger.error("ping 错误",ex);
                }

                try {
                    boolean isTelnet = Telnet.exec(equ.getIp(), Integer.parseInt(equ.getPort()));
                    if(telnet.containsKey(equ.getId())){
                        telnet.remove(equ.getId());
                    }
                    telnet.put(equ.getId(),isTelnet);
                } catch (Ex ex) {
                    logger.error("telnet 错误",ex);
                }
            }
            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {

            }
        }
    }

    private boolean getResult(String pingStr) {
        if(( pingStr.indexOf("ttl")>-1 && pingStr.indexOf("time")>-1 )
                ||(pingStr.indexOf("bytes")>-1 && pingStr.indexOf("time")>-1
                                                && pingStr.indexOf("TTL")>-1)){
            return true;
        } else {
            return false;
        }
	}

    public void restart() {
        if(Service.isPingTelnetService){
            Service.pingTelnetService.close();
        }
        Service.pingTelnetService.init();
        new Thread(Service.pingTelnetService).start();
        Service.isPingTelnetService = true;
    }

    public void close() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
