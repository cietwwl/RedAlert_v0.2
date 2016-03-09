package com.youxigu.dynasty2.combat.service.impl;

import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.UtilDate;
import com.youxigu.dynasty2.chat.client.IChatClientService;
import com.youxigu.dynasty2.combat.dao.ICombatDao;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.service.IAtferCombatService;
import com.youxigu.dynasty2.combat.service.ICombatService;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.develop.service.ICastleService;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IAccountService;
import com.youxigu.dynasty2.user.service.IUserAchieveService;
import com.youxigu.dynasty2.user.service.IUserEffectService;
import com.youxigu.dynasty2.user.service.IUserService;

/**
 * 默认的 战斗完毕后数据处理：只处理的PVP类的数据恢复
 * 
 * 
 * Combat.COMBAT_TYPE_PVP_ROB, Combat.COMBAT_TYPE_PVP_DESTROY
 * 
 * @author Administrator
 * 
 */
public class DefaultAfterCombatService implements IAtferCombatService {
	public final Logger log = LoggerFactory.getLogger(this.getClass());

	protected ICombatService combatService;
	protected ICombatDao combatDao;
	protected IUserEffectService userEffectService;
	protected IUserService userService;
	protected IHeroService heroService;
	protected ICastleService castleService;
	protected IChatClientService messageService;
	protected ICastleEffectService castleEffectService;
	protected IUserAchieveService userAchieveService;
	protected ICommonService commonService;
	protected IAccountService accountService;

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public void setUserAchieveService(IUserAchieveService userAchieveService) {
		this.userAchieveService = userAchieveService;
	}

	public void setCombatService(ICombatService combatService) {
		this.combatService = combatService;
	}

	public void setCombatDao(ICombatDao combatDao) {
		this.combatDao = combatDao;
	}

	public void setCastleEffectService(ICastleEffectService castleEffectService) {
		this.castleEffectService = castleEffectService;
	}

	public void setMessageService(IChatClientService messageService) {
		this.messageService = messageService;
	}

	public void setCastleService(ICastleService castleService) {
		this.castleService = castleService;
	}

	public void setUserEffectService(IUserEffectService userEffectService) {
		this.userEffectService = userEffectService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setHeroService(IHeroService heroService) {
		this.heroService = heroService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	public void doSaveAfterCombat(Combat combat, boolean allEnd,
			Map<String, Object> params) {

		// 发送战斗消息
		if (log.isDebugEnabled()) {
			log.debug("开始保存战斗信息{}", combat.getCombatId());
		}
		//
		// // 是否是夜间出征衰减模式
		// boolean isInArmyoutAttenuation = combatService
		// .isInArmyoutAttenuation((ArmyOut) combat.getParams());
		//
		// Castle atkCas = castleService.getCastleById(combat.getAttacker()
		// .getCasId());
		// Castle defCas = castleService.getCastleById(combat.getDefender()
		// .getCasId());
		//
		// CombatTeam atkTeam = combat.getAttacker();
		// CombatTeam defTeam = combat.getDefender();
		//
		// sendAttackerCombatMessage(combat, true);
		// sendDefenderCombatMessage(combat, true);
		//
		// // 发送联盟频道消息
		// // sendGuildMessage(combat, atkCas, defCas);
		//
		// // 处理联盟荣誉
		// // setGuildHounor(combat, atkCas, defCas, isInArmyoutAttenuation);
		//
		// // 回复进攻方
		//
		// resumeAttackerCombatTeam(atkTeam);
		// // 协防玩家走出征处理
		// if (defTeam.getTeamType() == CombatTeam.TEAM_TYPE_PLAYERHERO_ASSIT) {
		// // 更新协防玩家的出征记录
		// ArmyOut armyout1 = (ArmyOut) defTeam.getParams();
		// if (armyout1 != null) {
		// if (combat.getWinType() == CombatConstants.WIN_ATK) {
		// armyout1 = lockArmyout(armyout1);
		// armyout1.setArmyCount("");// 清除兵记录
		// combatDao.updateArmyOut(armyout1);
		// // 协防玩家失败则直接返回
		// combatService.doStartArmyoutBackJob(armyout1, false,
		// combat.getCombatId());
		// } else {
		// armyout1 = lockArmyout(armyout1);
		// armyout1.setCombatId(combat.getCombatId());
		// this.resumeCounquerCombatTeam(defTeam, true, 0);
		// combatDao.updateArmyOut(armyout1);
		// }
		// }
		// } else {
		// // 回复守方兵力
		// resumeDefenderCombatTeam(defTeam);
		// }
		//
		// // 更新出征记录与战斗ID的关联
		// ArmyOut armyout = (ArmyOut) combat.getParams();
		// if (armyout != null) {
		// armyout = lockArmyout(armyout);
		// armyout.setCombatId(combat.getCombatId());
		// //
		// 设置成战斗状态，战斗后返回会改成Back状态，这里设置战斗状态的目的是防止战斗导致被攻击方流亡，流亡功能会强制返回所有前进以及驻守状态的出征。
		// armyout.setStatus(ArmyOut.STATUS_COMBAT);
		// combatDao.updateArmyOut(armyout);
		// }
		//
		// if (allEnd) {
		// // 这里处理联盟与城池，
		// doSaveAfterAllCombat(combat, atkCas, defCas, isInArmyoutAttenuation);
		// }
		//
		// // 发送战报邮件
		// // doSendCombatReportMail(combat, allEnd, isInArmyoutAttenuation);
		// // 保存战斗对象，目前是保存在远程缓存中,12小时,这里只保存到远程缓存，目的是去除transient属性值
		// combat.setParams(null);
		// combatService.saveCombat(combat);

	}

	/**
	 * 取死亡回复率
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public int getArmyResume(User user, int type) {
		long daySecond = 24 * 60 * 60;
		Timestamp now = UtilDate.nowTimestamp();
		if (UtilDate.moveSecond(user.getCreateDate(), 7 * daySecond)
				.before(now)) {
			return 0;
		}
		long distance = UtilDate.secondDistance(now, user.getCreateDate());
		int day = (int) (distance / daySecond) + 1;

		int rate = 0;
		if (day > 0) {
			if (type == CombatConstants.COMBAT_TYPE_RISK) {// 如果是冒险
				rate = Math.min((int) (130 - day * 10), 100);
			} else {
				rate = Math.min((int) (160 - day * 20), 100);
			}
		}
		return rate;
	}

}
