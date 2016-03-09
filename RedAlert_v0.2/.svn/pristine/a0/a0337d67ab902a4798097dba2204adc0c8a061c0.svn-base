package com.youxigu.dynasty2.openapi.service;

import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.openapi.Quser;
import com.youxigu.dynasty2.openapi.domain.QQFriendsVipInfo;
import com.youxigu.dynasty2.openapi.domain.QQScoreReport;

/**
 * Tecent MSDK API 包装类 手Q类
 * 
 * @author Administrator
 * 
 */
public interface ITecentMobileQqService {
	/**
	 * 验证登录态是否有效，有效的话会自动续期。 /auth/verify_login
	 * 
	 * @param openid
	 * @param openkey
	 * @param userip
	 * @return
	 */
	boolean verifyLogin(String openid, String openkey, String userip);

	/**
	 * 获取用户帐号基本信息 /relation/qqprofile
	 * 
	 * @param openid
	 * @param openkey
	 * @return
	 */
	Object profile(String openid, String openkey);

	/**
	 * 取VIP信息 /profile/load_vip
	 * 
	 * @param openid
	 * @param openkey
	 * @return
	 */
	String loadVip(String openid, String openkey, Quser user);

	/**
	 * 获取QQ同玩好友详细的个人信息接口 /relation/qqfriends_detail
	 * 
	 * @param openid
	 * @param openkey
	 * @param flag
	 *            flag=1时，返回不包含自己在内的好友关系链; flag=2时，返回包含自己在内的好友关系链。其它值无效，使用当前逻辑
	 * @return openid /好友的openid<br/>
	 *         nickName //昵称(优先输出备注，无则输出昵称)<br/>
	 *         gender //性别，用户未填则默认返回男<br/>
	 *         figureurl //好友QQ头像URL<br/>
	 */
	List<Map<String, Object>> qqFriends(String openid, String openkey, int flag);

	/**
	 * 根据指定openId集合获得会员信息
	 * 
	 * 
	 * @param openid
	 * @param openkey
	 * @param flag
	 * @return
	 */
	List<QQFriendsVipInfo> qqfriendsVip(String openid, String openkey,
			String userip, String pf, List<String> friendIds);

	// TODO: share

	/**
	 * 上报玩家成就到QQ平台，在QQ游戏中心显示好友分数排行。（实时生效）
	 * 
	 * @param openid
	 * @param openkey
	 * @param reportList
	 * @return
	 */
	boolean qqscoreBatch(String openid, String openkey,
			List<QQScoreReport> reportList);

	// //////////游客的，
	boolean verifyVistorLogin(String openid, String openkey, String userip);

	/**
	 * 分享 2.2.1.2输入参数说明 参数名称 类型 描述 act int 跳转行为(0: URL跳转；1:APP跳转,默认:0)<br/>
	 * oauth_consumer_key int appid(应用在QQ平台的唯一id)<br/>
	 * dst int msf-手q(包括iphone, android qq等),目前只能填1001<br/>
	 * flag int 漫游 (0:是；1:否. 目前只能填1)<br/>
	 * image_url string 分享图片url (图片尺寸规格为128*128；需要保证网址可访问；且图片大小不能超过2M)<br/>
	 * openid string 用户标识<br/>
	 * access_token string 授权凭证<br/>
	 * src int 消息来源 (默认值:0)<br/>
	 * summary string 摘要，长度不超过45字节<br/>
	 * target_url string
	 * 游戏中心详情页的URL(http://gamecenter.qq.com/gcjump?appid={YOUR_APPID
	 * }&pf=invite&from=iphoneqq&plat=qq&originuin=111&ADTAG=gameobj.msg_invite
	 * )，长度不超过1024字节<br/>
	 * 
	 * title string 分享标题,长度不能超过45字节<br/>
	 * fopenids vector<jsonObject>或者json字符串(兼容) Json数组，数据格式为
	 * [{"openid":"","type":0}]，openid为好友openid，type固定传0 .只支持分享给一个好友<br/>
	 * appid int 应用在QQ平台的唯一id，同上oauth_consumer_key<br/>
	 * previewText string 非必填。分享的文字内容，可为空。如“我在天天连萌”，长度不能超过45字节<br/>
	 * game_tag string 非必填。game_tag
	 * 用于平台对分享类型的统计，比如送心分享、超越分享，该值由游戏制定并同步给手Q平台，目前的值有：<br/>
	 * MSG_INVITE //邀请<br/>
	 * MSG_FRIEND_EXCEED //超越炫耀<br/>
	 * MSG_HEART_SEND //送心<br/>
	 * MSG_SHARE_FRIEND_PVP //PVP对战<br/>
	 * （请注意输入参数的类型，参考1.5）<br/>
	 * 
	 * @param openid
	 * @param openkey
	 * @param targetOpenid
	 * @param title
	 * @param targetUrl
	 * @param previewText
	 * @param summary
	 * @param imageUrl
	 * @param gameTag
	 * @param act
	 */
	void share(String openid, String openkey, String targetOpenid,
			String title, String targetUrl, String previewText, String summary,
			String imageUrl, String gameTag, int act);
}
