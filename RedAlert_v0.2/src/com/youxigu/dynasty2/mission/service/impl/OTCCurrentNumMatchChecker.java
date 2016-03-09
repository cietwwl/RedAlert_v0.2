package com.youxigu.dynasty2.mission.service.impl;

import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 当前数量匹配 任务：必须当前拥有的值匹配，即使以前到达过这个值也不算完成
 * 
 * QCT_Recruit 武将雇佣数量<br>
 * QCT_Hero 雇佣指定的武将<br> 
 * QCT_Equip 任意武将身上装备达到N个
 * QCT_HeroLevel 任意武将升级到N级
 * QCT_Drill/QCT_ARMY 训练兵种/冒险掉落兵种<br>
 * QCT_Farm 农场开工地块<br>
 * QCT_Lead 武将当前配兵<br>
 * QCT_League 当前是否加入联盟<br>
 * QCT_Level 建筑升级任务 <br>
 * QCT_Upleague联盟升级<br>
 * QCT_Study 科技升级<br>
 * QCT_Item 道具的当前数量<br>
 * QCT_Resource 囤积资源的当前数量 <br>
 * 
 * @author Administrator
 * 
 */
public class OTCCurrentNumMatchChecker extends AbstractMissionCompleteChecker {

	@Override
	public boolean check(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit, int entId, int num) {
		// if (num != Integer.MIN_VALUE) {
		int limitIndex = mission.getLimitIndex(missionLimit);
		int oldNum = userMission.getNum(limitIndex);
		if (oldNum != num) {
			userMission.setNum(limitIndex, num);
			return true;
		}
		// }
		return false;
	}

	@Override
	public void doConsume(User user, UserMission userMission, Mission mission,
			MissionLimit missionLimit) {
		// 不消耗
	}
}