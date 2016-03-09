package com.youxigu.dynasty2.log;

/**
 * 定义经分系统的表以其字段
 * 
 * @author ninglong
 * 
 */
public enum TLogTable {
	//*******************************经分日志开始*****************************************
	//	 <!--//////////////////////////////////////////////
	//	    ///////服务器状态日志///////////////////////////////
	//	    /////////////////////////////////////////////////-->
	//	  <struct  name="GameSvrState"  version="1" desc="(必填)服务器状态流水，每分钟一条日志">
	//	    <entry  name="dtEventTime"		type="datetime"					desc="(必填) 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameIP"			type="string"		size="32"						desc="(必填)服务器IP" />
	//	  </struct>
	//	  
	//		<!--//////////////////////////////////////////////
	//	    ///////用户在线表///////////////////////////////
	//	    /////////////////////////////////////////////////-->
	//	   <struct  name="PlayerOnline"  version="1" desc="(必填)玩家在线人数，每5分钟记录一条日志">
	//		<entry  name="num"		type="int"						defaultvalue="0"	desc="(必填)在线人数"/>
	//		<entry  name="reg"		type="int"						defaultvalue="0"	desc="(必填)5分钟注册数"/>
	//		<entry  name="login"		type="int"						defaultvalue="0"	desc="(必填)5分钟之内登陆数"/>
	//	    <entry  name="dtEventTime"		type="datetime"					desc="(必填) 格式 YYYY-MM-DD HH:MM:30   MM为5的整倍数  SS为30" />
	//		<entry  name="PlatID"			type="int"						defaultvalue="0"	desc="(必填)ios 0 /android 1"/>
	//	    <entry  name="GameSvrId"			type="string"		size="25"	desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	  </struct>

	/**
	 * 用户在线表
	 */
	TLOG_PLAYERONLINE("PlayerOnline", new String[] { "num", "reg", "login", "dtEventTime", "PlatID", "GameSvrId",
			"LoginChannel" }),

	//	  <!--//////////////////////////////////////////////
	//		///////玩家注册表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <struct  name="PlayerRegister"  version="1" desc="(必填)玩家注册">
	//	    <entry  name="GameSvrId"			type="string"		size="25"	desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"		type="datetime"					desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"			type="string"		size="32"						desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			type="int"						defaultvalue="0"	desc="(必填)ios 0 /android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"			type="string"		size="64"						desc="(必填)用户OPENID号" />
	//	    <entry  name="ClientVersion"		type="string"		size="64"	defaultvalue="NULL" desc="(可选)客户端版本"/>
	//	    <entry  name="SystemSoftware"	type="string"		size="64"	defaultvalue="NULL" desc="(必填)移动终端操作系统版本"/>
	//	    <entry  name="SystemHardware"	type="string"		size="64"	defaultvalue="NULL" desc="(必填)移动终端机型"/>
	//	    <entry  name="TelecomOper"		type="string"		size="64"	defaultvalue="NULL" desc="(必填)运营商"/>
	//	    <entry  name="Network"			type="string"		size="64"	defaultvalue="NULL" desc="(可选)3G/WIFI/2G"/>
	//	    <entry  name="ScreenWidth"		type="int"						defaultvalue="0"	desc="(必填)显示屏宽度"/>
	//	    <entry  name="ScreenHight"		type="int"						defaultvalue="0"	desc="(必填)显示屏高度"/>
	//	    <entry  name="Density"			type="float"					defaultvalue="0"	desc="(必填)像素密度"/>
	//	    <entry  name="RegChannel"		type="int"						defaultvalue="0"	desc="(必填)注册渠道"/>
	//	    <entry  name="CpuHardware"		type="string"		size="64"	defaultvalue="NULL" desc="(可选)cpu类型|频率|核数"/>
	//	    <entry  name="Memory"			type="int"						defaultvalue="0"	desc="(可选)内存信息单位M"/>
	//	    <entry  name="GLRender"			type="string"		size="64"	defaultvalue="NULL" desc="(可选)opengl render信息"/>
	//	    <entry  name="GLVersion"			type="string"		size="64"	defaultvalue="NULL" desc="(可选)opengl版本信息"/>
	//	    <entry  name="DeviceId"			type="string"		size="64"	defaultvalue="NULL"	desc="(可选)设备ID"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//		<entry  name="Ip"			type="string"		size="64"						desc="(必填)玩家登录IP" />
	//	  </struct>

	TLOG_PLAYERREGISTER("PlayerRegister", new String[] { "GameSvrId", "dtEventTime", "vGameAppid", "PlatID", "ZoneID",
			"vopenid", "ClientVersion", "SystemSoftware", "SystemHardware", "TelecomOper", "Network", "ScreenWidth",
			"ScreenHight", "Density", "RegChannel", "CpuHardware", "Memory", "GLRender", "GLVersion", "DeviceId",
			"RoleId", "Ip" }),
	//
	//	     <!--//////////////////////////////////////////////
	//		///////玩家登录表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <struct  name="PlayerLogin"  version="1" desc="(必填)玩家登陆">
	//	    <entry  name="GameSvrId"         type="string"		size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"		type="datetime"											desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"         type="string"		size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			type="int"						defaultvalue="0"		desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"            type="string"		size="64"							desc="(必填)用户OPENID号" />
	//	    <entry  name="Level"             type="int"												desc="(必填)等级" />
	//	    <entry  name="PlayerFriendsNum"  type="int"												desc="(必填)玩家好友数量"/>
	//	    <entry  name="ClientVersion"		type="string"		size="64"	defaultvalue="NULL"		desc="(必填)客户端版本"/>
	//	    <entry  name="SystemSoftware"	type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端操作系统版本"/>
	//	    <entry  name="SystemHardware"	type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端机型"/>
	//	    <entry  name="TelecomOper"		type="string"		size="64"	defaultvalue="NULL"		desc="(必填)运营商"/>
	//	    <entry  name="Network"			type="string"		size="64"	defaultvalue="NULL"		desc="(必填)3G/WIFI/2G"/>
	//	    <entry  name="ScreenWidth"		type="int"						defaultvalue="0"		desc="(必填)显示屏宽度"/>
	//	    <entry  name="ScreenHight"		type="int"						defaultvalue="0"		desc="(必填)显示屏高度"/>
	//	    <entry  name="Density"			type="float"					defaultvalue="0"		desc="(必填)像素密度"/>
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="CpuHardware"		type="string"		size="64"	defaultvalue="NULL"		desc="(可选)cpu类型|频率|核数"/>
	//	    <entry  name="Memory"			type="int"						defaultvalue="0"		desc="(可选)内存信息单位M"/>
	//	    <entry  name="GLRender"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl render信息"/>
	//	    <entry  name="GLVersion"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl版本信息"/>
	//	    <entry  name="DeviceId"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)设备ID"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//		<entry  name="Ip"			type="string"		size="64"						desc="(必填)玩家登录IP" />
	//	  </struct>

