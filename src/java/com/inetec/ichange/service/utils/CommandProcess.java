package com.inetec.ichange.service.utils;

import com.inetec.common.exception.Ex;
import com.inetec.common.util.OSInfo;
import com.inetec.common.util.Proc;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-24
 * Time: 21:00:55
 * To change this template use File | Settings | File Templates.
 */
public class CommandProcess {
    public static void exeReStartCommand() throws Ex {
        OSInfo osinfo = OSInfo.getOSInfo();
        if (osinfo.isWin()) {
            Proc proc = new Proc();
            proc.exec("nircmd service restart dbchange");
        }
        if (osinfo.isLinux()) {
            Proc proc = new Proc();
            proc.exec("service dbchange restart");
        }


    }

    public static void main(String arg[]) throws Exception {
        exeReStartCommand();
    }
}
