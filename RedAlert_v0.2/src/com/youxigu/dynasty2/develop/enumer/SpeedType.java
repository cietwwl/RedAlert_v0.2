package com.youxigu.dynasty2.develop.enumer;

import java.util.List;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.entity.domain.enumer.SpeedUpItemType;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 加速
 * 
 * @author fengfeng
 *
 */
public enum SpeedType implements IndexEnum {
	BUILD_PART_SPEED_UP(1, true, SpeedUpItemType.PRODUCE_SPEED_UP_TYPE,
			"生产零件加速", LogCashAct.HEROS_COMPOENTS_SPEED_UP,
			LogItemAct.LOGITEMACT_52) {
		@Override
		public int getDiamondSeconds(ICommonService commonService) {
			int v = commonService.getSysParaIntValue(
					AppConstants.SYS_BARRACK_ONE_ARMY_DIAMOND_NUM, 60);
			return v;
		}
	}, //
    BUILDING_SPEED_UP(2, true, SpeedUpItemType.BUILDING_SPEED_UP_TYPE,
            "建筑升级加速", LogCashAct.BUILD_SPEED_UP,
            LogItemAct.LOGITEMACT_201) {
		@Override
		public int getDiamondSeconds(ICommonService commonService) {
			int v = 20;
			return v;
		}
	}, //
	TECH_SPEED_UP(3, true, SpeedUpItemType.TECH_SPEED_UP_TYPE,
            "科技升级加速", LogCashAct.TECH_SPEED_UP,
            LogItemAct.LOGITEMACT_204) {
		@Override
		public int getDiamondSeconds(ICommonService commonService) {
			int v = 20;
			return v;
		}
	}, //
    ;
	private int type;
	private boolean commSpeed;// 是否可以使用通用加速道具
	private String desc;
	private SpeedUpItemType speedItem;// 特定的加速道具
	private LogCashAct dmlog;// 钻石加速日志
	private LogItemAct log;// 物品加速日志

	SpeedType(int type, boolean commSpeed, SpeedUpItemType speedItem,
			String desc, LogCashAct dmlog, LogItemAct log) {
		this.type = type;
		this.commSpeed = commSpeed;
		this.speedItem = speedItem;
		this.desc = desc;
		this.dmlog = dmlog;
		this.log = log;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIndex() {
		return type;
	}

	public LogItemAct getLog() {
		return log;
	}

	public boolean isCommSpeed() {
		return commSpeed;
	}

	public LogCashAct getDmlog() {
		return dmlog;
	}

	/**
	 * 判断是否为支持的加速道具类型
	 * 
	 * @return
	 */
	public boolean supportSpeedUpItem(SpeedUpItemType type) {
		if (type == null) {
			return false;
		}
		if (isCommSpeed()) {// 支持通用加速道具
			if (SpeedUpItemType.TIMER_SPEED_UP_TYPE.equals(type)) {
				return true;
			}
		}
		return type.equals(speedItem);

	}

	/**
	 * 一颗钻石可以秒多少时间 单位秒
	 * 
	 * @param commonService
	 * @return
	 */
	public abstract int getDiamondSeconds(ICommonService commonService);

	static List<SpeedType> result = IndexEnumUtil.toIndexes(SpeedType.values());

	public static SpeedType valueOf(int index) {
		return result.get(index);
	}

}