	TLOG_PLAYERLOGIN("PlayerLogin", new String[] { "GameSvrId", "dtEventTime", "vGameAppid", "PlatID", "ZoneID",
			"vopenid", "Level", "PlayerFriendsNum", "ClientVersion", "SystemSoftware", "SystemHardware", "TelecomOper",
			"Network", "ScreenWidth", "ScreenHight", "Density", "LoginChannel", "CpuHardware", "Memory", "GLRender",
			"GLVersion", "DeviceId", "RoleId", "Ip" }),

	//	 
	//	  <!--//////////////////////////////////////////////
	//		///////玩家登出表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <struct  name="PlayerLogout" version="1" desc="(必填)玩家登出">
	//	    <entry  name="GameSvrId"          type="string"      size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"		 type="datetime"										desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"          type="string"		size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			 type="int"						defaultvalue="0"		desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"             type="string"		size="64"							desc="(必填)用户OPENID号" />
	//	    <entry  name="OnlineTime"		 type="int"												desc="(必填)本次登录在线时间(秒)" />
	//	    <entry  name="Level"				 type="int"												desc="(必填)等级" />
	//	    <entry  name="PlayerFriendsNum"   type="int"												desc="(必填)玩家好友数量"/>
	//	    <entry  name="ClientVersion"		 type="string"		size="64"	defaultvalue="NULL"		desc="(必填)客户端版本"/>
	//	    <entry  name="SystemSoftware"	 type="string"		size="64"	defaultvalue="NULL"		desc="(可选)移动终端操作系统版本"/>
	//	    <entry  name="SystemHardware"	 type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端机型"/>
	//	    <entry  name="TelecomOper"		 type="string"		size="64"	defaultvalue="NULL"		desc="(必填)运营商"/>
	//	    <entry  name="Network"			 type="string"		size="64"	defaultvalue="NULL"		desc="(必填)3G/WIFI/2G"/>
	//	    <entry  name="ScreenWidth"		 type="int"						defaultvalue="0"		desc="(可选)显示屏宽度"/>
	//	    <entry  name="ScreenHight"		 type="int"						defaultvalue="0"		desc="(可选)显示高度"/>
	//	    <entry  name="Density"			 type="float"					defaultvalue="0"		desc="(可选)像素密度"/>
	//	    <entry  name="LoginChannel"		 type="int"						defaultvalue="0"		desc="(可选)登录渠道"/>
	//	    <entry  name="CpuHardware"		 type="string"		size="64"	defaultvalue="NULL"		desc="(可选)cpu类型;频率;核数"/>
	//	    <entry  name="Memory"			 type="int"						defaultvalue="0"		desc="(可选)内存信息单位M"/>
	//	    <entry  name="GLRender"			 type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl render信息"/>
	//	    <entry  name="GLVersion"			 type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl版本信息"/>
	//	    <entry  name="DeviceId"			 type="string"		size="64"	defaultvalue="NULL"		desc="(可选)设备ID"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//	  </struct>

	TLOG_PLAYERLOGOUT("PlayerLogout", new String[] { "GameSvrId", "dtEventTime", "vGameAppid", "PlatID", "ZoneID",
			"vopenid", "OnlineTime", "Level", "PlayerFriendsNum", "ClientVersion", "SystemSoftware", "SystemHardware",
			"TelecomOper", "Network", "ScreenWidth", "ScreenHight", "Density", "LoginChannel", "CpuHardware", "Memory",
			"GLRender", "GLVersion", "DeviceId", "RoleId" }),

	//
	//
	//	   <!--//////////////////////////////////////////////
	//		///////游戏货币流水表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <macrosgroup name="ADDORREDUCE">
	//	    <macro name="ADD"       value="0" desc="加"/>
	//	    <macro name="REDUCE"    value="1" desc="减"/>
	//	  </macrosgroup>
	//
	//	  <macrosgroup name="iMoneyType">
	//	    <macro name="MT_MONEY"       value="0" desc="游戏币"/>
	//	    <macro name="MT_DIAMOND"     value="1" desc="钻石"/>
	//	  </macrosgroup>
	//	  
	//	  <struct  name="MoneyFlow" version="1" desc="(必填)货币流水">
	//	    <entry  name="GameSvrId"        type="string"      size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"      type="datetime"										desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"        type="string"      size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"		   type="int"						defaultvalue="0"	desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"           type="string"      size="64"							desc="(必填)用户OPENID号" />
	//	    <entry  name="Sequence"		   type="int"											desc="(可选)用于关联一次动作产生多条不同类型的货币流动日志" />
	//	    <entry  name="Level"            type="int"											desc="(必填)玩家等级" />
	//	    <entry  name="AfterMoney"       type="int"       									desc="(可选)动作后的金钱数" />
	//	    <entry  name="iMoney"            type="int"       									desc="(必填)动作涉及的金钱数" />
	//	    <entry  name="Reason"           type="int"       									desc="(必填)货币流动一级原因" />
	//	    <entry  name="SubReason"        type="int"       									desc="(必填) 功能消费 1/非功能能消费 0 功能消费指界面上直接扣元宝的不产生道具转换" />
	//	    <entry  name="AddOrReduce"      type="int"											desc="(必填)增加 0/减少 1" />
	//	    <entry  name="iMoneyType"        type="int"											desc="(必填)钱的类型MONEYTYPE" />
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//		<entry  name="Rmb"			type="int"						desc="(可选)充值的RMB数量" />
	//	  </struct>
	//
	//
	TLOG_MONEYFLOW("MoneyFlow", new String[] { "GameSvrId", "dtEventTime", "vGameAppid", "PlatID", "ZoneID", "vopenid",
			"Sequence", "Level", "AfterMoney", "iMoney", "Reason", "SubReason", "AddOrReduce", "iMoneyType",
			"LoginChannel", "RoleId", "Rmb" }),

