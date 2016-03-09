package com.youxigu.dynasty2.develop.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.develop.dao.ICastleEffectDao;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEffectChangedListener;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;

/**
 * 城池被各种entity渲染后的效果管理
 * 
 * @author Administrator
 * 
 */
public class CastleEffectService implements ICastleEffectService {
	private ICastleEffectDao castleEffectDao;
	private Map<String, IEffectChangedListener> listeners;

	public void setListeners(Map<String, IEffectChangedListener> listeners) {
		this.listeners = listeners;
	}

	public void setCastleEffectDao(ICastleEffectDao castleEffectDao) {
		this.castleEffectDao = castleEffectDao;
	}

	@Override
	@Deprecated
	public void addCastleEffect(long casId, Entity entity, EffectDefine effect) {
		lockCastleEffect(casId);
		CastleEffect ce = this.getCastleEffectByEffTypeId(casId, entity
				.getEntId(), effect.getEffTypeId());
		if (ce == null) {
			ce = new CastleEffect();
			ce.setCasId(casId);
			ce.setEntId(entity.getEntId());
			ce.setEffTypeId(effect.getEffTypeId());

			ce.setPerValue(effect.getPara1());
			ce.setAbsValue(effect.getPara2());
			ce.setExpireDttm(null);
			castleEffectDao.createCastleEffect(ce);
		} else {
			// 效果直接更新，不累加
			ce.setPerValue(effect.getPara1());
			ce.setAbsValue(effect.getPara2());
			ce.setExpireDttm(null);
			// ce.setPerValue(techEffect.getPara1());
			// ce.setAbsValue(techEffect.getPara2());
			castleEffectDao.updateCastleEffect(ce);
		}
	}

	/**
	 * 取城池效果
	 */
	public List<CastleEffect> getCastleEffectByCasId(long casId) {
		List<CastleEffect> effects = castleEffectDao
				.getCastleEffectByCasId(casId);

		if (effects != null) {
			Iterator<CastleEffect> lit = effects.iterator();
			while (lit.hasNext()) {
				CastleEffect e = lit.next();
				if (e.isExpired()) {// 过滤过期的效果

					lit.remove();

				}
			}
		}

		return effects;

	}

	/**
	 * 取特定类型的城市效果:不包括过期的
	 */
	public List<CastleEffect> getCastleEffectByEffTypeId(long casId,
			String effTypeId) {

		List<CastleEffect> effects = this.getCastleEffectByCasId(casId);
		List<CastleEffect> subs = new ArrayList<CastleEffect>();
		for (CastleEffect e : effects) {
			if (effTypeId.equals(e.getEffTypeId())) {// 过滤特定类型的城市效果
				subs.add(e);
			}
		}
		return subs;
	}

	/**
	 * 取特定类型的城市效果:包括过期的
	 */
	public List<CastleEffect> getCastleEffectByEffTypeIdWithTimeout(long casId,
			String effTypeId) {
		List<CastleEffect> effects = castleEffectDao
				.getCastleEffectByCasId(casId);
		// List<CastleEffect> effects = this.getCastleEffectByCasId(casId);
		List<CastleEffect> subs = new ArrayList<CastleEffect>();
		for (CastleEffect e : effects) {
			if (effTypeId.equals(e.getEffTypeId())) {// 过滤特定类型的城市效果
				subs.add(e);
			}
		}
		return subs;
	}
	/**
	 * 取特定类型的城市效果:包括过期的
	 */
	@Override
    public List<CastleEffect> getCastleEffectByEffTypeIdWithTimeout(long casId,
                                                                    String effTypeId, int entId) {
		List<CastleEffect> effects = castleEffectDao
				.getCastleEffectByCasId(casId);
		List<CastleEffect> subs = new ArrayList<CastleEffect>();
		for (CastleEffect e : effects) {
			if (e.getEntId() == entId && effTypeId.equals(e.getEffTypeId())) {// 过滤特定类型的城市效果
				subs.add(e);
			}
		}
		return subs;
	}

	/**
	 * 按实体ID+效果类型,只能是一条
	 * 
	 * @param casId
	 * @param entId
	 * @param effTypeId
	 * @return
	 */
	@Override
	public CastleEffect getCastleEffectByEffTypeId(long casId, int entId,
			String effTypeId) {
		List<CastleEffect> effects = this.getCastleEffectByCasId(casId);

		for (CastleEffect e : effects) {
			if (e.getEntId() == entId && e.getEffTypeId().equals(effTypeId)) {
				return e;
			}
		}
		return null;

	}

	@Override
	public List<CastleEffect> getCastleEffectByEntId(long casId, int entId) {
		List<CastleEffect> subs = new ArrayList<CastleEffect>();
		List<CastleEffect> effects = this.getCastleEffectByCasId(casId);

		for (CastleEffect e : effects) {
			if (e.getEntId() == entId) {
				subs.add(e);
			}
		}
		return subs;

	}

