package com.hzih.gap.core.servlet;

import com.hzih.gap.core.Main;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-18
 * Time: 下午3:35
 * To change this template use File | Settings | File Templates.
 */
public class InitServlet extends HttpServlet{
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);    //To change body of overridden methods use File | Settings | File Templates.
        if(!Main.isRun){
            Main main = new Main();
            Thread td = new Thread(main);
            td.start();
        }
    }
}
