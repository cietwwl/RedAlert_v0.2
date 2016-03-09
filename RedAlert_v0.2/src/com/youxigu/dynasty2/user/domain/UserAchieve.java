package com.youxigu.dynasty2.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 君主成就定义
 * 
 * @author Dagangzi
 *
 */
public class UserAchieve implements Serializable {
	public static final int TYPE_CASLV = 1;// 城池等级
	public static final int TYPE_COMBAT_BABEL = 2;// 重楼
	public static final int TYPE_COMBAT_FIGHT = 3;// 百战
	public static final int TYPE_COMBAT_PVP = 4;// 攻打其他玩家
	public static final int TYPE_VIP = 5;// VIP等级
	public static final int TYPE_USER = 6;// 君主等级
	public static final int TYPE_RISK = 7;// 冒险进度
	public static final int TYPE_OFFICAL = 8;// 国家官职
	public static final int TYPE_DONATIONMONEY = 9;// 公益捐款
	private int id;
	private long userId; // 君主ID
	private int type;// 大类
	private int achieveId; // 达到的最大成就ID
	private int entId;// 成就实体
	private int entNum;// 成就实体数量
	private Timestamp finishDttm; // 完成时间
	private transient boolean isAdd; // 是否已经加过了

	public UserAchieve() {
	}

	public UserAchieve(long userId, int type, int entId, int entNum) {
		this.userId = userId;
		this.type = type;
		this.entId = entId;
		this.entNum = entNum;
		this.isAdd = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAchieveId() {
		return achieveId;
	}

	public void setAchieveId(int achieveId) {
		this.achieveId = achieveId;
	}

	public int getEntId() {
		return entId;
	}

	public void setEntId(int entId) {
		this.entId = entId;
	}

	public int getEntNum() {
		return entNum;
	}

	public void setEntNum(int entNum) {
		if (this.isAdd) {
			return;
		}
		this.entNum = entNum;
		this.isAdd = true;
	}

	public Timestamp getFinishDttm() {
		return finishDttm;
	}

	public void setFinishDttm(Timestamp finishDttm) {
		this.finishDttm = finishDttm;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public static class AchieveInfo implements Serializable {
		private int achieveId; // 成就ID
		private int entId;// 成就实体
		private int entNum;// 成就实体数量
		private boolean finish;// 完成状态
		private int type;// 类型

		public AchieveInfo() {
		}

		public AchieveInfo(int achieveId, int entId, int entNum, boolean finish,
				int type) {
			this.achieveId = achieveId;
			this.entId = entId;
			this.entNum = entNum;
			this.finish = finish;
			this.type = type;
		}

		public int getAchieveId() {
			return achieveId;
		}

		public void setAchieveId(int achieveId) {
			this.achieveId = achieveId;
		}

		public int getEntId() {
			return entId;
		}

		public void setEntId(int entId) {
			this.entId = entId;
		}

		public int getEntNum() {
			return entNum;
		}

		public void setEntNum(int entNum) {
			this.entNum = entNum;
		}

		public boolean isFinish() {
			return finish;
		}

		public void setFinish(boolean finish) {
			this.finish = finish;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
	}
}
