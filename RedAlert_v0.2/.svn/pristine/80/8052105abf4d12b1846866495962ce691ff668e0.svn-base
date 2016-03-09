package com.youxigu.dynasty2.user.service;

import com.youxigu.dynasty2.user.domain.UserCount;
/**
 * 需要外部加锁
 * 用户操作次数记录辅助类，记录操作次数
 * @author Dagangzi
 *
 */
public interface IUserCountService {
	/**
	 * 取得指定类型的君主计数器
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	UserCount getUserCount(long userId, String type);

	/**
	 * 创建指定类型的君主计数器
	 * 
	 * @param userCount
	 */
	void createUserCount(UserCount userCount);

	/**
	 * 修改指定类型的君主计数器
	 * 
	 * @param userCount
	 */
	void updateUserCount(UserCount userCount);
}
