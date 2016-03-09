package com.youxigu.dynasty2.hero.dao.impl;

import java.util.List;

import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.hero.dao.IHeroDrawDao;
import com.youxigu.dynasty2.hero.domain.HeroPub;
import com.youxigu.dynasty2.hero.domain.UserPubAttr;

@SuppressWarnings("unchecked")
public class HeroDrawDao extends BaseDao implements IHeroDrawDao {

	@Override
	public List<HeroPub> getHeroPubs() {
		return this.getSqlMapClientTemplate().queryForList("getHeroPubs");

	}

	@Override
	public UserPubAttr getUserPubAttrById(long userId) {
		return (UserPubAttr) this.getSqlMapClientTemplate().queryForObject(
				"getUserPubAttrById", userId);
	}

	@Override
	public void insertUserPubAttr(UserPubAttr userPubAttr) {

		this.getSqlMapClientTemplate().insert("insertUserPubAttr", userPubAttr);
	}

	@Override
	public void updateUserPubAttr(UserPubAttr userPubAttr) {

		this.getSqlMapClientTemplate().update("updateUserPubAttr", userPubAttr);
	}
}
