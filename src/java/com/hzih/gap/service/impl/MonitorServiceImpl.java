package com.hzih.gap.service.impl;

import cn.collin.commons.domain.PageResult;
import com.hzih.gap.dao.DepartmentDao;
import com.hzih.gap.dao.EquipmentDao;
import com.hzih.gap.dao.EquipmentTypeDao;
import com.hzih.gap.dao.XmlOperatorDAO;
import com.hzih.gap.dao.impl.XmlOperatorDAOImpl;
import com.hzih.gap.domain.Department;
import com.hzih.gap.domain.Equipment;
import com.hzih.gap.domain.EquipmentType;
import com.hzih.gap.service.MonitorService;
import com.hzih.gap.utils.ServiceResponse;
import com.hzih.gap.utils.ServiceUtil;
import com.hzih.gap.utils.StringContext;
import com.hzih.gap.utils.StringUtils;
import com.inetec.common.config.stp.nodes.Type;
import net.sf.json.JSONObject;
import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-7
 * Time: 上午9:52
 * To change this template use File | Settings | File Templates.
 */
public class MonitorServiceImpl implements MonitorService {
    private final static Logger logger = Logger.getLogger(MonitorServiceImpl.class);
    private EquipmentDao equipmentDao;
    private EquipmentTypeDao equipmentTypeDao;
    private DepartmentDao departmentDao;
    private XmlOperatorDAO xmlOperatorDAO = new XmlOperatorDAOImpl();

