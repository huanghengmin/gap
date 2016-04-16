package com.inetec.ichange.service.monitor.device.source;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public interface DeviceSourceService {

    public void init(String device, String deviceRun, String deviceAlert, String equName, String equId, String ip, int port);

    public void send();

    public void close();
}
