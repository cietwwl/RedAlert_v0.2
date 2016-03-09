package com.youxigu.dynasty2.entity.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.entity.domain.EntityConsume;

/**
 * 实体消耗检查器接口类
 * 
 * @author Administrator
 * 
 */
public interface IEntityConsumeChecker {

	/**
	 * 
	 * 检查要消耗的资源与实际拥有的资源
	 * 
	 * @param consumes
	 *            要检查的consume定义
	 * @param context
	 *            参数map：<br>
	 *            casId: 必须 <br>
	 *            num: 倍数，consumes是一个实体的消耗,如果是多个实体，则用num来表示数量 。默认是1 <br>
	 *            factor: 除数，对于取消，需要返回消耗，默认是1.0
	 * 
	 * @return
	 */
	EntityConsumeResultSet check(List<EntityConsume> consumes,
			Map<String, Object> context);

	/**
	 * 检查要消耗的资源与实际拥有的资源，如果资源足够，则更新资源，否则抛出异常
	 * 
	 * @param consumes
	 * @param context
	 * @return
	 */
	EntityConsumeResultSet checkAndUpdate(List<EntityConsume> consumes,
			Map<String, Object> context);
}
