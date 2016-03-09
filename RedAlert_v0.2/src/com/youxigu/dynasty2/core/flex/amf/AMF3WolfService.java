package com.youxigu.dynasty2.core.flex.amf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.MessageFilter;
import com.youxigu.dynasty2.chat.MessageFilter.FilterMessages;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.core.flex.ActionDefine;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.IInitListener;
import com.youxigu.wolf.net.IWolfService;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.ResultMgr;
import com.youxigu.wolf.net.SocketContext;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.UserSession;

/**
 * 接收并处理所有Flex客户端的请求
 * 
 * @author Administrator
 * 
 */
public class AMF3WolfService implements IWolfService, IInitListener,
		ApplicationContextAware {

	public static Logger log = LoggerFactory.getLogger(AMF3WolfService.class);

	// /测试用的同步调用方式
	public static final String SYNC_KEY = "SYNC_STAT";

	private static final String SESSION_LAST_REQUEST_TIME = "LAST_REQUEST_TIME";
	// private static final String SESSION_VC = "V_CODE"; // 验证码标志
	private Map<Integer, ActionDefine> actions = new HashMap<Integer, ActionDefine>();
	private List<ActionDefine> actionDefines;

	private Map<String, Long> forbidOpenIds = new java.util.concurrent.ConcurrentHashMap<String, Long>();
	/**
	 * 连接到mainServer的socketClient
	 */
	private IWolfClientService mainClient;

	/**
	 * 命令的最小频率，同一个session每分钟最多请求次数 ,<=0无效
	 */
	private int maxRequestPerSecond = 300;

	/**
	 * 进入游戏忽略掉的请求数
	 */
	private int startRequestNum = 30;

	/**
	 * 高频帐号封停时间,毫秒
	 */
	private int forbidTimes = 3600000;
	/**
	 * 同步请求处理
	 */
	private ResultMgr resultMgr;

	/**
	 * 命令执行超过多少毫秒，输出log数据
	 */
	private int cmdWarnTime = 50;

	private boolean stop;
	private OpenPlatformTransFilter openPlatformTransFilter;
	private IProtoReportService protoReportService;

	private IAccountService accountService;

	public void setStartRequestNum(int startRequestNum) {
		this.startRequestNum = startRequestNum;
	}

	public void setForbidTimes(int forbidTimes) {
		this.forbidTimes = forbidTimes;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	// private IUserDailyActivityService udaService;

	// public void setUdaService(IUserDailyActivityService udaService) {
	// this.udaService = udaService;
	// }

	public void setMainClient(IWolfClientService mainClient) {
		this.mainClient = mainClient;
	}

	public void setOpenPlatformTransFilter(
			OpenPlatformTransFilter openPlatformTransFilter) {
		this.openPlatformTransFilter = openPlatformTransFilter;
	}

	public void setCmdWarnTime(int cmdWarnTime) {
		this.cmdWarnTime = cmdWarnTime;
	}

	public void setMaxRequestPerSecond(int maxRequestPerSecond) {
		this.maxRequestPerSecond = maxRequestPerSecond;
	}

	public void setActionDefines(List<ActionDefine> actionDefines) {
		this.actionDefines = actionDefines;
	}

	// public void setCheckFrequency(boolean checkFrequency) {
	// this.checkFrequency = checkFrequency;
	// }
	//
	// public void setFrequencyLimit(int frequencyLimit) {
	// this.frequencyLimit = frequencyLimit;
	// }

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		if (actionDefines == null) {
			actionDefines = new ArrayList<ActionDefine>();

			while (ctx != null) {
				Map<String, ActionDefine> maps = ctx
						.getBeansOfType(ActionDefine.class);
				if (maps.values() != null && maps.values().size() > 0) {
					actionDefines.addAll(maps.values());
				}
				ctx = ctx.getParent();

			}
		}
		if (actionDefines != null) {
			// 初始化ActionDefine

			Class[] paramsType = { Map.class, Response.class };
			for (ActionDefine ad : actionDefines) {
				int cmd = ad.getCmd();
				if (actions.containsKey(cmd)) {
					throw new BaseException("重复的命令号:" + cmd);
				}
				if (ad.getSubActionIds() == null) {
					Map<Integer, Object> discardEventTypeMaps = ad
							.getDiscardEventTypeMaps();
					if (discardEventTypeMaps != null) {
						ad.addFilterEventType(discardEventTypeMaps);
					}

					try {
						Method m = null;
						try {
							m = ad.getBean().getClass().getDeclaredMethod(ad.getMethodName(), paramsType);
						} catch (Exception e) {
							log.error("action mapping method is error : cmd={}, method={}", cmd, ad.getMethodName());
						}
						
						if(m == null) {
							continue;
						}
						
						ad.setMethod(m);
						String beforeMethodName = ad.getPrevMethodName();
						if (beforeMethodName != null
								&& !"".equals(beforeMethodName)) {
							m = ad.getBean().getClass().getDeclaredMethod(
									beforeMethodName, paramsType);
							ad.setPrevMethod(m);
						}

					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						log.error("action mapping is error : cmd={}", cmd);
					}
				}
				actions.put(cmd, ad);
			}
			actionDefines.clear();
			actionDefines = null;
		}
	}

	public void setProtoReportService(IProtoReportService protoReportService) {
		this.protoReportService = protoReportService;
	}

	public void init() {

	}

	@Override
	public Object handleMessage(Response response, Object message) {
		if (message instanceof Map) {
			Map params = (Map) message;
			Object tmp = params.get(IAMF3Action.ACTION_CMD_KEY);
			if (tmp != null) {

				ActionDefine ad = actions.get((Integer) tmp);

				List<Integer> cmds = null;
				if (ad != null) {
					cmds = ad.getSubActionIds();
				}
				if (cmds != null) {
					// 集合类消息
					int paraNum = params.size();
					List<Map<String, Object>> allDatas = new ArrayList<Map<String, Object>>(
							cmds.size());
					for (Integer cmd : cmds) {
						Map<String, Object> param1 = new HashMap<String, Object>(
								paraNum);
						param1.putAll(params);
						param1.put(IAMF3Action.ACTION_CMD_KEY, cmd);
						Object one = _handleMessage(response, param1, true);
						if (one instanceof Map) {
							allDatas.add(param1);
						}
						// 测试不要使用这种集合CMD,这里就是不想remove
						// params.remove(SYNC_KEY);
					}
					params.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);// 默认没有异常
					params.put("maps", allDatas);

					response.getSession().write(params);
					return params;

				} else {
					return _handleMessage(response, params, false);
				}

			}

		}
		return null;
	}

	public Object _handleMessage(Response response, Map message,
			boolean isParentCmd) {
		Object retu = null;
		long now = System.currentTimeMillis(); // 开始时间

		// //同步调用标志
		SyncStat syncstat = (SyncStat) message.get(SYNC_KEY);
		if (syncstat != null) {

			if (syncstat.getStat() != SyncStat.SYNC_STATUS_REQUEST) {
				ResultMgr.requestCompleted(response.getSession(), syncstat
						.getId(), message);
				return Boolean.TRUE;
			} else {
				syncstat.setStat(SyncStat.SYNC_STATUS_RESPONSE);
			}
		}

		boolean hasException = false;
		int cmd = 0;
		ActionDefine ad = null;
		UserSession session = null;
		try {
			if (stop) {
				return Boolean.TRUE;
			}
			Object tmp = message.get(IAMF3Action.ACTION_CMD_KEY);
			if (tmp == null && !(tmp instanceof Integer)) {
				throw new BaseException("错误的命令号.....");
			}
			cmd = (Integer) tmp;

			if (cmd == -1) {// test

				response.getSession().write(message);

				return Boolean.TRUE;
			}

			Method m = null;

			if (actions != null) {
				ad = actions.get(cmd);
				if (ad != null)
					m = ad.getMethod();
			}
			if (m == null) {
				throw new BaseException("错误的命令号:" + cmd);
			}

			// 请求频率检查
			if (maxRequestPerSecond > 0 && ad.isFrequency()) {
				if (!checkRequestNumLimit(response, message, cmd, session)) {
					return new BaseException("访问频率过快");
				}

			}

			// if (ad.isCheckToken()){
			// openPlatformTransFilter.checkToken(response.getSession(),
			// object);
			// }

			session = (UserSession) response.get(UserSession.KEY_USERSESSION);

			if (log.isDebugEnabled()) {
				if (session != null) {
					log.debug("account:{},receive request:{}", session
							.getAccId(), message);
				} else {
					log.debug("receive request:{}", message);
				}
			}

			// ///////////////////////
			if (ad.isSecCheck()) {
				// 必须检查session
				session = validateAndRegister(response, message);
			} else {
				message.remove(IAMF3Action.ACTION_SID_KEY);
			}

			if (session != null) {
				Long forbidTime = forbidOpenIds.get(session.getOpenid());
				if (forbidTime != null) {
					if (forbidTime > now) {
						log.error("高频请求帐号，正在封停期间内.....{}", session.getOpenid());
						Map<String, Object> param1 = new HashMap<String, Object>(
								3);
						param1.put("cmd", 1);
						param1.put("errCode", -9022);
						param1.put("err", "高频请求帐号，正在封停期间内，禁止登录,服务器关闭连接");
						IoSession iosession = response.getSession();
						iosession.write(param1);
						iosession.close(false);
						// response.getSession().close(true);
						return Boolean.TRUE;
					} else {
						forbidOpenIds.remove(session.getOpenid());
					}
				}
			}

			if (ad.isAddicted() && !checkAddicted(session)) {
				throw new BaseException("您已进入防沉迷疲劳期，无法使用此功能!");
			}
			
			if(ad.isPwdcheak() && !checkPwd(session)) {
				Map<String, Object> param1 = new HashMap<String, Object>(3);
				param1.put("cmd", cmd);
				param1.put("errCode", IAMF3Action.CMD_PASSWORD);
				param1.put("err", "您已设置了二级密码，无法使用此功能!");
				IoSession iosession = response.getSession();
				iosession.write(param1);
				return Boolean.TRUE;
			}

			// 设置event过滤器
			boolean hasFilter = false;
			long userId = 0;
			if (ad.getFilterEventTypeMaps() != null
					|| ad.getFilterChannelMaps() != null) {

				if (session != null) {
					userId = session.getUserId();
					MessageFilter.setUserId(userId);
					MessageFilter.addFilterEventTypes(ad
							.getFilterEventTypeMaps());
					MessageFilter.addFilterChannels(ad.getFilterChannelMaps());
					hasFilter = true;
				}
			}

			// if (cmd == IAMF3Action.CMD_OPENAPI_PAY_TOKEN) {
			// openPlatformTransFilter.doBeforeFilter(response, object,
			// actions);
			// }

			retu = m.invoke(ad.getBean(), new Object[] { message, response });
			// retu = call(cmd, object, response);
			if (retu != null && retu.getClass() != Void.class) {
				if (retu instanceof Map) {
					Map<String, Object> retuMap = (Map<String, Object>) retu;
					if (!retuMap.containsKey(IAMF3Action.ACTION_CMD_KEY)) {
						retuMap.put(IAMF3Action.ACTION_CMD_KEY, cmd);
					}
					if (!retuMap.containsKey(IAMF3Action.ACTION_ERROR_CODE_KEY)) {
						retuMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);// 默认没有异常
					}

					if (hasFilter) {
						processFilterMessage(userId, ad, retuMap);
					}

					if (syncstat != null) {
						retuMap.put(SYNC_KEY, syncstat);
					}

					// if (cmd == IAMF3Action.CMD_OPENAPI_PAY_TOKEN) {
					// openPlatformTransFilter.doAfterFilter(response,
					// object, retuMap);
					// }
				}

			}

			// if (ad.getActivityId() >= 0) {
			// // 处理活跃度。这里与action不在一个事务内
			// udaService.addUserDailyActivity(userId, ad.getActivityId(),
			// (byte) 1);
			// }

		} catch (Throwable e) {
			hasException = true;
			Map<String, Object> exceptionMap = new HashMap<String, Object>();
			if (syncstat != null) {
				exceptionMap.put(SYNC_KEY, syncstat);
			}
			exceptionMap.put(IAMF3Action.ACTION_CMD_KEY, cmd);

			String errDesc = null;
			int errCode = IAMF3Action.CMD_SYSTEM_ERROR;

			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			} else if (e.getCause() != null) {
				e = e.getCause();
			}

			if (e instanceof BaseException) {
				// 业务异常
				if (log.isDebugEnabled()) {
					log.debug("请求异常：", e);
				} else {
					log.error("cmd:" + cmd + "," + e.toString());
				}
				BaseException tmp = (BaseException) e;
				errCode = tmp.getErrCode();
				errDesc = tmp.getErrMsg();
				if (errDesc == null) {
					Throwable e1 = e.getCause();
					if (e1 instanceof TimeoutException) {
						errDesc = "超时，请重试!";
					} else {
						errDesc = "系统异常，稍后再试";
						// errDesc = e.toString();
					}
				}
			} else {
				log.error("请求异常：" + cmd, e);
				if (e instanceof TimeoutException) {
					errDesc = "超时，请重试!";
				} else {
					errDesc = "系统异常，稍后再试";
				}
			}

			if (errCode == IAMF3Action.ERROR_PAY) {
				// 前台要求支付1018异常发送特定的cmd
				Map<String, Object> payExceptionMap = new HashMap<String, Object>(
						2);
				payExceptionMap.put(IAMF3Action.ACTION_CMD_KEY,
						IAMF3Action.CMD_PAY_ERROR);
				payExceptionMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);
				response.getSession().write(payExceptionMap);
			}

			exceptionMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY, errCode);
			exceptionMap.put(IAMF3Action.ACTION_ERROR_KEY, errDesc);
			// if (log.isDebugEnabled()) {
			// if (debug_time != null) {
			// exceptionMap.put("debug_time", debug_time);
			// }
			// }
			retu = exceptionMap;
		} finally {
			// 清除过滤器
			MessageFilter.clear();
			// /发送给开放平台协议上报接口
			if (session != null) {
				protoReportService.report(ad, session, hasException, message);

			} else {
				session = (UserSession) response
						.get(UserSession.KEY_USERSESSION);
				if (session != null) {
					protoReportService.report(ad, session, hasException,
							message);
				}
			}
			// System.out.println("time="+(System.currentTimeMillis()-time));
			if (retu != null) {

				// 前台要求加上的交易完成标志
				// Object flashTransFLag = object
				// .remove(OpenPlatformTransFilter.T_BACK2FLASH);
				// if (flashTransFLag != null && (retu instanceof Map)) {
				// Map<String, Object> retuMap = (Map<String, Object>) retu;
				// retuMap.put(OpenPlatformTransFilter.T_BACK2FLASH,
				// flashTransFLag);
				//
				// }
				if (!isParentCmd) {
					response.getSession().write(retu);
				}
			}

			if (log.isWarnEnabled()) {
				long lag = System.currentTimeMillis() - now;
				if (lag > cmdWarnTime) {
					log.warn("命令:{}处理时间过长:{}", cmd, lag);
				}
				// else {
				// log.debug("命令：{} 处理时间:{}", cmd, lag);
				// }
			}
		}

		return retu == null ? Boolean.TRUE : retu;

	}

	private void processFilterMessage(long userId, ActionDefine ad,
			Map<String, Object> retuMap) {
		FilterMessages eventMessages = MessageFilter.getEventsByUserId(userId);
		if (eventMessages != null) {
			List<Object> msgs = eventMessages.getMessages();
			if (msgs != null && msgs.size() > 0) {
				Iterator<Object> lit = msgs.iterator();
				while (lit.hasNext()) {
					Object msg = lit.next();
					if (msg instanceof EventMessage) {
						EventMessage e = (EventMessage) msg;
						int eventType = e.getEventType();
						if (ad.isDiscardEvent(eventType)) {
							lit.remove();// 吃掉的消息
						} else {
							String key = EventMessage.getEventKey(eventType);
							if (key != null) {
								retuMap.put(key, e.getParams());
								lit.remove();
							}
						}
					}

				}
				if (msgs.size() > 0) {
					// 普通消息
					retuMap.put("msg", msgs);
				}
			}
			// if (log.isDebugEnabled()) {
			// log.debug("send by amf filter message:{}",
			// JSONUtil.toJson(events));
			// }
			// retuMap.put("_msg",msgs);
		}
	}

	/**
	 * 
	 * @param response
	 */
	private UserSession validateAndRegister(Response response, Map params) {
		IoSession ioSession = response.getSession();
		UserSession us = (UserSession) ioSession
				.getAttribute(UserSession.KEY_USERSESSION);

		/**
		 * 如果是mainServer ，这里userSession不会为null 如果是game
		 * Serer,第一次做游戏逻辑请求时null:需要参数中传递sid
		 */
		String sid = (String) params.remove(IAMF3Action.ACTION_SID_KEY);
		if (us == null) {
			synchronized (ioSession) {
				us = (UserSession) ioSession
						.getAttribute(UserSession.KEY_USERSESSION);
				if (us == null) {
					if (sid == null) {
						if (log.isDebugEnabled()) {
							log.debug("收到没有sid的请求：来源：{},参数:{}", ioSession
									.getRemoteAddress(), params);
						}
						throw new BaseException("非法请求，请先登陆或者提供sid:");
					}
					if (mainClient != null) {
						us = mainClient
								.sendTask(UserSession.class,
										"wolf_onlineUserService",
										"getUserSession", sid);
					}

					if (us == null) {
						if (log.isDebugEnabled()) {
							log.debug("收到没有登陆的请求：来源：{},参数:{}", ioSession
									.getRemoteAddress(), params);
						}
						throw new BaseException("无效的连接,请重新登陆");
					}
					// 用于表示进行一次登录。有些后续请求需要判断是不是登陆后第一次调用请求，后续请求有责任清除这个标志
					us.addAttribute("login", 1);
					OnlineUserSessionManager.register(us, ioSession);
				}
			}

		}
		return us;

	}

	private boolean checkAddicted(UserSession us) {
		if (accountService == null) {
			accountService = (IAccountService) ServiceLocator
					.getSpringBean("accountService");
		}
		return accountService.getOnlineUserEffect(us) == 0;
	}
	
	private boolean checkPwd(UserSession us) {
		if(us.getOldPwd() == null) {
			us.setOldPwd("");
		}
		
		if(us.getNewPwd() == null) {
			us.setNewPwd("");
		}
		
		boolean flag = us.getOldPwd().equals(us.getNewPwd());
		if(!flag) {
			try {
				//TODO 目前只考虑了nodeserver发的情况
				IChatClientService messageService = (IChatClientService) ServiceLocator.getSpringBean("messageService");
				messageService.sendEventMessage(us.getUserId(), EventMessage.TYPE_PASSWOPRD, null);
			} catch (Exception e) {

			}
		}
		return flag;
	}

	private boolean checkRequestNumLimit(Response response, Map message,
			int cmd, UserSession userSession) {
		IoSession ioSession = response.getSession();
		synchronized (ioSession) {

			long requestCount = ioSession.getReadMessages() - 1;
			// iosession创建后前50次请求不检查.
			// 这里requestCount是所有的请求计数，包括被拒绝的，以及不进行频率检查的，
			// 因此，会存在超过maxRequestPerSecond也不检查的情况

			if (requestCount > startRequestNum
					&& requestCount % maxRequestPerSecond == 0) {
				// 检查时间
				Long lastTime = (Long) ioSession
						.getAttribute(SESSION_LAST_REQUEST_TIME);
				if (lastTime == null) {
					lastTime = System.currentTimeMillis();
					ioSession.setAttribute(SESSION_LAST_REQUEST_TIME, lastTime);
				} else {
					long now = System.currentTimeMillis();
					if (now - lastTime <= 60000) {
						IoSession session = response.getSession();
						log.error("请求频率过快，关闭连接:{},{}", session
								.getLocalAddress(), userSession == null ? ""
								: userSession.getAccName());

						if (userSession != null) {
							forbidOpenIds.put(userSession.getOpenid(), now
									+ forbidTimes);
						}

						Map<String, Object> params = new HashMap<String, Object>(
								3);
						params.put("cmd", 1);
						params.put("errCode", -9020);
						params.put("err", "请求过快，服务器关闭连接");
						session.write(params);
						session.close(false);
						// response.getSession().close(true);
						return true;
					} else {
						ioSession.setAttribute(SESSION_LAST_REQUEST_TIME, now);
					}
				}
			}
		}
		return true;
	}

	@Override
	public void init(SocketContext context) {
		resultMgr = context.get("resultMgr", ResultMgr.class);
	}

	public static class SyncStat implements java.io.Serializable {
		public static final int SYNC_STATUS_REQUEST = 1;
		public static final int SYNC_STATUS_RESPONSE = 2;

		private long id;
		private int stat = SYNC_STATUS_REQUEST;

		public SyncStat() {
			this.id = SyncWolfTask.getNewRequestId();
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public int getStat() {
			return stat;
		}

		public void setStat(int stat) {
			this.stat = stat;
		}

	}

	@Override
	public void stop(boolean force) {
		stop = true;

	}

}
