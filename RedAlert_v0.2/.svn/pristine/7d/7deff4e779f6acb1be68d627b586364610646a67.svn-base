package com.youxigu.dynasty2.risk.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节信息
 * 
 * @author fengfeng
 *
 */
public class Risk {
	/** 章节id */
	private int id;
	/** 章节名字 */
	private String name;
	/** 章节的前置章节 */
	private int prevSceneId;
	/** 大的章节下面包含的小的章节 */
	private transient List<RiskParentScene> riskScene = new ArrayList<RiskParentScene>();

	public void addRiskParentScene(RiskParentScene scene) {
		if (scene == null) {
			return;
		}
		this.riskScene.add(scene);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrevSceneId() {
		return prevSceneId;
	}

	public void setPrevSceneId(int prevSceneId) {
		this.prevSceneId = prevSceneId;
	}

	public List<RiskParentScene> getRiskParentScene() {
		return riskScene;
	}

}
