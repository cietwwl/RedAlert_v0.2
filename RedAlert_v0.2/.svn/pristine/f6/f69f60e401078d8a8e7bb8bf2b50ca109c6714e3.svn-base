package com.youxigu.dynasty2.entity.service.impl;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.youxigu.dynasty2.entity.domain.Army;
import com.youxigu.dynasty2.entity.service.IEntityService;


/**
 * 兵种实体的实现类 继承抽象类AbstractEntityFactoryService，完成对方法loadEntity的具体实现
 * 
 */
public class ArmyEntityService extends AbstractEntityFactoryService<Army> {

	private Map<Integer, Army> entityCache = null;

	public void initService() {
		if(entityCache != null) {
			return;
		}
		entityCache = new ConcurrentHashMap<Integer, Army>();
		List<Army> list = entityDao.listArmys();
		if(list != null && list.size() >0) {
			for(Army army : list) {
				entityCache.put(army.getEntId(), army);
			}
		}
	}
	
	@Override
	public Army loadEntity(int entityId, String entityType,
			Map<String, Object> context) {

		Army army = entityCache.get(entityId);
		if(army == null) {
			army = entityDao.getArmyByEntId(entityId);
		}
		if (army != null){
			army.setEntType(entityType);
		}
		return army;

	}
	@Override
	public void afterLoad(Army entity,IEntityService entityService)	{
		
	}
	@Override
	public void createEntity(Army entity,IEntityService entityService){
		super.createEntity(entity,entityService);
		//entityDao.deleteArmy(entity.getEntId());
		entityDao.createArmy(entity);	
	}

}