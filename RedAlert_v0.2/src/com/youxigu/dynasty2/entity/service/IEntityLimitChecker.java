package com.youxigu.dynasty2.entity.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.EntityLimit;

public interface IEntityLimitChecker {

	/**
	 * 
	 * 检查实体升降级的约束条件
	 * 
	 * @param limits
	 *            要检查的EntityLimit定义
	 * @param context
	 *            参数map：<br>
	 *            casId: 必须 <br>
	 * 
	 * 
	 * @return
	 * @return
	 */
	EntityLimitResultSet check(List<EntityLimit> limits,
			Map<String, Object> context);

}
