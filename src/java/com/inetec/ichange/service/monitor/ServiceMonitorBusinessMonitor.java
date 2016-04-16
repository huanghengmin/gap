package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.business.BusinessPlatformService;
import com.inetec.ichange.service.monitor.databean.BusinessDataBean;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:18
 * To change this template use File | Settings | File Templates.
 * 业务运行监控
 */
public class ServiceMonitorBusinessMonitor implements IServiceCommandProcess {
    Logger logger = Logger.getLogger(ServiceMonitorBusinessMonitor.class);

    /**
     * @param input
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        return null;
    }

    /**
     * @param input
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input) throws Ex {
        return null;
    }

    /**
     * @param fileName
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        String busname = dataAttributes.getProperty(ServiceUtil.Str_Monitor_businessname, "");
        String status = "200";
        if (!busname.equalsIgnoreCase("")) {
            BusinessDataBean bean = BusinessPlatformService.dataset.getBusinessDataBeanByID(busname);
            if (bean != null) {
                if(bean.toJsonString().startsWith("{")&&bean.toJsonString().endsWith("}")){
                    dataAttributes.setResultData(bean.toJsonString().getBytes());
                    status ="200";
                } else {
                    status = "500";
                }
            } else {
                status = "500";
            }
        } else
            status = "400";
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    /**
     * @param fileName
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName) throws Ex {
        return null;
    }

    public int getProcessgetCapabilitie() {
        return 0;
    }
}
