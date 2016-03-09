package com.youxigu.wolf.net;

public class NodeSessionListener implements ISessionListener {

	@Override
	public void close(Response response) {
		// 关闭gameserver session
		if (response.isNodeServerSession()) {
			NodeSessionMgr.unRegister(response.getSession());
		}

	}

	@Override
	public void open(Response response) {
		// TODO Auto-generated method stub

	}

}
