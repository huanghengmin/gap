package com.inetec.ichange.service.monitor.alarm;

import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.service.monitor.databean.AlertDataBean;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 报警服务
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-7
 * Time: 14:43:58
 * To change this template use File | Settings | File Templates.
 */
public class AlarmService extends Thread {
    public static final String AlertType_Business = "business";
    public static final String AlertType_Equipment = "equipment";
    public static final String AlertType_Security = "security";
    private static final String AlertProcess = "alertprocess";
    private static final String AlertClass = "class";
    private static final String keySperator = "-";
    public static int alertEqu;
    public static int alertBus;
    public static int alertSecurity;


    private final static Logger m_log = Logger.getLogger(AlarmService.class);
    private boolean isRun = true;

    private List<IAlarmProcess> alerts = new ArrayList<IAlarmProcess>();

    private boolean isInitClient;
    private boolean isRunDbService = false;
    private boolean isProcessFlag;
    private Properties props;

    public AlarmService() {

    }

    public void init() throws Ex {
        if (props == null) {
            props = new Properties();
            try {
                props.load(AlarmService.class.getResourceAsStream("/alertconfig.properties"));
            } catch (IOException e) {
                m_log.warn("Alarm init error", e);
                throw new Ex().set(E.E_OperationFailed, e, new Message("{0}alertconfig.properties not load.", ""));
            }
        }
        String alertprocess = props.getProperty(AlarmService.AlertProcess);
        String process[] = alertprocess.split(",");
        for (int i = 0; i < process.length; i++) {
            IAlarmProcess proc = AlarmProcessFactory.createAlarmProcess(process[i] + keySperator + AlarmService.AlertClass);
            proc.init(process[i], process[i]);
            alerts.add(proc);
        }

    }


    public void process(String type, AlertDataBean bean) {
        isProcessFlag = true;
//        m_log.info("AlarmService begin process.");
        if (type.equalsIgnoreCase(AlertType_Business)) {
            alertBus = alertBus + 1;
        }
        if (type.equalsIgnoreCase(AlertType_Equipment)) {
            alertEqu = alertEqu + 1;
        }
        if (type.equalsIgnoreCase(AlertType_Security)) {
            alertSecurity = alertSecurity + 1;
        }
        for (int i = 0; i < alerts.size(); i++) {
            alerts.get(i).process(type, bean);
        }
//        m_log.info("AlarmService end process.");
    }

    public int getAlertEqu() {
        int result = alertEqu;
        alertEqu = 0;
        return result;
    }

    public int getAlertBus() {
        int result = alertBus;
        alertBus = 0;
        return result;
    }

    public int getAlertSecurity() {
        int result = alertSecurity;
        alertSecurity = 0;
        return result;
    }

    public boolean isRun() {
        return isRun;
    }

    public void run() {
        isRun = true;

    }

    public void close() {
        isRun = false;
        for (int i = 0; i < alerts.size(); i++) {
            alerts.get(i).close();
        }

    }

    public static void main(String arg[]) throws Exception {
        AlarmService alarm = new AlarmService();
        alarm.init();
        while (true) {
            Thread.sleep(100);
        }

    }


}
