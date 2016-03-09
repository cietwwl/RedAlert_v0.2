package com.youxigu.dynasty2.entity.service.script;

import java.util.Map;

import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;

/**
 * 增加资源产量百分比效果
 * 
 * 
 * 
 * @author Administrator
 * 
 */
public class ResourceSkillRender extends CastleEffectTimePeriodRender {
	@Override
	public Map<String, Object> render(Entity entity, EffectDefine effectDefine,
                                      Map<String, Object> context) {
		super.render(entity, effectDefine, context);

		return null;
	}
}
