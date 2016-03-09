package com.youxigu.dynasty2.gmtool.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.activity.domain.Activity;
import com.youxigu.dynasty2.activity.domain.AwardActivity;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.service.IActivityService;
import com.youxigu.dynasty2.activity.service.IGMOperateActivityService;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.ChatInterface;
import com.youxigu.dynasty2.chat.ChatMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.develop.domain.CastleBuilding;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.DropPackEntity;
import com.youxigu.dynasty2.entity.domain.DropPackItem;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.enumer.ItemType;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.gmtool.dao.IGmToolDao;
import com.youxigu.dynasty2.gmtool.domain.OnlineUser;
import com.youxigu.dynasty2.gmtool.domain.UserCashBonus;
import com.youxigu.dynasty2.gmtool.service.IGMToolService;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.log.imp.JunGongAct;
import com.youxigu.dynasty2.log.imp.LogActiveAct;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.log.imp.LogHeroAct;
import com.youxigu.dynasty2.log.imp.LogHpAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.log.imp.LogUserExpAct;
import com.youxigu.dynasty2.mail.domain.MailMessage;
import com.youxigu.dynasty2.mail.service.IMailMessageService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.mission.service.IWorldMissionClientService;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.IForumService;
import com.youxigu.dynasty2.user.service.IOperPasswordService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.UserSession;

import groovy.lang.GroovyShell;

public class GMToolService implements IGMToolService {
	private static Logger log = LoggerFactory.getLogger(GMToolService.class);
	private IUserService userService;
	private IAccountService accountService;
	private ITroopService troopService;
	private IHeroService heroService;
	private ITreasuryService treasuryService;
	private IEntityService entityService;
	private ICastleResService castleResService;
	private ICastleService castleService;
	private DataSource dataSource;
	private ICommonService commonService;
	private IGmToolDao gmToolDao;
	private IMailMessageService mailMessageService;
	private ChatInterface chatService;
	private IGMOperateActivityService operateActivityService = null;
	private IActivityService activityService;
	private IChatClientService messageService;
	private IOperPasswordService operPasswordService;
	private IForumService forumService;
	private IMissionService missionService;
	private IWorldMissionClientService worldMissionClientService;

