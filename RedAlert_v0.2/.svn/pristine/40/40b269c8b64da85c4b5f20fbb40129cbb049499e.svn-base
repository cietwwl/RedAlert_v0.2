package com.youxigu.dynasty2.hero.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.combat.domain.CombatFactor;
import com.youxigu.dynasty2.combat.service.ICombatEngine;
import com.youxigu.dynasty2.develop.domain.CastleEffect;
import com.youxigu.dynasty2.develop.service.ICastleEffectService;
import com.youxigu.dynasty2.entity.domain.Army;
import com.youxigu.dynasty2.entity.domain.HeroProperty;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.ItemProperty;
import com.youxigu.dynasty2.entity.domain.SysHero;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;
import com.youxigu.dynasty2.entity.domain.enumer.ColorType;
import com.youxigu.dynasty2.entity.domain.enumer.EquipPosion;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.entity.service.IEquipService;
import com.youxigu.dynasty2.entity.service.ISysHeroService;
import com.youxigu.dynasty2.hero.enums.HeroIdle;
import com.youxigu.dynasty2.hero.enums.HeroTroopIng;
import com.youxigu.dynasty2.hero.enums.TeamLeaderType;
import com.youxigu.dynasty2.hero.proto.HeroCardAndDebris;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroAttrInfo;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroInfo;
import com.youxigu.dynasty2.hero.service.IHeroService;
import com.youxigu.dynasty2.hero.service.ITroopGridService;
import com.youxigu.dynasty2.treasury.domain.Treasury;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.user.service.IUserTitleService;
import com.youxigu.dynasty2.util.EffectValue;

/**
 * 君主武将定义.武将使用的时候一定要注意记得判断是否为武将@see Hero#isHero()
 * 
 * @author Dagangzi
 * @version 1.2
 * 
 */
public class Hero implements Serializable {
	private static final long serialVersionUID = -225516825244929351L;

	// 基本信息
	protected long heroId;// 主键
	@Deprecated
	protected long casId;// 所属城堡id
	protected long userId;// 君主id
	protected int sysHeroId;// 系统武将entId
	protected String name;// 名字
	protected String icon;// 武将头像
	protected int level;// 级别
	protected int exp;// 经验
	protected int curHp;// 当前血量

	// 四围属性
	protected int magicAttack;// 法术攻击
	protected int physicalAttack;// 物理攻击
	protected int magicDefend;// 法术防御
	protected int physicalDefend;// 物理防御
	protected int intHp;// 生命值上限

	// 状态锁
	protected int actionStatus; // 行动状态
	protected Timestamp freeDttm;// 行动超时时间

	// 技能
	protected int skillId1;// 普通攻击技能
	protected int skillId2;// 合击技能
	protected int skillId3;// 士气技能
	protected int skillId4;// 飞机技能
	protected int skillId5;// 被动技能1
	protected int skillId6;// 被动技能2

	protected String heroFate;// 武将情缘

	protected int growing;// 当前成长
	protected int growingItem;// 强化达到当前成长消耗的道具数量
	protected int relifeNum;// 进阶等级
	protected int heroStrength;// 强化等级。。这里绑定的是强化对应的配置表id

	protected int status;// // 2:上阵,3是未上阵 4.武将图纸或者碎片
	protected long troopGridId;// 武将所属的格子id 0表示没有放入军团

	protected int teamLeader;// 标记武将是否为队长 1队长，2或者其他不是队长

	protected int heroCardEntId;// 图纸-信物entId
	protected int heroCardNum;// 图纸-信物
	protected int heroSoulEntId;// 碎片entId
	protected int heroSoulNum;// 碎片

	protected int sumHeroCardNum;// 累计分解和退役的坦克图纸数量最要做找回图纸用

	protected byte commander;// 是否指挥官武将 >0是

	// 非数据库字段

	// 武将效果:通常包括仅针对一个武将的各种功能产生的效果
	protected transient Map<String, EffectValue> effects = null;

	// 科技产生的武将加成效果
	protected transient List<CastleEffect> casEffects = null;

	protected transient SysHero sysHero;// 系统武将
	protected transient Army army;// 系统兵
	protected transient IHeroService heroService = null;
	protected transient ICombatEngine combatEngine = null;
	protected transient IEntityService entityService = null;
	protected transient ITroopGridService troopGridService = null;
	protected transient ITreasuryService treasuryService = null;
	protected transient IUserTitleService userTitleService = null;
	protected transient IUserService userService = null;

	protected transient int currentPower = -1;// 满兵武将战略站战斗力

