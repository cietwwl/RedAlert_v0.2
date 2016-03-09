package com.youxigu.dynasty2.risk.domain;

import java.util.HashMap;
import java.util.Map;

import com.youxigu.dynasty2.risk.enums.RiskSceneType;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.EffectValue;
import com.youxigu.dynasty2.util.StringUtils;

/**
 * 冒险小关卡
 * 
 * @author Administrator
 * 
 */
public class RiskScene implements java.io.Serializable {
	private static final long serialVersionUID = -5702067173311865692L;
	private int sceneId;
	/*** 大关卡Id */
	private int parentId;
	private String name;
	private String pic;
	/** * 前置场景Id */
	private int prevSceneId;

	/** @see RiskSceneType 普通关, 装备关，BOSS关，坦克碎片关， */
	private int sceneType;

	/*** 消耗的体力:user.hpPoint */
	private int hpPoint;

	/*** 每天最大参加次数 */
	private int maxJoinNum;

	/** 君主经验 */
	private int userExp;

	/*** NpcId, NcpDefine */
	private int npcId;
	/*** NpcId, NcpDefine 配置君主可以带着上阵的npc */
	private int npcId2;// 助战军团id,君主坦克的位置用sysheroId匹配君主坦克
	private int dropPackId;

	/*** 运营掉落包 */
	private int operateDropPackId;

	/*** 地形 */
	private int terrian;

	/*** 第一次进入场景时候的我方NPCId, =0，则使用自己的冒险军团 否则为NpcDefine表的Id */
	private int sceneNpc1;
	/*** 第一次进入场景时候的敌方NPCId, =0，则默认使用 npcId1 否则为NpcDefine表的Id */
	private int sceneNpc2;

	/*** 第一次进入场景时候的起始剧情Id */
	private int storyId1;
	/*** 第一次进入场景战斗后的剧情Id */
	private int storyId2;

	/** Npc递增系数 HERO_AGILE,26,3 属性key,absValue,perValue **/
	private String addition;

	/** 首次通关奖励掉落包 **/
	private int firstBonus;

	/** 最多失败次数 */
	private int failNum;

	/** 和vip等级相关的重置次数 */
	private String restVipNum;

	/*** 序号,系统生成的 */
	private transient int seqId;

	/*** 前置小场景 */
	private transient RiskScene next;

	/*** 后置小场景 */
	private transient RiskScene prev;

	private transient RiskParentScene parent;

	private transient RiskSceneType riskSceneType = null;

	/** 和vip等级关联的重置次数 */
	private transient Map<Integer, Integer> restVipNumMap = null;
	/** npc的属性加成 */
	private transient Map<String, com.youxigu.dynasty2.util.EffectValue> attrValues = null;

	public RiskScene() {
		super();
		attrValues = new HashMap<String, EffectValue>();
	}

	public void init() {
		riskSceneType = RiskSceneType.valueOf(sceneType);
		if (riskSceneType == null) {
			throw new BaseException("关卡配置类型错误" + sceneType);
		}

		if (StringUtils.isEmpty(restVipNum)) {
			throw new BaseException("vip等级关联重置次数不能为null");
		}
		restVipNumMap = new HashMap<Integer, Integer>();
		String vps[] = restVipNum.split(";");
		for (String vp : vps) {
			String v[] = vp.split("=");
			int l = Integer.valueOf(v[0]);
			int n = Integer.valueOf(v[1]);
			if (restVipNumMap.containsKey(l)) {
				throw new BaseException("配置vip数据重复" + sceneId);
			}
			restVipNumMap.put(l, n);
		}
		if (!StringUtils.isEmpty(addition) && !"0".equals(addition)) {
			String ats[] = addition.split(",");
			if (ats.length != 3) {
				throw new BaseException(
						"[属性key,absValue,perValue]关卡配置的加成属性格式错误" + sceneId
								+ "," + addition);
			}
			EffectValue val = new EffectValue(Integer.valueOf(ats[1]),
					Integer.valueOf(ats[2]));
			attrValues.put(ats[0], val);
		}

	}

	public Map<String, com.youxigu.dynasty2.util.EffectValue> getAttrValues() {
		return attrValues;
	}

	public int getRestVipNum(int vipLevel) {
		Integer v = restVipNumMap.get(vipLevel);
		if (v == null) {
			throw new BaseException("没有配置vip等级对应的重置数据" + vipLevel);
		}
		return v;
	}

	public int getNpcId2() {
		return npcId2;
	}

	public void setNpcId2(int npcId2) {
		this.npcId2 = npcId2;
	}

	public int getFirstBonus() {
		return firstBonus;
	}

	public void setFirstBonus(int firstBonus) {
		this.firstBonus = firstBonus;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public int getHpPoint() {
		return hpPoint;
	}

	public void setHpPoint(int hpPoint) {
		this.hpPoint = hpPoint;
	}

	public int getMaxJoinNum() {
		return maxJoinNum;
	}

	public void setMaxJoinNum(int maxJoinNum) {
		this.maxJoinNum = maxJoinNum;
	}

	public int getDropPackId() {
		return dropPackId;
	}

	public void setDropPackId(int dropPackId) {
		this.dropPackId = dropPackId;
	}

	public int getTerrian() {
		return terrian;
	}

	public void setTerrian(int terrian) {
		this.terrian = terrian;
	}

	public RiskScene getNext() {
		return next;
	}

	public void setNext(RiskScene next) {
		this.next = next;
	}

	public RiskScene getPrev() {
		return prev;
	}

	public void setPrev(RiskScene prev) {
		this.prev = prev;
	}

	public int getUserExp() {
		return userExp;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getSceneNpc1() {
		return sceneNpc1;
	}

	public void setSceneNpc1(int sceneNpc1) {
		this.sceneNpc1 = sceneNpc1;
	}

	public int getSceneNpc2() {
		return sceneNpc2;
	}

	public void setSceneNpc2(int sceneNpc2) {
		this.sceneNpc2 = sceneNpc2;
	}

	public int getStoryId1() {
		return storyId1;
	}

	public void setStoryId1(int storyId1) {
		this.storyId1 = storyId1;
	}

	public int getStoryId2() {
		return storyId2;
	}

	public void setStoryId2(int storyId2) {
		this.storyId2 = storyId2;
	}

	public int getPrevSceneId() {
		return prevSceneId;
	}

	public void setPrevSceneId(int prevSceneId) {
		this.prevSceneId = prevSceneId;
	}

	public RiskParentScene getParent() {
		return parent;
	}

	public void setParent(RiskParentScene parent) {
		this.parent = parent;
	}

	/**
	 * 是否有剧情场景
	 * 
	 * @return
	 */
	public boolean hasStory() {
		return (sceneNpc1 != 0 || sceneNpc2 != 0 || storyId1 != 0 || storyId2 != 0);
	}

	public int getSceneType() {
		return sceneType;
	}

	public void setSceneType(int sceneType) {
		this.sceneType = sceneType;
	}

	public int getOperateDropPackId() {
		return operateDropPackId;
	}

	public void setOperateDropPackId(int operateDropPackId) {
		this.operateDropPackId = operateDropPackId;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public String getRestVipNum() {
		return restVipNum;
	}

	public void setRestVipNum(String restVipNum) {
		this.restVipNum = restVipNum;
	}

	public void setUserExp(int userExp) {
		this.userExp = userExp;
	}

}
