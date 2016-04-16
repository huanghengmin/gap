package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.ServiceUtil;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-16
 * Time: 21:49:46
 * To change this template use File | Settings | File Templates.
 */
public class ServiceStart implements IServiceCommandProcess {
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        DataAttributes result = dataAttributes;
        String bprivate = result.getValue(ServiceUtil.STR_ChannelPrivate);
        if (bprivate == null || bprivate == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Channel Private value is null."));
        }
        //todo:????????

        if (bprivate == "true") {
            throw new Ex().set(E.E_Unknown, new Message("????????????."));

        } else {
            if (Service.m_platformConfigred && !Service.m_publicStart) {
                dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                result = Service.m_dispPlatform.disposeControl(ServiceUtil.STR_PlatFormStart, dataAttributes);
                if (result.getStatus().isSuccess()) {
                    result.setStatus(Status.S_Success);
                    Service.m_publicStart = true;
                } else
                    result.setStatus(Status.S_Faild);
            } else {
                if (Service.m_publicStart)
                    result.setStatus(Status.S_Success);
            }
        }
        return result;
    }

    public DataAttributes process(InputStream input) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public DataAttributes process(String fileName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public int getProcessgetCapabilitie() {
        return I_StreamProcess;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
