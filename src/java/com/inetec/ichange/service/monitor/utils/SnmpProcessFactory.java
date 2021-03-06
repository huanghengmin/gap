package com.inetec.ichange.service.monitor.utils;


import com.inetec.ichange.service.monitor.snmp.HostSnmpProcessV2Imp;
import com.inetec.ichange.service.monitor.snmp.ISnmpProcess;
import com.inetec.ichange.service.monitor.snmp.SnmpProcessV2Imp;

public class SnmpProcessFactory {
	public static ISnmpProcess getSnmpProcessByVer(String ver,String product, String type) {
		ISnmpProcess result = null;
		if (ver.equalsIgnoreCase("v1")) {
//			result = new SnmpProcessV1Imp();
            result = new SnmpProcessV2Imp();
		}
		if (ver.equalsIgnoreCase("v2")) {
			if (type.equalsIgnoreCase("pcserver")) {
				result = new HostSnmpProcessV2Imp();
				return result;
			}
			if (product.equalsIgnoreCase("leadsec")&&(type.equalsIgnoreCase("firewall")||type.equalsIgnoreCase("ips"))) {
//				result = new LenovoProcessV2Imp();
                result = new SnmpProcessV2Imp();
				return result;
			}
			if(product.equalsIgnoreCase("netchina")&&(type.equalsIgnoreCase("firewall"))){
				result = new SnmpProcessV2Imp();
				return result;
				
			}
			result = new SnmpProcessV2Imp();

		}
		
		if (ver.equalsIgnoreCase("v3")) {
//			result = new SnmpProcessV3Imp();
            result = new SnmpProcessV2Imp();
		}
		if (ver.equalsIgnoreCase("trapv1")) {
//			result = new SnmpProcessV1TrapImp();
            result = new SnmpProcessV2Imp();
		}
		if (ver.equalsIgnoreCase("trapv2")) {
//			result = new SnmpProcessV2TrapImp();
            result = new SnmpProcessV2Imp();
		}
		if (ver.equalsIgnoreCase("trapv3")) {
//			result = new SnmpProcessV3TrapImp();
            result = new SnmpProcessV2Imp();
		}

		return result;

	}
}
