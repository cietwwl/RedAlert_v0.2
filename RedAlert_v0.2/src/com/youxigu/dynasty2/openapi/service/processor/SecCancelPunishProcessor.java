package com.youxigu.dynasty2.openapi.service.processor;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 文件名 SecCancelPunishProcessor.java
 * 
 * 描 述 取消惩罚
 * 
 * 时 间 2014-12-3
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.4
 */
public class SecCancelPunishProcessor extends AreaAndOpenProcessor {
	
	private IChatClientService chatService;
	// "RelieveAllPlay" : , /* 解除所有玩法限制（0 否，1 是） */
	// "RelieveBan" : , /* 解除封号（0 否，1 是） */
	// "RelieveMaskchat" : , /* 解除禁言（0 否，1 是） */
	@Override
	protected Status doProcessAndViewImp(Account account, User user,
			JsonNode body, Map<String, Object> returnBody) {
		int RelieveAllPlay = body.path("RelieveAllPlay").getValueAsInt();
		int RelieveBan = body.path("RelieveBan").getValueAsInt();
		int RelieveMaskchat = body.path("RelieveMaskchat").getValueAsInt();
//		if (RelieveAllPlay == 1) {
//
//		}
		/* 解除封号（0 否，1 是） */
		if (RelieveAllPlay == 1||RelieveBan == 1) {
			int second = 0;
			if (account.getEnvelopDttm() != null) {
				second = (int) ((account.getEnvelopDttm().getTime() - System
						.currentTimeMillis()) / 1000);
				second = second < 0 ? 0 : second;
			}
			accountService.doDealBanAccount(account.getAccName(), second,
					false, account.getAreaId(),"");
		}
		/* 解除禁言（0 否，1 是） */
		if (RelieveMaskchat == 1) {
			chatService.removeFromBlackList(user.getUserId());
		}
		
//		 "Result" : ,      /* 操作结果 */
//	        "RetMsg" : ""     /* 返回消息 */
		returnBody.put("Result", 0);
		returnBody.put("RetMsg", "操作成功");
		return new Status();
	}

	@Override
	public int getCmd() {
		return 4133;
	}

	@Override
	public int getRetrunCmd() {
		return 4134;
	}

	public void setChatService(IChatClientService chatService) {
		this.chatService = chatService;
	}

	
}
