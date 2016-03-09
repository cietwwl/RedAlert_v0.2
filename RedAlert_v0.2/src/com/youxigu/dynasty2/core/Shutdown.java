package com.youxigu.dynasty2.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.ResultMgr;
import com.youxigu.wolf.net.ResultMgr.TaskFuture;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.codec.NewMutilCodecFactory;
import com.youxigu.wolf.node.core.NodeRegTask;

public class Shutdown {

	private final static String conf = "/application.properties";
	public static Logger log = LoggerFactory.getLogger(Shutdown.class);

	public static void main(String[] args) {
		// 获取Serverip ,端口
		String conPath = System.getProperty("TX_CONF_PATH");
		if (conPath == null) {
			conPath = System.getenv("TX_CONF_PATH");
		}
		if (conPath == null) {
			System.setProperty("TX_CONF_PATH", "");
			conPath = conf;
		}
		log.warn("TX_CONF_PATH:{}", conPath);

		String serverTypeStr = null;
		if (args.length >= 1 && args[0] != null) {
			serverTypeStr = args[0];
		}

		BufferedReader b = null;

		String ip = null;
		int port = 0;
		String usr = null;
		String pwd = null;
		try {

			Properties p = new Properties();

			try {
				b = new BufferedReader(new FileReader(conPath + conf));
			} catch (Exception e) {
				b = new BufferedReader(new InputStreamReader(Shutdown.class.getResourceAsStream(conf), "utf-8"));
			}
			if (b != null) {
				p.load(b);
			}
			ip = p.getProperty(serverTypeStr + ".ip");
			if (ip != null) {
				port = Integer.parseInt(p.getProperty(serverTypeStr + ".port"));
			}

			usr = p.getProperty(serverTypeStr + ".usr");
			pwd = p.getProperty(serverTypeStr + ".pwd");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (b != null) {
					b.close();
				}
			} catch (Exception e) {
			}
		}

		if (ip == null || port == 0) {
			System.out.println("没有找到server的配置信息，请检查classpath中是否有:" + conf);
		} else {
			shutdownServer(ip, port, usr, pwd);
		}
		System.exit(0);
	}

	private static void shutdownServer(String ip, int port, String usr,
			String pwd) {
		try {
			NioSocketConnector conn = new NioSocketConnector();
			conn.setHandler(new IoHandlerAdapter(){
				public void messageReceived(IoSession session, Object message) {
					if (message instanceof SyncWolfTask) {
						SyncWolfTask task = SyncWolfTask.class.cast(message);

						if (task.getState() == SyncWolfTask.RESPONSE) {
							ResultMgr.requestCompleted(session,
									task.getRequestId(), task.getResult());
						}
					}
				}
			});
			DefaultIoFilterChainBuilder chain = conn.getFilterChain();

			// chain.addLast("codec", new ProtocolCodecFilter(new
			// AMF3CodecFactory())); // 设置编码过滤器
			chain.addLast("codec", new ProtocolCodecFilter(
					new NewMutilCodecFactory())); // 设置编码过滤器

			chain.addLast("logger", new LoggingFilter(Shutdown.class));

			ConnectFuture cf = conn.connect(new InetSocketAddress(ip, port));// 建立连接

			cf.awaitUninterruptibly();

			if (cf.isConnected()) {
				// login

				Map<String, Object> params = new HashMap<String, Object>();
				NodeRegTask mainTask = new NodeRegTask();
				params.put("usr", usr);
				params.put("pwd", pwd);

				mainTask.setParams(new Object[] { params });
				TaskFuture<Object> future = ResultMgr.requestSent(cf
						.getSession(), mainTask.getRequestId());
				cf.getSession().write(mainTask);

				Object o = null;
				try {
					o = future.get(20000, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					ResultMgr.requestCompleted(cf.getSession(), mainTask
							.getRequestId(), e);
					throw e;
				}

				cf.getSession().write(
						new AsyncWolfTask("com.youxigu.dynasty2.core.Start",
								"shutdown", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
