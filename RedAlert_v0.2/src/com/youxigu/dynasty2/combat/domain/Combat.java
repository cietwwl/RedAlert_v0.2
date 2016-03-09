package com.youxigu.dynasty2.combat.domain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.combat.domain.action.AbstractCombatAction;
import com.youxigu.dynasty2.combat.domain.action.FireSkillAction;
import com.youxigu.dynasty2.combat.proto.CombatMsg;

/**
 * 战斗描述
 * @author Dagangzi
 *
 */
public class Combat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8963327168408366157L;

	protected static final Logger log = LoggerFactory.getLogger(Combat.class);

	/**
	 * 导致战斗的数据参数，在战斗结束后会有用：通常是armyOut或者hashMap来保存一些数据
	 */
	private transient Object params;

	private String combatId;

	private short combatType;
	private short scoreType;//评分方式

	/**
	 * 进攻团队
	 */
	private CombatTeam attacker;

	/**
	 * 防守团队
	 */
	private CombatTeam defender;

	/**
	 * 开始前的所有操作独立出来
	 */
	private List<AbstractCombatAction> prevActions;

	/**
	 * 战场行为集合
	 * 集合层级关系   战场行为集合-n个回合行为集合-回合变更-单个回合行为集合-技能行为集合-效果行为集合-效果行为
	 * AbstractCombatAction 代表一个行为
	 */
	private List<List<AbstractCombatAction>> actions;

	/**
	 * 当前的回合数:所有单元执行完攻击+移动后为一个轮次
	 */
	private int round;

	/**
	 * 胜利类型
	 */
	private byte winType;

	/**
	 * 战斗后城池资源变化
	 */
	private CombatRob combatRob;

	/**
	 * 战斗系数
	 */
	private transient Map<String, CombatFactor> combatFactors;
	
	private transient int attackRight;//默认出手权 1攻击方  2防守方

	/**
	 * 最后一个技能行为
	 */
	private transient FireSkillAction lastSkillAction;

	public Combat() {
	}

	public Combat(short combatType, short scoreType, CombatTeam attacker,
			CombatTeam defender) {
		this(combatType, scoreType, attacker, defender, null);
	}

	/**
	 * 构造战斗对象
	 * @param combatType 战斗类型
	 * @param scoreType 评分方式  1回合数  2总血量  3死亡单位数 
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @param params 参数集
	 */
	public Combat(short combatType, short scoreType, CombatTeam attacker, CombatTeam defender, Object params) {
		this();
		setAttacker(attacker);
		setDefender(defender);
		this.combatType = combatType;
		this.scoreType = scoreType;
		this.params = params;

		// 生成唯一ID
		this.combatId = "C_" + UUID.randomUUID().toString();
		
		//出手权 1=A方  2=B方 -1=永久A方  -2=永久B方
		// 默认按照战力先手PVP
		this.attackRight = attacker.getTeamPower() - defender.getTeamPower() > 0 ? 1 : 2;

		// PVE EVP 时玩家先手
		if (attacker.getTeamType() == CombatTeam.TEAM_TYPE_NPCHERO) {
			attackRight = 2;
		} else if (defender != null
				&& defender.getTeamType() == CombatTeam.TEAM_TYPE_NPCHERO) {
			attackRight = 1;
		}

		init();
	}

	public void init() {
		actions = new LinkedList<List<AbstractCombatAction>>();

		if (this.prevActions == null) {
			this.prevActions = new LinkedList<AbstractCombatAction>();
		}

		//军团中未上阵的武将不出战
		List<CombatUnit> units = null;
		if (this.attacker != null) {
			units = attacker.getUnits();
			Iterator<CombatUnit> lit = units.iterator();
			while (lit.hasNext()) {
				CombatUnit unit = lit.next();
				if (unit == null) {
					lit.remove();
					continue;
				}
				unit.init();
			}
		}
		if (this.defender != null) {
			units = this.defender.getUnits();
			Iterator<CombatUnit> lit = units.iterator();
			while (lit.hasNext()) {
				CombatUnit unit = lit.next();
				if (unit == null) {
					lit.remove();
					continue;
				}
				unit.init();
			}
		}
	}

	public CombatTeam getAttacker() {
		return attacker;
	}

	public void setAttacker(CombatTeam attacker) {
		this.attacker = attacker;
		this.attacker.setParent(this);
	}

	public CombatTeam getDefender() {
		return defender;
	}

	public void setDefender(CombatTeam defender) {
		this.defender = defender;
		this.defender.setParent(this);
	}

	public String getCombatId() {
		return combatId;
	}

	public void setCombatId(String combatId) {
		this.combatId = combatId;
	}

	public List<List<AbstractCombatAction>> getActions() {
		return actions;
	}

	/**
	 * 在最后一个效果集中追加一个行为
	 * @param action
	 * @return
	 */
	public AbstractCombatAction appendTailAction(AbstractCombatAction action) {
		if (actions == null) {
			actions = new LinkedList<List<AbstractCombatAction>>();
		}
		int count = actions.size();

		if (count > 0) {
			actions.get(count - 1).add(action);
		} else {
			List<AbstractCombatAction> subs = new LinkedList<AbstractCombatAction>();
			subs.add(action);
			actions.add(subs);
		}
		return action;
	}

	/**
	 * 增加新的效果集
	 * @param actions
	 */
	public void addSubActions(List<AbstractCombatAction> actions) {
		this.actions.add(actions);
	}

	/**
	 * 取得战斗中最后一个行为集合
	 * @return
	 */
	public List<AbstractCombatAction> getLastSubActions() {
		if (actions == null) {
			actions = new LinkedList<List<AbstractCombatAction>>();
		}
		int count = actions.size();
		if (count > 0) {
			return actions.get(count - 1);
		} else {
			List<AbstractCombatAction> subs = new LinkedList<AbstractCombatAction>();
			actions.add(subs);
			return subs;
		}
	}

	public void setActions(List<List<AbstractCombatAction>> actions) {
		this.actions = actions;
	}

	public int getRound() {
		return round;
	}

	public void setRound(byte round) {
		this.round = round;
	}

	/**
	 * 进入下个回合
	 * @return
	 */
	public int increaseRound() {
		this.round++;
		return this.round;
	}

	public short getCombatType() {
		return combatType;
	}

	public void setCombatType(short combatType) {
		this.combatType = combatType;
	}

	public short getScoreType() {
		return scoreType;
	}

	public void setScoreType(short scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * 战斗是否结束
	 * @return
	 */
	public boolean isEnd() {
		//攻守双一方死亡或是达到最大回合数
		boolean end = (attacker.isFailure() || defender.isFailure());
		if (!end) {
			end = this.getRound() >= CombatConstants.MAX_ROUND;
		}
		return end;
	}

	public byte getWinType() {
		return winType;
	}

	public void setWinType(byte winType) {
		this.winType = winType;
	}

	/**
	 * 用战斗单元编号取得战斗单元
	 * @param unitId
	 * @return
	 */
	public CombatUnit getCombatUnitById(int unitId) {
		for (CombatUnit unit : attacker.getUnits()) {
			if (unit.getId() == unitId) {
				return unit;
			}
		}
		for (CombatUnit unit : defender.getUnits()) {
			if (unit.getId() == unitId) {
				return unit;
			}
		}
		return null;
	}

	public CombatRob getCombatRob() {
		return combatRob;
	}

	public void setCombatRob(CombatRob combatRob) {
		this.combatRob = combatRob;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	/**
	 * 取得战斗开始前的行为集合
	 * @return
	 */
	public List<AbstractCombatAction> getPrevActions() {
		return prevActions;
	}

	public void setPrevActions(List<AbstractCombatAction> prevActions) {
		this.prevActions = prevActions;
	}

	/**
	 * 增加一个战前行为
	 * @param prevAction
	 */
	public void addPrevActions(AbstractCombatAction prevAction) {
		if (this.prevActions == null) {
			this.prevActions = new LinkedList<AbstractCombatAction>();
		}
		this.prevActions.add(prevAction);
	}

	public Map<String, CombatFactor> getCombatFactors() {
		return combatFactors;
	}

	public void setCombatFactors(Map<String, CombatFactor> combatFactors) {
		this.combatFactors = combatFactors;
	}

	/**
	 * 按死亡人数计算分数
	 * @param combatTeam
	 * @return
	 */
	private short getScoreByDeadUnit(CombatTeam combatTeam) {
		//死亡单位数
		List<CombatUnit> units = combatTeam.getUnits();
		int count = 0;
		short score = 0;
		for (CombatUnit unit : units) {
			if (unit == null || unit.getId() < 1 || unit.getId() > 12) {
				continue;
			}

			if (unit.dead()) {
				count = count + 1;
			}
		}

		if (count < 1) {
			score = 3;
		} else if (count <= 1) {
			score = 2;
		} else if (count > 1) {
			score = 1;
		}
		return score;
	}

	/**
	 * 按照剩余血量比例
	 * @param combatTeam
	 * @return
	 */
	private short getScoreByAllHp(CombatTeam combatTeam) {
		//死亡单位数
		List<CombatUnit> units = combatTeam.getUnits();
		int hp = 0;
		int initHp = 0;
		short score = 0;
		for (CombatUnit unit : units) {
			if (unit == null || unit.dead() || unit.getId() < 1 || unit.getId() > 12) {
				continue;
			}
			hp = unit.getTotalHp();
			initHp = unit.getInitHp();
		}

		double rate = 1.0d * hp / initHp;
		if (rate >= 0.8) {
			score = 3;
		} else if (rate >= 0.5) {
			score = 2;
		} else if (rate < 0.5) {
			score = 1;
		}
		return score;
	}

	/**
	 * 战斗完毕后评分
	 * @param mode 评分方式 0战斗回合数
	 */
	public void atferCombat() {
		if (defender != null) {
			defender.clean();
		}
		if (attacker != null) {
			attacker.clean();
		}

		if (defender.getUnits() == null || defender.getUnits().size() == 0) {
			attacker.setScore((short) 3);
			return;
		} else if (attacker.getUnits() == null || attacker.getUnits().size() == 0) {
			defender.setScore((short) 3);
			return;
		}

		CombatTeam win = null;
		if (this.winType == CombatConstants.WIN_ATK) {
			win = attacker;
		} else {
			if (this.round >= CombatConstants.MAX_ROUND) {
				win = defender;
				return;
			}
			win = defender;
		}

		short score = 0;
		//战斗回合数
		switch (this.scoreType) {
		case CombatConstants.SCORETYPE_UNITLIVE:
			//死亡单位数
			score = this.getScoreByDeadUnit(win);
			break;
		case CombatConstants.SCORETYPE_HP:
			//总HP占比
			score = this.getScoreByAllHp(win);
			break;
		default:
			//回合数
			if (this.round <= 3) {
				score = 3;
			} else if (score <= 5) {
				score = 2;
			} else if (score > 5) {
				score = 1;
			}
			break;
		}
		win.setScore(score);

	}

	public CombatMsg.Combat toCombat() {
		CombatMsg.Combat.Builder combatBr = CombatMsg.Combat.newBuilder();
		combatBr.setCombatId(this.combatId);
		combatBr.setCombatType(this.combatType);
		combatBr.setScoreType(this.scoreType);
		
		if(this.getAttacker() != null) {
			combatBr.setAttacker(this.getAttacker().toCombatTeam());	
		}
		
		if(this.getDefender() != null) {
			combatBr.setDefender(this.getDefender().toCombatTeam());
		}

		List<AbstractCombatAction> prevActions = this.prevActions;
		if (prevActions != null && prevActions.size() > 0) {
			for (AbstractCombatAction tmp : prevActions) {
				CombatMsg.AbstractCombatAction.Builder abstractCombatAction = CombatMsg.AbstractCombatAction
						.newBuilder();
				abstractCombatAction.setActionId(tmp.getAction());
				abstractCombatAction.setActionBytes(tmp.toProBytes());
				combatBr.addPrevActions(abstractCombatAction.build());
			}
		}

		List<List<AbstractCombatAction>> actions = this.actions;
		if (actions != null && actions.size() > 0) {
			for (List<AbstractCombatAction> list : actions) {
				CombatMsg.Combat.RoundAction.Builder roundAction = CombatMsg.Combat.RoundAction.newBuilder();
				if (list != null && list.size() > 0) {
					for (AbstractCombatAction tmp : list) {
						CombatMsg.AbstractCombatAction.Builder abstractCombatAction = CombatMsg.AbstractCombatAction
								.newBuilder();
						abstractCombatAction.setActionId(tmp.getAction());
						abstractCombatAction.setActionBytes(tmp.toProBytes());
						roundAction.addRoundaction(abstractCombatAction.build());
					}
				}
				combatBr.addActions(roundAction.build());
			}
		}

		combatBr.setRound(this.round);
		combatBr.setWinType(this.winType);
		
		if(this.combatRob != null) {
			combatBr.setCombatRob(this.combatRob.toCombatRob());
		}

		// 战斗结算面板信息
		CombatMsg.CombatStats combatStats = this.toCombatStats(this);
		if (combatStats != null) {
			combatBr.setCombatStats(combatStats);
		}
		return combatBr.build();
	}

	/**
	 * 战斗结算面板
	 * 
	 * @param combat
	 * @return
	 */
	private CombatMsg.CombatStats toCombatStats(Combat combat) {
		// 结算统计面板信息
		CombatMsg.CombatStats.Builder combatStatsBr = CombatMsg.CombatStats
				.newBuilder();
		if (this.defender != null) {
			combatStatsBr.setDefUserId(this.defender.getUserId());
			combatStatsBr.setDefName(this.defender.getTeamName());
			combatStatsBr.setDefScore((int) this.defender.getScore());
			List<CombatUnit> defUnits = this.defender.getUnits();
			if (defUnits != null && defUnits.size() > 0) {
				for (CombatUnit combatUnit : defUnits) {
					CombatMsg.CombatStatsUnit.Builder csu = CombatMsg.CombatStatsUnit
							.newBuilder();
					csu.setName(combatUnit.getName());
					csu.setInitHp(combatUnit.getInitHp());
					csu.setLostHp(combatUnit.getTotalLostHp());
					csu.setLastHp(combatUnit.getTotalHp());
					csu.setHarmHp(combatUnit.getTotalHarm());
					combatStatsBr.addDefUnit(csu.build());
				}
			}
		}

		if (this.attacker != null) {
			combatStatsBr.setAtkUserId(this.attacker.getUserId());
			combatStatsBr.setAtkName(this.attacker.getTeamName());
			combatStatsBr.setAtkScore((int) this.attacker.getScore());
			List<CombatUnit> atkUnits = this.attacker.getUnits();
			if (atkUnits != null && atkUnits.size() > 0) {
				for (CombatUnit combatUnit : atkUnits) {
					CombatMsg.CombatStatsUnit.Builder csu = CombatMsg.CombatStatsUnit
							.newBuilder();
					csu.setName(combatUnit.getName());
					csu.setInitHp(combatUnit.getInitHp());
					csu.setLostHp(combatUnit.getTotalLostHp());
					csu.setLastHp(combatUnit.getTotalHp());
					csu.setHarmHp(combatUnit.getTotalHarm());
					combatStatsBr.addAtkUnit(csu.build());
				}
			}
		}
		combatStatsBr.setPvp(combatStatsBr.getAtkUserId() > 0
				&& combatStatsBr.getDefUserId() > 0);
		combatStatsBr.setCombatType(this.combatType);
		combatStatsBr.setScoreType(this.scoreType);
		combatStatsBr.setWinType(this.winType);
		return combatStatsBr.build();
	}

	public int getAttackRight() {
		return attackRight;
	}

	public void setAttackRight(int attackRight) {
		this.attackRight = attackRight;
	}
	
	public FireSkillAction getLastSkillAction() {
		return lastSkillAction;
	}

	/**
	 * 设置最后一个技能行为
	 * 
	 * @param lastSkillAction
	 */
	public void setLastSkillAction(FireSkillAction lastSkillAction) {
		this.lastSkillAction = lastSkillAction;
	}

	/**
	 * 重置出手状态
	 */
	public void resetCurrRoundAttacked() {
		for (CombatUnit unit : attacker.getUnits()) {
			unit.setCurrRoundAttacked(false);
		}
		for (CombatUnit unit : defender.getUnits()) {
			unit.setCurrRoundAttacked(false);
		}
	}

	/**
	 * 展现日志
	 * 
	 * @return
	 */
	public String showLog() {
		StringBuilder sb = new StringBuilder(1024);
		sb.append("\n**************输出战斗消息的参数内容****************").append("\n");
		sb.append("\n========战斗开始前事件=======").append("\n");
		if (this.prevActions.size() > 0) {
			for (AbstractCombatAction action : this.prevActions) {
				action.desc(sb);
			}
		}

		sb.append("\n========战斗事件=======").append("\n");
		if (this.actions != null && this.actions.size() > 0) {
			for (List<AbstractCombatAction> actionList : this.actions) {
				if (actionList != null && actionList.size() > 0) {
					for (AbstractCombatAction action : actionList) {
						action.desc(sb);
					}
				}
			}
		}
		return sb.toString();
	}
}
