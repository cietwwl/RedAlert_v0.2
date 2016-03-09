package com.youxigu.dynasty2.develop.service;

import com.youxigu.dynasty2.develop.enumer.SpeedType;
import com.youxigu.dynasty2.user.domain.User;

/**
 * 加速操作的service
 * 
 * @author fengfeng
 *
 */
public interface ISpeedService {

	/**
	 * 加速。。次方法只负责扣除钻石或者道具。然后返回加速了多少时间。具体的操作需要外面实现
	 * 
	 * @param speedType
	 *            加速类型
	 * @param sppedTime
	 *            需要加速的时间 单位秒
	 * @param diamond
	 *            true表示使用钻石加速，false表示道具加速
	 * @param entId
	 *            如果是道具加速则传入道具id
	 * @param num
	 *            道具数量
	 * @return 返回加速的时间。。单位秒
	 */
	public int doSpeedUp(long userId, SpeedType speedType, int speedTime,
			boolean diamond, int entId, int num);

	public int doSpeedUp(User user, SpeedType speedType, int speedTime,
			boolean diamond, int entId, int num);

}
