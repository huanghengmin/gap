package com.inetec.ichange.service.monitor.utils;

import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.ReceiveService;
import com.inetec.ichange.service.monitor.databean.*;
import com.inetec.ichange.service.monitor.databean.dao.DepartmentDao;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentTypeDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 13:20:27
 * To change this template use File | Settings | File Templates.
 */
public class DeviceDataBeanSet {
    public ConcurrentHashMap beanset = new ConcurrentHashMap();
    public ConcurrentHashMap beanset2 = new ConcurrentHashMap();
    public ConcurrentHashMap beanset3 = new ConcurrentHashMap();    //目标端--装载接收到的源端设备信息
    public ConcurrentHashMap beanset4 = new ConcurrentHashMap();    //目标端--装载接收到的源端设备信息 的告警阀值
    private List<Equipment> equipments = new ArrayList<Equipment>();

    public void init(List<Equipment> list) {
        for (Equipment equ : list) {
            beanset.put(equ.getEquipment_name(), initBeanByEquipment(equ));
            beanset2.put(String.valueOf(equ.getId()), equ.getEquipment_name());
        }
    }

    public DeviceDataBean getDeviceDataBeanByIP(String id) {
        if (beanset.containsKey(id)) {
            return (DeviceDataBean) beanset.get(id);
        }
        return null;
    }

    public DeviceDataBean getDeviceDataBeanByID(String id) {
        if (beanset.containsKey(id)) {
            DeviceDataBean bean1 = (DeviceDataBean) beanset.get(id);
            beanset.put(id, bean1);
            return bean1;
        }
        return initBeanByEquipment(id);
    }

    public DeviceDataBean getDeviceDataBean(String id) {
        String name = "";
        if (beanset2.containsKey(id)) {
            name = (String) beanset2.get(id);
            beanset2.put(id, name);
        }
        if (beanset.containsKey(name)) {
            DeviceDataBean bean1 = (DeviceDataBean) beanset.get(name);
            beanset.put(name, bean1);
            return bean1;
        }
        return initBeanByEquipment(name);
    }

    public void putEquipment(Equipment equipment){
        equipment.setLastTime(System.currentTimeMillis());
        if(!SnmpMonitorService.dataset.beanset3.containsKey(equipment.getEquipment_name())) {
            beanset3.put(equipment.getEquipment_name(),equipment);
        } else {
            beanset3.remove(equipment.getEquipment_name());
            beanset3.put(equipment.getEquipment_name(),equipment);
        }
    }

    public String getEquipmentByNameToString(String eType) {
        Collection<Equipment> list = beanset3.values();
        Iterator<Equipment> iterator = list.iterator();
        String json = "";
        while (iterator.hasNext()){
            Equipment equ = iterator.next();

            if("stp".equalsIgnoreCase(eType)){
                if("ext_stp".equals(equ.getEquipment_name())){
                    json += toJsonString(equ,eType)+",";
                }
            } else if ("notstp".equalsIgnoreCase(eType)){
                if(!"ext_stp".equals(equ.getEquipment_name())){
                    json += toJsonString(equ,eType)+",";
                }
            }
        }
        return json;
    }

    public String getDeviceDataBeanByIDToJsonString(String id) {
        if (beanset.containsKey(id)) {
            return ((DeviceDataBean) beanset.get(id)).toJsonString();
        } else {
            return initBeanByEquipment(id).toJsonString();
        }
    }

    public void returnDeviceDataBean(String key, DeviceDataBean bean) {
        if(beanset.containsKey(key)){
            beanset.remove(key);
        }
        beanset.put(key, bean);
    }

    protected DeviceDataBean initBeanByEquipment(Equipment equ) {
        DeviceDataBean bean = new DeviceDataBean();
        bean.setStatus(DeviceDataBean.I_Status_Error);
        bean.setEqu_name(equ.getEquipment_name());
        bean.setEqu_id(equ.getId());
        bean.setCpu(0);
        bean.setCurrentcon(0);
        bean.setDisk(0);
        bean.setDisk_total(0);
        bean.setMaxcon(0);
        bean.setMem(0);
        bean.setMem_total(0);
        bean.setVpn(0);
        return bean;
    }

