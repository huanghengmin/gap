package com.inetec.ichange.service.monitor;

import cn.collin.commons.utils.DateUtils;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.config.target.ConfigTargetService;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.syslog.SysLogSend;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * 目标端接收配置文件
 */
public class ServiceMonitorConfigReceive implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorConfigReceive.class);
    public static ConfigTargetService configTargetService = new ConfigTargetService();
    public static boolean isConfigTargetServiceRun = false;
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
        runConfigTargetService();
        String header = dataAttributes.getProperty("header","");
        ByteArrayInputStream in = dataAttributes.getResultData();
        String status = "200";
        try {
            byte[] buff = DataAttributes.readInputStream(in);
            if(!header.equalsIgnoreCase("") && in!=null){
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setHeader(header);
                auditInfo.setByteArray(buff);
                configTargetService.query.offer(auditInfo);
                status = "200";
            } else {
                status = "505";
                logger.error("UDP 接收不到配置文件信息");
            }
        } catch (IOException e) {
            logger.error("UDP 接收的配置文件信息解析错误",e);
            status = "500";
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    private void runConfigTargetService() {
        if (isConfigTargetServiceRun){
            return;
        }
        configTargetService.init();
        configTargetService.start();
        isConfigTargetServiceRun = true;

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
