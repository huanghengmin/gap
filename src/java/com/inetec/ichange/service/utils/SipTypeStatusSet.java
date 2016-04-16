package com.inetec.ichange.service.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xerces.dom.DocumentImpl;
import com.inetec.common.exception.Ex;
import com.inetec.common.io.IOUtils;
import com.inetec.ichange.service.utils.TypeStatus;
import com.inetec.ichange.service.utils.DomUtil;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-10-4
 * Time: 17:45:23
 * To change this template use File | Settings | File Templates.
 */
public class SipTypeStatusSet {
    public static final String XmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n";
    private ArrayList m_list = new ArrayList();
    public final static String Str_CharacterSet = "UTF-8";
    private OSSystemInfo in;
    private OSSystemInfo out;

    public int size() {
        return m_list.size();
    }

    public void clear() {
        m_list.clear();
    }

    public void setTypeStatus(SipTypeStatus type) {
        int index = m_list.indexOf(type);
        if (index != -1) {
            m_list.set(index, type);
        } else {
            m_list.add(type);
        }
    }

    public List getTypeStatus() {
        return m_list;
    }

    public SipTypeStatus getSIpTypeStatusByName(String name) {
        for (int i = 0; i < m_list.size(); i++) {
            SipTypeStatus temp = (SipTypeStatus) m_list.get(i);
            if (temp != null && temp.getM_appType().equalsIgnoreCase(name)) {
                return temp;
            }
        }
        return null;
    }

    public HashMap toMap() {
        HashMap map = new HashMap();
        for (int i = 0; i < m_list.size(); i++) {
            SipTypeStatus typeStatus = (SipTypeStatus) m_list.get(i);
            map.put(typeStatus.getM_appType(), typeStatus);
        }
        return map;
    }

    public Document toDocument() throws Ex {
        Document doc = new DocumentImpl();
        doc.appendChild(toElement(doc));
        return doc;
    }

    public Element toElement(Document doc) throws Ex {
        Element ichange = doc.createElement("ichange");
        Element types = doc.createElement("types");
        for (int i = 0; i < m_list.size(); i++) {
            SipTypeStatus type = (SipTypeStatus) m_list.get(i);
            types.appendChild(type.toElement(doc));
        }
        ichange.appendChild(types);
        Element resource = doc.createElement("resource");
        Element external = doc.createElement("external");
        Element internal = doc.createElement("internal");
        Element total_memory = doc.createElement("total_memory");
        total_memory.setTextContent(String.valueOf(out.getTotalmem()));
        external.appendChild(total_memory);
        Element use_memory = doc.createElement("used_memory");
        use_memory.setTextContent(String.valueOf(out.getUsemem()));
        external.appendChild(use_memory);
        Element used_cpu = doc.createElement("used_cpu");
        used_cpu.setTextContent(String.valueOf(new Double(out.getCpu()).intValue()));
        external.appendChild(used_cpu);
        resource.appendChild(external);
        Element intotal_memory = doc.createElement("total_memory");
        intotal_memory.setTextContent(String.valueOf(in.getTotalmem()));
        internal.appendChild(intotal_memory);
        Element inuse_memory = doc.createElement("used_memory");
        inuse_memory.setTextContent(String.valueOf(in.getUsemem()));
        internal.appendChild(inuse_memory);
        Element inused_cpu = doc.createElement("used_cpu");
        inused_cpu.setTextContent(String.valueOf(new Double(in.getCpu()).intValue()));
        internal.appendChild(inused_cpu);
        resource.appendChild(internal);
        ichange.appendChild(resource);

        return ichange;
    }

    public ByteArrayInputStream getDataInputStream() throws Ex {
        Document doc = toDocument();
        StringBuffer result = DomUtil.read(doc);
        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(result.toString().getBytes(Str_CharacterSet));
        } catch (UnsupportedEncodingException e) {
            throw new Ex().set(e);
        }
        return is;
    }

    public byte[] getByte() throws Ex, IOException {
        Document doc = toDocument();
        StringBuffer result = DomUtil.read(doc);
        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(result.toString().getBytes(Str_CharacterSet));
        } catch (UnsupportedEncodingException e) {
            throw new Ex().set(e);
        }

        return IOUtils.readByteArray(is);
    }

    public void AddResource(boolean internal, OSSystemInfo resoruce) {
        if (internal) {
            in = resoruce;
        } else {
            out = resoruce;
        }

    }


}