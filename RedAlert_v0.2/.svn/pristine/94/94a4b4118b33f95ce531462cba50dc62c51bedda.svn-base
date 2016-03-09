package com.youxigu.dynasty2.tower.dao.impl;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.tower.dao.ITowerDao;
import com.youxigu.dynasty2.tower.domain.Tower;
import com.youxigu.dynasty2.tower.domain.TowerSectionBonus;
import com.youxigu.dynasty2.tower.domain.TowerUser;

import java.util.List;

/**
 * 打塔dao实现类
 * @author Dagangzi
 *
 */
public class TowerDao extends BaseDao implements ITowerDao {

	@Override
	public List<Tower> getTowers() {
		return this.getSqlMapClientTemplate().queryForList("getTowers");
	}

    @Override
    public List<TowerSectionBonus> getTowerSectionBonuses() {
        return this.getSqlMapClientTemplate().queryForList("getTowerSectionBonuses");
    }

    @Override
	public TowerUser getTowerUser(long userId) {
		return (TowerUser) this.getSqlMapClientTemplate().queryForObject("getTowerUser", userId);
	}

	@Override
	public void createTowerUser(TowerUser towerUser) {
		this.getSqlMapClientTemplate().insert("insertTowerUser", towerUser);
	}

	@Override
	public void updateTowerUser(TowerUser towerUser) {
		this.getSqlMapClientTemplate().update("updateTowerUser", towerUser);
	}

}
