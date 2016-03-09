package com.youxigu.dynasty2.entity.domain.enumer;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.TroopGrid;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 定义所有的准备装备的位置
 * 
 * @author fengfeng
 *
 */
public enum EquipPosion implements IndexEnum {
	//  主炮（攻击）
	//  装甲（防御）
	//  履带（血量）
	//  炮塔（防御和血量）
	//  指挥塔（命中、闪避）
	//  副炮（暴击，免暴，加伤，减伤）
	EQUIP_POSION1(1, "装备1号位置") {
		@Override
		public void setEqu(TroopGrid grid, long treasuryId) {
			grid.setEqu1(treasuryId);
		}

		@Override
		public long getEqu(TroopGrid grid) {
			return grid.getEqu1();
		}
	}, //
	EQUIP_POSION2(2, "装备2号位置") {
		@Override
		public void setEqu(TroopGrid grid, long treasuryId) {
			grid.setEqu2(treasuryId);
		}

		@Override
		public long getEqu(TroopGrid grid) {
			return grid.getEqu2();
		}
	}, //
	EQUIP_POSION3(3, "装备3号位置") {
		@Override
		public void setEqu(TroopGrid grid, long treasuryId) {
			grid.setEqu3(treasuryId);
		}

		@Override
		public long getEqu(TroopGrid grid) {
			return grid.getEqu3();
		}
	}, //
	EQUIP_POSION4(4, "装备4号位置") {
		@Override
		public void setEqu(TroopGrid grid, long treasuryId) {
			grid.setEqu4(treasuryId);
		}

		@Override
		public long getEqu(TroopGrid grid) {
			return grid.getEqu4();
		}
	}, //
	EQUIP_POSION5(5, "装备5号位置") {
		@Override
		public void setEqu(TroopGrid grid, long treasuryId) {
			grid.setEqu5(treasuryId);
		}

		@Override
		public long getEqu(TroopGrid grid) {
			return grid.getEqu5();
		}
	}, //
	EQUIP_POSION6(6, "装备6号位置") {
		@Override
		public void setEqu(TroopGrid grid, long treasuryId) {
			grid.setEqu6(treasuryId);
		}

		@Override
		public long getEqu(TroopGrid grid) {
			return grid.getEqu6();
		}
	}, //
	;

	private static final int MAX_EQUIP = 6;
	private int type;
	private String desc;

	EquipPosion(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 设置每个位置上面的装备
	 * 
	 * @param grid
	 * @param treasuryId
	 */
	public abstract void setEqu(TroopGrid grid, long treasuryId);

	/**
	 * 获取每个位置上面的装备
	 * 
	 * @param grid
	 * @return
	 */
	public abstract long getEqu(TroopGrid grid);

	/**
	 * 获取有多少装备位置
	 * 
	 * @return
	 */
	public static int getEquipSize() {
		return MAX_EQUIP;
	}

	@Override
	public int getIndex() {
		return type;
	}

	static List<EquipPosion> result = IndexEnumUtil.toIndexes(EquipPosion
			.values());

	public static EquipPosion valueOf(int index) {
		return result.get(index);
	}

}
