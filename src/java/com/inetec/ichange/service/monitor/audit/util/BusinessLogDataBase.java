package com.inetec.ichange.service.monitor.audit.util;

import cn.collin.commons.utils.DateUtils;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.alarm.AlarmService;
import com.inetec.ichange.service.monitor.databean.AlertDataBean;
import com.inetec.ichange.service.monitor.databean.entity.BusinessLog;
import com.inetec.ichange.service.monitor.databean.dao.BusinessLogDao;
import com.inetec.ichange.service.monitor.databean.entity.BusinessSecurityAlert;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import net.sf.json.JSONException;
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
public class BusinessLogDataBase {
    private static final Logger logger = Logger.getLogger(BusinessLogDataBase.class);
    private static final String auditReceivePath = System.getProperty("ichange.home")+"/upload/audit/business";
    private static final String auditReceiveBackPath = System.getProperty("ichange.home")+"/upload/audit/business/back";


    public void putIntoDatabase(byte[] bytes) {
        long time = System.currentTimeMillis();
        String fileName = auditReceivePath + "/audit_"+time+".log";
        String backFile = auditReceiveBackPath + "/audit_"+time+".log";
        File file = new File(fileName);
        boolean isSuccess = uploadFile(bytes,fileName);
        if(isSuccess){
            List<BusinessLog> beans = makeList(file);
            logger.info("业务审计日志保存到数据库,开始..."+beans.size());
            BusinessLogDao dao = new BusinessLogDao();
            boolean isOk = dao.saveBusinessLogs(beans);
            logger.info("业务审计日志保存到数据库...");
            if(!isOk){
                try {
                    FileUtils.copyFile(file,new File(backFile));
                } catch (IOException e) {
                    logger.error("目标端备份源端业务日志入库审计文件出错!",e);
                }
            }
            file.delete();
        }else {
            logger.warn("审计业务未能写入目标文件中");
        }
    }

    public boolean uploadFile( byte[] bytes, String fileName) {
        FileOutputStream out = null ;
        try {
            out = new FileOutputStream(fileName);
            out.write(bytes);
            out.flush();
            return true;
        } catch (FileNotFoundException e) {
            logger.error("找不到文件"+fileName);
        } catch (IOException e) {
            logger.error("I/O流操作错误",e);
        } finally {
            if (out != null ) {
                try {
                    out.close();
                } catch (IOException e)   {
                    logger.error("I/O流关闭错误",e);
                }
            }
        }
        return false;
    }

    private List<BusinessLog> makeList(File file) {
        List<BusinessLog> beans = new ArrayList<BusinessLog>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(file),"GBK"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                JSONObject json = null;
                try{
                    json = JSONObject.fromObject(tempString);
                } catch (JSONException e) {
                    logger.error("json字符串解析错误,不是json数据",e);
                }
                if(json!=null){
                    String level = json.getString("level");
                    if(level.equalsIgnoreCase("warn")){           //TODO 业务报警处理
                        BusinessSecurityAlert alert = new BusinessSecurityAlert();
                        alert = alert.jsonToBusinessSecurityAlert(json);
                        Service.alert.process(AlarmService.AlertType_Business, getAlertBean(alert));
                        Service.alert.process(AlarmService.AlertType_Security, getAlertBean(alert));
                    } else if(level.equalsIgnoreCase("error")){    //TODO 业务报警处理
                        BusinessSecurityAlert alert = new BusinessSecurityAlert();
                        alert = alert.jsonToBusinessSecurityAlert(json);
                        Service.alert.process(AlarmService.AlertType_Business, getAlertBean(alert));
                        Service.alert.process(AlarmService.AlertType_Security, getAlertBean(alert));
                    } else {
                        BusinessLog bean = new BusinessLog();
                        bean = bean.jsonToBusinessLogBean(json);
                        beans.add(bean);
                    }
                    SysLogSend.sysLog(tempString);   //TODO 日志发出
                } else {
                    SysLogSend.sysLog("错误的json数据 : "+tempString);   //TODO 日志发出
                }

            }
        } catch (IOException e) {
            logger.error("业务日志文件json解析",e);
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

    public AlertDataBean getAlertBean(BusinessSecurityAlert alert) {
        AlertDataBean result = new AlertDataBean();
        if (alert.getIp() != null && !alert.getIp().equalsIgnoreCase(""))
            result.setIp(alert.getIp());
        result.setName(alert.getBusiness_name());
        result.setUsername(alert.getUser_name());
        result.setAlert_time(alert.getAlert_time());
        result.setAlert_info(alert.getAlert_info());
        result.setCode(alert.getAlert_type_code());
        result.setIsread("N");
        result.setObjType("business");
        return result;
    }

    /*private static BusinessSecurityAlert jsonToBusinessSecurityAlert(JSONObject json) {
        BusinessSecurityAlert bean = new BusinessSecurityAlert();
        bean.setBusiness_name(json.getString("name"));
        bean.setAlert_info(json.getString("alert_info"));
        bean.setAlert_type_code(json.getString("alert_code"));
        bean.setIp(json.getString("ip"));
        bean.setUser_name(json.getString("user_name"));
        bean.setAlert_time(toTime(json.getString("alert_time")));
        bean.setRead("N");
        return bean;
    }*/

    /*private static BusinessLog jsonToBean(JSONObject json) {
        BusinessLog bean = new BusinessLog();
        bean.setLevel(json.getString("level"));
        bean.setBusiness_name(json.getString("name"));
        bean.setBusiness_desc(json.getString("desc"));
        bean.setBusiness_type(json.getString("type"));
        bean.setSource_ip(json.getString("sIp"));
        bean.setSource_port(json.getString("sPort"));
        bean.setSource_jdbc(json.getString("sdb"));
        bean.setDest_ip(json.getString("tIp"));
        bean.setDest_port(json.getString("tPort"));
        bean.setDest_jdbc(json.getString("tdb"));
        bean.setAudit_count(json.getInt("count"));
        bean.setLog_time(toTime(json.getString("time")));
        bean.setFile_name(json.getString("fName"));
        bean.setJson_id(json.getInt("jsonId"));
        bean.setPlugin(json.getString("plugin"));
        bean.setFlag(0);
        return bean;
    }*/

    /*private static Timestamp toTime(String time) {
        Date date = null;
        try {
            date = DateUtils.parse(time, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }*/

}
