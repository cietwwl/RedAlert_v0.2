package com.youxigu.dynasty2.entity.domain;

/**
 * 建筑实体定义
 * 
 */
public class Building extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3896259788190427381L;
    private String buiName;
	private String buiDesc;// 建筑无风水描述
	private int maxLevel;// 最大等级
    private int initialLevel;//建筑的初始等级

    public int getInitialLevel() {
        return initialLevel;
    }

    public void setInitialLevel(int initialLevel) {
        this.initialLevel = initialLevel;
    }

    public String getBuiName() {
        return buiName;
    }

    public void setBuiName(String buiName) {
        this.buiName = buiName;
    }

    public String getBuiDesc() {
        return buiDesc;
    }

    public void setBuiDesc(String buiDesc) {
        this.buiDesc = buiDesc;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public String getEntityName() {
        return this.buiName;
    }
}
