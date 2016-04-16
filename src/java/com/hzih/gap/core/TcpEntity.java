package com.hzih.gap.core;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-17
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class TcpEntity {

    private String id;//编号
    private String task;//任务号
    private String serverIp;//服务端ip
    private String serverPort;//服务端端口
    private String band;//
    private String clientIp;//客户端ip
    private String clientPort;//客户端端口
    private String start;//任务运行开始时间
    private String end;//任务运行的结束时间
    private boolean run;//是否允许任务启动
    private String describe;//备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
