package com.youxigu.wolf.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.Quser;
import com.youxigu.dynasty2.openapi.service.IFaceBookService;
import com.youxigu.dynasty2.openapi.service.ITecentMobileQqService;
import com.youxigu.dynasty2.openapi.service.ITecentUnionService;
import com.youxigu.dynasty2.openapi.service.ITecentWeiXinService;
import com.youxigu.wolf.net.NodeSessionMgr.GmaeNodeAddress;

/**
 * 管理所有的flash 客户端连接，不包括本身是server的连接
 * 
 * @author Administrator
 * 
 */
public class OnlineUserSessionManager implements ISessionListener {
	public static final Logger log = LoggerFactory.getLogger(OnlineUserSessionManager.class);

	/**
	 * 缓存所有的login后的客户端连接 key = sessionId value = Iosession
	 */
	private static Map<String, IoSession> onlineIoSessions = new ConcurrentHashMap<String, IoSession>();

	/**
	 * 缓存用户accID与IoSession的映射
	 */
	private static Map<Long, String> accIdSidMappings = new ConcurrentHashMap<Long, String>();

	/**
	 * 缓存用户Id与iosession的映射
	 */
	private static Map<Long, IoSession> userIdSidMappings = new ConcurrentHashMap<Long, IoSession>();

	private static int checkPeriod = 1000 * 60 * 60;// 60分钟检查一次
	private static int period = 1000 * 60 * 55;// 距离上次检查超过55分钟则检查
	private static int maxNodeUser = 5000;
	private static ITecentMobileQqService openApi_qq = null;
	private static ITecentWeiXinService openApi_wx = null;
	private static ITecentUnionService openApi_union = null;
	private static IFaceBookService openApi_facebook = null;
	private static boolean shutDown = false;
	
	private static int COUNT_LOGINNUM4_FIVEMIN = 0;// 5分钟内的登陆人数

	/**
	 * 前台很让人纠结,只要弹出tx的各种界面,必定掉线,这里保留吊线后的dirty的UserSession<br>
	 * , 保留300个dirty UserSession, 防止支付的时候找不到各种支付相关的数据
	 * 
	 */
	public static final int DIRTY_SESSION_NUM = 300;
	private static ConcurrentLinkedHashMap<Long, UserSession> dirty_accId_us_Map = null;
	private static ConcurrentLinkedHashMap<Long, UserSession> dirty_userId_us_Map = null;
	static {
		ConcurrentLinkedHashMap.Builder<Long, UserSession> builder = new ConcurrentLinkedHashMap.Builder<Long, UserSession>();
		// builder.concurrencyLevel(32);
		builder.initialCapacity(DIRTY_SESSION_NUM);
		builder.maximumWeightedCapacity(DIRTY_SESSION_NUM);
		builder.concurrencyLevel(16);

		dirty_accId_us_Map = builder.build();

		builder = new ConcurrentLinkedHashMap.Builder<Long, UserSession>();
		// builder.concurrencyLevel(32);
		builder.initialCapacity(DIRTY_SESSION_NUM);
		builder.maximumWeightedCapacity(DIRTY_SESSION_NUM);
		builder.concurrencyLevel(16);
		dirty_userId_us_Map = builder.build();

	}

	public static void init() {

	}

	public void setCheckPeriod(int checkPeriod) {
		OnlineUserSessionManager.checkPeriod = checkPeriod;
	}

