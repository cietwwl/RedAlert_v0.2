package com.youxigu.dynasty2.entity.domain;

import java.util.List;

/**
 * 武将技能效果定义
 * 
 * @author Dagangzi
 *
 */
public class HeroSkillEffect implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6786964796127031147L;
	public static final short EFF_TYPE_GEN = 0;
	public static final short EFF_TYPE_CONTROLBUF = 1;// 控制类BUF
	public static final short EFF_TYPE_BUF = 2;
	public static final short EFF_TYPE_DOT = 4;
	/**
	 * 效果ID
	 */
	private int effId;

	private String effPic;

	/**
	 * 组ID,组内的效果互斥，按priority来决定.不同组的同时存在，即使效果Key相同。通常是BUF类，DOT类有效，普通类不考虑吧.
	 */
	private int groupId;
	/**
	 * 同一组内的优先级，值越大则优先级越高 同一组内优先级不能相同
	 */
	private int priority;

	/**
	 * 目标
	1：自己
	2：对前排单体造成伤害
	3：对后排单体造成伤害
	4：对前排3个单位造成伤害。
	5：对后排3个单位造成伤害
	6：对1列敌人造成伤害
	7：对全体敌人造成伤害
	8：己方全体,不包括城墙，城防
	9：己方随机n个部队(不包括城墙)
	10：对自己进行攻击的战斗单元；
	11：敌方随机n个部队(不包括城墙)
	12：己方特定兵种(不包括城墙)
	13：敌方特定兵种(不包括城墙)
	14：敌方特定国家的单位(不包括城墙)
	15：我方特定国家的单位(不包括城墙)
	 */
	private short target;

	/**
	 * 最大目标数，<=0表示不限制 仅对target=4有效
	 */
	private short targetNum;

	/**
	 * 目标兵种entId
	 */
	private int targetArmy;

	/**
	 * 目标兵种国家属性
	 */
	private int targetCountryId;

	/**
	 * 0普通类，通常是指瞬间效果，或者说对一次攻击有效，对一次防御有效的效果。
	 * 1 控制类BUF,这类的参数是指效果发生的百分比
	 * 2BUF类
	 * 4DOT类
	 */
	private short effType;

	/**
	 * 目标属性
	 * 
	 * 这个Key实际上只对BUF/DEBUF有效， 另外两类的处理不需要这个
	 */
	private String effKey;

	/**
	 * 技能效果持续回合数
	 */
	private int round;

	/**
	 * 系数1,
	 */
	private int para1;
	/**
	 * 系数2:
	 */
	private String para2;
	
	private int isShow;//0不展示  1展示
	
	private int skillId;//临时查询用,加载配数的sql会用到

	private transient String effTypeStr;

	private List<HeroSkillEffectLimit> skillEffectLimits;
	
	private transient int[] entIds;//合击的武将id

	private short owner;// 选取目标的方式

	public int getEffId() {
		return effId;
	}

	public void setEffId(int effId) {
		this.effId = effId;
	}

	public String getEffPic() {
		return effPic;
	}

	public void setEffPic(String effPic) {
		this.effPic = effPic;
	}

	public short getTarget() {
		return target;
	}

	public void setTarget(short target) {
		this.target = target;
	}

	public short getTargetNum() {
		return targetNum;
	}

	public void setTargetNum(short targetNum) {
		this.targetNum = targetNum;
	}

	public int getTargetArmy() {
		return targetArmy;
	}

	public void setTargetArmy(int targetArmy) {
		this.targetArmy = targetArmy;
	}

	public int getTargetCountryId() {
		return targetCountryId;
	}

	public void setTargetCountryId(int targetCountryId) {
		this.targetCountryId = targetCountryId;
	}

	public short getEffType() {
		return effType;
	}

	public void setEffType(short effType) {
		this.effType = effType;
		this.effTypeStr = String.valueOf(effType);
	}

	public String getEffKey() {
		return effKey;
	}

	public void setEffKey(String effKey) {
		this.effKey = effKey;
	}

	public int getPara1() {
		return para1;
	}

	public void setPara1(int para1) {
		this.para1 = para1;
	}

	public String getPara2() {
		return para2;
	}

	public void setPara2(String para2) {
		this.para2 = para2;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getEffTypeStr() {
		return effTypeStr;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public List<HeroSkillEffectLimit> getSkillEffectLimits() {
		return skillEffectLimits;
	}

	public void setSkillEffectLimits(List<HeroSkillEffectLimit> skillEffectLimits) {
		this.skillEffectLimits = skillEffectLimits;
	}

	public int[] getEntIds() {
		return entIds;
	}

	public void setEntIds(int[] entIds) {
		this.entIds = entIds;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public short getOwner() {
		return owner;
	}

	public void setOwner(short owner) {
		this.owner = owner;
	}

}
