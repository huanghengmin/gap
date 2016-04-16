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
@Entity(schema = "stp", table = "safe_event_security_alert")
public class SafeEventSecurityAlert extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public int id;
    @Column
    public String name;
    @Column
    public String obj_type;
    @Column
    public Timestamp alert_time;
    @Column
    public String alert_type_code;
    @Column
    public String alert_info;
    @Column
    public String isRead;
    @Column
    public String ip;

    public String getAlert_type_code() {
        return alert_type_code;
    }

    public void setAlert_type_code(String alert_type_code) {
        this.alert_type_code = alert_type_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObj_type() {
        return obj_type;
    }

    public void setObj_type(String obj_type) {
        this.obj_type = obj_type;
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
