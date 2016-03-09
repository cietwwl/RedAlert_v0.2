package com.youxigu.dynasty2.activity.domain;

import java.sql.Timestamp;

/**
 * 
 * 系统中的各种活动定义 通常是指GM定义的运营活动，该活动在一定时间段内影响某些游戏任务的效果
 * 
 * 
 * @author Administrator
 * 
 */
public class Activity implements java.io.Serializable {

	private static final long serialVersionUID = -8888264398753807203L;

	public static final String TYPE_EF_TOWER_FREE_NUM = "EF_TOWER_FREE_NUM";// 千重楼次数
	public static final String TYPE_EF_TOWER_HERO_EXP = "EF_TOWER_HERO_EXP";// 千重楼武将经验

	public static final String TYPE_EF_LEAGUE_FREE_NUM = "EF_LEAGUE_FREE_NUM";// 百战次数
	public static final String TYPE_EF_LEAGUE_RONGYU_NUM = "EF_LEAGUE_RONGYU_NUM";// 百战荣誉

	public static final String TYPE_EF_HERO_TRAINER_EXP = "EF_HERO_TRAINER_EXP";// 武将修炼经验
	public static final String TYPE_EF_HERO_TRAINER_MONEY = "EF_HERO_TRAINER_MONEY";// 武将修炼经验铜币
	public static final String TYPE_EF_HERO_TRAINER_CASH = "EF_HERO_TRAINER_CASH";// 武将修炼经验元宝

	public static final String TYPE_EF_RISKHP_POINT_NUM = "EF_RISKHP_POINT_NUM";// 体力力次数
	public static final String TYPE_EF_ACTION_POINT_NUM = "EF_ACTION_POINT_NUM";//行动力次数
	public static final String TYPE_EF_WATER_JUGONG_NUM = "EF_WATER_JUGONG_NUM";// 浇水荣誉数
	public static final String TYPE_EF_USERTASK_EXP = "EF_USERTASK_EXP";// 君主任务经验
	public static final String TYPE_EF_MONEYPRODUCTION = "EF_MONEYPRODUCTION";// 铜币产量
	public static final String TYPE_EF_FOODPRODUCTION = "EF_FOODPRODUCTION";// 粮食产量
	public static final String TYPE_EF_BRZONEPRODUCTION = "EF_BRZONEPRODUCTION";// 矿物产量	

	public static final String TYPE_EF_RISK_HERO_EXP = "EF_RISK_HERO_EXP";// 冒险经验
	public static final String TYPE_EF_STORY_HERO_EXP = "EF_STORY_HERO_EXP";// 剧情经验

	public static final String TYPE_EF_ARMYOUT_TIME = "EF_ARMYOUT_TIME";// 出征速度

	public static final String TYPE_EF_FARM_STANDYEILD = "EF_FARM_STANDYEILD";// 农场产量加成
	
	public static final String TYPE_EF_RECHARGE_AWARD_CASH = "EF_RECHARGE_AWARD_CASH";// 充值元宝奖励倍数
	public static final String TYPE_EF_SEAWORLD_FREE_NUM = "EF_SEAWORLD_FREE_NUM";// 海底世界免费次数
	public static final String TYPE_EF_SEAWORLD_ITEM_NUM = "EF_SEAWORLD_ITEM_NUM";// 海底世界道具次数
	// public static final String TYPE_EF_VISIT_EVENT =
	// "EF_VISIT_EVENT";//好友访问事件

	// public static final String TYPE_EF_QUESTION_MONEY =
	// "EF_QUESTION_MONEY";//答题铜币
	//	
	// public static final String TYPE_EF_HERO_EVENT_EXP =
	// "EF_HERO_EVENT_EXP";//武将事件
	//	
	// public static final String TYPE_EF_BUY_MONEY = "EF_BUY_MONEY";//充值返利

	public static final byte STATUS_OPEN = 1;// 开启
	public static final byte STATUS_CLOSE = 0;// 关闭
	public static final byte RELATION_AND = 1;// 并且
	public static final byte RELATION_OR = 0;// 或者

	private int actId;
	private String name;
	private String description;
	private String url;
	private String icon;

	// 活动开启条件
	private Timestamp startDttm;
	private Timestamp endDttm;
	private long timeStart;
	private long timeEnd;
	private int weekStart;
	private int weekEnd;

	private String channel; // 进入的平台名称

	// 黄 蓝 会员 3366 的等级
	private String qqYellowLv;
	private String qqYellowLvYear;// 年费黄
	private String qqYellowLvHigh;// 豪华
	private String qqBlueLv;
	private String qqBlueLvYear;// 年费蓝
	private String qqBlueLvHigh;// 豪华
	private String qqPlusLv;
	private String qqLv;
	private String qqVipLv;
	private String qqVipLvYear;// 年费会员
	private String qq3366Lv;
	private String qqRedLv;
	private String qqGreenLv;
	private String qqPinkLv;
	private String qqPinkLvYear;// 年费粉
	private String qqSuperLv;

	private String usrLv;
	private String casteLv;

	// 效果
	private String efId;

	private byte status = STATUS_CLOSE;// 默认活动为关闭