	public void setForumService(IForumService forumService) {
		this.forumService = forumService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setActivityService(IActivityService activityService) {
		this.activityService = activityService;
	}

	public void setOperateActivityService(
			IGMOperateActivityService operateActivityService) {
		this.operateActivityService = operateActivityService;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setTroopService(ITroopService troopService) {
		this.troopService = troopService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public static void setLog(Logger log) {
		GMToolService.log = log;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setGmToolDao(IGmToolDao gmToolDao) {
		this.gmToolDao = gmToolDao;
	}

	public void setMailMessageService(IMailMessageService mailMessageService) {
		this.mailMessageService = mailMessageService;
	}

	public void setChatService(ChatInterface chatService) {
		this.chatService = chatService;
	}

	public void setOperPasswordService(IOperPasswordService operPasswordService) {
		this.operPasswordService = operPasswordService;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setWorldMissionClientService(
			IWorldMissionClientService worldMissionClientService) {
		this.worldMissionClientService = worldMissionClientService;
	}

	private Account getAccountByOpenIdAreaId(String openIdAreaId) {
		Account account = accountService.getAccountByNameAreaId(openIdAreaId,
				Constant.AREA_ID);
		if (account != null) {
			return account;
		}
		if (openIdAreaId.indexOf("_") < 0) {
			account = accountService.getAccountByNameAreaId(openIdAreaId,
					Constant.AREA_ID);
		} else {
			int pos = openIdAreaId.lastIndexOf("_");
			String accName = openIdAreaId.substring(0, pos);
			String areaId = openIdAreaId.substring(pos + 1,
					openIdAreaId.length());
			account = accountService.getAccountByNameAreaId(accName, areaId);
		}
		return account;

	}

	@Override
	public String doAddHero(String accId, int sysHeroId) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Account account = getAccountByOpenIdAreaId(accId);
		if (account == null) {
			throw new BaseException("账号不存在");
		}
		User user = userService.getUserByaccId(account.getAccId());
		if (user == null) {
			throw new BaseException("角色不存在");
		}

		heroService.doCreateAHero(user.getUserId(), sysHeroId,
				LogHeroAct.GM_Hero_ADD);
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	private User getUser(String accId) {
		Account account = getAccountByOpenIdAreaId(accId);
		if (account == null) {
			throw new BaseException("账号不存在");
		}
		User user = userService.getUserByaccId(account.getAccId());
		if (user == null) {
			throw new BaseException("角色不存在");
		}
		return user;
	}

	@Override
	public String doAddItemToTreasury(String accId, int itemId, int itemNum) {
		return this.doAddItem(accId, itemId, itemNum);
	}

	@Override
	public String doCreateCastleBuilding(String accId, int buildingEntId) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		User user = getUser(accId);
		long casId = user.getMainCastleId();
		List<Integer> buildingIds = new ArrayList<Integer>();
		buildingIds.add(buildingEntId);
		List<CastleBuilding> castleBuildings = castleService
				.createCastleBuildingsWithoutLimit(casId, buildingIds);
		if (castleBuildings.size() == 0) {
			map.put("success", false);
		} else {
			map.put("success", true);
		}
		return JSONUtil.toJson(map);
	}

	@Override
	public String doUpgradeCastleBuilding(String accId, int buildingEntId,
			int builderIndex) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		User user = getUser(accId);
		long casId = user.getMainCastleId();
		CastleBuilding castleBuilding = null;
		List<CastleBuilding> castleBuildings = castleService
				.doGetCastleBuildingsByCasId(casId);
		if (castleBuildings != null && castleBuildings.size() > 0) {
			for (CastleBuilding cb : castleBuildings) {
				if (cb.getBuiEntId() == buildingEntId) {
					castleBuilding = cb;
					break;
				}
			}
		}
		if (castleBuilding == null) {
			throw new BaseException("指定的建筑不存在。");
		}
		castleService.doUpgradeCastleBuildingWithoutLimit(casId, builderIndex,
				castleBuilding.getCasBuiId());
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	// @Override
	// public String doChangeHeroLevel(String accId, long heroId, int level) {
	// Map<String, Boolean> map = new HashMap<String, Boolean>();
	// User user = getUser(accId);
	// long userId = user.getUserId();
	// heroService.doGmUppdateHeroLevel(userId, heroId, level);
	// map.put("success", true);
	// return JSONUtil.toJson(map);
	// }

	@Override
	public String doAddUserExp(String accId, int exp) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		User user = getUser(accId);
		if (exp <= 0) {
			throw new BaseException("君主经验不能小于0");
		}
		userService.doUpdateUserHonor(user, exp, LogUserExpAct.User_EXP_4);// 增加经验
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doSqlQuery(String sql) {
		Connection _conn = null;
		Statement _stmt = null;
		String str = null;
		ResultSet rs = null;
		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();
		try {
			_conn = dataSource.getConnection();
			_stmt = _conn.createStatement();
			rs = _stmt.executeQuery(sql);
			ResultSetMetaData rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();
			boolean flag = true;
			while (rs.next()) {
				flag = false;
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 1; i < columnCount + 1; i++) {
					String cName = rsm.getColumnName(i);
					Object obj = rs.getObject(cName);
					map.put(cName, obj);
				}
				list.add(map);
			}
			if (flag) {// 表示无数据，直接返回列头
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				for (int i = 1; i < columnCount + 1; i++) {
					String cName = rsm.getColumnName(i);
					map.put(cName, null);
				}
				list.add(map);
			}
			str = JSONUtil.toJson(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (_stmt != null) {
				try {
					_stmt.close();
				} catch (Exception e) {
				}
			}
			if (_conn != null) {
				try {
					_conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}

	@Override
	public String doGetOnlineUser(Timestamp start, Timestamp end, boolean flag) {
		String str = "";
		if (flag) {
			List<OnlineUser> onlines = gmToolDao.getOnlineUser(start, end);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (OnlineUser online : onlines) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dtStatTime", online.getDtStatTime().getTime());
				map.put("iUserNum", online.getIUserNum());
				list.add(map);
			}
			str = JSONUtil.toJson(list);
		}
		int curr = OnlineUserSessionManager.getCurrentOnlineUserNum();// 当前在线人数
		return str + "_" + curr;
	}

	@Override
	public String doSetSystemParam(String paramId, String value) {
		commonService.updateSysPara(paramId, value);
		// if (paramId.equals(AppConstants.SYS_OPEN_EGG_ACT)) {
		// openEggActivityService.startActivity(false);
		// }
		// if (paramId.equals(AppConstants.SYS_LINE_FRIEND_EFFECT_TIME)) {
		// ILinePlatformFriendService linePlatformFriendService =
		// (ILinePlatformFriendService) ServiceLocator
		// .getSpringBean("linePlatformFriendService");
		// linePlatformFriendService.doclearInviteFriends();
		// }
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("success", true);
		return JSONUtil.toJson(map);

	}

	@Override
	public String doSendMail(String userNames, String mailTilte,
			String mailContent, String entityIds) {
		String[] UIds = StringUtils.split(userNames, ",");
		for (String userName : UIds) {
			MailMessage mail = new MailMessage();
			// Account account = getAccountByOpenIdAreaId(userName);
			// if (account == null) {
			// continue;
			// }
			// 运营需要改成角色名
			User user = userService.getUserByName(userName);
			if (user == null)
				continue;
			mail.setReceiveUserId(user.getUserId());
			mail.setTitle(AppConstants.FONT_COLOR_WHITE + mailTilte
					+ "</color>");
			mail.setComment(mailContent);
			mail.setMessageType(MailMessage.TYPE_SYSTEM);
			mail.setSendDttm(new Timestamp(System.currentTimeMillis()));
			if (entityIds != null && !"".equals(entityIds)) {
				String[] entityId = entityIds.split(",");
				for (int i = 0; i < entityId.length; i++) {
					String[] ids = entityId[i].split("=");
					int eId = Integer.valueOf(ids[0].trim());
					short num = Short.valueOf(ids[1].trim());
					switch (i) {
					case 0:
						mail.setEntityId0(eId);
						mail.setItemNum0(num);
						mail.setStatus0ByPos(0, MailMessage.APPENDIX_HAVE);
						break;
					case 1:
						mail.setEntityId1(eId);
						mail.setItemNum1(num);
						mail.setStatus0ByPos(1, MailMessage.APPENDIX_HAVE);
						break;
					case 2:
						mail.setEntityId2(eId);
						mail.setItemNum2(num);
						mail.setStatus0ByPos(2, MailMessage.APPENDIX_HAVE);
						break;

					case 3:
						mail.setEntityId3(eId);
						mail.setItemNum3(num);
						mail.setStatus0ByPos(3, MailMessage.APPENDIX_HAVE);
						break;

					case 4:
						mail.setEntityId4(eId);
						mail.setItemNum4(num);
						mail.setStatus0ByPos(4, MailMessage.APPENDIX_HAVE);
						break;

					case 5:
						mail.setEntityId5(eId);
						mail.setItemNum5(num);
						mail.setStatus0ByPos(5, MailMessage.APPENDIX_HAVE);
						break;
					}
				}
			}
			mailMessageService.createSystemMessage(mail);
		}
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doPublishNotice(String noticeContent, String url, int locale,
			String pf) {
		noticeContent = noticeContent.trim();
		String channelId = "";
		if (locale == 0) {
			channelId = "GM";
		}

		ChatMessage chatMessage = new ChatMessage(null, null,
				ChatChannelManager.CHANNEL_NOTICE, channelId, noticeContent);
		if (url != null && url.length() > 0) {
			chatMessage.setUrl(url);
		}

		if (pf != null && pf.length() > 0) {
			chatMessage.setPf(pf);
		}
		chatService.userSendMessage(chatMessage);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public void setOpenServerTime(long time) {
		commonService.setServerInfoTime(time);
	}

	@Override
	public String doDeleteItem(String accId, int itemId, int num) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Account account = getAccountByOpenIdAreaId(accId);
		User user = userService.getUserByaccId(account.getAccId());
		int count = treasuryService.getTreasuryCountByEntId(user.getUserId(),
				itemId);
		if (num > count) {
			num = count;
		}
		if (num == 0) {
			map.put("success", true);
			return JSONUtil.toJson(map);
		}
		treasuryService.deleteItemFromTreasury(user.getUserId(), itemId, num,
				true, com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_203);
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doAddItem(String accId, int itemId, int itemNum) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		User user = getUser(accId);
		long userId = user.getUserId();
		Item item = (Item) entityService.getEntity(itemId);
		// 加武将图纸
		LogItemAct act = com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_1;
		if (item.getItemType().isHeroCard()) {
			heroService.doAddHeroCardNum(userId, item, itemNum, act);
			map.put("success", true);
		} else if (item.getItemType().isHeroSoul()) {
			heroService.doAddHeroSoulNum(userId, item, itemNum, act);
			map.put("success", true);
		} else {
			treasuryService.addItemToTreasury(user.getUserId(),
					item.getEntId(), itemNum, 0, 1, false, true, act);
			map.put("success", true);
		}
		return JSONUtil.toJson(map);
	}

	@Override
	public String doAddResource(String accId, int type, int num) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Account account = getAccountByOpenIdAreaId(accId);
		User user = userService.getUserByaccId(account.getAccId());
		switch (type) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			castleResService.doGmtoolAddResource(user.getMainCastleId(), num,
					type);
			break;
		default:
			throw new BaseException("错误的类型");
		}
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doAddMoney(String accId, int type, int num) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Account account = getAccountByOpenIdAreaId(accId);
		User user = userService.getUserByaccId(account.getAccId());
		switch (type) {
		case 1: {
			// 元宝
			if (num > 0) {
				userService.doGmAddCash(user.getUserId(), num,
						LogCashAct.GM_ADD_ACTION);
			} else {
				userService.doConsumeCash(user.getUserId(), Math.abs(num),
						LogCashAct.GM_DES_ACTION);
			}
			break;
		}
		case 2:
			// 体力
			if (num > 0) {
				userService.doAddHpPoint(user, num, LogHpAct.GM_ADD);
			} else {
				userService.doCostHpPoint(user, Math.abs(num), LogHpAct.GM_DEL);
			}
			break;
		case 3:
			// 行动力
			if (num > 0) {
				userService.doAddCurActPoint(user, num, LogActiveAct.GM_ADD);
			} else {
				userService.doCostCurActPoint(user, Math.abs(num),
						LogActiveAct.GM_DEL);
			}
			break;
		case 4:
			// 军功
			if (num > 0) {
				userService.doAddjunGong(user, num, JunGongAct.TYPE_GM_ADD);
			} else {
				userService.doCostjunGong(user, Math.abs(num),
						JunGongAct.TYPE_GM_DEL);
			}
			break;

		default: {
			throw new BaseException("错误的类型");
		}
		}
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doAddExp2Hero(String accId, long heroId, int exp) {
		Account account = getAccountByOpenIdAreaId(accId);
		User user = userService.getUserByaccId(account.getAccId());
		heroService.lockHero(user.getUserId());
		Hero hero = heroService.getHeroByHeroId(user.getUserId(), heroId);
		if (hero == null) {
			hero = heroService.getHeroBySysHeroId(user.getUserId(),
					(int) heroId);
		}
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (hero != null) {
			// hero.setExp(hero.getExp() + exp);
			heroService.doChangeHeroExp(hero, exp, false);
		} else {
			map.put("success", false);
		}
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String lockAccount(String openId, long second, int isBan) {
		// lockAccount这个方法不要事务，因为OnlineUserSessionManager.unRegisterByAccId会通知nodeserverlogout,也会更新Account,并且是异步的
		// 去掉事务，保证accService.doDealBanAccount先执行完毕，并更新MC+DB,然后再通知nodeserver.
		// 否则不一定哪个server先更新account.导致封号不成功
		IAccountService accService = (IAccountService) ServiceLocator
				.getSpringBean("accountService");
		Account account = accService.doDealBanAccount(openId, second,
				isBan == 1, Constant.AREA_ID, "");
		if (openId.indexOf("_") < 0) {
			account = accService.doDealBanAccount(openId, second, isBan == 1,
					Constant.AREA_ID, "");
		} else {
			int pos = openId.lastIndexOf("_");
			String accName = openId.substring(0, pos);
			String areaId = openId.substring(pos + 1, openId.length());
			account = accService.doDealBanAccount(accName, second, isBan == 1,
					areaId, "");
		}

		OnlineUserSessionManager.unRegisterByAccId(account.getAccId());

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public Object doExecuteScript(String scriptText) {
		Map<String, Object> map = new HashMap<String, Object>();
		// if (scriptText.indexOf("GM") == -1) {// 做一个判断，防止乱记日志
		// map.put("result", "日志记录中必须包含GM字样");
		// return JSONUtil.toJson(map);
		// }
		GroovyShell groovyShell = new GroovyShell();
		try {
			Object O = groovyShell.evaluate(scriptText);
			map.put("result", O);

		} catch (Exception e) {
			map.put("result", e.toString());
		}
		return JSONUtil.toJson(map);
	}

	@Override
	public String doAddToBlackList(String openId, Long seconds) {
		// TODO Auto-generated method stub
		Account account = getAccountByOpenIdAreaId(openId);
		User user = userService.getUserByaccId(account.getAccId());
		long userId = user.getUserId();
		chatService.addToBlackList(userId, seconds);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doDelMemcachedKey(String key) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String[] array = key.split(";");
		for (String str : array) {
			Object obj = MemcachedManager.get(str);
			if (obj != null) {
				MemcachedManager.delete(str, 3);
				map.put("success", true);
			} else {
				map.put("success", false);
			}
		}
		return JSONUtil.toJson(map);
	}

	@Override
	public String doOperateActivity(int type, Map<String, Object> data) {
		OperateActivity operateActivity = new OperateActivity();
		operateActivity.setActId(Long.parseLong(data.get("actId").toString()));
		operateActivity.setType(Integer.parseInt(data.get("type").toString()));
		operateActivity.setActName(data.get("actName").toString());
		operateActivity.setAutoReward(Integer.parseInt(data.get("autoReward")
				.toString()));
		operateActivity.setStartTime(new Timestamp(Long.parseLong(data.get(
				"startTime").toString())));
		operateActivity.setEndTime(new Timestamp(Long.parseLong(data.get(
				"endTime").toString())));
		operateActivity.setMaxTime(new Timestamp(Long.parseLong(data.get(
				"maxTime").toString())));
		operateActivity
				.setRewardContext((data.get("rewardContext").toString()));
		if (type == 1) {
			operateActivityService.addActivity(operateActivity);
		} else if (type == 2) {
			operateActivityService.deleteActivity(operateActivity);
		} else {
			operateActivityService.updateActivity(operateActivity);
		}
		return "success";

	}

	@Override
	public String doDeleteActivity(int actId) {
		activityService.deleteActivity(actId, true);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	private Activity getActivity(Map<String, String> params) {
		if (params == null || params.size() == 0) {
			return null;
		}
		Activity activity = new Activity(Integer.valueOf(params.get("actId")),
				params.get("name"), params.get("description"),
				params.get("url"), params.get("icon"), Timestamp.valueOf(params
						.get("startDttm")), Timestamp.valueOf(params
						.get("endDttm")),
				Long.valueOf(params.get("timeStart")), Long.valueOf(params
						.get("timeEnd")), Integer.valueOf(params
						.get("weekStart")), Integer.valueOf(params
						.get("weekEnd")), params.get("channel"),
				params.get("qqYellowLv"), params.get("qqYellowLvYear"),
				params.get("qqYellowLvHigh"), params.get("qqBlueLv"),
				params.get("qqBlueLvYear"), params.get("qqBlueLvHigh"),
				params.get("qqPlusLv"), params.get("qqLv"),
				params.get("qqVipLv"), params.get("qqVipLvYear"),
				params.get("qq3366Lv"), params.get("qqRedLv"),
				params.get("qqGreenLv"), params.get("qqPinkLv"),
				params.get("qqPinkLvYear"), params.get("qqSuperLv"),
				params.get("usrLv"), params.get("caslteLv"),
				params.get("efId"), Byte.valueOf(params.get("status")),
				Byte.valueOf(params.get("relation")));
		return activity;
	}

	@Override
	public String doAddOrUpdateActivity(Map<String, String> params) {
		Activity activity = getActivity(params);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (activity != null) {
				Activity act = activityService.getActivityById(activity
						.getActId());
				if (act != null) {
					activityService.updateActivity(activity);
				} else {
					activityService.createActivity(activity);
				}
				map.put("success", true);
			} else {
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtil.toJson(map);
	}

	private void lockUserCashBonus() {
		try {
			MemcachedManager.lock("L_GM_UCB");
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
	}

	@Override
	public void doAddOrUpdateUserCashBonus(String openId, int num, int status,
			String dttmStr) {
		this.lockUserCashBonus();
		UserCashBonus ucb = gmToolDao.getUserCashBonus(openId);
		Timestamp dttm = null;
		if (dttmStr != null && dttmStr.length() > 0) {
			dttmStr = dttmStr.trim();
			dttm = Timestamp.valueOf(dttmStr);
		}
		if (ucb == null) {
			ucb = new UserCashBonus();
			ucb.setOpenId(openId);
			ucb.setNum(num);
			ucb.setStatus(status == 1 ? 1 : 0);
			ucb.setDttm(dttm);
			gmToolDao.createUserCashBonus(ucb);
		} else {
			ucb.setNum(num);
			ucb.setStatus(status == 1 ? 1 : 0);
			ucb.setDttm(dttm);
			gmToolDao.updateUserCashBonus(ucb);
		}

	}

	@Override
	public String getALlUserCashBonus() {
		List<UserCashBonus> list = gmToolDao.getAllUserCashBonus();
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		for (UserCashBonus ub : list) {
			Map<String, Object> tmp = new HashMap<String, Object>();
			tmp.put("openId", ub.getOpenId());
			tmp.put("num", ub.getNum());
			tmp.put("dttm", ub.getDttm());
			tmp.put("total", ub.getTotal());
			tmp.put("status", ub.getStatus());// =0 停止发放状态,=1可发放状态
			tmpList.add(tmp);
		}
		return JSONUtil.toJson(tmpList);
	}

	@Override
	public String getUserCashBonus(String openId) {
		UserCashBonus ub = gmToolDao.getUserCashBonus(openId);
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("openId", ub.getOpenId());
		tmp.put("num", ub.getNum());
		tmp.put("dttm", ub.getDttm());
		tmp.put("total", ub.getTotal());
		tmp.put("status", ub.getStatus());// =0 停止发放状态,=1可发放状态
		return JSONUtil.toJson(tmp);
	}

	@Override
	public void doGainUserCashBonus(long userId) {
		UserSession us = OnlineUserSessionManager
				.getUserSessionByUserId(userId);
		String openId = us.getOpenid();
		String areaId = us.getAreaId();
		this.lockUserCashBonus();
		UserCashBonus ucb = gmToolDao.getUserCashBonus(openId + "_" + areaId);
		if (ucb == null) {
			ucb = gmToolDao.getUserCashBonus(openId);
			if (ucb == null) {
				log.error("非内部玩家,不能领取:{}", openId);
				return;
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp dttm = ucb.getDttm();
		if (dttm != null) {
			if (dttm.getTime() > now.getTime()) {
				log.error("还未到可领取日期:{}", openId);
				return;
			}
			if (DateUtils.isSameMonth(now, dttm)) {
				log.error("本月已经领取过了:{}", openId);
				return;
			}
		}
		int num = ucb.getNum();
		if (num <= 0) {
			log.error("领取过数量小于0:{}", openId);
			return;

		}

		if (Constant.USE_OP_TRANS) {
			if (num > 0) {
				userService.addGiftCash(userId, num, LogCashAct.IN_ADD_ACTION);
				// vipService.doUpdateUserVip(userService.getUserById(userId),
				// num);
			} else {
				throw new BaseException("正式环境不能减元宝");
			}
		} else {
			if (num > 0) {
				userService.addCash(userId, num, LogCashAct.IN_ADD_ACTION);
			}
		}
		// 这里可以不用锁玩家,只有内部测试才会更新user,开放平台不更新user
		// userService.addGiftCash(userService.lockGetUser(userId), num,
		// "内发元宝");
		ucb.setTotal(ucb.getTotal() + num);
		ucb.setDttm(now);
		gmToolDao.updateUserCashBonus(ucb);

	}

	@Override
	public String doAddOrUpdateAwardActivity(Map<String, Object> datas) {
		boolean newActivity = false;
		int id = (Integer) datas.get("id");
		AwardActivity activity = activityService.getAwardActivityById(id);
		if (activity == null) {
			activity = new AwardActivity();
			activity.setId(id);
			newActivity = true;
		}
		activity.setActName((String) datas.get("actName"));
		activity.setActDesc((String) datas.get("actDesc"));
		activity.setIcon((String) datas.get("icon"));
		activity.setStartDttm((Timestamp) datas.get("startDttm"));
		activity.setEndDttm((Timestamp) datas.get("endDttm"));
		if (activity.getStartDttm() == null) {
			throw new BaseException("活动开始时间不能为空");
		}
		if (activity.getEndDttm() == null) {
			throw new BaseException("活动结束时间不能为空");
		}
		Integer tmp = (Integer) datas.get("type");
		if (tmp == null) {
			activity.setType(AwardActivity.TYPE_MAINTAIN);
		} else {
			activity.setType(tmp);
		}

		tmp = (Integer) datas.get("minUsrLv");
		if (tmp == null) {
			activity.setMinUsrLv(0);
		} else {
			activity.setMinUsrLv(tmp);
		}
		tmp = (Integer) datas.get("maxUsrLv");
		if (tmp == null) {
			activity.setMaxUsrLv(0);
		} else {
			activity.setMaxUsrLv(tmp);
		}

		tmp = (Integer) datas.get("itemId1");
		if (tmp == null) {
			activity.setItemId1(0);
		} else {
			activity.setItemId1(tmp);
			tmp = (Integer) datas.get("num1");
			if (tmp == null) {
				activity.setNum1(0);
			} else {
				activity.setNum1(tmp);
			}
		}

		tmp = (Integer) datas.get("itemId2");
		if (tmp == null) {
			activity.setItemId2(0);
		} else {
			activity.setItemId2(tmp);
			tmp = (Integer) datas.get("num2");
			if (tmp == null) {
				activity.setNum2(0);
			} else {
				activity.setNum2(tmp);
			}
		}

		tmp = (Integer) datas.get("itemId3");
		if (tmp == null) {
			activity.setItemId3(0);
		} else {
			activity.setItemId3(tmp);
			tmp = (Integer) datas.get("num3");
			if (tmp == null) {
				activity.setNum3(0);
			} else {
				activity.setNum3(tmp);
			}
		}

		tmp = (Integer) datas.get("itemId4");
		if (tmp == null) {
			activity.setItemId4(0);
		} else {
			activity.setItemId4(tmp);
			tmp = (Integer) datas.get("num4");
			if (tmp == null) {
				activity.setNum4(0);
			} else {
				activity.setNum4(tmp);
			}
		}

		tmp = (Integer) datas.get("itemId5");
		if (tmp == null) {
			activity.setItemId5(0);
		} else {
			activity.setItemId5(tmp);
			tmp = (Integer) datas.get("num5");
			if (tmp == null) {
				activity.setNum5(0);
			} else {
				activity.setNum5(tmp);
			}
		}

		int oldHas = activityService.hasAwardActivity();
		if (newActivity) {
			activityService.createAwardActivity(activity);
		} else {
			activityService.updateAwardActivity(activity);
		}

		int has = activityService.hasAwardActivity();
		if (has != oldHas) {
			// TODO 代码注释掉了。。根据具体需求再加
			// 推送的消息
			// Map<String, Object> event = new HashMap<String, Object>(1);
			// event.put("gmAct", has);
			// messageService.sendEventMessage(0,
			// EventMessage.TYPE_AWARD_ACTIVITY_REWARD, event);
			throw new BaseException("//TODO ...");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doDeleteAwardActivity(int[] ids) {

		int oldHas = activityService.hasAwardActivity();
		for (int id : ids) {
			activityService.deleteAwardActivity(id, true);
		}
		int has = activityService.hasAwardActivity();
		if (has != oldHas) {
			// TODO 代码注释掉了。。根据具体需求再加
			// 推送的消息
			// Map<String, Object> event = new HashMap<String, Object>(1);
			// event.put("gmAct", has);
			// messageService.sendEventMessage(0,
			// EventMessage.TYPE_USER_CHANGED,
			// event);
			throw new BaseException("//TODO ...");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doGMDelPassword(String openId, String areaId) {
		Account account = accountService.getAccountByNameAreaId(openId, areaId);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (account != null) {
			User user = userService.getUserByaccId(account.getAccId());
			map.put("success",
					operPasswordService.doGMDelPassword(user.getUserId()));
		} else {
			map.put("success", true);
		}
		return JSONUtil.toJson(map);
	}

	@Override
	public String doConfigBag(int itemId, List<Map<String, Integer>> items,
			String itemDesc, String bagName, int leve, String icon) {
		if (itemId < AppConstants.ENT_DYNAMIC_ID_MIN
				&& itemId > AppConstants.ENT_DYNAMIC_ID_MAX) {// 运营配置礼包
			throw new BaseException("只能添加运营礼包 编号只能在"
					+ AppConstants.ENT_DYNAMIC_ID_MIN + "到"
					+ AppConstants.ENT_DYNAMIC_ID_MAX + "之间");
		}

		Entity entity = entityService.getEntity(itemId);
		if (entity != null) {
			throw new BaseException("礼包已经存在了");
		}

		DropPackEntity dpe = new DropPackEntity();

		// <Item entId="10688001" itemName="君主10级礼包" itemDesc="0" iconPath="礼包"
		// type="8" sumAble="1" bandAble="0"
		// throwAble="1" useAble="1" useMaxNum="1" childType="88" level="1"
		// color="1" sellPrice="0" buyPrice="0"
		// buyHonor="0" buyGuild="0" guildLv="0" dropAble="0" instDesc="0"
		// instCur="0" instMax="0" missionId="0" time="0"
		// userHasMaxNum="0" npcSellLevel="0" useContent="0"/>
		dpe.setEntId(itemId);
		dpe.setEntType(Entity.TYPE_DROPPACK);// 掉落包
		dpe.setItemName(bagName);
		dpe.setItemDesc(itemDesc);
		dpe.setIconPath(icon);
		dpe.setType(ItemType.ITEM_TYPE_DROPPACK.getType());
		dpe.setSumAble(1);
		dpe.setBandAble(0);
		dpe.setThrowAble(1);
		dpe.setUseAble(1);
		dpe.setUseMaxNum(1);
		dpe.setChildType(ItemType.ITEM_TYPE_DROPPACK_CHILD1.getType());
		dpe.setLevel(1);
		dpe.setColor(1);
		// dpe.setSellPrice(0);
		// dpe.setBuyPrice(0);
		// dpe.setBuyHonor(0);
		// dpe.setBuyGuild(0);
		// dpe.setGuildLv(0);
		// dpe.setDropAble(0);
		// dpe.setInstDesc("0");
		// dpe.setInstCur(0);
		// dpe.setInstMax(0);
		dpe.setMissionId(0);
		dpe.setTime(0);
		dpe.setUserHasMaxNum(0);
		dpe.setStackNum(99999);
		dpe.setExchangeId(0);
		dpe.setNotJoinPack((byte) 0);
		dpe.setSysHeroEntId(0);
		// dpe.setNpcSellLevel(0);
		// dpe.setUseContent("0");

		// <DropPack Id="10060001" entId="10688001" dropentId="10610166"
		// dropPercent="10000000" minValue="1"
		// axValue="1" weight="0" />
		List<DropPackItem> ditems = new ArrayList<DropPackItem>();
		for (Map<String, Integer> map : items) {
			DropPackItem dpt = new DropPackItem();
			dpt.setEntId(itemId);
			Integer tmp = map.get("entId");
			if (tmp != null) {
				dpt.setDropEntId(tmp);
			} else {
				throw new BaseException("必须配置entId");
			}
			tmp = map.get("maxNum");
			if (tmp == null) {
				tmp = 1;
			}
			dpt.setMaxValue2(tmp);

			tmp = map.get("minNum");
			if (tmp == null) {
				tmp = dpt.getMaxValue2();
			}
			dpt.setMinValue(tmp);

			tmp = map.get("weight");
			if (tmp == null) {
				tmp = 0;
			}
			dpt.setWeight(tmp);

			tmp = map.get("percent");
			if (tmp == null) {
				tmp = 10000000;
			}
			dpt.setDropPercent(tmp);
			ditems.add(dpt);
		}
		dpe.setItems(ditems);
		if (leve > 0) {
			List<EntityLimit> entityLimits = new ArrayList<EntityLimit>(1);
			EntityLimit entityLimit = new EntityLimit();
			entityLimit.setEntId(itemId);
			entityLimit.setLeastNum(0);
			entityLimit.setLevel(1);
			entityLimit.setNeedEntId(AppConstants.ENT_USER_LV);// 君主等级
			entityLimit.setNeedLevel(leve);
			entityLimits.add(entityLimit);
			dpe.setLimits(entityLimits);
		}

		entityService.createEntity(dpe);

		// select * from item where entId=10688001;
		//
		// select * from entity where entId=10688001;
		//
		// select * from droppack where entId=10688001;
		//
		//
		// select * from entitylimit where entId=10688001;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public void doGmSynForumContext(int type, List<Map<String, Object>> dataList) {
		this.forumService.doGmSynForumContext(type, dataList);
	}

	@Override
	public String createMailInternal(long sendUserId, long receiveUserId,
			String sendTime, String readTime, String title, String comment,
			byte status, byte mailType, String appendix) {

		mailMessageService.createMailInternal(sendUserId, receiveUserId,
				sendTime, readTime, title, comment, status, mailType, appendix);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doGmAddMission(String openId, String areaId, int missionId) {
		Account account = accountService.getAccountByNameAreaId(openId, areaId);
		if (account == null) {
			throw new BaseException("账号不存在");
		}
		User user = userService.getUserByaccId(account.getAccId());
		if (user == null) {
			throw new BaseException("角色不存在");
		}
		Mission mission = missionService.getMissionById(missionId);
		if (mission.getMissionType().equals(Mission.MISSION_TYPE_WORLD)) {
			worldMissionClientService.doGmtoolCreateMission(missionId);
		} else {
			missionService.createUserMission(user.getUserId(), missionId);
		}

		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("success", true);
		return JSONUtil.toJson(map);
	}

	@Override
	public String doSetUserVip(String accId, int vip) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		Account account = getAccountByOpenIdAreaId(accId);
		if (account == null) {
			throw new BaseException("玩家不存在");
		}
		User user = userService.getUserByaccId(account.getAccId());
		if (vip <= 0 || vip > 12) {
			vip = 1;
		}
		user.setVip(vip);
		userService.doUpdateUser(user);
		map.put("success", true);
		return JSONUtil.toJson(map);
	}
}
