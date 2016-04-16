package com.inetec.ichange.service.utils;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.service.Service;
import com.inetec.common.exception.Ex;


/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-9-4
 * Time: 18:09:13
 * To change this template use File | Settings | File Templates.
 */
public class ServiceInitThread extends Thread {
    private String m_configName = null;
    private String m_ExconfigName = null;
    private boolean m_bPrivated = false;
    private static final int Int_SleepTime = 1 * 1000;
    private static final int Int_TaskTime = 60 * 1000;
    private boolean m_isRun = false;

    public ServiceInitThread(String configName, String exconfigName, boolean isPrivated) {
        m_ExconfigName = exconfigName;
        m_configName = configName;
        m_bPrivated = isPrivated;
        if (Service.s_log.isDebugEnabled()) {
            Service.s_log.debug("Start certificateVerify License.");
        }
    }

    public boolean isRun() {
        return m_isRun;
    }

    public void run() {
        m_isRun = true;

        while (m_isRun) {
            try {
                sleep(Int_SleepTime);
                DataAttributes dataAttributes = new DataAttributes();
                if (m_bPrivated) {
                    if (Service.m_public) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Config);
                        dataAttributes = Service.m_dispService.disposeDataPost(m_ExconfigName, dataAttributes);
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_public = false;
                            Service.m_publicConfigured = true;
                        } else {
                            if (Service.s_log.isDebugEnabled()) {
                                Service.s_log.debug("Send Public platform  config data faild.");
                            }
                        }
                    } else {
                        if (!Service.m_publicConfigured) {
                            dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
                            dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Config);
                            dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_Test_Config, dataAttributes);
                            if (dataAttributes.getStatus().isSuccess()) {
                                Service.m_publicConfigured = true;

                            } else {
                                if (Service.s_log.isDebugEnabled()) {
                                    Service.s_log.debug("Test Public platform Config faild.");
                                }
                            }
                        }
                    }
                    if (Service.m_publicConfigured && !Service.m_platformConfigred) {
                        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
                        dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Config);
                        dataAttributes = Service.m_dispPlatform.disposeDataPost(m_configName, dataAttributes);
                        if (dataAttributes.getStatus().isSuccess()) {
                            Service.m_platformConfigred = true;
                        } else {
                            if (Service.s_log.isDebugEnabled()) {
                                Service.s_log.debug("Send private platform  config data faild.");
                            }
                        }
                        //Service.m_platformConfigred = true;
                    }
                    //                                       if (Service.m_platformConfigred && Service.m_publicConfigured) {
                    if (Service.m_platformConfigred && Service.m_publicConfigured) {
                        m_isRun = false;
                        new ServiceStartThread(m_bPrivated).start();
                    }

                } else {
                    dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBody_Public);
                    dataAttributes.putValue(ServiceUtil.HDR_ServiceCommand, ServiceUtil.STR_ServiceData_Init);
                    dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_ServiceData_Init, dataAttributes);
                    if (dataAttributes.getStatus().isSuccess()) {
                        m_isRun = false;
                    }
                }

            } catch (Ex ex) {
                Service.s_log.warn("����������ƽ̨����.");
            } catch (InterruptedException e) {
                //okay
            }
        }
    }
}
