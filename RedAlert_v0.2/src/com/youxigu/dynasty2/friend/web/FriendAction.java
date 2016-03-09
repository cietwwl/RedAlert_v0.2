package com.youxigu.dynasty2.friend.web;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.friend.domain.Friend;
import com.youxigu.dynasty2.friend.proto.FriendInfoMsg;
import com.youxigu.dynasty2.friend.proto.FriendMsg.FriendInfo;
import com.youxigu.dynasty2.friend.proto.FriendMsg.GroupType;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51001Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51003Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51005Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51007Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51009Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51011Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51013Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51015Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51017Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51050Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51052Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51054Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51001Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51003Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51005Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51007Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51009Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51011Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51013Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51015Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51017Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51050Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51052Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Response51054Define;
import com.youxigu.dynasty2.friend.service.IFriendService;
import com.youxigu.dynasty2.friend.service.IHpFriendService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 客户端取好友数据的通信类
 * 
 * @author Dagangzi
 * 
 */
public class FriendAction extends BaseAction {
	private IFriendService friendService;
	private IUserService userService;
	private IHpFriendService hpFriendService;
	private ICommonService commonService;

	public void setFriendService(IFriendService friendService) {
		this.friendService = friendService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setHpFriendService(IHpFriendService hpFriendService) {
		this.hpFriendService = hpFriendService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	private void check(int userLevel) {
		int sysLevel = commonService.getSysParaIntValue(
				AppConstants.FRIEND_OPEN_LEVEL, 1);
		if (userLevel < sysLevel) {
			throw new BaseException("等级不够");
		}
	}

	private void check(User user) {
		check(user.getUsrLv());
	}

	private void check(long userId) {
		check(userService.getUserById(userId));
	}

	/**
	 * 推荐好友列表-51001
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getFriendRecommends(Object obj, Response context) {
		Request51001Define req = (Request51001Define) obj;
		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();
		check(userId);
		List<FriendInfoMsg> result = null;
		if (req.hasUserName()) {
			String name = StringUtils.trim(req.getUserName());
			result = friendService.searchFriendByName(userId, name);
		} else {
			result = friendService.getFriendRecommendCache(userId);
		}

		Response51001Define.Builder res = Response51001Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (result != null && !result.isEmpty()) {
			for (FriendInfoMsg f : result) {
				res.addInfos((FriendInfo) f.build());
			}
		}
		return res.build();
	}

	/**
	 * 51003申请添加好友
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object appFriend(Object obj, Response context) {
		Request51003Define req = (Request51003Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());
		User friendUser = null;
		if (req.hasFriendUserId()) {
			if (us.getUserId() == req.getFriendUserId()) {
				throw new BaseException("不能申请自己为好友。");
			}
			friendUser = userService.getUserById(req.getFriendUserId());
		}
		if (friendUser == null) {
			throw new BaseException("您要添加的君主不存在！");
		}
		User user = userService.getUserById(us.getUserId());
		friendService.doAppFriend(user, friendUser);
		Response51003Define.Builder res = Response51003Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setFriendUserId(req.getFriendUserId());
		return res.build();
	}

	/**
	 * 51005获取申请列表(收到的)
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object getReceivedApp(Object obj, Response context) {
		Request51005Define req = (Request51005Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());
		List<FriendInfoMsg> result = friendService.getReceivedAppByPage(us
				.getUserId());
		Response51005Define.Builder res = Response51005Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (result != null && !result.isEmpty()) {
			for (FriendInfoMsg info : result) {
				res.addInfos((FriendInfo) info.build());
			}
		}
		return res.build();
	}

	/**
	 * 51007同意或拒绝好友申请
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object accFriend(Object obj, Response context) {
		Request51007Define req = (Request51007Define) obj;
		UserSession us = super.getUserSession(context);
		User user = userService.getUserById(us.getUserId());
		check(user);
		List<java.lang.Long> list = req.getFriendUserIdList();
		if (list == null || list.isEmpty()) {
			throw new BaseException("操作的好友id不能为null");
		}
		Response51007Define.Builder res = Response51007Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setAgree(req.getAgree());

		if (req.getAgree()) {
			List<FriendInfoMsg> rlist = friendService
					.doAccAllFriend(user, list);
			for (FriendInfoMsg l : rlist) {
				res.addInfos((FriendInfo) l.build());
			}
		} else {
			List<java.lang.Long> rlist = friendService.doRefuseAllFriends(user,
					list);
			for (Long l : rlist) {
				FriendInfo.Builder info = FriendInfo.newBuilder();
				info.setUserId(l);
				res.addInfos(info.build());
			}
		}

		return res.build();
	}

	/**
	 * 51009删除好友
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object delFriend(Object obj, Response context) {
		Request51009Define req = (Request51009Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());
		User friendUser = null;
		if (req.hasFriendUserId()) {
			friendUser = userService.getUserById(req.getFriendUserId());
		}
		if (friendUser == null) {
			throw new BaseException("操作的用户不存在");
		}
		Friend friend = friendService.getFriendByFriendUserId(us.getUserId(),
				friendUser.getUserId());
		if (friend == null) {
			throw new BaseException("操作的好友不存在");
		}
		friendService.doDeleteFriend(friend);
		Response51009Define.Builder res = Response51009Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setFriendUserId(req.getFriendUserId());
		return res.build();
	}

	/**
	 * 51011获取列表
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getFriendListByGroupId(Object obj, Response context) {
		Request51011Define req = (Request51011Define) obj;
		long userId = super.getUserSession(context).getUserId();
		check(userId);

		if (!req.hasGroup() || GroupType.NULL.equals(req.getGroup())) {
			throw new BaseException("好友分组错误");
		}
		List<FriendInfoMsg> result = friendService.getSocialListByType(userId,
				req.getGroup());
		Response51011Define.Builder res = Response51011Define.newBuilder();
		res.setGroup(req.getGroup());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		if (result != null && !result.isEmpty()) {
			for (FriendInfoMsg msg : result) {
				res.addInfos((FriendInfo) msg.build());
			}
		}
		return res.build();
	}

	/**
	 * 51013移出列表(最近联系人)
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object removeFromList(Object obj, Response context) {
		Request51013Define req = (Request51013Define) obj;
		long userId = super.getUserSession(context).getUserId();
		check(userId);
		if (!req.hasFriendUserId()) {
			throw new BaseException("数据错误");
		}
		User friendUser = userService.getUserById(req.getFriendUserId());
		if (friendUser == null) {
			throw new BaseException("玩家不存在");
		}
		friendService.removeFromList(userId, friendUser);
		Response51013Define.Builder res = Response51013Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setFriendUserId(req.getFriendUserId());
		return res.build();
	}

	/**
	 * 51015屏蔽此人(加入黑名单)
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object addBlack(Object obj, Response context) {
		Request51015Define req = (Request51015Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());
		User buser = null;
		if (req.hasFriendUserId()) {
			buser = userService.getUserById(req.getFriendUserId());
		}
		if (buser == null) {
			throw new BaseException("您要添加的君主不存在！");
		}
		User user = userService.getUserById(us.getUserId());
		friendService.doAddBlack(user, buser);
		Response51015Define.Builder res = Response51015Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setFriendUserId(req.getFriendUserId());
		return res.build();
	}

	/**
	 * 51017移出黑名单
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object delBlack(Object obj, Response context) {
		Request51017Define req = (Request51017Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());
		User buser = userService.getUserById(req.getFriendUserId());
		if (buser == null) {
			throw new BaseException("玩家数据不存在");
		}
		friendService.doDelBlack(us.getUserId(), buser);
		Response51017Define.Builder res = Response51017Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setFriendUserId(req.getFriendUserId());
		return res.build();
	}

	/**
	 * 51050赠送好友体力
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doSendFriendAll(Object obj, Response context) {

		Request51050Define req = (Request51050Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());

		List<Long> succ = hpFriendService.doSendFriendAll(us.getUserId(),
				req.getFriendUserIdList());
		Response51050Define.Builder res = Response51050Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		for (long l : succ) {
			res.addFriendUserId(l);
		}
		return res.build();
	}

	/**
	 * 51052领取体力(单个好友)
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doReceiveHp(Object obj, Response context) {
		Request51052Define req = (Request51052Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());
		hpFriendService.doReceiveHp(us.getUserId(), req.getFriendUserId());
		Response51052Define.Builder res = Response51052Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setFriendUserId(req.getFriendUserId());
		return res.build();
	}

	/**
	 * 51054一键领取体力并且反赠
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doReceiveHpAll(Object obj, Response context) {
		Request51054Define req = (Request51054Define) obj;
		UserSession us = super.getUserSession(context);
		check(us.getUserId());

		Map<String, List<Long>> map = hpFriendService.doReceiveHpAll(us
				.getUserId());

		Response51054Define.Builder res = Response51054Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		List<Long> ls = map.get("giveMefriends");
		for (long l : ls) {
			res.addGiveMefriends(l);
		}

		ls = map.get("giftfriends");
		for (long l : ls) {
			res.addGiftfriends(l);
		}

		return res.build();
	}

	/**
	 * 好友分组-51007
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object addGroup(Map<String, Object> params, Response context) {
		// 输入
		int groupId = (Integer) params.get("groupId");// 好友的userId
		long friUserId = MathUtils.getLong(params.get("friUserId"));// 好友的userId

		User friendUser = userService.getUserById(friUserId);

		if (friendUser == null) {
			throw new BaseException("该君主不存在！");
		}

		// 输出
		UserSession us = super.getUserSession(context);
		User user = userService.getUserById(us.getUserId());
		if (user.getUserId() == friendUser.getUserId()) {
			throw new BaseException("不能加自己为好友。");
		}

		Friend friend = friendService.getFriendByFriendUserId(us.getUserId(),
				friendUser.getUserId());

		friendService.doAddGroup(friend, groupId);

		return params;
	}

	/**
	 * 取消好友申请-51012
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object cancelApp(Map<String, Object> params, Response context) {
		long appUserId = MathUtils.getLong(params.remove("appUserId"));// 申请人的userId

		User friendUser = userService.getUserById(appUserId);
		if (friendUser == null) {
			throw new BaseException("该君主不存在！");
		}

		UserSession us = super.getUserSession(context);
		User user = userService.getUserById(us.getUserId());

		friendService.doCancelFriend(user, friendUser);
		return params;
	}

	/**
	 * 修改备注-51018
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object modifyNote(Map<String, Object> params, Response context) {
		String userName = (String) params.get("userName");
		String note = (String) params.get("note");

		User friendUser = userService.getUserByName(userName);
		if (friendUser != null) {
			UserSession us = super.getUserSession(context);

			Friend friend = friendService.getFriendByFriendUserId(
					us.getUserId(), friendUser.getUserId());
			if (friend != null) {
				friendService.doModifyNote(friend, note);
			}
		}
		return params;
	}

}