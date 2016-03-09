package com.youxigu.wolf.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.ResultMgr.TaskFuture;

/**
 * 管理连接到本Server的所有其他socket server的链接: game server,job server, async db server等等
 * 
 * 必须是通过RetTask注册过的连接，才作为Node Server
 * 
 * 
 * @author
 * 
 */
public class NodeSessionMgr {

	// public final static String NODE_USER_NUM_KEY = "USER_NUM";

	public final static String NODE_ADDR = "nodeAddr";

	public static final String SERVER_TYPE_KEY = "com.dynasty2.server.type";
	public static final String SERVER_TYPE_NAME_KEY = "com.dynasty2.server.type.name";

	public static final int SERVER_TYPE_NODE = 1; // game node server
	public static final int SERVER_TYPE_MAIN = 2;// 主server
	public static final int SERVER_TYPE_JOB = 4;// job server
	public static final int SERVER_TYPE_ASYNCDB = 8;// 异步DB server
	public static final int SERVER_TYPE_ACTIVE = 16;// 活动 server
	public static final int SERVER_TYPE_OTHER = 32;// 其他
	public static final int SERVER_TYPE_GM = 64;// GM工具

	public static final String SERVER_TYPE_NODE_STR = "nodeserver";// game node
	// server
	public static final String SERVER_TYPE_MAIN_STR = "mainserver";// 主server
	public static final String SERVER_TYPE_JOB_STR = "jobserver";// job server
	public static final String SERVER_TYPE_ASYNCDB_STR = "asyncdbserver";// 异步DB
	public static final String SERVER_TYPE_ACTIVE_STR = "activityserver";// 活动
	// server
	public static final String SERVER_TYPE_OTHER_STR = "other";// 其他
	public static final String SERVER_TYPE_GM_STR = "GM";// 其他
	/**
	 *通过用户名密码,防止不信任的客户端连接
	 */
	private static String adminUser;
	private static String adminPwd;
	/**
	 * 允许的IP列表，如果为空，则不检查IP
	 */
	private static Map<String, Object> ipMaps = new HashMap<String, Object>();
	private static int maxNodeUser = 5000;

	private static Logger logger = LoggerFactory
			.getLogger(NodeSessionMgr.class);

	// 避免遍历的时候进行更改操作带来的异常：hashMap 修改为 ConcurrentHashMap
	private static Map<String, IoSession> nodeSessionMap = new ConcurrentHashMap<String, IoSession>();

	// 独立出来所有的gameNode
	private static Map<String, IoSession> gameNodeSessionMap = new ConcurrentHashMap<String, IoSession>();

	// game server的IP+Port
	/**
	 * game server即是mainserver的客户端，又是flash client的server端 这里保存的是game
	 * Server作为flash client的server端的ip+port
	 * 
	 */
	private static Map<String, GmaeNodeAddress> gameNodeIpPortMap = new ConcurrentHashMap<String, GmaeNodeAddress>();
	private static volatile int roundCounter = 0; // 用于轮训计数
	// private static ResultMgr resultMgr = null;// 用于同步调用gameserver端
	/**
	 * 发出同步请求，等待多少毫秒不返回，则认为timeout
	 */
	private static long syncRequestTimeout = 20000;

	public void setSyncRequestTimeout(long syncRequestTimeout) {
		this.syncRequestTimeout = syncRequestTimeout;
	}

	public static void setAdminUser(String adminUser) {
		NodeSessionMgr.adminUser = adminUser;
	}

	public static void setAdminPwd(String adminPwd) {
		NodeSessionMgr.adminPwd = adminPwd;
	}

	/**
	 * 为Spring使用的Set方法
	 * 
	 * 
	 * @param maxNodeUser
	 */
	public static void setMaxUserNum(int maxNodeUser) {
		NodeSessionMgr.maxNodeUser = maxNodeUser;
	}

	public static void setAllowIps(String ipList) {
		if (ipList != null) {
			String[] ipArray = ipList.split(",");
			for (String ip : ipArray) {
				if (ip != null) {
					ip = ip.trim();
					if (ip.length() > 0) {
						ipMaps.put(ip, ip);
					}
				}
			}
		}

		// 加上自己的ip列表
		try {
			Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
			if (netInterfaces != null) {
				while (netInterfaces.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) netInterfaces
							.nextElement();
					InetAddress ip = (InetAddress) ni.getInetAddresses()
							.nextElement();
					// System.out.println("ip.isSiteLocalAddress()="
					// + ip.isSiteLocalAddress());
					// System.out.println("ip.isLoopbackAddress()="
					// + ip.isLoopbackAddress());

					String ipAddress = ip.getHostAddress();
					if (ipAddress.indexOf(":") == -1 && !ip.isLoopbackAddress()) {
						logger.info("加入本地信任IP:{}", ipAddress);
						ipMaps.put(ipAddress, ipAddress);
					} else {
						logger.info("忽略的本地IP:{}", ipAddress);
					}

				}
			}
		} catch (Exception e) {
			logger.error("获取本地ip出错", e);
		}

