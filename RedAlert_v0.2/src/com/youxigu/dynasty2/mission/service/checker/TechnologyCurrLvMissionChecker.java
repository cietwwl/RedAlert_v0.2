package com.youxigu.dynasty2.mission.service.checker;

import com.youxigu.dynasty2.develop.domain.UserTechnology;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.OTCCurrentNumMatchChecker;
import com.youxigu.dynasty2.user.domain.User;

/**
 * QCT_Study 科技升级<br>
 * @author Administrator
 *
 */
public class TechnologyCurrLvMissionChecker  extends OTCCurrentNumMatchChecker{
	
	private ICastleService castleService;
	
	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		if (num==Integer.MIN_VALUE){
			UserTechnology ut = castleService.getUserTechnologyByEntId(user.getUserId(), entId);
			if(ut!=null){
				num = ut.getLevel();
			}else{
				num=0;
			}
		}
		return super.check(user, userMission, mission, missionLimit, entId, num);
	}

}
