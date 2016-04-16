package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.device.target.DeviceTargetService;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * 目标端接收设备运行监控信息
 */
public class ServiceMonitorDeviceMonitorRunReceive implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorDeviceMonitorRunReceive.class);
    public static DeviceTargetService deviceTargetService = new DeviceTargetService();
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
        runDeviceTargetService();
        String header = dataAttributes.getProperty("header","");
        String body = dataAttributes.getProperty("body","");
        ByteArrayInputStream in = dataAttributes.getResultData();
        String status = "200";
            if(!header.equalsIgnoreCase("") && !body.equalsIgnoreCase("")){
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setHeader(header);
                auditInfo.setBody(body);
                deviceTargetService.query.offer(auditInfo);
            } else {
                status = "505";
                logger.error("UDP 接收不到设备监控信息");
            }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    private void runDeviceTargetService() {
        if(deviceTargetService.isRunning()){
            return;
        }
        deviceTargetService.init();
        deviceTargetService.start();
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
