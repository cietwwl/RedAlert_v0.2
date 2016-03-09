package com.youxigu.dynasty2.hero.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.proto.EquipCardAndDebris;
import com.youxigu.dynasty2.hero.proto.EquipInfoMsg;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33111Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33113Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33115Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33117Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33119Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33121Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33123Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33125Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33127Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33131Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33112Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33114Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33116Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33118Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33120Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33122Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33124Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33126Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33127Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33131Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.SellEquipCardAndDebriInfo;
import com.youxigu.dynasty2.hero.service.IHeroEquipService;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.TreasuryEvent;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class HeroEquipAction extends BaseAction {
	private IHeroEquipService heroEquipService;

	/**
	 * 换装备-33111
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object takeEquip(Object obj, Response context) {
		// 输入
		Request33111Define req = (Request33111Define) obj;
		long heroId = req.getHeroId();// 武将id
		// 输出
		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();

		long treasuryId = req.getTreasuryId();
		if (treasuryId > 0) {// 穿单件装备
			heroEquipService.doTakeEquip(userId, heroId, treasuryId);
		} else {
			// 自动换装
			heroEquipService.doAutoTakeAllEquip(userId, heroId);
		}
		Response33112Define.Builder res = Response33112Define.newBuilder();
		res.setHeroId(heroId);
		res.setSucc(true);
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 脱装备-33113
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object takeOffEquip(Object obj, Response context) {
		Request33113Define req = (Request33113Define) obj;
		// 输入
		long heroId = req.getHeroId();// 武将id

		// 输出
		UserSession us = super.getUserSession(context);

		long userId = us.getUserId();

		long treasuryId = req.getTreasuryId();
		Hero hero = null;
		if (treasuryId > 0) {// 脱单件装备
			hero = heroEquipService.doTakeOffEquip(userId, heroId, treasuryId);
		} else {
			// 脱全部装备
			hero = heroEquipService.doTakeOffAllEquip(userId, heroId);
		}
		Response33114Define.Builder res = Response33114Define.newBuilder();
		res.setInfo(hero.toView());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33114);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 装备强化 33115
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object equipLvUp(Object obj, Response context) {
		Request33115Define req = (Request33115Define) obj;
		UserSession session = getUserSession(context);
		long id = req.getTId();
		int num = 1;
		if (req.hasNum()) {
			num = req.getNum();
			if (num != 1 && num != 10) {
				throw new BaseException("次数不合法");
			}
		}
		EquipInfoMsg msg = heroEquipService.doEquipLevelup(session.getUserId(),
				id, num);
		Response33116Define.Builder res = Response33116Define.newBuilder();
		res.setDesc(msg.getDesc());
		res.setSucc(msg.isSucc());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33116);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 武将装备合成 33117
	 * 
	 * @return
	 */
	public Object equipCompose(Object obj, Response context) {
		Request33117Define req = (Request33117Define) obj;
		UserSession session = getUserSession(context);
		heroEquipService.doEquipCompose(session.getUserId(), req.getEntId());

		Response33118Define.Builder res = Response33118Define.newBuilder();
		res.setSucc(true);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33118);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 装备分解 33119
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object equipDestroy(Object obj, Response context) {
		Request33119Define req = (Request33119Define) obj;
		UserSession session = getUserSession(context);
		heroEquipService.doEquipDestroy(session.getUserId(), req.getTIdsList());
		Response33120Define.Builder res = Response33120Define.newBuilder();
		res.setSucc(true);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33120);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 装备回炉 33121
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object equipRebirth(Object obj, Response context) {
		Request33121Define req = (Request33121Define) obj;
		UserSession session = getUserSession(context);
		heroEquipService.doEquipRebirth(session.getUserId(), req.getTIdsList());
		Response33122Define.Builder res = Response33122Define.newBuilder();
		res.setSucc(true);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33122);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 背包装备加锁33123
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object equipLock(Object obj, Response context) {
		Request33123Define req = (Request33123Define) obj;
		UserSession session = getUserSession(context);
		long userId = session.getUserId();
		heroEquipService.doEquipLock(userId, req.getTId(), req.getLock());
		Response33124Define.Builder res = Response33124Define.newBuilder();
		res.setSucc(true);
		res.setTId(req.getTId());
		res.setLock(req.getLock());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33124);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 装备生产 33125
	 * 
	 * @return
	 */
	public Object equipBuild(Object obj, Response context) {
		Request33125Define req = (Request33125Define) obj;
		UserSession session = getUserSession(context);

		Treasury tr = heroEquipService.doEquipBuild(session.getUserId(),
				req.getEntId());
		TreasuryEvent view = tr.getView().convetMessage();
		Response33126Define.Builder res = Response33126Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setSucc(true);
		res.setEntId(req.getEntId());
		res.setInfo(view.getEquipInfo());
		return res.build();
	}

	/**
	 * 获取装备图纸和碎片信息 请求一次。 客户端缓存
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public Object equipCardAndDebris(Object obj, Response context) {
		Request33127Define req = (Request33127Define) obj;
		UserSession session = getUserSession(context);
		Map<String, Object> map = heroEquipService.equipCardAndDebris(session
				.getUserId());
		Map<Integer, Integer> cards = (Map<Integer, Integer>) map.get("cards");
		Map<Integer, Integer> debris = (Map<Integer, Integer>) map
				.get("debris");
		Response33127Define.Builder res = Response33127Define.newBuilder();

		for (Entry<Integer, Integer> en : cards.entrySet()) {
			res.addCards(new EquipCardAndDebris(en.getKey(), en.getValue())
					.toMsg());
		}

		for (Entry<Integer, Integer> en : debris.entrySet()) {
			res.addDebris(new EquipCardAndDebris(en.getKey(), en.getValue())
					.toMsg());
		}
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 出售装备图纸和碎片 33131
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object sellItem(Object obj, Response context) {
		UserSession session = getUserSession(context);

		Request33131Define req = (Request33131Define) obj;
		long userId = session.getUserId();
		Map<Integer, Integer> items = new HashMap<Integer, Integer>();
		for (SellEquipCardAndDebriInfo sell : req.getInfosList()) {
			Integer c = items.get(sell.getEntId());
			if (c == null) {
				c = 0;
			}
			c += sell.getNum();
			items.put(sell.getEntId(), c);
		}
		if (items.isEmpty()) {
			throw new BaseException("出售物品为null");
		}
		heroEquipService.doSellItem(userId, items);

		Response33131Define.Builder res = Response33131Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));

		for (SellEquipCardAndDebriInfo sl : req.getInfosList()) {
			res.addInfos(sl);
		}
		return res.build();
	}

	public void setHeroEquipService(IHeroEquipService heroEquipService) {
		this.heroEquipService = heroEquipService;
	}

}
