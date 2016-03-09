package com.youxigu.dynasty2.develop.service.impl;

import java.util.List;

import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.enumer.SpeedType;
import com.youxigu.dynasty2.develop.service.ISpeedService;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.domain.enumer.SpeedUpItemType;
import com.youxigu.dynasty2.entity.service.impl.EntityService;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

public class SpeedService implements ISpeedService {
	private ICommonService commonService;
	private ITreasuryService treasuryService;
	private EntityService entityService;
	private IUserService userService;

	@Override
	public int doSpeedUp(long userId, SpeedType speedType, int speedTime,
			boolean diamond, int entId, int num) {
		User user = userService.lockGetUser(userId);
		return doSpeedUp(user, speedType, speedTime, diamond, entId, num);
	}

	@Override
	public int doSpeedUp(User user, SpeedType speedType, int speedTime,
			boolean diamond, int entId, int num) {
		if (user == null) {
			throw new BaseException("用户对象为null");
		}
		if (speedTime < 0) {
			throw new BaseException("加速时间错误" + speedTime);
		}

		if (speedTime == 0) {
			return 0;
		}
		long userId = user.getUserId();
		if (diamond) {
			// 钻石加速
			int dia = speedType.getDiamondSeconds(commonService);
			int yb = (int) (speedTime / (dia));// 需要多少元宝
			if (yb < 0) {
				throw new BaseException("加速扣除元宝异常" + yb);
			}
			userService.doConsumeCash(user.getUserId(), yb,
					speedType.getDmlog());
			return speedTime;
		}
		Entity en = entityService.getEntity(entId);
		if (!(en instanceof Item)) {
			throw new BaseException("道具类型错误");
		}
		Item it = (Item) en;
		if (!it.getItemType().isSpeedUpItem()) {
			throw new BaseException("不是加速道具");
		}
		boolean b = speedType.supportSpeedUpItem(SpeedUpItemType.valueOf(it
				.getChildType()));
		if (!b) {
			throw new BaseException("不支持的加速道具" + it.getEntId());
		}
		// 道具加速
		treasuryService.lockTreasury(userId);
		int count = treasuryService.getTreasuryCountByEntId(userId, entId);
		if (count <= 0 || num > count) {
			throw new BaseException("加速道具不够");
		}

		List<EffectDefine> ef = en.getEffects();
		if (ef == null || ef.isEmpty()) {
			throw new BaseException("未配置道具效果");
		}
		// 扣除加速道具
		treasuryService.deleteItemFromTreasury(userId, entId, num, true,
				speedType.getLog());

		EffectDefine ed = ef.get(0);
		// 计算加速时间
		int e = num * ed.getPara2();// 单位秒
		return e;

	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

}
