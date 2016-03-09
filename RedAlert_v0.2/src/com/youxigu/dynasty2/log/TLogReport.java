package com.youxigu.dynasty2.log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.core.JSONUtil;
import com.youxigu.dynasty2.openapi.ErrorCode;
import com.youxigu.dynasty2.util.BaseException;
import com.youxigu.dynasty2.util.MD5;

public class TLogReport implements ITlogDemoDao {
	public static final Logger log = LoggerFactory.getLogger(TLogReport.class);
	// 编码方式
	private static final String CONTENT_CHARSET = "UTF-8";

	// 连接超时时间
	private static final int CONNECTION_TIMEOUT = 3000;

	// 读数据超时时间
	private static final int READ_DATA_TIMEOUT = 3000;
	// 上报服务器的Name
	private String tlogUrl = "http://qx2sqlapi.app100667751.twsapp.com/index.php";
	private String tlogKey = "qx2gb8u4dg";

	private HttpClient httpClient = null;

	public void init() {

		httpClient = new HttpClient();

		// 设置建立连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				CONNECTION_TIMEOUT);

		// 设置读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(
				READ_DATA_TIMEOUT);
	}

	public void setTlogUrl(String tlogUrl) {
		this.tlogUrl = tlogUrl;
	}

	public void setTlogKey(String tlogKey) {
		this.tlogKey = tlogKey;
	}

	private String getURL(int sid, String t, String d, String key) {
		StringBuffer url = new StringBuffer();
		try {
			url.append(tlogUrl).append("?sid=").append(sid).append("&t=")
					.append(t).append("&d=").append(
							URLEncoder.encode(d, "UTF-8")).append("&key=")
					.append(key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url.toString();
	}

	/**
	 * 向经分系统里写日志
	 * 
	 * @param sid
	 *            服务器Id
	 * @param sqlId
	 *            表名
	 * @param datas
	 *            数据
	 * @return
	 * @throws TLogException
	 */
	public void insertTlog(int sid, String sqlId, Map<String, Object> datas) {
		String d = JSONUtil.toJson(datas);
		String key = MD5.getMD5(sid + sqlId + d + tlogKey);
		String url = getURL(sid, sqlId, d, key);
		if (log.isDebugEnabled()){
			log.debug("{}={}",sqlId,datas);
		}
		GetMethod getMethod = new GetMethod(url);

		// 设置User-Agent
		getMethod.setRequestHeader("User-Agent", "Java OpenApiV3 SDK Client");

		// 设置编码
		getMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, CONTENT_CHARSET);

		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		try {

			int statusCode = httpClient.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				throw new BaseException(ErrorCode.NETWORK_ERROR, "Request ["
						+ url + "] failed:" + getMethod.getStatusLine());
			}
			// 读取内容
			String result = getMethod.getResponseBodyAsString();
			if (result != null && result.length() > 0) {
				log.warn("tlogerror,return={},url={}", result, url);
			}

		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			throw new BaseException(ErrorCode.NETWORK_ERROR, "Request [" + url
					+ "] failed:" + e.getMessage());
		} catch (IOException e) {
			// 发生网络异常
			throw new BaseException(ErrorCode.NETWORK_ERROR, "Request [" + url
					+ "] failed:" + e.getMessage());
		} finally {
			// 释放链接
			getMethod.releaseConnection();
		}
	}

}
