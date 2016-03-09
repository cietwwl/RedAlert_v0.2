package com.youxigu.dynasty2.combat.skill.render;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.domain.action.StoryAction;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

/*
 * 剧情对话
 */
public class StoryRender extends DefaultSkillEffectRender {

	@Override
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action) {
		HeroSkillEffect baseEffect = effect.getEffect();
		int storyId = baseEffect.getPara1();//可以配置正负值

		//给所有目标加效果
		for (CombatUnit target : targets) {
			if (target.dead()) {
				continue;
			}

			//加展示
			action.addResult(new StoryAction(CombatConstants.ACTION_STORY, target, storyId));
		}
	}

}
