package com.youxigu.dynasty2.mission.service.checker;

import java.util.List;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCMaxNumMatchChecker;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_equipLv 任意装备强化达到N级，如果装备被分解掉，也算完成
 * 
 * @author Administrator
 * 
 */
public class EquipCurrentLvMissionChecker extends OTCMaxNumMatchChecker {
	private ITreasuryService treasuryService;

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num == Integer.MIN_VALUE) {
			List<Treasury> treasurys = treasuryService
					.getTreasurysByUserId(user.getUserId());

			if (treasurys != null) {
				for (Treasury t : treasurys) {
					if (t.getItem().isEquip()) {
						int lv = t.getLevel();
						if (lv > num) {
							num = lv;
						}
					}
				}
			} else {
				num = 0;
			}

		}
		return super.check(user, userMission, mission, missionLimit, entId,
				num);
	}

}
