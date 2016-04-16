package com.hzih.gap.dao;

import cn.collin.commons.dao.BaseDao;
import com.hzih.gap.domain.Role;

public interface RoleDao extends BaseDao {

    public Role findByName(String name) throws Exception;
}
