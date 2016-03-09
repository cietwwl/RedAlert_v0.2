package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import com.youxigu.dynasty2.openapi.Constant;

/**
 * 玩家帐户信息
 * 
 * @author Administrator
 * 
 */
public class Account implements Serializable {
	public static final int STATUS_BAN = 1;// 封号
	public static final int STATUS_UNAVAILABLE = 2;// 未激活

	public static final int LOGIN_QQ = 1;
	public static final int LOGIN_WEIXIN = 2;
	public static final int LOGIN_VISTOR = 3;

	public static final int QQ_VIP = 0x1;  //QQ会员
	public static final int QQ_VIP_YEAR = 0x2;
	public static final int QQ_SUPER = 0x2000;
	
	public static final int QQ_YELLOW = 0x4;
	public static final int QQ_YELLOW_YEAR = 0x8;
	public static final int QQ_YELLOW_HIGH = 0x10;
	public static final int QQ_BLUE = 0x20;
	public static final int QQ_BLUE_YEAR = 0x40;
	public static final int QQ_PINK = 0x80;
	public static final int QQ_PINK_YEAR = 0x100;
	public static final int QQ_RED = 0x200;
	public static final int QQ_GREEN = 0x400;
	public static final int QQ_3366 = 0x800;
	public static final int QQ_PLUS = 0x1000;

	public static final int WX_VIP = 0x4000;  //微信会员，策划配置的礼包要与qq分开，这里需要独立的标志位

	public static final int QQ_RED_YEAR = 0x8000;
	public static final int QQ_RED_HIGH = 0x10000;
	public static final int QQ_BLUE_HIGH = 0x40000; // 豪华蓝钻	


	private long accId;// 帐号ID
	private String accName;// 帐号名称
	private String accDesc;// 平台账号昵称
	private int status; // 0-正常，1-锁定，2-未激活。
	private String statusDec;// 锁定原因描述
	private String loginIp; // ip
	private Timestamp createDttm;// 创建时间
	private Timestamp lastLoginDttm; // 最后一次登录时间

	/**
	 * 最后一次登出时间，防沉迷使用
	 */
	private Timestamp lastLogoutDttm;
	/**
	 * 累计在线时间，防沉迷使用，上次下线与本次登录的时间差大于5小时，则清零
	 */
	private int onlineTimeSum;
	/**
	 * 累计不在线时间，超过5个小时，则同时清理掉在线时间，不在线时间
	 */
	private int offlineTimeSum;

	private Timestamp envelopDttm; // 封号时间
	private long inviteAccId;// 邀请人AccId
	private String nodeIp; // 上一次分配的nodeIp+port
	/**
	 * QQ类型<br>
	 * 1=VIP<br>
	 * 2=年费VIP<br>
	 * 4=普通黄钻<br>
	 * 8=年费黄钻 <br>
	 * 16=超级黄钻<br>
	 * 32=普通蓝钻<br>
	 * 64=年费蓝钻 <br>
	 * 128=粉钻<br>
	 * 256=年费粉钻<br>
	 * 512=红钻 <br>
	 * 1024=绿钻 <br>
	 * 2048=3366<br>
	 * 4096=Q+ <br>
	 * 8192=SUPER QQ <br>
	 */
	private int qqFlag;

	/**
	 * 当前的QQ特权标示，从不同平台进入有不同的标志
	 */
	private int qqCurrFlag;
	/**
	 * qqVIP等级
	 */
	private int qqVipLv;
	/**
	 * qq黄钻等级
	 */
	private int qqYellowLv;

	/**
	 * qq蓝钻等级
	 */
	private int qqBlueLv;//

	/**
	 * qq红钻等级
	 */
	private int qqRedLv;//

	/**
	 * qq绿钻等级
	 */
	private int qqGreenLv;//

	/**
	 * qq粉钻等级
	 */
	private int qqPinkLv;//

	/**
	 * super qq等级
	 */
	private int qqSuperLv;//
	/**
	 * q+用户等级
	 */
	private int qqPlusLv;//

	/**
	 * qq等级
	 */
	private int qqLv;

	/**
	 * 3366等级
	 */
	private int qq3366Lv;

	private String pf;// 平台
	private int pType;//平台类型,1.andriod 2.ios	
	private int dType;//设备类型,1.andriod 2.ios
	private String dInfo;//设备信息

	private String via;// 渠道

	private String areaId;// 大区Id,为合服预留字段

	// private byte monthlyLoginTime;//本月登录次数
	// private int contLoginTime;//连续登录次数
	// private User user;

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public long getAccId() {
		return accId;
	}

