package com.hzih.gap.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-6
 * Time: 下午5:40
 * To change this template use File | Settings | File Templates.
 */
public class Ipmac {

    private int id;
    private String ip;
    private String mac;
    private int probe;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getProbe() {
        return probe;
    }

    public void setProbe(int probe) {
        this.probe = probe;
    }
}
