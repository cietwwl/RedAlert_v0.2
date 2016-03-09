package com.youxigu.dynasty2.hero.web;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.develop.domain.CastleArmy;
import com.youxigu.dynasty2.hero.domain.Hero;
import com.youxigu.dynasty2.hero.proto.EffectValueMsg;
import com.youxigu.dynasty2.hero.proto.HeroCardAndDebris;
import com.youxigu.dynasty2.hero.proto.HeroMsg.EffectValue;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31021Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31025Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31027Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31029Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31031Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31035Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31037Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31039Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31041Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31043Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33107Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31022Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31026Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31028Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31030Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31032Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31036Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31038Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31040Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31042Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response31044Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33108Define;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MathUtils;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class HeroAction extends BaseAction {
	private IHeroService heroService = null;

	/**
	 * [前台缓存]取得所有武将-31021
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object loadHeroViewByUserId(Object obj, Response context) {
		Request31021Define req = (Request31021Define) obj;
		// 输出
		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();
		List<Hero> tmpList = heroService.getUserHeroList(userId);
		Response31022Define.Builder res = Response31022Define.newBuilder();
		for (Hero h : tmpList) {
			res.addHeros(h.toView());
		}
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31022);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 武将升级-31025
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object heroLevelUp(Object obj, Response context) {
		Request31025Define req = (Request31025Define) obj;
		long heroId = req.getHeroId();
		UserSession us = super.getUserSession(context);
		heroService
				.doHeroLevelUp(us.getUserId(), heroId, req.getExpItemsList());
		Response31026Define.Builder res = Response31026Define.newBuilder();
		res.setSucc(true);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31026);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 武将进阶-31027
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object relifeHero(Object obj, Response context) {
		Request31027Define req = (Request31027Define) obj;
		// 输入
		long heroId = req.getHeroId();
		// 输出
		UserSession us = super.getUserSession(context);
		heroService.doRelifeHero(us.getUserId(), us.getMainCasId(), heroId);
		heroService.getHeroByHeroId(us.getUserId(), heroId);
		Response31028Define.Builder res = Response31028Define.newBuilder();
		res.setSucc(true);
		res.setHeroId(heroId);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31028);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 武将强化-31029
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doHeroStrength(Object obj, Response context) {
		Request31029Define req = (Request31029Define) obj;
		// 输入
		long heroId = req.getHeroId();
		UserSession us = super.getUserSession(context);
		heroService.doHeroStrength(us.getUserId(), heroId);
		Response31030Define.Builder res = Response31030Define.newBuilder();
		res.setSucc(true);
		res.setHeroId(heroId);

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31030);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 武将重生,退役-31031
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doHeroRebirth(Object obj, Response context) {

		Request31031Define req = (Request31031Define) obj;
		// 输入
		UserSession us = super.getUserSession(context);

		heroService.doHeroRebirth2(us.getUserId(), req.getHeroIdsList(),
				req.getRetire());

		Response31032Define.Builder res = Response31032Define.newBuilder();
		res.setSucc(true);
		res.setRetire(req.getRetire());
		for (long id : req.getHeroIdsList()) {
			res.addHeroIds(id);
		}
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31032);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 碎片兑换坦克-31035
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doHeroSoulComposite(Object obj, Response context) {
		Request31035Define req = (Request31035Define) obj;
		UserSession us = super.getUserSession(context);
		Map<String, Object> rt = heroService.doHeroSoulComposite(
				us.getUserId(), req.getHeroId());
		Response31036Define.Builder res = Response31036Define.newBuilder();

		res.setEntId(MathUtils.getInt(rt.remove("entId")));
		res.setType(MathUtils.getInt(rt.remove("type")));

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31036);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 分解坦克图纸-31037
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doHeroCardDiscard(Object obj, Response context) {
		Request31037Define req = (Request31037Define) obj;
		UserSession us = super.getUserSession(context);
		heroService.doHeroCardDiscard(us.getUserId(), req.getHeroIdsList());
		Response31038Define.Builder res = Response31038Define.newBuilder();
		for (Long id : req.getHeroIdsList()) {
			res.addHeroIds(id);
		}
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(31038);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 33107获取坦克碎片和坦克图纸信息(请求一次后客户端自行缓存.改变服务器主动推送)
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object doHeroCardAndDebris(Object obj, Response context) {
		Request33107Define req = (Request33107Define) obj;
		UserSession us = super.getUserSession(context);
		List<HeroCardAndDebris> rs = heroService.doHeroCardAndDebris(us
				.getUserId());
		Response33108Define.Builder res = Response33108Define.newBuilder();
		if (rs != null && !rs.isEmpty()) {
			for (HeroCardAndDebris t : rs) {
				com.youxigu.dynasty2.hero.proto.HeroMsg.HeroCardAndDebris.Builder b = com.youxigu.dynasty2.hero.proto.HeroMsg.HeroCardAndDebris
						.newBuilder();
				b.setCardNum(t.getCardNum());
				b.setDebrisNum(t.getDebrisNum());
				b.setHeroId(t.getHeroId());
				b.setSysHeroId(t.getSysHeroId());
				res.addCards(b.build());
			}
		}
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 坦克维修-31039
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object changeHeroArmy(Object obj, Response context) {
		Request31039Define req = (Request31039Define) obj;
		UserSession us = super.getUserSession(context);
		if (req.getHeroArmysCount() <= 0) {
			throw new BaseException("没有可用的坦克维修数据");
		}
		if (req.getStatus() != 0 && req.getStatus() != 1) {
			throw new BaseException("自动维修状态错误");
		}
		CastleArmy casArmy = heroService.doChangeHeroArmyNum(us.getUserId(),
				us.getMainCasId(), req.getHeroArmysList(), req.getStatus());
		Response31040Define.Builder res = Response31040Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setArmyNum(casArmy.getNum());// 空闲零件
		res.setSucc(true);
		return res.build();
	}

	/**
	 * 坦克一键维修-31041
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object autoHeroArmy(Object obj, Response context) {
		Request31041Define req = (Request31041Define) obj;
		UserSession us = super.getUserSession(context);
		if (req.getStatus() != 0 && req.getStatus() != 1) {
			throw new BaseException("自动维修状态错误");
		}
		CastleArmy casArmy = heroService.doAutoChangeHeroArmyNum(
				us.getUserId(), us.getMainCasId(), req.getStatus());
		Response31042Define.Builder res = Response31042Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setArmyNum(casArmy.getNum());// 空闲零件
		res.setSucc(true);
		return res.build();
	}

	/**
	 * 获取坦克的原始属性(客户端通过原始属性自己来计算属性)-31043
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getHeroEffectValue(Object obj, Response context) {
		Request31043Define req = (Request31043Define) obj;
		UserSession us = super.getUserSession(context);
		List<EffectValueMsg> evs = heroService.getHeroEffectValue(
				us.getUserId(), req.getHeroId());
		Response31044Define.Builder res = Response31044Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));

		res.setHeroId(req.getHeroId());
		for (EffectValueMsg ev : evs) {
			EffectValue.Builder e = EffectValue.newBuilder();
			e.setAbsValue(ev.getAbsValue());
			e.setPerValue(ev.getPerValue());
			e.setKey(ev.getKey());
			e.setIsPercent(ev.isPercent());
			res.addEffects(e.build());
		}
		return res.build();
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

}
