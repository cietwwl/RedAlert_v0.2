package com.youxigu.dynasty2.user.dao;

import java.util.List;

import com.youxigu.dynasty2.user.domain.Achieve;
import com.youxigu.dynasty2.user.domain.AchieveLimit;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.UserAchieve;

/**
 * 君主成就DAO接口定义
 * @author Dagangzi
 *
 */
public interface IUserAchieveDao {
	/**
	 * 列出所有的成就定义
	 * @return
	 */
	List<Achieve> listAllAchieve();
	
	/**
	 * 取出所有的成就类型
	 * @return
	 */
	List<AchieveType> listAchieveType();
	
	/**
	 * 取出所有的成就约束
	 * @return
	 */
	List<AchieveLimit> listAchieveLimit();

	/**
	 * 按成就类型取君主成就
	 * 
	 * @param userId
	 * @param entId
	 * @return
	 */
	UserAchieve getUserAchieveByEntId(long userId, int entId);
	
	/**
	 * 城救助id取君主成就
	 * @param userId
	 * @return
	 */
	List<UserAchieve> getUserAchieveByUserId(long userId);
	
	/**
	 * 创建君主成就
	 * @param userAchieve
	 */
	void createUserAchieve(UserAchieve userAchieve);

	/**
	 * 修改君主成就
	 * @param userAchieve
	 */
	void updateUserAchieve(UserAchieve userAchieve);
}
