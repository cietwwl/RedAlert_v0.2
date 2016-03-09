package com.youxigu.dynasty2.common.service;

import com.youxigu.dynasty2.user.domain.UserChat;

public interface ILogOutService {
	/**
	 * 插入聊天设置
	 * 
	 * @param userChat
	 */
	void insertUserChat(UserChat userChat);

	/**
	 * 取得聊天设置
	 * 
	 * @param userId
	 * @return
	 */
	UserChat getUserChat(long userId);
}
