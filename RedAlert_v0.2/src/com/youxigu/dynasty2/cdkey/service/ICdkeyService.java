package com.youxigu.dynasty2.cdkey.service;

import java.util.Map;

public interface ICdkeyService {

	/**
	 * 使用cdkey
	 * @param userId
	 * @param cdkey
	 */
	void doUseCdKey(long userId,String openId,String areaId,String cdkey,Map<String,Object> params);
}
