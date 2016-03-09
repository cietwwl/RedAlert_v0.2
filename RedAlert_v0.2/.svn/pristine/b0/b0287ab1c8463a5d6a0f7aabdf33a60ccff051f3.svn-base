package com.youxigu.dynasty2.hero.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.hero.domain.Hero;

/**
 * 武将技能Service操作类
 * 
 * @author Dagangzi
 *
 */
public interface IHeroSkillService {
	/**
	 * 学习技能
	 * 
	 * @param userId
	 * @param heroId
	 * @param entId
	 *            技能实体id
	 */
	Hero doStudySkill(long userId, long heroId, int entId);

	/**
	 * 遗忘技能
	 * 
	 * @param userId
	 * @param heroId
	 * @param entId
	 */
	Hero doForgetSkill(long userId, long heroId, int entId,
			Map<String, Object> params);

	/**
	 * 转换技能
	 * 
	 * @param userId
	 * @param heroId
	 * @param entId
	 */
	Hero doReplaceSkill(long userId, long heroId, int entId);

	/**
	 * 这个方法是重生调用，没有加锁，没有更新hero
	 * 
	 * @param hero
	 */
	void forgetAllSkill(Hero hero, List<DroppedEntity> items);

	void getForgetAllSkillItems(Hero hero, Map<Integer, Integer> entIdNumMaps);
}
