package com.inetec.ichange.service.monitor.alarm;

import JACE.ASX.MessageQueue;

import java.net.*;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;

import com.inetec.common.exception.Ex;
import com.inetec.common.exception.E;
import com.inetec.common.i18n.Message;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2009-3-31
 * Time: 17:20:12
 * To change this template use File | Settings | File Templates.
 */
public class DatagramSendThread extends Thread {
    final static Logger logger = Logger.getLogger(DatagramSendThread.class);
    DatagramSocket fSendSocket;
    private MessageQueue mQueue;
    private boolean isRun = true;
    private int INTERVAL = 3;
    InetSocketAddress clientadress;
    DatagramPacket sendpacket;
    private float flow = 0f;
    private long beginTime;
    private long sendcount;
    private int sendError;

    /**
     * 返回 当前已经传输了多少MB数据。(单位MB(兆字节))。
     *
     * @return
     */
    public float getFlow() {
        return flow;
    }


    public synchronized void addUdpBeanPacket(byte[] data) throws Ex {
        //logger.info("Source recv udp data packet :" + new String(data));
        ArrayList list = RtPUdpDataBean.getUdpDataBeanSet(data);
        int n = list.size();
        for (int i = 0; i < n; i++) {
            addDataGramPacket((RtPUdpDataBean) list.get(i));


        }
    }

    public synchronized void addDataGramPacket(RtPUdpDataBean packet) throws Ex {

        try {
            mQueue.enqueueTail(new JACE.ASX.MessageBlock(packet));
        } catch (InterruptedException ie) {
            logger.warn("Add udp data is bad  message type");
            throw new Ex().set(E.E_FormatError, ie, new Message("Add udp data is bad  message type"));
            /*} catch (SocketException e) {
                logger.warn(" bad  message type");
                throw new Ex().set(E.E_FormatError, e, new Message(" bad  message type"));
            }*/
        }
    }

    public DatagramSendThread(
            DatagramSocket sendSocket, int interval, InetSocketAddress clientadress) {
        fSendSocket = sendSocket;
        mQueue = new MessageQueue();
        this.INTERVAL = interval;
        this.clientadress = clientadress;


    }//DatagramSendThread

    public DatagramSendThread() {


    }//DatagramSendThread

    public void init(InetSocketAddress
                             clientadress, int bufsize, int interval) {


        mQueue = new MessageQueue();
        this.INTERVAL = interval;
        this.clientadress = clientadress;
        try {
            fSendSocket = new DatagramSocket();
            fSendSocket.setSendBufferSize(bufsize * 1024);
            fSendSocket.setBroadcast(false);
            fSendSocket.setReuseAddress(true);
            fSendSocket.setTrafficClass(0);
            fSendSocket.connect(clientadress);
            sendpacket = new DatagramPacket(new byte[0], 0, clientadress);
        } catch (SocketException e) {
            logger.warn("Upd  Client init error.", e);
        }

    }


    public void close(boolean trueClose) {
        if (!trueClose) {
            sendError++;
            if (sendError <= 5) {
                logger.warn("Upd Send to Client error,Retry :" + sendError);
                return;
            }
        }
        isRun = false;
        if (fSendSocket != null) {
            fSendSocket.disconnect();
            fSendSocket.close();
        }
        try {
            while (haveMessages()) {
                RtPUdpDataBean newMessage = (RtPUdpDataBean) mQueue.dequeueHead().obj();
                newMessage.clear();
            }
        } catch (InterruptedException e) {
            logger.warn("Upd Send to Client error.", e);
        }
        logger.warn("Upd Send to Client process close.");
        StringBuffer buff = new StringBuffer();
        buff.append("Udp Send address(");
        buff.append(clientadress.getHostName());
        buff.append(":");
        buff.append(clientadress.getPort());
        buff.append(") begin time:");
        buff.append(beginTime);
        buff.append(" total send udp data number:");
        buff.append(sendcount);
        buff.append(" total data length:");
        buff.append(flow);
        buff.append("MB");
        logger.info(buff.toString());

    }

    public boolean isRun() {
        return isRun;
    }

