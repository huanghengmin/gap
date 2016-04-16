package com.hzih.gap.core;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-29
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class PortEntity {

    private String id ;
    private String port ;
    private String worktime ;
    private String run ;
    private String type ;
    private String tunnel ;
    private String tunneltype;
    private String runstate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    public String getTunneltype() {
        return tunneltype;
    }

    public void setTunneltype(String tunneltype) {
        this.tunneltype = tunneltype;
    }

    public String getRunstate() {
        return runstate;
    }

    public void setRunstate(String runstate) {
        this.runstate = runstate;
    }
}
