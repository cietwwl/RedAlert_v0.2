package com.youxigu.dynasty2.core.flex.amf;

public interface IAMF3Action {
	public static final String ACTION_SID_KEY = "sid";
	public static final String ACTION_CMD_KEY = "cmd";
	public static final String ACTION_ERROR_CODE_KEY = "errCode";
	public static final String ACTION_ERROR_KEY = "err";
	public static final int CMD_DEFAULT=1;
	public static final int CMD_KEEPALIVE_TIMEOUT=-9011;//心跳超时
	public static final int CMD_BEAT_REQ = 100000;//心跳请求
	public static final int CMD_BEAT_RES = 100001;//心跳响应
	
	
	
	public static final int CMD_ECHO = 0;// echo ,测试用
	public static final int CMD_BUSINESS_ERROR = -10001;// 业务异常
	// -10001---99999都是业务异常
	public static final int CMD_SYSTEM_ERROR = -10000;// 系统异常

	public static final int CMD_PAY_ERROR = -20000;// 支付token异常
	
	public static final int CMD_PASSWORD = -7777;// 二级密码
	
	// 用户10
	public static final int CMD_LOGIN = 10001;// 登录命令
	public static final int CMD_LOGLOG = 10002;// 记录登录后的演示场景日期
	public static final int CMD_GETUSER = 10003;// 取得角色信息
	public static final int CMD_NOUSER = 1;// 没有创建角色
	public static final int CMD_GET_COMMONUSER_INFO = 10004;// 指定君主信息面板-10004
	public static final int CMD_CREATE_USER = 10005;// 创建角色
	public static final int CMD_CHANGE_SIGNATURE = 10006;// 修改个性签名
	public static final int CMD_GET_USER_INFO = 10007;// 君主【头像】详细信息面板-10007
	
	public static final int CMD_GET_CASH = 10008;// 取得玩家在开放平台二级货币余额	
	//聊天
	//public static final int CMD_CHAT_RESEIVE = 10009; // 接收聊天消息
	public static final int CMD_CHAT_SEND = 10009; // 发送聊天消息
	public static final int CMD_CHAT_SEND_RETURN = 10010;
	public static final int CMD_CHAT_ERROR = -10030; // 聊天异常异常
	public static final String ACTION_CHAT_KEY = "ChatMesage"; // 聊天内容对象
	
	public static final int CMD_UPGRADE_USER_POST = 10010; // 升官
	public static final int CMD_GAIN_DAYLY_AWARD = 10011;// 领取每日俸禄
	
	
	public static final int CMD_VIEW_CHANGE_COUNTRY = 10012;//转国视图
	public static final int CMD_CHANGE_COUNTRY = 10013;//转国
	
	//邮件
	public static final int CMD_MAIL_SEND = 10110;// 发送邮件
	public static final int CMD_MAIL_GETMAILS = 10112;// 获取用户邮件列表
	public static final int CMD_MAIL_DELETE = 10114;// 删除邮件	
	public static final int CMD_MAIL_APPENDIX = 10116;// 获取邮件附件
	public static final int CMD_MAIL_READ = 10118;// 标记邮件已读
	public static final int CMD_GET_ALL_FRIENDLIST = 10119;// 取所有好友名
	
	//军机阁任务
	public static final int CMD_REFRESH_USERTASK = 12001;// 君主任务刷新-12001
	public static final int CMD_EXEC_USERTASK = 12002;// 执行君主任务-12002
	public static final int CMD_GET_USERTASK_AWARD = 12003;// 领取君主任务奖励-12003
	public static final int CMD_USE_TASKPACK = 12006;// 使用锦囊-12006
	
	//生活技能
	public static final int CMD_VIEW_LIVESKILL = 12101;//查看生活技能信息
	public static final int CMD_upgrade_LiveSkill = 12102;//升级技能
	
	public static final int CMD_VIEW_ONLINE_AWARD = 13000;//取得在线奖励信息
	public static final int CMD_USER_GUIDELINES = 13001;//新手指引
	public static final int CMD_GAIN_ONLINE_AWARD = 13002;//领取在线奖励
	
	public static final int CMD_VIEW_USEREQUIP_INFO = 13003;//查看君主装备信息
	public static final int CMD_DO_TRIBUTE = 13004;//朝贡
	public static final int CMD_CHANGE_EQUIP = 13005;//君主换装
	
	public static final int CMD_GAIN_TIME_AWARD = 13006;//领取定时奖励-13006
	public static final int CMD_VIEW_TRIBUTE = 13007;//查看朝贡信息-13007
	public static final int CMD_GAIN_VIP_POS_GUILD_AWARD = 13008;//集中领取Vip,爵位，联盟福利-13008
	
	public static final int CMD_GET_AWARD_STATUS = 13009;//7日获取领赏状态-13009
	public static final int CMD_DO_GAIN_AWARD = 13010;//7日领赏-13010
	
