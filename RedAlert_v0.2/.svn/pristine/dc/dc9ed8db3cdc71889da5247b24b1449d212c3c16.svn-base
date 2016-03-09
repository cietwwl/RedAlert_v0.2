package com.youxigu.dynasty2.combat.attack;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.Util;
import com.youxigu.dynasty2.combat.attack.combat.CriticalPhysicsAttack;
import com.youxigu.dynasty2.combat.attack.combat.DodgePhysicsAttack;
import com.youxigu.dynasty2.combat.attack.combat.GeneralPhysicsAttack;
import com.youxigu.dynasty2.combat.attack.combat.ShieldAttack;
import com.youxigu.dynasty2.combat.domain.CombatFactor;
import com.youxigu.dynasty2.combat.domain.CombatUnit;

public class AttackFactory {
	public final static Logger log = LoggerFactory.getLogger(AttackFactory.class);

	// 这些常量不能改变，常量的值参与运算
	public static final short ATTACK_GEN = 20;//普通攻击
	public static final short ATTACK_DODGE = 30;// 攻击被闪避
	public static final short ATTACK_CRITICAL = 40;//暴击
	public static final short ATTACK_SHIELD = 50;//护盾攻击

	private static final Map<Short, IAttack<? extends CombatUnit>> combatAttackMap = new HashMap<Short, IAttack<? extends CombatUnit>>();

	static {
		// 战略战物理攻击
		combatAttackMap.put(ATTACK_GEN, new GeneralPhysicsAttack());
		combatAttackMap.put(ATTACK_DODGE, new DodgePhysicsAttack());
		combatAttackMap.put(ATTACK_CRITICAL, new CriticalPhysicsAttack());
		combatAttackMap.put(ATTACK_SHIELD, new ShieldAttack());
	}

	/**
	 * 返回攻击类型
	 * @param source
	 * @param target
	 * @return
	 */
	public static IAttack<? extends CombatUnit> getAttack(CombatUnit source, CombatUnit target) {
		short attackType = selectPhysicsAttackType(source, target);
		return combatAttackMap.get(attackType);

	}

	/**
	 * 直接返回指定类型的攻击
	 * @param source
	 * @param type
	 * @return
	 */
	public static IAttack<? extends CombatUnit> getAttack(short attackType) {
		return combatAttackMap.get(attackType);

	}

	/**
	 * 选取攻击方式
	 * @param target
	 * @return
	 */
	private static short selectPhysicsAttackType(CombatUnit source, CombatUnit target) {
		//是否闪避==================
		//A命中
		double aglityA = source._getInitHit();

		//B闪避
		double aglityB = target._getInitDodge();

		//命中系数
		CombatFactor dodgeFactor = source.getParent().getParent().getCombatFactors().get(CombatFactor.F_HIT);
		double t = dodgeFactor.getPara1();

		//是否闪避
		double dodgeRate = (1 - aglityA / (aglityA + aglityB * t)) * 1000d;
		int r = Util.randInt(1000);
		if (log.isDebugEnabled()) {
			log.debug("A命中={}", aglityA);
			log.debug("B闪避={}", aglityB);
			log.debug("命中系数={}", t);
			log.debug("闪避={}", dodgeRate);
			log.debug("随机数={}", r);
		}

		if (dodgeRate > 0) {
			if (dodgeRate >= 1000) {
				return ATTACK_DODGE;
			} else {
				if (r <= dodgeRate) {
					return ATTACK_DODGE;
				}
			}
		}

		//是否暴击===================
		//A暴击率
		double critPA = source._getCritAdd();
		//B免暴率
		double critPB = target._getCritDec();

		//暴击系数
		CombatFactor critFactor = source.getParent().getParent().getCombatFactors().get(CombatFactor.F_CRITICAL);
		double critT = critFactor.getPara1();

		//a攻击b暴击率=a暴击/（a暴击+b暴击抵抗）*暴击率系数
		double critRate = 1.0d * critPA / (critPA + critPB) * critT * 1000;

		if (log.isDebugEnabled()) {
			log.debug("a暴击千分比加成={}", critPA);
			log.debug("b暴击免伤千分比加成={}", critPB);
			log.debug("暴击系数={}", critT);
			log.debug("暴击={}", critRate);
		}

		if (critRate > 0) {
			if (critRate >= 1000) {
				return ATTACK_CRITICAL;
			} else {
				r = Util.randInt(1000);

				if (log.isDebugEnabled()) {
					log.debug("随机数={}", r);
				}

				if (r <= critRate) {
					return ATTACK_CRITICAL;
				}
			}
		}

		return ATTACK_GEN;
	}
}
