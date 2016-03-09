package com.youxigu.wolf.net;

import java.io.Serializable;

/**
 * 
 * 记录玩家机器信息 为了简单，懒得构造标准javabean了
 * 
 * 
 * @author Administrator
 * 
 */
public class MobileClient implements Serializable {
	private static final long serialVersionUID = 4381551907980723802L;
	public String vClientVersion;
	public String vSystemSoftware;
	public String vSystemHardware;
	public String vTelecomOper;
	public String vNetwork;
	public int iScreenWidth;
	public int iScreenHight;
	public float Density;
	public int iRegChannel;
	public String vCpuHardware;
	public int iMemory;
	public String vGLRender;
	public String vGLVersion;
	public String vDeviceId;
}
