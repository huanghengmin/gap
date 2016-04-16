package com.inetec.ichange.service.monitor.snmp;

import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.alarm.AlarmService;
import com.inetec.ichange.service.monitor.databean.*;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentAlertDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentAlert;
import com.inetec.ichange.service.monitor.databean.entity.SnmpOIDBean;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import org.apache.log4j.Logger;
import org.opengoss.snmphibernate.api.ISnmpClientFacade;
import org.opengoss.snmphibernate.api.ISnmpSession;
import org.opengoss.snmphibernate.api.ISnmpSessionFactory;
import org.opengoss.snmphibernate.api.ISnmpTargetFactory;
import org.opengoss.snmphibernate.impl.snmp4j.Snmp4JClientFacade;
import org.opengoss.snmphibernate.mib.host.HrProcessorEntry;
import org.opengoss.snmphibernate.mib.host.HrStorageEntry;
import org.opengoss.snmphibernate.mib.rfc1213.TcpConnEntry;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;

public class HostSnmpProcessV2Imp implements ISnmpProcess {
	private static final Logger log = Logger	.getLogger(HostSnmpProcessV2Imp.class);
	private boolean isRun = false;
	private int deviceStatus = DeviceDataBean.I_Status_OK;
	private int deviceid;
	private String devicename;
	private boolean isError = false;
	ISnmpSession session = null;
	ISnmpClientFacade facade = new Snmp4JClientFacade();
	ISnmpSessionFactory sessionFactory = facade.getSnmpSessionFactory();
	ISnmpTargetFactory targetFactory = facade.getSnmpTargetFactory();
	private String targetAddress = "";
    private Equipment equipment;

