package com.youxigu.dynasty2.develop.web;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.develop.domain.CastleArmy;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20019Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20021Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20023Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20025Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Response20019Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Response20021Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Response20023Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Response20026Define;
import com.youxigu.dynasty2.develop.service.ICastleArmyService;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 维修工厂
 * 
 * @author fengfeng
 * 
 */
public class CastleBarrackAction extends BaseAction {
	private ICastleArmyService castleArmyService;
	private IHeroService heroService;
	private ICastleEffectService castleEffectService;
	private ICommonService commonService;

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setCastleArmyService(ICastleArmyService castleArmyService) {
		this.castleArmyService = castleArmyService;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	/**
	 * 维修工厂功能开关
	 * 
	 * @return
	 */
	private boolean isOpenCastleBarrack() {
		int opened = commonService.getSysParaIntValue(
				AppConstants.SYS_BARRACK_OPEN_STATUS,
				AppConstants.SYS_OPNE_STATUS_DEFAULTVALUE);
		return opened == 1;
	}

	/**
	 * 打开维修工厂界面-20019
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object viewBuiBarrack(Object obj, Response context) {
		if (!isOpenCastleBarrack()) {
			throw new BaseException("兵营功能暂未开放");
		}
		Request20019Define req = (Request20019Define) obj;
		// 输入
		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();
		long casId = us.getMainCasId();
		CastleArmy casArmy = castleArmyService.doRefreshAndGetCastleArmy(casId);
		Response20019Define.Builder res = Response20019Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setArmy(buildCastleArmy(casArmy, userId, casId));
		return res.build();
	}

	private com.youxigu.dynasty2.develop.proto.DevelopMsg.CastleArmy buildCastleArmy(
			CastleArmy casArmy, long userId, long casId) {
		com.youxigu.dynasty2.develop.proto.DevelopMsg.CastleArmy.Builder army = com.youxigu.dynasty2.develop.proto.DevelopMsg.CastleArmy
				.newBuilder();
		int all = casArmy.getNum() + heroService.getHeroArmyNum(userId);
		army.setAllNum(all);
		army.setArmyLimit(castleArmyService.getCastleArmyLimit(casId));
		army.setArmyNum(casArmy.getNum());
		int s = (int) ((casArmy.getRefreshCDTime() - System.currentTimeMillis()) / 1000);
		if (s <= 0) {
			s = 0;
		}
		army.setRefreshCD(s);
		army.setStatus(casArmy.getStatus());
		army.setBuildNum(casArmy.getBuildNum());
		return army.build();
	}

	/**
	 * 20021 生产零件
	 * 
	 * @param params
	 * @return
	 */
	public Object buiBarrackAddArmy(Object obj, Response context) {
		if (!isOpenCastleBarrack()) {
			throw new BaseException("兵营功能暂未开放");
		}
		Request20021Define req = (Request20021Define) obj;

		int num = req.getArmyNum();// 兵种数量
		if (num < 0 || num >= 100000000) {
			throw new BaseException("招兵数量不合法");
		}
		UserSession us = super.getUserSession(context);
		long casId = us.getMainCasId();
		long userId = us.getUserId();
		CastleArmy casArmy = castleArmyService.doCreateArmy(userId, casId, num);// 招募兵
		Response20021Define.Builder res = Response20021Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setArmy(buildCastleArmy(casArmy, userId, casId));
		return res.build();
	}

	/**
	 * 20023 高级零件兑换
	 * 
	 * @param params
	 * @return
	 */
	public Object exchangeItem(Object obj, Response context) {
		if (!isOpenCastleBarrack()) {
			throw new BaseException("兵营功能暂未开放");
		}
		Request20023Define req = (Request20023Define) obj;
		UserSession us = super.getUserSession(context);
		long casId = us.getMainCasId();
		long userId = us.getUserId();
		CastleArmy casArmy = castleArmyService.doExchangeItem(userId, casId);
		Response20023Define.Builder res = Response20023Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setArmy(buildCastleArmy(casArmy, userId, casId));
		return res.build();
	}

	/**
	 * 加速
	 * 
	 * @param params
	 * @return
	 */
	public Object speedUp(Object obj, Response context) {
		if (!isOpenCastleBarrack()) {
			throw new BaseException("兵营功能暂未开放");
		}
		Request20025Define req = (Request20025Define) obj;
		UserSession us = super.getUserSession(context);
		long casId = us.getMainCasId();
		long userId = us.getUserId();
		CastleArmy casArmy = castleArmyService.doSpeedUp(userId, casId,
				req.getDiamond(), req.getEntId(), req.getNum());
		Response20026Define.Builder res = Response20026Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setArmy(buildCastleArmy(casArmy, userId, casId));
		return res.build();
	}
}
