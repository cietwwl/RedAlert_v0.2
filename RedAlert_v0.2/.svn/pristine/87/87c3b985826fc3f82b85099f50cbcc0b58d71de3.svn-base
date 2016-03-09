package com.youxigu.dynasty2.user.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.user.dao.IUserEffectDao;
import com.youxigu.dynasty2.user.domain.UserEffect;
import com.youxigu.dynasty2.user.service.IUserEffectService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;

public class UserEffectService implements IUserEffectService {

	private IUserEffectDao userEffectDao;

	public void setUserEffectDao(IUserEffectDao userEffectDao) {
		this.userEffectDao = userEffectDao;
	}

	@Override
	public List<UserEffect> getUserEffectByEffTypeId(long userId,
			String effTypeId) {
		List<UserEffect> effects = this.getUserEffectByUserId(userId);
		List<UserEffect> subs = new ArrayList<UserEffect>();
		for (UserEffect e : effects) {
			if (effTypeId.equals(e.getEffTypeId())) {
				subs.add(e);
			}
		}
		return subs;
	}

	@Override
	public List<UserEffect> getUserEffectByEffTypeIdWithTimeout(long userId,
			String effTypeId) {
		List<UserEffect> effects = userEffectDao.getUserEffectByUserId(userId);
		List<UserEffect> subs = new ArrayList<UserEffect>();
		for (UserEffect e : effects) {
			if (effTypeId.equals(e.getEffTypeId())) {// 过滤特定类型的城市效果
				subs.add(e);
			}
		}
		return subs;
	}

	@Override
	public UserEffect getUserEffectByEffTypeId(long userId, int entId,
			String effTypeId) {
		List<UserEffect> effects = this.getUserEffectByUserId(userId);

		for (UserEffect e : effects) {
			if (e.getEntId() == entId && e.getEffTypeId().equals(effTypeId)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public List<UserEffect> getUserEffectByUserId(long userId) {
		List<UserEffect> effects = userEffectDao.getUserEffectByUserId(userId);

		if (effects != null) {
			Iterator<UserEffect> lit = effects.iterator();
			while (lit.hasNext()) {
				UserEffect e = lit.next();
				if (e.isExpired()) {
					// TODO：这里可能有并发的问题
					// userEffectDao中返回的list没有做copy,可能多个人用同一个list来遍历

					lit.remove();

					// userEffectDao.deleteUserEffect(e);
				}
			}
		}

		return effects;

	}

	@Override
	public EffectValue getSumUserEffectValueByEffectType(long userId,
			String effTypeId) {
		EffectValue value = new EffectValue();
		List<UserEffect> effects = this.getUserEffectByUserId(userId);
		for (UserEffect e : effects) {
			if (!e.isExpired() && effTypeId.equals(e.getEffTypeId())) {
				value.setAbsValue(value.getAbsValue() + e.getAbsValue());
				value.setPerValue(value.getPerValue() + e.getPerValue());
			}
		}
		return value;
	}

	@Override
	@Deprecated
	public void addUserEffect(long userId, Entity entity, EffectDefine effect) {
		// TODO:这里不对。需要重新做
		UserEffect ce = this.getUserEffectByEffTypeId(userId,
				entity.getEntId(), effect.getEffTypeId());
		if (ce == null) {
			ce = new UserEffect();
			ce.setUserId(userId);
			ce.setEntId(entity.getEntId());
			ce.setEffTypeId(effect.getEffTypeId());
			ce.setExpireDttm(null);
			ce.setPerValue(effect.getPara1());
			ce.setAbsValue(effect.getPara2());
			userEffectDao.createUserEffect(ce);
		} else {
			// 效果直接更新，不累加
			ce.setPerValue(effect.getPara1());
			ce.setAbsValue(effect.getPara2());
			ce.setExpireDttm(null);
			// ce.setPerValue(techEffect.getPara1());
			// ce.setAbsValue(techEffect.getPara2());
			userEffectDao.updateUserEffect(ce);
		}

	}

	@Override
	public void createUserEffect(UserEffect ue) {
		userEffectDao.createUserEffect(ue);

	}

	@Override
	public void updateUserEffect(UserEffect ue) {
		userEffectDao.updateUserEffect(ue);
	}

	@Override
	public void lockUserEffect(long userId) {
		try {
			MemcachedManager.lock(
					UserEffect.USER_EFFECT_LOCKER_PREFIX + userId,
					"usereffect_");
		} catch (TimeoutException e) {
			throw new BaseException(e);
		}
	}

	@Override
	public void removeTimeoutUserEffect(long userId) {
		lockUserEffect(userId);
		List<UserEffect> effects = userEffectDao.getUserEffectByUserId(userId);

		if (effects != null) {
			Iterator<UserEffect> lit = effects.iterator();
			while (lit.hasNext()) {
				UserEffect e = lit.next();
				if (e.isExpired()) {
					userEffectDao.deleteUserEffect(e);

					lit.remove();

				}
			}
		}

	}
}
