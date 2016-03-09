package com.youxigu.dynasty2.friend.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.friend.domain.FriendHp;

public interface IHpFriendService {

	/**
	 * 领取单个好友体力
	 * 
	 * @param userId
	 * @param friendUserId
	 * @return
	 */
	public void doReceiveHp(long userId, long friendUserId);

	/**
	 * 一键领取并并反馈
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, List<Long>> doReceiveHpAll(long userId);

	/**
	 * 赠送体力
	 * 
	 * @param userId
	 * @param ids
	 * @return
	 */
	public List<Long> doSendFriendAll(long userId, List<Long> ids);

	public FriendHp doGetFriendHp(long userId);

	public FriendHp getFriendHp(long userId);
}
