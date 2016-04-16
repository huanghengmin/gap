package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.common.exception.Ex;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-16
 * Time: 21:50:20
 * To change this template use File | Settings | File Templates.
 */
public class ServiceStop implements IServiceCommandProcess {

    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public DataAttributes process(InputStream input) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public DataAttributes process(String fileName) throws Ex {
        //To change body of implemented methods use File | Settings | File Templates.
        return null;
    }

    public int getProcessgetCapabilitie() {
        return I_StreamProcess;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
