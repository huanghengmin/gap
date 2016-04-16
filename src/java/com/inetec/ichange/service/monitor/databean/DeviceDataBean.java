package com.inetec.ichange.service.monitor.databean;

/**
 *
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 10:09:33
 * To change this template use File | Settings | File Templates.
 */
public class DeviceDataBean extends BaseDataBean {
    /**
     *设备名
     */
    public String equ_name;
    /**
     * 设备ID
     */
    public int equ_id;
    /**
     * 最大连接数
     */
    public int maxcon;
    /**
     * 当前连接数
     */
    public int currentcon;
    /**
     * cpu使用率
     */
    public int cpu;
    /**
     *内存使用量
     */
    public float mem;
    /**
     *内存总量
     */
    public float mem_total;
    /**
     *硬盘总量
     */
    public float disk_total;
    /**
     *硬盘使用量
     */
    public float disk;
    /**
     * vpn隧道数
     */
    public int vpn;

    public int getVpn() {
        return vpn;
    }

    public void setVpn(int vpn) {
        this.vpn = vpn;
    }

    public String getEqu_name() {
        return equ_name;
    }

    public void setEqu_name(String equ_name) {
        this.equ_name = equ_name;
    }

    public int getEqu_id() {
        return equ_id;
    }

    public void setEqu_id(int equ_id) {
        this.equ_id = equ_id;
    }

    public int getMaxcon() {
        return maxcon;
    }

    public void setMaxcon(int maxcon) {
        this.maxcon = maxcon;
    }

    public int getCurrentcon() {
        return currentcon;
    }

    public void setCurrentcon(int currentcon) {
        this.currentcon = currentcon;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public float getMem() {
        return mem;
    }

    public void setMem(float mem) {
        this.mem = mem;
    }

    public float getMem_total() {
        return mem_total;
    }

    public void setMem_total(float mem_total) {
        this.mem_total = mem_total;
    }

    public float getDisk_total() {
        return disk_total;
    }

    public void setDisk_total(float disk_total) {
        this.disk_total = disk_total;
    }

    public float getDisk() {
        return disk;
    }

    public void setDisk(float disk) {
        this.disk = disk;
    }

    public String toJsonString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{'equ_name':'");
        buff.append(equ_name);
        buff.append("','equ_id':");
        buff.append(equ_id);
        buff.append(",'maxcon':");
        buff.append(maxcon);
        buff.append(",'currentcon':");
        buff.append(currentcon);
        buff.append(",'cpu':");
        buff.append(cpu);
        buff.append(",'mem':");
        buff.append(mem);
        buff.append(",'mem_total':");
        buff.append(mem_total);
        buff.append(",'disk':");
        buff.append(disk);
        buff.append(",'disk_total':");
        buff.append(disk_total);
        buff.append(",'vpn':");
        buff.append(vpn);
        buff.append("}");
        return buff.toString();
    }

    public static void main(String args[]) throws Exception {
        DeviceDataBean bean = new DeviceDataBean();
        bean.setCpu(30);
        bean.setEqu_name("id22");
        bean.setEqu_id(2);
        bean.setCurrentcon(300);
        bean.setMaxcon(1000);
        bean.setDisk(30);
        bean.setDisk_total(300);
        bean.setMem(16);
        bean.setMem_total(8);
        bean.setVpn(0);
        System.out.println(bean.toJsonString());
    }
}
