package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.AuditInfo;
import com.inetec.ichange.service.monitor.device.target.DeviceTargetService;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * 界面取 通道测试结果 值
 */
public class ServiceMonitorInitReceive implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorInitReceive.class);

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
        String s1 = (String) ReceiveService.initMap.get("s1")==null?"0":(String) ReceiveService.initMap.get("s1");
        String r1 = (String) ReceiveService.initMap.get("r1")==null?"0":(String) ReceiveService.initMap.get("r1");
        String s2 = (String) ReceiveService.initMap.get("s2")==null?"0":(String) ReceiveService.initMap.get("s2");
        String r2 = (String) ReceiveService.initMap.get("r2")==null?"0":(String) ReceiveService.initMap.get("r2");
        if(!s1.equalsIgnoreCase("") && !r1.equalsIgnoreCase("")){
            String result = "{s1:'"+("0".equals(s1)?"":s1)+"',r1:'"+("0".equals(r1)?"":s1)+"',s2:'"+("0".equals(s2)?"":s1)+"',r2:'"+("0".equals(r2)?"":s1)+"'}";
            dataAttributes.setResultData(result.getBytes());
            status = "200";
        } else {
            status = "500";
            logger.error("取得通道测试消息结果的值错误");
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }

    /**
     * @param fileName
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName) throws Ex {
        return null;
    }

    public int getProcessgetCapabilitie() {
        return 0;
    }
}