	//
	//
	//	   <!--//////////////////////////////////////////////
	//		///////道具流水表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <macrosgroup name="ADDORREDUCE">
	//	    <macro name="ADD"       value="0" desc="加"/>
	//	    <macro name="REDUCE"    value="1" desc="减"/>
	//	  </macrosgroup>
	//
	//
	//	  <macrosgroup name="iMoneyType">
	//	    <macro name="MT_MONEY"       value="0" desc="游戏币"/>
	//	    <macro name="MT_DIAMOND"     value="1" desc="钻石"/>
	//	  </macrosgroup>
	//	  
	//	  <struct  name="ItemFlow" version="1" desc="(必填)道具流水表">
	//	    <entry  name="GameSvrId"      type="string"       size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"    type="datetime"										desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"      type="string"       size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"         type="int"							defaultvalue="0"    desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"         type="string"       size="64"							desc="(必填)玩家" />
	//	    <entry  name="Level"          type="int"												desc="(必填)玩家等级" />
	//	    <entry  name="Sequence"			 type="int"												desc="(必填)用于关联一次购买产生多条不同类型的货币日志" />
	//	    <entry  name="iGoodsType"       type="int"												desc="(必填)道具类型" />
	//	    <entry  name="iGoodsId"         type="int"												desc="(必填)道具ID" />
	//	    <entry  name="Count"          type="int"												desc="(必填)数量" />
	//	    <entry  name="AfterCount"			type="int"											desc="(必填)动作后的物品存量" />
	//	    <entry  name="Reason"				type="int"       									desc="(必填)道具流动一级原因" />
	//	    <entry  name="SubReason"				type="int"       									desc="(必填)道具流动二级原因" />
	//	    <entry  name="iMoney"          type="int"												desc="(必填)花费代币或金币购买道具情况下输出消耗的钱数量，否则填0" />
	//	    <entry  name="iMoneyType"      type="int"												desc="(必填)钱的类型MONEYTYPE,其它货币类型参考FAQ文档" />
	//	    <entry  name="AddOrReduce"           type="int"											desc="(必填)增加 0/减少 1" />
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//	  </struct>

	TLOG_ITEMFLOW("ItemFlow", new String[] { "GameSvrId", "dtEventTime", "vGameAppid", "PlatID", "ZoneID", "vopenid",
			"Level", "Sequence", "iGoodsType", "iGoodsId", "Count", "AfterCount", "Reason", "SubReason", "iMoney",
			"iMoneyType", "AddOrReduce", "LoginChannel", "RoleId" }),
	//
	//
	//		<!--//////////////////////////////////////////////
	//		///////人物等级流水表///////////////////////////////
	//	   /////////////////////////////////////////////////-->	
	//
	//	  <struct  name="PlayerExpFlow" version="1" desc="(可选)人物等级流水表">
	//	    <entry  name="GameSvrId"          type="string"        size="25"						desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"        type="datetime"									desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"          type="string"		  size="32"						desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			       type="int"						defaultvalue="0"	desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"             type="string"        size="64"						desc="(必填)玩家" />
	//	    <entry  name="ExpChange"          type="int"											desc="(必填)经验变化" />
	//	    <entry  name="BeforeLevel"        type="int"											desc="(可选)动作前等级" />
	//	    <entry  name="AfterLevel"         type="int"											desc="(必填)动作后等级" />
	//	    <entry  name="Time"				       type="int"											desc="(必填)升级所用时间(秒)" />
	//	    <entry  name="Reason"             type="int"       									desc="(必填)经验流动一级原因" />
	//	    <entry  name="SubReason"          type="int"       									desc="(必填)经验流动二级原因" />
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//	  </struct>

	TLOG_PLAYEREXPFLOW("PlayerExpFlow", new String[] { "GameSvrId", "dtEventTime", "vGameAppid", "PlatID", "ZoneID",
			"vopenid", "ExpChange", "BeforeLevel", "AfterLevel", "Time", "Reason", "SubReason", "LoginChannel",
			"RoleId" }),

