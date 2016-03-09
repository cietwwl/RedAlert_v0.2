package com.youxigu.wolf.net;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.codec.IAMF3Message;
import com.youxigu.wolf.node.core.NodeRegTask;

/**
 * 获取配置的服务队列来处理消息
 * 
 * @author wuliangzhu
 * 
 */
public class WolfMessageChain extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory
			.getLogger(WolfMessageChain.class);
	private List<IWolfService> services;
	private List<ISessionListener> sessionListeners;

	public void setServices(List<IWolfService> services) {
		this.services = services;
	}

	public void setSessionListeners(List<ISessionListener> sessionListeners) {
		this.sessionListeners = sessionListeners;
	}

	/**
	 * 初始化Services列表
	 * 
	 */
	public void init(SocketContext context) {

		if (services == null || services.size() == 0) {
			throw new RuntimeException("WolfMessageChain必须设置services");
		}

		for (IWolfService service : services) {
			if (service instanceof IInitListener) {
				IInitListener.class.cast(service).init(context);
			}
		}

		// Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		// public void run() {
		// close();
		// }
		// }));

	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		// MAP,IMessage为客户端请求，由AMF3WolfService自行验证安全性
		if (message instanceof Map || message instanceof NodeRegTask
				|| message instanceof IAMF3Message
				|| message instanceof com.google.protobuf.Message) {
			handleMessageImpl(session, message);
		} else {
			//后台之间的通讯
			Integer tmp = (Integer) session.getAttribute(Response.CREDIT);
			if (tmp == null || (Integer) tmp != 1) {
				logger.warn("没有经过安全验证的请求，丢弃:{}", session.getRemoteAddress());
			} else {
				handleMessageImpl(session, message);
			}

		}

	}

	private void handleMessageImpl(IoSession session, Object message) {
		if (this.services == null) {
			return;
		}

		boolean handled = false;

		// long s = 0;
		// if (logger.isDebugEnabled()) {
		// s = System.currentTimeMillis(); // 开始时间
		// }

		// Response response = (Response) session.getAttribute(Response.KEY);
		// if (response == null) {
		// response = new Response(session);
		// session.setAttributeIfAbsent(Response.KEY,response);
		//
		// }
		Response response = new Response(session);
		IWolfService service = null;
		int size = this.services.size();
		for (int i = 0; i < size; i++) {
			service = this.services.get(i);
			handled = service.handleMessage(response, message) != null;
			if (handled) {
				break;
			}
		}

		// if (logger.isDebugEnabled()) {
		// long lag = System.currentTimeMillis() - s;
		// if (lag > 50) {
		// logger.debug("{} 处理耗时过长:{}", message, lag);
		// }
		// }

		if (!handled) {
			logger.error("没有匹配的消息处理：" + message);
		}

	}

	/**
	 * 可以动态添加Service，这个是作为优先级最低加入
	 * 
	 * @param service
	 */
	public void addLast(IWolfService service) {
		this.services.add(this.services.size(), service);
	}

	/**
	 * 加入优先级最高的service
	 * 
	 * @param service
	 */
	public void addFirst(IWolfService service) {
		this.services.add(0, service);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// super.exceptionCaught(session, cause);
		// if (logger.isDebugEnabled()) {
		// cause.printStackTrace();
		// }
		if (cause instanceof IOException) {
			logger.error("socket IOException：{}", cause.toString());
			session.close(false);
		} else {
			logger.error("socket exception:{}", cause.toString());
		}

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (this.sessionListeners == null)
			return;
		// logger.info("session closed........."
		// + session.getRemoteAddress().toString());
		Response r = new Response(session);
		try {
			for (ISessionListener listener : sessionListeners) {
				listener.close(r);
			}
		} finally {
			try {
				synchronized (session) {
					session.notifyAll();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// session.close(false);

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		if (this.sessionListeners == null)
			return;
		Response r = new Response(session);
		for (ISessionListener listener : sessionListeners) {
			listener.open(r);
		}

	}

	public void close() {
		for (IWolfService service : services) {
			service.stop(false);
		}
	}
}
