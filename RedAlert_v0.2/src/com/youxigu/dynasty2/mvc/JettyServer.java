package com.youxigu.dynasty2.mvc;

import java.net.URL;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.manu.core.ServiceLocator;

/**
 * @author zhaiyong Jetty实现的嵌入式WEB服务器，用SPRING 管理
 * 
 */
public class JettyServer {

	private static final Logger logger = LoggerFactory
			.getLogger(JettyServer.class);

	private int port = 8080;
	private String contextPath = "/";
	private String webPath;
	private int threadNum = 50;
	private boolean useJetty = false;// 是否启动jetty 默认不启动

	private Map<String, Filter> filters;

	private Map<String, Servlet> servlets;

	private Server server;

	private void init() throws Exception {
		server = new Server();

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		// connector.setRequestHeaderSize(8192);
		// 加一个fifo类型队列线程池，提高并发效率
		QueuedThreadPool threadPool = new QueuedThreadPool(threadNum);
		threadPool.setName("embed-jetty-http");
		connector.setMaxIdleTime(30000);
		connector.setThreadPool(threadPool);
		server.addConnector(connector);

		// 提供handler
		WebAppContext context = new WebAppContext();
		context.setContextPath(contextPath);
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				"web.xml");
		context.setDescriptor(url.getFile());
		context.setDefaultsDescriptor(url.getFile());
		context.setParentLoaderPriority(true);
		context.setResourceBase("./");
		// context.setConfigurationDiscovered(true);

		// 让jetty与spring使用同一个classloader
		context.setClassLoader(ServiceLocator.getContext().getClassLoader());
		// 让jetty与spring使用同一个applicationContext
		context.setAttribute("applicationContext", ServiceLocator.getContext());

		server.setHandler(context);

		// add filter
		if (filters != null) {
			for (Map.Entry<String, Filter> eFilter : filters.entrySet()) {
				logger.info("add filter={}, path={}", eFilter.getValue()
						.getClass(), eFilter.getKey());
				context.addFilter(new FilterHolder(eFilter.getValue()), eFilter
						.getKey(), EnumSet.allOf(DispatcherType.class));

			}
		}

		// add servlet
		if (servlets != null) {
			for (Map.Entry<String, Servlet> eServlet : servlets.entrySet()) {
				logger.info("add servlet={}, path={} ", eServlet.getValue()
						.getClass(), eServlet.getKey());
				context.addServlet(new ServletHolder(eServlet.getValue()),
						eServlet.getKey());
			}
		}

		if (webPath == null) {
			context.addServlet(DefaultServlet.class, "/*");
		}
	}

	public void start() throws Exception {
		if (useJetty) {
			init();
			server.start();
			logger.info("jetty1 server started , port={}", port);
		}
	}

	public void stop() throws Exception {
		if (server != null) {
			server.stop();
			server.destroy();
		}
	}

	public static void main(String[] args) {
		String contextFile = "conf/applicationContext_jetty.xml";// "classpath:spring-context.xml";

		ApplicationContext context = null;
		try {
			context = new FileSystemXmlApplicationContext(contextFile);
		} catch (Exception e) {
			System.out.println("RunMain [spring-conf-file]");
			logger.warn("", e);
		}

		String jettyServerBeanName = "jettyServer";

		final JettyServer jettyServer = (JettyServer) context
				.getBean(jettyServerBeanName);

		try {
			jettyServer.start();
			logger.info("server started");
		} catch (Throwable e) {
			logger.warn("has exception!", e);
			System.exit(-1);
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}

	public void setServlets(Map<String, Servlet> servlets) {
		this.servlets = servlets;
	}

	public boolean isUseJetty() {
		return useJetty;
	}

	public void setUseJetty(boolean useJetty) {
		this.useJetty = useJetty;
	}
}
