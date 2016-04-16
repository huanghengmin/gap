package com.inetec.ichange.service.monitor.databean;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 报警数据Bean
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 10:10:35
 * To change this template use File | Settings | File Templates.
 */


public class AlertDataBean extends BaseDataBean {

    private long id;

    private Timestamp alert_time;

    private String alert_info;

    private String name;

    private String isread;

    private String ip;

    private String code;

    private String username;

    private String objType;    //equipment or business

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(Date alert_time) {
        this.alert_time = (Timestamp) alert_time;
    }

    public String getAlert_info() {
        return alert_info;
    }

    public void setAlert_info(String alert_info) {
        this.alert_info = alert_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
