

	select count(*) as 'bufftip' from bufftip where userId not in (select userId from user);
	select count(*) as 'castle' from castle where userId not in (select userId from user);
	select count(*) as 'castlearmy' from castlearmy where casId not in (select casId from castle);
	select count(*) as 'castlebattleunit' from castlebattleunit where casId not in (select casId from castle);

	select count(*) as 'castlebuilding' from castlebuilding_0  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_1  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_2  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_3  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_4  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_5  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_6  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_7  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_8  where casId not in (select casId from castle)
	union
	select count(*) as 'castlebuilding' from castlebuilding_9  where casId not in (select casId from castle);

	select count(*) as 'castlebuilder.1' from castlebuilder where userId not in (select userId from user);
	select count(*) as 'castlebuilder.2' from castlebuilder where userId not in (select userId from user);
	/*
	select count(*) as 'castlebuilder.3' from castlebuilder where buiId!=0 and buiId not in (select casBuiId from castlebuilding_0 union select casBuiId from castlebuilding_1 union select casBuiId from castlebuilding_2 union select casBuiId from castlebuilding_3 union select casBuiId from castlebuilding_4 union select casBuiId from castlebuilding_5 union select casBuiId from castlebuilding_6 union select casBuiId from castlebuilding_7 union select casBuiId from castlebuilding_8 union select casBuiId from castlebuilding_9);
	*/

	select count(*) as 'castleeffect' from castleeffect_0 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_1 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_2 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_3 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_4 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_5 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_6 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_7 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_8 where casId not in (select casId from castle)
	union
	select count(*) as 'castleeffect' from castleeffect_9 where casId not in (select casId from castle);
	
	
	select count(*) as 'castlepop' from castlepop  where casId not in (select casId from castle);

	select count(*) as 'castleres' from castleres  where casId not in (select casId from castle);

	

	select count(*) as 'castlevisit' from castlevisit  where userId not in (select userId from user);

	

	select count(*) as 'defplan.1' from defplan  where  casId not in (select casId from castle);


	select count(*) as 'defplan.2' from defplan  where troopId!=0 and  troopId not in (select troopId from troop);

	select count(*) as 'donationmoney' from donationmoney   where userId not in (select userId from user);

	select count(*) as  'userduty' from userduty where userId not in (select userId from user);


	
	select count(*) as  'firedhero' from firedhero_0 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_1 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_2 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_3 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_4 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_5 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_6 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_7 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_8 where userId not in (select userId from user)
	union
	select count(*) as  'firedhero' from firedhero_9 where userId not in (select userId from user);



	select count(*) as  'firedhero.2' from firedhero_0 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_1 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_2 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_3 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_4 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_5 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_6 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_7 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_8 where casId not in (select casId from castle)
	union
	select count(*) as  'firedhero.2' from firedhero_9 where casId not in (select casId from castle);


	select count(*) as  'firedhero.3' from firedhero_0 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_1 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_2 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_3 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_4 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_5 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_6 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_7 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_8 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0
	union
	select count(*) as  'firedhero.3' from firedhero_9 where equ1!=0 and equ2!=0 and equ3!=0 and equ4!=0 and equ5!=0 and equ6!=0;


	select count(*) as  'friend.1' from friend_0 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_1 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_2 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_3 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_4 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_5 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_6 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_7 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_8 where userId not in (select userId from user)
	union
	select count(*) as  'friend.1' from friend_9 where userId not in (select userId from user);

	
	select count(*) as  'friend.2' from friend_0 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_1 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_2 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_3 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_4 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_5 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_6 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_7 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_8 where friendUserId not in (select userId from user)
	union
	select count(*) as  'friend.2' from friend_9 where friendUserId not in (select userId from user);


	select count(*) as 'giftcount' from giftcount  where userId not in (select userId from user);

	select count(*) as 'guild' from guild where leaderId not in  (select userId from user);

	select count(*) as 'guildbuilding' from guildbuilding_0 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_1 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_2 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_3 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_4 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_5 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_6 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_7 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_8 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildbuilding' from guildbuilding_9 where guildId not in (select guildId from guild);

	
	select count(*) as 'guildcave' from guildcave  where guildId not in (select guildId from guild);
	

	
	select count(*) as 'guildeffect' from guildeffect_0 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_1 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_2 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_3 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_4 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_5 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_6 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_7 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_8 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildeffect' from guildeffect_9 where guildId not in (select guildId from guild);



	select count(*) as 'guildtechnology' from guildtechnology_0 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_1 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_2 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_3 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_4 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_5 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_6 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_7 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_8 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtechnology' from guildtechnology_9 where guildId not in (select guildId from guild);


	select count(*) as 'guildtrans' from guildtrans_0 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_1 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_2 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_3 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_4 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_5 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_6 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_7 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_8 where guildId not in (select guildId from guild)
	union
	select count(*) as 'guildtrans' from guildtrans_9 where guildId not in (select guildId from guild);



	select count(*) as 'guildtrans.2' from guildtrans_0 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_1 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_2 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_3 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_4 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_5 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_6 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_7 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_8 where sellerUserId not in (select userId from user)
	union
	select count(*) as 'guildtrans.2' from guildtrans_9 where sellerUserId not in (select userId from user);




	select count(*) as 'guilduser.1' from guilduser where guildId not in (select guildId from guild);
	select count(*) as 'guilduser.2' from guilduser where userId  not in (select userId from user);





	select count(*) as  'hero' from hero_0 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_1 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_2 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_3 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_4 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_5 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_6 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_7 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_8 where userId not in (select userId from user)
	union
	select count(*) as  'hero' from hero_9 where userId not in (select userId from user);



	select count(*) as  'hero.2' from hero_0 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_1 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_2 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_3 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_4 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_5 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_6 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_7 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_8 where casId not in (select casId from castle)
	union
	select count(*) as  'hero.2' from hero_9 where casId not in (select casId from castle);


	/*TODO:hero的equip的判断*/
	/*TODO:heroeffect的判断*/
	
	select count(*) as 'message' from message_0 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_1 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_2 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_3 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_4 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_5 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_6 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_7 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_8 where sendUserId!=0 and sendUserId not in (select userId from user)
	union
	select count(*) as 'message' from message_9 where sendUserId!=0 and sendUserId not in (select userId from user);



	select count(*) as 'message.2' from message_0 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_1 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_2 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_3 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_4 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_5 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_6 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_7 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_8 where receiveUserId not in (select userId from user)
	union
	select count(*) as 'message.2' from message_9 where receiveUserId not in (select userId from user);




	select count(*) as 'rankdata.1' from rankdata where userId  not in (select userId from user);
	select count(*) as 'rankdata.2' from rankdata where guildId!=0 and guildId  not in (select guildId from guild);


	select count(*) as 'toweruser.1' from toweruser where userId  not in (select userId from user);
	select count(*) as 'toweruser.2' from toweruser where troopId!=0 and troopId  not in (select troopId from troop);

	select count(*) as 'treasury.1' from treasury_0 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_1 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_2 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_3 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_4 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_5 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_6 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_7 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_8 where userId  not in (select userId from user)
	union
	select count(*) as 'treasury.1' from treasury_9 where userId  not in (select userId from user);

	/*TODO:检查treasury中的equip*/


	select count(*) as 'troop' from troop where userId  not in (select userId from user);
	/*TODO:检查troop中的heroId*/

	select count(*) as 'user.1' from user where accId  not in (select accId from account);
	select count(*) as 'user.2' from user where mainCastleId  not in (select casId from castle);
	select count(*) as 'user.3' from user where guildId!=0 and  guildId  not in (select guildId from guild);


	select count(*) as 'userachieve' from userachieve where  userId  not in (select userId from user);









	select count(*) as 'useraltar' from useraltar where  userId  not in (select userId from user);


	select count(*) as 'useraltarcard' from useraltarcard_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'useraltarcard' from useraltarcard_9 where  userId  not in (select userId from user);




	select count(*) as 'useranimal' from useranimal where  userId  not in (select userId from user);


	select count(*) as 'userattributes' from userattributes_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'userattributes' from userattributes_9 where  userId  not in (select userId from user);


	select count(*) as 'usercard' from usercard_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercard' from usercard_9 where  userId  not in (select userId from user);



	select count(*) as 'usercardinfo' from usercardinfo where  userId  not in (select userId from user);


	select count(*) as 'usercashconsumegift' from usercashconsumegift where  userId  not in (select userId from user);



	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'usercontinueloginaward' from usercontinueloginaward_9 where  userId  not in (select userId from user);



	select count(*) as 'usercount' from usercount where  userId  not in (select userId from user);

	select count(*) as 'userdailyactivity' from userdailyactivity where  userId  not in (select userId from user);

	select count(*) as 'userdailymissionnum' from userdailymissionnum where  userId  not in (select userId from user);

	select count(*) as 'usereffect' from usereffect where  userId  not in (select userId from user);



	select count(*) as 'userenemy' from userenemy_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'userenemy' from userenemy_9 where  userId  not in (select userId from user);


	select count(*) as 'userenemy.2' from userenemy_0 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_1 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_2 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_3 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_4 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_5 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_6 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_7 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_8 where  enemyUserId  not in (select userId from user)
	union
	select count(*) as 'userenemy.2' from userenemy_9 where  enemyUserId  not in (select userId from user);


	select count(*) as 'userenemy.3' from userenemy_0 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_1 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_2 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_3 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_4 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_5 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_6 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_7 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_8 where  enemyCasId  not in (select casId from castle)
	union
	select count(*) as 'userenemy.3' from userenemy_9 where  enemyCasId  not in (select casId from castle);




	select count(*) as 'usergovpotz' from usergovpotz where  userId  not in (select userId from user);

	select count(*) as 'userliveskill' from userliveskill where  userId  not in (select userId from user);
	

	
	select count(*) as 'usermission' from usermission_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'usermission' from usermission_9 where  userId  not in (select userId from user);


	select count(*) as 'usernobody' from usernobody where  userId  not in (select userId from user);

	select count(*) as 'usernpcattacked' from usernpcattacked where  userId  not in (select userId from user);

	
	select count(*) as 'userpubattr' from userpubattr where userId  not in (select userId from user);



	select count(*) as 'userqqgift' from userqqgift_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'userqqgift' from userqqgift_9 where  userId  not in (select userId from user);


	select count(*) as 'userquestion' from userquestion where userId  not in (select userId from user);

	select count(*) as 'userrecharge' from userrecharge where userId  not in (select userId from user);

	
	select count(*) as 'usershared' from usershared where userId  not in (select userId from user);

	select count(*) as 'usersign' from usersign where userId  not in (select userId from user);
	select count(*) as 'userstove' from userstove where userId  not in (select userId from user);
	select count(*) as 'usertask' from usertask where userId  not in (select userId from user);



	select count(*) as 'usertech' from usertech_0 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_1 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_2 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_3 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_4 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_5 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_6 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_7 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_8 where  userId  not in (select userId from user)
	union
	select count(*) as 'usertech' from usertech_9 where  userId  not in (select userId from user);


	select count(*) as 'usertuanshop' from usertuanshop where userId  not in (select userId from user);

	select count(*) as 'uservip' from uservip where userId  not in (select userId from user);

	select count(*) as 'userriskdata' from userriskdata where userId  not in (select userId from user);

	select count(*) as 'userriskscene_0' from userriskscene_0 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_1' from userriskscene_1 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_2' from userriskscene_2 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_3' from userriskscene_3 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_4' from userriskscene_4 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_5' from userriskscene_5 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_6' from userriskscene_6 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_7' from userriskscene_7 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_8' from userriskscene_8 where userId  not in (select userId from user);
	select count(*) as 'userriskscene_9' from userriskscene_9 where userId  not in (select userId from user);

	select count(*) as 'userherocard' from userherocard where userId  not in (select userId from user);
	select count(*) as 'userherosoul' from userherosoul where userId  not in (select userId from user);
	
	



