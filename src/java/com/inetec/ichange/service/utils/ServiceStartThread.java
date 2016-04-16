package com.inetec.ichange.service.utils;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.service.Service;
import com.inetec.common.exception.Ex;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-12-12
 * Time: 21:10:04
 * To change this template use File | Settings | File Templates.
 */
public class ServiceStartThread extends Thread {
    private static final int Int_SleepTime = 1 * 1000;
    private boolean m_bPrivated = false;

    public ServiceStartThread(boolean privated) {
        m_bPrivated = privated;
    }

    public void run() {
        //testConfig();
        //startPlatform();
        boolean isRun = true;
        while (isRun) {
            try {
                sleep(Int_SleepTime);
                DataAttributes dataAttributes = new DataAttributes();
                if (m_bPrivated) {
                    if (Service.s_log.isDebugEnabled()) {
                        Service.s_log.debug("starting platform....");
                    }
                    if (!Service.m_publicStart && Service.m_publicConfigured) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Start);
                        dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_ServiceData_Start, dataAttributes);
                        if (Service.s_log.isDebugEnabled()) {
                            Service.s_log.debug("Start public platform status:" + dataAttributes.getStatus().toString());
                        }
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_publicStart = true;

                        }
                    }
                    if (!Service.m_bPrivateStart && Service.m_publicConfigured && Service.m_platformConfigred) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_PlatFormStart);
                        dataAttributes = Service.m_dispPlatform.disposeControl(ServiceUtil.STR_PlatFormStart, dataAttributes);
                        if (Service.s_log.isDebugEnabled()) {
                            Service.s_log.debug("Start private platform status:" + dataAttributes.getStatus().toString());
                        }
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_bPrivateStart = true;

                        }
                    }
                    if (Service.m_bPrivateStart && Service.m_publicStart) {
                        isRun = false;
                    }
                }
            } catch (Ex ex) {
                Service.s_log.warn("����������ƽ̨����.", ex);
            } catch (InterruptedException e) {
                //okay
            }
        }
    }

    private void testConfig() {
        boolean isRun = true;
        while (isRun) {
            try {
                sleep(Int_SleepTime);
                DataAttributes dataAttributes = new DataAttributes();

                if (m_bPrivated) {
                    if (!Service.m_publicConfigured || !Service.m_platformConfigred) {
                        continue;
                    }
                    if (Service.m_public && !Service.m_publicConfigured) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Config);
                        dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_Test_Config, dataAttributes);
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_publicConfigured = true;
                        }
                    }
                    if (Service.m_publicConfigured && Service.m_platformConfigred) {
                        isRun = false;
                    }
                }
            } catch (Ex ex) {
                Service.s_log.warn("����������ƽ̨����.", ex);
            } catch (InterruptedException e) {
                //okay
            }
        }
    }

    private void startPlatform() {
        boolean isRun = true;
        while (isRun) {
            try {
                sleep(Int_SleepTime);
                DataAttributes dataAttributes = new DataAttributes();
                if (m_bPrivated) {
                    if (Service.s_log.isDebugEnabled()) {
                        Service.s_log.debug("starting platform....");
                    }
                    if (!Service.m_publicStart && Service.m_publicConfigured) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Start);
                        dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_ServiceData_Start, dataAttributes);
                        if (Service.s_log.isDebugEnabled()) {
                            Service.s_log.debug("Start public platform status:" + dataAttributes.getStatus().toString());
                        }
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_publicStart = true;

                        }
                    }
                    if (!Service.m_bPrivateStart && Service.m_publicConfigured) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_PlatFormStart);
                        dataAttributes = Service.m_dispPlatform.disposeControl(ServiceUtil.STR_PlatFormStart, dataAttributes);
                        if (Service.s_log.isDebugEnabled()) {
                            Service.s_log.debug("Start private platform status:" + dataAttributes.getStatus().toString());
                        }
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_bPrivateStart = true;

                        }
                    }
                    if (Service.m_bPrivateStart && Service.m_publicStart) {
                        isRun = false;
                    }
                }
            } catch (Ex ex) {
                Service.s_log.warn("����������ƽ̨����.", ex);
            } catch (InterruptedException e) {
                //okay
            }
        }
    }
}
