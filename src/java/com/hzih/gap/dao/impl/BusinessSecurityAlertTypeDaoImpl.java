package com.hzih.gap.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.gap.dao.BusinessSecurityAlertTypeDao;
import com.hzih.gap.domain.BusinessSecurityAlertType;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-7
 * Time: 上午11:01
 * To change this template use File | Settings | File Templates.
 */
public class BusinessSecurityAlertTypeDaoImpl extends MyDaoSupport implements BusinessSecurityAlertTypeDao {
    @Override
    public void setEntityClass() {
        this.entityClass = BusinessSecurityAlertType.class;
    }
}