package com.inetec.ichange.service.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xerces.dom.DocumentImpl;
import com.inetec.common.exception.Ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-4
 * Time: 16:37:11
 * To change this template use File | Settings | File Templates.
 */
public class SipTypeStatus {
    private String m_name = "sipchange";
    private String m_desc = "";
    private String m_appType = "sip";
    private int max_connect;
    private Map current_connect = new HashMap();
    private float total_flux;
    private int total_requests;
    private int total_errors = 0;
    private int sucess = 100;
    private String response_interval = "2";
    private int appstatus = 0;
    private int status = 0;

    public String getM_appType() {
        return m_appType;
    }

    public void setM_appType(String m_appType) {
        this.m_appType = m_appType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMax_connect() {
        return max_connect;
    }

    public void setMax_connect(int max_connect) {
        this.max_connect = max_connect;
    }

    public int getCurrentsize() {
        return current_connect.size();
    }

    public void setCurrent_connect(String id) {
        this.current_connect.put(id, id);
    }

    public void delCurrent_connect(String id) {
        this.current_connect.remove(id);
    }

    public float getTotal_flux() {
        return total_flux;
    }

    public void setTotal_flux(float total_flux) {
        this.total_flux = total_flux;
    }

    public int getTotal_requests() {
        return total_requests;
    }

    public void setTotal_requests(int total_requests) {
        this.total_requests = total_requests;
    }

    public int getTotal_errors() {
        return total_errors;
    }

    public void setTotal_errors(int total_errors) {
        this.total_errors = total_errors;
    }

    public int getAppstatus() {
        return appstatus;
    }

    public void setAppstatus(int appstatus) {
        this.appstatus = appstatus;
    }

    public String getDesc() {
        return m_desc;
    }

    public void setDesc(String m_desc) {
        this.m_desc = m_desc;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public Element toElement(Document doc) throws Ex {
        Element type = doc.createElement("type");
        type.setAttribute("value", m_name);
        type.setAttribute("desc", m_desc);
        type.setAttribute("apptype", m_appType);
        Element status = doc.createElement("status");
        status.setTextContent(String.valueOf(this.status));
        type.appendChild(status);
        Element total_errors = doc.createElement("total_errors");
        total_errors.setTextContent(String.valueOf(this.total_errors));
        type.appendChild(total_errors);
        Element max_connect = doc.createElement("max_connect");
        max_connect.setTextContent(String.valueOf(this.max_connect));
        type.appendChild(max_connect);
        Element current_connect = doc.createElement("current_connect");
        current_connect.setTextContent(String.valueOf(getCurrentsize()));
        type.appendChild(current_connect);
        Element total_flux = doc.createElement("total_flux");
        total_flux.setTextContent(String.valueOf(this.total_flux));
        type.appendChild(total_flux);
        Element total_requests = doc.createElement("total_requests");
        total_requests.setTextContent(String.valueOf(this.total_requests));
        type.appendChild(total_requests);
        Element sucess = doc.createElement("sucess");
        if (this.total_errors > 0) {
            int temp = this.total_errors / this.total_requests;
            this.sucess = 100 - temp;
            sucess.setTextContent(String.valueOf(this.sucess));
        } else
            sucess.setTextContent(String.valueOf(this.sucess));

        type.appendChild(sucess);
        Element response_interval = doc.createElement("response_interval");
        response_interval.setTextContent(this.response_interval);
        type.appendChild(response_interval);
        Element appStatus = doc.createElement("appstatus");
        appStatus.setTextContent(String.valueOf(this.appstatus));
        type.appendChild(appStatus);
        return type;
    }

}