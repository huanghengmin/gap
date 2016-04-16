package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.utils.ServiceUtil;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:49:50
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMonitorDeviceSnmp implements IServiceCommandProcess {
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        return null;
    }

    public DataAttributes process(InputStream input) throws Ex {
        return null;
    }

    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {

        String ip = dataAttributes.getProperty(ServiceUtil.Str_Monitor_IP, "");

        String status = "200";
        if (!ip.equalsIgnoreCase(""))
            status = "200";
        else
            status = "400";
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    public DataAttributes process(String fileName) throws Ex {
        return null;
    }

    public int getProcessgetCapabilitie() {
        return 0;
    }
}
