package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.ServiceUtil;
import com.inetec.ichange.service.utils.OSSystemInfo;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import java.io.InputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-4
 * Time: 16:28:28
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMonitor implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitor.class);
    String buff = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\n" +
            "    <ichange>\n" +
            "      <types>\n" +
            "      </types>\n" +
            "    </ichange>";

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

                } catch (IOException e) {
                    //okay
                }
                if (temp != null && !temp.equalsIgnoreCase("")) {
                    String[] outs = temp.split(";");
                    OSSystemInfo out = new OSSystemInfo();
                    out.setCpu(Double.parseDouble(outs[0].split(":")[1]));
                    out.setTotalmem(Long.parseLong(outs[1].split(":")[1]));
                    out.setUsemem(Long.parseLong(outs[2].split(":")[1]));
                    out.setFreemem(out.getTotalmem() - out.getUsemem());
                    Service.siptypeSet.AddResource(false, out);
                    OSSystemInfo in = new OSSystemInfo();
                    in.init();
                    Service.siptypeSet.AddResource(true, in);

                } else {
                    result.clear();
                    result.putValue("Command", ServiceUtil.STR_ServiceData_Monitor);
                    result.setStatus(Status.S_Faild);
                    logger.warn("Monitor all Type Status error not get Extenal OS System Info.");
                    return result;
                }

                result.clear();
                result.putValue("Command", ServiceUtil.STR_ServiceData_Monitor);
                if (logger.isDebugEnabled()) {
                    logger.debug("Type Status:" + new String(Service.m_typeStatus));
                }
                if (Service.m_typeStatus != null) {
                    result.setResultData(Service.m_typeStatus);
                    result.setStatus(Status.S_Success);
                } else {
                    result.setResultData(buff.getBytes());
                    result.setStatus(Status.S_Success);
                }
                try {
                    Service.m_typeStatus = Service.siptypeSet.getByte();
                } catch (IOException e) {
                    throw new Ex().set(E.E_IOException, new Message("��ȡӦ��״̬����."));
                }
            } else {
                throw new Ex().set(E.E_Unknown, new Message("����֧�ִ�����."));
            }

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
