package com.youxigu.dynasty2.entity.service.validator.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.entity.domain.EntityConsume;
import com.youxigu.dynasty2.entity.service.EntityConsumeResult;
import com.youxigu.dynasty2.entity.service.IEntityConsumeValidator;

/**
 * 默认的实体消耗校验器
 * 什么也不做，直接返回具体的消耗数字
 */
public class DefaultEntityConsumeValidator implements IEntityConsumeValidator {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public EntityConsumeResult validate(EntityConsume consume,
			Map<String, Object> context) {
		return validateAndUpdate(consume,context);
	}

	@Override
	public EntityConsumeResult validateAndUpdate(EntityConsume consume,
			Map<String, Object> context) {
		EntityConsumeResult result = new EntityConsumeResult(consume);

		int num = 1;
		Object tmp = context.get("num");
		if (tmp != null)
			num = (Integer) tmp;
		
		int value = consume.getNeedEntNum() * Math.abs(num);
		result.setActualNum(value);
		
		return result;
	}


}
