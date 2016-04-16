package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentType;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

public class EquipmentTypeDao {
    private static Logger logger = Logger.getLogger(EquipmentTypeDao.class);

    public EquipmentTypeDao() {

    }

    public Pagination<EquipmentType> listDevice(int limit, int start) {
        Pagination<EquipmentType> result = null;
        try {
            TransferUtil.registerClass(EquipmentType.class);

            GenericDAO<EquipmentType> genericDAO = new GenericDaoImpl<EquipmentType>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(EquipmentType.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(EquipmentType.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public EquipmentType findById(String id) {
        EquipmentType equipmentType = null;
        try {
            TransferUtil.registerClass(EquipmentType.class);

            GenericDAO<EquipmentType> genericDAO = new GenericDaoImpl<EquipmentType>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            Object[] obj = new Object[1];
            obj[0] = id;
            equipmentType = genericDAO.findByPrimaryKey(EquipmentType.class,obj);

        } catch (Exception e) {
            logger.error(e);
        }
        return equipmentType;
    }

}
