package com.hzih.gap.core;

import com.hzih.gap.domain.Tunnel;
import com.hzih.gap.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 下午1:55
 * To change this template use File | Settings | File Templates.
 */
public class TunnelProcess {

    private static HashMap<String,Tunnel> tunnels = new HashMap<>();
    //private static final String path = "F:/stp/repository/app.xml";
    private static final String path = StringContext.systemPath +"/repository/app.xml";
    // private static final String path = "F:/gap/app.xml";
    private static Logger logger = Logger.getLogger(TunnelProcess.class);

    private Document document;

    public TunnelProcess(){
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
//        tunnels.clear();
//        getTunnels();
    }

   /* private HashMap<String,Tunnel> getTunnels(){
        Element root = document.getRootElement();
        Element _tunnels = root.element("tunnels");
        List<Element> list = _tunnels.elements("tunnel");
        for(Element tunnel : list){
            Tunnel tunnel1 = new Tunnel();
            tunnel1.setId(tunnel.attribute("id").getValue());
            tunnel1.setName(tunnel.element("name").getText());
            tunnel1.setLocalIp(tunnel.element("localIp").getText());
            tunnel1.setTargetIp(tunnel.element("targetIp").getText());
            tunnel1.setGapip(tunnel.element("gapip").getText());
            tunnel1.setVideoip(tunnel.element("videoip").getText());
            tunnels.put(tunnel1.getId(),tunnel1);
        }
        return tunnels;
    }*/
    public static Tunnel getTunnel(String id){
        Document document = null;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
//        String result = "";
        Element tunnel = (Element) document.selectSingleNode("/application/tunnels/tunnel[@id='"+id+"']");
        if(tunnel != null){
                Tunnel tunnel1 = new Tunnel();
                tunnel1.setId(tunnel.attribute("id").getValue());
                tunnel1.setName(tunnel.element("name").getText());
                tunnel1.setLocalIp(tunnel.element("localIp").getText());
                tunnel1.setTargetIp(tunnel.element("targetIp").getText());
                tunnel1.setGapip(tunnel.element("gapip").getText());
                tunnel1.setVideoip(tunnel.element("videoip").getText());
            return tunnel1;
        } else {
            logger.info("未找到通道"+id+"的配置!");
        }
        return null;
    }
}
