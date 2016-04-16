package com.inetec.ichange.service.utils;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-30
 * Time: 21:04:04
 * To change this template use File | Settings | File Templates.
 */
public class DbInfo {
    private String m_name;
    private String m_desc;
    private String m_code;
    private String m_info = "";

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        this.m_name = m_name;
    }

    public String getDesc() {
        return m_desc;
    }

    public void setDesc(String m_desc) {
        this.m_desc = m_desc;
    }

    public String getInfo() {
        if (m_info == null) {
            m_info = "";
        }
        return m_info;
    }

    public void setInfo(String m_info) {
        this.m_info = m_info;
    }

    public String getCode() {
        return m_code;
    }

    public void setCode(String m_code) {
        this.m_code = m_code;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        DbInfo dbInfo = (DbInfo) obj;
        if (m_code.equals(dbInfo.getCode()) && m_name.equals(dbInfo.getName()) && m_desc.equals(dbInfo.getDesc()) && m_info.equals(dbInfo.getInfo()))
            result = true;
        else
            result = false;
        return result;

    }
}
