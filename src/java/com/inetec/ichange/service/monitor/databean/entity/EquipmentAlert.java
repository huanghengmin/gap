package com.inetec.ichange.service.monitor.databean.entity;

import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;
import com.inetec.ichange.service.monitor.databean.BaseDataBean;

/**
 * 设备告警阀值信息
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 10:10:35
 * To change this template use File | Settings | File Templates.
 */

@Entity(schema = "gap", table = "equipment_alert")
public class EquipmentAlert extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public long id;
    @Column
    public int cpu;
    @Column
    public String equipment_name;
    @Column
    public int memory;
    @Column
    public int disk;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDisk() {
        return disk;
    }

    public void setDisk(int disk) {
        this.disk = disk;
    }

    public String toJsonString(){
        StringBuffer buff = new StringBuffer();
        buff.append("{id:'");
        buff.append(id);
        buff.append("',equ_name:'");
        buff.append(equipment_name);
        buff.append("',cpu:");
        buff.append(cpu);
        buff.append(",memory:");
        buff.append(memory);
        buff.append(",disk:");
        buff.append(disk);
        buff.append("}");
        return buff.toString();
    }
}
