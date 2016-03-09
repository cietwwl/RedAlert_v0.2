package com.youxigu.dynasty2.develop.dao;

import com.youxigu.dynasty2.develop.domain.CastleArmy;

public interface ICastleArmyDao {
	/**
	 * 得到玩家可用新兵数量
	 * 
	 * @param casId
	 */
	CastleArmy getCastleArmy(long casId);

	/**
	 * 创建兵种
	 * 
	 * @param ca
	 */
	void createCastleArmy(CastleArmy ca);

	/**
	 * 更新兵种数量
	 * 
	 * @param ca
	 */
	void updateCastleArmy(CastleArmy ca);
}
