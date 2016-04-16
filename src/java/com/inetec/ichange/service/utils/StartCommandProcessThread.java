package com.inetec.ichange.service.utils;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.Service;

/**
 * Created by IntelliJ IDEA.
 * User: dell
 * Date: 2005-11-13
 * Time: 23:07:11
 * To change this template use File | Settings | File Templates.
 */
public class StartCommandProcessThread extends Thread {
    public static int I_SleepTime = 10 * 1000;

    public void run() {


        try {
            sleep(I_SleepTime);
            CommandProcess.exeReStartCommand();
        } catch (Ex ex) {
            Service.s_log.error("����ƽ̨����.", ex);
        } catch (InterruptedException e) {
            //okay
        }
    }
}
