package com.youxigu.wolf.node.core;

import org.apache.mina.core.session.IoSession;

import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.WolfTask;

/**
 * 注销节点 同时注销此节点上的聊天用户
 * @author Administrator
 *
 */
public class NodeUnRegTask extends NodeRegTask{
	@Override
	public WolfTask execute(Response response) {
		IoSession ioSession = response.getSession();
		//String nodeName = ioSession.getRemoteAddress().toString();
		// RegService.getInstance().unRegNode(nodeName);// 注销节点 同时注销此节点上的聊天用户
		NodeSessionMgr.unRegister(ioSession);
		this.setResult(System.currentTimeMillis());		
		return null;
	}
	
	public String toString(){
		return "NodeUnRegTask";
	}
}
