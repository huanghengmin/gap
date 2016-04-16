package com.inetec.ichange.service.utils;

import com.inetec.ichange.service.Service;
import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-5-24
 * Time: 21:21:37
 * To change this template use File | Settings | File Templates.
 */
public class ReStartTimerTask extends Thread {
    private Logger m_log = Logger.getLogger(ReStartTimerTask.class);
    private String datetime;
    private long sleeptime;

    public ReStartTimerTask(String date) {
        datetime = date;

    }

    public void init() throws Ex {
        sleeptime = getDateTime(datetime);
    }

    public void run() {
        try {
            Thread.sleep(sleeptime);
            Restart();
        } catch (Ex ex) {
            m_log.warn("?????????????????????.", ex);
        } catch (InterruptedException e) {
            m_log.warn("?????????????????????.", e);
        }
    }

    public void Restart() throws Ex {
        DataAttributes dataAttributes = new DataAttributes();
        dataAttributes.putValue(ServiceUtil.STR_CommandBody, ServiceUtil.STR_CommandBoday_Private);
        dataAttributes = Service.m_dispService.disposeControl(ServiceUtil.STR_ServiceData_ReStart, dataAttributes);
        if (dataAttributes.getStatus().isSuccess()) {
            new StartCommandProcessThread().start();
        } else {
            throw new Ex().set(E.E_OperationFailed, new Message("??????????????????."));
        }
    }


    /**
     * ??????????,???????????.
     *
     * @param date ???????????.h:1 ?1С?, d:12h???????12??.w:3:12 ??????3??12??. m:3:12 ???3??12С?.
     * @return long
     */
    public long getDateTime(String date) throws Ex {
        long result = 0;
        if (date == null || date.equals("") || date.length() < 3) {
            throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12h???????12??.w:3:12 ??????3??12??,m:3:12 ???3??12С?"));
        }
        String formatClass = date.substring(0, 1);
        // ?NС?
        if (formatClass.equals("h") || formatClass.equals("H")) {
            int number = 0;
            try {
                number = Integer.parseInt(date.substring(2));
            } catch (NumberFormatException e) {
                throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12h???????12??.w:3:12 ??????3??12??,m:3:12 ???3??12С?"));
            }
            result = number * 60 * 60 * 1000;
        }
        // ???N??.
        if (formatClass.equals("d") || formatClass.equals("D")) {
            result = getHours(date.substring(2, date.length() - 1));

        }
        // ??????M??.
        if (formatClass.equals("w") || formatClass.equals("W")) {
            int number = 0;
            String temp[] = date.split(":");
            if (temp.length < 3) {
                throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12???????12??.w:3:12 ??????3??12??,m:3:12 ???3??12С?"));
            }
            try {
                number = Integer.parseInt(temp[1]);
            } catch (NumberFormatException e) {
                throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12???????12??.w:3:12 ??????3??12??,m:3:12 ???3??12С?"));
            }
            long currentTime = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime);
            number = number - calendar.get(Calendar.DAY_OF_WEEK);
            if (number <= 0) {
                number = number + 7;
            }
            result = number * 24 * 60 * 60 * 1000 + getHours(temp[2].substring(0, temp[2].length() - 1));
            //return result;
        }
        // ???N??M??.
        if (formatClass.equals("m") || formatClass.equals("M")) {
            int number = 0;
            String temp[] = date.split(":");
            if (temp.length < 3) {
                throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12???????12??.w:3:12 ??????3??12??,"));
            }
            try {
                number = Integer.parseInt(temp[1]);
            } catch (NumberFormatException e) {
                throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12???????12??.w:3:12 ??????3??12??,"));
            }
            long currentTime = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTime);
            number = number - calendar.get(Calendar.DAY_OF_MONTH);
            if (number <= 0) {
                number = number + 30;
            }
            result = number * 24 * 60 * 60 * 1000 + getHours(temp[2].substring(0, temp[2].length() - 1));
        }
        return result;
    }

    private long getHours(String hours) throws Ex {
        long result;
        int number = 0;
        try {
            number = Integer.parseInt(hours);
        } catch (NumberFormatException e) {
            throw new Ex().set(E.E_FormatError, new Message("???????????????????.h:1 ?1С?, d:12???????12??.w:3:12 ??????3??12??,"));
        }
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        number = number - calendar.get(Calendar.HOUR_OF_DAY);
        if (number <= 0) {
            number = number + 24;
        }
        result = number * 60 * 60 * 1000;
        return result;
    }
}
