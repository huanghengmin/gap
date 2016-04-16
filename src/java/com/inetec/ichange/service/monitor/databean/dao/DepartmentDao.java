package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.Department;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

public class DepartmentDao {
    private static Logger logger = Logger.getLogger(DepartmentDao.class);

    public DepartmentDao() {

    }

    public Pagination<Department> listDevice(int limit, int start) {
        Pagination<Department> result = null;
        try {
            TransferUtil.registerClass(Department.class);

            GenericDAO<Department> genericDAO = new GenericDaoImpl<Department>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(Department.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(Department.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public Department findById(String id) {
        Department department = null;
        try {
            TransferUtil.registerClass(Department.class);

            GenericDAO<Department> genericDAO = new GenericDaoImpl<Department>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            Object[] obj = new Object[1];
            obj[0] = id;
            department = genericDAO.findByPrimaryKey(Department.class,obj);

        } catch (Exception e) {
            logger.error(e);
        }
        return department;
    }

}
