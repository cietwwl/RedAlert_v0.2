package com.youxigu.dynasty2.entity.domain;

/**
 * 武将技能约束
 * 
 * @author Dagangzi
 *
 */
public class HeroSkillLimit implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7384182213461005394L;
	private int id;
	private int skillId;
	private String limitType;
	private int para1;
	private String para2;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
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

}
