package com.youxigu.dynasty2.armyout.domain.qingbao;

import java.util.List;

/**
 * 我的侦查
 * **/
public class MyDetect extends MiSiDetail {
	private static final long serialVersionUID = -8153411254610997201L;
	
	private String defenderUnionName; // 防守者的联盟名称和指挥官名
	private String defenderCommanderName;
	
	private int defenderAttackPower; // 军团战力值
	private List<General> defenderGenerals; // 军团内的每个武将头像,武将等级，武将耐久值
	
	private DetectAimResources aimResource; // 目标拥有的资源
	
	private int defenderCount;
	/**
	 * 所有的属性都要赋值
	 */
	public MyDetect(){
		
	}
	/**
	 * 所有的属性都要赋值
	 */
	public MyDetect(String aimIcon,String aimName,int aimPointX,int aimPointY,String defenderUnionName,String defenderCommanderName,
			int defenderAttackPower,List<General> defenderGenerals,DetectAimResources aimResource,int defenderCount){
		super(aimIcon, aimName, aimPointX, aimPointY);
		this.defenderUnionName = defenderUnionName;
		this.defenderCommanderName = defenderCommanderName;
		this.defenderAttackPower = defenderAttackPower;
		this.defenderGenerals = defenderGenerals;
		this.aimResource = aimResource;
		this.defenderCount = defenderCount;
	}

	public String getDefenderUnionName() {
		return defenderUnionName;
	}

	public void setDefenderUnionName(String defenderUnionName) {
		this.defenderUnionName = defenderUnionName;
	}

	public String getDefenderCommanderName() {
		return defenderCommanderName;
	}

	public void setDefenderCommanderName(String defenderCommanderName) {
		this.defenderCommanderName = defenderCommanderName;
	}

	public int getDefenderAttackPower() {
		return defenderAttackPower;
	}

	public void setDefenderAttackPower(int defenderAttackPower) {
		this.defenderAttackPower = defenderAttackPower;
	}

	public List<General> getDefenderGenerals() {
		return defenderGenerals;
	}

	public void setDefenderGenerals(List<General> defenderGenerals) {
		this.defenderGenerals = defenderGenerals;
	}

	public DetectAimResources getAimResource() {
		return aimResource;
	}

	public void setAimResource(DetectAimResources aimResource) {
		this.aimResource = aimResource;
	}

	public int getDefenderCount() {
		return defenderCount;
	}

	public void setDefenderCount(int defenderCount) {
		this.defenderCount = defenderCount;
	}
}
