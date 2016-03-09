package com.youxigu.dynasty2.user.dao;

import com.youxigu.dynasty2.user.domain.UserCount;
/**
 * 计数器dao接口
 * @author Dagangzi
 *
 */
public interface IUserCountDao {
	/**
	 * 取得指定类型的君主计数器
	 * @param userId
	 * @param type
	 * @return
	 */
	UserCount getUserCount(long userId, String type);
	
	/**
	 * 创建指定类型的君主计数器
	 * @param userCount
	 */
	void createUserCount(UserCount userCount);
	
	/**
	 * 修改指定类型的君主计数器
	 * @param userCount
	 */
	void updateUserCount(UserCount userCount);
}
