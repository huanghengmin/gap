package com.hzih.gap.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.gap.dao.EquipmentAlertDao;
import com.hzih.gap.domain.EquipmentAlert;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-3
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public class EquipmentAlertDaoImpl extends MyDaoSupport implements EquipmentAlertDao {

    @Override
    public void setEntityClass() {
        this.entityClass = EquipmentAlert.class;
    }

}
