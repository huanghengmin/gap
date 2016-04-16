package com.inetec.ichange.main.utils;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-1-29
 * Time: 14:44:05
 * To change this template use File | Settings | File Templates.
 */
public class FileProcess {
    private String m_fileName = null;
    private boolean m_isDelete = false;
    private boolean m_isRecover = false;
    public static final String Str_Recover = "recover";

    public FileProcess(String fileName, boolean delete) {
        m_fileName = fileName;
        m_isDelete = delete;
    }

    public FileProcess(String fileName, boolean delete, boolean isRecover) {
        m_fileName = fileName;
        m_isDelete = delete;
        m_isRecover = isRecover;
    }

    public String getFileName() {
        return m_fileName;
    }

    public boolean isDelete() {
        return m_isDelete;
    }

    public boolean isRecover() {
        return m_isRecover;
    }
}
