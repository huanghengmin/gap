package com.inetec.ichange.service.monitor.config.source;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-15
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigSourceService {

    public void init(String config, String ip, int port);

    public void send();

    public void close();
}
