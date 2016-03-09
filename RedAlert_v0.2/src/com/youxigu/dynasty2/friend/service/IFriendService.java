package com.youxigu.dynasty2.friend.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.friend.domain.Friend;
import com.youxigu.dynasty2.friend.enums.FriendGroupType;
import com.youxigu.dynasty2.friend.proto.FriendInfoMsg;
import com.youxigu.dynasty2.friend.proto.FriendMsg.GroupType;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.PagerResult;
import com.youxigu.wolf.net.UserSession;

/**
 * 好友service接口
 * 
 * @author Dagangzi
 *
 */
public interface IFriendService {
	/**
	 * 无需申请直接加为好友
	 * 
	 * @param userId
	 * @param firendUser
	 */
	Friend doAddFriend(long userId, User firendUser);

	/**
	 * 申请加为好友
	 * 
	 * @param myUser
	 *            发出申请的玩家
	 * @param friendUser
	 *            接受申请的玩家
	 */
	void doAppFriend(User myUser, User friendUser);

	/**
	 * 同意加为好友
	 * 
	 * @param friendUser
	 *            发出申请的玩家
	 * @param myUser
	 *            接受申请的玩家
	 */
	void doAccFriend(User friendUser, User myUser);

	List<FriendInfoMsg> doAccAllFriend(User myUser, List<Long> ids);

	/**
	 * 拒绝加为好友
	 * 
	 * @param firendUser
	 *            发出申请的玩家
	 * @param myUser
	 *            接受申请的玩家
	 */
	void doRefuseFriend(User firendUser, User myUser);

	/**
	 * 拒绝加好友
	 * 
	 * @param myUser
	 * @param ids
	 * @return
	 */
	List<Long> doRefuseAllFriends(User myUser, List<Long> ids);

	/**
	 * 取消申请好友
	 * 
	 * @param myUser
	 *            发出申请的玩家
	 * @param friendUser
	 *            接受申请的玩家
	 */
	void doCancelFriend(User myUser, User friendUser);

	/**
	 * 好友分组
	 * 
	 * @param friend
	 *            好友用户
	 * @param groupId
	 *            新组别
	 */
	void doAddGroup(Friend friend, int groupId);

	/**
	 * 修改备注
	 * 
	 * @param friend
	 * @param note
	 */
	void doModifyNote(Friend friend, String note);

	/**
	 * 加入黑名单
	 * 
	 * @param user
	 * @param blackUser
	 * @param groupId
	 */
	void doAddBlack(User user, User blackUser);

	/**
	 * 删除黑名单
	 * 
	 * @param userId
	 * @param friendUser
	 */
	void doDelBlack(long userId, User friendUser);

	/**
	 * @author ninglong
	 *         <p>
	 *         删除好友
	 *         </p>
	 * @param friend
	 *            好友信息
	 * @param buser
	 *            好友用户信息
	 */
	void doDeleteFriend(Friend friend);

	/**
	 * 取得好友
	 * 
	 * @param userId
	 *            所属君主id
	 * @param friendUserId
	 *            好友君主id
	 * @return
	 */
	Friend getFriendByFriendUserId(long userId, long friendUserId);

	/**
	 * 取得玩家所有好友
	 * 
	 * @param userId
	 * @return
	 */
	List<Friend> getFriendListByGroupId(long userId, FriendGroupType group);

	/**
	 * 按页取好友
	 * 
	 * @return
	 */
	List<FriendInfoMsg> getUserFriends(long userId, int pageNo, int pageSize,
			FriendGroupType group);

	/**
	 * 是否为好友
	 * 
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	boolean isFriend(long userId, long friendUserId);

	/**
	 * 是否在黑名单中[屏蔽聊天和邮件功能时调用]
	 * 
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	boolean isBlack(long userId, long friendUserId);

	/**
	 * 取得好友总页数
	 * 
	 * @param recordNum
	 * @return
	 */
	int getPageSum(int recordNum);

	/**
	 * 所有我发出的申请
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PagerResult getSendedAppByPage(long userId, int pageNo, int pageSize);

	/**
	 * 等待我审核的申请
	 * 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<FriendInfoMsg> getReceivedAppByPage(long userId);

	/**
	 * 删除过期的申请
	 */
	void cleanFriApp();

	/**
	 * 取社会关系列表
	 * 
	 * @return
	 */
	List<FriendInfoMsg> getSocialListByType(long userId, GroupType groupType);

	/**
	 * 加入好友推荐缓存
	 *
	 * @param userId
	 * @param oldUsrLv
	 * @param newUsrLv
	 */
	void putToFriendRecommendCache(long userId, int oldUsrLv, int newUsrLv);

	/**
	 * 取推荐好友列表
	 * 
	 * @param userId
	 * @return
	 */
	List<FriendInfoMsg> getFriendRecommendCache(long userId);

	/**
	 * 通过玩家名字查找用户名
	 * 
	 * @param userId
	 * @param userName
	 * @return
	 */
	List<FriendInfoMsg> searchFriendByName(long userId, String userName);

	/**
	 * 取得平台同玩好友
	 */
	void getPlatformFriend(UserSession userSession, Map<String, Object> params);

	int getFirendCount(long userId);

	/**
	 * 从最近联系人里面移出列表
	 * 
	 * @param userId
	 * @param friendUser
	 */
	void removeFromList(long userId, User friendUser);

	/**
	 * 获取最近联系人列表
	 * 
	 * @param userId
	 * @return
	 */
	List<FriendInfoMsg> getRecentTimeFriendList(long userId);

}
