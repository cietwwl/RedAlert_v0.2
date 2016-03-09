package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.risk.domain.OneRisk;
import com.youxigu.dynasty2.risk.domain.RiskScene;
import com.youxigu.dynasty2.risk.domain.UserRiskScene;
import com.youxigu.dynasty2.risk.service.IRiskService;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 判断是否通过了某个冒险关卡
 * 
 * @author fengfeng
 *
 */
public class RiskScenePassMissionChecker extends OTCCurrentNumMatchChecker {
	private IRiskService riskService;

	public void setRiskService(IRiskService riskService) {
		this.riskService = riskService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num == Integer.MIN_VALUE) {
			num = 0;
			RiskScene riskScene = riskService.getRiskScene(entId);
			if (riskScene != null) {
				UserRiskScene curr = riskService.getUserRiskScene(
						user.getUserId(), riskScene.getParentId());
				if (curr != null) {
					OneRisk currStageInfo = curr.getStageInfoByIndex(riskScene
							.getSeqId());
					if (currStageInfo != null) {
						num = currStageInfo.getStar();
					}

				}
			}
		}
		return super
				.check(user, userMission, mission, missionLimit, entId, num);

	}
}
