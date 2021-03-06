package com.youxigu.dynasty2.tower.domain;

import com.youxigu.dynasty2.npc.domain.NPCDefine;
import com.youxigu.dynasty2.util.EffectValue;

import java.io.Serializable;
import java.util.Map;

/**
 * 作战试验中心（重楼）定义
 */
public class Tower implements Serializable {
    private static final long serialVersionUID = -198245221111154800L;
    private int stageId;//PK 关id
    private String name;//名称
    private int npcId;  //NPCID
    private String additionEffect;  //NPC额外加成
    private int firstBonusId;  //首通奖励掉落包ID
    private int bonusId;//获胜的掉落包
    private String description;//关卡描述
    private transient NPCDefine npc;
    private transient Map<String, EffectValue> attrValues = null;

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public String getAdditionEffect() {
        return additionEffect;
    }

    public void setAdditionEffect(String additionEffect) {
        this.additionEffect = additionEffect;
    }

    public int getFirstBonusId() {
        return firstBonusId;
    }

    public void setFirstBonusId(int firstBonusId) {
        this.firstBonusId = firstBonusId;
    }

    public int getBonusId() {
        return bonusId;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NPCDefine getNpc() {
        return npc;
    }

    public void setNpc(NPCDefine npc) {
        this.npc = npc;
    }

    public Map<String, EffectValue> getAttrValues() {
        return attrValues;
    }

    public void setAttrValues(Map<String, EffectValue> attrValues) {
        this.attrValues = attrValues;
    }
}
