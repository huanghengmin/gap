package com.inetec.ichange.service.monitor.utils;

import com.inetec.common.config.stp.nodes.Channel;
import com.inetec.common.config.stp.nodes.Type;
import com.inetec.common.exception.Ex;
import com.inetec.ichange.service.monitor.databean.BusinessDataBean;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 钱晓盼
 * Date: 12-8-17
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class ConfigUtils {
    private static final Logger logger = Logger.getLogger(ConfigUtils.class);
    private static final String InternalXml = System.getProperty("ichange.home")+"/repository/config.xml";
    private static final String ExternalXml = System.getProperty("ichange.home")+"/repository/external/config.xml";
    public static List<BusinessDataBean> getRunBusinesses() {
        List<BusinessDataBean> list = new ArrayList<BusinessDataBean>();
        try {
            Configuration config = new Configuration(InternalXml);
            List<Type> types = config.readTypes();
            for (Type type : types){
                BusinessDataBean bean = new BusinessDataBean();
                bean.setBusiness_name(type.getTypeName());
                bean.setXt_dataflow(0);
                bean.setConnect_count(0);
                bean.setAlert_count(0);
                bean.setRecord_count(0);
                list.add(bean);
            }
        } catch (Ex ex) {
            logger.error("读取启动业务出错",ex);
        }
        return list;
    }

    public static List<Integer> getPorts() {
        List<Integer> ports = new ArrayList<Integer>();
        try {
            Configuration config = new Configuration(ExternalXml);
            List<Channel>  list = config.getChannels();
            for (Channel channel : list) {
                if( channel.getAuditPort() != null && channel.getAuditPort().length() > 0 ){
                    Integer tPort = Integer.valueOf(channel.gettPort());
                    Integer auditPort = Integer.valueOf(channel.getAuditPort());
                    ports.add(tPort);
                    ports.add(auditPort);
                }
            }

        } catch (Ex ex) {
            logger.error("读取审计端口出错",ex);
        }
        return removeDuplicate(ports);
    }

    public static List<Integer> removeDuplicate(List<Integer> list){
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }


    public static boolean getPrivated() {
        Configuration config = null;
        try {
            config = new Configuration(ExternalXml);
        } catch (Ex ex) {

        }
        return config.getPrivated();
    }
}
