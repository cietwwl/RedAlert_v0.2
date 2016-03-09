package com.youxigu.dynasty2.openapi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.openapi.Quser;
import com.youxigu.dynasty2.openapi.domain.QQFriendsVipInfo;
import com.youxigu.dynasty2.openapi.domain.QQScoreReport;
import com.youxigu.dynasty2.openapi.service.ITecentMobileQqService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MD5;

public class TecentMobileQqService implements ITecentMobileQqService {
	public static final Logger log = LoggerFactory
			.getLogger(TecentMobileQqService.class);
	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 3000;

	private String appid = "1000001640";
	private String appkey = "B0CpjjRg6Zt9eVl6";

	private String verifyLoginUrl = "http://msdktest.qq.com/auth/verify_login";
	private String profile = "http://msdktest.qq.com/relation/qqprofile";
	private String loadVip = "http://msdktest.qq.com/profile/load_vip";
	private String friends = "http://msdktest.qq.com/relation/qqfriends_detail";
	private String qqfriendsVip = "http://msdktest.qq.com/relation/qqfriends_vip";
	private String qqscoreBatch = "http://msdktest.qq.com/profile/qqscore_batch";

	private String vistorVerfyUrl = "http://msdktest.qq.com/auth/guest_check_token";

	private String shareUrl = "http://msdktest.qq.com/share/qq";

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public void setVistorVerfyUrl(String vistorVerfyUrl) {
		this.vistorVerfyUrl = vistorVerfyUrl;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public void setVerifyLoginUrl(String verifyLoginUrl) {
		this.verifyLoginUrl = verifyLoginUrl;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setLoadVip(String loadVip) {
		this.loadVip = loadVip;
	}

	/**
	 * @param qqscoreBatch
	 *            the qqscoreBatch to set
	 */
	public void setQqscoreBatch(String qqscoreBatch) {
		this.qqscoreBatch = qqscoreBatch;
	}

	/**
	 * @param qqfriendsVip
	 *            the qqfriendsVip to set
	 */
	public void setQqfriendsVip(String qqfriendsVip) {
		this.qqfriendsVip = qqfriendsVip;
	}

	public void init() {

	}

	@Override
	public boolean verifyLogin(String openid, String openkey, String userip) {
		String url = genUrl(this.verifyLoginUrl, openid);
		// BODY
		StringBuilder sb = new StringBuilder(200);
		sb.append("{\"appid\":").append(this.appid).append(",");
		sb.append("\"openid\":\"").append(openid).append("\",");
		sb.append("\"openkey\":\"").append(openkey).append("\",");
		sb.append("\"userip\":\"");
		if (userip != null) {
			sb.append(userip);
		}
		sb.append("\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				return true;
			} else {
				log.error("verfy login error:ret={},msg={}", ret, jsonObj.path(
						"msg").getValueAsText());
				throw new BaseException(-20000, "校验用户登录态错误，请重新登录");
			}

		}
		return false;
	}

	@Override
	public Object profile(String openid, String openkey) {

		// URL
		String url = genUrl(this.profile, openid);

		// BODY
		StringBuilder sb = new StringBuilder(200);
		sb.append("{\"appid\":").append(this.appid).append(",");
		sb.append("\"accessToken\":\"").append(openkey).append("\",");
		sb.append("\"openid\":\"").append(openid).append("\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			if (log.isDebugEnabled()) {
				log.debug("profile:{}" + result);
			}
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				Quser user = new Quser();
				user.setNickname(jsonObj.path("nickName").getTextValue());
				user.setGender(jsonObj.path("gender").getTextValue());
				user.setPicture40(jsonObj.path("picture40").getTextValue());
				user.setPicture100(jsonObj.path("picture100").getTextValue());
				user.setIs_yellow_vip(String.valueOf(jsonObj.path("yellow_vip")
						.getIntValue()));
				user.setYellow_vip_level(String.valueOf(jsonObj.path(
						"yellow_vip_level").getIntValue()));
				user.setIs_yellow_year_vip(String.valueOf(jsonObj.path(
						"yellow_year_vip").getIntValue()));
				return user;
				// is_lost not use
			} else {
				log.error("qqprofile:ret={},msg={}", ret, jsonObj.path("msg")
						.getValueAsText());
				throw new BaseException("取QQ用户信息出错，请重新登录");
			}

		}
		return null;
	}

	@Override
	public String loadVip(String openid, String openkey, Quser user) {
		// URL
		String url = genUrl(this.loadVip, openid);

		// BODY
		StringBuilder sb = new StringBuilder(200);
		sb.append("{\"appid\":").append(this.appid).append(",");
		sb.append("\"login\":2,");
		sb.append("\"uin\":0,");
		sb.append("\"openid\":\"").append(openid).append("\",");
		sb.append("\"vip\":29}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			if (log.isDebugEnabled()) {
				log.debug("vip:{}" + result);
			}
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				if (user != null) {
					Iterator<JsonNode> vips = jsonObj.path("lists")
							.getElements();
					while (vips.hasNext()) {
						JsonNode vip = vips.next();
						int vipType = vip.path("flag").getIntValue();
						if (vipType == 1) {// 会员
							user.setIs_vip(String.valueOf(vip.path("isvip")
									.getIntValue()));
							user.setIs_year_vip(String.valueOf(vip.path("year")
									.getIntValue()));
							user.setQq_vip_level(String.valueOf(vip.path(
									"level").getIntValue()));
						} else if (vipType == 4) {// 蓝钻
							user.setIs_blue_vip(String.valueOf(vip
									.path("isvip").getIntValue()));
							user.setIs_blue_year_vip(String.valueOf(vip.path(
									"year").getIntValue()));
							user.setBlue_vip_level(String.valueOf(vip.path(
									"level").getIntValue()));
							user.setIs_high_blue(String.valueOf(vip.path(
									"luxury").getIntValue()));
						} else if (vipType == 8) {// 红钻
							user.setIs_red_vip(String.valueOf(vip.path("isvip")
									.getIntValue()));
							user.setIs_red_year_vip(String.valueOf(vip.path(
									"year").getIntValue()));
							user.setRed_vip_level(String.valueOf(vip.path(
									"level").getIntValue()));
							user.setIs_high_red(String.valueOf(vip.path(
									"luxury").getIntValue()));
						} else if (vipType == 16) {// 超级会员
							user.setIs_super_vip(String.valueOf(vip.path(
									"isvip").getIntValue()));
						}
//						user.setIs_super_vip("1");
//						user.setIs_vip("1");
//						user.setIs_year_vip("1");
//						user.setQq_vip_level("1");						
					}
				}
				return result;
				// is_lost not use
			} else {
				String err = jsonObj.path("msg").getValueAsText();
				log.error("loadVip:ret={},msg={}", ret, err);
				throw new BaseException("取QQ用户VIP出错，ret=" + ret + ",err=" + err);
			}

		}
		return null;
	}

	@Override
	public List<Map<String, Object>> qqFriends(String openid, String openkey,
			int flag) {
		// URL
		String url = genUrl(this.friends, openid);

		// BODY
		StringBuilder sb = new StringBuilder(200);
		sb.append("{\"appid\":").append(this.appid).append(",");
		sb.append("\"accessToken\":\"").append(openkey).append("\",");
		sb.append("\"openid\":\"").append(openid).append("\",");
		sb.append("\"flag\":").append(flag).append("}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				Iterator<JsonNode> lit = jsonObj.path("lists").getElements();
				while (lit.hasNext()) {
					JsonNode node = lit.next();
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("openid", node.path("openid").getTextValue());
					data.put("nickName", node.path("nickName").getTextValue());
					data.put("gender", node.path("gender").getTextValue());
					data
							.put("figureurl", node.path("figureurl")
									.getTextValue());
					datas.add(data);
				}

				return datas;
				// is_lost not use
			} else {
				String err = jsonObj.path("msg").getValueAsText();
				log.error("qqFriends:ret={},msg={}", ret, err);
				throw new BaseException("取QQ好友出错，ret=" + ret + ",err=" + err);
			}

		}
		return null;
	}

	private String genUrl(String url, String openid) {
		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder sb = new StringBuilder(256);
		sb.append(url);
		sb.append("?timestamp=").append(time);
		sb.append("&appid=").append(this.appid);
		sb.append("&sig=").append(MD5.getMD5(this.appkey + time));
		sb.append("&openid=").append(openid);
		sb.append("&encode=1");
		return sb.toString();
	}

	private String httpPost(String url, String body) {
		if (log.isDebugEnabled()) {
			log.debug("url:{},body:{}", url, body);
		}
		HttpClient httpClient = new HttpClient();

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				CONNECTION_TIMEOUT);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(
				READ_DATA_TIMEOUT);

		PostMethod postMethod = new PostMethod(url);
		// 设置User-Agent
		postMethod.setRequestHeader("User-Agent", "Java OpenApiV3 SDK Client");

		// 设置编码
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);

		try {
			RequestEntity entity = new StringRequestEntity(body,
					"application/x-www-form-urlencoded", CONTENT_CHARSET);
			postMethod.setRequestEntity(entity);

			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("request url:{},data{}", url, body);
				throw new BaseException("Request [" + url + "] failed:"
						+ postMethod.getStatusLine());
			}

			// 读取内容
			byte[] responseBody = postMethod.getResponseBody();

			return new String(responseBody, CONTENT_CHARSET);

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
			postMethod.releaseConnection();
		}

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
					log.debug("MDK:return={},url={}", result, url);
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

	/**
	 * 
	 * appid string 用户在应用的唯一标识 openid string 玩家在当前应用的唯一标识 accessToken string
	 * 用户在应用中的登录凭据 fopenids vector<string> 好友openid列表，每次最多可输入50个 flags string
	 * VIP业务查询标识
	 * 。目前只支持查询QQ会员信息：qq_vip。后期会支持更多业务的用户VIP信息查询。如果要查询多种VIP业务，通过“,”分隔。如果不输入该值
	 * ，默认为全部查询。 userip string 调用方ip信息 pf string
	 * 玩家登录平台，默认openmobile，有openmobile_android
	 * /openmobile_ios/openmobile_wp等，该值来自客户端手Q登录返回
	 * 
	 * 
	 * @param openid
	 * @param openkey
	 * @param flag
	 * @return
	 */
	@Override
	public List<QQFriendsVipInfo> qqfriendsVip(String openid, String openkey,
			String userip, String pf, List<String> friendIds) {
		// URL 头
		String url = genUrl(this.qqfriendsVip, openid);
		// BODY ["1","2"]
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("appid", appid);
		body.put("accessToken", openkey);
		body.put("openid", openid);
		body.put("userip", userip);
		body.put("fopenids", friendIds);
		body.put("pf", pf);
		body.put("flags", "qq_vip");

		String result = httpPost(url, JSONUtil.toJson(body));
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {

				List<QQFriendsVipInfo> datas = new ArrayList<QQFriendsVipInfo>();
				Iterator<JsonNode> lit = jsonObj.path("lists").getElements();
				while (lit.hasNext()) {
					JsonNode node = lit.next();
					QQFriendsVipInfo vipInfo = new QQFriendsVipInfo(node.path(
							"openid").getTextValue(), node.path("is_qq_vip")
							.getIntValue(), node.path("qq_vip_level")
							.getIntValue(), node.path("is_qq_year_vip")
							.getIntValue());
					datas.add(vipInfo);
				}

				return datas;
				// is_lost not use
			} else {
				log.error("qqfriendsVip:ret={},msg={}", ret, jsonObj
						.path("msg").getValueAsText());
				throw new BaseException("取QQvip信息错误，请重新登录");
			}

		}
		return null;
	}

	@Override
	public boolean qqscoreBatch(String openid, String openkey,
			List<QQScoreReport> reportList) {
		// URL 头
		String url = genUrl(this.qqscoreBatch, openid);
		// BODY ["1","2"]
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("appid", appid);
		body.put("accessToken", openkey);
		body.put("openid", openid);
		body.put("param", reportList);

		String result = httpPost(url, JSONUtil.toJson(body));
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {

				return true;
			} else {
				log.error("qqscoreBatch:ret={},msg={}", ret, jsonObj
						.path("msg").getValueAsText());
				throw new BaseException("批量汇报得分情况失败");
			}

		}
		return false;
	}

	// 这个方法没测试
	@Override
	public boolean verifyVistorLogin(String openid, String openkey,
			String userip) {

		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder sb = new StringBuilder(256);
		sb.append(vistorVerfyUrl);
		sb.append("?timestamp=").append(time);
		sb.append("&appid=G_").append(this.appid);
		sb.append("&sig=").append(MD5.getMD5(this.appkey + time));
		sb.append("&openid=").append(openid);
		sb.append("&encode=1");
		String url = sb.toString();

		// BODY
		sb = new StringBuilder(200);
		sb.append("{\"accessToken\":\"").append(openkey).append("\",");
		// sb.append("\"openid\":\"").append(openid).append("\",");
		// sb.append("\"openkey\":\"").append(openkey).append("\",");
		sb.append("\"guestid\":\"").append(openid).append("\"}");
		// sb.append("\"appid\":\"G_").append(this.appid).append("\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				return true;
			} else {
				log.error("verfy login error:ret={},msg={}", ret, jsonObj.path(
						"msg").getValueAsText());
				throw new BaseException(IAMF3Action.CMD_PAY_ERROR,
						"校验用户登录态错误，请重新登录");
			}

		}
		return false;
	}

	@Override
	public void share(String openid, String openkey, String targetOpenid,
			String title, String targetUrl, String previewText, String summary,
			String imageUrl, String gameTag, int act) {
		String url = genUrl(this.shareUrl, openid);
		// BODY
		StringBuilder sb = new StringBuilder(512);
		sb.append("{");
		sb.append("\"act\":").append(act).append(",");
		sb.append("\"oauth_consumer_key\":").append(this.appid).append(",");
		sb.append("\"dst\":0,");
		sb.append("\"flag\":1,");
		if (imageUrl == null) {
			sb.append("\"image_url\":\"\",");
		} else {
			sb.append("\"image_url\":\"").append(imageUrl).append("\",");
		}

		sb.append("\"openid\":\"").append(openid).append("\",");
		sb.append("\"access_token\":\"").append(openkey).append("\",");
		sb.append("\"src\":0,");
		if (summary == null) {
			sb.append("\"summary\":\"\",");
		} else {
			sb.append("\"summary\":\"").append(summary).append("\",");
		}

		if (targetUrl == null) {
			sb.append("\"target_url\":\"\",");
		} else {
			sb.append("\"target_url\":\"").append(targetUrl).append("\",");
		}

		if (title == null) {
			sb.append("\"title\":\"\",");
		} else {
			sb.append("\"title\":\"").append(title).append("\",");
		}

		sb.append("\"fopenids\":[{\"openid\":\"").append(targetOpenid).append(
				"\",\"type\":0}],");

		sb.append("\"appid\":").append(this.appid).append(",");

		if (previewText != null) {
			sb.append("\"previewText\":\"").append(previewText).append("\",");
		}

		if (gameTag != null) {
			sb.append("\"game_tag\":\"").append(gameTag).append("\"");
		}
		sb.append("}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret != 0) {
				String msg = jsonObj.path("msg").getValueAsText();
				log.error("分享错误:ret={},msg={}", ret, msg);
				throw new BaseException("分享错误:" + ret + "," + msg);
			}
		}
	}

	public static void main(String[] args) {
		List<String> sss = new ArrayList<String>();
		sss.add("1");
		sss.add("2");
		System.out.println(JSONUtil.toJson(sss));

		TecentMobileQqService t = new TecentMobileQqService();
		t.init();
		// 测试nginx代理
		// String url = "http://182.254.178.81/idip/portal.htm";
		// System.out.println(t.httpPost(url, "{\"a\":1,\"AreaId\" : 7 }"));

		// boolean succ = t.verifyLogin("A4F911A869350A20F505F1D107FA0BBC",
		// "6AA96606BAC13E4CD4539A7CC2965B4D", null);
		//
		// Object user = t.profile("A4F911A869350A20F505F1D107FA0BBC",
		// "6AA96606BAC13E4CD4539A7CC2965B4D");
		//
		// String result = t.loadVip("A4F911A869350A20F505F1D107FA0BBC",
		// "6AA96606BAC13E4CD4539A7CC2965B4D", null);
		// List<String> friendIds = new ArrayList<String>();
		// friendIds.add("69FF99F3B17436F2F6621FA158B30549");
		// t.qqfriendsVip("A3284A812ECA15269F85AE1C2D94EB37",
		// "964EE8FACFA24AE88AEEEEBD84028E19", "127.0.0.1", "openmobile",
		// friendIds);

		boolean succ = t.verifyVistorLogin("G_F911A869350A20F505F1D107FA0BBC",
				"6AA96606BAC13E4CD4539A7CC2965B4D", null);
		System.out.println("====");
	}

}
