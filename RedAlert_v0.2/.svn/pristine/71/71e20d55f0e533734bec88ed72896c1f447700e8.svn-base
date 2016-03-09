package com.youxigu.dynasty2.combat.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.combat.dao.ICombatDao;
import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatConstants;
import com.youxigu.dynasty2.combat.domain.CombatFactor;
import com.youxigu.dynasty2.combat.domain.CombatTeam;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.FireSkillAction;
import com.youxigu.dynasty2.combat.domain.action.RoundChangeAction;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.service.ICombatEngine;
import com.youxigu.dynasty2.entity.domain.HeroSkill;

/**
 * 战斗服务
 * 
 * @author Administrator
 * 
 */
public class CombatEngine implements ICombatEngine {
	public static final Logger log = LoggerFactory.getLogger(CombatEngine.class);

	private ICombatDao combatDao;

	private static AttackComparator atkComparator = new AttackComparator();
	private Map<String, CombatFactor> combatFactors = new HashMap<String, CombatFactor>(5);//战斗中系数

	public void setCombatDao(ICombatDao combatDao) {
		this.combatDao = combatDao;
	}

	/**
	 * 战力从高到低排序
	 * @author Dagangzi
	 */
	static class AttackComparator implements Comparator<CombatUnit> {
		@Override
		public int compare(CombatUnit unit1, CombatUnit unit2) {
			return unit2._getCurrentPower() - unit1._getCurrentPower();
		}
	}

	public void init() {
		List<CombatFactor> factors = combatDao.getCombatFactor();
		for (CombatFactor factor : factors) {
			combatFactors.put(factor.getFormula(), factor);
		}
	}

	@Override
	public void execCombat(Combat combat) {
		long time = System.currentTimeMillis();

		CombatTeam atkTeam = combat.getAttacker();
		CombatTeam defTeam = combat.getDefender();
		if (atkTeam == null || defTeam == null) {
			return;
		}

		//携带战斗系数
		combat.setCombatFactors(combatFactors);

		List<CombatUnit> atkUnits = atkTeam.getUnits();
		List<CombatUnit> defUnits = defTeam.getUnits();
		if (defUnits == null || defUnits.size() == 0) {
			combat.setWinType(CombatConstants.WIN_ATK);
		} else if (atkUnits == null || atkUnits.size() == 0) {
			combat.setWinType(CombatConstants.WIN_DEF);
		} else {
			List<CombatUnit> units = new ArrayList<CombatUnit>();
			units.addAll(atkUnits);
			units.addAll(defUnits);

			if (units.size() > 1) {
				//战斗开始前和回合前技能,按攻击顺序排序
				Collections.sort(units, atkComparator);
			}

			if (log.isDebugEnabled()) {
				log.debug("进攻方：{}", atkTeam.getTeamName());
				log.debug("防守方：{}", defTeam.getTeamName());
				log.debug("==============输出战斗单元战斗初始数据开始==============");
				Iterator<CombatUnit> itl = units.iterator();
				while (itl.hasNext()) {
					CombatUnit unit = itl.next();
					if (unit == null) {
						//过滤未上阵的将
						itl.remove();
						continue;
					}
					StringBuilder sb = new StringBuilder(512);
					sb.append("名称:").append(unit.getName());
					sb.append("entId:").append(unit.getUnitEntId());
					sb.append(",等级:").append(unit.getLevel());
					sb.append(",兵种/血量:").append(unit._getArmyName()).append("/").append(unit.getTotalHp());
					sb.append(",攻击类型:").append(unit.getAttackType() == 1 ? "物理" : "法术").append(",战斗力:")
							.append(unit._getCurrentPower());
					sb.append(",物理攻击:").append(unit._getPhysicalAttack());
					sb.append(",物理防御:").append(unit._getPhysicalDefend());
					sb.append(",法术攻击:").append(unit._getMagicAttack());
					sb.append(",法术防御:").append(unit._getMagicDefend());
					log.debug(sb.toString());
				}
				log.debug("==============输出战斗单元战斗初始数据完毕===============");
			}

			//开战前的行为集合
			List<AbstractCombatAction> subActions = combat.getPrevActions();
			if (subActions == null) {
				subActions = new LinkedList<AbstractCombatAction>();
				combat.setPrevActions(subActions);
			}

			//混编后按战力优先释放开战前的技能
			beforeCombat(combat, units, subActions);

			// 进行战斗，直到一方队伍全灭
			while (!combat.isEnd()) {
				// long time = System.currentTimeMillis();
				beforeNextRound(combat, units);

				doNextRound(combat);

				afterRound(combat, units);
			}

			//攻方负或是达到最大回合算守方胜利
			if (combat.getRound() >= CombatConstants.MAX_ROUND || combat.getAttacker().isFailure()) {
				combat.setWinType(CombatConstants.WIN_DEF);
			} else {
				combat.setWinType(CombatConstants.WIN_ATK);
			}

		}
		//计算得分
		combat.atferCombat();

		if (log.isDebugEnabled()) {
			// log消息内容
			System.out.println(combat.showLog());
			log.debug("一场战斗{}的战斗时间:{}", combat.getCombatId(), (System.currentTimeMillis() - time));
			//			log.debug(combat.showLog());
		}

	}
	
