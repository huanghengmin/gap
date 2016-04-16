package com.inetec.ichange.main.api;

import com.inetec.common.exception.Ex;
import com.inetec.common.logs.LogHelper;
import com.inetec.common.db.stp.datasource.DatabaseSource;

import java.io.InputStream;

/**
 * 平台接口，是供输入适配器调用的接口。用户不需要实现。
 * 平台内部会实现这个接口，并且，把实例化过的对象传入到适配器中,
 *
 * @author inetec System
 * @version 1.0
 */
public interface IChangeMain {
    /**
     * 启动命令; 启动一个类型的操作。
     */
    public static final String Str_ControlStart = "start";

    /**
     * 停止命令; 停止一个类型的操作。
     */
    public static final String Str_ControlStop = "stop";


    public static final String Str_ControlGetStatus = "getstatus";


    /**
     * 发送控制请求到平台。这样控制请求通常是通过输入与输出适配器发出的。
     *
     * @param type    这个对象通常是由平台新建的一个对象。
     * @param command 可以用的命令: start, stop, connect, disconnect, controlwrite, or getstatus; or
     *                other 用户自行定义的命令。
     * @param fp      可以是没有属性。这些属性在执行命令之有有效。
     * @throws com.inetec.common.exception.Ex 当有错误发生时。
     */
    DataAttributes control(IChangeType type, String command, DataAttributes fp) throws Ex;

    /**
     * 输入适配器向平台发送数据的方法。（数据流）
     *
     * @param type  这个对象通常是由平台新建的一个对象
     * @param is    数据流对象。
     * @param props 可能出现的值;没有属性，有一些相应的属性。
     * @throws com.inetec.common.exception.Ex 当有错误发生时
     */
    DataAttributes dispose(IChangeType type, InputStream is, DataAttributes props) throws Ex;

    /**
     * 输入适配器向平台发送数据的方法.(字节数组)
     *
     * @param type  这个对象通常是由平台新建的一个对象
     * @param date  字节数组
     * @param props 可能出现的值;没有属性，有一些相应的属性。
     * @throws com.inetec.common.exception.Ex 当有错误发生时
     */
    DataAttributes dispose(IChangeType type, byte[] date, DataAttributes props) throws Ex;

    /**
     * 设置应用的状态
     * @param type     应用名称
     * @param stauts   应用状态
     * @param info     状态描述
     * @param source   是否为源
     * @throws com.inetec.common.exception.Ex
     */
    void setStatus(IChangeType type, EStatus stauts, String info, boolean source) throws Ex;

    /**
     * 取平台应用状态
     * @return
     * @throws com.inetec.common.exception.Ex
     */
    public TypeStatusSet getStatus()throws Ex ;

    /**
     * 日志处理对象
     *
     * @return LogHelper
     */
    public LogHelper createLogHelper();

    /**
     * 查找数据源
     * @param dbSourceName
     * @return  DatabasesSource.
     */
    public DatabaseSource findDataSource(String dbSourceName)throws Ex;

    /**
     *取得网络状态.
     * @return   boolean  Object
     */
    public boolean isNetWorkOkay();
}
