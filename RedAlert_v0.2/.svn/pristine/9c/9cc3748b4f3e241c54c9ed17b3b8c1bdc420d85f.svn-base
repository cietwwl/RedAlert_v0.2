package com.youxigu.dynasty2.map.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.map.service.IMapService;

/**
 * 郡定义
 * 
 * @author Administrator
 * 
 */
public class State implements java.io.Serializable {
	public static final int STATUS_CLOSE = 0; // 未开放
	public static final int STATUS_OPEN = 1; // 已开放
	public static final int STATUS_FULL = 2; // 已满

	private int stateId;// 郡id
	private int countryId;// 国家id
	private String stateName;// 郡名
	private int status;// 状态-见上
	private int level;// 等级

	private int nextStateId;// 开启的下一个区块id
	private int mapCellId1;// 开启下一个区块需要占领的资源点
	private int mapCellId2;// 开启下一个区块需要占领的资源点
	private int mapCellId3;// 开启下一个区块需要占领的资源点
	private int mapCellId4;// 开启下一个区块需要占领的资源点
	private int mapCellId5;// 开启下一个区块需要占领的资源点

	private Country country;// 所属国家
	private State nextState;// 下一个开放的区块, null表示没有可用开放的区块了
	private State parentState;// 上一个关卡 null表示是新手区

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
		if (nextState != null) {
			nextState.setParentState(this);
		}
	}

	private void setParentState(State parentState) {
		this.parentState = parentState;
	}

	public String getCountryName() {
		IMapService mapService = (IMapService) ServiceLocator
				.getSpringBean("mapService");
		Country cty = mapService.getCountryById(countryId);
		if (cty != null)
			return cty.getCountryName();
		else
			return "错误的国家";
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getNextStateId() {
		return nextStateId;
	}

	public void setNextStateId(int nextStateId) {
		this.nextStateId = nextStateId;
	}

	public int getMapCellId1() {
		return mapCellId1;
	}

	public void setMapCellId1(int mapCellId1) {
		this.mapCellId1 = mapCellId1;
	}

	public int getMapCellId2() {
		return mapCellId2;
	}

	public void setMapCellId2(int mapCellId2) {
		this.mapCellId2 = mapCellId2;
	}

	public int getMapCellId3() {
		return mapCellId3;
	}

	public void setMapCellId3(int mapCellId3) {
		this.mapCellId3 = mapCellId3;
	}

	public int getMapCellId4() {
		return mapCellId4;
	}

	public void setMapCellId4(int mapCellId4) {
		this.mapCellId4 = mapCellId4;
	}

	public int getMapCellId5() {
		return mapCellId5;
	}

	public void setMapCellId5(int mapCellId5) {
		this.mapCellId5 = mapCellId5;
	}

	/**
	 * 获取开放下一个区块所需的所有资源点信息
	 * 
	 * @return
	 */
	public Set<Integer> getAllMapCells() {
		Set<Integer> ids = new HashSet<Integer>();
		if (mapCellId1 > 0) {
			ids.add(mapCellId1);
		}
		if (mapCellId2 > 0) {
			ids.add(mapCellId2);
		}
		if (mapCellId3 > 0) {
			ids.add(mapCellId3);
		}
		if (mapCellId4 > 0) {
			ids.add(mapCellId4);
		}
		if (mapCellId5 > 0) {
			ids.add(mapCellId5);
		}
		return ids;
	}

	public State getParentState() {
		return parentState;
	}

	// /**
	// * 点是否在郡内
	// * @param posX
	// * @param posY
	// * @return
	// */
	// public boolean isInState(int posX,int posY){
	// return (posX>=minPosX && posY>=minPosY && posX<=maxPosX &&
	// posY<=maxPosY);
	// }

}
