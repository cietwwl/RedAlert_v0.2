package com.youxigu.dynasty2.core.flex;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.util.StringUtils;
import com.youxigu.wolf.net.codec.IAMF3Message;

/**
 * 消息编号与消息处理类的映射定义
 * 
 * @author Administrator
 * 
 */
public class ActionDefine {
	/**
	 * 消息编号
	 */
	private int cmd;
	/**
	 * 版本号,not use
	 */
	private int ver;

	/**
	 * 消息处理类
	 */
	private Object bean;
	/**
	 * 消息处理类的处理方法
	 */
	private String methodName;

	/**
	 * 需要进行预处理的方法:OpenPlatformTransFilter
	 */
	private String prevMethodName;

	/**
	 * 活跃度编号
	 */
	// private int activityId=-1;
	/**
	 * 是否进行sid的验证,（是否检查已经登录过，有些请求不需要登录，就可以访问）
	 */
	private boolean secCheck = true;

	/**
	 * 是否进行请求频率验证
	 */
	private boolean frequency = true;

	/**
	 * 是否受防沉迷控制
	 */
	private boolean addicted = false;
	
	/**
	 * 二级密码校验
	 */
	private boolean pwdcheak = false;

	private String subCmds = null;
	private transient List<Integer> subActionIds = null;
	/**
	 * 是否检查userId
	 */
	// private boolean userIdCheck = true;

	/**
	 * 是否执行开放平台的交易token检查
	 */
	// private boolean checkToken;

	/**
	 * 是前台自动调用协议,还是玩家主动调用协议<BR>
	 * 主动记录为 主动记录为 1 自动记录为 自动记录为 2 无法区分记录为 无法区分记录为 无法区分记录为 0<br>
	 * 默认为1
	 * 
	 */
	private byte auto = 1;

	/**
	 * 拦截并附加到本协议内的系统事件
	 */
	private Map<Integer, Object> filterEventTypeMaps;
	/**
	 * 拦截并丢弃的系统事件
	 */
	private Map<Integer, Object> discardEventTypeMaps;

	/**
	 * 过滤的消息频道
	 */
	private Map<String, Object> filterChannelMaps;
	/**
	 * 传入参数的类型
	 */
	private Class<? extends IAMF3Message> inParmClass;

	/**
	 * 返回参数的类型
	 */
	private Class<? extends IAMF3Message> outParmClass;

	/**
	 * 缓存Method实例，避免每次都查找Method
	 */
	private transient Method method;
	private transient Method prevMethod;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getPrevMethodName() {
		return prevMethodName;
	}

	public void setPrevMethodName(String prevMethodName) {
		this.prevMethodName = prevMethodName;
	}

	public Method getPrevMethod() {
		return prevMethod;
	}

	public void setPrevMethod(Method prevMethod) {
		this.prevMethod = prevMethod;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isSecCheck() {
		return secCheck;
	}

	public void setSecCheck(boolean secCheck) {
		this.secCheck = secCheck;
	}

	public boolean isFrequency() {
		return frequency;
	}

	public void setFrequency(boolean frequency) {
		this.frequency = frequency;
	}

	public Class<? extends IAMF3Message> getInParmClass() {
		return inParmClass;
	}

	public void setInParmClass(Class<? extends IAMF3Message> inParmClass) {
		this.inParmClass = inParmClass;
	}

	public Class<? extends IAMF3Message> getOutParmClass() {
		return outParmClass;
	}

	public void setOutParmClass(Class<? extends IAMF3Message> outParmClass) {
		this.outParmClass = outParmClass;
	}

	public void setFilterEventTypes(String filterEventTypes) {
		if (filterEventTypes != null) {
			String[] typeArr = StringUtils.split(filterEventTypes, ",");
			filterEventTypeMaps = new HashMap<Integer, Object>(typeArr.length);
			for (String typeStr : typeArr) {
				int eventType = Integer.parseInt(typeStr);
				filterEventTypeMaps.put(eventType, eventType);
			}
		}

	}

	public void setDiscardEventTypes(String discardEventTypes) {
		if (discardEventTypes != null) {
			String[] typeArr = StringUtils.split(discardEventTypes, ",");
			discardEventTypeMaps = new HashMap<Integer, Object>(typeArr.length);
			for (String typeStr : typeArr) {
				int eventType = Integer.parseInt(typeStr);
				discardEventTypeMaps.put(eventType, eventType);
			}
		}

	}

	public void setFilterChannels(String filterChannels) {
		if (filterChannels != null) {
			String[] channelArr = StringUtils.split(filterChannels, ",");
			filterChannelMaps = new HashMap<String, Object>(channelArr.length);
			for (String typeStr : channelArr) {
				filterChannelMaps.put(typeStr, typeStr);
			}
		}

	}

	public Map<String, Object> getFilterChannelMaps() {
		return filterChannelMaps;
	}

	public Map<Integer, Object> getDiscardEventTypeMaps() {
		return discardEventTypeMaps;
	}

	public void setFilterChannelMaps(Map<String, Object> filterChannelMaps) {
		this.filterChannelMaps = filterChannelMaps;
	}

	public void setFilterEventTypeMaps(Map<Integer, Object> filterEventTypeMaps) {
		this.filterEventTypeMaps = filterEventTypeMaps;
	}

	public Map<Integer, Object> getFilterEventTypeMaps() {
		return filterEventTypeMaps;
	}

	public boolean isAddicted() {
		return addicted;
	}

	public void setAddicted(boolean addicted) {
		this.addicted = addicted;
	}

	public boolean isPwdcheak() {
		return pwdcheak;
	}

	public void setPwdcheak(boolean pwdcheak) {
		this.pwdcheak = pwdcheak;
	}

	public byte getAuto() {
		return auto;
	}

	public void setAuto(byte auto) {
		this.auto = auto;
	}

	public boolean isDiscardEvent(int eventType) {
		return discardEventTypeMaps != null
				&& discardEventTypeMaps.containsKey(eventType);
	}

	public void addFilterEventType(int eventType) {
		if (filterEventTypeMaps == null) {
			filterEventTypeMaps = new HashMap<Integer, Object>();
		}
		filterEventTypeMaps.put(eventType, eventType);
	}

	public void addFilterEventType(Map<Integer, Object> maps) {
		if (filterEventTypeMaps == null) {
			filterEventTypeMaps = new HashMap<Integer, Object>();
		}
		filterEventTypeMaps.putAll(maps);
	}

	public void setSubCmds(String subCmds) {
		if (subCmds != null) {

			String[] idArr = StringUtils.split(subCmds, ",");
			subActionIds = new ArrayList<Integer>(idArr.length);
			for (String idStr : idArr) {
				subActionIds.add(Integer.parseInt(idStr));
			}
		}
	}

	public List<Integer> getSubActionIds() {
		return subActionIds;
	}

	// public int getActivityId() {
	// return activityId;
	// }
	//
	// public void setActivityId(int activityId) {
	// this.activityId = activityId;
	// }

	// public boolean isUserIdCheck() {
	// return userIdCheck;
	// }
	//
	// public void setUserIdCheck(boolean userIdCheck) {
	// this.userIdCheck = userIdCheck;
	// }

}