    public String selectBusiness(int start, int limit) throws Exception {
        List<Type>  list = xmlOperatorDAO.readTypes(StringContext.INTERNAL);
        int total = list.size();
        String json = "{success:true,total:"+total+",rows:[";
        int index = 0;
        int count = 0;
        for (Type type : list){
            if(index == start && count < limit){
                String businessName = type.getTypeName();
                String businessType = type.getAppType();
                boolean isActive = type.isActive();
                long recordCount = 0;
                Float dataVolume = null;
                int linkCount = 0;
                int alertCount = 0;
                int responseTime = 0;
                int runStatus = 503;
                try {
                    if(isActive){
                        // 从接口获取实时信息
                        String[][] params = new String[][] {
                                { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                                { "Command", "businessmonitor" },
                                { "businessname", businessName } };
                        ServiceResponse response = ServiceUtil.callService(params);
                        if (response != null && response.getData() != null&&response.getCode()==200) {
                            JSONObject obj = JSONObject.fromObject(""+response.getData());
//                            logger.info("实时信息:"+response.getData());
                            recordCount = obj.getLong("record_count");
                            dataVolume = Float.parseFloat(obj.getString("xt_dataflow"));
                            alertCount = obj.getInt("alert_count");
                            linkCount = obj.getInt("connect_count");
                            responseTime = obj.getInt("action_time");
                        }
                        runStatus = response.getCode();
                    }else {
                        recordCount = 0;
                        dataVolume = Float.valueOf("0");
                        alertCount = 0;
                        linkCount = 0;
                        responseTime = 0;
                        runStatus = 0;
                    }
                } catch (Exception e) {
                    logger.error("从接口获取业务运行信息出错", e);
                    recordCount = 0;
                    dataVolume = Float.valueOf("0");
                    alertCount = 0;
                    linkCount = 0;
                    responseTime = 0;
                    runStatus = 503;
                }
                json +="{businessName:'"+businessName+"',businessType:'"+businessType+"',runStatus:"+runStatus+
                        ",dataVolume:'"+ makeDataVolume(dataVolume) +"',alertCount:"+alertCount+
                        ",linkCount:"+linkCount+",recordCount:"+recordCount+
                        ",responseTime:"+responseTime+"},";
                count++;
                start++;
            }
            index ++;
        }
        json += "]}";
        return json;
    }

    private String makeDataVolume(Float dataVolume) {
        String data = null;
        int len = dataVolume.toString().substring(dataVolume.toString().indexOf(".")+1).length();
        if(dataVolume==null){
            data = "0";
        } else if( len<3 ||dataVolume==0){
            data = dataVolume.toString();
        } else {
            data = dataVolume.toString();
            float dataFow = Float.parseFloat(data.substring(0, data.indexOf(".") + 3));
            int three = Integer.parseInt(data.substring(data.indexOf(".") + 3, data.indexOf(".") + 4));
            if(three>=5){
                dataFow += (float)0.01;
            }
            data = String.valueOf(dataFow);
        }
        len = data.toString().substring(data.toString().indexOf(".")+1).length();
        if (len==0) {
            data +=".00";
        } else if (len==1) {
            data += "0";
        }
        return data;
    }

    public String selectEquipment(int start, int limit) throws Exception {
        PageResult ps = equipmentDao.listPageResult(start/limit+1,limit,"","1");
        List<Equipment> equipments = ps.getResults();
        int total = ps.getAllResultsAmount();
        String json = "{success:true,total:"+total + ",rows:[";
        if(StringUtils.getPrivated()){//内网
            try {
                String[][] params = new String[][] {
                        { "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
                        { "Command", "devicemonitorext" },
                        { "linktype", "ext" }
                };
                ServiceResponse response = ServiceUtil.callService(params);
                if (response != null && response.getData() != null) {
                    byte[] buf = new Base64().decode(response.getData());
                    json +=  new String(buf);
                }
            } catch (Exception ex) {
                logger.error("从接口获取设备运行状态出错", ex);
            }
        }

        int runStatus = 503;
        for (Equipment e : equipments) {
            try {
				String[][] params = new String[][] {
						{ "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
						{ "Command", "devicemonitor" },
						{ "linktype", e.getLinkType()},
						{ "deviceid", String.valueOf(e.getId())},
						{ "deviceip", e.getIp() }
                };
				ServiceResponse response = ServiceUtil.callService(params);
                runStatus = response.getCode();
			} catch (Exception ex) {
				logger.error("从接口获取设备运行状态出错", ex);
			}
            json += "{id:'"+e.getId()+"',equipmentName:'"+e.getEquipmentName()+ "',equipmentDesc:'"+e.getEquipmentDesc()+
                    "',linkType:'"+e.getLinkType()+"',linkName:'"+e.getLinkName()+
                    "',runStatus:"+runStatus+",monitorUsed:'"+e.getMonitorUsed()+
                    "',ip:'"+e.getIp()+"',otherIp:'"+e.getOtherIp()+"',port:'"+e.getPort()+
                    "',isKeyDevice:'"+e.getKeyDevice()+"',mac:'"+e.getMac()+
                    "',subNetMask:'"+e.getSubNetMask()+"',equipmentTypeCode:'"+e.getEquTypeCode()+
                    "',equipmentSysConfig:'"+e.getEquSysConfig()+"',equipmentManagerDepart:'"+getDepartmentName(e.getEquManagerDepart())+
                    "',equipmentTypeName:'"+getEquipmentTypeName(e.getEquTypeCode())+
                    "',oidName:'"+e.getOidName()+"',snmpVer:'"+e.getSnmpVer()+
                    "',auth:'"+e.getAuth()+"',common:'"+e.getCommon()+
                    "'},";
        }
        json += "]}";
        return json;
    }

    @Override
    public String getRunEquipmentInfo(String id) throws Exception {
        String json = "{success:true,total:1,rows:[";
        Long idx = null;
        if(id.startsWith("ext_")){
             idx = Long.valueOf(id.substring(4));
        } else {
            idx = Long.valueOf(id);
        }
        Equipment entry = (Equipment) equipmentDao.getById(idx);
        try {
            String[][] params = new String[][] {
					{ "SERVICEREQUESTTYPE", "SERVICECONTROLPOST" },
					{ "Command", "devicemonitorrun" },
					{ "deviceid", id },
					{ "deviceip", entry.getIp() }
            };
            ServiceResponse response = ServiceUtil.callService(params);
            if (response != null && response.getCode() == 200
					&& response.getData() != null) {
                JSONObject obj = JSONObject.fromObject(response.getData());
                json += "{vpnCount:"+obj.get("vpn")+",connect:"+obj.get("currentcon")+
                        ",maxConnect:"+obj.get("maxcon")+",cpu:"+obj.get("cpu")+
                        ",memory:"+obj.get("mem")+",memory_total:"+obj.get("mem_total")+
                        ",disk:"+obj.get("disk")+",disk_total:"+obj.get("disk_total")+"}";
//                json += response.getData();
            }else {
                json += "{vpnCount:30,connect:40"+
                        ",maxConnect:200,cpu:45"+
                        ",memory:2,memory_total:4"+
                        ",disk:50,disk_total:1000}";
            }
        } catch (Exception e) {
            logger.error("获取设备运行详细信息出错", e);
        }
        json += "]}";
        return json;
    }

    @Override
    public String getHostDeviceId() {
        Equipment equipment = null;
        try {
            equipment = equipmentDao.findHost();
        } catch (Exception e) {
            logger.info(e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
        return equipment.getId().toString();  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getDepartmentName(String equManagerDepartCode) {
        Department department = (Department) departmentDao.getById(equManagerDepartCode);
        return department.getName();
    }

    private String getEquipmentTypeName(String equTypeCode) {
        EquipmentType equipmentType = (EquipmentType) equipmentTypeDao.getById(equTypeCode);
        return equipmentType.getName();
    }

    public void setEquipmentDao(EquipmentDao equipmentDao) {
        this.equipmentDao = equipmentDao;
    }

    public void setEquipmentTypeDao(EquipmentTypeDao equipmentTypeDao) {
        this.equipmentTypeDao = equipmentTypeDao;
    }

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
}
