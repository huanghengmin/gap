package com.hzih.gap.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.gap.dao.SnmpOIDDao;
import com.hzih.gap.domain.Role;
import com.hzih.gap.domain.SnmpOid;


import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-12
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public class SnmpOIDDaoImpl extends MyDaoSupport implements SnmpOIDDao {
    @Override
    public void setEntityClass() {
        //To change body of implemented methods use File | Settings | File Templates.
        this.entityClass = SnmpOid.class;
    }
    /*public List<SnmpOIDBean> list() {
        String hql = new String("from SnmpOid ");
        List<SnmpOIDBean> list = getHibernateTemplate().find(hql,null);
        return list;
    }*/
}
