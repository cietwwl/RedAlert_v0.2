package com.youxigu.dynasty2.risk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.dynasty2.util.TimeUtils;

/**
 * 每一关的数据信息<br/>
 * 关于每个关卡的的数据记录表示,数据采用long表示 64位 @param MAX_BIT=63<br/>
 * star 表示获得的星数 最大值 3 ------------------------2个字节，所在的位置<br/>
 * joinNum 已经通关的次数 最大值 32767 ------------------------16个字节，所在的位置(joinNum <<(2)<br/>
 * failNum 已经失败的次数 最大值 32767 ------------------------16个字节，所在的位置(failNum <<(2 +
 * 16)<br/>
 * restNum 已经重置次数 最大值 1024 ------------------------16个字节，所在的位置(restNum << (2 +
 * 16 + 16)<br/>
 * firstBonus 已经领取首通奖励 最大值1 ------------------------1个字节，所在的位置(firstBonus <<( 2
 * + 16 + 16 + 10)<br/>
 * restFailNum 已经领取首通奖励 最大值32 ------------------------45个字节，所在的位置(restFailNum
 * <<(2 + 16 + 16 + 10 +1))<br/>
 * 
 * @author fengfeng
 *
 */
public class OneRisk implements Serializable {
	private static final long serialVersionUID = 683583798188708869L;
	private static final int STAR_BIT = 2;
	private static final int JOINNUM_BIT = 16;
	private static final int FAILNUM_BIT = 16;
	private static final int RESTNUM_BIT = 10;
	private static final int FIRSTBONUS_BIT = 1;
	// private static final int RESTFAILNUM_BIT = 5;

	private int id;// 关卡id
	private int version;// 数据更新的版本时间
	private long star;// 获得星数 2字节
	private long joinNum;// 本日已经通关该小关卡的次数 16字节
	private long failNum;// 本日已经失败的次数 16字节
	private long restNum;// 本日已经重置次数 10字节
	private boolean firstBonus;// 是否已经领取首通奖励 true已经领取 1字节
	private long restFailHour;// 重置失败次数的最近 一次时间 5个字节 0 到 23 小时

	/** 记录掉落的物品次数 */
	private Map<Integer, Integer> items = null;

	public OneRisk(int id, int version, long data, String items) {
		super();
		this.items = new HashMap<Integer, Integer>();
		this.id = id;
		this.version = version;
		paraData(data);
		if (!StringUtils.isEmpty(items)) {
			String its[] = items.split(";");
			for (String it : its) {
				String is[] = it.split("=");
				this.items.put(Integer.valueOf(is[0]), Integer.valueOf(is[1]));

			}
		}
	}

	/**
	 * 判断对象是有效的特殊方法
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (this.id <= 0) {
			return true;
		}
		return false;
	}

	public String getItems() {
		StringBuffer sb = new StringBuffer();
		for (Entry<Integer, Integer> en : this.items.entrySet()) {
			sb.append(en.getKey()).append("=").append(en.getValue())
					.append(";");
		}
		int len = sb.length();
		if (len > 0) {
			sb.deleteCharAt(len - 1);
		}
		return sb.toString();
	}

	/**
	 * 如果某个物品掉落过了就重置下掉落次数
	 * 
	 * @param itemId
	 */
	public void restDropItem(int itemId) {
		if (!this.items.containsKey(itemId)) {
			// 没有，不用清除了
			return;
		}
		this.items.remove(itemId);
		// this.items.put(itemId, 0);
	}

	/**
	 * 获取必然掉落的物品id
	 * 
	 * @param maxNum
	 * @return
	 */
	public List<Integer> getDrops(int maxNum) {
		List<Integer> item = new ArrayList<Integer>();
		for (Entry<Integer, Integer> en : this.items.entrySet()) {
			if (en.getValue() >= maxNum) {
				item.add(en.getKey());
			}
		}
		return item;
	}

	/**
	 * 记录物品的未掉落次数
	 * 
	 * @param itemId
	 */
	public void addItem(int itemId) {
		Integer count = this.items.get(itemId);
		if (count == null) {
			count = 0;
		}
		count += 1;
		this.items.put(itemId, count);
	}

	/**
	 * 判断版本数据是否一样，跨天了重置数据
	 */
	public boolean refresh(boolean canRestFailNum) {
		boolean update = false;
		if (canRestFailNum) {
			Calendar _calendar = Calendar.getInstance();
			int now = _calendar.get(Calendar.HOUR_OF_DAY);
			this.failNum = 0;
			this.restFailHour = now;
			update = true;
		}
		int version = TimeUtils.getVersionOfToday(System.currentTimeMillis());
		if (this.version == version) {
			return update;
		}
		this.joinNum = 0;
		this.restNum = 0;
		this.version = version;
		update = true;
		return update;
	}

	public int getRestFailHour() {
		return (int) restFailHour;
	}

	public void setRestFailHour(long restFailHour) {
		this.restFailHour = restFailHour;
	}

	private int getStartBit() {
		return 0;
	}

	private int getJoinNumBit() {
		return STAR_BIT;
	}

	private int getFailNumBit() {
		return STAR_BIT + JOINNUM_BIT;
	}

	private int getRestNumBit() {
		return STAR_BIT + JOINNUM_BIT + FAILNUM_BIT;
	}

	private int getFirstBonusBit() {
		return STAR_BIT + JOINNUM_BIT + FAILNUM_BIT + RESTNUM_BIT;
	}

	private int getRestFailNumBit() {
		return STAR_BIT + JOINNUM_BIT + FAILNUM_BIT + RESTNUM_BIT
				+ FIRSTBONUS_BIT;
	}

