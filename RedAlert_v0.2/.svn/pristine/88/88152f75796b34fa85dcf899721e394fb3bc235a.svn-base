package com.youxigu.dynasty2.entity.service.effect.impl;

import java.util.Map;

import com.manu.util.Util;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.util.BaseException;

public class DefaultEffectRender implements IEffectRender {

	@Override
	public Map<String, Object> render(Entity entity, EffectDefine effectDefine,
			Map<String, Object> context) {
		throw new BaseException("  not found IEffectRender");
	}
	
	protected int getRandomNum(int max, int min) {
		return max > min ? Util.randInt(max - min+1) + min : min;
	}
}