	//
	//
	//	  <!--//////////////////////////////////////////////
	//		///////社交系统流水表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <macrosgroup name="SNSTYPE">
	//	    <macro name="SNSTYPE_SHOWOFF"                 value="0" desc="炫耀"/>
	//	    <macro name="SNSTYPE_INVITE"                  value="1" desc="邀请"/>
	//	    <macro name="SNSTYPE_SENDHEART"               value="2" desc="送心"/>
	//	    <macro name="SNSTYPE_RECEIVEHEART"            value="3" desc="收取心"/>
	//	    <macro name="SNSTYPE_SENDEMAIL"               value="4" desc="发邮件"/>
	//	    <macro name="SNSTYPE_RECEIVEEMAIL"            value="5" desc="收邮件"/>
	//	    <macro name="SNSTYPE_SHARE"                    value="6" desc="分享"/>
	//	    <macro name="SNSTYPE_OTHER"                   value="7" desc="其他原因"/>
	//	  </macrosgroup>
	//
	//	    
	//	  <struct     name="SnsFlow" version="1" desc="(必填)SNS流水">
	//	    <entry  name="GameSvrId"         type="string"        size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"       type="datetime"											desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"         type="string"        size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"            type="int"							defaultvalue="0"	desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="ActorOpenID"       type="string"        size="64"					desc="(必填)动作发起玩家" />
	//	    <entry  name="RecNum"             type="int"												desc="(可选)接收玩家个数"/>
	//	    <entry  name="Count"			  type="int"												desc="(必填)发送的数量"/>
	//	    <entry  name="SNSType"           type="int"										desc="(必填)交互一级类型,其它说明参考FAQ文档" />
	//	    <entry  name="SNSSubType"        type="int"										desc="(可选)交互二级类型" />
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	     <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//	  </struct>
	//
	//
	//
	//	   <!--//////////////////////////////////////////////
	//		///////单局流水表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <macrosgroup name="BATTLETYPE">
	//	    <macro name="BATTLE_PVE"       value="0" desc="单人游戏"/>
	//	    <macro name="BATTLE_PVP"       value="1" desc="对战游戏"/>	
	//	    <macro name="BATTLE_OTHER"     value="2" desc="其他对局"/>
	//	  </macrosgroup>
	//	  
	//	  <struct name="RoundFlow" version="1" desc="(必填)单局结束数据流水">
	//	    <entry  name="GameSvrId"          type="string"		  size="25"					desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"        type="datetime"										desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"          type="string"		  size="32"				desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			 type="int"							defaultvalue="0"	desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"             type="string"        size="64"			desc="(必填)玩家" />
	//	    <entry  name="BattleID"           type="int"												desc="(必填)本局id" />
	//	    <entry  name="BattleType"         type="int"						desc="(必填)战斗类型 对应BATTLETYPE" />
	//	    <entry  name="RoundScore"         type="int"												desc="(必填)本局分数" />
	//	    <entry  name="RoundTime"         type="int"												desc="(必填)对局时长(秒)" />
	//	    <entry  name="Result"             type="int"												desc="(必填)单局结果" />
	//	    <entry  name="Rank"               type="int"												desc="(必填)排名" />
	//	    <entry  name="Gold"               type="int"												desc="(必填)金钱" />
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//	  </struct>
	//
	//
	//	  <!--//////////////////////////////////////////////
	//		///////玩家Crash 表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//	  <struct  name="PlayerCrash"  version="1" desc="(必填)玩家崩溃表">
	//	    <entry  name="GameSvrId"         type="string"		size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="dtEventTime"		type="datetime"											desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"         type="string"		size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			type="int"						defaultvalue="0"		desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"            type="string"		size="64"							desc="(必填)用户OPENID号" />
	//	    <entry  name="Level"             type="int"												desc="(必填)等级" />
	//	    <entry  name="PlayerFriendsNum"  type="int"												desc="(必填)玩家好友数量"/>
	//	    <entry  name="ClientVersion"		type="string"		size="64"	defaultvalue="NULL"		desc="(必填)客户端版本"/>
	//	    <entry  name="SystemSoftware"	type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端操作系统版本"/>
	//	    <entry  name="SystemHardware"	type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端机型"/>
	//	    <entry  name="TelecomOper"		type="string"		size="64"	defaultvalue="NULL"		desc="(必填)运营商"/>
	//	    <entry  name="Network"			type="string"		size="64"	defaultvalue="NULL"		desc="(必填)3G/WIFI/2G"/>
	//	    <entry  name="ScreenWidth"		type="int"						defaultvalue="0"		desc="(必填)显示屏宽度"/>
	//	    <entry  name="ScreenHight"		type="int"						defaultvalue="0"		desc="(必填)显示屏高度"/>
	//	    <entry  name="Density"			type="float"					defaultvalue="0"		desc="(必填)像素密度"/>
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="CpuHardware"		type="string"		size="64"	defaultvalue="NULL"		desc="(可选)cpu类型|频率|核数"/>
	//	    <entry  name="Memory"			type="int"						defaultvalue="0"		desc="(可选)内存信息单位M"/>
	//	    <entry  name="GLRender"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl render信息"/>
	//	    <entry  name="GLVersion"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl版本信息"/>
	//	    <entry  name="DeviceId"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)设备ID"/>
	//	    <entry  name="RoleId"			type="string"		size="64"						desc="(必填)角色ID" />
	//	  </struct>
	//
	//
	//
	//	  <!--//////////////////////////////////////////////
	//		///////玩家Demo 表///////////////////////////////
	//	   /////////////////////////////////////////////////-->
	//
	//	  <macrosgroup name="Reason">
	//	    <macro       value="0" desc="启动游戏      stEventTime必填 ltEventTime cteEventTime itEventTime不必填"/>
	//	    <macro       value="1" desc="资源加载完成  stEventTime ltEventTime 必填 cteEventTime itEventTime不必填"/>
	//	    <macro       value="2" desc="创建角色      stEventTime ltEventTime cteEventTime 必填 itEventTime不必填"/>	
	//	    <macro       value="3" desc="进入游戏      stEventTime ltEventTime cteEventTime itEventTime 必填"/>
	//	    
	//	  </macrosgroup>
	//	  
	//	  <struct  name="PlayerDemo"  version="1" desc="(必填)玩家登陆流程">
	//	    <entry  name="GameSvrId"         type="string"		size="25"							desc="(必填)登录的游戏服务器编号" />
	//	    <entry  name="stEventTime"		type="datetime"											desc="(必填)启动游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="ltEventTime"		type="datetime"											desc="(可选)加载事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="ctEventTime"		type="datetime"											desc="(可选)创建事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="itEventTime"		type="datetime"											desc="(可选)进入游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
	//	    <entry  name="vGameAppid"         type="string"		size="32"							desc="(必填)游戏APPID" />
	//	    <entry  name="PlatID"			type="int"						defaultvalue="0"		desc="(必填)ios 0/android 1"/>
	//	    <entry  name="ZoneID"			type="int"						defaultvalue="0"	desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
	//	    <entry  name="vopenid"            type="string"		size="64"							desc="(可选)用户OPENID号" />
	//	    <entry  name="Reason"           type="int"       									desc="(必填)操作原因" />
	//	    <entry  name="ClientVersion"		type="string"		size="64"	defaultvalue="NULL"		desc="(必填)客户端版本"/>
	//	    <entry  name="SystemSoftware"	type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端操作系统版本"/>
	//	    <entry  name="SystemHardware"	type="string"		size="64"	defaultvalue="NULL"		desc="(必填)移动终端机型"/>
	//	    <entry  name="TelecomOper"		type="string"		size="64"	defaultvalue="NULL"		desc="(必填)运营商"/>
	//	    <entry  name="Network"			type="string"		size="64"	defaultvalue="NULL"		desc="(必填)3G/WIFI/2G"/>
	//	    <entry  name="ScreenWidth"		type="int"						defaultvalue="0"		desc="(必填)显示屏宽度"/>
	//	    <entry  name="ScreenHight"		type="int"						defaultvalue="0"		desc="(必填)显示屏高度"/>
	//	    <entry  name="Density"			type="float"					defaultvalue="0"		desc="(必填)像素密度"/>
	//	    <entry  name="LoginChannel"		type="int"						defaultvalue="0"		desc="(必填)登录渠道"/>
	//	    <entry  name="CpuHardware"		type="string"		size="64"	defaultvalue="NULL"		desc="(可选)cpu类型|频率|核数"/>
	//	    <entry  name="Memory"			type="int"						defaultvalue="0"		desc="(可选)内存信息单位M"/>
	//	    <entry  name="GLRender"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl render信息"/>
	//	    <entry  name="GLVersion"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)opengl版本信息"/>
	//	    <entry  name="DeviceId"			type="string"		size="64"	defaultvalue="NULL"		desc="(可选)设备ID"/>
	//	  </struct>
	//*******************************经分日志结束*****************************************