	/**
	 * 解析数据
	 */
	private void paraData(long l) {

		restFailHour = (l >> getRestFailNumBit());

		l = l ^ (restFailHour << getRestFailNumBit());
		long r_firstBonus = (l >> getFirstBonusBit());

		l = l ^ (r_firstBonus << getFirstBonusBit());
		restNum = (l >> getRestNumBit());

		l = l ^ (restNum << getRestNumBit());
		failNum = (l >> getFailNumBit());

		l = l ^ (failNum << (getFailNumBit()));
		joinNum = (l >> getJoinNumBit());

		star = l ^ ((joinNum << (getJoinNumBit())));

		if (r_firstBonus == 1) {
			firstBonus = true;
		} else {
			firstBonus = false;
		}

	}

	/**
	 * 转换成位数据
	 * 
	 * @return
	 */
	public long toData() {
		long r = 0;
		// r = (star) | (joinNum << (2)) | (failNum << (2 + 16))
		// | (restNum << (2 + 16 + 16))
		// | ((firstBonus ? 1l : 0l) << (2 + 16 + 16 + 10))
		// | (restFailHour << (2 + 16 + 16 + 10 + 1));

		// r = (star << getStartBit()) | (joinNum << getJoinNumBit())
		// | (failNum << getFirstBonusBit())
		// | (restNum << getRestNumBit())
		// | ((firstBonus ? 1 : 0) << getFirstBonusBit())
		// | (restFailHour << getRestFailNumBit());

		r = (star << getStartBit()) | (joinNum << getJoinNumBit())
				| (failNum << getFailNumBit()) | (restNum << getRestNumBit())
				| ((firstBonus ? 1l : 0l) << getFirstBonusBit())
				| (restFailHour << getRestFailNumBit());
		return r;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getStar() {
		return (int) star;
	}

	public void setStar(long star) {
		if (star > this.star) {
			this.star = star;
		}
	}

	public int getJoinNum() {
		return (int) joinNum;
	}

	public void setJoinNum(long joinNum) {
		this.joinNum = joinNum;
	}

	public void addJoinNum() {
		this.joinNum += 1;
	}

	public int getFailNum() {
		return (int) failNum;
	}

	public void addFailNum() {
		this.failNum += 1;
	}

	public void setFailNum(long failNum) {
		this.failNum = failNum;
	}

	public int getRestNum() {
		return (int) restNum;
	}

	public void setRestNum(long restNum) {
		this.restNum = restNum;
	}

	public boolean canRest(int maxNum) {
		return maxNum > restNum;
	}

	public void addRestNum() {
		restNum += 1;
		joinNum = 0;
	}

	public boolean isFirstBonus() {
		if (isPass()) {
			return firstBonus;
		}
		return true;
	}

	public void setFirstBonus(boolean firstBonus) {
		this.firstBonus = firstBonus;
	}

	/**
	 * 是否可以扫荡
	 * 
	 * @return
	 */
	public boolean canAutoCombat() {
		return getStar() >= 3;
	}

	/**
	 * true表示本关已经打过
	 * 
	 * @return
	 */
	public boolean isPass() {
		return getStar() > 0;
	}

	@Override
	public String toString() {
		return "OneRisk [id=" + id + ", version=" + version + ", star=" + star
				+ ", joinNum=" + joinNum + ", failNum=" + failNum
				+ ", restNum=" + restNum + ", firstBonus=" + firstBonus
				+ ", restFailHour=" + restFailHour + "]";
	}

	public static void main(String[] args) {
		// long star = 3;// 2
		// long joinNum = 2;// 16
		// long failNum = 13;// 16
		// long restNum = 16;// 10
		// long firstBonus = 1;// 1
		// long restFailNum = 14;// 4

		// OneRisk [id=104, version=1032250, star=0, joinNum=0, failNum=1,
		// restNum=0, firstBonus=false, restFailNum=10]

		long star = 0;// 2
		long joinNum = 0;// 16
		long failNum = 1;// 16
		long restNum = 0;// 10
		long firstBonus = 1;// 1
		long restFailHour = 10;// 4

		long l = (star) | (joinNum << (2)) | (failNum << (2 + 16))
				| (restNum << (2 + 16 + 16))
				| (firstBonus << (2 + 16 + 16 + 10))
				| (restFailHour << (2 + 16 + 16 + 10 + 1));
		// 738855171137539----10100111111111110000100000000001000010000000000011
		// 738855171137539----10100111111111110000100000000001000010000000000011

		System.out.println(l + "----" + Long.toBinaryString(l));
		OneRisk risk = new OneRisk(1, 2, l, "");
		System.out.println(risk);

		// //OneRisk [id=104, version=1032250, star=0, joinNum=0, failNum=1,
		// restNum=0, firstBonus=false, restFailNum=10]

		// OneRisk risk = new OneRisk(1, 2, 0, "");
		// risk.setFailNum(1);
		// risk.setFirstBonus(true);
		// risk.setId(104);
		// risk.setJoinNum(0);
		// risk.setRestFailHour(10);
		// risk.setRestNum(0);
		// risk.setStar(0);
		// risk.setVersion(1032250);
		//
		// long l = risk.toData();
		// // l = 351843721154560l;
		// System.out.println(l + "----" + Long.toBinaryString(l));
		// risk = new OneRisk(104, 1032250, l, "");
		// System.out.println(risk);

		// OneRisk risk = new OneRisk(1, 2, 369435906932736l, "");
		// System.out.println(risk);

		// risk = new OneRisk(101, 1032249, 6, "");
		// System.out.println(risk);
	}
}
