package com.youxigu.wolf.net;

/**
 * 异步调用方法:
 * 	对于不关心方法调用返回结果/或者返回异常的，使用AsyncWolfTask
 *  对于长时间的方法，也用AsyncWolfTask,防止阻塞其他socket请求
 *  
 * 
 * 与SyncWolfTask对应
 * 
 * 
 * 由RemoteWolfService负责处理
 * 
 * 
 * @author Administrator
 * 
 */
public class AsyncWolfTask implements WolfTask {
	private static final long serialVersionUID = 7558761807306518978L;
	private String serviceName;// 服务名 app端
	private String methodName;// 方法名
	private Object[] params;

	public AsyncWolfTask() {

	}

	public AsyncWolfTask(String serviceName, String methodName, Object[] params) {
		this.serviceName = serviceName;
		this.methodName = methodName;
		this.params = params;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public WolfTask execute(Response response) {

		return null;
	}
	
	public String toString()
	{
		StringBuilder sb = new  StringBuilder();
		sb.append(serviceName).append(".").append(methodName);
		return sb.toString();
	}

}
