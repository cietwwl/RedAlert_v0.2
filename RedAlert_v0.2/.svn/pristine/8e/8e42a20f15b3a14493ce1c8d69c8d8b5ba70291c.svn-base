package com.youxigu.dynasty2.develop.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.develop.dao.ICastleBuilderDao;
import com.youxigu.dynasty2.develop.domain.CastleBuilder;

/**
 * 建筑工Dao实现类
 *
 */
public class CastleBuilderDao extends BaseDao implements ICastleBuilderDao {

	@Override
	public void createCastleBuilder(CastleBuilder casBuilder) {
		this.getSqlMapClientTemplate().insert("insertCastleBuilder", casBuilder);
		
	}

	@Override
	public void deleteCastleBuilder(CastleBuilder casBuilder) {
		this.getSqlMapClientTemplate().delete("deleteCastleBuilder", casBuilder);
		
	}

	@Override
	public List<CastleBuilder> getCastleBuilders(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getCastleBuilders",userId);
	}

    @Override
    public CastleBuilder getCastleBuilderByIndex(long userId, int builderIndex) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("builderIndex", builderIndex);
        return (CastleBuilder) this.getSqlMapClientTemplate().queryForObject("getCastleBuilderByIndex", params);
    }

    @Override
	public void updateCastleBuilder(CastleBuilder casBuilder) {
		this.getSqlMapClientTemplate().update("updateCastleBuilder",casBuilder);
		
	}
}
