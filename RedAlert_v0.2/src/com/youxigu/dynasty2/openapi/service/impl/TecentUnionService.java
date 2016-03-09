package com.youxigu.dynasty2.openapi.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.service.ITecentUnionService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MD5;

public class TecentUnionService implements ITecentUnionService {
	public static final Logger log = LoggerFactory
			.getLogger(TecentUnionService.class);
	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 3000;

	private String appid = "1000000000";
	private String appkey = "930ab05795d6bb6018153956c838bfc9";

	private String verifyLoginUrl = "http://debug.api.gcos.qq.com:8080/v1/user/verify";
	private String profile = "http://debug.api.gcos.qq.com:8080/v1/user/profile";

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public void setVerifyLoginUrl(String verifyLoginUrl) {
		this.verifyLoginUrl = verifyLoginUrl;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void init() {

	}

	@Override
	public Map<String, Object> profile(String openid, String openkey) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("p_accesstoken=").append(openkey);
		sb.append("&p_appid=").append(appid);
		sb.append("&p_openid=").append(openid);
		sb.append("&p_appsecret=").append(appkey);
		String sig = MD5.getMD5(sb.toString());

		String url = genUrl(this.profile, openid, openkey, appid, sig);

		String result = httpGet(url);
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {

				JsonNode node = jsonObj.path("info");
				if (node != null) {
					Map<String, Object> map = new HashMap<String, Object>(2);
					map.put("p_nickname", node.path("p_nickname")
							.getTextValue());
					map.put("p_avatar", node.path("p_avatar").getTextValue());
					return map;
				}

			} else {
				log.error("get profile error:error_code={},msg={}", jsonObj
						.path("error_code").getIntValue(), jsonObj.path("msg")
						.getValueAsText());
				throw new BaseException(-20000, "取平台用户信息出错，请重新登录");
			}

		}
		return null;
	}

	@Override
	public boolean verifyLogin(String openid, String openkey) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("p_accesstoken=").append(openkey);
		sb.append("&p_appid=").append(appid);
		sb.append("&p_openid=").append(openid);
		sb.append("&p_appsecret=").append(appkey);
		String sig = MD5.getMD5(sb.toString());

		String url = genUrl(this.verifyLoginUrl, openid, openkey, appid, sig);

		String result = httpGet(url);
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				return true;
			} else {
				log.error("verfy login error:error_code={},msg={}", jsonObj
						.path("error_code").getIntValue(), jsonObj.path("msg")
						.getValueAsText());
				throw new BaseException(-20000, "校验用户登录态错误，请重新登录");
			}

		}
		return false;
	}

	private String genUrl(String url, String openid, String openkey,
			String appid, String sig) {

		StringBuilder sb = new StringBuilder(128);
		sb.append(url);
		sb.append("?p_appid=").append(appid);
		sb.append("&p_openid=").append(openid);
		sb.append("&sig=").append(sig);
		sb.append("&p_accesstoken=").append(openkey);
		return sb.toString();
	}

	private String httpGet(String url) {
		HttpClient httpClient = new HttpClient();

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				CONNECTION_TIMEOUT);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(
				READ_DATA_TIMEOUT);

		GetMethod getMethod = new GetMethod(url);

		// 设置User-Agent
		getMethod.setRequestHeader("User-Agent", "Java MDK Client");

		// 设置编码
		getMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);

		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		try {

			int statusCode = httpClient.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				throw new BaseException("Request [" + url + "] failed:"
						+ getMethod.getStatusLine());
			}
			// 读取内容
			String result = getMethod.getResponseBodyAsString();
			if (result != null && result.length() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("return={},url={}", result, url);
				}
			}
			return result;

		} catch (HttpException e) {
			log.error("HttpException", e);
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			throw new BaseException("Request [" + url + "] failed:"
					+ e.getMessage());
		} catch (IOException e) {
			log.error("IOException", e);
			// 发生网络异常
			throw new BaseException("Request [" + url + "] failed:"
					+ e.getMessage());
		} finally {
			// 释放链接
			getMethod.releaseConnection();
		}
	}
	
	public static void main(String[] args){
		TecentUnionService service = new TecentUnionService();
		service.verifyLogin("gede2547ser354w2", "ser34sd54784sege");
	}
}
