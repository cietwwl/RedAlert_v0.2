import com.youxigu.dynasty2.core.GMSocketClient;
import com.youxigu.dynasty2.map.domain.action.TimeAction;
import com.youxigu.dynasty2.user.domain.User;

public class TestTool4Service {

	public static void main(String[] args) {
		// 封测2用户专属礼包
		// s7.app100656690.qqopenapp.com 8010 admin egistics6tjdr9
		// e:/sftp/gifts/fengce2-openId.txt e:/sftp/gifts/fengce2.vm

		// String ip = args[0];// s7.app100656690.qqopenapp.com
		// int port = Integer.parseInt(args[1]);// 8010
		// String usr = args[2];// admin
		// String pwd = args[3];
		//
		// long guildId = Long.parseLong(args[4]);
		// int caveId = Integer.parseInt(args[5]);
		//
		// StringBuilder sb = new StringBuilder();
		GMSocketClient client = new GMSocketClient("192.168.0.49", 8739,
				"admin", "123456");

		// GMSocketClient client = new GMSocketClient(ip, port, usr, pwd);

		try {
			long userId = 1L;
			User user = (User) client.sendTask("userService", "getUserById",
					userId);

			// 例子 treasuryService 中的List<DroppedEntity> doExchangeItem(long
			// userId, Item item, int num);
			// Item item = (Item)client.sendTask("entityService", "getEntity",
			// 1001001);
			// client.sendTask("treasuryService", "doExchangeItem",
			// user.getUserId(), item, 1);

			// CombatDB combatDB = (CombatDB)client.sendTask("combatDao",
			// "getCombatDB", "testCombatId");
			// CombatMsg.Combat tmp =
			// (CombatMsg.Combat)client.sendTask("combatService", "getCombatPf",
			// "C_c51b1f67-c7d9-4fd9-8d07-142d0e0f16d7");
			// combat.setCombatId("testCombatId");
			// byte[] datas = combat.toCombat().toByteArray();
			// byte[] datas = combatDB.getCombatData();
			// CombatMsg.Combat tmp = CombatMsg.Combat.parseFrom(datas);
			//
			// MemcachedManager.set("testCombatId", datas,
			// CacheModel.CACHE_REMOTE, 60 * 60);
			// MemcachedManager.set("testCombatId", tmp,
			// CacheModel.CACHE_REMOTE, 60 * 60);

			// datas = (byte[])MemcachedManager.get("testCombatId",
			// CacheModel.CACHE_REMOTE);
			// CombatMsg.Combat tmp = CombatMsg.Combat.parseFrom(datas);
			// CombatDB cb = new CombatDB();
			//
			// cb.setCombatId(combat.getCombatId());
			// cb.setCombatDt(new Timestamp(System.currentTimeMillis()));
			// cb.setCombatData(datas);
			// client.sendTask("combatDao", "saveCombatDB", cb);
			client.sendTask("strategyCommander", "doExcute", new TimeAction());
			// mapCell.setCasId(1);
			// client.sendTask("mapService", "updateMapCell", mapCell);

			// StateCache stateCache = (StateCache)
			// client.sendTask("mapService",
			// "getStateCacheByStateId", 1);

			System.out.println("==================");
			// System.out.println(tmp.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			client.close();
		}
		System.out.println("==================");

	}
}