	//vip
	public static final int CMD_VIP_BGG = 14001;// 领取vip礼包
	public static final int CMD_VIP_GET = 14002;// 打开vip面板
	
	public static final int CMD_QQVIP = 14804;// QQVIP信息	

	
	//验证码处理
	public static final int CMD_VC_ERR = 18001;// 请求频率过快，自动产生验证码
	public static final int CMD_VC_REFRESH = 18002;// 验证码看不清，重新生成验证码
	public static final int CMD_VC_CHECK = 18003;// 验证码校验	

	// 地图20
	public static final int CMD_FIND_MAPCELLS_BY_RANGE = 20001;// 获取地图
	public static final int CMD_CELL_TOOL_TIP = 20003;// 获取地图
	public static final int CMD_GET_CASTLE_INFO = 20005;// 城池信息面板
	public static final int CMD_MAP_APP_FRIEND = 20006;//地图申请好友-20006
	
	//逐鹿中原命令号占坑 21001 - 22000
	public static final int CMD_CWAR_ENTER = 21010;//进入战场
	public static final int CMD_CWAR_MOVE = 21011;//移动
	public static final int CMD_CWAR_TRANSFOOD = 21012;//卸粮
	public static final int CMD_CWAR_RESUMEARMY = 21013;//补兵	
	public static final int CMD_CWAR_MOVEITEM = 21014;//使用移动道具
	public static final int CMD_CWAR_ATKITEM = 21015;//使用补兵道具

	
	//联盟战(古迹)命令号占坑 21520 - 21550
	public static final int CMD_GWAR_ENTER = 21520;//进入战场
	public static final int CMD_GWAR_MOVE = 21521;//移动
	public static final int CMD_GWAR_COMBAT = 21522;//击杀	
	public static final int CMD_GWAR_USEEFF = 21523;//使用补兵效果	
	public static final int CMD_GWAR_CLEAR = 21524;//清除CD

	//生肖奖励
	public static final int CMD_VIEW_ANINAL = 22001;//生肖兑奖视图
	public static final int CMD_BUY_ANINAL = 22002;//消耗元宝购买
	public static final int CMD_ANIMAL_AWARD = 22003;//兑奖
	
	//二级密码
	public static final int CMD_SET_PASSWORD = 23001;//设置二级密码
	public static final int CMD_PDATE_PASSWORD = 23002;//修改二级密码
	public static final int CMD_DEL_PASSWORD = 23003;//删除二级密码
	public static final int CMD_CHEAK_PASSWORD = 23004;//校验二级密码
	public static final int CMD_GET_PASSWORDINFO = 23005;//获取二级密码次数信息
	
	//24抗击匈奴
	public static final int CMD_HUNS_VIEWACTIVITY = 24001;//活动界面
	public static final int CMD_HUNS_VIEWTIME = 24002;//查看时段界面
	public static final int CMD_HUNS_VIEWJOIN = 24003;//参战界面
	public static final int CMD_HUNS_PAGEJOINLIST = 24004;//分页显示报名列表
	public static final int CMD_HUNS_GUILDAPP = 24005;//申请联盟副本
	public static final int CMD_HUNS_USERAPP = 24006;//联盟成员申请参战
	public static final int CMD_HUNS_CHANGEPOS = 24007;//换防
	public static final int CMD_HUNS_ADDARMY = 24008;//补兵
	public static final int CMD_HUNS_ASSISTFORTRESS = 24009;//支援其他据点
	public static final int CMD_HUNS_NOTICE = 24010;//发援助公告
	public static final int CMD_HUNS_ENTER = 24011;//进入战场
	public static final int CMD_HUNS_EXIT = 24012;//退出战场
	public static final int CMD_HUNS_PAGEVIEWFORTRESS = 24013;//分页查看据点信息
	public static final int CMD_HUNS_PAGEVIEWMESSAGE = 24014;//分页查看据点战报
	public static final int CMD_HUNS_USERGIVEUPAPP = 24015;//放弃参战

	//25霜月迷谷
	public static final int CMD_VALLEY_VIEW = 25001;//活动界面
	public static final int CMD_VALLEY_CALTOKENNUM = 25002;//前台按照后台给定时间调用，刷新行军令数量
	public static final int CMD_VALLEY_LISTSTAR = 25003;//章节的通过星数
	public static final int CMD_VALLEY_BUYTOKEN = 25004;//购买行军令
	public static final int CMD_VALLEY_ENTER = 25005;//进入战场/继续挑战
	public static final int CMD_VALLEY_VIEWDISPACHER = 25006;//配兵界面
	public static final int CMD_VALLEY_BUYRESTRATE = 25007;//购买士兵之魂
	public static final int CMD_VALLEY_DISPACHER = 25008;//分配武将兵力比例
	public static final int CMD_VALLEY_MOVE = 25009;//移动
	public static final int CMD_VALLEY_FIGHT = 25010;//挑战

