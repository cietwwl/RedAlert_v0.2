package com.youxigu.dynasty2.entity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.DropPackEntity;
import com.youxigu.dynasty2.entity.domain.DropPackItem;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.service.IEntityService;

public class DropPackEntityService extends
		AbstractEntityFactoryService<DropPackEntity> {
	private Map<Integer, DropPackEntity> entityCache = null;
	
	public void initService() {
		log.info("初始化droppack");
		if(entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, DropPackEntity>();
		List<DropPackEntity> list = entityDao.listDropPackEntitys();
		if(list != null && list.size() >0) {
			for(DropPackEntity entity : list) {
				entity.check();
				entityCache.put(entity.getEntId(), entity);
			}
		}
	}

	@Override
	public DropPackEntity loadEntity(int entityId, String entityType,
			Map<String, Object> context) {

		DropPackEntity entity = entityCache.get(entityId);
		if(entity == null) {
			entity = entityDao.getDropPackByEntId(entityId);
		}
		if (entity != null) {
			entity.setEntType(entityType);
			List<DropPackItem> items = dropPackItemMaps.get(entityId);
			if (items == null) {
				synchronized (this) {
					items = dropPackItemMaps.get(entityId);
					if (items == null) {
						items = this.entityDao
								.getDropPackItemsByEntId(entityId);
						if (items != null) {
							dropPackItemMaps.put(entityId, items);
						}
					}
				}
			}
			if (items != null && items.size() > 0) {
				entity.setItems(items);
			} else {
				log
						.warn(
								"can not find DropPack's config, entId:{} ,maybe error",
								entityId);
			}

		}
		return entity;
	}

	@Override
	public void afterLoad(DropPackEntity entity, IEntityService entityService) {
		List<DropPackItem> items = entity.getItems();
		if (items != null) {
			for (DropPackItem item : items) {
				Entity DropEntity = entityService
						.getEntity(item.getDropEntId());
				if (DropEntity == null) {
					log.error("掉落包:{}掉落的entity:{}不存在", entity.getEntId(), item
							.getDropEntId());
				}

			}
		}
		// DO NOTHING
	}

	@Override
	public void createEntity(DropPackEntity entity, IEntityService entityService) {
		List<DropPackItem> items = entity.getItems();
		if (items != null) {
			int id = entity.getEntId() * 10;
			for (DropPackItem item : items) {
				item.setId(id);
				entityDao.createDropPackItem(item);
				
				id++;
			}
			dropPackItemMaps.put(entity.getEntId(), items);
		}
	}

}
