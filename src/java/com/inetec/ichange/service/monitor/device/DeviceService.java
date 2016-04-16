package com.inetec.ichange.service.monitor.device;

import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.DeviceDataBean;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentAlertDao;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentAlert;
import com.inetec.ichange.service.monitor.device.source.DeviceSourceService;
import com.inetec.ichange.service.monitor.device.source.DeviceSourceServiceImpl;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.monitor.utils.Configuration;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 * 发送设备监控信息
 */
public class DeviceService extends Thread {
    private static final Logger logger = Logger.getLogger(DeviceService.class);
    private static final String ExternalXml = System.getProperty("ichange.home")+"/repository/external/config.xml";
    private boolean run = false;
    private String ip;
    private int port;

    public void init( ){
        try {
            Configuration config = new Configuration(ExternalXml);
            List<Channel> list = config.getChannels();
            for (Channel channel : list) {
                if(channel.getChannelValue().equals("1")){
                    ip = channel.gettIp();
                    port = Integer.parseInt(channel.gettPort());
                }
            }
        } catch (Ex ex) {
            logger.error("加载发送设备监控信息通道出错",ex);
        }
    }

    public void run(){
        run = true;
        while (run) {
            work();
            try {
                Thread.sleep(1000*20);
            } catch (InterruptedException e) {
            }
        }
    }

    private void work() {
        EquipmentDao dao = new EquipmentDao();
        List<Equipment> list = dao.list();
        for(Equipment equ : list){
            DeviceDataBean bean = (DeviceDataBean) SnmpMonitorService.dataset.beanset.get(equ.getEquipment_name());
            if (bean != null) {
                String status = String.valueOf(bean.getStatus());
                if(equ.getLink_type().equalsIgnoreCase("ext")
                        && equ.getMonitor_used().equalsIgnoreCase("1")){
                    String device = setDeviceInfo(equ,status);
                    String deviceRun = setDeviceRunInfo(bean);
                    String deviceAlert = setDeviceAlert(bean);
                    //TODO 发送运行监控信息到目标端
                    String equName = "ext_"+equ.getEquipment_name();

                    String equId = String.valueOf(bean.getEqu_id());

                    DeviceSourceService deviceSourceService = new DeviceSourceServiceImpl();
                    deviceSourceService.init(device,deviceRun,deviceAlert,equName,equId,ip,port);
                    deviceSourceService.send();
                }
            }
        }
    }

    public boolean isRunning() {
        return this.run;
    }

    public void close(){
        this.run = false;
    }

    public void restart() {
        if(Service.isRunDeviceService) {
            Service.deviceService.close();
        }
        Service.deviceService.init();
        new Thread(Service.deviceService).start();
        Service.isRunDeviceService = true;
    }


    private String setDeviceAlert(DeviceDataBean bean) {
        EquipmentAlertDao dao = new EquipmentAlertDao();
        EquipmentAlert alert = dao.findById((long)bean.equ_id);
        alert.setEquipment_name("ext_"+alert.getEquipment_name());
        byte[] buf = alert.toJsonString().getBytes();
        return new BASE64Encoder().encode(buf);
    }

    private String setDeviceRunInfo(DeviceDataBean bean) {
        byte[] buf = bean.toJsonString().getBytes();
        return new BASE64Encoder().encode(buf);
    }

    private String setDeviceInfo(Equipment equ, String status) {
//        logger.info(toJsonString(equ,status));
        byte[] buf = toJsonString(equ, status).getBytes();
        return new BASE64Encoder().encode(buf);
    }
    private String toJsonString(Equipment equ, String status) {
        boolean isPing = (Boolean)Service.pingTelnetService.ping.get(equ.getId());
        boolean isTelnet = (Boolean)Service.pingTelnetService.telnet.get(equ.getId());
        StringBuffer buff = new StringBuffer();
        buff.append("{equ_id:'");
        buff.append(equ.getId());
        buff.append("',status:");
        buff.append(status);
        buff.append(",equ_name:'");
        buff.append("ext_"+equ.getEquipment_name());
        buff.append("',equ_desc:'");
        buff.append(equ.getEquipment_desc());
        buff.append("',ping:'");
        buff.append(isPing);
        buff.append("',telnet:'");
        buff.append(isTelnet);
        buff.append("',equ_type_code:'");
        buff.append(equ.getEquipment_type_code());
        buff.append("',equ_sys_config:'");
        buff.append(equ.getEquipment_sys_config());
        buff.append("',equ_manager_depart:'");
        buff.append(equ.getEquipment_manager_depart());
        buff.append("',monitor_used:");
        buff.append(equ.getMonitor_used());
        buff.append(",key_device:'");
        buff.append(equ.getKey_device());
        buff.append("',ip:'");
        buff.append(equ.getIp());
        buff.append("',other_ip:'");
        buff.append(equ.getOther_ip());
        buff.append("',mac:'");
        buff.append(equ.getMac());
        buff.append("',sub_net_mask:'");
        buff.append(equ.getSub_net_mask());
        buff.append("',link_name:'");
        buff.append(equ.getLink_name());
        buff.append("',link_type:'");
        buff.append(equ.getLink_type());
        buff.append("',port:'");
        buff.append(equ.getPort());
        buff.append("',oidname:'");
        buff.append(equ.getOidname());
        buff.append("',password:'");
        buff.append(equ.getPassword());
        buff.append("',snmpver:'");
        buff.append(equ.getSnmpver());
        buff.append("',auth:'");
        buff.append(equ.getAuth());
        buff.append("',authpassword:'");
        buff.append(equ.getAuthpassword());
        buff.append("',common:'");
        buff.append(equ.getCommon());
        buff.append("',commonpassword:'");
        buff.append(equ.getCommonpassword());
        buff.append("'}");
        return buff.toString();
    }
}
