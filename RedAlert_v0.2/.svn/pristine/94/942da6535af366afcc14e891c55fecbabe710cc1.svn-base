package com.youxigu.dynasty2.log;

public interface LogTypeConst {

	/*************** 腾讯 TLOG日志 Key begin ************/
	public static String TLOG_TYPE_ROLE_CREATE = "PlayerRegister";// 创建角色
	public static String TLOG_TYPE_ROLE_LOGIN = "PlayerLogin";// 角色登入日志
	public static String TLOG_TYPE_ROLE_LOGOUT = "PlayerLogout";// 角色登出日志
	public static String TLOG_TYPE_CASH = "MoneyFlow";// 元宝/点券/荣誉变动日志
	public static String TLOG_TYPE_ITEM = "ItemFlow";// 背包道具变动日志
	public static String TLOG_TYPE_ROLE_LVUP = "PlayerExpFlow";// 角色升级
	public static String TLOG_TYPE_SNSFLOW = "SnsFlow";// 社交
	public static String TLOG_TYPE_GameSvrState = "GameSvrState";// 服务器状态
	public static String TLOG_TYPE_RoundFlow= "RoundFlow";// 战斗
	public static String TLOG_TYPE_MissionFlow= "MissionFlow";//任务
	public static String TLOG_TYPE_ItemMoneyFlow= "ItemMoneyFlow";//商城购买
	public static String TLOG_TYPE_CityWar= "WarCityFlow";//国战
	public static String TLOG_TYPE_SecOperationFlow= "SecOperationFlow";//安全操作日志
	public static String TLOG_TYPE_SecTalkFlow= "SecTalkFlow";//安全日志：聊天相关日志
	public static String TLOG_TYPE_HpActiveFlow = "HpActiveFlow";// 体力行动力

    public static String TLOG_TYPE_BuildFlow= "BuildFlow";//建筑
    public static String TLOG_TYPE_TechFlow= "TechFlow";//科技
	
	public static final String SQL_INSERT_onlinecnt = "insert into tb_qxsy_onlinecnt(gameappid,timekey,gsid,zoneid,onlinecntios,onlinecntandroid,prevonlinecntios,prevonlinecntandroid) values(?,?,?,?,?,?,?,?)";
	public static final String SQL_INSERT_roleinfo = "insert into tb_qxsy_roleinfo(gameappid,openid,zoneid,regtime,level,iFriends,moneyios,moneyandroid,diamondios,diamondandroid,foodios,foodandroid,brzoneios,brzoneandroid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//这个sql需要需改，资源字段增加
	public static final String SQL_UPDATE_roleinfo = "update tb_qxsy_roleinfo set level=?,iFriends=?,moneyios=?,moneyandroid=?,diamondios=?,diamondandroid=?,foodios=?,foodandroid=?,brzoneios=?,brzoneandroid=? where openid=? and zoneid=?";

//	public static String TLOG_TYPE_SHOP = "Shop";// 商城购买日志
//	public static String TLOG_TYPE_ONLINE = "OnlineCount";// 在线人数日志
//	
//
//	
//	public static String TLOG_TYPE_MISSION = "TaskStart";// 角色任务日志
//	public static String TLOG_TYPE_USERTASK = "UserTask";// 君主任务日志	
//	public static String TLOG_TYPE_GUILDCREATE = "PartyBuild";// 创建联盟日志	
//	public static String TLOG_TYPE_GUILDDISMISS = "GuildDismiss";// 联盟解散日志	
//	public static String TLOG_TYPE_GUILDLVUP = "PartyLevelUp";// 联盟升级日志	
//	public static String TLOG_TYPE_GUILDADDMEMBER = "PartyAddMember";//添加 联盟成员日志
//	public static String TLOG_TYPE_GUILDDELMEMBER = "PartyDelMember";//删除 联盟成员日志
//	public static String TLOG_TYPE_GUILDMERGED = "GuildMerged";// 联盟合并日志
//	public static String TLOG_TYPE_GUILDONATE = "GuildDonate";// 联盟捐献日志	
//	public static String TLOG_TYPE_GUILDOMIGIC = "GuildMigic";// 联盟洞穴日志
//	public static String TLOG_TYPE_GODGET = "GodGet";// 请神日志	
//	public static String TLOG_TYPE_GODSEND = "GodSend";// 送神日志
//	public static String TLOG_TYPE_GODBOSS = "GodBoss";// 请神Boss日志
//	public static String TLOG_TYPE_USERACTION = "UserAction";// 玩家操作日志
//	public static String TLOG_TYPE_TRIBUTE = "Tribute";// 朝贡日志	
//	public static String TLOG_TYPE_ARMYOUT = "ArmyOut";// 朝贡日志	
//	public static String TLOG_TYPE_TOWER = "Tower";// 重楼日志	
//	public static String TLOG_TYPE_LEAGUE = "League";// 百战日志	
//	public static String TLOG_TYPE_RISK = "Risk";// 冒险日志	
//	public static String TLOG_TYPE_FRIENDEVENT = "FriendEvent";// 好友事件日志
//	public static String TLOG_TYPE_COUNTRYCHG = "CountryChange";// 转换国家日志	
//	public static String TLOG_TYPE_HEROINHERT = "HeroInhert";// 武将传承日志	
//	public static String TLOG_TYPE_HEROARMYTRANS= "HeroArmyTrans";//武将兵符转移日志
	
//	

