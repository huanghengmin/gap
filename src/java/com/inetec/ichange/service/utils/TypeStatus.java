package com.inetec.ichange.service.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xerces.dom.DocumentImpl;
import com.inetec.common.exception.Ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-4
 * Time: 16:37:11
 * To change this template use File | Settings | File Templates.
 */
public class TypeStatus {
    private String m_name = "";
    private String m_desc = "";
    private DbInfo m_internalSourceDb;
    private HashMap m_internalTargetDb = new HashMap();
    private DbInfo m_externalSourceDb;
    private HashMap m_externalTargetDb = new HashMap();

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

    public void setInternalSourceDb(DbInfo dbinfo) {
        m_internalSourceDb = dbinfo;
    }

    public void setInternalTargetDb(DbInfo dbinfo) {

        m_internalTargetDb.put(dbinfo.getName(), dbinfo);

    }

    public void setExternalSourceDb(DbInfo dbinfo) {
        m_externalSourceDb = dbinfo;
    }

    public void setExternalTargetDb(DbInfo dbinfo) {
        m_externalTargetDb.put(dbinfo.getName(), dbinfo);
    }

    public DbInfo getInternalSourceDb() {
        return m_internalSourceDb;
    }

    public void setTypeCodeAndInfo(String code, String info) {
        if (m_internalSourceDb != null) {
            m_internalSourceDb.setCode(code);
            if (info != null)
                m_internalSourceDb.setInfo(info);
        }
        if (m_externalSourceDb != null) {
            m_externalSourceDb.setCode(code);
            if (info != null)
                m_externalSourceDb.setInfo(info);
        }
        if (!m_internalTargetDb.isEmpty()) {
            HashMap map = new HashMap();
            for (Iterator i = m_internalTargetDb.values().iterator(); i.hasNext();) {
                DbInfo dbinfo = (DbInfo) i.next();
                dbinfo.setCode(code);
                if (info != null)
                    dbinfo.setInfo(info);
                map.put(dbinfo.getName(), dbinfo);
            }
            m_internalTargetDb = map;
        }
        if (!m_externalTargetDb.isEmpty()) {
            HashMap map = new HashMap();
            for (Iterator i = m_externalTargetDb.values().iterator(); i.hasNext();) {
                DbInfo dbinfo = (DbInfo) i.next();
                dbinfo.setCode(code);
                if (info != null)
                    dbinfo.setInfo(info);
                map.put(dbinfo.getName(), dbinfo);
            }
        }
    }

    public void setExternalCodeAndInfo(DbInfo dbInfo, boolean source) {
        if (source) {
            if (m_externalSourceDb != null) {
                if (dbInfo.getName() != null) {
                    m_externalSourceDb = dbInfo;
                } else {
                    if (dbInfo.getCode() != null)
                        m_externalSourceDb.setCode(dbInfo.getCode());
                    if (dbInfo.getInfo() != null)
                        m_externalSourceDb.setInfo(dbInfo.getInfo());
                    if (dbInfo.getDesc() != null)
                        m_externalSourceDb.setDesc(dbInfo.getDesc());
                }
            }
        } else {
            if (dbInfo.getName() != null) {
                m_externalTargetDb.put(dbInfo.getName(), dbInfo);
            } else {
                for (Iterator i = m_externalTargetDb.values().iterator(); i.hasNext();) {
                    DbInfo dbinfo = (DbInfo) i.next();
                    if (dbInfo.getCode() != null)
                        dbinfo.setCode(dbInfo.getCode());
                    if (dbInfo.getInfo() != null)
                        dbinfo.setInfo(dbInfo.getInfo());
                    if (dbInfo.getDesc() != null)
                        dbinfo.setDesc(dbInfo.getDesc());
                    m_externalTargetDb.put(dbinfo.getName(), dbinfo);
                }
            }
        }
    }

    public void setInternalCodeAndInfo(DbInfo dbInfo, boolean source) {
        if (source) {
            if (m_internalSourceDb != null) {
                if (dbInfo.getName() != null) {
                    m_internalSourceDb = dbInfo;
                } else {
                    if (dbInfo.getCode() != null)
                        m_internalSourceDb.setCode(dbInfo.getCode());
                    if (dbInfo.getInfo() != null)
                        m_internalSourceDb.setInfo(dbInfo.getInfo());
                    if (dbInfo.getDesc() != null)
                        m_internalSourceDb.setDesc(dbInfo.getDesc());
                }
            }
        } else {
            if (dbInfo.getName() != null) {
                m_internalTargetDb.put(dbInfo.getName(), dbInfo);
            } else {
                for (Iterator i = m_internalTargetDb.values().iterator(); i.hasNext();) {
                    DbInfo dbinfo = (DbInfo) i.next();
                    if (dbInfo.getCode() != null)
                        dbinfo.setCode(dbInfo.getCode());
                    if (dbInfo.getInfo() != null)
                        dbinfo.setInfo(dbInfo.getInfo());
                    if (dbInfo.getDesc() != null)
                        dbinfo.setDesc(dbInfo.getDesc());
                    m_internalTargetDb.put(dbinfo.getName(), dbinfo);
                }
            }
        }
    }

