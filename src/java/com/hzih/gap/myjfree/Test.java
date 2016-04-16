package com.hzih.gap.myjfree;

/**
 * Created by IntelliJ IDEA.
 * User: cx
 * Date: 13-3-13
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import java.io.*;
public class Test{

    public static void main(String[] args) {
        Connection conn = null;
        boolean isAuthenticated = false;
        try
        {
            /* Create a connection instance */

            conn = new Connection("192.168.1.231"); // hostname 你要远程登录的主机IP地址,如10.0.2.1

            /* Now connect */

            conn.connect();

            /* Authenticate.
                * If you get an IOException saying something like
                * "Authentication method password not supported by the server at this stage."
                * then please check the FAQ.
                */

            isAuthenticated = conn.authenticateWithPassword("root", "admin"); //你要远程登录的主机的用户名及密码,如admin/123456
            //System.out.println("authenticate sucess ...........................");
            if (isAuthenticated == false)
                System.out.println("SSH Login  Authentication failed.");
            else
            {
                /* Create a session */
                Session sess = conn.openSession();
                System.out.println(new SysCpuInfo(sess).getCPUInfo());
                /*注意,一个session只能执行一次shell,因此,如果你要再执行shell的话,必须重新创建一个session*/
                sess.close();
//                sess = conn.openSession();
//                System.out.println(new SysMemInfo(sess).getMEMInfo());
//                sess.close();
            }
        }catch(Exception e){System.out.println(e.toString());}

    }
}