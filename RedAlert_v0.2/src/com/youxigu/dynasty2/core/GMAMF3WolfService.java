package com.youxigu.dynasty2.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.chat.MessageFilter;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.wolf.net.IInitListener;
import com.youxigu.wolf.net.IWolfService;
import com.youxigu.wolf.net.MethodUtil;
import com.youxigu.wolf.net.NodeSessionMgr;
import com.youxigu.wolf.net.Response;
import com.youxigu.wolf.net.SocketContext;

/**
 * 接收并处理所有GM请求
 * 
 * @author Administrator
 * 
 */
public class GMAMF3WolfService implements IWolfService,IInitListener{

	public static Logger log = LoggerFactory.getLogger(GMAMF3WolfService.class);

	/**
	 * 同步请求处理
	 */
	//private ResultMgr resultMgr;
	
	@Override
	public Object handleMessage(Response response, Object message) {
		
		if (message instanceof Map) {

			Map inMap = (Map) message;

			int cmd = 0;
			Object tmp = inMap.get(IAMF3Action.ACTION_CMD_KEY);
			if (tmp == null && !(tmp instanceof Integer)) {
				return null;// 不是GM命令
			}
			cmd = (Integer) tmp;
			if (cmd <= 900000 || cmd>909999) {// 不是GM命令
				return null;
			}
			try {

//				// //同步调用标志
//				SyncStat syncstat = (SyncStat) inMap.get(AMF3WolfService.SYNC_KEY);
//				if (syncstat != null) {
//
//					if (syncstat.getStat() != SyncStat.SYNC_STATUS_REQUEST) {
//						ResultMgr.requestCompleted(response.getSession(), syncstat
//								.getId(), message);
//						return true;
//					} else {
//						syncstat.setStat(SyncStat.SYNC_STATUS_RESPONSE);
//					}
//				}
				GMTool.initCommandConfig(null);
				
				IoSession session = response.getSession();
				if (cmd == 900001) {// login
					inMap.put("serverType", NodeSessionMgr.SERVER_TYPE_GM);
					NodeSessionMgr.regIoSession(session, inMap);
					
					//设置flax gmtool标志，心跳处判断使用
					session.setAttribute(Response.GMTOOL_KEY, "gmtool");
					
					if (log.isInfoEnabled()) {
						log.info("gm 登录 ：{}", session.getRemoteAddress());
					}

				} else {

					// 安全性检查
					tmp = session.getAttribute(Response.CREDIT);
					if (tmp == null || (Integer) tmp != 1) {
						throw new BaseException("未登录的请求，您必须一先登录");
					}

					String gmCommand = GMTool.get(String.valueOf(cmd));
					if (gmCommand == null) {
						throw new BaseException("不可识别的GM命令" + cmd);
					}

					String serviceName = GMTool.get(gmCommand
							+ GMTool.SERVICENAME);
					String methodName = GMTool.get(gmCommand
							+ GMTool.METHODNAME);

					if (serviceName == null || methodName == null) {
						throw new BaseException("不可识别的GM命令" + cmd);
					}
					String paramTypes = GMTool
							.get(gmCommand + GMTool.PARAMTYPE);

					String[] inParamsTypes = null;
					if (paramTypes != null && !"".equals(paramTypes)) {
						inParamsTypes = StringUtils.split(paramTypes,",");
					}
					Object[] inParams = (Object[]) inMap.remove("param");

					Object[] inParamArr = null;
					if (inParamsTypes != null) {
						if (inParams == null
								|| inParams.length != inParamsTypes.length) {
							throw new BaseException("错误的命令参数，需要"
									+ inParamsTypes.length + "个参数");
						}
						inParamArr = new Object[inParamsTypes.length];
						for (int i = 0; i < inParamArr.length; i++) {
							try {
								inParamArr[i] = GMTool.parseParam(
										inParamsTypes[i], inParams[i].toString());
							} catch (Exception e) {
								throw new BaseException("参数" + i + "错误，应该是"
										+ inParamsTypes[i] + "类型");

							}
						}
					}
					Object retu = MethodUtil.call(serviceName, methodName, inParamArr);	
					if (retu != null && retu.getClass() != Void.class) {
						inMap.put("retu", GMTool.toJsonString(retu));
					}
				}
				inMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY, 0);// 默认没有异常				
			} catch (Throwable e) {

				inMap.put(IAMF3Action.ACTION_CMD_KEY, cmd);
				if (e instanceof BaseException) {
					// 业务异常
					inMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY,
							((BaseException) e).getErrCode());
				} else {
					// e.printStackTrace();
					if (e instanceof InvocationTargetException) {
						e = ((InvocationTargetException) e)
								.getTargetException();
					} else if (e.getCause() != null) {
						e = e.getCause();
					}
					if (e instanceof BaseException) {
						// 业务异常
						inMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY,
								((BaseException) e).getErrCode());
					} else {
						inMap.put(IAMF3Action.ACTION_ERROR_CODE_KEY,
								IAMF3Action.CMD_SYSTEM_ERROR);
					}
				}
				log.error("请求异常：" + cmd, e);

				inMap.put(IAMF3Action.ACTION_ERROR_KEY,
						e.getMessage() == null ? e.toString() : e.getMessage());

			} finally {
				// 清除过滤器
				MessageFilter.clear();

				// System.out.println("time="+(System.currentTimeMillis()-time));
				if (inMap != null) {
					response.getSession().write(inMap);
				}

			}

			return Boolean.TRUE;
		}
		return null;
	}

	@Override
	public void init(SocketContext context) {
		GMTool.initCommandConfig(null);
		
	}

	@Override
	public void stop(boolean force) {
		// TODO Auto-generated method stub
		
	}


}
