package com.hzih.gap.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.gap.domain.DeleteStatus;

public interface DeleteStatusDao extends BaseDao{

    public PageResult listByPage(int pageIndex, int limit) throws Exception;

    public DeleteStatus findByAppName(String appName) throws Exception;

    public void deleteByAppName(String appName) throws Exception;
}
