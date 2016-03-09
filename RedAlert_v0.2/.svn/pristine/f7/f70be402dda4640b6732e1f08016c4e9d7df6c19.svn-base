package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.wolf.net.OnlineUserSessionManager;

/**
 * 文件名    SecBanOperateProcessor.java
 *
 * 描  述    禁止操作，与deying协商做成封号
 *
 * 时  间    2014-12-3
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.4
 */
public class SecBanOperateProcessor extends AreaAndOpenProcessor{

	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
		int Time = body.path("Time").getValueAsInt();
		String reason = body.path("Tip").getValueAsText();
		super.accountService.doDealBanAccount(account.getAccName(), Time, true, account.getAreaId(),reason);
		OnlineUserSessionManager.unRegisterByAccId(account.getAccId());
		returnBody.put("Result", 0);
		returnBody.put("RetMsg", "封号成功");
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4127;
	}

	@Override
	public int getRetrunCmd() {
		return 4128;
	}

}
