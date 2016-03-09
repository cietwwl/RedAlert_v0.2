package com.youxigu.dynasty2.combat.skill.render;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

/**
 * 控制类buf的效果处理
 * @author Dagangzi
 *
 */
public class ControlBufRender extends DefaultSkillEffectRender {

	@Override
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action) {
		HeroSkillEffect baseEffect = effect.getEffect();

//		int value = baseEffect.getPara1();
//
//		int random = Util.randInt(1001);
//		if (random > value) {
//			if (log.isDebugEnabled()) {
//				log.debug("控制类效果，随机概率值大于发生概率，忽略");
//			}
//			return;
//
//		}

		if(baseEffect.getPara1() <= 0) {
			effect.setValue(1);	
		}else {
			//TODO 混乱会使用这个效果值
			effect.setValue(baseEffect.getPara1());
		}
		effect.setValidRound(baseEffect.getRound() + combat.getRound() - 1);
		effect.setLastTrigerRound(combat.getRound());

		for (CombatUnit target : targets) {
			target.addSkillEffect(effect, action);
		}
	}

}
