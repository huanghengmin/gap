package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.BusinessLog;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BusinessLogDao {
    private static Logger logger = Logger.getLogger(BusinessLogDao.class);

    public BusinessLogDao() {

    }

    public Pagination<BusinessLog> listBusinessLog(int limit, int start) {
        Pagination<BusinessLog> result = null;
        try {
            TransferUtil.registerClass(BusinessLog.class);

            GenericDAO<BusinessLog> genericDAO = new GenericDaoImpl<BusinessLog>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(BusinessLog.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(BusinessLog.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public void saveBusinessLog(BusinessLog bean) {
        try {
            TransferUtil.registerClass(BusinessLog.class);

            GenericDAO<BusinessLog> genericDAO = new GenericDaoImpl<BusinessLog>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            genericDAO.createEntity(bean);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void saveOrUpdateBusinessLog(BusinessLog bean) {
        try {
            TransferUtil.registerClass(BusinessLog.class);

            GenericDAO<BusinessLog> genericDAO = new GenericDaoImpl<BusinessLog>(
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

    public boolean saveBusinessLogs(List<BusinessLog> beans) {
        try {
            TransferUtil.registerClass(BusinessLog.class);

            GenericDAO<BusinessLog> genericDAO = new GenericDaoImpl<BusinessLog>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
//            genericDAO.batchInsert(beans);
            for(BusinessLog log : beans) {
                List<BusinessLog> list = new ArrayList<BusinessLog>();
                if(log.getBusiness_type().equals("file")){
                    String sql = "select * from business_log where business_name='"+log.getBusiness_name()+"' and " +
                            "level='"+log.getLevel()+"' and business_type='"+log.getBusiness_type()+"' and " +
                            "dest_ip='"+log.getDest_ip()+"' and dest_port='"+log.getDest_port()+"' and " +
                            "file_name='"+log.getFile_name()+"' and json_id= " +log.getJson_id()+" and " +
                            "audit_count="+log.getAudit_count()+" and plugin='"+log.getPlugin()+"'";
                    list = genericDAO.findByQuery(BusinessLog.class,sql,1,1);
                } else if(log.getBusiness_type().equals("db")){
                    String sql = "select * from business_log where business_name='"+log.getBusiness_name()+"' and " +
                            "level='"+log.getLevel()+"' and business_type='"+log.getBusiness_type()+"' and " +
//                            "dest_ip='"+log.getDest_ip()+"' and dest_port='"+log.getDest_port()+"' and " +
                            "file_name='"+log.getFile_name()+"' and source_ip='"+log.getSource_ip()+"' and " +
                            "source_jdbc='"+log.getSource_jdbc()+"' and dest_ip='"+log.getDest_jdbc()+"' and " +
                            "audit_count="+log.getAudit_count()+" and plugin='"+log.getPlugin()+"'";
                    list = genericDAO.findByQuery(BusinessLog.class,sql,1,1);
                }
                if(list.size()>0){
                    BusinessLog bean = list.get(0);
                    bean.setLog_time(log.getLog_time());
                    bean.setFlag(0);
                    genericDAO.saveOrUpdate(bean);
                } else {
                    genericDAO.createEntity(log);
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("目标端业务审计日志入库错误",e);
        }
        return false;
    }

    public boolean isSavedBusinessLog(BusinessLog log) throws Exception {
        TransferUtil.registerClass(BusinessLog.class);
        GenericDAO<BusinessLog> genericDAO = new GenericDaoImpl<BusinessLog>(
                DaoService.getDaoService().getDataProvider()
                        .getDataFetcher());
        if(log.getBusiness_type().equals("file")){
            String sql = "select * from business_log where business_name='"+log.getBusiness_name()+"' and " +
                    "level='"+log.getLevel()+"' and business_type='"+log.getBusiness_type()+"' and " +
                    "dest_ip='"+log.getDest_ip()+"' and dest_port='"+log.getDest_port()+"' and " +
                    "file_name='"+log.getFile_name()+"' and json_id= " +log.getJson_id()+" and " +
                    "audit_count="+log.getAudit_count()+" and plugin='"+log.getPlugin()+"'";
            List<BusinessLog> list = genericDAO.findByQuery(BusinessLog.class,sql,1,1);
            if(list.size()>0){
                return true;
            }
        }else if(log.getBusiness_type().equals("db")){
            String sql = "select * from business_log where business_name='"+log.getBusiness_name()+"' and " +
                    "level='"+log.getLevel()+"' and business_type='"+log.getBusiness_type()+"' and " +
//                    "dest_ip='"+log.getDest_ip()+"' and dest_port='"+log.getDest_port()+"' and " +
                    "file_name='"+log.getFile_name()+"' and source_ip='"+log.getSource_ip()+"' and " +
                    "source_jdbc='"+log.getSource_jdbc()+"' and dest_ip='"+log.getDest_jdbc()+"' and " +
                    "audit_count="+log.getAudit_count()+" and plugin='"+log.getPlugin()+"'";
            List<BusinessLog> list = genericDAO.findByQuery(BusinessLog.class,sql,1,1);
            if(list.size()>0){
                return true;
            }
        }
        return false;
    }
}
