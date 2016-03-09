package com.youxigu.dynasty2.risk.web;

import java.util.List;

import com.youxigu.dynasty2.chat.proto.CommonHead.ItemInfo;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;
import com.youxigu.dynasty2.risk.domain.OneRisk;
import com.youxigu.dynasty2.risk.domain.RiskParentScene;
import com.youxigu.dynasty2.risk.domain.UserRiskScene;
import com.youxigu.dynasty2.risk.enums.RiskType;
import com.youxigu.dynasty2.risk.proto.OneRiskInfo;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54001Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54003Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54005Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54007Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54009Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54011Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54013Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54015Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54001Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54003Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54005Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54007Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54009Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54011Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54013Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Response54015Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.RiskInfo;
import com.youxigu.dynasty2.risk.proto.RiskParentSceneInfo;
import com.youxigu.dynasty2.risk.service.IRiskService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

public class RiskAction extends BaseAction {
	private IRiskService riskService;
	private ICommonService commonService;

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public void setRiskService(IRiskService riskService) {
		this.riskService = riskService;
	}

	private boolean isOpen() {
		int open = commonService.getSysParaIntValue(
				AppConstants.SYS_RISK_COMBAT_OPEN_STATUS, 1);
		if (open != 1) {
			throw new BaseException("功能未开放");
		}
		return true;
	}

	/**
	 * 获取当前已经打到的关卡54013
	 * 
	 * @param obj
	 * @param context
	 * @return
	 */
	public Object getUserRiskScenes(Object obj, Response context) {
		isOpen();
		Request54013Define req = (Request54013Define) obj;
		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		if (!req.hasType()) {
			throw new BaseException("参数非法");
		}
		RiskType t = RiskType.valueOf(req.getType().getNumber());
		if (t == null) {
			throw new BaseException("参数错误" + req.getType().getNumber());
		}
		List<UserRiskScene> userScenes = riskService.getUserRiskScenes(userId,
				t);
		Response54013Define.Builder res = Response54013Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setType(req.getType());
		for (UserRiskScene u : userScenes) {
			res.addIds(u.getPid());
			res.addPasss(u.isPassAll());
			RiskParentSceneInfo userScene = riskService.doGetOneRiskScene(
					userId, u.getPid());
			if (userScene == null) {
				res.addStars(0);
			} else {
				res.addStars(userScene.getSumStar());
			}
		}

		return res.build();
	}

	/**
	 * 54001获取某个章节下的信息
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getOneRiskScene(Object obj, Response context) {
		isOpen();
		Request54001Define req = (Request54001Define) obj;
		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		if (!req.hasId()) {
			throw new BaseException("参数非法");
		}
		Response54001Define.Builder res = Response54001Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));

		RiskParentSceneInfo userScene = riskService.doGetOneRiskScene(userId,
				req.getId());
		RiskInfo.Builder info = RiskInfo.newBuilder();
		for (OneRisk rs : userScene.getInfos()) {
			OneRiskInfo io = new OneRiskInfo(rs.getId(), rs.getStar(),
					rs.getJoinNum(), rs.getFailNum(), rs.getRestNum(),
					rs.isFirstBonus());
			info.addInfos(io.toRiskSceneInfo());
		}

		info.setId(req.getId());
		info.setStarBonus(userScene.getStarBonus());
		info.setSumStar(userScene.getSumStar());
		info.setPass(userScene.isPass());
		res.setInfo(info.build());
		return res.build();
	}

	/**
	 * 54003挑战关卡
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doCombat(Object obj, Response context) {
		isOpen();
		Request54003Define req = (Request54003Define) obj;
		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		int id = req.getId();
		com.youxigu.dynasty2.risk.proto.RiskSceneInfo info = riskService
				.doCombat(userId, id, us.getQqFlag());
		Response54003Define.Builder res = Response54003Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setInfo(info.toRiskSceneInfo());
		res.setWin(info.isWin());
		res.setCombatId(info.getCombatId());
		res.setUserExp(info.getUserExp());
		for (DroppedEntity d : info.getItems()) {
			ItemInfo.Builder i = ItemInfo.newBuilder();
			i.setEntId(d.getEntId());
			i.setNum(d.getNum());
			res.addItems(i.build());
		}
		return res.build();
	}

	/**
	 * 54005扫荡关卡
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object doAutoCombat(Object obj, Response context) {
		isOpen();
		Request54005Define req = (Request54005Define) obj;
		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		boolean b = false;
		if (req.hasNum()) {
			if (req.getNum() == 10) {
				b = true;
			}
		}
		Response54005Define.Builder res = riskService.doAutoCombat(userId,
				req.getIdList(), b, us.getQqFlag());
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		return res.build();
	}

	/**
	 * 54007领取星级级奖励
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object gainStarAward(Object obj, Response context) {
		isOpen();
		Request54007Define req = (Request54007Define) obj;
		if (!req.hasStarType()) {
			throw new BaseException("领取星级奖励参数错误");
		}
		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		riskService.doGainStarAward(userId, req.getId(), (byte) req
				.getStarType().getNumber());
		Response54007Define.Builder res = Response54007Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setId(req.getId());
		res.setStarType(req.getStarType());
		return res.build();
	}

	/**
	 * 54009领取首通奖励
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object firstBonus(Object obj, Response context) {
		isOpen();
		Request54009Define req = (Request54009Define) obj;

		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		riskService.doFirstBonus(userId, req.getId());
		Response54009Define.Builder res = Response54009Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setId(req.getId());
		return res.build();
	}

	/**
	 * 54011重置次数
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object clearJoinNum(Object obj, Response context) {
		isOpen();
		Request54011Define req = (Request54011Define) obj;
		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		int num = riskService.doClearJoinNum(userId, req.getId());
		Response54011Define.Builder res = Response54011Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setId(req.getId());
		res.setRestNum(num);
		return res.build();
	}

	/**
	 * 54015 获取可以扫荡的点
	 * 
	 * @param params
	 * @param context
	 * @return
	 */
	public Object getQuickIds(Object obj, Response context) {
		isOpen();
		Request54015Define req = (Request54015Define) obj;
		if (!req.hasRiskId()) {
			throw new BaseException("参数错误");
		}

		UserSession us = getUserSession(context);
		long userId = us.getUserId();
		// List<UserRiskScene> userScenes =
		// riskService.getUserRiskScenes(userId,
		// req.getRiskId());
		List<RiskParentScene> rps = riskService.getRiskParentSceneByRiskId(req
				.getRiskId());

		Response54015Define.Builder res = Response54015Define.newBuilder();
		res.setResponseHead(super.getResponseHead(req.getCmd()));
		res.setRiskId(req.getRiskId());

		for (RiskParentScene u : rps) {
			// res.addIds(u.getPid());
			// res.addPasss(u.isPassAll());
			RiskParentSceneInfo userScene = riskService.doGetOneRiskScene(
					userId, u.getId());
			for (OneRisk rs : userScene.getInfos()) {
				if (!rs.canAutoCombat()) {
					break;
				}
				res.addIds(rs.getId());
			}
		}
		return res.build();
	}
}
