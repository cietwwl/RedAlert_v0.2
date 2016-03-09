package com.youxigu.dynasty2.develop.service.impl;

import java.sql.Timestamp;
import java.util.concurrent.TimeoutException;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.dao.ICastleArmyDao;
import com.youxigu.dynasty2.develop.domain.CastleArmy;
import com.youxigu.dynasty2.develop.enumer.SpeedType;
import com.youxigu.dynasty2.develop.service.ICastleArmyService;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ISpeedService;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;

public class CastleArmyService implements ICastleArmyService {
	private ICastleArmyDao castleArmyDao;
	private ICommonService commonService;
	private ICastleEffectService castleEffectService;
	private IHeroService heroService;
	private ITreasuryService treasuryService;
	private ICastleResService castleResService;
	private ISpeedService speedService;

	@Override
	public CastleArmy lockGetCastleArmy(long casId) {
		this.lockCastleArmy(casId);
		return this.getCastleArmy(casId);
	}

	@Override
	public void lockCastleArmy(long casId) {
		try {
			MemcachedManager.lockClass(CastleArmy.class, casId);
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
	}

	@Override
	public CastleArmy doRefreshAndGetCastleArmy(long casId) {
		CastleArmy casArmy = this.lockGetCastleArmy(casId);
		if (casArmy == null) {
			CastleArmy castleArmy = new CastleArmy(casId, 0, 0, null);
			castleArmyDao.createCastleArmy(castleArmy);
		} else {
			if (casArmy.hasBuildIng()) {
				long now = System.currentTimeMillis();
				if (now >= casArmy.getRefreshCDTime()) {
					// 生产结束了
					buildFinsh(casArmy);
				}
			}
		}
		return casArmy;

	}

	/**
	 * 零件生产结束
	 * 
	 * @param casArmy
	 */
	private void buildFinsh(CastleArmy casArmy) {
		casArmy.setRefreshCD(null);
		casArmy.setNum(casArmy.getNum() + casArmy.getBuildNum());
		casArmy.setBuildNum(0);
		castleArmyDao.updateCastleArmy(casArmy);
	}

	@Override
	public CastleArmy getCastleArmy(long casId) {
		CastleArmy castleArmy = castleArmyDao.getCastleArmy(casId);
		if (castleArmy == null) {
			castleArmy = new CastleArmy(casId, 0, 0, null);
			castleArmyDao.createCastleArmy(castleArmy);
		}
		return castleArmy;
	}

	@Override
	public CastleArmy doCreateArmy(long userId, long casId, int num) {
		CastleArmy casArmy = this.doRefreshAndGetCastleArmy(casId);
		castleResService.lockCasRes(casId);

		// 武将身上的总兵数
		int hasArmy = heroService.getHeroArmyNum(userId) + casArmy.getNum();
		// 城池的兵数上限
		int max = getCastleArmyLimit(casId);

		if (hasArmy + num > max) {
			throw new BaseException("您的兵数已经达到上限.");
		}
		// 判断是否有正在生产的
		if (casArmy.hasBuildIng()) {
			throw new BaseException("当前正在生产.");
		}
		int i = commonService.getSysParaIntValue(
				AppConstants.SYS_BARRACK_ONE_ARMY_OIL_NUM, 0);
		castleResService.doDelRes(casId, AppConstants.ENT_RES_OIL, i * num * 1l, true, true);

		long now = System.currentTimeMillis();
		int sc = commonService.getSysParaIntValue(
				AppConstants.SYS_BARRACK_ONE_ARMY, 30);
		long s = sc * 1000l * num + now;
		casArmy.setRefreshCD(new Timestamp(s));
		casArmy.setBuildNum(num);
		castleArmyDao.updateCastleArmy(casArmy);
		return casArmy;

	}

	@Override
	public void doUpdateCasArmy(CastleArmy casArmy) {
		castleArmyDao.updateCastleArmy(casArmy);
	}

	@Override
	public int getCastleArmyLimit(long casId) {
		int max = 0;// 城池的兵数上限
		EffectValue ef = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_NEWARMY_LIMIT);
		if (ef != null) {
			max = ef.getAbsValue();
		}
		return max;
	}

	@Override
	public CastleArmy doExchangeItem(long userId, long casId) {
		int itemId = commonService.getSysParaIntValue(
				AppConstants.SYS_BARRACK_ONE_ARMY_ITEM, 0);
		if (itemId < 0) {
			throw new BaseException("高级零件错误");
		}
		treasuryService.lockTreasury(userId);
		int count = treasuryService.getTreasuryCountByEntId(userId, itemId);
		if (count <= 0) {
			throw new BaseException("高级零件不足");
		}
		CastleArmy casArmy = this.lockGetCastleArmy(casId);
		// 武将身上的总兵数
		int hasArmy = heroService.getHeroArmyNum(userId) + casArmy.getNum();
		// 城池的兵数上限
		int max = getCastleArmyLimit(casId);
		if (hasArmy >= max) {
			throw new BaseException("您的兵数已经达到上限.");
		}
		int c = commonService.getSysParaIntValue(
				AppConstants.SYS_BARRACK_ONE_ARMY_EXCHANGE_COUNT, 1);
		int num = 1;
		treasuryService.deleteItemFromTreasury(userId, itemId, num, true,
				LogItemAct.LOGITEMACT_18);
		casArmy.setNum(casArmy.getNum() + (num * c));
		castleArmyDao.updateCastleArmy(casArmy);
		return casArmy;
	}

	@Override
	public CastleArmy doSpeedUp(long userId, long casId, boolean diamond,
			int entId, int num) {
		CastleArmy casArmy = this.lockGetCastleArmy(casId);
		casArmy = doRefreshAndGetCastleArmy(casId);
		if (!casArmy.hasBuildIng()) {
			throw new BaseException("没有生产零件");
		}

		if (casArmy.getRefreshCD() == null) {
			throw new BaseException("生产完成,不需要加速");
		}
		long now = System.currentTimeMillis();
		long speedTime = casArmy.getRefreshCDTime() - now;
		int t = speedService.doSpeedUp(userId, SpeedType.BUILD_PART_SPEED_UP,
				(int) (speedTime / 1000), diamond, entId, num);
		// 计算加速时间
		int e = t * 1000;// 单位秒
		if ((now + e) >= casArmy.getRefreshCDTime()) {
			// 生产结束
			buildFinsh(casArmy);
		} else {
			// 还有部分倒计时
			long rt = casArmy.getRefreshCDTime() - e;
			casArmy.setRefreshCD(new Timestamp(rt));
			doUpdateCasArmy(casArmy);
		}
		return casArmy;
	}

	@Override
	public CastleArmy doUpdateCasArmyNum(long casId, int num) {
		if (num < 0) {
			throw new BaseException("坦克退回的零件数量错误" + num);
		}
		CastleArmy casArmy = lockGetCastleArmy(casId);
		if (num == 0) {
			return casArmy;
		}
		casArmy.setNum(casArmy.getNum() + num);
		castleArmyDao.updateCastleArmy(casArmy);
		return casArmy;
	}

	public void setCastleArmyDao(ICastleArmyDao castleArmyDao) {
		this.castleArmyDao = castleArmyDao;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setSpeedService(ISpeedService speedService) {
		this.speedService = speedService;
	}

}
