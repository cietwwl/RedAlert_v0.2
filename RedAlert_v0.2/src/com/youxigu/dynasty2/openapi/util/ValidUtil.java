package com.youxigu.dynasty2.openapi.util;

import com.youxigu.dynasty2.openapi.ErrorCode;
import com.youxigu.dynasty2.openapi.OpensnsException;

public class ValidUtil {

	public void connect(String openid, String openkey) {

	}

	public static boolean checkStatu(int value) {
		if (value == 0) {
			return true;

		} else {
		//	throw new BaseException("系统接口错误" + value);
			return false;
		}
		// else if(value>0){
		// throw new BaseException("调用OpenAPI时发生错误");
		// }else if(-20 <= value || value<= -1){
		// throw new BaseException("接口调用不能通过接口代理机校验");
		// }else if(value <-50){
		// throw new BaseException("系统内部错误");
		// }
		// return false;
	}

	public static boolean validOpenid(String openid, String openkey)
			throws OpensnsException {
		if (openid == null) {
			throw new OpensnsException(ErrorCode.PARAMETER_EMPTY, "openid 不能为空");
		}
		if (openkey == null) {
			throw new OpensnsException(ErrorCode.PARAMETER_EMPTY,
					"openkey 不能为空");
		}
		// 验证openid正确性 32位数字字母组合
		return (openid.length() == 32) && openid.matches("^[0-9A-Fa-f]+$");
	}

}