	// //////////////////////不要改变字段的顺序，tx tlog必须基于顺序
//	/**
	// * 商城购买
	// */
	// TLOG_BUYITEM("tlog_buyitem", new String[] { "areaId", "userId", "itemId",
	// "itemName", "payType", "num", "price", "dttm", "via",
	// "userCreateDttm" }),
	// /**
//	 * 道具（增加、消耗）
//	 */
//	TLOG_ITEMLOG("tlog_item", new String[] { "areaId", "logType", "userId",
//			"itemId", "itemName", "num", "reason", "dttm", "via",
//			"userCreateDttm","beforeNum","afterNum" }),
//	/**
//	 * 代币
//	 */
//	TLOG_CASH("tlog_cash", new String[] { "areaId", "logType", "userId",
//			"cashType", "olderNum", "num", "reason", "dttm", "via",
//			"userCreateDttm", "openId", "userName", "usrLv", "accId" }),
	// /**
	// * Q币Q点金券消费日至
	// */
	// TLOG_QB("tlog_qb", new String[] { "areaId", "openId", "userId", "qb",
	// "quan", "dttm" }),

//	/**
	// * 登录登出
	// */
	// TLOG_LOGIN("tlog_login", new String[] { "areaId", "logType", "accId",
	// "openId", "usrLv", "pf", "via", "dttm", "loginIp", "onlineTime",
	// "userCreateDttm" }),
	// /**
	// * 用户信息日志
	// */
	// TLOG_USERINFO("tlog_userinfo", new String[] { "areaId", "logType",
	// "accId",
	// "openId", "userId", "usrLv", "pf", "dttm", "via", "userCreateDttm",
	// "userName" }),
	// /**
//	 * 在线信息
//	 */
//	TLOG_ONLINE("tlog_online", new String[] { "areaId", "dttm", "num" }),
//	/**
//	 * 任务
//	 */
//	TLOG_MISSION("tlog_mission", new String[] { "areaId", "logType", "userId",
//			"usrLv", "missionId", "missionName", "dttm", "flag", "via",
//			"userCreateDttm" }),
//	/**
//	 * 君主任务
//	 */
	// TLOG_USERTASK("tlog_usertask", new String[] { "areaId", "userId",
	// "userLv",
	// "taskId", "star", "flag", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 联盟操作
	// */
	// TLOG_GUILDACTION("tlog_guildaction", new String[] { "areaId", "logType",
	// "guildId", "guildLv", "userId", "usrLv", "dttm", "via",
	// "userCreateDttm" }),
	// /**
	// * 联盟合并
	// */
	// TLOG_GUILDMERGE("tlog_guildmerge", new String[] { "areaId", "userId",
	// "guildId", "guildLv", "mGuildId", "mGuildLv", "dttm", "via",
	// "userCreateDttm" }),
	// /**
	// * 联盟捐款
	// */
	// TLOG_GUILDDONATE("tlog_guilddonate", new String[] { "areaId", "logType",
	// "guildId", "guildLv", "userId", "userLv", "num", "dttm", "via",
	// "userCreateDttm","construction"}),
	// /**
	// * 请神
	// // */
	// TLOG_GOD("tlog_god", new String[] { "areaId", "logType", "userId",
	// "userLv", "dttm", "godId", "targetName", "via", "userCreateDttm" }),
	// /**
	// * 玩家操作
	// */
	// TLOG_ACTIONCOUNT("tlog_actioncount", new String[] { "areaId", "logType",
	// "userId", "userLv", "num", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 朝贡
	// */
	// TLOG_TRIBUTE("tlog_tribute", new String[] { "areaId", "logType",
	// "userId",
	// "userLv", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 出征
	// */
	// TLOG_ARMYOUT("tlog_armyout",
	// new String[] { "areaId", "logType", "userId", "userLv",
	// "rangeValue", "targetType", "targetUserId", "targetUserLv",
	// "targetRangeValue", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 重楼
	// */
	// TLOG_TOWER("tlog_tower", new String[] { "areaId", "logType", "userId",
	// "userLv", "num", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 百战
	// */
	// TLOG_LEAGUE("tlog_league", new String[] { "areaId", "userId", "userLv",
	// "num", "honor", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 战役
	// */
	// TLOG_RISK("tlog_risk", new String[] { "areaId", "logType", "userId",
	// "userLv", "sceneId", "sceneName", "star", "win", "dttm", "via",
	// "userCreateDttm", "pSceneId","npcId","combatNUm" }),

