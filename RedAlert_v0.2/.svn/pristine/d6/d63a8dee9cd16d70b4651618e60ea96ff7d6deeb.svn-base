package com.youxigu.dynasty2.friend.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.youxigu.dynasty2.chat.OfflineMsgCacheService;
import com.youxigu.dynasty2.chat.OfflineMsgCacheService.OffLineMsg;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.friend.dao.IFriendDao;
import com.youxigu.dynasty2.friend.domain.FriendRecommend;
import com.youxigu.dynasty2.friend.service.IFriendRecommendCacheService;
import com.youxigu.dynasty2.user.domain.UserChat;
import com.youxigu.dynasty2.user.domain.UserChat.RecentTimeFriend;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.OnlineUserSessionManager;
import com.youxigu.wolf.net.UserSession;

/**
 * 驻留在mainserver上的可推荐的好友缓存
 * 
 * @author Administrator
 * 
 */
public class FriendRecommendCacheService implements
		IFriendRecommendCacheService {

	private IFriendDao friendDao;
	/*** 按策划配置的玩家分段缓存的玩家userId ,使用ConcurrentLinkedHashMap 作LRU */
	private ConcurrentLinkedHashMap<Long, Object>[] cache = null;
	private List<FriendRecommend> friendRecommends = null;
	/*** 每个分段缓存的userId数量 */
	private int cachedNum = 100;
	/*** 最小的进入缓存的玩家等级 */
	private int minUsrLv = Integer.MAX_VALUE;
	private int maxUsrLv = Integer.MIN_VALUE;
	private ICommonService commonService;

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setFriendDao(IFriendDao friendDao) {
		this.friendDao = friendDao;
	}

	public void setCachedNum(int cachedNum) {
		this.cachedNum = cachedNum;
	}

	@SuppressWarnings("unchecked")
	public void init() {
		friendRecommends = friendDao.getFriendRecommends();

		int size = friendRecommends == null ? 0 : friendRecommends.size();
		// 初始化缓存
		if (size > 0) {
			ConcurrentLinkedHashMap.Builder<Long, Object> builder = new ConcurrentLinkedHashMap.Builder<Long, Object>();

			cache = new ConcurrentLinkedHashMap[size];
			for (int i = 0; i < size; i++) {
				// builder.concurrencyLevel(32);
				builder.initialCapacity(cachedNum);
				builder.maximumWeightedCapacity(cachedNum);
				builder.concurrencyLevel(16);
				cache[i] = builder.build();
			}

			Map<String, Object> params = new HashMap<String, Object>(3);
			params.put("num", cachedNum);
			int i = 0;
			for (FriendRecommend fr : friendRecommends) {
				int minLv = fr.getMinLv();
				int maxLv = fr.getMaxLv();
				// int usrLv = fr.getUsrLv();
				if (minLv < minUsrLv) {
					minUsrLv = minLv;
				}
				if (maxLv > maxUsrLv) {
					maxUsrLv = maxLv;
				}
				params.put("minLv", minLv);
				params.put("maxLv", maxLv);
				List<Long> userIds = friendDao.getFriendRecommendUsers(params);
				if (userIds != null) {
					for (Long id : userIds) {
						cache[i].put(id, id);
					}
				}
				i++;

			}
		}
	}

	@Override
	public void addCache(long userId, int oldUsrLv, int newUsrLv) {
		if (friendRecommends == null) {
			return;
		}
		// 删除原等级所在的缓存
		// 增加新等级所在的缓存
		int i = 0;
		for (FriendRecommend fr : friendRecommends) {
			int minLv = fr.getMinLv();
			int maxLv = fr.getMaxLv();
			if (oldUsrLv >= minLv && oldUsrLv <= maxLv) {
				cache[i].remove(userId);
			}
			if (newUsrLv >= minLv && newUsrLv <= maxLv) {
				cache[i].put(userId, userId);
			}
			i++;
		}

	}

	@Override
	public Long[] getCache(int usrLv) {
		if (friendRecommends == null) {
			return null;
		}
		int i = -1;
		for (FriendRecommend fr : friendRecommends) {
			int lv = fr.getUsrLv();
			if (usrLv < lv) {
				break;
			}
			i++;
		}
		if (i < 0) {
			i = 0;
		} else if (i == cache.length) {
			i--;
		}

		Collection<Long> c = cache[i].keySet();
		Long[] ids = c.toArray(new Long[c.size()]);
		return ids;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RecentTimeFriend> getRecentTimeFriendList(long userId) {
		// 获取玩家最近联系人的时候 记得判断下是否有离线消息。有把离线消息里面的联系人加进来
		UserSession sess = OnlineUserSessionManager
				.getUserSessionByUserId(userId);
		if (sess == null) {
			throw new BaseException("获取最近联系人异常,玩家session不存在");
		}

		List<RecentTimeFriend> list = null;
		Object obj = sess.getAttribute(UserSession.KEY_RECENT_TIME_FRIEND);
		if (obj == null) {
			list = new ArrayList<UserChat.RecentTimeFriend>();
			sess.addAttribute(UserSession.KEY_RECENT_TIME_FRIEND, list);
		} else {
			list = (List<RecentTimeFriend>) obj;
		}

		Map<Long, List<OffLineMsg>> msgs = OfflineMsgCacheService
				.getOffLineMsg().get(userId);
		if (msgs != null && !msgs.isEmpty()) {
			Map<Long, Integer> result = new LinkedHashMap<Long, Integer>();
			int i = 0;
			for (RecentTimeFriend r : list) {
				result.put(r.getUserId(), i++);
			}
			List<OffLineMsg> ss = new ArrayList<OffLineMsg>();
			// 排序玩家离线消息先后顺序
			for (List<OffLineMsg> s : msgs.values()) {
				ss.add(s.get(0));// 每个离线消息的第一条消息
			}
			Collections.sort(ss);// 最近的在最前面
			for (int z = ss.size() - 1; z >= 0; z--) {
				OffLineMsg m = ss.get(z);
				Integer idx = result.get(m);
				if (idx == null) {
					// 原来最近联系人列表里面有。加入进去
					list.add(0,
							new RecentTimeFriend(m.getUserId(), m.getTimes()));
				} else {
					// 有了,更新原来顺序
					list.remove(idx);
					list.add(0,
							new RecentTimeFriend(m.getUserId(), m.getTimes()));
				}
			}
		}
		int size = list.size();
		int num = commonService.getSysParaIntValue(
				AppConstants.SYS_CASTLE_RECENT_FRIEND_MAX, 50);
		if (size > num) {
			for (int i = num; i < size; i++) {
				RecentTimeFriend r = list.remove(i);
				OfflineMsgCacheService.removeOffLineMsg(userId, r.getUserId());
			}
		}
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean removeRecentTimeFriend(long userId, long friendId) {
		UserSession sess = OnlineUserSessionManager
				.getUserSessionByUserId(userId);
		if (sess == null) {
			return false;
		}

		Object obj = sess.getAttribute(UserSession.KEY_RECENT_TIME_FRIEND);
		if (obj == null) {
			return false;
		}

		List<RecentTimeFriend> rt = (List<RecentTimeFriend>) obj;
		int size = rt.size();
		for (int i = 0; i < size; i++) {
			if (rt.get(i).getUserId() == friendId) {
				rt.remove(i);
				return true;
			}
		}
		return false;
	}

}