	// 内政30
	public static final int CMD_LIST_CASTLE_BUILDING = 30001;// 进入城池页面，列出城池里的所有已经建造的建筑
	public static final int CMD_LEVEL_UP_BUILDING = 30003;// 升级建筑
	public static final int CMD_FRESH_CASTLE_DATA = 30005;// 客户端每分钟调用一次
	public static final int CMD_VIEW_BUI_BARRACK = 30008;// 兵营视图-30008
	public static final int CMD_BUI_BARRACK_ADDARMY = 30009;// 按兵种招募兵-30009
	public static final int CMD_BUI_BARRACK_DESTROYARMY = 30010;// 遣散已经招募的兵力-30010
	public static final int CMD_BUI_BARRACK_UPGRADEARMY = 30011;// 士兵升阶-30011
	public static final int CMD_VIEW_BUI_HOSPITAL = 30013;//医馆视图-30013
	public static final int CMD_SET_HOSPITAL_CURE = 30014;// 设置医馆治疗-30014
	public static final int CMD_BUY_RESOURCE_BY_CASH = 30019;// 元宝购买资源-30019
	public static final int CMD_VIEW_OFFICE = 30020;// 官府视图-30020
	public static final int CMD_OFFICE_SEARCH = 30021;// 官府搜索-30021
	public static final int CMD_VIEW_COIN_CAST = 30022;// 铸币炉视图-30022
	public static final int CMD_COINS_CAST = 30023;// 铸币炉-铸币-30023
	public static final int CMD_VIEW_BUI_WALL = 30024;// 城墙视图-30024
	public static final int CMD_PRODUCT_TOWER_ARMY = 30025;// 建造城防-30025
	public static final int CMD_DISTORY_PRODUCT_TOWER_ARMY = 30026;// 拆除城防-30026
	public static final int CMD_GET_CASTLE_DEFPLAN = 30027;// 取得城池守备-30027
	public static final int CMD_SET_CASTLE_DEFPLAN = 30028;// 保存城池守备-30028
	public static final int CMD_VIEW_ACADEMY = 30029;// 书院视图-30029
	public static final int CMD_UPGRADE_TECHNOLOGY = 30030;// 升级科技-30030
	public static final int CMD_LIST_ALL_QUEUE_NEW = 30031;// 刷新并列出所有队列-30031
	public static final int CMD_UPGRADE_QUEUE_BY_TYPE = 30032;// 升级队列-30032
	public static final int CMD_FAST_COOL_QUEUE = 30033;// 快速建造-30033
	public static final int CMD_FAST_COOL_ALL_QUEUE = 30034;// 全部快速建造-30034
	public static final int CMD_UPGRADE_CASTLE = 30035;// 城池升级 -30035
	public static final int CMD_GET_CASTLE_ARMY_OUT = 30036;// 协防部队-30036
	public static final int CMD_ARMY_BACK = 30037;// 协防部队遣返-30037
	
	public static final int CMD_GET_ALL_BUFFTIPS = 30039;// 取城池内的buff-30039
	
	public static final int CMD_CREATE_BRANCH_CASTLE = 30040;//建造分城
	public static final int CMD_TRANSFER_BRANCH_CASTLE = 30041;//转换分城
	public static final int CMD_USE_RESOURCE_EXCHANGE_EXP = 30042;//祭坛兑换经验-30042
	public static final int CMD_GET_RESOURCE_EXCHANGE_EXPNUM = 30043;//祭坛视图-30043
	
