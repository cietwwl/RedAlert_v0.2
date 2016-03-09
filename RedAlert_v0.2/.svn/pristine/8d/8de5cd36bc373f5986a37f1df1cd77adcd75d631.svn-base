package com.youxigu.dynasty2.entity.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;

/**
 * 实体service工厂接口定义
 * 
 */
public interface IEntityFactoryService<T extends Entity> {

	/**
	 * 加载实体定义 
	 * @param entityId
	 * @param entityType
	 * @param context
	 * @return
	 */
	public T getEntity(int entityId, String entityType,
			Map<String, Object> context);

	/**
	 * 全部实体加载后的后续操作
	 * @param entity
	 * @param entityService
	 */
	public void afterLoad(T entity,IEntityService entityService);
	
	
	
	/**
	 * 根据实体子类型获得实体列表
	 * @param type
	 * @return
	 */
	List<T> getEntityByTypes(String subType,IEntityService entityService);
	/**
	 * 实体操作服务
	 * 
	 * @return
	 */
	// public IEntityActionService<T> getActionService();
	
	public void createEntity(T entity,IEntityService entityService);	

}
