package com.youxigu.dynasty2.user.service;

import java.sql.Timestamp;
import java.util.Map;

/**
 * User扩展属性操作接口
 * 
 * @author FengRui 2011.04.07
 */
public interface IUserAttrService {
	
	/**
	 * 所回玩家所有属性
	 * @return key = attrName, value = attrValue
	 */
	Map<String, String> getAllUserAttr(long userId);
	
	/**
	 * 删除所有用户属性缓存
	 * @param userId
	 */
	void removeCachedUserAttrs(long userId);
	/**
	 * 得到玩家指定属性
	 * @param userId
	 * @param attrName
	 * @return
	 */
	String getStringUserAttr(long userId, String attrName);
	
	/**
	 * 得到玩家指定属性
	 * @param userId
	 * @param attrName
	 * @return
	 */
	int getIntUserAttr(long userId, String attrName);
	
	/**
	 * 得到玩家指定属性
	 * @param userId
	 * @param attrName
	 * @return
	 */
	Timestamp getTimeUserAttr(long userId, String attrName);
	
	/**
	 * 保存玩家属性
	 * @param userId
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	void saveUserAttr(long userId, String attrName, Object attrValue);
	
	/**
	 * 初始化君主属性
	 * @param userId
	 */
	void doInitUserAttr(long userId);
}