	//建筑工
	public static final int CMD_GET_CASTLE_BUILDERS = 30070;// 得到城池建筑工-30070
	public static final int CMD_HIRE_CASTLE_BUILDER = 30071;// 雇佣建筑工--30071
	public static final int CMD_FIRE_CASTLE_BUILDER = 30072;// 解雇建筑工-30072
	public static final int CMD_OPEN_AUTO_BUILD = 30073;// 开启自动建造-30073
	public static final int CMD_CLOSE_AUTO_BUILD = 30074;// 关闭自动建造-30074
	public static final int CMD_GET_AUTO_BUILIST = 30075;// 取得所有自动建造的建筑信息-30075
	
//	public static final int CMD_GET_CASTLE_BUILDER_SEARCH = 30073;// 获取建筑工搜索信息-30073
//	public static final int CMD_FRESH_FIVE_BUILDER = 30074;// 随机刷新建筑工搜索中的建筑工-30074
//	public static final int CMD_GIVEUP_CARD = 30075;// 弃牌-30075
//	public static final int CMD_RESUME_CARD = 30076;// 复牌-30076
//	public static final int CMD_DRAWOFF_CARD = 30077;// 开始抽取-30077
//	public static final int CMD_DELETE_UNHIRE_BUILDER = 30078;// 删除未雇佣的建筑工-30078
	public static final int CMD_GET_FRIENDINFO = 30079;// 取好友信息-30079
//	public static final int CMD_GET_UNHIRE_BUILDERS = 30080;// 取未雇佣的建筑工-30080
	
	
	//武将31
	public static final int CMD_LOAD_SINGLE_HERO_INFO = 31002;//取得单个武将的信息-31002
	public static final int CMD_LOAD_USER_EXP_POOL = 31003;//取君主经验池信息-31003
	public static final int CMD_CHANGE_HERO_NAME = 31004;//武将改名-31004
	public static final int CMD_CHANGE_HERO_EXP = 31005;//经验分配-31005
	public static final int CMD_RELIFE_HERO = 31006;//武将转生-31006
	public static final int CMD_TAKE_GOVPOTZ = 31007;//武将册封官职-31007
	public static final int CMD_TAKEOFF_GOVPOTZ = 31008;//武将免除官职-31008
	public static final int CMD_FIRE_HERO = 31009;//解雇武将-31009
	public static final int CMD_TAKE_EQUIP = 31010;//换装备-31010
	public static final int CMD_TAKEOFF_EQUIP = 31011;//脱装备-31011
	public static final int CMD_VIEW_CHANGE_HERO_ARMY = 31012;//配兵视图-31012
	public static final int CMD_CHANGE_HERO_ARMY = 31013;//武将配兵-31013
	public static final int CMD_LODA_USER_GOVPOTZ = 31015;// 加载用户官职-31015
	public static final int CMD_VIEW_BUI_WG = 31016;// 修炼馆视图-31016
	public static final int CMD_BUI_WG_STOP_PRACTICE = 31017;//停止修炼-31017
	public static final int CMD_BUI_WG_PRACTICE = 31018;//武将修炼-31018
	public static final int CMD_BUI_BAR_HIRE_HERO = 31020;// 酒馆中招募武将-31020
	public static final int CMD_VIEW_ALL_EQUIP = 31021;// 背包里的装备-31021
	public static final int CMD_LOAD_HERO_VIEW_BY_USERID = 31022;// [前台缓存]取得所有武将
	public static final int CMD_CLEAN_HERO_ARMY= 31023;// 清空武将兵力-31023
	public static final int CMD_GET_UNLOCKED_HEROS = 31028;//得到所有解锁武将
	public static final int CMD_DO_GET_SOUL_FROM_BAR = 31029;// 酒馆获取将魂
	public static final int CMD_DOGET_HERO_DETAIL_PROPERTIES = 31035;// 获取武将二级属性
	public static final int CMD_STUDY_HEROSKILL = 31036;// 学习武将技能
	public static final int CMD_FORGET_HEROSKILL = 31037;// 遗忘武将技能
	public static final int CMD_UPGRADE_HEROSKILL = 31038;// 升级武将技能
	
	//收藏夹-32
	public static final int CMD_CASTLE_FAVORITES_ADD = 32101;//收藏玩家城池/联盟基地
	public static final int CMD_CASTLE_FAVORITES_REMOVE = 32102;//删除收藏的玩家城池/联盟基地	
	public static final int CMD_CASTLE_FAVORITES_LIST = 32103;//显示收藏的玩家城池/联盟基地列表	
	public static final int CMD_CASTLE_RANGE_CONFLICT = 32110;//势力范围冲突玩家城池列表
	


	//军团-33
	public static final int CMD_TROOP_CREATE = 33001;//创建军团
	public static final int CMD_TROOP_LIST = 33002;//军团列表	
	public static final int CMD_TROOP_GET = 33003;//军团信息	
	public static final int CMD_TROOP_UPDATE = 33004;//更新军团
	public static final int CMD_TROOP_DELETE = 33005;//解散军团
	public static final int CMD_GET_FORMATIONS = 33007;//获取玩家已有的阵法

	
	//跑商-34
	public static final int CMD_CASTLE_TRADE_GET = 34001;//显示跑商信息
	public static final int CMD_CASTLE_TRADE_CAS_LIST = 34002;//显示跑商联盟城池信息
	public static final int CMD_CASTLE_TRADE_SET = 34003;//设置跑商城池信息
	public static final int CMD_CASTLE_TRADE_START = 34005;//开始跑商	
	public static final int CMD_CASTLE_TRADE_CANCEL = 34006;//取消跑商
	
	//运势
	public static final int CMD_GET_LUCK = 35001;//当前运势- 35001
	public static final int CMD_GET_NOBODY_EVENT = 35002;//当前小人事件- 35002
	public static final int CMD_EXEC_NOBODY_EVENT = 35003;//执行小人事件- 35003
	
