package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentLog;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

import java.util.List;

public class EquipmentLogDao {
    private static Logger logger = Logger.getLogger(EquipmentLogDao.class);

    public EquipmentLogDao() {

    }

    public Pagination<EquipmentLog> listDevice(int limit, int start) {
        Pagination<EquipmentLog> result = null;
        try {
            TransferUtil.registerClass(EquipmentLog.class);

            GenericDAO<EquipmentLog> genericDAO = new GenericDaoImpl<EquipmentLog>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(EquipmentLog.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(EquipmentLog.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public void saveEquipmentLog(EquipmentLog bean) {
        try {
            TransferUtil.registerClass(EquipmentLog.class);

            GenericDAO<EquipmentLog> genericDAO = new GenericDaoImpl<EquipmentLog>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());

            if (genericDAO.isEntityExists(bean)) {

                genericDAO.saveOrUpdate(bean);
            } else {
                genericDAO.createEntity(bean);
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public boolean saveEquipmentLog(List<EquipmentLog> beans) {
        try {
            TransferUtil.registerClass(EquipmentLog.class);

            GenericDAO<EquipmentLog> genericDAO = new GenericDaoImpl<EquipmentLog>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            genericDAO.batchInsert(beans);
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
        return true;
    }
}
