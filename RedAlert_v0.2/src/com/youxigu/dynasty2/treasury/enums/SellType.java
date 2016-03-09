package com.youxigu.dynasty2.treasury.enums;

import java.util.List;

import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.develop.service.ICastleResService;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 物品出售的货币类型
 * 
 * @author fengfeng
 *
 */
public enum SellType implements IndexEnum {
	GOLD(1, "金矿") {
		@Override
		public boolean sellItem(User user, Item item, int num,
				ICastleResService castleResService,
				ITreasuryService treasuryService) {
			treasuryService.deleteItemFromTreasury(user.getUserId(),
					item.getEntId(), num, true, LogItemAct.LOGITEMACT_69);
			castleResService.doAddRes(user.getMainCastleId(),
					AppConstants.ENT_RES_GOLD, item.getSellPrice() * num, true);
			return true;
		}
	}, //
	;
	private int type;
	private String desc;

	private SellType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	static List<SellType> result = IndexEnumUtil.toIndexes(SellType.values());

	/**
	 * 出售物品
	 * 
	 * @param ty
	 * @param castleResService
	 * @return
	 */
	public abstract boolean sellItem(User user, Item item, int num,
			ICastleResService castleResService, ITreasuryService treasuryService);

	@Override
	public int getIndex() {
		return type;
	}

	public static SellType valueOf(int type) {
		return result.get(type);
	}

}
