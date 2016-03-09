package com.youxigu.dynasty2.friend.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.friend.dao.IFriendDao;
import com.youxigu.dynasty2.friend.domain.Friend;
import com.youxigu.dynasty2.friend.domain.FriendApp;
import com.youxigu.dynasty2.friend.domain.FriendHp;
import com.youxigu.dynasty2.friend.domain.FriendRecommend;
import com.youxigu.dynasty2.friend.enums.FriendGroupType;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 好友Dao接口实现类
 * 
 * @author Dagangzi
 * 
 */
@SuppressWarnings("unchecked")
public class FriendDao extends BaseDao implements IFriendDao {
	@Override
	public Friend getFriendByFriendUserId(long userId, long friendUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("friendUserId", friendUserId);
		return (Friend) this.getSqlMapClientTemplate().queryForObject(
				"getFriendByFriendUserId", params);
	}

	@Override
	public List<Friend> getAllFriendByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllFriendByUserId", userId);
	}

	@Override
	public List<Friend> getFriendListByGroupId(long userId,
			FriendGroupType group) {
		if (group == null) {
			throw new BaseException("好友分组不能为null");
		}
		List<Friend> groupList = new ArrayList<Friend>();
		// 所有该玩家的好友
		List<Friend> friList = this.getAllFriendByUserId(userId);
		Iterator<Friend> itl = friList.iterator();
		while (itl.hasNext()) {
			Friend fri = itl.next();

			if (group.equals(fri.getGroup())) {
				groupList.add(fri);
			}
		}
		return groupList;
	}

	@Override
	public int getFriendCountByGroupId(long userId, FriendGroupType group) {
		List<Friend> friendList = this.getFriendListByGroupId(userId, group);
		return (friendList == null ? 0 : friendList.size());
	}

	@Override
	public void insertFriend(Friend friend) {
		this.getSqlMapClientTemplate().insert("insertFriend", friend);
	}

	@Override
	public void updateFriend(Friend friend) {
		this.getSqlMapClientTemplate().update("updateFriend", friend);
	}

	@Override
	public void deleteFriend(Friend friend) {
		this.getSqlMapClientTemplate().delete("deleteFriend", friend);
	}

	@Override
	public FriendApp getFriendAppByUserIdAndFriendId(long userId,
			long friendUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("friendUserId", friendUserId);
		return (FriendApp) this.getSqlMapClientTemplate().queryForObject(
				"getFriendAppByUserIdAndFriendId", params);
	}

	@Override
	public List<FriendApp> getAllFriendAppByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllFriendAppByUserId", userId);
	}

	@Override
	public List<FriendApp> getAllFriendAppByFriendUserId(long friendUserId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getAllFriendAppByFriendUserId", friendUserId);
	}

	@Override
	public void deleteFriendApp(FriendApp friendApp) {
		this.getSqlMapClientTemplate().delete("deleteFriendApp", friendApp);
	}

	@Override
	public void insertFriendApp(FriendApp friendApp) {
		this.getSqlMapClientTemplate().insert("insertFriendApp", friendApp);
	}

	@Override
	public void cleanFriApp(int appValidDays) {
		this.getSqlMapClientTemplate().delete("cleanFriApp", appValidDays);
	}

	@Override
	public List<FriendRecommend> getFriendRecommends() {
		return this.getSqlMapClientTemplate().queryForList(
				"getFriendRecommends");
	}

	@Override
	public List<Long> getFriendRecommendUsers(Map<String, Object> params) {
		return this.getSqlMapClientTemplate().queryForList(
				"getFriendRecommendUsers", params);
	}

	@Override
	public FriendHp getFriendHp(long userId) {
		return (FriendHp) super.getSqlMapClientTemplate().queryForObject(
				"getFriendHp", userId);
	}

	@Override
	public void insertFriendHp(FriendHp friendHp) {
		super.getSqlMapClientTemplate().insert("insertFriendHp", friendHp);

	}

	@Override
	public void updateFriendHp(FriendHp friendHp) {
		super.getSqlMapClientTemplate().update("updateFriendHp", friendHp);

	}

}
