package com.youxigu.dynasty2.entity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.Building;
import com.youxigu.dynasty2.entity.service.IEntityService;

/**
 * 建筑实体的实现类 继承抽象类AbstractEntityFactoryService，完成对方法loadEntity的具体实现
 * 
 */
public class BuildingEntityService extends AbstractEntityFactoryService<Building> {

	private Map<Integer, Building> entityCache = null;

	public void initService() {
		log.info("初始化建筑");
		if (entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, Building>();
		List<Building> list = entityDao.listBuildings();
		if (list != null && list.size() > 0) {
			for (Building entity : list) {
				entityCache.put(entity.getEntId(), entity);
			}
		}
	}

	// 按照指定的实体id，在building表中取一个建筑
	@Override
	public Building loadEntity(int entityId, String entityType, Map<String, Object> context) {
		Building building = entityCache.get(entityId);
		if (building == null) {
			building = entityDao.getBuildingByEntId(entityId);
		}
		if (building != null)
			building.setEntType(entityType);
		return building;
	}

	@Override
	public void createEntity(Building entity, IEntityService entityService) {
		super.createEntity(entity, entityService);
		entityDao.deleteBuilding(entity.getEntId());
		entityDao.createBuilding(entity);
	}

}
