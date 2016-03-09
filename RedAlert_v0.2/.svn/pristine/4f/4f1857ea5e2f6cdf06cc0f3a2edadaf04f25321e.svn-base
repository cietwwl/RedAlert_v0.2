package com.youxigu.dynasty2.combat.skill.render;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.MoraleAddAction;
import com.youxigu.dynasty2.combat.domain.action.MoraleDecAction;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

/*
 * 加士气或是减士气
 */
public class MoraleRender extends DefaultSkillEffectRender {

	@Override
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action) {
		HeroSkillEffect baseEffect = effect.getEffect();
		int morale = baseEffect.getPara1();//可以配置正负值

		//给所有目标加效果
		for (CombatUnit target : targets) {
			if (target.dead()) {
				continue;
			}
			
			target.setCurMorale(target.getCurMorale() + morale);
			
			//加展示
			if(morale >0) {
				action.addResult(new MoraleAddAction(CombatConstants.ACTION_ADD_MORALE, target,
						CombatConstants.ACTION_EFFECT, morale));
			}else {
				action.addResult(new MoraleDecAction(CombatConstants.ACTION_DEC_MORALE, target,
						CombatConstants.ACTION_EFFECT, morale));
			}
			
		}
	}

}
