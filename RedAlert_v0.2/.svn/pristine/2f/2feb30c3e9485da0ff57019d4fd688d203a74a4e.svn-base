package com.youxigu.dynasty2.hero.domain;

import org.apache.commons.lang.StringUtils;

import com.youxigu.dynasty2.hero.enums.HeroDrawType;
import com.youxigu.dynasty2.log.imp.LogCashAct;

/**
 * 酒馆抽奖配置策划表
 * 
 * @author fengfeng
 *
 */
public class HeroPub {
	/** id主键 */
	private int id;
	/** 抽奖类型@see HeroDrawType */
	private short type;
	/** vip等级不一样。。获取的掉落包不一样 */
	private short vipLv;
	/** 道具抽奖消耗的道具0表示不可以用道具抽奖 */
	private int item;
	private int itemCount;
	private int itemCount10;

	/** 元宝抽将需要的元宝数,<=0不能用元宝抽将 */
	private int cash;
	/** 10连抽的元宝数,≤0只代表不能元宝抽，还是可以道具十连抽 */
	private int cash10;

	/** 物品掉落包 */
	private int dropPackId;
	/** 免费抽将间隔时间（秒），《=0则没有免费间隔时间 */
	private int freePeriod;
	/** 免费抽将武将掉落包Id */
	private int freeDropPackId;
	/**
	 * 必紫次数配置, 逗号分开的部分为必紫的次数 1,4,9,14,20
	 */
	private String luckNum;
	/** 当抽取次数〉luckNum时,没隔luckNum2次必紫 */
	private int luckNum2;
	/**
	 * 必紫武将招募掉落包Id =0表示没有必紫
	 */
	private int luckDropPackId;

	/** 每日的免费次数，≤0则没有免费次数 */
	private int freetimes;
	/** 首次抽奖掉落包id */
	private int firstDropPackId;

	private int[] luckNums;
	private HeroDrawType drawType;

	public HeroPub() {
		super();
	}

	public void check() {
		drawType = HeroDrawType.valueOf(getType());
		if (drawType == null) {
			throw new IllegalArgumentException("抽奖类型不存在 drawType" + getType());
		}

		if (getLuckNum() != null && getLuckNum().length() > 0) {
			String[] arr = StringUtils.split(getLuckNum(), ",");
			int size = arr.length;
			int[] luckNumArr = new int[size];
			for (int i = 0; i < size; i++) {
				luckNumArr[i] = Integer.parseInt(arr[i]);
			}
			this.luckNums = luckNumArr;
		}
	}

	public String getPubName() {
		return drawType.getDesc();
	}

	public LogCashAct getLog() {
		return drawType.getLog();
	}

	public LogCashAct getLog10() {
		return drawType.getLog10();
	}

	public HeroDrawType getDrawType() {
		return drawType;
	}

	/*
	 * 当前次数是否是必紫
	 */
	public boolean isLuckNum(int num) {
		if (this.luckNums == null) {
			return false;
		}
		int[] luckNumArr = this.luckNums;
		int max = luckNumArr[luckNumArr.length - 1];
		if (num > max) {
			if (getLuckNum2() != 0) {
				int mod = (num - max) % getLuckNum2();
				if (mod == 0) {
					return true;
				}
			}
		} else {
			for (int freeNum : luckNumArr) {
				if (freeNum == num) {
					return true;
				}
			}
		}
		return false;

	}

	// 取得下一次必紫的次数
	public int getNextLuckNum(int num) {
		if (this.luckNums == null) {
			return -1;
		}
		int[] luckNumArr = this.luckNums;
		int max = luckNumArr[luckNumArr.length - 1];
		if (num > max) {
			if (getLuckNum2() != 0) {
				int mode = num % getLuckNum2();
				if (mode == 0) {
					return 0;
				} else {
					return getLuckNum2() - mode;
				}
			}
		} else {
			for (int freeNum : luckNumArr) {
				if (freeNum >= num) {
					return freeNum - num;
				}
			}
		}
		return -1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public short getVipLv() {
		return vipLv;
	}

	public void setVipLv(short vipLv) {
		this.vipLv = vipLv;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public int getCash10() {
		return cash10;
	}

	public void setCash10(int cash10) {
		this.cash10 = cash10;
	}

	public int getDropPackId() {
		return dropPackId;
	}

	public void setDropPackId(int dropPackId) {
		this.dropPackId = dropPackId;
	}

	public int getFreePeriod() {
		return freePeriod;
	}

	public void setFreePeriod(int freePeriod) {
		this.freePeriod = freePeriod;
	}

	public int getFreeDropPackId() {
		return freeDropPackId;
	}

	public void setFreeDropPackId(int freeDropPackId) {
		this.freeDropPackId = freeDropPackId;
	}

	public String getLuckNum() {
		return luckNum;
	}

	public void setLuckNum(String luckNum) {
		this.luckNum = luckNum;
	}

	public int getLuckNum2() {
		return luckNum2;
	}

	public void setLuckNum2(int luckNum2) {
		this.luckNum2 = luckNum2;
	}

	public int getLuckDropPackId() {
		return luckDropPackId;
	}

	public void setLuckDropPackId(int luckDropPackId) {
		this.luckDropPackId = luckDropPackId;
	}

	public int getFreetimes() {
		return freetimes;
	}

	public void setFreetimes(int freetimes) {
		this.freetimes = freetimes;
	}

	public int getFirstDropPackId() {
		return firstDropPackId;
	}

	public void setFirstDropPackId(int firstDropPackId) {
		this.firstDropPackId = firstDropPackId;
	}

	public int[] getLuckNums() {
		return luckNums;
	}

	public void setLuckNums(int[] luckNums) {
		this.luckNums = luckNums;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getItemCount10() {
		return itemCount10;
	}

	public void setItemCount10(int itemCount10) {
		this.itemCount10 = itemCount10;
	}

}