	// private
	public static void register(UserSession us, IoSession is) {
		boolean isMainServer = String.valueOf(NodeSessionMgr.SERVER_TYPE_MAIN).equals(
				System.getProperty(NodeSessionMgr.SERVER_TYPE_KEY));
		if (isMainServer) {
			_checkLogin(us);
		}

		UserSession oldUserSession = (UserSession) is.getAttribute(UserSession.KEY_USERSESSION);

		if (oldUserSession != null) {

			if (oldUserSession != us) {
				// ///这种情况应该不可能出现，
				if (log.isDebugEnabled()) {
					log.debug("重复登陆，删除旧session");
				}
				// 只有main Server执行这个,mainserver上的game server
				// session中保存了连接的客户端数量，这里清除

				Object obj = onlineIoSessions.remove(oldUserSession.getSessionId());
				if (obj != null) {
					NodeSessionMgr.exitNode(is);
				}

				accIdSidMappings.remove(oldUserSession.getAccId());
				if (oldUserSession.getUserId() > 0) {
					userIdSidMappings.remove(oldUserSession.getUserId());
				}
			} else {
				if (!userIdSidMappings.containsKey(oldUserSession.getUserId())) {
					userIdSidMappings.put(oldUserSession.getUserId(), is);
				}

				return;
			}
		}

		String sid = accIdSidMappings.get(us.getAccId());
		if (sid != null) {
			IoSession ioSession = onlineIoSessions.get(sid);
			if (ioSession != null && ioSession != is) {

				if (isMainServer) {
					notyfyNodeServerLogout(ioSession, us);
				}

				// 已经登陆过了,关闭原来的 session

				// 这里close,会激发WolfMessageChain中配置的ISessionListener.closed
				// 但是ioSession.close()是异步的，ISessionListener.closed会比下面的注册逻辑后执行
				// 因此,必须等待close完成
				ResponseHead.Builder badRequest = ResponseHead.newBuilder();
				badRequest.setCmd(IAMF3Action.CMD_DEFAULT);
				badRequest.setErrCode(-9010);
				badRequest.setErr("您的账号在另一个地点登录，您被迫下线。<br>如果这不是您本人的操作，那么您的密码可能已经泄露。建议修改密码。");
				badRequest.setRequestCmd(IAMF3Action.CMD_LOGIN);
				ioSession.write(badRequest.build());
				ioSession.close(false);
				try {
					synchronized (ioSession) {
						ioSession.wait(1000L);// 等待10秒或者WolfMessageChain.closed中的nofify。
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				NodeSessionMgr.exitNode(ioSession);

				if (log.isDebugEnabled()) {
					log.debug("重复登陆，关闭旧连接");
				}
			}
		}

		// 分配game server ip and port,如果本身就是ganeserver,不用这一步
		if (isMainServer) {

			GmaeNodeAddress address = NodeSessionMgr.getRandomNode(is, us.getAccId(), us.getGameNodeName(),
					us.getUserId() == 0);
			if (address == null) {
				is.setAttribute(NodeSessionMgr.NODE_ADDR, address);
				is.setAttribute(UserSession.KEY_USERSESSION, us);

				return;

			} else {

				address.increaseNum();
				is.setAttribute(NodeSessionMgr.NODE_ADDR, address);

				if (log.isDebugEnabled()) {
					log.debug("用户{}加入gameNode:{},当前人数:" + address.clientNum.get(), us.getAccId(), address.key);
				}
				
				COUNT_LOGINNUM4_FIVEMIN = COUNT_LOGINNUM4_FIVEMIN +1;//统计5分钟内的登陆人数

				us.setGameNodeName(address.key);
				us.setGameServerIp(address.ip);
				us.setGameServerPort(address.port);
			}

		} else {
			if (log.isDebugEnabled()) {
				log.debug("用户{}加入gameNode:", us.getAccId());
			}
		}

		is.setAttribute(UserSession.KEY_USERSESSION, us);
		accIdSidMappings.put(us.getAccId(), us.getSessionId());
		onlineIoSessions.put(us.getSessionId(), is);
		if (us.getUserId() > 0) {
			userIdSidMappings.put(us.getUserId(), is);
		}
		// 如果已经信任过,就不在设置:后台为了测试，可能要模拟成Response.CREDIT=1
		if (!is.containsAttribute(Response.CREDIT)) {
			is.setAttribute(Response.CREDIT, 2);// 设置信任标志位
		}

		// new Thread(new Runnable(){
		//
		// @Override
		// public void run() {
		//				
		// try{
		// Thread.sleep(5000);
		// Iterator<IoSession> lit = onlineIoSessions.values().iterator();
		// while (lit.hasNext()){
		// close(lit.next());
		// System.out.println("====");
		// }
		// }
		// catch (Exception e){
		// e.printStackTrace();
		// }
		//				
		// }
		//			
		// }).start();
	}

	public static boolean registerFromQueue(UserSession us, IoSession is) {
		GmaeNodeAddress address = NodeSessionMgr.getRandomNode(is, us.getAccId(), us.getGameNodeName(),
				us.getUserId() == 0);
		if (address == null) {
			return false;
		}
		address.increaseNum();
		is.setAttribute(NodeSessionMgr.NODE_ADDR, address);

		if (log.isDebugEnabled()) {
			log.debug("用户{}加入gameNode:{},当前人数:" + address.clientNum.get(), us.getAccId(), address.key);
		}

		us.setGameNodeName(address.key);
		us.setGameServerIp(address.ip);
		us.setGameServerPort(address.port);

		accIdSidMappings.put(us.getAccId(), us.getSessionId());
		onlineIoSessions.put(us.getSessionId(), is);
		if (us.getUserId() > 0) {
			userIdSidMappings.put(us.getUserId(), is);
		}
		is.setAttribute(UserSession.KEY_USERSESSION, us);
		is.setAttribute(Response.CREDIT, 2);// 设置信任标志位
		return true;
	}

	public static Map<Long, IoSession> getUserIdSidMappings() {
		return userIdSidMappings;
	}

	public static void setUserIdSidMappings(Map<Long, IoSession> userIdSidMappings) {
		OnlineUserSessionManager.userIdSidMappings = userIdSidMappings;
	}

	/**
	 * mainserver发送来的关闭请求
	 * 
	 * @param userId
	 */
	public static void unRegisterByUserId(long userId) {
		IoSession ioSession = userIdSidMappings.get(userId);
		if (ioSession != null) {
			ioSession.close(false);
		}
	}

	public static void unRegisterByAccId(long accId) {
		String sid = accIdSidMappings.get(accId);
		if (sid != null) {
			IoSession ioSession = onlineIoSessions.get(sid);
			if (ioSession != null) {
				// close(ioSession);
				ioSession.close(false);
			}
		}
	}

	public static void unRegister(Response context) {
		IoSession ioSession = context.getSession();
		if (ioSession != null) {
			ioSession.close(false);
		}

		// 处理其他游戏业务需要的unRegister
	}

	public static IoSession getIoSessionByAccId(long accId) {
		String sid = accIdSidMappings.get(accId);
		return onlineIoSessions.get(sid);
	}

	public static IoSession getIoSessionByUserId(long userId) {
		return userIdSidMappings.get(userId);
	}

	public static IoSession getIoSession(String sid) {
		return onlineIoSessions.get(sid);
	}

	public static UserSession getUserSession(String sid) {

		IoSession ioSession = onlineIoSessions.get(sid);
		if (ioSession != null) {
			return (UserSession) ioSession.getAttribute(UserSession.KEY_USERSESSION);
		}
		return null;
	}

	public static UserSession getUserSessionByAccId(long accId) {
		String sid = accIdSidMappings.get(accId);
		if (sid != null) {
			return getUserSession(sid);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("玩家不在线，取dirty缓存数据");
			}
			return dirty_accId_us_Map.get(accId);
		}
	}

	public static UserSession getUserSessionByUserId(long userId) {
		IoSession ioSession = userIdSidMappings.get(userId);
		if (ioSession != null) {
			return (UserSession) ioSession.getAttribute(UserSession.KEY_USERSESSION);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("玩家不在线，取dirty缓存数据");
			}
			return dirty_userId_us_Map.get(userId);
		}

	}

