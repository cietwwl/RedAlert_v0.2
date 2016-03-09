package com.youxigu.dynasty2.core.jgroup;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.broadcast.IBroadcastConsumer;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.MethodUtil;

/**
 * 接收广播的异步方法调用
 * 
 * @author Administrator
 * 
 */
public class AsyncTaskBroascastingListener implements IBroadcastConsumer {
	private static final Logger log = LoggerFactory
			.getLogger(AsyncTaskBroascastingListener.class);

	private static ExecutorService pool = null;// 初始化线程池
	static {
		int num = Runtime.getRuntime().availableProcessors();
		if (num > 1) {
			num--;
		}
		pool = Executors.newFixedThreadPool(num);
	}

	@Override
	public boolean handleNotification(Serializable serializable)
			throws Exception {
		if (serializable instanceof AsyncWolfTask) {
			call((AsyncWolfTask) serializable);
			return true;
		} else if (serializable instanceof List) {
			List<Serializable> list = (List<Serializable>) serializable;
			Iterator<Serializable> lit = list.iterator();
			while (lit.hasNext()) {
				Serializable s = lit.next();
				if (s instanceof AsyncWolfTask) {
					call((AsyncWolfTask) s);
					lit.remove();
				}
			}
			if (list.size() == 0) {
				return true;
			}
		}
		return false;
	}

	private void call(final AsyncWolfTask task) {

		if (log.isDebugEnabled()) {
			log.debug("接收到异步方法调用广播消息:{}.{}", task.getServiceName(), task
					.getMethodName());
		}

		pool.submit(new Runnable() {

			@Override
			public void run() {
				try {
					MethodUtil.call(task.getServiceName(),
							task.getMethodName(), task.getParams());

				} catch (InvocationTargetException e) {
					if (e.getTargetException() != null) {
						log.error(e.getTargetException().toString(), e
								.getTargetException());

					} else {
						log.error(e.toString(), e);

					}
				} catch (Exception e) {
					log.error("error:", e);
				}
			}

		});

	}

	public void shutdown() {
		if (pool != null)
			pool.shutdown();
	}

}
