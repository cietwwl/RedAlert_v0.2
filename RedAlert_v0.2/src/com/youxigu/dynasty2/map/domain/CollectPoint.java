package com.youxigu.dynasty2.map.domain;

import java.sql.Timestamp;

import com.youxigu.dynasty2.map.enums.CollectType;

/**
 * 收藏的坐标点
 * 
 * @author fengfeng
 *
 */
public class CollectPoint {
	private long id;
	/** 所属的用户id */
	private long userId;
	/** 收藏点的名字 */
	private String name;
	/** x坐标 */
	private int posX;
	/** y坐标 */
	private int posY;
	/** 收藏所属的分类 @see CollectType */
	private int collectType;
	/** 创建时间 */
	private Timestamp createTime;

	private transient CollectType type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getCollectType() {
		return collectType;
	}

	public void setCollectType(int collectType) {
		this.collectType = collectType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public CollectType getType() {
		if (type == null) {
			this.type = CollectType.valueOf(this.getCollectType());
		}
		return type;
	}

}
