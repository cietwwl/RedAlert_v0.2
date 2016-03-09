/**
 * 
 */
package com.youxigu.dynasty2.openapi.service;

import java.util.HashMap;

import org.codehaus.jackson.JsonNode;

import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.ErrorCode;
import com.youxigu.dynasty2.openapi.OpensnsException;
import com.youxigu.dynasty2.openapi.SnsNetwork;
import com.youxigu.dynasty2.openapi.SnsSigCheck;

/**
 * @author asus
 * 验证好友邀请的invkey，用于“邀请好友即赠送礼品”等场景。

 平台提供了好友邀请前台接口（fusion2.dialog.invite），用户可以邀请好友添加应用。
 当邀请成功并且被邀请对象开通应用后，平台会在 iframe 嵌入的页面中，加入三个参数 invkey , itime , iopenid，分别对应的含义是：
 invkey 加密串 
 itime 邀请时间 
 iopenid 发起邀请者的openid 
 应用可以通过调用本接口来验证invkey，以此判断添加了该应用的用户是否是由某个用户邀请过来的，从而发起送礼等操作。

请求参数
 *http://113.108.20.23/v3/spread/verify_invkey?openid=B624064BA065E01CB73F835017FE96FA&openkey=5F154D7D2751AEDC8527269006F290F70297B7E54667536C&appid=2&
 sig=***********&pf=qzone&format=json&userip=112.90.139.30&iopenid=0000000000000000000000000039811C&
 itime=1334202931&invkey=8A96E97D5F393241F04CFD0255550241 
签名加密
openid=11111111111111111&openkey=2222222222222222&appid=123456&pf=qzone 

 */
public class VerifyInvkey {

	String appid = Constant.APP_ID_QQ;

	String appkey = Constant.APP_KEY_QQ;

	String pf = Constant.PF_QZONE;

	String scriptName = Constant.CGI_VERIFY_INVKEY;//"/v3/user/get_info";

	String serverName = Constant.SERVER_NAME;//"113.108.20.23";

	// 指定HTTP请求协议类型
	String protocol = Constant.PROTOCOL_HTTP;//"http";
	
	String format = Constant.FORMAT_JSON;
	

	public boolean api(String openid,String openkey,String userip,String iopenid, String itime, String invkey) throws OpensnsException {
		
		if( userip == null || iopenid == null || itime == null || invkey == null){
			throw new OpensnsException(ErrorCode.PARAMETER_EMPTY,
			"传入参数不完整");
		}
		// 检查openid openkey等参数
	
//		验证openid正确性 32位数字字母组合
		

//		 请求方法
		String method = "post";

		// 签名密钥
		String secret = this.appkey + "&";
		
		HashMap<String, String> sigparams = new HashMap<String, String>();

		sigparams.put("openid", openid);
		sigparams.put("openkey", openkey);
		sigparams.put("appid", this.appid);
		sigparams.put("pf", pf);
		// 计算签名
		String sig = SnsSigCheck.makeSig(method, scriptName, sigparams, secret);

		
		// 添加固定参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		params.put("openkey", openkey);
		params.put("appid", this.appid);
		params.put("sig", sig);
		params.put("pf", pf);
		params.put("format", format);
		params.put("userip", userip);
		params.put("iopenid", iopenid);
		params.put("itime", itime);
		params.put("invkey", invkey);

		StringBuilder sb = new StringBuilder(64);
		sb.append(protocol).append("://").append(this.serverName).append(
				scriptName);
		String url = sb.toString();

		// cookie
		HashMap<String, String> cookies = null;

		// 发送请求
		String resp = SnsNetwork.postRequest(url, params, cookies, protocol);

		//验证返回状态是否正确
		//this.checkStatu(resp);
		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		if(jsonObj.path("is_right").getIntValue()==1){
			return true;
		}else{
			return false;
		}
		// 
	}

	/* (non-Javadoc)
	 * @see com.youxigu.dynasty2.openapi.service.ValidUtil#connect(java.lang.String, java.lang.String)
	 */
	

}