	//城池互访
	public static final int CMD_VIEW_VISIT_CASTLE = 36001;//互访城池视图
	public static final int CMD_VISIT_EVENT = 36002;//开宝箱(访问事件)-36002
	public static final int CMD_CASTLE_EVENT = 36003;//点击随机事件(城池事件)-36003
	public static final int CMD_GET_SOCIAL_LIST = 36004;//取得社交信息-36004
	public static final int CMD_HERO_EVENT = 36005;//点击武将事件-36005
	public static final int CMD_LIST_ALL_HERO = 36006;//好友聚贤馆列出所有武将-36006
	public static final int CMD_FIGHT_NPC_EVENT = 36007;//挑战城池事件NPC
	public static final int CMD_FIGHT_PLAYER_EVENT = 36008;//与玩家切磋
	
	//神坛
	public static final int CMD_FRESH_SHRINES_DATA = 37001;//取得神坛信息
	public static final int CMD_PLEASE_GOD = 37002;//请神
	public static final int CMD_SEND_GOD = 37003;//送神
	public static final int CMD_SACRIFICE = 37004;//祭祀
	public static final int CMD_GET_SHRINE_FRIENDINFO = 37005;//取好友信息
	public static final int CMD_SEND_NPC_GOD = 37006;//送神(NPC好友)
	
	//ERROR_CODE
	public static final int ERROR_PAY = -1018; // 支付token无效	
	public static final int ERROR_GETCASH_ERROR = -21010; //获取余额未变化	
	public static final int ERROR_CASH = -21000; // 元宝
	public static final int ERROR_ACTION_POINT = -21001; // 行动力
	public static final int ERROR_HP_POINT = -21002; // 体力
	
	public static final int ERROR_MONEY = -21010; // 铜币不足	
	public static final int ERROR_FOOD = -21011; // 粮食不足	
	public static final int ERROR_WOOD = -21012; // 木材不足
	public static final int ERROR_STONE = -21013; // 石头不足
	public static final int ERROR_BRONZE = -21014; // 矿物不足	
	//public static final int ERROR_USERALTARCARD = 0; // 祭坛庇佑错误	
	
	
	
	// 联盟40
	public static final int ERROR_GUILD = -10040; // 联盟模块异常
	public static final int CMD_GUILD_CREATE = 40001; // 创建联盟
	public static final int CMD_GUILD_DISMISS = 40002;//解散联盟
	public static final int CMD_GET_GUILD_DATA = 40003;//获取联盟信息
	public static final int CMD_QUIT_GUILD = 40004;//退出联盟
	public static final int CMD_CHECK_GUILD_NAME = 40005;//检查联盟名称
	public static final int CMD_GET_GUILD_FLAG = 40006;//随机获取联盟旗号
	public static final int CMD_SEND_INVITATION = 40007;//邀请加入联盟
	public static final int CMD_APPLY_JOIN_GUILD = 40008;//申请加入联盟
	public static final int CMD_AGREE_JOIN_GUILD = 40009;//同意加入联盟
	public static final int CMD_TRANSFER_HEAD = 40010;//转让盟主
	public static final int CMD_PASS_JOIN_GUILD = 40011;//批准加入联盟
	public static final int CMD_PLEASE_OUT_GUILD = 40012;//请离联盟
	public static final int CMD_GET_GUILD_USER = 40013;//获取成员列表
	public static final int CMD_UPDATE_GUILD_DATA = 40014;//修改联盟公告
	public static final int CMD_DISTRIBUTION_POSTS = 40015;//联盟职务分配
	public static final int CMD_RESUME_GUILD = 40016;//恢复解散联盟
	public static final int CMD_GET_HISDONATE_DATA = 40017;//查询历史捐献列表
	public static final int CMD_GET_GUILD_TODAY_DONATE = 40018;//查询今日捐献列表
	public static final int CMD_GET_GUILD_WEEK_DONATE = 40019;//查看联盟周捐献
	public static final int CMD_GUILD_DONATE_DATA = 40020;//联盟捐献
	public static final int CMD_GET_GUILD_BY_PAGE = 40021;//获取所有联盟列表
	public static final int CMD_GET_APPLY_GUILD_TABLE = 40022;//申请列表
	public static final int CMD_GET_GUILD_UPGRADE = 40023;//联盟升级初始化
	public static final int CMD_GUILD_UPGRADE = 40024;//联盟升级
	public static final int CMD_CLEAN_GUILD_APPLY = 40025;//清空联盟申请列表
	public static final int CMD_UPGREADEGUILDHONOUR = 40026;///联盟荣誉升级 
	public static final int CMD_SELECT_GUILDBYLEADER = 40028;//根据盟主的名称获取联盟
 	public static final int CMD_GETAPPLAYUSER = 40029;       //获取联盟邀请列表
 	public static final int CMD_GETUSERBYNAME =  40030;      //根据姓名查找玩家
 	public static final int CMD_CANLCELINVITE = 40031;       //联盟取消成员邀请
 	public static final int CMD_GETGUILDINVITEDATA=40032;    //获取联盟已经邀请的玩家数据
 	public static final int CMD_CANCELTRANSFERHEAD = 40033;  //取消盟主禅让
 	public static final int CMD_REFUSEGUILD = 40034;//玩家主动拒绝联盟邀请加入联盟
 	public static final int CMD_CLEANUSERAPPLY = 40035;//取消联盟申请
 	public static final int CMD_WSYGETUSERAPPLY = 40036;// 获取联盟申请和邀请列表 
 	public static final int CMD_UPDATEGROUPCODE = 40037;//修改联盟成员团队编号
 	public static final int CMD_LOOKGUILD = 40039;//获取联盟信息
	public static final int CMD_GUILD_MERGE_INVITE = 40040; // 合并联盟邀请
	public static final int CMD_GUILD_MERGE_INVITE_LIST = 40041; // 合并联盟邀请列表	
	public static final int CMD_GUILD_MERGE_INVITE_REJECT = 40042; // 拒绝联盟邀请	
	public static final int CMD_GUILD_MERGE_INVITE_AGREE = 40043; // 同意联盟邀请	
	public static final int CMD_GUILD_MERGE_INVITE_CANCEL = 40044; // 取消联盟邀请
	public static final int CMD_MODIFYGUILDFLAGTYPE = 40045;//更换联盟旗帜
	public static final int CMD_GETGUILDS = 40046; //合并联盟邀请列表
	public static final int CMD_GUILD_BY_GUILDNAME = 40047; // 根据联盟名称获取联盟
	public static final int CMD_GETGUILDBYPAGE = 40048;//合并联盟邀请列表
	
