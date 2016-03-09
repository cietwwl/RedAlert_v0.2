package com.youxigu.dynasty2.risk.service.impl;

import java.util.Map;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatTeam;
import com.youxigu.dynasty2.combat.service.impl.DefaultAfterCombatService;

public class RiskAfterCombatService extends DefaultAfterCombatService {
	@Override
	public void doSaveAfterCombat(Combat combat, boolean allEnd,
			Map<String, Object> params) {
		// 恢复兵力
		CombatTeam team = combat.getAttacker();
		if (team.getTeamType() == CombatTeam.TEAM_TYPE_NPCHERO) {
			combatService.saveCombatPf(combat);
			return;
		}
		combat.setParams(null);
		combatService.saveCombatPf(combat);
	}
}
