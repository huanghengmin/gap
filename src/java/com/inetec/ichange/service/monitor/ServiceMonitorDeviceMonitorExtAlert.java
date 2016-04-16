package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.monitor.databean.DeviceDataBean;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentAlert;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * To change this template use File | Settings | File Templates.
 *  读取源端传到目标端的设备告警阀值
 */
public class ServiceMonitorDeviceMonitorExtAlert implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorDeviceMonitorExtAlert.class);

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
        String linkType = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceLinkType, "");
        String id = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceid, "");
        String status = "200";
        if (!linkType.equalsIgnoreCase("")) {
            //TODO 从内存 读取源端传到目标端的设备告警阀值
            EquipmentAlert alert = (EquipmentAlert) SnmpMonitorService.dataset.beanset4.get(id);
            dataAttributes.setResultData(new Base64().encode(alert.toJsonString().getBytes()));
        } else
            status = "503";
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
