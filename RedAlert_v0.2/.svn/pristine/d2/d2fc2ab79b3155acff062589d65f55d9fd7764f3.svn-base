package com.youxigu.dynasty2.combat.domain;

import java.util.LinkedList;
import java.util.List;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.entity.domain.Army;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.domain.Hero;

/**
 * 玩家武将战斗单位
 * @author Dagangzi
 *
 */
public class PlayHeroCombatUint extends CombatUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6588726935852418337L;

	private transient Hero hero;
	private transient IEntityService entityService;

	public PlayHeroCombatUint() {
		super();
	}

	public PlayHeroCombatUint(Hero hero, int id) {
		this();
		this.id = id;
		this.name = hero.getName();
		SysHero syshero = hero.getSysHero();
		this.hero = hero;
		this.icon = hero.getIcon();
		this.level = hero.getLevel();
		this.unitEntId = hero.getSysHeroId();
		this.armyEntId = hero.getArmy().getEntId();
		this.initHp = hero._getInitHp();
		this.curHp = hero.getCurHp();
		this.initMorale = hero._getMorale();
		this.curMorale = this.initMorale;
		this.unitType = CombatConstants.COMBATUNIT_TYPE_PLAYERHERO;
		this.attackType = syshero.getAttackType();

		if (entityService == null) {
			entityService = (IEntityService) ServiceLocator.getSpringBean("entityService");
		}

		//初始化是技能
		List<Integer> list = hero.getSkills();
		if (list != null && list.size() > 0) {
			for (int skillId : list) {
				HeroSkill skill = (HeroSkill) entityService.getEntity(skillId);
				if (skill != null) {
					List<CombatSkill> subs = skills.get(skill.getFiredAt());
					if (subs == null) {
						subs = new LinkedList<CombatSkill>();
						this.skills.put(skill.getFiredAt(), subs);
					}
					subs.add(new CombatSkill(skill));
				}
			}
			//同触发时机的技能按优先级排序
			this.sortSkill();
		}
	}

	public Army _getArmy() {
		return hero.getArmy();
	}

	public Hero _getHero() {
		return hero;
	}

	@Override
	public int _getAttackMorale() {
		return hero.getSysHero().getAttackMorale();
	}

	@Override
	public int _getDefendMorale() {
		return hero.getSysHero().getDefendMorale();
	}

	@Override
	public String _getArmyName() {
		if (this.getArmyEntId() > 0) {
			return _getArmy().getArmyName();
		}
		return null;
	}

	@Override
	public int _getMagicAttack() {
		int abs = hero._getMagicAttack();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_MAGICATTACK);
		int percent = getEffectValueByType(CombatSkillEffect.SP_MAGICATTACK);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getPhysicalAttack() {
		int abs = hero._getPhysicalAttack();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_PHYSICALATTACK);
		int percent = getEffectValueByType(CombatSkillEffect.SP_PHYSICALATTACK);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getMagicDefend() {
		int abs = hero._getMagicDefend();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_MAGICDEFEND);
		int percent = getEffectValueByType(CombatSkillEffect.SP_MAGICDEFEND);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getPhysicalDefend() {
		int abs = hero._getPhysicalDefend();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_PHYSICALDEFEND);
		int percent = getEffectValueByType(CombatSkillEffect.SP_PHYSICALDEFEND);

		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getInitHit() {
		int abs = hero._getInitHit();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_Hit);
		int percent = getEffectValueByType(CombatSkillEffect.SP_Hit);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getInitDodge() {
		int abs = hero._getInitDodge();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_DODGE);
		int percent = getEffectValueByType(CombatSkillEffect.SP_DODGE);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getCritDec() {
		int abs = hero._getCritDec();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_CRITDEC);
		int percent = getEffectValueByType(CombatSkillEffect.SP_CRITDEC);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getCritAdd() {
		int abs = hero._getCritAdd();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_CRITADD);
		int percent = getEffectValueByType(CombatSkillEffect.SP_CRITADD);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getCritDamageAdd() {
		int abs = hero._getCritDamageAdd();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_CRITDAMAGE);
		int percent = getEffectValueByType(CombatSkillEffect.SP_CRITDAMAGE);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getCritDamageDec() {
		int abs = hero._getCritDamageDec();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_CRITDAMAGE_DEC);
		int percent = getEffectValueByType(CombatSkillEffect.SP_CRITDAMAGE_DEC);
		
		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getDamageAdd() {
		//第一重计算
		int abs = hero._getDamageAdd();
		
		//技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_DAMAGE_PER);
		int percent = getEffectValueByType(CombatSkillEffect.SP_DAMAGE_PER);
		
		double result = 0;
		if(percent < 1000) {
			result = Math.max(abs, 1) * percent;
		}else {
			result = Math.max(abs, 1) * (1000 + percent);
		}
		
		if(result <= 0) {
			result = 1000;
		}
		return (int) result;
	}

	@Override
	public int _getDamageDec() {
		int abs = hero._getDamageDec();
		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_SHIELD_PER);
		int percent = getEffectValueByType(CombatSkillEffect.SP_SHIELD_PER);

		double result = 0;
		if(percent < 1000) {
			result = Math.max(abs, 1) * percent;
		}else {
			result = Math.max(abs, 1) * (1000 + percent);
		}
		
		if(result <= 0) {
			result = 1000;
		}
		return (int) result;
	}

	@Override
	public int _getCurrentPower() {
		return hero._getCurrentPower();
	}

	@Override
	public int _getArmyBiteRoit() {
		// 技能加成
		int abs = getEffectValueByType(CombatSkillEffect.SA_ARMY_BITE_ROIT);
		int percent = getEffectValueByType(CombatSkillEffect.SP_ARMY_BITE_ROIT);

		double result = 0;
		if(percent < 1000) {
			result = Math.max(abs, 1) * percent;
		}else {
			result = Math.max(abs, 1) * (1000 + percent);
		}
		
		if(result <= 0) {
			result = 1000;
		}
		return (int) result;
	}

	@Override
	public int _getDamage() {
		// 技能加成
		int abs = hero._getDamage();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_DAMAGE);
		int percent = getEffectValueByType(CombatSkillEffect.SP_DAMAGE);

		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getShield() {
		// 技能加成
		int abs = hero._getShield();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_SHIELD);
		int percent = getEffectValueByType(CombatSkillEffect.SP_SHIELD);

		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}

	@Override
	public int _getMorale() {
		int abs = hero._getMorale();

		// 技能加成
		abs = abs + getEffectValueByType(CombatSkillEffect.SA_MORALE);
		int percent = getEffectValueByType(CombatSkillEffect.SP_MORALE);

		double result = abs * (1 + percent / 1000d);

		if (result <= 0) {
			result = 1;
		}
		return (int) result;
	}
	
	@Override
	public boolean _isLeader() {
		return hero.getTeamLeader() >0;
	}
}