	protected transient int finalMagicAttack = -1;// 法术攻击
	protected transient int finalPhysicalAttack = -1;// 物理攻击
	protected transient int finalMagicDefend = -1;// 法术防御
	protected transient int finalPhysicalDefend = -1;// 物理防御
	protected transient int finalHit = -1;// 命中
	protected transient int finalDodge = -1;// 闪避
	protected transient int finalCritDec = -1;// 免暴率
	protected transient int finalCritAdd = -1;// 暴击率
	protected transient int finalCritDamageAdd = -1;// 暴击伤害
	protected transient int finalCritDamageDec = -1;// 暴击伤害减免
	protected transient int finalDamageAdd = -1;// 最终伤害加成
	protected transient int finalDamageDec = -1;// 最终伤害减免
	protected transient int finalInitHp = -1;// 血量最终值
	protected transient int finalDamage = -1;// 固定伤害
	protected transient int finalShield = -1;// 固定减免
	protected transient int finalInitMorale = -1;// 士气初始值
	protected transient String href;// 前端显示的超链接字段
	/** 情缘效果 */
	protected transient Map<String, EffectValue> fateEffects = null;
	/** 装备加成的效果 */
	protected transient Map<String, EffectValue> equipEffects = null;
	// 指挥官军衔加成
	protected transient Map<String, EffectValue> titleEffects = null;

	public Hero() {
	}

	public Hero(long userId, int sysHeroId, int heroCardEntId, int heroCardNum,
			int heroSoulEntId, int heroSoulNum, int sumHeroCardNum) {
		this.userId = userId;
		this.sysHeroId = sysHeroId;
		this.heroCardEntId = heroCardEntId;
		this.heroCardNum = heroCardNum;
		this.heroSoulEntId = heroSoulEntId;
		this.heroSoulNum = heroSoulNum;
		this.sumHeroCardNum = sumHeroCardNum;
		this.status = HeroTroopIng.HERO_NONE.getIndex();
	}

	public Hero(SysHero sysHero, long userId) {
		initHero(sysHero, userId);
	}

	/**
	 * 初始武将信息
	 * 
	 * @param hero
	 * @param sysHero
	 * @param casId
	 * @param userId
	 */
	public void initHero(SysHero sysHero, long userId) {
		setSysHero(sysHero);
		setCasId(casId);
		setUserId(userId);
		setSysHeroId(sysHero.getEntId());
		setName(sysHero.getName());
		setIcon(sysHero.getPicPath());
		setRelifeNum(0);
		setActionStatus(HeroIdle.STATUS_IDLE.getIndex());
		// 职业
		restSkillId();

		setLevel(1);// sysHero.getLevel()
		setGrowing(sysHero.getGrowing());

		setTeamLeader(TeamLeaderType.HERO_COMMON.getIndex());

		setStatus(HeroTroopIng.HERO_TROOP_OUT.getIndex());
		// recalcHeroBaseProp(hero, getGrowing());
	}

