package com.inetec.ichange.service.monitor.utils;

import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.service.IServiceCommandProcess;
import com.inetec.ichange.service.utils.ServiceUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-17
 * Time: 20:32:25
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMonitorFactory {
    public static Properties props = null;

    public static String getValueByKey(String key) throws Ex {
        if (props == null) {
            props = new Properties();
            try {
                props.load(ServiceMonitorFactory.class.getResourceAsStream("/monitor.properties"));
            } catch (IOException e) {
                throw new Ex().set(E.E_OperationFailed, e, new Message("monitor.properties not load."));
            }
        }
        return props.getProperty(key);
    }

    public static IServiceCommandProcess createServiceCommon(String common) throws Ex {
        IServiceCommandProcess object = null;
        if (common == null || common == "") {
            throw new Ex().set(E.E_InvalidArgument, new Message("Commond is null."));
        }
        if (props == null) {
            props = new Properties();
            try {
                props.load(ServiceMonitorFactory.class.getResourceAsStream("/monitor.properties"));
            } catch (IOException e) {
                throw new Ex().set(E.E_OperationFailed, e, new Message("{0} monitor.properties not load.", common));
            }
        }
        common = common.toLowerCase();
        String classname = null;
        classname = props.getProperty(common, "");
        if (classname != null && !classname.equalsIgnoreCase("")) {
            //object = (IServiceCommandProcess) m_map.get(STR_ServiceCommonClassName_Audit);
            if (object == null) {
                object = (IServiceCommandProcess) ServiceUtil.newObjectByClass(classname, IServiceCommandProcess.class);
                // m_map.put(STR_ServiceCommonClassName_Audit, object);
            }
        }
        if (object == null) {
            throw new Ex().set(E.E_OperationFailed, new Message("{0} Common is undefined.", common));
        }

        return object;

    }
}
