package com.inetec.ichange.service.monitor.utils;

import com.inetec.ichange.service.monitor.databean.UdpHeader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * 调用服务接口封装
 * 
 * @author collin.code@gmail.com
 * 
 */
public class ServiceUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ServiceUtil.class);

    public static ServiceResponse callBackService(String serviceUrl,String[][] params) {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5 * 1000);
		client.getHttpConnectionManager().getParams().setSoTimeout(5 * 1000);

		PostMethod post = new PostMethod(serviceUrl);
		post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5 * 1000);
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		for (String[] param : params) {
			post.addParameter(param[0], param[1]);
		}

        ServiceResponse response = new ServiceResponse();

		int statusCode = 0;
		try {
			statusCode = client.executeMethod(post);
			response.setCode(statusCode);
			if (statusCode == 200) {
				String data = post.getResponseBodyAsString();
				response.setData(data);
			}
		} catch (Exception e) {
			logger.error("访问接口失败"+e.getMessage(), e);
		}

		return response;
	}

    public static boolean send(DatagramSocket ds,InetSocketAddress target,byte[] buf) {
        try {
//            ds.connect(target);
//           boolean isconnetcted= ds.isConnected();
            DatagramPacket op = new DatagramPacket(buf, buf.length, target);//将BUF缓冲区中的数据打包
            ds.send(op);
            Thread.sleep(10 * 1);
            return true;
        } catch (PortUnreachableException e){
            System.setProperty("networkok_1", String.valueOf(false));
            logger.error("UDP发送端口不可达");
        } catch (SocketException e) {
            logger.error("UDP发送错误",e);
        } catch (UnknownHostException e) {
            logger.error("未知的主机名",e);
        } catch (IOException e) {
            logger.error("I/O流操作错误",e);
        } catch (InterruptedException e) {
        }
        return false;
    }

     /**
     * 发送之前先判断UDP发送端口是否可达,
     * 不可达,则一直等待直到可达
     * @param ds
     * @param target
     * @param buff
     */
    public static void callUdpService(DatagramSocket ds, InetSocketAddress target, byte[] buff) {
        boolean isOkSend = send(ds,target,buff);
        if(!isOkSend){
            boolean isNetWorkOk = Boolean.valueOf(System.getProperty("networkok_1"));
            while (!isNetWorkOk){
                try {
                    logger.warn("UDP发送到"+target.getHostString()+"的端口"+target.getPort()+"不可达,等待10秒...");
                    Thread.sleep(1000*10);
                    isOkSend = send(ds,target,buff);
                    if(isOkSend){
                        System.setProperty("networkok_1", String.valueOf(true));
                    }
                    isNetWorkOk = Boolean.valueOf(System.getProperty("networkok_1"));
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static String getId(int index1, int index2) {
        String id_1 = String.valueOf(index1);
        String id_2 = String.valueOf(index2);
        switch (id_1.length()) {
            case 1 : id_1 = "000" +id_1;break;
            case 2 : id_1 = "00" +id_1;break;
            case 3 : id_1 = "0" +id_1;break;
            case 4 : id_1 = id_1;break;
        }
        switch (id_2.length()) {
            case 1 : id_2 = "000" +id_2;break;
            case 2 : id_2 = "00" +id_2;break;
            case 3 : id_2 = "0" +id_2;break;
            case 4 : id_2 = id_2;break;
        }
        /**
         * 0000-0001
         * 0000-0002
         * 0000-0003
         * *****
         * 0001-9999
         * 0002-0001
         */
        String id = id_1 +"-"+id_2;
        return id;
    }

    public static byte[] addHead(UdpHeader udpHeader,byte[] buf) throws IOException {              //byte[] buf, String fileName, String id, int len
        byte[] buff = new byte[buf.length+200];
        String header = udpHeader.toJsonString();
        if(header!=null) {
            byte[] heads = header.getBytes();
            for(int i=0;i<200;i++){
                buff[i] = heads[i];
            }
            for(int i=0;i<buf.length;i++){
                buff[i+heads.length] = buf[i];
            }
        } else {
            logger.error("UDP包头部大于200个字节,需要重新处理(升级)...");
        }
        return buff;
    }


}
