package com.youxigu.dynasty2.entity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.Party;
import com.youxigu.dynasty2.entity.service.IEntityService;

/**
 * 时间，人口，面积，主城，贡献等实体的实现类 继承抽象类AbstractEntityFactoryService，完成对方法loadEntity的具体实现
 * 
 */
public class PartyEntityService extends AbstractEntityFactoryService<Party> {
	private Map<Integer, Party> entityCache = null;

	public void initService() {
		log.info("初始化party");
		if (entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, Party>();
		List<Party> list = entityDao.listPartys();
		if (list != null && list.size() > 0) {
			for (Party entity : list) {
				entityCache.put(entity.getEntId(), entity);
			}
		}
	}
	// 按照指定的实体id，在party表中取一个实体
	@Override
	public Party loadEntity(int entityId, String entityType,
			Map<String, Object> context) {
		Party entity = entityCache.get(entityId);
		if(entity == null) {
			entity = entityDao.getPartyByEntId(entityId);
		}
		if (entity != null) {
			entity.setEntType(entityType);
		}
		return entity;
	}

	public void createEntity(Party entity, IEntityService entityService) {
		super.createEntity(entity, entityService);
		entityDao.createParty(entity);
	}
}