	//联盟拍卖 41
//	public static final int CMD_GUILDTRANS_ERROR = -10041;//联盟拍卖异常
	public static final int CMD_GETGUILDTRANS = 410001;//获取联盟玩家所拍卖的物品列表
	public static final int CMD_BUYGUILDTRANSITEM = 410002;//购买联盟道具
	public static final int CMD_CLEANGUILDTRANSITEM = 410003;//玩家取消联盟道具拍卖
	public static final int CMD_SELLMYGUILDTRANSITEM = 410004;//玩家拍出自己的道具上架
	public static final int CMD_GETMYGUILDTRANSITEM = 410005;//玩家获取自己背包里可以拍卖的道具和已经拍卖的道具
	



	// 农场50
	public static final int CMD_REFRESH_FARM = 50001;// 刷新农场
	public static final int CMD_ENTER_FARM = 50002;// 进入农场【刷新自己农场的基本信息】-50002
	public static final int CMD_SEEDING = 50003;// 播种
	public static final int CMD_PLOWING = 50004;// 犁地
	public static final int CMD_GAIN = 50005;// 征收
	public static final int CMD_GAIN_ALL = 50006;// 征收全部
	public static final int CMD_FORWARD_GAIN = 50007;// 提前征收
	public static final int CMD_PICK = 50008;// 采摘
	public static final int CMD_PICK_ALL = 50009;// 全部采摘
	public static final int CMD_WATERING = 50010;// 浇水
	public static final int CMD_TREE_GAIN = 50011;// 摇钱树收获
	public static final int CMD_GET_FARM_NEWS = 50012;// 取农场动态
	public static final int CMD_REFRESH_ALL_AREA = 50013;// 刷新农场地块
	public static final int CMD_REFRESH_ALL_PLANT = 50014;// 刷新农场开工信息
	public static final int CMD_REFRESH_TREE = 50015;// 刷新农场开工信息
	public static final int CMD_GET_FRIEND_INFO = 50016;// 取得社交信息-50016
	public static final int CMD_REFRESH_PET = 50017;//刷新宠物-50017
	public static final int CMD_FEED_USER_PET = 50018;//喂食-50018
	public static final int CMD_HATCH = 50019;//孵蛋-50019
	public static final int CMD_HIT_EGG = 50020;//砸蛋-50020
	public static final int CMD_REFRESH_HOME = 50021;//刷新小屋-50021
	public static final int CMD_UPTODOWN = 50022;//回收-50022
	public static final int CMD_DOWNTOUP = 50023;//摆放-50023
	public static final int CMD_CHANGE_POS = 50024;//改变位置和方向-50024
	public static final int CMD_LIST_ALL_TIPS = 50025;//列出所有的小纸条-50025
	public static final int CMD_CREATE_TIP = 50026;//创建小纸条-50026
	public static final int CMD_DELETE_TIP = 50027;//删除小纸条-50027
	public static final int CMD_CHANGE_CLEANNESS = 50028;//盖印或是擦除印章-50028
	public static final int CMD_GET_FRIEND_BYUSER_NAME = 50029;//按名字取好友-50029
	
