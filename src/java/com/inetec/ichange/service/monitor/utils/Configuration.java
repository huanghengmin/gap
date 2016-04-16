package com.inetec.ichange.service.monitor.utils;

import com.inetec.common.config.stp.EConfig;
import com.inetec.common.config.stp.nodes.*;
import com.inetec.common.exception.E;
import com.inetec.common.exception.Ex;
import com.inetec.common.i18n.Message;
import com.inetec.ichange.service.monitor.databean.IpPort;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Configuration {
    static Logger logger = Logger.getLogger(Configuration.class);
    private Document document;
    public String confPath;

    public Configuration(Document doc) {
        this.document = doc;
    }

    public Configuration(String path) throws Ex {
        this.confPath = path;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    public Configuration(InputStream is, String path) throws Ex {
        this.confPath = path;
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(is);
        } catch (DocumentException e) {
            logger.info(e.getMessage());
        }
    }

     public List<Type> readTypes() throws Ex {
        List types = document.selectNodes("/configuration/system/ichange/types/type");
        if (types == null)
            throw new Ex().set(E.E_NullPointer, E.KEY_NULLPOINTER, "没有应用");
        List<Type> list = new ArrayList<Type>();
        for (Iterator it = types.iterator(); it.hasNext();) {
            Element typeNode = (Element) it.next();
            String isActive = typeNode.element("isactive").getText();
            Type t = new Type();
            t.setActive(isActive);
            if(t.isActive()){
                String name = typeNode.attribute("value").getText();
                String appType = typeNode.attribute("apptype").getText();
                t.setTypeName(name);
                t.setAppType(appType);
                list.add(t);
            }
        }
        return list;
    }

    public List<Type> readTypes(String appType) throws Ex {
        List types = document.selectNodes("/configuration/system/ichange/types/type[@apptype='"+appType+"']");
        if (types == null)
            throw new Ex().set(E.E_NullPointer, E.KEY_NULLPOINTER, "没有应用");
        List<Type> list = new ArrayList<Type>();
        for (Iterator it = types.iterator(); it.hasNext();) {
            Element typeNode = (Element) it.next();
            Type t = new Type();
            String name = typeNode.attribute("value").getText();
            t.setTypeName(name);
            t.setChannelPort(typeNode.element("channelport").getText());
            list.add(t);
        }
        return list;
    }

    public List<Channel> getChannels() throws Ex{
        List<Channel> list = new ArrayList<Channel>();
        List channels = document.selectNodes("/configuration/system/ichange/stp/channel");
        for (Iterator it = channels.iterator(); it.hasNext();) {
            Element channel = (Element) it.next();
            Channel c = new Channel();
            c.setChannelValue(channel.attribute("value").getText());
            c.setIp(channel.element("ip").getText());
            c.setPort(channel.element("port").getText());
            c.settIp(channel.element("tip").getText());
            c.settPort(channel.element("tport").getText());
            c.setProtocol(channel.element("protocol").getText());
            c.setAuditPort(channel.element("auditport").getText()==null?"":channel.element("auditport").getText());
            c.setCount(channel.element("count").getText()==null?"":channel.element("count").getText());
            c.setSize(channel.element("size").getText()==null?"":channel.element("size").getText());
            list.add(c);
        }
        return list;
    }

    public List<IpPort> getSNMPClient(){
        List sNMPClients = document.selectNodes("/configuration/system/ichange/ichangeutils/snmpclientset/snmpclient");
        List<IpPort> list = new ArrayList<IpPort>();
        for (Iterator it = sNMPClients.iterator(); it.hasNext();) {
            IpPort ipPort = new IpPort();
            Element sNMPClient = (Element) it.next();
            ipPort.setIp(sNMPClient.element("host").getText());
            ipPort.setPort(Integer.parseInt(sNMPClient.element("port").getText()));
            list.add(ipPort);
        }
        return list;
    }

    public List<IpPort> getSysLogClient(){
        List sysLogClients = document.selectNodes("/configuration/system/ichange/ichangeutils/syslogclientset/syslogclient");
        List<IpPort> list = new ArrayList<IpPort>();
        for (Iterator it = sysLogClients.iterator(); it.hasNext();) {
            IpPort ipPort = new IpPort();
            Element sysLogClient = (Element) it.next();
            ipPort.setIp(sysLogClient.element("host").getText());
            ipPort.setPort(Integer.parseInt(sysLogClient.element("port").getText()));
            list.add(ipPort);
        }
        return list;
    }

    public boolean getPrivated() {
        String value = "1";
        Element channel = (Element)document.selectSingleNode("/configuration/system/ichange/stp/channel[@value='"+value+"']");
        return Boolean.valueOf(channel.element("privated").getText());
    }

    public void updatePrivated() throws Ex {
        String value = "1";
        Element channel = (Element)document.selectSingleNode("/configuration/system/ichange/stp/channel[@value='"+value+"']");
        channel.element("privated").setText(String.valueOf(true));
        saveExternal();
    }

    public void updateProxySocketPort(List<Type> types) throws Ex {
         for (Type type : types){
             Element port = (Element)document.selectSingleNode("/configuration/system/ichange/types/type[@value='"
                     + type.getTypeName() + "']/plugin/targetplugin/socketchange/port");
             if(port!=null){
                 port.setText(type.getChannelPort());
             }
         }
         saveInternal();
    }

    public void saveExternal() throws Ex {
        String fileName = confPath.substring(confPath.lastIndexOf("/"), confPath.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("'['yyyy-MM-dd_HH-mm-ss']'");
        String historyPath = System.getProperty("ichange.home") + "/repository/external/history";
        String historyFile = historyPath + fileName + sdf.format(new Date()) + ".xml";
        XMLWriter output = null;
        try {
            File file = new File(confPath);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();

            File history = new File(historyFile);
            if (!history.getParentFile().exists())
                history.getParentFile().mkdir();
            FileOutputStream out = new FileOutputStream(history);
            out.write(bytes);
            out.close();

            OutputFormat format = OutputFormat.createPrettyPrint();

            format.setEncoding("UTF-8");

//            output = new XMLWriter(new FileWriter(file), format);
            output = new XMLWriter(new FileOutputStream(file),format);
            output.write(document);

        } catch (FileNotFoundException e) {
            throw new Ex().set(E.E_FileNotFound, e, new Message("File {0} not found!", historyPath));
        } catch (IOException e) {
            throw new Ex().set(E.E_IOException, e, new Message("ccured exception when move Internal configuration To History"));
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                throw new Ex().set(E.E_IOException, e, new Message("ccured exception when close XMLWrite"));
            }
        }
    }

    public void saveInternal() throws Ex {
        String fileName = confPath.substring(confPath.lastIndexOf("/"), confPath.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("'['yyyy-MM-dd_HH-mm-ss']'");

        String historyPath =System.getProperty("ichange.home") + "/repository/internal/history";
        String historyFile = historyPath + fileName + sdf.format(new Date()) + ".xml";
        XMLWriter output = null;
        try {
            File file = new File(confPath);
            FileInputStream fin = new FileInputStream(file);
            byte[] bytes = new byte[fin.available()];
            while (fin.read(bytes) < 0) fin.close();

            File history = new File(historyFile);
            if (!history.getParentFile().exists())
                history.getParentFile().mkdir();
            FileOutputStream out = new FileOutputStream(history);
            out.write(bytes);
            out.close();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
//            output = new XMLWriter(new FileWriter(file), format);
            output = new XMLWriter(new FileOutputStream(file),format);
            output.write(document);


        } catch (FileNotFoundException e) {
            throw new Ex().set(E.E_FileNotFound, e, new Message("File {0} not found!", historyPath));
        } catch (IOException e) {
            throw new Ex().set(E.E_IOException, e, new Message("ccured exception when move Internal configuration To History"));
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                throw new Ex().set(E.E_IOException, e, new Message("Occured exception when close XMLWrite"));
            }
        }
    }


}