package com.youxigu.dynasty2.mission.service.award;

import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.log.imp.LogHpAct;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionAward;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.mission.service.impl.AbstractMissionAward;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;

/**
 * 奖励：君主体力
 * 
 * @author Administrator
 * 
 */
public class UserHpAward extends AbstractMissionAward {

	private IUserService userService;
	private ICastleService castleService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	@Override
	public void doAward(User user, MissionAward award, Mission mission,
			UserMission userMission) {
		int num = this.calcAward(award.getNum(), userMission);

		userService.doAddHpPoint(user, num,LogHpAct.Mission_ADD);
//		Map<String, Object> params = new HashMap<String, Object>(1);
//		params.put("hpPoint", user.getHpPoint());
//		// 体力上限
//		int hpPointLimit = userService.getHpPointMax(user);
//		params.put("hpPointLimit", hpPointLimit);
//		castleService.sendFlushDevDataEvent(user.getUserId(),EventMessage.TYPE_USER_CHANGED, params);
	}
}
