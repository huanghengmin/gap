package com.inetec.ichange.main.utils;

import com.inetec.ichange.main.exception.XChange;
import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.main.api.EChange;


/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-5-23
 * Time: 20:33:45
 * To change this template use File | Settings | File Templates.
 */
public class ChangeUtils {
    /**
     * Change request type: ChangeData (null), ChangeControl
     */
    public static final String HDR_ChangeRequestType = "CHANGEREQUESTTYPE";
    public static final String HDR_ChangeControlType = "CHANGECONTROLTYPE";

    /**
     * Change type
     */
    public static final String HDR_ChangeType = "CHANGETYPE";
    public static final String HDR_DataLength = "DATALENGTH";
    public static final String HDR_Compression = "COMPRESSION";
    public static final String HDR_ISProxy = "PLUGIN_ISPROXY";
    // Request types
    //public static final String STR_REQTP_ChangeData = "CHANGEDATA";
    public static final String STR_REQTP_ChangeDataPost = "CHANGEDATAPOST";
    public static final String STR_REQTP_Compressed = "Compressed";
    public static final String STR_REQTP_toCompress = "toCompress";

    public static final String STR_REQTP_ChangeControlPost = "CHANGECONTROLPOST";
    public static final String STR_REQTP_Changeproxy = "PROXY_ENABLE";


    //public static final String STR_REQTP_CHANGEControl = "CHANGECONTROL";
    public static Object newObjectByClass(String classname, Class cls) throws Ex {
        // Make a class object with the plug-in name
        Class c = null;
        try {
            //test
            // c = Class.forName(classname, true, ChangeServer.getClassLoader());
            c = Class.forName(classname, true,ChangeUtils.class.getClassLoader());
        } catch (ClassNotFoundException Ex) {
            throw new XChange().set(E.E_Unknown, new Message("Class not found:{0} ", Ex.getMessage()));
        }

        // Make sure c implements the two required interfaces:
        /*Class intf = IConfigurable.class;
        if (!intf.isAssignableFrom(c)) {
            throw new XChange().set(EChange.E_CF_InterfaceNotImplemented, new Message("The File Receiving Processing class  does not implement the required interface 'IConfigurable'.", classname));

        }*/
        if (!cls.isAssignableFrom(c)) {
            throw new XChange().set(EChange.E_CF_InterfaceNotImplemented, new Message("The class  does not implement the required interface {0}", cls.getName()));
        }
        // Now, ready to create an instance.
        Object fr = null;
        try {
            fr = c.newInstance();
        } catch (InstantiationException Ex) {
            throw new XChange().set(EChange.E_UNKNOWN, new Message("Failed to instantiate class: ", Ex.getMessage()));
        } catch (IllegalAccessException Ex) {
            throw new XChange().set(EChange.E_UNKNOWN, new Message("Failed to instantiate class; access exception: ", Ex.getMessage()));
        }

        return fr;
    }

}
