package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentSecurityAlert;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class EquipmentSecurityAlertDao {
    private static Logger logger = Logger.getLogger(EquipmentSecurityAlertDao.class);

    public EquipmentSecurityAlertDao() {

    }

    public Pagination<EquipmentSecurityAlert> listBusinessHourReport(int limit, int start) {
        Pagination<EquipmentSecurityAlert> result = null;
        try {
            TransferUtil.registerClass(EquipmentSecurityAlert.class);

            GenericDAO<EquipmentSecurityAlert> genericDAO = new GenericDaoImpl<EquipmentSecurityAlert>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(EquipmentSecurityAlert.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(EquipmentSecurityAlert.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public void saveEquipmentSecurityAlert(EquipmentSecurityAlert bean) {
        try {
            TransferUtil.registerClass(EquipmentSecurityAlert.class);

            GenericDAO<EquipmentSecurityAlert> genericDAO = new GenericDaoImpl<EquipmentSecurityAlert>(
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
                String sql = "select * from equipment_security_alert " +
                        "where DATE_FORMAT(alert_time, '%Y-%m-%d %H:%i:%s')=curdate() " +
                        "and equipment_name = '" + bean.getEquipment_name() +
                        "' and ip = '" + bean.getIp() +
                        "' and alert_info = '"+bean.getAlert_info()+"';";

                List<EquipmentSecurityAlert> list = genericDAO.findByQuery(EquipmentSecurityAlert.class, sql, 1, 1);
                if (list.size()==0){
                    genericDAO.createEntity(bean);
                } else if(list.size()==1){
                    EquipmentSecurityAlert alert = list.get(0);
                    /*if(alert.getRead().equals("Y")){
                        alert.setRead("N");
                    }
                    alert.setAlert_time(entity.getAlert_time());
                    genericDAO.saveOrUpdate(alert);*/
                    String sqlUpdate = "update equipment_security_alert " +
                            "set alert_time = DATE_FORMAT('" +bean.getAlert_time() +"','%Y-%m-%d %H:%i:%s'),isread = 'N' " +
                            "where id = "+alert.getId()+";";
                    genericDAO.executeQuery(sqlUpdate);
                }
            } else {
                logger.warn("该设备故障告警时间不对(请确保相关设备的系统时间正确),不能入库!["+bean.toJsonString()+"]");
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

}