	//好友51
	public static final int CMD_FIND_USER = 51001;//查询用户-51001
	public static final int CMD_APP_FRIEND = 51002;//申请好友-51002
	public static final int CMD_ACC_FRIEND = 51003;//同意接受好友--51003
	public static final int CMD_REFUSE_FRIEND = 51004;//拒绝成为好友-51004
	public static final int CMD_GET_FRIENDLIST_BY_GROUPID = 51006;//按组取好友列表-51006
	public static final int CMD_ADD_GROUP = 51007;//好友分组-51007
	public static final int CMD_GET_EVALUTIONVOS = 51008;//取所有的好友评价-51008
	public static final int CMD_SUBMIT_EVALUTION = 51009;//提交好友评价-51009
	public static final int CMD_DELETE_EVALUTION = 51010;//删除单条好友评价-51010
	public static final int CMD_DELETE_MUTIL_EVALUTION = 51011;//删除多条好友评价-51011
	public static final int CMD_CANCEL_APP = 51012;//取消好友申请-51012
	public static final int CMD_ADD_BLACK = 51013;//拉入黑名单-51013
	public static final int CMD_DEL_BLACK = 51014;//从黑名单中删除-51014
	public static final int CMD_DEL_FRIEND = 51015;//删除好友-51015
	public static final int CMD_GET_BLACK = 51016;//取黑名单信息-51016
	public static final int CMD_MODIFY_NOTE = 51018;//修改备注-51018
	public static final int CMD_ADD_ENEMY = 51022;//添加仇人-51022
	public static final int CMD_DELETE_ENEMY = 51023;//删除仇人-51023
	
	
	
	public static final int CMD_START_DEMO_SCENE = 54001;//开始挑战副本-54001
	public static final int CMD_ENTER_DEMO_SCENE = 54002;//进入副本游戏中的画面-54002
	public static final int CMD_DEMOWALK_BY_USER = 54003;//副本模式下用户操作行走-54003

	// 道具60
	public static final int CMD_SHOW_ITEM = 60001;// 查询道具
	public static final int CMD_ITEM_VIEW = 60003;// 显示单个信息
	public static final int CMD_ITEM_BUY = 60005;// 购买道具	
	public static final int CMD_ITEM_DEL = 60007;// 丢弃道具
	public static final int CMD_ITEM_USE = 60009;// 使用道具	
	public static final int CMD_ITEM_ADD = 60011;//添加道具
	public static final int CMD_ITEM_LIST_BYTYPE = 60013;//按类型查询道具	
	public static final int CMD_ITEM_SELL = 60015;//卖出道具
	
	public static final int CMD_SHORTCUT_SET = 60017;//道具快捷栏设置		
	public static final int CMD_SHORTCUT_GET = 60019;//取得道具快捷栏设
	
	public static final int CMD_EQUIP_LEVELUP= 61001;//强化装备
	public static final int CMD_EQUIP_DESTROY = 61003;//分解装备
	public static final int CMD_EQUIP_LEVELUP_TRANSFER = 61005;//强化转移		

	public static final int CMD_EQUIP_ADDGEM= 61011;//镶嵌宝石
	public static final int CMD_UPGRADE_EQUIPED_GEM= 61012;//宝石升级
	public static final int CMD_EQUIP_REMOVEGEM = 61013;//摘除宝石	
	public static final int CMD_GEM_COMPOSE = 61021;//宝石合成	
	public static final int CMD_LOAD_TREASURY_VIEW_BY_USERID = 61022;//[前台缓存用]显示背包道具列表 输入-61022
	public static final int CMD_GET_SHORTCUT_DATAS = 61024;//[前台缓存用]加载快捷栏-61024
	public static final int CMD_GEM_EXCHANGE = 61025;// 宝石转换
	

	//战斗
	public static final int CMD_COMBAT_ARMYOUT = 70001;// 出征
	public static final int CMD_COMBAT_DECLAREWAR = 70002;// 宣战
	public static final int CMD_COMBAT_ARMYOUT_BACK = 70003;// 出征返回
	
	public static final int CMD_COMBAT_LAST_ARMYOUT = 70004;// 最后出征记录
	public static final int CMD_COMBAT_PROVOKE = 70005;// 挑衅NPC	
	public static final int CMD_COMBAT_NPCANIMUS = 70006;// 获取NPC仇恨
	public static final int CMD_COMBAT_GETCOMBAT = 70101;// 获取战斗结果数据	
	
