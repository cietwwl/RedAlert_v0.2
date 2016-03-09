package com.youxigu.wolf.net;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.util.AlarmTool;

/**
 * 用来监护连接是否正常，若不正常，则进行重连：重连次数无线等到，每次重连间隔 10s
 * 
 * @author wuliangzhu
 * 
 */
public class SessionDaemon implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SessionDaemon.class);
	private static ConcurrentMap<WolfClient, Thread> daemonMap = new ConcurrentHashMap<WolfClient, Thread>();

	private WolfClient client;

	public void run() {
		try {
			while (!client.isShutDown() && !client.reconnect()) {
				try {
					logger.error("5s 后进行重试！！");
					AlarmTool.alarm(AlarmTool.ALARM_CONNECT, client.getLocalAddress()+"连接"+client.getServerAddress()+"异常");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.error(e.toString(), e);
				}
			}

			// 连接成功要删除原来的连接对象
			logger.error("reconnect success:" + client.toString() + " "
					+ client.hashCode());
		} finally {
			daemonMap.remove(client);
		}
	}

	public static void startReconnect(WolfClient client) {

		if (client == null)
			return;
		if (client.isShutDown())
			return;

		logger.error("start Reconnect");
		synchronized (client) {
			Thread old = daemonMap.get(client);
			if (old != null) {
				logger.error("start Reconnect: 重连线程已经存在");
				return;
			}

			SessionDaemon daemon = new SessionDaemon();
			daemon.client = client;
			Thread t = new Thread(daemon);
			t.setName("sessionDaemon：" + client.toString() + " "
					+ client.hashCode());
			t.setDaemon(true);
			t.start();

			daemonMap.putIfAbsent(client, t);
		}
	}
}
