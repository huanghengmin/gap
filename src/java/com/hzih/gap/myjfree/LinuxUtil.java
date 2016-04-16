package com.hzih.gap.myjfree;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: cx
 * Date: 13-3-12
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class LinuxUtil {
    public static void main(String args[]) {
        LinuxUtil linuxUtil = new LinuxUtil();
//        System.out.println(linuxUtil.getLinuxNetFlow());
//        System.out.println(linuxUtil.getRxBytesNum());
//        System.out.println(linuxUtil.getTxBytesNum());
        System.out.println(linuxUtil.getCPU());
        System.out.println(linuxUtil.getMem());
        System.out.println(linuxUtil.getLinuxByCommand("192.168.1.231", "root", "admin","ps -aux"));
//        System.out.println(linuxUtil.getLinuxByCommand("192.168.1.231", "root", "admin","ps -ef"));
    }

    public String getLinuxByCommand(String ip, String name, String passwords, String command ) {
        String hostname = ip;
        String username = name;
        String password = passwords;
        String netFlow="";
        //指明连接主机的IP地址
        Connection conn = new Connection(hostname);
        Session ssh = null;
        try {
            //连接到主机
            conn.connect();
            //使用用户名和密码校验
            boolean isconn = conn.authenticateWithPassword(username, password);
            if(!isconn){
                System.out.println("用户名称或者是密码不正确");
            }else{
                System.out.println("已经连接OK");
                ssh = conn.openSession();
                //使用多个命令用分号隔开
//              ssh.execCommand("pwd;cd /tmp;mkdir hb;ls;ps -ef|grep weblogic");
                ssh.execCommand(command);
                //只允许使用一行命令，即ssh对象只能使用一次execCommand这个方法，多次使用则会出现异常
//              ssh.execCommand("mkdir hb");
                //将屏幕上的文字全部打印出来
                InputStream is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                while(true){
                    String line = brs.readLine();
                    if(line==null){
                        break;
                    }else {
                        netFlow = netFlow+line+"\n";
                    }
                }
            }
            //连接的Session和Connection对象都需要关闭
            ssh.close();
            conn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return netFlow;
    }

    public String getLinuxNetFlow() {
        String hostname = "192.168.1.231";
        String username = "root";
        String password = "admin";
        String netFlow="";
        //指明连接主机的IP地址
        Connection conn = new Connection(hostname);
        Session ssh = null;
        try {
            //连接到主机
            conn.connect();
            //使用用户名和密码校验
            boolean isconn = conn.authenticateWithPassword(username, password);
            if(!isconn){
                System.out.println("用户名称或者是密码不正确");
            }else{
                System.out.println("已经连接OK");
                ssh = conn.openSession();
                //使用多个命令用分号隔开
//              ssh.execCommand("pwd;cd /tmp;mkdir hb;ls;ps -ef|grep weblogic");
//                ssh.execCommand("cd /app/weblogic/Oracle/Middleware/user_projects/domains/base_domain;./startWebLogic.sh &");
//                ssh.execCommand("watch -n 1 \"/sbin/ifconfig eth41 | grep bytes\"");
                ssh.execCommand("/sbin/ifconfig eth43 | grep bytes");
                //只允许使用一行命令，即ssh对象只能使用一次execCommand这个方法，多次使用则会出现异常
//              ssh.execCommand("mkdir hb");
                //将屏幕上的文字全部打印出来
                InputStream is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                while(true){
                    String line = brs.readLine();
                    if(line==null){
                        break;
                    }else {
                        netFlow = netFlow+line;
                    }
                }
            }
            //连接的Session和Connection对象都需要关闭
            ssh.close();
            conn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return netFlow;
    }

    public Double getRxBytesNum() {
        String netFlow = getLinuxNetFlow();
        String[] lines = netFlow.split(":");
        String[] aa = lines[1].split(" ");
        String rxbytes = aa[0];
        return Double.valueOf(rxbytes)/1024/1024;
    }

    public Double getTxBytesNum() {
        String netFlow = getLinuxNetFlow();
        String[] lines = netFlow.split(":");
        String[] aa = lines[2].split(" ");
        String rxbytes = aa[0];
        return Double.valueOf(rxbytes)/1024/1024;
    }

    public Double getCPU() {
        String hostname = "192.168.1.231";
        String username = "root";
        String password = "admin";
        String netFlow="";
        Connection conn = new Connection(hostname);
        Session ssh = null;
        Double cpuUse = 0.0;
        try {
            conn.connect();
            boolean isconn = conn.authenticateWithPassword(username, password);
            if(!isconn){
                System.out.println("用户名称或者是密码不正确");
            }else{
                System.out.println("已经连接OK");
                ssh = conn.openSession();
                ssh.execCommand("ps -aux");
                InputStream is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                brs.readLine();
                while(true){
                    String line = brs.readLine();
                    if(line==null){
                        break;
                    }else {
                        String[] aa = line.split("\\s{1,}");
                        cpuUse = cpuUse + Double.valueOf(aa[2]);
                    }
                }
            }
            ssh.close();
            conn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuUse;
    }

    public Double getMem() {
        String hostname = "192.168.1.231";
        String username = "root";
        String password = "admin";
        String netFlow="";
        Connection conn = new Connection(hostname);
        Session ssh = null;
        Double cpuUse = 0.0;
        try {
            conn.connect();
            boolean isconn = conn.authenticateWithPassword(username, password);
            if(!isconn){
                System.out.println("用户名称或者是密码不正确");
            }else{
                System.out.println("已经连接OK");
                ssh = conn.openSession();
                ssh.execCommand("ps -aux");
                InputStream is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                brs.readLine();
                while(true){
                    String line = brs.readLine();
                    if(line==null){
                        break;
                    }else {
                        String[] aa = line.split("\\s{1,}");
                        cpuUse = cpuUse + Double.valueOf(aa[3]);
                    }
                }
            }
            ssh.close();
            conn.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuUse;
    }
}
