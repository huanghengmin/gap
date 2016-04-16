package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.ServiceUtil;
import com.inetec.ichange.service.utils.StartCommandProcessThread;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-16
 * Time: 21:50:57
 * To change this template use File | Settings | File Templates.
 */
public class ServiceReStart implements IServiceCommandProcess {
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        DataAttributes result = dataAttributes;
        String bprivate = result.getValue(ServiceUtil.STR_ChannelPrivate);
        if (bprivate == null || bprivate == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Channel Private value is null."));
        }
        if (bprivate == "true") {
            dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
            dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_ServiceData_ReStart, dataAttributes);
            if (dataAttributes.getStatus().isSuccess()) {
                new StartCommandProcessThread().start();
                result.setStatus(Status.S_Success);
            } else {
                throw new Ex().set(E.E_OperationFailed, new Message("������������ִ��ʧ��."));
            }

        } else { //todo:������������
            //todo:����ϵͳ��������TOMCAT����.
            new StartCommandProcessThread().start();
            result.setStatus(Status.S_Success);

        }
        return result;  //To change body of imple
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
