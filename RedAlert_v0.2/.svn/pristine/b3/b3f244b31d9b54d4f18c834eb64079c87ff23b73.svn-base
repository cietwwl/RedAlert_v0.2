package com.youxigu.dynasty2.core.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.youxigu.dynasty2.core.Start;

public class WebContextLoaderListener implements ServletContextListener {

	public static Logger logger = LoggerFactory.getLogger(WebContextLoaderListener.class);
	public static final String SERVER_TYPE_KEY="serverType";
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		

		if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
			throw new IllegalStateException(
					"Cannot initialize context because there is already a root application context present - " +
					"check whether you have multiple ContextLoader* definitions in your web.xml!");
		}

		servletContext.log("Initializing Spring root WebApplicationContext");

		
		
		String conPath = System.getProperty("TX_CONF_PATH");
		if (conPath == null) {
			conPath = System.getenv("TX_CONF_PATH");
		}
		if (conPath == null) {
			servletContext.log("没有找到应用配置环境变量:TX_CONF_PATH,采用默认系统目录");
			System.setProperty("TX_CONF_PATH", "");
		}
		
		String serverType = (String)servletContext.getAttribute(SERVER_TYPE_KEY);
		if(serverType==null){
			serverType =  System.getProperty("serverType");
			if(serverType==null){
				serverType =  System.getenv("serverType");
			}
		}
		
		ClassPathXmlApplicationContext context = Start.start(serverType);
		if (context==null){
			throw new IllegalStateException(
					"启动失败，请检查日志信息，重新启动");
		}
		servletContext.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
		
		servletContext.log("Initializing Spring root WebApplicationContext end.......");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Start.shutdown();
	}

}
