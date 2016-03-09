package com.youxigu.dynasty2.hero.domain;

/**
 * 可序列化的满兵的Hero,用于在不同服务器间传递，或者持久化保存，作为武将的镜像
 * 
 * @author Administrator
 * 
 */
public class SerialHero extends Hero {
	protected int finalLead;
	protected int finalPower;// 武力值 攻击
	protected int finalSkill;// 技巧值 防御
	protected int finalIntel;// 智力值 体力
	protected int finalStrategy;// 谋略值 敏捷

	protected int addCrit;// 额外的暴击率，千分比
	protected int addHit;// 额外的命中率，千分比
	protected int addDodge;// 额外的闪避率，千分比

	protected int damageAdd;// 伤害增加的千分比，自己攻击的时候
	protected int damageDec;// 伤害减少的千分比，被攻击的时候

	// ////////战斗属性
	// 以下都是指单兵的

	protected int combatPower;// 武将战略站战斗力

	public SerialHero() {

	}

    //todo: 根据现有实现修改SerialHero
	public SerialHero(Hero hero) {
		super();
		super.heroId = hero.heroId;
		super.casId = hero.casId;// 所属城堡id
		super.userId = hero.userId;// 君主id
		super.sysHeroId = hero.sysHeroId;// 系统武将entId
		super.name = hero.name;// 名字
		super.level = hero.level;// 级别
		super.exp = hero.exp;// 经验

//		super.power = hero.power;// 武力
//		super.skill = hero.skill;// 技巧
//		super.intel = hero.intel;// 智力
//		super.strategy = hero.strategy;// 谋略
//
//		super.growing = hero.growing;// 当前成长
//
//		super.relifeNum = hero.relifeNum;// 转生次数
//
//		// lead=hero.;// 统帅=hero.;
//		super.featureId = hero.featureId;// 特点id
//		super.chartr = hero.chartr;// 性格 （1：谨慎，2：冷静，3：果敢，4：勇猛）
//
//		super.status = hero.status; // 雇佣状态 1:未雇佣 2：已雇佣
//		super.actionStatus = hero.actionStatus; // 行动状态
//		// isLocked=hero.; // 解锁状态：0-已经解锁1-加锁
//
//		// 装备
//		super.equ1 = hero.equ1;// 头盔-treasuryId
//		super.equ2 = hero.equ2;// 盔甲
//		super.equ3 = hero.equ3;// 武器
//		super.equ4 = hero.equ4;// 护腿
//		super.equ5 = hero.equ5;// 饰品
//		super.equ6 = hero.equ6;// 披风-treasuryId
//		super.equ7 = hero.equ7;
//		super.equ8 = hero.equ8;
//		super.equ9 = hero.equ9;
//		super.equ10 = hero.equ10;
//
//		// 封官
//		super.govPotzId = hero.govPotzId;// 官职
//		super.govPotzTime = hero.govPotzTime;// 封官时间

		// 修炼
		// super.practiceStartDttm=hero.practiceStartDttm;// 修炼开始时间
		// super.practiceEndDttm=hero.practiceEndDttm;// 修炼截止时间
		// super.practiceExp=hero.practiceExp;// 修炼结束后后获得的经验
		// super.practiceCost=hero.practiceCost;// 修炼消耗(元宝或是铜币)

//		super.careerId = hero.careerId;// 职业id
//
//		super.heroSkillId = hero.heroSkillId;// 技能
//		super.heroSkillId1 = hero.heroSkillId1;// 技能
//		super.heroSkillId2 = hero.heroSkillId2;// 技能
//		super.heroSkillId3 = hero.heroSkillId3;// 技能
//		super.heroSkillId4 = hero.heroSkillId4;// 技能
//		super.heroSkillId5 = hero.heroSkillId5;// 技能
//		super.heroSkillId6 = hero.heroSkillId6;// 技能
//
//		super.heroFate = hero.heroFate;
//		super.equipFate = hero.equipFate;
//		super.growingItem = hero.growingItem;
//
//		super.gemId1 = hero.gemId1;
//		super.gemId2 = hero.gemId2;
//		super.gemId3 = hero.gemId3;
//		super.gemId4 = hero.gemId4;
//		super.gemId5 = hero.gemId5;
//		super.gemId6 = hero.gemId6;
//		super.gemId7 = hero.gemId7;
//		super.gemId8 = hero.gemId8;
//		super.gemId9 = hero.gemId9;
//
//		super.formationId = hero.formationId;
//		super.hangerEntId1 = hero.hangerEntId1;
//		super.hangerEntId2 = hero.hangerEntId2;
//		super.hangerEntId3 = hero.hangerEntId3;
//		super.hangerEntId4 = hero.hangerEntId4;
//		super.hangerEntId5 = hero.hangerEntId5;
//
//		// 配兵
//		super.armyEntId = hero.armyEntId; // 带兵entId-兵种
//		// super.armyNum = super.getArmyNum();
//
//
//
//
//
//
//
//		/****** 武将8个秘宝 *********/
//		super.treasureId1 = hero.treasureId1;
//		super.treasureId2 = hero.treasureId2;
//		super.treasureId3 = hero.treasureId3;
//		super.treasureId4 = hero.treasureId4;
//		super.treasureId5 = hero.treasureId5;
//		super.treasureId6 = hero.treasureId6;
//		super.treasureId7 = hero.treasureId7;
//		super.treasureId8 = hero.treasureId8;
//		super.treasure1SumStone = hero.treasure1SumStone;
//		super.treasure2SumStone = hero.treasure2SumStone;
//		super.treasure3SumStone = hero.treasure3SumStone;
//		super.treasure4SumStone = hero.treasure4SumStone;
//		super.treasure5SumStone = hero.treasure5SumStone;
//		super.treasure6SumStone = hero.treasure6SumStone;
//		super.treasure7SumStone = hero.treasure7SumStone;
//		super.treasure8SumStone = hero.treasure8SumStone;
//
//		super.horseId = hero.horseId;
//
//		//!!!!!!!!!!这一行要放在初始化最后一行
//
//		this.finalLead = super.getFinalLeadD();
//		this.finalPower = super.getFinalPowerD();
//		this.finalSkill = super.getFinalSkillD();
//		this.finalIntel = super.getFinalIntelD();
//		this.finalStrategy = super.getFinalStrategyD();
//		this.addCrit = super.getAddCrit();
//		this.addHit = super.getAddHit();
//		this.addDodge = super.getAddDodge();
//		this.damageAdd = super.getDamageAdd();
//		this.damageDec = super.getDamageDec();
//		super.armyNum = this.finalLead;
//		this.combatPower = super.getCombatPower(true);
	}

	public int getFinalLeadD() {
		return this.finalLead;
	}

	public int getFinalPowerD() {
		return this.finalPower;

	}

	public int getFinalSkillD() {
		return this.finalSkill;

	}

	// 体力
	public int getFinalIntelD() {
		return this.finalIntel;

	}

	// 敏捷
	public int getFinalStrategyD() {
		return this.finalStrategy;
	}

	// public int getHp() {
	// return this.getFinalIntelD() * 5;
	// }

	public int getAddCrit() {
		return this.addCrit;
	}

	public int getAddHit() {
		return this.addHit;
	}

	public int getAddDodge() {

		return this.addDodge;
	}

	public int getDamageAdd() {
		return this.damageAdd;

	}

	public int getDamageDec() {
		return this.damageDec;

	}

	public int getCombatPower() {
		return this.combatPower;
	}

	public int getCombatPower(boolean isFullArmy) {
		return this.combatPower;
	}
}