	public static int getCurrentOnlineUserNum() {

		return onlineIoSessions.size();
	}
	
	/**
	 * 5分钟内的累计登陆
	 * @return
	 */
	public static int getCountLoginnum4Fivemin() {
		int tmp = COUNT_LOGINNUM4_FIVEMIN;
		COUNT_LOGINNUM4_FIVEMIN = 0;
		return tmp;
	}

	/**
	 * 用户是否在线
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean isOnline(long userId) {
		return userIdSidMappings.containsKey(userId);
	}

	/**
	 * 
	 * @param userIds
	 * @return
	 */
	public static Map<Long, Boolean> isOnlines(long[] userIds) {
		Map<Long, Boolean> datas = new HashMap<Long, Boolean>();
		for (long id : userIds) {
			datas.put(id, userIdSidMappings.containsKey(id));
		}
		return datas;
	}

	public static void removeOverloadUsers() {
		Iterator<IoSession> lit = onlineIoSessions.values().iterator();
		while (lit.hasNext()) {
			IoSession session = lit.next();
			GmaeNodeAddress address = (GmaeNodeAddress) session.getAttribute(NodeSessionMgr.NODE_ADDR);
			if (address != null) {
				if (address.isFull()) {
					NodeSessionMgr.exitNode(session);
					lit.remove();
					session.close(false);
				}
			}
		}

	}

	public static boolean isOnlineByAccId(long accId) {
		return accIdSidMappings.containsKey(accId);
	}

