package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.ServiceUtil;
import com.inetec.ichange.service.utils.ServiceInitThread;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-17
 * Time: 21:40:44
 * To change this template use File | Settings | File Templates.
 */
public class ServiceInit implements IServiceCommandProcess {
    /**
     * @param input
     * @param dataAttributes
     * @return
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        DataAttributes result = dataAttributes;
        String bprivate = result.getValue(ServiceUtil.STR_ChannelPrivate);
        if (bprivate == null || bprivate == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Channel Private value is null."));
        }
        //todo:????????

        if (bprivate == "true") {
            if (!Service.m_public) {
                Service.m_public = true;
                Service.m_publicConfigured = false;
                Service.m_publicStart = false;
                if (!Service.m_serInitThread.isRun()) {
                    Service.m_serInitThread = new ServiceInitThread(Service.m_config, Service.m_externalFilename, true);
                    Service.m_serInitThread.start();
                }
            }

        }

        result.setStatus(Status.S_Success);
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param input
     * @return
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param fileName
     * @param dataAttributes
     * @return
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param fileName
     * @return
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getProcessgetCapabilitie() {
        return I_StreamProcess;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static final String STR_ChannelUrl = "ChannelUrl";

}
