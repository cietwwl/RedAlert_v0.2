package com.youxigu.dynasty2.tips.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.tips.dao.IBuffTipDao;
import com.youxigu.dynasty2.tips.domain.BuffDefine;
import com.youxigu.dynasty2.tips.domain.BuffTip;

public class BuffTipDao extends BaseDao implements IBuffTipDao{

	@Override
	public void createBuffTip(BuffTip buffTip) {
		this.getSqlMapClientTemplate().insert("insertBuffTip",buffTip);
	}

	@Override
	public void deleteBuffTip(BuffTip buffTip) {
		this.getSqlMapClientTemplate().delete("deleteBuffTipById",buffTip);
	}

	@Override
	public List<BuffTip> getBuffTipsByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getBuffTipsByUserId",userId);
	}

	@Override
	public void updateBuffTip(BuffTip buffTip) {
		this.getSqlMapClientTemplate().update("updateBuffTip",buffTip);
	}

	@Override
	public List<BuffDefine> getBuffDefines() {
		return this.getSqlMapClientTemplate().queryForList("getBuffDefines");
	}

}