	/**
	 * 开战前
	 * @param combat
	 * @param units
	 */
	protected void beforeCombat(Combat combat, List<CombatUnit> units, List<AbstractCombatAction> subActions) {
		if (log.isDebugEnabled()) {
			log.debug("==============开战前===============");
		}
		
		//默认出手权
		int attackRight = combat.getAttackRight();
		while (true) {
			//最多查找count次
			CombatUnit attackUnit = null;
			attackUnit = this.getFightUnit(combat, attackUnit, attackRight, units);
			attackRight = this.getAttackRight(attackUnit, attackRight);
			if (attackUnit == null) {
				attackUnit = this.getFightUnit(combat, attackUnit, attackRight, units);
				attackRight = this.getAttackRight(attackUnit, attackRight);
			}

			if (attackUnit == null) {
				break;
			}

			//施放回合前的技能
			attackUnit.doFireSkill(HeroSkill.FIRED_AT_BEGIN, subActions);

			if (combat.isEnd()) {
				return;
			}
		}
	}

	/**
	 * 每回合前要做的工作
	 * @param combat
	 * @param units  攻守双方的unit按战力排序出手
	 */
	protected void beforeNextRound(Combat combat, List<CombatUnit> units) {
		// 增加回合计数
		int round = combat.increaseRound();

		//回合变更行为
		RoundChangeAction rAction = new RoundChangeAction(CombatConstants.ACTION_ROUNDCHANGE, (byte) round);
		if (round == 1) {
			combat.getPrevActions().add(rAction);
		} else {
			combat.getLastSubActions().add(rAction);
		}
		if (log.isDebugEnabled()) {
			log.debug("开始第{}轮回合前战斗", round);
		}

		//回合行为集合
		List<AbstractCombatAction> subActions = new java.util.LinkedList<AbstractCombatAction>();
		combat.addSubActions(subActions);

		// 释放回合开始前技能

		//重置出手状态
		combat.resetCurrRoundAttacked();

		//默认出手权
		int attackRight = combat.getAttackRight();
		while (true) {
			//最多查找count次
			CombatUnit attackUnit = null;
			attackUnit = this.getFightUnit(combat, attackUnit, attackRight, units);
			attackRight = this.getAttackRight(attackUnit, attackRight);
			if (attackUnit == null) {
				attackUnit = this.getFightUnit(combat, attackUnit, attackRight, units);
				attackRight = this.getAttackRight(attackUnit, attackRight);
			}

			if (attackUnit == null) {
				break;
			}

			//施放回合前的技能
			attackUnit.doFireSkill(HeroSkill.FIRED_AT_ROUNDBEGIN, subActions);

			if (combat.isEnd()) {
				return;
			}
		}
	}

