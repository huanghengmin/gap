package com.inetec.ichange.service.monitor.business;


/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-10-31
 * Time: 10:05:47
 * To change this template use File | Settings | File Templates.
 */
public interface IBusinessProcess extends Runnable{

    public void init(int id, String name, String host, String port);

    public boolean isRun();

    public void close();

}
