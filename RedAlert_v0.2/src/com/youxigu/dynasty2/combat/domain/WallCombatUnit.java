package com.youxigu.dynasty2.combat.domain;

import com.youxigu.dynasty2.entity.domain.SysHero;

/**
 * 城墙战斗单位
 * @author Dagangzi
 *
 */
public class WallCombatUnit extends CombatUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2251145879898950504L;
	private int defence;

	public WallCombatUnit() {
		super();
	}

	public WallCombatUnit(int hp, int def) {
		this.unitType = CombatConstants.COMBATUNIT_TYPE_WALL;
		this.initHp = hp;
		this.curHp = hp;
		this.defence = def;
		this.name = "城墙";
		this.attackType = SysHero.ATTACKTYPE_PHYSICAL;
	}
	
	@Override
	public int _getAttackMorale() {
		return 0;
	}

	@Override
	public int _getDefendMorale() {
		return 0;
	}

	@Override
	public String _getArmyName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int _getMagicAttack() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getPhysicalAttack() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getMagicDefend() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getPhysicalDefend() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getInitHit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getInitDodge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getCritDec() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getCritAdd() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getCritDamageAdd() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getCritDamageDec() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getDamageAdd() {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public int _getDamageDec() {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public int _getCurrentPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getArmyBiteRoit() {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public int _getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getShield() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int _getMorale() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean _isLeader() {
		return false;
	}
}
