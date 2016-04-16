package com.hzih.gap.web.servlet;

import com.hzih.gap.constant.AppConstant;
import com.hzih.gap.constant.ServiceConstant;
import com.hzih.gap.domain.SafePolicy;
import com.hzih.gap.service.SafePolicyService;
import com.hzih.gap.utils.ServiceUtil;
import com.hzih.gap.web.SiteContext;
import com.hzih.myjfree.RunMonitorInfoList;
import com.hzih.myjfree.RunMonitorLiuliangBean2List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.IOException;
import java.util.Properties;

public class SiteContextLoaderServlet extends DispatcherServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(SiteContextLoaderServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);

		SiteContext.getInstance().contextRealPath = config.getServletContext()
				.getRealPath("/");

		// set constants value to app context
		servletContext.setAttribute("appConstant", new AppConstant());

		Properties pros = new Properties();
		try {
			pros.load(getClass().getResourceAsStream("/config.properties"));
			ServiceUtil.serviceUrl = pros.getProperty("serviceUrl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SafePolicyService service = (SafePolicyService)context.getBean(ServiceConstant.SAFEPOLICY_SERVICE);
		SafePolicy data = service.getData();
		SiteContext.getInstance().safePolicy = data;

        new RunMonitorInfoList().start();
        new RunMonitorLiuliangBean2List().start();


	}

	@Override
	public ServletConfig getServletConfig() {
		// do nothing
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// do nothing
	}

	@Override
	public String getServletInfo() {
		// do nothing
		return null;
	}

	@Override
	public void destroy() {

	}

}
