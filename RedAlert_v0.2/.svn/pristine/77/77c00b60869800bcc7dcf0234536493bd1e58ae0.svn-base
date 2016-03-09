package com.youxigu.dynasty2.user.service.impl;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.manu.core.ServiceLocator;
import com.manu.util.UtilDate;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.log.AbsLogLineBuffer;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogBeanFactory;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.log.TLogTable;
import com.youxigu.dynasty2.log.TlogHeadUtil;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.Quser;
import com.youxigu.dynasty2.openapi.service.ITecentMobileQqService;
import com.youxigu.dynasty2.openapi.service.ITecentWeiXinService;
import com.youxigu.dynasty2.user.dao.IAccountDao;
import com.youxigu.dynasty2.user.dao.IUserDao;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.AccountWhiteList;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.proto.UserMsg;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10001Define;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.CommonUtil;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.wolf.net.ISessionListener;
import com.youxigu.wolf.net.MobileClient;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.UserSession;

public class AccountService implements IAccountService, ISessionListener,
		ApplicationListener {

	public static final Logger log = LoggerFactory
			.getLogger(AccountService.class);
	private static final Pattern pattern = Pattern.compile("[0-9A-F]{32}");
	private IUserDao userDao;
	private IAccountDao accountDao;
	private ILogService logService;
	private ILogService tlogService;
	private IUserService userService;
	private IWolfClientService mainServerClient;
	private ITecentMobileQqService tecentMobileQqService;
	private ITecentWeiXinService tecentWeiXinService;
	protected IChatClientService messageService;
	/**
	 * 测试用的防沉迷的登陆帐号
	 */
	private Map<String, String> testIds = new HashMap<String, String>(5);
	/**
	 * 是否使用白名单校验帐号是否可创建
	 */
	private boolean useWhiteList = false;

	/**
	 * 登陆等待队列
	 */
	private BlockingQueue<IoSession> loginQueue = null;
	/**
	 * 登陆等待队列 大小
	 */
	private int loginQueueSize = 500;
	/**
	 * 登陆等待队列检查周期
	 */
	private int queueCheckPeriod = 10000;// 10秒检查一次

	public void setLoginQueueSize(int loginQueueSize) {
		this.loginQueueSize = loginQueueSize;
	}

	public void setQueueCheckPeriod(int queueCheckPeriod) {
		this.queueCheckPeriod = queueCheckPeriod;
	}

	public void setMainServerClient(IWolfClientService mainServerClient) {
		this.mainServerClient = mainServerClient;
	}

	public void setUseWhiteList(boolean useWhiteList) {
		this.useWhiteList = useWhiteList;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public void setTlogService(ILogService tlogService) {
		this.tlogService = tlogService;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public void setAccountDao(IAccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setTecentMobileQqService(
			ITecentMobileQqService tecentMobileQqService) {
		this.tecentMobileQqService = tecentMobileQqService;
	}

	public void setTecentWeiXinService(ITecentWeiXinService tecentWeiXinService) {
		this.tecentWeiXinService = tecentWeiXinService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setTestAccIds(String accNames) {
		if (accNames != null) {
			String[] tmps = accNames.split(",");
			for (String tmp : tmps) {
				testIds.put(tmp, tmp);
			}
		}
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			init();
		}

	}

	public void init() {
		boolean isMainServer = String.valueOf(NodeSessionMgr.SERVER_TYPE_MAIN)
				.equals(System.getProperty(NodeSessionMgr.SERVER_TYPE_KEY));
		if (isMainServer && loginQueueSize > 0) {
			log.info("登录等待队列大小:{}", loginQueueSize);
			loginQueue = new ArrayBlockingQueue<IoSession>(loginQueueSize);
			new Thread(new Runnable() {
				@Override
				public void run() {
					AccountService.this.checkLoginQueue();
				}
			}, "LoginQueueThread").start();
		} else {
			log.info("不启动登录等待队列...........");
		}
	}

	private void checkLoginQueue() {
		IAccountService accountService = null;
		try {
			Thread.sleep(10000L);
		} catch (Exception e) {
		}
		log.info("登录等待队列处理线程启动.......");

		while (true) {
			try {
				synchronized (loginQueue) {
					loginQueue.wait(queueCheckPeriod);// 等待10秒或者loginQueue中的notify。
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (accountService == null) {
				accountService = (IAccountService) ServiceLocator
						.getSpringBean("accountService");
			}

			try {
				Iterator<IoSession> lit = loginQueue.iterator();
				while (lit.hasNext()) {
					IoSession session = lit.next();
					try {

						if (!session.isClosing() && session.isConnected()) {
							UserSession us = (UserSession) session
									.getAttribute(UserSession.KEY_USERSESSION);
							boolean register = OnlineUserSessionManager
									.registerFromQueue(us, session);
							if (!register) {
								break;// 仍然满的
							} else {
								// 登陆成功
								lit.remove();

								boolean newAcc = (Boolean) us
										.removeAttribute("newAcc");
								User user = (User) us.removeAttribute("user");
								Account account = (Account) us
										.removeAttribute("account");
								accountService.doAfterLogin(account, us, user,
										newAcc);

								// 构造推送消息
								Map<String, Object> map = new HashMap<String, Object>();
								map.put(IAMF3Action.ACTION_CMD_KEY,
										IAMF3Action.CMD_LOGIN);
								map.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);
								map.put(IAMF3Action.ACTION_SID_KEY,
										us.getSessionId());
								map.put(UserSession.KEY_GAMESERVER_IP,
										us.getGameServerIp());
								map.put(UserSession.KEY_GAMESERVER_PORT,
										us.getGameServerPort());
								map.put("accId",
										String.valueOf(account.getAccId()));
								map.put("aid", account.getAccName());
								String pf = account.getPf();
								map.put("pf", pf);
								map.put("appId",
										Constant.getAppId(us.getpType()));
								session.write(map);

							}
						} else {
							// 删除无效的Queue
							lit.remove();
						}
					} catch (Exception e) {
						log.error("登陆等待队列处理错误1", e);
						session.close(true);
					}

				}

				// 推送队列长度消息
				int size = loginQueue.size();
				int num = 1;
				lit = loginQueue.iterator();
				while (lit.hasNext()) {
					IoSession session = lit.next();
					if (!session.isClosing() && session.isConnected()) {
						// 构造推送消息
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(IAMF3Action.ACTION_CMD_KEY,
								IAMF3Action.CMD_LOGIN);
						map.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);
						map.put("queue", size);
						map.put("my", num);
						session.write(map);
						num++;
					} else {
						// 删除无效的Queue
						lit.remove();
					}
				}
			} catch (Exception e) {
				log.error("登陆等待队列处理错误", e);
			}

		}

	}

	private void decodeUrl(String url, Map<String, Object> params) {
		try {
			// String param = URLDecoder.decode(url,"utf-8");
			String[] urlArr = url.split("&");
			for (String one : urlArr) {
				String[] tmpArr = one.split("=");
				String key = URLDecoder.decode(tmpArr[0], "utf-8");
				String value = URLDecoder.decode(tmpArr[1], "utf-8");
				params.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> login(Request10001Define request,
			Map<String, Object> params) {
		Map<String, Object> retu = null;
		boolean checkByWhiteList = this.useWhiteList;
		String openid = null;
		String openkey = null;
		String pf = null;
		String pfkey = null;
		String remoteIp = null;
		String areaId = null;
		String areaName = null;
		// String seqId = null;
		String pay_token = null;
		String refreshToken = null;
		int platformType = Constant.PLATFORM_TYPE;
		int deviceType = Constant.DEVIDE_TYPE;
		String dInfo = null;// 设备信息
		int channel = 0;// 渠道号

		try {
			if (!Constant.isTestEnv()) {
				// if (log.isDebugEnabled()) {
				// Iterator<Map.Entry<String, Object>> lit = params.entrySet()
				// .iterator();
				// while (lit.hasNext()) {
				// Map.Entry<String, Object> entry = lit.next();
				// log.debug("{}={}", entry.getKey(), entry.getValue());
				// }
				// }
				// // 外网环境校验
				// String url = (String) params.remove("url");
				// if (url != null) {
				// decodeUrl(url, params);
				// }
				// 1=手Q 2=微信 4=访客
				if (CommonUtil.isNotEmpty(request.getPType())) {
					platformType = MathUtils.getInt(request.getPType());
				}

				if (Constant.PLATFORM_TYPE != Constant.PLATFORM_TYPE_ANY
						&& platformType != Constant.PLATFORM_TYPE_VISTOR
						&& platformType != Constant.PLATFORM_TYPE) {
					if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
						throw new BaseException("本区必须用手机QQ登录");
					} else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
						throw new BaseException("本区必须用微信登录");
					} else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_ITOOLS) {
						throw new BaseException("本区必须用ITOOLS登录");
					} else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_UNION) {
						throw new BaseException("本区必须用联运平台登录");
					} else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_FACEBOOK) {
						throw new BaseException("本区必须用FaceBook登录");
					}
				}
				// 设备类型
				if (CommonUtil.isNotEmpty(request.getDType())) {
					deviceType = MathUtils.getInt(request.getDType());
				}
				if (Constant.DEVIDE_TYPE != Constant.DEVIDE_TYPE_ANY
						&& deviceType != Constant.DEVIDE_TYPE) {
					if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_ANDROID) {
						throw new BaseException("本区必须用安卓设备登录");
					} else {
						throw new BaseException("本区必须用苹果设备登录");
					}
				}

				openid = request.getOpenid();
				if (CommonUtil.isEmpty(request.getOpenid())) {
					openid = request.getAid();
				}
				if (CommonUtil.isEmpty(openid)) {
					throw new BaseException(-99999, "请提供openid");
				} else {
					openkey = request.getOpenkey();
					if (CommonUtil.isEmpty(openkey)) {
						if (platformType != Constant.PLATFORM_TYPE_ANY
								&& platformType != Constant.PLATFORM_TYPE_VISTOR) {
							throw new BaseException(-99999,
									"请提供openkey/accesstoken");
						} else {
							openkey = UUID.randomUUID().toString();
						}
					}
				}
				pay_token = request.getPayToken();
				refreshToken = request.getRefreshToken();
				pf = request.getPf();
				pfkey = request.getPfkey();
				areaId = request.getServerId();
				areaName = request.getServerName();
				if (CommonUtil.isEmpty(areaId)) {
					if (CommonUtil.isNotEmpty(pf)) {
						int pos = pf.lastIndexOf("#");
						if (pos != -1) {
							areaId = pf.substring(pos + 1, pf.length());
							pf = pf.substring(0, pos);
						}
					}
				}

				// seqId = request.getSeqId();
				// check = params.remove("check") != null;

				dInfo = request.getDInfo();

			} else {
				// 内网测试环境
				// vistor = true;
				checkByWhiteList = false;
				openid = request.getOpenid();
				if (CommonUtil.isEmpty(openid)) {
					openid = request.getAid();
				}
				openkey = request.getOpenkey();
				if (CommonUtil.isEmpty(openkey)) {
					openkey = UUID.randomUUID().toString();
				}
				areaId = request.getServerId();
				areaName = request.getServerName();
				pf = request.getPf();
				pfkey = request.getPfkey();
				if (CommonUtil.isEmpty(areaId)) {
					if (CommonUtil.isNotEmpty(pf)) {
						int pos = pf.lastIndexOf("#");
						if (pos != -1) {
							areaId = pf.substring(pos + 1, pf.length());
							pf = pf.substring(0, pos);
						}
					}
				}

				pay_token = request.getPayToken();
				refreshToken = request.getRefreshToken();

				if (CommonUtil.isNotEmpty(request.getPType())) {
					platformType = Integer.parseInt(request.getPType());
				}
				if (CommonUtil.isNotEmpty(request.getDType())) {
					deviceType = Integer.parseInt(request.getDType());
				}

				dInfo = request.getDInfo();

			}
			channel = request.getMobileClient().getIRegChannel();
			if (CommonUtil.isEmpty(pf)) {
				pf = Constant.PF_MAIN;
			}

			if (CommonUtil.isEmpty(areaId)) {
				areaId = Constant.AREA_ID;
			}
			remoteIp = (String) params.remove("userip");

			User user = null;

			Account account = accountDao.getAccountByAccNameAreaId(openid,
					areaId);
			long now = System.currentTimeMillis();
			Timestamp nowDttm = new Timestamp(now);
			boolean newAcc = false;
			if (account == null) {
				newAcc = true;
				// register
				if (checkByWhiteList) {
					// 检查白名单，必须是激活状态，否则抛出异常
					AccountWhiteList awl = this.getAccountWhiteList(openid);
					if (awl == null
							|| awl.getStatus() == AccountWhiteList.STATUS_LOCK) {
						throw new BaseException(-99999, "帐号未激活，请先激活帐号");
					}
				}

				// log.debug("new account");
				long accId = IDUtil.getNextId("ACCOUNT");
				MemcachedManager.lock("CREATE_ACCOUNT_" + accId);
				account = accountDao.getAccountByAccNameAreaId(openid, areaId);
				if (account != null) {
					throw new BaseException("账号创建中，请稍后");
				}
				account = new Account();
				account.setAccId(accId);
				// account.setAccId(Long.parseLong(accId));
				account.setAccName(openid);
				account.setLoginIp(remoteIp);
				account.setCreateDttm(nowDttm);
				account.setLastLoginDttm(nowDttm);
				account.setLastLogoutDttm(nowDttm);
				account.setPf(pf);
				account.setAreaId(areaId);
				account.setpType(platformType);
				account.setdType(deviceType);
				account.setdInfo(dInfo);
				account.setVia("" + channel);

				accountDao.insertAccount(account);

			} else {
				if (account.getStatus() == Account.STATUS_BAN) {// 封号状态
					if (account.getEnvelopDttm() != null
							&& account.getEnvelopDttm().before(
									UtilDate.nowTimestamp())) {// 状态已解除
						account.setEnvelopDttm(null);
						account.setStatus(0);
					} else {
						String defaultDesc = "您的帐号已经被锁定.";
						if (account.getStatusDec() != null
								&& !account.getStatusDec().equals("")) {
							defaultDesc = account.getStatusDec();
						}
						throw new BaseException(-99999, defaultDesc);
					}
				} else if (account.getStatus() == Account.STATUS_UNAVAILABLE)
					throw new BaseException(-99999, "您的帐号没有激活.");
				account.setLoginIp(remoteIp);

				Timestamp dttm = account.getLastLogoutDttm();
				if (dttm != null) {
					int offlineTimeSum = account.getOfflineTimeSum();
					offlineTimeSum = offlineTimeSum
							+ (int) ((now - dttm.getTime()) / 1000);
					if (offlineTimeSum > 5 * AppConstants.HOUR_SECONDS) {
						// /下线5小时清0
						account.setOnlineTimeSum(0);
						account.setOfflineTimeSum(0);
					} else {
						account.setOfflineTimeSum(offlineTimeSum);
					}
				} else {
					account.setOnlineTimeSum(0);
					account.setOfflineTimeSum(0);
				}
				account.setLastLoginDttm(nowDttm);
				account.setLastLogoutDttm(nowDttm);// 也修改成最后上线时间
				account.setPf(pf);
				account.setdType(deviceType);
				account.setpType(platformType);
				account.setdInfo(dInfo);
				account.setAreaId(areaId);
				account.setVia("" + channel);
				// accountDao.updateAccount(account);

				user = userService.getUserByaccId(account.getAccId());

			}

			// 取客户端机型
			MobileClient mobileClient = new MobileClient();
			UserMsg.MobileClient mobileClient2 = (UserMsg.MobileClient) request
					.getMobileClient();
			mobileClient.vClientVersion = mobileClient2.getVClientVersion();
			mobileClient.vSystemSoftware = mobileClient2.getVSystemSoftware();
			mobileClient.vSystemHardware = mobileClient2.getVSystemHardware();
			mobileClient.vTelecomOper = mobileClient2.getVTelecomOper();
			mobileClient.vNetwork = mobileClient2.getVNetwork();
			mobileClient.iScreenWidth = mobileClient2.getIScreenWidth();
			mobileClient.iScreenHight = mobileClient2.getIScreenHight();
			mobileClient.Density = mobileClient2.getDensity();
			mobileClient.iRegChannel = mobileClient2.getIRegChannel();
			mobileClient.vCpuHardware = mobileClient2.getVCpuHardware();
			mobileClient.iMemory = mobileClient2.getIMemory();
			mobileClient.vGLRender = mobileClient2.getVGLRender();
			mobileClient.vGLVersion = mobileClient2.getVGLVersion();
			mobileClient.vDeviceId = mobileClient2.getVDeviceId();
			UserSession us = new UserSession(openkey);
			us.setMobileClient(mobileClient);
			us.setAccId(account.getAccId());
			us.setAccName(account.getAccName());// ==openid
			us.setOpenkey(openkey);

			us.setdType(deviceType);
			us.setpType(platformType);

			us.setPf(pf);
			us.setPfKey(pfkey);
			us.setPay_token(pay_token);
			us.setRefreshToken(refreshToken);
			us.setAreaId(areaId);
			us.setAreaName(areaName);
			// us.setSeqId(seqId);
			us.setGameNodeName(account.getNodeIp());
			us.setUserip(remoteIp);
			us.setLoginDttm(now);
			us.setPrevOnlineTimeSum(account.getOnlineTimeSum());
			us.setOfflineTimeSum(account.getOfflineTimeSum());
			us.setVia(account.getVia());
			us.setdInfo(dInfo);
			// String lastNodeIp = account.getNodeIp();

			if (user != null) {
				us.setUserId(user.getUserId());
				us.setGender(user.getSex());
				us.setMainCasId(user.getMainCastleId());
				us.setUserName(user.getUserName());
				// us.setOldPwd(user.parsePassword()[0]);
			}

			IoSession iosession = (IoSession) params.remove("iosession");

			OnlineUserSessionManager.register(us, iosession);
			if (us.getGameServerIp() == null) {
				// 处理登陆队列
				if (loginQueueSize > 0
						&& loginQueue.size() < this.loginQueueSize
						&& loginQueue.offer(iosession)) {
					us.addAttribute("account", account);
					us.addAttribute("newAcc", newAcc);
					if (user != null) {
						us.addAttribute("user", user);
					}

					retu = new HashMap<String, Object>(3);
					retu.put("account", account);
					retu.put("us", us);
					retu.put("user", user);
					retu.put("queue", loginQueue.size());
					return retu;
				} else {
					throw new BaseException("此服务器的排队人数过多，请稍后再试");
				}
			}

			doAfterLogin(account, us, user, newAcc);
			// ////test
			// try {
			// tecentWeiXinService.friendsProfile(openid, openkey);
			// tecentWeiXinService.share_wx(openid, openkey,
			// "o8-CZjtYWFkYbdS77mgGA0WpQIeQ", "测试title", "测试description", null
			// ,null);
			// tecentMobileQqService.qqFriends(openid, openkey, 2);
			// tecentMobileQqService
			// .share(
			// openid,
			// openkey,
			// "8322BA3D9C0C7C30DB661F7B52C984E9",
			// "七雄争霸",
			// "http://gamecenter.qq.com/gcjump?appid=1000001640&pf=invite&from=iphoneqq&plat=qq&originuin=111&ADTAG=gameobj.msg_invite",
			// "测试preview", "测试summary", "http://aaa.bbb.com",
			// "MSG_HEART_SEND", 0);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			retu = new HashMap<String, Object>(3);
			retu.put("account", account);
			retu.put("us", us);
			retu.put("user", user);
		} catch (BaseException e) {
			log.error("错误", e);
			throw e;
			// throw new BaseException(-99999, e.getErrMsg() == null ? e
			// .toString() : e.getErrMsg());
		} catch (Exception e) {
			log.error("错误", e);
			String message = e.getMessage();
			if (message == null) {
				message = e.toString();
			}
			throw new BaseException(-99999, message);
		}
		return retu;
	}

	/**
	 * 战斗力汇报
	 * 
	 * @param user
	 */
	// private void battlePower(Account account, User user, UserSession us) {
	// if (user != null) {
	//
	// try {
	//
	// if (us != null) {
	// Troop troop = troopService.getTroopById(user.getUserId(),
	// user.getTroopId());
	// int power = troopService.getTroopCombatPower(troop);
	//
	// if (us.isWeixin()) {
	// tecentWeiXinService.wxScore(us.getOpenid(), power + "",
	// 0);
	// } else if (us.isMobileQQ()) {
	// List<QQScoreReport> reportList = new ArrayList<QQScoreReport>(
	// 2);
	// QQScoreReport scoreReport = new QQScoreReport();
	// scoreReport.setType(3);
	// scoreReport.setBcover(1);
	// scoreReport.setExpireds("0");
	// scoreReport.setData("" + power);
	// reportList.add(scoreReport);
	// QQScoreReport scoreReport1 = new QQScoreReport();
	// scoreReport1.setType(QQScoreReport.TYPE_LEVEL);
	// scoreReport1.setBcover(1);
	// scoreReport1.setExpireds("0");
	// scoreReport1.setData("" + user.getUsrLv());
	// reportList.add(scoreReport1);
	// QQScoreReport scoreReport2 = new QQScoreReport();
	// scoreReport2.setType(8);
	// scoreReport2.setBcover(1);
	// scoreReport2.setExpireds("0");
	// scoreReport2.setData(""
	// + account.getLastLoginDttm().getTime() / 1000);
	// reportList.add(scoreReport2);
	// QQScoreReport scoreReport3 = new QQScoreReport();
	// scoreReport3.setType(25);
	// scoreReport3.setBcover(1);
	// scoreReport3.setExpireds("0");
	// scoreReport3.setData(""
	// + user.getCreateDate().getTime() / 1000);
	// reportList.add(scoreReport3);
	// tecentMobileQqService.qqscoreBatch(us.getOpenid(), us
	// .getOpenkey(), reportList);
	// }
	// }
	//
	// } catch (Exception e) {
	// log.error("上报得分失败", e);
	// }
	// }
	// }

	public void doAfterLogin(Account account, UserSession us, User user,
			boolean newAcc) {
		// 设置QQ平台特权数据
		if (us.isMobileQQ()) {
			setQQPlatformInfo(account, us);
		} else if (us.isWeixin()) {
			setWeiXinPlatformInfo(account, us);
		} else if (us.isUnion()) {
			setUnionPlatformInfo(account, us);
		} else {
			if (Constant.isTestEnv()) {
				setDebugQQPlatformInfo(account, us);
			}
		}
		// battlePower(account, user, us);
		// 加入登录的nodeServer IP
		account.setNodeIp(us.getGameNodeName());
		String areaId = us.getAreaId();
		if (newAcc) {
			accountDao.updateAccount(account);
			// 注册tlog
			// Timestamp createDate = null;
			// logService.log(AbsLogLineBuffer
			// .getBuffer(areaId, TLogTable.TLOG_USERINFO.getName())
			// .append(LogTypeConst.TYPE_REGISTERUSER)
			// .append(account.getAccId()).append(us.getOpenid())
			// .append(0).append(0).append(us.getPfEx())
			// .append(account.getCreateDttm()).append(account.getVia())
			// .append(createDate).append(""));

			logService.log(LogBeanFactory.buildUserInfoLog(user,
					account.getAccId(), us.getPfEx(), account.getVia(),
					account.getCreateDttm()));
		} else {
			accountDao.updateAccount(account);
			// 如果账号已经有角色，上报登陆用户信息

		}
		// TLOG
		setLoginTLog(LogTypeConst.TYPE_LOGIN, account, user, 0, us);
		sendLogLogin(user, account, us);
	}

	/**
	 * 登录的时候发送日志
	 * 
	 * @param user
	 */
	private void sendLogLogin(User user, Account account, UserSession us) {
		String ip = null;
		MobileClient mobile;
		Date now = new Date();
		String openId = null;
		String areaId = null;
		String head = "";
		int dType = 0;
		if (us != null) {
			mobile = us.getMobileClient();
			ip = us.getUserip();
			openId = us.getOpenid();
			areaId = us.getAreaId();
			if (us.getdType() == Constant.DEVIDE_TYPE_IOS) {
				dType = 0;
			} else {
				dType = 1;
			}
			if (us.isVistor()) {
				head = "G_";
			}
		} else {
			ip = account.getLoginIp();
			mobile = new MobileClient();
			openId = account.getAccName();
			areaId = account.getAreaId();
		}

		int playerFriendsNum = 0;// TODO 好友数量。。目前没有做

		AbsLogLineBuffer log = AbsLogLineBuffer
				.getBuffer(areaId, AbsLogLineBuffer.TYPE_MYLOG,
						TLogTable.TLOG_PLAYERLOGIN.getName())
				.append(Constant.SVR_IP).append(now)
				.append(head + Constant.getAppId()).append(dType)
				.append(areaId).appendLegacy(openId);
		logService.log(log.append(user == null ? 0 : user.getUsrLv())
				.append(playerFriendsNum).append(mobile.vClientVersion)
				.append(mobile.vSystemSoftware).append(mobile.vSystemHardware)
				.append(mobile.vTelecomOper).append(mobile.vNetwork)
				.append(mobile.iScreenWidth).append(mobile.iScreenHight)
				.append(mobile.Density).append(mobile.iRegChannel)
				.append(mobile.vCpuHardware).append(mobile.iMemory)
				.append(mobile.vGLRender).append(mobile.vGLVersion)
				.append(mobile.vDeviceId)
				.append(user == null ? 0 : user.getUserId()).append(ip));
	}

	/**
	 * 登录分出tlog
	 */
	private void setLoginTLog(String logType, Account account, User user,
			long num, UserSession us) {
		// Date now = new Date();
		// Timestamp createDate = null;
		String pf = Constant.getPfEx(account.getPf());
		String areaId = account.getAreaId();
		// logService.log(AbsLogLineBuffer
		// .getBuffer(areaId, TLogTable.TLOG_LOGIN.getName())
		// .append(logType).append(account.getAccId())
		// .append(account.getAccName())
		// .append(user == null ? 0 : user.getUsrLv()).append(pf)
		// .append(account.getVia()).append(now)
		// .append(account.getLoginIp()).append(num)
		// .append(user == null ? createDate : user.getCreateDate()));

		logService.log(LogBeanFactory.buildLoginLog(user, logType,
				account.getAccId(),
				pf, account.getVia(), account.getLoginIp(),
				new Timestamp(System.currentTimeMillis()),
				user == null ? null : user.getCreateDate()));


		// UserSession us =
		// OnlineUserSessionManager.getUserSessionByAccId(account
		// .getAccId());
		AbsLogLineBuffer buffer = null;
		MobileClient mobile = null;
		if (us != null) {
			mobile = us.getMobileClient();
		} else {
			mobile = new MobileClient();
		}
		int countryId = 0;
		if (user != null) {
			countryId = user.getCountryId();
		}
		if (logType == LogTypeConst.TYPE_LOGIN) {
			// <entry name="vGameSvrId" type="string" size="25"
			// desc="(必填)登录的游戏服务器编号" />
			// <entry name="dtEventTime" type="datetime"
			// desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
			// <entry name="vGameAppid" type="string" size="32"
			// desc="(必填)游戏APPID" />
			// <entry name="iPlatID" type="int" desc="(必填)ios 0/android 1"/>
			// <entry name="iZoneID" type="int"
			// desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
			// <entry name="vopenid" type="string" size="64"
			// desc="(必填)用户OPENID号" />
			// <entry name="iLevel" type="int" desc="(必填)等级" />
			// <entry name="iPlayerFriendsNum" type="int" desc="(必填)玩家好友数量"/>
			// <entry name="vClientVersion" type="string" size="64"
			// desc="(必填)客户端版本"/>
			// <entry name="vSystemSoftware" type="string" size="64"
			// desc="(可选)移动终端操作系统版本"/>
			// <entry name="vSystemHardware" type="string" size="64"
			// desc="(必填)移动终端机型"/>
			// <entry name="vTelecomOper" type="string" size="64"
			// desc="(必填)运营商"/>
			// <entry name="vNetwork" type="string" size="64"
			// desc="(必填)3G/WIFI/2G"/>
			// <entry name="iScreenWidth" type="int" desc="(可选)显示屏宽度"/>
			// <entry name="iScreenHight" type="int" desc="(可选)显示屏高度"/>
			// <entry name="Density" type="float" desc="(可选)像素密度"/>
			// <entry name="iLoginChannel" type="int" desc="(必填)登录渠道"/>
			// <entry name="vCpuHardware" type="string" size="64"
			// desc="(可选)cpu类型|频率|核数"/>
			// <entry name="iMemory" type="int" desc="(可选)内存信息单位M"/>
			// <entry name="vGLRender" type="string" size="64"
			// desc="(可选)opengl render信息"/>
			// <entry name="vGLVersion" type="string" size="64"
			// desc="(可选)opengl版本信息"/>
			// <entry name="vDeviceId" type="string" size="64" desc="(可选)设备ID"/>
			buffer = TlogHeadUtil.getTlogHead(
					LogTypeConst.TLOG_TYPE_ROLE_LOGIN, account, user, us);
			int lv = 0;
			int friendCount = 0;
			int vipLv = 0;
			if (user != null) {
				lv = user.getUsrLv();
				// friendCount = friendService.getFirendCount(user.getUserId());
				// vipLv = vipService.getVipLvByUserId(user.getUserId());
			}

			buffer.append(lv).append(friendCount).append(mobile.vClientVersion)
					.append(mobile.vSystemSoftware)
					.append(mobile.vSystemHardware).append(mobile.vTelecomOper);
			buffer.append(mobile.vNetwork).append(mobile.iScreenWidth)
					.append(mobile.iScreenHight).append(mobile.Density)
					.append(mobile.iRegChannel).append(mobile.vCpuHardware);
			buffer.append(mobile.iMemory).append(mobile.vGLRender)
					.append(mobile.vGLVersion).append(mobile.vDeviceId)
					.append(countryId).append(vipLv);
		} else {
			// <struct name="PlayerLogout" version="1" desc="(必填)玩家登出">
			// <entry name="vGameSvrId" type="string" size="25"
			// desc="(必填)登录的游戏服务器编号" />
			// <entry name="dtEventTime" type="datetime"
			// desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
			// <entry name="vGameAppid" type="string" size="32"
			// desc="(必填)游戏APPID" />
			// <entry name="iPlatID" type="int" desc="(必填)ios 0/android 1"/>
			// <entry name="iZoneID" type="int"
			// desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
			// <entry name="vopenid" type="string" size="64"
			// desc="(必填)用户OPENID号" />
			// <entry name="iOnlineTime" type="int" desc="(必填)本次登录在线时间(秒)" />
			// <entry name="iLevel" type="int" desc="(必填)等级" />
			// <entry name="iPlayerFriendsNum" type="int" desc="(必填)玩家好友数量"/>
			// <entry name="vClientVersion" type="string" size="64"
			// desc="(必填)客户端版本"/>
			// <entry name="vSystemSoftware" type="string" size="64"
			// desc="(可选)移动终端操作系统版本"/>
			// <entry name="vSystemHardware" type="string" size="64"
			// desc="(必填)移动终端机型"/>
			// <entry name="vTelecomOper" type="string" size="64"
			// desc="(必填)运营商"/>
			// <entry name="vNetwork" type="string" size="64"
			// desc="(必填)3G/WIFI/2G"/>
			// <entry name="iScreenWidth" type="int" desc="(可选)显示屏宽度"/>
			// <entry name="iScreenHight" type="int" desc="(可选)显示高度"/>
			// <entry name="Density" type="float" desc="(可选)像素密度"/>
			// <entry name="iLoginChannel" type="int" desc="(可选)登录渠道"/>
			// <entry name="vCpuHardware" type="string" size="64"
			// desc="(可选)cpu类型;频率;核数"/>
			// <entry name="iMemory" type="int" desc="(可选)内存信息单位M"/>
			// <entry name="vGLRender" type="string" size="64"
			// desc="(可选)opengl render信息"/>
			// <entry name="vGLVersion" type="string" size="64"
			// desc="(可选)opengl版本信息"/>
			// <entry name="vDeviceId" type="string" size="64" desc="(可选)设备ID"/>
			// </struct>

			buffer = TlogHeadUtil.getTlogHead(
					LogTypeConst.TLOG_TYPE_ROLE_LOGOUT, account, user, us);
			buffer.append(num);
			int lv = 0;
			int friendCount = 0;
			int vipLv = 0;
			if (user != null) {
				lv = user.getUsrLv();
				// friendCount = friendService.getFirendCount(user.getUserId());
				// vipLv = vipService.getVipLvByUserId(user.getUserId());
			}
			// int pfi = 0;
			// if (us != null) {
			// pfi = Constant.getPFIntValue(us.getPf());
			// }

			buffer.append(lv).append(friendCount).append(mobile.vClientVersion)
					.append(mobile.vSystemSoftware)
					.append(mobile.vSystemHardware).append(mobile.vTelecomOper);
			buffer.append(mobile.vNetwork).append(mobile.iScreenWidth)
					.append(mobile.iScreenHight).append(mobile.Density)
					.append(mobile.iRegChannel).append(mobile.vCpuHardware);
			buffer.append(mobile.iMemory).append(mobile.vGLRender)
					.append(mobile.vGLVersion).append(mobile.vDeviceId)
					.append(countryId).append(vipLv);

		}
		tlogService.log(buffer);
	}

	private void setWeiXinPlatformInfo(Account account, UserSession us) {
		Map<String, Object> datas = (Map<String, Object>) us.getQuser();
		if (datas != null) {

			int flag = 0;
			int lv = 0;
			Object obj = datas.get("level");
			if (obj != null) {
				lv = (Integer) obj;
			}

			if (lv > 0) {
				flag = Account.WX_VIP;
			}
			// String pf = Constant.getPfEx(account.getPf());
			account.setQqFlag(flag);
			account.setQqVipLv(lv);
			account.setQqCurrFlag(flag);
			us.setQqFlag(flag);
			us.setQqFlagLv(lv);
			us.setQuser(null);
			us.addAttribute("nickName", datas.get("nickname"));
			us.addAttribute("picture", datas.get("picture"));
		}
	}

	private void setUnionPlatformInfo(Account account, UserSession us) {
		Map<String, Object> datas = (Map<String, Object>) us.getQuser();
		if (datas != null) {
			us.setQuser(null);
			us.addAttribute("nickName", datas.get("p_nickname"));
			us.addAttribute("picture", datas.get("p_avatar"));
		}
	}

	private void setQQPlatformInfo(Account account, UserSession us) {
		if (Constant.isTestEnv()) {
			setDebugQQPlatformInfo(account, us);
			return;
		}

		// String pf = Constant.getPfEx(account.getPf());
		account.setQqFlag(0);
		account.setQq3366Lv(0);
		account.setQqBlueLv(0);
		account.setQqGreenLv(0);
		account.setQqLv(0);
		account.setQqPinkLv(0);
		account.setQqPlusLv(0);
		account.setQqRedLv(0);
		account.setQqSuperLv(0);
		account.setQqVipLv(0);
		account.setQqYellowLv(0);

		// if (pf == null)
		// return;
		Object quserObj = us.getQuser();
		if (quserObj == null)
			return;

		int flag = 0;
		int lv = 0;
		String tmpStr = null;
		Quser quser = (Quser) quserObj;

		// QQ等级
		tmpStr = quser.getQq_level();
		if (tmpStr != null && !tmpStr.equals("")) {
			account.setQqLv(Integer.parseInt(tmpStr));
		}

		// 3366
		// tmpStr = quser.getPf3366_grow_level();
		// if (tmpStr != null && !tmpStr.equals("")) {
		// lv = Integer.parseInt(tmpStr);
		// account.setQq3366Lv(lv);
		// if (lv > 0) {
		// flag = flag | Account.QQ_3366;
		// }
		// }
		// 蓝钻

		// tmpStr = quser.getIs_blue_year_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_BLUE_YEAR;
		// }
		// // 如果是超级蓝钻,则忽略普通蓝钻
		// tmpStr = quser.getIs_high_blue();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_BLUE_HIGH;
		// } else {
		// tmpStr = quser.getIs_blue_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_BLUE;
		// }
		// }
		//
		// tmpStr = quser.getBlue_vip_level();
		// if (tmpStr != null && !tmpStr.equals("")) {
		// lv = Integer.parseInt(tmpStr);
		// account.setQqBlueLv(lv);
		// }

		// 黄钻
		// tmpStr = quser.getIs_yellow_year_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_YELLOW_YEAR;
		// }
		// tmpStr = quser.getIs_yellow_high_vip();
		// // 如果是超级黄钻,则忽略普通黄钻
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_YELLOW_HIGH;
		// } else {
		// tmpStr = quser.getIs_yellow_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_YELLOW;
		// }
		// }
		// tmpStr = quser.getYellow_vip_level();
		// if (tmpStr != null && !tmpStr.equals("")) {
		// lv = Integer.parseInt(tmpStr);
		// account.setQqYellowLv(lv);
		// }

		// 红钻

		// tmpStr = quser.getIs_red_year_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_RED_YEAR;
		// }
		// // 如果是超级红钻,则忽略普通红钻
		// tmpStr = quser.getIs_high_red();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_RED_HIGH;
		// } else {
		// tmpStr = quser.getIs_red_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_RED;
		// }
		// }
		//
		// tmpStr = quser.getRed_vip_level();
		// if (tmpStr != null && !tmpStr.equals("")) {
		// lv = Integer.parseInt(tmpStr);
		// account.setQqRedLv(lv);
		// }

		// Q+
		// tmpStr = quser.getQplus_level();
		// if (tmpStr != null && !tmpStr.equals("")) {
		// lv = Integer.parseInt(tmpStr);
		// account.setQqPlusLv(lv);
		// if (lv != 0) {
		// flag = flag | Account.QQ_PLUS;
		// }
		// }

		// QQ VIP等级
		tmpStr = quser.getQq_vip_level();
		if (tmpStr != null && !tmpStr.equals("")) {
			lv = Integer.parseInt(tmpStr);
			account.setQqVipLv(lv);
		}
		tmpStr = quser.getIs_vip();
		if ("1".equals(tmpStr)) {
			flag = flag | Account.QQ_VIP;
		}

		// tmpStr = quser.getIs_year_vip();
		// if ("1".equals(tmpStr)) {
		// flag = flag | Account.QQ_VIP_YEAR;
		// }
		tmpStr = quser.getIs_super_vip();
		if ("1".equals(tmpStr)) {
			flag = flag | Account.QQ_SUPER;
		}

		// TODO:
		// 粉钻
		// 红钻
		// 绿钻
		// 超Q

		account.setQqFlag(flag);
		account.setQqCurrFlag(flag);
		us.setQqFlag(flag);
		// 以下为聊天用
		// if (pf.startsWith(Constant.PF_3366)) {
		// // 3366平台同时具有蓝钻特权
		// us
		// .setQqFlag(account.getQqFlag()
		// & (Account.QQ_3366 | Account.QQ_BLUE
		// | Account.QQ_BLUE_YEAR | Account.QQ_BLUE_HIGH));
		// us.setQqFlagLv(account.getQq3366Lv());
		//
		// } else if (pf.startsWith(Constant.PF_PENGYOU)
		// || pf.startsWith(Constant.PF_QZONE)
		// || pf.startsWith(Constant.PF_QPLUS)
		// || pf.startsWith(Constant.PF_UNION)) {
		// // Qzone，朋友，Q+ 平台具有黄钻特权
		// us.setQqFlagLv(account.getQqYellowLv());
		// us
		// .setQqFlag(account.getQqFlag()
		// & (Account.QQ_YELLOW | Account.QQ_YELLOW_YEAR |
		// Account.QQ_YELLOW_HIGH));
		// } else if (pf.startsWith(Constant.PF_QQGAME)) {
		// // 蓝钻
		// us.setQqFlagLv(account.getQqBlueLv());
		// us
		// .setQqFlag(account.getQqFlag()
		// & (Account.QQ_BLUE | Account.QQ_BLUE_YEAR | Account.QQ_BLUE_HIGH));
		// } else if (pf.startsWith(Constant.PF_MAIN)) {
		// us.setQqFlagLv(account.getQqVipLv());
		// us.setQqFlag(account.getQqFlag()
		// & (Account.QQ_VIP | Account.QQ_VIP_YEAR));
		//
		// }
		// account.setQqCurrFlag(us.getQqFlag());

		us.setQuser(null);
		us.addAttribute("nickName", quser.getNickname());
		us.addAttribute("picture", quser.getPicture100());

	}

	/**
	 * 内网测试环境使用 帐号 以下列字符开始的含义： 1 3366特权 11 3366特权+普通蓝钻 111 3366特权+年费蓝钻 2 普通蓝钻 22
	 * 年费蓝钻 3 普通黄钻 33 年费黄钻 333 超级黄钻 3333 超级年费黄钻
	 * 
	 * 帐号最后一位表示等级
	 * 
	 * @param account
	 * @param us
	 */
	private void setDebugQQPlatformInfo(Account account, UserSession us) {

		String openId = account.getAccName();
		String pf = account.getPf();
		int flag = 0;

		// if (openId.startsWith("1") && openId.length() > 1) {
		// // account.setQq3366Lv(3);
		// int lv = 1;
		// String lvStr = openId.substring(openId.length() - 2);
		// try {
		// lv = Integer.parseInt(lvStr);
		// } catch (Exception e) {
		//
		// }
		// account.setQq3366Lv(lv);
		// pf = Constant.PF_3366;
		// flag = flag | Account.QQ_3366;
		// if (openId.startsWith("1111")) {
		// flag = flag | Account.QQ_BLUE_HIGH | Account.QQ_BLUE_YEAR;
		// } else if (openId.startsWith("111")) {
		// flag = flag | Account.QQ_BLUE_HIGH;
		// } else if (openId.startsWith("11")) {
		// flag = flag | Account.QQ_BLUE | Account.QQ_BLUE_YEAR;
		// } else {
		// flag = flag | Account.QQ_BLUE;
		// }
		// account.setQqBlueLv(lv);
		//
		// us.setQqFlagLv(account.getQq3366Lv());
		// } else if (openId.startsWith("2") && openId.length() > 1) {
		// pf = Constant.PF_QQGAME;
		// // flag = flag | Account.QQ_BLUE;
		// int lv = 1;
		// String lvStr = openId.substring(openId.length() - 2);
		// try {
		// lv = Integer.parseInt(lvStr);
		// } catch (Exception e) {
		//
		// }
		// account.setQqBlueLv(lv);
		// us.setQqFlagLv(account.getQqBlueLv());
		//
		// if (openId.startsWith("2222")) {
		// flag = flag | Account.QQ_BLUE_HIGH | Account.QQ_BLUE_YEAR;
		// } else if (openId.startsWith("222")) {
		// flag = flag | Account.QQ_BLUE_HIGH;
		// } else if (openId.startsWith("22")) {
		// flag = flag | Account.QQ_BLUE | Account.QQ_BLUE_YEAR;
		// } else {
		// flag = flag | Account.QQ_BLUE;
		// }
		//
		// } else
		if (openId != null && openId.startsWith("4") && openId.length() > 1) {
			pf = Constant.PF_MAIN;
			flag = flag | Account.QQ_VIP;
			int lv = 1;
			String lvStr = openId.substring(openId.length() - 2);
			try {
				lv = Integer.parseInt(lvStr);
			} catch (Exception e) {

			}
			account.setQqVipLv(lv);
			us.setQqFlagLv(account.getQqVipLv());
			if (openId.startsWith("44")) {
				flag = flag | Account.QQ_VIP_YEAR;
			}

			if (openId.startsWith("444")) {
				flag = flag | Account.QQ_SUPER;
			}

		} else if (openId != null && openId.startsWith("5")
				&& openId.length() > 1) {
			pf = Constant.PF_MAIN;
			flag = flag | Account.WX_VIP;
			int lv = 1;
			if (openId.startsWith("55")) {
				lv = 2;
			}

			if (openId.startsWith("555")) {
				lv = 3;
			}
			account.setQqVipLv(lv);
			us.setQqFlagLv(account.getQqVipLv());

		}

		// else if (openId.startsWith("3") && openId.length() > 1) {
		// pf = Constant.PF_PENGYOU;
		// account.setQqYellowLv(2);
		// if (openId.startsWith("3333")) {
		// // account.setQqYellowLv(8);
		// flag = flag | Account.QQ_YELLOW_HIGH | Account.QQ_YELLOW_YEAR;
		// } else if (openId.startsWith("333")) {
		// // account.setQqYellowLv(8);
		// flag = flag | Account.QQ_YELLOW_HIGH;
		// } else if (openId.startsWith("33")) {
		// // account.setQqYellowLv(3);
		// flag = flag | Account.QQ_YELLOW | Account.QQ_YELLOW_YEAR;
		//
		// } else {
		// // account.setQqYellowLv(2);
		// flag = flag | Account.QQ_YELLOW;
		// }
		//
		// int lv = 1;
		// String lvStr = openId.substring(openId.length() - 2);
		// try {
		// lv = Integer.parseInt(lvStr);
		// } catch (Exception e) {
		//
		// }
		// account.setQqYellowLv(lv);
		// us.setQqFlagLv(account.getQqYellowLv());
		// }
		if (pf != null) {
			account.setPf(pf);
			us.setPf(account.getPf());
		}
		account.setQqFlag(flag);
		account.setQqCurrFlag(flag);
		us.setQqFlag(flag);

	}

	public Account getAccount(long accId) {
		return accountDao.getAccountByAccId(accId);
	}

	@Override
	public void logout(long accId, UserSession session) {
		// Account account = this.getAccount(accId);
		long now = System.currentTimeMillis();
		long onlineTime = 0;
		int onlineTimeSum = 0;// 连续在线时间:秒
		String openId = null;

		Account account = this.getAccount(accId);
		UserSession us = OnlineUserSessionManager.getUserSessionByAccId(accId);

		if (us != null) {

			if (!us.getSessionId().equals(session.getSessionId())) {
				// 这里是防止用户下线后立即上线，mainserver是异步调用logout的.
				log.info("重新登录过，忽略logout请求");
				return;
			}
			onlineTime = (now - us.getLoginDttm()) / 1000;
			openId = us.getOpenid();
			onlineTimeSum = us.getOnlineTimeSum();
			// System.out.println("online old11 =" +account.getOnlineTimeSum());
		} else {
			onlineTime = (now - account.getLastLoginDttm().getTime()) / 1000;
			openId = account.getAccName();
			// System.out.println("online old22 =" +account.getOnlineTimeSum());
			onlineTimeSum = (int) (account.getOnlineTimeSum() + onlineTime);
		}
		account.setLastLogoutDttm(new Timestamp(now));
		account.setOnlineTimeSum(onlineTimeSum);
		// System.out.println("online new =" +account.getOnlineTimeSum());
		accountDao.updateAccount(account);

		User user = userService.getUserByaccId(accId);
		// this.battlePower(account, user, us);
		setLoginTLog(LogTypeConst.TYPE_LOGOUT, account, user, onlineTime,
				session);

		if (us != null && user != null) {
			int userLevel = user.getUsrLv();
			if (userLevel > 1) {
				int moneyios = 0;
				int moneyandroid = 0;
				int diamondios = 0;
				int diamondandroid = 0;

				int foodandroid = 0;
				int foodios = 0;
				int brzoneandroid = 0;
				int brzoneios = 0;

				// CastleResource casRes =
				// castleService.getCastleResourceById(us
				// .getMainCasId());
				// if (Constant.isAndroid()) {
				// moneyandroid = casRes.getMoneyNum();
				// diamondandroid = user.getCash();
				// foodandroid = casRes.getFoodNum();
				// brzoneandroid = casRes.getBronzeNum();
				// } else {
				// moneyios = casRes.getMoneyNum();
				// diamondios = user.getCash();
				// foodios = casRes.getFoodNum();
				// brzoneios = casRes.getBronzeNum();
				// }
				int iFriends = 0/*
								 * friendService.getFirendCount(user.getUserId())
								 */;
				// update tb_qxsy_roleinfo set
				// level=?,iFriends=?,moneyios=?,moneyandroid=?,diamondios=?,diamondandroid=?,foodios=?,foodandroid=?,brzoneios=?,brzoneandroid=?
				// where openid=? and zoneid=?
				tlogService.logDB(new Object[] {
						LogTypeConst.SQL_UPDATE_roleinfo, userLevel, iFriends,
						moneyios, moneyandroid, diamondios, diamondandroid,
						foodios, foodandroid, brzoneios, brzoneandroid,
						us.getOpenid(), us.getAreaId() });

			}
		}

		OnlineUserSessionManager.unRegisterByAccId(accId);

		// TODO:others

	}

	@Override
	public void updateAccount(Account account) {
		accountDao.updateAccount(account);
	}

	@Override
	public Account getAccountByNameAreaId(String accName, String areaId) {
		return accountDao.getAccountByAccNameAreaId(accName, areaId);
	}

	/**
	 * account 加锁
	 * 
	 * @param account
	 * @return
	 */
	private Account lockAccount(Account account) {
		try {
			account = (Account) MemcachedManager.lockObject(account,
					account.getAccId());
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
		return account;
	}

	@Override
	public Account doDealBanAccount(String openId, long second, boolean isBan,
			String areaId, String reason) {
		Account account = this.getAccountByNameAreaId(openId, areaId);
		// Account account = accountDao.getAccountByAccId(user.getAccId());
		if (account == null) {
			return null;
		}
		account = this.lockAccount(account);
		if (isBan) {
			if (second > 0) {
				account.setStatusDec(reason);
				account.setStatus(Account.STATUS_BAN);
				account.setEnvelopDttm(UtilDate.moveSecond(second));
				accountDao.updateAccount(account);
			}
		} else {
			account.setStatus(0);
			account.setEnvelopDttm(null);
			account.setStatusDec(null);
			accountDao.updateAccount(account);
		}
		return account;
	}

	@Override
	public int getSumAccount() {
		return accountDao.getSumAccount();
	}

	@Override
	public void createAccountWhiteList(AccountWhiteList accountWhiteList) {
		accountDao.insertAccountWhiteList(accountWhiteList);

	}

	@Override
	public void updateAccountWhiteList(AccountWhiteList accountWhiteList) {
		accountDao.updateAccountWhiteList(accountWhiteList);

	}

	@Override
	public AccountWhiteList getAccountWhiteList(String openId) {
		return accountDao.getAccountWhiteList(openId);
	}

	@Override
	public int getOnlineUserEffect(long userId) {
		return 0;
		// UserSession us = OnlineUserSessionManager
		// .getUserSessionByUserId(userId);
		// if (us == null) {
		// // jobService上没有onlineUserSession
		// us = mainServerClient.sendTask(UserSession.class,
		// "wolf_onlineUserService", "getUserSessionByUserId", userId);
		// }
		// int seconds = 0;
		// if (us != null) {
		// if (testIds.containsKey(us.getOpenid())) {
		// seconds = us.getOnlineTimeSum();
		// } else {
		// return 0;
		// }
		// } else {
		// // jobServer的情况
		// User user = userService.getUserById(userId);
		// Account account = this.getAccount(user.getAccId());
		// if (!testIds.containsKey(account.getAccName())) {
		// return 0;
		// }
		// Timestamp dttm = account.getLastLogoutDttm();
		// if (dttm != null) {
		// long now = System.currentTimeMillis();
		// int offlineTimeSum = account.getOfflineTimeSum();
		// offlineTimeSum = offlineTimeSum
		// + (int) ((now - dttm.getTime()) / 1000);
		// if (offlineTimeSum > AppConstants.HOUR_5_SECONDS) {
		// return 0;
		// } else {
		// seconds = account.getOnlineTimeSum();
		// }
		// } else {
		// return 0;
		// }
		//
		// }
		//
		// if (seconds >= AppConstants.HOUR_5_SECONDS) {
		// return -100;
		// } else if (seconds >= AppConstants.HOUR_3_SECONDS) {
		// return -50;
		// } else {
		// return 0;
		// }
	}

	@Override
	public int getOnlineUserEffect(UserSession us) {
		if (testIds.containsKey(us.getOpenid())) {
			int seconds = us.getOnlineTimeSum();
			if (seconds >= 5 * AppConstants.HOUR_SECONDS) {
				return -100;
			} else if (seconds >= 3 * AppConstants.HOUR_SECONDS) {
				return -50;
			}
		}
		return 0;
	}

	@Override
	public void close(Response response) {
		// 删除等待队列{
		IoSession ioSession = response.getSession();
		if (loginQueue.contains(ioSession)) {
			loginQueue.remove(ioSession);
			if (log.isDebugEnabled()) {
				log.debug("等待队列用户下线：");
			}
		}
	}

	@Override
	public void open(Response response) {

	}

	@Override
	public void setLoginQueue(int queueLen, int period) {
		if (queueLen >= 0) {
			this.loginQueueSize = queueLen;
		}
		if (period >= 0) {
			this.queueCheckPeriod = period * 1000;
		}

	}

	@Override
	public void doBindVistor(UserSession us, Map<String, Object> params) {
		String openid = (String) params.get("openid");
		String openkey = null;

		if (openid == null) {
			throw new BaseException(-99999, "请提供openid");
		} else {
			openkey = (String) params.remove("openkey");
			if (openkey == null) {
				throw new BaseException(-99999, "请提供openkey");
			}
		}
		String pay_token = (String) params.remove("paytoken");
		String refreshToken = (String) params.remove("refreshToken");
		String pf = (String) params.remove("pf");
		String pfkey = (String) params.remove("pfkey");

		if (!us.isVistor()) {
			throw new BaseException("不是游客，不能绑定");
		}
		int platformType = 0;
		Object tmp = params.remove("pType");
		if (tmp != null) {
			platformType = MathUtils.getInt(tmp);
		}

		if (platformType != Constant.PLATFORM_TYPE) {
			if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
				throw new BaseException("本区只能绑定手机QQ");
			} else {
				throw new BaseException("本区只能绑定微信");
			}
		}

		Account hasAccount = accountDao.getAccountByAccNameAreaId(openid,
				us.getAreaId());
		if (hasAccount != null) {
			if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
				throw new BaseException("该QQ账号在本区已拥有角色，无法绑定游客账号");
			} else {
				throw new BaseException("该微信账号在本区已拥有角色，无法绑定游客账号");
			}
		}

		String oldAccName = us.getAccName();
		String oldOpenKey = us.getOpenkey();
		String oldPayToken = us.getPay_token();
		String oldPf = us.getPf();
		String oldPfKey = us.getPfKey();
		int oldPlatformType = us.getpType();
		if (platformType == Constant.PLATFORM_TYPE_QQ) {// qq平台验证
			tecentMobileQqService.verifyLogin(openid, openkey, null);
			Object quser = tecentMobileQqService.profile(openid, openkey);
			if (quser != null) {
				us.setQuser(quser);
				// VIP数据
				tecentMobileQqService.loadVip(openid, openkey, (Quser) quser);
				if (log.isDebugEnabled()) {
					log.debug("qq昵称:{}", ((Quser) quser).getNickname());
				}
			}
		} else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {// WEIXIN平台验证
			tecentWeiXinService.checkToken(openid, openkey);
			try {
				Map<String, Object> profiles = tecentWeiXinService.userInfo(
						openid, openkey);
				us.setQuser(profiles);
			} catch (Exception e) {
				log.error("取微信玩家数据出错", e);
				// do nothing
			}
		}

		long accId = us.getAccId();
		Account account = this.getAccount(accId);
		account.setAccName(openid);
		account.setPf(pf);
		account.setpType(platformType);

		us.setAccName(openid);
		us.setSessionId(openkey);
		us.setOpenkey(openkey);
		us.setpType(platformType);
		us.setPf(pf);
		us.setPfKey(pfkey);
		us.setPay_token(pay_token);
		us.setRefreshToken(refreshToken);

		if (platformType == Constant.PLATFORM_TYPE_QQ) {// qq平台
			setQQPlatformInfo(account, us);
		} else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {// WEIXIN
			setWeiXinPlatformInfo(account, us);
		}

		accountDao.updateAccount(account);

		MemcachedManager.delete("getAccountByAccName_" + oldAccName, 3);

		OnlineUserSessionManager.rebindSessionId(us, oldOpenKey);

		SyncWolfTask task = new SyncWolfTask();
		task.setServiceName("wolf_onlineUserService");
		task.setMethodName("rebindSessionId");
		task.setParams(new Object[] { us, oldOpenKey });

		// String areaId = us.getAreaId();
		// int balance = tecentPayService.getBalance(oldPlatformType,
		// oldAccName,
		// oldOpenKey, oldPayToken, oldPf, oldPfKey, areaId);
		// if (balance > 0) {
		// /**
		// * int platformType, String openid, String openkey, String pf,
		// * String dstzoneid, String payer_session_id, String payer, String
		// * srczoneid, String amt, String pfkey, String appremark
		// */
		// tecentPayService.transfer(oldPlatformType, openid, openkey, pf,
		// areaId, "hy_gameid", oldAccName, areaId, String
		// .valueOf(balance), pfkey, "");
		// }

		// 通知nodeServer更新
		try {
			NodeSessionMgr.sendTask(us.getGameNodeName(), Void.class, task);
		} catch (Exception e) {
			log.error("rebind vistor error", e);
			throw new BaseException(e);
		}

		// mainServerClient.sendTask(Void.class, "wolf_onlineUserService",
		// "rebindSessionId", new Object[] { us, oldOpenKey });

		// 不用删除mainserver上本地cache

		// TLOG
		User user = userService.getUserById(us.getUserId());

		setLoginTLog(LogTypeConst.TYPE_LOGIN, account, user, 0, us);
	}

	@Override
	public List<Account> getBanList(int areaId) {
		List<Account> accountList = accountDao.getBanList(areaId);
		if (accountList == null) {
			return new ArrayList<Account>(0);
		}
		return accountList;
	}

	@Override
	public void updateQQVip(UserSession us, Map<String, Object> params) {
		Quser user = new Quser();
		tecentMobileQqService.loadVip(us.getOpenid(), us.getOpenkey(), user);
		us.setQuser(user);

		Account account = accountDao.getAccountByAccId(us.getAccId());
		setQQPlatformInfo(account, us);
		accountDao.updateAccount(account);

		account.putPlatformProperties(params);
		// 这里就不同步session到adminserver了
	}

	@Override
	public void updateQQVipByMainServer(long accId, int qqFlag) {
		// UserSession us =
		// OnlineUserSessionManager.getUserSessionByAccId(accId);
		// if (us != null) {
		// us.setQqFlag(qqFlag);
		// if (us.getQqFlagLv() == 0) {
		// us.setQqFlagLv(1);
		// }
		//
		// // 发送给本人
		// long userId = us.getUserId();
		// List<Map<String, Object>> gifts = qqGiftService.getUserQQGifts(
		// userId, accId, QQGift.TYPE_NEWBEE);
		//
		// Map<String, Object> allDatas = new HashMap<String, Object>();
		// allDatas.put("qf", qqFlag);
		// allDatas.put("qqVipLv", 1);
		// allDatas.put("datas", gifts);
		// allDatas.put(IAMF3Action.ACTION_CMD_KEY, IAMF3Action.CMD_QQVIP);
		// allDatas.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);
		// messageService.sendMessage(0, userId, null, null, allDatas);
		// } else {
		// log.error("玩家不在线，不能通知qqVIP信息");
		// }
	}

}
