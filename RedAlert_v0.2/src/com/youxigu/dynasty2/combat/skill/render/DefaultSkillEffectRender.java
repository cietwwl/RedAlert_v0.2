package com.youxigu.dynasty2.combat.skill.render;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.CombatUnitAction;
import com.youxigu.dynasty2.combat.domain.action.MoraleAddAction;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillEffectRender;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

/**
 * 默认技能效果render
 * @author Dagangzi
 *
 */
public class DefaultSkillEffectRender implements ISkillEffectRender {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public AbstractCombatAction applyEffect(CombatSkillEffect effect,
			CombatUnit target) {
		return null;
	}

	@Override
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action) {

		HeroSkillEffect baseEffect = effect.getEffect();

		//默认取参数1的值
		int value = baseEffect.getPara1();

		if (value == 0) {
			log.error("技能效果的效果值为0，忽略该效果:effId={}", baseEffect.getEffId());
			return;
		}

		effect.setValue(value);

		//当前回合生效
		effect.setValidRound(baseEffect.getRound() + combat.getRound() - 1);
		effect.setLastTrigerRound(combat.getRound());

		//给所有目标加效果
		List<Integer> missList = new ArrayList<Integer>();
		for (CombatUnit target : targets) {
			//目标有免疫时对持续性buff免疫，
			if (effect.getEffType() != HeroSkillEffect.EFF_TYPE_GEN
					&& target.containsEffect(CombatSkillEffect.EFF_IMMUNE)) {
				missList.add(target.getId());
				continue;
			}
			target.addSkillEffect(effect, action);
		}

		// 原有的miss集+免疫掉的=新的miss集
		int[] missIds = action.getMissIds();
		if (missIds != null && missIds.length > 0) {
			for (int id : missIds) {
				missList.add(id);
			}
		}

		int[] missIds2 = new int[missList.size()];
		for (int i = 0; i < missList.size(); i++) {
			missIds2[i] = missList.get(i);
		}
		action.setMissIds(missIds2);
	}

	/**
	 * 增加士气
	 * @param attacker
	 * @param defender
	 * @param action
	 */
	protected void addMorale(CombatUnit unit, int addMorale, CombatUnitAction action) {
		if (addMorale > 0 && !unit.dead()) {
			unit.setCurMorale(unit.getCurMorale() + addMorale);

			//加展示
			action.addResult(new MoraleAddAction(CombatConstants.ACTION_ADD_MORALE, unit,
					CombatConstants.ACTION_ATTACK_HIT, addMorale));

			if (log.isDebugEnabled()) {
				log.debug("{}增加{}点士气", unit.getName(), addMorale);
			}
		}
	}
}
