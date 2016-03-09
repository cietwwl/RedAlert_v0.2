package com.youxigu.dynasty2.openapi.service.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.shardbatis.spring.jdbc.transaction.DefaultTransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.ErrorCode;
import com.youxigu.dynasty2.openapi.OpenApiV3;
import com.youxigu.dynasty2.openapi.OpensnsException;
import com.youxigu.dynasty2.openapi.service.ITecentPayService;
import com.youxigu.dynasty2.util.BaseException;

public class TecentPayService extends DefaultTransactionListener implements
		ITecentPayService {
	public static final Logger log = LoggerFactory
			.getLogger(TecentPayService.class);
	private String serverName = "msdktest.qq.com"; // msdk.qq.com
	private String balaceUrl = "/mpay/get_balance_m";
	private String payUrl = "/mpay/pay_m";
	private String cancelPayUrl = "/mpay/cancel_pay_m";
	private String presentUrl = "/mpay/present_m";
	private String subscribeUrl = "/mpay/subscribe_m";
	private String transferUrl = "/mpay/transfer_m";

	private String appip = null;

	private static ThreadLocal<Boolean> inTrans = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}

	};

	private static ThreadLocal<List<PayCache>> payInLocal = new ThreadLocal<List<PayCache>>() {
		protected List<PayCache> initialValue() {
			return null;
		}

	};

	public void setTransferUrl(String transferUrl) {
		this.transferUrl = transferUrl;
	}

	public void setSubscribeUrl(String subscribeUrl) {
		this.subscribeUrl = subscribeUrl;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setBalaceUrl(String balaceUrl) {
		this.balaceUrl = balaceUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public void setCancelPayUrl(String cancelPayUrl) {
		this.cancelPayUrl = cancelPayUrl;
	}

	public void setPresentUrl(String presentUrl) {
		this.presentUrl = presentUrl;
	}

	public void init() {
		try {
			this.appip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			this.appip = "127.0.0.1";
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getBalances(int platformType, String openid,
			String openkey, String pay_token, String pf, String pfkey,
			String zoneid) {

		String appid = Constant.APP_ID_QQ;
		String appkey = Constant.APP_KEY_QQ;

		// if (platformType == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		//
		// } else {
		// if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
		// if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// }
		// tecent 小小让ios上使用这个appid,那么appkey呢?
		if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
			appid = Constant.PAY_ID_IOS;
		}

		// 构造cookie参数
		Map<String, String> cookies = getCookies(platformType, balaceUrl);

		// 构造请求参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
			params.put("openkey", openkey);
			params.put("pay_token", openkey);
		} else {
			params.put("openkey", openkey);
			params.put("pay_token", pay_token == null ? "" : pay_token);
		}
		// params.put("appid", appid);
		params.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
		params.put("pf", pf);
		params.put("pfkey", pfkey);
		params.put("zoneid", zoneid);

		// 可选的

		// format
		OpenApiV3 sdk = new OpenApiV3(appid, appkey, serverName);

		String resp = null;
		try {
			resp = sdk.api(balaceUrl, params, cookies, Constant.PROTOCOL_HTTP,
					null);
		} catch (OpensnsException e) {
			if (e.getErrorCode() == ErrorCode.NETWORK_ERROR) {
				// retry
				try {
					resp = sdk.api(balaceUrl, params, cookies,
							Constant.PROTOCOL_HTTP, null);
				} catch (OpensnsException e1) {
					// TODO Auto-generated catch block
					log.error("获取帐户余额失败", e1);
					throw new BaseException("网络异常：["
							+ ErrorCode.getMsg(e1.getErrorCode()) + "]");
				}

			} else {
				// TODO Auto-generated catch block
				log.error("获取帐户余额失败", e);
				throw new BaseException("获取帐户余额失败：["
						+ ErrorCode.getMsg(e.getErrorCode()) + "]");

			}
		}
		if (log.isDebugEnabled()) {
			log.debug("获取帐户余额结果内容：" + resp);
		}

		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		int ret = jsonObj.path("ret").getIntValue();
		if (ret != 0) {
			throw new BaseException(-ret, "获取帐户余额失败,错误码:" + ret + ","
					+ jsonObj.path("msg").getTextValue());
			// throw new BaseException("获取帐户余额失败:" + ret);
		}

		int balance = jsonObj.path("balance").getIntValue();

		Map<String, Object> retu = new HashMap<String, Object>(2);
		retu.put("balance", balance);
		retu.put("gen_balance", jsonObj.path("gen_balance").getIntValue());
		retu.put("first_save", jsonObj.path("first_save").getIntValue());
		retu.put("save_amt", jsonObj.path("save_amt").getIntValue());

		JsonNode node = jsonObj.path("tss_list");
		if (node != null) {
			List<Map<String, Object>> tssList = new ArrayList<Map<String, Object>>();
			Iterator<JsonNode> lit = node.getElements();
			while (lit.hasNext()) {
				Map<String, Object> oneMap = new HashMap<String, Object>();
				JsonNode one = lit.next();
				oneMap.put("inner_productid", one.path("inner_productid")
						.getTextValue());
				oneMap.put("begintime", one.path("begintime").getTextValue());
				oneMap.put("endtime", one.path("endtime").getTextValue());
				oneMap.put("paychan", one.path("paychan").getTextValue());
				oneMap.put("paysubchan", one.path("paysubchan").getTextValue());
				oneMap.put("autopaychan", one.path("autopaychan")
						.getTextValue());
				oneMap.put("autopaysubchan", one.path("autopaysubchan")
						.getTextValue());
				oneMap.put("grandtotal_opendays", one.path(
						"grandtotal_opendays").getTextValue());
				oneMap.put("grandtotal_presentdays", one.path(
						"grandtotal_presentdays").getTextValue());
				oneMap.put("first_buy_time", one.path("first_buy_time")
						.getTextValue());
				oneMap.put("extend", one.path("extend").getTextValue());
				tssList.add(oneMap);

			}
			retu.put("tss_list", tssList);
		}
		return retu;

	}

	@Override
	public int getBalance(int platformType, String openid, String openkey,
			String pay_token, String pf, String pfkey, String zoneid) {

		Map<String, Object> retu = getBalances(platformType, openid, openkey,
				pay_token, pf, pfkey, zoneid);

		return (Integer) retu.get("balance");

	}

	@Override
	public Map<String, Object> pay(int platformType, String openid,
			String openkey, String pay_token, String pf, String pfkey,
			String zoneid, String amt) {
		String appid = Constant.APP_ID_QQ;
		String appkey = Constant.APP_KEY_QQ;
		// String appid = null;
		// String appkey = null;
		// if (platformType == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		// } else {
		// if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
		// if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// }
		if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
			appid = Constant.PAY_ID_IOS;
		}
		// 构造cookie参数
		Map<String, String> cookies = getCookies(platformType, payUrl);

		// 构造请求参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
			params.put("openkey", openkey);
			params.put("pay_token", openkey);
		} else {
			params.put("openkey", openkey);
			params.put("pay_token", pay_token == null ? "" : pay_token);
		}

		// params.put("pay_token", pay_token);
		// params.put("appid", appid);
		params.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
		params.put("pf", pf);
		params.put("pfkey", pfkey);
		params.put("zoneid", zoneid);
		params.put("amt", amt);
		params.put("format", "json");
		// 可选的
		// userip
		// accounttype
		OpenApiV3 sdk = new OpenApiV3(appid, appkey, serverName);

		String resp = null;
		try {
			resp = sdk.api(payUrl, params, cookies, Constant.PROTOCOL_HTTP,
					null);

		} catch (OpensnsException e) {
			if (e.getErrorCode() == ErrorCode.NETWORK_ERROR) {
				try {
					resp = sdk.api(payUrl, params, cookies,
							Constant.PROTOCOL_HTTP, null);
				} catch (OpensnsException e1) {
					log.error("支付失败", e1);
					throw new BaseException("网络异常：["
							+ ErrorCode.getMsg(e1.getErrorCode()) + "]");
				}

			} else {
				log.error("支付失败", e);
				throw new BaseException("支付失败：["
						+ ErrorCode.getMsg(e.getErrorCode()) + "]");
			}

		}
		if (log.isDebugEnabled()) {
			log.debug("付款结果内容：" + resp);
		}
		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		int ret = jsonObj.path("ret").getIntValue();
		if (ret != 0) {
			throw new BaseException(-ret,"支付失败,错误码:" + ret + ","
					+ jsonObj.path("msg").getTextValue());
		}
		int balance = jsonObj.path("balance").getIntValue();
		String billno = jsonObj.path("billno").getTextValue();

		////这里不用了，目前采用的假托管，不理会腾讯侧余额的对错
		// 放到threadLocal缓存中
//		if (inTrans.get()) {
//			addPayToLocalCache(billno, sdk, params, cookies);
//		}

		if (log.isDebugEnabled()) {

			log.debug("balance:{}", balance);
			log.debug("billno:{}", billno);
		}
		Map<String, Object> retu = new HashMap<String, Object>(2);
		retu.put("balance", balance);
		retu.put("billno", billno);
		return retu;

	}

	@Override
	public void cancelPay(int platformType, String openid, String openkey,
			String pay_token, String pf, String pfkey, String zoneid,
			String amt, String billno) {
		String appid = Constant.APP_ID_QQ;
		String appkey = Constant.APP_KEY_QQ;
		// String appid = null;
		// String appkey = null;
		// if (platformType == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		//
		// } else {
		// if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
		// if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// }
		if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
			appid = Constant.PAY_ID_IOS;
		}
		// 构造cookie参数
		Map<String, String> cookies = getCookies(platformType, cancelPayUrl);

		// 构造请求参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
			params.put("openkey", openkey);
			params.put("pay_token", openkey);
		} else {
			params.put("openkey", openkey);
			params.put("pay_token", pay_token == null ? "" : pay_token);
		}

		// params.put("pay_token", pay_token);
		// params.put("appid", appid);
		params.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
		params.put("pf", pf);
		params.put("pfkey", pfkey);
		params.put("zoneid", zoneid);
		params.put("amt", amt);
		params.put("billno", billno);
		params.put("format", "json");
		// 可选的
		// userip
		// accounttype
		OpenApiV3 sdk = new OpenApiV3(appid, appkey, serverName);

		String resp = null;
		try {
			resp = sdk.api(payUrl, params, cookies, Constant.PROTOCOL_HTTP,
					null);

		} catch (OpensnsException e) {
			if (e.getErrorCode() == ErrorCode.NETWORK_ERROR) {
				try {
					resp = sdk.api(payUrl, params, cookies,
							Constant.PROTOCOL_HTTP, null);
				} catch (OpensnsException e1) {
					log.error("取消支付失败", e1);
					throw new BaseException("网络异常：["
							+ ErrorCode.getMsg(e1.getErrorCode()) + "]");
				}

			} else {
				log.error("取消支付失败", e);
				throw new BaseException("取消支付失败：["
						+ ErrorCode.getMsg(e.getErrorCode()) + "]");
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("取消支付结果内容：" + resp);
		}
		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		int ret = jsonObj.path("ret").getIntValue();
		if (ret != 0) {
			throw new BaseException(-ret,"取消支付错误:" + ret);
		}

	}

	@Override
	public int present(int platformType, String openid, String openkey,
			String pay_token, String pf, String pfkey, String zoneid,
			String discountid, String giftid, String amt) {
		String appid = Constant.APP_ID_QQ;
		String appkey = Constant.APP_KEY_QQ;
		// String appid = null;
		// String appkey = null;
		// if (platformType == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		//
		// } else {
		// if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
		// if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// }
		if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
			appid = Constant.PAY_ID_IOS;
		}
		// 构造cookie参数
		Map<String, String> cookies = getCookies(platformType, presentUrl);

		// 构造请求参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
			params.put("openkey", openkey);
			params.put("pay_token", openkey);
		} else {
			params.put("openkey", openkey);
			params.put("pay_token", pay_token == null ? "" : pay_token);
		}

		// params.put("appid", appid);
		params.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
		params.put("pf", pf);
		params.put("pfkey", pfkey);
		params.put("zoneid", zoneid);
		params.put("pay_token", pay_token);
		params.put("discountid", discountid);
		params.put("giftid", giftid);
		params.put("presenttimes", amt);
		params.put("format", "json");
		// 可选的
		// userip
		OpenApiV3 sdk = new OpenApiV3(appid, appkey, serverName);

		String resp = null;
		try {
			resp = sdk.api(presentUrl, params, cookies, Constant.PROTOCOL_HTTP,
					null);

		} catch (OpensnsException e) {

			if (e.getErrorCode() == ErrorCode.NETWORK_ERROR) {
				try {
					resp = sdk.api(presentUrl, params, cookies,
							Constant.PROTOCOL_HTTP, null);
				} catch (OpensnsException e1) {
					log.error("赠送失败", e1);
					throw new BaseException("网络异常：["
							+ ErrorCode.getMsg(e1.getErrorCode()) + "]");
				}

			} else {
				log.error("赠送失败", e);
				throw new BaseException("赠送失败：["
						+ ErrorCode.getMsg(e.getErrorCode()) + "]");
			}

		}
		if (log.isDebugEnabled()) {
			log.debug("赠送结果内容：" + resp);
		}
		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		int ret = jsonObj.path("ret").getIntValue();
		if (ret != 0) {
			throw new BaseException(-ret,"赠送失败，平台返回:" + ret);
		}
		JsonNode b = jsonObj.path("balance");
		if (b != null) {
			return b.getIntValue();
		} else {
			return -1;
		}
		// int balance = jsonObj.path("balance").getIntValue();
		// String billno = jsonObj.path("billno").getTextValue();

		// // 放到threadLocal缓存中
		// if (inTrans.get()) {
		//
		// }

		// if (log.isDebugEnabled()) {
		//
		// log.debug("balance:{}", balance);
		// log.debug("billno:{}", billno);
		// }
		// Map<String, Object> retu = new HashMap<String, Object>(2);
		// retu.put("balance", balance);
		// retu.put("billno", billno);
		// return retu;

	}

	private Map<String, String> getCookies(int platformType, String org_loc) {
		Map<String, String> cookies = new HashMap<String, String>(4);
		if (platformType == Constant.PLATFORM_TYPE_QQ) {
			cookies.put("session_id", "openid");
			cookies.put("session_type", "kp_actoken");
		} else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {
			cookies.put("session_id", "hy_gameid");
			cookies.put("session_type", "wc_actoken");
		} else {// 游客
			cookies.put("session_id", "hy_gameid");
			cookies.put("session_type", "st_dummy");

		}
		// TODO:这里要不要urlEncode?
		cookies.put("org_loc", org_loc);
		cookies.put("appip", this.appip);
		return cookies;
	}

	public void doBeginAfter(Object transaction,
			TransactionDefinition definition) {
		inTrans.set(true);
		payInLocal.remove();
	}

	public void doCommitAfter(DefaultTransactionStatus status) {
		// 清理
		inTrans.set(false);
		payInLocal.remove();
	}

	/**
	 * 取消支付
	 */
	public void doRollbackAfter(DefaultTransactionStatus status) {

		try {
			List<PayCache> caches = payInLocal.get();
			if (caches != null) {
				log.warn("事务回滚，取消事务中的开放平台支付数据");
				for (PayCache cache : caches) {

					Map<String, String> params = cache.params;
					params.put("billno", cache.billno);
					Map<String, String> cookies = cache.cookies;
					if (cookies != null) {
						cookies.put("org_loc", this.cancelPayUrl);
					}

					String resp = null;
					try {
						resp = cache.sdk.api(this.cancelPayUrl, params,
								cookies, Constant.PROTOCOL_HTTP, null);
						if (log.isDebugEnabled()) {
							log.debug("取消支付付款结果内容：" + resp);
						}
					} catch (OpensnsException e) {
						log.error("取消支付失败，billno:{}", cache.billno);
						log.error("取消支付失败", e);
					}
					JsonNode jsonObj = JSONUtil.getJsonNode(resp);
					int ret = jsonObj.path("ret").getIntValue();
					if (ret != 0) {
						log.error("取消支付失败:billno={},msg={}", cache.billno,
								jsonObj.path("msg").getTextValue());
					}
				}
			}

		} catch (Exception e) {
			// 吃掉异常
			e.printStackTrace();
		} finally {
			inTrans.set(false);
			payInLocal.remove();
		}
	}

	private void addPayToLocalCache(String billno, OpenApiV3 sdk,
			Map<String, String> params, Map<String, String> cookies) {
		PayCache cache = new PayCache();
		cache.billno = billno;
		cache.sdk = sdk;
		cache.params = params;
		cache.cookies = cookies;

		List<PayCache> caches = payInLocal.get();
		if (caches == null) {
			caches = new ArrayList<PayCache>(1);
			payInLocal.set(caches);
		}
		caches.add(cache);
	}

	static class PayCache {
		public OpenApiV3 sdk;
		public Map<String, String> params;
		public Map<String, String> cookies;
		public String billno;

		public PayCache() {

		}

		public PayCache(OpenApiV3 sdk, Map<String, String> params,
				Map<String, String> cookies, String billno) {
			this.sdk = sdk;
			this.params = params;
			this.cookies = cookies;
			this.billno = billno;
		}
	}

	@Override
	public List<Map<String, Object>> getSubscribe(int platformType,
			String openid, String openkey, String pay_token, String pf,
			String pfkey, String zoneid) {
		String appid = Constant.APP_ID_QQ;
		String appkey = Constant.APP_KEY_QQ;
		// String appid = null;
		// String appkey = null;
		//
		// if (platformType == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (platformType == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		//
		// } else {
		// if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
		// if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_QQ) {
		// appid = Constant.APP_ID_QQ;
		// appkey = Constant.APP_KEY_QQ;
		// } else if (Constant.PLATFORM_TYPE == Constant.PLATFORM_TYPE_WEIXIN) {
		// appid = Constant.APP_ID_WX;
		// appkey = Constant.APP_KEY_WX;
		// pay_token = "";
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// } else {
		// throw new BaseException("支付平台错误");
		// }
		// }
		//
		if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
			appid = Constant.PAY_ID_IOS;
		}

		// 构造cookie参数
		Map<String, String> cookies = new HashMap<String, String>(1);
		cookies.put("org_loc", subscribeUrl);

		// 构造请求参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		if (platformType == Constant.PLATFORM_TYPE_QQ) {
			params.put("session_id", "openid");
			params.put("session_type", "kp_actoken");
		} else {
			params.put("session_id", "hy_gameid");
			params.put("session_type", "wc_actoken");
		}
		// params.put("openkey", openkey);

		// if (platformType == Constant.PLATFORM_TYPE_VISTOR) {
		// params.put("openkey", "openkey");
		// }else{
		// params.put("openkey", openkey);
		// }

		// 这里腾讯要求传openkey=pay_token
		params.put("openkey", pay_token == null ? "" : pay_token);
		params.put("pay_token", pay_token == null ? "" : pay_token);

		// params.put("appip", this.appip);

		params.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
		params.put("pf", pf);
		params.put("pfkey", pfkey);

		params.put("zoneid", zoneid);
		params.put("cmd", "QUERY");

		// 可选的

		// format
		OpenApiV3 sdk = new OpenApiV3(appid, appkey, serverName);

		String resp = null;
		try {
			resp = sdk.api(subscribeUrl, params, cookies,
					Constant.PROTOCOL_HTTP, null);

		} catch (OpensnsException e) {
			if (e.getErrorCode() == ErrorCode.NETWORK_ERROR) {
				try {
					resp = sdk.api(subscribeUrl, params, cookies,
							Constant.PROTOCOL_HTTP, null);
				} catch (OpensnsException e1) {
					log.error("获取订阅失败", e1);
					throw new BaseException("网络异常：["
							+ ErrorCode.getMsg(e1.getErrorCode()) + "]");
				}

			} else {
				log.error("获取订阅失败", e);
				throw new BaseException("获取订阅失败：["
						+ ErrorCode.getMsg(e.getErrorCode()) + "]");
			}

		}
		if (log.isInfoEnabled()) {
			log.info("获取订阅内容：" + resp);
		}
		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		int ret = jsonObj.path("ret").getIntValue();
		if (ret != 0) {
			throw new BaseException(-ret,"获取订阅失败:" + ret);
		}

		JsonNode node = jsonObj.path("list");
		if (node != null) {
			List<Map<String, Object>> tssList = new ArrayList<Map<String, Object>>();
			Iterator<JsonNode> lit = node.getElements();
			while (lit.hasNext()) {
				Map<String, Object> oneMap = new HashMap<String, Object>();
				JsonNode one = lit.next();
				oneMap.put("inner_productid", one.path("inner_productid")
						.getTextValue());
				oneMap.put("begintime", one.path("begintime").getTextValue());
				oneMap.put("endtime", one.path("endtime").getTextValue());
				oneMap.put("paychan", one.path("paychan").getTextValue());
				oneMap.put("paysubchan", one.path("paysubchan").getTextValue());
				oneMap.put("autopaychan", one.path("autopaychan")
						.getTextValue());
				oneMap.put("autopaysubchan", one.path("autopaysubchan")
						.getTextValue());
				oneMap.put("extend", one.path("extend").getTextValue());
				tssList.add(oneMap);

			}
			return tssList;
		}

		return null;
	}

	// /这个接口没有实现
	@Override
	public List<Map<String, Object>> transfer(int platformType, String openid,
			String openkey, String pf, String dstzoneid, String payerSessionId,
			String payer, String srczoneid, String amt, String pfkey,
			String appremark) {
		String appid = Constant.APP_ID_QQ;
		String appkey = Constant.APP_KEY_QQ;

		if (Constant.DEVIDE_TYPE == Constant.DEVIDE_TYPE_IOS) {
			appid = Constant.PAY_ID_IOS;
		}

		// 构造cookie参数
		Map<String, String> cookies = getCookies(platformType, transferUrl);

		// 构造请求参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		params.put("openkey", openkey);
		// params.put("appid", appid);
		params.put("ts", String.valueOf(System.currentTimeMillis() / 1000L));
		params.put("pf", pf);
		params.put("dstzoneid", dstzoneid);

		params.put("payer_session_id", "hy_gameid");
		params.put("payer", payer);

		params.put("srczoneid", srczoneid);

		params.put("amt", amt);
		params.put("pfkey", pfkey);

		// 可选的
		// accounttype
		// userip
		// appremark

		// format
		OpenApiV3 sdk = new OpenApiV3(appid, appkey, serverName);

		String resp = null;
		try {
			resp = sdk.api(transferUrl, params, cookies,
					Constant.PROTOCOL_HTTP, null);
		} catch (OpensnsException e) {
			if (e.getErrorCode() == ErrorCode.NETWORK_ERROR) {
				// retry
				try {
					resp = sdk.api(transferUrl, params, cookies,
							Constant.PROTOCOL_HTTP, null);
				} catch (OpensnsException e1) {
					// TODO Auto-generated catch block
					log.error("获取帐户余额失败", e1);
					throw new BaseException("网络异常：["
							+ ErrorCode.getMsg(e1.getErrorCode()) + "]");
				}

			} else {
				// TODO Auto-generated catch block
				log.error("获取帐户余额失败", e);
				throw new BaseException("获取帐户余额失败：["
						+ ErrorCode.getMsg(e.getErrorCode()) + "]");

			}
		}
		if (log.isDebugEnabled()) {
			log.debug("获取帐户余额结果内容：" + resp);
		}

		JsonNode jsonObj = JSONUtil.getJsonNode(resp);
		int ret = jsonObj.path("ret").getIntValue();
		if (ret != 0) {
			throw new BaseException(-ret,"转帐失败:" + ret);
		}
		return null;
	}

	public static void main(String[] args) {
		TecentPayService t = new TecentPayService();
		t.init();
		// t.getBalance(Constant.PLATFORM_TYPE_QQ,
		// "A3284A812ECA15269F85AE1C2D94EB37",
		// "933FE8C9AB9C585D7EABD04373B7155F",
		// "933FE8C9AB9C585D7EABD04373B7155F", "qq_m_qq-2001-android-2011-xxxx",
		// "933FE8C9AB9C585D7EABD04373B7155F", "7");
		t.pay(Constant.PLATFORM_TYPE_QQ, "A3284A812ECA15269F85AE1C2D94EB37",
				"933FE8C9AB9C585D7EABD04373B7155F",
				"933FE8C9AB9C585D7EABD04373B7155F",
				"qq_m_qq-2001-android-2011-xxxx",
				"933FE8C9AB9C585D7EABD04373B7155F", "7", "100");
		System.out.println("=====");
	}

}
