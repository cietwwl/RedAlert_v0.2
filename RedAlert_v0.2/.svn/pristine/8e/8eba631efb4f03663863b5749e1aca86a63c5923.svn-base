package com.youxigu.dynasty2.develop.web;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.core.flex.amf.BaseAction;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20027Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20029Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Response20027Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Response20029Define;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.IFlushCastleService;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.UserSession;

/**
 * 刷新城池-资源建筑
 * @author Dagangzi
 *
 */
public class FlushCastleAction extends BaseAction {
	public static final Logger log = LoggerFactory.getLogger(FlushCastleAction.class);
	private IUserService userService;
	private IFlushCastleService flushCastleService;
	private ICastleEffectService castleEffectService;
	private ICastleResService castleResService;
	private ILogService tlogService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setFlushCastleService(IFlushCastleService flushCastleService) {
		this.flushCastleService = flushCastleService;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setTlogService(ILogService tlogService) {
		this.tlogService = tlogService;
	}

	/**
	 * 客户端调用刷新-20027 这里可能需要修改，有可能成为性能的瓶颈
	 * @param params
	 * @param context
	 */
	public Object freshCastleData(Object params, Response context) {
		//传入值
		Request20027Define request = (Request20027Define) params;

		UserSession us = super.getUserSession(context);
		long userId = us.getUserId();
		long casId = us.getMainCasId();
		// TODO 添加需要刷新后台数据
		User user = userService.getUserById(userId);
		if (user == null) {
			throw new BaseException("君主不存在。" + "accId=" + us.getAccId());
		}

		Object login = us.removeAttribute("login");

		//计算15分钟刷新的内容，或是0点刷新的内容
		Map<String, Object> result = flushCastleService.updateCastleDevelopData(user, login != null);

		user = userService.getUserById(userId);

		Castle cas = (Castle) result.get("cas");
		if (cas == null) {
			throw new BaseException("城池不存在。" + "accId=" + us.getAccId());
		}

		// 下一次15分钟刷新的时间
		Timestamp nextCasDttm = UtilDate.moveSecond(cas.getQuarCalcuDttm(), AppConstants.CAS_FRESH_QUARCALCU_GAP);
		if (log.isDebugEnabled()) {
			log.debug(user.getUserName() + "的下一次15分钟刷新的时间是" + nextCasDttm);
		}

		Timestamp now = (Timestamp) result.get("now");
		long nowTime = now.getTime();

		// 计算下次刷新剩余的秒数
		int disScecond = 0;

		long nextTime = nextCasDttm.getTime();
		long next24 = nowTime - DateUtils.getMillisecondInADay(nowTime) + 24 * 3600 * 1000L;

		if (nextTime > next24) {// 下次刷新的时间和24点比较一个小的
			disScecond = (int) ((next24 - nowTime) / 1000) + 1;
		} else {
			disScecond = (int) ((nextTime - nowTime) / 1000) + 1;
		}

		if (disScecond < 60) {// 至少60秒刷新
			disScecond = 60;
		} else if (disScecond > 3600) {// 最多不能超过1小时
			disScecond = 3600;
		}

		//返回值
		Response20027Define.Builder response = Response20027Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		response.setRemainSecond(disScecond);// 客户端下次调用剩余的秒数
		response.setCurTime(System.currentTimeMillis());//后台系统时间
		if (log.isDebugEnabled()) {
			log.debug(cas.getCasName() + "城池在" + UtilDate.moveSecond(disScecond) + "刷新内政");
		}

		List<CastleEffect> effects = castleEffectService.getCastleEffectByCasId(casId);
		// 资源相关
		response.setGoldCap(castleResService.getGoldCapacity(casId, effects)); //金矿存储上限
		response.setIronCap(castleResService.getIronCapacity(casId, effects)); //铁矿存储上限
		response.setOilCap(castleResService.getOilCapacity(cas.getCasId(), effects)); //油矿存储上限
		response.setUraniumCap(castleResService.getUraniumCapacity(cas.getCasId(), effects)); //铀矿存储上限

		response.setGoldProMax(castleResService.getGoldProduceLimit(casId, effects)); //金矿产量上限
		response.setIronProMax(castleResService.getIronProduceLimit(casId, effects)); //铁矿产量上限
		response.setOilProduceMax(castleResService.getOilProduceLimit(casId, effects)); //油矿产量上限
		response.setUraniumProMax(castleResService.getUraniumProduceLimit(casId, effects)); //铀矿产量上限

		response.setGoldProNum(castleResService.getGoldProducePerQuarter(userId, casId, effects)); // 金矿每15分钟产量
		response.setIronProcNum(castleResService.getIronProducePerQuarter(userId, casId, effects)); // 铁矿每15分钟产量
		response.setOilProcNum(castleResService.getOilProducePerQuarter(userId, casId, effects)); // 油矿每15分钟产量
		response.setUraniumProcNum(castleResService.getUraniumProducePerQuarter(userId, casId, effects)); // 铀矿每15分钟产量

		CastleResource casRes = (CastleResource) result.get("casRes");
		if (casRes == null) {
			casRes = castleResService.getCastleResourceById(cas.getCasId());
		}
		response.setGoldNum(casRes.getGoldNum());// 金矿数
		response.setIronNum(casRes.getIronNum());// 铁矿数
		response.setOilNum(casRes.getOilNum());// 油矿数量
		response.setUraniumNum(casRes.getUranium());// 铀矿数量

		response.setCasGold(casRes.getCasGoldNum());// 建筑内金矿数
		response.setCasIron(casRes.getCasIronNum());// 建筑内铁矿数
		response.setCasOil(casRes.getCasOilNum());// 建筑内油矿数量
		response.setCasUranium(casRes.getCasUranium());// 建筑内铀矿数量

		// 是否自动建造
		response.setAutoBuild(cas.getAutoBuild());// 是否自动建造 0：不自动 1：自动

		response.setProtectGold(castleResService.getGoldRobLimit(casId, effects));//受保护的金矿数(仓库效果)
		response.setProtectIron(castleResService.getIronRobLimit(casId, effects));//受保护的铁矿数(仓库效果)
		response.setProtectOil(castleResService.getOilRobLimit(casId, effects));//受保护的油矿数(仓库效果)
		response.setProtectUranium(castleResService.getUraniumRobLimit(casId, effects));//受保护的铀矿数(仓库效果)
		
		
		//体力行动力
		response.setHpPoint(user.getHpPoint());
		response.setCurActPoint(user.getCurActPoint());

		// /tlog
		if (login != null) {
			int userLevel = user.getUsrLv();
			int balance = user.getCash();
			if (userLevel > 1) {
				int moneyios = 0;
				int moneyandroid = 0;
				int diamondios = 0;
				int diamondandroid = 0;

				int foodandroid = 0;
				int foodios = 0;
				int brzoneandroid = 0;
				int brzoneios = 0;

				if (Constant.isAndroid()) {
					moneyandroid = (int) casRes.getGoldNum();
					diamondandroid = balance;
					foodandroid = (int)casRes.getIronNum();
					brzoneandroid = (int)casRes.getOilNum();

				} else {
					moneyios = (int) casRes.getGoldNum();;
					diamondios = balance;
					foodios = (int)casRes.getIronNum();
					brzoneios = (int)casRes.getOilNum();

				}
				int iFriends = 0/*friendService.getFirendCount(userId)*/;

				// update tb_qxsy_roleinfo set
				// level=?,iFriends=?,moneyios=?,moneyandroid=?,diamondios=?,diamondandroid=?,foodios=?,foodandroid=?,brzoneios=?,brzoneandroid=?
				// where openid=? and zoneid=?
				tlogService.logDB(new Object[] { LogTypeConst.SQL_UPDATE_roleinfo, userLevel, iFriends, moneyios,
						moneyandroid, diamondios, diamondandroid, foodios, foodandroid, brzoneios, brzoneandroid,
						us.getOpenid(), us.getAreaId() });
			}
		}
		return response.build();
	}

	/**
	 * 收获资源资源建筑-20029
	 * @param params
	 * @param context
	 * @return
	 */
	public Object gainRes(Object params, Response context) {
		//传入值
		Request20029Define request = (Request20029Define) params;
		UserSession us = super.getUserSession(context);
		int type = request.getType();
		CastleResource casRes = null;
		long curNum = 0;//已收取的资源
		long casNum = 0;//未收取的资源
		switch (type) {
		case 1:
			casRes = castleResService.doGainGold(us.getMainCasId(), us.getUserId());
			curNum = casRes.getGoldNum();
			casNum = casRes.getCasGoldNum();
			break;
		case 2:
			casRes = castleResService.doGainIron(us.getMainCasId(), us.getUserId());
			curNum = casRes.getIronNum();
			casNum = casRes.getCasIronNum();
			break;
		case 3:
			casRes = castleResService.doGainOil(us.getMainCasId(), us.getUserId());
			curNum = casRes.getOilNum();
			casNum = casRes.getCasOilNum();
			break;
		case 4:
			casRes = castleResService.doGainUranium(us.getMainCasId(), us.getUserId());
			curNum = casRes.getUranium();
			casNum = casRes.getCasUranium();
			break;
		default:
			throw new BaseException("资源类型不存在");
		}
		
		//返回值
		Response20029Define.Builder response = Response20029Define.newBuilder();
		response.setResponseHead(super.getResponseHead(request.getCmd()));
		response.setType(type);
		response.setCasNum(curNum);
		response.setCasNum(casNum);
		return response.build();
	}
}