	/**
	 * 取相同效果的累积效果
	 */
	@Override
	public EffectValue getSumCastleEffectValueByEffectType(long casId,
			String effTypeId) {
		return getSumCastleEffectValueByEffectType(effTypeId, this
				.getCastleEffectByCasId(casId));
	}

	public EffectValue getSumCastleEffectValueByEffectType(String effTypeId,
			List<CastleEffect> effects) {
		EffectValue value = new EffectValue();
		// 这里为什么不把effTypeId一起传下去？
		for (CastleEffect e : effects) {
			if (!e.isExpired() && effTypeId.equals(e.getEffTypeId())) {
				value.setAbsValue(value.getAbsValue() + e.getAbsValue());
				value.setPerValue(value.getPerValue() + e.getPerValue());
			}
		}
		return value;
	}

	public int getSumCastleEffectAbsValueByEffectType(long casId,
			String effTypeId) {
		int value = 0;
		List<CastleEffect> effects = this.getCastleEffectByCasId(casId);// TODO
		// 这里为什么不把effTypeId一起传下去？
		for (CastleEffect e : effects) {
			if (!e.isExpired() && effTypeId.equals(e.getEffTypeId())) {
				value = value + e.getAbsValue();
			}
		}
		return value;
	}

	public int getSumCastleEffectPercentValueByEffectType(long casId,
			String effTypeId) {
		int value = 0;
		List<CastleEffect> effects = this.getCastleEffectByCasId(casId);// TODO
		// 这里为什么不把effTypeId一起传下去？
		for (CastleEffect e : effects) {
			if (!e.isExpired() && effTypeId.equals(e.getEffTypeId())) {
				value = value + e.getPerValue();
			}
		}
		return value;

	}

	@Override
	public void createCastleEffect(CastleEffect ce) {

		castleEffectDao.createCastleEffect(ce);
		notiftListener(ce);

	}

	public void createCastleEffect(CastleEffect ce, boolean notify) {

		castleEffectDao.createCastleEffect(ce);
		if (notify) {
			notiftListener(ce);
		}

	}

	private void notiftListener(CastleEffect ce) {
		if (listeners != null) {
			IEffectChangedListener listener = listeners.get(ce.getEffTypeId());
			if (listener != null) {
				listener.effectChanged(ce);
			}
		}
	}

	@Override
	public void updateCastleEffect(CastleEffect ce) {

		castleEffectDao.updateCastleEffect(ce);
		notiftListener(ce);
	}

	@Override
	public void updateCastleEffect(CastleEffect ce, boolean notify) {
		castleEffectDao.updateCastleEffect(ce);
		if (notify) {
			notiftListener(ce);
		}
	}

	@Override
	public void deleteCastleEffect(CastleEffect ce) {
		castleEffectDao.deleteCastleEffect(ce);
		notiftListener(ce);
	}

	@Override
	public void deleteCastleEffect(CastleEffect ce, boolean notify) {
		castleEffectDao.deleteCastleEffect(ce);
		if (notify) {
			notiftListener(ce);
		}

	}

	@Override
	public List<CastleEffect> removeTimeoutCastleEffect(long casId) {
		lockCastleEffect(casId);
		List<CastleEffect> effects = castleEffectDao
				.getCastleEffectByCasId(casId);

		if (effects != null) {
			Iterator<CastleEffect> lit = effects.iterator();
			while (lit.hasNext()) {
				CastleEffect e = lit.next();
				if (e.isExpired()) {// 过滤过期的效果

					castleEffectDao.deleteCastleEffect(e);

					lit.remove();

				}
			}
		}
		return effects;

	}

	@Override
	public void removeCastleEffectByType(long casId, String effTypeId) {
		lockCastleEffect(casId);
		List<CastleEffect> effects = castleEffectDao
				.getCastleEffectByCasId(casId);

		if (effects != null) {
			Iterator<CastleEffect> lit = effects.iterator();
			while (lit.hasNext()) {
				CastleEffect e = lit.next();
				if (e.getEffTypeId().equals(effTypeId)) {
					castleEffectDao.deleteCastleEffect(e);

					lit.remove();

				}
			}
		}
	}

	@Override
	public void lockCastleEffect(long casId) {
		try {
			MemcachedManager.lock(CastleEffect.CASTLE_EFFECT_LOCKER_PREFIX
					+ casId, "castleeffect_");
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	@Override
	public int getActualTime(long casId, int time, String effectType) {
        //todo: 完成effect加成
		// double percent = 1.0d;
		// EffectValue value = this.getSumCastleEffectValueByEffectType(casId,
		// effectType);
		// if (value != null) {
		// percent = value.getDoublePerValue();
		// }
		// if (percent <= 0) {
		// percent = 1.0d;
		// }
		//
		// time = (int) (time * percent);
		// if (time < 2) {
		// time = 2;
		// }
		return time;
	}
}
