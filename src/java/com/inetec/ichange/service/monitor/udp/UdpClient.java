package com.inetec.ichange.service.monitor.udp;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-12-24
 * Time: 上午9:33
 * To change this template use File | Settings | File Templates.
 */
public class UdpClient {
    private static final Logger logger = Logger.getLogger(UdpClient.class);
    private InetSocketAddress target;
    private IoSession session;
    private NioDatagramConnector connector;
    private ConnectFuture connFuture;
    public UdpClient(InetSocketAddress target) {
        this.target = target;
        connector = new NioDatagramConnector();
        connector.setConnectTimeoutMillis(60000L);
        connector.setConnectTimeoutCheckInterval(10);
        connector.setHandler(new ClientHandler());
        DatagramSessionConfig ccfg = connector.getSessionConfig();
        ccfg.setReuseAddress(true);
        ccfg.setBroadcast(false);
        ccfg.setReceiveBufferSize(6553600);
        ccfg.setSendBufferSize(6553600);
        ccfg.setMaxReadBufferSize(6553600);
        ccfg.setMinReadBufferSize(65536);
        ccfg.setTrafficClass(0x04|0x08); //高性能和可靠性
    }

    private boolean sendBuf(byte[] buf) {
        try{
            IoBuffer buffer = IoBuffer.allocate(buf.length, false);
            buffer.put(buf);
            buffer.flip();
            session.write(buffer);
            return true;
        } catch (Exception e) {

        }
        return true;
    }

    public void send(byte[] buff){
        try{
            if(session.isClosing()){
                session = null;
            }
        } catch (Exception e) {
            session = null;
        }
        if(session==null){
            connFuture = connector.connect(target);
            connFuture.awaitUninterruptibly();
            session = connFuture.getSession();
        }
        boolean isOkSend = sendBuf(buff);
        if(!isOkSend){
            boolean isNetWorkOk = Boolean.valueOf(System.getProperty("networkok_1"));
            while (!isNetWorkOk){
                try {
                    logger.warn("应用UDP发送到"+target.getHostString()+"的端口"+target.getPort()+"不可达,等待10秒...");
                    Thread.sleep(1000*10);
                    isOkSend = sendBuf(buff);
                    if(isOkSend){
                        System.setProperty("networkok_1", String.valueOf(true));
                    }
                    isNetWorkOk = Boolean.valueOf(System.getProperty("networkok_1"));
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void close() {
        if(connFuture!=null){
            connFuture.cancel();
        }
    }
    public void destroy(){
        if(connector!=null){
            connector.dispose();
        }
    }

    public InetSocketAddress getInetSocketAddress() {
        return target;
    }

}
