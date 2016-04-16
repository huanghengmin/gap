package com.hzih.gap.core;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-31
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */
public class IxgbeProcess implements Runnable {
    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.

        PingProcess pingProcess = new PingProcess();

        pingProcess.start();

    }
}
