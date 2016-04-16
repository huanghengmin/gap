package com.inetec.ichange.service.monitor.utils;

import com.inetec.ichange.service.monitor.databean.UdpHeader;
import com.inetec.ichange.service.monitor.udp.UdpClient;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * 调用服务接口封装
 * 
 * @author collin.code@gmail.com
 * 
 */
public class ServiceUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ServiceUtils.class);

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

     /**
     * 发送之前先判断UDP发送端口是否可达,
     * 不可达,则一直等待直到可达
     * @param target
     * @param list
     */
    public static void callUdpService(InetSocketAddress target, List<byte[]> list) {
        UdpClient client = new UdpClient(target);
        for (byte[] buff : list){
            client.send(buff);
        }
        client.close();
        client.destroy();
    }
}
