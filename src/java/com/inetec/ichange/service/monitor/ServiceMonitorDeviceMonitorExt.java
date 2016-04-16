package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import com.inetec.ichange.service.monitor.syslog.format.SysLog;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 *  目标端从内存获取源端设备运行监控信息
 */
public class ServiceMonitorDeviceMonitorExt implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorDeviceMonitorExt.class);

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
        String linkType = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceLinkType, "");
        String eType = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceEType, "");
        String status = "200";
        if (!linkType.equalsIgnoreCase("")) {

            //TODO 从内存获取源端设备运行监控信息

            byte[] buf = SnmpMonitorService.dataset.getEquipmentByNameToString(eType).getBytes();
            dataAttributes.setResultData(new Base64().encode(buf));
        } else
            status = "503";
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;  //To change body of implemented methods use File | Settings | 
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
