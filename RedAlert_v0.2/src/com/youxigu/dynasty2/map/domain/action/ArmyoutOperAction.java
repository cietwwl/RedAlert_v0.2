package com.youxigu.dynasty2.map.domain.action;

import com.youxigu.dynasty2.armyout.domain.Armyout;

/**
 * 出征相关的行为
 * 
 * @author LK
 * @date 2016年2月26日
 */
public class ArmyoutOperAction extends TimeAction {
	private Armyout armyout;

	public ArmyoutOperAction(Armyout armyout, long time, int cmd) {
		this.armyout = armyout;
		super.time = time;
		super.cmd = cmd;
	}

	public Armyout getArmyout() {
		return armyout;
	}

	public void setArmyout(Armyout armyout) {
		this.armyout = armyout;
	}

}
