package com.youxigu.dynasty2.develop.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.dao.ICastleDao;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.develop.service.IFlushCastleService;
import com.youxigu.dynasty2.develop.service.listener.CastleEffMessage;
import com.youxigu.dynasty2.hero.service.ITroopService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserEffectService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;

public class FlushCastleService implements IFlushCastleService {
	private ICastleDao castleDao;
	private ICastleService castleService;
	private ICastleEffectService castleEffectService;
	private IUserEffectService userEffectService;
	private ICastleResService castleResService;
	private ITroopService troopService;
	private IChatClientService messageService;
	private IUserService userService;
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setCastleDao(ICastleDao castleDao) {
		this.castleDao = castleDao;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setUserEffectService(IUserEffectService userEffectService) {
		this.userEffectService = userEffectService;
	}

	public void setCastleResService(ICastleResService castleResService) {
		this.castleResService = castleResService;
	}

	public void setTroopService(ITroopService troopService) {
		this.troopService = troopService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	@Override
	public Map<String, Object> updateCastleDevelopData(User user, boolean login) {
		Castle castle = castleService.lockAndGetCastle(user.getMainCastleId());
		if (castle == null) {
			throw new BaseException("城池不存在");
		}
		return updateCastleDevelopData(user, castle, login, false);
	}

	@Override
	public Map<String, Object> updateCastleDevelopData(User user, Castle castle, boolean login, boolean background) {
		// 返回部分被修改的对象
		long userId = user.getUserId();
		Map<String, Object> result = new HashMap<String, Object>();

		// 当前时间
		long nowTime = System.currentTimeMillis();
		Timestamp now = new Timestamp(nowTime);
		result.put("now", now);// 当前时间

		if (castle.getStatus() == Castle.STATUS_OVER) {
			result.put("cas", castle);// 城池
			return result;
		}

		Timestamp lastQuarCalcuDttm = castle.getQuarCalcuDttm();// 15分钟修改一次计算时间
		Timestamp lastDayCalcuDttm = castle.getCalcuDttm();// 每天刷新的时间
		if (lastDayCalcuDttm == null) {
			lastDayCalcuDttm = lastQuarCalcuDttm;
		}

		// 如果隔天了，重置最后一次刷新时间
		Calendar c = Calendar.getInstance();
		int day2 = c.get(Calendar.DAY_OF_YEAR);

		c.setTime(lastDayCalcuDttm);
		int day1 = c.get(Calendar.DAY_OF_YEAR);

		boolean update = false;
		long casId = castle.getCasId();
		// 15分钟计算一次内政数据,少于15分钟直接返回
		long seconds = (nowTime - lastQuarCalcuDttm.getTime()) / 1000L;
		int quarters = (int) (seconds / AppConstants.CAS_FRESH_QUARCALCU_GAP);

		// 下次15分钟刷新的时间
		Timestamp quarDttm = null;
		// 差的秒数
		int modSeconds = (int) (seconds % AppConstants.CAS_FRESH_QUARCALCU_GAP);
		if (modSeconds > 60) {
			// 60秒以内的忽略，大于60秒，则下次刷新时间向回倒modSeconds
			c.add(Calendar.SECOND, -modSeconds);
			if (c.get(Calendar.DAY_OF_YEAR) == day1) {
				quarDttm = new Timestamp(nowTime - modSeconds * 1000L);
			} else {
				quarDttm = now;
			}
		} else {
			quarDttm = now;
		}

		// 下一次的刷新时间
		if (quarters > 0) {
			castle = castleService.lockMainCastle(castle);

			// 删除过期的城池效果
			List<CastleEffect> effects = castleEffectService.removeTimeoutCastleEffect(castle.getCasId());
			
			// 删除过期的用户效果
			userEffectService.removeTimeoutUserEffect(user.getUserId());

			// 粮食/矿物/铜币
			boolean changeRes = true;

			CastleResource castleRes = castleResService.lockCasRes(casId);
            if(castleRes == null){
                throw new BaseException("用户资源错误。");
            }

			//计算金矿产量
			int goldLimit = castleResService.getGoldProduceLimit(casId, effects);
			//当前产出
			long hasGold = castleRes.getCasGoldNum();
			if (hasGold < goldLimit) {//超过产量上限不在产出
				//每15分钟的产量
				int add = castleResService.getGoldProducePerQuarter(userId, casId, effects);
				add = add * quarters;
				if (add > 0) {
					hasGold = hasGold + add;
					if (hasGold > goldLimit || hasGold < 0) {
						hasGold = goldLimit;
					}
					castleRes.setCasGoldNum(hasGold);
					changeRes = true;
				}
			}

			//计算铁矿产量
			int ironLimit = castleResService.getIronProduceLimit(casId, effects);
			//当前产出
			long hasIron = castleRes.getCasIronNum();
			if (hasIron < ironLimit) {//超过产量上限不在产出
				//每15分钟的产量
				int add = castleResService.getIronProducePerQuarter(userId, casId, effects);
				add = add * quarters;
				if (add > 0) {
					hasIron = hasIron + add;
					if (hasIron > ironLimit || hasIron < 0) {
						hasIron = ironLimit;
					}
					castleRes.setCasIronNum(hasIron);
					changeRes = true;
				}
			}

			//计算油矿产量
			int oilLimit = castleResService.getOilProduceLimit(casId, effects);
			//当前产出
			long hasOil = castleRes.getCasOilNum();
			if (hasOil < oilLimit) {//超过产量上限不在产出
				//每15分钟的产量
				int add = castleResService.getOilProducePerQuarter(userId, casId, effects);
				add = add * quarters;
				if (add > 0) {
					hasOil = hasOil + add;
					if (hasOil > oilLimit || hasOil < 0) {
						hasOil = oilLimit;
					}
					castleRes.setCasOilNum(hasOil);
					changeRes = true;
				}
			}

			//计算铀矿产量
			int uraniumLimit = castleResService.getUraniumProduceLimit(casId, effects);
			//当前产出
			long hasUranium = castleRes.getCasUranium();
			if (hasUranium < uraniumLimit) {//超过产量上限不在产出
				//每15分钟的产量
				int add = castleResService.getUraniumProducePerQuarter(userId, casId, effects);
				add = add * quarters;
				if (add > 0) {
					hasUranium = hasUranium + add;
					if (hasUranium > uraniumLimit || hasUranium < 0) {
						hasUranium = uraniumLimit;
					}
					castleRes.setCasUranium(hasUranium);
					changeRes = true;
				}
			}

			if (day1 != day2) {
				// 刷掉超过上限的资源
				int goldCap = castleResService.getGoldCapacity(casId, effects);
				if (castleRes.getGoldNum() > goldCap) {
					castleRes.setGoldNum(goldCap);
					changeRes = true;
				}

				int ironCap = castleResService.getIronCapacity(casId, effects);
				if (castleRes.getIronNum() > ironCap) {
					castleRes.setIronNum(ironCap);
					changeRes = true;
				}

				int oilCap = castleResService.getOilCapacity(casId, effects);
				if (castleRes.getOilNum() > oilCap) {
					castleRes.setOilNum(oilCap);
					changeRes = true;
				}

				int uraniumCap = castleResService.getUraniumCapacity(casId, effects);
				if (castleRes.getUranium() > uraniumCap) {
					castleRes.setUranium(uraniumCap);
					changeRes = true;
				}
			}
			if (changeRes) {
				castleResService.updateCastleResource(castleRes);
			}

			result.put("casRes", castleRes);// 资源

			// 检查Troop状态
			troopService.doRefreshTroop(user.getUserId());

			// TODO 需要每15分钟检查一次的内容

			// 每15分钟恢复一次体力和行动力
			userService.doFillUpHpAfteQuarters(user, now, quarters);
						
			// 登录时：大于15分钟,且隔天
			if (day1 != day2) {
				castle.setCalcuDttm(now);

				//需要每天或是0点刷新的内容
			}
			castle.setQuarCalcuDttm(quarDttm);
			update = true;
		} else {
			if (day1 != day2) {
				castle = castleService.lockMainCastle(castle);
				castle.setCalcuDttm(now);

				// 刷掉超过上限的资源
				List<CastleEffect> effects = castleEffectService.getCastleEffectByCasId(casId);
				CastleResource castleRes = castleResService.lockCasRes(casId);
				boolean changeRes = false;
				// 刷掉超过上限的资源
				int goldCap = castleResService.getGoldCapacity(casId, effects);
				if (castleRes.getGoldNum() > goldCap) {
					castleRes.setGoldNum(goldCap);
					changeRes = true;
				}

				int ironCap = castleResService.getIronCapacity(casId, effects);
				if (castleRes.getIronNum() > ironCap) {
					castleRes.setIronNum(ironCap);
					changeRes = true;
				}

				int oilCap = castleResService.getOilCapacity(casId, effects);
				if (castleRes.getOilNum() > oilCap) {
					castleRes.setOilNum(oilCap);
					changeRes = true;
				}

				int uraniumCap = castleResService.getUraniumCapacity(casId, effects);
				if (castleRes.getUranium() > uraniumCap) {
					castleRes.setUranium(uraniumCap);
					changeRes = true;
				}
				if (changeRes) {
					castleResService.updateCastleResource(castleRes);
				}

				//TODO 需要每天或是0点刷新的内容

				update = true;
			}
		}

		if (update) {
			castleDao.updateCastle(castle);
		}
		result.put("cas", castle);// 城池
		result.put("user", user);// 城池
		return result;
	}

	@Override
	public void sendFlushCasEffEvent(long userId, String fieldName, int fieldValue) {
		messageService.sendEventMessage(userId,
				EventMessage.TYPE_CASEFF_CHANGED, new CastleEffMessage(fieldName, fieldValue));
	}

}
