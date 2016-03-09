package com.youxigu.dynasty2.combat.domain;

import java.sql.Timestamp;
/**
 * 战斗存数据库
 * @author Dagangzi
 *
 */
public class CombatDB implements java.io.Serializable {
    private static final long serialVersionUID = 5980925742139353987L;
	private String combatId;
	
	private byte[] combatData;
	
	private Timestamp combatDt;

	public String getCombatId() {
		return combatId;
	}

	public void setCombatId(String combatId) {
		this.combatId = combatId;
	}

	public byte[] getCombatData() {
		return combatData;
	}

	public void setCombatData(byte[] combatData) {
		this.combatData = combatData;
	}

	public Timestamp getCombatDt() {
		return combatDt;
	}

	public void setCombatDt(Timestamp combatDt) {
		this.combatDt = combatDt;
	}

	
}