	public static void close(IoSession ioSession, boolean remove) {
		// Thread.dumpStack();

		final UserSession us = (UserSession) ioSession.removeAttribute(UserSession.KEY_USERSESSION);

		// NodeSessionMgr.exitNode(ioSession);

		if (us != null) {
			// 只有main Server执行这个,mainserver上的game server
			// session中保存了连接的客户端数量，这里清除
			NodeSessionMgr.exitNode(ioSession);
			onlineIoSessions.remove(us.getSessionId());
			// Object obj = remove ? onlineIoSessions.remove(us.getSessionId())
			// : onlineIoSessions.get(us.getSessionId());
			// if (obj != null) {
			// NodeSessionMgr.exitNode(ioSession);
			// }
			accIdSidMappings.remove(us.getAccId());
			userIdSidMappings.remove(us.getUserId());

			// 如果是mainServer//通知node server注销
			if ((String.valueOf(NodeSessionMgr.SERVER_TYPE_MAIN).equals(System
					.getProperty(NodeSessionMgr.SERVER_TYPE_KEY)))) {
				notyfyNodeServerLogout(ioSession, us);
				if (log.isDebugEnabled()) {
					log.debug("mainserver用户下线：{}", us.getAccId());
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("gameserver用户下线：{}", us.getAccId());
				}
			}

			dirty_accId_us_Map.put(us.getAccId(), us);
			dirty_userId_us_Map.put(us.getUserId(), us);

		} else {
			// 对于mainserver,login出异常，这个时候usersession就是没有，是正常现象
			// 其他情况下不应该出现usersession丢失
			// log.error("usersession丢失");
		}

		// Iterator<Object> lit = ioSession.getAttributeKeys().iterator();
		// try {
		// while (lit.hasNext()) {
		// lit.next();
		// lit.remove();
		// }
		// } catch (Exception e) {
		// }

		// ioSession.close(false);
	}

	/**
	 * 通知nodeserver logout
	 * 
	 * @param us
	 */
	public static void notyfyNodeServerLogout(IoSession ioSession, UserSession us) {
		if (!ioSession.containsAttribute("_logout")) {
			if (log.isDebugEnabled()) {
				log.debug("通知gameserver：{},用户{}下线", us.getGameNodeName(), us.getAccId());
			}
			// 通知gameServer 让上一次登录的socket connect 断开
			// SyncWolfTask task = new SyncWolfTask();
			// task.setServiceName("accountService");
			// task.setMethodName("logout");
			// task.setParams(new Object[] { us.getAccId(),us.getSessionId() });
			try {
				NodeSessionMgr.sendMessage2Node(us.getGameNodeName(), "accountService", "logout",
						new Object[] { us.getAccId(), us });
			} catch (Exception e) {
				e.printStackTrace();
			}
			ioSession.setAttribute("_logout", "1");
		}
		// } else {
		// ioSession.removeAttribute("_logout");
		// }
	}

	public static boolean sendMessage(long userId, Object msg) {
		if(msg==null){
			return false;
		}
		IoSession session = getIoSessionByUserId(userId);
		if (session != null) {
			session.write(msg);
			return true;
		}
		// } else {
		// if (log.isDebugEnabled()) {
		// log.debug("用户不在线:{}", userId);
		// }
		// }
		return false;
	}

	@Override
	public void close(Response response) {
		// if (!response.isNodeServerSession()) {
		IoSession ioSession = response.getSession();
		close(ioSession, true);
		// }

	}

	@Override
	public void open(Response response) {

	}