    public DbInfo[] getInternalTargetDbS() {
        return (DbInfo[]) m_internalTargetDb.values().toArray(new DbInfo[0]);
    }

    public DbInfo getExternalSourceDb() {
        return m_externalSourceDb;
    }

    public DbInfo[] getExternalTargetDbS() {
        return (DbInfo[]) m_externalTargetDb.values().toArray(new DbInfo[0]);
    }

    public Element toElement(Document doc) throws Ex {
        Element type = doc.createElement("type");
        type.setAttribute("value", m_name);
        type.setAttribute("desc", m_desc);
        if (m_externalSourceDb != null || !m_externalTargetDb.isEmpty()) {
            Element external = doc.createElement("external");
            if (m_externalSourceDb != null) {
                Element externalSource = doc.createElement("source");
                Element externalSourceDb = doc.createElement("source_db");
                externalSourceDb.setAttribute("value", m_externalSourceDb.getName());
                externalSourceDb.setAttribute("desc", m_externalSourceDb.getDesc());
                Element externalSourceDbCode = doc.createElement("source_status");
                externalSourceDbCode.appendChild(doc.createCDATASection(String.valueOf(m_externalSourceDb.getCode())));
                Element externalSourceDbInfo = doc.createElement("source_info");
                externalSourceDbInfo.appendChild(doc.createCDATASection(m_externalSourceDb.getInfo()));
                externalSource.appendChild(externalSourceDb);
                externalSource.appendChild(externalSourceDbCode);
                externalSource.appendChild(externalSourceDbInfo);
                external.appendChild(externalSource);
            }
            if (!m_externalTargetDb.isEmpty()) {
                Element externalTargets = doc.createElement("targets");
                DbInfo[] dbarray = getExternalTargetDbS();
                for (int i = 0; i < dbarray.length; i++) {
                    DbInfo dbinfo = (DbInfo) dbarray[i];
                    Element externalTarget = doc.createElement("target");
                    Element externalTargetDb = doc.createElement("target_db");
                    externalTargetDb.setAttribute("value", dbinfo.getName());
                    externalTargetDb.setAttribute("desc", dbinfo.getDesc());
                    Element externalTargetDbCode = doc.createElement("target_status");
                    externalTargetDbCode.appendChild(doc.createCDATASection(String.valueOf(dbinfo.getCode())));
                    Element externalTargetDbInfo = doc.createElement("target_info");
                    externalTargetDbInfo.appendChild(doc.createCDATASection(dbinfo.getInfo()));
                    externalTarget.appendChild(externalTargetDb);
                    externalTarget.appendChild(externalTargetDbCode);
                    externalTarget.appendChild(externalTargetDbInfo);
                    externalTargets.appendChild(externalTarget);
                }
                external.appendChild(externalTargets);

            }
            type.appendChild(external);
        }
        if (m_internalSourceDb != null || !m_internalTargetDb.isEmpty()) {
            Element internal = doc.createElement("internal");
            if (m_internalSourceDb != null) {
                Element internalSource = doc.createElement("source");
                Element internalSourceDb = doc.createElement("source_db");
                internalSourceDb.setAttribute("value", m_internalSourceDb.getName());
                internalSourceDb.setAttribute("desc", m_internalSourceDb.getDesc());
                Element internalSourceDbCode = doc.createElement("source_status");
                internalSourceDbCode.appendChild(doc.createCDATASection(m_internalSourceDb.getCode()));
                Element internalSourceDbInfo = doc.createElement("source_info");
                internalSourceDbInfo.appendChild(doc.createCDATASection(m_internalSourceDb.getInfo()));
                internalSource.appendChild(internalSourceDb);
                internalSource.appendChild(internalSourceDbCode);
                internalSource.appendChild(internalSourceDbInfo);
                internal.appendChild(internalSource);
            }
            if (!m_internalTargetDb.isEmpty()) {
                Element internalTargets = doc.createElement("targets");
                DbInfo[] dbarray = getInternalTargetDbS();
                for (int i = 0; i < dbarray.length; i++) {
                    DbInfo dbinfo = (DbInfo) dbarray[i];
                    Element internalTarget = doc.createElement("target");
                    Element internalTargetDb = doc.createElement("target_db");
                    internalTargetDb.setAttribute("value", dbinfo.getName());
                    internalTargetDb.setAttribute("desc", dbinfo.getDesc());
                    Element internalTargetDbCode = doc.createElement("target_status");
                    internalTargetDbCode.appendChild(doc.createCDATASection(dbinfo.getCode()));
                    Element internalTargetDbInfo = doc.createElement("target_info");
                    internalTargetDbInfo.appendChild(doc.createCDATASection(dbinfo.getInfo()));
                    internalTarget.appendChild(internalTargetDb);
                    internalTarget.appendChild(internalTargetDbCode);
                    internalTarget.appendChild(internalTargetDbInfo);
                    internalTargets.appendChild(internalTarget);
                }
                internal.appendChild(internalTargets);

            }
            type.appendChild(internal);
        }
        return type;
    }

}
