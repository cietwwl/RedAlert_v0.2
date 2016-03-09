package com.youxigu.dynasty2.entity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.entity.domain.effect.EffectDefine;
import com.youxigu.dynasty2.entity.service.IEntityEffectRender;

/**
 * 实体 内政中的资源，建筑，科技，军队，道具以及其他的一些基础物资（比如元宝等）都是实体 具体的实体都继承自这个类。数据均为策划人员准备。
 * 
 * | BUILDING | | TECH | | ITEM | | ARMY | | RESOURCE | | PARTY | DROPPACK | |
 * BOX | | SYSHERO
 * 
 * 对于有等级的实体：<br>
 * 部分是所有等级一个实体ID,部分是每个等级一个实体ID,主要是因为有些实体不同等级属性值不同，有些实体不同等级属性完全相同
 * ，只是capacity/consume/limit不同.<br>
 * 
 * 因此，capacity/consume/limit三个集合,对于所有等级一个实体ID，包含所有等级的相关定义，反之，只包含当前等级的相关定义
 * 
 */
public abstract class Entity implements Serializable, Comparable<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3792477744908626599L;
	// 实体的动作
	public static final int ACTION_LEVEL_UP = 1;
	public static final int ACTION_LEVEL_DOWN = 2;
	public static final int ACTION_LEVEL_MUTLI = 7; // 跨越级别的拆除或者升级
	public static final int ACTION_USE = 3;
	public static final int MISSION_REWARD = 4;
	public static final int ACTION_TAKE = 5;// 册封
	public static final int ACTION_TAKEOFF = 6;// 免除
    public static final int ACTION_ACTIVATE_BUFF = 8;   //激活Buff
	public static final String TYPE_BUILDING = "BUILDING";// 建筑
	public static final String TYPE_TECH = "TECH";
	public static final String TYPE_ITEM = "ITEM";
	public static final String TYPE_ARMY = "ARMY";
	public static final String TYPE_RESOURCE = "RESOURCE";
	public static final String TYPE_PARTY = "PARTY";
	public static final String TYPE_EQUIP = "EQUIP";// 装备
	public static final String TYPE_DROPPACK = "DROPPACK";// 掉落包
	public static final String TYPE_SYS_HERO = "SYS_HERO";// 系统武将

	private int entId;// 元实体id

	private String entType;// 元实体类型-建筑-科技-军队-道具等

	// 效果定义
	private transient List<EffectDefine> effects;

	/**
	 * 实体效果执行器
	 */
	private transient IEntityEffectRender render;

	/**
	 * 建造实体的消耗
	 */
	private transient List<EntityConsume> consumes;

	/**
	 * 建造实体的限制条件
	 */
	private transient List<EntityLimit> limits;

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public String getEntType() {
		return entType;
	}

	public void setEntType(String entType) {
		this.entType = entType;
	}

	public List<EntityConsume> getConsumes() {
		return consumes;
	}

	public void setConsumes(List<EntityConsume> consumes) {
		this.consumes = consumes;
	}

	public List<EntityLimit> getLimits() {
		return limits;
	}

	public void setLimits(List<EntityLimit> limits) {
		this.limits = limits;
	}

	/**
	 * 每个等级对应的消耗
	 * 
	 * @param level
	 *            等级
	 * @return
	 */
	public List<EntityConsume> getConsumesByLevel(int level) {
		// 初始化对应等级的消耗
		List<EntityConsume> tmp = new ArrayList<EntityConsume>();
		if (consumes != null) {
			for (EntityConsume data : consumes) {
				if (data.getLevel() == level) {
					tmp.add(data);
				}
			}
		}
		return tmp;
	}

	/**
	 * 每个等级对应的条件限制
	 * 
	 * @param level
	 *            等级
	 * @return
	 */
	public List<EntityLimit> getLimitsByLevel(int level) {
		// 初始化对应等级的限制
		List<EntityLimit> tmp = new ArrayList<EntityLimit>();
		if (limits != null) {
			for (EntityLimit data : limits) {
				if (data.getLevel() == level) {
					tmp.add(data);
				}
			}
		}
		return tmp;
	}

	public EntityLimit getLimitByLevel(int level, int needEntId) {
		if (limits != null) {
			for (EntityLimit data : limits) {
				if (data.getLevel() == level && data.getEntId() == needEntId) {
					return data;
				}
			}
		}
		return null;
	}

	public List<EffectDefine> getEffects() {
		return effects;
	}

	public void setEffects(List<EffectDefine> effects) {
		this.effects = effects;
	}

	public void setRender(IEntityEffectRender render) {
		this.render = render;
	}

	/**
	 * 取实体的效果
	 * 
	 * @return
	 */
	public List<EffectDefine> getEffects(int level) {
		List<EffectDefine> tmp = new ArrayList<EffectDefine>();
		if (effects != null) {
			for (EffectDefine data : effects) {
				if (data.getLevel() == level) {
					tmp.add(data);
				}
			}
		}
		return tmp;
	}

	public int getConsumeTime(int level) {
		for (EntityConsume data : consumes) {
			if (data.getLevel() == level
					&& data.getNeedEntId() == AppConstants.ENT_PARTY_TIME) {
				return data.getNeedEntNum();
			}
		}
		return 0;
	}

	public IEntityEffectRender getRender() {
		return render;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Entity) {
			return (this.getEntId() - ((Entity) o).getEntId());
		} else {
			return 0;
		}
	}

	public abstract String getEntityName();
}
