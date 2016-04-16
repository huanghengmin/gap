package com.inetec.ichange.service.monitor.alarm;

import org.apache.log4j.Logger;

import java.net.SocketAddress;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2009-3-31
 * Time: 18:13:17
 * To change this template use File | Settings | File Templates.
 */
public class RtPUdpDataBean implements Comparable, Serializable {
    final static Logger logger = Logger.getLogger(RtPUdpDataBean.class);
    /**
     * 数据包id
     */
    private int id;
    /**
     * 数据包长度
     */
    private int length;
    /**
     * 数据内容
     */
    private byte[] data = new byte[0];

    public static final int code_data = 1;
    /**
     * 一级命令码,数据包回应.为了数据安全到达,必须有安全机制,采用问答的方式实现
     */
    public static final int code_data_arc = 2;
    public static final int code_data_ok = 3; //发送数据回应码-数据包已经接收完成,请发下一个数据包
    public static final int code_data_err_ind = -2; //发送数据回应-当前包接收出错,包序号出错,这个包不是我正在等待的,请给我要的包
    public static String splitStr = "\r\n\r\n";

    public int getId() {
        return id;
    }

    public RtPUdpDataBean() {

    }

    public RtPUdpDataBean(int id, int length, byte[] data) {
        this.id = id;
        this.length = length;
        this.data = data;
    }

    /**
     * 对象转换成byte.
     */
    public byte[] toBytes() {

        return data;
    }

    public String toString() {
        return new String(toBytes());
    }

    /**
     * 由数据解析出相应的对像。
     *
     * @param data
     * @return
     */
    public synchronized static RtPUdpDataBean getUdpDataBean(byte[] data) {
        if (data.length < 8) {
            return null;
        }
        RtPUdpDataBean bean = new RtPUdpDataBean();
        if (data.length > 4)
            bean.setId(bytesToUIntInt(data, 2));
        bean.setLength(data.length);
        bean.setData(data);
        return bean;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.length = data.length;
    }

    public void clear() {
        data = null;
        length = 0;
        id = 0;
    }

    public int compareTo(Object o) {
        RtPUdpDataBean s = (RtPUdpDataBean) o;
        return getId() > s.getId() ? 1 : (getId() == s.getId() ? 0 : -1);

    }

    public synchronized static java.util.ArrayList getUdpDataBeanSet(byte[] val) {
        java.util.ArrayList al = new java.util.ArrayList();
        byte[] split = splitStr.getBytes();
        int len = val.length;
        int len2 = split.length;

        int startPos = 0;
        for (int i = 0; i < len; i++) {
            int j = 0;
            if (len - i < len2) break;
            while (j < len2) {
                if (val[i + j] == split[j]) {
                    j++;
                    continue;
                } else
                    break;
            }
            if (j == len2) {
                byte[] b = new byte[i - startPos];
                System.arraycopy(val, startPos, b, 0, i - startPos);
                RtPUdpDataBean databean = RtPUdpDataBean.getUdpDataBean(b);
                if (databean != null)
                    al.add(databean);
                i += len2;
                startPos = i;
            }
        }
        if (len - startPos > 0) {
            byte[] b = new byte[len - startPos];
            System.arraycopy(val, startPos, b, 0, len - startPos);
            RtPUdpDataBean databean = RtPUdpDataBean.getUdpDataBean(b);
            if (databean != null)
                al.add(databean);
        }
        java.util.Collections.sort(al);
        return al;
    }

    /**
     * 将一个整数转换成4字节数组,注意:低位在前,高位在后.因为网络通信不能保证所有客户端都使用相同的CPU,内存存储方式可能不同
     *
     * @param val 要转换的INT值
     * @return 转换后的字节数组
     */
    public static byte[] int32ToBytes(int val) {
        byte[] ret = new byte[4];
        /*for(int i=0;i<4;i++){
              int tmp = val>>>(24-i*8);
              ret[i] = (byte)(tmp&0xff);
          }*/
        int hi = val / (256 * 256);
        int low = val % (256 * 256);
        ret[3] = (byte) (hi / 256);
        ret[2] = (byte) (hi % 256);
        ret[1] = (byte) (low / 256);
        ret[0] = (byte) (low % 256);
        return ret;
    }

    /**
     * 将一个4字节数组转换成一个INT数值,按低位在前,高位在后组织.函数本身不判断字节数组大小,不做安全检查
     *
     * @param val 要转换的字节数组
     * @return 转换后的INT数值
     */
    public static int bytesToInt32(byte[] val) {
        int ret = 0;
        ret = ((val[0] & 0xff) + (val[1] & 0xff) * 256) + ((val[2] & 0xff) + (val[3] & 0xff) * 256) * 256;
        /*for(int i=0;i<4;i++)
          {
              ret = ret << 8;
              ret |= val[i]&0xff;
          }*/
        return ret;
    }

    /**
     * 判断整个包的完整性,有效性
     *
     * @param val 整个包数据
     * @return 有效-TRUE,无效-FALSE
     */
    public static boolean isValidData(byte[] val) {
        boolean ret = false;
        int len = 0;
        if (val == null) return false;
        if ((val[0] & 0xff) == 0xAA) {
            switch (val[1] & 0xff) {
                case code_data_arc:
                case code_data:
                    byte[] tmp = new byte[4];
                    System.arraycopy(val, 2, tmp, 0, 4);
                    len = bytesToInt32(tmp);
                    if ((val[0] & 0xff) == 0xAA && (val[6 + len] & 0xff) == 0xFF)
                        ret = true;
                    break;
            }
        }
        return ret;
    }

    public static int bytesToUIntInt(byte[] bytes, int index) {
        int accum = 0;
        int i = 1;
        for (int shiftBy = 0; shiftBy < 16; shiftBy += 8) {
            accum |= ((long) (bytes[index + i] & 0xff)) << shiftBy;
            i--;
        }
        return accum;
    }
}