package com.youxigu.dynasty2.risk.proto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ISendMessage;
import com.youxigu.dynasty2.entity.domain.DroppedEntity;

public class RiskSceneInfo implements ISendMessage, Serializable {
	private static final long serialVersionUID = 1L;
	private boolean win;
	private OneRiskInfo risk;
	private String combatId;
	private int userExp;
	private List<DroppedEntity> items = new ArrayList<DroppedEntity>();

	public RiskSceneInfo(String combatId) {
		super();
		this.combatId = combatId;
	}

	public boolean isWin() {
		return win;
	}

	public List<DroppedEntity> getItems() {
		return items;
	}

	public void setRisk(OneRiskInfo risk) {
		this.risk = risk;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public com.youxigu.dynasty2.risk.proto.RiskMsg.RiskSceneInfo toRiskSceneInfo() {
		return risk.toRiskSceneInfo();
	}

	public String getCombatId() {
		return combatId;
	}

	public void setCombatId(String combatId) {
		this.combatId = combatId;
	}

	public int getUserExp() {
		return userExp;
	}

	public void setUserExp(int userExp) {
		this.userExp = userExp;
	}

	public void addItems(List<DroppedEntity> items) {
		this.items.addAll(items);
	}

	@Override
	public Message build() {
		// TODO Auto-generated method stub
		return null;
	}

}
