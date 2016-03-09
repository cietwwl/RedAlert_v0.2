package com.youxigu.wolf.node.core;

import com.youxigu.wolf.net.IWolfService;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.ResultMgr;
import com.youxigu.wolf.net.SyncWolfTask;

/**
 * 
 * 
 * 
 * 
 */
public class MainService implements IWolfService {

	/**
	 * 处理客户端请求
	 */
	@Override
	public Object handleMessage(Response response, Object message) {
		if (message instanceof NodeRegTask) {
			NodeRegTask task  =(NodeRegTask)message;
			
			if (task.getState() == SyncWolfTask.RESPONSE) {
				ResultMgr.requestCompleted(response.getSession(), task.getRequestId(), task.getResult());
				return Boolean.TRUE;
			}
			try{
				task.setState(SyncWolfTask.RESPONSE);
				task.execute(response);
	
			}
			catch (Exception e){
				e.printStackTrace();
				task.setResult(e);
			}

			finally{
				//回写
				task.setParams(null);
				response.write(message);

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
