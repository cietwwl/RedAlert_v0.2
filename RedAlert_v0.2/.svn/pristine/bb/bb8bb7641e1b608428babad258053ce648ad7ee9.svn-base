package com.youxigu.dynasty2.entity.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.BoxEntity;
import com.youxigu.dynasty2.entity.domain.DropPackItem;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.service.IEntityService;

/**
 * 线上宝箱类掉落包Service，不直接掉落，扔到背包中，使用时打开掉落
 * @author Administrator
 *
 */
public class BoxEntityService extends
		AbstractEntityFactoryService<BoxEntity> {
	private Map<Integer, BoxEntity> entityCache = null;
	
	public void initService() {
		if(entityCache != null) {
			return;
		}
		log.info("初始化礼包");
		entityCache = new ConcurrentHashMap<Integer, BoxEntity>();
		List<BoxEntity> list = entityDao.listBoxEntitys();
		if(list != null && list.size() >0) {
			for(BoxEntity entity : list) {
				entity.check();
				entityCache.put(entity.getEntId(), entity);
			}
		}
	}

	@Override
	public BoxEntity loadEntity(int entityId, String entityType,
			Map<String, Object> context) {
		BoxEntity entity = entityCache.get(entityId);
		if(entity == null) {
			entity = entityDao.getBoxByEntId(entityId);
		}
		if (entity != null) {
			entity.setEntType(entityType);
			List<DropPackItem> items = dropPackItemMaps.get(entityId);
			if (items != null && items.size() > 0) {
				entity.setItems(items);
			}else {
				log.warn("can not find DropPack's config, entId:{} ,maybe error", entityId);
			}

		}
		return entity;
	}

	@Override
	public void afterLoad(BoxEntity entity, IEntityService entityService) {
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
	public void createEntity(BoxEntity entity,IEntityService entityService) {
		//super.createEntity(entity,entityService);
		//entityDao.createItem(entity);
		//entityDao.createBox(entity);
		List<DropPackItem> items = entity.getItems();
		if (items != null) {
			int id= entity.getEntId()*10;
			for (DropPackItem item : items) {
				item.setId(id);
				entityDao.createDropPackItem(item);
				id++;
			}
		}
	}

}
