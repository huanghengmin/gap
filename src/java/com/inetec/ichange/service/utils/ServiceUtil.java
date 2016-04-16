package com.inetec.ichange.service.utils;

import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import com.inetec.ichange.main.api.EChange;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-15
 * Time: 20:54:33
 * To change this template use File | Settings | File Templates.
 */
public class ServiceUtil {
    /**
     * Change request type: ChangeData (null), ChangeControl
     */
    public static final int UDPPACKETSIZE = 1024;

    public static final String HDR_ServiceRequestType = "SERVICEREQUESTTYPE";
    public static final String HDR_ServiceDataType = "SERVICEDATATYPE";

    public static final String HDR_ChangeRequestType = "CHANGEREQUESTTYPE";
    public static final String STR_REQTP_ChangeControlPost = "CHANGECONTROLPOST";
    public static final String Str_MonitorCommond = "Command";

    public static final String Str_MonitorCommond_configdbsourc = "configdbsourc";
    public static final String Str_MonitorCommond_configdevice = "configdevice";
    public static final String Str_MonitorCommond_configdevicerange = "configdevicerange";
    public static final String Str_MonitorCommond_devicesnmp = "devicesnmp";
    public static final String Str_MonitorCommond_sysloglevel = "sysloglevel";
    public static final String Str_MonitorCommond_alertlevel = "alertlevel";
    public static final String Str_MonitorCommond_configprobe = "configprobe";
    public static final String Str_MonitorCommond_configplatform = "configplatform";
    public static final String Str_MonitorCommond_devicemonitor = "devicemonitor";
    public static final String Str_MonitorCommond_businessmonitor = "businessmonitor";
    public static final String Str_Monitor_businessname = "businessname";
    public static final String Str_Monitor_deviceip = "deviceip";
    public static final String Str_Monitor_deviceid = "deviceid";
    public static final String Str_Monitor_deviceLinkType = "linktype";
    public static final String Str_Monitor_deviceEType = "etype";
    public static final String Str_Monitor_IP = "ip";
    public static final String Str_Monitor_ConfigInfo = "configinfo";
    public static final String Str_Monitor_AuditInfo = "auditinfo";
    public static final String Str_Monitor_DeviceInfo = "deviceinfo";
    public static final String Str_Monitor_DeviceRunInfo = "deviceruninfo";
    public static final String Str_Monitor_DeviceAlertInfo = "devicealertinfo";
    public static final String Str_Monitor_AuditLevel = "auditlevel";
    public static final String Str_Monitor_Lower_UpLink_Types = "loweruplinktypes";
    public static final String Str_Monitor_Send_Command = "sendLowerMonitorCommand";
    public static final String Str_Monitor_PrintScreen = "printScreen";
    public static final String Str_Monitor_RevokeCert = "revokeCert";
    public static final String Str_Monitor_ShowBlock = "showBlock";
    public static final String Str_Monitor_ReleaseBlock = "releaseBlock";
    public static final String Str_Monitor_policeno = "policeno";


    /*
    *
    */
    public static final String Str_Monitor_UplinkTypes = "uplinktypes";
    public static final String Str_Monitor_PlatformId = "platformid";
    public static final String Str_Monitor_ObjectName = "objectname";
    public static final String Str_Monitor_StartDate = "startdate";
    public static final String Str_Monitor_EndDate = "enddate";
    public static final String Str_Monitor_Start = "start";
    public static final String Str_Monitor_Limit = "limit";
    /*
     * terminalListOnline
     */
    public static final String Str_Monitor_PoliceNo = "policeno";
    public static final String Str_Monitor_BeginNo = "beginno";
    public static final String Str_Monitor_EndNo = "endno";
    public static final String Str_Monitor_PageSize = "pagesize";
    public static final String Str_Monitor_Status = "status";
    public static final String Str_Monitor_IfBlock = "ifblock";
    public static final String Str_Monitor_CardType = "cardType";
    public static final String Str_Monitor_PoliceValue = "policeValue";
    public static final String Str_Monitor_PoliceKey = "policeKey";
    public static final String Str_Monitor_IdNo = "idno";
    //    public static final String Str_Monitor_IP = "policeIP";
    public static final String Str_Monitor_TimeLenth = "timelength";
    public static final String Str_Monitor_UserId = "userId";
    public static final String Str_Monitor_IdTerminal = "idTerminal";

    /*
     * webservice
     */
    public static final String Str_Monitor_Sysreginf = "Sysreginf";
    public static final String Str_Monitor_Sysabnormal = "Sysabnormal";
    public static final String Str_Monitor_Sysbizinf = "Sysbizinf";
    public static final String Str_Monitor_Sysdeviceinf = "Sysdeviceinf";
    public static final String Str_Monitor_Sysoutlinkinf = "Sysoutlinkinf";
    public static final String Str_Monitor_Sysstrategyinf = "Sysstrategyinf";
    public static final String Str_Monitor_Web_IdSystem = "idsystem";
    public static final String Str_Monitor_Web_IdTerminal = "idterminal";
    public static final String Str_Monitor_Web_Taction = "taction";
    public static final String Str_Monitor_DeviceConfig_FileName= "filename";

