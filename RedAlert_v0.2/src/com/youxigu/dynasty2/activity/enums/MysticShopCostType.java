package com.youxigu.dynasty2.activity.enums;

import java.util.List;

import com.youxigu.dynasty2.activity.domain.MysticShopItem;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.common.service.ICommonService;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.impl.UserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 神秘商店的货币消耗类型
 * 
 * @author fengfeng
 *
 */
public enum MysticShopCostType implements IndexEnum {
	GOLD_TYPE(1, "金矿") {
		@Override
		public void costMoney(User user, UserService userService,
				MysticShopItem item, ICastleResService service,
				ITreasuryService treasuryService, ICommonService commonService) {
			service.doDelRes(user.getMainCastleId(), AppConstants.ENT_RES_GOLD, item.getCostNum(), true, true);

		}
	}, //
	CASH_TYPE(2, "钻石") {
		@Override
		public void costMoney(User user, UserService userService,
				MysticShopItem item, ICastleResService service,
				ITreasuryService treasuryService, ICommonService commonService) {
			if (user.getCash() < item.getCostNum()) {
				throw new BaseException("元宝不足，不能进行购买");
			}
			userService.doConsumeCash(user.getUserId(), item.getCostNum(),
					LogCashAct.SHENMI_BUY_ACTION);

		}
	}, //
	ITEM_TYPE(3, "物品消耗") {
		@Override
		public void costMoney(User user, UserService userService,
				MysticShopItem item, ICastleResService service,
				ITreasuryService treasuryService, ICommonService commonService) {
			int itemId = commonService.getSysParaIntValue(
					AppConstants.HERO_CARD_DISCARD_ENTID, 0);
			int cnt = treasuryService.getTreasuryCountByEntId(user.getUserId(),
					itemId);
			if (cnt < item.getCostNum()) {
				throw new BaseException("坦克币数量不足");
			}
			treasuryService.deleteItemFromTreasury(user.getUserId(), itemId,
					item.getCostNum(), true,
					com.youxigu.dynasty2.log.imp.LogItemAct.LOGITEMACT_58);

		}
	}, //
	;
	private int type;
	private String desc;

	MysticShopCostType(int type, String desc) {
		this.type = type;
		this.desc = desc;
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

	/**
	 * 购买物品消耗
	 * 
	 * @param item
	 * @param service
	 */
	public abstract void costMoney(User user, UserService userService,
			MysticShopItem item, ICastleResService service,
			ITreasuryService treasuryService, ICommonService commonService);

	static List<MysticShopCostType> result = IndexEnumUtil
			.toIndexes(MysticShopCostType.values());

	public static MysticShopCostType valueOf(int index) {
		return result.get(index);
	}
}
