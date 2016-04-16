package com.inetec.ichange.main.utils;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-12-12
 * Time: 21:26:58
 * To change this template use File | Settings | File Templates.
 */
public class FreeMemoryThread extends Thread {
    private long  m_sleepTime =0;
    public FreeMemoryThread(long time) {
        m_sleepTime = time;

    }
    public void run() {
        try {
            sleep(m_sleepTime);
            System.gc();
        } catch (InterruptedException e) {
            //okay
        }
    }
}