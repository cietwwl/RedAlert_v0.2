package com.youxigu.dynasty2.risk.proto;

public class OneRiskInfo {
	private int id;// 关卡id
	private int star;// 获得星数 2字节
	private int joinNum;// 本日已经通关该小关卡的次数 16字节
	private int failNum;// 本日已经失败的次数 16字节
	private int restNum;// 本日已经重置次数 10字节
	private boolean firstBonus;// 是否已经领取首通奖励 true已经领取 1字节

	public OneRiskInfo(int id, int star, int joinNum, int failNum, int restNum,
			boolean firstBonus) {
		super();
		this.id = id;
		this.star = star;
		this.joinNum = joinNum;
		this.failNum = failNum;
		this.restNum = restNum;
		this.firstBonus = firstBonus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(int joinNum) {
		this.joinNum = joinNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getRestNum() {
		return restNum;
	}

	public void setRestNum(int restNum) {
		this.restNum = restNum;
	}

	public boolean isFirstBonus() {
		return firstBonus;
	}

	public void setFirstBonus(boolean firstBonus) {
		this.firstBonus = firstBonus;
	}

	public com.youxigu.dynasty2.risk.proto.RiskMsg.RiskSceneInfo toRiskSceneInfo() {
		com.youxigu.dynasty2.risk.proto.RiskMsg.RiskSceneInfo.Builder res = com.youxigu.dynasty2.risk.proto.RiskMsg.RiskSceneInfo
				.newBuilder();
		res.setId(this.id);
		res.setStar(getStar());
		res.setJoinNum(getJoinNum());
		res.setFailNum(getFailNum());
		res.setRestNum(getRestNum());
		res.setFirstBonus(isFirstBonus());
		return res.build();
	}
}
