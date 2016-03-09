package com.youxigu.dynasty2.develop.dao.impl;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.develop.dao.ICastleArmyDao;
import com.youxigu.dynasty2.develop.domain.CastleArmy;

public class CastleArmyDao extends BaseDao implements ICastleArmyDao {
	@Override
	public void createCastleArmy(CastleArmy ca) {
		this.getSqlMapClientTemplate().insert("insertCastleArmy", ca);

	}

	@Override
	public CastleArmy getCastleArmy(long casId) {
		return (CastleArmy) this.getSqlMapClientTemplate().queryForObject(
				"getCastleArmy", casId);

	}

	@Override
	public void updateCastleArmy(CastleArmy ca) {
		this.getSqlMapClientTemplate().update("updateCastleArmy", ca);

	}
}
