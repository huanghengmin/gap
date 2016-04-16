package com.inetec.ichange.service.monitor.business;

import com.inetec.ichange.service.monitor.databean.*;
import com.inetec.ichange.service.monitor.utils.BusinessDataBeanSet;
import com.inetec.ichange.service.utils.syslog.SyslogMessage;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Busness
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-7
 * Time: 14:43:58
 * To change this template use File | Settings | File Templates.
 */
public class BusinessPlatformService extends Thread {
    private final static Logger m_log = Logger.getLogger(BusinessPlatformService.class);
    private boolean isRun = true;
    public static BusinessDataBeanSet dataset = new BusinessDataBeanSet();
    private List<BusinessMonitorTypeService> mplatforms = new ArrayList<BusinessMonitorTypeService>();
    public BusinessPlatformService() {

    }

    public void init(List<BusinessDataBean> list) {
        dataset.init(list);
        for (BusinessDataBean bean : list) {
            BusinessMonitorTypeService service = new BusinessMonitorTypeService();
            service.init(bean);
            mplatforms.add(service);
        }
    }

    public boolean isRun() {
        return isRun;
    }

    public void run() {
        isRun = true;
        while (isRun) {
            try {
                businessProcessRun();
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                //okay
            }
        }
    }

    /**
     *
     */
    public void close() {
        isRun = false;
        busiessProcessClose();

    }

    private void businessProcessRun() throws InterruptedException {
        for (int i = 0; i < mplatforms.size(); i++)
            new Thread(mplatforms.get(i)).start();
    }


    private void busiessProcessClose() {
        for (int i = 0; i < mplatforms.size(); i++)
            mplatforms.get(i).close();
    }

}
