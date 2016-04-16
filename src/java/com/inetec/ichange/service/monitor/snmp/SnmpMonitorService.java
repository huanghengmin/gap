package com.inetec.ichange.service.monitor.snmp;

import com.hzih.gap.dao.impl.SnmpOIDDaoImpl;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
//import com.inetec.ichange.service.monitor.databean.dao.SnmpOIDDao;
import com.inetec.ichange.service.monitor.databean.dao.SnmpOIDDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.SnmpOIDBean;
import com.inetec.ichange.service.monitor.utils.DeviceDataBeanSet;
import com.inetec.ichange.service.monitor.utils.SnmpProcessFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Snmp  Created by IntelliJ IDEA. User: bluesky Date: 2010-9-7 Time:
 * 14:43:58 To change this template use File | Settings | File Templates.
 */
public class SnmpMonitorService extends Thread {
    private static final Logger logger = Logger.getLogger(SnmpMonitorService.class);
	private boolean isRun = false;
	private boolean isInit = false;
	public static DeviceDataBeanSet dataset = new DeviceDataBeanSet();
	public List<ISnmpProcess> snmps;
	private List<SnmpOIDBean> snmpOIDS;

	public SnmpMonitorService() {

	}

	/**
	 * 初始化
	 *
	 */
	public void init() {

        EquipmentDao dao = new EquipmentDao();
        List<Equipment> beans = dao.list();
        SnmpOIDDao snmpOIDDao = new SnmpOIDDao();
        List<SnmpOIDBean> snmpoid = snmpOIDDao.list();
        if (beans!=null && beans.size()>0) {
            dataset.init(beans);
            this.snmpOIDS = snmpoid;
            snmps = new ArrayList();
            for (Equipment equ : beans) {
                try {
                    if (equ.getMonitor_used().equalsIgnoreCase("1")) {
                        SnmpOIDBean bean = getSnmpOIDByName( equ.getOidname() );
                        ISnmpProcess process = SnmpProcessFactory
                                .getSnmpProcessByVer(bean.getSnmpver(), bean.getCompany(), bean.getOidtype());

                        process.init(equ , bean);
                        snmps.add(process);
                    }
                } catch (RuntimeException e) {
                    logger.error("SNMP初始化",e);
                } catch (Exception e) {
                    logger.error("SNMP初始化",e);
                }
            }
            isInit = true;
        } else {
            isInit = false;
            logger.warn("设备不存在!");
        }

	}

	public boolean isRun() {
		return isRun;
	}

	public void run() {
		isRun = true;
        while (isRun) {
            if (isInit) {
                snmpProcessRun();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // okays
            }
        }
	}

	public void close() {
		isRun = false;
		snmpProcessClose();

	}

	public SnmpOIDBean getSnmpOIDByName(String name) {
		SnmpOIDBean result = null;
		for (int i = 0; i < snmpOIDS.size(); i++) {
			if (snmpOIDS.get(i).getName().equalsIgnoreCase(name)) {
				result = snmpOIDS.get(i);
			}
		}
		return result;
	}

	private void snmpProcessRun() {
		for (int i = 0; i < snmps.size(); i++) {
			if(!snmps.get(i).isRun()){
                new Thread(snmps.get(i)).start();
            }
		}
	}

	private void snmpProcessClose() {
		for (int i = 0; i < snmps.size(); i++) {
			if(snmps.get(i).isRun()){
                snmps.get(i).close();
            }
		}
	}

    public void restart() {
        if(Service.isRunSnmpMonitorService){
            Service.snmpservice.close();
        }
        Service.snmpservice.init();
        new Thread(Service.snmpservice).start();
        Service.isRunSnmpMonitorService = true;
    }
}
