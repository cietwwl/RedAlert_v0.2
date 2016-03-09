package com.youxigu.dynasty2.hero.web;

import java.util.List;

import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.hero.domain.TroopGridView;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroInfo;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33001Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33003Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33005Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33007Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33009Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33011Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33013Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33002Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33004Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33006Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33008Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33010Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33012Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Response33014Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.TroopInfo;
import com.youxigu.dynasty2.hero.proto.HeroUpTroop;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 军团接口
 * 
 * @author Administrator
 * 
 */
public class TroopAction extends BaseAction {

	private ITroopService troopService;

	public void setTroopService(ITroopService troopService) {
		this.troopService = troopService;
	}

	/**
	 * 33001坦克上阵
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object upTroop(Object obj, Response context) {
		Request33001Define req = (Request33001Define) obj;
		UserSession us = super.getUserSession(context);
		if (!req.hasHeroId()) {
			throw new BaseException("武将不存在");
		}
		HeroUpTroop tp = null;
		if (req.getTroopId2() <= 0) {
			// 单个军团的格子操作,1在原来军团里面上阵一个新武将，2把原来军团里面上阵的武将，移动到新军团里面去
			tp = troopService.doUpTroop(us.getUserId(), req.getTroopId1(),
					req.getHeroId(), req.getTroopGridId1());
		} else {
			// 跨军团的格子交换 或者 在同一个军团里面的格子交换
			if (req.getTroopId1() == req.getTroopId2()) {// 同一军团里面的格子交换
				tp = troopService.doSwapSameTroopGrid(us.getUserId(),
						req.getTroopId1(), req.getTroopGridId1(),
						req.getTroopGridId2());
			} else {
				tp = troopService.doSwapTroopGrid(us.getUserId(),
						req.getTroopId1(), req.getTroopGridId1(),
						req.getTroopId2(), req.getTroopGridId2());
			}
		}

		Response33002Define.Builder res = Response33002Define.newBuilder();

		res.setTroopId1(tp.getTroopId1());
		res.setTroopId2(tp.getTroopId2());

		res.setTroopGridId1(tp.getTroopGridId1());
		res.setTroopGridId2(tp.getTroopGridId2());

		res.setPosition1(tp.getPosition1());
		res.setPosition2(tp.getPosition2());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33002);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();

	}

	/**
	 * 33007武将布阵。。单个军团里面的武将进行格子交换
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object swapTroopGrid(Object obj, Response context) {
		Request33007Define req = (Request33007Define) obj;
		UserSession us = super.getUserSession(context);

		troopService.doSwapSameTroopGrid(us.getUserId(), req.getTroopId(),
				req.getTroopGridId1(), req.getTroopGridId2());
		Response33008Define.Builder res = Response33008Define.newBuilder();
		res.setTroopId(req.getTroopId());
		res.setTroopGridId1(req.getTroopGridId1());
		res.setTroopGridId2(req.getTroopGridId2());

		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33008);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();

	}

	/**
	 * 军团列表 33003
	 *
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getTroopList(Object obj, Response context) {
		Request33003Define req = (Request33003Define) obj;
		UserSession us = super.getUserSession(context);
		List<Troop> troops = troopService.doRefreshTroop(us.getUserId());
		Response33004Define.Builder res = Response33004Define.newBuilder();
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33004);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		for (Troop t : troops) {
			res.addTroops(troopService.getTroopMap(t, us.getUserId()));
		}
		return res.build();
	}

	/**
	 * 获取某个军团的详细信息 33005(前台自己通过缓存处理，不掉后台 战力后面提供算法)
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getTroop(Object obj, Response context) {
		Request33005Define req = (Request33005Define) obj;
		UserSession us = super.getUserSession(context);
		long troopId = req.getTroopId();
		Troop troop = troopService.getTroopById(us.getUserId(), troopId);
		if (troop == null || troop.getUserId() != us.getUserId()) {
			throw new BaseException("不存在该军团");
		}
		TroopInfo info = troopService.getTroopMap(troop, us.getUserId());
		Response33006Define.Builder res = Response33006Define.newBuilder();
		res.setTroop(info);
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33006);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 33009获取格子里面的信息。。包括武将。。装备等.
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object getTroopGrid(Object obj, Response context) {
		Request33009Define req = (Request33009Define) obj;
		if (!req.hasTroopGridId()) {
			throw new BaseException("格子id不对");
		}
		UserSession us = super.getUserSession(context);
		TroopGridView vi = troopService.getTroopGrid(us.getUserId(),
				req.getTroopGridId());
		Response33010Define.Builder res = Response33010Define.newBuilder();
		for (long id : vi.getEquipId()) {
			res.addEquips(id);
		}
		res.setInfo((HeroInfo) vi.getHeroView().build());
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33010);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());
		return res.build();
	}

	/**
	 * 33011设置队长
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object setTeamLeader(Object obj, Response context) {
		Request33011Define req = (Request33011Define) obj;
		UserSession us = super.getUserSession(context);
		troopService.doSetTeamLeader(us.getUserId(), req.getTroopId(),
				req.getHeroId());
		Response33012Define.Builder res = Response33012Define.newBuilder();
		ResponseHead.Builder hd = ResponseHead.newBuilder();
		hd.setCmd(33012);
		hd.setRequestCmd(req.getCmd());
		res.setResponseHead(hd.build());

		res.setHeroId(req.getHeroId());
		res.setTroopId(req.getTroopId());

		return res.build();

	}

	/**
	 * 33013调整军团格子位置
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object saveTroopGrid(Object obj, Response context) {
		Request33013Define req = (Request33013Define) obj;
		UserSession us = super.getUserSession(context);
		Troop troop = troopService.doSaveTroopGrid(us.getUserId(),
				req.getTroopId(), req.getTroopGridIdsList());
		Response33014Define.Builder res = Response33014Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setTroopInfo(troopService.getTroopMap(troop, us.getUserId()));
		return res.build();

	}

}
