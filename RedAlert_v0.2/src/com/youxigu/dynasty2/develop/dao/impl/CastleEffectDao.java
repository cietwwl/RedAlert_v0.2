package com.youxigu.dynasty2.develop.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.develop.dao.ICastleEffectDao;
import com.youxigu.dynasty2.develop.domain.CastleEffect;

@SuppressWarnings("unchecked")
public class CastleEffectDao extends BaseDao implements ICastleEffectDao {

	@Override
	public void createCastleEffect(CastleEffect ce) {
		this.getSqlMapClientTemplate().insert("insertCastleEffect", ce);

	}

	@Override
	public void deleteCastleEffect(CastleEffect ce) {
		this.getSqlMapClientTemplate().delete("deleteCastleEffectById", ce);

	}

	@Override
	public List<CastleEffect> getCastleEffectByCasId(long casId) {

		return this.getSqlMapClientTemplate().queryForList(
				"getCastleEffectByCasId", casId);
	}

	@Override
	public void updateCastleEffect(CastleEffect ce) {
		this.getSqlMapClientTemplate().update("updateCastleEffect", ce);

	}

}
