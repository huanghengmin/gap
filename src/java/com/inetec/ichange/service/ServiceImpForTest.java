package com.inetec.ichange.service;


import com.inetec.common.security.DecryptClassLoader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: wxh
 * Date: 2005-11-9
 * Time: 0:09:50
 * To change this template use File | Settings | File Templates.
 */
public class ServiceImpForTest extends HttpServlet {
    private Class m_service = null;
    private String m_path = null;


    public ServiceImpForTest() {
        try {
            //NdcProvider.push(ChangeServerImp.Str_DataCollectionService);
            //m_classLoad = new DecryptClassLoader(this.getClass().getClassLoader());
            String fileseparator = System.getProperty("file.separator");
            m_path = System.getProperty("ichange.home");
            m_path = m_path + fileseparator + "tomcat" + fileseparator + "webapps" + fileseparator + "service" + fileseparator + "WEB-INF";
            //todo:class load
            //String jarPath = m_path + fileseparator + "lib" + fileseparator;*/
            /* //m_classLoad.loadClassesFromJar(jarPath+"exception.jar",true);
          m_classLoad.loadClassesFromJar(jarPath + "common.jar", true);
          m_classLoad.loadClassesFromJar(jarPath + "service.jar");*/
            m_service = Class.forName("com.inetec.ichange.service.Service", true, this.getClass().getClassLoader());
            /* m_service = (ChangeServerImp) cls.newInstance();
          m_service = cls.newInstance();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            m_service.getMethod("doGet", new Class[]{HttpServletRequest.class, HttpServletResponse.class}).invoke(m_service.newInstance(), new Object[]{request, response});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            m_service.getMethod("doPost", new Class[]{HttpServletRequest.class, HttpServletResponse.class}).invoke(m_service.newInstance(), new Object[]{request, response});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void init() throws ServletException {
        //m_service.init(m_path);
        String configName = getInitParameter("configFile");
        try {
            m_service.getMethod("init", new Class[]{String.class, String.class}).invoke(m_service.newInstance(), new Object[]{m_path, configName});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
