package com.youxigu.dynasty2.armyout.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.armyout.dao.IMilitarySituationDao;
import com.youxigu.dynasty2.armyout.domain.qingbao.MilitarySituation;

public class MilitarySituationDao extends BaseDao implements IMilitarySituationDao {

	@Override
	public void insertMilitarySituation(MilitarySituation misi) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("insertMilitarySituation",misi);
	}

	@Override
	public MilitarySituation getMilitarySituationById(int id) {
		return (MilitarySituation)this.getSqlMapClientTemplate().queryForObject("getMilitarySituationById", id);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MilitarySituation> getMilitarySituationListByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getMilitarySituationListByUserId", 
				userId);
	}

	@Override
	public void deleteMilitarySituation(int id) {
		this.getSqlMapClientTemplate().delete("deleteMilitarySituation",id);
	}

	@Override
	public void setHasView(int id , boolean hasView) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("hasView", hasView?1:0);
		this.getSqlMapClientTemplate().update("setHasView",params);
	}

	@Override
	public void deleteMilitarySituations(int id, long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("userId", userId);
		this.getSqlMapClientTemplate().update("deleteMilitarySituations",params);
	}

}
