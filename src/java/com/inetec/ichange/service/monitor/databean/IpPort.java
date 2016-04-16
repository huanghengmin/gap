package com.inetec.ichange.service.monitor.databean;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-27
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public class IpPort {
    String ip;
    int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ip+":"+port;
    }
}
