package com.inetec.ichange.service.monitor.alarm;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-12-2
 * Time: 11:12:41
 * To change this template use File | Settings | File Templates.
 */
public class H3CAlarmHandler extends IoHandlerAdapter {
    private static final Logger logg = Logger.getLogger(H3CAlarmHandler.class);

    public void messageReceived(final IoSession session, Object message) throws
            Exception {
        if (message instanceof IoBuffer) {
            IoBuffer buffer = (IoBuffer) message;
            logg.info("h3c alarm recv message:" + new String(buffer.array()));
            buffer.free();
        }

    }

    public void exceptionCaught(IoSession session, Throwable cause) throws
            Exception {
        session.close(true);

    }

    public void sessionClosed(IoSession session) throws Exception {

        session.close(true);
    }
}
