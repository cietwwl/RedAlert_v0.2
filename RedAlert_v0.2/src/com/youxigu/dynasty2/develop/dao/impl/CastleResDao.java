package com.youxigu.dynasty2.develop.dao.impl;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.develop.dao.ICastleResDao;
import com.youxigu.dynasty2.develop.domain.CastleResource;

public class CastleResDao extends BaseDao implements ICastleResDao {
	@Override
	public void createCastleResource(CastleResource cr) {
		this.getSqlMapClientTemplate().insert("insertCastleRes", cr);

	}

	@Override
	public void updateCastleResource(CastleResource cr) {
		this.getSqlMapClientTemplate().update("updateCastleRes", cr);

	}

	@Override
	public CastleResource getCastleResourceById(long casId) {
		return (CastleResource) this.getSqlMapClientTemplate().queryForObject(
				"getCastleResById", casId);
	}
}
