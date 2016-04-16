package com.inetec.ichange.service.monitor.databean.entity;

import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;
import com.inetec.ichange.service.monitor.databean.BaseDataBean;


/**
 *
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 10:10:35
 * To change this template use File | Settings | File Templates.
 */

@Entity(schema = "gap", table = "equipment")
public class Equipment extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public int id;
    @Column
    public String equipment_name;
    @Column
    public String equipment_desc;
    @Column
    public String equipment_type_code;
    @Column
    public String equipment_sys_config;
    @Column
    public String equipment_manager_depart;
    @Column
    public String monitor_used;
    @Column
    public String key_device;
    @Column
    public String ip;
    @Column
    public String other_ip;
    @Column
    public String mac;
    @Column
    public String sub_net_mask;
    @Column
    public String link_name;
    @Column
    public String link_type;
    @Column
    public String port;
    @Column
    public String password;
    @Column
    public String oidname;
    @Column
    public String snmpver;
    @Column
    public String auth;
    @Column
    public String authpassword;
    @Column
    public String common;
    @Column
    public String commonpassword;

    public long lastTime;

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getEquipment_desc() {
        return equipment_desc;
    }

    public void setEquipment_desc(String equipment_desc) {
        this.equipment_desc = equipment_desc;
    }

    public String getEquipment_type_code() {
        return equipment_type_code;
    }

    public void setEquipment_type_code(String equipment_type_code) {
        this.equipment_type_code = equipment_type_code;
    }

    public String getEquipment_sys_config() {
        return equipment_sys_config;
    }

    public void setEquipment_sys_config(String equipment_sys_config) {
        this.equipment_sys_config = equipment_sys_config;
    }

    public String getEquipment_manager_depart() {
        return equipment_manager_depart;
    }

    public void setEquipment_manager_depart(String equipment_manager_depart) {
        this.equipment_manager_depart = equipment_manager_depart;
    }

    public String getMonitor_used() {
        return monitor_used;
    }

    public void setMonitor_used(String monitor_used) {
        this.monitor_used = monitor_used;
    }

    public String getKey_device() {
        return key_device;
    }

    public void setKey_device(String key_device) {
        this.key_device = key_device;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOther_ip() {
        return other_ip;
    }

    public void setOther_ip(String other_ip) {
        this.other_ip = other_ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSub_net_mask() {
        return sub_net_mask;
    }

    public void setSub_net_mask(String sub_net_mask) {
        this.sub_net_mask = sub_net_mask;
    }

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOidname() {
        return oidname;
    }

    public void setOidname(String oidname) {
        this.oidname = oidname;
    }

    public String getSnmpver() {
        return snmpver;
    }

    public void setSnmpver(String snmpver) {
        this.snmpver = snmpver;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuthpassword() {
        return authpassword;
    }

    public void setAuthpassword(String authpassword) {
        this.authpassword = authpassword;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getCommonpassword() {
        return commonpassword;
    }

    public void setCommonpassword(String commonpassword) {
        this.commonpassword = commonpassword;
    }

    public String toJsonString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{equ_id:'");
        buff.append(id);
        buff.append("',equ_name:'");
        buff.append(equipment_name);
        buff.append("',equ_type_code:'");
        buff.append(equipment_type_code);
        buff.append("',equ_sys_config:'");
        buff.append("',equ_manager_depart:'");
        buff.append(equipment_manager_depart);
        buff.append("',monitor_used:");
        buff.append(monitor_used);
        buff.append(",key_device:'");
        buff.append(key_device);
        buff.append("',ip:'");
        buff.append(ip);
        buff.append("',other_ip:'");
        buff.append(other_ip);
        buff.append("',mac:'");
        buff.append(mac);
        buff.append("',sub_net_mask:'");
        buff.append(sub_net_mask);
        buff.append("',link_name:'");
        buff.append(link_name);
        buff.append("',link_type:'");
        buff.append(link_type);
        buff.append("',port:'");
        buff.append(port);
        buff.append("'}");
        return buff.toString();
    }
}