	public void setAccId(long accId) {
		this.accId = accId;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccDesc() {
		return accDesc;
	}

	public void setAccDesc(String accDesc) {
		this.accDesc = accDesc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDec() {
		return statusDec;
	}

	public void setStatusDec(String statusDec) {
		this.statusDec = statusDec;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Timestamp getCreateDttm() {
		return createDttm;
	}

	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}

	public Timestamp getLastLoginDttm() {
		return lastLoginDttm;
	}

	public void setLastLoginDttm(Timestamp lastLoginDttm) {
		this.lastLoginDttm = lastLoginDttm;
	}

	public Timestamp getEnvelopDttm() {
		return envelopDttm;
	}

	public void setEnvelopDttm(Timestamp envelopDttm) {
		this.envelopDttm = envelopDttm;
	}

	public long getInviteAccId() {
		return inviteAccId;
	}

	public void setInviteAccId(long inviteAccId) {
		this.inviteAccId = inviteAccId;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public int getQqYellowLv() {
		return qqYellowLv;
	}

	public void setQqYellowLv(int qqYellowLv) {
		this.qqYellowLv = qqYellowLv;
	}

	public int getQqBlueLv() {
		return qqBlueLv;
	}

	public void setQqBlueLv(int qqBlueLv) {
		this.qqBlueLv = qqBlueLv;
	}

	public int getQqPlusLv() {
		return qqPlusLv;
	}

	public void setQqPlusLv(int qqPlusLv) {
		this.qqPlusLv = qqPlusLv;
	}

	public int getQqLv() {
		return qqLv;
	}

	public void setQqLv(int qqLv) {
		this.qqLv = qqLv;
	}

	public int getQqVipLv() {
		return qqVipLv;
	}

	public void setQqVipLv(int qqVipLv) {
		this.qqVipLv = qqVipLv;
	}

	public int getQq3366Lv() {
		return qq3366Lv;
	}

	public void setQq3366Lv(int qq3366Lv) {
		this.qq3366Lv = qq3366Lv;
	}

	public int getQqFlag() {
		return qqFlag;
	}

	public void setQqFlag(int qqFlag) {
		this.qqFlag = qqFlag;
	}

	public int getQqRedLv() {
		return qqRedLv;
	}

	public void setQqRedLv(int qqRedLv) {
		this.qqRedLv = qqRedLv;
	}

	public int getQqGreenLv() {
		return qqGreenLv;
	}

	public void setQqGreenLv(int qqGreenLv) {
		this.qqGreenLv = qqGreenLv;
	}

	public int getQqPinkLv() {
		return qqPinkLv;
	}

	public void setQqPinkLv(int qqPinkLv) {
		this.qqPinkLv = qqPinkLv;
	}

	public int getQqSuperLv() {
		return qqSuperLv;
	}

	public void setQqSuperLv(int qqSuperLv) {
		this.qqSuperLv = qqSuperLv;
	}

	public int getQqCurrFlag() {
		return qqCurrFlag;
	}

	public void setQqCurrFlag(int qqCurrFlag) {
		this.qqCurrFlag = qqCurrFlag;
	}

	public int getLvByFlag(int flag) {
		if (flag == 0) {
			return 0;
		} else if (flag == QQ_VIP || flag == QQ_VIP_YEAR) {
			return this.qqVipLv;
		} else if (flag == QQ_SUPER) {
			return this.qqSuperLv;
		} else {
			return 0;
		}
	}

	public Timestamp getLastLogoutDttm() {
		return lastLogoutDttm;
	}

	public void setLastLogoutDttm(Timestamp lastLogoutDttm) {
		this.lastLogoutDttm = lastLogoutDttm;
	}

	public int getOnlineTimeSum() {
		return onlineTimeSum;
	}

	public void setOnlineTimeSum(int onlineTimeSum) {
		this.onlineTimeSum = onlineTimeSum;
	}

	public int getOfflineTimeSum() {
		return offlineTimeSum;
	}

	public void setOfflineTimeSum(int offlineTimeSum) {
		this.offlineTimeSum = offlineTimeSum;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public void putPlatformProperties(Map<String, Object> params) {

		params.put("qf", this.qqFlag);
//		if (this.qq3366Lv > 0) {
//			params.put("lv3366", this.qq3366Lv);
//		}
//		if (this.qqBlueLv > 0) {
//			params.put("lvBlue", this.qqBlueLv);
//		}
//
//		if (this.qqYellowLv > 0) {
//			params.put("lvYellow", this.qqYellowLv);
//		}
		if (this.qqVipLv > 0) {
			params.put("lvVip", this.qqVipLv);
		}
	}

	public String getAreaId() {
		if (this.areaId == null) {
			return Constant.AREA_ID;
		}
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;

	}

	public int getdType() {
		return dType;
	}

	public void setdType(int dType) {
		this.dType = dType;
	}

	public String getdInfo() {
		return dInfo;
	}

	public void setdInfo(String dInfo) {
		this.dInfo = dInfo;
	}

	public int getpType() {
		return pType;
	}

	public void setpType(int pType) {
		this.pType = pType;
	}

}
