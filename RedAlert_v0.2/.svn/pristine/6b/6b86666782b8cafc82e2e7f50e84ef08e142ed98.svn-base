package com.youxigu.dynasty2.entity.service;

import com.youxigu.dynasty2.entity.domain.EquipLevelUpRule;
import com.youxigu.dynasty2.hero.domain.HeroFate;

/**
 * 装备实体接口
 * 
 * @author Administrator
 * 
 */
public interface IEquipService {
	/**
	 * 取得装备强化规则，按位置、品质、强化等级
	 * 
	 * @param color
	 *            装备品质
	 * @param level
	 *            强化等级
	 * @return
	 */
	EquipLevelUpRule getEquipLevelUpRule(int color, int level);

	HeroFate getHeroFate(int fateId);

	/**
	 * 需要id转换来获取套装对象
	 * 
	 * @param fateId
	 * @return
	 */
	HeroFate getEquipFateByProcessId(String fateId);
}
