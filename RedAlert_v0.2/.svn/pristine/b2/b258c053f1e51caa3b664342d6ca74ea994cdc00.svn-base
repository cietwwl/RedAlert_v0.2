package com.youxigu.dynasty2.map.service;

import java.util.Map;

import com.youxigu.dynasty2.map.domain.action.TimeAction;

public interface ICommandDistatcher {
	/**
	 * 投递消息
	 * 
	 * @param thread
	 * @param action
	 */
	void putCommander(TimeAction action);
	void putCommander(int thread, TimeAction action);

	/**
	 * 校验消息
	 * 
	 * @param cmd
	 * @param params
	 */
	void doCheak(int cmd, Map<String, Object> params);
}
