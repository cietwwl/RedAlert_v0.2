package com.youxigu.dynasty2.entity.domain.enumer;

import java.util.List;

import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 定义所有的颜色类型
 * 
 * @author fengfeng
 *
 */
public enum ColorType implements IndexEnum {
	//  品质 1-7 白 绿 蓝 紫 橙 金 红
	WHITE(1, "白色", "#ffffff"), //
	GREEN(2, "绿 色", "#00ffff"), //
	BLUE(3, "蓝色", "#FC00FF"), //
	VIOLET(4, "紫色", "#FDAD35"), //
	Orange(5, "橙色", "##FFFF00"), //
	GOLDEN(6, "金色", "#FD3535"), //
	RED(7, "红色", "#FFEB00"), //
	;
	private int type;
	private String desc;
	private String colorVal;

	ColorType(int type, String desc, String colorVal) {
		this.type = type;
		this.desc = desc;
		this.colorVal = colorVal;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIndex() {
		return type;
	}

	static List<ColorType> result = IndexEnumUtil.toIndexes(ColorType.values());

	public static ColorType valueOf(int index) {
		return result.get(index);
	}

	public String getColorVal() {
		return colorVal;
	}

}
