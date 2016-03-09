package com.youxigu.dynasty2.develop.service;

import java.util.Map;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.user.domain.User;

/*
 * 刷新城池service接口定义
 */
public interface IFlushCastleService {
	/**
	 * 刷新城池
	 * @param user
	 * @param castle
	 * @param login
	 *            是否是登陆导致的调用
	 * @param background
	 *            是否是后台其他方法调用
	 * @return
	 */
	Map<String, Object> updateCastleDevelopData(User user, boolean login);

	Map<String, Object> updateCastleDevelopData(User user, Castle castle, boolean login, boolean background);
	
	/**
	 * 推送刷新城池效果
	 * @param userId
	 * @param fieldName
	 * @param fieldValue
	 */
	void sendFlushCasEffEvent(long userId, String fieldName, int fieldValue);
}
