package com.inetec.ichange.service.monitor.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-16
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class);
    public static int count(String file){
        BufferedReader reader = null;
        int line = 0;     //行号
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(
                            new InputStreamReader(
                                new FileInputStream(file),"GBK"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {//一次读入一行，直到读入null为文件结束
                ++ line;
            }
            reader.close();
        } catch (IOException e) {
            logger.error("文件不存在!");
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e1) {
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return line;
    }
}
