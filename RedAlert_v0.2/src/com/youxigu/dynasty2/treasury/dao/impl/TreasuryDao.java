package com.youxigu.dynasty2.treasury.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.hero.domain.HeroEquipDebris;
import com.youxigu.dynasty2.treasury.dao.ITreasuryDao;
import com.youxigu.dynasty2.treasury.domain.Treasury;

@SuppressWarnings("unchecked")
public class TreasuryDao extends BaseDao implements ITreasuryDao {
	private static final String ID_TYPE = "TREASURY";

	@Override
	public Treasury getTreasuryById(long userId, long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("id", id);
		return (Treasury) this.getSqlMapClientTemplate().queryForObject(
				"getTreasuryById", params);
	}

	@Override
	public List<Treasury> getTreasurysByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getTreasurysByUserId", userId);
	}

	@Override
	public List<Treasury> getTreasurysByEntId(long userId, int entId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("entId", entId);
		return this.getSqlMapClientTemplate().queryForList(
				"getTreasurysByEntId", params);
	}

	@Override
	public void createTreasury(Treasury treasury) {
		treasury.setId(IDUtil.getNextId(ID_TYPE));
		this.getSqlMapClientTemplate().insert("insertTreasury", treasury);

	}

	@Override
	public void deleteTreasury(Treasury treasury) {
		this.getSqlMapClientTemplate().delete("deleteTreasury", treasury);
	}

	@Override
	public void updateTreasury(Treasury treasury) {
		this.getSqlMapClientTemplate().update("updateTreasury", treasury);

	}

	@Override
	public void createHeroEquipDebris(HeroEquipDebris card) {
		this.getSqlMapClientTemplate().insert("insertHeroEquipDebris", card);

	}

	@Override
	public HeroEquipDebris getHeroEquipDebris(long userId) {
		return (HeroEquipDebris) this.getSqlMapClientTemplate().queryForObject(
				"geHeroEquipDebris", userId);
	}

	@Override
	public void updateHeroEquipDebris(HeroEquipDebris card) {
		this.getSqlMapClientTemplate().update("updateHeroEquipDebris", card);

	}
}
