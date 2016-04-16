package com.inetec.ichange.service.monitor.databean.entity;

import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;


@Entity(schema="gap",table="snmpoid")
public class SnmpOIDBean {
	@Column(isPrimaryKey = true)
	public String name;
	@Column
	public String oidtype;
	@Column
	public String company;
	@Column
	public String snmpver;
	@Column 
	public String cpuuse;
	@Column 
	public String disktotal;
	@Column 
	public String diskuse;
	@Column 
	public String memtotal;
	@Column 
	public String memuse;
	@Column 
	public String curconn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOidtype() {
        return oidtype;
    }

    public void setOidtype(String oidtype) {
        this.oidtype = oidtype;
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