    protected DeviceDataBean initBeanByEquipment(String name) {
        DeviceDataBean bean = new DeviceDataBean();
        bean.setStatus(DeviceDataBean.I_Status_Error);
        //entity.setEqu_id(Integer.parseInt(getId()));
        bean.setEqu_name(name);
        bean.setCpu(0);
        bean.setCurrentcon(0);
        bean.setDisk(0);
        bean.setDisk_total(0);
        bean.setMaxcon(0);
        bean.setMem(0);
        bean.setMem_total(0);
        bean.setVpn(0);
        return bean;
    }

    private String toJsonString(Equipment equ, String eType) {
        boolean isPing = (Boolean) Service.pingTelnetService.pingExt.get(equ.getId());
        boolean isTelnet = (Boolean)Service.pingTelnetService.telnetExt.get(equ.getId());
        long lastTime = equ.getLastTime();
        if(System.currentTimeMillis()-lastTime>1000*60*5){  //5分钟未更新
            equ.setStatus(500);
            isPing = false;
            isTelnet = false;
        }

        StringBuffer buff = new StringBuffer();
        buff.append("{id:'");
        buff.append("ext_"+equ.getId());
        buff.append("',runStatus:");
        buff.append(equ.getStatus());
        buff.append(",equipmentName:'");
        buff.append(equ.getEquipment_name().substring(4));
        buff.append("',equipmentDesc:'");
        buff.append(equ.getEquipment_desc());
        buff.append("',ping:'");
        buff.append(isPing==true?"200":"500");
        buff.append("',telnet:'");
        buff.append(isTelnet==true?"200":"500");
        if("stp".equalsIgnoreCase(eType)){
            buff.append("',test:'");
            String s1 = (String) ReceiveService.initMap.get("s1")==null?"0":(String) ReceiveService.initMap.get("s1");
            String r1 = (String) ReceiveService.initMap.get("r1")==null?"0":(String) ReceiveService.initMap.get("r1");
//            String s2 = (String) ReceiveService.initMap.get("s2")==null?"0":(String) ReceiveService.initMap.get("s2");
//            String r2 = (String) ReceiveService.initMap.get("r2")==null?"0":(String) ReceiveService.initMap.get("r2");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long l_1 = sdf.parse(s1).getTime();
                long l_2 = sdf.parse(r1).getTime();
                if(System.currentTimeMillis()-l_2>1000*60){
                    buff.append(false);
                } else {
                    buff.append(true);
                }
            } catch (ParseException e) {

            }

        } else {
            buff.append("',test:'");
        }
        buff.append("',equipmentTypeCode:'");
        buff.append(equ.getEquipment_type_code());
        buff.append("',equipmentSysConfig:'");
        buff.append("',equ_manager_depart:'");
        buff.append(equ.getEquipment_manager_depart());
        buff.append("',monitorUsed:");
        buff.append(equ.getMonitor_used());
        buff.append(",isKeyDevice:'");
        buff.append(equ.getKey_device());
        buff.append("',ip:'");
        buff.append(equ.getIp());
        buff.append("',otherIp:'");
        buff.append(equ.getOther_ip());
        buff.append("',mac:'");
        buff.append(equ.getMac());
        buff.append("',subNetMask:'");
        buff.append(equ.getSub_net_mask());
        buff.append("',linkName:'");
        buff.append(equ.getLink_name());
        buff.append("',linkType:'");
        buff.append(equ.getLink_type());
        buff.append("',port:'");
        buff.append(equ.getPort());
        buff.append("',equipmentManagerDepart:'");
        buff.append(getDepartmentName(equ.getEquipment_manager_depart()));
        buff.append("',equipmentTypeName:'");
        buff.append(getEquipmentTypeName(equ.getEquipment_type_code()));
        buff.append("'}");
        return buff.toString();
    }

    private String getEquipmentTypeName(String equipment_type_code) {
        EquipmentTypeDao dao = new EquipmentTypeDao();
        return dao.findById(equipment_type_code).getName();
    }

    private String getDepartmentName(String equipment_manager_depart) {
        DepartmentDao dao = new DepartmentDao();
        return dao.findById(equipment_manager_depart).getName();
    }
}
