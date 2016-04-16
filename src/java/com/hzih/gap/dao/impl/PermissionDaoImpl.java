package com.hzih.gap.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.gap.dao.PermissionDao;
import com.hzih.gap.domain.Permission;

public class PermissionDaoImpl extends MyDaoSupport implements PermissionDao {

	@Override
	public void setEntityClass() {
		this.entityClass = Permission.class;
	}

    @Override
    public PageResult listByPage(int start, int limit) {
        String hql = "from Permission";
		String countHql = "select count(*) " + hql;
		PageResult ps = this.findByPage(hql, countHql, 	start/limit+1, limit);
		return ps;
    }
}
