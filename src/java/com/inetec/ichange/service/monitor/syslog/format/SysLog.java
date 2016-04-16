package com.inetec.ichange.service.monitor.syslog.format;

import com.inetec.ichange.service.monitor.databean.entity.BusinessLog;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentLog;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: bluesky
 * Date: 12-4-5
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public class SysLog implements ILogFormat {
    private static final String S_Separator_Keys = " ";
    private static final String S_Separator_KeyValue = "=";

    private String log;
    /**
     * id
     */
    public String equid;
    /*
     *ip��ַ
     */
    public String ip;
    private String level;
    @Override
    public int getDelay() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
    public long getIn_Flux() {

        return 0;
    }

    public long getOut_Flux() {

        return 0;
    }

    public void process(String log) {
        this.log = log;
        if (validate(log)) {

            String[] keyvalues = log.split(S_Separator_Keys);
            for (int i = 0; i < keyvalues.length; i++) {
                if (keyvalues[i].startsWith("deviceid=")) {
                    equid = keyvalues[i].substring("deviceid=".length());
                }
                if (keyvalues[i].startsWith("ip=")) {
                    ip = keyvalues[i].substring("ip=".length());
                }


            }

        }
    }

    @Override
    public void process(String log, String level) {
        //To change body of implemented methods use File | Settings | File Templates.
        this.level=level;
        process(log);
    }

    @Override
    public boolean validate(String log) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BusinessLog getBussinessLog() {
        BusinessLog businessLog = new BusinessLog();

        businessLog.setLevel(level);
        return businessLog;
    }

    @Override
    public EquipmentLog getEquipmentLog() {
        EquipmentLog log = new EquipmentLog();
        log.setEquipment_name(equid);
        log.setLog_time(new Timestamp(System.currentTimeMillis()));
        log.setLog_info(this.log);
        log.setLevel(level);
        if (level.equalsIgnoreCase("warn") || level.equalsIgnoreCase("error"))
            return log;
        else
            return log;

    }

    @Override
    public Equipment getEquipment() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
