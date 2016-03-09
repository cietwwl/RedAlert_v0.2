package com.youxigu.dynasty2.openapi.service;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.openapi.domain.Head;

/**
 * 文件名    IIdipProcessor.java
 *
 * 描  述    腾讯idip协议处理器
 *
 * 时  间    2014-8-23
 *
 * 作  者    huhaiquan
 *
 */
public interface IIdipProcessor{
	//请求传入道具列表不正确
	public static final int ERR_CODE_1001=-1001;
	//邮件附件添加的道具种类不能超过6种
	public static final int ERR_CODE_1002=-1002;
	//请求传入的OpenId为空
	public static final int ERR_CODE_1003=-1003;
	//用户不存在
	public static final int ERR_CODE_1004=1;
	//"请求命令错误，没有处理此命令方法。请检查idip接口请求命令：Cmdid"
	public static final int ERR_CODE_1005=-1005;
//	运行时错误
	public static final int ERR_CODE_1006=-1006;
//	//请求传入的OpenId为空
//	public static final int ERR_CODE_1007=-1007;
	
	/**
	 * 获得请求命令号
	 * 
	 * @return
	 */
	int getCmd();
	
	/**
	 * 返回命令号
	 * 
	 * @return
	 */
	int getRetrunCmd();

	String doIdipProcess(Head buildHead, JsonNode body);

}
