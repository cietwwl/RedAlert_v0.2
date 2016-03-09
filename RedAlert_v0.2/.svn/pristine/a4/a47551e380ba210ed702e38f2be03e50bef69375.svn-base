package com.youxigu.dynasty2.core.flex.amf;

import java.util.Map;

import com.youxigu.dynasty2.core.flex.ActionDefine;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.wolf.net.UserSession;

/**
 * 协议上报--开放平台安全需要
 * 
 * 参考:<br>
 * /doc/协议上报/腾讯开放平台业务安全_数据上报字段说明1.6.6.pdf<br>
 * /doc/协议上报/DC_API_manual_Java_V1.8.pdf<br>
 * 
 * @author Administrator
 * 
 */
public interface IProtoReportService {

	/**
	 * 
	 * 上报<腾讯开放平台业务安全_数据上报字段说明1.6.6.pdf>中的 6.20章节的 RPGProtoRP：RPGRPG 游戏原始协议
	 * 游戏原始协议 游戏原始协议
	 * 
	 * @param cmd
	 *            协议命令号
	 * @param session
	 *            玩家session
	 * @param fail
	 *            协议是否失败
	 * @param params
	 *            传入其他参数
	 */
	void report(ActionDefine action , UserSession session, boolean fail,
			Map<String, Object> params);

	/**
	 * 
	 * 上报<腾讯开放平台业务安全_数据上报字段说明1.6.6.pdf>中的 6.1章节的Login协议
	 * 
	 * @param userId
	 * @param account
	 * @param user
	 * @param params传入其他参数
	 */
	void login(UserSession session,Account account, User user, Map<String, Object> params);
}
