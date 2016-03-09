package com.youxigu.dynasty2.hero.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.hero.dao.ITroopDao;
import com.youxigu.dynasty2.hero.domain.Troop;

@SuppressWarnings("unchecked")
public class TroopDao extends BaseDao implements ITroopDao {
	private static final String ID_TYPE = "TROOP";

	@Override
	public void createTroop(Troop troop) {
		troop.setTroopId(IDUtil.getNextId(ID_TYPE));
		this.getSqlMapClientTemplate().insert("insertTroop", troop);
	}

	@Override
	public void deleteTroop(Troop troop) {
		this.getSqlMapClientTemplate().delete("deleteTroop", troop);

	}

	@Override
	public Troop getTroopById(long troopId) {
		return (Troop) this.getSqlMapClientTemplate().queryForObject(
				"getTroopById", troopId);
	}

	@Override
	public Troop getTroopById(long userId, long troopId) {
		Troop troop = getTroopById(troopId);
		if (troop == null) {
			return null;
		}
		if (troop.getUserId() != userId) {
			return null;
		}
		return troop;
	}

	@Override
	public List<Troop> getTroopsByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getTroopsByUserId",
				userId);
	}

	@Override
	public void updateTroop(Troop troop) {
		this.getSqlMapClientTemplate().update("updateTroop", troop);
	}
}
