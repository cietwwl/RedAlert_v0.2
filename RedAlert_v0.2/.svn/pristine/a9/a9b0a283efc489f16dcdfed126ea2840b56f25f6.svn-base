package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 文件名    AreaAndOpenProcessor.java
 *
 * 描  述    凡是带openId 和小区id的可以实现该接口
 *
 * 时  间    2014-8-25
 *
 * 作  者    huhaiquan
 *
 * 版  本   v2.3  
 */
public abstract class AreaAndOpenProcessor extends AIdipProcessor {

	@Override
	protected Status doProcessAndView(JsonNode body,
			Map<String, Object> returnBody) {
		String OpenId = body.path("OpenId").getValueAsText();
		String AreaId = body.path("Partition").getValueAsText();
		Account account = accountService.getAccountByNameAreaId(OpenId, AreaId);
		Status status = new Status();
		if (OpenId == null || OpenId.equals("")) {
			status.errCode = ERR_CODE_1003;
			status.errDesc = "请求传入的OpenId为空";
			return status;
		}
		if (account == null) {
			status.errCode = ERR_CODE_1004;
			status.errDesc = "用户不存在";
			return status;
		}
		User user = userService.getUserByaccId(account.getAccId());
		
		if(user==null){
			status.errCode = ERR_CODE_1004;
			status.errDesc = "用户不存在";
			return status;
		}
		return doProcessAndViewImp(account, user, body, returnBody);
	}
	
	protected abstract Status doProcessAndViewImp(Account account,User user,JsonNode body,
			Map<String, Object> returnBody);
	


}
