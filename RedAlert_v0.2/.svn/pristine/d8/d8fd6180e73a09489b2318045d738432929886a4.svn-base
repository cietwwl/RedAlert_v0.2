package com.youxigu.wolf.node.core;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.WolfTask;

/**
 * gameserver在节点注册
 * @author Administrator
 *
 */
public class NodeRegTask extends SyncWolfTask{
	@Override
	public WolfTask execute(Response response) {
		IoSession session = response.getSession();
		NodeSessionMgr.regIoSession(session,(Map)this.getParams()[0]);
		this.setResult(session.getRemoteAddress().toString());
		//System.out.println("===="+session.getRemoteAddress().toString());
		return null;
	}
	public String toString(){
		return "NodeRegTask";
	}
}
