package com.youxigu.dynasty2.hero.dao;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.hero.domain.CommanderColorProperty;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.domain.StrongLimit;

public interface IHeroDao {

	/**
	 * 按指定的id取武将
	 * 
	 * @param heroId
	 * @return
	 */
	Hero getHeroByHeroId(long userId, long heroId);

	Hero getHeroBySysHeroId(long userId, int sysHeroId);

	/**
	 * 生成一个武将
	 * 
	 * @param hero
	 */
	void insertHero(Hero hero);

	/**
	 * 删除一个武将
	 * 
	 * @param hero
	 */
	void deleteHero(Hero hero);

	/**
	 * 修改一个武将
	 * 
	 * @param hero
	 */
	void updateHero(Hero hero);

	/**
	 * 按指定的用户id取武将列表包括拥有的武将 还有拥有碎片或者图纸
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	List<Hero> getHeroListByUserId(long userId);

	/**
	 * 取的君主的所有武将图纸
	 * 
	 * @param userId
	 * @return
	 */
	List<DroppedEntity> getHeroCardsByUserId(long userId);

	/**
	 * 取得君主的所有武将碎片
	 * 
	 * @param userId
	 * @return
	 */
	List<DroppedEntity> getHeroSoulsByUserId(long userId);

	// //////////////////////初始化
	/**
	 * 取武将等级经验值设定
	 */
	List<Map<Integer, Integer>> getHeroExp();

	/**
	 * 取武将强化条件约束
	 * 
	 * @return
	 */
	List<StrongLimit> getStrongLimitList();

	/**
	 * 清除hero状态，防止因为未知的状况导致武将状态被锁住
	 */
	void cleanHeroStatus(int status);

	/**
	 * 指挥官品质属性配数
	 * 
	 * @return
	 */
	List<CommanderColorProperty> listCommanderColorPropertys();

}