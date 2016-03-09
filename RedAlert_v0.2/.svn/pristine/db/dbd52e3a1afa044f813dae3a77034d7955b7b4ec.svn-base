package com.youxigu.dynasty2.combat.skill.render;

import java.util.List;

import com.youxigu.dynasty2.combat.attack.AttackFactory;
import com.youxigu.dynasty2.combat.attack.IAttack;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

/*
 * 普通攻击的效果处理
 * HeroSkillEffect para1=0 需要走命中和暴击  20;//普通攻击  30;// 攻击被闪避   40;//暴击  50;//护盾攻击
 */
public class AttackRender extends DefaultSkillEffectRender {

	@Override
	public void doRender(Combat combat, CombatSkill skill,
			CombatSkillEffect effect, List<CombatUnit> owners,
			List<CombatUnit> targets,
			SkillEffectAction action) {
		HeroSkillEffect baseEffect = effect.getEffect();
		int attackType = baseEffect.getPara1();

		// 释放技能者 不一定和 释放效果者相同
		CombatUnit owner = effect.getOwner();

		//给所有目标加效果
		boolean isAttack = false;// 是否命中
		// 选取攻击方式
		for (CombatUnit target : targets) {
			if (!target.dead()) {
				IAttack attack = null;
				double harm = 0d;
				double oldHarm = target.getCurrHarm();
				for (CombatUnit source : owners) {
					if (log.isDebugEnabled()) {
						log.debug("--------------{}准备对{}发起进攻---------", source.getName(), target.getName());

						log.debug("****选取攻击方式****");
					}
					if (attackType == 1) {
						attack = AttackFactory.getAttack(source, target);
					} else {
						attack = AttackFactory.getAttack((short) attackType);
					}

					if (log.isDebugEnabled()) {
						log.debug("****选取{}方式****", CombatConstants.attackTypeMap.get(attack.getAttackType()));
					}

					// 发动攻击
					attack.attack(source, target, action);

					// 累计伤害
					harm = harm + target.getCurrHarm();

					if (!isAttack) {
						isAttack = CombatConstants
								.isAttacked("" + attack.getAttackType());
					}

					if (target.dead()) {
						if (log.isDebugEnabled()) {
							log.debug("{}死亡", target.getName());
						}
						break;
					}
				}

				// 造成伤害给防守方加士气
				if (oldHarm != harm) {
					this.addMorale(target, target._getDefendMorale(), action);
				}

				if (log.isDebugEnabled()) {
					log.debug("--------------ending---------");
				}

				// 被攻击后触发被攻技能
				// 护盾技能不触发被动
				if (attack.getAttackType() != CombatConstants.ACTION_SHIELD_ATTACK
						&& attack.getAttackType() != CombatConstants.ACTION_ATTACK_DODGE) {
					if (skill.getSkill().getFiredAt() != HeroSkill.FIRED_AT_ATTACKED) {
						//被动技能触发被动技能
						target.doFireSkill(HeroSkill.FIRED_AT_ATTACKED, combat.getLastSubActions());
					}
				}
			}
		}

		// 有一次命中就给释放技能者加士气
		if (isAttack) {
			this.addMorale(owner, owner._getAttackMorale(), action);
		}
	}

}
