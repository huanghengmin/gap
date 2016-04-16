package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.databean.DeviceDataBean;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 * 获取设备运行状态信息(cpu,memory,disk)
 */
public class ServiceMonitorDeviceMonitorRun implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorDeviceMonitorRun.class);

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
        String ip = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceip, "");
        String id = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceid, "");
        String status = "200";
        if (!ip.equalsIgnoreCase("") && !id.equalsIgnoreCase("")) {
            DeviceDataBean bean = SnmpMonitorService.dataset.getDeviceDataBean(id);
            if (bean != null && bean.getStatus() == DeviceDataBean.I_Status_OK) {
                dataAttributes.setResultData(bean.toJsonString().getBytes());
                status = String.valueOf(bean.getStatus());
            } else {
                status = "503";
            }
        } else
            status = "503";
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    /**
     * @param fileName
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getProcessgetCapabilitie() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
