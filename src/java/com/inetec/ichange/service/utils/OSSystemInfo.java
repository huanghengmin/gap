package com.inetec.ichange.service.utils;

import com.ichange.common.utils.OSMonitSystemInfo;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2007-10-27
 * Time: 19:55:00
 * To change this template use File | Settings | File Templates.
 */
public class OSSystemInfo {
    private double cpu;
    private long freemem;
    private long totalmem;
    private long usemem;

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public long getFreemem() {
        return freemem;
    }

    public void setFreemem(long freemem) {
        this.freemem = freemem;
    }

    public long getTotalmem() {
        return totalmem;
    }

    public void setTotalmem(long totalmem) {
        this.totalmem = totalmem;
    }

    public long getUsemem() {
        return usemem;
    }

    public void setUsemem(long usemem) {
        this.usemem = usemem;
    }

    private void getSysInfo() {
        cpu = Math.abs(OSMonitSystemInfo.getProcessCPUUsage());
        freemem = OSMonitSystemInfo.getFreeMem() / 1024;
        totalmem = OSMonitSystemInfo.getMaxMem() / 1024;
        usemem = (OSMonitSystemInfo.getMaxMem() - OSMonitSystemInfo.getFreeMem()) / 1024;
        OSMonitSystemInfo.detachProcess();
    }

    public String toString(boolean m_bPrivate) {
        getSysInfo();
        StringBuffer buff = new StringBuffer();
        buff.append("{'network':'");
        if (m_bPrivate)
            buff.append("t'");
        else
            buff.append("f'");
        buff.append(",'cpu':");
        buff.append(cpu);
        buff.append(",");
        buff.append("'freemem':");
        buff.append(freemem);
        buff.append(",");
        buff.append("'totalmem':");
        buff.append(totalmem);
        buff.append(",");
        buff.append("'usemem':");
        buff.append(usemem);
        buff.append("}");
        return buff.toString();
    }

    public String toXmlData() {
        getSysInfo();
        StringBuffer buff = new StringBuffer();
        buff.append("used_cpu:");
        buff.append(cpu);
        buff.append(";");
        buff.append("total_memory:");
        buff.append(totalmem);
        buff.append(";");
        buff.append("used_memory:");
        buff.append(usemem);
        return buff.toString();
    }

    public void init() {
        getSysInfo();
    }
}
