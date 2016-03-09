package com.youxigu.dynasty2.combat.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.combat.dao.ICombatDao;
import com.youxigu.dynasty2.combat.domain.CombatDB;
import com.youxigu.dynasty2.combat.domain.CombatFactor;

public class CombatDao extends BaseDao implements ICombatDao {
	@Override
	public List<CombatFactor> getCombatFactor() {
		return this.getSqlMapClientTemplate().queryForList("listCombatFactors");
	}

	@Override
	public void saveCombatDB(CombatDB combatDB) {
		this.getSqlMapClientTemplate().insert("insertCombatDB", combatDB);
	}

	@Override
	public CombatDB getCombatDB(String combatId) {
		return (CombatDB) this.getSqlMapClientTemplate().queryForObject("getCombatDB", combatId);
	}

	@Override
	public void deleteCombatDBByDate(int day) {
		this.getSqlMapClientTemplate().delete("deleteCombatDB", day);
	}

	@Override
	public void deleteCombatDBById(String combatId) {
		this.getSqlMapClientTemplate().delete("deleteCombatDBById", combatId);
	}

}
