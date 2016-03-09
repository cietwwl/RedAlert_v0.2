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

public class TlogHeadUtil {

	static final AccountService accountService = (AccountService) ServiceLocator
			.getSpringBean("accountService");
	static final UserService userService = (UserService) ServiceLocator
			.getSpringBean("userService");

	public final static AbsLogLineBuffer getTlogHead(String logType, User user) {
		Account account = accountService.getAccount(user.getAccId());
		return TlogHeadUtil.getTlogHead(logType, account, user);
	}

	public final static AbsLogLineBuffer getTlogHead(String logType,
			Account account, User user) {
		UserSession us = OnlineUserSessionManager.getUserSessionByAccId(account
				.getAccId());
		return getTlogHead(logType, account, user, us);
	}

	public final static AbsLogLineBuffer getTlogHead(String logType,
			Account account, User user, UserSession us) {
		Date now = new Date();
		String openId = null;
		// String pf = null;
		// String ip = null;
		// String via = null;
		String areaId = null;
		// String appid = null;
		String head = "";
		int dType = 0;
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
				.getBuffer(areaId, AbsLogLineBuffer.TYPE_TLOG, logType)
				.append(Constant.SVR_IP).append(now)
				.appendLegacy(head + Constant.getAppId()).append(dType)
				.append(areaId).appendLegacy(openId);
	}

}
