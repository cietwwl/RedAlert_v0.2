package com.youxigu.dynasty2.hero.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.hero.dao.ITroopGridDao;
import com.youxigu.dynasty2.hero.domain.TroopGrid;

@SuppressWarnings("unchecked")
public class TroopGridDao extends BaseDao implements ITroopGridDao {
	private static final String ID_TYPE = "TROOPGRID";

	@Override
	public void createTroopGrid(TroopGrid troop) {
		troop.setTroopGridId(IDUtil.getNextId(ID_TYPE));
		this.getSqlMapClientTemplate().insert("insertTroopGrid", troop);
	}

	@Override
	public void updateTroopGrid(TroopGrid troop) {
		this.getSqlMapClientTemplate().update("updateTroopGrid", troop);

	}

	@Override
	public List<TroopGrid> getTroopGridsByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getTroopGridsByUserId", userId);
	}

	@Override
	public TroopGrid getTroopGridById(long userId, long troopGridId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("troopGridId", troopGridId);
		return (TroopGrid) this.getSqlMapClientTemplate().queryForObject(
				"getTroopGridById", params);
	}

}
