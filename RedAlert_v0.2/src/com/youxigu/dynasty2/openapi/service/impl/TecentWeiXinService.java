package com.youxigu.dynasty2.openapi.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
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
import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.openapi.service.ITecentWeiXinService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MD5;

public class TecentWeiXinService implements ITecentWeiXinService {
	public static final Logger log = LoggerFactory
			.getLogger(TecentWeiXinService.class);
	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 3000;

	private String appid = "1000001640";
	private String appkey = "B0CpjjRg6Zt9eVl6";

	private String verifyLoginUrl = "http://msdktest.qq.com/auth/check_token";
	private String refreshTokenUrl = "http://msdktest.qq.com/auth/refresh_token";
	private String profile = "http://msdktest.qq.com/relation/wxprofile";
	private String friendIds = "http://msdktest.qq.com/relation/wxfriends";
	private String friendsProfile = "http://msdktest.qq.com/relation/wxfriends_profile";
	private String wxscore = "http://msdktest.qq.com/relation/profile/wxscore";
	private String wxuserinfo = "http://msdktest.qq.com/relation/wxuserinfo";
	private String vistorVerfyUrl = "http://msdktest.qq.com/profile//auth/guest_check_token";
	private String shareUrl = "http://msdktest.qq.com/share/wx";
	private String vipUrl = "http://api.weixin.qq.com/game/getvip";

	public void setVipUrl(String vipUrl) {
		this.vipUrl = vipUrl;
	}

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

	public void setRefreshTokenUrl(String refreshTokenUrl) {
		this.refreshTokenUrl = refreshTokenUrl;
	}

	public void setFriendIds(String friendIds) {
		this.friendIds = friendIds;
	}

