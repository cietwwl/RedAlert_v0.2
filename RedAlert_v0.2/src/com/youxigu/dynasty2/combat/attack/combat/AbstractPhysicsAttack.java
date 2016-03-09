package com.youxigu.dynasty2.combat.attack.combat;

import com.manu.util.Util;
import com.youxigu.dynasty2.combat.domain.CombatFactor;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.SysHero;

public abstract class AbstractPhysicsAttack extends AbstractCombatAttack {
	/**
	 * 伤害值计算:<br>
	 * @param attacker
	 * @param defender
	 * @return
	 */
	public double getCoreHarmNum(CombatUnit attacker, CombatUnit defender) {
		//目标有免疫时对持续性buff免疫，
		if (defender.containsEffect(CombatSkillEffect.EFF_INVINCIBLE)) {
			return 0;
		}

		double attackA = attacker._getPhysicalAttack();
		double defenseB = defender._getPhysicalDefend();
		if (attacker.getAttackType() == SysHero.ATTACKTYPE_MAGIC) {
			attackA = attacker._getMagicAttack();
			defenseB = defender._getMagicDefend();
		}

		//兵种相克系数
		double t = defender._getArmyBiteRoit() / 1000d;

		//TODO 伤害修正系数
		CombatFactor combatFactor = attacker.getParent().getParent().getCombatFactors().get(CombatFactor.F_HARM);
		double sx = combatFactor.getPara1();

		// 随机数
		double r = (Util.randInt(21) + 90) / 100d;

		//攻击方伤害千分比buff
		double aBonus = attacker._getDamageAdd() / 1000d;

		//防守方的伤害千分比debuff
		double bBonus = defender._getDamageDec() / 1000d;

		//攻击方固定伤害
		double aDemage = attacker._getDamage();

		//防守方方固定免伤
		double bDemage = defender._getShield();

		double harm = Math.pow(attackA, 2) * aBonus / (attackA + defenseB / 3) * bBonus * t * sx * r + aDemage
				- bDemage;

		if (log.isDebugEnabled()) {
			log.debug("攻击方式=" + (attacker.getAttackType() == SysHero.ATTACKTYPE_MAGIC ? "法术攻击" : "物理攻击"));
			log.debug("attackA={}", attackA);
			log.debug("defenseB={}", defenseB);
			log.debug("兵种相克系数={}", t);
			log.debug("伤害修正系数={}", sx);
			log.debug("随机数={}", r);
			log.debug("aBonus={}", aBonus);
			log.debug("bBonus={}", bBonus);
			log.debug("aDemage={}", aDemage);
			log.debug("bDemage={}", bDemage);
			log.debug("总伤害:{}", harm);
		}

		return harm;
	}

	/**
	 * 合击计算
	 * @param attacker
	 * @param defender
	 * @return
	 */
	public int[] getTogetherCoreHarmNum(CombatUnit attacker, CombatUnit defender) {
		double harm = 0;
		CombatUnit[] sources = attacker.getSources();
		int[] entIds = null;
		if (sources != null) {
			entIds = new int[sources.length];
			// 合击
			int i = 0;
			for (CombatUnit unit : sources) {
				entIds[i] = unit.getId();
				i = i + 1;
				if (unit == null || unit.dead()) {
					continue;
				}
				harm += getCoreHarmNum(unit, defender);
			}
		} else {
			entIds = new int[1];
			//普通攻击
			harm = getCoreHarmNum(attacker, defender);
			entIds[0] = attacker.getId();
		}
		
		defender.setCurrHarm(defender.getCurrHarm() + harm);
		
		return entIds;
	}

	/**
	 * 护盾计算公式
	 * @param attacker
	 * @param defender
	 * @return
	 */
	public double getShieldHarmNum(CombatUnit attacker, CombatUnit defender) {//A攻击
		double attackA = attacker._getPhysicalAttack();
		if (attacker.getAttackType() == SysHero.ATTACKTYPE_MAGIC) {
			attackA = attacker._getMagicAttack();
		}

		//B最大血量
		double initHpB = defender.getInitHp();

		// 随机数
		double r = (Util.randInt(21) + 90) / 100d;

		//护盾系数
		CombatFactor combatFactor = attacker.getParent().getParent().getCombatFactors().get(CombatFactor.F_SHIELD);
		double hT = combatFactor.getPara1();

		//攻击方伤害千分比buff
		double aBonus = 1.0d + attacker._getDamageAdd() / 1000d;

		double harm = (int) (Math.atan(attackA / 100d) / 5 * r * hT * initHpB * aBonus);

		if (log.isDebugEnabled()) {
			log.debug("attackA={}", attackA);
			log.debug("initHpB={}", initHpB);
			log.debug("r={}", r);
			log.debug("hT={}", hT);
			log.debug("aBonus:{}", aBonus);
			log.debug("总护盾:{}", harm);
		}

		return harm;
	}

}
