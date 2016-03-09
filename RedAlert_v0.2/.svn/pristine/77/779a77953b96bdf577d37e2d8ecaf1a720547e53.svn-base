package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 奖励:道具
 * 
 * @author Administrator
 *
 */
public class ItemAward extends AbstractMissionAward {
	private ITreasuryService treasuryService;

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		int num = this.calcAward(award.getNum(), userMission);
		if (num < 0) {
			treasuryService.deleteItemFromTreasury(user.getUserId(),
					award.getEntId(), num * -1, true,
					com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_64);
		} else {
			treasuryService.addItemToTreasury(user.getUserId(),
					award.getEntId(), num, 1, -1, false, true,
					com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_64);
		}
	}

}
