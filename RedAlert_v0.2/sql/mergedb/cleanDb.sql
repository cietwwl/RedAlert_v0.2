DROP PROCEDURE IF EXISTS TARGETDB.cleanDb;
DELIMITER //
CREATE PROCEDURE TARGETDB.cleanDb()
BEGIN

/*
	minDay 天没有登录，消费金额小于mincash,君主等级<=minusrlv 不是盟主
*/
    DECLARE minDay int default 30;
    DECLARE mincash int default 0;
    DECLARE minusrlv int default 5;
    DECLARE minusrlv2 int default 30;
    DECLARE clearAccountNum int default 0;

    
	/* 删除所有没有创建角色的帐号*/
	delete from account where accId not in (select accId from user);

	/*要删除的account,user,主城*/
	DROP TABLE IF EXISTS TARGETDB.cleanaccount;

	CREATE TABLE TARGETDB.cleanaccount (
	  `accId` bigint NOT NULL default 0,
	  `userId` bigint NOT NULL default 0,
	  `casId` bigint NOT NULL default 0,
	  PRIMARY KEY  (`userId`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	alter table TARGETDB.cleanaccount add index ndex_cleanaccount_casId(casId);
	alter table TARGETDB.cleanaccount add index ndex_cleanaccount_accId(accId);

	/*要删除的分城*/
	DROP TABLE IF EXISTS TARGETDB.cleanbranchcastle;

	CREATE TABLE TARGETDB.cleanbranchcastle (
	  `casId` bigint NOT NULL default 0,
	  PRIMARY KEY  (`casId`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;


	insert into TARGETDB.cleanaccount select a.accId,u.userId,u.mainCastleId from TARGETDB.account a inner join TARGETDB.user u on a.accId=u.accId where u.mainCastleId!=0 and u.usrLv<=minusrlv and   u.cashTotal<=0 and u.userId not in (select leaderId from TARGETDB.guild);
	/*maincastleId=0的也删除*/
	insert into TARGETDB.cleanaccount select a.accId,u.userId,c.casId from TARGETDB.account a inner join TARGETDB.user u on (a.accId=u.accId and u.mainCastleId = 0 ) inner join TARGETDB.castle c on u.userId=c.userId where u.usrLv<=minusrlv and u.cashTotal<=0 ;
	/*
	insert into TARGETDB.cleanaccount select a.accId,u.userId,u.mainCastleId from TARGETDB.account a inner join TARGETDB.user u on a.accId=u.accId where u.usrLv>minusrlv and  u.usrLv<=minusrlv2 and (TO_DAYS(NOW()) - TO_DAYS(a.lastLoginDttm)) >minDay and cashTotal<=0 and u.userId not in (select leaderId from TARGETDB.guild);
	*/

	/*由于错误数据，产生的垃圾数据*/
	/*insert into TARGETDB.cleanaccount select 0 ,userId,mainCastleId from TARGETDB.user where accId not in (select accId from TARGETDB.account);*/


	select count(*) into clearAccountNum from cleanaccount;
	select clearAccountNum as '清理死号个数';




	
	if clearAccountNum>0 then

	select count(*) into @hasIndex from information_schema.statistics where TABLE_SCHEMA='TARGETDB' and TABLE_NAME='castle' and INDEX_NAME='tmp_index_parentcasId';
	if @hasIndex >0 then
		alter table TARGETDB.castle drop index tmp_index_parentcasId;
	end if;

	alter table TARGETDB.castle add index tmp_index_parentcasId(parentCasId);
	insert into TARGETDB.cleanbranchcastle select casId from TARGETDB.castle where parentCasId in (select casId from TARGETDB.cleanaccount);
	alter table TARGETDB.castle drop index tmp_index_parentcasId;


	delete from TARGETDB.arenarank where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.bufftip where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.cashlog where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.castlearmy where casId in (select casId from TARGETDB.cleanaccount);
	delete from TARGETDB.castlebattleunit where casId in (select casId from TARGETDB.cleanaccount);
	delete from TARGETDB.castlebuilder where userId in (select userId from TARGETDB.cleanaccount);




	/*主城建筑*/
	delete from TARGETDB.castlebuilding_0 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=0);
	delete from TARGETDB.castlebuilding_1 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=1);
	delete from TARGETDB.castlebuilding_2 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=2);
	delete from TARGETDB.castlebuilding_3 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=3);
	delete from TARGETDB.castlebuilding_4 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=4);
	delete from TARGETDB.castlebuilding_5 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=5);
	delete from TARGETDB.castlebuilding_6 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=6);
	delete from TARGETDB.castlebuilding_7 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=7);
	delete from TARGETDB.castlebuilding_8 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=8);
	delete from TARGETDB.castlebuilding_9 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=9);

	/*分城建筑*/
	delete from TARGETDB.castlebuilding_0 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=0);
	delete from TARGETDB.castlebuilding_1 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=1);
	delete from TARGETDB.castlebuilding_2 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=2);
	delete from TARGETDB.castlebuilding_3 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=3);
	delete from TARGETDB.castlebuilding_4 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=4);
	delete from TARGETDB.castlebuilding_5 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=5);
	delete from TARGETDB.castlebuilding_6 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=6);
	delete from TARGETDB.castlebuilding_7 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=7);
	delete from TARGETDB.castlebuilding_8 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=8);
	delete from TARGETDB.castlebuilding_9 where casId in (select casId from TARGETDB.cleanbranchcastle where MOD(casId,10)=9);


	delete from TARGETDB.castleeffect_0 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=0);
	delete from TARGETDB.castleeffect_1 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=1);
	delete from TARGETDB.castleeffect_2 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=2);
	delete from TARGETDB.castleeffect_3 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=3);
	delete from TARGETDB.castleeffect_4 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=4);
	delete from TARGETDB.castleeffect_5 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=5);
	delete from TARGETDB.castleeffect_6 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=6);
	delete from TARGETDB.castleeffect_7 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=7);
	delete from TARGETDB.castleeffect_8 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=8);
	delete from TARGETDB.castleeffect_9 where casId in (select casId from TARGETDB.cleanaccount where MOD(casId,10)=9);

	delete from TARGETDB.castlefavorites where casId in (select casId from TARGETDB.cleanaccount);

	delete from TARGETDB.castlepop where casId in (select casId from TARGETDB.cleanaccount);
	delete from TARGETDB.castleres where casId in (select casId from TARGETDB.cleanaccount);

	delete from TARGETDB.castletowerarmy where casId in (select casId from TARGETDB.cleanaccount);

	delete from TARGETDB.castlevisit where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.chum where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.chumachieve where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.defplan where casId in (select casId from TARGETDB.cleanaccount);
	delete from TARGETDB.donationmoney where userId in (select userId from TARGETDB.cleanaccount);



	delete from TARGETDB.firedhero_0 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0);
	delete from TARGETDB.firedhero_1 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1);
	delete from TARGETDB.firedhero_2 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2);
	delete from TARGETDB.firedhero_3 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3);
	delete from TARGETDB.firedhero_4 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4);
	delete from TARGETDB.firedhero_5 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5);
	delete from TARGETDB.firedhero_6 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6);
	delete from TARGETDB.firedhero_7 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7);
	delete from TARGETDB.firedhero_8 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8);
	delete from TARGETDB.firedhero_9 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9);


	delete from TARGETDB.friend_0 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_1 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_2 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_3 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_4 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_5 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_6 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_7 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_8 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_9 where userId in (select userId from TARGETDB.cleanaccount); 

	delete from TARGETDB.friend_0 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_1 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_2 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_3 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_4 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_5 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_6 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_7 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_8 where friendUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friend_9 where friendUserId in (select userId from TARGETDB.cleanaccount); 

	delete from TARGETDB.friendapp where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friendapp where friendUserId in (select userId from TARGETDB.cleanaccount); 

	delete from TARGETDB.friendevalution_0 where userId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.friendevalution_0 where friendUserId in (select userId from TARGETDB.cleanaccount); 

	delete from TARGETDB.giftcount where userId in (select userId from TARGETDB.cleanaccount) ;

