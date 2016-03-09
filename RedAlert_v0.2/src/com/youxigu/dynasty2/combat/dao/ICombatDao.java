package com.youxigu.dynasty2.combat.dao;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatDB;
import com.youxigu.dynasty2.combat.domain.CombatFactor;

/**
 * 战斗dao接口
 * @author Dagangzi
 *
 */
public interface ICombatDao {
	/**
	 * 战斗计算系数
	 * @return
	 */
	List<CombatFactor> getCombatFactor();

	/**
	 * 保存战斗
	 * @param combatDB
	 */
	void saveCombatDB(CombatDB combatDB);

	/**
	 * 取得战斗
	 * @param combatId
	 * @return
	 */
	CombatDB getCombatDB(String combatId);

	/**
	 * 批量删除多少天之前的战斗数据
	 * @param day
	 */
	void deleteCombatDBByDate(int day);

	/**
	 * 删除一场战斗
	 * @param combatId
	 */
	void deleteCombatDBById(String combatId);
}
