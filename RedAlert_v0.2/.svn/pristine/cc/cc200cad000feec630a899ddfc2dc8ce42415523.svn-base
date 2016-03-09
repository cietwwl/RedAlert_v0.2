DROP PROCEDURE IF EXISTS TARGETDB.mergedb;
DELIMITER //
CREATE PROCEDURE TARGETDB.mergedb()
BEGIN
	DECLARE i int default 0;
	DECLARE add_accId bigint default 0;
	DECLARE add_userId bigint default 0;
	DECLARE add_casId bigint default 0;
	DECLARE add_heroId bigint default 0;
	DECLARE add_treasuryId bigint default 0;
	DECLARE add_troopId bigint default 0;
	DECLARE add_guildId bigint default 0;
	/*源库大区ID*/
	DECLARE source_areaId VARCHAR(4) default '0';
	/*目标库大区ID*/
	DECLARE target_areaId VARCHAR(4) default '0';
	


	/*
	castlebuilder 与 castlebuilding 要特殊处理，因为有自增ID引用
	*/
	DECLARE add_cb0 int default 0;
	DECLARE add_cb1 int default 0;
	DECLARE add_cb2 int default 0;
	DECLARE add_cb3 int default 0;
	DECLARE add_cb4 int default 0;
	DECLARE add_cb5 int default 0;
	DECLARE add_cb6 int default 0;
	DECLARE add_cb7 int default 0;
	DECLARE add_cb8 int default 0;
	DECLARE add_cb9 int default 0;


	/*用于找出重复的联盟旗号,并重新生成*/
	DECLARE curr_guildId bigint default 0;
	DECLARE curr_flag char(2) default '';
	DECLARE done int default 0;

	DECLARE cursor_guildFlag CURSOR FOR select guildId from (select max(guildId) as guildId,count(*) as num from TARGETDB.guild group by flag) tmp where num>1;
	DECLARE continue handler FOR SQLSTATE '02000' SET done = 1; 

	
	/*
	取得目标数据库的最大ID
	SourceDB中相应的ID都要加上这个值,由于有些ID作为分表的依据,这里要保证最后一位是0
	*/
	SELECT ceil((currValue+10)/10)*10 into add_accId from TARGETDB.idgen where idType='ACCOUNT';
	SELECT ceil((currValue+10)/10)*10 into add_userId from TARGETDB.idgen where idType='USER';
	SELECT ceil((currValue+10)/10)*10 into add_casId from TARGETDB.idgen where idType='CASTLE';
	SELECT ceil((currValue+10)/10)*10 into add_heroId from TARGETDB.idgen where idType='HERO';
	SELECT ceil((currValue+10)/10)*10 into add_treasuryId from TARGETDB.idgen where idType='TREASURY';
	SELECT ceil((currValue+10)/10)*10 into add_troopId from TARGETDB.idgen where idType='TROOP';
	SELECT ceil((currValue+10)/10)*10 into add_guildId from TARGETDB.idgen where idType='GUILD';


	select add_accId 帐号ID增量,add_userId 角色ID增量,add_casId 城池ID增量 ,add_heroId 武将ID增量,add_treasuryId 道具ID增量,add_troopId 军团ID增量,add_guildId 联盟ID增量;


	SELECT serverId into source_areaId from SOURCEDB.serverinfo limit 1;
	SELECT serverId into target_areaId from TARGETDB.serverinfo limit 1;

	/*外网数据存在 accName+areaid重复的情况,转移这些account到dupliaccount,其他数据保留 */
	select '...........' as  '清除被合并数据库中openId+areaid与目标库重复的account';
	DROP TABLE IF EXISTS TARGETDB.dupliaccount;
	CREATE TABLE TARGETDB.dupliaccount (
	  `id` int(16) NOT NULL auto_increment,
	  `accId` bigint(19) default '0',
	  `accName` varchar(64) default NULL,
	  `accDesc` varchar(40) default '',
	  `status` int(2) default '0',
	  `createDttm` timestamp NULL default NULL,
	  `loginIp` varchar(20) default NULL,
	  `lastLoginDttm` timestamp NULL default NULL,
	  `envelopDttm` timestamp NULL default NULL COMMENT '封号时间',
	  `inviteAccId` bigint(16) default '0' COMMENT '邀请人的账号',
	  `nodeIp` varchar(50) default NULL,
	  `qqYellowLv` int(4) default '0' COMMENT 'qq黄钻等级',
	  `pf` varchar(100) default NULL,
	  `via` varchar(255) default NULL,
	  `qqFlag` int(16) default '0' COMMENT 'qq标志',
	  `qqBlueLv` int(4) default '0' COMMENT 'qq蓝钻等级',
	  `qqPlusLv` int(4) default '0' COMMENT 'q+等级',
	  `qqLv` int(4) default '0' COMMENT 'qq等级',
	  `qqVipLv` int(4) default '0' COMMENT 'qqVip等级',
	  `qq3366Lv` int(4) default '0' COMMENT '3366等级',
	  `qqRedLv` int(4) default '0' COMMENT '红钻等级',
	  `qqGreenLv` int(4) default '0' COMMENT '绿钻等级',
	  `qqPinkLv` int(4) default '0' COMMENT '粉钻等级',
	  `qqSuperLv` int(4) default '0' COMMENT '超Q等级',
	  `qqCurrFlag` int(16) default '0' COMMENT '当前qq标志',
	  `lastLogoutDttm` timestamp NULL default NULL,
	  `onlineTimeSum` int(16) default '0' COMMENT '累计在线时间',
	  `offlineTimeSum` int(16) default '0' COMMENT '累计离线时间',
	  `areaId` varchar(10) default NULL,
	  PRIMARY KEY  (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	INSERT INTO TARGETDB.dupliaccount(accId,accName,accDesc,status,createDttm,loginIp,lastLoginDttm,envelopDttm,inviteAccId,nodeIp,qqYellowLv,pf,via,qqFlag,qqBlueLv,qqPlusLv,qqLv,qqVipLv,qq3366Lv,qqRedLv,qqGreenLv,qqPinkLv,qqSuperLv,qqCurrFlag,lastLogoutDttm,onlineTimeSum,offlineTimeSum,areaId) select s.accId,s.accName,s.accDesc,s.status,s.createDttm,s.loginIp,s.lastLoginDttm,s.envelopDttm,s.inviteAccId,s.nodeIp,s.qqYellowLv,s.pf,s.via,s.qqFlag,s.qqBlueLv,s.qqPlusLv,s.qqLv,s.qqVipLv,s.qq3366Lv,s.qqRedLv,s.qqGreenLv,s.qqPinkLv,s.qqSuperLv,s.qqCurrFlag,s.lastLogoutDttm,s.onlineTimeSum,s.offlineTimeSum,s.areaId from SOURCEDB.account s inner join TARGETDB.account t on s.accName=t.accName and s.areaId=t.areaId ;
	delete from SOURCEDB.account where accId in (select accId from TARGETDB.dupliaccount);
	
	select '...........' as  'user重名处理开始';

	DROP TABLE IF EXISTS SOURCEDB.dupliuser;
	CREATE TABLE SOURCEDB.dupliuser (
	  `userId` bigint(19) default '0',
	  `userName` varchar(20) default null,
	  `newUserName` varchar(20) default null,
	  `usrLv` int(16) default '0',
	  PRIMARY KEY  (`userId`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	INSERT INTO SOURCEDB.dupliuser(userId,userName,newUserName,usrLv) select userId,userName,concat(source_areaId,userName),usrLv from  SOURCEDB.user where userName in (select userName from TARGETDB.user);


	update SOURCEDB.dupliuser set newUserName = concat(source_areaId,'-',userName) where newUserName in (select userName from SOURCEDB.user);
	update SOURCEDB.dupliuser set newUserName = concat(source_areaId,'-',userName) where newUserName in (select userName from TARGETDB.user);


	/*
	update SOURCEDB.user set userName = (select newUserName from SOURCEDB.dupliuser where SOURCEDB.dupliuser.userId = SOURCEDB.user.userId )
	*/
	update SOURCEDB.user u,SOURCEDB.dupliuser tmp set u.userName =tmp.newUserName where u.userId = tmp.userId;


/*
	update SOURCEDB.rankdata set userName = (select userName from SOURCEDB.user u where u.userId = SOURCEDB.rankdata.userId);
	update SOURCEDB.castle set casName = (select userName from SOURCEDB.user u where u.userId = SOURCEDB.castle.userId) where parentCasId=0;
	update SOURCEDB.guilduser set name = (select userName from SOURCEDB.user u where u.userId = SOURCEDB.guilduser.userId);
*/
	update SOURCEDB.rankdata r,SOURCEDB.user u set r.userName = u.userName where r.userId = u.userId;
	update SOURCEDB.castle c,SOURCEDB.user u set c.casName = u.userName where c.userId = u.userId and c.parentCasId=0;
	update SOURCEDB.guilduser g,SOURCEDB.user u set g.name = u.userName where g.userId = u.userId;


	/*大于35及并且重名的发更名帖*/
	insert into SOURCEDB.message_0(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=0;
	insert into SOURCEDB.message_1(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=1;
	insert into SOURCEDB.message_2(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=2;
	insert into SOURCEDB.message_3(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=3;
	insert into SOURCEDB.message_4(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=4;
	insert into SOURCEDB.message_5(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=5;
	insert into SOURCEDB.message_6(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=6;
	insert into SOURCEDB.message_7(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=7;
	insert into SOURCEDB.message_8(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=8;
	insert into SOURCEDB.message_9(sendUserId,receiveUserId,sendDttm,comment,title,status,messageType,isGift,entityId0,itemNum0,entityId1,entityId2,entityId3,entityId4,entityId5,status0) select 0,userId,now(),'合服赠送更名帖','合服赠送更名帖',0,10,1,10610188,1,0,0,0,0,0,1 from SOURCEDB.dupliuser where  MOD(userId,10)=9;


	select '...........' as 'guild重名处理开始';
	DROP TABLE IF EXISTS SOURCEDB.dupliguild;
	CREATE TABLE SOURCEDB.dupliguild (
	  `guildId` bigint(19) default '0',
	  `guildName` varchar(20) default null,
	  `newguildName` varchar(20) default null,
	  `guildLv` int(16) default '0',
	  PRIMARY KEY  (`guildId`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	INSERT INTO SOURCEDB.dupliguild(guildId,guildName,newguildName,guildLv) select guildId,name,concat(source_areaId,name),0 from  SOURCEDB.guild where name in (select name from TARGETDB.guild);
	update SOURCEDB.dupliguild set newguildName = concat(source_areaId,'-',guildName) where newguildName in (select name from SOURCEDB.guild);
	update SOURCEDB.dupliguild set newguildName = concat(source_areaId,'-',guildName) where newguildName in (select name from TARGETDB.guild);
	update SOURCEDB.guild g,SOURCEDB.dupliguild tmp set g.name =tmp.newguildName,g.freeFlag=g.freeFlag+1 where g.guildId = tmp.guildId;


/*
	update SOURCEDB.rankdata set guildName = (select guildName from SOURCEDB.guild g where g.guildId = SOURCEDB.rankdata.guildId) where guildId!=0;
*/
	update SOURCEDB.rankdata r,SOURCEDB.guild g set r.guildName = g.name where r.guildId = g.guildId;

	select '...........' as '合并数据:account';
	INSERT INTO TARGETDB.account (accId,accName,accDesc,status,createDttm,loginIp,lastLoginDttm,envelopDttm,inviteAccId,nodeIp,qqYellowLv,pf,via,qqFlag,qqBlueLv,qqPlusLv,qqLv,qqVipLv,qq3366Lv,qqRedLv,qqGreenLv,qqPinkLv,qqSuperLv,qqCurrFlag,lastLogoutDttm,onlineTimeSum,offlineTimeSum,areaId,dType,pType,dInfo) SELECT accId+add_accId,accName,accDesc,status,createDttm,loginIp,lastLoginDttm,envelopDttm,(case inviteAccId when 0 then 0 else inviteAccId+add_accId end),nodeIp,qqYellowLv,pf,via,qqFlag,qqBlueLv,qqPlusLv,qqLv,qqVipLv,qq3366Lv,qqRedLv,qqGreenLv,qqPinkLv,qqSuperLv,qqCurrFlag,lastLogoutDttm,onlineTimeSum,offlineTimeSum,areaId,dType,pType,dInfo FROM SOURCEDB.account;


	truncate table  TARGETDB.arenarank;
	truncate table  TARGETDB.armyout;

	select '...........' as '合并数据:bufftip';
	INSERT INTO TARGETDB.bufftip (userId,buffName,endTime) SELECT userId+add_userId,buffName,endTime FROM SOURCEDB.bufftip;


	select '...........' as '合并数据:castle';
	INSERT INTO TARGETDB.castle (casId,userId,casName,casLv,maxCasLv,rangeValue,rangeCeil,calcuDttm,quarCalcuDttm,status,castleType,parentCasId,branchCasTypeList,occuFlagTime,changeCountryDttm,icon,iconEndTime,atkGuildFlag,atkGuildId,autoBuild) SELECT casId+add_casId,userId+add_userId,casName,casLv,maxCasLv,rangeValue,rangeCeil,calcuDttm,quarCalcuDttm,status,castleType,(case parentCasId when 0 then 0 else parentCasId+add_casId end),branchCasTypeList,occuFlagTime,changeCountryDttm,icon,iconEndTime,atkGuildFlag,atkGuildId,autoBuild FROM SOURCEDB.castle;


	/*城池流亡，去掉坐标，满繁荣,取消自动建造*/
	update TARGETDB.castle set status=0,atkGuildId=0,atkGuildFlag=null,occuFlagTime=null,rangeValue=(case when casLv>0 then rangeCeil else 0 end),autoBuild=0;
	update TARGETDB.castle set quarCalcuDttm=now() where quarCalcuDttm is null; 
	/*主城对应的分城列表*/
	DROP TABLE IF EXISTS TARGETDB.tmp_castle;
	CREATE TABLE TARGETDB.tmp_castle (`userId` bigint NOT NULL default 0,`branchCas` varchar(512) NULL,PRIMARY KEY  (`userId`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	insert into TARGETDB.tmp_castle(userId,branchCas) select userId,concat(group_concat(concat(castleType,'-',casId) order by casId SEPARATOR ',' ),',')  from TARGETDB.castle where parentCasId!=0 group by userId;
	update TARGETDB.castle set branchCasTypeList=IFNULL((select branchCas from TARGETDB.tmp_castle as tmp where  tmp.userId = TARGETDB.castle.userId),'')  where parentCasId=0;
	DROP TABLE IF EXISTS TARGETDB.tmp_castle;


	INSERT INTO TARGETDB.castlearmy (casId,recruitNum,forceNum,num,harmNum,refreshCD) SELECT casId+add_casId,recruitNum,forceNum,num,harmNum,refreshCD FROM SOURCEDB.castlearmy;

	INSERT INTO TARGETDB.castlebattleunit (casId,armyEntId1,armynum1,armyEntId2,armynum2,armyEntId3,armynum3,armyEntId4,armynum4,armyEntId5,armynum5) SELECT casId+add_casId,armyEntId1,armynum1,armyEntId2,armynum2,armyEntId3,armynum3,armyEntId4,armynum4,armyEntId5,armynum5 FROM SOURCEDB.castlebattleunit;




	select IFNULL(max(casBuiId),0) into add_cb0 from TARGETDB.castlebuilding_0;
	select IFNULL(max(casBuiId),0) into add_cb1 from TARGETDB.castlebuilding_1;
	select IFNULL(max(casBuiId),0) into add_cb2 from TARGETDB.castlebuilding_2;
	select IFNULL(max(casBuiId),0) into add_cb3 from TARGETDB.castlebuilding_3;
	select IFNULL(max(casBuiId),0) into add_cb4 from TARGETDB.castlebuilding_4;
	select IFNULL(max(casBuiId),0) into add_cb5 from TARGETDB.castlebuilding_5;
	select IFNULL(max(casBuiId),0) into add_cb6 from TARGETDB.castlebuilding_6;
	select IFNULL(max(casBuiId),0) into add_cb7 from TARGETDB.castlebuilding_7;
	select IFNULL(max(casBuiId),0) into add_cb8 from TARGETDB.castlebuilding_8;
	select IFNULL(max(casBuiId),0) into add_cb9 from TARGETDB.castlebuilding_9;

	select '...........' as '合并数据:castlebuilder';

	INSERT INTO TARGETDB.castlebuilder (userId,builderId,builderType,baseSpeed,totalSpeed,casId,buiId,beginDttm,coolEndDttm,expireDttm,level,buiEntId,builv) SELECT userId+add_userId,(case builderType when 1 then builderId+add_userId else builderId end),builderType,baseSpeed,totalSpeed,(case casId when 0 then 0 else casId+add_casId end),(case casId when 0 then buiId else (case MOD(casId,10) when 0 then buiId+add_cb0 when 1 then buiId+add_cb1 when 2 then buiId+add_cb2 when 3 then buiId+add_cb3 when 4 then buiId+add_cb4 when 5 then buiId+add_cb5 when 6 then buiId+add_cb6 when 7 then buiId+add_cb7 when 8 then buiId+add_cb8 when 9 then buiId+add_cb9 else buiId end) end ),beginDttm,coolEndDttm,expireDttm,level,buiEntId,builv FROM SOURCEDB.castlebuilder;


	/*清除所有正在建造的建筑工
	--update TARGETDB.castlebuilder set casId=0,buiId=0,buiEntId=0,builv=0,beginDttm=null,coolEndDttm=null;
	删除雇佣的建筑工
	--delete from TARGETDB.castlebuilder where builderType=1 and builderId in (select userId from TARGETDB.cleanaccount);
	*/


	select '...........' as '合并数据:castlebuilding';
	INSERT INTO TARGETDB.castlebuilding_0 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb0,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_0;
	INSERT INTO TARGETDB.castlebuilding_1 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb1,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_1;
	INSERT INTO TARGETDB.castlebuilding_2 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb2,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_2;
	INSERT INTO TARGETDB.castlebuilding_3 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb3,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_3;
	INSERT INTO TARGETDB.castlebuilding_4 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb4,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_4;
	INSERT INTO TARGETDB.castlebuilding_5 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb5,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_5;
	INSERT INTO TARGETDB.castlebuilding_6 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb6,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_6;
	INSERT INTO TARGETDB.castlebuilding_7 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb7,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_7;
	INSERT INTO TARGETDB.castlebuilding_8 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb8,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_8;
	INSERT INTO TARGETDB.castlebuilding_9 (casBuiId,casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild) SELECT casBuiId+add_cb9,casId+add_casId,buiEntId,level,posNo,beginDttm,buildDttm,autoBuild FROM SOURCEDB.castlebuilding_9;

	

	select '...........' as '合并数据:castleeffect';

	INSERT INTO TARGETDB.castleeffect_0 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_0;
	INSERT INTO TARGETDB.castleeffect_1 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_1;
	INSERT INTO TARGETDB.castleeffect_2 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_2;
	INSERT INTO TARGETDB.castleeffect_3 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_3;
	INSERT INTO TARGETDB.castleeffect_4 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_4;
	INSERT INTO TARGETDB.castleeffect_5 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_5;
	INSERT INTO TARGETDB.castleeffect_6 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_6;
	INSERT INTO TARGETDB.castleeffect_7 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_7;
	INSERT INTO TARGETDB.castleeffect_8 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_8;
	INSERT INTO TARGETDB.castleeffect_9 (casId,entId,effTypeId,absValue,perValue,expireDttm) SELECT casId+add_casId,entId,effTypeId,absValue,perValue,expireDttm FROM SOURCEDB.castleeffect_9;

	truncate table TARGETDB.castlefavorites;


	INSERT INTO TARGETDB.castlepop (casId,idlePop,foodPop,woodPop,stonePop,bronzePop) SELECT casId+add_casId,idlePop,foodPop,woodPop,stonePop,bronzePop FROM SOURCEDB.castlepop;


	INSERT INTO TARGETDB.castleres (casId,foodNum,bronzeNum,moneyNum,casFood,casBronze,casMoney,cashNum,lastBuyDttm) SELECT casId+add_casId,foodNum,bronzeNum,moneyNum,casFood,casBronze,casMoney,cashNum,lastBuyDttm FROM SOURCEDB.castleres;

	INSERT INTO TARGETDB.castletowerarmy (casId,entId,num,buildNum,buildDttm) SELECT casId+add_casId,entId,num,buildNum,buildDttm FROM SOURCEDB.castletowerarmy;


	truncate table TARGETDB.castletrade;

	select '...........' as '合并数据:castlevisit';
	INSERT INTO TARGETDB.castlevisit (userId,visitEventNum,visitEventUserIds,heroEventNum,heroEvents,castleEventNum,castleEvents,lastDttm,npcId,atkName,expireDttm) SELECT userId+add_userId,visitEventNum,visitEventUserIds,heroEventNum,heroEvents,castleEventNum,castleEvents,lastDttm,npcId,atkName,expireDttm FROM SOURCEDB.castlevisit;
	
	update TARGETDB.castlevisit set visitEventNum=0,visitEventUserIds='',heroEventNum=0,heroEvents='',castleEventNum=0,castleEvents='',lastDttm=null,expireDttm=null,npcId=0,atkName=null;


	select '...........' as '合并数据:chum';
	INSERT INTO TARGETDB.chum (openid,iopenid,itime,invkey,userid,name,icon) SELECT openid,iopenid,itime,invkey,userid+add_userId,name,icon FROM SOURCEDB.chum;
	INSERT INTO TARGETDB.chumachieve (userId,num,userlv,towerlv,castlelv,viplv) SELECT userId+add_userId,num,userlv,towerlv,castlelv,viplv FROM SOURCEDB.chumachieve;



	truncate table TARGETDB.combatdb;
 
	truncate table TARGETDB.countryevent;
	truncate table TARGETDB.countrynotice;
	truncate table TARGETDB.declarewar;

	INSERT INTO TARGETDB.defplan (casId,troopId) SELECT casId+add_casId,(case troopId when 0 then 0 else troopId+add_troopId end) FROM SOURCEDB.defplan;

	INSERT INTO TARGETDB.donationmoney (userId,status,lastDonation,totalNum,lastAwards) SELECT userId+add_userId,status,lastDonation,totalNum,lastAwards FROM SOURCEDB.donationmoney;



	truncate table TARGETDB.dotarank;
	truncate table TARGETDB.dotarank0;
	truncate table TARGETDB.dotarank1;
	truncate table TARGETDB.dotarank2;
	truncate table TARGETDB.dotarank3;
	truncate table TARGETDB.dotarank4;
	truncate table TARGETDB.dotarank5;
	truncate table TARGETDB.dotarank6;
	truncate table TARGETDB.dotarank7;




	select '...........' as '合并数据:userduty';
	INSERT INTO TARGETDB.userduty (userId,dutyId,rank,status,num,finishDttm) SELECT userId+add_userId,dutyId,rank,status,num,finishDttm FROM SOURCEDB.userduty;

	/*这个要根据UserDuty重新计算，因为删号原因，不能简单叠加*/

	/*
	delete from TARGETDB.dutycount;
	insert into TARGETDB.dutycount select dutyId,count(*) from TARGETDB.userduty where status=1 group by dutyId;
	insert into TARGETDB.dutycount select dutyId,0  from TARGETDB.duty where dutyId not in (select dutyId from TARGETDB.dutycount);
	*/

	DROP TABLE IF EXISTS TARGETDB.tmp_dutycount;
	CREATE TABLE TARGETDB.tmp_dutycount (`dutyId` int(16) NOT NULL default '0' COMMENT '使命id',`total` int(16) default '0' COMMENT '最大名次') ENGINE=InnoDB DEFAULT CHARSET=utf8;
	insert into TARGETDB.tmp_dutycount select dutyId,total from TARGETDB.dutycount;
	insert into TARGETDB.tmp_dutycount select dutyId,total from SOURCEDB.dutycount;
	truncate table TARGETDB.dutycount;
	insert into TARGETDB.dutycount (select dutyId,sum(total) from tmp_dutycount group by dutyId) ;
	DROP TABLE IF EXISTS TARGETDB.tmp_dutycount;

	truncate table TARGETDB.eventexenum_0;
	truncate table TARGETDB.eventexenum_1;
	truncate table TARGETDB.eventexenum_2;
	truncate table TARGETDB.eventexenum_3;
	truncate table TARGETDB.eventexenum_4;
	truncate table TARGETDB.eventexenum_5;
	truncate table TARGETDB.eventexenum_6;
	truncate table TARGETDB.eventexenum_7;
	truncate table TARGETDB.eventexenum_8;
	truncate table TARGETDB.eventexenum_9;


	select '...........' as '合并数据:firedhero';
	INSERT INTO TARGETDB.firedhero_0 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_0;
	INSERT INTO TARGETDB.firedhero_1 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_1;
	INSERT INTO TARGETDB.firedhero_2 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_2;
	INSERT INTO TARGETDB.firedhero_3 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_3;
	INSERT INTO TARGETDB.firedhero_4 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_4;
	INSERT INTO TARGETDB.firedhero_5 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_5;
	INSERT INTO TARGETDB.firedhero_6 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_6;
	INSERT INTO TARGETDB.firedhero_7 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_7;
	INSERT INTO TARGETDB.firedhero_8 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_8;
	INSERT INTO TARGETDB.firedhero_9 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,growing,growingItem,heroSkillId,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.firedhero_9;


	truncate table  TARGETDB.forbidchatuser;

	select '...........' as '合并数据:friend';
	/*玩家自己处理名字不一样的好友*/
	INSERT INTO TARGETDB.friend_0 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_0;
	INSERT INTO TARGETDB.friend_1 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_1;
	INSERT INTO TARGETDB.friend_2 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_2;
	INSERT INTO TARGETDB.friend_3 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_3;
	INSERT INTO TARGETDB.friend_4 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_4;
	INSERT INTO TARGETDB.friend_5 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_5;
	INSERT INTO TARGETDB.friend_6 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_6;
	INSERT INTO TARGETDB.friend_7 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_7;
	INSERT INTO TARGETDB.friend_8 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_8;
	INSERT INTO TARGETDB.friend_9 (userId,friendUserId,friendUserName,friendMainCasId,groupId,addTime,note) SELECT userId+add_userId,friendUserId+add_userId,friendUserName,friendMainCasId+add_casId,groupId,addTime,note FROM SOURCEDB.friend_9;


	truncate table TARGETDB.friendapp;

	truncate table TARGETDB.friendevalution_0;
	truncate table TARGETDB.friendevalution_1;
	truncate table TARGETDB.friendevalution_2;
	truncate table TARGETDB.friendevalution_3;
	truncate table TARGETDB.friendevalution_4;
	truncate table TARGETDB.friendevalution_5;
	truncate table TARGETDB.friendevalution_6;
	truncate table TARGETDB.friendevalution_7;
	truncate table TARGETDB.friendevalution_8;
	truncate table TARGETDB.friendevalution_9;
	

	INSERT INTO TARGETDB.giftcount (userId,type,num,status,lastDttm,ext) SELECT userId+add_userId,type,num,status,lastDttm,ext FROM SOURCEDB.giftcount;

	select '...........' as '合并数据:guild';

	select count(*) into @hasIndex from information_schema.statistics where TABLE_SCHEMA='TARGETDB' and TABLE_NAME='guild' and INDEX_NAME='idx_guild_flag';
	if @hasIndex >0 then
		alter table TARGETDB.guild drop index idx_guild_flag;
	end if;


	INSERT INTO TARGETDB.guild (guildId,countryId,name,flag,guildNotice,introduction,level,construction,hufu,honour,QQGroup,QQGroupOpenId,QQGroupSync,QQGroupBindCount,QQGroupPrefix,creator,leaderId,leader,createDttm,coolingTime,inviteDttm,inviteEndDttm,clanNum,userNum,state,dismissTime,funds,everyRefreshTime,flagTypeId,guildFlagTypeColor,chargebackNum,transferTime,posX,posY,transferUserId,actPoint,actDttm,freeFlag,topAreaId,appNum,appTime) SELECT guildId+add_guildId,countryId,name,flag,guildNotice,introduction,level,construction,hufu,honour,QQGroup,QQGroupOpenId,QQGroupSync,QQGroupBindCount,QQGroupPrefix,creator,leaderId+add_userId,leader,createDttm,coolingTime,inviteDttm,inviteEndDttm,clanNum,userNum,state,dismissTime,funds,everyRefreshTime,flagTypeId,guildFlagTypeColor,chargebackNum,transferTime,posX,posY,(case transferUserId when 0 then 0 else transferUserId+add_userId end),actPoint,actDttm,freeFlag,topAreaId,appNum,appTime FROM SOURCEDB.guild;
	update TARGETDB.guild set honour=0;

	/*重复的旗标重新生成*/
	 OPEN cursor_guildFlag; 
	 FETCH cursor_guildFlag INTO curr_guildId;
	 WHILE (done=0) DO 
		select guildFlag into curr_flag from TARGETDB.guildflag where guildFlag not in (select flag from TARGETDB.guild) limit 1;
		update TARGETDB.guild set flag = curr_flag,freeFlag=freeFlag+2 where guildId =curr_guildId ;
		select curr_guildId as '联盟ID',curr_flag as '更新后的旗标';
		FETCH cursor_guildFlag INTO curr_guildId;
	 END WHILE;

	 CLOSE cursor_guildFlag; 

	 alter table TARGETDB.guild add unique idx_guild_flag(flag);



	truncate table TARGETDB.guildapplyinvite;


	INSERT INTO TARGETDB.guildautopermit (guildId,castlesLv,babelLv,leagueLv,pvpLv,vipLv,usrLv,riskLv,status,updateDttm,updateUser) SELECT guildId+add_guildId,castlesLv,babelLv,leagueLv,pvpLv,vipLv,usrLv,riskLv,status,updateDttm,updateUser FROM SOURCEDB.guildautopermit;


	select '...........' as '合并数据:guildbuilding';
	INSERT INTO TARGETDB.guildbuilding_0 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_0;
	INSERT INTO TARGETDB.guildbuilding_1 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_1;
	INSERT INTO TARGETDB.guildbuilding_2 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_2;
	INSERT INTO TARGETDB.guildbuilding_3 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_3;
	INSERT INTO TARGETDB.guildbuilding_4 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_4;
	INSERT INTO TARGETDB.guildbuilding_5 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_5;
	INSERT INTO TARGETDB.guildbuilding_6 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_6;
	INSERT INTO TARGETDB.guildbuilding_7 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_7;
	INSERT INTO TARGETDB.guildbuilding_8 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_8;
	INSERT INTO TARGETDB.guildbuilding_9 (guildId,entId,entLevel,posNo) SELECT guildId+add_guildId,entId,entLevel,posNo FROM SOURCEDB.guildbuilding_9;



	truncate table TARGETDB.guildcave;

	select '...........' as '合并数据:guildeffect';

	INSERT INTO TARGETDB.guildeffect_0 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_0;
	INSERT INTO TARGETDB.guildeffect_1 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_1;
	INSERT INTO TARGETDB.guildeffect_2 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_2;
	INSERT INTO TARGETDB.guildeffect_3 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_3;
	INSERT INTO TARGETDB.guildeffect_4 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_4;
	INSERT INTO TARGETDB.guildeffect_5 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_5;
	INSERT INTO TARGETDB.guildeffect_6 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_6;
	INSERT INTO TARGETDB.guildeffect_7 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_7;
	INSERT INTO TARGETDB.guildeffect_8 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_8;
	INSERT INTO TARGETDB.guildeffect_9 (guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT guildId+add_guildId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.guildeffect_9;


	truncate table TARGETDB.guildevent_0;
	truncate table TARGETDB.guildevent_1;
	truncate table TARGETDB.guildevent_2;
	truncate table TARGETDB.guildevent_3;
	truncate table TARGETDB.guildevent_4;
	truncate table TARGETDB.guildevent_5;
	truncate table TARGETDB.guildevent_6;
	truncate table TARGETDB.guildevent_7;
	truncate table TARGETDB.guildevent_8;
	truncate table TARGETDB.guildevent_9;
	
	truncate table TARGETDB.guildmergeinvite;
	truncate table TARGETDB.guildmergelog;
	truncate table TARGETDB.guildfavorites;

	truncate table TARGETDB.guildpkseason;

	truncate table TARGETDB.guildrank0;
	truncate table TARGETDB.guildrank1;
	truncate table TARGETDB.guildrank2;
	truncate table TARGETDB.guildrank3;
	truncate table TARGETDB.guildrank4;
	truncate table TARGETDB.guildrank5;
	truncate table TARGETDB.guildrank6;
	truncate table TARGETDB.guildrank7;

	select '...........' as '合并数据:guildtechnology';
	INSERT INTO TARGETDB.guildtechnology_0 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_0;
	INSERT INTO TARGETDB.guildtechnology_1 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_1;
	INSERT INTO TARGETDB.guildtechnology_2 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_2;
	INSERT INTO TARGETDB.guildtechnology_3 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_3;
	INSERT INTO TARGETDB.guildtechnology_4 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_4;
	INSERT INTO TARGETDB.guildtechnology_5 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_5;
	INSERT INTO TARGETDB.guildtechnology_6 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_6;
	INSERT INTO TARGETDB.guildtechnology_7 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_7;
	INSERT INTO TARGETDB.guildtechnology_8 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_8;
	INSERT INTO TARGETDB.guildtechnology_9 (guildId,entId,level,cdTime) SELECT guildId+add_guildId,entId,level,cdTime FROM SOURCEDB.guildtechnology_9;


	truncate table TARGETDB.guildtrans_0;
	truncate table TARGETDB.guildtrans_1;
	truncate table TARGETDB.guildtrans_2;
	truncate table TARGETDB.guildtrans_3;
	truncate table TARGETDB.guildtrans_4;
	truncate table TARGETDB.guildtrans_5;
	truncate table TARGETDB.guildtrans_6;
	truncate table TARGETDB.guildtrans_7;
	truncate table TARGETDB.guildtrans_8;
	truncate table TARGETDB.guildtrans_9;

	truncate table TARGETDB.guildtranslog;


	select '...........' as '合并数据:guilduser';
	INSERT INTO TARGETDB.guilduser (guildId,userId,name,guildPost,permissions,joinGuildTime,donateRes,donateItem,donateSum,todayDonateRes,todaydonateItem,todaydonateSum,guildConstruction,donateTime,weekDonateRes,weekDonateItem,weekDonateSum,clanId,buyNum,buyDttm,luckNum,luckDttm,shopBuyTime,nextShopBuyDttm) SELECT guildId+add_guildId,userId+add_userId,name,guildPost,permissions,joinGuildTime,donateRes,donateItem,donateSum,todayDonateRes,todaydonateItem,todaydonateSum,guildConstruction,donateTime,weekDonateRes,weekDonateItem,weekDonateSum,clanId,buyNum,buyDttm,luckNum,luckDttm,shopBuyTime,nextShopBuyDttm FROM SOURCEDB.guilduser;



	select '...........' as '合并数据:hero';
	INSERT INTO TARGETDB.hero_0 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_0;
	INSERT INTO TARGETDB.hero_1 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_1;
	INSERT INTO TARGETDB.hero_2 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_2;
	INSERT INTO TARGETDB.hero_3 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_3;
	INSERT INTO TARGETDB.hero_4 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_4;
	INSERT INTO TARGETDB.hero_5 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_5;
	INSERT INTO TARGETDB.hero_6 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_6;
	INSERT INTO TARGETDB.hero_7 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_7;
	INSERT INTO TARGETDB.hero_8 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_8;
	INSERT INTO TARGETDB.hero_9 (heroId,casId,userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,equ1,equ2,equ3,equ4,equ5,equ6,armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4) SELECT heroId+add_heroId,casId+add_casId,userId+add_userId,sysHeroId,name,level,exp,power,skill,intel,strategy,relifeNum,featureId,chartr,status,actionStatus,(case equ1 when 0 then equ1 else equ1+add_treasuryId end),(case equ2 when 0 then equ2 else equ2+add_treasuryId end),(case equ3 when 0 then equ3 else equ3+add_treasuryId end),(case equ4 when 0 then equ4 else equ4+add_treasuryId end),(case equ5 when 0 then equ5 else equ5+add_treasuryId end),(case equ6 when 0 then equ6 else equ6+add_treasuryId end),armyEntId,armyNum,govPotzId,govPotzTime,careerId,heroSkillId,growing,growingItem,freeDttm,heroSkillId1,heroSkillId2,heroSkillId3,heroSkillId4,heroSkillId5,heroSkillId6,heroFate,equipFate,gemId1,gemExp1,gemId2,gemExp2,gemId3,gemExp3,gemId4,gemExp4,gemId5,gemExp5,gemId6,gemExp6,gemId7,gemExp7,gemId8,gemExp8,gemId9,gemExp9,hangerEntId1,hangerEntId2,hangerEntId3,hangerEntId4 FROM SOURCEDB.hero_9;


	UPDATE TARGETDB.hero_0 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_1 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_2 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_3 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_4 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_5 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_6 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_7 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_8 set actionStatus=1,freeDttm=null;
	UPDATE TARGETDB.hero_9 set actionStatus=1,freeDttm=null;

	select '...........' as '合并数据:heroeffect';
	INSERT INTO TARGETDB.heroeffect_0 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_0;
	INSERT INTO TARGETDB.heroeffect_1 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_1;
	INSERT INTO TARGETDB.heroeffect_2 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_2;
	INSERT INTO TARGETDB.heroeffect_3 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_3;
	INSERT INTO TARGETDB.heroeffect_4 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_4;
	INSERT INTO TARGETDB.heroeffect_5 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_5;
	INSERT INTO TARGETDB.heroeffect_6 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_6;
	INSERT INTO TARGETDB.heroeffect_7 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_7;
	INSERT INTO TARGETDB.heroeffect_8 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_8;
	INSERT INTO TARGETDB.heroeffect_9 (heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT heroId+add_heroId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.heroeffect_9;


	INSERT INTO TARGETDB.hospital (casId,hosPoint,medical,lastUpdateTime) SELECT casId+add_casId,hosPoint,medical,lastUpdateTime FROM SOURCEDB.hospital;


	truncate table TARGETDB.jobcontext;
	truncate table TARGETDB.jobcontext_err;
	truncate table TARGETDB.lastarmyoutrecord;

	truncate table TARGETDB.leagueseason;
	truncate table TARGETDB.leagueuser;
	truncate table TARGETDB.leaguerank1;
	truncate table TARGETDB.leaguerank2;
	truncate table TARGETDB.leaguerank3;
	truncate table TARGETDB.leaguerank4;
	truncate table TARGETDB.leaguerank5;
	truncate table TARGETDB.leaguerank6;
	truncate table TARGETDB.leaguerank7;
	truncate table TARGETDB.leaguerank8;
	

	
	select '...........' as '合并数据:message';
	
	INSERT INTO TARGETDB.message_0 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_0;
	INSERT INTO TARGETDB.message_1 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_1;
	INSERT INTO TARGETDB.message_2 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_2;
	INSERT INTO TARGETDB.message_3 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_3;
	INSERT INTO TARGETDB.message_4 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_4;
	INSERT INTO TARGETDB.message_5 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_5;
	INSERT INTO TARGETDB.message_6 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_6;
	INSERT INTO TARGETDB.message_7 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_7;
	INSERT INTO TARGETDB.message_8 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_8;
	INSERT INTO TARGETDB.message_9 (sendUserId,receiveUserId,sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift) SELECT (case sendUserId when 0 then sendUserId else sendUserId+add_userId end),(case receiveUserId when 0 then 0 else receiveUserId+add_userId end),sendDttm,readDttm,comment,title,status,messageType,entityId0,itemNum0,entityId1,itemNum1,entityId2,itemNum2,entityId3,itemNum3,entityId4,itemNum4,entityId5,itemNum5,status0,map,isGift FROM SOURCEDB.message_9;



	
	select '...........' as '合并数据:rankdata';
	INSERT INTO TARGETDB.rankdata (userId,userName,countryId,userLv,userExp,rangeValue,guildId,guildName,dotaType,topStageId,topDttm) SELECT userId+add_userId,userName,countryId,userLv,userExp,rangeValue,(case guildId when 0 then 0 else guildId+add_guildId end),guildName,dotaType,topStageId,topDttm FROM SOURCEDB.rankdata;


	truncate table TARGETDB.refineryattr;


/*	TARGETDB.serverinfo 保留原来的数据，不修改，不合并*/

	truncate table TARGETDB.shrinesboss;

/*	TARGETDB.tbrealonline 保留原来的数据 */


	select '...........' as '合并数据:treasury';
	INSERT INTO TARGETDB.treasury_0 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_0;
	INSERT INTO TARGETDB.treasury_1 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_1;
	INSERT INTO TARGETDB.treasury_2 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_2;
	INSERT INTO TARGETDB.treasury_3 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_3;
	INSERT INTO TARGETDB.treasury_4 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_4;
	INSERT INTO TARGETDB.treasury_5 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_5;
	INSERT INTO TARGETDB.treasury_6 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_6;
	INSERT INTO TARGETDB.treasury_7 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_7;
	INSERT INTO TARGETDB.treasury_8 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_8;
	INSERT INTO TARGETDB.treasury_9 (id,userId,entId,entType,itemCount,useCount,band,equip,throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock) SELECT id+add_treasuryId,userId+add_userId,entId,entType,itemCount,useCount,band,(case equip when 0 then equip else equip+add_heroId end),throwAble,childType,isGift,existEndTime,level,holeNum,gemIds,randomProp,randomPropTmp,spiritLv,gemExp,isLock FROM SOURCEDB.treasury_9;

	truncate table TARGETDB.treasuryshortcut;

	select '...........' as '合并数据:troop';
	INSERT INTO TARGETDB.troop (troopId,userId,name,formationId,heroId1,heroId2,heroId3,heroId4,heroId5,armyEntId1,armyEntId2,armyEntId3,armyEntId4,armyEntId5,status,freeDttm,mainHeroId,showMode) SELECT troopId+add_troopId,userId+add_userId,name,formationId,(case heroId1 when 0 then heroId1 else heroId1+add_heroId end),(case heroId2 when 0 then heroId2 else heroId2+add_heroId end),(case heroId3 when 0 then heroId3 else heroId3+add_heroId end),(case heroId4 when 0 then heroId4 else heroId4+add_heroId end),(case heroId5 when 0 then heroId5 else heroId5+add_heroId end),armyEntId1,armyEntId2,armyEntId3,armyEntId4,armyEntId5,status,freeDttm,(case mainHeroId when 0 then mainHeroId else mainHeroId+add_heroId end),showMode FROM SOURCEDB.troop;
	UPDATE  TARGETDB.troop set status = 0,freeDttm = null;


		
/*	TARGETDB.tuannum 保留不合并*/


	select '...........' as '合并数据:user';
	INSERT INTO TARGETDB.user (userId,accId,userName,mainCastleId,sex,icon,usrLv,cash,skillPoint,honor,expPoint,lastLoginDttm,payPoint,protectStatus,protectEndDttm,tacticPoint,warStatus,curActPoint,lastActDmDttm,junGong,createDate,prestige,selfSignature,countryId,guildId,exitGuildDate,postId,titleId,titleDttm,buildSpeed,builderMaxNum,isEmployee,autoResumeArmy,lastAwardDttm,nextAwardLv,consumecash,officalId,sevenDayGift,lvRewards,troopId,hpPoint,star,cashTotal,armyAdviser) SELECT userId+add_userId,accId+add_accId,userName,mainCastleId+add_casId,sex,icon,usrLv,cash,skillPoint,honor,expPoint,lastLoginDttm,payPoint,protectStatus,protectEndDttm,tacticPoint,warStatus,curActPoint,lastActDmDttm,junGong,createDate,prestige,selfSignature,countryId,(case guildId when 0 then 0 else guildId+add_guildId end),exitGuildDate,postId,titleId,titleDttm,buildSpeed,builderMaxNum,isEmployee,autoResumeArmy,lastAwardDttm,nextAwardLv,consumecash,officalId,sevenDayGift,lvRewards,(case troopId when 0 then 0 else troopId+add_troopId end),hpPoint,star,cashTotal,armyAdviser FROM SOURCEDB.user;
	
	update TARGETDB.user set troopId= IFNULL((select min(troopId) from TARGETDB.troop where TARGETDB.troop.userId=TARGETDB.user.userId),0) where troopId=0;
	update TARGETDB.user set officalId=0,titleId=0,titleDttm=null,isEmployee=0;

	select '...........' as '合并数据:toweruser';
	INSERT INTO TARGETDB.toweruser(userId,troopId,resumePer,stageId,joinStatus,reliveTimes,freeJoinTime,
        itemJoinTime,lastJoinDttm,topStageId,topDttm,score,winItemId,winExpNum,winUserExp,
        seasonWinItemId,combatId,terrian) select userId+add_userId,(case troopId when 0 then 0 else troopId+add_troopId end),resumePer,stageId,joinStatus,reliveTimes,freeJoinTime,
        itemJoinTime,lastJoinDttm,topStageId,topDttm,score,winItemId,winExpNum,winUserExp,
        seasonWinItemId,combatId,terrian from SOURCEDB.toweruser;
	update TARGETDB.toweruser set troopId=0 where troopId!=0 and troopId  not in (select troopId from TARGETDB.troop);
	update TARGETDB.toweruser set troopId=IFNULL((select troopId from TARGETDB.user where TARGETDB.user.userId=TARGETDB.toweruser.userId),0) where troopId=0;


	select '...........' as '合并数据:userachieve';
	INSERT INTO TARGETDB.userachieve (userId,achieveId,type,finishDttm) SELECT userId+add_userId,achieveId,type,finishDttm FROM SOURCEDB.userachieve;


	select '...........' as '合并数据:useraltar';
	INSERT INTO TARGETDB.useraltar (userId,resourceNum,cashNum,lastShelter) SELECT userId+add_userId,resourceNum,cashNum,lastShelter FROM SOURCEDB.useraltar;

	INSERT INTO TARGETDB.useraltarcard_0 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_0;
	INSERT INTO TARGETDB.useraltarcard_1 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_1;
	INSERT INTO TARGETDB.useraltarcard_2 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_2;
	INSERT INTO TARGETDB.useraltarcard_3 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_3;
	INSERT INTO TARGETDB.useraltarcard_4 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_4;
	INSERT INTO TARGETDB.useraltarcard_5 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_5;
	INSERT INTO TARGETDB.useraltarcard_6 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_6;
	INSERT INTO TARGETDB.useraltarcard_7 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_7;
	INSERT INTO TARGETDB.useraltarcard_8 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_8;
	INSERT INTO TARGETDB.useraltarcard_9 (cardId,userId,num) SELECT cardId,userId+add_userId,num FROM SOURCEDB.useraltarcard_9;


	INSERT INTO TARGETDB.useranimal (userId,animal1,animal2,animal3,animal4,animal5,lastDttm,status) SELECT userId+add_userId,animal1,animal2,animal3,animal4,animal5,lastDttm,status FROM SOURCEDB.useranimal;
	delete from TARGETDB.useranimal where userId not in (select userId from TARGETDB.user);

	select '...........' as '合并数据:userattributes';
	INSERT INTO TARGETDB.userattributes_0 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_0;
	INSERT INTO TARGETDB.userattributes_1 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_1;
	INSERT INTO TARGETDB.userattributes_2 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_2;
	INSERT INTO TARGETDB.userattributes_3 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_3;
	INSERT INTO TARGETDB.userattributes_4 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_4;
	INSERT INTO TARGETDB.userattributes_5 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_5;
	INSERT INTO TARGETDB.userattributes_6 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_6;
	INSERT INTO TARGETDB.userattributes_7 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_7;
	INSERT INTO TARGETDB.userattributes_8 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_8;
	INSERT INTO TARGETDB.userattributes_9 (userId,attrName,attrValue) SELECT userId+add_userId,attrName,attrValue FROM SOURCEDB.userattributes_9;


	select '...........' as '合并数据:usercard';
	INSERT INTO TARGETDB.usercard_0 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_0;
	INSERT INTO TARGETDB.usercard_1 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_1;
	INSERT INTO TARGETDB.usercard_2 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_2;
	INSERT INTO TARGETDB.usercard_3 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_3;
	INSERT INTO TARGETDB.usercard_4 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_4;
	INSERT INTO TARGETDB.usercard_5 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_5;
	INSERT INTO TARGETDB.usercard_6 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_6;
	INSERT INTO TARGETDB.usercard_7 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_7;
	INSERT INTO TARGETDB.usercard_8 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_8;
	INSERT INTO TARGETDB.usercard_9 (userId,entId,throwAble,changeAble,status,addTime) SELECT userId+add_userId,entId,throwAble,changeAble,status,addTime FROM SOURCEDB.usercard_9;


	INSERT INTO TARGETDB.usercardinfo (userId,name,icon,level,exp,magic,stoveId,stoveNum,cardBox,changeCardBox,expireDttm1,expireDttm2) SELECT userId+add_userId,name,icon,level,exp,magic,stoveId,stoveNum,cardBox,changeCardBox,expireDttm1,expireDttm2 FROM SOURCEDB.usercardinfo;


	update TARGETDB.usercashbonus set openId = concat(openId,'_',target_areaId) where INSTR(openId,'_')=0;
	INSERT INTO TARGETDB.usercashbonus (openId,num,status,total,dttm) SELECT ( case INSTR(openId,'_') when 0 then concat(openId,'_',source_areaId) else openId end),num,status,total,dttm FROM SOURCEDB.usercashbonus;



	INSERT INTO TARGETDB.usercashconsumegift (userId,consumeGiftId,dttm) SELECT userId+add_userId,consumeGiftId,dttm FROM SOURCEDB.usercashconsumegift;


	truncate table TARGETDB.usercombat;

	select '...........' as '合并数据:usercontinueloginaward';

	INSERT INTO TARGETDB.usercontinueloginaward_0 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_0;
	INSERT INTO TARGETDB.usercontinueloginaward_1 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_1;
	INSERT INTO TARGETDB.usercontinueloginaward_2 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_2;
	INSERT INTO TARGETDB.usercontinueloginaward_3 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_3;
	INSERT INTO TARGETDB.usercontinueloginaward_4 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_4;
	INSERT INTO TARGETDB.usercontinueloginaward_5 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_5;
	INSERT INTO TARGETDB.usercontinueloginaward_6 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_6;
	INSERT INTO TARGETDB.usercontinueloginaward_7 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_7;
	INSERT INTO TARGETDB.usercontinueloginaward_8 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_8;
	INSERT INTO TARGETDB.usercontinueloginaward_9 (userId,pf,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,pf,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.usercontinueloginaward_9;



	INSERT INTO TARGETDB.usercount (userId,type,num,lastDttm) SELECT userId+add_userId,type,num,lastDttm FROM SOURCEDB.usercount;

	
	truncate table TARGETDB.usercountryoffical;
	truncate table TARGETDB.userspecialcountryoffical;


	INSERT INTO TARGETDB.userdailyactivity (userId,currDttm,act1,act2,awards,total,lastLoginDttm,cntLoginNum,loginAward) SELECT userId+add_userId,currDttm,act1,act2,awards,total,lastLoginDttm,cntLoginNum,loginAward FROM SOURCEDB.userdailyactivity;


	INSERT INTO TARGETDB.userdailymissionnum (userId,missionType,num,lastDttm) SELECT userId+add_userId,missionType,num,lastDttm FROM SOURCEDB.userdailymissionnum;

	select '...........' as '合并数据:usereffect';

	INSERT INTO TARGETDB.usereffect (userId,entId,effTypeId,absValue,perValue,showFlag,expireDttm) SELECT userId+add_userId,entId,effTypeId,absValue,perValue,showFlag,expireDttm FROM SOURCEDB.usereffect;


	select '...........' as '合并数据:userenemy';
	INSERT INTO TARGETDB.userenemy_0 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_0;
	INSERT INTO TARGETDB.userenemy_1 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_1;
	INSERT INTO TARGETDB.userenemy_2 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_2;
	INSERT INTO TARGETDB.userenemy_3 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_3;
	INSERT INTO TARGETDB.userenemy_4 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_4;
	INSERT INTO TARGETDB.userenemy_5 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_5;
	INSERT INTO TARGETDB.userenemy_6 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_6;
	INSERT INTO TARGETDB.userenemy_7 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_7;
	INSERT INTO TARGETDB.userenemy_8 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_8;
	INSERT INTO TARGETDB.userenemy_9 (userId,enemyCasId,atkDttm,atkCount,enemyUserId) SELECT userId+add_userId,enemyCasId+add_casId,atkDttm,atkCount,enemyUserId+add_userId FROM SOURCEDB.userenemy_9;






	/*删除usergift*/
	truncate table TARGETDB.usergift;

	/*删除金矿数据*/
	truncate table TARGETDB.usergold;


	INSERT INTO TARGETDB.usergovpotz (userId,govPotzId,giveNum,allNum) SELECT userId+add_userId,govPotzId,giveNum,allNum FROM SOURCEDB.usergovpotz;

	/*这个表干吗的呢??*/
	truncate table TARGETDB.userimpression;

	truncate table TARGETDB.userinvite;

	INSERT INTO TARGETDB.userliveskill (userId,woodSkillId,oreSkillId,drugSkillId,sandSkillId) SELECT userId+add_userId,woodSkillId,oreSkillId,drugSkillId,sandSkillId FROM SOURCEDB.userliveskill;

	truncate table TARGETDB.userluckyrank;
	truncate table TARGETDB.userluckyrankinfo;



	select '...........' as '合并数据:usermission';
	INSERT INTO TARGETDB.usermission_0 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_0;
	INSERT INTO TARGETDB.usermission_1 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_1;
	INSERT INTO TARGETDB.usermission_2 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_2;
	INSERT INTO TARGETDB.usermission_3 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_3;
	INSERT INTO TARGETDB.usermission_4 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_4;
	INSERT INTO TARGETDB.usermission_5 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_5;
	INSERT INTO TARGETDB.usermission_6 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_6;
	INSERT INTO TARGETDB.usermission_7 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_7;
	INSERT INTO TARGETDB.usermission_8 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_8;
	INSERT INTO TARGETDB.usermission_9 (missionId,userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0) SELECT missionId,userId+add_userId,completeLimitTime,status,num1,num2,num3,num4,num5,factor,read0 FROM SOURCEDB.usermission_9;


	INSERT INTO TARGETDB.usernobody (userId,luckTip0,luckTip1,luckTime,alarmNobody,eventNobody0,eventNobody1,eventTime) SELECT userId+add_userId,luckTip0,luckTip1,luckTime,alarmNobody,eventNobody0,eventNobody1,eventTime FROM SOURCEDB.usernobody;


	INSERT INTO TARGETDB.usernpcanimus (userId,junior,winNum0,isConquest0,npcIds0,baomin,winNum1,isConquest1,npcIds1,liukou,winNum2,isConquest2,npcIds2,shuzu,winNum3,isConquest3,npcIds3,panjun,winNum4,isConquest4,npcIds4,manzu,winNum5,isConquest5,npcIds5,lastAddTime) SELECT userId+add_userId,junior,winNum0,isConquest0,npcIds0,baomin,winNum1,isConquest1,npcIds1,liukou,winNum2,isConquest2,npcIds2,shuzu,winNum3,isConquest3,npcIds3,panjun,winNum4,isConquest4,npcIds4,manzu,winNum5,isConquest5,npcIds5,lastAddTime FROM SOURCEDB.usernpcanimus;

	INSERT INTO TARGETDB.usernpcattacked (userId,dttm,maxNpcId,atkDesc) SELECT userId+add_userId,dttm,maxNpcId,atkDesc FROM SOURCEDB.usernpcattacked;

	INSERT INTO TARGETDB.userpet (userId,petId,feedNum,feedUser,maturnTime,status,eggedNum) SELECT userId+add_userId,petId,feedNum,feedUser,maturnTime,status,eggedNum FROM SOURCEDB.userpet;
	update TARGETDB.userpet set feedNum=0,feedUser='';


	INSERT INTO TARGETDB.userpubattr (userId,hireNum1,hireCD1,hireNum2,hireCD2,hireNum3,hireCD3) SELECT userId+add_userId,hireNum1,hireCD1,hireNum2,hireCD2,hireNum3,hireCD3 FROM SOURCEDB.userpubattr;

	select '...........' as '合并数据:userqqgift';
	INSERT INTO TARGETDB.userqqgift_0 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_0;
	INSERT INTO TARGETDB.userqqgift_1 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_1;
	INSERT INTO TARGETDB.userqqgift_2 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_2;
	INSERT INTO TARGETDB.userqqgift_3 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_3;
	INSERT INTO TARGETDB.userqqgift_4 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_4;
	INSERT INTO TARGETDB.userqqgift_5 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_5;
	INSERT INTO TARGETDB.userqqgift_6 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_6;
	INSERT INTO TARGETDB.userqqgift_7 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_7;
	INSERT INTO TARGETDB.userqqgift_8 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_8;
	INSERT INTO TARGETDB.userqqgift_9 (userId,giftId,dttm,status) SELECT userId+add_userId,giftId,dttm,status FROM SOURCEDB.userqqgift_9;



	INSERT INTO TARGETDB.userquestion (userId,lastQuestionId,record,lastDttm) SELECT userId+add_userId,lastQuestionId,record,lastDttm FROM SOURCEDB.userquestion;

	truncate table TARGETDB.userrank0;
	truncate table TARGETDB.userrank1;
	truncate table TARGETDB.userrank2;
	truncate table TARGETDB.userrank3;
	truncate table TARGETDB.userrank4;
	truncate table TARGETDB.userrank5;
	truncate table TARGETDB.userrank6;
	truncate table TARGETDB.userrank7;



	INSERT INTO TARGETDB.usershared (userId,dailyShareDttm,dailyShareNum,shareEventDttm,shareEventNum,status) SELECT userId+add_userId,dailyShareDttm,dailyShareNum,shareEventDttm,shareEventNum,status FROM SOURCEDB.usershared;


	select '...........' as '合并数据:usershrines';
	INSERT INTO TARGETDB.usershrines (userId,value,putFreeNum,getFreeNum,itemPutFreeNum,itemGetFreeNum,lastDttm,godId1,godUserId1,endDttm1,godId2,godUserId2,endDttm2,godId3,godUserId3,endDttm3,godId4,godUserId4,endDttm4,godId5,godUserId5,endDttm5) SELECT userId+add_userId,value,putFreeNum,getFreeNum,itemPutFreeNum,itemGetFreeNum,lastDttm,godId1,godUserId1,endDttm1,godId2,godUserId2,endDttm2,godId3,godUserId3,endDttm3,godId4,godUserId4,endDttm4,godId5,godUserId5,endDttm5 FROM SOURCEDB.usershrines;
	
	/*神全部修改为自己请的*/
	update TARGETDB.usershrines set godUserId1 = (case godId1 when 0 then 0 else  userId end),godUserId2 = (case godId2 when 0 then 0 else  userId end),godUserId3=(case godId3 when 0 then 0 else  userId end),godUserId4=(case godId4 when 0 then 0 else  userId end),godUserId5=(case godId5 when 0 then 0 else  userId end);


	select '...........' as '合并数据:usersign';
	INSERT INTO TARGETDB.usersign (userId,month,lastVipLv,cntSign,dttm) SELECT userId+add_userId,month,lastVipLv,cntSign,dttm FROM SOURCEDB.usersign;
	
	INSERT INTO TARGETDB.userstove (userId,cardId,needCards,producerId,status,needSceond,endDttm) SELECT userId+add_userId,cardId,needCards,producerId+add_userId,status,needSceond,endDttm FROM SOURCEDB.userstove;

	select '...........' as '合并数据:usertask';
	INSERT INTO TARGETDB.usertask (userId,currTaskId,endDttm,honor,refreshNum,lastDttm,stars,awards,taskId0,taskId1,taskId2,taskId3,taskId4,usedPackType,packPos0,packPos1,packPos2,packPos3,packPos4,packPos5,packPos6,childType0,childType1,childType2,childType3,parentTypeExp0,parentTypeExp1,parentTypeExp2,parentTypeExp3) SELECT userId+add_userId,currTaskId,endDttm,honor,refreshNum,lastDttm,stars,awards,taskId0,taskId1,taskId2,taskId3,taskId4,usedPackType,packPos0,packPos1,packPos2,packPos3,packPos4,packPos5,packPos6,childType0,childType1,childType2,childType3,parentTypeExp0,parentTypeExp1,parentTypeExp2,parentTypeExp3 FROM SOURCEDB.usertask;


	select '...........' as '合并数据:usertech';
	INSERT INTO TARGETDB.usertech_0 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_0;
	INSERT INTO TARGETDB.usertech_1 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_1;
	INSERT INTO TARGETDB.usertech_2 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_2;
	INSERT INTO TARGETDB.usertech_3 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_3;
	INSERT INTO TARGETDB.usertech_4 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_4;
	INSERT INTO TARGETDB.usertech_5 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_5;
	INSERT INTO TARGETDB.usertech_6 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_6;
	INSERT INTO TARGETDB.usertech_7 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_7;
	INSERT INTO TARGETDB.usertech_8 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_8;
	INSERT INTO TARGETDB.usertech_9 (userId,entId,level,beginDttm,endDttm) SELECT userId+add_userId,entId,level,beginDttm,endDttm FROM SOURCEDB.usertech_9;

	INSERT INTO TARGETDB.usertuanshop (userId,point,lastShop) SELECT userId+add_userId,point,lastShop FROM SOURCEDB.usertuanshop;

	select '...........' as '合并数据:uservip';

	INSERT INTO TARGETDB.uservip (userId,vipStartTime,vipEndTime,lastDttm,vipLv,vipPoint,prizeCount,totalCash,reward,everyReward,unfreeReward) SELECT userId+add_userId,vipStartTime,vipEndTime,lastDttm,vipLv,vipPoint,prizeCount,totalCash,reward,everyReward,unfreeReward FROM SOURCEDB.uservip;



	select '...........' as '合并数据:userqqprivilegeopenaward';
	INSERT INTO TARGETDB.userqqprivilegeopenaward (userId,type,num,point,dttm) SELECT userId+add_userId,type,num,point,dttm FROM SOURCEDB.userqqprivilegeopenaward;


	INSERT INTO TARGETDB.userrechargelog (userId,type,cash,balance,qb,quan,dttm,orderId,pf) SELECT userId+add_userId,type,cash,balance,qb,quan,dttm,orderId,pf FROM SOURCEDB.userrechargelog;
	INSERT INTO TARGETDB.userconsumelog (userId,reason,cash,balance,pf,dttm) SELECT userId+add_userId,reason,cash,balance,pf,dttm FROM SOURCEDB.userconsumelog;
	INSERT INTO TARGETDB.userawardactivity (userId,actId,num,dttm) SELECT userId+add_userId,actId,num,dttm FROM SOURCEDB.userawardactivity;

	INSERT INTO TARGETDB.useropenegg (userId,dttm,freeIronNum,ironNum,silverNum,goldNum) SELECT userId+add_userId,dttm,freeIronNum,ironNum,silverNum,goldNum FROM SOURCEDB.useropenegg;	
	INSERT INTO TARGETDB.userconsumegift (userId,pf,cash,awardNum,dttm) SELECT userId+add_userId,pf,cash,awardNum,dttm FROM SOURCEDB.userconsumegift;
	INSERT INTO TARGETDB.userrechargeaward(userId, pf, cash, num, type, dttm) SELECT userId+add_userId, pf, cash, num, type, dttm FROM SOURCEDB.userrechargeaward;




	select '...........' as '合并数据:userfestival';
	INSERT INTO TARGETDB.userfestival (userId,type,totalNum,remainNum,freeNum,lastDttm) SELECT userId+add_userId,type,totalNum,remainNum,freeNum,lastDttm FROM SOURCEDB.userfestival;



	select '...........' as '合并数据:userfund';
	INSERT INTO TARGETDB.userfund(userId,fundId,num,lastDttm,buyDttm,originalCash) SELECT userId+add_userId,fundId,num,lastDttm,buyDttm,originalCash FROM SOURCEDB.userfund;


	select '...........' as '合并数据:userseaworld';
	INSERT INTO TARGETDB.userseaworld(userId,worldId,dttm,freeJoinTime,itemJoinTime,stageId,point) select userId+add_userId,worldId,dttm,freeJoinTime,itemJoinTime,stageId,point from SOURCEDB.userseaworld;


	truncate table TARGETDB.userhallstage;



	select '...........' as '合并结婚数据';
	INSERT INTO TARGETDB.marri_user_room(userId,roomLv) select userId+add_userId,roomLv from SOURCEDB.marri_user_room;
	INSERT INTO TARGETDB.marriage_seek(userId,marriageContext,finishTime) select userId+add_userId,marriageContext,finishTime from SOURCEDB.marriage_seek;

	INSERT INTO TARGETDB.marriage_status(userId,marriMaxLv,blessContext,blessTime,maxRingLv,reqType,careCount,careTime) select userId+add_userId,marriMaxLv,blessContext,blessTime,maxRingLv,reqType,careCount,careTime from SOURCEDB.marriage_status;
	UPDATE TARGETDB.marriage_status set blessContext = null;
	INSERT INTO TARGETDB.usermarriage(userId,targetId,createdDttm,blessCount,finishMarrTime,marriStatus,loveCount,lv,marriType,divorceFinishTime,divorceSide) select userId+add_userId,targetId+add_userId,createdDttm,blessCount,finishMarrTime,marriStatus,loveCount,lv,marriType,divorceFinishTime,divorceSide from SOURCEDB.usermarriage;




	select '...........' as '君主秘宝';
	INSERT INTO TARGETDB.usertreasure(userId,tid1,prop1,tid2,prop2,tid3,prop3,tid4,prop4,tid5,prop5,tid6,prop6) select userId+add_userId,tid1,prop1,tid2,prop2,tid3,prop3,tid4,prop4,tid5,prop5,tid6,prop6 from SOURCEDB.usertreasure;


	truncate table TARGETDB.userconquer;

	select '...........' as 'usercombatsoul';
	INSERT INTO TARGETDB.usercombatsoul(userId,num1,num2,num3,num4,num5,num6,num7,num8,lastDttm,freeNum,itemNum,trainNum) select userId+add_userId,num1,num2,num3,num4,num5,num6,num7,num8,lastDttm,freeNum,itemNum,trainNum from SOURCEDB.usercombatsoul;


	insert into TARGETDB.userspringfestival(userId,freeNum,refreshFreeNum,awardIndex,excludeStr,lastDttm) select userId+add_userId,freeNum,refreshFreeNum,awardIndex,excludeStr,lastDttm from SOURCEDB.userspringfestival;



	INSERT INTO TARGETDB.guildshop(guildId,entData,nextDttm) select guildId+add_guildId,entData,nextDttm from SOURCEDB.guildshop;


	select '...........' as '冒险数据';
	INSERT INTO TARGETDB.userriskdata(userId,cdDttm,cdNum,cdDay,clearJoinNum,clearDay) select userId+add_userId,cdDttm,cdNum,cdDay,clearJoinNum,clearDay from SOURCEDB.userriskdata;
	INSERT INTO TARGETDB.userriskscene_0 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_0;
	INSERT INTO TARGETDB.userriskscene_1 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_1;
	INSERT INTO TARGETDB.userriskscene_2 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_2;
	INSERT INTO TARGETDB.userriskscene_3 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_3;
	INSERT INTO TARGETDB.userriskscene_4 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_4;
	INSERT INTO TARGETDB.userriskscene_5 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_5;
	INSERT INTO TARGETDB.userriskscene_6 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_6;
	INSERT INTO TARGETDB.userriskscene_7 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_7;
	INSERT INTO TARGETDB.userriskscene_8 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_8;
	INSERT INTO TARGETDB.userriskscene_9 (userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14) SELECT userId+add_userId,pid,starAward1,starAward2,starAward3,sceneId0,star0,dttm0,joinNum0,sceneId1,star1,dttm1,joinNum1,sceneId2,star2,dttm2,joinNum2,sceneId3,star3,dttm3,joinNum3,sceneId4,star4,dttm4,joinNum4,sceneId5,star5,dttm5,joinNum5,sceneId6,star6,dttm6,joinNum6,sceneId7,star7,dttm7,joinNum7,sceneId8,star8,dttm8,joinNum8,sceneId9,star9,dttm9,joinNum9,sceneId10,star10,dttm10,joinNum10,sceneId11,star11,dttm11,joinNum11,sceneId12,star12,dttm12,joinNum12,sceneId13,star13,dttm13,joinNum13,sceneId14,star14,dttm14,joinNum14 FROM SOURCEDB.userriskscene_9;

	select '...........' as '卡片，魂魄数据';
	INSERT INTO TARGETDB.userherocard (userId,cardIds) SELECT userId+add_userId,cardIds FROM SOURCEDB.userherocard;
	INSERT INTO TARGETDB.userherosoul (userId,soulIds) SELECT userId+add_userId,soulIds FROM SOURCEDB.userherosoul;


	INSERT INTO TARGETDB.userdailyshopitem (userId,shopId,num,total,dttm) SELECT userId+add_userId,shopId,num,total,dttm FROM SOURCEDB.userdailyshopitem;
	INSERT INTO TARGETDB.userguide (userId,stageIds) SELECT userId+add_userId,stageIds FROM SOURCEDB.userguide;
	
	select '...........' as '好友体力';
	INSERT INTO TARGETDB.friendhp (userId,giftUserIds,giveMeUserIds,receiveUserIds,lastTime) SELECT userId+add_userId,giftUserIds,giveMeUserIds,receiveUserIds,lastTime FROM SOURCEDB.friendhp;
	update TARGETDB.friendhp set giftUserIds=null,giveMeUserIds=null,receiveUserIds=null;

	INSERT INTO TARGETDB.platformfriendhp (userId,giftUserIds,giveMeUserIds,receiveNum,dttm) SELECT userId+add_userId,giftUserIds,giveMeUserIds,receiveNum,dttm FROM SOURCEDB.platformfriendhp;
	update TARGETDB.platformfriendhp set giftUserIds=null,giveMeUserIds=null;

	select '...........' as '各种商店';
	INSERT INTO TARGETDB.usermysticshop (userId,shopId,curShopIds,curBuyStatus,buyShopIds,freeCount,lastTimes,cashCount) SELECT userId+add_userId,shopId,curShopIds,curBuyStatus,buyShopIds,freeCount,lastTimes,cashCount FROM SOURCEDB.usermysticshop;
	INSERT INTO TARGETDB.userexp (userId, taskNum, refNum, score, missions, awardStr, executeDateStr) SELECT userId+add_userId, taskNum, refNum, score, missions, awardStr, executeDateStr FROM SOURCEDB.userexp;
	
	

	select '...........' as '逐鹿';
	truncate table TARGETDB.warseasondata;
	truncate table TARGETDB.guildapplydata;
	truncate table TARGETDB.userapplydata;
	truncate table TARGETDB.waruserrank;
	truncate table TARGETDB.warguildrank;
	truncate table TARGETDB.warcountryrank;
	truncate table TARGETDB.wardoublecity;
	/*
	UPDATE TARGETDB.wardoublecity set guildId=0,guildName=0,winNum=0,point=0;
	*/

	select '...........' as '钓鱼';
	INSERT INTO TARGETDB.userfish (userId,fishEntId,fishNum) SELECT userId+add_userId,fishEntId,fishNum FROM SOURCEDB.userfish;

	select '...........' as '重楼活动';
	INSERT INTO TARGETDB.userrecharge (userId,rechargeId,num,begDttm,endDttm,awardDttm) SELECT userId+add_userId,rechargeId,num,begDttm,endDttm,awardDttm FROM SOURCEDB.userrecharge;
	update TARGETDB.towerranksfirstdata t, SOURCEDB.towerranksfirstdata s set t.userId=s.userId+add_userId,t.powerNum=s.powerNum,t.dttm=s.dttm where t.stageId=s.stageId and t.dttm>s.dttm ;

	insert into TARGETDB.towerranksmindata(stageId,userId,userName,powerNum,dttm) select stageId,userId+add_userId,userName,powerNum,dttm from SOURCEDB.towerranksmindata;
	SET @row_num=0;SET @stage_id='';
	delete from TARGETDB.towerranksmindata where id in (select id from (SELECT id,stageId,userId ,powerNum,CASE WHEN @stage_id = stageId THEN @row_num:=@row_num+1 ELSE @row_num:=1 END rownum, @stage_id:=stageId FROM TARGETDB.towerranksmindata ORDER BY stageId,powerNum) tmp where rownum>3);  
	update TARGETDB.towerranksmindata set userName = (select userName from TARGETDB.user where userId = TARGETDB.towerranksmindata.userId);


	INSERT INTO TARGETDB.userillustrations (userId,type,hasNew,num,ids1,ids2,ids3,ids4,ids5,ids6,ids7) SELECT userId+add_userId,type,hasNew,num,ids1,ids2,ids3,ids4,ids5,ids6,ids7 FROM SOURCEDB.userillustrations;

	select '...........' as '军师';
	INSERT INTO TARGETDB.userarmyadviser (userId,adviserId,level,equip1,equip2,equip3,equip4,equip5,equip6) SELECT userId+add_userId,adviserId,level,equip1,equip2,equip3,equip4,equip5,equip6 FROM SOURCEDB.userarmyadviser;

	INSERT INTO TARGETDB.useroperateactivity (userId,actId,type,status,lastTime) SELECT userId+add_userId,actId,type,status,lastTime FROM SOURCEDB.useroperateactivity;


	select '...........' as '官职';
	truncate table TARGETDB.userprestigeranks;
	truncate table TARGETDB.userprestigecountryranks1;
	truncate table TARGETDB.userprestigecountryranks2;
	truncate table TARGETDB.userprestigecountryranks3;
	truncate table TARGETDB.userprestigecountryranks4;
	truncate table TARGETDB.userprestigecountryranks5;
	truncate table TARGETDB.userprestigecountryranks6;
	truncate table TARGETDB.userprestigecountryranks7;

	truncate table TARGETDB.userprestigecountryranks_bak;
	truncate table TARGETDB.userofficial;
	truncate table TARGETDB.officialtickets;
	truncate table TARGETDB.usercivilofficial;

	update TARGETDB.syspara_gm set paraValue =concat(DATE_FORMAT(DATE_ADD(now(),INTERVAL 1 DAY),'%Y-%m-%d'),' 00:00:00') where paraId='PRESTIGE_RANK_LASTDTTM';
	update TARGETDB.syspara_gm set paraValue ='1'  where paraId='GUILD_WAR_OPEN';
	update TARGETDB.syspara set paraValue ='1'  where paraId='GUILD_WAR_OPEN';



	select '...........' as 'PVP';
	INSERT INTO TARGETDB.userpvp (userId,userName,fromInviteEndDttm,fromInvitedUserId,fromDayInviteUserIds,toInviteEndDttm,toNowarEndDttm,toDeportationEndDttm,combatMessages,armys,armyoutId) SELECT userId+add_userId,userName,null,0,null,null,toNowarEndDttm,toDeportationEndDttm,null,null,0 FROM SOURCEDB.userpvp;
	UPDATE TARGETDB.userpvp set fromInviteEndDttm=null,fromInvitedUserId=0,fromDayInviteUserIds=null,toInviteEndDttm=null,combatMessages=null,armys=null,armyoutId=0;
	truncate table TARGETDB.armyout;

	select '...........' as 'guildWar';
	/*
	update TARGETDB.guildwarseasondata set round=7,period=40;
	*/
	truncate table TARGETDB.guildwarseasondata;
	truncate table TARGETDB.guildvsdata;
	truncate table TARGETDB.guildwaruserapply;

	select '...........' as '门客';
	INSERT INTO TARGETDB.userhangerdata (userId,fame,active,lastDttm,lastChapterId,lastType,result) SELECT userId+add_userId,fame,active,lastDttm,lastChapterId,lastType,result FROM SOURCEDB.userhangerdata;

	INSERT INTO TARGETDB.userhanger_0 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_0;
	INSERT INTO TARGETDB.userhanger_1 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_1;
	INSERT INTO TARGETDB.userhanger_2 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_2;
	INSERT INTO TARGETDB.userhanger_3 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_3;
	INSERT INTO TARGETDB.userhanger_4 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_4;
	INSERT INTO TARGETDB.userhanger_5 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_5;
	INSERT INTO TARGETDB.userhanger_6 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_6;
	INSERT INTO TARGETDB.userhanger_7 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_7;
	INSERT INTO TARGETDB.userhanger_8 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_8;
	INSERT INTO TARGETDB.userhanger_9 (entId,userId,status,heroId,level,color,prop,extrarop) SELECT entId,userId+add_userId,status,heroId+add_heroId,level,color,prop,extrarop FROM SOURCEDB.userhanger_9;
	
	INSERT INTO TARGETDB.userhangerpaper_0 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_0;
	INSERT INTO TARGETDB.userhangerpaper_1 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_1;
	INSERT INTO TARGETDB.userhangerpaper_2 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_2;
	INSERT INTO TARGETDB.userhangerpaper_3 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_3;
	INSERT INTO TARGETDB.userhangerpaper_4 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_4;
	INSERT INTO TARGETDB.userhangerpaper_5 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_5;
	INSERT INTO TARGETDB.userhangerpaper_6 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_6;
	INSERT INTO TARGETDB.userhangerpaper_7 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_7;
	INSERT INTO TARGETDB.userhangerpaper_8 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_8;
	INSERT INTO TARGETDB.userhangerpaper_9 (entId,userId,num) SELECT entId,userId+add_userId,num FROM SOURCEDB.userhangerpaper_9;

	/*删除竞价商店数据*/
	truncate table TARGETDB.biddingactivity;
	truncate table TARGETDB.userbiddingdata;
	truncate table TARGETDB.biddingactivity_bk;
	truncate table TARGETDB.userbiddingdata_bk;

	/*删除限时商店数据*/
	truncate table TARGETDB.usertimershop;

	/*玩家头像*/
	INSERT INTO TARGETDB.usericon (userId,userIconId,passDttm,status) SELECT userId+add_userId,userIconId,passDttm,status FROM SOURCEDB.usericon;

	/*官职商店*/
	INSERT INTO TARGETDB.userofficialshop(userId,dttm,ids) SELECT userId+add_userId,dttm,ids FROM SOURCEDB.userofficialshop;

	/*cdkey*/
	INSERT INTO TARGETDB.usercdkey(userId,keyType,cdkey,dttm) SELECT userId+add_userId,keyType,cdkey,dttm FROM SOURCEDB.usercdkey;

	/*天子公告*/
	INSERT INTO TARGETDB.forum(forumType, title, forumContext, lastTime,countryId,tianZiName) SELECT forumType, title, forumContext, lastTime,countryId,tianZiName FROM SOURCEDB.forum where forumType=1;
	/*删除合服当天的天子公告*/
	delete from TARGETDB.forum where DAYOFYEAR(lastTime)=DAYOFYEAR(now());


	/*联盟战*/
	INSERT INTO TARGETDB.guildapphuns (guildId,guildName,countryId,areaId,timeKey,appTime,joinDttm,combatDttm,endDttm,consumeCons) SELECT guildId+add_guildId,guildName,countryId,areaId,timeKey,appTime,joinDttm,combatDttm,endDttm,consumeCons FROM SOURCEDB.guildapphuns;



	/*去掉错误数据*/
	update TARGETDB.guilduser set guildId = (select guildId from TARGETDB.user u where u.userId =TARGETDB.guilduser.userId); 
	update TARGETDB.guilduser set guildPost = 5 where guildPost=1 and userid not in (select leaderId from TARGETDB.guild); 
	delete from TARGETDB.guilduser where guildId not in (select guildId from TARGETDB.guild);
	update TARGETDB.user set guildId = 0 where guildId!=0 and userId not in (select userId from TARGETDB.guilduser);
	update TARGETDB.user set guildId = 0 where guildId!=0 and guildId not in (select guildId from TARGETDB.guild);
	
	/*更新联盟成员总数*/
	update TARGETDB.guild set userNum = (select count(*) from TARGETDB.guilduser gu where gu.guildId = TARGETDB.guild.guildId);
	update TARGETDB.guild set leader = (select userName from TARGETDB.user where userId = TARGETDB.guild.leaderId);
	update TARGETDB.guild set creator='' where creator not in (select userName from TARGETDB.user);
	
	/*更新排行数据*/
	update TARGETDB.rankdata set guildId = 0,guildName=null where guildId!=0 and guildId not in(select guildId from TARGETDB.guild );
	update TARGETDB.rankdata set guildName = (select name from TARGETDB.guild g where g.guildId = TARGETDB.rankdata.guildId) where guildId!=0;
	update TARGETDB.rankdata set rangeValue = (select rangeValue from TARGETDB.castle c inner join TARGETDB.user u on c.userId=u.userId where parentCasId=0 and u.userId = TARGETDB.rankdata.userId) ;


	INSERT INTO TARGETDB.serverinfo SELECT * FROM SOURCEDB.serverinfo;

/*
	更新成新的最大值
*/

	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_accId from  SOURCEDB.idgen where idType='ACCOUNT') where idType='ACCOUNT';
	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_userId from  SOURCEDB.idgen where idType='USER') where idType='USER';
	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_casId from  SOURCEDB.idgen where idType='CASTLE') where idType='CASTLE';
	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_heroId from  SOURCEDB.idgen where idType='HERO') where idType='HERO';
	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_treasuryId from  SOURCEDB.idgen where idType='TREASURY') where idType='TREASURY';
	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_troopId from  SOURCEDB.idgen where idType='TROOP') where idType='TROOP';
	UPDATE  TARGETDB.idgen set currValue = (SELECT currValue+add_guildId from  SOURCEDB.idgen where idType='GUILD') where idType='GUILD';

	select * from TARGETDB.idgen;
	
	select '...........' as '合并数据库完成.';

END	
//
DELIMITER ;
call TARGETDB.mergedb;