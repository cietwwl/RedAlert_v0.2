package com.youxigu.dynasty2.entity.domain;

import java.io.Serializable;

import com.youxigu.dynasty2.entity.service.IEntityLimitValidator;

/**
 * 实体消耗--升级建筑，科技等以及生产物资的依赖条件在这里定
 * 
 */
public class EntityLimit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2584664150095918722L;

	private int id;

	private int entId;// 元实体id

	private int level;// 元实体等级

	private int needEntId;// 需要的实体id

	private int needLevel;// 需要的实体等级

	private int leastNum;// 需要的实体数量

	// needEntId对应的实体
	private transient Entity needEntity;
	// 检验器
	private transient IEntityLimitValidator valitor;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNeedEntId() {
		return needEntId;
	}

	public void setNeedEntId(int needEntId) {
		this.needEntId = needEntId;
	}

	public int getNeedLevel() {
		return needLevel;
	}

	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}

	public int getLeastNum() {
		return leastNum;
	}

	public void setLeastNum(int leastNum) {
		this.leastNum = leastNum;
	}

	public Entity getNeedEntity() {
		return needEntity;
	}

	public void setNeedEntity(Entity needEntity) {
		this.needEntity = needEntity;
	}

	public IEntityLimitValidator getValitor() {
		return valitor;
	}

	public void setValitor(IEntityLimitValidator valitor) {
		this.valitor = valitor;
	}

}
