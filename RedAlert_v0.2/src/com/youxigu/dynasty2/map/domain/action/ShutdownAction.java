package com.youxigu.dynasty2.map.domain.action;

/**
 * 用来终止线程
 * 
 * @author Administrator
 * 
 */
public class ShutdownAction extends TimeAction {
	public ShutdownAction() {
		this.time = System.currentTimeMillis();
	}
}
