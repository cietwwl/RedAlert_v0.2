package com.youxigu.dynasty2.mission.service.impl;



import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.domain.MissionLimit;
import com.youxigu.dynasty2.mission.domain.UserMission;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 
 * 累计次数任务： <br>
 * 
 * 
 * QCT_Kill 打野怪次数任务<br>
 * QCT_KillMission 击败发布任务“武将ID”N次
 * QCT_Pass 通关“场景ID”N次
 * QCT_Defeat 单挑<br>
 * QCT_Move<br>
 * QCT_Dig<br>
 * QCT_Side<br>
 * QCT_Story<br>
 * QCT_Scene<br>
 * QCT_Defence 制造城防 <br>
 * QCT_War 攻陷营寨 <br>
 * QCT_Use 使用道具<br>
 * QCT_Speedup<br>
 * 
 * 
 * @author Administrator
 * 
 */
public class OTCCumulativeMatchChecker extends AbstractMissionCompleteChecker {

	@Override
	public boolean check(User user, UserMission userMission,Mission mission ,MissionLimit missionLimit,int entId,int num) {
		if (num != Integer.MIN_VALUE) {
			int limitIndex = mission.getLimitIndex(missionLimit);
			int oldNum = userMission.getNum(limitIndex);
			userMission.setNum(limitIndex, oldNum + num);
			return true;
		}
		return false;
	}
	public void doConsume(User user, UserMission userMission,Mission mission ,MissionLimit missionLimit){
		//do nothing
		//累计次数的不消耗任何物品
	}

}
