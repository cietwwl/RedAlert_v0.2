package com.youxigu.dynasty2.mission.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.core.wolf.IWolfClientService;
import com.youxigu.dynasty2.mission.dao.IWorldMissionDao;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.domain.UserWorldMission;
import com.youxigu.dynasty2.mission.domain.WorldMission;
import com.youxigu.dynasty2.mission.service.IMissionAwardService;
import com.youxigu.dynasty2.mission.service.IMissionService;
import com.youxigu.dynasty2.mission.service.IWorldMissionClientService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.PagerResult;
import com.youxigu.wolf.net.AsyncWolfTask;

public class WorldMissionClientService implements IWorldMissionClientService {
	public static final Logger log = LoggerFactory
			.getLogger(WorldMissionClientService.class);
	public final static String wolf_worldMissionService = "wolf_worldMissionService";
	public final static String Locker = "LOCKER_WORLD_MISSION_";

	private IWolfClientService wolfService;
	private IWorldMissionDao worldMissionDao;
	private IMissionService missionService;
	private IUserService userService;

	public void setWolfService(IWolfClientService wolfService) {
		this.wolfService = wolfService;
	}

	public void setWorldMissionDao(IWorldMissionDao worldMissionDao) {
		this.worldMissionDao = worldMissionDao;
	}

	public void setMissionService(IMissionService missionService) {
		this.missionService = missionService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public PagerResult getUserMissionByUserId(long userId, int pageNo) {
		return wolfService.sendTask(PagerResult.class, wolf_worldMissionService,
				"getUserMissionByUserId", new Object[] { userId, pageNo });
	}

	@Override
	public int[] getFinishWorldMissionNum(long userId) {
		int finishNum = 0;// 完成数
		int awardedNum = 0;// 领取数
		int[] nums = new int[2];
		List<WorldMission> list = worldMissionDao.getAllWorldMission();
		if (list != null && list.size() > 0) {
			for (WorldMission worldMission : list) {
				if (worldMission.getStatus() != WorldMission.STATUS_COMPLETE) {
					continue;
				}

				finishNum = finishNum + 1;
				UserWorldMission userWorldMission = worldMissionDao
						.getUserWorldMission(userId,
								worldMission.getMissionId());
				if (userWorldMission != null) {
					awardedNum = awardedNum + 1;
				}
			}
		}

		nums[0] = finishNum;
		nums[1] = awardedNum;
		return nums;
	}

	@Override
	public void notifyMissionModule(String otcType, int entId, int num) {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setServiceName(wolf_worldMissionService);
		task.setMethodName("asynNotifyMissionModule");
		task.setParams(new Object[] { otcType, entId, num });
		wolfService.asynSendTask(task);
	}

	@Override
	public WorldMission doAward(long userId, int missionId) {
		// 锁任务
		missionService.lockUserMission(userId);

		Mission mission = missionService.getMissionById(missionId);
		if (mission == null) {
			throw new BaseException("任务不存在");
		}
		if (!mission.getMissionType().equals(Mission.MISSION_TYPE_WORLD)) {
			throw new BaseException("不是特殊事件");
		}
		WorldMission worldMission = worldMissionDao.getWorldMission(missionId);
		if (worldMission == null) {
			throw new BaseException("任务不存在");
		}
		if (worldMission.getStatus() == WorldMission.STATUS_FAIL) {
			throw new BaseException("任务失败");
		}
		if (worldMission.getStatus() != WorldMission.STATUS_COMPLETE) {
			throw new BaseException("任务尚未完成");
		}
		UserWorldMission userWorldMission = worldMissionDao
				.getUserWorldMission(userId, missionId);
		if (userWorldMission != null) {
			throw new BaseException("奖励已经领取");
		}

		User user = userService.getUserById(userId);
		UserMission userMission = new UserMission();
		userMission.setUserMissionId(-1);
		userMission.setUserId(userId);
		userMission.setMissionId(missionId);
		userMission.setStatus(UserMission.STATUS_COMPLETE);
		userMission.setNum1(Integer.MAX_VALUE);
		userMission.setNum2(Integer.MAX_VALUE);
		userMission.setNum3(Integer.MAX_VALUE);
		userMission.setNum4(Integer.MAX_VALUE);
		userMission.setNum5(Integer.MAX_VALUE);
		userMission.setCompleteLimitTime(worldMission.getCompleteLimitTime());
		this.commitGeneralMission(user, userMission, mission);
		return worldMission;
	}

	/**
	 * 发奖励
	 * 
	 * @param user
	 * @param userMission
	 * @param mission
	 */
	private void commitGeneralMission(User user, UserMission userMission,
			Mission mission) {

		Collection<MissionLimit> limits = mission.getLimitIndexs().keySet();

		if (limits == null || limits.size() == 0) {
			return;
		}

		Iterator<MissionLimit> lit = limits.iterator();

		while (lit.hasNext()) {
			MissionLimit limit = lit.next();
			limit.getLimitChecker().doConsume(user, userMission, mission,
					limit);
		}

		List<MissionAward> awards = mission.getMissionAwards();
		if (awards == null) {
			return;
		}

		for (MissionAward award : awards) {
			IMissionAwardService awardService = missionService
					.getAwardService(award.getAwardtype());
			if (awardService == null) {
				throw new BaseException("奖励定义错误：" + award.getAwardtype());
			}
			awardService.doAward(user, award, mission, userMission);
		}

		// 记录领奖记录
		UserWorldMission userWorldMission = new UserWorldMission(
				user.getUserId(), mission.getMissionId(),
				userMission.getCompleteLimitTime(), UtilDate.nowTimestamp());
		worldMissionDao.insertUserWorldMission(userWorldMission);
	}

	@Override
	public void doGmtoolCreateMission(int missionId) {
		wolfService.sendTask(Void.class, wolf_worldMissionService,
				"doGmtoolCreateMission", new Object[] { missionId });
		
	}
}
