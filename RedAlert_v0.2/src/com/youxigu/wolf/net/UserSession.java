package com.youxigu.wolf.net;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.youxigu.dynasty2.openapi.Constant;
import com.youxigu.dynasty2.user.domain.User;

/*
 * 用户session在这里定义
 */
public class UserSession implements java.io.Serializable {
	private static final long serialVersionUID = -1161136501365203059L;
	public static String KEY_USERSESSION = "USERSESSION";
	public static String KEY_GAMESERVER_IP = "gip";
	public static String KEY_GAMESERVER_PORT = "gport";
	// 记录由系统推荐的国家id，如果使用了由系统推荐的国家id创建国家，需要发奖
	public static final String KEY_COUNTRYID = "countryId";
	public static String KEY_PVPinviteLList = "PVPinviteLList";// PVP邀请盟友列表
	public static final String KEY_RECENT_TIME_FRIEND = "RECENT_TIME_FRIEND";// 最近联系人key
	public static final String KEY_OFF_LINE_MSG = "OFF_LINE_MSG";// 离线消息key
	public static final String KEY_CHAT_MSG = "CHAT_MSG";// 聊天消息设置状态值

	/**
	 * 唯一ID
	 */
	private String sessionId;
	/**
	 * tx的openid,与APP通信的用户key
	 */
	private String accName;

	/**
	 * tx的 手Q openkey 威信 accesstoken
	 */
	private String openkey;

	/**
	 * tx的 pay_token;
	 */
	private String pay_token;

	/**
	 * 微信刷新openkey(accesstoken)用的
	 */
	private String refreshToken;
	/**
	 * 支付用 平台来源，$平台-$渠道-$版本-$业务标识。例如：openmobile_android-2001-android-xxxx
	 */
	private String pf;
	/**
	 * 支付用
	 */
	private String pfKey;

	/**
	 * 这两个参数是聊天使用
	 * 
	 */
	private int qqFlag;// 当前的QQ标志，如，黄钻，蓝钻等，这里只根据pf值的不同，只取当前pf对应的值
	private int qqFlagLv;// 当前的QQ标志等级，如，黄钻，蓝钻等级

	private String seqId;
	private String gameNodeName;
	private String gameServerIp;
	private int gameServerPort;

	private long accId;// 账号id
	private long userId;// 用户id
	private int gender;// 性别
	private long mainCasId;// 玩家主城id
	private String userName;// 君主名

	private long loginDttm;// 登陆时间戳
	/**
	 * 连续在线时间：登录间隔在5小时以内，都算连续在线，防沉迷用
	 * 
	 * 不包含本次上线后的时间
	 */
	private int prevOnlineTimeSum;

	/**
	 * 不在线累计时间
	 */
	private int offlineTimeSum;

	private String userip;// /

	/**
	 * 渠道号，外发联运的g_cid
	 */
	private String via;
	// private boolean invalid; //
	// 是否是无效的session,当用户重复登陆时，将原来的session设置为invalid=true;
	// private long mainCasId;// 主城id
	// private List<Integer> casIds;// 君主所有城池的id列表

	// 玩家上次的操作时间（不算聊天）
	// private long lastUserOpTime = System.currentTimeMillis();
	// QQ信息
	private Object quser = null;
	// 上一次通过开放平台检查的时间
	private long lastChkTime = 0;

	/**
	 * 区服Id
	 */
	private String areaId;
	/**
	 * 区服名称
	 */
	private String areaName;

	private int pType;// 平台类型
	private int dType;// 设备类型
	/**
	 * 外发联运的p_device_info
	 */
	private String dInfo;//
	// 客户端机型信息
	private MobileClient mobileClient;

	/**
	 * 客户端操作流水
	 */
	private AtomicInteger operateCount = new AtomicInteger(0);

	// 用来缓存临时的用户信息，用户下线后不保留，不要保存大对象，能及时删除的尽量及时删除
	private Map<String, Object> attributes = new HashMap<String, Object>();

	// 二级密码
	private String oldPwd;
	private String newPwd;

	public UserSession() {
		this.sessionId = UUID.randomUUID().toString();

	}

	public UserSession(String sessionId) {
		if (sessionId == null || sessionId.length() == 0) {
			this.sessionId = UUID.randomUUID().toString();
		} else {
			this.sessionId = sessionId;
		}
	}

	public String getGameNodeName() {
		return gameNodeName;
	}

	public void setGameNodeName(String gameNodeName) {
		this.gameNodeName = gameNodeName;
	}

	public String getGameServerIp() {
		return gameServerIp;
	}

