package com.youxigu.wolf.net;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 客户端指定时间内不发送请求则认为timeout,关闭连接
 * 
 * 暂时不用
 * @author Administrator
 * 
 */
public class ReadIdleTimeoutFilter extends IoFilterAdapter {

	private int readIdleTimeout = 0;



	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception {
		if (status == IdleStatus.READER_IDLE && readIdleTimeout != 0) {
			if (session.containsAttribute(NodeSessionMgr.SERVER_TYPE_KEY)) {// 不是服务器
				long lastReadTime = session.getLastReadTime();
				if (System.currentTimeMillis() - lastReadTime > readIdleTimeout) {
					Map<String, Object> params = new HashMap<String, Object>(3);
					params.put("cmd", 1);
					params.put("errCode", -9012);
					params
							.put("err","连接空闲时间过长，服务器关闭连接");
					session.write(params);
					session.close(false);
				}
			}
		}
	}
}
