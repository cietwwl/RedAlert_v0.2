package com.youxigu.dynasty2.entity.service;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.EntityConsume;

/**
 * 实体升级消耗检查器
 * 
 * @author Administrator
 * 
 */
public interface IEntityConsumeValidator {

	/**
	 * 
	 * @param consume
	 * @param context
	 * @return
	 */
	public EntityConsumeResult validate(EntityConsume consume,
			Map<String, Object> context);

	/**
	 * 
	 * @param consume
	 * @param context
	 * @return
	 */
	public EntityConsumeResult validateAndUpdate(EntityConsume consume,
			Map<String, Object> context);

}