	public void setGameServerIp(String gameServerIp) {
		this.gameServerIp = gameServerIp;
	}

	public int getGameServerPort() {
		return gameServerPort;
	}

	public void setGameServerPort(int gameServerPort) {
		this.gameServerPort = gameServerPort;
	}

	public String getSessionId() {
		return sessionId;
	}

	public long getAccId() {
		return accId;
	}

	public void setAccId(long accId) {
		this.accId = accId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getMainCasId() {
		return mainCasId;
	}

	public void setMainCasId(long mainCasId) {
		this.mainCasId = mainCasId;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void addAttribute(String key, Object obj) {
		attributes.put(key, obj);
	}

	public Object removeAttribute(String key) {
		return attributes.remove(key);
	}

	public void removeAllAttributes() {
		attributes.clear();
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getPf() {
		return pf;
	}

	public String getPfEx() {
		return pf;
	}

	public String getPf_customer(User user) {
		return pf + "-" + user.getUsrLv() + "*" + user.getCountryId();
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPfKey() {
		return pfKey;
	}

	public void setPfKey(String pfKey) {
		this.pfKey = pfKey;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOpenkey() {
		return openkey;
	}

	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}

	public String getOpenid() {
		return this.accName;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

	public long getLastChkTime() {
		return lastChkTime;
	}

	public void setLastChkTime(long lastChkTime) {
		this.lastChkTime = lastChkTime;
	}

	public Object getQuser() {
		return quser;
	}

	public void setQuser(Object quser) {
		this.quser = quser;
	}

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	public long getLoginDttm() {
		return loginDttm;
	}

	public void setLoginDttm(long loginDttm) {
		this.loginDttm = loginDttm;
	}

	public int getQqFlag() {
		return qqFlag;
	}

	public void setQqFlag(int qqFlag) {
		this.qqFlag = qqFlag;
	}

	public int getQqFlagLv() {
		return qqFlagLv;
	}

	public void setQqFlagLv(int qqFlagLv) {
		this.qqFlagLv = qqFlagLv;
	}

	/**
	 * 连续在线时间(秒):
	 * 
	 * @return
	 */
	public int getOnlineTimeSum() {
		long now = System.currentTimeMillis();
		return (int) ((now - loginDttm) / 1000 + prevOnlineTimeSum);
	}

	public int getPrevOnlineTimeSum() {
		return prevOnlineTimeSum;
	}

	public void setPrevOnlineTimeSum(int prevOnlineTimeSum) {
		this.prevOnlineTimeSum = prevOnlineTimeSum;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setOfflineTimeSum(int offlineTimeSum) {
		this.offlineTimeSum = offlineTimeSum;
	}

	public int getOfflineTimeSum() {
		return offlineTimeSum;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getAreaId() {
		if (this.areaId == null) {
			return Constant.AREA_ID;
		}
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public boolean isAny() {
		return pType == Constant.PLATFORM_TYPE_ANY;
	}

	public boolean isVistor() {
		return pType == Constant.PLATFORM_TYPE_VISTOR;
	}

	public boolean isMobileQQ() {
		return pType == Constant.PLATFORM_TYPE_QQ;

	}

	public boolean isWeixin() {
		return pType == Constant.PLATFORM_TYPE_WEIXIN;
	}

	public boolean isITools() {
		return pType == Constant.PLATFORM_TYPE_ITOOLS;
	}

	public boolean isUnion() {
		return pType == Constant.PLATFORM_TYPE_UNION;
	}

	public boolean isFaceBook() {
		return pType == Constant.PLATFORM_TYPE_FACEBOOK;
	}

	public int getpType() {
		return pType;
	}

	public void setpType(int pType) {
		this.pType = pType;
	}

	public int getdType() {
		return dType;
	}

	public void setdType(int dType) {
		this.dType = dType;
	}

	public String getPay_token() {
		return pay_token;
	}

	public void setPay_token(String payToken) {
		pay_token = payToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public MobileClient getMobileClient() {
		return mobileClient;
	}

	public void setMobileClient(MobileClient mobileClient) {
		this.mobileClient = mobileClient;
	}

	public void putPlatformProperties(Map<String, Object> params) {
		params.put("qf", this.qqFlag);
		if (this.qqFlagLv > 0) {
			params.put("lvVip", qqFlagLv);
		}

	}

	public AtomicInteger getOperateCount() {
		return operateCount;
	}

	public void setOperateCount(AtomicInteger operateCount) {
		this.operateCount = operateCount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getdInfo() {
		return dInfo;
	}

	public void setdInfo(String dInfo) {
		this.dInfo = dInfo;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
