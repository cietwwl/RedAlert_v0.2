package com.youxigu.dynasty2.user.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.manu.util.Util;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.user.dao.IUserAttrDao;
import com.youxigu.dynasty2.user.service.IUserAttrService;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 玩家属性只保存在本机，<b>如果需要不同服务器交互修改不能使用</b>
 * 
 * 这里都是玩家的私有数据，不会有其他用户访问，假设不会出现并发情况
 * 
 * <p>
 * 数据库对应表为UserAttributes表, 联合主键：userId+attrName
 * 
 * 属性值不允许为null
 * 
 * @author FengRui 2011.04.08
 * 
 */
public class UserAttrService implements IUserAttrService {

	private IUserAttrDao userAttrDao;

	// 本机缓存，当前设定为保存2000个玩家的数据，FastCache<userId, Map<attrName, attrValue>>
	private ConcurrentLinkedHashMap<Long, Map<String, String>> userAttrCache = null;

	public void setUserAttrDao(IUserAttrDao userAttrDao) {
		this.userAttrDao = userAttrDao;
	}

	public void init() {
		ConcurrentLinkedHashMap.Builder<Long, Map<String, String>> builder = new ConcurrentLinkedHashMap.Builder<Long, Map<String, String>>();
		builder.initialCapacity(2000);
		builder.maximumWeightedCapacity(5000);
		builder.concurrencyLevel(32);
		userAttrCache = builder.build();
	}

	@Override
	public Map<String, String> getAllUserAttr(long userId) {
		Map<String, String> userAttrMap = this.userAttrCache.get(userId);
		if (userAttrMap == null) {
			userAttrMap = this.userAttrDao.getAllUserAttr(userId);
			if (userAttrMap == null) {
				userAttrMap = new HashMap<String, String>();
			}

			if (!userAttrMap.containsKey(AppConstants.USERATTR_LAST_SHARE_TIME)) {
				userAttrMap.put(AppConstants.USERATTR_LAST_SHARE_TIME, null);
			}
			if (!userAttrMap.containsKey(AppConstants.USERATTR_HEROINHERIT_NUM)) {
				userAttrMap.put(AppConstants.USERATTR_HEROINHERIT_NUM, null);
			}
			if (!userAttrMap
					.containsKey(AppConstants.USERATTR_USED_FIGHT_NPC_NUM)) {
				userAttrMap.put(AppConstants.USERATTR_USED_FIGHT_NPC_NUM, null);
			}
			this.userAttrCache.put(userId, userAttrMap);

		}
		return userAttrMap;
	}

	@Override
	public String getStringUserAttr(long userId, String attrName) {
		Map<String, String> userAttrMap = this.getAllUserAttr(userId);
		if (!userAttrMap.containsKey(attrName)) {
			String value = this.userAttrDao.getUserAttr(userId, attrName);
			userAttrMap.put(attrName, value);
			return value;

		} else {
			return userAttrMap.get(attrName);
		}
	}

	@Override
	public int getIntUserAttr(long userId, String attrName) {
		String value = getStringUserAttr(userId, attrName);
		if (value == null) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

	@Override
	public Timestamp getTimeUserAttr(long userId, String attrName) {
		String value = getStringUserAttr(userId, attrName);
		if (value == null) {
			return null;
		} else {
			return Timestamp.valueOf(value);
		}
	}

	@Override
	public void saveUserAttr(long userId, String attrName, Object attrValue) {
		if (userId <= 0 || Util.isEmpty(attrName) || attrValue == null) {
			return;
		}
		String value = attrValue.toString();
		String oldValue = null;
		Map<String, String> userAttrMap = null;
		try {
			userAttrMap = this.getAllUserAttr(userId);
			oldValue = userAttrMap.get(attrName);

			if (oldValue == null) {// 新增
				userAttrDao.insertUserAttr(userId, attrName, value);
			} else {// 更新
				userAttrDao.updateUserAttr(userId, attrName, value);
			}
			userAttrMap.put(attrName, value);
		} catch (Exception e) {

			if (userAttrMap != null) {
				if (oldValue == null) {
					userAttrMap.remove(attrName);
				} else {
					userAttrMap.put(attrName, oldValue);
				}
			}
			throw new BaseException(e.toString());

		}
		// userAttrCache.remove(userId);
	}

	@Override
	public void doInitUserAttr(long userId) {
		// 酒馆武将容纳个数
		this.saveUserAttr(userId, AppConstants.USERATTR_HOTEL_HERO_NUM,
				AppConstants.INIT_HOTEL_HERO_NUM);
	}

	@Override
	public void removeCachedUserAttrs(long userId) {
		userAttrCache.remove(userId);

	}
}
