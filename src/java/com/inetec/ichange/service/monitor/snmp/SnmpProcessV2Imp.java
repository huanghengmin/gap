package com.inetec.ichange.service.monitor.snmp;

import com.inetec.ichange.service.monitor.databean.DeviceDataBean;
import com.inetec.ichange.service.monitor.databean.entity.Equipment;
import com.inetec.ichange.service.monitor.databean.entity.SnmpOIDBean;
import com.inetec.ichange.service.monitor.snmp.utils.StringNumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.SimpleOIDTextFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

public class SnmpProcessV2Imp implements ISnmpProcess {
    private static final Logger log = Logger.getLogger(SnmpProcessV2Imp.class);
    private boolean isRun = false;
    private int deviceStatus = DeviceDataBean.I_Status_OK;
    private int deviceid;
    private String devicename;
    private boolean isError = false;
    CommunityTarget target;
    PDU pdu;
    /**
     * 初始化
     */
    Address targetAddress;
    TransportMapping transport;
    Snmp snmp = new Snmp(transport);
    OID cpu = new OID();
    OID memtotal = new OID();
    OID memfree = new OID();
    OID curconnect = new OID();
    OID disktotal = new OID();
    OID diskfree = new OID();

    @Override
    public void init(Equipment equ, SnmpOIDBean snmpoidbean) {
        // TODO Auto-generated method stub
        if (snmpoidbean == null) {
            return;
        }
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);

            snmp.listen();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        targetAddress = GenericAddress.parse("udp:" + equ.getIp() + "/" + 161);

        deviceid = equ.getId();
        devicename = equ.getEquipment_name();

        cpu = initOID(cpu, snmpoidbean.getCpuuse());
        memtotal = initOID(memtotal, snmpoidbean.getMemtotal());
        memfree = initOID(memfree, snmpoidbean.getMemuse());
        curconnect = initOID(curconnect, snmpoidbean.getCurconn());
        disktotal = initOID(disktotal, snmpoidbean.getDisktotal());
        diskfree = initOID(diskfree, snmpoidbean.getDiskuse());

        target = new CommunityTarget();
        target.setCommunity(new OctetString(equ.getPassword()));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        // creating PDU
        pdu = new PDU();
        // cpu
        if (cpu.isValid()) {
            pdu.add(new VariableBinding(cpu));
        }
        if (memtotal.isValid()) {
            // memtotal
            pdu.add(new VariableBinding(memtotal));
        }
        // memfree
        if (memfree.isValid())
            pdu.add(new VariableBinding(memfree));
        // curconnect
        if (curconnect.isValid())
            pdu.add(new VariableBinding(curconnect));
        // disktotal
        if (disktotal.isValid())
            pdu.add(new VariableBinding(disktotal));

        // disk free
        if (diskfree.isValid())
            pdu.add(new VariableBinding(diskfree));

