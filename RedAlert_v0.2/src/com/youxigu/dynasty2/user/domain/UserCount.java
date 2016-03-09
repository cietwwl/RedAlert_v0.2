package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * 记录次数和时间
 * @author Dagangzi
 *
 */
public class UserCount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5733249443205548766L;
	//类型
	public static final String TYPE_TRIBUTE = "TRIBUTE";//朝贡
	public static final String TYPE_SENT_USERGIFT = "SENT_USERGIFT";//赠送免费礼物
	public static final String TYPE_GET_USERGIFT = "GET_USERGIFT";//索要免费礼物
	public static final String TYPE_REQUEST_USERGIFT = "REQUEST_USERGIFT";//请求免费礼物
	
	public static final String TYPE_GUILD_AWARD = "GUILD_AWARD";//联盟福利领取	
	public static final String TYPE_USER_LOTTERY = "USER_LOTTERY";//升级抽奖	
	public static final String TYPE_SHORCUTS = "SHORTCUTS";//标志用户是否安装快捷图标	
	public static final String TYPE_FRI_RECOMMEND = "RECOMMEND";//本日是否进行了好友推荐
	public static final String TYPE_FIGHT_SHRINESBOSS = "FIGHT_SHRINESBOSS";//请神boss	
	
	public static final String TYPE_QQ_BLUE_OPEN = "QQBLUE_OPEN";//	
	public static final String TYPE_MAIL_DAILYNUM = "MAIL_NUM";//
	//public static final String TYPE_FIGHT_SHRINESBOSS = "FIGHT_SHRINESBOSS";//请神boss	
	public static final String TYPE_NOWAR_ITEM_CD = "NOWAR_ITEM_CD";//免战道具使用冷却
	//public static final String TYPE_USER_GUIDELINES= "USER_GUIDELINES";// 新手指引执行步骤
	
	public static final String TYPE_USER_LV_GIFT = "USER_LV_GIFT";//君主等级礼包
	//重楼限时活动
	public static final String TYPE_TOWER_LIMIT_TIME_REWARD = "TOWER_TIME_REWARD";
	//	限时特够
	public static final String TYPE_LIMIT_BUY = "TYPE_LIMIT_BUY";
	
	public static final String TYPE_RISK_DAILY_MAX = "RISK_DAILY_MAX";//冒险一天的最大次数	
	public static final String TYPE_OFFICAL_LABA = "OFFICAL_LABA";//官职（天子）上线喇叭的广播间隔	
	public static final String TYPE_OFFICIAL_DAILY_AWARD = "OFFICIAL_DAILY_AWARD";//官职每日奖励
	
	private long userId;
	private String type;//类型
	private int num;//已经进行的次数
	private Timestamp lastDttm;
	
	public UserCount() {}
	
	public UserCount(long userId, String type, int num, Timestamp lastDttm) {
		this.userId = userId;
		this.type = type;
		this.num = num;
		this.lastDttm = lastDttm;
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Timestamp getLastDttm() {
		return lastDttm;
	}
	public void setLastDttm(Timestamp lastDttm) {
		this.lastDttm = lastDttm;
	}
	

	/**
	 * 得到所有状态
	 * 
	 * @return
	 */
	public int[] getNumStatus() {
		return new int[] {
				this.num >> 19 & 0x1,
				this.num >> 18 & 0x1,
				this.num >> 17 & 0x1,
				this.num >> 16 & 0x1,
				this.num >> 15 & 0x1,
				this.num >> 14 & 0x1,
				this.num >> 13 & 0x1,
				this.num >> 12 & 0x1,
				this.num >> 11 & 0x1,
				this.num >> 10 & 0x1,
				this.num >> 9 & 0x1,
				this.num >> 8 & 0x1,
				this.num >> 7 & 0x1,
				this.num >> 6 & 0x1,
				this.num >> 5 & 0x1, 
				this.num >> 4 & 0x1,
				this.num >> 3 & 0x1, 
				this.num >> 2 & 0x1,
				this.num >> 1 & 0x1, 
				this.num & 0x1 };
	}

	/**
	 * 设置状态
	 * 
	 * @param awardFlags
	 */
	public void setNumStatus(int[] awardFlags) {
		this.num = awardFlags[0] << 19
				| awardFlags[1] << 18
				| awardFlags[2] << 17
				| awardFlags[3] << 16
				| awardFlags[4] << 15
				| awardFlags[5] << 14
				| awardFlags[6] << 13
				| awardFlags[7] << 12
				| awardFlags[8] << 11
				| awardFlags[9] << 10
				| awardFlags[10] << 9
				| awardFlags[11] << 8
				| awardFlags[12] << 7
				| awardFlags[13] << 6 
				| awardFlags[14] << 5
				| awardFlags[15] << 4 
				| awardFlags[16] << 3 
				| awardFlags[17] << 2
				| awardFlags[18] << 1 
				| awardFlags[19];
	}
}