	public long getHeroId() {
		return heroId;
	}

	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}

	public long getCasId() {
		return casId;
	}

	public void setCasId(long casId) {
		this.casId = casId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getSysHeroId() {
		return sysHeroId;
	}

	public void setSysHeroId(int sysHeroId) {
		this.sysHeroId = sysHeroId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCurHp() {
		return curHp;
	}

	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getMagicDefend() {
		return magicDefend;
	}

	public void setMagicDefend(int magicDefend) {
		this.magicDefend = magicDefend;
	}

	public int getPhysicalDefend() {
		return physicalDefend;
	}

	public void setPhysicalDefend(int physicalDefend) {
		this.physicalDefend = physicalDefend;
	}

	public void setActionStatus(int actionStatus) {
		this.actionStatus = actionStatus;
	}

	public Timestamp getFreeDttm() {
		return freeDttm;
	}

	public void setFreeDttm(Timestamp freeDttm) {
		this.freeDttm = freeDttm;
	}

	public int getIntHp() {
		return intHp;
	}

	public void setIntHp(int intHp) {
		this.intHp = intHp;
	}

	public int getActionStatus() {
		return actionStatus;
	}

	public HeroIdle getActionStatusEnum() {
		return HeroIdle.valueOf(actionStatus);
	}

	/**
	 * true表示空闲的
	 * 
	 * @return
	 */
	public boolean idle() {
		if (this.getStatus() == HeroTroopIng.HERO_NONE.getIndex()) {
			// 未获得武将
			return false;
		}
		if (!getActionStatusEnum().isCombat()) {
			if (freeDttm != null
					&& freeDttm.getTime() < System.currentTimeMillis()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	// ==================战斗属性=================
	public int getSkillId1() {
		return skillId1;
	}

	public void setSkillId1(int skillId1) {
		this.skillId1 = skillId1;
	}

	public int getSkillId2() {
		return skillId2;
	}

	public void setSkillId2(int skillId2) {
		this.skillId2 = skillId2;
	}

	public int getSkillId3() {
		return skillId3;
	}

	public void setSkillId3(int skillId3) {
		this.skillId3 = skillId3;
	}

	public int getSkillId4() {
		return skillId4;
	}

	public void setSkillId4(int skillId4) {
		this.skillId4 = skillId4;
	}

	public int getSkillId5() {
		return skillId5;
	}

	public void setSkillId5(int skillId5) {
		this.skillId5 = skillId5;
	}

	public int getSkillId6() {
		return skillId6;
	}

	public void setSkillId6(int skillId6) {
		this.skillId6 = skillId6;
	}

	/**
	 * 法术攻击最终值
	 */
	public int _getMagicAttack() {
		if (finalMagicAttack < 0) {
			finalMagicAttack = (int) getPEffectValue(this.magicAttack,
					EffectTypeDefine.H_MAGICATTACK, false);

			// 纯粹攻击
			finalMagicAttack += (int) getPEffectValue(this.physicalAttack,
					EffectTypeDefine.H_ATTACK, false);
		}
		return finalMagicAttack;
	}

	/**
	 * 物理攻击最终值
	 */
	public int _getPhysicalAttack() {
		if (finalPhysicalAttack < 0) {
			// 物理攻击
			finalPhysicalAttack = (int) getPEffectValue(this.physicalAttack,
					EffectTypeDefine.H_PHYSICALATTACK, false);
			// 纯粹攻击
			finalPhysicalAttack += (int) getPEffectValue(this.physicalAttack,
					EffectTypeDefine.H_ATTACK, false);
		}
		return finalPhysicalAttack;
	}

	/**
	 * 法术防御最终值
	 */
	public int _getMagicDefend() {
		if (finalMagicDefend < 0) {
			finalMagicDefend = (int) getPEffectValue(this.magicDefend,
					EffectTypeDefine.H_MAGICDEFEND, false);
		}
		return finalMagicDefend;
	}

	/**
	 * 物理防御最终值
	 */
	public int _getPhysicalDefend() {
		if (finalPhysicalDefend < 0) {
			finalPhysicalDefend = (int) getPEffectValue(this.physicalDefend,
					EffectTypeDefine.H_PHYSICALDEFEND, false);
		}
		return finalPhysicalDefend;
	}

	/**
	 * 命中最终值
	 * 
	 * @return
	 */
	public int _getInitHit() {
		if (finalHit < 0) {
			int defaultBase = getSysHero().getInitHit();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getInitHit();
				}
			}
			finalHit = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_Hit, false);
		}
		return finalHit;
	}

	/**
	 * 闪避最终值
	 * 
	 * @return
	 */
	public int _getInitDodge() {
		if (finalDodge < 0) {
			int defaultBase = getSysHero().getInitDodge();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getInitDodge();
				}
			}

			finalDodge = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_DODGE, false);
		}
		return finalDodge;
	}

	/**
	 * 免暴率最终值
	 * 
	 * @return
	 */
	public int _getCritDec() {
		if (finalCritDec < 0) {
			int defaultBase = getSysHero().getCritDec();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getCritDec();
				}
			}
			finalCritDec = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_CRITDEC, false);
		}
		return finalCritDec;
	}

	/**
	 * 暴击率最终值
	 * 
	 * @return
	 */
	public int _getCritAdd() {
		if (finalCritAdd < 0) {
			int defaultBase = getSysHero().getCritAdd();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getCritAdd();
				}
			}
			finalCritAdd = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_CRITADD, false);
		}
		return finalCritAdd;
	}

	/**
	 * 暴击伤害最终值
	 * 
	 * @return
	 */
	public int _getCritDamageAdd() {
		if (finalCritDec < 0) {
			int defaultBase = getSysHero().getCritDamageAdd();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getCritDamageAdd();
				}
			}
			finalCritDec = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_CRITDAMAGE, false);
		}
		return finalCritDec;
	}

	/**
	 * 暴击伤害减免最终值
	 * 
	 * @return
	 */
	public int _getCritDamageDec() {
		if (finalCritDamageDec < 0) {
			int defaultBase = getSysHero().getCritDamageDec();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getCritDamageDec();
				}
			}
			finalCritDamageDec = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_CRITDAMAGE_DEC, false);
		}
		return finalCritDamageDec;
	}

	/**
	 * 最终伤害加成最终值
	 * 
	 * @return
	 */
	public int _getDamageAdd() {
		if (finalDamageAdd < 0) {
			finalDamageAdd = (int) getPEffectValue(0,
					EffectTypeDefine.H_DAMAGE_PER, true);
		}
		return finalDamageAdd;
	}

	/**
	 * 最终伤害减免最终值
	 * 
	 * @return
	 */
	public int _getDamageDec() {
		if (finalDamageDec < 0) {
			finalDamageDec = (int) getPEffectValue(0,
					EffectTypeDefine.H_SHIELD_PER, true);
		}
		return finalDamageDec;
	}

	/**
	 * 血量上限最终值
	 * 
	 * @return
	 */
	public int _getInitHp() {
		if (finalInitHp < 0) {
			finalInitHp = (int) getPEffectValue(this.intHp,
					EffectTypeDefine.H_INIT_HP, false);
		}
		return finalInitHp;
	}

	/**
	 * 固定伤害
	 * 
	 * @return
	 */
	public int _getDamage() {
		if (finalDamage < 0) {
			finalDamage = (int) getPEffectValue(0, EffectTypeDefine.H_DAMAGE,
					false);
		}
		return finalDamage;
	}

	/**
	 * 固定减免
	 * 
	 * @return
	 */
	public int _getShield() {
		if (finalShield < 0) {
			finalShield = (int) getPEffectValue(0, EffectTypeDefine.H_SHIELD,
					false);
		}
		return finalShield;
	}

	/**
	 * 士气初始值
	 * 
	 * @return
	 */
	public int _getMorale() {
		if (finalInitMorale < 0) {
			int defaultBase = getSysHero().getInitMorale();
			if (this.isCommander()) {
				CommanderColorProperty ccp = getHeroService()
						.getCommanderColorProperty(this.userId);
				if (ccp != null) {
					defaultBase = ccp.getInitMorale();
				}
			}
			finalInitMorale = (int) getPEffectValue(defaultBase,
					EffectTypeDefine.H_MORALE, false);
		}
		return finalInitMorale;
	}

	/**
	 * transient 字段值重置
	 */
	public void reset() {
		finalMagicAttack = -1;// 法术攻击
		finalPhysicalAttack = -1;// 物理攻击
		finalMagicDefend = -1;// 法术防御
		finalPhysicalDefend = -1;// 物理防御
		finalHit = -1;// 命中
		finalDodge = -1;// 闪避
		finalCritDec = -1;// 免暴率
		finalCritAdd = -1;// 暴击率
		finalCritDamageAdd = -1;// 暴击伤害
		finalCritDamageDec = -1;// 暴击伤害减免
		finalDamageAdd = -1;// 最终伤害加成
		finalDamageDec = -1;// 最终伤害减免
		finalInitHp = -1;// 血量最终值
		finalDamage = -1;// 固定伤害
		finalShield = -1;// 固定减免
		finalInitMorale = -1;// 士气
		effects = null;
		casEffects = null;
		fateEffects = null;
		equipEffects = null;
		titleEffects = null;
	}

	/**
	 * 序列化的时候 transient 字段值重置
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		if (in != null) {
			in.defaultReadObject();
		}
		reset();
	}

	/**
	 * 战力相关的武将效果
	 * 
	 * @param base
	 * @param effectType
	 * @return
	 */
	private double getPEffectValue(int base, String effectType,
			boolean isPercent) {
		EffectValue effectValue = this._getPEffectValue(base, effectType,
				isPercent);
		// TODO 可以继续添加其他外部功能增加的效果
		if (isPercent) {
			return effectValue.getPerValue();
		} else {
			double finalProp = base * (1 + effectValue.getPerValue() / 1000d)
					+ effectValue.getAbsValue();
			return finalProp;
		}
	}

	/**
	 * 战力相关的武将效果-用于直接提取效果值
	 * 
	 * @param base
	 * @param effectType
	 * @param isPercent
	 * @return
	 */
	public EffectValue _getPEffectValue(int base, String effectType,
			boolean isPercent) {
		int abs = 0;
		int percent = 0;

		// 影响所有武将的效果 科技 + 图鉴加成，是按千分比加的
		EffectValue heroEff = getCastleHeroEffectValue(effectType);
		if (heroEff != null) {
			abs = abs + heroEff.getAbsValue();
			percent = percent + heroEff.getPerValue();
		}
		// 武将
		heroEff = getHeroValue(effectType);
		if (heroEff != null) {
			abs = abs + heroEff.getAbsValue();
			percent = percent + heroEff.getPerValue();
		}

		// 装备
		heroEff = getHeroEquipValue(effectType);
		if (heroEff != null) {
			abs = abs + heroEff.getAbsValue();
			percent = percent + heroEff.getPerValue();
		}

		// 加情缘
		heroEff = getFateEffectValue(effectType);
		if (heroEff != null) {
			abs = abs + heroEff.getAbsValue();
			percent = percent + heroEff.getPerValue() * 10;
		}

		// 加军衔效果
		heroEff = this.getTitleValue(effectType);
		if (heroEff != null) {
			abs = abs + heroEff.getAbsValue();
			percent = percent + heroEff.getPerValue();
		}

		// TODO 可以继续添加其他外部功能增加的效果

		EffectValue result = new EffectValue();
		if (isPercent) {
			return result = new EffectValue(0, percent);
		} else {
			result = new EffectValue(abs, percent);
		}
		return result;
	}

	private EffectValue getFateEffectValue(String effectType) {
		if (!isInTroop()) {
			return null;
		}

		if (this.fateEffects != null && !fateEffects.isEmpty()) {
			return fateEffects.get(effectType);
		}
		ISysHeroService sysHeroService = (ISysHeroService) ServiceLocator
				.getSpringBean("sysHeroEntityService");
		if (!StringUtils.isEmpty(this.heroFate)) {
			String[] fateIds = StringUtils.split(this.heroFate, ",");
			fateEffects = new HashMap<String, EffectValue>(8);
			for (String fateId : fateIds) {
				HeroFate fate = sysHeroService.getHeroFate(fateId);
				if (fate != null) {
					for (Map<String, EffectValue> mp : fate.getProperties()) {
						for (Map.Entry<String, EffectValue> entry : mp
								.entrySet()) {
							String key = entry.getKey();
							EffectValue v = fateEffects.get(key);
							if (v == null) {
								EffectValue tmp = entry.getValue();
								fateEffects.put(
										key,
										new EffectValue(tmp.getAbsValue(), tmp
												.getPerValue()));
							} else {
								v.add(entry.getValue());
							}
						}
					}
				}
			}
		}
		TroopGrid tg = getTroopGrid();

		if (!StringUtils.isEmpty(tg.getEquipFate())) {
			String[] fateIds = StringUtils.split(tg.getEquipFate(), ",");
			if (fateEffects == null) {
				fateEffects = new HashMap<String, EffectValue>(8);
			}
			for (String fateId : fateIds) {
				IEquipService equipService = (IEquipService) ServiceLocator
						.getSpringBean("equipEntityService");
				HeroFate fate = equipService.getEquipFateByProcessId(fateId);
				if (fate != null) {
					// 获取激活的是几件套装属性
					Map<String, EffectValue> mp = fate.getEquipFate(HeroFate
							.resoveHeroFateAttrIndex(Integer.valueOf(fateId)));
					for (Map.Entry<String, EffectValue> entry : mp.entrySet()) {
						String key = entry.getKey();
						EffectValue v = fateEffects.get(key);
						if (v == null) {
							EffectValue tmp = entry.getValue();
							fateEffects.put(
									key,
									new EffectValue(tmp.getAbsValue(), tmp
											.getPerValue()));
						} else {
							v.add(entry.getValue());
						}
					}

				}
			}
		}

		if (fateEffects == null) {
			fateEffects = new HashMap<String, EffectValue>(1);
		}

		return fateEffects.get(effectType);
	}

	/**
	 * 加武将强化 进阶 基础属性
	 * 
	 * @param effectType
	 * @return
	 */
	private EffectValue getHeroValue(String effectType) {
		EffectValue value = new EffectValue();
		// 武将进阶加的属性
		int abs = 0;
		int percent = 0;
		if (getRelifeNum() > 0) {
			RelifeLimit relifeLimit = getSysHero().getRelifeLimit(
					getRelifeNum());
			if (relifeLimit != null) {
				for (HeroProperty hp : relifeLimit.getProperties()) {
					if (hp.getPropName().equals(effectType)) {
						abs = abs + hp.getAbsValue();
						percent = percent + hp.getPercentValue();
					}
				}
			}
		}
		value.setAbsValue(abs);
		value.setPerValue(percent);
		return value;
	}

	/**
	 * 武将的装备加成属性
	 * 
	 * @param effectType
	 * @return
	 */
	private EffectValue getHeroEquipValue(String effectType) {
		EffectValue value = new EffectValue();
		if (!isInTroop()) {
			return value;
		}
		// 武将进阶加的属性
		int abs = 0;
		int percent = 0;
		// 武将装备强化加成属性
		if (equipEffects == null) {
			equipEffects = new HashMap<String, EffectValue>();
			TroopGrid tg = getTroopGrid();
			List<Long> eqs = tg.getEquip();
			for (long l : eqs) {
				if (l <= 0) {
					continue;
				}
				Treasury t = getTreasuryService().getTreasuryById(userId, l);
				if (t == null) {
					continue;
				}
				Map<String, ItemProperty> val = getTreasuryService()
						.getEquipAttr(t, percent);
				for (Entry<String, ItemProperty> en : val.entrySet()) {
					EffectValue it = equipEffects.get(en.getKey());
					if (it == null) {
						EffectValue v = new EffectValue();
						v.setAbsValue(en.getValue().getAbsValue());
						v.setPerValue(en.getValue().getPercentValue());
						equipEffects.put(en.getKey(), v);
					} else {
						it.setAbsValue(it.getAbsValue()
								+ en.getValue().getAbsValue());
						it.setPerValue(it.getPerValue()
								+ en.getValue().getPercentValue());
					}
				}
			}

		}
		if (equipEffects.containsKey(effectType)) {
			EffectValue p = equipEffects.get(effectType);
			abs = abs + p.getAbsValue();
			percent = percent + p.getPerValue();
		}
		value.setAbsValue(abs);
		value.setPerValue(percent);
		return value;
	}

	/**
	 * 取得城池效果加成 影响所有武将的效果 科技 + 图鉴加成，是按千分比加的
	 * 
	 * @param effectType
	 * @return
	 */
	private EffectValue getCastleHeroEffectValue(String effectType) {
		if (this.casEffects == null) {
			ICastleEffectService effectService = (ICastleEffectService) ServiceLocator
					.getSpringBean("castleEffectService");

			this.casEffects = effectService.getCastleEffectByCasId(casId);
			if (this.casEffects == null) {
				this.casEffects = new ArrayList<CastleEffect>(0);
			}
		}
		int abs = 0;
		int percent = 0;
		for (CastleEffect e : casEffects) {
			if (e.isExpired()) {
				continue;
			}
			if (!effectType.equals(e.getEffTypeId())) {
				continue;
			}
			abs = abs + e.getAbsValue();
			percent = percent + e.getPerValue();
		}
		if (abs == 0 && percent == 0) {
			return null;
		}
		EffectValue value = new EffectValue();
		value.setAbsValue(abs);
		value.setPerValue(percent);
		return value;
	}

	public IUserTitleService getUserTitleService() {
		if (this.userTitleService == null) {
			userTitleService = (IUserTitleService) ServiceLocator
					.getSpringBean("userTitleService");
		}
		return userTitleService;
	}

	public IUserService getUserService() {
		if (this.userService == null) {
			userService = (IUserService) ServiceLocator
					.getSpringBean("userService");
		}
		return userService;
	}

	/**
	 * 指挥官军衔效果
	 * 
	 * @param effectType
	 * @return
	 */
	private EffectValue getTitleValue(String effectType) {
		EffectValue value = new EffectValue();
		if (!this.isCommander()) {
			return value;
		}

		// 指挥官军衔效果
		if (titleEffects == null) {
			titleEffects = getUserTitleService().getTitleEffectValue(this);
		}

		if (titleEffects.containsKey(effectType)) {
			EffectValue p = titleEffects.get(effectType);
			value.setAbsValue(p.getAbsValue());
			value.setPerValue(p.getPerValue());
		}

		return value;
	}

	/**
	 * 取得系统武将
	 * 
	 * @return
	 */
	public SysHero getSysHero() {
		if (this.sysHeroId > 0 && this.sysHero == null) {
			this.sysHero = getHeroService().getSysHeroById(this.sysHeroId);
		}
		return this.sysHero;
	}

	public IHeroService getHeroService() {
		if (this.heroService == null) {
			heroService = (IHeroService) ServiceLocator
					.getSpringBean("heroService");
		}
		return heroService;
	}

	public ITroopGridService getTroopGridService() {
		if (this.troopGridService == null) {
			troopGridService = (ITroopGridService) ServiceLocator
					.getSpringBean("troopGridService");
		}
		return troopGridService;
	}

	public ITreasuryService getTreasuryService() {
		if (this.treasuryService == null) {
			treasuryService = (ITreasuryService) ServiceLocator
					.getSpringBean("treasuryService");
		}
		return treasuryService;
	}

	/**
	 * 取得武将带的兵种
	 * 
	 * @return
	 */
	public Army getArmy() {
		if (this.army == null) {
			this.army = (Army) getEntityService().getEntity(
					getSysHero().getArmyEntId());
		}
		return this.army;
	}

	/**
	 * 取得技能集合
	 * 
	 * @return
	 */
	public List<Integer> getSkills() {
		List<Integer> list = new ArrayList<Integer>();
		if (skillId1 > 0) {
			list.add(skillId1);
		}
		if (skillId2 > 0) {
			list.add(skillId2);
		}
		if (skillId3 > 0) {
			list.add(skillId3);
		}
		if (skillId4 > 0) {
			list.add(skillId4);
		}
		if (skillId5 > 0) {
			list.add(skillId5);
		}
		if (skillId6 > 0) {
			list.add(skillId6);
		}
		return list;
	}

	public ICombatEngine getCombatEngine() {
		if (combatEngine == null) {
			combatEngine = (ICombatEngine) ServiceLocator
					.getSpringBean("combatEngine");
		}
		return combatEngine;
	}

	public IEntityService getEntityService() {
		if (entityService == null) {
			entityService = (IEntityService) ServiceLocator
					.getSpringBean("entityService");
		}
		return entityService;
	}

	public int _getCurrentPower() {
		if (currentPower > 0) {
			return currentPower;
		}

		int heroSkillLv = 0;

		IEntityService entityService = getEntityService();
		if (this.skillId1 > 0) {
			HeroSkill heroSkill = (HeroSkill) entityService
					.getEntity(this.skillId1);
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}
		if (this.skillId2 > 0) {
			HeroSkill heroSkill = (HeroSkill) entityService
					.getEntity(this.skillId2);
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (this.skillId3 > 0) {
			HeroSkill heroSkill = (HeroSkill) entityService
					.getEntity(this.skillId3);
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (this.skillId4 > 0) {
			HeroSkill heroSkill = (HeroSkill) entityService
					.getEntity(this.skillId4);
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (this.skillId5 > 0) {
			HeroSkill heroSkill = (HeroSkill) entityService
					.getEntity(this.skillId5);
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		if (this.skillId6 > 0) {
			HeroSkill heroSkill = (HeroSkill) entityService
					.getEntity(this.skillId6);
			if (heroSkill != null) {
				heroSkillLv = heroSkillLv + heroSkill.getLevel();
			}
		}

		double atk = 0;
		if (getSysHero().getAttackType() == SysHero.ATTACKTYPE_PHYSICAL) {
			atk = this._getPhysicalAttack();
		} else {
			atk = this._getMagicAttack();
		}
		double tmp1 = atk + this._getMagicDefend() + this._getPhysicalDefend();

		double tmp2 = this._getInitHp() / 5d;

		double tmp3 = (this._getInitHit() + this._getInitDodge()) * 5d;

		double tmp4 = this._getDamage() + this._getShield();

		double tmp5 = 1.0d + this._getCritAdd() + _getCritDec()
				+ _getCritDamageAdd() + _getCritDamageDec() + _getDamageAdd()
				+ _getDamageDec();

		double tmp6 = 1 + heroSkillLv;

		double roit = getCombatEngine().getCombatFactor(CombatFactor.F_COMBAT)
				.getPara1();

		double tmp = Math.pow(
				(((tmp1 + tmp2 + tmp3 + tmp4) + tmp5) * tmp6 * roit), 0.5);

		currentPower = (int) tmp;
		return currentPower;
	}

	/**
	 * 获取武将战力
	 * 
	 * @return
	 */
	public int getCombatPower() {
		return _getCurrentPower();
	}

	public int getGrowing() {
		return growing;
	}

	public void setGrowing(int growing) {
		this.growing = growing;
	}

	public String getHeroFate() {
		return heroFate;
	}

	public void setHeroFate(String heroFate) {
		this.heroFate = heroFate;
	}

	public int getColorInt() {
		if (this.isCommander()) {
			return this.getUserService().getUserById(userId).getColor();
		}
		return this.getSysHero().getEvaluate();
	}

	public String getColor() {
		if (this.isCommander()) {
			return ColorType.valueOf(getColorInt()).getColorVal();
		}
		return this.getSysHero().getColorType().getColorVal();
	}

	public String getHrefStr() {
		if (href == null) {
			href = new StringBuilder(64).append("<font color=\"")
					.append(this.getColor()).append("\">")
					.append(this.getName()).append("</font>").toString();
		}
		return href;
	}

	/**
	 * 此方法每次调用都会重新计算所有的属性
	 * 
	 * @return
	 */
	public HeroAttrInfo getAttrInfo() {
		reset();

		HeroAttrInfo.Builder info = HeroAttrInfo.newBuilder();
		info.setMagicAttack(_getMagicAttack());
		info.setPhysicalAttack(_getPhysicalAttack());
		info.setMagicDefend(_getMagicDefend());
		info.setPhysicalDefend(_getPhysicalDefend());
		info.setHp(_getInitHp());

		info.setHit(_getInitHit());
		info.setDodge(_getInitDodge());
		info.setCritDec(_getCritDec());
		info.setCritAdd(_getCritAdd());

		info.setCritDamageAdd(_getCritDamageAdd());
		info.setCritDamageDec(_getCritDamageDec());
		info.setDamageAdd(_getDamageAdd());
		info.setDamageDec(_getDamageDec());

		info.setDamage(_getDamage());
		info.setShield(_getShield());
		info.setInitMorale(_getMorale());

		info.setCurHp(getCurHp());
		return info.build();
	}

	/**
	 * 获取格子
	 * 
	 * @return
	 */
	public TroopGrid getTroopGrid() {
		TroopGrid tg = getTroopGridService().getTroopGrid(getUserId(),
				troopGridId);
		return tg;
	}

	public HeroInfo toView() {
		HeroInfo.Builder info = HeroInfo.newBuilder();
		info.setHeroId(heroId);
		info.setLevel(level);
		info.setExp(exp);
		info.setGrowing(growing);
		info.setTroopGridId(this.troopGridId);

		info.setSysEntid(sysHeroId);
		info.setAttrInfo(getAttrInfo());
		info.setSkillId1(skillId1);
		info.setSkillId2(skillId2);
		info.setSkillId3(skillId3);
		info.setSkillId4(skillId4);
		info.setSkillId5(skillId5);
		info.setSkillId6(skillId6);
		info.setPower(getCombatPower());

		info.setRelifeNum(getRelifeNum());
		info.setHeroStrength(getHeroStrength());

		info.setLeader(getTeamLeader());
		info.setMainHero(isCommander());

		// 判断武将是否在格子里面。在才有装备
		if (isInTroop()) {
			TroopGrid tg = getTroopGrid();
			info.setTroopId(tg.getTroopId());
			info.setHeroFate(getHeroFate() == null ? "" : getHeroFate());
			info.setEquipFate(tg.getEquipFate() == null ? "" : tg
					.getEquipFate());

			List<Long> eqs = tg.getEquip();
			for (long l : eqs) {
				info.addEquips(l);
			}
		} else {
			int size = EquipPosion.getEquipSize();
			while (size-- > 0) {
				info.addEquips(0);
			}
		}
		return info.build();
	}

	public int getRelifeNum() {
		return relifeNum;
	}

	public void setRelifeNum(int relifeNum) {
		this.relifeNum = relifeNum;
	}

	public int getGrowingItem() {
		return growingItem;
	}

	public void setGrowingItem(int growingItem) {
		this.growingItem = growingItem;
	}

	/**
	 * 武将不在军团里面了
	 */
	public void heroDownTroop() {
		this.setTeamLeader(TeamLeaderType.HERO_COMMON.getIndex());
		this.setTroopGridId(0);
		this.setStatus(HeroTroopIng.HERO_TROOP_OUT.getIndex());
		this.restFate();
	}

	/**
	 * 重置情缘。。 在武将下阵或者退役
	 */
	public void restFate() {
		this.heroFate = null;
	}

	/**
	 * 重置武将技能。。只有在重生的时候调用
	 */
	public void restSkillId() {
		this.skillId1 = getSysHero().getSkillId1();
		this.skillId2 = getSysHero().getSkillId2();
		this.skillId3 = getSysHero().getSkillId3();
		this.skillId4 = getSysHero().getSkillId4();
		this.skillId5 = getSysHero().getSkillId5();
		this.skillId6 = getSysHero().getSkillId6();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setSysHero(SysHero sysHero) {
		this.sysHero = sysHero;
	}

	public long getTroopGridId() {
		return troopGridId;
	}

	public void setTroopGridId(long troopGridId) {
		this.troopGridId = troopGridId;
	}

	/**
	 * 表示武将已经加入军团了
	 * 
	 * @return
	 */
	public boolean isInTroop() {
		return troopGridId != 0;
	}

	public int getHeroStrength() {
		return heroStrength;
	}

	public void setHeroStrength(int heroStrength) {
		this.heroStrength = heroStrength;
	}

	public int getTeamLeader() {
		return teamLeader;
	}

	public void setTeamLeader(int teamLeader) {
		this.teamLeader = teamLeader;
	}

	/**
	 * 是否为队长
	 * 
	 * @return
	 */
	public boolean isTeamLeader() {
		TeamLeaderType tp = TeamLeaderType.valueOf(getTeamLeader());
		if (tp == null) {
			return false;
		}
		return tp.isLeader();
	}

	public int getHeroCardNum() {
		return heroCardNum;
	}

	public void setHeroCardNum(int heroCardNum) {
		this.heroCardNum = heroCardNum;
	}

	public int getHeroSoulNum() {
		return heroSoulNum;
	}

	public void setHeroSoulNum(int heroSoulNum) {
		this.heroSoulNum = heroSoulNum;
	}

	public int getHeroCardEntId() {
		return heroCardEntId;
	}

	public void setHeroCardEntId(int heroCardEntId) {
		this.heroCardEntId = heroCardEntId;
	}

	public int getHeroSoulEntId() {
		return heroSoulEntId;
	}

	public void setHeroSoulEntId(int heroSoulEntId) {
		this.heroSoulEntId = heroSoulEntId;
	}

	/**
	 * true表示是武将。不是图纸或碎片
	 * 
	 * @return
	 */
	public boolean isHero() {
		HeroTroopIng ing = HeroTroopIng.valueOf(getStatus());
		if (ing == null) {
			return false;
		}
		return ing.isHero();
	}

	public int getSumHeroCardNum() {
		return sumHeroCardNum;
	}

	public void setSumHeroCardNum(int sumHeroCardNum) {
		this.sumHeroCardNum = sumHeroCardNum;
	}

	public byte getCommander() {
		return commander;
	}

	public void setCommander(byte commander) {
		this.commander = commander;
	}

	/**
	 * 是否是指挥官武将
	 * 
	 * @return
	 */
	public boolean isCommander() {
		return this.commander != 0;
	}

	/**
	 * 获取坦克碎片图纸信息
	 * 
	 * @return
	 */
	public HeroCardAndDebris getHeroCardAndDebris() {
		HeroCardAndDebris d = new HeroCardAndDebris(getHeroId(),
				getSysHeroId(), getHeroCardNum(), getHeroSoulNum());
		return d;
	}

}
