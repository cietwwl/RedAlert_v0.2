package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 文件名    ResetPassWordProcessor.java
 *
 * 描  述    重置二级密码
 *
 * 时  间    2015-1-19
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.7
 */
public class ResetPassWordProcessor  extends AreaAndOpenProcessor {

//	private IGMToolService gmToolService;
	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
//	    "body" :
//	    {
//	        "Result" : ,      /* 结果 */
//	        "RetMsg" : ""     /* 返回消息 */
//	    }
//		gmToolService.doGMDelPassword(account.getAccName(), account.getAreaId());
		returnBody.put("Result", 0);
		returnBody.put("RetMsg", "操作成功");
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4137;
	}

	@Override
	public int getRetrunCmd() {
		return 4138;
	}

//	public void setGmToolService(IGMToolService gmToolService) {
//		this.gmToolService = gmToolService;
//	}
	
	

}
