package com.hzih.gap.utils;

import com.hzih.gap.dao.EquipmentDao;
import com.hzih.gap.dao.impl.EquipmentDaoImpl;
import com.hzih.gap.domain.Equipment;
import com.hzih.gap.service.impl.MonitorServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-13
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public class DeviceInfo {
    public static String result;
    public static Double cpuuse = 30.0 ;
    public static Double memuse = 40.0;
    public static Double diskuse = 10.0;
    public static Double flow = 10.0;
    public static void init(){
        result = result.substring(result.lastIndexOf("{"));
        System.out.println(result);
        String[] results = result.split(",");
        cpuuse = Double.parseDouble(results[3].substring(results[3].indexOf(":")+1));
        memuse = Double.parseDouble(results[4].substring(results[4].indexOf(":")+1))/Double.parseDouble(results[5].substring(results[5].indexOf(":")+1))*100;
        diskuse = Double.parseDouble(results[6].substring(results[6].indexOf(":")+1))/Double.parseDouble(results[7].substring(results[7].indexOf(":")+1,results[7].indexOf("}")))*100;

    }
}
