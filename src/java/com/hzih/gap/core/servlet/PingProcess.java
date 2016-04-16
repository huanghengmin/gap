package com.hzih.gap.core.servlet;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-20
 * Time: 下午6:06
 * To change this template use File | Settings | File Templates.
 */
public class PingProcess {

    private final static String logPaht = "/root/ixgbe.log";
    
    private final static String configPath = "/root/ixgbe.txt";

    private FileOutputStream fileOutputStream;
    
    private String pingIP ;
    
    private String ixgbeCmd;

    private String rmmod;
    
    private String modprobe;
    
    private String deleteDay;
    
    private int pingTime;

    private void init() throws Exception{

        File file = new File(logPaht);

        if(!file.exists()){

            file.createNewFile();

        }
        fileOutputStream = new FileOutputStream(file,true);

        FileInputStream fileInputStream = new FileInputStream(configPath);
        
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        
        String line = "";

        while((line = bufferedReader.readLine()) != null){

            if(line.startsWith("pingIP")){

                pingIP = line.substring(line.indexOf(":")+1,line.length());

            }
            else if(line.startsWith("ixgbeCmd")){

                ixgbeCmd = line.substring(line.indexOf(":")+1,line.length());

            }
            else if(line.startsWith("deleteDay")){

                deleteDay = line.substring(line.indexOf(":")+1,line.length());

            }
            else if(line.startsWith("pingTime")){

                pingTime = Integer.parseInt(line.substring(line.indexOf(":")+1,line.length()));

            }
            else if(line.startsWith("rmmod")){

                rmmod = line.substring(line.indexOf(":")+1,line.length());

            }
            else if(line.startsWith("modprobe")){

                modprobe = line.substring(line.indexOf(":")+1,line.length());

            }
        }

        bufferedReader.close();

        inputStreamReader.close();

        fileInputStream.close();
        
    }

    public static void main(String arg[]){

        while (true){

            //String command = " ping  192.168.1.20 -c 1 ";

            PingProcess pingProcess = new PingProcess();

            try {
                pingProcess.init();
                String command = " ping -q "+pingProcess.pingIP+" -i 0.0001 -c 10000";

                String result = pingProcess.pingResult(command);

                String[] strs = result.split(",");

                for(int i = 0 ; i < strs.length ; i++){

                    if(strs[i].contains("packet loss")){

                        String data = strs[i].replace(" ","").substring(0,strs[i].indexOf("%"));

                        if(!data.equals("0%")){

                            pingProcess.lossPcaket(data);

                            Thread.sleep(50*1000);

                            pingProcess.rmmod();

                            Thread.sleep(10*1000);

                            pingProcess.modprobe();

                            Thread.sleep(10*1000);

                            pingProcess.netWokRestart();

                            String time = pingProcess.getDateString();

                            String log = "系统在"+time+"更新驱动完成\n";

                            pingProcess.fileOutputStream.write(log.getBytes("UTF-8"));

                            Thread.sleep(pingProcess.pingTime*60*1000);

                        }

                    }

                }


            } catch (Exception e) {

                try {

                    FileOutputStream fo = new FileOutputStream(logPaht,true);

                    fo.write(e.getMessage().getBytes());

                    fo.flush();

                    fo.close();

                } catch (Exception e1) {

                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

                }

            }

        }
    }
    public String pingResult(String cmd) throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        CommandLine commandline = CommandLine.parse(cmd);

        DefaultExecutor exec = new DefaultExecutor();

        exec.setExitValues(null);

        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);

        exec.setStreamHandler(streamHandler);

        exec.execute(commandline);

        String out = outputStream.toString("gbk");

        String error = errorStream.toString("gbk");

        return (out+error);
    }
    public void updateIxgbe() throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

       ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        //String cmd = "/etc/init.d/ixgbe.sh /root/ixgbe-3.4.24/src";

        CommandLine commandline = CommandLine.parse(ixgbeCmd);

        DefaultExecutor exec = new DefaultExecutor();

        exec.setExitValues(null);

        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);

        exec.setStreamHandler(streamHandler);

        exec.execute(commandline);

        String out = outputStream.toString("gbk");

        String error = errorStream.toString("gbk");

        String data = out+error;

        fileOutputStream.write(data.getBytes());
        
        fileOutputStream.flush();
    }
    public void rmmod() throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        CommandLine commandline = CommandLine.parse(rmmod);

        DefaultExecutor exec = new DefaultExecutor();

        exec.setExitValues(null);

        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);

        exec.setStreamHandler(streamHandler);

        exec.execute(commandline);

        String out = outputStream.toString("gbk");

        String error = errorStream.toString("gbk");

        String data = out+error;

        fileOutputStream.write(data.getBytes());

        fileOutputStream.flush();
    }

    public void modprobe() throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        CommandLine commandline = CommandLine.parse(modprobe);

        DefaultExecutor exec = new DefaultExecutor();

        exec.setExitValues(null);

        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);

        exec.setStreamHandler(streamHandler);

        exec.execute(commandline);

        String out = outputStream.toString("gbk");

        String error = errorStream.toString("gbk");

        String data = out+error;

        fileOutputStream.write(data.getBytes());

        fileOutputStream.flush();
    }
    public void netWokRestart() throws Exception{

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        String cmd = "service networking restart";

        CommandLine commandline = CommandLine.parse(cmd);

        DefaultExecutor exec = new DefaultExecutor();

        exec.setExitValues(null);

        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);

        exec.setStreamHandler(streamHandler);

        exec.execute(commandline);

        String out = outputStream.toString("gbk");

        String error = errorStream.toString("gbk");

        String data = out+error;

        fileOutputStream.write(data.getBytes());

        fileOutputStream.flush();
    }


    
    private void lossPcaket(String lossPcaket) throws Exception {

        String time = getDateString();

        String log = "系统在"+time+"检测到驱动出错,隔离卡的丢包率为:"+lossPcaket+"\n";

        fileOutputStream.write(log.getBytes("UTF-8"));

        fileOutputStream.flush();

        updateIxgbe();
    }
    
    public String getDateString(){

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time = simpleDateFormat.format(date);

        return time;
    }


}