		logger.info("============列出所有信任ip=======");
		for (String ip : ipMaps.keySet()) {
			logger.info(ip);
		}
		logger.info("============列出所有信任ip完毕=======");
	}

	public static Map<String, Object> getAllowIps() {
		return ipMaps;
	}

	/**
	 * 注册gameserver连接
	 * 
	 * 1 如果存在当前名字的连接，而且处于连接状态，则关闭连接； 2 对于已经断开的连接，则进行连接删除
	 * 
	 * 
	 * 接受参数： <br>
	 * usr: admin的用户名 , 必须<br>
	 * pwd: admin的密码 , 必须 <br>
	 * serverType ： nodeServer的类型 ，可选，参见
	 * Response.SESSION_TYPE_GAMESERVER,Response.SESSION_TYPE_OTHERSERVER<br>
	 * nodeServerIp:，可选，<br>
	 * gameNodeSerber必须<br>
	 * nodeServerPort:可选，<br>
	 * gameNodeSerber必须提供
	 * 
	 * @param clientName
	 * @param ioSession
	 */
	public static void regIoSession(IoSession ioSession, Map params) {
		if (ioSession == null) {
			throw new RuntimeException("iosession不能为null");
		}

		InetSocketAddress remoteAddress = ((InetSocketAddress) ioSession
				.getRemoteAddress());

		String ip = remoteAddress.getAddress().getHostAddress();
		String clientName = remoteAddress.toString();// ip+port
		String usr = (String) params.remove("usr");
		String pwd = (String) params.remove("pwd");
		if (!(adminUser.equals(usr) && adminPwd.equals(pwd))) {
			// logger.info("无效的连接,请提供正确的用户名/密码", clientName, ioSession);
			throw new RuntimeException("无效的连接,请提供正确的用户名/密码");
		}

		if (ipMaps.size() > 0) {
			if (!ipMaps.containsKey(ip)) {
				throw new RuntimeException("不可信任ip:" + ip);
			}
		}

		Iterator<Entry<String, IoSession>> ir = nodeSessionMap.entrySet()
				.iterator();
		String key = null;
		IoSession value = null;
		Entry<String, IoSession> entry = null;
		while (ir.hasNext()) {
			entry = ir.next();
			key = entry.getKey();
			value = entry.getValue();
			if (key.equals(clientName)) {
				if (value.isConnected()) {
					value.close(false);
				}
			} else {
				if (!value.isConnected()) {
					ir.remove();
					logger.error("{} 的连接已经断开，请查看原因！！", key);
				}
			}

		}

		Integer nodeServerType = (Integer) params.remove("serverType");
		ioSession.setAttribute(SERVER_TYPE_KEY, nodeServerType);

		if (nodeServerType != null && nodeServerType == SERVER_TYPE_NODE) {
			logger.info("有game server 连接上来：{} ", clientName);

			// 连接用户计数器
			// ioSession.setAttribute(NODE_USER_NUM_KEY, new AtomicInteger());

			String clientServerIp = (String) params.get("nodeServerIp");
			if (clientServerIp != null) {
				int port = (Integer) params.get("nodeServerPort");
				GmaeNodeAddress address = new GmaeNodeAddress(clientName,
						clientServerIp, port);
				ioSession.setAttribute(NODE_ADDR, address);
				// ioSession.setAttribute("nodeServerPort", port);
				// String ipPort = clientServerIp+":"+port;
				gameNodeIpPortMap.put(address.key, address);
				gameNodeSessionMap.put(clientName, ioSession);
				logger.info("gameServer 提供的 socket address:{}:{}",
						clientServerIp, port);
			} else {
				throw new RuntimeException("game server没有设置 ip port");
			}

		} else {
			logger.info("有 server 连接上来：{} - > {}", clientName, ioSession);
		}
		ioSession.setAttribute("REMOTE_ADDR", clientName);
		//附带的参数
		Iterator<Map.Entry> lit =  params.entrySet().iterator();
		while (lit.hasNext()){
			Map.Entry entry1 = lit.next();
			ioSession.setAttribute(entry1.getKey(),entry1.getValue());
		}
		nodeSessionMap.put(clientName, ioSession);

		// 设置信任标志位
		ioSession.setAttribute(Response.CREDIT, 1);

	}

	public static void unRegister(IoSession session) {
		String key = (String) session.getAttribute("REMOTE_ADDR");
		if (key == null) {
			key = session.getRemoteAddress().toString();
		}
		nodeSessionMap.remove(key);
		IoSession gameNodeSession = gameNodeSessionMap.remove(key);
		if (gameNodeSession != null) {
			GmaeNodeAddress address = (GmaeNodeAddress) session
					.getAttribute(NODE_ADDR);
			if (address != null) {
				gameNodeIpPortMap.remove(address.key);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("game node server 中断：{}:{}", address.ip,
						address.port);
			}

		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("server 中断：{}", key);
			}
		}