	/*************** 腾讯 TLOG日志 Key end ************/

	// 登录登出
	public static String TYPE_LOGIN = "PlayerLogin";// 登录
	public static String TYPE_LOGOUT = "PlayerLogout";// 登出

	// 道具
	public static String TYPE_ADDITEM = "ADDITEM";// 添加道具
	public static String TYPE_DELITEM = "DELITEM";// 消耗道具

	// 代币
	public static String TYPE_ADDCASH = "ADDCASH";// 添加
	public static String TYPE_USECASH = "USECASH";// 消耗

	// 请神
	public static String TYPE_ADD_GOD = "ADDGOD";// 执行请神
	public static String TYPE_DEL_GOD = "DELGOD";// 执行送神

	public static String TYPE_TRIBUTE1 = "TRIBUTE1";// 普通朝贡1次
	public static String TYPE_TRIBUTE2 = "TRIBUTE2";// 元宝朝贡1次

	public static String TYPE_PVP1 = "PVP1";// PVP讨伐
	public static String TYPE_PVP2 = "PVP2";// PVP摧毁
	public static String TYPE_PVP3 = "PVP3";// PVP增援
	public static String TYPE_PVE = "PVE";// PVE讨伐	

	// /联盟相关
	public static String TYPE_GUILD_CREATE = "CREATE";// 创建联盟
	public static String TYPE_GUILD_DELETE = "DELETE";// 解散联盟
	public static String TYPE_GUILD_JOIN = "JOIN";// 加入联盟
	public static String TYPE_GUILD_EXIT = "EXIT";// 退出联盟
	public static String TYPE_GUILD_AWARD = "AWARD";// 领取福利
	public static String TYPE_GUILD_LEVELUP = "LEVELUP";// 升级	

//	public static String TYPE_GUILD_MERGE = "MERGE";// 联盟合并

	public static String TYPE_GUILD_RESOURCE = "RESOURCE";// 联盟捐献资源
	public static String TYPE_GUILD_HUFU = "HUFU";// 联盟捐献虎符

	// 任务相关
	// public static String TYPE_MISSIOM_MAIN="QTYPE_MAIN";//内政任务
	// public static String TYPE_MISSIOM_HONOR="QTYPE_HONOR";//威望任务
	// public static String TYPE_MISSIOM_RISK = "QTYPE_RISK";//冒险任务

	// 军机阁任务
	// public static String TYPE_JJG_TASK_EXE = "JJG_TASK_EXE";

