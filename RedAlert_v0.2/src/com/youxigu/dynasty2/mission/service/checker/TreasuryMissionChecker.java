package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 背包中是否有n个道具的任务
 * 
 * 这实际上就是OTCCurrentNumMatchChecker类的一个特例：这里用treasuryService取数量，比外面取更合适
 * 
 * QCT_Item
 * 
 * @author Administrator
 * 
 */
public class TreasuryMissionChecker extends OTCCurrentNumMatchChecker {

	private ITreasuryService treasuryService;

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {

		if (num == Integer.MIN_VALUE) {
			num = treasuryService.getTreasuryCountByEntId(user.getUserId(),
					entId);
		}
		return super.check(user, userMission, mission, missionLimit, entId,
				num);

	}

	@Override
	public void doConsume(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit) {
		if (Mission.QCT_TYPE_COLLECTION.equals(missionLimit.getOctType())) {
			treasuryService.deleteItemFromTreasury(user.getUserId(),
					missionLimit.getEntId(),
					missionLimit.getLimitChecker().getLimitEntNum(user,
							userMission, mission, missionLimit),
					true, LogItemAct.LOGITEMACT_109);
		}

	}
}
