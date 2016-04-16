package com.inetec.ichange.main.utils;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2004-3-22
 * Time: 14:29:58
 * To change this template use File | Settings | File Templates.
 */
public class StringProcess {

    public static boolean existSubString(String str, String separator) {
        int i = str.indexOf(separator);
        if (i != -1)
            return true;
        else
            return false;
    }

    public static String getSubString(String str, String separator) {
        int i = str.lastIndexOf(separator);
        if (i != -1)
            return str.substring(i + separator.length(), str.length());
        else
            return "";
    }

    public static String getFirstSubString(String str, String separator) {
        int i = str.lastIndexOf(separator);
        if (i != -1)
            return str.substring(0, i + 1);
        else
            return "";
    }

    public static String getEndSubString(String str, String separator) {
        int i = str.lastIndexOf(separator);
        if (i != -1) {
            return str.substring(i + 1);
        } else {
            return "";
        }

    }
}
