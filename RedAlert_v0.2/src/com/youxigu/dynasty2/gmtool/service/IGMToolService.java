package com.youxigu.dynasty2.gmtool.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * GMTool 命令service
 * 
 */
public interface IGMToolService {

	/**
	 * 添加货币值
	 * 
	 * @param openId
	 * @param type
	 *            1=元宝
	 * @param num
	 * @return
	 */
	String doAddMoney(String accId, int type, int num);

	/**
	 * 添加武将
	 * 
	 * @param accId
	 * @param sysHeroId
	 *            武将的配数表id
	 * @return
	 */
	String doAddHero(String accId, int sysHeroId);

	/**
	 * 往背包里面添加物品
	 * 
	 * @param accId
	 * @param itemId
	 * @param itemNum
	 * @return
	 */
	String doAddItemToTreasury(String accId, int itemId, int itemNum);

	/**
	 * 创建指定的建筑，无论限制条件是否满足
	 * 
	 * @param accId
	 * @param buildingEntId
	 * @return
	 */
	String doCreateCastleBuilding(String accId, int buildingEntId);

	/**
	 * 启动指定建筑的升级，无论限制条件是否满足
	 * 
	 * @param accId
	 * @param buildingEntId
	 * @param builderIndex
	 * @return
	 */
	String doUpgradeCastleBuilding(String accId, int buildingEntId,
			int builderIndex);

	/**
	 * 直接修改武将等级
	 * 
	 * @param openId
	 * @param level
	 * @return
	 */
	// String doChangeHeroLevel(String accId, long heroId, int level);

	/**
	 * 加君主坦克经验
	 * 
	 * @param accId
	 * @param exp
	 * @return
	 */
	String doAddUserExp(String accId, int exp);

	/**
	 * 查询sql
	 * 
	 * @param sql
	 * @return
	 */
	String doSqlQuery(String sql);

	/**
	 * @author ninglong
	 *         <p>
	 *         得到一段时间内的在线人数
	 *         </p>
	 * @param start
	 * @param end
	 * @param end
	 *            是否要取历史数据
	 * @return
	 */
	String doGetOnlineUser(Timestamp start, Timestamp end, boolean flag);

	/**
	 * 修改系统参数
	 * 
	 * @param paramId
	 * @param value
	 */
	String doSetSystemParam(String paramId, String value);

	/**
	 * <p>
	 * 发邮件
	 * </p>
	 * 
	 * @param userIds
	 * @param mailTilte
	 * @param mailContent
	 * @param entityIds
	 */
	String doSendMail(String userIds, String mailTilte, String mailContent,
			String entityIds);

	/**
	 * <p>
	 * 发公告
	 * </p>
	 * 
	 * @param noticeContent
	 * @param url
	 */
	String doPublishNotice(String noticeContent, String url, int locale,
			String pf);

	/**
	 * 设置开服时间
	 * 
	 * @param time
	 */
	public void setOpenServerTime(long time);

	/**
	 * GM删除道具
	 * 
	 * @param accId
	 * @param itemId
	 * @param num
	 * @return
	 */
	String doDeleteItem(String openId, int itemId, int num);

	/**
	 * 增加道具
	 * 
	 * @param accId
	 * @param itemId
	 * @param num
	 */
	String doAddItem(String openId, int itemId, int num);

	/**
	 * 加资源 gmtool:addResource param1（账号）param2(0=各种资源, 1=金矿,2=铁矿,3=油矿,4=铀矿)
	 * param3（数量） 例如 gmtool:addResource 123456 0 100000 在聊天里输入
	 * 
	 * @param accId
	 * @param num
	 */
	String doAddResource(String openId, int type, int num);

	/**
	 * 武将加经验
	 * 
	 * @param accId
	 *            账号
	 * @param heroId
	 *            武将id
	 * @param exp
	 *            经验数
	 */
	String doAddExp2Hero(String openId, long heroId, int exp);

	/**
	 * 封禁账号
	 * 
	 * @param userName
	 *            君主角色名
	 * @param second
	 *            封号时间(秒)
	 * @param isBan
	 *            true：封号； false:解封
	 */
	String lockAccount(String openId, long second, int isBan);

	/**
	 * 平台工具发送过来的groovy脚本代码，主要用来动态维护线上数据 应该记录GM操作日志
	 * 
	 * @param script
	 * @return
	 */
	public Object doExecuteScript(String script);

