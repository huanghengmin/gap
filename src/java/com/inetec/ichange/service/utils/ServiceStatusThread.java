package com.inetec.ichange.service.utils;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.service.Service;
import com.inetec.common.exception.Ex;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-6
 * Time: 18:17:09
 * To change this template use File | Settings | File Templates.
 */
public class ServiceStatusThread extends Thread {
    private Logger logger = Logger.getLogger(ServiceStatusThread.class);
    private boolean m_bPrivated = false;
    private static final int Int_SleepTime = 60 * 1000;
    private TypeStatusSet m_typeset = new TypeStatusSet();

    public ServiceStatusThread(boolean privated) {
        m_bPrivated = privated;

    }

    public void run() {
        boolean isRun = true;
        if (!m_bPrivated) {
            isRun = false;
        }
        while (isRun) {
            try {
                if (m_bPrivated) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("private platform started:" + Service.m_bPrivateStart);

                    }
                    if (!Service.m_bPrivateStart) {
                        sleep(Int_SleepTime);
                        continue;
                    }
                    DataAttributes dataAttributes = new DataAttributes();
                    dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                    dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_TypeStatus);
                    dataAttributes = Service.m_dispPlatform.disposeControl(ServiceUtil.STR_ServiceData_TypeStatus, dataAttributes);
                    if (dataAttributes.getStatus().isSuccess()) {
                        XmlSaxParser xmlParser = new XmlSaxParser();
                        m_typeset = xmlParser.parse(dataAttributes.getResultData());

                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("��ȡ����״̬�ɹ�");
                    }
                    DataAttributes dataAttributes1 = new DataAttributes();
                    dataAttributes1.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                    dataAttributes1.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_TypeStatus);
                    dataAttributes1 = Service.m_dispService.disposeDataPost(m_typeset.getDataInputStream(), dataAttributes1);
                    if (dataAttributes1.getStatus().isSuccess()) {
                        InputStream im = dataAttributes1.getResultData();
                        if (im != null) {
                            XmlSaxParser xmlParser = new XmlSaxParser();
                            TypeStatusSet typeset = xmlParser.parse(im);
                            if (logger.isDebugEnabled()) {
                                logger.debug("��ȡ����״̬�ɹ�");
                            }
                            mergerTypeStatus(typeset);
                            if (logger.isDebugEnabled()) {
                                logger.debug("�ϲ�������״̬�ɹ�");
                            }

                        }


                    }

                    Service.m_typeStatus = DataAttributes.readInputStream(m_typeset.getDataInputStream());
                    if (logger.isDebugEnabled()) {
                        logger.debug("type status:" + new String(Service.m_typeStatus));
                    }
                }
                sleep(Int_SleepTime);
            } catch (Ex ex) {
                Service.s_log.warn("��ȡ������ƽ̨״̬����.", ex);
            } catch (InterruptedException e) {
                //okay
            } catch (IOException e) {
                Service.s_log.warn("��ȡ������ƽ̨״̬����.", e);
            }
        }
    }

    private void mergerTypeStatus(TypeStatusSet set) {
        TypeStatusSet typeset = new TypeStatusSet();
        HashMap imap = m_typeset.toMap();
        HashMap emap = set.toMap();
        List iarray = m_typeset.getTypeStatus();
        List earray = set.getTypeStatus();
        if (imap.size() >= emap.size()) {
            for (int i = 0; i < iarray.size(); i++) {
                TypeStatus type = (TypeStatus) iarray.get(i);
                TypeStatus type2 = (TypeStatus) emap.get(type.getName());
                if (type2 != null) {
                    if (type2.getExternalSourceDb() != null) {
                        type.setExternalSourceDb(type2.getExternalSourceDb());
                    }
                    DbInfo[] dbInfoArray = type2.getExternalTargetDbS();
                    for (int j = 0; j < dbInfoArray.length; j++) {
                        type.setExternalTargetDb(dbInfoArray[j]);
                    }

                }
                typeset.setTypeStatus(type);
            }
        } else {
            for (int i = 0; i < earray.size(); i++) {
                TypeStatus type = (TypeStatus) earray.get(i);
                TypeStatus type2 = (TypeStatus) imap.get(type.getName());
                if (type2 != null) {
                    if (type2.getInternalSourceDb() != null) {
                        type.setInternalSourceDb(type2.getInternalSourceDb());
                    }
                    DbInfo[] dbInfoArray = type2.getInternalTargetDbS();
                    for (int j = 0; j < dbInfoArray.length; j++) {
                        type.setInternalTargetDb(dbInfoArray[j]);
                    }

                }
                typeset.setTypeStatus(type);
            }
        }

        m_typeset = typeset;
    }

}