	// 千重楼
	public static String TYPE_TOWER_JOIN = "TOWER_JOIN";// 进入
	public static String TYPE_TOWER_THROW = "TOWER_EXIT";// 退出

	public static String TYPE_FISH_SINGLE = "FISH";//单人捕鱼
	public static String TYPE_FISH_MUTLI = "FISH_M";//多人捕鱼
	public static String TLOG_TYPE_FISH= "UserFish";//捕鱼日志

	// 邀请好友建筑工
	public static String TYPE_USER_BUILDER = "USER_BUILDER";

	// 冒险
	public static String TYPE_RISK_JOIN = "RISK_JOIN";
	public static String TYPE_RISK_JOIN10 = "RISK_JOIN10";

	// 武将
	@Deprecated
	public static String TYPE_HERO_INHERIT = "INHERIT";
	@Deprecated
	public static String TYPE_HERO_ARMY_TRANS = "ARMY_TRAN";

	// 恢复行动力
	public static String TYPE_MOVE_RANGE = "MOVE_RANGE";

	// 恢复体力
	public static String TYPE_HP = "HP";
	
	public static String TYPE_BEGIN = "BEGIN";	
	public static String TYPE_END = "END";	
	
	//七日领奖
	public static String TYPE_SEVENDAY_GIFT = "SEVENDAY_GIFT";

	// 城池事件
	public static String TYPE_CASTLE_EVENT = "CASTLE_EVENT";
	public static String TYPE_VISIT_EVENT = "VISIT_EVENT";
	public static String TYPE_HERO_EVENT = "HERO_EVENT";

	// 偷取资源
	public static String TYPE_PICK = "PICK";

	// 浇水
	public static String TYPE_WATER = "WATER";

	// 转国
	public static String TYPE_CHANGE_COUNTRY = "CHANGE_COUNTRY";
	// 休养生息
	public static String TYPE_RECUPERATE = "RECUPERATE";

	// 请神boss
	public static String TYPE_PLEASEBOSS = "PLEASEBOSS";// 请出boss
	public static String TYPE_FIGHTBOSS = "FIGHTBOSS";// 挑战boss
	public static String TYPE_DELBOSS = "DELBOSS";// boss自动消失

	// 用户
	public static String TYPE_REGISTERUSER = "REGISTERUSER";// 注册
	public static String TYPE_USERCREATE = "USERCREATE";// 创建角色
	public static String TYPE_USERLV = "USERLV";// 用户升级

	// 神秘洞
	public static String TYPE_PLEASEMIGIC = "PLEASEMIGIC";// 开启神秘洞
	public static String TYPE_JOINMIGIC = "JOINMIGIC";// 加入
	public static String TYPE_FIGHTMIGIC = "FIGHTMIGIC";// 挑战
	
	//公益捐款
	public static String TYPE_DONATION = "DONATION";
	
	//首次充值发放奖励，需要玩家手动领取
	public static String TYPE_QQTASK = "QQ_TASK_AWARD"; 
	//任务集市发放奖励,通过邮件附件发放
	public static String TYPE_FIRST_RECHARGE = "FIRST_RECHARGE_AWARD";
	
	public static String TYPE_USER_GOLD_NPC = "USER_GOLD_NPC";//占领金矿
	public static String TYPE_USER_GOLD = "USER_GOLD";//抢夺金矿
	public static String TYPE_USER_GOLD_GIVEUP = "USER_GOLD_GIVEUP";//放弃金矿
	
	public static String TYPE_TX_UNION_REPORT = "TX_UNION_REPORT";//腾讯游戏联盟上报
	public static String TYPE_TX_COMPASS_REPORT = "TX_COMPASS_REPORT";//腾讯罗盘数据上报
	
	public static final String LOG_TYPE_ADD = "DUTY_ADD";//增加使命
	public static final String LOG_TYPE_FINISH = "DUTY_FINISH";//完成使命
}