	/**
	 * 每回合后要做的工作
	 * 
	 * @param combat
	 * @param units
	 */
	protected void afterRound(Combat combat, List<CombatUnit> units) {
		if (log.isDebugEnabled()) {
			log.debug("开始第{}轮回合后战斗", combat.getRound());
		}
		if (!combat.isEnd()) {
			// List<AbstractCombatAction> subActions =
			// combat.getLastSubActions();

			// 在最后一个技能行为的效果集里，添加特殊的DOT效果生效
			FireSkillAction lastSkillAction = combat.getLastSkillAction();
			if(lastSkillAction == null){
				return;
			}
			SkillEffectAction skillEffectAction = new SkillEffectAction(
					CombatConstants.DOT_EFFECT_TRIGERED, lastSkillAction);
			lastSkillAction.addResult(skillEffectAction);

			Iterator<CombatUnit> lit = units.iterator();
			while (lit.hasNext()) {
				CombatUnit unit = lit.next();
				if (unit.dead()) {
					unit.removeAllkillEffect();
					lit.remove();
				} else {
					if (lastSkillAction != null) {
						// 释放DOTA效果
						List<AbstractCombatAction> subActions = skillEffectAction
								.getResults();
						unit.applySkillEffect(subActions);
					}

					//清除过期的技能效果
					unit.removeSkillEffect(combat.getRound());
				}
			}

		}
	}

	/**
	 * 每轮战斗： <br>
	 * team战力决定优先出手的一方
	 * 双方交替出手权，出手方每次一人出手，从站位低到高<br>
	 * 循环直到所有的战斗单元战斗过
	 * @param combat
	 */
	protected void doNextRound(Combat combat) {
		List<CombatUnit> units = new ArrayList<CombatUnit>();
		units.addAll(combat.getAttacker().getUnits());
		units.addAll(combat.getDefender().getUnits());
		
		if (log.isDebugEnabled()) {
			log.debug("开始第{}轮战斗", combat.getRound());
		}
		
		//出手权 1=A方  2=B方 -1=永久A方  -2=永久B方
		int attackRight = combat.getAttacker().getTeamPower() - combat.getDefender().getTeamPower() > 0 ? 1 : 2;
		//重置出手状态
		combat.resetCurrRoundAttacked();
		//本次出手的unit
		while (true) {
			//最多查找count次
			CombatUnit attackUnit = null;
			attackUnit = this.getFightUnit(combat, attackUnit, attackRight, units);
			attackRight = this.getAttackRight(attackUnit, attackRight);
			if (attackUnit == null) {
				attackUnit = this.getFightUnit(combat, attackUnit, attackRight, units);
				attackRight = this.getAttackRight(attackUnit, attackRight);
			}

			if (attackUnit == null) {
				break;
			}

			//施放回合中的技能
			attackUnit.doFireSkill(HeroSkill.FIRED_AT_AFTERATK);

			if (combat.isEnd()) {
				return;
			}
		}
	}

	private CombatUnit getFightUnit(Combat combat, CombatUnit fightUnit, int attackerFirst, List<CombatUnit> units) {
		boolean isDeffender = (Math.abs(attackerFirst) == 2);

		Iterator<CombatUnit> itl = units.iterator();
		while (itl.hasNext()) {
			CombatUnit unit = itl.next();

			//死亡后移除
			if (unit == null || unit.dead()) {
				itl.remove();
				continue;
			}

			//已经出过手了
			if (unit.isCurrRoundAttacked()) {
				continue;
			}

			//出手权不一致
			if (isDeffender != (unit.getParent() == combat.getDefender())) {
				continue;
			}

			fightUnit = unit;
			unit.setCurrRoundAttacked(true);
			break;
		}

		return fightUnit;
	}

	/**
	 * 交换出手权
	 * @param fightUnit
	 * @param attackRight
	 * @return
	 */
	private int getAttackRight(CombatUnit fightUnit, int attackRight) {
		if (attackRight < 0) {
			//			return attackerFirst;
		} else {
			if (fightUnit == null) {
				//没有找到or已经是最后一个就永久切换出手权
				attackRight = (attackRight == 1 ? -2 : -1);
			} else {
				//切换出手权
				attackRight = (attackRight == 1 ? 2 : 1);
			}
		}
		return attackRight;
	}

	@Override
	public CombatFactor getCombatFactor(String formula) {
		// TODO Auto-generated method stub
		return combatFactors.get(formula);
	}

}
