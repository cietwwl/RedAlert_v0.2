package com.youxigu.dynasty2.vip.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * vip实体
 */
public class UserVip implements Serializable {

	private static final long serialVersionUID = -7862389193306608823L;

	public static final byte PRIZE = 1;// 已经领过了

	private static long MASK = 0xF;
	public static final int PACK = 0;
	public static final int BODY = 4;
	public static final int ACTION = 8;
	public static final int TACTIC = 12;
	public static final int SHOWTIPS = 3;// 三天有效期时提示用户续费

	private long userId;
	private int vipPoint;// vip成长值
	private Timestamp vipStartTime;// vip开始时间
	private Timestamp vipEndTime;// vip到期时间
	private Timestamp lastDttm;// 最后一次计算成长值的时间
	private int vipLv;// vip等级

	/***************************************************************************
	 * prizeCount: 是否可领礼包(4bit) 0可以领 1不可以领 | 体力值次数(4bit) | 行动力次数(4bit) |
	 * 每个存4位，最多存16个
	 **************************************************************************/
	private long prizeCount;

	/**
	 * 充值元宝的总数
	 */
	private int totalCash;

	/**
	 * 终生购买一次 按位存储等级对应的唯一礼包记录[.....3,2,1]
	 */
	private int reward;

	/**
	 * 每日购买按位存储等级对应的唯一礼包记录[.....3,2,1]
	 */
	private int everyReward;
	/**
	 * 收费终身礼包购买按位存储等级对应的唯一礼包记录[.....3,2,1]
	 */
	private int unfreeReward;

	public long getPrizeCount() {
		return prizeCount;
	}

	public void setPrizeCount(long prizeCount) {
		this.prizeCount = prizeCount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getVipPoint() {
		return vipPoint;
	}

	public void setVipPoint(int vipPoint) {
		this.vipPoint = vipPoint;
	}

	public Timestamp getVipStartTime() {
		return vipStartTime;
	}

	public void setVipStartTime(Timestamp vipStartTime) {
		this.vipStartTime = vipStartTime;
	}

	public Timestamp getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(Timestamp vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

	public Timestamp getLastDttm() {
		return lastDttm;
	}

	public void setLastDttm(Timestamp lastDttm) {
		this.lastDttm = lastDttm;
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}

	/**
	 * @author ninglong
	 *         <p>
	 *         是否过期 true=过期
	 *         </p>
	 * @return
	 */
	public boolean isExpire() {
		// Timestamp t = DateUtils.nowTimestamp();
		// if (vipEndTime != null && vipEndTime.after(t)) {// 未过期
		// return false;
		// } else {
		// return true;
		// }
		return false;
	}

	/**
	 * @author ninglong
	 *         <p>
	 *         是否可以领取vip礼包 1＝可以领取0不可以领取
	 *         </p>
	 * @return
	 */
	public long isGetBag() {
		// if (!this.isExpire()) {
		return prizeCount & 0x0F;
		// } else {
		// return 1L;
		// }
	}

	/**
	 * @author ninglong
	 *         <p>
	 *         </p>
	 * @param offset
	 *            第几位开始
	 * @param maxValue
	 *            最大的值
	 * @param addValue
	 *            每次增加或减少的
	 * @return
	 */
	public boolean _addPrizeCount(int offset, byte maxValue, byte addValue) {
		boolean flag = false;
		long mask = MASK << offset;
		long unMask = ~mask;// 只有offset位是0其于全是1
		int left = 60 - offset;
		long tmp = prizeCount << left;// 去掉offset左边的数
		tmp >>>= 60;// 去掉offset右边的所以位
		if (tmp < maxValue) {
			tmp = tmp + addValue;
			if (tmp > maxValue) {
				tmp = maxValue;
			}
			tmp <<= offset;
			prizeCount = prizeCount & unMask | tmp;// 此句前部分是将offset位保持为0不变将其于各位原数加上，后面是将offset位加上
			flag = true;
		}
		return flag;
	}

	/**
	 * @return 体力使用的次数
	 */
	public int getBodyCount() {
		int left = 60 - UserVip.BODY;
		long tmp = prizeCount << left;
		tmp >>>= 60;
		return (int) tmp;
	}

	/**
	 * @return 行动力使用次数
	 */
	public int getActionCount() {
		int left = 60 - UserVip.ACTION;
		long tmp = prizeCount << left;
		tmp >>>= 60;
		return (int) tmp;
	}

	/**
	 * @return 策略使用次数
	 */
	public int getTacticCount() {
		int left = 60 - UserVip.TACTIC;
		long tmp = prizeCount << left;
		tmp >>>= 60;
		return (int) tmp;
	}

	public int getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(int totalCash) {
		this.totalCash = totalCash;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public boolean isReceive(int vipLv) {
		int pos = vipLv - 1;
		int temp = reward >> pos;
		return (temp & 0x1) == 0x1;
	}

	public boolean isEveryReceive(int vipLv) {
		int pos = vipLv - 1;
		int temp = everyReward >> pos;
		return (temp & 0x1) == 0x1;
	}

	public boolean isUnfreeReceive(int vipLv) {
		int pos = vipLv - 1;
		int temp = unfreeReward >> pos;
		return (temp & 0x1) == 0x1;
	}

	public int getEveryReward() {
		return everyReward;
	}

	public void setEveryReward(int everyReward) {
		this.everyReward = everyReward;
	}

	public int getUnfreeReward() {
		return unfreeReward;
	}

	public void setUnfreeReward(int unfreeReward) {
		this.unfreeReward = unfreeReward;
	}

}
