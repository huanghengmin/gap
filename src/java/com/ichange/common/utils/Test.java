package com.ichange.common.utils;

import com.ichange.common.utils.OSMonitSystemInfo.NegativeCPUTime;
import com.inetec.ichange.service.utils.OSSystemInfo;

public class Test {

    /**
     * @param args
     * @throws NegativeCPUTime
     */
    public static void main(String[] args) throws NegativeCPUTime {
        while (true) {
            System.out.println(new OSSystemInfo().toString(true));
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
