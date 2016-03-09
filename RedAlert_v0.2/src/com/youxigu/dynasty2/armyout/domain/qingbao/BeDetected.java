package com.youxigu.dynasty2.armyout.domain.qingbao;

/**
 * 被侦查
 * */
public class BeDetected  extends MiSiDetail{
	private static final long serialVersionUID = -4396842561918995796L;
	private String originIcon; // 侦查方的指挥官头像
	private String originCommanderName; // 侦查方指挥官名
	private int originBasePointX; // 侦查方基地坐标
	private int originBasePointY; // 侦查内容字符串
	
	/**
	 * 所有的属性都要赋值
	 */
	public BeDetected(){
		
	}
	/**
	 * 所有的属性都要赋值
	 */
	public BeDetected(String aimIcon,String aimName,int aimPointX,int aimPointY,String originIcon,String originCommanderName,
			int originBasePointX,int originBasePointY){
		super(aimIcon, aimName, aimPointX, aimPointY);
		this.originIcon = originIcon;
		this.originCommanderName = originCommanderName;
		this.originBasePointX = originBasePointX;
		this.originBasePointY = originBasePointY;
	}
	
	public String getOriginIcon() {
		return originIcon;
	}
	public void setOriginIcon(String originIcon) {
		this.originIcon = originIcon;
	}
	public String getOriginCommanderName() {
		return originCommanderName;
	}
	public void setOriginCommanderName(String originCommanderName) {
		this.originCommanderName = originCommanderName;
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
