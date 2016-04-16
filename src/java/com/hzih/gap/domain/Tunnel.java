package com.hzih.gap.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-10
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
public class Tunnel {
    
    private String id;
    
    private String name;
    
    private String localIp;
    
    private String targetIp;
    
    private String gapip;
    
    private String videoip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public String getGapip() {
        return gapip;
    }

    public void setGapip(String gapip) {
        this.gapip = gapip;
    }

    public String getVideoip() {
        return videoip;
    }

    public void setVideoip(String videoip) {
        this.videoip = videoip;
    }
}
