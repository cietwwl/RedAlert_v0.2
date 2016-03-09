package com.youxigu.dynasty2.entity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.Resource;
import com.youxigu.dynasty2.entity.service.IEntityService;

/**
 * 资源实体的实现类 继承抽象类AbstractEntityFactoryService，完成对方法loadEntity的具体实现
 * 
 */
public class ResourceEntityService extends
		AbstractEntityFactoryService<Resource> {
	private Map<Integer, Resource> entityCache = null;

	public void initService() {
		log.info("初始化资源");
		if (entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, Resource>();
		List<Resource> list = entityDao.listResources();
		if (list != null && list.size() > 0) {
			for (Resource entity : list) {
				entityCache.put(entity.getEntId(), entity);
			}
		}
	}
	
	// 按照指定的实体id，在resource表中取一个资源类型
	public Resource loadEntity(int entityId, String entityType,
			Map<String, Object> context) {
		Resource entity = entityCache.get(entityId);
		if(entity == null) {
			entity = entityDao.getResourceByEntId(entityId);
		}
		if (entity != null) {
			entity.setEntType(entityType);
		}
		return entity;
	}

	public void createEntity(Resource entity, IEntityService entityService) {
		super.createEntity(entity,entityService);
		//entityDao.deleteResource(entity.getEntId());
		entityDao.createResource(entity);	
	}
}