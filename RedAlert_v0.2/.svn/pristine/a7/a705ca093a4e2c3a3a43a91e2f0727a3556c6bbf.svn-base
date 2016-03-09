package com.youxigu.dynasty2.map.domain.action;

import java.util.Map;

import com.youxigu.dynasty2.map.domain.MapCell;
import com.youxigu.dynasty2.map.domain.State;
import com.youxigu.dynasty2.map.domain.StateCache;
import com.youxigu.dynasty2.map.service.ICommander;

/**
 * 加载地图action
 * 
 * @author LK
 * @date 2016年2月5日
 */
public class LoadMapAction extends TimeAction {
	private State state;
	private Map<Integer, MapCell> ALL_CELLS;
	private Map<Integer, StateCache> STATE_CELLS;

	public LoadMapAction(State state,
			Map<Integer, MapCell> ALL_CELLS,
			Map<Integer, StateCache> STATE_CELLS) {
		this.state = state;
		super.time = System.currentTimeMillis();
		super.cmd = ICommander.COMMAND_1;
		this.ALL_CELLS = ALL_CELLS;
		this.STATE_CELLS = STATE_CELLS;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Map<Integer, MapCell> getALL_CELLS() {
		return ALL_CELLS;
	}

	public void setALL_CELLS(Map<Integer, MapCell> aLL_CELLS) {
		ALL_CELLS = aLL_CELLS;
	}

	public Map<Integer, StateCache> getSTATE_CELLS() {
		return STATE_CELLS;
	}

	public void setSTATE_CELLS(Map<Integer, StateCache> sTATE_CELLS) {
		STATE_CELLS = sTATE_CELLS;
	}
}