	/**
	 * 禁言
	 * 
	 * @return
	 */
	String doAddToBlackList(String openId, Long seconds);

	/**
	 * 删除memcached key
	 * 
	 * @param key
	 * @return
	 */
	String doDelMemcachedKey(String key);

	public String doOperateActivity(int type, Map<String, Object> data);

	public String doDeleteActivity(int actId);

	public String doAddOrUpdateActivity(Map<String, String> params);

	/**
	 * 增加/修改内部玩家福利数据
	 * 
	 * @param openId
	 * @param num
	 *            每次发放的数量
	 * @param status
	 *            =1 可发放,=0不可发放
	 * @param dttm
	 *            默认 =null,yyyy-MM-dd格式,表示可领取开始日期
	 */
	void doAddOrUpdateUserCashBonus(String openId, int num, int status,
			String dttm);

	/**
	 * 取得所有内部玩家福利数据
	 */
	String getALlUserCashBonus();

	/**
	 * 取得某个内部玩家福利数据
	 * 
	 * @param openId
	 */
	String getUserCashBonus(String openId);

	/**
	 * 内部玩家领取元宝
	 * 
	 * @param userId
	 */
	void doGainUserCashBonus(long userId);

	/**
	 * 增加一个奖励活动
	 * 
	 * @param datas
	 *            id int 活动Id ,必须<br>
	 *            actName String(50) 活动名称,必须<br>
	 *            actDesc String(2048) 活动描述,必须<br>
	 *            type int 活动类型 ,必须 =1节日活动礼包, =2维护活动礼包 icon String(255) 活动图标<br>
	 *            startDttm Timestamp 开始日期,必须<br>
	 *            endDttm Timestamp 结束日期,必须<br>
	 *            minUsrLv int 最小角色等级,default=0<br>
	 *            maxUsrLv int 最大角色等级,default=0<br>
	 *            itemId1 int 道具Id1，道具1-5至少有一个<br>
	 *            num1 int 道具1数量,num1-5至少有一个<br>
	 *            itemId2 int 道具Id2<br>
	 *            num2 int 道具2数量<br>
	 *            itemId3 int 道具Id3<br>
	 *            num3 int 道具3数量<br>
	 *            itemId4 int 道具Id4<br>
	 *            num4 int 道具4数量<br>
	 *            itemId5 int 道具Id5<br>
	 *            num5 int 道具5数量<br>
	 * @return
	 */
	String doAddOrUpdateAwardActivity(Map<String, Object> datas);

	/**
	 * 删除奖励活动
	 * 
	 * @param ids
	 * @return
	 */
	String doDeleteAwardActivity(int[] ids);

	/**
	 * 删除二级密码
	 * 
	 * @param openId
	 * @param areaId
	 * @return
	 */
	String doGMDelPassword(String openId, String areaId);

	/**
	 * 配置运营礼包
	 * 
	 * @param itemId
	 * @param items
	 * @param itemDesc
	 * @param bagName
	 * @param leve
	 * @param icon
	 * @return
	 */
	String doConfigBag(int itemId, List<Map<String, Integer>> items,
			String itemDesc, String bagName, int leve, String icon);

	/**
	 * GM同步论坛内容
	 *
	 * @param type
	 *            2,上周热帖，3，本周热帖
	 * @param dataList
	 */
	public void doGmSynForumContext(int type, List<Map<String, Object>> dataList);

	/**
	 * gm创建邮件
	 * 
	 * @param sendUserId
	 * @param receiveUserId
	 * @param sendTime
	 * @param readTime
	 * @param title
	 * @param comment
	 * @param status
	 * @param mailType
	 * @param appendix
	 * @return
	 */
	String createMailInternal(long sendUserId, long receiveUserId,
			String sendTime, String readTime, String title, String comment,
			byte status, byte mailType, String appendix);

	/**
	 * gm创建任务
	 * 
	 * @param openId
	 * @param areaId
	 * @param missionId
	 * @return
	 */
	String doGmAddMission(String openId, String areaId, int missionId);
	
	/**
	 * 直接设置vip等级
	 * @param accId
	 * @param vip
	 * @return
	 */
	String doSetUserVip(String accId, int vip);
}