	//勇闯王陵（打塔）
	public static final int CMD_SHOW_TOWER = 71001;// 显示嗒信息
	public static final int CMD_ENTER = 71002;// 进入打塔
	public static final int CMD_SAVE_AUTO_CONFIG = 71003;// 保存自动打塔设置
	public static final int CMD_SET_TROOP = 71004;// 设置军团
	public static final int CMD_START_COMBAT = 71005;// 挑战 or 自动挑战
	public static final int CMD_STOP_COMBAT = 71006;// 停止挑战
	public static final int CMD_EXIT = 71007;// 退出打塔
	public static final int CMD_GET_COMBAT = 71008;// 观战
	
	//竞技场
	public static final int CMD_GET_ARENA_INFO = 72001;// 取得竞技场信息
	public static final int CMD_ARENA_TROOP = 72002;// 显示对手军团	
	public static final int CMD_EXECUTE_COMBAT = 72003;// 竞技场挑战
	public static final int CMD_ARENA_AWARD = 72004;// 竞技场领取奖励
	public static final int CMD_ARENA_RANK = 72005;// 竞技场排行	
	
	//神秘洞
	public static final int CMD_OPEN_CAVE = 73001;// 盟主开启神秘洞
	public static final int CMD_JOINCAVE = 73002;// 加入神秘洞
	public static final int CMD_FIGHT_CAVE = 73003;// 挑战
	public static final int CMD_GET_CAVE_INFO = 73004;// 取得当前的活动信息
	
	// 任务80
	public static final int CMD_SHOW_MISSION = 80001;// 查询任务列表
	public static final int CMD_SHORTCUT_USERMISSION = 80002;// 任务状态快捷显示
	public static final int CMD_COMMIT_MISSION = 80003;// 提交任务

	
	
	
	// 商城90
	public static final int CMD_BUY = 90110;// 买道具
	public static final int CMD_QUERY_POINTS = 90111;// 取货币类型的当前值-90111
	//public static final int CMD_GET_ALL_SHOP_ITEMS = 90112;// 取所有商城道具-90112
	
	// 排行91
	public static final int CMD_GET_USER_RANK = 91001;// 取君主排行-91001
	public static final int CMD_GET_USER_RANK_BY_NAME = 91002;// 按君主名取君主排行-91002
	public static final int CMD_GET_GUILD_RANK = 91003;// 取联盟排行-91003
	public static final int CMD_GET_GUILD_RANK_BY_NAME = 91004;// 按联盟名取联盟排行-91004
	public static final int CMD_GET_DOTA_RANK = 91005;// 取打塔排行-91005

	//联赛92
	public static final int CMD_LEAGUE_GET_INFO = 92001;// 取得联赛主界面数据	
	public static final int CMD_LEAGUE_ENTER = 92002;// 进入联赛大厅	
	public static final int CMD_LEAGUE_JOIN = 92003;// 加入本回合联赛	
	public static final int CMD_LEAGUE_SET_TROOP = 92004;// 选择军团	
	public static final int CMD_LEAGUE_QUIT = 92005;// 退出本回合联赛
	
	//PVP93
	public static final int CMD_PVP_ENTER = 93001;//进入活动
	public static final int CMD_PVP_GIVEUP = 93002;//主动放弃
	public static final int CMD_PVP_BUYTOKEN = 93004;//购买策略令
	public static final int CMD_PVP_CHANGEPOS = 93005;//交换位置
	public static final int CMD_PVP_USESTRATEGY = 93006;//使用策略
	
	// 宣战
	public static final int CMD_DECLARE_WAR_VIEW = 93007;// 宣战视图
	public static final int CMD_DECLARE_WAR_REFRESH = 93008;// 刷新对手	
	public static final int CMD_DECLARE_WAR_DECLARE = 93009;// 宣战
	public static final int CMD_DECLARE_WAR_DEPORTATION = 93010;// 驱逐
	public static final int CMD_PVP_ADD_FRIEND = 93011;//邀请好友
	
	//门客 95
	public static final int CMD_HANGER_VIEW = 95001;// 拜访界面
	public static final int CMD_GETHANGER = 95002;// 拜访
	public static final int CMD_FIGHT_HANGER = 95003;//挑战
	public static final int CMD_HANGER_AWARD = 95004;//领取
	
	
	// 师徒110000
	//110001--110021 see TeacherStudentAction
	
	// 结婚120000
	//120001--120030 see MarriageAction
	
	
	//推广13
	public static final int CMD_FEEDS_SHARE_AWARD = 130000;//分享奖励；
	public static final int CMD_FEEDS_INVITE_INFO = 130010;//获去已领奖信息
	public static final int CMD_FEEDS_INVITE_AWARD = 130011; //好友邀请领奖
	public static final int CMD_FEEDS_INVITE_NUM = 130012;//邀请数量
	public static final int CMD_REACTIVE_ACTION = 130100;//召回老友奖励
	
	//腾讯开放平台14
	public static final int CMD_OPENAPI_PAY_TOKEN = 140000;//获取腾讯交易token
	
}

