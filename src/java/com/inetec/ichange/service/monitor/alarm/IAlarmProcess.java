package com.inetec.ichange.service.monitor.alarm;

import com.inetec.ichange.service.monitor.databean.AlertDataBean;

/**
 * 报警处理服务接口。
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-7
 * Time: 16:21:12
 * To change this template use File | Settings | File Templates.
 */
public interface IAlarmProcess {

    public void init(String type, String name);

    public void process(String type, AlertDataBean bean);

    public void close();
}
