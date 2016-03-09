package com.youxigu.dynasty2.entity.service.script;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectRender;
import com.youxigu.dynasty2.tips.service.IBuffTipService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.BaseException;

public class CastleEffectTimePeriodRender implements IEffectRender {
	public static final Logger log = LoggerFactory
			.getLogger(CastleEffectTimePeriodRender.class);
	private ICastleEffectService castleEffectService;
	private IBuffTipService buffTipService;

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setBuffTipService(IBuffTipService buffTipService) {
		this.buffTipService = buffTipService;
	}

	@Override
	public Map<String, Object> render(Entity entity, EffectDefine effectDefine,
                                      Map<String, Object> context) {
		if (effectDefine.getPara3() <= 0) {
			throw new BaseException("时间周期类效果，必须配置param3");
		}

		// 一次只能使用一个
		// 注意这个render的实现，不考虑entity的区别，只要是相同效果的entity都会对应同一个CastleEffect
        User user = (User) context.get("user");
        if(user == null){
            throw new BaseException("参数错误。");
        }
		long casId = user.getMainCastleId();
		String effTypeId = effectDefine.getEffTypeId();
		int time = effectDefine.getPara3();
		int percent = effectDefine.getPara1();
		int abs = effectDefine.getPara2();

		castleEffectService.lockCastleEffect(casId);

		// 这里，只会有一个同类型的效果
		CastleEffect ce = getTimePeriodEffect(castleEffectService
				.getCastleEffectByEffTypeIdWithTimeout(casId, effTypeId, AppConstants.ENT_PARTY_TIME));
		// 找到第一个具有时间效果的Effect
		long now = System.currentTimeMillis();
		if (ce == null) {
			ce = new CastleEffect();
			ce.setCasId(casId);
			ce.setEntId(AppConstants.ENT_PARTY_TIME);
			ce.setEffTypeId(effTypeId);
			ce.setExpireDttm(new Timestamp(now + time * 1000L));
			ce.setPerValue(effectDefine.getPara1());
			ce.setAbsValue(effectDefine.getPara2());
			castleEffectService.createCastleEffect(ce);
		} else {
			if (ce.getPerValue() == percent && ce.getAbsValue() == abs) {
				long old = ce.getExpireDttm().getTime();
				if (old < now) {
					old = now;
				}
				// 相同效果+时间
				ce.setExpireDttm(new Timestamp(old + time * 1000L));
				ce.setEntId(AppConstants.ENT_PARTY_TIME);
			} else if (ce.isExpired() || (ce.getPerValue() != percent || ce.getAbsValue() != abs)) {
				// 更新成新效果
				ce.setAbsValue(abs);
				ce.setPerValue(percent);
				ce.setExpireDttm(new Timestamp(now + time * 1000L));
				ce.setEntId(AppConstants.ENT_PARTY_TIME);
			} else {
				throw new BaseException("已经存在更高级的效果，不能使用低级的效果");
			}
			castleEffectService.updateCastleEffect(ce);
		}

		if (effectDefine.getBuffName() != null
				&& !"".equals(effectDefine.getBuffName())) {
			// 添加buf
			buffTipService.addBuffTip(user.getUserId(), effectDefine.getEffId(), effectDefine
					.getBuffName(), new Timestamp(now), ce.getExpireDttm(), effectDefine.getBuffId());
		}

		if (log.isDebugEnabled()) {
			log.debug("使用[{}],效果[{}]变更", entity.getEntId(), effectDefine.getEffTypeId());
			log.debug("效果值:[{}%],效果持续时间:{}", ce.getPerValue(), ce.getExpireDttm());
		}

		return null;
	}

	private CastleEffect getTimePeriodEffect(List<CastleEffect> castleEffects) {
		if (castleEffects == null)
			return null;
		for (CastleEffect e : castleEffects) {
//			if(e.getEntId() != 0) {
//				continue;
//			}
			if (e.getExpireDttm() != null) {
				return e;
			}
		}
		return null;
	}

}
