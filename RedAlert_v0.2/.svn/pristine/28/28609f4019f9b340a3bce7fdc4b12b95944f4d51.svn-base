package com.youxigu.dynasty2.hero.enums;

import java.util.List;

import com.youxigu.dynasty2.hero.domain.Troop;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 标记上阵武将所在军团中的位置
 * 
 * @author fengfeng
 *
 */
public enum TroopGridPosition implements IndexEnum {
	GRID_POSITION_0(0, "军团1号位置") {
		@Override
		public long getTroopGridId(Troop troop) {
			return troop.getTroopGridId1();
		}

		@Override
		public void setTroopGridId(Troop troop, long troopGridId) {
			troop.setTroopGridId1(troopGridId);

		}
	}, //
	GRID_POSITION_1(1, "军团2号位置") {
		@Override
		public long getTroopGridId(Troop troop) {
			return troop.getTroopGridId2();
		}

		@Override
		public void setTroopGridId(Troop troop, long troopGridId) {
			troop.setTroopGridId2(troopGridId);

		}
	}, //
	GRID_POSITION_2(2, "军团3号位置") {
		@Override
		public long getTroopGridId(Troop troop) {
			return troop.getTroopGridId3();
		}

		@Override
		public void setTroopGridId(Troop troop, long troopGridId) {
			troop.setTroopGridId3(troopGridId);

		}
	}, //
	GRID_POSITION_3(3, "军团4号位置") {
		@Override
		public long getTroopGridId(Troop troop) {
			return troop.getTroopGridId4();
		}

		@Override
		public void setTroopGridId(Troop troop, long troopGridId) {
			troop.setTroopGridId4(troopGridId);

		}
	}, //
	GRID_POSITION_4(4, "军团5号位置") {
		@Override
		public long getTroopGridId(Troop troop) {
			return troop.getTroopGridId5();
		}

		@Override
		public void setTroopGridId(Troop troop, long troopGridId) {
			troop.setTroopGridId5(troopGridId);

		}
	}, //
	GRID_POSITION_5(5, "军团6号位置") {
		@Override
		public long getTroopGridId(Troop troop) {
			return troop.getTroopGridId6();
		}

		@Override
		public void setTroopGridId(Troop troop, long troopGridId) {
			troop.setTroopGridId6(troopGridId);

		}
	}, //
	;
	public static final int MAX_TROOP_GRID = 6;// 最多6个格子

	private int position;
	private String desc;

	TroopGridPosition(int position, String desc) {
		this.position = position;
		this.desc = desc;
	}

	/**
	 * 获取军团里面个格子id
	 * 
	 * @param troop
	 * @return
	 */
	public abstract long getTroopGridId(Troop troop);

	/**
	 * 设置军团里面的格子id
	 * 
	 * @param troop
	 */
	public abstract void setTroopGridId(Troop troop, long troopGridId);

	/**
	 * 通过格子id遍历所有格子 找到对应的格子
	 * 
	 * @param troop
	 * @param troopGridId
	 * @return
	 */
	public static TroopGridPosition getTroopGridPosition(Troop troop,
			long troopGridId) {
		for (TroopGridPosition p : result) {
			if (p.getTroopGridId(troop) == troopGridId) {
				return p;
			}
		}
		throw new BaseException("找不到格子位置:" + troopGridId);
	}

	static List<TroopGridPosition> result = IndexEnumUtil
			.toIndexes(TroopGridPosition.values());

	public static TroopGridPosition valueOf(int index) {
		return result.get(index);
	}

	public int getPosition() {
		return position;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public int getIndex() {
		return position;
	}
}
