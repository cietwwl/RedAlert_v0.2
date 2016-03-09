package com.youxigu.dynasty2.entity.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Technology;
import com.youxigu.dynasty2.entity.service.IEntityFactoryService;
import com.youxigu.dynasty2.entity.service.IEntityService;

/**
 * 科技实体的实现类 继承抽象类AbstractEntityFactoryService，完成对方法loadEntity的具体实现
 * 
 */
@SuppressWarnings("rawtypes")
public class TechnologyEntityService extends
		AbstractEntityFactoryService<Technology> {
	private Map<Integer, Technology> entityCache = null;

	public void initService() {
		log.info("初始化科技");
		if (entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, Technology>();
		List<Technology> list = entityDao.listTechnologys();
		if (list != null && list.size() > 0) {
			for (Technology entity : list) {
				entityCache.put(entity.getEntId(), entity);
			}
		}
	}

	/**
	 * Item分成多个子类,这里是子类的加载器
	 */
	private Map<String, IEntityFactoryService> factorys;

	public void setFactorys(Map<String, IEntityFactoryService> factorys) {
		this.factorys = factorys;
	}

	// private Map<String,List<Technology>> typeMaps = new
	// HashMap<String,List<Technology>>();
	// 按照指定的实体id，在technology表中取一个科技类型
	@SuppressWarnings("unchecked")
	public Technology loadEntity(int entityId, String entityType,
			Map<String, Object> context) {

		Technology entity = null;
		if (factorys != null) {
			IEntityFactoryService factory = factorys.get(entityType);
			if (factory != null) {
				entity = (Technology) factory.getEntity(entityId, entityType,
						context);

				if (entity != null) {
					// 子类也要放到父类的分类中
					Map<String, List<Entity>> entityCacheByType = (Map<String, List<Entity>>) context
							.get("entityCacheByType");
					List<Entity> entityes = entityCacheByType
							.get(Entity.TYPE_TECH);
					if (entityes == null) {
						entityes = new ArrayList<Entity>();
						entityCacheByType.put(Entity.TYPE_TECH, entityes);
					}
					entityes.add(entity);
				}
			}
		}
		if (entity == null) {
			entity = entityCache.get(entityId);
			if (entity == null) {
				entity = entityDao.getTechnologyByEntId(entityId);
			}
			if (entity != null) {
				entity.setEntType(entityType);
			}
		}

		return entity;

	}

	/**
	 * 根据实体子类型获得实体列表
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public List<Technology> getEntityByTypes(String subType,
			IEntityService entityService) {
		// entityService.getEntityByEntTypes(entType)
		List<Technology> results = new ArrayList<Technology>();
		List<Entity> entitys = entityService
				.getEntityByEntTypes(Entity.TYPE_TECH);
		if (entitys != null) {
			for (Entity e : entitys) {
				Technology t = (Technology) e;
				if (subType.equals(t.getTechType())) {
					results.add(t);
				}
			}
		}

		return results;
	}

	public void createEntity(Technology entity, IEntityService entityService) {
		super.createEntity(entity, entityService);
		// entityDao.deleteTechnology(entity.getEntId());
		entityDao.createTechnology(entity);
	}

}