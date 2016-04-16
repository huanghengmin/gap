package com.inetec.ichange.service.monitor.alarm;

import com.inetec.ichange.service.monitor.databean.AlertDataBean;

import javax.mail.*;
import java.util.*;
import javax.mail.internet.*;

/**
 * 邮件报警
 * Created by IntelliJ IDEA.
 * User: bluesky
 * Date: 2010-9-7
 * Time: 16:22:23
 * To change this template use File | Settings | File Templates.
 */
public class MailAlarmProcessImp implements IAlarmProcess {
    String host = "";
    String user = "";
    String password = "";

    public void setHost(String host) {
        this.host = host;
    }

    public void setAccount(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public void send(String from, String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);//ָ
        props.put("mail.smtp.auth", "true");//
        try {
            Session mailSession = Session.getDefaultInstance(props);

            mailSession.setDebug(true);

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            message.setSubject("=?UTF-8?B?" + enc.encode(subject.getBytes()) + "?=");


            message.setContent(new String(content.getBytes("UTF-8")), "text/html;charset=utf-8 ");

            // message.setContent(content);//�ʼ�����
            message.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String args[]) {
        MailAlarmProcessImp sm = new MailAlarmProcessImp();

        sm.setHost("smtp.mail.yahoo.com.cn");
        sm.setAccount("wxh7669", "wxhwer");

        sm.send("wxh7669@yahoo.com.cn", "wxh7669@yahoo.com.cn", "标题", "测试邮件");
    }

    public void init(String type, String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void process(String type, AlertDataBean bean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void close() {

    }
}