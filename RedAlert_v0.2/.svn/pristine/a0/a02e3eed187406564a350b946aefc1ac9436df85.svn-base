package com.youxigu.dynasty2.combat.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.util.Util;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.AddCombatUnitEffectAction;
import com.youxigu.dynasty2.combat.domain.action.FireSkillAction;
import com.youxigu.dynasty2.combat.domain.action.RemoveCombatUnitEffectAction;
import com.youxigu.dynasty2.combat.domain.action.SkillEffectAction;
import com.youxigu.dynasty2.combat.proto.CombatMsg;
import com.youxigu.dynasty2.combat.skill.CombatSkill;
import com.youxigu.dynasty2.combat.skill.CombatSkillEffect;
import com.youxigu.dynasty2.combat.skill.ISkillFiredLimitValidator;
import com.youxigu.dynasty2.combat.skill.SkillEffectFactory;
import com.youxigu.dynasty2.entity.domain.HeroSkill;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;
import com.youxigu.dynasty2.entity.domain.HeroSkillLimit;
import com.youxigu.dynasty2.entity.domain.effect.EffectTypeDefine;

/**
 * 抽象战斗单元，独立进行作战的单位，如带兵的武将，城防设施，城墙等
 * @author Dagangzi
 *
 */
public abstract class CombatUnit implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2631683020385536207L;
	protected static final Logger log = LoggerFactory.getLogger(CombatUnit.class);

	protected static SkillPriorityComparator skillPriorityComparator = new SkillPriorityComparator();

	/**
	 * 技能触发优先级排序
	 * @author Administrator
	 */
	static class SkillPriorityComparator implements Comparator<CombatSkill> {
		@Override
		public int compare(CombatSkill skillA, CombatSkill skillB) {
			return skillB.getPriority() - skillA.getPriority();
		}

	}

	/**
	 *战斗单元的ID,攻击方1-6 防守方7-12
	 */
	protected int id;

	/**
	 * 战斗单元的名称(武将/哨塔/城防/城墙)
	 */
	protected String name;

	/**
	 * 战斗单元的头像
	 */
	protected String icon;

	/**
	 * 战斗单元的等级
	 */
	protected int level;

	protected int unitEntId;// 战斗单元的entId(SysheroId....)，前台显示用
	protected int armyEntId;// 兵种，前台显示用

	protected int initHp;// 初始武将生命值

	protected int curHp;// 当前武将生命值

	protected int shieldHp;//护盾生命值

	protected int initMorale;// 初始士气

	protected int curMorale;// 当前士气

	protected short unitType;// 战斗单元类型：默认为子类名称;

	protected byte attackType;//1物攻  2法攻

	protected int totalHarm;// 统计打掉敌人的血量

	protected int totalLostHp;// 统计累计损失的血量

	/**
	 * 所属的团队
	 */
	protected transient CombatTeam parent;

	protected transient boolean canAttacked = true;// 是否可被攻击

	// 记录当前回合是否已经攻击过
	protected transient boolean currRoundAttacked;

	// 本次被攻击受到的伤害值 ，临时缓存，每次攻击逻辑完成后清零
	protected transient double currHarm;

	//被打击状态-被普通攻击  被攻击后闪避  被暴击  被打死
	protected transient Map<String, Integer> status = new HashMap<String, Integer>();

	/**
	 * 战斗单元的技能列表,key =发动时机
	 */
	protected transient Map<Short, List<CombatSkill>> skills;

	// //////////////////////////武将类技能缓存
	/**
	 * 附加在战斗单元身上的技能效果,BUF类与DOT类
	 */
	protected transient List<CombatSkillEffect> skillEffects;

	/**
	 * 累计的未失效的技能效果和,BUF类与DOT类
	 */
	protected transient Map<String, Integer> skillEffectValues;

	/**
	 * 普通==通常是指瞬间效果，或者说对一次攻击有效，对一次防御有效的效果,一次攻击后清除 或是临时状态 被暴击  被命中等
	 */
	protected transient Map<String, Integer> tmpSkillEffectValues;

	/**
	 * 目前没有使用 支持多个攻击者的行为合并 实现一个行为可描述 nVS1 例如 一个伤血行为描述的事件为(攻击者n个，对目标，
	 * 造成累计伤害30000)
	 */
	private transient CombatUnit[] sources;

	public CombatUnit() {
		skills = new HashMap<Short, List<CombatSkill>>();
	}

	public void init() {
		// do0 nothing
	}

	/**
	 * 战斗单元是否已经死亡
	 */
	public boolean dead() {
		return this.getTotalHp() <= 0;
	}

	/**
	 * 是否可以释放技能
	 * @return
	 */
	public boolean canNotFireSkill(short firedAt) {
		if (this.parent.getParent().isEnd()) {
			//战斗已经结束
			return true;
		}

		if (firedAt != HeroSkill.FIRED_AT_ATTACKED) {
			//眩晕熏晕和死亡不能释放技能
			return this.dead() || this.containsEffect(CombatSkillEffect.EFF_CONF);
		} else {
			//被动技能不检查死亡
			return this.containsEffect(CombatSkillEffect.EFF_CONF);
		}
	}

	/**
	 * 战斗单位释放技能
	 * 检查技能释放的约束，选择第一个满足条件的技能
	 */
	public FireSkillAction doFireSkill(short firedAt, List<AbstractCombatAction> subActions) {
		if (this.canNotFireSkill(firedAt)) {
			return null;
		}

		//移除所有的瞬时效果,移除本次攻击伤害
		Combat combat = this.getParent().getParent();

		//取出的技能初始化时已经按优先级做了排序
		List<CombatSkill> subs = skills.get(firedAt);

		//满足释放条件并且优先级最高的技能
		CombatSkill targetSkill = null;

		if (subs != null && subs.size() > 0) {
			for (CombatSkill combatSkill : subs) {
				if (this.containsEffect(CombatSkillEffect.EFF_SILENCE)
						&& combatSkill.getSkill().getSkillType() != HeroSkill.SKILLTYPE_0) {
					//沉默只能释放普通技能
					continue;
				}
				
				if (this.containsEffect(CombatSkillEffect.EFF_UNSILENCE)
						&& combatSkill.getSkill().getSkillType() == HeroSkill.SKILLTYPE_0) {
					//缴械-除普通攻击以外
					continue;
				}
				
				//回合限制校验
				int currRound = combat.getRound();

				if (combatSkill.getRoundPeriod() != 0) {
					if (combatSkill.getFiredRound() == currRound) {
						//本回合已经释放过了
						continue;
					}

					if (combatSkill.getNextFiredRound() > currRound) {
						//冷却中
						continue;
					}
				}

				//施放概率校验
				if (Util.randInt(1001) > combatSkill.getSkill().getPercent()) {
					continue;
				}

				//limits条件校验-主要针对状态/buff/释放者
				List<HeroSkillLimit> limits = combatSkill.getSkill().getSkillLimits();
				if (limits != null) {
					boolean canFired = false;
					for (HeroSkillLimit limit : limits) {
						ISkillFiredLimitValidator validator = SkillEffectFactory.getSkillFiredLimitValidator(limit
								.getLimitType());
						canFired = validator.checked(combatSkill, limit, this);
						if (!canFired) {
							break;
						}
					}
					if (!canFired) {
						continue;
					}
				}

				//士气校验
				if (this.curMorale < combatSkill.getSkill().getMorale()) {
					continue;
				}

				targetSkill = combatSkill;
				break;
			}
		}

		if (targetSkill == null) {
			if (log.isDebugEnabled()) {
				log.debug("{}没有可以释放的{}", this.getName(), CombatConstants.skillTypeMap.get(firedAt));
			}
			return null;
		}

		FireSkillAction action = targetSkill.doFire(combat, this, targetSkill, subActions);

		return action;
	}

	/**
	 * 按释放时机释放技能
	 * 取满足条件并优先级最高的技能释放
	 */
	public FireSkillAction doFireSkill(short firedAt) {
		Combat combat = this.getParent().getParent();
		List<AbstractCombatAction> subActions = combat.getLastSubActions();// new
		FireSkillAction action = this.doFireSkill(firedAt, subActions);

		return action;
	}

	public Map<Short, List<CombatSkill>> getSkills() {
		return skills;
	}

	public void setSkills(Map<Short, List<CombatSkill>> skills) {
		this.skills = skills;
	}

	/**
	 * 增加临时效果
	 * @param key
	 * @param value
	 */
	public void addTmpSkillEffect(String key, int value) {
		if (tmpSkillEffectValues == null) {
			tmpSkillEffectValues = new HashMap<String, Integer>(10);
			tmpSkillEffectValues.put(key, value);
		} else {
			Integer old = tmpSkillEffectValues.get(key);
			if (old == null) {
				tmpSkillEffectValues.put(key, value);
			} else {
				tmpSkillEffectValues.put(key, value + old);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("{}增加瞬时效果key={},value={}", new Object[] { this.getName(), key, value });
		}
	}

	/**
	 * 增加临时状态，如被暴击，命中，闪避等
	 * @param key
	 * @param value
	 */
	public void addTmpStatus(String key, int value) {
		if (tmpSkillEffectValues == null) {
			tmpSkillEffectValues = new HashMap<String, Integer>(10);
		}
		if (CombatConstants.isAttacked(key)) {
			//加入命中状态
			tmpSkillEffectValues.put("" + CombatConstants.ACTION_ATTACK_HIT, value);
		}
		tmpSkillEffectValues.put(key, value);
	}

	/**
	 * 增加buff效果
	 * @param effect
	 * @param action
	 */
	public void addSkillEffect(CombatSkillEffect effect, SkillEffectAction action) {
		short effType = effect.getEffType();
		if (effType == HeroSkillEffect.EFF_TYPE_GEN) {
			// 瞬时效果直接相加
			String key = effect.getEffKey();
			int value = effect.getValue();
			addTmpSkillEffect(key, value);
		} else {
			if (skillEffects == null) {
				skillEffects = new ArrayList<CombatSkillEffect>();
			}
			HeroSkillEffect he = effect.getEffect();
			// TODO:判断同一个战斗单元施放的相同效果，则延长效果的有效回合数
			int index = 0;
			for (CombatSkillEffect tmp : skillEffects) {
				HeroSkillEffect tmphe = tmp.getEffect();
				if (he.getGroupId() == tmphe.getGroupId()) {
					if (he.getPriority() < tmphe.getPriority()) {
						if (log.isDebugEnabled()) {
							log.debug("{}效果优先级低，忽略", he.getEffId());
						}
						return;//
					} else {
						// 替换
						skillEffects.remove(index);

						String key = tmp.getEffKey();
						int value = tmp.getValue();

						Integer old = skillEffectValues.get(key);
						if (old == effect.getValue()) {
							skillEffectValues.remove(key);
						} else {
							skillEffectValues.put(key, old - value);
						}
						if (log.isDebugEnabled()) {
							log.debug("删除互斥效果{}={}", this.getName(), key);
						}
						if (action != null) {// action==null表示被动技能，不用传前台
//							action.addResult(new RemoveCombatUnitEffectAction(this, tmphe.getEffId()));
						}
						break;
					}
				}
				index++;
			}

			String key = effect.getEffKey();
			int value = effect.getValue();
			skillEffects.add(effect);
			if (action != null && effect.getEffect().getIsShow() > 0) {
				// 前台要求这里传技能ID,不传效果Id
				action.addResult(new AddCombatUnitEffectAction(this, effect.getCombatSkill().getSkillId(), value,
						effect.getValidRound()));
			}

			if (log.isDebugEnabled()) {
				log.debug("{}增加效果key={},value={},持续{}回合", new Object[] { this.getName(), key, value,
						effect.getEffect().getRound() });
			}

			// 加入到合计值
			if (skillEffectValues == null) {
				skillEffectValues = new HashMap<String, Integer>();
				skillEffectValues.put(key, value);
			} else {
				Integer old = skillEffectValues.get(key);
				if (old == null) {
					skillEffectValues.put(key, value);
				} else {
					skillEffectValues.put(key, value + old);
				}
			}

		}

	}

	/**
	 * 删除过期的技能效果
	 * @param currRound
	 */
	public void removeSkillEffect(int currRound) {
		// 武将类
		if (skillEffects != null) {
			Iterator<CombatSkillEffect> lit = skillEffects.iterator();
			while (lit.hasNext()) {
				CombatSkillEffect effect = lit.next();
				if (currRound >= effect.getValidRound()) {
					lit.remove();

					if (log.isDebugEnabled()) {
						log.debug("移除{}的过期的技能效果：{}", this.getName(), effect.getEffKey());
					}
					if (effect.getEffType() != HeroSkillEffect.EFF_TYPE_GEN) {
						String key = effect.getEffKey();
						int value = effect.getValue();

						Integer old = skillEffectValues.get(key);
						if (old == effect.getValue()) {
							skillEffectValues.remove(key);
						} else {
							skillEffectValues.put(key, old - value);
						}

					}
				}
			}
		}

	}

	/**
	 * 按技能效果Key查询施放后的技能效果值
	 * @param type
	 * @return
	 */
	public int getEffectValueByType(String type) {
		int retu = 0;
		// 战斗中持续多回合的buff加成
		if (skillEffectValues != null) {
			Integer value = skillEffectValues.get(type);
			if (value != null) {
				retu = value;
			}
		}

		// 战斗中瞬间buff加成
		if (tmpSkillEffectValues != null) {
			Integer value = tmpSkillEffectValues.get(type);
			if (value != null) {
				retu = retu + value;
			}
		}

		// 来自team的武将效果加成
		// "H_" 可以外部手动添加，调用team.addEff()方法
		if (type.startsWith("SA_")) {
			String tmpType = type.replace("SA_", "H_");
			retu = retu + parent._getAbsEffect(tmpType);

			// 全属性加成
			retu = retu + parent._getAbsEffect(EffectTypeDefine.H_ALL);
		} else if (type.startsWith("SP_")) {
			String tmpType = type.replace("SP_", "H_");
			retu = retu + parent._getPercentEffect(tmpType);

			// 全属性加成
			retu = retu + parent._getPercentEffect(EffectTypeDefine.H_ALL);
		}

		// 来自team的NPC效果加成
		// "N_" 可以外部手动添加，调用team.addEff()方法
		if (this instanceof NPCHeroCombatUnit) {

			String tmpType = "";
			if (type.startsWith("SA_")) {
				// npc绝对值加成
				tmpType = type.replace("SA_", "N_");
				retu = retu + parent._getAbsEffect(tmpType);

				// 全属性加成
				retu = retu + parent._getAbsEffect(EffectTypeDefine.N_ALL);
			} else if (type.startsWith("SP_")) {
				// npc千分比加成
				tmpType = type.replace("SP_", "N_");
				retu = retu + parent._getPercentEffect(tmpType);

				// 全属性加成
				retu = retu + parent._getPercentEffect(EffectTypeDefine.N_ALL);
			}
		}
		return retu;
	}

	/**
	 * 清除unit身上的buff
	 * @param key
	 * @param action
	 */
	public void removeSkillEffect(String key, SkillEffectAction action) {
		if (skillEffectValues != null) {
			Object obj = skillEffectValues.remove(key);
			if (obj != null) {
				if (skillEffects != null) {
					Iterator<CombatSkillEffect> lit = skillEffects.iterator();
					while (lit.hasNext()) {
						CombatSkillEffect tmp = lit.next();
						if (key.equals(tmp.getEffKey())) {
							action.addResult(new RemoveCombatUnitEffectAction(this, tmp.getEffId()));
							lit.remove();
						}
					}
				}
			}
		}

		//临时效果也清理掉
		if (tmpSkillEffectValues != null) {
			tmpSkillEffectValues.remove(key);
		}

		if (log.isDebugEnabled()) {
			log.debug("{}删除效果key={}", new Object[] { this.getName(), key });
		}
	}

	/**
	 * 是否有某种效果
	 * @param type
	 * @return
	 */
	public boolean containsEffect(String type) {
		boolean has = false;
		if (skillEffectValues != null) {
			//跨回合效果中校验
			has = skillEffectValues.containsKey(type);
		}
		if (has) {
			return true;
		} else {
			if (tmpSkillEffectValues != null) {
				//一次性效果中校验
				has = tmpSkillEffectValues.containsKey(type);
				if (has) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 移除所有临时效果
	 */
	public void removeTmpSkillEffectValues() {
		if (tmpSkillEffectValues != null) {
			tmpSkillEffectValues.clear();
		}
	}

	/**
	 * 死掉后清空临时和技能效果
	 */
	public void removeAllkillEffect() {
		skillEffects = null;
		skillEffectValues = null;
		tmpSkillEffectValues = null;
	}

	/**
	 * 执行DOT效果
	 * @param actions
	 * @return
	 */
	public AbstractCombatAction applySkillEffect(
			List<AbstractCombatAction> actions) {
		AbstractCombatAction action = null;
		if (skillEffects != null) {
			Iterator<CombatSkillEffect> lit = skillEffects.iterator();
			while (lit.hasNext()) {
				if (this.dead()) {
					break;
				}
				CombatSkillEffect effect = lit.next();
				AbstractCombatAction tmp = effect.doRender(this);
				if (tmp != null) {
					action = tmp;
					actions.add(action);
				}
			}
		}

		return action;
	}

	public void clean() {
		if (skillEffects != null) {
			skillEffects = null;
		}
		if (skillEffectValues != null) {
			skillEffectValues = null;
		}
		if (tmpSkillEffectValues != null) {
			tmpSkillEffectValues = null;
		}

		this.currRoundAttacked = false;

		if (skills != null) {
			Iterator<List<CombatSkill>> lit = skills.values().iterator();
			while (lit.hasNext()) {
				List<CombatSkill> tmpSkills = lit.next();

				for (CombatSkill s : tmpSkills) {
					s.setFiredCount(0);
					s.setNextFiredRound(0);
					s.setFiredRound(-1);
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CombatTeam getParent() {
		return parent;
	}

	public void setParent(CombatTeam parent) {
		this.parent = parent;
	}

	public String getName() {
		if (log.isDebugEnabled()) {
			return name + "[" + this.id + "]";
		} else {
			return name;
		}
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

	/**
	 * 是否可攻击
	 * @return
	 */
	public boolean isCanAttacked() {
		return canAttacked;
	}

	public void setCanAttacked(boolean canAttacked) {
		this.canAttacked = canAttacked;
	}

	public int getUnitEntId() {
		return unitEntId;
	}

	public void setUnitEntId(int unitEntId) {
		this.unitEntId = unitEntId;
	}

	public int getArmyEntId() {
		return armyEntId;
	}

	public void setArmyEntId(int armyEntId) {
		this.armyEntId = armyEntId;
	}

	public int getInitHp() {
		return initHp;
	}

	public void setInitHp(int initHp) {
		this.initHp = initHp;
	}

	public int getCurHp() {
		return curHp;
	}

	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}

	public int getShieldHp() {
		return shieldHp;
	}

	public void setShieldHp(int shieldHp) {
		this.shieldHp = Math.max(0, shieldHp);
	}

	/**
	 * 减血
	 * @param hp
	 */
	public void reduceTotalHp(int hp) {
		int curShield = this.shieldHp - hp;
		if (curShield < 0) {
			this.curHp = Math.max(0, this.curHp + curShield);
		}
		this.shieldHp = Math.max(0, curShield);
	}

	/**
	 * 统计打掉敌人的血量
	 * 
	 * @param hp
	 */
	public void addTotalHarm(int hp) {
		this.totalHarm = Math.min(Integer.MAX_VALUE, this.totalHarm + hp);
	}

	/**
	 * 统计损失的血量
	 * 
	 * @param hp
	 */
	public void addTotalLostHp(int hp) {
		this.totalLostHp = Math.min(Integer.MAX_VALUE, this.totalLostHp + hp);
	}

	/**
	 * 加血，不能超过上限
	 * @param hp
	 */
	public void addTotalHp(int hp) {
		int current = this.getCurHp();
		int shieldHp = this.getShieldHp();
		int max = this.getInitHp();
		int add = Math.min((max - current - shieldHp), hp);
		this.shieldHp = this.shieldHp + add;
	}

	/**
	 * 取得总血量
	 * @return
	 */
	public int getTotalHp() {
		return this.shieldHp + this.curHp;
	}

	public int getInitMorale() {
		return initMorale;
	}

	public void setInitMorale(int initMorale) {
		this.initMorale = initMorale;
	}

	public int getCurMorale() {
		return curMorale;
	}

	public void setCurMorale(int curMorale) {
		CombatFactor dodgeFactor = this.getParent().getParent().getCombatFactors().get(CombatFactor.F_MORALE_MAX);
		int maxMorale = (int)dodgeFactor.getPara1();
		this.curMorale = Math.min(Math.max(curMorale, 0), maxMorale);
	}

	public short getUnitType() {
		return unitType;
	}

	public void setUnitType(short unitType) {
		this.unitType = unitType;
	}

	public byte getAttackType() {
		return attackType;
	}

	public void setAttackType(byte attackType) {
		this.attackType = attackType;
	}

	public int getTotalHarm() {
		return totalHarm;
	}

	public void setTotalHarm(int totalHarm) {
		this.totalHarm = totalHarm;
	}

	public int getTotalLostHp() {
		return totalLostHp;
	}

	public void setTotalLostHp(int totalLostHp) {
		this.totalLostHp = totalLostHp;
	}

	/**
	 * 当前回合是否已经攻击过
	 */
	public boolean isCurrRoundAttacked() {
		return currRoundAttacked;
	}

	public void setCurrRoundAttacked(boolean currRoundAttacked) {
		this.currRoundAttacked = currRoundAttacked;
	}

	public double getCurrHarm() {
		return currHarm;
	}

	public void setCurrHarm(double currHarm) {
		this.currHarm = currHarm;
	}

	public CombatUnit[] getSources() {
		return sources;
	}

	public void setSources(CombatUnit[] sources) {
		this.sources = sources;
	}

	/**
	 * 取的武将攻击时增加的士气
	 * @return
	 */
	public abstract int _getAttackMorale();

	/**
	 * 取的武将被攻击时增加的士气
	 * @return
	 */
	public abstract int _getDefendMorale();

	/**
	 * 兵种名称
	 * @return
	 */
	public abstract String _getArmyName();

	/**
	 * 法术攻击
	 * @return
	 */
	public abstract int _getMagicAttack();

	/**
	 * 物理攻击
	 * @return
	 */
	public abstract int _getPhysicalAttack();

	/**
	 * 法术防御
	 * @return
	 */
	public abstract int _getMagicDefend();

	/**
	 * 物理防御
	 * @return
	 */
	public abstract int _getPhysicalDefend();

	/**
	 * 命中
	 * @return
	 */
	public abstract int _getInitHit();

	/**
	 * 闪避
	 * @return
	 */
	public abstract int _getInitDodge();

	/**
	 * 免爆率-决定坦克被攻击时的暴击率
	 * @return
	 */
	public abstract int _getCritDec();

	/**
	 * 暴击率-决定坦克攻击时的暴击率
	 * @return
	 */
	public abstract int _getCritAdd();

	/**
	 * 暴击伤害-决定坦克攻击时的暴击伤害。（暂定为1.5倍，后期投放属性）
	 * @return
	 */
	public abstract int _getCritDamageAdd();

	/**
	 * 爆伤减免-决定坦克被攻击时的暴击伤害。（暂定为1.5倍，后期投放属性）
	 * @return
	 */
	public abstract int _getCritDamageDec();

	/**
	 * 伤害加成
	 * @return
	 */
	public abstract int _getDamageAdd();

	/**
	 * 伤害减免
	 * @return
	 */
	public abstract int _getDamageDec();

	/**
	 * 兵种克制系数
	 * @return
	 */
	public abstract int _getArmyBiteRoit();

	/**
	 * 战力
	 * @return
	 */
	public abstract int _getCurrentPower();

	/**
	 * 固定伤害
	 * @return
	 */
	public abstract int _getDamage();

	/**
	 * 固定减免
	 * @return
	 */
	public abstract int _getShield();

	/**
	 * 士气
	 * @return
	 */
	public abstract int _getMorale();
	
	/**
	 * 是否为队长
	 * @return
	 */
	public abstract boolean _isLeader();

	/**
	 * 同触发时机的技能按优先级排序
	 */
	public void sortSkill() {
		//同触发时机的技能按优先级排序
		Iterator<Entry<Short, List<CombatSkill>>> itl = this.getSkills().entrySet().iterator();
		while (itl.hasNext()) {
			Entry<Short, List<CombatSkill>> ent = itl.next();
			List<CombatSkill> subs = ent.getValue();
			//按优先级排序
			if (subs != null && subs.size() > 1) {
				Collections.sort(subs, skillPriorityComparator);
			}

		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this.id == ((CombatUnit) obj).getId()) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("战斗单元：").append(this.getName());

		builder.append(",兵种:").append(this._getArmyName());
		builder.append(",初始血量:").append(this.initHp);
		builder.append(",剩余血量:").append(this.curHp);
		builder.append("\n");
		return builder.toString();
	}

	public CombatMsg.CombatUnit toCombatUnit() {
		CombatMsg.CombatUnit.Builder combatUnitBr = CombatMsg.CombatUnit.newBuilder();
		combatUnitBr.setId(this.id);
		combatUnitBr.setName(this.name);
		combatUnitBr.setIcon(this.icon);
		combatUnitBr.setLevel(this.level);
		combatUnitBr.setUnitEntId(this.unitEntId);
		combatUnitBr.setArmyEntId(this.armyEntId);
		combatUnitBr.setInitHp(this.initHp);
		combatUnitBr.setCurHp(this.curHp);
		combatUnitBr.setShieldHp(this.shieldHp);
		combatUnitBr.setInitMorale(this.initMorale);
		combatUnitBr.setCurMorale(this.curMorale);
		combatUnitBr.setUnitType(this.unitType);
		combatUnitBr.setAttackType(this.attackType);
		return combatUnitBr.build();
	}
}