	// /**
	// * 竞技场
	// */
	// TLOG_ARENA("tlog_arena", new String[] { "areaId", "userId", "rankId",
	// "dUserId", "dRankId", "todayNum", "win", "dttm", "via",
	// "userCreateDttm" }),
	// /**
	// * 冒险奖励
	// */
	// TLOG_RISK_AWARD("tlog_riskaward", new String[] { "areaId", "userId",
	// "userLv", "pSceneId", "award", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 多人冒险副本
	// */
	// @Deprecated
	// TLOG_MUTLI_RISK("tlog_mutli_risk", new String[] { "areaId", "logType",
	// "userNum", "userIds", "sceneId", "sceneName", "sceneType",
	// "pSceneId", "dttm" }),
	// /**
	// * 好友事件
	// */
	// TLOG_CASTLEEVENT("tlog_castleevent", new String[] { "areaId", "logType",
	// "userId", "userLv", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 武将传承
	// */
	// TLOG_HEROINHERIT("tlog_heroinherit", new String[] { "areaId", "userId",
	// "userLv", "heroId", "sysHeroId", "heroLv", "heroLvTo", "reLifeNum",
	// "reLifeNumTo", "olderExp", "expTo", "growing", "growingTo",
	// "toHeroId", "toSysHeroId", "toHeroLv", "toHeroLvTo", "toReLifeNum",
	// "toReLifeNumTo", "toExp", "toExpTo", "toGrowing", "toGrowingTo",
	// "dttm", "via", "userCreateDttm" }),
	// /**
	// * 武将兵符转移
	// */
	// TLOG_HEROARMYTRANS("tlog_heroarmytrans", new String[] { "areaId",
	// "userId",
	// "userLv", "heroId", "armyEntId", "armyEntIdTo", "toHeroId",
	// "toArmyEntId", "toArmyEntIdTo", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 转换国家
	// */
	// TLOG_CHANGECOUNTRY("tlog_changecountry", new String[] { "areaId",
	// "userId",
	// "userLv", "oldCountryId", "newCountryId", "dttm", "via",
	// "userCreateDttm" }),
	// /**
	// * 玩家注册角色前的演示
	// */
	// TLOG_DEMO("tlog_demo", new String[] { "areaId", "accId", "openId",
	// "step",
	// "dttm", "via", "userCreateDttm" }),
	// /**
	// * 请神boss
	// */
	// TLOG_SHRINESBOSS("tlog_shrinesboss", new String[] { "areaId", "logType",
	// "userId", "userLv", "bossId", "mark", "dttm", "via",
	// "userCreateDttm" }),
	// /**
	// * 神秘洞穴
	// */
	// TLOG_MIGIC("tlog_migic", new String[] { "areaId", "logType", "userId",
	// "userLv", "guildId", "guildLv", "dttm", "via", "userCreateDttm" }),

	// /**
	// * 策略 区号/君主id/君主等级/策略类型/消耗策略值/第2消耗/第1效果/第2效果/时间/渠道/君主创号时间
	// */
	// TLOG_STATEGY("tlog_stategy", new String[] { "areaId", "userId", "userLv",
	// "logType", "consume1", "consume2", "eff1", "eff2", "dttm", "via",
	// "userCreateDttm" }),
	//
	// /**
	// * 使命 区号/君主id/君主等级/日志类型/使命id/时间/渠道/君主创号时间
	// */
	// TLOG_DUTY("tlog_duty", new String[] { "areaId", "userId", "userLv",
	// "logType", "dutyId", "dttm", "via", "userCreateDttm" }),
	//
	// /**
	// * 联盟PK 区号/君主id/君主等级/日志类型/使命id/时间/渠道/君主创号时间
	// */
	// TLOG_GUILD_PK("tlog_guild_pk", new String[] { "areaId", "seasonNum",
	// "playerNum", "total3WinNum", "totalDurmNum", "dttm", "via",
	// "userCreateDttm" }),
	//
	// /**
	// * 城池升级日志
	// */
	// TLOG_CASTLE_LVUP("tlog_castle_lvup", new String[] { "areaId", "userId",
	// "userLv", "casId", "casLv", "dttm", "via", "userCreateDttm" }),
	//
	// /**
	// * 城池建筑升级日志
	// */
	// TLOG_CASTLE_BUI("tlog_castle_bui", new String[] { "areaId", "logType",
	// "userId", "casId", "casLv", "buiId", "buiLv", "dttm", "via",
	// "userCreateDttm" }),
	// /**
	// * 武将升级日志
	// */
	// TLOG_HERO_LVUP("tlog_hero_lvup", new String[] { "areaId", "userId",
	// "heroId", "heroLv", "sysHeroId", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 武将强化日志
	// */
	// TLOG_HERO_STRENGTH("tlog_hero_strength", new String[] { "areaId",
	// "userId",
	// "heroId", "heroLv", "sysHeroId", "itemNum", "growing1", "growing2",
	// "dttm", "via", "userCreateDttm" }),

	// /**
	// * 武将进阶日志
	// */
	// TLOG_HERO_RELIFE("tlog_hero_relife", new String[] { "areaId", "userId",
	// "heroId", "heroLv", "sysHeroId", "reliefNum", "dttm", "via",
	// "userCreateDttm" }),

	// /**
	// * 武将重生日志
	// */
	// TLOG_HERO_REBIRTH("tlog_hero_rebirth", new String[] { "areaId", "userId",
	// "heroId", "heroLv", "sysHeroId", "exp", "growing", "relifeNum",
	// "backExp", "backHunNum", "backFuNum", "backCardNum", "dttm", "via",
	// "userCreateDttm" }),

	// /**
	// * 抽武将日志
	// */
	// TLOG_HERO_GET("tlog_hero_get", new String[] { "areaId", "userId", "type",
	// "sysHeroId", "evaluate", "num", "dttm", "via", "userCreateDttm" }),

	// /**
	// * 开服特惠活动
	// */
	// TLOG_FUND("tlog_fund", new String[] { "areaId", "userId", "num",
	// "refund",
	// "dttm", "via", "userCreateDttm" }),
	//
	// /**
	// * VIP升级日志
	// */
	// TLOG_VIP_LVUP("tlog_vip_lvup", new String[] { "areaId", "userId",
	// "userLv",
	// "vipLv", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 春节消费日志
	// */
	// TLOG_SPRINGFESTIVAL_CONSUME("tlog_springFestivalConsume", new String[] {
	// "areaId", "userId", "npcId", "dropPackId", "cash", "awardId",
	// "awardNum", "dttm", "via", "userCreateDttm" }),
	//
	// /**
	// * 春节刷新日志
	// */
	// TLOG_SPRINGFESTIVAL_REFRESH("tlog_springFestivalRefresh", new String[] {
	// "areaId", "userId", "cash", "dttm", "via", "userCreateDttm" }),
	//
	// /**
	// * 元宵节消费日志
	// */
	// TLOG_LANTERNFESTIVAL("tlog_lanternFestival", new String[] { "areaId",
	// "userId", "type", "cash", "itemId", "itemNum", "awardId",
	// "awardNum", "dttm", "via", "userCreateDttm" }),
	// /**
	// * 商城兑换
	// */
	// TLOG_EXCHANGESHOP("tlog_exchangeShop", new String[] { "areaId", "userId",
	// "entId", "num", "dttm", "via", "userCreateDttm" }),
	// 发送时间
	// 玩家对应的QQ号（手游为openid）
	// 大区号
	// 道具id
	// 道具数量
	// IDIP命令字（记录IDIP指令定义id，16进制需转换为10进制）
	// 流水号
	// 渠道id
	// reason

