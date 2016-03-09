package com.youxigu.wolf.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.UnorderedThreadPoolExecutor;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.ResultMgr.TaskFuture;
import com.youxigu.wolf.node.core.NodeRegTask;

public class WolfClient {
	private static Logger logger = LoggerFactory.getLogger(WolfClient.class);

	private static Logger logger_server_err = LoggerFactory
			.getLogger("com.youxigu.server.error");

	private String clientName = "wolfClient";
	private IoSession session;
	private int timeout = 600000; // 60s连接超时时间
	private String serverIp; // 外网serverIP
	private String localServerIp;// TODO：本地serverIp,这个一般不用
	private int serverPort;

	private SocketConnector connector;

	/**
	 * 编码、解码器工厂
	 */
	private ProtocolCodecFactory codecFactory;

	/**
	 * 这个实际上作为Client端，不需要
	 */
	private int processorNum = Runtime.getRuntime().availableProcessors() * 2;

	/**
	 * 消息处理器
	 */
	private IoProcessor<NioSession> processor = null;

	/**
	 *消息执行器
	 */
	private IoHandler handler;
	/**
	 * 消息执行器线程池的最小线程数
	 */
	private int corePoolSize = Runtime.getRuntime().availableProcessors();

	/**
	 * 消息执行器线程池的最大线程数
	 */
	private int maxPoolSize = Runtime.getRuntime().availableProcessors() * 8 + 1;

	/**
	 * 消息执行器线程池超过corePoolSize的Thread存活时间;秒
	 */
	private long keepAliveTime = 300;

	/**
	 * 消息执行器线程池
	 */
	private ThreadPoolExecutor executor;
	// 写socket线程池
	private ThreadPoolExecutor executorWrite;

	/**
	 * 检查服务器端心跳的时间间隔 ，默认0秒,client不发送心跳包到server,server负责发送到client
	 */
	private int keepAliveRequestInterval = 0;//

	private SocketContext context = new SocketContext();
	// private ResultMgr resultMgr = new ResultMgr();
	private NodeRegTask loginTask;

	private boolean shutDown = false;// 客户端主动关闭连接

	private InetSocketAddress serverAddress;

	private int useLogFilter = 1;// =1在编码之前、解码之后使用logfilter,=2在编码之后、解码之前使用logfilter，else，不使用logfilter

	/**
	 * 是否使用tcp参数： SocketOptions.TCP_NODELAY; 默认是true;
	 */
	private boolean tcpNoDelay = true;

	/**
	 * 是否使用分离的写线程池
	 */
	private boolean useWriteThreadPool = false;

