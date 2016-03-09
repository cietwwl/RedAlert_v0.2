package com.youxigu.dynasty2.openapi.service;

import java.util.List;
import java.util.Map;

/**
 * Tecent MSDK API 包装类 微信类
 * 
 * @author Administrator
 * 
 */
public interface ITecentWeiXinService {
	/**
	 * 微信检验授权凭证(access_token)是否有效。
	 * url中带上msdkExtInfo=xxx（请求序列号），可以在后回内容中，将msdkExtInfo原数据带回来
	 * ，即可实现纯异常请求。msdkExtInfo为可选参数。
	 * 
	 * @param openid
	 * @param accessToken
	 * @return
	 */
	boolean checkToken(String openid, String accessToken);

	/**
	 * 由于access_token拥有较短的有效期(2小时)，当access_token超时后，可以使用refresh_token进行刷新，
	 * refresh_token拥有较长的有效期30天，refresh_token失效后，需要重新登录。
	 * url中带上msdkExtInfo=xxx（请求序列号
	 * ），可以在后回内容中，将msdkExtInfo原数据带回来，即可实现纯异常请求。msdkExtInfo为可选参数。
	 * 
	 * @param openid
	 * @param refresh_token
	 * @return
	 */
	Map<String, Object> refreshToken(String openid, String refresh_token);

	/**
	 * 获取同玩好友的详细信息
	 * 
	 * @param openid
	 * @param openids
	 * @param accessToken
	 * @return
	 */
	Map<String, Object> friendsProfile(String openid,
			String openkey);

	/**
	 * 获取同玩好友的openid
	 * 
	 * @param openid
	 * @param accessToken
	 * @return
	 */
	List<String> friendIds(String openid, String accessToken);

	/**
	 * 取得自己的profile
	 * 
	 * @param openid
	 * @param accessToken
	 * @return
	 */
	Map<String, Object> userInfo(String openid, String accessToken);

	/**
	 * 获取他人用户帐号基本信息
	 * 
	 * @param openids
	 * @param accessToken
	 * @return
	 */
	Map<String, Object> profile(String openid, List<String> openids,
			String accessToken);

	/**
	 * 上报玩家成就到微信平台，在微信游戏中心显示好友排行（立即生效）
	 * 
	 * @param openId
	 * @param score
	 *            分数值
	 * @param expires
	 *            超时时间，unix时间戳，0时标识永不超时
	 */
	void wxScore(String openid, String score, long expires);

	boolean verifyVistorLogin(String openid, String openkey, String userip);

	/**
	 * openid string 用户在微信平台的标识<br/>
	 * fopenid string 分享到的好友openid<br/>
	 * access_token string 调用接口凭证<br/>
	 * extinfo string 第三方程序自定义简单数据，微信会回传给第三方程序处理，长度限制2k, 客户端点击的时候可以获取到这个字段。<br/>
	 * title string 应用消息标题<br/>
	 * description string 应用消息描述<br/>
	 * media_tag_name string 区分游戏消息类型，用于数据统计<br/>
	 * thumb_media_id（可选参数） string
	 * 通过/share/upload_wx接口获取（如无thumb_media_id该参数置为空）<br/>
	 * 
	 * @param openid
	 * @param openkey
	 * @param fopneId
	 * @param title
	 * @param description
	 * @param mediaTagName
	 * @param extinfo
	 */
	void share_wx(String openid, String openkey, String fopenid, String title,
			String description, String mediaTagName, String extinfo);

	
	/**
	 * 
	 * @param openid
	 * @param openkey
	 * @param optType
	 * @return
	 */
	Map<String,Object> getVip(String openid, String openkey);
	/**
	 * 取得同玩好友的vip
	 * @param openid
	 * @param openkey
	 * @return
	 */
	Map<String,Object> getVips(String openid, String openkey);	
}
