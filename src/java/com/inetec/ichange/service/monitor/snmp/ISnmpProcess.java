package com.inetec.ichange.service.monitor.snmp;

import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.SnmpOIDBean;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-10-31
 * Time: 10:05:47
 * To change this template use File | Settings | File Templates.
 */
public interface ISnmpProcess extends Runnable {
	public static final int I_SleepTime=10*1000;

	 public void init(Equipment equ, SnmpOIDBean snmpoidbean);
	 public boolean isRun();
	 public void close();

}
