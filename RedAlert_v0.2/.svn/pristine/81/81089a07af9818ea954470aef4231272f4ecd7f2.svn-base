package com.youxigu.dynasty2.develop.service.impl;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.ibatis.sqlmap.engine.cache.memcached.MemcachedManager;
import com.youxigu.dynasty2.chat.EventMessage;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.dao.ICastleResDao;
import com.youxigu.dynasty2.develop.domain.Castle;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.domain.CastleResource;
import com.youxigu.dynasty2.develop.domain.ResourceMessage;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.user.domain.AchieveType;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;

public class CastleResService implements ICastleResService {
	private ICastleResDao castleResDao;
	private ICastleEffectService castleEffectService;
	private ICastleService castleService;
	private IChatClientService messageService;
	private IMissionService missionService;
	private IUserService userService;
	private IUserAchieveService userAchieveService;

	public void setCastleResDao(ICastleResDao castleResDao) {
		this.castleResDao = castleResDao;
	}

	public void setCastleEffectService(
			ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	@Override
	public CastleResource lockCasRes(long casId) {
		try {
			MemcachedManager.lockClass(CastleResource.class, casId);
			return castleResDao.getCastleResourceById(casId);
		} catch (TimeoutException e) {
			throw new BaseException(e.toString());
		}
	}

	@Override
	public void updateCastleResource(CastleResource castleResource) {
		castleResDao.updateCastleResource(castleResource);
	}

	@Override
	public void updateCastleResource(CastleResource castleResource, long userId,
			boolean sendEvent) {
		this.updateCastleResource(castleResource);
		// 推送刷新资源
		if (sendEvent) {
			this.sendFlushCastleRes(userId, castleResource);
		}
	}

	@Override
	public CastleResource getCastleResourceById(long casId) {
		return castleResDao.getCastleResourceById(casId);
	}

	@Override
	public CastleResource doGmtoolAddResource(long casId, int num, int type) {
		if (num == 0) {
			return this.getCastleResourceById(casId);
		}
		Castle cas = castleService.getCastleById(casId);
		User user = userService.getUserById(cas.getUserId());
		CastleResource casRes = this.lockCasRes(casId);
		switch (type) {
		case 0:
			casRes.setGoldNum(casRes.getGoldNum() + num);
			casRes.setIronNum(casRes.getIronNum() + num);
			casRes.setOilNum(casRes.getOilNum() + num);
			casRes.setUranium(casRes.getUranium() + num);

			if (num > 0) {
				// 任务
				missionService.notifyMissionModule(user,
						Mission.QCT_TYPE_RESOURCE, AppConstants.ENT_RES_GOLD,
						(int) casRes.getGoldNum());
				missionService.notifyMissionModule(user,
						Mission.QCT_TYPE_RESOURCE, AppConstants.ENT_RES_IRON,
						(int) casRes.getIronNum());
				missionService.notifyMissionModule(user,
						Mission.QCT_TYPE_RESOURCE, AppConstants.ENT_RES_OIL,
						(int) casRes.getOilNum());
				missionService.notifyMissionModule(user,
						Mission.QCT_TYPE_RESOURCE, AppConstants.ENT_RES_URANIUM,
						(int) casRes.getUranium());

				// 成就
				userAchieveService.doNotifyAchieveModule(user.getUserId(),
						AchieveType.TYPE_RES, AppConstants.ENT_RES_GOLD, num);
				userAchieveService.doNotifyAchieveModule(user.getUserId(),
						AchieveType.TYPE_RES, AppConstants.ENT_RES_IRON, num);
				userAchieveService.doNotifyAchieveModule(user.getUserId(),
						AchieveType.TYPE_RES, AppConstants.ENT_RES_OIL, num);
				userAchieveService.doNotifyAchieveModule(user.getUserId(),
						AchieveType.TYPE_RES, AppConstants.ENT_RES_URANIUM,
						num);
			}
			break;
		case 1:
			casRes.setGoldNum(casRes.getGoldNum() + num);
			missionService.notifyMissionModule(user, Mission.QCT_TYPE_RESOURCE,
					AppConstants.ENT_RES_GOLD, (int) casRes.getGoldNum());

			// 成就
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_RES, AppConstants.ENT_RES_GOLD, num);
			break;
		case 2:
			casRes.setIronNum(casRes.getIronNum() + num);
			missionService.notifyMissionModule(user, Mission.QCT_TYPE_RESOURCE,
					AppConstants.ENT_RES_IRON, (int) casRes.getIronNum());

			// 成就
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_RES, AppConstants.ENT_RES_IRON, num);
			break;
		case 3:
			casRes.setOilNum(casRes.getOilNum() + num);
			missionService.notifyMissionModule(user, Mission.QCT_TYPE_RESOURCE,
					AppConstants.ENT_RES_OIL, (int) casRes.getOilNum());

			// 成就
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_RES, AppConstants.ENT_RES_OIL, num);
			break;
		case 4:
			casRes.setUranium(casRes.getUranium() + num);
			missionService.notifyMissionModule(user, Mission.QCT_TYPE_RESOURCE,
					AppConstants.ENT_RES_URANIUM, (int) casRes.getUranium());

