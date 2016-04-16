package com.inetec.ichange.service.monitor.audit.util;

import cn.collin.commons.utils.DateUtils;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.alarm.AlarmService;
import com.inetec.ichange.service.monitor.databean.AlertDataBean;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentLog;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentLogDao;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-16
 * Time: 上午9:48
 * To change this template use File | Settings | File Templates.
 */
public class EquipmentLogDataBase {
    private static final Logger logger = Logger.getLogger(EquipmentLogDataBase.class);
    private static final String auditReceivePath = System.getProperty("ichange.home")+"/upload/audit/equipment";
    private static final String auditReceiveBackPath = System.getProperty("ichange.home")+"/upload/audit/equipment/back";

    public void putIntoDatabase(byte[] bytes) {
        long time = System.currentTimeMillis();
        String fileName = auditReceivePath + "/audit_"+time+".log";
        String backFile = auditReceiveBackPath + "/audit_"+time+".log";
        File file = new File(fileName);
        boolean isSuccess = uploadFile(bytes,fileName);
        if(isSuccess){
            List<EquipmentLog> beans = makeList(fileName);
            EquipmentLogDao dao = new EquipmentLogDao();
            boolean isOk = dao.saveEquipmentLog(beans);
            if(!isOk){
                try {
                    FileUtils.copyFile(file,new File(backFile));
                } catch (IOException e) {
                    logger.error("目标端备份设备日志入库审计文件出错!",e);
                }
            }
            file.delete();
        }
    }

    public boolean uploadFile( byte[] bytes, String fileName) {
        FileOutputStream out = null ;
        try {
            out = new FileOutputStream(fileName);
            try {
                out.write(bytes);
                out.flush();
            } catch (IOException e)   {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false ;
        } finally {
            if (out != null ) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private List<EquipmentLog> makeList(String fileName) {
        List<EquipmentLog> beans = new ArrayList<EquipmentLog>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(file),"GBK"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {//一次读入一行，直到读入null为文件结束
                JSONObject json = JSONObject.fromObject(tempString);//格式化成json对象
                String level = json.getString("level");
                if(level.equalsIgnoreCase("warn")){           //TODO 设备报警处理
                    Service.alert.process(AlarmService.AlertType_Equipment, getAlertBean(json));
                    Service.alert.process(AlarmService.AlertType_Security, getAlertBean(json));
                } else if(level.equalsIgnoreCase("error")){    //TODO 设备报警处理
                    Service.alert.process(AlarmService.AlertType_Equipment, getAlertBean(json));
                    Service.alert.process(AlarmService.AlertType_Security, getAlertBean(json));
                } else {
                    EquipmentLog bean = jsonToBean(json);
                    beans.add(bean);
                }
                SysLogSend.sysLog(tempString);   //TODO 日志发出

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return beans;
    }

    private AlertDataBean getAlertBean(JSONObject json ) {
        AlertDataBean alertDataBean = new AlertDataBean();
        alertDataBean.setIsread("N");
        alertDataBean.setAlert_info(json.getString("logInfo"));
        alertDataBean.setAlert_time(toTime(json.getString("time")));
        alertDataBean.setIp(json.getString("ip"));
        alertDataBean.setName(json.getString("name"));
        alertDataBean.setCode(json.getString("code"));
        alertDataBean.setObjType("equipment");
        return alertDataBean;
    }

    private EquipmentLog jsonToBean(JSONObject json) {
        EquipmentLog bean = new EquipmentLog();
        bean.setLevel(json.getString("level"));
        bean.setEquipment_name(json.getString("name"));
        bean.setLink_name("linkName");
        bean.setLog_info(json.getString("logInfo"));
        bean.setLog_time(toTime(json.getString("time")));
        return bean;
    }

    private Timestamp toTime(String time) {
        Date date = null;
        try {
            date = DateUtils.parse(time, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

}
