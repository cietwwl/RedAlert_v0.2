package com.youxigu.dynasty2.hero.dao;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.HeroPub;
import com.youxigu.dynasty2.hero.domain.UserPubAttr;

public interface IHeroDrawDao {
	/**
	 * 取酒馆配置数据
	 * 
	 * @return
	 */
	List<HeroPub> getHeroPubs();

	/**
	 * 获取指定玩家的酒馆属性
	 * 
	 * @param userId
	 * @return
	 */
	public UserPubAttr getUserPubAttrById(long userId);

	/**
	 * 生成一条玩家酒馆属性记录
	 * 
	 * @param UserPubAttr
	 */
	public void insertUserPubAttr(UserPubAttr userPubAttr);

	/**
	 * 修改一条玩家酒馆属性记录
	 * 
	 * @param UserPubAttr
	 */
	public void updateUserPubAttr(UserPubAttr userPubAttr);
}
