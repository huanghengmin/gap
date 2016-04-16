package com.inetec.ichange.service.monitor.databean.entity;

import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;
import com.inetec.ichange.service.monitor.databean.BaseDataBean;

import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 10:10:35
 * To change this template use File | Settings | File Templates.
 */

@Entity(schema = "stp", table = "equipment_log")
public class EquipmentLog extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public int id;
    @Column
    public Timestamp log_time;
    @Column
    public String level;
    @Column
    public String link_name;
    @Column
    public String equipment_name;
    @Column
    public String log_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getLog_time() {
        return log_time;
    }

    public void setLog_time(Timestamp log_time) {
        this.log_time = log_time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLink_name() {
        return link_name;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getLog_info() {
        return log_info;
    }

    public void setLog_info(String log_info) {
        this.log_info = log_info;
    }
}