	// TLOG_IDIP_MESSAGE_ITEM("tlog_ipdpItem", new String[] { "areaId",
	// "sendTime", "openId", "entId", "num", "cmd", "serial", "source",
	// "reason" }),
	// /**
	// * 数据上报日志
	// */
	// TLOG_REPORT("tlog_report", new String[] { "areaId", "logType", "openId",
	// "pf", "pfKey", "ret", "msg", "dttm" }),

	// /**
	// * 装备强化
	// */
	// TLOG_EQUIP_LVUP("tlog_equip_lvup", new String[] { "areaId", "userId",
	// "usName", "userLv", "treId", "itemId", "consumeItemId1", "num1",
	// "consumeItemId2", "num2", "consumeMoney", "dttm" }),
	// /**
	// * 装备分解
	// */
	// TLOG_EQUIP_DESTROY("tlog_equip_destroy", new String[] { "areaId",
	// "userId",
	// "usName", "userLv", "treId", "itemId", "equipLevel",
	// "returnItemId1", "num1", "returnItemId2", "num2", "returnItemId3",
	// "num3", "returnMoney", "dttm" }),
	// /**
	// * 君主任务
	// */
	// TLOG_USER_EXP_MISSION("tlog_user_exp_mission", new String[] { "areaId",
	// "userId", "usName", "userLv", "logType", "missionId",
	// "scoreAwardId", "refNum", "execNum", "dttm" }),
	//
	// /**
	// * 君主军功
	// */
	// TLOG_USER_JUNGONG("tlog_userjungong", new String[] { "areaId", "userId",
	// "userName", "jungong", "balance", "dttm" }),
	// /**
	// * 逐鹿中原
	// */
	// TLOG_COUNRETWAR("tlog_countrywar", new String[] { "areaId", "cityId",
	// "cityName", "guildId1", "guildName1", "applyNum1", "num1",
	// "guildId2", "guildName2", "applyNum2", "num2", "warTime", "dttm" }),
	//
	// /**
	// * 钓鱼
	// */
	// TLOG_USERFISH("tlog_userfish", new String[] { "areaId", "userId",
	// "userName", "userLv", "fishEntId", "fishNum", "dttm" }),

	// /**
	// * 定时限购商城日志
	// */
	// TLOG_TIMERSHOP("tlog_timershop", new String[] { "areaId", "userId",
	// "timerShopId", "itemId", "num", "price", "disPrice", "maxNum",
	// "sellNum", "dttm", "via", "userCreateDttm" }),
	//
	// /**
	// * PVP
	// */
	// TLOG_PVPWAR("tlog_pvpwar", new String[] { "areaId", "userId", "toUserId",
	// "winType", "logType", "armyout", "dttm" }),

	/**
	 * 获得武将
	 * 
	 * 获得途径，获得武将名称及ID或武将信物名称和ID，获得时间
	 */
	TLOG_GETHERO("tlog_gethero",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"entId", "heroName", "reason", "type", "dttm" }),

	/**
	 * 升级武将
	 * 
	 * 武将名称及ID，升级后等级， 、升级消耗铜币数量，升级后武将经验池剩余经验，剩余铜币数量，升级时间
	 */
	TLOG_LVHERO("tlog_lvhero", new String[] { "areaId", "openId", "userId",
			"userName", "userLv", "entId", "beforeLv", "afterLv", "num",
			"heroId", "remainMoney", "dttm" }),
	/**
	 * 分解武将日志
	 * 
	 * 武将名称及ID，分解获取（道具名称和数量），时间
	 */
	TLOG_DISCARDHERO("tlog_discardhero", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "entId", "heroName", "heroId",
			"sysHeroId", "dttm" }),

	/**
	 * 
	 * 神秘商店购买 购买物品名称和ID，购买消耗（将魂），购买后剩余将魂数量，购买时间，
	 */
	TLOG_MYSTRICSHOP("tlog_mysticshop", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "entId", "entName", "count",
			"remainResource", "type", "dttm" }),

	/**
	 * 商店刷新消耗（使用免费刷新次数还是消耗元宝刷新），剩余免费刷新次数，进行商店刷新时间
	 */
	TLOG_MYSTRICFREASH("tlog_mysticfreash", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "freashType", "cost", "remainCnt",
			"dttm" }),

	/**
	 * 装备合成 合成消耗（装备碎片名称和数量），合成的装备名称和ID，合成后剩余装备碎片数量，合成时间
	 */
	TLOG_EQUIPCOMPOSE("tlog_equipcompose", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "item1", "num1", "item2", "num2",
			"item3", "num3", "composeEntId", "entName", "remainNum1",
			"remainNum2", "remainNum3", "dttm" }),
	//
	/**
	 * 装备回炉日志 装备名称和ID，装备回炉返还（道具名称和数量），回炉时间
	 */
	TLOG_EQUIPREBIRTH("tlog_equiprebirth", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "item1", "num1", "item2", "num2",
			"item3", "num3", "item4", "num4", "item5", "num5", "entId",
			"entName", "returnMoney", "dttm" }),

	// /**
	// * 邀请的军师名称和ID，邀请时间
	// */
	// TLOG_GETARMYADVISER("tlog_getarmyadviser", new String[] { "areaId",
	// "openId", "userId", "userName", "userLv", "entId", "entName",
	// "dttm" }),
	// /**
	// * 军师合成装备名称，时间
	// */
	// TLOG_ARMYEQUIPCOMPOSE("tlog_armyequipCompose", new String[] { "areaId",
	// "openId", "userId", "userName", "userLv", "entId", "entName",
	// "dttm" }),
	/**
	 * 联盟祈福
	 * 
	 * 联盟名称和ID，祈福获得道具名称及ID，当前贡献，消耗贡献，祈福时间,luckNum
	 */
	TLOG_GUILDLUCK("tlog_guildluck", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "guildId", "guildName",
			"remainConstruction", "costCons", "luckNum", "entId", "entName",
			"entNum", "dttm" }),
	/**
	 * 建筑升级
	 * 
	 * 建筑名称，当前等级，升级后等级，升级时间
	 */
	TLOG_LVBUILD("tlog_lvbuild",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"entId", "entName", "curLv", "nextLv", "dttm" }),
	/**
	 * 科技升级
	 * 
	 * 科技实体id，名称， 科技原等级，科技升级后等级
	 */
	TLOG_LVTECH("tlog_lvtech",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"entId", "entName", "curLv", "nextLv", "dttm" }),

	/**
	 * 庇佑方式，庇佑消耗（元宝或铜币数量），庇佑获得经验数量，剩余庇佑次数，庇佑时间。
	 */
	TLOG_SHELTER("tlog_shelter", new String[] { "areaId", "openId", "userId",
			"userName", "userLv", "cashCost", "resCost", "cashNum",
			"resourceNum", "dttm" }),

	/**
	 * 经验兑换数量，兑换时间，兑换后武将经验池经验。
	 */
	TLOG_EXCHANGE("tlog_exchange", new String[] { "areaId", "openId", "userId",
			"userName", "userLv", "exchange", "remainExp", "dttm" }),

	/**
	 * 活跃度获得途径，当前活跃度，增加数量，获得时间
	 */
	TLOG_GETACTIVE("tlog_getactive",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"actId", "curActive", "num", "dttm" }),

	/**
	 * CDK兑换 兑换奖励 accName,userID，角色名、角色等级 使用的兑换码，兑换奖励内容，兑换时间
	 */
	TLOG_CDKEXCHANGE("tlog_cdkexchange", new String[] { "areaId", "openId",
			"userId", "userName", "userLv", "cdk", "rwds", "dttm" }),
			
