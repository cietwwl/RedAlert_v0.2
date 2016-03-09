package com.youxigu.dynasty2.develop.service.impl;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;
import com.youxigu.dynasty2.util.BaseException;
/**
 * 科技实体渲染器
 * @author Dagangzi
 *
 */
public class CastleTechnologyEffectRender implements IEntityEffectRender {
	ICastleEffectService castleEffectService;

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	@Override
	public Map<String, Object> render(Entity entity, Map<String, Object> context) {
		Castle mainCastle = (Castle) context.get("mainCastle");
		int level = (Integer) context.get("level");
		int entId=0;//entity.getEntId(); //科技的效果不按entID区分，全部累加
		List<EffectDefine> effects = entity.getEffects(level);
		if (effects != null) {
			for (EffectDefine effect : effects) {
				if (effect.getTarget() == EffectDefine.TARGET_CASTLE
						|| effect.getTarget() == EffectDefine.TARGET_DEFAULT) {
					
					castleEffectService.lockCastleEffect(mainCastle.getCasId());
					
					CastleEffect ce = castleEffectService
							.getCastleEffectByEffTypeId(mainCastle.getCasId(),
									entId, effect.getEffTypeId());
					if (ce == null) {
						// 新建
						ce = new CastleEffect();
						ce.setCasId(mainCastle.getCasId());
						ce.setEntId(entId);
						ce.setEffTypeId(effect.getEffTypeId());
						ce.setExpireDttm(null);
						ce.setPerValue(effect.getPara1());
						ce.setAbsValue(effect.getPara2());
						castleEffectService.createCastleEffect(ce);
					} else {
						// 科技效果累加
						ce.setPerValue(ce.getPerValue()+ effect.getPara1());
						ce.setAbsValue(ce.getAbsValue()+ effect.getPara2());
						ce.setExpireDttm(null);
						castleEffectService.updateCastleEffect(ce);
					}
				} else {
					throw new BaseException("科技效果只影响城池效果");
				}
			}
		}
		return null;
	}
	// @Override
	// public void render(EffectDefine effectDefine, Map<String, Object>
	// context) {
	// long casId = (Long) context.get("casId");
	// castleEffectService.addCastleTechnologyEffect(casId, effectDefine);
	//
	// }

}
