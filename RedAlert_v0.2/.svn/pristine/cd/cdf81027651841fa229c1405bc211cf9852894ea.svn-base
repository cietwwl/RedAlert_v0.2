package com.youxigu.dynasty2.armyout.domain.qingbao;

import java.util.Date;
import java.util.List;

/**
 * 被进攻
 * */
public class BeAttacked  extends MiSiDetail{
	private static final long serialVersionUID = 7867543948277324934L;
	private String originUnionName; // 进攻者的联盟名称和指挥官名
	private String originCommanderName;
	
	private int unionAttackPower;// 军团战力值
	private List<General> originGenerals; // 军团内的每个武将头像,武将等级，武将耐久值
	private Date arriveTime; // 到达时间
	
	private int commanderBasePointX; // 该条目中的指挥官基地坐标
	private int commanderBasePointY;
	
	/**
	 * 所有的属性都要赋值
	 */
	public BeAttacked(){
		
	}
	/**
	 * 所有的属性都要赋值
	 */
	public BeAttacked(String aimIcon,String aimName,int aimPointX,int aimPointY,String originUnionName,String originCommanderName,
			int unionAttackPower,List<General> originGenerals,Date arriveTime,int commanderBasePointX,int commanderBasePointY){
		super(aimIcon, aimName, aimPointX, aimPointY);
		this.originUnionName = originUnionName;
		this.originCommanderName = originCommanderName;
		this.unionAttackPower = unionAttackPower;
		this.originGenerals = originGenerals;
		this.arriveTime = arriveTime;
		this.commanderBasePointX = commanderBasePointX;
		this.commanderBasePointY = commanderBasePointY;
	}
	
	public String getOriginUnionName() {
		return originUnionName;
	}
	public void setOriginUnionName(String originUnionName) {
		this.originUnionName = originUnionName;
	}
	public String getOriginCommanderName() {
		return originCommanderName;
	}
	public void setOriginCommanderName(String originCommanderName) {
		this.originCommanderName = originCommanderName;
	}
	public int getUnionAttackPower() {
		return unionAttackPower;
	}
	public void setUnionAttackPower(int unionAttackPower) {
		this.unionAttackPower = unionAttackPower;
	}
	public List<General> getOriginGenerals() {
		return originGenerals;
	}
	public void setOriginGenerals(List<General> originGenerals) {
		this.originGenerals = originGenerals;
	}
	public Date getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}
	public int getCommanderBasePointX() {
		return commanderBasePointX;
	}
	public void setCommanderBasePointX(int commanderBasePointX) {
		this.commanderBasePointX = commanderBasePointX;
	}
	public int getCommanderBasePointY() {
		return commanderBasePointY;
	}
	public void setCommanderBasePointY(int commanderBasePointY) {
		this.commanderBasePointY = commanderBasePointY;
	}
	
	
}