			// 成就
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_RES, AppConstants.ENT_RES_URANIUM, num);
			break;
		default:
			throw new BaseException("资源类型错误");
		}

		this.updateCastleResource(casRes, cas.getUserId(), true);

		return casRes;
	}

	@Override
	public void doAddRes(long casId, int entId, long resNum,
			boolean sendFlushRes) {
		if (resNum < 0) {
			throw new BaseException("资源数量必须为正数");
		}

		CastleResource castleResource = this.lockCasRes(casId);

		// 金矿
		long curNum = 0;
		if (entId == AppConstants.ENT_RES_GOLD) {
			curNum = castleResource.getGoldNum() + resNum;
			castleResource.setGoldNum(curNum);
		}
		// 铁矿
		else if (entId == AppConstants.ENT_RES_IRON) {
			curNum = castleResource.getIronNum() + resNum;
			castleResource.setIronNum(curNum);
		}
		// 油矿
		else if (entId == AppConstants.ENT_RES_OIL) {
			curNum = castleResource.getOilNum() + resNum;
			castleResource.setOilNum(curNum);
		}
		// 铀矿
		else if (entId == AppConstants.ENT_RES_URANIUM) {
			curNum = castleResource.getUranium() + resNum;
			castleResource.setUranium(curNum);
		}
		// 错误的entId
		else {
			throw new BaseException("错误的资源实体ID。");
		}

		castleResDao.updateCastleResource(castleResource);

		// 囤积资源,当前资源量
		Castle castle = castleService.getCastleById(castleResource.getCasId());
		User user = userService.getUserById(castle.getUserId());
		if (curNum > 0) {
			missionService.notifyMissionModule(user, Mission.QCT_TYPE_RESOURCE,
					entId, (int) curNum);

			// 成就
			userAchieveService.doNotifyAchieveModule(user.getUserId(),
					AchieveType.TYPE_RES, entId, (int) resNum);
		}

		if (sendFlushRes) {
			// 推送刷新资源
			this.sendFlushCastleRes(user.getUserId(), castleResource);
		}
	}

	@Override
	public void doAddRes(long casId, int[] entIds, long[] resNums,
			boolean sendFlushRes) {
		CastleResource castleResource = this.lockCasRes(casId);
		for (int i = 0; i < entIds.length; i++) {
			long resNum = resNums[i];
			int entId = entIds[i];
			long curNum = 0;
			if (resNum < 0) {
				throw new BaseException("资源数量必须为正数");
			}
			// 金矿
			if (entId == AppConstants.ENT_RES_GOLD) {
				curNum = castleResource.getGoldNum() + resNum;
				castleResource.setGoldNum(curNum);
			}
			// 铁矿
			else if (entId == AppConstants.ENT_RES_IRON) {
				curNum = castleResource.getIronNum() + resNum;
				castleResource.setIronNum(curNum);
			}
			// 油矿
			else if (entId == AppConstants.ENT_RES_OIL) {
				curNum = castleResource.getOilNum() + resNum;
				castleResource.setOilNum(curNum);
			}
			// 铀矿
			else if (entId == AppConstants.ENT_RES_URANIUM) {
				curNum = castleResource.getUranium() + resNum;
				castleResource.setUranium(curNum);
			}
			// 错误的entId
			else {
				throw new BaseException("错误的资源实体ID。");
			}

			// 囤积资源,当前资源量
			if (curNum > 0) {
				Castle castle = castleService
						.getCastleById(castleResource.getCasId());
				User user = userService.getUserById(castle.getUserId());
				missionService.notifyMissionModule(user,
						Mission.QCT_TYPE_RESOURCE, entId, (int) curNum);

				// 成就
				userAchieveService.doNotifyAchieveModule(user.getUserId(),
						AchieveType.TYPE_RES, entId, (int) resNum);
			}
		}

		castleResDao.updateCastleResource(castleResource);
		if (sendFlushRes) {
			// 推送刷新资源
			this.sendFlushCastleRes(castleResource);
		}
	}

	@Override
	public boolean doDelRes(long casId, int entId, long resNum,
			boolean sendFlushRes, boolean throwException) {
		if (resNum < 0) {
			throw new BaseException("资源数量必须为正数");
		}

		CastleResource castleResource = this.lockCasRes(casId);

		String error = "";
		long tmp = 0;
		// 金矿
		if (entId == AppConstants.ENT_RES_GOLD) {
			tmp = castleResource.getGoldNum() - resNum;
			if (tmp < 0) {
				error = "金矿不足";
			} else {
				castleResource.setGoldNum(Math.max(0, tmp));
			}
		}
		// 铁矿
		else if (entId == AppConstants.ENT_RES_IRON) {
			tmp = castleResource.getIronNum() - resNum;
			if (tmp < 0) {
				error = "铁矿不足";
			} else {
				castleResource.setIronNum(Math.max(0, tmp));
			}
		}
		// 油矿
		else if (entId == AppConstants.ENT_RES_OIL) {
			tmp = castleResource.getOilNum() - resNum;
			if (tmp < 0) {
				error = "油矿不足";
			} else {
				castleResource.setOilNum(Math.max(0, tmp));
			}
		}
		// 铀矿
		else if (entId == AppConstants.ENT_RES_URANIUM) {
			tmp = castleResource.getUranium() - resNum;
			if (tmp < 0) {
				error = "铀矿不足";
			} else {
				castleResource.setUranium(Math.max(0, tmp));
			}
		}
		// 错误的entId
		else {
			throw new BaseException("错误的资源实体ID。");
		}

		if (error.equals("")) {
			castleResDao.updateCastleResource(castleResource);
			if (sendFlushRes) {
				// 推送刷新资源
				this.sendFlushCastleRes(castleResource);
			}
		} else {
			if (throwException) {
				throw new BaseException(error);
			}
		}
		return error.equals("");
	}

	@Override
	public boolean doDelRes(long casId, int[] entIds, long[] resNums,
			boolean sendFlushRes, boolean throwException) {
		CastleResource castleResource = this.lockCasRes(casId);

		String error = "";
		long tmp = 0;
		for (int i = 0; i < entIds.length; i++) {
			long resNum = resNums[i];
			int entId = entIds[i];

			if (resNum < 0) {
				throw new BaseException("资源数量必须为正数");
			}

			// 金矿
			if (entId == AppConstants.ENT_RES_GOLD) {
				tmp = castleResource.getGoldNum() - resNum;
				if (tmp < 0) {
					error = "金矿不足";
				} else {
					castleResource.setGoldNum(Math.max(0, tmp));
				}
			}
			// 铁矿
			else if (entId == AppConstants.ENT_RES_IRON) {
				tmp = castleResource.getIronNum() - resNum;
				if (tmp < 0) {
					error = "铁矿不足";
				} else {
					castleResource.setIronNum(Math.max(0, tmp));
				}
			}
			// 油矿
			else if (entId == AppConstants.ENT_RES_OIL) {
				tmp = castleResource.getOilNum() - resNum;
				if (tmp < 0) {
					error = "油矿不足";
				} else {
					castleResource.setOilNum(Math.max(0, tmp));
				}
			}
			// 铀矿
			else if (entId == AppConstants.ENT_RES_URANIUM) {
				tmp = castleResource.getUranium() - resNum;
				if (tmp < 0) {
					error = "铀矿不足";
				} else {
					castleResource.setUranium(Math.max(0, tmp));
				}
			}
			// 错误的entId
			else {
				throw new BaseException("错误的资源实体ID。");
			}
		}

		if (error.equals("")) {
			castleResDao.updateCastleResource(castleResource);
			if (sendFlushRes) {
				// 推送刷新资源
				this.sendFlushCastleRes(castleResource);
			}
		} else {
			if (throwException) {
				throw new BaseException(error);
			}
		}
		return error.equals("");
	}

	/**
	 * 推送资源变化
	 * 
	 * @param castleResource
	 */
	private void sendFlushCastleRes(CastleResource castleResource) {
		// event 刷新资源
		Castle castle = castleService.getCastleById(castleResource.getCasId());
		this.sendFlushCastleRes(castle.getUserId(), castleResource);
	}

	private void sendFlushCastleRes(long userId,
			CastleResource castleResource) {
		// event 刷新资源
		messageService.sendEventMessage(userId,
				EventMessage.TYPE_RESOURCE_CHANGED,
				new ResourceMessage(castleResource));
	}

	@Override
	public int getGoldProduceLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_GOLD_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getGoldProduceLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_GOLD_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getIronProduceLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_IRON_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getIronProduceLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_IRON_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getOilProduceLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_OIL_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getOilProduceLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_OIL_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getUraniumProduceLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_URANIUM_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getUraniumProduceLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_URANIUM_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getGoldProducePerQuarter(User user) {
		return getGoldProducePerQuarter(user.getUserId(),
				user.getMainCastleId(), null);
	}

	@Override
	public int getGoldProducePerQuarter(User user, List<CastleEffect> effects) {
		return getGoldProducePerQuarter(user.getUserId(),
				user.getMainCastleId(), effects);
	}

	@Override
	public int getGoldProducePerQuarter(long userId, long casId) {
		return getGoldProducePerQuarter(userId, casId, null);
	}

	@Override
	public int getGoldProducePerQuarter(long userId, long casId,
			List<CastleEffect> effects) {
		if (effects == null) {
			effects = castleEffectService.getCastleEffectByCasId(casId);
		}

		// 15分钟的产量效果
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_GOLD_YIELD, effects);

		// 科技效果15分钟的产量
		EffectValue techValue = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_GOLD_SKILL, effects);

		int percent = 0;

		// 效果合并
		value.setPerValue(
				percent + value.getPerValue() + techValue.getPerValue());
		value.setAbsValue(value.getAbsValue() + techValue.getAbsValue());

		int result = Math.max(
				(int) ((value.getAbsValue()) * (1 + value.getDoublePerValue())),
				1);
		return result;
	}

	@Override
	public int getIronProducePerQuarter(User user) {
		return getIronProducePerQuarter(user.getUserId(),
				user.getMainCastleId(), null);
	}

	@Override
	public int getIronProducePerQuarter(User user, List<CastleEffect> effects) {
		return getIronProducePerQuarter(user.getUserId(),
				user.getMainCastleId(), effects);
	}

	@Override
	public int getIronProducePerQuarter(long userId, long casId) {
		return getIronProducePerQuarter(userId, casId, null);
	}

	@Override
	public int getIronProducePerQuarter(long userId, long casId,
			List<CastleEffect> effects) {
		if (effects == null) {
			effects = castleEffectService.getCastleEffectByCasId(casId);
		}

		// 15分钟的产量效果
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_IRON_YIELD, effects);

		// 科技效果15分钟的产量
		EffectValue techValue = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_IRON_SKILL, effects);

		int percent = 0;

		// 效果合并
		value.setPerValue(
				percent + value.getPerValue() + techValue.getPerValue());
		value.setAbsValue(value.getAbsValue() + techValue.getAbsValue());

		int result = Math.max(
				(int) ((value.getAbsValue()) * (1 + value.getDoublePerValue())),
				1);
		return result;
	}

	@Override
	public int getOilProducePerQuarter(User user) {
		return getOilProducePerQuarter(user.getUserId(), user.getMainCastleId(),
				null);
	}

	@Override
	public int getOilProducePerQuarter(User user, List<CastleEffect> effects) {
		return getOilProducePerQuarter(user.getUserId(), user.getMainCastleId(),
				effects);
	}

	@Override
	public int getOilProducePerQuarter(long userId, long casId) {
		return getOilProducePerQuarter(userId, casId, null);
	}

	@Override
	public int getOilProducePerQuarter(long userId, long casId,
			List<CastleEffect> effects) {
		if (effects == null) {
			effects = castleEffectService.getCastleEffectByCasId(casId);
		}

		// 15分钟的产量效果
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_OIL_YIELD, effects);

		// 科技效果15分钟的产量
		EffectValue techValue = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_OIL_SKILL, effects);

		int percent = 0;

		// 效果合并
		value.setPerValue(
				percent + value.getPerValue() + techValue.getPerValue());
		value.setAbsValue(value.getAbsValue() + techValue.getAbsValue());

		int result = Math.max(
				(int) ((value.getAbsValue()) * (1 + value.getDoublePerValue())),
				1);
		return result;
	}

	@Override
	public int getUraniumProducePerQuarter(User user) {
		return getUraniumProducePerQuarter(user.getUserId(),
				user.getMainCastleId(), null);
	}

	@Override
	public int getUraniumProducePerQuarter(User user,
			List<CastleEffect> effects) {
		return getUraniumProducePerQuarter(user.getUserId(),
				user.getMainCastleId(), effects);
	}

	@Override
	public int getUraniumProducePerQuarter(long userId, long casId) {
		return getUraniumProducePerQuarter(userId, casId, null);
	}

	@Override
	public int getUraniumProducePerQuarter(long userId, long casId,
			List<CastleEffect> effects) {
		if (effects == null) {
			effects = castleEffectService.getCastleEffectByCasId(casId);
		}

		// 15分钟的产量效果
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_URANIUM_YIELD, effects);

		// 科技效果15分钟的产量
		EffectValue techValue = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_URANIUM_SKILL, effects);

		int percent = 0;

		// 效果合并
		value.setPerValue(
				percent + value.getPerValue() + techValue.getPerValue());
		value.setAbsValue(value.getAbsValue() + techValue.getAbsValue());

		int result = Math.max(
				(int) ((value.getAbsValue()) * (1 + value.getDoublePerValue())),
				1);
		return result;
	}

	@Override
	public int getGoldCapacity(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_GOLD_CAP);
		return value.getAbsValue();
	}

	@Override
	public int getGoldCapacity(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_GOLD_CAP, effects);
		return value.getAbsValue();
	}

	@Override
	public int getIronCapacity(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_IRON_CAP);
		return value.getAbsValue();
	}

	@Override
	public int getIronCapacity(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_IRON_CAP, effects);
		return value.getAbsValue();
	}

	@Override
	public int getOilCapacity(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_OIL_CAP);
		return value.getAbsValue();
	}

	@Override
	public int getOilCapacity(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(EffectTypeDefine.B_OIL_CAP,
						effects);
		return value.getAbsValue();
	}

	@Override
	public int getUraniumCapacity(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_URANIUM_CAP);
		return value.getAbsValue();
	}

	@Override
	public int getUraniumCapacity(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_URANIUM_CAP, effects);
		return value.getAbsValue();
	}

	@Override
	public int getGoldRobLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_GOLDROB_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getGoldRobLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_GOLDROB_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getIronRobLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_IRONROB_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getIronRobLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_IRONROB_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getOilRobLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_OILROB_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getOilRobLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_OILROB_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public int getUraniumRobLimit(long casId) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(casId,
						EffectTypeDefine.B_URANIUMROB_LIMIT);
		return value.getAbsValue();
	}

	@Override
	public int getUraniumRobLimit(long casId, List<CastleEffect> effects) {
		EffectValue value = castleEffectService
				.getSumCastleEffectValueByEffectType(
						EffectTypeDefine.B_URANIUMROB_LIMIT, effects);
		return value.getAbsValue();
	}

	@Override
	public CastleResource doGainGold(long casId, long userId) {
		CastleResource resource = this.lockCasRes(casId);
		long canGain = resource.getCasGoldNum();
		if (canGain == 0) {
			throw new BaseException("没有可以收取的资源");
		}

		int capacity = this.getGoldCapacity(casId);
		long resNum = resource.getGoldNum();
		if (resNum >= capacity) {
			throw new BaseException("仓库资源已经达到上限，不能再收取");
		}

		if (resNum + canGain >= capacity) {
			canGain = canGain - (int) (capacity - resNum);
			resNum = capacity;
		} else {
			resNum = resNum + canGain;
			canGain = 0;
		}
		resource.setGoldNum(resNum);
		resource.setCasGoldNum(canGain);
		castleResDao.updateCastleResource(resource);

		// 收取金矿N次，累计值
		User user = userService.getUserById(userId);
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_GAINGOLD, 0,
				1);
		return resource;
	}

	@Override
	public CastleResource doGainIron(long casId, long userId) {
		CastleResource resource = this.lockCasRes(casId);
		long canGain = resource.getCasIronNum();
		if (canGain == 0) {
			throw new BaseException("没有可以收取的资源");
		}

		int capacity = this.getIronCapacity(casId);
		long resNum = resource.getIronNum();
		if (resNum >= capacity) {
			throw new BaseException("仓库资源已经达到上限，不能再收取");
		}

		if (resNum + canGain >= capacity) {
			canGain = canGain - (int) (capacity - resNum);
			resNum = capacity;
		} else {
			resNum = resNum + canGain;
			canGain = 0;
		}
		resource.setIronNum(resNum);
		resource.setCasIronNum(canGain);
		castleResDao.updateCastleResource(resource);

		// 收取金矿N次，累计值
		User user = userService.getUserById(userId);
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_GAINIRON, 0,
				1);

		return resource;
	}

	@Override
	public CastleResource doGainOil(long casId, long userId) {
		CastleResource resource = this.lockCasRes(casId);
		long canGain = resource.getCasOilNum();
		if (canGain == 0) {
			throw new BaseException("没有可以收取的资源");
		}

		int capacity = this.getOilCapacity(casId);
		long resNum = resource.getOilNum();
		if (resNum >= capacity) {
			throw new BaseException("仓库资源已经达到上限，不能再收取");
		}

		if (resNum + canGain >= capacity) {
			canGain = canGain - (int) (capacity - resNum);
			resNum = capacity;
		} else {
			resNum = resNum + canGain;
			canGain = 0;
		}
		resource.setOilNum(resNum);
		resource.setCasOilNum(canGain);
		castleResDao.updateCastleResource(resource);

		// 收取油矿N次，累计值
		User user = userService.getUserById(userId);
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_GAINOIL, 0,
				1);

		return resource;
	}

	@Override
	public CastleResource doGainUranium(long casId, long userId) {
		CastleResource resource = this.lockCasRes(casId);
		long canGain = resource.getCasUranium();
		if (canGain == 0) {
			throw new BaseException("没有可以收取的资源");
		}

		int capacity = this.getUraniumCapacity(casId);
		long resNum = resource.getUranium();
		if (resNum >= capacity) {
			throw new BaseException("仓库资源已经达到上限，不能再收取");
		}

		if (resNum + canGain >= capacity) {
			canGain = canGain - (int) (capacity - resNum);
			resNum = capacity;
		} else {
			resNum = resNum + canGain;
			canGain = 0;
		}
		resource.setUranium(resNum);
		resource.setCasUranium(canGain);
		castleResDao.updateCastleResource(resource);

		// 收取铀矿N次，累计值
		User user = userService.getUserById(userId);
		missionService.notifyMissionModule(user, Mission.QCT_TYPE_GAINURANIUM,
				0, 1);
		return resource;
	}

}
