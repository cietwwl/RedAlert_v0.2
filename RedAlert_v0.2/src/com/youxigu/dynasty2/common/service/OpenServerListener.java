package com.youxigu.dynasty2.common.service;

import com.youxigu.dynasty2.common.domain.ServerInfo;

/**
 * 文件名    OpenServerListener.java
 *
 * 描  述    开服时间修改监听器,有受开服时间影响的功能，可以实现这个接口
 *
 * 时  间    2014-4-9
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.2
 */
public interface OpenServerListener {
	
	public void notice(ServerInfo serverInfo);

}
