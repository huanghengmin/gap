package com.inetec.ichange.service.monitor.udp;

import com.inetec.ichange.service.monitor.ReceiveService;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-12-9
 * Time: 下午6:21
 * To change this template use File | Settings | File Templates.
 */
public class UdpServerHandler extends IoHandlerAdapter {

    private static final Logger logger = Logger.getLogger(UdpServerHandler.class);
    private ReceiveService receiveService;

    public UdpServerHandler(ReceiveService receiveService) {
        this.receiveService = receiveService;
    }

    /**
     * 抛出异常触发的事件
     * @param session
     * @param cause
     * @throws Exception
     */
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        session.close(true);
    }

    /**
     * 连接关闭触发的事件
     * @param session
     * @throws Exception
     */
    public void sessionClosed(IoSession session) throws Exception {
//        logger.info("Session closed...");
    }

    /**
     * 建立连接触发的事件
     * @param session
     * @throws Exception
     */
    public void sessionCreated(IoSession session) throws Exception {
//        logger.info("Session created...");
        if(session.isConnected()){
            Object message = session.getCurrentWriteMessage();
            messageReceived(session,message);
        }
    }

    /**
     * 声明这里message必须为IoBuffer类型
     * @param session
     * @param message
     * @throws Exception
     */
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if (message instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer)message;
            receiveService.query.offer(buffer);
            buffer.flip();
//            receiveService.addList(buff);
//            String str = new String(buff,"UTF-8");
//            System.out.println("服务器收到来自客户端的消息:" + str);
        }
    }

    /**
     * 会话空闲
     * @param session
     * @param status
     * @throws Exception
     */
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        logger.info("Session idle...");
    }

    /**
     * 打开连接触发的事件，它与sessionCreated的区别在于，
     * 一个连接地址（A）第一次请求Server会建立一个Session默认超时时间为1分钟，
     * 此时若未达到超时时间这个连接地址（A）再一次向Server发送请求
     * 即是sessionOpened（连接地址（A）第一次向Server发送请求或者连接超时后
     * 向Server发送请求时会同时触发sessionCreated和sessionOpened两个事件）
     * @param session
     * @throws Exception
     */
    public void sessionOpened(IoSession session) throws Exception {
//        logger.info("Session opened...");

    }
}
