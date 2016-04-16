package com.inetec.ichange.service.monitor.databean.entity;

import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;
import com.inetec.ichange.service.monitor.databean.BaseDataBean;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-11-14
 * Time: 20:09:11
 * To change this template use File | Settings | File Templates.
 */
@Entity(schema = "stp", table = "equipment_security_alert")
public class EquipmentSecurityAlert extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public int id;
    @Column
    public String equipment_name;
    @Column
    public Timestamp alert_time;
    @Column
    public String alert_info;
    @Column
    public String isRead;
    @Column
    public String ip;

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getRead() {
        return isRead;
    }

    public void setRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(Timestamp alert_time) {
        this.alert_time = alert_time;
    }

    public String getAlert_info() {
        return alert_info;
    }

    public void setAlert_info(String alert_info) {
        this.alert_info = alert_info;
    }
}
