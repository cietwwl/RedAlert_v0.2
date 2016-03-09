package com.youxigu.dynasty2.friend.dao;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.friend.domain.Friend;
import com.youxigu.dynasty2.friend.domain.FriendApp;
import com.youxigu.dynasty2.friend.domain.FriendHp;
import com.youxigu.dynasty2.friend.domain.FriendRecommend;
import com.youxigu.dynasty2.friend.enums.FriendGroupType;

/**
 * 好友Dao接口
 * 
 * @author Dagangzi
 *
 */
public interface IFriendDao {
	/**
	 * 取得指定id的好友
	 * 
	 * @param friendId
	 * @return
	 */
	Friend getFriendByFriendUserId(long userId, long friendUserId);

	/**
	 * 取玩家的所有好友记录
	 * 
	 * @param userId
	 * @return
	 */
	List<Friend> getAllFriendByUserId(long userId);

	/**
	 * 取分组下的所有好友
	 * 
	 * @param userId
	 * @param groupId
	 *            组别
	 * @return
	 */
	List<Friend> getFriendListByGroupId(long userId, FriendGroupType group);

	/**
	 * 取好友的数量 不包含黑名单
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	int getFriendCountByGroupId(long userId, FriendGroupType group);

	/**
	 * 加入好友
	 * 
	 * @param friend
	 */
	void insertFriend(Friend friend);

	/**
	 * 修改好友
	 * 
	 * @param friend
	 */
	void updateFriend(Friend friend);

	/**
	 * 删除好友
	 * 
	 * @param friend
	 */
	void deleteFriend(Friend friend);

	/**
	 * 取指定的好友申请记录
	 * 
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	FriendApp getFriendAppByUserIdAndFriendId(long userId, long friendUserId);

	/**
	 * 取发出的好友申请
	 * 
	 * @param userId
	 * @return
	 */
	List<FriendApp> getAllFriendAppByUserId(long userId);

	/**
	 * 取未审核的好友申请
	 * 
	 * @param friendUserId
	 * @return
	 */
	List<FriendApp> getAllFriendAppByFriendUserId(long friendUserId);

	/**
	 * 删除好友申请
	 * 
	 * @param friend
	 */
	void deleteFriendApp(FriendApp friendApp);

	/**
	 * 加入好友申请
	 * 
	 * @param friend
	 */
	void insertFriendApp(FriendApp friendApp);

	/**
	 * 删除过期申请
	 * 
	 * @param appValidDays
	 */
	void cleanFriApp(int appValidDays);

	/**
	 * 取得好友推荐配置
	 * 
	 * @return
	 */
	List<FriendRecommend> getFriendRecommends();

	List<Long> getFriendRecommendUsers(Map<String, Object> params);

	void insertFriendHp(FriendHp friendHp);

	void updateFriendHp(FriendHp friendHp);

	FriendHp getFriendHp(long userId);

}
