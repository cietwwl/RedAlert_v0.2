package com.youxigu.dynasty2.openapi.util;
/**
 *  @creator  zhaiyong
 *  openapi返回码解析处理
 *  @creatortime 2012-9-12 下午05:25:03
 *
 */

public class ErrorCode {
	
	/*
	 * ret = 0: 正确返回 
	ret > 0: 调用OpenAPI时发生错误，需要开发者进行相应的处理。 
	-20 <= ret <= -1: 接口调用不能通过接口代理机校验，需要开发者进行相应的处理。 
	ret <-50: 系统内部错误，请通过企业QQ联系技术支持，调查问题原因并获得解决方案。 

	 */
	
	public static String parseCode(String code){
		int ret = Integer.parseInt(code.trim());
		String result = null;
		switch(ret)
		{
		case    1 : result="请求参数错误";
		case 	2 : result="用户没有开通对应的平台（朋友、空间、微博...）";
		case 1000 : result="非法操作";
		case 1001 : result="服务器繁忙";
		case 1002 : result="用户没有登录态";
		case 1003 : result="账户被冻结";
		case 1004 : result="账户余额不足";
		case 1005 : result="用户没有开通腾讯朋友，请先到http://www.pengyou.com/ 开通腾讯朋友";
		case 1006 : result="用户没有开通QQ空间，请先到http://qzone.qq.com/ 开通QQ空间";
		case 1100 : result="cdkey不存在";
		case 1101 : result="用户和cdkey不存在绑定关系，或礼品赠送完毕";
		case 1102 : result="参加活动受限";
		case 1300 : result="多区选服页面登录验证失败";
		case -1   : result="请求参数无效";
		case -2   : result="请求中的appid不存在";
		case -3   : result="无API访问权限";
		case -4   : result="IP没有权限";
		case -5   : result="签名参数sig校验失败";
		case -6   : result="访问频率超限";
		case -7   : result="协议不合法（要求必须为https协议的地方，使用了http协议）";
		case -8   : result="请求受限";
		case -9   : result="API不存在";
		case -64  : result="openid或者openkey不合法";
		case -65  : result="系统繁忙导致的连接超时";
		default   : result="系统内部错误。code："+ret+" 请联系技术支持";
		}
		return result;
	}

}
