package com.youxigu.dynasty2.activity.service.impl;

import com.youxigu.dynasty2.activity.domain.BaseReward;
import com.youxigu.dynasty2.activity.domain.OperateActivity;
import com.youxigu.dynasty2.activity.enums.ActivityType;
import com.youxigu.dynasty2.activity.proto.ActivityView;
import com.youxigu.dynasty2.activity.proto.ExtraActivityView;

/**
 * 文件名 ExtraProcessor.java
 *
 * 描 述 额外奖励活动加成
 *
 */
public class ExtraProcessor extends AOperateActivityProcessor {

	@Override
	protected ActivityView doGetActByIdImp(long userId, OperateActivity activity) {
		ExtraReward reward = (ExtraReward) activity.getReward();
		ExtraActivityView view = new ExtraActivityView(reward.effectId,
				reward.actDesc);
		return view;
	}

	@Override
	protected void doRewardImp(long userId, OperateActivity activity) {
		// throw new BaseException("不能进行领取操作");
	}

	@Override
	public int getNoticeType() {
		return 0;
	}

	@Override
	public ActivityType getProcessorKey() {
		return ActivityType.EXTRA_ACTIVITY_TYPE;
	}

	@Override
	public boolean hasReward(long userId, OperateActivity activity) {
		return false;
	}

	@Override
	public BaseReward parseReward(String reward, OperateActivity activity) {
		return new ExtraReward(reward);
	}

	/**
	 * 额外奖励活动加成 1-1,item1:count,item2,item3,item4;
	 * 
	 * @author fengfeng
	 *
	 */
	public static class ExtraReward extends BaseReward {
		private static final long serialVersionUID = -5485815520220089385L;
		public final int effectId;
		public final boolean isPercent;
		public final int effectVal;
		public final String actDesc;

		ExtraReward(String reward) {
			String[] arr = reward.split(":");
			effectId = Integer.parseInt(arr[0]);
			isPercent = arr[1].equals("1");
			effectVal = Integer.parseInt(arr[2]);
			actDesc = arr[3];
		}
	}

	@Override
	protected void doAutoRewardImp(OperateActivity activity) {
		System.out.println("ExtraProcessor.doAutoRewardImp");
	}
}
