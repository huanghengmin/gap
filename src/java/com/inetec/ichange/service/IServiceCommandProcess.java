package com.inetec.ichange.service;

import com.inetec.ichange.main.api.DataAttributes;
import com.inetec.common.exception.Ex;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-8-16
 * Time: 21:22:54
 * To change this template use File | Settings | File Templates.
 */
public interface IServiceCommandProcess {
    /**
     * @param input
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input, DataAttributes dataAttributes) throws Ex;

    /**
     * @param input
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(InputStream input) throws Ex;

    /**
     * @param fileName
     * @param dataAttributes
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName, DataAttributes dataAttributes) throws Ex, IOException;

    /**
     * @param fileName
     * @throws com.inetec.common.exception.Ex
     */
    public DataAttributes process(String fileName) throws Ex;

    public static int I_FileProcess = 0;
    public static int I_StreamProcess = 1;

    public int getProcessgetCapabilitie();
}
