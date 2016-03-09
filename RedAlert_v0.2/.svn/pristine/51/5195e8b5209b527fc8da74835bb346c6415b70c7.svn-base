package com.youxigu.dynasty2.npc.domain;

import java.io.Serializable;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.combat.domain.CombatFactor;
import com.youxigu.dynasty2.combat.service.ICombatEngine;
import com.youxigu.dynasty2.entity.domain.Army;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.hero.service.IHeroService;

/**
 * NPC的武将定义
 * 
 * @author Dagangzi
 *
 */
public class NPCHero implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6436689242159292467L;
	private int heroId;// 主键
	private String name;//
	private int sysHeroId;// 关联的系统武将
	private int level;

	// ////////随机计算得出或者数据库中预先指定的
	private transient String showName;
	private transient SysHero sysHero;
	private transient Army army;
	private transient IHeroService heroService = null;
	private transient ICombatEngine combatEngine = null;
	private transient IEntityService entityService = null;
	private transient int currentPower = -1;// 武将战略站战斗力
	private transient boolean teamleader;//是否为队长

	public Army getArmy() {
		if (army == null) {
			army = (Army) getEntityService().getEntity(sysHero.getArmyEntId());
		}
		return army;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSysHeroId() {
		return sysHeroId;
	}

	public void setSysHeroId(int sysHeroId) {
		this.sysHeroId = sysHeroId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public SysHero getSysHero() {
		return sysHero;
	}

	public void setSysHero(SysHero sysHero) {
		this.sysHero = sysHero;
	}

	public void setArmy(Army army) {
		this.army = army;
	}

	// ==================战斗属性=================

	public boolean isTeamleader() {
		return teamleader;
	}

	public void setTeamleader(boolean teamleader) {
		this.teamleader = teamleader;
	}

	/**
	 * 法术攻击最终值
	 */
	public int _getMagicAttack() {
		return sysHero.getMagicAttack();
	}

	/**
	 * 物理攻击最终值
	 */
	public int _getPhysicalAttack() {
		return sysHero.getPhysicalAttack();
	}

	/**
	 * 法术防御最终值
	 */
	public int _getMagicDefend() {
		return sysHero.getMagicDefend();
	}

	/**
	 * 物理防御最终值
	 */
	public int _getPhysicalDefend() {
		return sysHero.getPhysicalDefend();
	}

	/**
	 * 命中最终值
	 * 
	 * @return
	 */
	public int _getInitHit() {
		return sysHero.getInitHit();
	}

	/**
	 * 闪避最终值
	 * 
	 * @return
	 */
	public int _getInitDodge() {
		return sysHero.getInitDodge();
	}

	/**
	 * 免暴率最终值
	 * 
	 * @return
	 */
	public int _getCritDec() {
		return sysHero.getCritDec();
	}

	/**
	 * 暴击率最终值
	 * 
	 * @return
	 */
	public int _getCritAdd() {
		return sysHero.getCritAdd();
	}

	/**
	 * 暴击伤害最终值
	 * 
	 * @return
	 */
	public int _getCritDamageAdd() {
		return sysHero.getCritDamageAdd();
	}

	/**
	 * 暴击伤害减免最终值
	 * 
	 * @return
	 */
	public int _getCritDamageDec() {
		return sysHero.getCritDamageDec();
	}

	/**
	 * 最终伤害加成最终值
	 * 
	 * @return
	 */
	public int _getDamageAdd() {
		return 0;
	}

	/**
	 * 最终伤害减免最终值
	 * 
	 * @return
	 */
	public int _getDamageDec() {
		return 0;
	}

	/**
	 * 血量上限最终值
	 * 
	 * @return
	 */
	public int _getInitHp() {
		return sysHero.getInitHp();
	}

	/**
	 * 固定伤害
	 * 
	 * @return
	 */
	public int _getDamage() {
		return 0;
	}

	/**
	 * 固定减免
	 * 
	 * @return
	 */
	public int _getShield() {
		return 0;
	}

	/**
	 * 士气初始值
	 * @return
	 */
	public int _getMorale() {
		return sysHero.getInitMorale();
	}

	public IHeroService getHeroService() {
		if (this.heroService == null) {
			heroService = (IHeroService) ServiceLocator.getSpringBean("heroService");
		}
		return heroService;
	}

	public ICombatEngine getCombatEngine() {
		if (combatEngine == null) {
			combatEngine = (ICombatEngine) ServiceLocator.getSpringBean("combatEngine");
		}
		return combatEngine;
	}

	public IEntityService getEntityService() {
		if (entityService == null) {
			entityService = (IEntityService) ServiceLocator.getSpringBean("entityService");
		}
		return entityService;
	}

	public int _getCurrentPower() {
		if (currentPower >= 0) {
			return currentPower;
		}

		int heroSkillLv = 0;

		IEntityService entityService = getEntityService();
		if (sysHero.getSkillId1() > 0) {
			HeroSkill heroSkill = (HeroSkill)entityService.getEntity(sysHero.getSkillId1());
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}
		if (sysHero.getSkillId2() > 0) {
			HeroSkill heroSkill = (HeroSkill)entityService.getEntity(sysHero.getSkillId2());
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (sysHero.getSkillId3() > 0) {
			HeroSkill heroSkill = (HeroSkill)entityService.getEntity(sysHero.getSkillId3());
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (sysHero.getSkillId4() > 0) {
			HeroSkill heroSkill = (HeroSkill)entityService.getEntity(sysHero.getSkillId4());
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (sysHero.getSkillId5() > 0) {
			HeroSkill heroSkill = (HeroSkill)entityService.getEntity(sysHero.getSkillId5());
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (sysHero.getSkillId6() > 0) {
			HeroSkill heroSkill = (HeroSkill)entityService.getEntity(sysHero.getSkillId6());
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		double atk = 0;
		if (sysHero.getAttackType() == SysHero.ATTACKTYPE_PHYSICAL) {
			atk = this._getPhysicalAttack();
		} else {
			atk = this._getMagicAttack();
		}
		double tmp1 = atk + this._getMagicDefend() + this._getPhysicalDefend();

		double tmp2 = this._getInitHp() / 5d;

		double tmp3 = (this._getInitHit() + this._getInitDodge()) * 5d;

		double tmp4 = this._getDamage() + this._getShield();

		double tmp5 = 1.0d + this._getCritAdd() + _getCritDec() + _getCritDamageAdd() + _getCritDamageDec()
				+ _getDamageAdd() + _getDamageDec();

		double tmp6 = 1 + heroSkillLv;

		double roit = getCombatEngine().getCombatFactor(CombatFactor.F_COMBAT).getPara1();

		double tmp = Math.pow((((tmp1 + tmp2 + tmp3 + tmp4) + tmp5) * tmp6 * roit), 0.5);

		currentPower = (int) tmp;
		return currentPower;
	}

	public int getCurrentPower() {
		return currentPower;
	}

	public void setCurrentPower(int currentPower) {
		this.currentPower = currentPower;
	}

}
