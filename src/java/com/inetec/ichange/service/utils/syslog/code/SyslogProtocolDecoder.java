package com.inetec.ichange.service.utils.syslog.code;

import com.inetec.common.config.nodes.IpMac;
import com.inetec.ichange.service.utils.syslog.SyslogMessage;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

public class SyslogProtocolDecoder implements ProtocolDecoder {
    final static Logger logger = Logger.getLogger(SyslogProtocolDecoder.class);
    private static final AttributeKey BUF_BYTE = new AttributeKey(
            SyslogProtocolDecoder.class, "syslog");
    private String charset = "GBK";

    public void decode(IoSession ioSession, IoBuffer ioBuffer,
                       ProtocolDecoderOutput protocolDecoderOutput) throws
            Exception {
        try {
            IoBuffer bufTmp = ioBuffer;
            byte[] buf = (byte[]) ioSession.getAttribute(BUF_BYTE);
            if (buf == null) {
                //System.out.println("没有尚未处理的数据" + ioBuffer.remaining());
                bufTmp = ioBuffer;
            } else {
                //System.out.println("合并尚未处理的数据" + ioBuffer.remaining());
                bufTmp = IoBuffer.allocate(buf.length + ioBuffer.remaining());
                bufTmp.setAutoExpand(true);
                bufTmp.put(buf);
                bufTmp.put(ioBuffer);
                bufTmp.flip();
            }
            if (bufTmp.remaining() >= 4) { // 循环处理数据包
                //System.out.println("循环处理数据包");

                String data = null;
                try {
                    data = bufTmp.getString(Charset.forName(charset).newDecoder());
                    int n = data.indexOf(">");
                    SyslogMessage syslogMessage = new SyslogMessage();
                    if (n > 0) {
                        int facility = SyslogMessage.extractFacility(Integer.parseInt(data.substring(1, n)));
                        int priority = SyslogMessage.extractPriority(Integer.parseInt(data.substring(1, n)));
                        int serverty = SyslogMessage.serverty(priority, facility);
                        syslogMessage.setServerty(priority);
                        syslogMessage.setFacility(facility);

                        int msgn = data.indexOf("]:", n) + 2;
                        if (!data.matches("]:"))
                            msgn = n + 1;

                        syslogMessage.setHostName(IpMac.getMinaRemoteIp(ioSession.getRemoteAddress().toString()));
                        syslogMessage.setMessage(data.substring(msgn));


                    } else {
                        logger.warn("Syslog format is error:" + data);
                    }
                    /* ProtobufResponse pak = new ProtobufResponse();
                   pak.setResByteLen(dataLen);
                   pak.readFrom(b, 4);*/
                    //System.out.println("往下传递");
                    protocolDecoderOutput.write(syslogMessage);
                } catch (java.nio.charset.MalformedInputException e) {
                    logger.warn("Syslog format is error:" + data);
                }

            }
            if (bufTmp.hasRemaining()) { // 如果有剩余的数据，则放入Session中
                //ystem.out.println("如果有剩余的数据，则放入Session中" + bufTmp.remaining());
                byte[] tmpb = new byte[bufTmp.remaining()];
                try {
                    bufTmp.get(tmpb, bufTmp.position(), tmpb.length);
                    ioSession.setAttribute(BUF_BYTE, tmpb);
                    bufTmp.free();
                } catch (RuntimeException e) {
                    //OKAY
                }
            }
            bufTmp.free();
        } catch (Exception ex) {
            logger.warn("Tcp Server decode data Exception:", ex);
        }


    }

    public void dispose(IoSession session) throws Exception {
        // System.out.println("dispose");
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws
            Exception {
        //System.out.println("finishDecode");
    }

    public boolean IsOddNumber(int n) {
        return n % 2 == 0;
    }

}