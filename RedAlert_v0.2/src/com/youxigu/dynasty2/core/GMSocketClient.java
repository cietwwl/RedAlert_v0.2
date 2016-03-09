package com.youxigu.dynasty2.core;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.youxigu.dynasty2.core.flex.amf.AMF3WolfService;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.wolf.net.KeepAliveMessageFactoryProtoImpl;
import com.youxigu.wolf.net.NamingThreadFactory;
import com.youxigu.wolf.net.ResultMgr;
import com.youxigu.wolf.net.ResultMgr.TaskFuture;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.codec.NewMutilCodecFactory;
import com.youxigu.wolf.node.core.NodeRegTask;

public class GMSocketClient {
	private IoSession session = null;
	private NioSocketConnector connector = null;
	private IoProcessor<NioSession> processor = null;
	private ExecutorService exector = null;
	private String userName;
	private String password;
	// private ResultMgr resultMgr = new ResultMgr();

	private int init = 0;

	public GMSocketClient(String ip, int port, String userName, String password) {

		this.userName = userName;
		this.password = password;
		exector = Executors.newFixedThreadPool(2, new NamingThreadFactory(
				"gmClient"));
		processor = new SimpleIoProcessorPool<NioSession>(NioProcessor.class, 4);
		connector = new NioSocketConnector((Executor) exector, processor);
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();

		// chain.addLast("codec", new ProtocolCodecFilter(new
		// AMF3CodecFactory())); // 设置编码过滤器
		chain
				.addLast("codec", new ProtocolCodecFilter(
						new NewMutilCodecFactory())); // 设置编码过滤器
		// chain.addLast("codec", new ProtocolCodecFilter(new
		// AMF3CompressCodecFactory())); // 设置编码过滤器
		chain.addLast("logger", new LoggingFilter());

		// 添加执行线程池
		// ThreadPoolExecutor executor = new UnorderedThreadPoolExecutor(8, 8,
		// 300, TimeUnit.SECONDS, new NamingThreadFactory(
		// "test"));
		// //,logger.isDebugEnabled() ? new LoggingIoEventQueueHandler("exec") :
		// null
		// ThreadPoolExecutor executorWrite = new UnorderedThreadPoolExecutor(8,
		// 8, 300, TimeUnit.SECONDS,
		// new NamingThreadFactory("testwrite"));
		// //,logger.isDebugEnabled() ? new
		// LoggingIoEventQueueHandler("execWrite") : nulls
		// executor.prestartAllCoreThreads();
		// executorWrite.prestartAllCoreThreads();
		// chain.addLast("exec", new
		// ExecutorFilter(executor,IoEventType.EXCEPTION_CAUGHT,IoEventType.MESSAGE_RECEIVED,IoEventType.SESSION_CLOSED,IoEventType.SESSION_IDLE,IoEventType.SESSION_OPENED));
		//        
		// chain.addLast("execWrite", new ExecutorFilter(executorWrite,
		// IoEventType.WRITE,IoEventType.MESSAGE_SENT));

		// 心跳过滤器
		KeepAliveFilter filter = new KeepAliveFilter(
				new KeepAliveMessageFactoryProtoImpl(), IdleStatus.READER_IDLE,
				KeepAliveRequestTimeoutHandler.CLOSE, 600, 30);
		chain.addLast("ping", filter);

		connector.setHandler(new IoHandlerAdapter() {
			public void sessionOpened(final IoSession session) throws Exception {

			}

			public void messageReceived(IoSession session, Object message) {
				System.out.println("收到消息：" + message);
				if (message instanceof SyncWolfTask) {
					SyncWolfTask task = SyncWolfTask.class.cast(message);
					if (task.getState() == SyncWolfTask.RESPONSE) {
						ResultMgr.requestCompleted(session,
								task.getRequestId(), task.getResult());
					}
				} else if (message instanceof Map) {
					Map map = (Map) message;
					AMF3WolfService.SyncStat synstat = (AMF3WolfService.SyncStat) map
							.remove(AMF3WolfService.SYNC_KEY);
					if (synstat != null
							&& synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
						ResultMgr.requestCompleted(session, synstat.getId(),
								message);
					}
				}
			}
		});
		System.out.println("正在连接服务器,ip=" + ip + ",port=" + port);
		ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));// 建立连接
		cf.awaitUninterruptibly();
		if (cf.isConnected()) {
			session = cf.getSession();
		} else {
			System.out.println("连接服务器失败:" + cf.getException().toString());
		}
		this.session.write("tgw_l7_forward\r\nHost: " + ip + ":" + port
				+ "\r\n\r\n");
		try {
			this.login();
			init = 1;
		} catch (Exception e) {
			e.printStackTrace();

		}

		if (init != 1) {
			this.close();
			System.exit(0);
		}

	}

	public void runCommand(String command) {
		// if (command.startsWith("call ")){
		// //call service
		// command = command.substring(6);
		// String[] arguments=command.split(" ");
		// if (arguments.length<2){
		// System.out.println("call 至少需要两个参数serviceName serviceMethod");
		// }
		//			
		//			
		// }
	}

	public Object login() throws Exception {
		NodeRegTask task = new NodeRegTask();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usr", GMSocketClient.this.userName);
		params.put("pwd", GMSocketClient.this.password);
		params.put("serverType", 64);
		task.setParams(new Object[] { params });

		TaskFuture<Object> future = ResultMgr.requestSent(this.session, task
				.getRequestId());
		this.session.write(task);

		// if (logger.isDebugEnabled()) {
		// logger.debug("同步请求开始：{} -> {}", serviceName, methodName);
		// }
		Object o = null;
		try {
			o = future.get(20000, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			ResultMgr.requestCompleted(this.session, task.getRequestId(), e);
			throw e;
		}

		if (o instanceof Exception) {
			throw Exception.class.cast(o);
		} else if (o instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) o;
			if (((Integer) map.get(IAMF3Action.ACTION_ERROR_CODE_KEY)) < 0) {
				throw new RuntimeException(map
						.get(IAMF3Action.ACTION_ERROR_KEY).toString());
			}
		}

		return o;

	}

	public Object sendTask(String serviceName, String methodName,
			Object... params) throws Exception {
		SyncWolfTask task = new SyncWolfTask();
		task.setParams(params);
		task.setMethodName(methodName);
		task.setServiceName(serviceName);

		if (this.session == null || this.session.isClosing()
				|| !this.session.isConnected()) {
			throw new RuntimeException("连接已经断开，请重新建立连接！！");
		}
		TaskFuture<Object> future = ResultMgr.requestSent(this.session, task
				.getRequestId());
		this.session.write(task);

		// if (logger.isDebugEnabled()) {
		// logger.debug("同步请求开始：{} -> {}", serviceName, methodName);
		// }
		Object o = null;
		try {
			o = future.get(20000, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			ResultMgr.requestCompleted(this.session, task.getRequestId(), e);
			throw e;
		}

		// if (logger.isDebugEnabled()) {
		// logger.debug("同步请求结束：{} ：{}", serviceName + "->" + methodName,
		// future.isDone() + " " + count);
		// }

		// o = future.get();

		if (o == null) {
			return null;
		}

		if (o instanceof Exception) {
			throw Exception.class.cast(o);
		}
		return o;

	}

	public Object asyncSendTask(Object task) throws Exception {
		if (task != null) {
			if (this.session == null || this.session.isClosing()
					|| !this.session.isConnected()) {
				throw new RuntimeException("连接已经断开，请重新建立连接！！");
			}

			return this.session.write(task);
		}
		return null;
	}

	public void close() {
		if (this.session != null) {
			CloseFuture future = this.session.close(false);
			try {
				future.wait();
			} catch (Exception e) {
			}
		}

		connector.dispose();

		if (this.processor != null) {
			this.processor.dispose();
		}

		if (exector != null) {
			exector.shutdown();
		}
	}
}
