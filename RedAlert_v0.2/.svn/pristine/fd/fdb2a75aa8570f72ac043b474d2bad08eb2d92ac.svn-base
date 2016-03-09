package com.youxigu.dynasty2.friend.service;

import java.util.List;

import com.youxigu.dynasty2.user.domain.UserChat.RecentTimeFriend;

/**
 * 好友推荐缓存
 * 
 * @author Administrator
 * 
 */
public interface IFriendRecommendCacheService {

	/**
	 * 加入好友推荐缓存
	 * 
	 * @param userId
	 * @param oldLv
	 * @param newLv
	 */
	void addCache(long userId, int oldUsrLv, int newUsrLv);

	/**
	 * 获取推荐的玩家id
	 * 
	 * @param usrv
	 * @return
	 */
	Long[] getCache(int usrv);

	/**
	 * 获取玩家的最近联系人列表
	 * 
	 * @param userId
	 * @return
	 */
	List<RecentTimeFriend> getRecentTimeFriendList(long userId);

	/**
	 * 移除最近联系人
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	boolean removeRecentTimeFriend(long userId, long friendId);
}
