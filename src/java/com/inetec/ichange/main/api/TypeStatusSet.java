package com.inetec.ichange.main.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xerces.dom.DocumentImpl;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.utils.DomUtil;
import com.inetec.ichange.main.api.TypeStatus;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
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
public class TypeStatusSet {
    private ArrayList m_list = new ArrayList();
    public final static String Str_CharacterSet = "GB2312";

    public int size() {
        return m_list.size();
    }

    public void clear() {
        m_list.clear();
    }

    public void setTypeStatus(TypeStatus type) {
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

    public HashMap toMap() {
        HashMap map = new HashMap();
        for (int i = 0; i < m_list.size(); i++) {
            TypeStatus typeStatus = (TypeStatus) m_list.get(i);
            map.put(typeStatus.getName(), typeStatus);
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
            TypeStatus type = (TypeStatus) m_list.get(i);
            types.appendChild(type.toElement(doc));
        }
        ichange.appendChild(types);
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


}
