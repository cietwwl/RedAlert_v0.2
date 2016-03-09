package com.youxigu.dynasty2.user.dao;

import java.util.Map;

/**
 * 玩家属性数据操作接口
 * 
 * @author FengRui 2011.04.07
 *
 */
public interface IUserAttrDao {
	
	/**
	 * 新增玩家属性
	 * @param userId
	 * @param attrName
	 * @param attrValue
	 */
	void insertUserAttr(long userId, String attrName, String attrValue);
	
	/**
	 * 更新玩家属性
	 * @param userId
	 * @param attrName
	 * @param attrValue
	 */
	void updateUserAttr(long userId, String attrName, String attrValue);
	
	
	String getUserAttr(long userId, String attrName);
	
	/**
	 * 得到玩家所有属性
	 * @param userId
	 * @return
	 */
	Map<String, String> getAllUserAttr(long userId);	
	
}
