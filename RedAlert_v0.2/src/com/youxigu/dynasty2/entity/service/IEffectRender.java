package com.youxigu.dynasty2.entity.service;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;

/**
 * 效果渲染器
 * 
 * 
 * 渲染器分为2层次
 * 
 * 每个effect自行定义的，在EffectDefine表的serviceName字段来定义
 * 
 * @author Administrator
 * 
 */
public interface IEffectRender {

	/**
	 * 处理效果类型
	 * @param effectDefine
	 * @param context
	 */
	Map<String, Object> render(Entity entity, EffectDefine effectDefine,
			Map<String, Object> context);
}
