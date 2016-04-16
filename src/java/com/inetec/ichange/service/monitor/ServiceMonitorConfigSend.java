package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.config.ConfigService;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 * 接收stp界面发送配置文件命令
 */
public class ServiceMonitorConfigSend implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorConfigSend.class);
    public static ConfigService configService = new ConfigService();
    public static boolean isConfigServiceRunning = false;
    /**
     * @param input
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        return null;
    }

    /**
     * @param input
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input) throws Ex {
        return null;
    }

    /**
     * @param fileName
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        String status = "200";
        try{
            runConfigService();
            logger.info("接收stp界面发送 配置文件 命令,开始发送...");
            configService.query.offer("send");
            logger.info("发送 配置文件 成功执行...");
            status = "200";
        } catch (Exception e){
            status = "503";
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    private void runConfigService(){
        if(isConfigServiceRunning){
            return;
        }
        configService.init();
        configService.start();
        isConfigServiceRunning = true;
    }

    /**
     * @param fileName
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getProcessgetCapabilitie() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