	public void setFriendsProfile(String friendsProfile) {
		this.friendsProfile = friendsProfile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setWxscore(String wxscore) {
		this.wxscore = wxscore;
	}

	public void setWxuserinfo(String wxuserinfo) {
		this.wxuserinfo = wxuserinfo;
	}

	public void init() {
	}

	@Override
	public boolean checkToken(String openid, String accessToken) {
		String url = genUrl(this.verifyLoginUrl, openid);

		// BODY
		StringBuilder sb = new StringBuilder(200);
		sb.append("{\"accessToken\":\"").append(accessToken).append("\",");
		sb.append("\"openid\":\"").append(openid).append("\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				return true;
			} else {
				log.error("checkToken error:ret={},msg={}", ret, jsonObj.path(
						"msg").getValueAsText());
				throw new BaseException(-20000, "校验微信用户登录态错误，请重新登录");
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> refreshToken(String openid, String refresh_token) {
		String url = genUrl(this.refreshTokenUrl, openid);

		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"appid\":\"").append(this.appid).append("\",");
		sb.append("\"refreshToken\":\"").append(refresh_token).append("\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				Map<String, Object> datas = new HashMap<String, Object>(5);
				datas.put("accessToken", jsonObj.path("accessToken")
						.getTextValue());
				datas.put("expiresIn", jsonObj.path("accessToken")
						.getIntValue());
				datas.put("refreshToken", jsonObj.path("refreshToken")
						.getTextValue());
				datas.put("openid", jsonObj.path("openid").getTextValue());
				datas.put("scope", jsonObj.path("scope").getTextValue());
				return datas;
			} else {
				log.error("refreshToken error:ret={},msg={}", ret, jsonObj
						.path("msg").getValueAsText());
				throw new BaseException("刷新微信用户授权凭证出错，请重新登录");
			}
		}

		return null;
	}

	@Override
	public Map<String, Object> friendsProfile(String openid, String openkey) {
		// URL
		String url = genUrl(this.friendsProfile, openid);
		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"accessToken\":\"").append(openkey).append("\",");
		sb.append("\"openid\":\"").append(openid).append("\"}");
		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {

				Map<String, Object> reuData = new HashMap<String, Object>(4);

				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				Iterator<JsonNode> lit = jsonObj.path("lists").getElements();
				while (lit.hasNext()) {
					JsonNode node = lit.next();
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("nickName", node.path("nickName").getTextValue());
					data.put("sex", node.path("sex").getIntValue());
					data.put("picture", node.path("picture").getTextValue());
					data.put("provice", node.path("provice").getTextValue());
					data.put("city", node.path("city").getTextValue());
					data.put("openid", node.path("openid").getTextValue());
					datas.add(data);
				}

				List<String> privileges = new ArrayList<String>();
				lit = jsonObj.path("privilege").getElements();

				while (lit.hasNext()) {
					JsonNode node = lit.next();
					privileges.add(node.getTextValue());
				}

				reuData.put("country", jsonObj.path("country").getTextValue());
				reuData
						.put("language", jsonObj.path("language")
								.getTextValue());
				reuData.put("lists", datas);
				reuData.put("privilege", privileges);

				return reuData;

			} else {
				String err = jsonObj.path("msg").getValueAsText();
				log.error("friendsProfile:ret={},msg={}", ret, err);
				throw new BaseException("取同玩好友数据异常:ret=" + ret + ",err=" + err);
			}

		}
		return null;
	}

	@Override
	public List<String> friendIds(String openid, String accessToken) {
		// URL
		String url = genUrl(this.friendIds, openid);
		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"openid\":\"").append(openid).append("\",");
		sb.append("\"accessToken\":\"").append(accessToken).append("\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {
				List<String> datas = new ArrayList<String>();
				Iterator<JsonNode> lit = jsonObj.path("openids").getElements();
				while (lit.hasNext()) {
					JsonNode node = lit.next();
					datas.add(node.getTextValue());
				}
				return datas;
			} else {
				log.error("friendIds error:ret={},msg={}", ret, jsonObj.path(
						"msg").getValueAsText());
				throw new BaseException("取同玩好友数据异常");
			}
		}

		return null;
	}

	@Override
	public Map<String, Object> userInfo(String openid, String accessToken) {
		// URL
		String url = genUrl(this.wxuserinfo, openid);

		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"openid\":\"").append(openid).append("\",");
		sb.append("\"appid\":\"").append(this.appid).append("\",");
		sb.append("\"accessToken\":\"").append(accessToken).append("\"}");
		String result = httpPost(url, sb.toString());

		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("nickname", jsonObj.path("nickname").getTextValue());
				data.put("picture", jsonObj.path("picture").getTextValue());
				data.put("province", jsonObj.path("province").getTextValue());
				data.put("city", jsonObj.path("city").getTextValue());
				data.put("country", jsonObj.path("country").getTextValue());
				data.put("sex", jsonObj.path("sex").getIntValue());
				data.put("unionid", jsonObj.path("unionid").getTextValue());
				// data.put("privilege",
				// jsonObj.path("privilege").getTextValue());
				return data;
			} else {
				log.error("userInfo error:ret={},msg={}", ret, jsonObj.path(
						"msg").getValueAsText());
				throw new BaseException("取同玩好友数据异常");
			}
		}

		return null;
	}

	@Override
	public Map<String, Object> profile(String openid, List<String> openids,
			String accessToken) {

		// URL
		String url = genUrl(this.profile, openid);

		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"accessToken\":\"").append(accessToken).append("\",");
		int size = openids == null ? 0 : openids.size();
		if (size == 0) {
			sb.append("\"openids\":[\"").append(openid).append("\"]}");
		} else {
			sb.append("\"openids\":[");
			for (String oid : openids) {
				sb.append("\"").append(oid).append("\"");
				size--;
				if (size != 0) {
					sb.append(",");
				}
			}
			sb.append("]");
		}
		sb.append("}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret == 0) {

				Map<String, Object> reuData = new HashMap<String, Object>(4);

				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				Iterator<JsonNode> lit = jsonObj.path("lists").getElements();
				while (lit.hasNext()) {
					JsonNode node = lit.next();
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("nickName", node.path("nickName").getTextValue());
					data.put("sex", node.path("sex").getTextValue());
					data.put("picture", node.path("picture").getTextValue());
					data.put("provice", node.path("provice").getTextValue());
					data.put("city", node.path("city").getTextValue());
					data.put("openid", node.path("openid").getTextValue());
					data.put("country", node.path("country").getTextValue());
					data.put("language", node.path("language").getTextValue());
					datas.add(data);
				}

				List<String> privileges = new ArrayList<String>();
				lit = jsonObj.path("privilege").getElements();

				while (lit.hasNext()) {
					JsonNode node = lit.next();
					privileges.add(node.getTextValue());
				}

				reuData.put("lists", datas);
				reuData.put("privilege", privileges);

				return reuData;

			} else {
				String msg = jsonObj.path("msg").getValueAsText();
				log.error("wxprofile:ret={},msg={}", ret, msg);
				throw new BaseException("拉取用户数据异常,code:" + ret + ",msg:" + msg);
			}

		}
		return null;
	}

	@Override
	public void wxScore(String openid, String score, long expires) {
		// URL
		String url = genUrl(this.wxscore, openid);

		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"appid\":\"").append(this.appid).append("\",");
		sb.append("\"grantType\":\"client_credential\",");
		sb.append("\"openid\":\"").append(openid).append("\",");
		sb.append("\"score\":\"").append(score).append("\",");
		sb.append("\"expires\":\"").append(String.valueOf(expires)).append(
				"\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret != 0) {
				log.error("qqprofile:ret={},msg={}", ret, jsonObj.path("msg")
						.getValueAsText());
				throw new BaseException("上报成就异常");
			}
		}

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

	// 这个方法没测试
	@Override
	public boolean verifyVistorLogin(String openid, String openkey,
			String userip) {

		String time = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder sb = new StringBuilder(256);
		sb.append(vistorVerfyUrl);
		sb.append("?timestamp=").append(time);
		sb.append("&appid=G_").append(Constant.APP_ID_QQ);// this.appid
		sb.append("&sig=").append(MD5.getMD5(Constant.APP_KEY_QQ + time));// this.appkey
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
	public void share_wx(String openid, String openkey, String fopenid,
			String title, String description, String mediaTagName,
			String extinfo) {
		// URL
		String url = genUrl(this.shareUrl, openid);

		// BODY
		StringBuilder sb = new StringBuilder(100);
		sb.append("{\"openid\":\"").append(openid).append("\",");
		sb.append("\"fopenid\":\"").append(fopenid).append("\",");
		sb.append("\"access_token\":\"").append(openkey).append("\",");
		if (extinfo == null) {
			sb.append("\"extinfo\":\"\",");
		} else {
			sb.append("\"extinfo\":\"").append(extinfo).append("\",");
		}
		sb.append("\"title\":\"").append(title).append("\",");
		sb.append("\"description\":\"").append(description).append("\",");
		if (mediaTagName == null) {
			sb.append("\"media_tag_name\":\"\",");
		} else {
			sb.append("\"media_tag_name\":\"").append(mediaTagName).append(
					"\",");
		}

		sb.append("\"thumb_media_id\":\"\"}");

		String result = httpPost(url, sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret != 0) {
				String err = jsonObj.path("msg").getValueAsText();
				log.error("share_wx:ret={},msg={}", ret, err);
				throw new BaseException("微信分享出错,ret=" + ret + ",err=" + err);
			}
		}

	}

	@Override
	public Map<String, Object> getVip(String openid, String openkey) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(vipUrl);
		sb.append("?openid=").append(openid);
		sb.append("&access_token=").append(openkey);
		try {
			sb.append("&j=").append(
					URLEncoder.encode("{\"optype\"=0}", "utf-8"));
		} catch (Exception e) {
			log.error("===", e);
		}

		String result = httpGet(sb.toString());
		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("errcode").getIntValue();
			if (ret != 0) {
				String err = jsonObj.path("errmsg").getValueAsText();
				log.error("getVipError:errcode={},errmsg={}", ret, err);
				throw new BaseException("取VIP数据出错:" + ret + "," + err);
			}
			jsonObj = jsonObj.path("data");
			if (jsonObj != null) {
				Map<String, Object> params = new HashMap<String, Object>(2);
				Iterator<JsonNode> lit = jsonObj.path("vipinfo").getElements();
				while (lit.hasNext()) {
					JsonNode node = lit.next();

					params.put("level", node.path("level").getIntValue());
					params.put("score", node.path("score").getIntValue());
					params.put("nick", node.path("nick").getTextValue());
					params
							.put("logo_url", node.path("logo_url")
									.getTextValue());
					params.put("logo_faceurl", node.path("logo_faceurl")
							.getTextValue());
					// 只取第一条
					break;
				}
				return params;
			}

		}
		return null;
	}

	@Override
	public Map<String, Object> getVips(String openid, String openkey) {
		// 跟getVip一样的url.只是参数不同
		throw new BaseException("do it later.......");
		// return null;
	}

	public static void main(String[] args) {
		TecentMobileQqService t = new TecentMobileQqService();
		t.init();
		boolean succ = t.verifyLogin("A4F911A869350A20F505F1D107FA0BBC",
				"6AA96606BAC13E4CD4539A7CC2965B4D", null);

		Object user = t.profile("A4F911A869350A20F505F1D107FA0BBC",
				"6AA96606BAC13E4CD4539A7CC2965B4D");

		String result = t.loadVip("A4F911A869350A20F505F1D107FA0BBC",
				"6AA96606BAC13E4CD4539A7CC2965B4D", null);
		System.out.println("====");
	}

}
