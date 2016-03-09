package com.youxigu.dynasty2.openapi.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.service.ITecentUnionPayService;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.user.service.IUserService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.DateUtils;
import com.youxigu.dynasty2.util.MD5;
import com.youxigu.wolf.net.UserSession;

/**
 * 联运支付接口
 * 
 * 目前可以使用的各种开发环境，请开发同学选择对应的环境修改下面的COSDK_URL=**
 * #开发内部联调环境：http://debug.api.gcos.qq.com:8080 #提测联调环境：
 * http://test.api.gcos.qq.com:8080 #游戏联调环境：
 * http://business.api.gcos.qq.com:8080 #正式环境： http://api.gcos.qq.com
 * #**************************************************************************
 * #当前使用的环境 COSDK_URL=http://business.api.gcos.qq.com:8080
 * 
 * 
 * @author Administrator
 * 
 */
public class TecentUnionPayService implements ITecentUnionPayService {
	private static Logger log = LoggerFactory
			.getLogger(TecentUnionPayService.class);
	private static final String PREFIX = "7x_";
	private AtomicLong orderId = new AtomicLong(System.currentTimeMillis());

	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 3000;

	private String appid = "1000000000";
	private String appkey = "930ab05795d6bb6018153956c838bfc9";

	private String payUrl = "/v1/pay/buy_goods";
	private String presentUrl = "/v1/pay/send_gift";
	private IUserService userService;
//	private IVipService vipService;

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public void setPresentUrl(String presentUrl) {
		this.presentUrl = presentUrl;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

//	public void setVipService(IVipService vipService) {
//		this.vipService = vipService;
//	}

	public void init() {

	}

	@Override
	public String getPaySig(Map<String, String> params) {
		params.put("p_appid", appid);

		String sig = makeSig(params);

		params.put("sig", sig);
		return sig;
	}

	@Override
	public Map<String, Object> pay(UserSession us, int cash) {

		if (userService == null) {
			userService = (IUserService) ServiceLocator
					.getSpringBean("userService");
//			vipService = (IVipService) ServiceLocator
//					.getSpringBean("vipService");
		}

		String cashStr = String.valueOf(cash);
		long userId = us.getUserId();
		Timestamp dttm = new Timestamp(System.currentTimeMillis());
		Map<String, String> params = new HashMap<String, String>();
		params.put("p_appid", appid);
		params.put("p_openid", us.getOpenid());
		params.put("g_cid", us.getVia());
		params.put("p_accesstoken", us.getOpenkey());
		params.put("server_id", us.getAreaId());
		params.put("pay_order", getOrderId(us.getAreaId(), userId));
		params.put("pay_model", "2");
		params.put("pay_time", DateUtils.datetime2Text(dttm));
		params.put("pay_product_code", Constant.CASH_ID);
		params.put("pay_product_name", Constant.CASH_NAME);
		params.put("pay_product_count", cashStr);

		User user = userService.getUserById(userId);
		if (user == null) {
			throw new BaseException("您还没有创建角色");
		}
//		UserVip uservip = vipService.getUserVip(userId);
//		int vipLv = uservip == null ? 0 : uservip.getVipLv();

		params.put("user_vip", String.valueOf(0/*vipLv*/));
		params.put("user_lv", String.valueOf(user.getUsrLv()));
		params.put("user_party", String.valueOf(user.getGuildId()));
		params.put("user_race", String.valueOf(user.getCountryId()));
		params.put("user_rolename", user.getUserName());
		params.put("user_roleid", String.valueOf(userId));
		params.put("buy_product_code", "10301007");
		params.put("buy_product_name", "虚拟道具");
		params.put("buy_product_count", cashStr);
		params.put("buy_product_price", "1");
		params.put("user_ip", us.getUserip() == null ? "" : us.getUserip());
		params.put("p_device_info", us.getdInfo());
		params.put("extinfo", "");
		String sig = makeSig(params);

		// 构造URL
		String url = makeUrl(payUrl, params, sig);

		String result = httpGet(url);
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				Map<String, Object> map = new HashMap<String, Object>(2);
				map.put("p_orderid", jsonObj.path("p_orderid").getTextValue());
				map.put("total_used", jsonObj.path("total_used").getIntValue());
				map.put("used", jsonObj.path("used").getIntValue());
				map.put("gift_used", jsonObj.path("gift_used").getIntValue());
				map.put("balance", jsonObj.path("balance").getIntValue());
				map.put("gift_balance", jsonObj.path("gift_balance")
						.getIntValue());
				map.put("extinfo", jsonObj.path("extinfo").getTextValue());
				return map;

			} else {
				int errCode = jsonObj.path("error_code").getIntValue();
				String msg = jsonObj.path("msg").getValueAsText();
				log.error("pay error:error_code={},msg={}", errCode, msg);

				throw new BaseException(-ret, "支付失败,错误码:" + errCode + "," + msg);

			}

		}
		return null;
	}

	@Override
	public Map<String, Object> present(UserSession us, int cash) {

		if (userService == null) {
			userService = (IUserService) ServiceLocator
					.getSpringBean("userService");
//			vipService = (IVipService) ServiceLocator
//					.getSpringBean("vipService");
		}

		String cashStr = String.valueOf(cash);
		long userId = us.getUserId();
		Timestamp dttm = new Timestamp(System.currentTimeMillis());
		Map<String, String> params = new HashMap<String, String>();
		params.put("p_appid", appid);
		params.put("p_openid", us.getOpenid());
		params.put("g_cid", us.getVia());
		params.put("p_accesstoken", us.getOpenkey());

		params.put("server_id", us.getAreaId());
		String orderId = getOrderId(us.getAreaId(), userId);
		params.put("pay_order", orderId);
		params.put("send_time", DateUtils.datetime2Text(dttm));
		params.put("discountid", "");

		User user = userService.getUserById(userId);
		if (user == null) {
			throw new BaseException("您还没有创建角色");
		}
//		UserVip uservip = vipService.getUserVip(userId);
//		int vipLv = uservip == null ? 0 : uservip.getVipLv();

		params.put("user_vip", String.valueOf(0/*vipLv*/));
		params.put("user_lv", String.valueOf(user.getUsrLv()));
		params.put("user_party", String.valueOf(user.getGuildId()));
		params.put("user_race", String.valueOf(user.getCountryId()));
		params.put("user_rolename", user.getUserName());
		params.put("user_roleid", String.valueOf(userId));

		params.put("gift_serial", orderId);
		params.put("gift_product_code", Constant.CASH_ID);
		params.put("gift_product_name", Constant.CASH_NAME);
		params.put("gift_product_count", cashStr);
		params.put("user_ip", us.getUserip() == null ? "" : us.getUserip());
		params.put("p_device_info", us.getdInfo());
		params.put("extinfo", "");
		String sig = makeSig(params);

		// 构造URL
		String url = makeUrl(presentUrl, params, sig);

		String result = httpGet(url);
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				Map<String, Object> map = new HashMap<String, Object>(2);
				map.put("p_orderid", jsonObj.path("p_orderid").getTextValue());
				map.put("balance_total", jsonObj.path("balance_total")
						.getIntValue());
				map.put("total", jsonObj.path("total").getIntValue());
				map.put("gift_total", jsonObj.path("gift_total").getIntValue());
				map.put("balance", jsonObj.path("balance").getIntValue());
				map.put("gift_balance", jsonObj.path("gift_balance")
						.getIntValue());
				map.put("extinfo", jsonObj.path("extinfo").getTextValue());
				return map;

			} else {
				int errCode = jsonObj.path("error_code").getIntValue();
				String msg = jsonObj.path("msg").getValueAsText();
				log.error("pay error:error_code={},msg={}", errCode, msg);

				throw new BaseException(-ret, "赠送失败,错误码:" + errCode + "," + msg);

			}

		}
		return null;
	}

	@Override
	public String getOrderId(String areaId, long userId) {
		long currId = orderId.getAndIncrement();
		StringBuilder sb = new StringBuilder(20);
		sb.append(PREFIX).append(areaId).append("_").append(currId).append("_")
				.append(userId);
		return sb.toString();
	}

	@Override
	public String makeSig(Map<String, String> params) {

		params.remove("sig");
		Object[] keys = params.keySet().toArray();

		Arrays.sort(keys);

		StringBuilder buffer = new StringBuilder(256);

		for (int i = 0; i < keys.length; i++) {
			buffer.append(keys[i]).append("=").append(params.get(keys[i]));
			buffer.append("&");
		}
		buffer.append("p_appsecret=").append(this.appkey);
		String source = buffer.toString();
		String sig = MD5.getMD5(source);

		if (log.isDebugEnabled()) {
			log.debug("source:{}", source);
			log.debug("sig:{}", sig);
		}

		return sig;

	}

	private String makeUrl(String url, Map<String, String> params, String sig) {

		StringBuilder sb = new StringBuilder(256);
		sb.append(url).append("?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (value == null) {
				value = "";
			} else {
				try {
					value = java.net.URLEncoder.encode(value, CONTENT_CHARSET);
				} catch (Exception e) {
					log.error("encode error", e);
				}
			}
			sb.append(key).append("=").append(value);
			sb.append("&");
		}

		sb.append("sig=").append(sig);

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
}
