package com.youxigu.dynasty2.log.imp;

public enum LogCashAct {

	CHONGZHI_ACTION(LogCashAct.CASH_TYPE_1, 1, "充值"), //
	CREATE_USER_ACTION(LogCashAct.CASH_TYPE_1, 2, "创建角色"), //
	CHONGZHI_SENGSONG_ACTION(LogCashAct.CASH_TYPE_1, 3, "充值赠送"), //
	XIAOFEILIBAO_ACTION(LogCashAct.CASH_TYPE_1, 4, "消费礼包"), //
	MONTH_DAY_ZENGSONG_ACTION(LogCashAct.CASH_TYPE_1, 5, "月卡每日赠送"), //
	MONTH_ZENGSONG_ACTION(LogCashAct.CASH_TYPE_1, 6, "月卡赠送"), //
	GM_ADD_ACTION(LogCashAct.CASH_TYPE_1, 7, "GM加元宝"), //
	IN_ADD_ACTION(LogCashAct.CASH_TYPE_1, 8, "内加元宝"), //
	LICAI_FANLI_ACTION(LogCashAct.CASH_TYPE_1, 9, "理财返利"), //
	ITEM_ACTION(LogCashAct.CASH_TYPE_1, 10, "道具加元宝"), //
	DROP_PACK_ACTION(LogCashAct.CASH_TYPE_1, 11, "掉落包掉落元宝"), //
	CHONGZHI_YUEKA_ACTION(LogCashAct.CASH_TYPE_1, 12, "月卡充值"), //
	IDIP_ADD_ACTION(LogCashAct.CASH_TYPE_1, 13, "idip增加"), //
	IDIP_ADD_CASH_ACTION(LogCashAct.CASH_TYPE_1, 14, "idip增加_算充值"), //
	HAIWAI_MONTH_CARD_ACTION(LogCashAct.CASH_TYPE_1, 15, "月卡超出部分赠送"), //
	HAIWAI_GIFT_ACTION(LogCashAct.CASH_TYPE_1, 16, "海外版赠送"), //
	HERO_CHONGSHENG_ACTION(LogCashAct.CASH_TYPE_1, 17, "重生"), //
	CHOUJIANG_ACTION_1(LogCashAct.CASH_TYPE_1, 18, "骁将招募"), //
	CHOUJIANG_ACTION_2(LogCashAct.CASH_TYPE_1, 19, "名将招募"), //
	CHOUJIANG_ACTION_3(LogCashAct.CASH_TYPE_1, 20, "神将招募"), //
	SHILIANCHOU_ACTION_1(LogCashAct.CASH_TYPE_1, 21, "十连抽骁将招募"), //
	SHILIANCHOU_ACTION_2(LogCashAct.CASH_TYPE_1, 22, "十连抽名将招募"), //
	SHILIANCHOU_ACTION_3(LogCashAct.CASH_TYPE_1, 23, "十连抽神将招募"), //
	GM_DES_ACTION(LogCashAct.CASH_TYPE_1, 24, "GM减元宝"), //
	EQUIP_REBIRTH(LogCashAct.CASH_TYPE_1, 25, "装备回炉"), //
	HEROS_COMPOENTS_SPEED_UP(LogCashAct.CASH_TYPE_1, 26, "坦克生产零件加速"), //
    BUILD_SPEED_UP(LogCashAct.CASH_TYPE_1, 27, "建筑加速"), //
    SHENMI_SHUAXIN_ACTION(LogCashAct.CASH_TYPE_1,28,"神秘商城刷新"),
    SHENMI_BUY_ACTION(LogCashAct.CASH_TYPE_1,29,"神秘商城购买"),
    ACTIVATE_BUFF_ACTION(LogCashAct.CASH_TYPE_1,30,"激活主基地BUFF"),
    BYE_RESOURCE(LogCashAct.CASH_TYPE_1,31,"购买资源"),
    TECH_SPEED_UP(LogCashAct.CASH_TYPE_1, 32, "科技加速"), //
	MISSION_AWARD(LogCashAct.CASH_TYPE_1, 33, "任务奖励"), //
	MAOXIAN_RISK_CLEAR_ACTION(LogCashAct.CASH_TYPE_1, 34, "重置冒险关卡消耗元宝"), //
	;
	/**
	 * 元宝，
	 */
	public static final int CASH_TYPE_1 = 1;
	/**
	 * 古迹货币
	 */
	public static final int CASH_TYPE_2 = 2;

	/**
	 * 功勋
	 */
	public static final int CASH_TYPE_3 = 3;
	public final String iaction;

	public final int id;

	public final int cashType;

	private LogCashAct(int cashType, int id, String iaction) {
		this.iaction = iaction;
		this.id = id;
		this.cashType = cashType;
	}

	public String toString() {
		return iaction;
	}

}
