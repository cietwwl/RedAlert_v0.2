package com.youxigu.dynasty2.openapi.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.openapi.domain.FaceBookRecharge;

/**
 * 文件名    IFaceBookService.java
 *
 * 描  述    海外接口
 *
 * 时  间    2015-1-19
 *
 * 作  者    huhaiquan
 *
 * 版  本    v1.7
 */
public interface IFaceBookService {
	
	
	/**
	 * 
	 * 登录校验
	 * md5(appkey+efun_uid+时间戳)32位大写
	 * 
	 * @return
	 */
	boolean verifyLogin(String oldKey,String openId,String time);
	
	/**
	 * 获得角色列表信息
	 * 
	 * @param openId
	 * @param areaId
	 * @return
	 */
	public List<Map<String,Object>> getRoleList(String openId,String areaId);
	
	
	/**
	 * 充值
	 * @param recharge
	 * @return
	 */
	public String doRecharge(FaceBookRecharge recharge);
	

}