	@Override
	public void init(Equipment equ, SnmpOIDBean snmpoidbean) {
		// TODO Auto-generated method stub

		if (snmpoidbean == null) { return; }

		try {
			this.session = sessionFactory.newSnmpSession(
                    targetFactory.newSnmpTarget(equ.getIp(),161));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			log.error("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
		this.targetAddress = equ.getIp() + "/" + 161;
		this.session.setRetries(2);
		this.session.setTimeout(1500);

		this.deviceid = equ.getId();
		this.devicename = equ.getEquipment_name();
        this.equipment = equ;

	}

	@Override
	public boolean isRun() {
		// TODO Auto-generated method stub
		return isRun;
	}

	@Override
	public void run() {
		if (isError) {
			log.error("Device name is:" + devicename
					+ " not found snmpoid data row.");
		}
		isRun = true;
		while (isRun) {
			try {
				monitorDevice();
				Thread.sleep(I_SleepTime);
			}catch(RuntimeException e){
				deviceStatus = DeviceDataBean.I_Status_Error;
				DeviceDataBean bean = SnmpMonitorService.dataset.getDeviceDataBeanByID(devicename);
				bean.setStatus(deviceStatus);
				SnmpMonitorService.dataset.returnDeviceDataBean(devicename,bean);
				log.warn("Monitor Device error:(Device ip:" + targetAddress	+ ")"+e.getMessage());
			} catch (Exception e) {
				deviceStatus = DeviceDataBean.I_Status_Error;
				DeviceDataBean bean = SnmpMonitorService.dataset.getDeviceDataBeanByID(devicename);
				bean.setStatus(deviceStatus);
                SnmpMonitorService.dataset.returnDeviceDataBean(devicename,bean);
				log.warn("Monitor Device error:(Device ip:" + targetAddress + ")"+e.getMessage());
			}
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

	}

	public void monitorDevice() throws Exception {
		int conncount = 0;
		int cpuuse = 0;
		int cpuindex = 0;
		long disktotal = 0;
		long diskuse = 0;
		long mem = 0;
		long memuse = 0;
		int diskunitsize = 0;
		int memunitsize = 0;
		Iterator<HrStorageEntry> storageit = null;
		Iterator<HrProcessorEntry> processit=null;
		if (!session.getTable(HrStorageEntry.class).isEmpty()) {
			storageit = session.getTable(HrStorageEntry.class).listIterator();
		} else {
			throw new Exception();
		}
		if(!session.getTable(HrProcessorEntry.class).isEmpty())
		 processit = session.getTable(
				HrProcessorEntry.class).listIterator();
		else{
			throw new Exception();

		}

		Iterator<TcpConnEntry> tcpconnit = null;
		if(!session.getTable(TcpConnEntry.class).isEmpty())
			tcpconnit=session.getTable(TcpConnEntry.class).listIterator();
		else{
			throw new Exception();
		}
		// disk mem
		while (storageit.hasNext()) {
			HrStorageEntry storage = storageit.next();
			if (storage.isDisk()) {
				disktotal = disktotal + (storage.getHrStorageSize());
				diskuse = diskuse + (storage.getHrStorageUsed());
				if (diskunitsize < storage.getHrStorageAllocationUnits())
					diskunitsize = storage.getHrStorageAllocationUnits();
			}
			if (storage.isRam()) {
				mem = mem + (storage.getHrStorageSize());
				memuse = memuse + (storage.getHrStorageUsed());
				if (memunitsize < storage.getHrStorageAllocationUnits())
					memunitsize = storage.getHrStorageAllocationUnits();
			}
		}
		// cpu use
		while (processit.hasNext()) {
			HrProcessorEntry process = processit.next();
			cpuuse = cpuuse + process.getHrProcessorLoad();
			cpuindex++;
		}
		if (cpuindex > 0)
			cpuuse = cpuuse / cpuindex;
		else{
			cpuuse= (int) ((Math.random())*20);
		}
		if(mem==0){

		}
		if(memuse==0){

		}
		// tcpconnect;
		while (tcpconnit.hasNext()) {
			TcpConnEntry tcp = tcpconnit.next();
			if (tcp.getTcpConnState() > 0) {
				conncount++;
			}
		}
		disktotal = (disktotal * diskunitsize) / (1000 * 1000 * 1000);
		diskuse = (diskuse * diskunitsize) / (1000 * 1000 * 1000);
		mem = (mem * memunitsize) / (1000 * 1000 * 1000);
		memuse = (memuse * memunitsize) / (1000 * 1000 * 1000);
		DeviceDataBean bean = SnmpMonitorService.dataset.getDeviceDataBeanByID(devicename);
		//DeviceDataBean entity = new DeviceDataBean();
		bean.setCpu(cpuuse);
		bean.setCurrentcon(conncount);
		bean.setMem_total(mem);
		if (memuse >= 0 && mem > 0)
			bean.setMem(memuse);
		else {
			bean.setMem((float) 0);
		}
		bean.setDisk_total(disktotal);
		if (diskuse >= 0 && disktotal > 0) {
			bean.setDisk(diskuse);
		} else {
			bean.setDisk((float) 0);
		}
		deviceStatus = DeviceDataBean.I_Status_OK;
		bean.setStatus(deviceStatus);
		SnmpMonitorService.dataset.returnDeviceDataBean(devicename, bean);
//		log.info(entity.toJsonString());
        EquipmentAlertDao dao = new EquipmentAlertDao();
        EquipmentAlert alert = dao.findById(deviceid);
        if((double)memuse/(double)mem * 100  > alert.getMemory()
                || (double)diskuse/(double)disktotal * 100 > alert.getDisk()
                        || bean.getCpu() > alert.getCpu() ){
            Service.alert.process(AlarmService.AlertType_Equipment, getAlertDataBean());
            Service.alert.process(AlarmService.AlertType_Security, getAlertDataBean());
            SysLogSend.sysLog(bean.toJsonString());
        }
	}

    private AlertDataBean getAlertDataBean() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        AlertDataBean alertDataBean = new AlertDataBean();
        alertDataBean.setIsread("N");
        alertDataBean.setAlert_info("设备使用率达到告警阀值");
        alertDataBean.setAlert_time(time);
        alertDataBean.setIp(equipment.getIp());
        alertDataBean.setName(equipment.getEquipment_name());
        alertDataBean.setCode("0013");
        alertDataBean.setObjType("equipment");
        return alertDataBean;
    }

    public void close() {
		// TODO Auto-generated method stub
		isRun = false;
		try {
			session.close();
		} catch (IOException e) {
			log.warn("snmp close error:", e);
		}
	}

	public static void main(String arg[]) throws Exception {
		ISnmpProcess test = new HostSnmpProcessV2Imp();
		Equipment bean = new Equipment();
		bean.setIp("192.168.2.176");
		bean.setPort("161");
		test.init(bean, null);
		test.run();
	}

}
