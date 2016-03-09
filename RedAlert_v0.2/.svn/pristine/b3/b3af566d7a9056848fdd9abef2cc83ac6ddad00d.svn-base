package com.youxigu.dynasty2.log;

import java.util.Map;

import com.manu.core.base.BaseDao;

public class TlogDemoDao extends BaseDao implements ITlogDemoDao{

	@Override
	public void insertTlog(int areaId,String sqlId, Map<String, Object> params) {
		this.getSqlMapClientTemplate().insert(sqlId,params);
	}

}
