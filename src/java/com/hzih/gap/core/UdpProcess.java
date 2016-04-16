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
public class UdpProcess {
    public static Logger logger = Logger.getLogger(UdpProcess.class);
    private boolean isRuning = false;
    private Executor executor = new DefaultExecutor();
    private ExecuteWatchdog watchdog;
    private CommandLine command;
    private DefaultExecuteResultHandler Handler = new DefaultExecuteResultHandler();
    private ShutdownHookProcessDestroyer processDestroyer = new ShutdownHookProcessDestroyer();

    public boolean isRun() {
        return isRuning;
    }

    public void init(String bhost, int bport, String shost, int sport, String thost, int tport) {

        if (OSInfo.getOSInfo().isWin()) {
            command = new CommandLine("socat.exe");
            command.addArgument("-u");
            command.addArgument("-s");
            /* command.addArgument("-t");
            command.addArgument("60");*/
            command.addArgument("-T");
            command.addArgument("60");
            command.addArgument("udp-listen:" + bport + ",reuseaddr,fork,bind=" + bhost);
            command.addArgument("udp:" + thost + ":" + tport);

        }
        if (OSInfo.getOSInfo().isLinux()) {
            command = new CommandLine("/usr/bin/socat");
            command.addArgument("-u");
            command.addArgument("-s");
            /* command.addArgument("-t");
            command.addArgument("60");*/
            command.addArgument("-T");
            command.addArgument("60");
            command.addArgument("udp-listen:" + bport + ",reuseaddr,fork,bind=" + bhost);
            command.addArgument("udp:" + thost + ":" + tport );
        }
        //socat.exe -u -s -T 60 udp-listen:2000,bind=192.168.1.65 udp:192.168.1.65:3000
    }

    public void start() {
        isRuning = true;
        executor.setExitValue(1);

        watchdog=new ExecuteWatchdog(-1);
        executor.setWatchdog(watchdog);
        executor.setProcessDestroyer(processDestroyer);
        Handler.onProcessComplete(0);
        try {
            executor.execute(command, Handler);
        } catch (IOException e) {
            logger.warn("VideoProcess run IOException:", e);
        }

    }

    public void stop() {
        isRuning = false;
        watchdog.destroyProcess();
        watchdog.killedProcess();
        watchdog.stop();


    }

    public static void main(String arg[]) throws Exception {
        UdpProcess video = new UdpProcess();
        video.init("127.0.0.1", 9000, "10.211.55.5", 9000, "10.211.55.5", 9006);
        video.start();
        int n = 0;
        while (n < 30) {
            n++;
            Thread.sleep(1000);
        }
        System.out.println("run end:."+System.currentTimeMillis());
    }
}
