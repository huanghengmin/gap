package com.hzih.gap.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-11-27
 * Time: 上午11:47
 * To change this template use File | Settings | File Templates.
 */
public class SnmpOid {
    private Integer id;
    
    private String name;

    private String type;

    private String company;

    private String snmpver;

    private String cpuuse;

    private String disktotal;

    private String diskuse;

    private String memtotal;

    private String memuse;

    private String curconn;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSnmpver() {
        return snmpver;
    }

    public void setSnmpver(String snmpver) {
        this.snmpver = snmpver;
    }

    public String getCpuuse() {
        return cpuuse;
    }

    public void setCpuuse(String cpuuse) {
        this.cpuuse = cpuuse;
    }

    public String getDisktotal() {
        return disktotal;
    }

    public void setDisktotal(String disktotal) {
        this.disktotal = disktotal;
    }

    public String getDiskuse() {
        return diskuse;
    }

    public void setDiskuse(String diskuse) {
        this.diskuse = diskuse;
    }

    public String getMemtotal() {
        return memtotal;
    }

    public void setMemtotal(String memtotal) {
        this.memtotal = memtotal;
    }

    public String getMemuse() {
        return memuse;
    }

    public void setMemuse(String memuse) {
        this.memuse = memuse;
    }

    public String getCurconn() {
        return curconn;
    }

    public void setCurconn(String curconn) {
        this.curconn = curconn;
    }
}
