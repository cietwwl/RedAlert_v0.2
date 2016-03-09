package com.youxigu.dynasty2.combat.skill;

import java.util.List;

import com.youxigu.dynasty2.combat.domain.CombatUnit;
import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.entity.domain.HeroSkillEffect;

public class CombatSkillEffect implements java.io.Serializable {
	
	/**
	 * 持续0-n回合buff
	 * H_武将效果加成  支持百分比和绝对值   SA_技能绝对值加成   SP_技能百分比加成
	 */
	
	/**
	 * 法术攻击
	 */
	public static final String SA_MAGICATTACK = "SA_MAGICATTACK";
	public static final String SP_MAGICATTACK = "SP_MAGICATTACK";
	
	/**
	 * 物理攻击
	 */
	public static final String SA_PHYSICALATTACK = "SA_PHYSICALATTACK";
	public static final String SP_PHYSICALATTACK = "SP_PHYSICALATTACK";
	
	/**
	 * 法术防御
	 */
	public static final String SA_MAGICDEFEND = "SA_MAGICDEFEND";
	public static final String SP_MAGICDEFEND = "SP_MAGICDEFEND";
	
	/**
	 * 物理防御
	 */
	public static final String SA_PHYSICALDEFEND = "SA_PHYSICALDEFEND";
	public static final String SP_PHYSICALDEFEND = "SP_PHYSICALDEFEND";
	
	/**
	 * 命中
	 */
	public static final String SA_Hit = "SA_Hit";
	public static final String SP_Hit = "SP_Hit";
	
	/**
	 * 闪避
	 */
	public static final String SA_DODGE = "SA_DODGE";
	public static final String SP_DODGE = "SP_DODGE";
	
	/**
	 * 免爆率
	 */
	public static final String SA_CRITDEC = "SA_CRITDEC";
	public static final String SP_CRITDEC = "SP_CRITDEC";
	
	/**
	 * 暴击率
	 */
	public static final String SA_CRITADD = "SA_CRITADD";
	public static final String SP_CRITADD = "SP_CRITADD";
	
	/**
	 * 暴击伤害
	 */
	public static final String SA_CRITDAMAGE = "SA_CRITDAMAGE";
	public static final String SP_CRITDAMAGE = "SP_CRITDAMAGE";
	
	/**
	 * 暴击伤害减免 
	 */
	public static final String SA_CRITDAMAGE_DEC = "SA_CRITDAMAGE_DEC";
	public static final String SP_CRITDAMAGE_DEC = "SP_CRITDAMAGE_DEC";
	
	/**
	 * 伤害千分比加成
	 */
	public static final String SA_DAMAGE_PER = "SA_DAMAGE_PER";
	public static final String SP_DAMAGE_PER = "SP_DAMAGE_PER";
	
	/**
	 * 伤害千分比减免
	 */
	public static final String SA_SHIELD_PER = "SA_SHIELD_PER";
	public static final String SP_SHIELD_PER = "SP_SHIELD_PER";
	
	/**
	 * 血量上限
	 */
	public static final String SA_INIT_HP = "SA_INIT_HP";
	public static final String SP_INIT_HP = "SP_INIT_HP";
	
	/**
	 * 兵种克制系数千分比
	 */
	public static final String SA_ARMY_BITE_ROIT = "SA_ARMY_BITE_ROIT";
	public static final String SP_ARMY_BITE_ROIT = "SP_ARMY_BITE_ROIT";
	
	/**
	 * 固定伤害abs
	 */
	public static final String SA_DAMAGE = "SA_DAMAGE";
	public static final String SP_DAMAGE = "SP_DAMAGE";
	
	/**
	 * 固定伤害减免abs
	 */
	public static final String SA_SHIELD = "SA_SHIELD";
	public static final String SP_SHIELD = "SP_SHIELD";
	
	/**
	 * 士气
	 */
	public static final String SA_MORALE = "SA_MORALE";
	public static final String SP_MORALE = "SP_MORALE";
	
