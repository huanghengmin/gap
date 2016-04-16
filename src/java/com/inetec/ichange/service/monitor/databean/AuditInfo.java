package com.inetec.ichange.service.monitor.databean;

import java.io.ByteArrayInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-12-4
 * Time: 下午1:44
 * To change this template use File | Settings | File Templates.
 */
public class AuditInfo {
    public String header;
    public String body;
    public byte[] byteArray;
    public ByteArrayInputStream byteArrayInputStream;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public ByteArrayInputStream getByteArrayInputStream() {
        return byteArrayInputStream;
    }

    public void setByteArrayInputStream(ByteArrayInputStream byteArrayInputStream) {
        this.byteArrayInputStream = byteArrayInputStream;
    }
}
