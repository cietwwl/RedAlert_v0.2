package com.youxigu.dynasty2.entity.domain;

/**
 * 系统武将强化属性
 * @author Dagangzi
 *
 */
public class HeroProperty {
	public String propName;// 属性名称
	public int value;
	public boolean abs;// true ,value是绝对值,false,value是百分比

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isAbs() {
		return abs;
	}

	public void setAbs(boolean abs) {
		this.abs = abs;
	}


	
	public int getAbsValue(){
		if (abs)
			return  value;
		else
			return 0;
	}
	
	public int getPercentValue(){
		if (!abs)
			return  value;
		else
			return 0;
	}
}
