package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.ServiceUtil;
import com.inetec.ichange.service.utils.SipTypeStatus;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-8
 * Time: 21:20:07
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMonitorGetStatus implements IServiceCommandProcess {
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        DataAttributes result = dataAttributes;
        String bprivate = result.getValue(ServiceUtil.STR_ChannelPrivate);
        if (bprivate == null || bprivate == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Channel Private value is null."));
        }
        String type = result.getValue(ServiceUtil.HDR_ChangeType);
        if (type == null || type == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Change Type value is null."));
        }
        if (bprivate == "true") {
            SipTypeStatus typestatus = Service.siptypeSet.getSIpTypeStatusByName(type);
            if (typestatus != null) {
                dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, "" + typestatus.getStatus());
            } else {
                dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, "" + -2);
            }
            result.setStatus(Status.S_Success);

        } else {
            throw new Ex().set(E.E_Unknown, new Message("����֧�ִ�����."));
        }
        return result;
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