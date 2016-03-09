package com.youxigu.dynasty2.hero.enums;

import java.sql.Timestamp;
import java.util.List;

import com.youxigu.dynasty2.hero.domain.UserPubAttr;
import com.youxigu.dynasty2.log.imp.LogCashAct;
import com.youxigu.dynasty2.util.IndexEnum;

/**
 * 酒馆抽奖类型
 * 
 * @author fengfeng
 *
 */
public enum HeroDrawType implements IndexEnum {
	HERO_DRAW_TYPE1(1, "普通征召 ", LogCashAct.CHOUJIANG_ACTION_1,
			LogCashAct.SHILIANCHOU_ACTION_1) {
		@Override
		public Timestamp getHireCD(UserPubAttr pub) {
			return pub.getHireCD1();
		}

		@Override
		public void setHireNum(UserPubAttr pub, int num) {
			pub.setHireNum1(num);

		}

		@Override
		public void setHireCD(UserPubAttr pub, Timestamp tm) {
			pub.setHireCD1(tm);

		}

		@Override
		public int getHireNum(UserPubAttr pub) {
			return pub.getHireNum1();
		}

		@Override
		public void refreshDailyData(UserPubAttr pub) {
			pub.setFreetimes1(0);

		}

		@Override
		public void addFreetimes(UserPubAttr pub) {
			pub.setFreetimes1(pub.getFreetimes1() + 1);

		}

		@Override
		public int getFreetimes(UserPubAttr pub) {
			return pub.getFreetimes1();
		}
	}, //
	HERO_DRAW_TYPE2(2, "高级征召", LogCashAct.CHOUJIANG_ACTION_2,
			LogCashAct.SHILIANCHOU_ACTION_2) {
		@Override
		public Timestamp getHireCD(UserPubAttr pub) {
			return pub.getHireCD2();
		}

		@Override
		public void setHireNum(UserPubAttr pub, int num) {
			pub.setHireNum2(num);

		}

		@Override
		public void setHireCD(UserPubAttr pub, Timestamp tm) {
			pub.setHireCD2(tm);

		}

		@Override
		public int getHireNum(UserPubAttr pub) {
			return pub.getHireNum2();
		}

		@Override
		public void refreshDailyData(UserPubAttr pub) {
			pub.setFreetimes2(0);
		}

		@Override
		public void addFreetimes(UserPubAttr pub) {
			pub.setFreetimes2(pub.getFreetimes2() + 1);

		}

		@Override
		public int getFreetimes(UserPubAttr pub) {
			return pub.getFreetimes2();
		}
	}, //
	HERO_DRAW_TYPE3(3, "特殊类型征召", LogCashAct.CHOUJIANG_ACTION_3,
			LogCashAct.SHILIANCHOU_ACTION_3) {
		@Override
		public Timestamp getHireCD(UserPubAttr pub) {
			return pub.getHireCD3();
		}

		@Override
		public void setHireNum(UserPubAttr pub, int num) {
			pub.setHireNum3(num);

		}

		@Override
		public void setHireCD(UserPubAttr pub, Timestamp tm) {
			pub.setHireCD3(tm);

		}

		@Override
		public int getHireNum(UserPubAttr pub) {
			return pub.getHireNum3();
		}

		@Override
		public void refreshDailyData(UserPubAttr pub) {
			pub.setFreetimes3(0);

		}

		@Override
		public void addFreetimes(UserPubAttr pub) {
			pub.setFreetimes3(pub.getFreetimes3() + 1);

		}

		@Override
		public int getFreetimes(UserPubAttr pub) {
			return pub.getFreetimes3();
		}
	};
	private static final int SUM_DRAW = 3;
	private int type;
	private String desc;
	private LogCashAct log;
	private LogCashAct log10;

	HeroDrawType(int type, String desc, LogCashAct log, LogCashAct log10) {
		this.type = type;
		this.desc = desc;
		this.log = log;
		this.log10 = log10;
	}

	public abstract Timestamp getHireCD(UserPubAttr pub);

	public abstract int getHireNum(UserPubAttr pub);

	public abstract void setHireCD(UserPubAttr pub, Timestamp tm);

	public abstract void setHireNum(UserPubAttr pub, int num);

	public abstract void refreshDailyData(UserPubAttr pub);

	/**
	 * 增加每日免费抽奖次数
	 * 
	 * @param pub
	 */
	public abstract void addFreetimes(UserPubAttr pub);

	/**
	 * 获得每日抽奖次数
	 * 
	 * @param pub
	 */
	public abstract int getFreetimes(UserPubAttr pub);

	public static int size() {
		return SUM_DRAW;
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

	public LogCashAct getLog() {
		return log;
	}

	public LogCashAct getLog10() {
		return log10;
	}

	static List<HeroDrawType> result = IndexEnumUtil.toIndexes(HeroDrawType
			.values());

	public static HeroDrawType valueOf(int index) {
		return result.get(index);
	}

}
