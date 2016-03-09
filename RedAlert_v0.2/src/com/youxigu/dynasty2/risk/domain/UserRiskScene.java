package com.youxigu.dynasty2.risk.domain;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.risk.service.IRiskService;

/**
 * 玩家冒险记录<br>
 * 
 * @author Administrator
 * 
 */
public class UserRiskScene implements java.io.Serializable {
	private static final long serialVersionUID = 3233790814994083782L;
	private static final short MASK_FLAGS = Short.MAX_VALUE;// 判断是否通关的标志码
	// private int id;
	private long userId;
	/*** 大场景Id */
	private int pid;
	/*** 星数奖励,采用位来表示，=0表示未领取 */
	private int starAward;
	// /////////////一个大场景的15个小场景数据
	/*** 小场景Id */
	private int sceneId0;
	private int version0;
	private long data0;
	private String items0;

	private int sceneId1;
	private int version1;
	private long data1;
	private String items1;

	private int sceneId2;
	private int version2;
	private long data2;
	private String items2;

	private int sceneId3;
	private int version3;
	private long data3;
	private String items3;

	private int sceneId4;
	private int version4;
	private long data4;
	private String items4;

	private int sceneId5;
	private int version5;
	private long data5;
	private String items5;

	private int sceneId6;
	private int version6;
	private long data6;
	private String items6;

	private int sceneId7;
	private int version7;
	private long data7;
	private String items7;

	private int sceneId8;
	private int version8;
	private long data8;
	private String items8;

	private int sceneId9;
	private int version9;
	private long data9;
	private String items9;

	private int sceneId10;
	private int version10;
	private long data10;
	private String items10;

	private int sceneId11;
	private int version11;
	private long data11;
	private String items11;

	private int sceneId12;
	private int version12;
	private long data12;
	private String items12;

	private int sceneId13;
	private int version13;
	private long data13;
	private String items13;

	private int sceneId14;
	private int version14;
	private long data14;
	private String items14;

	/** 设置一个标志主要用来判断关卡里面的小关是否都通过 才用位表示 根据关卡配数 目前使用 0到14位来表示15关数据 */
	private int flags;

	private transient IRiskService riskService = null;
	private transient boolean update = false;// 判断当个关卡的数据是否刷新后是否需要存库

	public UserRiskScene() {
		super();
	}

	public UserRiskScene(long userId, int pid) {
		super();
		this.userId = userId;
		this.pid = pid;
	}

	// public int getId() {
	// return id;
	// }
	//
	// public void setId(int id) {
	// this.id = id;
	// }

	public String getItems0() {
		return items0;
	}

	public void setItems0(String items0) {
		this.items0 = items0;
	}

	public String getItems1() {
		return items1;
	}

	public void setItems1(String items1) {
		this.items1 = items1;
	}

	public String getItems2() {
		return items2;
	}

	public void setItems2(String items2) {
		this.items2 = items2;
	}

	public String getItems3() {
		return items3;
	}

	public void setItems3(String items3) {
		this.items3 = items3;
	}

	public String getItems4() {
		return items4;
	}

	public void setItems4(String items4) {
		this.items4 = items4;
	}

	public String getItems5() {
		return items5;
	}

	public void setItems5(String items5) {
		this.items5 = items5;
	}

	public String getItems6() {
		return items6;
	}

	public void setItems6(String items6) {
		this.items6 = items6;
	}

	public String getItems7() {
		return items7;
	}

	public void setItems7(String items7) {
		this.items7 = items7;
	}

	public String getItems8() {
		return items8;
	}

	public void setItems8(String items8) {
		this.items8 = items8;
	}

	public String getItems9() {
		return items9;
	}

	public void setItems9(String items9) {
		this.items9 = items9;
	}

	public String getItems10() {
		return items10;
	}

	public void setItems10(String items10) {
		this.items10 = items10;
	}

	public String getItems11() {
		return items11;
	}

	public void setItems11(String items11) {
		this.items11 = items11;
	}

