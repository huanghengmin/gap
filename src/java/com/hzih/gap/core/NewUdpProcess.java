package com.hzih.gap.core;

import com.inetec.common.util.OSInfo;
import org.apache.commons.exec.*;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-20
 * Time: 下午6:06
 * To change this template use File | Settings | File Templates.
 */
public class NewUdpProcess {
    public static Logger logger = Logger.getLogger(NewUdpProcess.class);
    private boolean isRuning = false;
    private Executor executor = new DefaultExecutor();
    private ExecuteWatchdog watchdog;
    private CommandLine command;
    private DefaultExecuteResultHandler Handler = new DefaultExecuteResultHandler();
    private ShutdownHookProcessDestroyer processDestroyer = new ShutdownHookProcessDestroyer();

    public boolean isRun() {
        return isRuning;
    }

    public void initPrerouting(String bhost, String bport,String thost, String tport) {

//        if (OSInfo.getOSInfo().isLinux()) {
            /*command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-A");
            command.addArgument("PREROUTING");
            command.addArgument("-d");
            command.addArgument(bhost);
            command.addArgument("-p");
            command.addArgument("udp");
            command.addArgument("--dport");
            command.addArgument(bport);
            command.addArgument("-j");
            command.addArgument("DNAT");
            command.addArgument("--to");
            command.addArgument(thost + ":" + tport);*/

            command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-A");
            command.addArgument("PREROUTING");
            command.addArgument("-d");
            command.addArgument(bhost);
            command.addArgument("-p");
            command.addArgument("udp");
            command.addArgument("-m");
            command.addArgument("udp");
            command.addArgument("--dport");
            command.addArgument(bport);
            command.addArgument("-j");
            command.addArgument("DNAT");
            command.addArgument("--to-destination");
            command.addArgument(thost + ":" + tport);
//        }
        //socat.exe -u -s -T 60 tcp-listen:2000,reuseaddr,fork,bind=172.16.2.6 tcp:172.16.2.6:3000
        if(command != null){
            logger.info(command.toString());
        }
    }


    public void clearPrerouting(String bhost, String bport,String thost, String tport) {

//        if (OSInfo.getOSInfo().isLinux()) {
            /*command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-D");
            command.addArgument("PREROUTING");
            command.addArgument("-d");
            command.addArgument(bhost);
            command.addArgument("-p");
            command.addArgument("udp");
            command.addArgument("--dport");
            command.addArgument(bport);
            command.addArgument("-j");
            command.addArgument("DNAT");
            command.addArgument("--to");
            command.addArgument(thost + ":" + tport);*/

            command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-D");
            command.addArgument("PREROUTING");
            command.addArgument("-d");
            command.addArgument(bhost);
            command.addArgument("-p");
            command.addArgument("udp");
            command.addArgument("-m");
            command.addArgument("udp");
            command.addArgument("--dport");
            command.addArgument(bport);
            command.addArgument("-j");
            command.addArgument("DNAT");
            command.addArgument("--to-destination");
            command.addArgument(thost + ":" + tport);
//        }
        //socat.exe -u -s -T 60 tcp-listen:2000,reuseaddr,fork,bind=172.16.2.6 tcp:172.16.2.6:3000
        if(command != null){
            logger.info(command.toString());
        }
    }

    public void initPostrouting(String thost,String bhost) {

//        if (OSInfo.getOSInfo().isLinux()) {
           /* command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-A");
            command.addArgument("POSTROUTING");
            command.addArgument("-d");
            command.addArgument(thost);
            command.addArgument("-j");
            command.addArgument("SNAT");
            command.addArgument("--to");
            command.addArgument(bhost);*/
            command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-A");
            command.addArgument("POSTROUTING");
            command.addArgument("-d");
            command.addArgument(thost);
            command.addArgument("-j");
            command.addArgument("SNAT");
            command.addArgument("--to-source");
            command.addArgument(bhost);
//        }
        //socat.exe -u -s -T 60 tcp-listen:2000,reuseaddr,fork,bind=172.16.2.6 tcp:172.16.2.6:3000
        if(command != null){
            logger.info(command.toString());
        }
    }


    public void clearPostrouting(String thost,String bhost) {

//        if (OSInfo.getOSInfo().isLinux()) {
          /*  command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-D");
            command.addArgument("POSTROUTING");
            command.addArgument("-d");
            command.addArgument(thost);
            command.addArgument("-j");
            command.addArgument("SNAT");
            command.addArgument("--to");
            command.addArgument(bhost);*/
            command = new CommandLine("/sbin/iptables");
            command.addArgument("-t");
            command.addArgument("nat");
            command.addArgument("-D");
            command.addArgument("POSTROUTING");
            command.addArgument("-d");
            command.addArgument(thost);
            command.addArgument("-j");
            command.addArgument("SNAT");
            command.addArgument("--to-source");
            command.addArgument(bhost);
//        }
        //socat.exe -u -s -T 60 tcp-listen:2000,reuseaddr,fork,bind=172.16.2.6 tcp:172.16.2.6:3000
        if(command != null){
            logger.info(command.toString());
        }
    }
    public void start() {
        if(command != null){
            isRuning = true;
            executor.setExitValue(1);
            Handler.onProcessComplete(0);
            try {
                logger.info(command);
                executor.execute(command, Handler);
                logger.warn("execute success");
            } catch (IOException e) {
                logger.warn("VideoProcess run IOException:", e);
            }
        }
    }

    public void stop() {
        isRuning = false;
        watchdog.destroyProcess();
        watchdog.killedProcess();
        watchdog.stop();
    }

    public static void main(String arg[]) throws Exception {
        NewUdpProcess video = new NewUdpProcess();
        video.initPostrouting("127.0.0.1","10.211.55.5");
        video.start();
        int n = 0;
        while (n < 30) {
            n++;
            Thread.sleep(1000);
        }
        System.out.println("run end:."+System.currentTimeMillis());
    }
}
