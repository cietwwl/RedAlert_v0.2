package com.youxigu.dynasty2.hero.web;

import java.sql.Timestamp;
import java.util.Map;

import com.youxigu.dynasty2.chat.proto.CommonHead.ItemInfo;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.hero.domain.HeroPub;
import com.youxigu.dynasty2.hero.domain.UserPubAttr;
import com.youxigu.dynasty2.hero.enums.HeroDrawType;
import com.youxigu.dynasty2.hero.proto.HeroDrawInfo;
import com.youxigu.dynasty2.hero.proto.HeroDrawItemInfo;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroPubInfo;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33101Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33103Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33102Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33104Define;
import com.youxigu.dynasty2.hero.service.IHeroDrawService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.vip.domain.UserVip;
import com.youxigu.dynasty2.vip.service.IVipService;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class HeroDrawAction extends BaseAction {
	private IHeroDrawService heroDrawService = null;
	private ICommonService commonService;
	private IVipService vipService = null;

	/**
	 * 酒馆视图-33101
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object showBar(Object obj, Response context) {
		Request33101Define req = (Request33101Define) obj;
		long now = System.currentTimeMillis();
		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();
		UserPubAttr userPubAttr = heroDrawService.getUserPubAttrById(userId);
		Map<HeroDrawType, Map<Short, HeroPub>> pubMaps = heroDrawService
				.getHeroPubMaps();
		Response33102Define.Builder res = Response33102Define.newBuilder();

		short vipLv = 0;
		UserVip userVip = vipService.getUserVip(userId);
		if (userVip != null) {
			if (!userVip.isExpire()) {
				vipLv = (short) userVip.getVipLv();
			}
		}

		HeroPubInfo.Builder info1 = HeroPubInfo.newBuilder();
		HeroPubInfo.Builder info2 = HeroPubInfo.newBuilder();
		// HeroPubInfo.Builder info3 = HeroPubInfo.newBuilder();

		HeroPub pub1 = pubMaps.get(HeroDrawType.HERO_DRAW_TYPE1).get(vipLv);
		HeroPub pub2 = pubMaps.get(HeroDrawType.HERO_DRAW_TYPE2).get(vipLv);
		// HeroPub pub3 = pubMaps.get(HeroDrawType.HERO_DRAW_TYPE3).get(vipLv);
		int num1 = 0;
		int num2 = 0;
		// int num3 = 0;
		if (userPubAttr != null) {
			num1 = userPubAttr.getHireNum1();

			Timestamp dttm = userPubAttr.getHireCD1();
			if (dttm != null) {
				long cd1 = dttm.getTime() - now;
				if (cd1 > 0) {
					info1.setCd(cd1 / 1000L + 1);
					// info1.setPeriod((long) pub1.getFreePeriod());
				}
			}

			num2 = userPubAttr.getHireNum2();

			dttm = userPubAttr.getHireCD2();
			if (dttm != null) {
				long cd1 = dttm.getTime() - now;
				if (cd1 > 0) {
					info2.setCd(cd1 / 1000L + 1);
					// info2.setPeriod((long) pub2.getFreePeriod());
				}
			}

			// num3 = userPubAttr.getHireNum3();
			//
			// dttm = userPubAttr.getHireCD3();
			// if (dttm != null) {
			// long cd1 = dttm.getTime() - now;
			// if (cd1 > 0) {
			// info3.setCd(cd1 / 1000L + 1);
			// // info3.setPeriod((long) pub3.getFreePeriod());
			// }
			// }
		}
		info1.setType(HeroDrawType.HERO_DRAW_TYPE1.getIndex());
		info2.setType(HeroDrawType.HERO_DRAW_TYPE2.getIndex());
		// info3.setType(HeroDrawType.HERO_DRAW_TYPE3.getIndex());

		info1.setNum(num1);
		info2.setNum(num2);
		// info3.setNum(num3);

		info1.setLuck(pub1.getNextLuckNum(num1 + 1));
		info2.setLuck(pub2.getNextLuckNum(num2 + 1));
		// info3.setLuck(pub3.getNextLuckNum(num3 + 1));

		res.addPubs(info1);
		res.addPubs(info2);
		// res.addPubs(info3);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33102);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 酒馆中招募武将-33103
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object buiBarHireHero(Object obj, Response context) {
		// int opened = commonService.getSysParaIntValue(
		// AppConstants.SYS_HERO_HIRE_OPEN_STATUS, 1);
		// if (opened == 0) {
		// throw new BaseException("功能未开放");
		// }
		Request33103Define request = (Request33103Define) obj;
		// 输入
		int type = request.getType();// 原样返回
		HeroDrawType dtype = HeroDrawType.valueOf(type);
		if (dtype == null) {
			throw new BaseException("抽奖类型错误");
		}
		boolean ten = request.getTen();
		UserSession us = super.getUserSession(context);

		long userId = us.getUserId();
		HeroDrawInfo br = null;
		if (ten) {
			br = heroDrawService.doHireHero10(userId, dtype);
		} else {
			br = heroDrawService.doHireHero(userId, dtype);
		}
		Response33104Define.Builder res = Response33104Define.newBuilder();
		res.setTen(br.isTen());

		HeroPubInfo.Builder info = HeroPubInfo.newBuilder();
		info.setLuck(br.getLuck());
		info.setType(br.getType().getIndex());
		info.setCd(br.getCd());
		info.setNum(br.getNum());
		for (HeroDrawItemInfo d : br.getDrops()) {
			com.youxigu.dynasty2.hero.proto.HeroMsg.HeroDrawInfo.Builder res1 = com.youxigu.dynasty2.hero.proto.HeroMsg.HeroDrawInfo
					.newBuilder();
			ItemInfo.Builder dp = ItemInfo.newBuilder();
			dp.setEntId(d.getDrops().getEntId());
			dp.setNum(d.getDrops().getNum());

			res1.setItem(dp.build());
			res1.setHeroId(d.getHeroId());
			res1.setType(d.getType());

			res.addDrops(res1.build());
		}
		res.setPub(info.build());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33104);
		hd.setRequestCmd(request.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	public void setHeroDrawService(IHeroDrawService heroDrawService) {
		this.heroDrawService = heroDrawService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setVipService(IVipService vipService) {
		this.vipService = vipService;
	}

}
