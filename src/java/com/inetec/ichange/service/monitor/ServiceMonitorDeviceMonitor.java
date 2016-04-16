package com.inetec.ichange.service.monitor;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.ichange.main.api.Status;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.Service;
import com.inetec.ichange.service.monitor.databean.*;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentAlertDao;
import com.inetec.ichange.service.monitor.databean.dao.EquipmentDao;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentAlert;
import com.inetec.ichange.service.monitor.snmp.SnmpMonitorService;
import com.inetec.ichange.service.utils.ServiceUtil;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-5
 * Time: 9:56:48
 * 设备运行监控
 */
public class ServiceMonitorDeviceMonitor implements IServiceCommandProcess {
    private static Logger logger = Logger.getLogger(ServiceMonitorDeviceMonitor.class);


    /**
     * @param input
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param input
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input) throws Ex {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param fileName
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        String ip = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceip, "");
        String id = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceid, "");
        String linkType = dataAttributes.getProperty(ServiceUtil.Str_Monitor_deviceLinkType, "");

        String status = "200";
        if (!ip.equalsIgnoreCase("") && !id.equalsIgnoreCase("")&&!linkType.equalsIgnoreCase("")) {
            DeviceDataBean bean = SnmpMonitorService.dataset.getDeviceDataBean(id);
            if (bean != null && bean.getStatus() == DeviceDataBean.I_Status_OK) {
                boolean isPing = (Boolean)Service.pingTelnetService.ping.get(bean.getEqu_id());
                boolean isTelnet = (Boolean)Service.pingTelnetService.telnet.get(bean.getEqu_id());
                dataAttributes.setResultData((",ping:'"+(isPing==true?"200":"500")+"',telnet:'"+(isTelnet==true?"200":"500")+"'").getBytes());
                status = "200";
            } else {
                status = "503";
            }
        } else {
            status = "503";
        }
        dataAttributes.setProperty(ServiceUtil.Str_ResponseProcessStatus, status);
        dataAttributes.setStatus(Status.S_Success);
        return dataAttributes;
    }



    private String setDeviceAlert(DeviceDataBean bean) {
        EquipmentAlertDao dao = new EquipmentAlertDao();
        EquipmentAlert alert = dao.findById((long)bean.equ_id);
        alert.setEquipment_name("ext_"+alert.getEquipment_name());
        byte[] buf = alert.toJsonString().getBytes();
        return new BASE64Encoder().encode(buf);
    }

    private String setDeviceRunInfo(DeviceDataBean bean) {
        byte[] buf = bean.toJsonString().getBytes();
        return new BASE64Encoder().encode(buf);
    }

    private String setDeviceInfo(DeviceDataBean bean, String status) {
        EquipmentDao dao = new EquipmentDao();
        Equipment equ = dao.findById(bean.getEqu_id());
        byte[] buf = toJsonString(equ, status).getBytes();
        return new BASE64Encoder().encode(buf);
    }

    private String toJsonString(Equipment equ, String status) {
        StringBuffer buff = new StringBuffer();
        buff.append("{equ_id:'");
        buff.append(equ.getId());
        buff.append("',status:");
        buff.append(status);
        buff.append(",equ_name:'");
        buff.append("ext_"+equ.getEquipment_name());
        buff.append("',equ_type_code:'");
        buff.append(equ.getEquipment_type_code());
        buff.append("',equ_sys_config:'");
        buff.append("',equ_manager_depart:'");
        buff.append(equ.getEquipment_manager_depart());
        buff.append("',monitor_used:");
        buff.append(equ.getMonitor_used());
        buff.append(",key_device:'");
        buff.append(equ.getKey_device());
        buff.append("',ip:'");
        buff.append(equ.getIp());
        buff.append("',other_ip:'");
        buff.append(equ.getOther_ip());
        buff.append("',mac:'");
        buff.append(equ.getMac());
        buff.append("',sub_net_mask:'");
        buff.append(equ.getSub_net_mask());
        buff.append("',link_name:'");
        buff.append(equ.getLink_name());
        buff.append("',link_type:'");
        buff.append(equ.getLink_type());
        buff.append("',port:'");
        buff.append(equ.getPort());
        buff.append("'}");
        return buff.toString();
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
