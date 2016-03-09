package com.youxigu.dynasty2.openapi.service;

import java.util.Map;



/**
 * Tecent MSDK API 包装类 ----联运类
 * 
 * @author Administrator
 * 
 */
public interface ITecentUnionService {
	/**
	 * 验证登录态是否有效，有效的话会自动续期。 /auth/verify_login
	 * 
	 * @param openid
	 * @param openkey
	 * @return
	 */
	boolean verifyLogin(String openid, String openkey);

	/**
	 * 获取用户帐号基本信息 /relation/qqprofile
	 * 
	 * @param openid
	 * @param openkey
	 * @return
	 */
	Map<String,Object> profile(String openid, String openkey);

}
