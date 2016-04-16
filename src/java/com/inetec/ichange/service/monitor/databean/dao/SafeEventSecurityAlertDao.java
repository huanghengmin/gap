package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.SafeEventSecurityAlert;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class SafeEventSecurityAlertDao {
    private static Logger logger = Logger.getLogger(SafeEventSecurityAlertDao.class);

    public SafeEventSecurityAlertDao() {

    }

    public Pagination<SafeEventSecurityAlert> listBusinessHourReport(int limit, int start) {
        Pagination<SafeEventSecurityAlert> result = null;
        try {
            TransferUtil.registerClass(SafeEventSecurityAlert.class);

            GenericDAO<SafeEventSecurityAlert> genericDAO = new GenericDaoImpl<SafeEventSecurityAlert>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(SafeEventSecurityAlert.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(SafeEventSecurityAlert.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public void saveSafeEventSecurityAlert(SafeEventSecurityAlert bean) {
        try {
            TransferUtil.registerClass(SafeEventSecurityAlert.class);

            GenericDAO<SafeEventSecurityAlert> genericDAO = new GenericDaoImpl<SafeEventSecurityAlert>(
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
                String sql = "select * from safe_event_security_alert " +
                        "where DATE_FORMAT(alert_time, '%Y-%m-%d %H:%i:%s')=curdate() " +
                        "and alert_type_code = '" + bean.getAlert_type_code() +
                        "' and name = '" + bean.getName() +
                        "' and obj_type = '" + bean.getObj_type() +
                        "' and ip = '" + bean.getIp() +
                        "' and alert_info = '"+bean.getAlert_info()+"';";
                List<SafeEventSecurityAlert> list = genericDAO.findByQuery(SafeEventSecurityAlert.class, sql, 1, 1);
                if (list.size()==0 ){
                    genericDAO.createEntity(bean);
                } else if(list.size()==1){
                    SafeEventSecurityAlert safe = list.get(0);
                    /*if(safe.getRead().equals("Y")){
                        safe.setRead("N");
                    }
                    safe.setAlert_time(entity.getAlert_time());
                    genericDAO.saveOrUpdate(safe);*/
                    String sqlUpdate = "update safe_event_security_alert " +
                            "set alert_time = DATE_FORMAT('" +bean.getAlert_time() +"','%Y-%m-%d %H:%i:%s'),isread = 'N' " +
                            "where id = "+safe.getId()+";";
                    genericDAO.executeQuery(sqlUpdate);
                }
            }else {
                logger.warn("该安全事件告警时间不对(请确保相关设备的系统时间正确),不能入库!["+bean.toJsonString()+"]");
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

}