    /**
     * proceed with all of the time-consuming stuff here
     */
    public void run() {
        isRun = true;

        beginTime = System.currentTimeMillis();
        RtPUdpDataBean newMessage = null;
        while (isRun) {
            try {

                if (haveMessages()) {

                    newMessage = (RtPUdpDataBean) mQueue.dequeueHead().obj();
                    sendpacket.setData(newMessage.getData());
                    sendpacket.setLength(newMessage.getLength());
                    sendpacket.setSocketAddress(clientadress);
                    fSendSocket.send(sendpacket);
                    flow = flow + ((float) sendpacket.getLength() / (float) (1024 * 1024));
                    //Thread.sleep(1);
                    //logger.info("total flow:" + flow + "MB");
                    sendcount++;
                    /*if (sendError > 0) {
                        sendError = 0;
                    }*/
                    newMessage.clear();
                } else {
                    Thread.sleep(2);
                }
            } catch (PortUnreachableException e) {
                try {
                    addDataGramPacket(newMessage);
                } catch (Ex ex) {
                    //okay
                }
                logger.warn("Upd Send to Client PortUnreachableException error.", e);
                close(false);
            } catch (IOException ex) {
                if (newMessage != null) {
                    newMessage.clear();
                }
                close(false);
                logger.warn("Upd Send to Client error.", ex);
            } catch (InterruptedException e) {
                logger.warn("Upd Send to Client error.", e);
            }


        }


    } // run

    private boolean haveMessages() {
        return (!mQueue.isEmpty());
    }

    public static void main(String arg[]) throws Exception {
        DatagramSendThread client = new DatagramSendThread();
        client.init(new InetSocketAddress("192.168.221.71", 6060), 1024, 30);
        client.start();
        client.addUdpBeanPacket(client.getH3cDataFormat().array());
        while (true) {
            Thread.sleep(10);

        }
    }

    public IoBuffer getH3cDataFormat() {
        IoBuffer buf = IoBuffer.allocate(340);
        buf.order(ByteOrder.BIG_ENDIAN);
        //buf.setAutoExpand(true);
        //h3c vmp flag
        buf.putShort((short) 0xbcbc);
        //h3c vmp version
        buf.put((byte) '2');
        //h3c vmp subversion
        buf.put((byte) '0');
        //h3c vmp type
        buf.put((byte) 0);
        //h3c vmp command.
        buf.put((byte) 42);
        //h3c status
        buf.putShort((short) 0);
        //h3c vmp seq number;
        buf.putInt(0);
        //h3c vmp from-id
        IoBuffer fromid = IoBuffer.allocate(32);
        //fromid.order(ByteOrder.LITTLE_ENDIAN);
        fromid.put("10000000".getBytes());
        buf.put(fromid.array());
        //h3c vmp to-id
        IoBuffer toid = IoBuffer.allocate(32);
        //toid.order(ByteOrder.LITTLE_ENDIAN);
        toid.put("vmserver".getBytes());
        buf.put(toid.array());
        //h3c vmp From-handle
        buf.putInt(0xddccbbaa);
        //h3c vmp to-handle
        buf.putInt(0xffffffff);
        //h3c vmp content length
        //buf.order(ByteOrder.LITTLE_ENDIAN);
        // buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort((short) 248);
        //h3c vmp reservied1
        buf.putShort((short) 0);
        //h3c vmp reservied2
        buf.putInt(0);
        //msgbody

        //厂商
        buf.putShort((short) 8);
        buf.putShort((short) 4);
        buf.putInt(120);
        //设备ID;
        buf.putShort((short) 6);
        buf.putShort((short) 32);
        IoBuffer devd = IoBuffer.allocate(32);
        //devd.order(ByteOrder.LITTLE_ENDIAN);
        devd.put("10000000".getBytes());
        buf.put(devd.array());

        //设备类型
        buf.putShort((short) 7);
        buf.putShort((short) 4);
        buf.putInt(22000);
        //事件信息
        buf.putShort((short) 90);
        buf.putShort((short) 144);
        buf.putInt((int) System.currentTimeMillis() / 1000);
        buf.putInt((int) System.currentTimeMillis() / 1000);
        buf.putInt(22001);
        buf.putInt(128);
        IoBuffer mes = IoBuffer.allocate(128);
        //devd.order(ByteOrder.LITTLE_ENDIAN);
        mes.put("192.168.220.12".getBytes());
        buf.put(mes.array());

        buf.flip();
        //System.out.println(new String(buf.array()));
        return buf;
    }
}/* class DatagramSendThread */