    /**
     * data attribute
     */
    public static final String HDR_DataLength = "DataLength";
    public static final String HDR_Compression = "COMPRESSION";
    public static final String STR_REQTP_Compressed = "Compressed";
    public static final String STR_REQTP_toCompress = "toCompress";
    public static final String HDR_ChangeType = "CHANGETYPE";
    public static final String Str_Platform = "platform";

    public static final String STR_REQTP_ServiceDataPost = "SERVICEDATAPOST";
    public static final String STR_REQTP_ServiceControlPost = "SERVICECONTROLPOST";

    // service types
    public static final String HDR_ServiceCommand = "SERVICECOMMAND";

    public static final String STR_ServiceData_TypeStatus = "getstatus";
    public static final String STR_Test_Config = "testConfig";
    public static final String STR_PlatFormStart = "start";
    public static final String STR_ServiceData_Config = "ServiceConfig";

    public static final String STR_ServiceData_Start = "ServiceStart";
    public static final String STR_ServiceData_Stop = "ServiceStop";
    public static final String STR_ServiceData_ReStart = "ServiceReStart";
    public static final String STR_ServiceData_Audit = "ServiceAudit";
    public static final String STR_ServiceData_Init = "ServiceInit";
    public static final String STR_ServiceData_Log = "ServiceLog";

    public static final String STR_ServiceData_Monitor = "ServiceMonitor";
    public static final String STR_ServiceData_OSMonitor = "ServiceOSMonitor";

    //db command
    public static final String STR_ServiceData_DbInfo = "ServiceDbInfo";
    public static final String STR_ServiceData_DbCreateFlag = "ServiceDataDbCreateFlag";
    public static final String STR_ServiceData_DbCreateTrigger = "ServiceDataDbCreateTrigger";
    public static final String STR_ServiceData_DbDeleteFlag = "ServiceDataDbDeleteFlag";
    public static final String STR_ServiceData_DbDeleteTrigger = "ServiceDataDbDeleteTrigger";
    public static final String STR_ServiceData_DbCreateSequence = "ServiceData_DbCreateSequence";
    public static final String STR_ServiceData_DbDeleteSequence = "ServiceData_DbDeleteSequence";
    public static final String STR_ServiceData_DbDeleteTempTable = "ServiceData_DbDeleteTempTable";
    public static final String STR_ServiceData_DbCreaterTempTable = "ServiceData_DbCreaterTempTable";
    public static final String STR_ServiceData_DbTestConnect = "ServiceData_DbTestConnect";

    public static final String STR_CommandBody = "CommandBody";
    public static final String STR_CommandBoday_Private = "private";
    public static final String STR_CommandBody_Public = "public";

    public static final String STR_ChannelPrivate = "ChannelPrivate";

    public static final String HDR_ChangeControlType = "CHANGECONTROLTYPE";

    //add by wxh 2009-04-12
    public static final String STR_ServiceAppStart = "ServiceAppStart";
    public static final String STR_ServiceAppStop = "ServiceAppStop";
    public static final String STR_ServiceAppPause = "ServiceAppPause";
    public static final String STR_ServiceApp_Monitor = "ServiceAppMonitor";
    public static final String STR_ServiceGetAppStart = "ServiceGetAppStart";

    public static final String Str_ResponseProcessStatus = "ResponseProcessStatus";
    public static String Str_Monitor_RestartType = "restarttype";



    public static Object newObjectByClass(String classname, Class cls) throws Ex {
        // Make a class object with the plug-in name
        Class c = null;
        try {
            c = Class.forName(classname, true, ServiceUtil.class.getClassLoader());
        } catch (ClassNotFoundException Ex) {
            throw new Ex().set(E.E_Unknown, new Message("Class not found:{0} ", Ex.getMessage()));
        }

        // Make sure c implements the two required interfaces:

        if (!cls.isAssignableFrom(c)) {
            throw new Ex().set(EChange.E_CF_InterfaceNotImplemented, new Message("The  class  does not implement the required interface {0}", cls.getName()));
        }
        // Now, ready to create an instance.
        Object fr = null;
        try {
            fr = c.newInstance();
        } catch (InstantiationException Ex) {
            throw new Ex().set(EChange.E_UNKNOWN, new Message("Failed to instantiate class: ", Ex.getMessage()));
        } catch (IllegalAccessException Ex) {
            throw new Ex().set(EChange.E_UNKNOWN, new Message("Failed to instantiate class; access exception: ", Ex.getMessage()));
        }

        return fr;
    }

}
