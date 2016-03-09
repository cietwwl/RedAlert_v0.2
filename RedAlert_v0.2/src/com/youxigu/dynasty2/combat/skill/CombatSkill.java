package com.youxigu.dynasty2.combat.skill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.combat.domain.Combat;
import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.FireSkillAction;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffectLimit;

/**
 * 技能释放状态
 * @author Dagangzi
 *
 */
public class CombatSkill implements java.io.Serializable {
	protected static final Logger log = LoggerFactory.getLogger(CombatSkill.class);
	/**
	 * 技能定义
	 */
	private HeroSkill skill;

	/**
	 * 最后一次施放的回合
	 */
	private int firedRound = -1;
	/**
	 * 技能下一次可施放技能的回合数
	 */
	private int nextFiredRound = 0;

	/**
	 * 已经释放的总次数
	 */
	private int firedCount;

	public CombatSkill() {
	}

	public CombatSkill(HeroSkill skill) {
		this.skill = skill;
	}

	public HeroSkill getSkill() {
		return skill;
	}

	public void setSkill(HeroSkill skill) {
		this.skill = skill;
	}

	public int getFiredRound() {
		return firedRound;
	}

	public void setFiredRound(int firedRound) {
		this.firedRound = firedRound;
	}

	public int getNextFiredRound() {
		return nextFiredRound;
	}

	public void setNextFiredRound(int nextFiredRound) {
		this.nextFiredRound = nextFiredRound;
	}

	public int getFiredCount() {
		return firedCount;
	}

	public void setFiredCount(int firedCount) {
		this.firedCount = firedCount;
	}

	/**
	 * 取得技能的优先级
	 * @return
	 */
	public int getPriority() {
		return skill.getPriority();
	}

	/**
	 * 冷却回合数
	 * @return
	 */
	public int getRoundPeriod() {
		return skill.getRoundPeriod();
	}

	/**
	 * 取得技能id
	 * @return
	 */
	public int getSkillId() {
		return skill.getEntId();
	}

