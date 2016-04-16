package com.inetec.ichange.service.monitor.databean;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-31
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public class UdpHeader {
    private static final Logger logger = Logger.getLogger(UdpHeader.class);
    /**
     * udp包编号
     */
    String id;
    /**
     * 接收包的分类处理命令
     */
    String command;
    /**
     * 传输数据类型
     */
    String type;
    /**
     * 文件名
     */
    String fileName;

    /**
     * 通用名
     */
    String stringName;
    /**
     *通用ID
     */
    String stringId;
    /**
     * 文件大小
     */
    long fileSize;
    /**
     * udp包的body实际大小
     */
    int len;

    /**
     * udp包同一包发送时间
     */
    long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getStringName() {
        return stringName;
    }

    public void setStringName(String stringName) {
        this.stringName = stringName;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String toJsonString() {

//        String header = "{id:"+id+",allSize:"+allSize+",size:"+len+"}";
        String header = "{command:'"+command+"',time:'"+time+"',type:'"+type+"',filename:'"+fileName+"',filesize:"+fileSize+",id:'"+id+"',size:"+len+",strname:'"+stringName+"',strid:'"+stringId+"'}";

        int l = header.getBytes().length;
        if(l<200){
            for(int i=0;i<200-l;i++){
                header += "=";
            }
        } else if (l>200){
            header = null;
        }
        return header;
    }

    public static byte[] addHead(UdpHeader udpHeader,byte[] buf) throws IOException {              //byte[] buf, String fileName, String id, int len
        byte[] buff = new byte[buf.length+200];
        String header = udpHeader.toJsonString();
        if(header!=null) {
            byte[] heads = header.getBytes();
            for(int i=0;i<200;i++){
                buff[i] = heads[i];
            }
            for(int i=0;i<buf.length;i++){
                buff[i+heads.length] = buf[i];
            }
        } else {
            logger.error("UDP包头部大于200个字节,需要重新处理(升级)...");
        }
        return buff;
    }

    public static String getCommand(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getString("command");
    }

    public static String getFileName(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getString("filename");

    }
    public static String getStringName(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getString("strname");
    }
    public static String getStringId(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getString("strid");
    }

    public static long getFileSize(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getLong("filesize");
    }

    public static int getSize(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getInt("size");
    }

    public static String getType(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getString("type");
    }

    public static long getId(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return json.getLong("id");
    }

    public static long getTime(String header) {
        JSONObject json = JSONObject.fromObject(header);
        return Long.parseLong(json.getString("time"));
    }
}
