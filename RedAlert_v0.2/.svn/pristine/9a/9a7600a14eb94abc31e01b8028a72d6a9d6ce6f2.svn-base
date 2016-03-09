package com.youxigu.wolf.net;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class WolfHandler extends IoHandlerAdapter implements IWolfService {

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof WolfTask) {
			WolfTask task = (WolfTask) message;
			WolfTask next = task.execute(new Response(session));
			if (next != null)
				session.write(next);
		}
	}

	public Object handleMessage(Response response, Object message) {
		if (message instanceof WolfTask) {
			try {
				this.messageReceived(response.getSession(), message);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return Boolean.TRUE;
		}

		return null;
	}

	@Override
	public void stop(boolean force) {
		// TODO Auto-generated method stub
		
	}

}