	private byte relation = RELATION_AND;// 活动开启里的条件关系

	private transient boolean checkPrivilege = false;

	public Activity() {
	}

	public byte getRelation() {
		return relation;
	}

	public void setRelation(byte relation) {
		this.relation = relation;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getStartDttm() {
		return startDttm;
	}

	public void setStartDttm(Timestamp startDttm) {
		this.startDttm = startDttm;
	}

	public Timestamp getEndDttm() {
		return endDttm;
	}

	public void setEndDttm(Timestamp endDttm) {
		this.endDttm = endDttm;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}

	public long getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(long timeEnd) {
		this.timeEnd = timeEnd;
	}

	public int getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(int weekStart) {
		this.weekStart = weekStart;
	}

	public int getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(int weekEnd) {
		this.weekEnd = weekEnd;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getEfId() {
		return efId;
	}

	public void setEfId(String efId) {
		this.efId = efId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getQqYellowLv() {
		return qqYellowLv;
	}

	public void setQqYellowLv(String qqYellowLv) {
		this.qqYellowLv = qqYellowLv;
	}

	public String getQqBlueLv() {
		return qqBlueLv;
	}

	public void setQqBlueLv(String qqBlueLv) {
		this.qqBlueLv = qqBlueLv;
	}

	public String getQqPlusLv() {
		return qqPlusLv;
	}

	public void setQqPlusLv(String qqPlusLv) {
		this.qqPlusLv = qqPlusLv;
	}

	public String getQqLv() {
		return qqLv;
	}

	public void setQqLv(String qqLv) {
		this.qqLv = qqLv;
	}

	public String getQqVipLv() {
		return qqVipLv;
	}

	public void setQqVipLv(String qqVipLv) {
		this.qqVipLv = qqVipLv;
	}

	public String getQq3366Lv() {
		return qq3366Lv;
	}

	public void setQq3366Lv(String qq3366Lv) {
		this.qq3366Lv = qq3366Lv;
	}

	public String getQqRedLv() {
		return qqRedLv;
	}

	public void setQqRedLv(String qqRedLv) {
		this.qqRedLv = qqRedLv;
	}

	public String getQqGreenLv() {
		return qqGreenLv;
	}

	public void setQqGreenLv(String qqGreenLv) {
		this.qqGreenLv = qqGreenLv;
	}

	public String getQqPinkLv() {
		return qqPinkLv;
	}

	public void setQqPinkLv(String qqPinkLv) {
		this.qqPinkLv = qqPinkLv;
	}

	public String getQqSuperLv() {
		return qqSuperLv;
	}

	public void setQqSuperLv(String qqSuperLv) {
		this.qqSuperLv = qqSuperLv;
	}

	public String getUsrLv() {
		return usrLv;
	}

	public void setUsrLv(String usrLv) {
		this.usrLv = usrLv;
	}

	public String getCasteLv() {
		return casteLv;
	}

	public String getQqYellowLvYear() {
		return qqYellowLvYear;
	}

	public void setQqYellowLvYear(String qqYellowLvYear) {
		this.qqYellowLvYear = qqYellowLvYear;
	}

	public String getQqYellowLvHigh() {
		return qqYellowLvHigh;
	}

	public void setQqYellowLvHigh(String qqYellowLvHigh) {
		this.qqYellowLvHigh = qqYellowLvHigh;
	}

	public String getQqBlueLvYear() {
		return qqBlueLvYear;
	}

	public void setQqBlueLvYear(String qqBlueLvYear) {
		this.qqBlueLvYear = qqBlueLvYear;
	}

	public String getQqVipLvYear() {
		return qqVipLvYear;
	}

	public void setQqVipLvYear(String qqVipLvYear) {
		this.qqVipLvYear = qqVipLvYear;
	}

	public String getQqPinkLvYear() {
		return qqPinkLvYear;
	}

	public void setQqPinkLvYear(String qqPinkLvYear) {
		this.qqPinkLvYear = qqPinkLvYear;
	}

	public String getQqBlueLvHigh() {
		return qqBlueLvHigh;
	}

	public void setQqBlueLvHigh(String qqBlueLvHigh) {
		this.qqBlueLvHigh = qqBlueLvHigh;
	}

	public Activity(int actId, String name, String description, String url,
			String icon, Timestamp startDttm, Timestamp endDttm,
			long timeStart, long timeEnd, int weekStart, int weekEnd,
			String channel, String qqYellowLv, String qqYellowLvYear,
			String qqYellowLvHigh, String qqBlueLv, String qqBlueLvYear,
			String qqBlueLvHigh, String qqPlusLv, String qqLv, String qqVipLv,
			String qqVipLvYear, String qq3366Lv, String qqRedLv,
			String qqGreenLv, String qqPinkLv, String qqPinkLvYear,
			String qqSuperLv, String usrLv, String casteLv, String efId,
			byte status, byte relation) {
		super();
		this.actId = actId;
		this.name = name;
		this.description = description;
		this.url = url;
		this.icon = icon;
		this.startDttm = startDttm;
		this.endDttm = endDttm;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.weekStart = weekStart;
		this.weekEnd = weekEnd;
		this.channel = channel;
		this.qqYellowLv = qqYellowLv;
		this.qqYellowLvYear = qqYellowLvYear;
		this.qqYellowLvHigh = qqYellowLvHigh;
		this.qqBlueLv = qqBlueLv;
		this.qqBlueLvYear = qqBlueLvYear;
		this.qqBlueLvHigh = qqBlueLvHigh;
		this.qqPlusLv = qqPlusLv;
		this.qqLv = qqLv;
		this.qqVipLv = qqVipLv;
		this.qqVipLvYear = qqVipLvYear;
		this.qq3366Lv = qq3366Lv;
		this.qqRedLv = qqRedLv;
		this.qqGreenLv = qqGreenLv;
		this.qqPinkLv = qqPinkLv;
		this.qqPinkLvYear = qqPinkLvYear;
		this.qqSuperLv = qqSuperLv;
		this.usrLv = usrLv;
		this.casteLv = casteLv;
		this.efId = efId;
		this.status = status;
		this.relation = relation;
	}

	public void setCasteLv(String casteLv) {
		this.casteLv = casteLv;
	}

	public void init() {
		//这里是为了防止每次都判断
		if (qqYellowLv != null) {
			if (qqYellowLv.length() == 0 || "1,100".equals(qqYellowLv) || "-1,-1".equals(qqYellowLv)) {
				qqYellowLv = null;
			}
		} else {
			checkPrivilege = true;
		}

		if (qqYellowLvYear != null) {
			if (qqYellowLvYear.length() == 0 || "1,100".equals(qqYellowLvYear) || "-1,-1".equals(qqYellowLvYear)) {
				qqYellowLvYear = null;
			}
		} else {
			checkPrivilege = true;
		}

		if (qqYellowLvHigh != null) {
			if (qqYellowLvHigh.length() == 0 || "1,100".equals(qqYellowLvHigh) || "-1,-1".equals(qqYellowLv)) {
				qqYellowLvHigh = null;
			}
		} else {
			checkPrivilege = true;
		}

		if (qqBlueLv != null) {
			if (qqBlueLv.length() == 0 || "1,100".equals(qqBlueLv) || "-1,-1".equals(qqBlueLv)) {
				qqBlueLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqBlueLvYear != null) {
			if (qqBlueLvYear.length() == 0 || "1,100".equals(qqBlueLvYear) || "-1,-1".equals(qqBlueLvYear)) {
				qqBlueLvYear = null;
			}
		} else {
			checkPrivilege = true;
		}

		if (qqBlueLvHigh != null) {
			if (qqBlueLvHigh.length() == 0 || "1,100".equals(qqBlueLvHigh) || "-1,-1".equals(qqBlueLvHigh)) {
				qqBlueLvHigh = null;
			}
		} else {
			checkPrivilege = true;
		}

		if (qqPlusLv != null) {
			if (qqPlusLv.length() == 0 || "1,100".equals(qqPlusLv) || "-1,-1".equals(qqPlusLv)) {
				qqPlusLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqLv != null) {
			if (qqLv.length() == 0 || "1,100".equals(qqLv) || "-1,-1".equals(qqLv)) {
				qqLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqVipLv != null) {
			if (qqVipLv.length() == 0 || "1,100".equals(qqVipLv) || "-1,-1".equals(qqVipLv)) {
				qqVipLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqVipLvYear != null) {
			if (qqVipLvYear.length() == 0 || "1,100".equals(qqVipLvYear) || "-1,-1".equals(qqVipLvYear)) {
				qqVipLvYear = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qq3366Lv != null) {
			if (qq3366Lv.length() == 0 || "1,100".equals(qq3366Lv) || "-1,-1".equals(qq3366Lv)) {
				qq3366Lv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqRedLv != null) {
			if (qqRedLv.length() == 0 || "1,100".equals(qqRedLv) || "-1,-1".equals(qqRedLv)) {
				qqRedLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqGreenLv != null) {
			if (qqGreenLv.length() == 0 || "1,100".equals(qqGreenLv) || "-1,-1".equals(qqGreenLv)) {
				qqGreenLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqPinkLv != null) {
			if (qqPinkLv.length() == 0 || "1,100".equals(qqPinkLv) || "-1,-1".equals(qqPinkLv)) {
				qqPinkLv = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqPinkLvYear != null) {
			if (qqPinkLvYear.length() == 0 || "1,100".equals(qqPinkLvYear) || "-1,-1".equals(qqPinkLvYear)) {
				qqPinkLvYear = null;
			}
		} else {
			checkPrivilege = true;
		}
		if (qqSuperLv != null) {
			if (qqSuperLv.length() == 0 || "1,100".equals(qqSuperLv) || "-1,-1".equals(qqSuperLv)) {
				qqSuperLv = null;
			}
		} else {
			checkPrivilege = true;
		}
	}

	public boolean isCheckPrivilege() {
		return checkPrivilege;
	}

	public void setCheckPrivilege(boolean checkPrivilege) {
		this.checkPrivilege = checkPrivilege;
	}
	
	
}