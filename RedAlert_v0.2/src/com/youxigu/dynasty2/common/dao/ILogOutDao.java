package com.youxigu.dynasty2.common.dao;

import com.youxigu.dynasty2.user.domain.UserChat;

/**
 * 记录聊天设置和快捷聊天记录
 * 
 * @author lvkai
 *
 */
public interface ILogOutDao {
	/**
	 * 取得聊天快捷记录
	 * 
	 * @param userId
	 * @return
	 */
	UserChat getUserChat(long userId);

	/**
	 * 保存聊天快捷记录
	 * 
	 * @param userChat
	 */
	void insertUserChat(UserChat userChat);
}