	/**
	 * 发出同步请求，等待多少毫秒不返回，则认为timeout
	 */
	private long syncRequestTimeout = 15000;

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setLocalServerIp(String localServerIp) {
		this.localServerIp = localServerIp;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setSyncRequestTimeout(long syncRequestTimeout) {
		this.syncRequestTimeout = syncRequestTimeout;
	}

	public void setUseWriteThreadPool(boolean useWriteThreadPool) {
		this.useWriteThreadPool = useWriteThreadPool;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public void setUseLogFilter(int useLogFilter) {
		this.useLogFilter = useLogFilter;
	}

	public void setKeepAliveRequestInterval(int keepAliveRequestInterval) {
		this.keepAliveRequestInterval = keepAliveRequestInterval;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setHandler(IoHandler handler) {
		this.handler = handler;
	}

	public void setCodecFactory(ProtocolCodecFactory codecFactory) {
		this.codecFactory = codecFactory;
	}

	public String getServerIp() {
		return serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getServerAddress() {
		return this.serverIp + ":" + this.getServerPort();
	}

	public String getLocalAddress() {
		if (this.session != null && this.session.isConnected()) {
			return this.session.getLocalAddress().toString();
		}
		return null;
	}

	public void setProcessorNum(int processorNum) {
		this.processorNum = processorNum;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public void init() {
		if (handler == null)
			throw new RuntimeException("必须设置handler");

		if (codecFactory == null)
			throw new RuntimeException("必须设置codecFactory");

		if (this.localServerIp != null
				&& this.localServerIp.trim().length() != 0) {
			logger.info("使用本地ip:{}", localServerIp);
			this.serverIp = this.localServerIp;
		}
		serverAddress = new InetSocketAddress(this.serverIp, this.serverPort);

	}

	private void setSession(IoSession session) {
		this.session = session;
		if (this.session != null) {
			this.session.setAttribute("wolfClient", this);
			this.session.setAttribute(Response.CREDIT, 1);
		}
	}

	/**
	 * 连接一个服务器，并指定处理接收到的消息的处理方法
	 * 
	 */
	public void start() {
		// this.context.put("resultMgr", this.resultMgr);

		logger.info("wolfClient消息处理器数量:{}", processorNum);
		logger.info("wolfClient消息执行器最小线程数:{}", corePoolSize);
		logger.info("wolfClient消息执行器最大线程数:{}", maxPoolSize);

		String threadPrefix = clientName + "[" + this.serverIp + ":"
				+ this.serverPort + "]";
		// exector = Executors.newCachedThreadPool(new
		// NamingThreadFactory(threadPrefix));
		processor = new SimpleIoProcessorPool<NioSession>(NioProcessor.class,
				processorNum);

		// connector = new NioSocketConnector((Executor) exector, processor);
		connector = new NioSocketConnector(processor);

		// connector.getSessionConfig().setReuseAddress(true);
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();

		if (useLogFilter == 2) {
			chain.addLast("logging", new LoggingFilter());
		}
		// codec filter要放在ExecutorFilter前，因为读写同一个socket connection的socket
		// buf不能并发（事实上主要是读，写操作mina已经封装成一个write Queue)
		chain.addLast("codec", new ProtocolCodecFilter(codecFactory)); // 设置编码过滤器

		// 添加心跳过滤器,客户端只接受服务端的心跳请求，不发送心跳请求
		// connector.getSessionConfig().setReaderIdleTime(readIdleTimeOut);
		// 这里的KeepAliveFilter必须在codec之后，因为KeepAliveMessageFactoryImpl返回的是Object,如果KeepAliveMessageFactoryImpl返回的是IOBuffer,则可以在codec之前
		// KeepAliveFilter到底在ExecutorFilter之前好还是之后好，我也不确定
		KeepAliveFilter filter = new KeepAliveFilter(
				new KeepAliveMessageFactoryProtoImpl(keepAliveRequestInterval <= 0),
				IdleStatus.READER_IDLE, new RequestTimeoutCloseProtoHandler(),
				keepAliveRequestInterval <= 0 ? 600 : keepAliveRequestInterval,
				30);
		chain.addLast("ping", filter);

		// 添加执行线程池
		executor = new UnorderedThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS, new NamingThreadFactory(
						threadPrefix));

		// 这里是预先启动corePoolSize个处理线程
		executor.prestartAllCoreThreads();

		chain.addLast("exec", new ExecutorFilter(executor,
				IoEventType.EXCEPTION_CAUGHT, IoEventType.MESSAGE_RECEIVED,
				IoEventType.SESSION_CLOSED, IoEventType.SESSION_IDLE,
				IoEventType.SESSION_OPENED));

		if (useWriteThreadPool) {
			executorWrite = new UnorderedThreadPoolExecutor(corePoolSize,
					maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
					new NamingThreadFactory(threadPrefix + "write"));
			executorWrite.prestartAllCoreThreads();
			chain.addLast("execWrite", new ExecutorFilter(executorWrite,
					IoEventType.WRITE, IoEventType.MESSAGE_SENT));

		}
		// ,logger.isDebugEnabled() ? new
		// LoggingIoEventQueueHandler("execWrite") : nulls

		// 配置handler的 logger,在codec之后，打印的是decode前或者encode后的消息的log
		// 可以配置在ExecutorFilter之后：是为了在工作线程中打印log,不是在NioProcessor中打印
		if (useLogFilter == 1) {
			chain.addLast("logging", new LoggingFilter());
		}

		connector.setHandler(handler);

		connector.getSessionConfig().setReuseAddress(true);
		connector.getSessionConfig().setTcpNoDelay(tcpNoDelay);
		logger.info("连接中：" + serverIp + ":" + serverPort);
		ConnectFuture cf = null;

		long start = System.currentTimeMillis();
		while (true) {

			cf = connector.connect(serverAddress);// 建立连接
			cf.awaitUninterruptibly(10000L);
			if (!cf.isConnected()) {
				if ((System.currentTimeMillis() - start) > timeout) {
					throw new RuntimeException("连接超时：" + serverIp + ":"
							+ serverPort);
				}
				if (cf.getException() != null) {
					logger.error("连接异常{},10秒后重试:{}", serverIp + ":"
							+ serverPort, cf.getException().getMessage());
				}
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
				}

				continue;
			}

			this.setSession(cf.getSession());

			logger.info("连接成功：" + serverIp + ":" + serverPort);

			if (handler instanceof WolfMessageChain) {
				WolfMessageChain wmc = WolfMessageChain.class.cast(handler);
				wmc.init(context);
			}

			// Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			// public void run() {
			// if (context != null) {
			// stop();
			// }
			// }
			// }));

			break;
		}

	}

	public boolean isShutDown() {
		return shutDown;
	}

	public boolean isConnected() {
		return (this.session != null && !this.session.isClosing() && this.session
				.isConnected());
	}

	/**
	 * 提供重新建立连接的方法 连接成功返回 true，否则返回false
	 */
	synchronized boolean reconnect() {
		if (shutDown)
			return false;
		IoSession oldSession = this.session;
		if (this.session != null && !this.session.isClosing()
				&& this.session.isConnected()) {
			return true;
		}

		final ConnectFuture cf = connector.connect(this.serverAddress);// 建立连接
		logger.info("重新连接中：{}：{}", serverIp, serverPort);

		cf.awaitUninterruptibly(10000L);

		if (!cf.isConnected()) {
			if (cf.getException() != null) {
				logger.warn("连接异常：{}：{}", serverIp, serverPort);
			} else {
				logger.warn("连接超时：{}：{}", serverIp, serverPort);
			}
			return false;
		}

		this.setSession(cf.getSession());

		// this.session.write("tgw_l7_forward\r\nHost: "+serverIp+":"+serverPort+"\r\n\r\n");
		logger.info("连接成功：" + serverIp + ":" + serverPort);

		if (oldSession == null) {
			if (handler instanceof WolfMessageChain) {
				WolfMessageChain wmc = WolfMessageChain.class.cast(handler);
				wmc.init(context);
			}
		}

		this.login(this.loginTask);

		return true;

	}

	public void stop() {
		if (!shutDown) {
			logger.info("wolfClient[{}.{}] shutdown begin.............",
					serverIp, serverPort);
			this.shutDown = true;
			if (this.session != null) {
				CloseFuture future = this.session.close(true);
				// try {
				// future.wait();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				logger.info("wolfClient shutdown!");
			}

			if (this.connector != null) {
				this.connector.dispose();
			}

			if (this.processor != null) {
				this.processor.dispose();
			}

			if (executor != null) {
				executor.shutdown();
			}
			if (executorWrite != null) {
				executorWrite.shutdown();
			}
			logger.info("wolfClient[{}.{}] shutdown end.............",
					serverIp, serverPort);
		}
	}

	/**
	 * 可以发送任何类型数据了
	 * 
	 * @param task
	 */
	public WriteFuture asynSendTask(Object task) {
		if (task != null) {
			if (this.session == null || this.session.isClosing()
					|| !this.session.isConnected()) {
				throw new RuntimeException("连接已经断开，请重新建立连接！！");
			}

			return this.session.write(task);
		}

		return null;
	}

	/**
	 * 同步的信息发送 1 发送数据的时候，会生成一个requestid,并且附带一个Future； 2
	 * 相应的时候恒为SetResult类型，把结果放到Future中，并把Future设置为ready；
	 * 
	 * @param serviceName
	 *            在ServiceLocator 注册的service名字
	 * @param methodName
	 *            service的方法名字
	 * @param params
	 *            方法的参数 暂时不支持重载，所以方法的形参的显示类型一定要和实参的实际类型相同
	 * @param task
	 * @throws Exception
	 */
	public <T> T sendTask(Class<T> resultType, String serviceName,
			String methodName, Object... params) throws Exception {
		SyncWolfTask task = new SyncWolfTask();
		task.setParams(params);
		task.setMethodName(methodName);
		task.setServiceName(serviceName);

		return sendTask(resultType, task, syncRequestTimeout);

	}

	public <T> T sendTask(Class<T> resultType, SyncWolfTask task)
			throws Exception {
		return sendTask(resultType, task, syncRequestTimeout);
	}

	public <T> T sendTask(Class<T> resultType, SyncWolfTask task, long timeOut)
			throws Exception {
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
			o = future.get(timeOut, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			logger_server_err.error("[server_timeout]:", e);
			ResultMgr.requestCompleted(this.session, task.getRequestId(), e);
			throw e;
		}

		// if (logger.isDebugEnabled()) {
		// logger.debug("同步请求结束：{} ：{}", serviceName + "->" + methodName,
		// future.isDone() + " " + count);
		// }

		if (o == null) {
			return null;
		}

		if (o instanceof Exception) {
			throw Exception.class.cast(o);
		}

		// AMF encode/decode会把long转换成double,这里做判断
		if (o instanceof Double && resultType.isAssignableFrom(Long.class)) {

			Long tmp = ((Double) o).longValue();
			return resultType.cast(tmp);

		}
		// System.out.println("o:="+o);
		return resultType.cast(o);
	}

	/**
	 * 用来标记一种功能
	 * 
	 * @param task
	 */
	public Object login(NodeRegTask task) {
		this.session.write("tgw_l7_forward\r\nHost: " + serverIp + ":"
				+ serverPort + "\r\n\r\n");
		this.loginTask = task;
		try {
			return this.sendTask(Object.class, task, 60000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
