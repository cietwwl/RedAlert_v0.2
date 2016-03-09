package com.youxigu.dynasty2.log.imp;

import com.manu.core.ServiceLocator;
import com.youxigu.dynasty2.log.AbsLogLineBuffer;
import com.youxigu.dynasty2.log.ILogService;
import com.youxigu.dynasty2.log.LogTypeConst;
import com.youxigu.dynasty2.log.TlogHeadUtil;
import com.youxigu.dynasty2.user.domain.User;

public class BattleLog {

	private static ILogService tlogService = (ILogService)ServiceLocator.getSpringBean("tlogService");
	
//	  <struct name="RoundFlow" version="1" desc="(必填)单局结束数据流水">
//	    <entry  name="vGameSvrId"          type="string"		  size="25"		desc="(必填)登录的游戏服务器编号" />
//	    <entry  name="dtEventTime"         type="datetime"						desc="(必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS" />
//	    <entry  name="vGameAppid"          type="string"		  size="32"		desc="(必填)游戏APPID" />
//	    <entry  name="iPlatID"			   type="int"							desc="(必填)ios 0/android 1"/>
//	    <entry  name="iZoneID"			   type="int"						    desc="(必填)针对分区分服的游戏填写分区id，用来唯一标示一个区；非分区分服游戏请填写0"/>
//	    <entry  name="vopenid"             type="string"          size="64"		desc="(必填)玩家" />
//	    <entry  name="iBattleID"           type="int"							desc="(必填)本局id" />
//	    <entry  name="iBattleType"         type="int"						    desc="(必填)战斗类型 对应BATTLETYPE,其它说明参考FAQ文档" />
//	    <entry  name="iRoundScore"         type="int"							desc="(必填)本局分数" />
//	    <entry  name="iRoundTime"          type="int"							desc="(必填)对局时长(秒)" />
//	    <entry  name="iResult"             type="int"							desc="(必填)单局结果" />
//	    <entry  name="iRank"               type="int"							desc="(必填)排名" />
//	    <entry  name="iGold"               type="int"							desc="(必填)金钱" />
//	  </struct>
	
	public static void setTlogBattle(User user,LogBattleAct act,boolean suc,int battleId,int difficulty){
		AbsLogLineBuffer buf = TlogHeadUtil.getTlogHead(LogTypeConst.TLOG_TYPE_RoundFlow, user);
		buf.append(battleId).append(act.vuale).append(0).append(0).append(suc?1:0).append(0).append(0).append(difficulty);
		tlogService.log(buf);
	}
	
}
