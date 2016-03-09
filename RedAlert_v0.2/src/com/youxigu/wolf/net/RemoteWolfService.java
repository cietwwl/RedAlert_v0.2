package com.youxigu.wolf.net;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理有返回值的请求，相当于RPC 1 利用ServiceLocator 取得 service指定的 实例； 2 利用反射找到methodName的调用
 * 
 * TODO:由于采用AMF3协议，目前不支持Double,不支持long[] TODO:不支持方法重载：相同参数个数的方法
 * 
 * @author wuliangzhu
 * 
 */
public class RemoteWolfService implements IWolfService, ISessionListener,
		IInitListener {
	private static Logger logger = LoggerFactory
			.getLogger(RemoteWolfService.class);
	private static Logger perf = LoggerFactory.getLogger("perf");

	public Object handleMessage(Response response, Object message) {
		if (message instanceof SyncWolfTask) {
			return syncCall(response, message);
		} else if (message instanceof AsyncWolfTask) {
			return asyncCall(response, message);
		}

		return null;
	}

	private Boolean syncCall(Response response, Object message) {

		SyncWolfTask task = SyncWolfTask.class.cast(message);
		if (task == null) {
			return Boolean.TRUE;
		}

		// 检测task状态，如果是请求，继续往下走；如果是响应，设置响应状态
		if (task.getState() == SyncWolfTask.RESPONSE) {
			ResultMgr.requestCompleted(response.getSession(),
					task.getRequestId(), task.getResult());

			return Boolean.TRUE;
		}

		long time = System.currentTimeMillis();

		task.setState(SyncWolfTask.RESPONSE);

		String serviceName = task.getServiceName();
		String methodName = task.getMethodName();

		// Object[] params = convertLong(task.getParams());
		Object[] params = task.getParams();

		try {
			Object ret = MethodUtil.call(serviceName, methodName, params);
			task.setResult(ret);
		} catch (InvocationTargetException e) {
			logger.error("远程同步调用异常", e.getTargetException());
			if (e.getTargetException() != null) {
				task.setResult(new RuntimeException(e.getTargetException()
						.toString()));
			} else {
				task.setResult(new RuntimeException(e.toString()));

			}
		} catch (Exception e) {
			logger.error("远程同步调用异常", e);
			task.setResult(new RuntimeException(e.toString()));
		}
		task.setParams(null);
		response.write(task);
		if (logger.isWarnEnabled()) {
			time = System.currentTimeMillis() - time;
			if (time > 100) {
				logger.warn("远程同步调用：{}.{},time={}", new Object[] { serviceName,
						methodName, time });
			}
		}
		return Boolean.TRUE;
	}

	private Boolean asyncCall(Response response, Object message) {
		AsyncWolfTask task = AsyncWolfTask.class.cast(message);
		if (task == null) {
			return Boolean.TRUE;
		}

		long time = System.currentTimeMillis();
		String serviceName = task.getServiceName();
		String methodName = task.getMethodName();

		// if (logger.isDebugEnabled()) {
		// logger.debug("call service:{}.{}", serviceName, methodName);
		// }

		Object[] params = task.getParams();// convertLong(task.getParams());

		try {
			MethodUtil.call(serviceName, methodName, params);
			return Boolean.TRUE;
		} catch (InvocationTargetException e) {
			logger.error("RemoteWolfService call error,{}.{}", serviceName,
					methodName);
			logger.error("远程异步调用异常", e);
		} catch (Exception e) {
			logger.error("error:", e);
		}

		if (logger.isWarnEnabled()) {
			logger.warn("远程异步调用：{}.{},time={}", new Object[] { serviceName,
					methodName, (System.currentTimeMillis() - time) });
		}

		// int retryCount = 3;// 重试3次.
		// while (retryCount > 0) {
		// try {
		// Object ret = MethodUtil.call(serviceName, methodName, params);
		// return true;
		// } catch (InvocationTargetException e) {
		// retryCount--;
		// if (e.getTargetException() != null) {
		// logger.error(e.getTargetException().toString(), e
		// .getTargetException());
		//
		// } else {
		// logger.error(e.toString(), e);
		//
		// }
		// } catch (Exception e) {
		// retryCount--;
		// logger.error("error:", e);
		// }
		// }

		return Boolean.TRUE;
	}

	public void close(Response response) {
		if (response.isNodeServerSession()) {
			Exception e = new Exception("连接已经关闭");
			// 释放ResultMgr关联的请求
			// if (this.resultMgr != null){
			ResultMgr.notifyAllRequest(response.getSession(), e);
			// }
		}
	}

	public void open(Response response) {
		// 新建一个ResultMgr
	}

	public void init(SocketContext context) {
		// resultMgr = context.get("resultMgr", ResultMgr.class);
	}

	// //TODO:AMF3encode/decode将long转换成double,这里反过来成Long.
	// 注意：这导致RemoteWolfServive不支持double参数
	private Object[] convertLong(Object[] params) {
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null) {
					if (params[i] instanceof Double) {
						params[i] = ((Double) params[i]).longValue();
					} else if (params[i] instanceof Object[]) {
						Object[] array = (Object[]) params[i];
						if (array.length > 0 && array[0] instanceof Double) {
							long[] newParam = new long[array.length];
							for (int k = 0; k < array.length; k++) {
								newParam[k] = ((Double) array[k]).longValue();
							}
							params[i] = newParam;
						}
					}
					// else if (params[i] instanceof Collection) {
					// Collection c = (Collection) params[i];
					// boolean isDouble = false;
					// Iterator lit = c.iterator();
					// while (lit.hasNext()) {
					// Object obj = lit.next();
					// if (obj instanceof Double) {
					// isDouble = true;
					// break;
					// }
					// }
					// if (isDouble) {//转换
					// c.toArray()
					// lit = c.iterator();
					// while (lit.hasNext()) {
					//
					// }
					// }
					//
					// }
				}
			}
		}

		return params;
	}

	@Override
	public void stop(boolean force) {
		// TODO Auto-generated method stub

	}

}
