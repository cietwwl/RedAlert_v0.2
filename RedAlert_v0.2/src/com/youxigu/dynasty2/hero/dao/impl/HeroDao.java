package com.youxigu.dynasty2.hero.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.engine.config.ShardingConfig;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.manu.core.base.BaseDao;
import com.youxigu.dynasty2.asyncdb.service.impl.IDUtil;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.hero.dao.IHeroDao;
import com.youxigu.dynasty2.hero.domain.CommanderColorProperty;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.domain.StrongLimit;

@SuppressWarnings("unchecked")
public class HeroDao extends BaseDao implements IHeroDao {
	private static final String ID_TYPE = "HERO";

	@Override
	public Hero getHeroByHeroId(long userId, long heroId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("heroId", heroId);
		return (Hero) this.getSqlMapClientTemplate().queryForObject(
				"getHeroByHeroId", params);
	}

	@Override
	public Hero getHeroBySysHeroId(long userId, int sysHeroId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("userId", userId);
		params.put("sysHeroId", sysHeroId);
		return (Hero) this.getSqlMapClientTemplate().queryForObject(
				"getHeroBySysHeroId", params);
	}

	@Override
	public void insertHero(Hero hero) {
		if (hero.getHeroId() <= 0)
			hero.setHeroId(IDUtil.getNextId(ID_TYPE));
		this.getSqlMapClientTemplate().insert("insertHero", hero);
	}

	@Override
	public void deleteHero(Hero hero) {
		this.getSqlMapClientTemplate().delete("deleteHero", hero);
	}

	@Override
	public void updateHero(Hero hero) {
		this.getSqlMapClientTemplate().update("updateHero", hero);
	}

	@Override
	public List<Hero> getHeroListByUserId(long userId) {
		return this.getSqlMapClientTemplate().queryForList("getHeroByUserId",
				userId);
	}

	@Override
	public List<DroppedEntity> getHeroCardsByUserId(long userId) {
		List<Hero> heros = this.getHeroListByUserId(userId);
		List<DroppedEntity> list = new ArrayList<DroppedEntity>();
		if (heros != null && heros.size() > 0) {
			for (Hero hero : heros) {
				int entId = hero.getHeroCardEntId();
				int num = hero.getHeroCardNum();
				if (num > 0 && entId > 0) {
					list.add(new DroppedEntity(entId, num));
				}
			}
		}
		return list;
	}

	@Override
	public List<DroppedEntity> getHeroSoulsByUserId(long userId) {
		List<Hero> heros = this.getHeroListByUserId(userId);
		List<DroppedEntity> list = new ArrayList<DroppedEntity>();
		if (heros != null && heros.size() > 0) {
			for (Hero hero : heros) {
				int entId = hero.getHeroSoulEntId();
				int num = hero.getHeroSoulNum();
				if (num > 0 && entId > 0) {
					list.add(new DroppedEntity(entId, num));
				}
			}
		}
		return list;
	}

	public List<Map<Integer, Integer>> getHeroExp() {
		return this.getSqlMapClientTemplate().queryForList("getHeroExp");
	}

	@Override
	public List<StrongLimit> getStrongLimitList() {
		return this.getSqlMapClientTemplate()
				.queryForList("getStrongLimitList");
	}

	@Override
	public void cleanHeroStatus(int status) {
		int shardNum = 1;
		SqlMapClientImpl client = (SqlMapClientImpl) this.getSqlMapClient();
		ShardingConfig conf = client.delegate.getShardingConfig("hero");
		if (conf != null) {
			if (conf.getProperties() != null) {
				shardNum = Integer.parseInt(conf.getProperties().getProperty(
						"shardingNum", "1"));
			}
		}

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("status", status);
		for (int i = 0; i < shardNum; i++) {
			params.put("shardNum", i);
			this.getSqlMapClientTemplate().update("updateHeroStatus", params);
		}
	}

	@Override
	public List<CommanderColorProperty> listCommanderColorPropertys() {
		return this.getSqlMapClientTemplate().queryForList(
				"listCommanderColorPropertys");
	}

}
