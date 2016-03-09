package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.wolf.net.OnlineUserSessionManager;

/**
 * 文件名    SecBanAccountProcessor.java
 *
 * 描  述    封号
 *
 * 时  间    2014-12-3
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.4
 */
public class SecBanAccountProcessor   extends AreaAndOpenProcessor{
	 // "Time" : ,         /* 封停时长（秒） */
	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
		int Time = body.path("Time").getValueAsInt();
		String Reason = body.path("Reason").getValueAsText();
		super.accountService.doDealBanAccount(account.getAccName(), Time, true, account.getAreaId(),Reason);
		OnlineUserSessionManager.unRegisterByAccId(account.getAccId());
		returnBody.put("Result", 0);
		returnBody.put("RetMsg", "封号成功");
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4131;
	}

	@Override
	public int getRetrunCmd() {
		return 4132;
	}

}
