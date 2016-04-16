package com.inetec.ichange.service.monitor.device.target;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.databean.DeviceDataBean;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentAlert;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public class DeviceTargetService extends Thread {
    private static final Logger logger = Logger.getLogger(DeviceTargetService.class);
    private ByteArrayOutputStream buffs = new ByteArrayOutputStream();
    private boolean run = false;
    private String body;
    private String equName;
    private String equId;
    private String type;
    public BlockingQueue<AuditInfo> query;

    public void init() {
        query = new LinkedBlockingQueue<AuditInfo>(1000);
    }

    public void addList(String header, ByteArrayInputStream in) {
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setHeader(header);
        auditInfo.setByteArrayInputStream(in);
        query.offer(auditInfo);
    }

    public AuditInfo pollQuery(){
        AuditInfo auditInfo = null;
        try {
            auditInfo = query.take();
        } catch (InterruptedException e) {
        }
        return auditInfo;
    }

    public boolean isQueryEmpty(){
        return query.isEmpty();
    }

    public void close() {
        run = false;
    }

    public boolean isRunning() {
        return run;
    }

    public void run(){
        run = true;
        while (run){
            if(!isQueryEmpty()){
                makeReceive();
            } else {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void makeReceive() {
        AuditInfo auditInfo = pollQuery();
        if(auditInfo!=null) {
            String header = auditInfo.getHeader();
            String equName = UdpHeader.getStringName(header);
            String equId = UdpHeader.getStringId(header);
            String type = UdpHeader.getType(header);
            String body = auditInfo.getBody();
            if(type!=null&&body!=null&&equName!=null&&equId!=null){
                receive(type,body,equName,equId);
            }
        }
    }


    private byte[] readInputStream(InputStream isReceive) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 1024];   //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = isReceive.read(buff)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
        return in_b;
    }

    public void receive(String type,String body,String equName,String equId) {
        try {
            //TODO a.存入内存,等待调用
            if(type.equals("device")){
                byte[] devices = new BASE64Decoder().decodeBuffer(body);
                Equipment equ = makeEquipment(devices);
                if(equ!=null){
                    SnmpMonitorService.dataset.putEquipment(equ);
                }
            } else if(type.equals("deviceRun")){
                byte[] deviceRuns = new BASE64Decoder().decodeBuffer(body);
                DeviceDataBean bean = makeDeviceRun(deviceRuns);
                if(bean!=null){
                    if(SnmpMonitorService.dataset.beanset.containsKey(equName)) {
                        SnmpMonitorService.dataset.beanset.remove(equName);
                    }
                    SnmpMonitorService.dataset.returnDeviceDataBean(bean.getEqu_name(),bean);
                    if(SnmpMonitorService.dataset.beanset2.containsKey("ext_"+bean.getEqu_id())){
                        SnmpMonitorService.dataset.beanset2.remove("ext_"+bean.getEqu_id());
                    }
                    SnmpMonitorService.dataset.beanset2.put("ext_"+bean.getEqu_id(),bean.getEqu_name());
                    SnmpMonitorService.dataset.beanset.put(equName,bean);
                }
            } else if(type.equals("deviceAlert")){
                byte[] deviceAlerts = new BASE64Decoder().decodeBuffer(body);
                EquipmentAlert alert = makeEquipmentAlert(deviceAlerts);
                if(alert!=null){
                    if(SnmpMonitorService.dataset.beanset4.containsKey("ext_"+equId)){
                        SnmpMonitorService.dataset.beanset4.remove("ext_"+alert.getId());
                    }
                    SnmpMonitorService.dataset.beanset4.put("ext_"+alert.getId(),alert);
                }
            }
            //TODO b.SysLog发出
        } catch (IOException e) {
            logger.error("接收审计日志解码出错",e);
        }
    }

/*    private void putDeviceBeanRun(Equipment equ,DeviceDataBean bean,EquipmentAlert alert) {

        SnmpMonitorService.dataset.putEquipment(equ);
        if(!SnmpMonitorService.dataset.beanset.containsKey(equ.getEquipment_name())) {
            SnmpMonitorService.dataset.beanset.put(equ.getEquipment_name(),bean);
        }
        if(!SnmpMonitorService.dataset.beanset2.containsKey("ext_"+bean.getEqu_id())){
            SnmpMonitorService.dataset.beanset2.put("ext_"+bean.getEqu_id(),bean.getEqu_name());
        }
        if(!SnmpMonitorService.dataset.beanset4.containsKey("ext_"+bean.getEqu_id())){
            SnmpMonitorService.dataset.beanset4.put("ext_"+alert.getId(),alert);
        }
        SnmpMonitorService.dataset.returnDeviceDataBean(bean.getEqu_name(),bean);
    }*/

    private EquipmentAlert makeEquipmentAlert(byte[] deviceAlerts) {
        String tempString = new String(deviceAlerts);
        if(tempString!=null&&tempString.startsWith("{")){
            JSONObject json = JSONObject.fromObject(tempString);
            return jsonToAlert(json);
        }
        return null;
    }

    private EquipmentAlert jsonToAlert(JSONObject json) {
        EquipmentAlert alert = new EquipmentAlert();
        alert.setId(json.getLong("id"));
        alert.setEquipment_name(json.getString("equ_name"));
        alert.setCpu(json.getInt("cpu"));
        alert.setMemory(json.getInt("memory"));
        alert.setDisk(json.getInt("disk"));
        return alert;
    }

    private Equipment makeEquipment(byte[] devices) {
        String tempString = new String(devices);
        if(tempString!=null&&tempString.startsWith("{")){
            JSONObject json = JSONObject.fromObject(tempString);
            return jsonToEqu(json);
        }
        return null;
    }

    private Equipment jsonToEqu(JSONObject json) {
        Equipment equ = new Equipment();
        equ.setId(json.getInt("equ_id"));
        equ.setEquipment_name(json.getString("equ_name"));
        equ.setEquipment_desc(json.getString("equ_desc"));
        boolean isPing = json.getBoolean("ping");
        boolean isTelnet = json.getBoolean("telnet");
        if(Service.pingTelnetService.pingExt.containsKey(equ.getId())){
            Service.pingTelnetService.pingExt.remove(equ.getId());
        }
        if(Service.pingTelnetService.telnetExt.containsKey(equ.getId())){
            Service.pingTelnetService.telnetExt.remove(equ.getId());
        }
        Service.pingTelnetService.pingExt.put(equ.getId(),isPing);
        Service.pingTelnetService.telnetExt.put(equ.getId(),isTelnet);
        equ.setEquipment_type_code(json.getString("equ_type_code"));
        equ.setEquipment_manager_depart(json.getString("equ_manager_depart"));
        equ.setEquipment_sys_config(json.getString("equ_sys_config"));
        equ.setKey_device(json.getString("key_device"));
        equ.setIp(json.getString("ip"));
        equ.setOther_ip(json.getString("other_ip"));
        equ.setPort(json.getString("port"));
        equ.setMac(json.getString("mac"));
        equ.setMonitor_used(json.getString("monitor_used"));
        equ.setSub_net_mask(json.getString("sub_net_mask"));
        equ.setLink_name(json.getString("link_name"));
        equ.setLink_type(json.getString("link_type"));
        equ.setStatus(json.getInt("status"));
        equ.setAuth(json.getString("auth"));
        equ.setAuthpassword(json.getString("authpassword"));
        equ.setCommon(json.getString("common"));
        equ.setCommonpassword(json.getString("commonpassword"));
        equ.setSnmpver(json.getString("snmpver"));
        equ.setOidname(json.getString("oidname"));
        equ.setPassword(json.getString("password"));
        return equ;
    }

    private DeviceDataBean makeDeviceRun(byte[] deviceRuns) {
        String tempString = new String(deviceRuns);
        if(tempString!=null&&tempString.startsWith("{")){
            JSONObject json = JSONObject.fromObject(tempString);
            return jsonToBean(json);
        }
        return null;
    }

    private DeviceDataBean jsonToBean(JSONObject json) {
        DeviceDataBean bean = new DeviceDataBean();
        bean.setEqu_id(json.getInt("equ_id"));
        bean.setEqu_name("ext_"+json.getString("equ_name"));
        bean.setCpu(json.getInt("cpu"));
        bean.setVpn(json.getInt("vpn"));
        bean.setCurrentcon(json.getInt("currentcon"));
        bean.setMaxcon(json.getInt("maxcon"));
        bean.setDisk(json.getInt("disk"));
        bean.setDisk_total(json.getInt("disk_total"));
        bean.setMem(Float.parseFloat(json.getString("mem")));
        bean.setMem_total(Float.parseFloat(json.getString("mem_total")));
        return bean;
    }
}
