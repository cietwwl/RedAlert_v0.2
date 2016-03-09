package com.youxigu.dynasty2.entity.domain;

/**
 * 资源实体定义
 * 
 */
public class Resource extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7560815653303053475L;

	private String resName;// 资源名称

	private String resDesc;// 资源描述

	private String iconPath;// 图片

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getEntityName() {
		return this.resName;
	}

}
