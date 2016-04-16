package com.inetec.ichange.service;

import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.OSSystemInfo;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-4
 * Time: 16:28:28
 * To change this template use File | Settings | File Templates.
 */
public class ServiceOSMonitor implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceOSMonitor.class);
    String buff = "";
    OSSystemInfo info = new OSSystemInfo();

    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        DataAttributes result = dataAttributes;
        String bprivate = result.getValue(ServiceUtil.STR_ChannelPrivate);
        if (bprivate == null || bprivate == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Channel Private value is null."));
        }
        if (bprivate == "true") {
            result.clear();
            result.putValue("Command", ServiceUtil.STR_ServiceData_OSMonitor);
            if (logger.isDebugEnabled()) {
                logger.debug("Type Status:" + new String(Service.m_typeStatus));
            }
            dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
            dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_ServiceData_OSMonitor, dataAttributes);
            if (dataAttributes.getStatus().isSuccess()) {
                String temp = null;
                try {
                    temp = new String(DataAttributes.readInputStream(dataAttributes.getResultData()));
                    temp = temp + ",";
                } catch (IOException e) {
                    //okay
                }
                result.clear();
                result.putValue("Command", ServiceUtil.STR_ServiceData_OSMonitor);
                temp = temp + info.toString(true);
                result.setResultData(temp.getBytes());
            } else {
                throw new Ex().set(E.E_OperationFailed, new Message("打开文件失败!"));
            }

        } else {
            result.clear();
            //result.putValue("Command", ServiceUtils.STR_ServiceData_OSMonitor);
            if (logger.isDebugEnabled()) {
                logger.debug("Type Status:" + new String(Service.m_typeStatus));
            }
            result.setResultData(info.toXmlData().getBytes());
            result.setStatus(Status.S_Success);
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DataAttributes process(InputStream input) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DataAttributes process(String fileName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getProcessgetCapabilitie() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}