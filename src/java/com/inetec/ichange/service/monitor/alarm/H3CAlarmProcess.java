package com.inetec.ichange.service.monitor.alarm;

import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.monitor.databean.AlertDataBean;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;

/**
 * h3c报警联动
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-7
 * Time: 16:23:09
 * To change this template use File | Settings | File Templates.
 */
public class H3CAlarmProcess implements IAlarmProcess {
    private static final Logger logg = Logger.getLogger(H3CAlarmProcess.class);
    private String name;
    private String host;
    private int port;
    private NioDatagramAcceptor acceptor;
    private InetSocketAddress clientaddress;
    private InetSocketAddress serveraddress;
    private DatagramSendThread client;

    public void init(String type, String name) {
        this.name = name;
        try {
            this.host = AlarmProcessFactory.getConfigByKey(name + "-" + "host");
            this.port = Integer.parseInt(AlarmProcessFactory.getConfigByKey(name + "-" + "port"));


        } catch (Ex ex) {
            logg.warn(ex);
        }
        clientaddress = new InetSocketAddress(host, port);
        serveraddress = new InetSocketAddress("0.0.0.0", port);
        init();
    }

    private void init() {
        //DatagramSocket client=new DatagramSocket();
        acceptor = new NioDatagramAcceptor();
        acceptor.setHandler(new H3CAlarmHandler());
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        DatagramSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReceiveBufferSize(1024 * 1024);
        dcfg.setReuseAddress(true);
        dcfg.setSendBufferSize(1024 * 1024);
        dcfg.setBroadcast(false);
        try {
            acceptor.bind(serveraddress);
        } catch (IOException e) {
            //okay
            logg.warn("h3c alarm procee init start service error", e);
        }
        client = new DatagramSendThread();
        client.init(new InetSocketAddress("192.168.221.71", 6060), 1024, 30);
        client.start();
    }

    public void process(String type, AlertDataBean bean) {

        try {
            client.addUdpBeanPacket(getH3cDataFormat(bean).array());
        } catch (Ex ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public IoBuffer getH3cDataFormat(AlertDataBean bean) {
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

    public void close() {
        if (acceptor != null) {
            acceptor.unbind(serveraddress);
            acceptor.dispose();
            logg.info("h3c alarm procee stop service");
            /*if (server != null) {
                server.returnDataPort(serveraddress.getPort());
                //server.returnDataPort(serveraddress.getPort() + 1);

            }*/
        }
    }

    public static void main(String arg[]) throws Exception {
        H3CAlarmProcess h3c = new H3CAlarmProcess();
        h3c.init("h3c", "h3c");
        h3c.process("h3c", null);
        h3c.process("h3c", null);
    }
}
