package com.youxigu.dynasty2.entity.service.validator.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.entity.domain.EntityLimit;
import com.youxigu.dynasty2.entity.service.EntityLimitResult;
import com.youxigu.dynasty2.entity.service.IEntityLimitValidator;
import com.youxigu.dynasty2.util.BaseException;

/**
 * 默认的实体约束校验器
 * 
 */
public class DefaultEntityLimitValidator implements IEntityLimitValidator {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public EntityLimitResult validate(EntityLimit limit,
			Map<String, Object> context) {

		throw new BaseException("没有找到对应的实体校验器");
	}

}
