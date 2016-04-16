package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.business.BusinessLogImp;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 * 目标端接收平台发送的目标端的业务审计日志
 */
public class ServiceMonitorAuditBusinessPlatform implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorAuditBusinessPlatform.class);
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
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex, IOException {
        String audit = dataAttributes.getProperty(ServiceUtil.Str_Monitor_AuditInfo,"");
        String status = "200";
        if(!audit.equalsIgnoreCase("")){
            byte[] bytes = new BASE64Decoder().decodeBuffer(audit);
            BusinessLogImp buslog = new BusinessLogImp();
            buslog.processLogByte(bytes);
        } else {
            status = "503";
        }
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
