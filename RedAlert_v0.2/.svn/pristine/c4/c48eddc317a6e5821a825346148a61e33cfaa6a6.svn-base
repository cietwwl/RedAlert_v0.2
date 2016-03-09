package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 文件名    SecBanTalkProcessor.java
 *
 * 描  述    禁言
 *
 * 时  间    2014-12-3
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.4
 */
public class SecBanTalkProcessor extends AreaAndOpenProcessor {
	private IChatClientService chatService;
	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
//        "Time" : ,         /* 禁言时长（秒） */
//        "Reason" : "",     /* 禁言原因 */
		int Time = body.path("Time").getValueAsInt();
		chatService.disableChat(user.getUserId(), Time);
//		 "Result" : ,      /* 操作结果 */
//        "RetMsg" : ""     /* 返回消息 */
	returnBody.put("Result", 0);
	returnBody.put("RetMsg", "操作成功");
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4129;
	}

	@Override
	public int getRetrunCmd() {
		return 4130;
	}

	public void setChatService(IChatClientService chatService) {
		this.chatService = chatService;
	}
	
	

}
