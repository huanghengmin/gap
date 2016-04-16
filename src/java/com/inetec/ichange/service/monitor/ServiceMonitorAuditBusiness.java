package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.audit.target.AuditTargetBusinessService;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 * 目标端接收源端发送的业务审计日志
 */
public class ServiceMonitorAuditBusiness implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorAuditBusiness.class);
    public static AuditTargetBusinessService auditTargetBusinessService = new AuditTargetBusinessService();
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
        runAuditBusinessReceiveService();
        String header = dataAttributes.getProperty("header","");
        ByteArrayInputStream in = dataAttributes.getResultData();
        String status = "200";
        try {
            byte[] buff = DataAttributes.readInputStream(in);
            if(!header.equalsIgnoreCase("") && in!=null){
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setHeader(header);
                auditInfo.setByteArray(buff);
                auditTargetBusinessService.query.offer(auditInfo);
                status = "200";
            } else {
                status = "505";
                logger.error("UDP 接收不到业务审计信息");
            }
        } catch (IOException e) {
            status = "500";
            logger.error("UDP 接收的业务审计信息解析错误",e);
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    private void runAuditBusinessReceiveService() {
        if (auditTargetBusinessService.isRunning()){
            return;
        } else {
            auditTargetBusinessService.init();
            auditTargetBusinessService.start();
        }
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
