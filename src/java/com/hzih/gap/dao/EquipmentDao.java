package com.hzih.gap.dao;

import cn.collin.commons.dao.BaseDao;
import cn.collin.commons.domain.PageResult;
import com.hzih.gap.domain.Equipment;

public interface EquipmentDao extends BaseDao {

    public Equipment findByName(String equipmentName) throws Exception;

    public PageResult listPageResult(int pageIndex, int limit, String equipmentName) throws Exception;

    public PageResult listPageResult(int pageIndex, int limit, String equipmentName, String monitorUsed) throws Exception;

    public Equipment findByDesc(String equipmentDesc) throws Exception;

    public Equipment findHost() throws Exception;

}