	/**
	 * 技能效果施放
	 * 检查效果释放条件
	 * @param combat
	 * @param owner
	 * @param combatSkill
	 * @param actions
	 * @return
	 */
	public FireSkillAction doFire(Combat combat, CombatUnit owner, CombatSkill combatSkill,
			List<AbstractCombatAction> actions) {
		//释放技能前，要删除所有人身上临时效果，被动技能时保留
		if (combatSkill.skill.getFiredAt() != HeroSkill.FIRED_AT_ATTACKED) {
			for (CombatUnit unit : combat.getAttacker().getUnits()) {
				unit.removeTmpSkillEffectValues();
				unit.setCurrHarm(0);
			}

			for (CombatUnit unit : combat.getDefender().getUnits()) {
				unit.removeTmpSkillEffectValues();
				unit.setCurrHarm(0);
			}
		}

		//回合行为中加入技能action
		int consumeMorale = combatSkill.getSkill().getMorale();
		FireSkillAction skillAction = new FireSkillAction(skill.getEntId(), (short) skill.getRoundPeriod(),
				consumeMorale, owner);
		actions.add(skillAction);
		// 设置最后一个技能行为
		combat.setLastSkillAction(skillAction);

		if (log.isDebugEnabled()) {
			log.debug(owner.getName() + ",释放[" + combatSkill.getSkill().getEntityName() + "]("
					+ combatSkill.getSkillId() + ")");
		}
		//处理技能消耗
		//技能使用成功，扣士气
		if (consumeMorale > 0) {
			owner.setCurMorale(owner.getCurMorale() - consumeMorale);
		}

		//记录技能使用状态
		this.firedRound = combat.getRound();
		this.nextFiredRound = firedRound + skill.getRoundPeriod();
		this.firedCount = this.firedCount + 1;

		//释放技能效果
		List<HeroSkillEffect> effects = skill.getSkillEffects();
		if (effects != null && effects.size() > 0) {
			// ITargetSearcher searcher = null;
			// //混乱中使用指定的找目标方式
			// if(owner.containsEffect(CombatSkillEffect.EFF_CHAOS)) {
			// int tar =
			// owner.getEffectValueByType(CombatSkillEffect.EFF_CHAOS);//选目标的方式
			// if (tar > 0) {
			// searcher = SkillEffectFactory.getTargetSearcher(tar);
			// if(log.isDebugEnabled()) {
			// log.debug("{}混乱中选取目标的方式改变为{}",owner.getName(),tar);
			// }
			// }
			// }
			
			for (HeroSkillEffect heroSkillEffect : effects) {
				//效果渲染器
				ISkillEffectRender render = SkillEffectFactory.getSkillEffectRender(heroSkillEffect);

				if (render == null) {
					if (log.isDebugEnabled()) {
						log.debug("没有对应的效果渲染器:skillId=" + skill.getEntId() + ",effectId=" + heroSkillEffect.getEffId());
					}
					continue;
				}

				//效果释放状态
				CombatSkillEffect combatSkillEffect = new CombatSkillEffect();
				combatSkillEffect.setCombatSkill(this);
				combatSkillEffect.setOwner(owner);
				combatSkillEffect.setEffect(heroSkillEffect);
				combatSkillEffect.setRender(render);

				// 技能行为中加入效果action
				boolean unShow = heroSkillEffect.getIsShow() <= 0;
				SkillEffectAction skillEffectAction = new SkillEffectAction(
						heroSkillEffect.getEffId(), heroSkillEffect.getEffKey(),
						skillAction, owner, unShow);
				skillAction.addResult(skillEffectAction);

				// 主体集
				List<CombatUnit> ownerUnits = this.getOwnerIds(owner,
						heroSkillEffect, combatSkillEffect, skillEffectAction);
				int[] ownerIds = skillEffectAction.getOwnerIds();

				// 目标集
				List<CombatUnit> targetUnits = this.getTargetIds(owner,
						heroSkillEffect, combatSkillEffect, skillEffectAction);
				int[] targetIds = skillEffectAction.getTargetIds();
				if (targetIds == null || targetIds.length == 0) {
					// 没有目标，效果不执行
					continue;
				}

				// miss集
				targetUnits = this.getMissIds(owner, heroSkillEffect,
						combatSkillEffect,
						skillEffectAction, targetUnits);
				int[] missIds = skillEffectAction.getMissIds();

				if (targetUnits == null || targetUnits.size() == 0) {
					if (log.isDebugEnabled()) {
						log.debug(owner.getName() + "释放技能效果["
								+ heroSkillEffect.getEffKey() + "]***MISS***");
					}
					continue;
				}

				// 设置效果选取的打击目标
				combatSkillEffect.setTargets(targetUnits);

				//效果行为中加入各种action
				render.doRender(combat, combatSkill, combatSkillEffect,
						ownerUnits, targetUnits, skillEffectAction);

				if (combat.isEnd()) {
					//战斗已经结束
					break;
				}

				if (skill.getFiredAt() != HeroSkill.FIRED_AT_ATTACKED && owner.dead()) {
					//除被动技能以外释放者死亡不能继续释放效果
					break;
				}
			}
		}
		return skillAction;
	}

	/**
	 * 选取主体集
	 * 
	 * @param owner
	 * @param heroSkillEffect
	 * @param combatSkillEffect
	 * @return
	 */
	private List<CombatUnit> getOwnerIds(CombatUnit owner,
			HeroSkillEffect heroSkillEffect,
			CombatSkillEffect combatSkillEffect,
			SkillEffectAction skillEffectAction) {
		List<CombatUnit> ownerUnits = null;
		// 选取主体集
		ITargetSearcher searcher = SkillEffectFactory
				.getOwnerSearcher(heroSkillEffect);
		if (searcher == null) {
			if (log.isDebugEnabled()) {
				log.debug("没有对应的目标查找器:effId=" + heroSkillEffect.getEffId()
						+ ",owner=" + heroSkillEffect.getOwner());
			}
			return ownerUnits;
		}
		ownerUnits = searcher.searchTarget(combatSkillEffect,
				owner, null);
		int[] ownerIds = null;
		if (ownerUnits != null) {
			ownerIds = new int[ownerUnits.size()];
			for (int i = 0; i < ownerUnits.size(); i++) {
				ownerIds[i] = ownerUnits.get(i).getId();
			}
		}

		skillEffectAction.setOwnerIds(ownerIds);
		combatSkillEffect.setSources(ownerUnits);
		return ownerUnits;
	}

