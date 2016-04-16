package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.utils.ServiceUtil;
import com.inetec.ichange.service.utils.SipTypeStatus;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;
import com.inetec.common.logs.LogHelper;

import java.io.InputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-25
 * Time: 23:44:57
 * To change this template use File | Settings | File Templates.
 */
public class ServiceLog implements IServiceCommandProcess {
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        DataAttributes result = dataAttributes;
        String bprivate = result.getValue(ServiceUtil.STR_ChannelPrivate);
        if (bprivate == null || bprivate == "") {
            throw new Ex().set(E.E_NullPointer, new Message("Channel Private value is null."));
        }
        //todo:????????

        if (bprivate == "true") {
            try {
                if (Service.s_log.isDebugEnabled()) {
                    Service.s_log.debug("Append Log.");
                }
                byte[] buff = DataAttributes.readInputStream(input);
                /* if(Service.s_log.isDebugEnabled()){
                    Service.s_log.debug("Append Log data:"+new String(buff));
                }*/


                LogHelper[] logs = LogHelper.getLogHelper(buff);

                for (int i = 0; i < logs.length; i++) {
                    String typename = logs[i].getAppName();
                    SipTypeStatus typestatus = Service.siptypeSet.getSIpTypeStatusByName(typename);
                    if (logs[i].getStatusCode().equalsIgnoreCase("0")) {
                        typestatus.setTotal_requests(typestatus.getTotal_requests() + 1);
                        typestatus.setTotal_flux(Float.parseFloat(logs[i].getFlux()));
                    } else {
                        typestatus.setTotal_errors(typestatus.getTotal_errors() + 1);
                    }
                    if (logs[i].getOperate().equalsIgnoreCase("INVITE")) {
                        typestatus.setCurrent_connect(logs[i].getDest_id());
                    }
                    if (logs[i].getOperate().equalsIgnoreCase("BYE")) {
                        typestatus.delCurrent_connect(logs[i].getDest_id());
                    }
                    Service.siptypeSet.setTypeStatus(typestatus);
                }
//                Service.logService.addLog(buff);
            } catch (IOException e) {
                throw new Ex().set(E.E_IOException, e, new Message("IOException:"));
            }
            result.setStatus(Status.S_Success);
        } else {
            dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
            dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Log);
            try {
                dataAttributes = Service.m_dispService.disposeDataPost(input, dataAttributes);
            } catch (IOException e) {
                throw new Ex().set(E.E_IOException, e, new Message("IOException:"));
            }
            if (dataAttributes.getStatus().isSuccess()) {
                result.setStatus(Status.S_Success);
            }
        }


        return result;  //To change body of implemented .
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
