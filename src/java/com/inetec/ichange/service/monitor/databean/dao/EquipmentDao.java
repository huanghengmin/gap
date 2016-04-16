package com.inetec.ichange.service.monitor.databean.dao;

import com.avdy.p4j.jdbc.service.GenericDAO;
import com.avdy.p4j.jdbc.service.GenericDaoImpl;
import com.avdy.p4j.jdbc.transfer.TransferUtil;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.utils.DaoService;
import com.inetec.ichange.service.monitor.utils.Pagination;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class EquipmentDao {
    private static Logger logger = Logger.getLogger(EquipmentDao.class);

    public EquipmentDao() {

    }

    public Pagination<Equipment> listDevice(int limit, int start) {
        Pagination<Equipment> result = null;
        try {

            TransferUtil.registerClass(Equipment.class);

            GenericDAO<Equipment> genericDAO = new GenericDaoImpl<Equipment>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(Equipment.class, "1=1", "1=1");
            result = new Pagination(genericDAO.findAll(Equipment.class, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }

    public Pagination<Equipment> listDeviceByLinkName(int limit, int start, String name) {
        Pagination<Equipment> result = null;
        try {
            String sql = "select * from equipment where id>=1 and link_name='" + name + "'";
            TransferUtil.registerClass(Equipment.class);

            GenericDAO<Equipment> genericDAO = new GenericDaoImpl<Equipment>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(Equipment.class, sql, null);
            result = new Pagination(genericDAO.findByQuery(Equipment.class, sql, start, limit), countrows);

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }


    public Equipment listDeviceByDeviceName(String name) {
       Equipment result = null;
        try {

            TransferUtil.registerClass(Equipment.class);

            GenericDAO<Equipment> genericDAO = new GenericDaoImpl<Equipment>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());

            List<Equipment> equlist=genericDAO.findByQuery(Equipment.class," select * from equipment where equipment_name='"+name+"'",1,1);
            if(equlist.size()>0){
                result=equlist.get(0);
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return result;

    }
    public void saveEquipment(Equipment bean) {
        try {
            TransferUtil.registerClass(Equipment.class);

            GenericDAO<Equipment> genericDAO = new GenericDaoImpl<Equipment>(
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

    /*
    * vpn?????
    */
    public String getVpnDeviceIp() {
        String code = "4008";
        String ip = "";
        Pagination<Equipment> agents = listDevice(3000, 1);
        if (agents != null && agents.getItems() != null) {
            Iterator<Equipment> agentit = agents.getItems().iterator();
            while (agentit.hasNext()) {
                Equipment device = agentit.next();
                if (device.getEquipment_type_code() != null && device.getEquipment_type_code().equalsIgnoreCase("1006")) {
                    ip = device.getIp();
                    return ip;
                }
            }
        }
        return "";

    }

    /**
     * ??????????
     *
     * @return
     */
    public String getTSRSDeviceIp() {
        String code = "1032";
        Pagination<Equipment> agents = listDevice(3000, 1);
        String ip = "";
        if (agents != null && agents.getItems() != null) {
            Iterator<Equipment> agentit = agents.getItems().iterator();
            while (agentit.hasNext()) {
                Equipment device = agentit.next();
                if (device.getEquipment_type_code() != null && device.getEquipment_type_code().equalsIgnoreCase("1032")) {
                    ip = device.getIp();
                    return ip;
                }
            }
        }
        return "";

    }

    public List<Equipment> list() {
        List<Equipment> result = null;
        try {
            TransferUtil.registerClass(Equipment.class);

            GenericDAO<Equipment> genericDAO = new GenericDaoImpl<Equipment>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            int countrows = genericDAO.countRows(Equipment.class, "1=1", "1=1");
            result = genericDAO.findAll(Equipment.class, 1, countrows);

        } catch (Exception e) {
            logger.error("查找设备列表失败:",e);
        }
        return result;
    }

    public static void main(String arg[])throws Exception {
        EquipmentDao dao=new EquipmentDao();
//        Equipment equipment= dao.listDeviceByDeviceName("vpn");
//        if(equipment!=null){
//           System.out.println("equ_name:"+equipment.getEquipment_name());
//        }else{
//            System.out.println("equipment is null.");
//        }
        Equipment equipment = dao.findById(1);
    }

    public Equipment findById(int equ_id) {
        Equipment equipment = null;
        try {
            TransferUtil.registerClass(Equipment.class);
            GenericDAO<Equipment> genericDAO = new GenericDaoImpl<Equipment>(
                    DaoService.getDaoService().getDataProvider()
                            .getDataFetcher());
            Object[] obj = new Object[1];
            obj[0] = equ_id;
            equipment = genericDAO.findByPrimaryKey(Equipment.class,obj);
        } catch (Exception e) {
            logger.error("查找单个设备失败",e);
        }

        return equipment;
    }
}
