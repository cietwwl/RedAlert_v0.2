package com.youxigu.dynasty2.entity.domain;

import java.io.Serializable;

import com.youxigu.dynasty2.entity.service.IEntityConsumeValidator;

/**
 * 实体消耗--升级建筑，科技等以及生产物资的消耗在这里定
 * 
 * 
 * 
 * @author jiamu
 * 
 */
public class EntityConsume implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1806516893274805850L;

	private int id;

	private int entId;// 元实体id

	private int level;// 元实体等级

	private int needEntId;// 需要的实体id

	private int needEntNum;// 需要的实体数量

	// needEntId对应的实体
	private transient Entity needEntity;

	private transient IEntityConsumeValidator validator;

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

	public int getNeedEntNum() {
		return needEntNum;
	}

	public void setNeedEntNum(int needEntNum) {
		this.needEntNum = needEntNum;
	}

	public Entity getNeedEntity() {
		return needEntity;
	}

	public void setNeedEntity(Entity needEntity) {
		this.needEntity = needEntity;
	}

	public IEntityConsumeValidator getValidator() {
		return validator;
	}

	public void setValidator(IEntityConsumeValidator validator) {
		this.validator = validator;
	}

	public EntityConsume clone() {
		EntityConsume consume = new EntityConsume();
		consume.setId(this.id);
		consume.setEntId(this.entId);
		consume.setLevel(this.level);
		consume.setNeedEntId(this.needEntId);
		consume.setNeedEntity(this.needEntity);
		consume.setNeedEntNum(this.needEntNum);
		consume.setValidator(this.validator);
		return consume;
	}
}
