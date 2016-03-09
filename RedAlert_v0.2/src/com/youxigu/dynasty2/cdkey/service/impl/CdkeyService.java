package com.youxigu.dynasty2.cdkey.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
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

import com.youxigu.dynasty2.cdkey.dao.ICdkeyDao;
import com.youxigu.dynasty2.cdkey.domain.UserCdkey;
import com.youxigu.dynasty2.cdkey.service.ICdkeyService;
import com.youxigu.dynasty2.common.AppConstants;
import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.entity.domain.Entity;
import com.youxigu.dynasty2.entity.domain.Item;
import com.youxigu.dynasty2.entity.service.IEntityService;
import com.youxigu.dynasty2.log.imp.LogItemAct;
import com.youxigu.dynasty2.treasury.service.ITreasuryService;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.StringUtils;

public class CdkeyService implements ICdkeyService {
	Logger log = LoggerFactory.getLogger(CdkeyService.class);
	private String url = "http://192.168.3.81:/cdkey/usecdkey.htm";
	private static final String CONTENT_CHARSET = "UTF-8";
	private ICdkeyDao cdkeyDao;
	private IEntityService entityService;
	private ITreasuryService treasuryService;

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCdkeyDao(ICdkeyDao cdkeyDao) {
		this.cdkeyDao = cdkeyDao;
	}

	public void setEntityService(IEntityService entityService) {
		this.entityService = entityService;
	}

	public void setTreasuryService(ITreasuryService treasuryService) {
		this.treasuryService = treasuryService;
	}

	@Override
	public void doUseCdKey(long userId, String openId, String areaId,
			String cdkey, Map<String, Object> params) {

		String type = cdkey.substring(0, 6);
		treasuryService.lockTreasury(userId);
		UserCdkey userCdkey = cdkeyDao.getUserCdKey(userId, type);
		if (userCdkey != null) {
			throw new BaseException("您已兑换过该礼包");
		}

		StringBuilder sb = new StringBuilder(this.url);
		sb.append("?cdkey=").append(cdkey);
		sb.append("&openId=").append(openId);
		sb.append("&areaId=").append(areaId);
		// 取cdkey;
		String result = httpGet(sb.toString());

		if (result != null) {
			JsonNode jsonObj = JSONUtil.getJsonNode(result);
			int ret = jsonObj.path("ret").getIntValue();
			if (ret != 0) {
				String err = jsonObj.path("msg").getValueAsText();
				log.error("使用cdkey错误:ret={},msg={}", ret, err);
				throw new BaseException(ret, err);
			}

			// 发道具:运营确定一定是道具
			String items = jsonObj.path("items").getValueAsText();
			if (items != null) {
				String[] itemArr = StringUtils.split(items, ";");
				for (String itemStr : itemArr) {
					String[] one = StringUtils.split(itemStr, ",");
					int itemId = Integer.parseInt(one[0]);
					int num = Integer.parseInt(one[1]);

					Item item = (Item) entityService.getEntity(itemId);
					if (item != null) {
						if ((itemId >= AppConstants.ENT_DYNAMIC_ID_MIN
								&& itemId <= AppConstants.ENT_DYNAMIC_ID_MAX && item
								.getItemType().isDropPack())
						/* || item.getChildType() == Item.ITEM_TYPE_BOX_BOX */) {
							params.put("userId", userId);
							params.put("iAction", LogItemAct.LOGITEMACT_172);
							params.put("num", num);
							entityService.doAction(item, Entity.ACTION_USE,
									params);
						} else {
							treasuryService.addItemToTreasury(userId, itemId,
									num, 1, 1, false, true,
									LogItemAct.LOGITEMACT_172);
						}
					} else {
						log.error("cdkey配置的礼包错误:{},{}", cdkey, itemId);
					}

				}
			}
			userCdkey = new UserCdkey();
			userCdkey.setUserId(userId);
			userCdkey.setCdkey(cdkey);
			userCdkey.setKeyType(type);
			userCdkey.setDttm(new Timestamp(System.currentTimeMillis()));
			cdkeyDao.insertUserCdKey(userCdkey);

		}

	}

	private String httpGet(String url) {
		HttpClient httpClient = new HttpClient();

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);

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
}