        pdu.setType(PDU.GET);

    }

    @Override
    public boolean isRun() {
        // TODO Auto-generated method stub
        return isRun;
    }

    @Override
    public void run() {
        if (isError) {
            log.error("Device name is:" + devicename
                    + " not found snmpoid data row.");
        }
        isRun = true;
        while (isRun) {
            try {
                monitorDevice();
                Thread.sleep(2 * 1000);
            } catch (Exception e) {
                deviceStatus = DeviceDataBean.I_Status_Error;
                DeviceDataBean bean = SnmpMonitorService.dataset
                        .getDeviceDataBeanByID(devicename);

                bean.setStatus(deviceStatus);
                SnmpMonitorService.dataset.returnDeviceDataBean(devicename,
                        bean);

                log.warn("Monitor Device error:(Device ip:"
                        + targetAddress.toString() + ")", e);
                try {
                    Thread.sleep(I_SleepTime);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

    }

    public void monitorDevice() throws Exception {
        // creating PDU

        // sending request

        ResponseEvent event = snmp.send(pdu, target);
        // event.getResponse());
        DeviceDataBean bean = SnmpMonitorService.dataset
                .getDeviceDataBeanByID(devicename);
        boolean iskb = false;
        boolean ismb = false;
        boolean isgb = false;

        if (event != null && event.getResponse() != null) {

            Iterator<VariableBinding> respIt = (Iterator<VariableBinding>) event.getResponse()
                    .getVariableBindings().iterator();
            while (respIt.hasNext()) {
                VariableBinding res = respIt.next();
                String value = res.getVariable().toString().toLowerCase();
                if (value.endsWith("%")) {

                    res.setVariable(new OctetString(value.substring(0, value
                            .length() - 1).trim()));

                }
                if (value.endsWith("b") || value.endsWith("B")) {
                    res.setVariable(new OctetString(value.substring(0, value
                            .length() - 1).trim()));

                }
                if (value.endsWith("kb") || value.endsWith("KB")) {
                    res.setVariable(new OctetString(value.substring(0, value
                            .length() - 2).trim()));
                    iskb = true;
                }
                if (value.endsWith("mb") || value.endsWith("MB")) {
                    res.setVariable(new OctetString(value.substring(0, value
                            .length() - 2).trim()));
                    ismb = true;
                }
                if (value.endsWith("gb") || value.endsWith("GB")) {
                    res.setVariable(new OctetString(value.substring(0, value
                            .length() - 2).trim()));
                    isgb = true;
                }
                if (res.getVariable().toString().indexOf(":") > 0) {
                    res.setVariable(new OctetString(res.getVariable().toString().split(":")[1].trim()));
                }
                if (!StringNumberUtils.isNumeric(res.getVariable().toString().trim())) {

                    log.warn("devicename:" + devicename + " "
                            + res.getOid().toString() + ":"
                            + res.getVariable().toString() + " is error.");
                    continue;
                } else {
                    if (res.getOid().equals(cpu))
                        bean.setCpu(Integer.parseInt(res.getVariable().toString()));
                    if (res.getOid().equals(memtotal)) {
                        bean.setMem_total(res.getVariable().toLong()
                                / (1000 * 1000));
                        if (isgb)
                            bean.setMem_total(res.getVariable().toLong());
                        if (ismb)
                            bean.setMem_total(res.getVariable().toLong() / (1000));
                    }

                    if (res.getOid().equals(memfree))

                        if (bean.getMem_total() == 0 && bean.getMem() <= 100) {
                            bean.setMem(Integer.parseInt(res.getVariable().toString()));
                        } else {
                            bean.setMem(res.getVariable().toLong()
                                    / (1000 * 1000));
                            if (isgb)
                                bean.setMem(res.getVariable().toLong());
                            if (ismb)
                                bean
                                        .setMem(res.getVariable().toLong() / (1000));
                        }

                    if (res.getOid().equals(curconnect))
                        if (!res.getVariable().isException()
                                && StringUtils.isNumeric(res.getVariable()
                                .toString()))

                            bean.setCurrentcon(Integer.parseInt(res
                                    .getVariable().toString()));
                        else {
                            log.warn("response is return:"
                                    + res.getVariable().toString());
                            bean.setCurrentcon(0);
                        }

                    if (res.getOid().equals(disktotal)) {
                        bean.setDisk_total(res.getVariable().toLong()
                                / (1000 * 1000));
                        if (isgb)
                            bean.setDisk_total(res.getVariable().toLong());
                        if (ismb)
                            bean
                                    .setDisk_total(res.getVariable().toLong() / (1000));
                    }
                    if (res.getOid().equals(diskfree))
                        if (bean.getDisk_total() == 0 && bean.getDisk() <= 100) {
                            bean.setDisk(res.getVariable().toInt());
                        } else {
                            bean.setDisk(res.getVariable().toLong()
                                    / (1000 * 1000));
                            if (isgb)
                                bean.setDisk(res.getVariable().toLong());
                            if (ismb)
                                bean
                                        .setDisk(res.getVariable().toLong() / (1000));
                        }

                }
                iskb = false;
                ismb = false;
                isgb = false;

            }

            deviceStatus = DeviceDataBean.I_Status_OK;
            bean.setStatus(deviceStatus);
            SnmpMonitorService.dataset.returnDeviceDataBean(devicename, bean);
            System.out.println(event.getResponse());
            log.info(event.getResponse());
            log.info(bean.toJsonString());
            System.out.println(bean.toJsonString());
        } else {
            System.out.println("Snmp()" + devicename
                    + " Device response is null.");
            log.warn("Snmp()" + devicename + " Device response is null.");
            deviceStatus = DeviceDataBean.I_Status_Error;
            bean.setStatus(deviceStatus);

        }

    }

    private OID initOID(OID oid, String soid) {
        if (soid.startsWith(".")) {
            soid = soid.substring(1);
        }
        if (soid.length() <= 2) {
            return oid;
        }
        try {
            oid.setValue(new SimpleOIDTextFormat().parse(soid));
        } catch (ParseException e) {
            log.warn("OID Format error.", e);
        }
        return oid;

    }

    public void close() {
        // TODO Auto-generated method stub
        isRun = false;

        try {
            snmp.close();
        } catch (IOException e) {
            log.warn("snmp close error:", e);
        }
    }

}
