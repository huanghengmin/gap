package com.hzih.gap.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-12
 * Time: 上午9:19
 * To change this template use File | Settings | File Templates.
 */
public class Control {
    private String id;
    private String clientip;
    private String clientport;
    private String targetip;
    private String targetport;
    private String allow;

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public String getClientport() {
        return clientport;
    }

    public void setClientport(String clientport) {
        this.clientport = clientport;
    }

    public String getTargetip() {
        return targetip;
    }

    public void setTargetip(String targetip) {
        this.targetip = targetip;
    }

    public String getTargetport() {
        return targetport;
    }

    public void setTargetport(String targetport) {
        this.targetport = targetport;
    }

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
