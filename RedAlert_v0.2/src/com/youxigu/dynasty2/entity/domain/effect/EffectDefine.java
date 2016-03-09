package com.youxigu.dynasty2.entity.domain.effect;

import java.io.Serializable;

import com.youxigu.dynasty2.entity.service.IEffectRender;

public class EffectDefine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5318713844578200607L;
	// integer(2) null default 0,comment '-1不影响,1=对城池效果,2=对用户效果3=对英雄效果...'
	public static final int TARGET_NONE = -1;
	public static final int TARGET_DEFAULT = 0;
	public static final int TARGET_CASTLE = 1;
	public static final int TARGET_USER = 2;
	public static final int TARGET_HERO = 3;

	private int effId;

	private String effName;//

	private String effDesc;

	private String effTypeId;

	private int level;// 等级

	private int para1;// 百分比

	private int para2;// 绝对值

	private int para3;// qita ,通常是时间限制

	private String iconPath; // 显示图片

	private String buffName; // 对应的buff图标名

    private int buffId; //BuffTip使用

	private String serviceName;// 对应的renderName

	private int target;// 效果针对的对象

	private transient EffectTypeDefine effectType;

	private transient IEffectRender render;

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public String getEffName() {
		return effName;
	}

	public void setEffName(String effName) {
		this.effName = effName;
	}

	public String getEffDesc() {
		return effDesc;
	}

	public void setEffDesc(String effDesc) {
		this.effDesc = effDesc;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getPara1() {
		return para1;
	}

	public void setPara1(int para1) {
		this.para1 = para1;
	}

	public int getPara2() {
		return para2;
	}

	public void setPara2(int para2) {
		this.para2 = para2;
	}

	public int getPara3() {
		return para3;
	}

	public void setPara3(int para3) {
		this.para3 = para3;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public IEffectRender getRender() {
		return render;
	}

	public void setRender(IEffectRender render) {
		this.render = render;
	}

	public EffectTypeDefine getEffectType() {
		return effectType;
	}

	public void setEffectType(EffectTypeDefine effectType) {
		this.effectType = effectType;
	}

	public int getEffId() {
		return effId;
	}

	public void setEffId(int effId) {
		this.effId = effId;
	}

	public String getEffTypeId() {
		return effTypeId;
	}

	public void setEffTypeId(String effTypeId) {
		this.effTypeId = effTypeId;
	}

    public int getBuffId() {
        return buffId;
    }

    public void setBuffId(int buffId) {
        this.buffId = buffId;
    }
}
