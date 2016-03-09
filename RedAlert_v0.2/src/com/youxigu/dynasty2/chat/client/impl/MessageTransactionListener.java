package com.youxigu.dynasty2.chat.client.impl;

import java.util.List;

import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.youxigu.dynasty2.chat.ChatMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;

public class MessageTransactionListener extends DefaultTransactionListener {

	private IChatClientService messageService;

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void doBeginAfter(Object transaction,
			TransactionDefinition definition) {
		ThreadLocalMessageCache.setInTrans(true);
	}

	@Override
	public void doCommitAfter(DefaultTransactionStatus status) {
		ThreadLocalMessageCache.setInTrans(false);
		try {
			List<ChatMessage> messages = ThreadLocalMessageCache.getData();
			if (messages != null) {
				//if (!MessageFilter.hasFiltered()) {
//				MessageFilter.doFilter(messages, MessageFilter.getEvents());
				//}
				//系统频道的发给同一个玩家的消息合并成一个
				
				messageService.sendMessagesAfterCommit(messages);
			}
		} catch (Exception e) {
			// 吃掉消息发送异常
			e.printStackTrace();
		} finally {
			ThreadLocalMessageCache.clean();
			// EventMessageFilter.clear();
		}

	}

	@Override
	public void doRollbackAfter(DefaultTransactionStatus status) {
		ThreadLocalMessageCache.clean();

	}

}
