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
 * Time: 13:40:39
 * To change this template use File | Settings | File Templates.
 */
@Entity(schema = "stp", table = "business_log")
public class BusinessLog extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public int id;
    @Column
    public Timestamp log_time;
    @Column
    public String level;
    @Column
    public String business_name;
    @Column
    public String business_desc;
    @Column
    public String business_type;
    @Column
    public String source_ip;
    @Column
    public String source_port;
    @Column
    public String source_jdbc;
    @Column
    public String dest_ip;
    @Column
    public String dest_port;
    @Column
    public String dest_jdbc;
    @Column
    public int audit_count;
    @Column
    public String file_name;
    @Column
    public String plugin;
    @Column
    public int json_id;
    @Column
    public int flag;

    public int recordCount;

    public int curconnect;

    public int count;

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

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_desc() {
        return business_desc;
    }

    public void setBusiness_desc(String business_desc) {
        this.business_desc = business_desc;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }

    public String getSource_port() {
        return source_port;
    }

    public void setSource_port(String source_port) {
        this.source_port = source_port;
    }

    public String getSource_jdbc() {
        return source_jdbc;
    }

    public void setSource_jdbc(String source_jdbc) {
        this.source_jdbc = source_jdbc;
    }

    public String getDest_ip() {
        return dest_ip;
    }

    public void setDest_ip(String dest_ip) {
        this.dest_ip = dest_ip;
    }

    public String getDest_port() {
        return dest_port;
    }

    public void setDest_port(String dest_port) {
        this.dest_port = dest_port;
    }

    public String getDest_jdbc() {
        return dest_jdbc;
    }

    public void setDest_jdbc(String dest_jdbc) {
        this.dest_jdbc = dest_jdbc;
    }

    public int getAudit_count() {
        return audit_count;
    }

    public void setAudit_count(int audit_count) {
        this.audit_count = audit_count;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public int getJson_id() {
        return json_id;
    }

    public void setJson_id(int json_id) {
        this.json_id = json_id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getCurconnect() {
        return curconnect;
    }

    public void setCurconnect(int curconnect) {
        this.curconnect = curconnect;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toSysLogString() {

        String str = business_name+","+business_type+","+level+","+plugin+","+source_ip+","+source_port+","+audit_count+","+business_desc;

        return str;
    }



    public BusinessLog jsonToBusinessLogBean(JSONObject json) {
        BusinessLog bean = new BusinessLog();
        bean.setLevel(json.getString("level"));
        bean.setBusiness_name(json.getString("name"));
        bean.setBusiness_desc(json.getString("desc"));
        bean.setBusiness_type(json.getString("type"));
        bean.setSource_ip(json.getString("sIp"));
        bean.setSource_port(json.getString("sPort"));
        bean.setSource_jdbc(json.getString("sdb"));
        bean.setDest_ip(json.getString("tIp"));
        bean.setDest_port(json.getString("tPort"));
        bean.setJson_id(json.getInt("jsonId"));
        bean.setDest_jdbc(json.getString("tdb"));
        bean.setAudit_count(Integer.parseInt(json.getString("count")));
        bean.setLog_time(toTime(json.getString("time")));
        bean.setFile_name(json.getString("fName"));
        bean.setPlugin(json.getString("plugin"));
        bean.setFlag(0);
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
