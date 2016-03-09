package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

public class QueryCreateRoleProcessor  extends AreaAndOpenProcessor {

	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
		returnBody.put("RegisterTime", user.getCreateDate().getTime()/1000);
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4113;
	}

	@Override
	public int getRetrunCmd() {
		return 4114;
	}

}
