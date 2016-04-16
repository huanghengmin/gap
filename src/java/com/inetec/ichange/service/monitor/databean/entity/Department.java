package com.inetec.ichange.service.monitor.databean.entity;

import com.avdy.p4j.jdbc.model.Column;
import com.avdy.p4j.jdbc.model.Entity;
import com.inetec.ichange.service.monitor.databean.BaseDataBean;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-21
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
@Entity(schema = "stp", table = "department")
public class Department extends BaseDataBean {
    @Column(isPrimaryKey = true)
    public String code;
    @Column
    public String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