/*
	delete from TARGETDB.guildautopermit where guildId in (select guildId from TARGETDB.guild) ;
*/




	delete from TARGETDB.guilduser where userId in (select userId from TARGETDB.cleanaccount);
	update TARGETDB.guild set userNum = (select count(*) from TARGETDB.guilduser gu where gu.guildId = TARGETDB.guild.guildId);

	delete from TARGETDB.guildtrans_0 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_1 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_2 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_3 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_4 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_5 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_6 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_7 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_8 where sellerUserId in (select userId from TARGETDB.cleanaccount); 
	delete from TARGETDB.guildtrans_9 where sellerUserId in (select userId from TARGETDB.cleanaccount); 


	delete from TARGETDB.guildcave where guildId not in (select guildId from TARGETDB.guild);


	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_0 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_1 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_2 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_3 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_4 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_5 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_6 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_7 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_8 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_0 where heroId in (select heroId from hero_9 where MOD(heroId,10)=0 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_0 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_1 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_2 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_3 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_4 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_5 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_6 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_7 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_8 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_1 where heroId in (select heroId from hero_9 where MOD(heroId,10)=1 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_0 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_1 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_2 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_3 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_4 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_5 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_6 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_7 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_8 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_2 where heroId in (select heroId from hero_9 where MOD(heroId,10)=2 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_0 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_1 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_2 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_3 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_4 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_5 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_6 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_7 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_8 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_3 where heroId in (select heroId from hero_9 where MOD(heroId,10)=3 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_0 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_1 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_2 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_3 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_4 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_5 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_6 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_7 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_8 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_4 where heroId in (select heroId from hero_9 where MOD(heroId,10)=4 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_0 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_1 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_2 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_3 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_4 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_5 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_6 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_7 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_8 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_5 where heroId in (select heroId from hero_9 where MOD(heroId,10)=5 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));


	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_0 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_1 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_2 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_3 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_4 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_5 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_6 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_7 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_8 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_6 where heroId in (select heroId from hero_9 where MOD(heroId,10)=6 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_0 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_1 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_2 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_3 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_4 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_5 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_6 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_7 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_8 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_7 where heroId in (select heroId from hero_9 where MOD(heroId,10)=7 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_0 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_1 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_2 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_3 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_4 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_5 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_6 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_7 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_8 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_8 where heroId in (select heroId from hero_9 where MOD(heroId,10)=8 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_0 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_1 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_2 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_3 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_4 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_5 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_6 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_7 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_8 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8));
	delete from TARGETDB.heroeffect_9 where heroId in (select heroId from hero_9 where MOD(heroId,10)=9 and userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9));

	delete from TARGETDB.hero_0 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0);
	delete from TARGETDB.hero_1 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1);
	delete from TARGETDB.hero_2 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2);
	delete from TARGETDB.hero_3 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3);
	delete from TARGETDB.hero_4 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4);
	delete from TARGETDB.hero_5 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5);
	delete from TARGETDB.hero_6 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6);
	delete from TARGETDB.hero_7 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7);
	delete from TARGETDB.hero_8 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8);
	delete from TARGETDB.hero_9 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9);

	delete from TARGETDB.hospital where casId in (select casId from TARGETDB.cleanaccount);



	delete from TARGETDB.message_0 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0) ; 
	delete from TARGETDB.message_1 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1) ; 
	delete from TARGETDB.message_2 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2) ; 
	delete from TARGETDB.message_3 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3) ; 
	delete from TARGETDB.message_4 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4) ; 
	delete from TARGETDB.message_5 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5) ; 
	delete from TARGETDB.message_6 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6) ; 
	delete from TARGETDB.message_7 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7) ; 
	delete from TARGETDB.message_8 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8) ; 
	delete from TARGETDB.message_9 where receiveUserId=0 or sendUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9) or receiveUserId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9) ; 
	

	delete from TARGETDB.rankdata where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.toweruser where userId in (select userId from TARGETDB.cleanaccount);


	delete from TARGETDB.treasury_0 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0) ; 
	delete from TARGETDB.treasury_1 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1) ; 
	delete from TARGETDB.treasury_2 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2) ; 
	delete from TARGETDB.treasury_3 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3) ; 
	delete from TARGETDB.treasury_4 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4) ; 
	delete from TARGETDB.treasury_5 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5) ; 
	delete from TARGETDB.treasury_6 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6) ; 
	delete from TARGETDB.treasury_7 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7) ; 
	delete from TARGETDB.treasury_8 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8) ; 
	delete from TARGETDB.treasury_9 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9) ; 


	delete from  TARGETDB.troop where userId in (select userId from TARGETDB.cleanaccount);


	delete from TARGETDB.userachieve where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.useraltar where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.useraltarcard_0 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0) ; 
	delete from TARGETDB.useraltarcard_1 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1) ; 
	delete from TARGETDB.useraltarcard_2 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2) ; 
	delete from TARGETDB.useraltarcard_3 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3) ; 
	delete from TARGETDB.useraltarcard_4 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4) ; 
	delete from TARGETDB.useraltarcard_5 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5) ; 
	delete from TARGETDB.useraltarcard_6 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6) ; 
	delete from TARGETDB.useraltarcard_7 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7) ; 
	delete from TARGETDB.useraltarcard_8 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8) ; 
	delete from TARGETDB.useraltarcard_9 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9) ; 


	delete from TARGETDB.useranimal where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userattributes_0 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=0) ; 
	delete from TARGETDB.userattributes_1 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=1) ; 
	delete from TARGETDB.userattributes_2 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=2) ; 
	delete from TARGETDB.userattributes_3 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=3) ; 
	delete from TARGETDB.userattributes_4 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=4) ; 
	delete from TARGETDB.userattributes_5 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=5) ; 
	delete from TARGETDB.userattributes_6 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=6) ; 
	delete from TARGETDB.userattributes_7 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=7) ; 
	delete from TARGETDB.userattributes_8 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=8) ; 
	delete from TARGETDB.userattributes_9 where userId in (select userId from TARGETDB.cleanaccount where MOD(userId,10)=9) ; 


