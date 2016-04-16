package com.hzih.gap.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.gap.dao.DepartmentDao;
import com.hzih.gap.domain.Department;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-7
 * Time: 上午11:01
 * To change this template use File | Settings | File Templates.
 */
public class DepartmentDaoImpl extends MyDaoSupport implements DepartmentDao {
    @Override
    public void setEntityClass() {
        this.entityClass = Department.class;
    }
}
