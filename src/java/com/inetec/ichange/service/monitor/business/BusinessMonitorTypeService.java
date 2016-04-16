package com.inetec.ichange.service.monitor.business;

import com.inetec.ichange.service.monitor.databean.BusinessDataBean;
import com.inetec.ichange.service.monitor.utils.BusinessDataBeanSet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Busness ?????
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-7
 * Time: 14:43:58
 * To change this template use File | Settings | File Templates.
 */
public class BusinessMonitorTypeService extends Thread {
    private final static Logger m_log = Logger.getLogger(BusinessMonitorTypeService.class);
    private static final String auditFile = System.getProperty("ichange.home")+"/audit/business/audit.log";
    private boolean isRun = true;
    public static BusinessDataBeanSet dataset = new BusinessDataBeanSet();
    private List<IBusinessProcess> busiess = new ArrayList<IBusinessProcess>();
    private BusinessLogImp buslog = new BusinessLogImp();
    private boolean isRunDbService = false;

    public BusinessMonitorTypeService() {

    }

    public void init(BusinessDataBean bean) {
        List<BusinessDataBean> beans = new ArrayList<BusinessDataBean>();
        beans.add(bean);
        dataset.init(beans);
    }

    public boolean isRun() {
        return isRun;
    }

    public void run() {
        isRun = true;
        if (!buslog.isRun()) {
            busLogRun();
        }
        if (!isRunDbService) {
        }
        /*while (isRun) {
            try {

                businessProcessRun();
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                //okay
            }
        }*/
    }

    /**
     *
     */
    public void close() {
        isRun = false;
        busiessProcessClose();

    }

    private void businessProcessRun() throws InterruptedException {
        try {

//            buslog.processLog(buff);
        } catch (Exception e ) {
            m_log.warn("目标端 internal Read Log error.", e);
            Thread.sleep(1 * 1000);
        }
    }

    public void busLogRun() {
        new Thread(buslog).start();
    }

    private void busiessProcessClose() {
        buslog.close();
    }
}