/*TODO===============以下懒得加 MOD(userId,10) 条件了*/
	delete from TARGETDB.usercard_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercard_9 where userId in (select userId from TARGETDB.cleanaccount);


	delete from TARGETDB.usercardinfo where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usercashconsumegift where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usercontinueloginaward_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usercontinueloginaward_9 where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usercount where userId in (select userId from TARGETDB.cleanaccount);


	delete from TARGETDB.userdailyactivity where userId in (select userId from TARGETDB.cleanaccount);	

	delete from TARGETDB.userdailymissionnum where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userduty where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usereffect where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userenemy_0 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_1 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_2 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_3 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_4 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_5 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_6 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_7 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_8 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userenemy_9 where userId in (select userId from TARGETDB.cleanaccount) or enemyUserId in (select userId from TARGETDB.cleanaccount);

	

	delete from TARGETDB.usergovpotz where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userimpression where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userliveskill where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usermission_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermission_9 where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usernobody where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usernpcanimus where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usernpcattacked where userId=0 or  userId in (select userId from TARGETDB.cleanaccount);

	

	delete from TARGETDB.userpet where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userpubattr where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userqqgift_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userqqgift_9 where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userquestion where userId in (select userId from TARGETDB.cleanaccount);



	delete from TARGETDB.usershared  where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usershrines  where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usersign where userId in (select userId from TARGETDB.cleanaccount);

	
	delete from TARGETDB.userstove  where userId in (select userId from TARGETDB.cleanaccount);
	update TARGETDB.userstove set producerId=0,status=0,endDttm=null,needSceond=0 where producerId in  (select userId from TARGETDB.cleanaccount);


	delete from TARGETDB.usertask  where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usertech_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usertech_9 where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usertuanshop where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.uservip where userId in (select userId from TARGETDB.cleanaccount);

	
	delete from TARGETDB.userqqprivilegeopenaward where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userrechargelog where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userconsumelog where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userawardactivity where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.useropenegg where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userconsumegift where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.marri_user_room  where userId in (select userId from TARGETDB.cleanaccount);	
	delete from TARGETDB.marriage_seek  where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.marriage_status  where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermarriage  where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.usermarriage  where targetId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userseaworld  where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userrechargeaward where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userfund where userId in (select userId from TARGETDB.cleanaccount);

	
	delete from TARGETDB.usercombatsoul where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userconquer where userId in (select userId from TARGETDB.cleanaccount);



	delete from TARGETDB.usertreasure where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userspringfestival where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userfestival where userId in (select userId from TARGETDB.cleanaccount);
	


	delete from TARGETDB.userriskdata where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userriskscene_9 where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userherocard where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userherosoul where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userdailyshopitem where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userguide where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.friendhp where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.platformfriendhp where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usermysticshop where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userexp where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userrecharge  where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userfish where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.towerranksfirstdata where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.towerranksmindata where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userillustrations where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userarmyadviser where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.useroperateactivity where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userofficial where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userpvp where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userhangerdata where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhanger_9 where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userhangerpaper_0 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_1 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_2 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_3 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_4 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_5 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_6 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_7 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_8 where userId in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.userhangerpaper_9 where userId in (select userId from TARGETDB.cleanaccount);



	delete from TARGETDB.castle where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.user where userId  in (select userId from TARGETDB.cleanaccount);
	delete from TARGETDB.account where accId not in (select accId from TARGETDB.user);

	delete from TARGETDB.usericon where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.userofficialshop where userId in (select userId from TARGETDB.cleanaccount);

	delete from TARGETDB.usercdkey where userId in (select userId from TARGETDB.cleanaccount);




	end if;
    /*
    删除只有帐号没有角色的account
    */
    delete from TARGETDB.account where accId not in (select accId from TARGETDB.user) ;
    update TARGETDB.account set areaId = (select serverId from TARGETDB.serverinfo) where areaId is null or areaId='';



/*删除大于７天的邮件*/
	DELETE FROM TARGETDB.message_0 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_1 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_2 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_3 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_4 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_5 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_6 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_7 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_8 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;
	DELETE FROM TARGETDB.message_9 where status0=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 7;

/*删除大于30天带附件的邮件*/
	DELETE FROM TARGETDB.message_0 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_1 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_2 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_3 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_4 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_5 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_6 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_7 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_8 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;
	DELETE FROM TARGETDB.message_9 where status0 !=0 and (TO_DAYS(NOW()) - TO_DAYS(sendDttm)) > 30;

	DROP TABLE IF EXISTS TARGETDB.cleanbranchcastle;
	DROP TABLE IF EXISTS TARGETDB.cleanaccount;
END    
//
DELIMITER ;
call TARGETDB.cleanDb;