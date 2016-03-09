package com.youxigu.dynasty2.combat.skill.render;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.CanotAttackedCombatUnit;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.WallCombatUnit;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

/**
 * 清除全体目标身上指定的的buff效果
 * @author Dagangzi
 *
 */
public class ClearConfRender extends DefaultSkillEffectRender {
	@Override
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action) {
		HeroSkillEffect baseEffect = effect.getEffect();
		String str = baseEffect.getPara2();
		if(str == null || str.equals("")) {
			return;
		}
		
		String[] buffs = str.trim().split(",");
		for(String buff : buffs) {
			for (CombatUnit target : targets) {
				if (!target.dead()) {
					if (!(target instanceof CanotAttackedCombatUnit)
							&& !(target instanceof WallCombatUnit)) {
						target.removeSkillEffect(buff, action);
					}
				}
			}
		}
	}
}
