package com.ichange.common.utils;

import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.common.security.DesEncrypterAsPassword;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2007-12-25
 * Time: 14:27:59
 * To change this template use File | Settings | File Templates.
 */
public class AuditClient {
    private final static Logger m_logger = Logger.getLogger(AuditClient.class);
    private static final String Str_password = "audit~!@#$%^&*()_+data";
    private Socket socket;
    private String host;
    private int port;
    private String userid;
    private String password;
    private InputStream in;
    private boolean isLogon = false;
    public final static int I_BufferSize = 100 * 1024;
    public final static int I_ReadTimeOut = 2 * 1000;

    public void init(String host, int port) throws Ex {
        this.host = host;
        this.port = port;
        try {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
            socket.setSendBufferSize(100 * 1024);
            socket.setReceiveBufferSize(100 * 1024);
        } catch (IOException e) {
            throw new Ex().set(E.E_InvalidArgument, new Message("Audit connect server faild.(host:{0},port:{1})", host, String.valueOf(port)));
        }

    }

    /**
     * @param userid
     * @param password
     * @return
     */
    public boolean logon(String userid, String password) throws Ex {

        isLogon = false;
        this.userid = userid;
        this.password = password;
        try {

            StringBuffer temp = new StringBuffer();
            temp.append("userid:");
            temp.append(new String(Base64.encodeBase64(userid.getBytes())));
            temp.append(",password:");
            temp.append(new String(Base64.encodeBase64(password.getBytes())));
            temp.append(";");
            String temps = temp.toString();
            temps = new String(new DesEncrypterAsPassword(Str_password).encrypt(temps.getBytes())) + "\r\n";
            socket.getOutputStream().write(temps.getBytes());
            socket.getOutputStream().flush();
            if (socket.isClosed()) {
                return false;
            } else {
                in = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String status = "500:";
                status = reader.readLine();
                if (status.startsWith("500:")) {
                    return false;
                }
            }
        } catch (IOException e) {
            throw new Ex().set(E.E_InvalidArgument, new Message("Audit logon server faild.(host:{0},port:{1})", host, String.valueOf(port)));
        }
        isLogon = true;
        return true;
    }

    /**
     * @return
     * @throws com.inetec.common.exception.Ex
     */
    public byte[] readAuditLog() throws Ex {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (isLogon) {
            byte[] buff = new byte[I_BufferSize];
            try {
                testConnection();
                socket.setSoTimeout(I_ReadTimeOut);
                int length = 0;
                while ((length = in.read(buff)) > 0) {
                    out.write(buff, 0, length);
                    out.flush();
                    if (length < I_BufferSize) {
                        break;
                    }
                }
            } catch (SocketTimeoutException e) {
                return out.toByteArray();
            } catch (IOException e) {
                close();
                if (out.size() > 0) {
                    return out.toByteArray();
                } else {
                    throw new Ex().set(E.E_OperationFailed, new Message("Read Audit Data error.(host:{0},port:{1})", host, String.valueOf(port)));
                }
            }
        } else {
            throw new Ex().set(E.E_OperationFailed, new Message("Read Audit Data error.(host:{0},port:{1})", host, String.valueOf(port)));
        }
        return out.toByteArray();
    }

    private void testConnection() throws IOException {
        if (!socket.isClosed() && socket.isConnected()) {
            try {
                if (in != null) {
                    in.available();
                }
                socket.getOutputStream().write(" ".getBytes());
                socket.getOutputStream().flush();
            } catch (IOException e) {
                m_logger.warn("Socket exception:", e);
                close();
                throw e;
            }
        } else {
            throw new IOException();

        }

    }

    public boolean isLogon() {
        return isLogon;
    }

    public void close() {
        isLogon = false;
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            m_logger.warn("");
        }
    }
}
