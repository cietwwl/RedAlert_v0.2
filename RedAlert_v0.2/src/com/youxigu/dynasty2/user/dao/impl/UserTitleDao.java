package com.youxigu.dynasty2.user.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.user.dao.IUserTitleDao;
import com.youxigu.dynasty2.user.domain.Title;
import com.youxigu.dynasty2.user.domain.TitleAward;

public class UserTitleDao extends BaseDao implements IUserTitleDao {

	@Override
	public List<Title> listTitles() {
		return this.getSqlMapClientTemplate().queryForList("listTitles");
	}

	@Override
	public List<TitleAward> listTitleAwards() {
		return this.getSqlMapClientTemplate().queryForList("listTitleAwards");
	}

}