//	/**
//	 * 七国军演 INIT初始化 
//		END结算  
//		JOIN报名  
//		GIVEUP放弃  
//		REMOVE被踢
//	 */
//	TLOG_FLAGWAR("tlog_flagwar", new String[] { "areaId", "round", "joinDttm", "combatDttm", "endDttm",
//			"cleanDttm", "logType", "userId",
//					"joinCountryId", "dttm" }),

	/**
	 * 邮件
	 */
	TLOG_MESSAGE("tlog_message", new String[] { "areaId", "openId", "userId",
			"userName", "userLv", "title","item1", "num1", "item2", "num2", "item3",
			"num3", "item4", "num4", "item5", "num5", "item6", "num6", "dttm" }), 
	
	
	/**
	 * 君主军功
	 */
	TLOG_USER_JUNGONG("tlog_getJungong",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"actId", "curJunGong", "num", "dttm" }),
	
	/**
	 * 君主任务
	 * 任务类型，任务id，名称，状态
	 */
	TLOG_USER_MISSION("tlog_mission",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"missionType", "missionId", "missionName", "status",
					"dttm" }),

	/**
	 * 特殊事件
	 * 任务id，名称，状态
	 */
	TLOG_USER_WORLGMISSION("tlog_worldmission", new String[] { "areaId",
			"missionId", "missionName", "status", "dttm" }),

	/**
	 * 用户信息日志
	 */
	TLOG_USERINFO("tlog_userinfo",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"accId", "pf", "via", "userCreateDttm", "dttm" }),

	/**
	 * 登录登出
	 */
	TLOG_LOGIN("tlog_login",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"logType", "accId", "pf", "via", "loginIp", "onlineTime",
					"userCreateDttm", "dttm" }),

	/**
	 * 武将强化日志
	 */
	TLOG_HERO_STRENGTH("tlog_hero_strength",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"heroId", "heroLv", "sysHeroId", "itemNum", "growing1",
					"growing2", "via", "userCreateDttm", "dttm" }),

	/**
	 * 武将进阶日志
	 */
	TLOG_HERO_RELIFE("tlog_hero_relife",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"heroId", "heroLv", "sysHeroId", "reliefNum", "via",
					"userCreateDttm", "dttm" }),

	/**
	 * 武将重生日志
	 */
	TLOG_HERO_REBIRTH("tlog_hero_rebirth",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"heroId", "heroLv", "sysHeroId", "exp", "growing",
					"relifeNum", "backExp", "backHunNum", "backFuNum",
					"backCardNum", "via", "userCreateDttm", "dttm" }),

	/**
	 * 抽武将日志
	 */
	TLOG_HERO_GET("tlog_hero_get",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"type", "sysHeroId", "evaluate", "num", "via",
					"userCreateDttm","dttm" }),

	/**
	 * 装备强化
	 */
	TLOG_EQUIP_LVUP("tlog_equip_lvup",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"treId", "itemId", "consumeItemId1", "num1",
					"consumeItemId2", "num2", "consumeMoney", "dttm" }),
	/**
	 * 装备分解
	 */
	TLOG_EQUIP_DESTROY("tlog_equip_destroy",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"treId", "itemId", "equipLevel", "returnItemId1", "num1",
					"returnItemId2", "num2", "returnItemId3", "num3",
					"returnMoney", "dttm" }),

	/**
	 * 冒险奖励
	 */
	TLOG_RISK_AWARD("tlog_riskaward",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"pSceneId", "award", "via", "userCreateDttm", "dttm" }),

	/**
	 * 战役
	 */
	TLOG_RISK("tlog_risk",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"logType", "sceneId", "sceneName", "star", "win",
					"via", "userCreateDttm", "pSceneId", "npcId",
					"combatNUm","dttm" }),

	/**
	 * 君主成就
	 * 成就id，名称，获得军功
	 */
	TLOG_USER_ACHIEVE("tlog_achieve",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"achieveId", "achieveName", "addJungong", "dttm" }),

	/**
	 * 君主军衔 
	 * 军衔id，勋章id，消耗的军功，勋章奖励类型，奖励的内容，晋升后的军衔
	 */
	TLOG_USER_TITLE("tlog_title",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"titleId", "awardId", "consumeJunGong", "awardType",
					"awardDetail", "upTitleId", "dttm" }),
	/**
	 * 重楼
	 */
	TLOG_TOWER("tlog_tower",
			new String[] { "areaId", "openId", "userId", "userName", "userLv",
					"stageId", "win", "battleId", "dttm" }),

	;
	private String[] column;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getColumn() {
		return column;
	}

	public void setColumn(String[] column) {
		this.column = column;
	}

	private TLogTable(String name, String[] column) {
		this.column = column;
		this.name = name;
	}

	public static TLogTable getEnum(String str) {
		for (TLogTable t : TLogTable.values()) {
			if (t.name.equals(str)) {
				return t;
			}
		}
		return null;
	}
}
