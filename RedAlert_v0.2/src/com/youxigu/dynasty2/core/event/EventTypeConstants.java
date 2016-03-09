package com.youxigu.dynasty2.core.event;

/**
 * 事件类型常量
 * 
 * @author Administrator
 * 
 */
public interface EventTypeConstants {

	// 默认事件，用EVT_DEFAULT注册的listener将会受到所有消息
	public final static int EVT_DEFAULT = 0;
	// 用户升级事件
	public final static int EVT_USER_LEVEL_UP = 1000;
	//这个事件主要是通知修改君主坦克的信息
	public final static int EVT_USER_INFO_CHANGE = 1001;
	// 君主军功增加
	public final static int EVT_USER_JUNGONG_ADD = 1002;
}
