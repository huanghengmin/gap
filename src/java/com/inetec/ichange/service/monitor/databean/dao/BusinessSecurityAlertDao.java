/*
package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.BusinessSecurityAlert;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class BusinessSecurityAlertDao {
    private static Logger logger = Logger.getLogger(BusinessSecurityAlertDao.class);

    public BusinessSecurityAlertDao() {

    }

    public Pagination<BusinessSecurityAlert> listBusinessHourReport(int limit, int start) {
        Pagination<BusinessSecurityAlert> result = null;
        try {
            TransferUtil.registerClass(BusinessSecurityAlert.class);

            GenericDAO<BusinessSecurityAlert> genericDAO = new GenericDaoImpl<BusinessSecurityAlert>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(BusinessSecurityAlert.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(BusinessSecurityAlert.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public void saveBusinessSecurityAlerts(List<BusinessSecurityAlert> beans) {
        try {
            for (BusinessSecurityAlert bean : beans) {
                saveBusinessSecurityAlert(bean);
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public void saveBusinessSecurityAlert(BusinessSecurityAlert bean) {
        try {
            TransferUtil.registerClass(BusinessSecurityAlert.class);

            GenericDAO<BusinessSecurityAlert> genericDAO = new GenericDaoImpl<BusinessSecurityAlert>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            Timestamp myDate = bean.getAlert_time();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            boolean isToday = false;
            if(now.getYear()==myDate.getYear()
                    && now.getMonth() == myDate.getMonth()
                            && now.getDay()==myDate.getDay()){
                isToday = true;
            }
            if(isToday){
                String sql = "select * from business_security_alert " +
                        "where DATE_FORMAT(alert_time, '%Y-%m-%d %H:%i:%s')=curdate() " +
                        "and business_name = '" + bean.getBusiness_name() +
                        "' and alert_type_code = '" + bean.getAlert_type_code() +
                        "' and user_name = '" + bean.getUser_name() +
                        "' and ip = '" + bean.getIp() +
                        "' and alert_info = '"+bean.getAlert_info()+"';";

                List<BusinessSecurityAlert> list = genericDAO.findByQuery(BusinessSecurityAlert.class, sql, 1, 1);
                if (list.size()==0){
                    genericDAO.createEntity(bean);
                } else if(list.size()==1){
                    BusinessSecurityAlert alert = list.get(0);
                    */
/*if(alert.getRead().equals("Y")){
                        alert.setRead("N");
                    }
                    alert.setAlert_time(entity.getAlert_time());
                    genericDAO.saveOrUpdate(alert);*//*

                    String sqlUpdate = "update business_security_alert " +
                            "set alert_time = DATE_FORMAT('" +bean.getAlert_time() +"','%Y-%m-%d %H:%i:%s'),isread = 'N' " +
                            "where id = "+alert.getId()+";";
                    genericDAO.executeQuery(sqlUpdate);
                }
            }else {
                logger.error("该业务异常告警时间不对(请确相关设备的系统时间正确),不能入库!["+bean.toJsonString()+"]");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
*/
