package com.youxigu.dynasty2.entity.domain;

import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.util.StringUtils;

/**
 * 装备/宝石属性
 * 
 * @author Administrator
 * 
 */
public class ItemProperty {

	private String propName;// 属性名称
	private int value;
	/**
	 * 强化一次加成值
	 */
	private int addValue;

	public boolean abs;// true ,value是绝对值,false,value是百分比

	public ItemProperty() {

	}

	public ItemProperty(String propName, int value, boolean abs) {
		this.propName = propName;
		this.value = value;
		this.abs = abs;
	}

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

	public ItemProperty clone() {
		ItemProperty p = new ItemProperty();
		p.propName = propName;
		p.value = value;
		p.addValue = addValue;
		p.abs = abs;
		return p;
	}

	public String toAttrString() {
		// // H_MAGICATTACK,2,8,A;H_MAGICDEFEND,3,4,A
		StringBuffer sb = new StringBuffer();
		sb.append(getPropName()).append(",");
		sb.append(getValue()).append(",");
		sb.append(getAddValue()).append(",");
		if (isAbs()) {
			sb.append("A");
		} else {
			sb.append("P");
		}
		return sb.toString();
	}

	/**
	 * 解析属性
	 * 
	 * @param value
	 * @return
	 */
	public static List<ItemProperty> parseProperty(String value) {
		List<ItemProperty> properties = new ArrayList<ItemProperty>();
		if (StringUtils.isEmpty(value)) {
			return properties;
		}
		String[] props = StringUtils.split(value, ";");
		for (String one : props) {
			String[] tmp = StringUtils.split(one, ",");
			if (tmp.length <= 1) {
				continue;
			}
			ItemProperty itemProp = new ItemProperty();
			itemProp.setPropName(tmp[0]);
			itemProp.setValue(Integer.parseInt(tmp[1]));
			if (tmp.length >= 4) {
				itemProp.setAddValue(Integer.parseInt(tmp[2]));
				itemProp.setAbs("A".equals(tmp[3]));
			} else {
				itemProp.setAbs("A".equals(tmp[2]));
			}
			properties.add(itemProp);
		}
		return properties;
	}

	public int getAbsValue() {
		if (abs)
			return value;
		else
			return 0;
	}

	public int getPercentValue() {
		if (!abs)
			return value;
		else
			return 0;
	}

	public int getAddValue() {
		return addValue;
	}

	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

}