	public String getItems12() {
		return items12;
	}

	public void setItems12(String items12) {
		this.items12 = items12;
	}

	public String getItems13() {
		return items13;
	}

	public void setItems13(String items13) {
		this.items13 = items13;
	}

	public String getItems14() {
		return items14;
	}

	public void setItems14(String items14) {
		this.items14 = items14;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getStarAward() {
		return starAward;
	}

	public void setStarAward(int starAward) {
		this.starAward = starAward;
	}

	/**
	 * 取得小关的过关信息
	 * 
	 * @param idx
	 * @return 数组：0=小关卡Id,1=小关卡星数,2=本日参加次数，3=最后参加的天（年内）
	 */
	public OneRisk getStageInfoByIndex(int idx) {
		int id = 0;
		int version = 0;
		long data = 0;
		String items = "";
		switch (idx) {
		case 0:
			id = sceneId0;
			version = version0;
			data = data0;
			items = items0;
			break;
		case 1:
			id = sceneId1;
			version = version1;
			data = data1;
			items = items1;
			break;
		case 2:
			id = sceneId2;
			version = version2;
			data = data2;
			items = items2;
			break;
		case 3:
			id = sceneId3;
			version = version3;
			data = data3;
			items = items3;
			break;
		case 4:
			id = sceneId4;
			version = version4;
			data = data4;
			items = items4;
			break;
		case 5:
			id = sceneId5;
			version = version5;
			data = data5;
			items = items5;
			break;
		case 6:
			id = sceneId6;
			version = version6;
			data = data6;
			items = items6;
			break;
		case 7:
			id = sceneId7;
			version = version7;
			data = data7;
			items = items7;
			break;
		case 8:
			id = sceneId8;
			version = version8;
			data = data8;
			items = items8;
			break;
		case 9:
			id = sceneId9;
			version = version9;
			data = data9;
			items = items9;
			break;
		case 10:
			id = sceneId10;
			version = version10;
			data = data10;
			items = items10;
			break;
		case 11:
			id = sceneId11;
			version = version11;
			data = data11;
			items = items11;
			break;
		case 12:
			id = sceneId12;
			version = version12;
			data = data12;
			items = items12;
			break;
		case 13:
			id = sceneId13;
			version = version13;
			data = data13;
			items = items13;
			break;
		case 14:
			id = sceneId14;
			version = version14;
			data = data14;
			items = items14;
			break;
		default:
			break;
		}
		if (id <= 0) {
			return null;
		}
		OneRisk stageInfo = new OneRisk(id, version, data, items);
		boolean r = getRiskService().canRestFailNum(version,
				stageInfo.getRestFailHour());
		boolean update = stageInfo.refresh(r);
		if (update) {
			setStageInfoByIndex(stageInfo, idx);
			this.update = true;
		}
		return stageInfo;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public int getSceneId0() {
		return sceneId0;
	}

	public void setSceneId0(int sceneId0) {
		this.sceneId0 = sceneId0;
	}

	public int getVersion0() {
		return version0;
	}

	public void setVersion0(int version0) {
		this.version0 = version0;
	}

	public long getData0() {
		return data0;
	}

	public void setData0(long data0) {
		this.data0 = data0;
	}

	public int getSceneId1() {
		return sceneId1;
	}

	public void setSceneId1(int sceneId1) {
		this.sceneId1 = sceneId1;
	}

	public int getVersion1() {
		return version1;
	}

	public void setVersion1(int version1) {
		this.version1 = version1;
	}

	public long getData1() {
		return data1;
	}

	public void setData1(long data1) {
		this.data1 = data1;
	}

	public int getSceneId2() {
		return sceneId2;
	}

	public void setSceneId2(int sceneId2) {
		this.sceneId2 = sceneId2;
	}

	public int getVersion2() {
		return version2;
	}

	public void setVersion2(int version2) {
		this.version2 = version2;
	}

	public long getData2() {
		return data2;
	}

	public void setData2(long data2) {
		this.data2 = data2;
	}

	public int getSceneId3() {
		return sceneId3;
	}

	public void setSceneId3(int sceneId3) {
		this.sceneId3 = sceneId3;
	}

	public int getVersion3() {
		return version3;
	}

	public void setVersion3(int version3) {
		this.version3 = version3;
	}

	public long getData3() {
		return data3;
	}

	public void setData3(long data3) {
		this.data3 = data3;
	}

	public int getSceneId4() {
		return sceneId4;
	}

	public void setSceneId4(int sceneId4) {
		this.sceneId4 = sceneId4;
	}

	public int getVersion4() {
		return version4;
	}

	public void setVersion4(int version4) {
		this.version4 = version4;
	}

	public long getData4() {
		return data4;
	}

	public void setData4(long data4) {
		this.data4 = data4;
	}

	public int getSceneId5() {
		return sceneId5;
	}

	public void setSceneId5(int sceneId5) {
		this.sceneId5 = sceneId5;
	}

	public int getVersion5() {
		return version5;
	}

	public void setVersion5(int version5) {
		this.version5 = version5;
	}

	public long getData5() {
		return data5;
	}

	public void setData5(long data5) {
		this.data5 = data5;
	}

	public int getSceneId6() {
		return sceneId6;
	}

	public void setSceneId6(int sceneId6) {
		this.sceneId6 = sceneId6;
	}

	public int getVersion6() {
		return version6;
	}

	public void setVersion6(int version6) {
		this.version6 = version6;
	}

	public long getData6() {
		return data6;
	}

	public void setData6(long data6) {
		this.data6 = data6;
	}

	public int getSceneId7() {
		return sceneId7;
	}

	public void setSceneId7(int sceneId7) {
		this.sceneId7 = sceneId7;
	}

	public int getVersion7() {
		return version7;
	}

	public void setVersion7(int version7) {
		this.version7 = version7;
	}

	public long getData7() {
		return data7;
	}

	public void setData7(long data7) {
		this.data7 = data7;
	}

	public int getSceneId8() {
		return sceneId8;
	}

	public void setSceneId8(int sceneId8) {
		this.sceneId8 = sceneId8;
	}

	public int getVersion8() {
		return version8;
	}

	public void setVersion8(int version8) {
		this.version8 = version8;
	}

	public long getData8() {
		return data8;
	}

	public void setData8(long data8) {
		this.data8 = data8;
	}

	public int getSceneId9() {
		return sceneId9;
	}

	public void setSceneId9(int sceneId9) {
		this.sceneId9 = sceneId9;
	}

	public int getVersion9() {
		return version9;
	}

	public void setVersion9(int version9) {
		this.version9 = version9;
	}

	public long getData9() {
		return data9;
	}

	public void setData9(long data9) {
		this.data9 = data9;
	}

	public int getSceneId10() {
		return sceneId10;
	}

	public void setSceneId10(int sceneId10) {
		this.sceneId10 = sceneId10;
	}

	public int getVersion10() {
		return version10;
	}

	public void setVersion10(int version10) {
		this.version10 = version10;
	}

	public long getData10() {
		return data10;
	}

	public void setData10(long data10) {
		this.data10 = data10;
	}

	public int getSceneId11() {
		return sceneId11;
	}

	public void setSceneId11(int sceneId11) {
		this.sceneId11 = sceneId11;
	}

	public int getVersion11() {
		return version11;
	}

	public void setVersion11(int version11) {
		this.version11 = version11;
	}

	public long getData11() {
		return data11;
	}

	public void setData11(long data11) {
		this.data11 = data11;
	}

	public int getSceneId12() {
		return sceneId12;
	}

	public void setSceneId12(int sceneId12) {
		this.sceneId12 = sceneId12;
	}

	public int getVersion12() {
		return version12;
	}

	public void setVersion12(int version12) {
		this.version12 = version12;
	}

	public long getData12() {
		return data12;
	}

	public void setData12(long data12) {
		this.data12 = data12;
	}

	public int getSceneId13() {
		return sceneId13;
	}

	public void setSceneId13(int sceneId13) {
		this.sceneId13 = sceneId13;
	}

	public int getVersion13() {
		return version13;
	}

	public void setVersion13(int version13) {
		this.version13 = version13;
	}

	public long getData13() {
		return data13;
	}

	public void setData13(long data13) {
		this.data13 = data13;
	}

	public int getSceneId14() {
		return sceneId14;
	}

	public void setSceneId14(int sceneId14) {
		this.sceneId14 = sceneId14;
	}

	public int getVersion14() {
		return version14;
	}

	public void setVersion14(int version14) {
		this.version14 = version14;
	}

	public long getData14() {
		return data14;
	}

	public void setData14(long data14) {
		this.data14 = data14;
	}

	public IRiskService getRiskService() {
		if (this.riskService == null) {
			riskService = (IRiskService) ServiceLocator
					.getSpringBean("riskService");
		}
		return riskService;
	}

	public void setStageInfoByIndex(OneRisk stageInfo, int idx) {
		switch (idx) {
		case 0:
			sceneId0 = stageInfo.getId();
			version0 = stageInfo.getVersion();
			data0 = stageInfo.toData();
			items0 = stageInfo.getItems();
			break;
		case 1:
			sceneId1 = stageInfo.getId();
			version1 = stageInfo.getVersion();
			data1 = stageInfo.toData();
			items1 = stageInfo.getItems();
			break;
		case 2:
			sceneId2 = stageInfo.getId();
			version2 = stageInfo.getVersion();
			data2 = stageInfo.toData();
			items2 = stageInfo.getItems();
			break;
		case 3:
			sceneId3 = stageInfo.getId();
			version3 = stageInfo.getVersion();
			data3 = stageInfo.toData();
			items3 = stageInfo.getItems();
			break;
		case 4:
			sceneId4 = stageInfo.getId();
			version4 = stageInfo.getVersion();
			data4 = stageInfo.toData();
			items4 = stageInfo.getItems();
			break;
		case 5:
			sceneId5 = stageInfo.getId();
			version5 = stageInfo.getVersion();
			data5 = stageInfo.toData();
			items5 = stageInfo.getItems();
			break;
		case 6:
			sceneId6 = stageInfo.getId();
			version6 = stageInfo.getVersion();
			data6 = stageInfo.toData();
			items6 = stageInfo.getItems();
			break;
		case 7:
			sceneId7 = stageInfo.getId();
			version7 = stageInfo.getVersion();
			data7 = stageInfo.toData();
			items7 = stageInfo.getItems();
			break;
		case 8:
			sceneId8 = stageInfo.getId();
			version8 = stageInfo.getVersion();
			data8 = stageInfo.toData();
			items8 = stageInfo.getItems();
			break;
		case 9:
			sceneId9 = stageInfo.getId();
			version9 = stageInfo.getVersion();
			data9 = stageInfo.toData();
			items9 = stageInfo.getItems();
			break;
		case 10:
			sceneId10 = stageInfo.getId();
			version10 = stageInfo.getVersion();
			data10 = stageInfo.toData();
			items11 = stageInfo.getItems();
			break;
		case 11:
			sceneId11 = stageInfo.getId();
			version11 = stageInfo.getVersion();
			data11 = stageInfo.toData();
			items11 = stageInfo.getItems();
			break;
		case 12:
			sceneId12 = stageInfo.getId();
			version12 = stageInfo.getVersion();
			data12 = stageInfo.toData();
			items12 = stageInfo.getItems();
			break;
		case 13:
			sceneId13 = stageInfo.getId();
			version13 = stageInfo.getVersion();
			data13 = stageInfo.toData();
			items13 = stageInfo.getItems();
			break;
		case 14:
			sceneId14 = stageInfo.getId();
			version14 = stageInfo.getVersion();
			data14 = stageInfo.toData();
			items14 = stageInfo.getItems();
			break;
		default:
			break;
		}
		if (stageInfo.isPass()) {
			// 本小关已经通过了
			this.flags = this.flags | (1 << idx);
		}
	}

	public static void main(String[] args) {
		long l;
		long star = 3;// 2
		long joinNum = 2048;// 16 <<2
		long failNum = 2049;// 16 <<18
		long restNum = 1023;// 10 <<34
		long firstBonus = 1;// 1 <<44
		long restFailNum = 20;// 4 << 45
		// long version =
		// TimeUtils.getVersionOfToday(System.currentTimeMillis());// 16位

		// l = (star << (MAX_BIT - 2)) | (joinNum << (MAX_BIT - 2 - 16))
		// | (failNum << (MAX_BIT - 2 - 16 - 16))
		// | (restNum << (MAX_BIT - 2 - 16 - 16 - 10))
		// | (firstBonus << (MAX_BIT - 2 - 16 - 16 - 10 - 1));

		l = (star) | (joinNum << (2)) | (failNum << (2 + 16))
				| (restNum << (2 + 16 + 16))
				| (firstBonus << (2 + 16 + 16 + 10))
				| (restFailNum << (2 + 16 + 16 + 10 + 1));
		// | (version << (MAX_BIT - 2 - 16 - 16 - 10 - 1 - 16));

		System.out.println(l + "----" + Long.toBinaryString(l));

		long r_restFailNum = (l >> (2 + 16 + 16 + 10 + 1));
		System.out.println(restFailNum + "----" + r_restFailNum);

		// long r_firstBonus = (l >> (2 + 16 + 16 + 10));
		l = l ^ (r_restFailNum << (2 + 16 + 16 + 10 + 1));
		long r_firstBonus = ((l >> (2 + 16 + 16 + 10)));
		System.out.println(firstBonus + "----" + r_firstBonus);

		l = l ^ (r_firstBonus << (2 + 16 + 16 + 10));
		long r_restNum = ((l >> (2 + 16 + 16)));
		System.out.println(restNum + "----" + r_restNum);

		l = l ^ (r_restNum << (2 + 16 + 16));
		long r_failNum = ((l >> (2 + 16)));
		System.out.println(failNum + "----" + r_failNum);

		l = l ^ (r_failNum << (2 + 16));
		long r_joinNum = ((l >> (2)));
		System.out.println(joinNum + "----" + r_joinNum);

		l = l ^ (r_joinNum << (2));
		long r_star = l;
		System.out.println(star + "----" + r_star);

		// l = l ^ (r_firstBonus << (MAX_BIT - 2 - 16 - 16 - 10 - 1));
		// long r_version = ((l >> (MAX_BIT - 2 - 16 - 16 - 10 - 1 - 16)));
		// System.out.println(version + "----" + r_version);
	}

	public boolean isStarAward(byte idx) {
		boolean award = false;
		if (idx == 0) {
			award = isStarAward1();
		} else if (idx == 1) {
			award = isStarAward2();
		} else {
			award = isStarAward3();
		}
		return award;
	}

	public void setStarAward(byte idx) {
		if (idx == 0) {
			starAward = (starAward | 0x1);
		} else if (idx == 1) {
			starAward = (starAward | 0x2);
		} else {
			starAward = (starAward | 0x4);
		}
	}

	/**
	 * true表示已经领取
	 * 
	 * @return
	 */
	public boolean isStarAward1() {
		return (starAward & 0x1) == 0x1;
	}

	/**
	 * true表示已经领取
	 * 
	 * @return
	 */
	public boolean isStarAward2() {
		return (starAward & 0x2) == 0x2;
	}

	/**
	 * true表示已经领取
	 * 
	 * @return
	 */
	public boolean isStarAward3() {
		return (starAward & 0x4) == 0x4;
	}

	/**
	 * 判断所有关卡都是1星以上。表示通关
	 * 
	 * @return
	 */
	public boolean isPassAll() {
		return (this.flags & MASK_FLAGS) == MASK_FLAGS;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

}
