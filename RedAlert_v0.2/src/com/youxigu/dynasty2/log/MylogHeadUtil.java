package com.youxigu.dynasty2.log;

import java.util.Date;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.impl.AccountService;
import com.youxigu.dynasty2.user.service.impl.UserService;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.UserSession;
/**
 * mylog固定头字段
 * @author Dagangzi
 *
 */
public class MylogHeadUtil {

	static final AccountService accountService = (AccountService) ServiceLocator.getSpringBean("accountService");
	static final UserService userService = (UserService) ServiceLocator.getSpringBean("userService");

	/**
	 * 		<entry  name="GameSvrId"      type="string"       size="25"							desc="(必填)登录的游戏服务器编号" />
	 *	    <entry  name="dtEventTime"    type="datetime"										desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	 *	    <entry  name="vGameAppid"      type="string"       size="32"							desc="(必填)游戏APPID" />
	 *	    <entry  name="PlatID"         type="int"							defaultvalue="0"    desc="(必填)ios 0/android 1"/>
	 *	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	 *	    <entry  name="vopenid"         type="string"       size="64"							desc="(必填)玩家" />
	 * @return
	 */
	public final static AbsLogLineBuffer getMylogHead(String logType, User user) {
		Account account = accountService.getAccount(user.getAccId());
		return MylogHeadUtil.getMylogHead(logType, account, user);
	}

	public final static AbsLogLineBuffer getMylogHead(String logType, Account account, User user) {
		UserSession us = OnlineUserSessionManager.getUserSessionByAccId(account.getAccId());
		return getMylogHead(logType, account, user, us);
	}

	public final static AbsLogLineBuffer getMylogHead(String logType, Account account, User user, UserSession us) {
		Date now = new Date();
		String openId = null;
		// String pf = null;
		// String ip = null;
		// String via = null;
		String areaId = null;
		// String appid = null;
		String head = "";
		int dType = 1;
		if (us != null) {
			openId = us.getOpenid();
			// pf = us.getPfEx();
			// ip = us.getUserip();
			// via = us.getVia();
			areaId = us.getAreaId();
			// if (us.getpType() == Constant.PLATFORM_TYPE_QQ) {
			// appid = Constant.APP_ID_QQ;
			// } else if (us.getpType() == Constant.PLATFORM_TYPE_WEIXIN) {
			// appid = Constant.APP_ID_WX;
			// } else {
			// appid = "wx46734908b808f4eb";// 瞎写的 正常不会出现这个问题，
			// }
			if (us.getdType() == Constant.DEVIDE_TYPE_IOS) {
				dType = 0;
			} else {
				dType = 1;
			}
			if (us.isVistor()) {
				head = "G_";
			}
		} else {
			// via = account.getVia();
			openId = account.getAccName();
			// ip = account.getLoginIp();
			// pf = Constant.getPfEx(account.getPf());
			areaId = account.getAreaId();

		}

		return AbsLogLineBuffer
				.getBuffer(areaId, AbsLogLineBuffer.TYPE_MYLOG, logType)
				.append(Constant.AREA_ID)
				.append(now).append(head + Constant.getAppId()).append(dType).append(areaId).appendLegacy(openId);
	}
	
	/**
	 * 取得渠道号
	 * @param via
	 * @return
	 */
	public final static int getIntVia(String via) {
		int intVia = 0;
		try {
			intVia = Integer.parseInt(via);
		} catch (Exception e) {

		}
		return intVia;
	} 

}
