package com.youxigu.dynasty2.entity.domain;

import java.util.List;

/**
 * 武将技能定义
 * 
 * @author Dagangzi
 *
 */
public class HeroSkill extends Entity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8932382925696419387L;
	public static final short FIRED_AT_AFTERATK = 0;// 攻击中发动
	public static final short FIRED_AT_ATTACKED = 1;// 被动技能
	public static final short FIRED_AT_ROUNDBEGIN = 2;// 回合开始前技能
	public static final short FIRED_AT_BEGIN = 3;// 开战前即发动，一直有效

	public static final short SKILLTYPE_0 = 0;// 普通攻击技能
	public static final short SKILLTYPE_1 = 1;// 合击技能
	public static final short SKILLTYPE_2 = 2;// 士气技能
	public static final short SKILLTYPE_3 = 3;// 飞机技能
	public static final short SKILLTYPE_4 = 4;// 被动技能

	private String skillName;
	private int level; // 等级
	private int nextSkillId;// 升级后的技能Id

	private String skillDesc;
	private String icon;
	private String pic;

	/**
	 * 技能品质：（军师技能有效） 0无 1绿 2蓝 3紫 4橙
	 * 
	 * 
	 */
	private int quality;
	/**
	 * 是否是天成技能 =0非天生技能；（后天学习的技能，即被动技能） =1：天生技能；（天生技能，即主动技能）
	 */
	private short ownSkill;

	/**
	 * 技能类型 0-普通攻击技能 1-合击技能 2-士气技能 3-飞机技能 4-被动技能
	 */
	private short skillType;

	/**
	 * 0：攻击中发动 1：被动技能 2：回合开始前技能 3：开战前即发动，一直有效
	 */
	private short firedAt;

	/**
	 * 施放的概率，千分比
	 */
	private int percent;

	/**
	 * 施放消耗的士气
	 */
	private int morale;

	/**
	 * 持续的回合数
	 */
	private int roundPeriod;

	/**
	 * 同一组内的优先级，值越大则优先级越高 同一组内优先级不能相同
	 */
	private int priority;

	/**
	 * 战斗力类型,1=千分比,0=绝对值
	 */
	private byte powerType;
	private int power; // 战斗力

	/**
	 * 按照优先级排序
	 */
	private List<HeroSkillEffect> skillEffects;

	private List<HeroSkillLimit> skillLimits;

	private transient HeroSkill prev;// 上一级技能（低级）=null则没有上一级，为根技能
	private transient HeroSkill next;// 下一级技能(高级)，=null则没有下一级

	private transient HeroSkill root;

	public int getRootSkillId() {
		return this.getRootSkill().getEntId();
	}

	/**
	 * 根技能
	 * 
	 * @return
	 */
	public HeroSkill getRootSkill() {
		if (root == null) {
			root = this;
			while (root.prev != null) {
				root = root.prev;
			}
		}
		return root;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextSkillId() {
		return nextSkillId;
	}

	public void setNextSkillId(int nextSkillId) {
		this.nextSkillId = nextSkillId;
	}

	public String getSkillDesc() {
		return skillDesc;
	}

	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public short getOwnSkill() {
		return ownSkill;
	}

	public void setOwnSkill(short ownSkill) {
		this.ownSkill = ownSkill;
	}

	public short getSkillType() {
		return skillType;
	}

	public void setSkillType(short skillType) {
		this.skillType = skillType;
	}

	public short getFiredAt() {
		return firedAt;
	}

	public void setFiredAt(short firedAt) {
		this.firedAt = firedAt;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getMorale() {
		return morale;
	}

	public void setMorale(int morale) {
		this.morale = morale;
	}

	public int getRoundPeriod() {
		return roundPeriod;
	}

	public void setRoundPeriod(int roundPeriod) {
		this.roundPeriod = roundPeriod;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<HeroSkillEffect> getSkillEffects() {
		return skillEffects;
	}

	public void setSkillEffects(List<HeroSkillEffect> skillEffects) {
		this.skillEffects = skillEffects;
	}

	public List<HeroSkillLimit> getSkillLimits() {
		return skillLimits;
	}

	public void setSkillLimits(List<HeroSkillLimit> skillLimits) {
		this.skillLimits = skillLimits;
	}

	public HeroSkill getPrev() {
		return prev;
	}

	public void setPrev(HeroSkill prev) {
		this.prev = prev;
	}

	public HeroSkill getNext() {
		return next;
	}

	public void setNext(HeroSkill next) {
		this.next = next;
	}

	@Override
	public String getEntityName() {
		return this.skillName;

	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public HeroSkill getHeroSkillByLv(int lv) {
		HeroSkill tmp = this.getRootSkill();
		while (tmp != null) {
			if (tmp.getLevel() == lv) {
				return tmp;
			}
			tmp = tmp.getNext();

		}
		return null;
	}

	public byte getPowerType() {
		return powerType;
	}

	public void setPowerType(byte powerType) {
		this.powerType = powerType;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}
}
