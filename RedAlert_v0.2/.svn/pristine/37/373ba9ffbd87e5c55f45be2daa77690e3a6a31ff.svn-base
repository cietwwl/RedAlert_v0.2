package com.youxigu.dynasty2.armyout.domain.qingbao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 被集结
 * */
public class BeAssembled extends MiSiDetail{
	private static final long serialVersionUID = 5099176277875984436L;
	private String originUnionName;// 进攻方发起集结者的联盟名称和指挥官名
	private String originCommanderName;
	
	private Date endTimeDate; // 集结阶段结束时间
	
	private List<String> originCommanderIcons; // 参与集结者的各指挥官头像，最多5个
	
	private int originBasePointX; // 发起集结者的基地坐标
	private int originBasePointY;
	
	/**
	 * 所有的属性都要赋值
	 */
	public BeAssembled(){
		
	}
	/**
	 * 所有的属性都要赋值
	 */
	public BeAssembled(String aimIcon,String aimName,int aimPointX,int aimPointY,String originUnionName,String originCommanderName,
			Date endTimeDate,List<String> originCommanderIcons,int originBasePointX,int originBasePointY){
		super(aimIcon, aimName, aimPointX, aimPointY);
		this.originUnionName = originUnionName;
		this.originCommanderName = originCommanderName;
		this.endTimeDate = endTimeDate;
		this.originCommanderIcons = originCommanderIcons;
		this.originBasePointX = originBasePointX;
		this.originBasePointY = originBasePointY;
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
	public Date getEndTimeDate() {
		return endTimeDate;
	}
	public void setEndTimeDate(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}
	public List<String> getOriginCommanderIcons() {
		return originCommanderIcons;
	}
	public void setOriginCommanderIcons(List<String> originCommanderIcons) {
		this.originCommanderIcons = originCommanderIcons;
	}
	public void setOriginCommanderIcons(String... originCommanderIcons ) {
		List<String> iconList = new ArrayList<String>();
		for (String string : originCommanderIcons) {
			iconList.add(string);
		}
		this.originCommanderIcons = iconList;
	}
	public int getOriginBasePointX() {
		return originBasePointX;
	}
	public void setOriginBasePointX(int originBasePointX) {
		this.originBasePointX = originBasePointX;
	}
	public int getOriginBasePointY() {
		return originBasePointY;
	}
	public void setOriginBasePointY(int originBasePointY) {
		this.originBasePointY = originBasePointY;
	}
	
	
}
