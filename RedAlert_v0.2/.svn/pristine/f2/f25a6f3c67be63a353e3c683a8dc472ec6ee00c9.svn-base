package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 成就类型
 * type是大类
 * entId是小类
 * @author Dagangzi
 * @date 2016年1月18日
 */
public class AchieveType implements Serializable {
	public static final int TYPE_RES = 1;// 资源获取
	// 资源的实体id 使用entId

	public static final int TYPE_TECH = 2;// 科技成长
	// 科技的实体id

	public static final int TYPE_MILITARY = 3;// 军事养成
	public static final int TYPE_MILITARY_ENTID1 = 31;// 装备收集
	public static final int TYPE_MILITARY_ENTID2 = 32;// X件Y品质Z级装备
	public static final int TYPE_MILITARY_ENTID3 = 33;// 坦克收集
	public static final int TYPE_MILITARY_ENTID4 = 34;// X辆Z级Y品质坦克
	public static final int TYPE_MILITARY_ENTID5 = 35;// 指挥官等级
	public static final int TYPE_MILITARY_ENTID6 = 36;// 指挥官军衔
	public static final int TYPE_MILITARY_ENTID7 = 37;// 指挥官坦克装备N件紫色品质装备

	public static final int TYPE_BUILDING = 4;// 国家建设
	// 建筑实体id

	public static final int TYPE_GUILD = 5;// 联盟成就
	public static final int TYPE_GUILD_ENTID1 = 51;// 联盟等级

	private int type;// 1-n
	private String name; // 名称
	private String icon; // 图标
	private int num;// 总个数
	private String achieveIds;// 子任务列表

	private List<Achieve> list;// 子任务列表

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getAchieveIds() {
		return achieveIds;
	}

	public void setAchieveIds(String achieveIds) {
		this.achieveIds = achieveIds;
	}

	public List<Achieve> getList() {
		return list;
	}

	public void setList(List<Achieve> list) {
		this.list = list;
	}

	public void addList(Achieve achieve) {
		if (list == null) {
			list = new ArrayList<Achieve>();
		}
		list.add(achieve);
	}
}