//		Iterator<Object> lit = session.getAttributeKeys().iterator();
//		try {
//			while (lit.hasNext()) {
//				lit.next();
//				lit.remove();
//			}
//		} catch (Exception e) {
//
//		}

	}

	// public static void setResultMgr(ResultMgr resultMgr) {
	// NodeSessionMgr.resultMgr = resultMgr;
	// }

	/**
	 * 
	 *用户登录后选择游戏服务器：<br>
	 *1.如果是第一次登录：以用户账号%游戏服务器总数，选择游戏服务器,如果该服务器已经满了或者有异常，则选择最后一个可用的服务器。<br>
	 *2.如果不是第一次登录，先找上一次登录的服务器，如果该服务器可用,则选择该服务器,否则，当作第一次登陆处理<br>
	 * 
	 *TODO：用户是可能登陆到不同的服务器上的：上次登陆的服务器满了，或者不可用
	 * 
	 * 
	 * @param accountId
	 * @return
	 */
	public static GmaeNodeAddress getRandomNode(IoSession ioSession,
			long accountId, String lastNodeAddr, boolean isnew) {
		int nodeCount = gameNodeSessionMap.size();
		if (gameNodeSessionMap.size() <= 0) {
			throw new RuntimeException("没有可用的游戏服务器");
		}

		IoSession canUseClient = null;
		GmaeNodeAddress lastAddress = null;

		if (lastNodeAddr != null) {
			lastAddress = gameNodeIpPortMap.get(lastNodeAddr);
			if (lastAddress != null && lastAddress.canEnter()) {
				canUseClient = gameNodeSessionMap.get(lastAddress.clientName);
			}
		}

		if (canUseClient == null) {
			lastAddress = null;
			int modeNum = (int) (accountId % nodeCount);
			Iterator<IoSession> clients = gameNodeSessionMap.values()
					.iterator();
			int i = 0;

			IoSession client = null;
			while (clients.hasNext()) {
				client = clients.next();
				if (client.isConnected()) {
					GmaeNodeAddress address = (GmaeNodeAddress) client
							.getAttribute(NODE_ADDR);
					if (address != null) {
						if (address.canEnter() || isnew) {// 没有达到人数上限，或者没有创建角色的都可以进入
							if (i == modeNum) {
								canUseClient = client;
								lastAddress = address;
								break;
							} else {
								i++;
								canUseClient = client; // 没有找到modeNum,则返回最后一个可登录的服务器
								lastAddress = address;
							}
						}
					}
				}
			}
		}
		return lastAddress;
	}

	/**
	 * 客户端连接退出Node,这里主要是为了修改每个node上的连接计数
	 * 
	 * @param ioSession
	 */
	public static void exitNode(IoSession ioSession) {
		GmaeNodeAddress address = (GmaeNodeAddress) ioSession
				.removeAttribute(NODE_ADDR);
		if (address != null) {
			address.decreaseNum();
		}
	}

	/**
	 * 使用不特定客户端发送任务
	 * 
	 * @param task
	 *            true 有可用的链接 发送成功 false 没有可用的链接发送失败
	 */
	private static String sendMessage(WolfTask task) {
		if (gameNodeSessionMap.size() <= 0) {
			logger.error("还没有可供使用的game server！！");
			return null;
		}

		try {
			// 找到模对应的session
			IoSession client = getRoundRobinClient();
			if (client == null) { // 轮询的这个不可用，就找一个可用的
				client = getAvaiableNode();
			}
			sendMessage(client, task);
			return client.toString();
		} catch (Exception e) {
			logger.error("数据发送失败：没有可用的game Server！！");

			return null;
		}
	}

	/**
	 * 根据轮询参数去找一个可用的tomcat
	 * 
	 * @param curCounter
	 * @return
	 */
	private static IoSession getRoundRobinClient() {
		int curCounter = roundCounter++;
		if (curCounter > Integer.MAX_VALUE - 1000) {
			roundCounter = 0;
		}
		int maxNodes = gameNodeSessionMap.size();
		if (maxNodes == 0)
			return null;
		int index = curCounter % maxNodes;
		Iterator<IoSession> clients = gameNodeSessionMap.values().iterator();
		int i = -1;

		IoSession client = null;
		while (clients.hasNext()) {
			client = clients.next();
			if (index == ++i && client.isConnected()) {
				break;
			}
		}

		return client;
	}

	/**
	 * 是否有可用的链接,初始化job时调用
	 * 
	 * @return
	 */
	public static boolean hasConn() {
		return gameNodeSessionMap.size() > 0;
	}

	public static int getGameNodeCount() {
		return gameNodeSessionMap.size();
	}

	public static int getAllNodeCount() {
		return nodeSessionMap.size();
	}

	/**
	 * 用特定客户端发送任务
	 * 
	 * @param nodeName
	 * @param obj
	 * @return true 表示成功
	 */
	public static boolean sendMessage(String nodeName, Object obj) {
		IoSession ioSession = getGameNodeIoSessionByNodeName(nodeName);
		if (ioSession != null) {
			return sendMessage(ioSession, obj);
		}

		return false;
	}

	public static IoSession getGameNodeIoSessionByNodeName(String nodeName) {
		IoSession iosession = null;
		GmaeNodeAddress address = gameNodeIpPortMap.get(nodeName);
		if (address != null) {
			iosession = gameNodeSessionMap.get(address.clientName);
		}
		if (iosession != null) {
			return iosession;
		} else {
			iosession = gameNodeSessionMap.get(nodeName);
			if (iosession != null) {
				return iosession;
			}
		}
		return iosession;

	}

	/**
	 * 给所有注册到本服务器的node异步广播消息
	 * 
	 * @param obj
	 */
	public static void broadcastMessages(Object obj) {
		// ioSession.setAttribute(SERVER_TYPE_KEY, nodeServerType);
		// nodeSessionMap
		Collection<IoSession> ios = nodeSessionMap.values();// gameNodeSessionMap.values();
		for (IoSession ioSession : ios) {

			if (ioSession != null && ioSession.isConnected()) {
				// Integer type =
				// (Integer)ioSession.getAttribute(SERVER_TYPE_KEY);
				// if (type)
				ioSession.write(obj);
			}
		}
	}

	public static void broadcastMessages(String serviceName, String methodName,
			Object[] params) {
		AsyncWolfTask appBaseTask = new AsyncWolfTask(serviceName, methodName,
				params);
		Collection<IoSession> ios = nodeSessionMap.values();// gameNodeSessionMap.values();
		for (IoSession ioSession : ios) {

			if (ioSession != null && ioSession.isConnected()) {
				// Integer type =
				// (Integer)ioSession.getAttribute(SERVER_TYPE_KEY);
				// if (type)
				ioSession.write(appBaseTask);
			}
		}
	}
	
	/**
	 * 按名称取得iosession
	 * @param address
	 * @return
	 */
	public static IoSession getIoSession(String address) {
		IoSession ioSession = nodeSessionMap.get(address);
		if (ioSession != null) {
			if (ioSession.isConnected()) {
				return ioSession;
			}
		}
		return null;
	}

	/**
	 * 异步调用gameServer端服务
	 * 
	 * @param nodeName
	 * @param serviceName
	 * @param methodName
	 * @param params
	 * @return
	 */
	public static String sendMessage2Node(String nodeName, String serviceName,
			String methodName, Object[] params) throws Exception {
		AsyncWolfTask appBaseTask = new AsyncWolfTask(serviceName, methodName,
				params);

		IoSession ioSession = null;
		if (nodeName != null) {
			ioSession = getGameNodeIoSessionByNodeName(nodeName);
		}
		boolean res = false;
		if (ioSession != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("发送方法请求,method={}.{}到{}", new Object[] {
						serviceName, methodName, nodeName });
			}
			res = NodeSessionMgr.sendMessage(ioSession, appBaseTask);
		}
		if (res) {
			return ioSession.toString();
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("随机选择gameServer执行方法");
			}
			String tmp = NodeSessionMgr.sendMessage(appBaseTask);
			if (tmp == null) {
				throw new IOException("没有可用的客户端连接");
			} else {
				return tmp;
			}
		}

	}

	/**
	 * 使用特定gameServer发送任务
	 * 
	 * @param ioSession
	 * @param task
	 */
	public static boolean sendMessage(IoSession ioSession, Object task) {
		try {
			if (ioSession != null && ioSession.isConnected()) {
				ioSession.write(task);
				return true;
			} else {
				logger.error("连接已经关闭，请检查网络连接是否正常！！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 在列表中找一个可用的连接使用
	 * 
	 * @return
	 */
	private static IoSession getAvaiableNode() {
		Set<Entry<String, IoSession>> entrySet = gameNodeSessionMap.entrySet();
		Iterator<Entry<String, IoSession>> ir = entrySet.iterator();

		IoSession value = null;
		Entry<String, IoSession> entry = null;
		while (ir.hasNext()) {
			value = entry.getValue();

			if (value.isConnected()) {
				return value;
			}
		}

		return null;
	}

	public static Collection<IoSession> getNodeSessions() {
		return gameNodeSessionMap.values();
	}
	
	public static Collection<IoSession> getAllSessions() {
		return nodeSessionMap.values();
	}

	/**
	 * 同步调用客户端service
	 * 
	 * @param <T>
	 * @param nodeName
	 * @param resultType
	 * @param task
	 * @return
	 * @throws Throwable
	 */
	public static <T> T sendTask(String nodeName, Class<T> resultType,
			SyncWolfTask task) throws Exception {
		IoSession ioSession = null;
		if (nodeName != null) {
			ioSession = getGameNodeIoSessionByNodeName(nodeName);
		}

		if (ioSession == null) {
			ioSession = getRoundRobinClient();
		}
		return sendTask(ioSession, resultType, task);

	}

	public static <T> T sendTask(IoSession ioSession, Class<T> resultType,
			SyncWolfTask task) throws Exception {

		if (ioSession == null) {
			throw new IOException("没有可用的客户端连接");
		}
		if (ioSession.isClosing() || !ioSession.isConnected()) {
			throw new IOException("连接已经断开，请重新建立连接！！");
		}
		TaskFuture<Object> future = ResultMgr.requestSent(ioSession, task
				.getRequestId());
		ioSession.write(task);

		if (logger.isDebugEnabled()) {
			logger.debug("同步请求开始：{} -> {}", task.getServiceName(), task
					.getMethodName());
		}

		Object o = null;
		try {
			o = future.get(syncRequestTimeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			ResultMgr.requestCompleted(ioSession, task.getRequestId(), e);
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("同步请求结束：{} ->{}", task.getServiceName(), task
					.getMethodName());
		}

		if (o == null) {
			return null;
		}

		if (o instanceof Exception) {
			throw (Exception) o;
		}

		if (Void.class == resultType) {
			return null;
		}

		return resultType.cast(o);
	}

	public static void showUsersOnGameNode() {
		Iterator<IoSession> lit = gameNodeSessionMap.values().iterator();
		while (lit.hasNext()) {
			IoSession session = lit.next();
			GmaeNodeAddress address = (GmaeNodeAddress) session
					.getAttribute(NODE_ADDR);
			if (address != null) {
				logger.debug("node server:{}:{} 上的用户数:{},", new Object[] {
						address.ip, address.port, address.clientNum.get() });
			}
		}

	}

	static class GmaeNodeAddress {

		public AtomicInteger clientNum = new AtomicInteger();
		public String clientName;
		public String ip;
		public int port;
		public String key;

		public GmaeNodeAddress(String clientName, String ip, int port) {

			this.clientName = clientName;
			this.ip = ip;
			this.port = port;
			this.key = this.toString();

		}

		public String toString() {
			if (key == null) {
				key = ip + ":" + port;
			}
			return key;
		}

		public void increaseNum() {
			clientNum.incrementAndGet();
		}

		public boolean canEnter() {
			return clientNum.get() < maxNodeUser;
		}

		public boolean isFull() {
			return clientNum.get() > maxNodeUser;
		}

		public void decreaseNum() {
			clientNum.decrementAndGet();
		}
	}

	public static boolean matchServer(int serverType, int targetServerType) {
		return (serverType & targetServerType) != 0;
	}
}
