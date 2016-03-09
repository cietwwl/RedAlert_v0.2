package com.youxigu.dynasty2.map.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.map.dao.ICollectPointDao;
import com.youxigu.dynasty2.map.domain.CollectPoint;

@SuppressWarnings("unchecked")
public class CollectPointDao extends BaseDao implements ICollectPointDao {
	private static final String ID_TYPE = "COLLECTPOINT";

	@Override
	public List<CollectPoint> getAllCollectPoints(long userId) {
		return this.getSqlMapClientTemplate().queryForList(
				"getCollectPointByUserId", userId);
	}

	@Override
	public void insertCollectPoint(CollectPoint p) {
		p.setId(IDUtil.getNextId(ID_TYPE));
		this.getSqlMapClientTemplate().insert("insertCollectPoint", p);
	}

	@Override
	public void updateCollectPoint(CollectPoint p) {
		this.getSqlMapClientTemplate().update("updateCollectPoint", p);
	}

	@Override
	public void deleteCollectPoint(CollectPoint p) {
		this.getSqlMapClientTemplate().delete("deleteCollectPoint", p);
	}

	@Override
	public CollectPoint getCollectPoint(long userId, long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("id", id);
		return (CollectPoint) this.getSqlMapClientTemplate().queryForObject(
				"getCollectPointById", params);
	}

}
