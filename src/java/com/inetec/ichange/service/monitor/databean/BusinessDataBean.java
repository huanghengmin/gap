package com.inetec.ichange.service.monitor.databean;

/**
 * 业务运行统计
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 10:10:03
 * To change this template use File | Settings | File Templates.
 */
public class BusinessDataBean extends BaseDataBean {
    /**
     * 业务名
     */
    public String business_name;
    /**
     * 总记录/请求数
     */
    public long record_count;
    /**
     * xt_dataflow总流量MB
     */
    public float xt_dataflow;
    /**
     * 总报警数
     */
    public int alert_count;
    /**
     * 连接数
     */
    public int connect_count;
    /**
     * action_time响应时间(毫秒)
     */
    public int action_time;


    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public long getRecord_count() {
        return record_count;
    }

    public void setRecord_count(long record_count) {
        this.record_count = record_count;
    }

    public int getAlert_count() {
        return alert_count;
    }

    public void setAlert_count(int alert_count) {
        this.alert_count = alert_count;
    }

    public float getXt_dataflow() {
        return xt_dataflow;
    }

    public void setXt_dataflow(float xt_dataflow) {
        this.xt_dataflow = xt_dataflow;
    }

    public int getConnect_count() {
        return connect_count;
    }

    public void setConnect_count(int connect_count) {
        this.connect_count = connect_count;
    }

    public int getAction_time() {
        return action_time;
    }

    public void setAction_time(int action_time) {
        this.action_time = action_time;
    }


    public String toJsonString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{'business_name':'");
        buff.append(business_name);
        buff.append("','record_count':");
        buff.append(record_count);
        buff.append(",'alert_count':");
        buff.append(alert_count);
        buff.append(",'xt_dataflow':");
        buff.append(xt_dataflow);
        buff.append(",'connect_count':");
        buff.append(connect_count);
        buff.append(",'action_time':");
        buff.append(action_time);
        buff.append("}");
        return buff.toString();
    }

}