	/**
	 * 选取目标集
	 * 
	 * @param owner
	 * @param heroSkillEffect
	 * @param combatSkillEffect
	 * @return
	 */
	private List<CombatUnit> getTargetIds(CombatUnit owner,
			HeroSkillEffect heroSkillEffect,
			CombatSkillEffect combatSkillEffect,
			SkillEffectAction skillEffectAction) {
		List<CombatUnit> targetUnits = null;

		// 混乱中使用指定的找目标方式
		ITargetSearcher searcher = null;
		if (owner.containsEffect(CombatSkillEffect.EFF_CHAOS)) {
			int tar = owner.getEffectValueByType(CombatSkillEffect.EFF_CHAOS);// 选目标的方式
			if (tar > 0) {
				searcher = SkillEffectFactory.getTargetSearcher(tar);
				if (log.isDebugEnabled()) {
					log.debug("{}混乱中选取目标的方式改变为{}", owner.getName(), tar);
				}
			}
		}

		// 寻找目标
		if (searcher == null) {
			searcher = SkillEffectFactory.getTargetSearcher(heroSkillEffect);
		}

		if (searcher == null) {
			if (log.isDebugEnabled()) {
				log.debug("没有对应的目标查找器:effId=" + heroSkillEffect.getEffId()
						+ ",target=" + heroSkillEffect.getTarget());
			}
			return targetUnits;
		}

		targetUnits = searcher.searchTarget(combatSkillEffect,
				owner, null);
		int targetNum = targetUnits == null ? 0 : targetUnits.size();
		if (targetNum == 0) {
			if (log.isDebugEnabled()) {
				log.debug("skillId=" + skill.getEntId() + ",effectId="
						+ heroSkillEffect.getEffId() + "未选取到目标");
			}
			return targetUnits;
		}

		if (targetUnits.size() <= 0) {
			// 没有满足条件的目标
			return targetUnits;
		}

		// 设置效果选取的打击目标
		combatSkillEffect.setTargets(targetUnits);

		int[] targetIds = new int[targetUnits.size()];
		for (int i = 0; i < targetUnits.size(); i++) {
			targetIds[i] = targetUnits.get(i).getId();
		}

		skillEffectAction.setTargetIds(targetIds);
		return targetUnits;
	}

	/**
	 * 选取miss集
	 * 
	 * @param owner
	 * @param heroSkillEffect
	 * @param combatSkillEffect
	 * @param skillEffectAction
	 * @param targetUnits
	 * @return
	 */
	private List<CombatUnit> getMissIds(CombatUnit owner,
			HeroSkillEffect heroSkillEffect,
			CombatSkillEffect combatSkillEffect,
			SkillEffectAction skillEffectAction, List<CombatUnit> targetUnits) {
		// 校验其他释放条件
		List<Integer> missList = new ArrayList<Integer>();
		List<HeroSkillEffectLimit> limits = heroSkillEffect
				.getSkillEffectLimits();
		if (limits != null && limits.size() > 0) {
			for (HeroSkillEffectLimit limit : limits) {
				ISkillEffectFiredLimitValidator validator = SkillEffectFactory
						.getSkillEffectFiredLimitValidator(limit);
				Iterator<CombatUnit> itl = targetUnits.iterator();
				while (itl.hasNext()) {
					CombatUnit target = itl.next();
					boolean canFired = validator.checked(combatSkillEffect,
							limit, owner, target);
					if (!canFired) {
						// 过滤掉不满足条件的目标
						itl.remove();
						missList.add(target.getId());
					}
				}
			}
		}

		int[] missIds = null;
		if (missList.size() > 0) {
			missIds = new int[missList.size()];
			for (int i = 0; i < missIds.length; i++) {
				missIds[i] = missList.get(i);
			}
		}

		skillEffectAction.setMissIds(missIds);
		return targetUnits;
	}

	public String toString() {
		return skill.getEntityName();
	}

}