	private static void _checkLogin(UserSession us) {
		if (Constant.isTestEnv()) {
			log.warn("内网环境,不进行校验");
			return;
		}
		if (openApi_qq == null) {
			synchronized (OnlineUserSessionManager.class) {
				if (openApi_qq == null) {
					openApi_qq = (ITecentMobileQqService) ServiceLocator.getSpringBean("tecentMobileQqService");
					openApi_wx = (ITecentWeiXinService) ServiceLocator.getSpringBean("tecentWeiXinService");
					openApi_union = (ITecentUnionService) ServiceLocator.getSpringBean("tecentUnionService");
					openApi_facebook = (IFaceBookService) ServiceLocator.getSpringBean("faceBookService");
					if (openApi_qq != null) {
						Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
							public void run() {
								shutDown = true;
								if (openApi_qq != null) {
									synchronized (openApi_qq) {
										openApi_qq.notifyAll();
									}
								}
							}
						}));

						new Thread(new Runnable() {

							@Override
							public void run() {
								batchCheckLogin();
							}
						}, "openApiLoginChecker").start();
					}
				}
			}
		}

		checkLogin(us);

	}

	public static void checkLogin(UserSession us) {

		if (us.isMobileQQ()) {// qq平台验证
			String openid = us.getOpenid();
			String openkey = us.getOpenkey();
			openApi_qq.verifyLogin(openid, openkey, null);
			try {
				// List<QQScoreReport> datas = new ArrayList<QQScoreReport>();
				// QQScoreReport aaa = new QQScoreReport();
				// aaa.setData("1000");
				// aaa.setType(3);
				// aaa.setExpireds("0");
				// datas.add(aaa);
				// openApi_qq.qqscoreBatch(openid, openkey,datas);
				Object quser = openApi_qq.profile(openid, openkey);
				if (quser != null) {
					us.setQuser(quser);
					// VIP数据
					openApi_qq.loadVip(openid, openkey, (Quser) quser);
					if (log.isDebugEnabled()) {
						log.debug("qq昵称:{}", ((Quser) quser).getNickname());
					}
				}
			} catch (Exception e) {
				log.error("取QQ玩家数据出错", e);
			}
		} else if (us.isWeixin()) {// wx平台验证
			openApi_wx.checkToken(us.getOpenid(), us.getOpenkey());
			// openApi_wx.wxScore(us.getOpenid(), "1000", 0);
			Map<String, Object> profiles = null;
			try {
				profiles = openApi_wx.userInfo(us.getOpenid(), us.getOpenkey());

			} catch (Exception e) {
				log.error("取微信玩家数据出错", e);
				// do nothing
			}
			try {
				Map<String, Object> vipInfo = openApi_wx.getVip(us.getOpenid(), us.getOpenkey());
				if (profiles == null) {
					profiles = vipInfo;
				} else {
					if (vipInfo != null) {
						profiles.putAll(vipInfo);
					}
				}

			} catch (Exception e) {
				log.error("取微信玩家VIP数据出错", e);
				// do nothing
			}
			us.setQuser(profiles);
			// openApi_wx.
			// openApi_wx.refreshToken(us.getOpenid(),
			// us.getRefreshToken());
		} else if (us.isVistor()) {
			// TODO:游客暂时不鉴权了
			if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
				if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
					openApi_qq.verifyVistorLogin(us.getOpenid(), us.getOpenkey(), null);
				} else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
					openApi_wx.verifyVistorLogin(us.getOpenid(), us.getOpenkey(), null);
				}
			}
		} else if (us.isUnion()) {
			// 联运
			String openid = us.getOpenid();
			String openkey = us.getOpenkey();
			openApi_union.verifyLogin(openid, openkey);
			Map<String, Object> profiles = null;
			try {
				profiles = openApi_union.profile(openid, openkey);
			} catch (Exception e) {
				log.error("取微信玩家数据出错", e);
				// do nothing
			}
			us.setQuser(profiles);
		} else if (us.isFaceBook()) {
			//md5(appkey+efun_uid+时间戳)32位大写
			openApi_facebook.verifyLogin(us.getOpenkey(), us.getOpenid(), us.getPay_token());

		}

	}

	private static void batchCheckLogin() {
		try {
			synchronized (openApi_qq) {
				openApi_qq.wait(checkPeriod);
			}
		} catch (Exception e) {
			log.error("检查登陆信息同步异常", e);
		}
		log.info("开放平台登录状态检查线程启动");

		while (!shutDown) {
			List<IoSession> invalidIoSessions = new ArrayList<IoSession>();
			Iterator<IoSession> lit = onlineIoSessions.values().iterator();
			try {
				if (log.isDebugEnabled()) {
					log.debug("检查开放平台玩家状态.......");
				}
				while (lit.hasNext()) {
					IoSession ios = lit.next();
					if (ios.isConnected()) {
						UserSession us = (UserSession) ios.getAttribute(UserSession.KEY_USERSESSION);
						if (us != null) {
							// 间隔大于
							long now = System.currentTimeMillis();
							long lastCheckTime = us.getLastChkTime();
							if ((now - lastCheckTime) > period) {
								try {
									// if (log.isDebugEnabled()) {
									// log.debug("检查开放平台玩家状态:{},{}", us
									// .getAccId(), us.getAccName());
									//
									// }
									if (us.isMobileQQ()) {
										boolean isLogin = openApi_qq.verifyLogin(us.getOpenid(), us.getOpenkey(), null);
										if (!isLogin) {
											log.warn("开放平台登录超时，强制玩家下线:{},{}", us.getAccId(), us.getAccName());
											if (lastCheckTime > 0) {
												// 第一次出现异常，放过
												us.setLastChkTime(-1);
											} else {
												// 第二次出现异常，下线
												invalidIoSessions.add(ios);
											}
										} else {
											us.setLastChkTime(now);
										}
									} else if (us.isWeixin()) {
										// wx平台验证目前确定由前台刷新后通知后台（LoginAction.refreshToken），后台不做定时刷新
										us.setLastChkTime(now);
									} else if (us.isVistor()) {
										// TODO:游客
										us.setLastChkTime(now);
									} else {
										// 联运 不检查
										us.setLastChkTime(now);
									}
								} catch (Exception e) {
									log.error("检查开放平台登陆信息异常", e);
									if (lastCheckTime > 0) {
										// 第一次出现异常，放过
										us.setLastChkTime(-1);
									} else {
										// 第二次出现异常，下线
										invalidIoSessions.add(ios);
									}
									// close(ios);
								}
							}
						} else {
							log.error("ioSession中没有userSession,删除.....");
							lit.remove();
							invalidIoSessions.add(ios);
						}
					} else {
						log.error("session已经关闭.....");
						// close(ios);
						lit.remove();
						invalidIoSessions.add(ios);
					}
				}

				if (invalidIoSessions.size() > 0) {
					for (IoSession ios : invalidIoSessions) {
						try {
							if (ios.isConnected()) {
								ios.close(false);
							} else {
								close(ios, true);
							}
						} catch (Exception e) {
							log.error("关闭ioSession异常", e);
						}
					}
				}
			} catch (Exception e) {
				log.error("检查登陆信息异常", e);
			}
			try {
				synchronized (openApi_qq) {
					openApi_qq.wait(checkPeriod);
				}
			} catch (Exception e) {
				log.error("检查登陆信息同步异常", e);
			}
		}
		log.info("开放平台登录状态检查线程停止");
	}

	/**
	 * 为Spring使用的Set方法
	 * 
	 * 
	 * @param maxNodeUser
	 */
	public void setMaxUserNum(int maxNodeUser) {
		OnlineUserSessionManager.maxNodeUser = maxNodeUser;
		NodeSessionMgr.setMaxUserNum(maxNodeUser);
	}

	public static int getMaxNodeUser() {
		return maxNodeUser;
	}

	public static void setMaxNodeUser(int maxNodeUser) {
		int old = OnlineUserSessionManager.maxNodeUser;
		OnlineUserSessionManager.maxNodeUser = maxNodeUser;
		NodeSessionMgr.setMaxUserNum(maxNodeUser);
		if (old > maxNodeUser) {
			// 当前在线超上限
			if (OnlineUserSessionManager.getCurrentOnlineUserNum() > NodeSessionMgr.getGameNodeCount() * maxNodeUser) {
				OnlineUserSessionManager.removeOverloadUsers();
			}
		}

	}

	/**
	 * 重新设置sessionId(就是openKey)
	 */
	public static void rebindSessionId(UserSession uSession, String oldSessionId) {
		String newSessionId = uSession.getSessionId();
		IoSession ioSession = onlineIoSessions.remove(oldSessionId);
		if (ioSession != null) {
			// UserSession oldUs = (UserSession) ioSession
			// .removeAttribute(UserSession.KEY_USERSESSION);
			// if (oldUs != null) {
			// String accName = oldUs.getAccName();
			// MemcachedManager.delete("getAccountByAccName_" + accName, 3);
			// }
			ioSession.setAttribute(UserSession.KEY_USERSESSION, uSession);
			onlineIoSessions.put(newSessionId, ioSession);
		} else {
			log.warn("session丢失........");
		}
		accIdSidMappings.put(uSession.getAccId(), newSessionId);
	}

	/**
	 * 同步mainServer与nodeServer的UserSession
	 * 
	 * @param uSession
	 * @param oldSessionId
	 */
	public static void syncUserSession(UserSession uSession) {
		String sessionId = uSession.getSessionId();
		IoSession ioSession = onlineIoSessions.get(sessionId);
		if (ioSession != null) {
			UserSession oldUserSession = (UserSession) ioSession.getAttribute(UserSession.KEY_USERSESSION);
			if (oldUserSession.getSessionId().equals(sessionId)) {
				ioSession.setAttribute(UserSession.KEY_USERSESSION, uSession);
				if (log.isDebugEnabled()) {
					log.debug("同步session,accessToken={},refreshToken={}", uSession.getOpenkey(),
							uSession.getRefreshToken());
				}
			} else {
				log.error("!!!! 同步session的sessionId不相同，忽略 ");
			}
		} else {
			log.error("!!!! 同步session不存在,accName=:{} ", uSession.getAccName());
		}

	}
}
