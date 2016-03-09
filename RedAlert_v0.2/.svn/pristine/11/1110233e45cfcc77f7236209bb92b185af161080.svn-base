package com.youxigu.dynasty2.core.wolf;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.youxigu.wolf.net.WolfClient;
import com.youxigu.wolf.node.job.Job;

public interface IWolfClientService {

	public void init();

	public void destory();

	/**
	 * 异步向nodeserver发送消息
	 * 
	 * @param task
	 * @return
	 */
	public Object asynSendTask(Object task);

	/**
	 * 同步向nodeServer发送消息，相当于远程方法调用
	 * 
	 * @param <T>
	 * @param resultType
	 * @param serviceName
	 * @param methodName
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> T sendTask(Class<T> resultType, String serviceName,
			String methodName, Object... params);
	
	/**
	 * 给指定的session发送消息
	 * @param <T>
	 * @param session
	 * @param resultType
	 * @param serviceName
	 * @param methodName
	 * @param params
	 * @return
	 */
	<T> T sendTaskByServerId(IoSession session, Class<T> resultType, String serviceName, String methodName,
			Object[] params);

	/**
	 * 将gameServer注册到Node Server上 gameServer初始化的时候调用
	 * 
	 * @param userId
	 */
	public Object registerNode(Map<String, Object> params) ;

	/**
	 * 解除gameServer在Node Server上的注册
	 * 
	 * @param nodeName
	 */
	public void unRegisterNode(String nodeName);



	public WolfClient getWolfClient();
	public void setWolfClient(WolfClient client);

	/////判断连接是否正常
	public boolean isConnected();
	
	/////////////////////////////////包装的任务服务接口
	///包装在这里不好，最好是独立出来一个，目前只是简单起见
	/**
	 * 启动一个后台定时任务(在node server上启动) ，参见JobService
	 * 
	 * 任务到了启动时间后，nodeserver会向node 发送消息，
	 * 
	 * node接收到消息后， 根据job中指定的服务名，方法名，执行相应的服务，参见RemoteWolfService
	 * 
	 * @param job
	 * @param asyn
	 *            =true，异步发送 =false 同步发送
	 * @throws Exception
	 */
	public void startJob(Job job, boolean asyn);

	public void startJob(Job job);

	/**
	 * 删除一个计划中的任务
	 * 
	 * @param groupName
	 * @param jobIdInGroup
	 * @param asyn
	 *            =true，异步发送 =false 同步发送
	 * @throws Exception
	 */
	public boolean deleteJob(String groupName, String jobIdInGroup, boolean asyn);

	public boolean deleteJob(String groupName, String jobIdInGroup);

	/**
	 * 判断job是否在等待运行中
	 * 
	 * @param groupName
	 * @param jobIdInGroup
	 * @return
	 */
	boolean hasJob(String groupName, String jobIdInGroup);
	

	
	
}
