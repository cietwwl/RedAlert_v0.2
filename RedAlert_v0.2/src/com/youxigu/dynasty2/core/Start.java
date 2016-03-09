package com.youxigu.dynasty2.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

import org.logicalcobwebs.proxool.ProxoolFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.mvc.JettyServer;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.util.AlarmTool;
import com.youxigu.wolf.net.IWolfService;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.WolfServer;

public class Start {

	public static Logger log = LoggerFactory.getLogger(Start.class);

	// public static final String SERVER_TYPE_MIXED_STR = "mixedserver";// 混合服务器

	// public static final int SERVER_TYPE_MIXED = 0;// 混合服务器

	public static boolean isShutdown = false;

	public static void main(String[] args) {
		String conPath = System.getProperty("TX_CONF_PATH");
		if (conPath == null) {
			conPath = System.getenv("TX_CONF_PATH");
		}
		if (conPath == null) {
			log.warn("没有找到应用配置环境变量:TX_CONF_PATH,采用默认系统目录");
			System.setProperty("TX_CONF_PATH", "");
		}

		String serverTypeStr = null;
		if (args.length >= 1 && args[0] != null) {
			serverTypeStr = args[0];
		}

		start(serverTypeStr);
	}

	public static ClassPathXmlApplicationContext start(String serverTypeStr) {
		try {
			ProxoolFacade.disableShutdownHook();
			String xmlFile = "applicationContext.xml";

			String serverTypeName = NodeSessionMgr.SERVER_TYPE_MAIN_STR;
			int serverType = NodeSessionMgr.SERVER_TYPE_MAIN;
			if (serverTypeStr != null) {
				if (NodeSessionMgr.SERVER_TYPE_NODE_STR
						.equalsIgnoreCase(serverTypeStr)) {
					serverType = NodeSessionMgr.SERVER_TYPE_NODE;
					serverTypeName = NodeSessionMgr.SERVER_TYPE_NODE_STR;
				} else if (NodeSessionMgr.SERVER_TYPE_MAIN_STR
						.equalsIgnoreCase(serverTypeStr)) {
					serverType = NodeSessionMgr.SERVER_TYPE_MAIN;
					serverTypeName = NodeSessionMgr.SERVER_TYPE_MAIN_STR;
				} else if (NodeSessionMgr.SERVER_TYPE_JOB_STR
						.equalsIgnoreCase(serverTypeStr)) {
					serverType = NodeSessionMgr.SERVER_TYPE_JOB;
					serverTypeName = NodeSessionMgr.SERVER_TYPE_JOB_STR;
				} else if (NodeSessionMgr.SERVER_TYPE_ASYNCDB_STR
						.equalsIgnoreCase(serverTypeStr)) {
					serverType = NodeSessionMgr.SERVER_TYPE_ASYNCDB;
					serverTypeName = NodeSessionMgr.SERVER_TYPE_ASYNCDB_STR;
					// 异步DB,禁止UDP
					System.setProperty("dynasty2.udp", "false");

				} else if (NodeSessionMgr.SERVER_TYPE_ACTIVE_STR
						.equalsIgnoreCase(serverTypeStr)) {
					serverType = NodeSessionMgr.SERVER_TYPE_ACTIVE;
					serverTypeName = NodeSessionMgr.SERVER_TYPE_ACTIVE_STR;
				} else if (NodeSessionMgr.SERVER_TYPE_OTHER_STR
						.equalsIgnoreCase(serverTypeStr)) {
					serverType = NodeSessionMgr.SERVER_TYPE_OTHER;
					serverTypeName = NodeSessionMgr.SERVER_TYPE_OTHER_STR;
				}
			}
			log.info("启动 {} server................", serverTypeName);

			// 设置到系统环境变量
			System.setProperty(NodeSessionMgr.SERVER_TYPE_KEY, serverType + "");
			System.setProperty(NodeSessionMgr.SERVER_TYPE_NAME_KEY,
					serverTypeName);

			// final ClassPathXmlApplicationContext parent = new
			// ClassPathXmlApplicationContext(
			// xmlFile);
			String fileName = null;

			if (serverType == NodeSessionMgr.SERVER_TYPE_NODE) {
				fileName = "wolf/app_nodeserver.xml";
			} else {
				fileName = "wolf/app_server.xml";
			}

			// loadBean(fileName,context);
			// final ClassPathXmlApplicationContext context = new
			// ClassPathXmlApplicationContext(
			// new String[] { fileName }, true, parent);

			final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { xmlFile, fileName });

			if (context != null) {
				ServiceLocator.getInstance().setApplicationContext(context);
			}

			// 启动socket server
			final WolfServer server = (WolfServer) ServiceLocator
					.getSpringBean("wolf_server");
			server.setServerType(serverType);
			server.start();

			// 启动socket客户端，只有nodeserver或者测试用的mixedServer会启动客户端
			startClient(server);

			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					_shutdown();
				}
			}, "shutdownHookThread"));

			// IUserService userService =
			// (IUserService)ServiceLocator.getSpringBean("userService");
			// User user = userService.getUserById(8802L);
			// IBabelService babelService =
			// (IBabelService)ServiceLocator.getSpringBean("babelService");
			// babelService.doStartCombat(user, BabelCons.BABEL_TYPE_NORMAL);
			// babelService.doExitBabel(8802L, 1);
			// babelService.doStartCombatAuto(user, 1, false);
			// babelService.doEntryBabel(8802L, 1);
			// ITreasuryService treasuryService =
			// (ITreasuryService)ServiceLocator.getSpringBean("treasuryService");
			// treasuryService.addItemToTreasury(109L, 10610131, 2);

			if (NodeSessionMgr.SERVER_TYPE_MAIN_STR
					.equalsIgnoreCase(serverTypeStr)) {
				JettyServer jettyServer = (JettyServer) ServiceLocator
						.getSpringBean("jettyServer");
				jettyServer.start();
			}

			log.info("start {} end................", serverTypeName);
			return context;

		} catch (Exception e) {
			e.printStackTrace();
			shutdown();
		} finally {

		}
		return null;
	}

	public static void _shutdown() {
		// 先关闭客户端
		if (!isShutdown) {
			isShutdown = true;
			System.out.println("正在关闭系统..................");
			//停止告警
			AlarmTool.shutdown=true;
			
			try {
				stopSocket();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("系统Socket完毕..................");
			try {
				ConfigurableApplicationContext context = (ConfigurableApplicationContext) (ServiceLocator
						.getInstance().getApplicationContext());
				while (context != null) {

					context.close();

					context = (ConfigurableApplicationContext) context
							.getParent();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("spring关闭完毕..................");

			try {
				ProxoolFacade.shutdown(0);

				closeMySqlTimer();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("连接池关闭完毕..................");

			try {
				MemcachedManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Memcached client关闭完毕..................");			
			
			System.out.println("系统关闭完成。");
		}
		// System.exit(0);

	}

	public static void shutdown() {
		System.exit(0);
	}

	private static void closeMySqlTimer() {
		/**
		 * 仅对mysql驱动
		 */
		try {
			Class ConnectionImplClass = Thread.currentThread()
					.getContextClassLoader().loadClass(
							"com.mysql.jdbc.ConnectionImpl");
			if (ConnectionImplClass != null
					&& ConnectionImplClass.getClassLoader() == Start.class
							.getClassLoader()) {
				java.lang.reflect.Field f = ConnectionImplClass
						.getDeclaredField("cancelTimer");
				f.setAccessible(true);
				Timer timer = (Timer) f.get(null);
				timer.cancel();
			}
		} catch (java.lang.ClassNotFoundException e1) {
			// do nothing
		} catch (Exception e) {
			System.out
					.println("Exception cleaning up MySQL cancellation timer: "
							+ e.getMessage());
		}
	}

	private static void stopSocket() {
		System.out.println("关闭服务器开始 beign.......");
		try {
			// 阻止客户端继续发请求
			IWolfService amf3WolfService = (IWolfService) ServiceLocator
					.getSpringBean("amf3WolfService");
			if (amf3WolfService != null) {
				amf3WolfService.stop(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 禁止jobServer再发给自己可执行的job
		IWolfClientService client = null;
		try {
			client = (IWolfClientService) ServiceLocator
					.getSpringBean("jobServerClient");
			if (client != null) {
				client.unRegisterNode(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("5秒后开始关闭Socket.......");
			Thread.sleep(5000);
		} catch (Exception e) {

		}

		// 停止Socket Server
		try {
			WolfServer wolfServer = (WolfServer) ServiceLocator
					.getSpringBean("wolf_server");
			if (wolfServer != null) {
				wolfServer.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 停止客户端连接
		try {
			client = (IWolfClientService) ServiceLocator
					.getSpringBean("mainServerClient");

			if (client != null) {
				client.destory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			client = (IWolfClientService) ServiceLocator
					.getSpringBean("jobServerClient");
			if (client != null) {
				client.destory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			client = (IWolfClientService) ServiceLocator
					.getSpringBean("asyncDBServerClient");
			if (client != null) {
				client.destory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			client = (IWolfClientService) ServiceLocator
					.getSpringBean("activityServerClient");
			if (client != null) {
				client.destory();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("关闭Socket end.......");

	}

	private static void startClient(WolfServer server) {
		// asyncdbServer只会被连接，不会主动连接其他server
		if (server.getServerType() == NodeSessionMgr.SERVER_TYPE_ASYNCDB
				|| server.getServerType() == NodeSessionMgr.SERVER_TYPE_ACTIVE) {
			return;
		}

		// 发送game Server ip port到mainserver
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nodeServerIp", server.getIp());
		params.put("nodeServerPort", server.getPort());
		params.put("serverType", server.getServerType());

		// 所有的server连接到mainServer
		final IWolfClientService mainServerClient = (IWolfClientService) ServiceLocator
				.getSpringBean("mainServerClient");

		// if (server.getServerType() != NodeSessionMgr.SERVER_TYPE_MAIN) {
		// 所有的Server都连到mainServer,包括mainServer自己也要连自己
		mainServerClient.init();
		Object localAddress = mainServerClient.registerNode(params);
		// }

		final IWolfClientService jobServerClient = (IWolfClientService) ServiceLocator
				.getSpringBean("jobServerClient");
		// node server连接到jobServer
		// if (server.getServerType() == NodeSessionMgr.SERVER_TYPE_NODE) {
		// nodeserver连接到jobServer，
		// 启动jobServer客户端
		// 判断jobserver是否与mainserver是同一服务器
		if (jobServerClient != null) {
			if (mainServerClient != null
					&& jobServerClient.getWolfClient().getServerAddress()
							.equals(
									mainServerClient.getWolfClient()
											.getServerAddress())) {
				jobServerClient.setWolfClient(mainServerClient.getWolfClient());
				log.info("jobserver与mainServer相同");
			} else {
				jobServerClient.init();
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.putAll(params);
				jobServerClient.registerNode(params1);
			}
		}
		// }

		// 启动asyncDBServer客户端
		// 判断asyncDBServer是否与mainserver是同一服务器
		// 判断asyncDBServer是否与jobserver是同一服务器
		final IWolfClientService asyncDBServerClient = (IWolfClientService) ServiceLocator
				.getSpringBean("asyncDBServerClient");
		if (asyncDBServerClient != null) {
			if (mainServerClient != null
					&& asyncDBServerClient.getWolfClient().getServerAddress()
							.equals(
									mainServerClient.getWolfClient()
											.getServerAddress())) {
				asyncDBServerClient.setWolfClient(mainServerClient
						.getWolfClient());
				log.info("asyncDBServer与mainServer相同");
			} else if (jobServerClient != null
					&& asyncDBServerClient.getWolfClient().getServerAddress()
							.equals(
									jobServerClient.getWolfClient()
											.getServerAddress())) {
				asyncDBServerClient.setWolfClient(jobServerClient
						.getWolfClient());
				log.info("asyncDBServer与jobserver相同");
			} else {
				log.info("启动asyncDBServer客户端");
				asyncDBServerClient.init();
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.putAll(params);
				asyncDBServerClient.registerNode(params1);
			}
		}

		Properties properties = (Properties) ServiceLocator
				.getSpringBean("app_properties");
		if ("1".equals(properties.getProperty("log.mylog.open"))) {
			final IWolfClientService logServerClient = (IWolfClientService) ServiceLocator
					.getSpringBean("logServerClient");
			if (logServerClient != null) {
				log.info("启动logServer客户端");
				logServerClient.init();
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.putAll(params);
				logServerClient.registerNode(params1);
			}
		}

		// 全区活动服--mainserver，jobServer 连接全区活动服
		if (server.getServerType() == NodeSessionMgr.SERVER_TYPE_MAIN
				/*|| server.getServerType() == NodeSessionMgr.SERVER_TYPE_JOB*/) {
			final IWolfClientService activityServerClient = (IWolfClientService) ServiceLocator
					.getSpringBean("activityServerClient");
			if (activityServerClient != null) {
				if (mainServerClient != null
						&& activityServerClient.getWolfClient()
								.getServerAddress().equals(
										mainServerClient.getWolfClient()
												.getServerAddress())) {
					activityServerClient.setWolfClient(mainServerClient
							.getWolfClient());
					log.info("activeserver与mainServer相同");

					if (server.getServerType() == NodeSessionMgr.SERVER_TYPE_MAIN) {
						activityServerClient
								.sendTask(void.class, "activityServerClient",
										"registerSession",
										new Object[] { localAddress,
												Constant.ALL_AREA_ID });

					}

				} else {
					log.info("全区战客户端启动 开始......");
					try {
						activityServerClient.init();
						if (activityServerClient.isConnected()) {
							Map<String, Object> params1 = new HashMap<String, Object>();
							params1.putAll(params);

							activityServerClient.registerNode(params1);
						} else {
							log.info("not start activity client");
						}
						log.info("全区战客户端启动完毕......");
					} catch (Exception e) {
						e.printStackTrace();
						log.error("全区战客户端启动 错误", e);
					}
					
				}
			}
		}

	}

	/**
	 * 向spring的beanFactory动态地装载bean
	 * 
	 * @param configLocationString
	 *            要装载的bean所在的xml配置文件位置。 spring配置中的contextConfigLocation，同样支持诸如
	 *            "/WEB-INF/ApplicationContext-*.xml"的写法。
	 */
	public static void loadBean(String configLocationString,
			ConfigurableApplicationContext parent) {

		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
				(BeanDefinitionRegistry) parent.getBeanFactory());
		beanDefinitionReader.setResourceLoader(parent);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(
				parent));
		try {
			String[] configLocations = new String[] { configLocationString };
			for (int i = 0; i < configLocations.length; i++) {
				beanDefinitionReader.loadBeanDefinitions(parent
						.getResources(configLocations[i]));
			}
			parent.refresh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
