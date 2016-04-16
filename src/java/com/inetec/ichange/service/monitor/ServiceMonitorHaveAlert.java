package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.utils.ServiceUtil;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:54:27
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMonitorHaveAlert implements IServiceCommandProcess {
    /**
     * @param input
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param input
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param fileName
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        int bus = Service.alert.getAlertBus();
        int equ = Service.alert.getAlertEqu();
        int sec = Service.alert.getAlertSecurity();

        StringBuffer buff = new StringBuffer();
        buff.append("{device:'");
        buff.append(equ);
        buff.append("',business:'");
        buff.append(bus);
        buff.append("',security:'");
        buff.append(sec);
        buff.append("'}");

        String status = null;
        if(buff.toString().startsWith("{")&&buff.toString().endsWith("}")){
            dataAttributes.setResultData(buff.toString().getBytes());
            status = "200";
        } else {
            status = "500";
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;  //To change body of implemented methods use File | Settings | File Templates.
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
