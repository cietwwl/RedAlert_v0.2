package com.youxigu.dynasty2.chat;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.Message;

public class EventMessage implements java.io.Serializable {
	private static final long serialVersionUID = -7452037211670256773L;
	public static final int TYPE_VALIDATE_CODE = 1801;// 推验证码
	public static final int TYPE_NEWMAIL = 1001;// 有新邮件

	public static final int TYPE_PASSWOPRD = 1003;// 输入二级密码
	public static final int TYPE_FIRST_PASSWOED_DEL = 1004;// 删除二级密码

	public static final int TYPE_ARMYOUT_BACK = 2004;// 部队回城

	public static final int TYPE_BUILDING_END = 2015;// 建筑完成

	public static final int TYPE_CASTLE_OVER = 3003;// 城池流亡
	public static final int TYPE_CASTLE_ICON = 3005;// 大地图城池外观变化

	public static final int TYPE_CASTLE_CHANGED = 3999;// 城池发生变化
	public static final int TYPE_RESOURCE_CHANGED = 4001;// 资源发生变化
	public static final int TYPE_USER_CHANGED = 4002;// 角色信息生变化
	public static final int TYPE_CASEFF_CHANGED = 4003;// 城池效果发生变化
	public static final int TYPE_CASTLE_BUILDER_CHANGED = 4004;// 建筑队列发生变化
	public static final int TYPE_CASTLE_BUILDING_CHANGED = 4005;// 建筑队列发生变化
	public static final int TYPE_BUFFTIP_CHANGED = 4006;// 主基地Buff发生变化
    public static final int TYPE_USER_TECH_CHANGED = 4007;// 用户科技发生变化

	public static final int TYPE_FRIEND_APP = 4008;// 有好友申请（图标闪烁）
	public static final int TYPE_DEL_BLACKNAME = 4009;// 删除黑名单中的人名

	public static final int TYPE_ACTPOINT_CHANGED = 4010;// 行动力变化
	public static final int TYPE_HPPOINT_CHANGED = 4011;// 体力变化
	public static final int TYPE_TITLE_REDPOINT = 4012;// 军衔小红点

	public static final int TYPE_ADD_FRIEND = 4020;// 增加好友
	public static final int TYPE_DEL_FRIEND = 4021;// 删除好友

	public static final int TYPE_NEW_ACHIEVE = 4202;// 有新成就

	public static final int TYPE_BACK_FRESH_HERO = 5005;// 后台推刷新武将
	public static final int TYPE_BACK_FRESH_FIRED_HERO = 5006;// 后台推刷新解雇武将
	public static final int TYPE_BACK_DELETE_FIRED_HERO = 5007;// 后台推删除解雇武将，雇佣／分解的时候
	public static final int TYPE_BACK_FRESH_HERO_ATTR = 5008;// 单独刷新武将属性信息

	// ////////////////////////////////////////////////////
	public static final int TYPE_HERO_CARD = 5010;// 武将卡变化
	public static final int TYPE_HERO_SOUL = 5011;// 武魂变化
	// //////////////////////////////////////////////////////

	// public static final int TYPE_FINISH_MISSION = 6001;// 任务完成
	// public static final int TYPE_NEW_MISSION=6002;//有新任务
	public static final int TYPE_FRESH_MISSION = 6002;// 刷新任务
	public static final int TYPE_MAIN_MISSION_FINISHCOUNT = 6003;// 推送普通任务未领奖的数量
	public static final int TYPE_WORLD_MISSIONS_FINISHNUM = 6004;// 广播世界任务完成数

	public static final int TYPE_FRESH_ITEM = 7001;// 刷新道具

	public static final int TYPE_TROOP_CHANGED = 7201;// 推刷新troop

	public static final int TYPE_EQUIP_DEBRIS_OR_CARD = 7202;// 推刷新装备图纸和碎片
	public static final int TYPE_AWARD_ACTIVITY_REWARD = 7308;

	public static final int TYPE_NEW_MISI = 7320;// 推新的情报

	public static final int TYPE_SIGLE_RECHARGE_ACTIVITY = 9420;// 广播：单笔充值返利活动开启
	public static final int TYPE_MUTI_RECHARGE_ACTIVITY = 9421;// 广播：累计充值返利活动开启
	public static final int TYPE_RECHARGE_MSG = 9423;// 广播：充值消息推送
	public static final int TYPE_ACTIVITY_REWARD_MSG = 9424;// 有活动奖励，就发送
	public static final int TYPE_TIMER_SHOP_START_MSG = 9426;// 限时商城活动开启，就发送
	public static final int TYPE_TIMER_SHOP_BUY_MSG = 9428;// 有人购买，就发送

	private int eventType;
	private ISendMessage params;

	private static final Map<Integer, String> eventKeys = new HashMap<Integer, String>();
	static {
		// eventKeys.put(TYPE_BACK_FRESH_HERO, "_heros");
		// eventKeys.put(TYPE_BACK_FRESH_FIRED_HERO, "_fheros");
		// eventKeys.put(TYPE_FRESH_ITEM, "_treasurys");
		// eventKeys.put(TYPE_HERO_CARD, "_cards");
		// eventKeys.put(TYPE_HERO_SOUL, "_souls");
		// // eventKeys.put(TYPE_BACK_DELETE_HERO, "_delHiredHero");

	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public ISendMessage getParams() {
		return params;
	}

	public void setParams(ISendMessage params) {
		this.params = params;
	}

	public static String getEventKey(int type) {
		return eventKeys.get(type);
		// if (type == TYPE_BACK_FRESH_HERO) {
		// return "_heros";
		// } else if (type == TYPE_FRESH_ITEM) {
		// return "_treasurys";
		// } else if (type == TYPE_HERO_CARD) {
		// return "_cards";
		// } else if (type == TYPE_HERO_SOUL) {
		// return "_souls";
		// } else {
		// return null;
		// }
	}

	public String toString() {
		return new StringBuilder(64).append("Event,type=").append(eventType)
				.append(",params=").append(params).toString();
	}

	public Message build() {
		return this.params.build();
	}
}
