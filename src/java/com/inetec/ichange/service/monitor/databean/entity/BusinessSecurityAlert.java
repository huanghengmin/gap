package com.inetec.ichange.service.monitor.databean.entity;

import cn.collin.commons.utils.DateUtils;
import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;
import com.inetec.ichange.service.monitor.databean.BaseDataBean;
import net.sf.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-11-14
 * Time: 20:09:11
 * To change this template use File | Settings | File Templates.
 */
@Entity(schema = "stp", table = "business_security_alert")
public class BusinessSecurityAlert extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public int id;
    @Column
    public String business_name;
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
    @Column
    public String user_name;

    public String getAlert_type_code() {
        return alert_type_code;
    }

    public void setAlert_type_code(String alert_type_code) {
        this.alert_type_code = alert_type_code;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
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

    public BusinessSecurityAlert jsonToBusinessSecurityAlert(JSONObject json) {
        BusinessSecurityAlert bean = new BusinessSecurityAlert();
        bean.setBusiness_name(json.getString("name"));
        bean.setAlert_info(json.getString("alert_info"));
        bean.setAlert_type_code(json.getString("alert_code"));
        bean.setIp(json.getString("ip"));
        bean.setUser_name(json.getString("user_name"));
        bean.setAlert_time(toTime(json.getString("alert_time")));
        bean.setRead("N");
        return bean;
    }

    private Timestamp toTime(String time) {
        Date date = null;
        try {
            date = DateUtils.parse(time, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }
}
