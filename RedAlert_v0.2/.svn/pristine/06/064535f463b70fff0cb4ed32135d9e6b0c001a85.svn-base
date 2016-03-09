package com.youxigu.dynasty2.risk.proto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.risk.domain.OneRisk;

public class RiskParentSceneInfo implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1L;
	// 单个章节信息
	private int pid;
	private List<OneRisk> infos;
	private int sumStar;
	private boolean pass;
	private int starBonus;

	public RiskParentSceneInfo(int pid) {
		super();
		this.pid = pid;
		infos = new ArrayList<OneRisk>();
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public List<OneRisk> getInfos() {
		return infos;
	}

	public void addInfos(OneRisk one) {
		this.infos.add(one);
	}

	public int getSumStar() {
		return sumStar;
	}

	public void setSumStar(int sumStar) {
		this.sumStar = sumStar;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public int getStarBonus() {
		return starBonus;
	}

	public void setStarBonus(int starBonus) {
		this.starBonus = starBonus;
	}

	@Override
	public Message build() {
		// TODO Auto-generated method stub
		return null;
	}

}
