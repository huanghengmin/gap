package com.hzih.gap.core;

import com.hzih.gap.utils.StringContext;
import com.inetec.common.util.OSInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-14
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class LocalAddress  {
    private final static String filename = "/etc/network/interfaces";
    //private final static String filename = "F:/interfaces";
    private ArrayList<String> localIp = new ArrayList<>();
    public ArrayList<String> getLocalIp(){
        if(OSInfo.getOSInfo().isLinux()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while((line = bufferedReader.readLine())!=null ){
                    if(line.contains("address")){
                        String ip = line.replace(" ","").trim().replace("address","");
                        if(!localIp.contains(ip)){
                            localIp.add(ip);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(OSInfo.getOSInfo().isWin()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(StringContext.INTERFACE);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while((line = bufferedReader.readLine())!=null ){
                    if(line.contains("address")){
                        String ip = line.replace(" ","").trim().replace("address","");
                        if(!localIp.contains(ip)){
                            localIp.add(ip);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return localIp;
    } 
    public static void main(String[] arg0){
        LocalAddress localAddress = new LocalAddress();
        localAddress.getLocalIp();
    }
}
