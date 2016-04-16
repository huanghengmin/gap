package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.DeviceDataBean;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 *  重启设备运行监控信息
 */
public class ServiceMonitorRestartService implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorRestartService.class);

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
        String restartType = dataAttributes.getProperty(ServiceUtil.Str_Monitor_RestartType,"");
        String status = "200";
        if(restartType.equalsIgnoreCase("snmpmonitorservice")) {
            Service.snmpservice.restart();
            Service.pingTelnetService.restart();
        } else if(restartType.equalsIgnoreCase("initservice")) {
            //TODO 所有用到配置文件channel信息的服务都重启
//            Service.serviceUdpReceive.restart();
//            Service.deviceService.restart();
        } else {
            status = "403";
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;  //To change body of implemented methods use File | Settings | 
    }

    private String setDeviceRunInfo(DeviceDataBean bean) {
        return null;
    }

    private String setDeviceInfo(DeviceDataBean bean) {
        EquipmentDao dao = new EquipmentDao();
        Equipment equ = dao.findById(bean.getEqu_id());

        return null;
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
