package com.youxigu.dynasty2.entity.service;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;

/**
 * 
 * @author Administrator
 * 
 * @param <T>
 * 
 */
@Deprecated
public interface IEntityActionService<T extends Entity> {

	/**
	 * 执行实体的某个操作，升级、降级，强化。分解等等，不同的实体有不同的操作
	 * 
	 * @param entity
	 * @param action
	 * @param params
	 */
	Map<String, Object> doAction(T entity, int action,
			Map<String, Object> params);
}
