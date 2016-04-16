package com.hzih.gap.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;

public interface BusinessSecurityAlertDao extends BaseDao {

	public PageResult listByPage(int pageIndex, int limit, String startDate,
                                 String endDate, String businessName, String alertCode, String read) throws Exception;

}
