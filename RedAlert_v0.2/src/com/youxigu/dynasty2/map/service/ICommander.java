package com.youxigu.dynasty2.map.service;

import java.io.Serializable;
import java.util.Map;

import com.youxigu.dynasty2.map.domain.action.TimeAction;

/**
 * 地图任务执行器接口
 * 
 * @author LK
 * @date 2016年2月18日
 */
public interface ICommander extends Serializable{
	public static final int COMMAND_0 = 0;// default
	public static final int COMMAND_1 = 1;// load地图坐标
    public static final int COMMAND_2 = 2;// 刷新动态资源地和野怪NPC

	/**
	 * 校验
	 * 
	 * @param params
	 */
	void doCheck(Map<String, Object> params);

	/**
	 * 执行行为，带事务
	 * 
	 * @param action
	 * @return
	 */
	Map<String, Object> doExcute(TimeAction action);

    /**
	 * 执行行为，不带事务
	 *
	 * @param action
	 * @return
	 */
	Map<String, Object> excute(TimeAction action);

    /**
     * 本Command执行的时候是否需要事务。
     * 需要则实现doExecute方法，并在本方法中返回true;不需要则实现execute方法，并在本方法中返回false
     * @return
     */
    boolean needTransaction();
}
