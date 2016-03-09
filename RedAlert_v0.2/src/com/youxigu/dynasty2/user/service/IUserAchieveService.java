package com.youxigu.dynasty2.user.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.youxigu.dynasty2.user.domain.Achieve;
/**
 * 君主成就service接口定义
 * @author Dagangzi
 *
 */
public interface IUserAchieveService {
	/**
	 * 按指定成就id取得成就定义
	 * @param achieveId
	 * @return
	 */
	Achieve getAchieveByAchieveId(int achieveId);
	
	/**
	 * 取得指定类型所有的成就定义
	 * @param type
	 * @return
	 */
	List<Achieve> getAchieveListByType(int type);
	
	/**
	 * 按实体id取成就
	 * 
	 * @param entId
	 * @return
	 */
	List<Achieve> getAchieveListByEntId(int entId);

	/**
	 * 取得所有的成就类型
	 * 
	 * @return
	 */
	Set<Integer> listAchieveTypes();

	/**
	 * 记录达成的成就
	 * 
	 * @param userId
	 * @param type
	 * @param entId
	 * @param entNum
	 */
	void doNotifyAchieveModule(long userId, int type, int entId, int entNum);
	
	/**
	 * 取得君主所有的成就
	 * 
	 * @param userId
	 * @param type -1总览 1-n其他大类
	 * @return
	 */
	Map<String, Object> getUserAchieveByType(long userId, int type);
	
}
