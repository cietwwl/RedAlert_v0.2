package com.youxigu.dynasty2.vip.service;

import com.youxigu.dynasty2.user.domain.User;
import com.youxigu.dynasty2.vip.domain.UserVip;

/**
 * vip服务接口
 * 
 * @author ninglong
 *
 */
public interface IVipService {

	/**
	 * <p>
	 * 根据用户id取出vip等级 已考虑vip过期情况
	 * </p>
	 * 
	 * @param userId
	 * @return
	 */
	int getVipLvByUserId(long userId);

	/**
	 * <p>
	 * 根据用户id取出vip信息
	 * </p>
	 * 
	 * @param userId
	 * @return
	 */
	UserVip getUserVip(long userId);

	/**
	 * @author ninglong
	 *         <p>
	 *         更新用户vip信息未考虑升级和降情况
	 *         </p>
	 * @param userVip
	 */
	void doUpdateUserVip(UserVip userVip);

	/**
	 * @author ninglong
	 *         <p>
	 *         根据用户Id修改用户vip成长值
	 *         </p>
	 * @param userId
	 * @param point
	 */
	void doUpdateUserVip(User user, int point);

	/**
	 * <p>
	 * 创建vip
	 * </p>
	 * 
	 * @param user
	 * @param point
	 * @param days
	 */
	void doCreateUserVip(long userId);

	/**
	 * <p>
	 * 推送刷新vip消息
	 * </p>
	 * 
	 * @param userId
	 * @param userVip
	 */
	void sendFlushDevDataEvent(long userId, UserVip userVip);

	/**
	 * <p>
	 * 是否是vip
	 * </p>
	 * 
	 * @param userId
	 */
	boolean isVip(long userId);

}
