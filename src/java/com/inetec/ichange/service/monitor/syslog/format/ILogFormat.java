package com.inetec.ichange.service.monitor.syslog.format;

import com.inetec.ichange.service.monitor.databean.entity.BusinessLog;
import com.inetec.ichange.service.monitor.databean.entity.EquipmentLog;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;


public interface ILogFormat {
    public static final String S_Deviceid = "deviceid";
    public static final String S_ip = "ip";

    public void process(String log, String level);

    public boolean validate(String log);

    public BusinessLog getBussinessLog();

    public EquipmentLog getEquipmentLog();

    public Equipment getEquipment();

    public long getIn_Flux();

    public long getOut_Flux();

    public int getDelay();

}
