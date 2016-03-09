package com.youxigu.dynasty2.combat.skill.cheak.skill;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;

/**
 * 多名武将同时在场[合击校验]
 * @author Dagangzi
 *
 */
public class PunchLimit implements ISkillFiredLimitValidator {

	@Override
	public boolean checked(CombatSkill skill, HeroSkillLimit limit, CombatUnit source) {
		String entIdstr = limit.getPara2();
		if (entIdstr != null && !entIdstr.equals("")) {
			String[] entIds = entIdstr.trim().split(",");
			if (entIds != null && entIds.length > 0) {
				List<CombatUnit> units = source.getParent().getUnits();
				for (String _entId : entIds) {
					int entId = Integer.parseInt(_entId);
					if (entId <= 0) {
						continue;
					}
					boolean flag = false;
					for (CombatUnit unit : units) {
						if (!unit.dead() && unit.getUnitEntId() == entId) {
							//系统武将entId是否匹配
							flag = true;
							break;
						}
					}
					if (!flag) {
						return false;
					}
				}
				return true;
			}

		}
		return false;
	}

}