	/**
	 * 剧情对话
	 */
	public static final String EFF_STORY = "EFF_STORY";
	
	
	public static final String EFF_NORMAL_ATTACK = "EFF_NORMAL_ATTACK";//普通攻击
	public static final String EFF_MORALE = "EFF_MORALE";//加减士气
	public static final String EFF_TOGETHER_ATTACK = "EFF_TOGETHER_ATTACK";//合击
	
	
	/**
	 * 是否是为普通攻击
	 * @param combatSkillEffect
	 * @return
	 */
	public boolean cheakAttack(CombatSkillEffect combatSkillEffect) {
		if(combatSkillEffect.getEffKey().equals(EFF_NORMAL_ATTACK)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 控制类buff
	 */
	public static final String EFF_CONF = "EFF_CONF"; // 眩晕效果-不能释放技能
	public static final String EFF_CLEAR = "EFF_CLEAR"; // 清除眩晕效果
	public static final String EFF_INVINCIBLE = "EFF_INVINCIBLE"; // 无敌-攻击不受伤害
	public static final String EFF_IMMUNE = "EFF_IMMUNE"; //免疫-不能加buff
	public static final String EFF_SILENCE = "EFF_SILENCE"; //沉默-只能普通攻击
	public static final String EFF_UNSILENCE = "EFF_UNSILENCE"; //缴械-除普通攻击以外
	public static final String EFF_CHAOS = "EFF_CHAOS"; //混乱-改变选取目标的方式
	
	/**
	 * DOT类型
	 */
	public static final String DOT_ADDHP = "DOT_ADD_SHIELD";//dot 加血
	public static final String DOT_LOSTHP = "DOT_LOST_ARMY";//dot 伤血
	
	/**
	 * 技能效果定义
	 */
	private HeroSkillEffect effect;

	/**
	 * 技能释放者
	 */
	private CombatUnit owner;

	/**
	 * 效果的释放者
	 */
	private List<CombatUnit> sources;

	/**
	 * 技能效果目标
	 */
	private List<CombatUnit> targets;

	private CombatSkill combatSkill;
	/**
	 * 效果产生后的值
	 */
	private int value = 0;

	/**
	 * 有效期：持续到的回合数
	 */
	private int validRound;

	/**
	 * 最后一次触发的回合数
	 */
	private int lastTrigerRound;

	/**
	 * 效果渲染器
	 */
	private transient ISkillEffectRender render;

	public HeroSkillEffect getEffect() {
		return effect;
	}

	public void setEffect(HeroSkillEffect effect) {
		this.effect = effect;
	}

	public CombatUnit getOwner() {
		return owner;
	}

	public void setOwner(CombatUnit owner) {
		this.owner = owner;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValidRound() {
		return validRound;
	}

	public void setValidRound(int validRound) {
		this.validRound = validRound;
	}

	public int getLastTrigerRound() {
		return lastTrigerRound;
	}

	public void setLastTrigerRound(int lastTrigerRound) {
		this.lastTrigerRound = lastTrigerRound;
	}

	public ISkillEffectRender getRender() {
		return render;
	}

	public void setRender(ISkillEffectRender render) {
		this.render = render;
	}

	public short getEffType() {
		return effect.getEffType();
	}

	public int getEffId() {
		return effect.getEffId();
	}

	public String getEffKey() {
		return effect.getEffKey();
	}

	public List<CombatUnit> getSources() {
		return sources;
	}

	public void setSources(List<CombatUnit> sources) {
		this.sources = sources;
	}

	public List<CombatUnit> getTargets() {
		return targets;
	}

	public void setTargets(List<CombatUnit> targets) {
		this.targets = targets;
	}

	public CombatSkill getCombatSkill() {
		return combatSkill;
	}

	public void setCombatSkill(CombatSkill combatSkill) {
		this.combatSkill = combatSkill;
	}

	public AbstractCombatAction doRender(CombatUnit target) {
		return this.render.applyEffect(this, target);
	}

}
