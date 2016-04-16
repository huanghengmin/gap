package com.hzih.gap.web.action.system;

import com.hzih.gap.service.LogService;
import com.hzih.gap.service.MonitorService;
import com.hzih.gap.utils.DeviceInfo;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-3-13
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class DeviceAction extends ActionSupport {
    private static final Logger logger = Logger.getLogger(DeviceAction.class);
    private LogService logService;
    private MonitorService monitorService;
    private boolean isRun = false;
    public String start(){
        if(!isRun){
            isRun = true;
            new Thread(){
                public void run(){
                    String id = monitorService.getHostDeviceId();
                    while(true){
                        try {
                            Thread.sleep(1000*50);
                            DeviceInfo.result = monitorService.getRunEquipmentInfo(id);
                            DeviceInfo.init();
                        } catch (Exception e) {
                            logger.info(e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }.start();
        }
        return null;
    }
    public MonitorService getMonitorService() {
        return monitorService;
    }

    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
