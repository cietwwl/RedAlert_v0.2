package com.youxigu.dynasty2.hero.domain;

/**
 * 永远满兵的武将(兵数=统帅),作为一种武将镜像
 * 
 * @author Administrator
 * 
 */
public class FullArmyHero extends Hero {
	private static final long serialVersionUID = 1L;

	public FullArmyHero() {
		super();
	}

	public FullArmyHero(Hero hero) {
		super();
		// 基本信息
		super.heroId = hero.getHeroId();// 主键
		super.casId = hero.getCasId();// 所属城堡id
		super.userId = hero.getUserId();// 君主id
		super.sysHeroId = hero.getSysHeroId();// 系统武将entId
		super.name = hero.getName();// 名字
		super.icon = hero.getIcon();// 武将头像
		super.level = hero.getLevel();// 级别
		super.exp = hero.getExp();// 经验
		super.curHp = hero.getCurHp();// 当前血量

		// 四围属性
		super.magicAttack = hero.getMagicAttack();// 法术攻击
		super.physicalAttack = hero.getPhysicalAttack();// 物理攻击
		super.magicDefend = hero.getMagicDefend();// 法术防御
		super.physicalDefend = hero.getPhysicalDefend();// 物理防御
		super.intHp = hero._getInitHp();

		// 状态锁
		super.actionStatus = hero.getActionStatusEnum().getIndex(); // 行动状态
		super.freeDttm = hero.getFreeDttm();// 行动超时时间

		// 技能
		super.skillId1 = hero.getSkillId1();// 普通攻击技能
		super.skillId2 = hero.getSkillId2();// 合击技能
		super.skillId3 = hero.getSkillId3();// 士气技能
		super.skillId4 = hero.getSkillId4();// 飞机技能
		super.skillId5 = hero.getSkillId5();// 被动技能1
		super.skillId6 = hero.getSkillId6();// 被动技能2

		// super.heroFate = hero.getHeroFate();
		// super.equipFate = hero.getEquipFate();

		super.growing = hero.getGrowing();
		super.growingItem = hero.getGrowingItem();
		super.relifeNum = hero.getRelifeNum();
		super.heroStrength = hero.getHeroStrength();

		super.status = hero.getStatus();
		super.troopGridId = hero.getTroopGridId();
		super.teamLeader = hero.getTeamLeader();

		super.heroCardEntId = hero.getHeroCardEntId();// 图纸-信物entId
		super.heroCardNum = hero.getHeroCardNum();// 图纸-信物
		super.heroSoulEntId = hero.getHeroSoulEntId();// 碎片entId
		super.heroSoulNum = hero.getHeroSoulNum();// 碎片
	}

	public int getCurHp() {
		return super._getInitHp();
	}
}
