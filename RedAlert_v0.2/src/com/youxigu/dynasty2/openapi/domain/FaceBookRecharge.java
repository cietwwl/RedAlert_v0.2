package com.youxigu.dynasty2.openapi.domain;

/**
 * 文件名 FaceBookRecharge.java
 * 
 * 描 述 海外充值数据
 * 
 * 时 间 2015-1-19
 * 
 * 作 者 huhaiquan
 * 
 * 版 本 v1.7
 */
public class FaceBookRecharge {
	private String pOrderId;// 合作商的订单id（EFUN订单号,确保唯一） 必填，20位
	private String userId;// EFUN账号ID 必填，20位
	private String creditId;// 储值id，调用储值时SDK传送的值，根据这个id可以发送游戏币到对应的角色) 必填，50位
	private String currency;// 币种 必填，USD
	private String amount;// 币种对应的总金额 必填，美金，两位小数
	private String RCurrency;// 原币种 选填，原币种
	private String RAmount;// 原币种对应金额 选填，金额
	private String gameCode;// 游戏代码 选填
	private String serverCode;// 服务器代码，正式服+1递增，测试服999 必填
	private int stone;// 游戏币数量 必填
	private String stoneType;// 游戏币类型 gold / diamond 金币/钻石
	// 游戏内存在可购买的多虚拟币种情况下使用。只有一种， 可传空 选填
	private String md5Str;// MD5(pOrderId+serverCode+creditId+userId +amount
							// +stone +stoneType+time+key) 必填，大写
	private String time;// 请求发游戏币时间 必填，时间戳
	private String remark;// 自定义数据串 详见注意事项6 选填
	private int activityExtra;// 储值活动额外赠送的游戏币，若无活动则默认传0 详见注意事项3
									// 必填,不参与首冲多倍或者其他储值活动计算
	private String orderStateMonth;// 对账月。 格式：201401 详见注意事项4 选填
	private int point;// 平台点数。 1点等于1台币 详见注意事项9 必填
	private int freePoint;// 是否免费点。1：付费点，0：免费点 必填

	public FaceBookRecharge() {
	}

	public String getPOrderId() {
		return pOrderId;
	}

	public void setPOrderId(String orderId) {
		pOrderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getRCurrency() {
		return RCurrency;
	}

	public void setRCurrency(String currency) {
		RCurrency = currency;
	}


	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}

	public String getStoneType() {
		return stoneType;
	}

	public void setStoneType(String stoneType) {
		this.stoneType = stoneType;
	}

	public String getMd5Str() {
		return md5Str;
	}

	public void setMd5Str(String md5Str) {
		this.md5Str = md5Str;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getFreePoint() {
		return freePoint;
	}

	public void setFreePoint(int freePoint) {
		this.freePoint = freePoint;
	}

	public String getOrderStateMonth() {
		return orderStateMonth;
	}

	public void setOrderStateMonth(String orderStateMonth) {
		this.orderStateMonth = orderStateMonth;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRAmount() {
		return RAmount;
	}

	public void setRAmount(String amount) {
		RAmount = amount;
	}

	public int getActivityExtra() {
		return activityExtra;
	}

	public void setActivityExtra(int activityExtra) {
		this.activityExtra = activityExtra;
	}

	
}
