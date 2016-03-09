import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.UnorderedThreadPoolExecutor;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.youxigu.dynasty2.core.flex.amf.AMF3WolfService;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.wolf.net.AsyncWolfTask;
import com.youxigu.wolf.net.KeepAliveMessageFactoryProtoImpl;
import com.youxigu.wolf.net.NamingThreadFactory;
import com.youxigu.wolf.net.ResultMgr;
import com.youxigu.wolf.net.ResultMgr.TaskFuture;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.codec.NewMutilCodecFactory;
import com.youxigu.wolf.node.core.NodeRegTask;

public class DemoSocketClient {

	
	public AtomicInteger TEST_COUNT = new AtomicInteger();
	public AtomicInteger TEST_SENDCOUNT = new AtomicInteger();
	public long timeBegin = 0;
	public DemoSocketClient mainClient;
	public IoSession session = null;
	public NioSocketConnector connector = null;
	public IoProcessor<NioSession> processor = null;
	public ThreadPoolExecutor executor;
	public String accId;
	public String accName;
	public String sid;
	public String usr;
	public String pwd;
	public String ip;
	public int port;
	// ResultMgr resultMgr = new ResultMgr();
	public boolean init = false;
	public static SimpleDateFormat formatter = new SimpleDateFormat(
			"HH:mm:ss.SSS");

	public static void main(String[] args) {

		final DemoSocketClient[] clients = new DemoSocketClient[1];
		for (int i = 0; i < clients.length; i++) {

			clients[i] = startClient("192.168.0.46", 8739, "74", "admin",
					"123456");
			
			//clients[i].testRiskCombat(15,1505,2);
			clients[i].testUseItem(44614701L,200);
			clients[i].testUseCdkey("A00001-024E53ivRYYxeOV1vnuP7Ps6MNWr58ymB");
			clients[i].testGetUserOfficialShop();
			clients[i].testDoOfficialShop(10);			
			clients[i].testGetHero();
			clients[i].testGuildWarEnter(395);
			clients[i].testBidding(31,1,25);
			//clients[i].testGuildWarEnter(363);
			clients[i].testGuildWarUserEff(363,1);
			
			
			
			clients[i].testShowGuildWar();
			
			clients[i].testEnterBidding();

			clients[i].testGetBiddingRanks(5,1);
			clients[i].testGuildWarUserApply(49, 1);
			clients[i].testChangeUserName("aaa13");
			clients[i].mainClient.test10011("aaa12");
			clients[i].testDoUserApply(1, -1, 0);



			clients[i].testGuildAply(1, 22, 1);

			clients[i].getWarGuildAply(1);

			
			clients[i].testInvite(16, "纵横灬迷茫");
			clients[i].testGuildApply(16, 3, 201);
			clients[i].testShowGuildApply();

			clients[i].testShowGuildVsDatas();

			clients[i].testBarHireHero(1, 1);

			clients[i].testShowUserApply(1);

			// clients[i].testDoUserArmyAdviserDestroy(30100008);
			clients[i].getWarPeriod();
			clients[i].getWarDoubleCitys();
			clients[i].testDoUserArmyAdviserEquipDestroy(39421, 1);

			// clients[i].testCreateUser("gk222", 1);
			clients[i].testGetUser();
			clients[i].testEnterTower();
			clients[i].testStartTowerCombat();
			// clients[i].testDoChangeTroopType(1,2,1502);
			clients[i].testGetHero();
			clients[i].testGetPlatformFriends();
			// testPlatformSendHp
			// testPlatformReceiveHp
			// testPlatformShareHp
			// testPlatformShareTower

			clients[i].testWarPrepare(1);

			// clients[i].testDoChangeNotice(1,"asdasdasd");
			// clients[i].testBarHireHero(3,10);
			// clients[i].testGetRiskStarAward(5,2);

			// clients[i] = startClient("192.168.0.46", 8739, "gkz",
			// "admin", "123456");

			// clients[i] = startClient("192.168.0.46", 8739, "Hlinks", "admin",
			// "123456");

			// clients[i] = startClient("182.254.179.241", 8010, "arena001",
			// "admin",
			// "123456");
			// clients[i] = startClient("s4.app100656690.qqopenapp.com", 8010,
			// "cd", "admin",
			// "egistics6tjdr9");

			// clients[i].testCreateUser("wang_"+i, 1);
			// clients[i].testRelifeHero(4901L);

			// clients[i].testCreateGuild();

			clients[i].testCollectionCmds();
			clients[i].testRiskCombat(1, 101, 0);
			clients[i].testEnterTower();
			clients[i].testStartTowerCombat();
			clients[i].testEnterCity(1);

			clients[i].testDoChangeAssist(1, 1);

			clients[i].testDoRemoveUser(1, 0);

			clients[i].getWarGuildAply(1);
			clients[i].testGuildAply(1, 20, 2);
			clients[i].testCreateUser("wang_" + i, 1);

		}
		try {
			Thread.currentThread().sleep(1000);

		} catch (Exception e) {
		}

	}

	public static DemoSocketClient startClient(final String ip, final int port,
			final String accId, final String usr, final String pwd) {

		// mutiLoginTest();

		final DemoSocketClient gameClient = new DemoSocketClient();

		final IoHandlerAdapter gameHandler = new IoHandlerAdapter() {
			public void sessionOpened(IoSession session) throws Exception {

				if (gameClient.timeBegin == 0) {
					gameClient.timeBegin = System.currentTimeMillis();
				}
			}

			public void messageReceived(IoSession session, Object message) {
				long time = 0;
				Map map = null;
				if (message instanceof Map) {
					map = (Map) message;
					int cmd = (Integer) map.get("cmd");
					AMF3WolfService.SyncStat synstat = (AMF3WolfService.SyncStat) map
							.remove(AMF3WolfService.SYNC_KEY);
					if (synstat != null
							&& synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
						ResultMgr.requestCompleted(session, synstat.getId(),
								message);
					}

				} else if (message instanceof SyncWolfTask) {
					SyncWolfTask task = SyncWolfTask.class.cast(message);

					if (task.getState() == SyncWolfTask.RESPONSE) {
						ResultMgr.requestCompleted(session,
								task.getRequestId(), task.getResult());
					}
				} else {
					System.out.println("unknow message:" + message);
				}

				gameClient.TEST_COUNT.incrementAndGet();
			}
		};

		gameClient.mainClient = new DemoSocketClient();
		gameClient.mainClient.usr = usr;
		gameClient.mainClient.pwd = pwd;
		gameClient.mainClient.init(ip, port, new IoHandlerAdapter() {

			public void messageSent(IoSession session, Object message)
					throws Exception {
			}

			public void sessionOpened(final IoSession session) throws Exception {
			}

			public void messageReceived(IoSession session, Object message) {
				// System.out.println("mainserver messageReceived,"+ message);
				if (message instanceof Map) {
					Map map = (Map) message;
					int cmd = (Integer) map.get("cmd");
					AMF3WolfService.SyncStat synstat = (AMF3WolfService.SyncStat) map
							.remove(AMF3WolfService.SYNC_KEY);
					if (synstat != null
							&& synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
						ResultMgr.requestCompleted(session, synstat.getId(),
								message);
						// System.out.println("Completed, accId=" + accId +
						// ", requestId=" + synstat.getId());
					}
					// System.out.println("message:" + message);
				} else if (message instanceof SyncWolfTask) {
					SyncWolfTask task = SyncWolfTask.class.cast(message);
					if (task.getState() == SyncWolfTask.RESPONSE) {
						ResultMgr.requestCompleted(session,
								task.getRequestId(), task.getResult());
					}
				}
			}

		});

		try {

			Map<String, Object> params = gameClient.mainClient.testLogin(accId);
			String sid = (String) params.get("sid");
			String aid = (String) params.get("accId");

			String aName = (String) params.get("aName");
			String ip1 = (String) params.get("gip");
			int port1 = (Integer) params.get("gport");
			gameClient.sid = sid;
			gameClient.accId = aid;
			gameClient.accName = aName;
			gameClient.mainClient.accId = aid;
			gameClient.mainClient.sid = sid;
			gameClient.usr = usr;
			gameClient.pwd = pwd;
			gameClient.init(ip1, port1, gameHandler);

			// System.out.println("accId=" + accId + ", sid=" + sid);
			gameClient.login();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return gameClient;
	}

	public Map<String, Object> testLogin(String accId) {
		System.out.println(this.ip);
		System.out.println(this.port);
		session.write("tgw_l7_forward\r\nHost: " + this.ip + ":" + this.port
				+ "\r\n\r\n");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10001);// IAMF3Action.CMD_LOGIN
		params.put("aid", accId);
		params.put("vClientVersion", "1.3.20");

		// params.put("openkey", String.valueOf(System.currentTimeMillis()));
		params.put("debug_time", System.currentTimeMillis());
		return this.request(params);
	}

	public void testgetFormations() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 33007);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void getWarPeriod() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21000);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void getWarDoubleCitys() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21026);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void getWarGuildAply(int areaId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21001);
		params.put("areaId", areaId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildAply(int cityId, int devote, int direction) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21002);
		params.put("cityId", cityId);
		params.put("devote", devote);
		params.put("direction", direction);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoUserApply(int cityId, int direction, int assist) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21004);
		params.put("cityId", cityId);
		params.put("direction", direction);
		params.put("assist", assist);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testWarPrepare(int cityId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21009);
		params.put("cityId", cityId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testChangeUserName(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 13050);
		params.put("newName", name);

		params.put("sid", this.sid);
		this.request(params);

	}

	public void test10011(String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10011);
		params.put("name", userName);

		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDoChangeNotice(int cityId, String notice) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21005);
		params.put("cityId", cityId);
		params.put("notice", notice);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoChangeAssist(int cityId, int assist) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21006);
		params.put("cityId", cityId);
		params.put("assist", assist);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoChangeTroopType(int cityId, int troopType, long userId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21007);
		params.put("cityId", cityId);
		params.put("troopType", troopType);
		if (userId > 0) {
			params.put("userId", userId);
		}
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoRemoveUser(int cityId, long userId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21008);
		params.put("cityId", cityId);

		if (userId > 0) {
			params.put("userId", userId);
		}

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShowUserApply(int areaId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21003);
		params.put("areaId", areaId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterCity(int cityId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21010);
		params.put("cityId", cityId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void test16025() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16025);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterBidding() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16050);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testBidding(int actId, int idx, int cash) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16051);
		params.put("actId", actId);
		params.put("idx", idx);
		params.put("cash", cash);

		params.put("sid", this.sid);
		this.request(params);
	}
	public void testGetBiddingRanks(int actId, int idx) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16052);
		params.put("actId", actId);
		params.put("idx", idx);


		params.put("sid", this.sid);
		this.request(params);
	}
	
	// /////////////古迹测试begin
	public void testShowGuildWar() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21501);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShowGuildVsDatas() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21502);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShowGuildApply() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21503);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildApply(int vsId, int idx, int devote) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21504);
		params.put("vsId", vsId);
		params.put("idx", idx);
		params.put("devote", devote);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildWarUserApply(int vsId, int idx) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21505);
		params.put("vsId", vsId);
		params.put("idx", idx);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testInvite(int vsId, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21507);
		params.put("vsId", vsId);
		params.put("name", name);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildWarEnter(int vsId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 21520);
		params.put("vsId", vsId);
		params.put("sid", this.sid);
		this.request(params);
	}
	
	public void testGuildWarUserEff(int vsId,int eff) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd",  21523);
		params.put("vsId", vsId);
		params.put("eff", eff);		
		params.put("sid", this.sid);
		this.request(params);
	}	

	// /////////////古迹测试end

	public void testCreateTroop() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 33001);
		params.put("name", "我的军团2");
		params.put("fId", 11301001);
		params.put("fpos", new long[] { 0, 47208, 0, 0, 0 });
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testUpdateTroop() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 33004);
		params.put("troopId", 1001L);
		params.put("mainHeroId", 1401L);
		params.put("formationId", 11301001);

		params.put("heroIds", new long[] { 1401, 1403, 1301 });
		params.put("type", 2);
		params.put("showMode", 1);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetTroopList() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 33002);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testbuyItem(int entId, int itemCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 60005);
		params.put("entId", entId);
		params.put("itemCount", itemCount);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDeleteTroop() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 33005);
		params.put("troopId", 104500L);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testCreateUser(String userName, int countryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10005);
		params.put("userName", userName);
		params.put("countryId", countryId);
		params.put("sex", 1);
		params.put("icon", "Head秦男");

		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetMapCell(int x0, int y0, int x1, int y1) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 20001);
		params.put("posX1", x0);
		params.put("posY1", y0);
		params.put("posX2", x1);
		params.put("posY2", y1);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testViewBuiWALL(int buiId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30024);
		params.put("casBuiId", buiId);

		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDummy() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", -1);
		params.put("userName", this.accId);
		params.put("countryId", 1);
		params.put("sex", 1);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetUser() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10003);
		params.put("userName", this.accId);
		params.put("countryId", 1);
		params.put("sex", 1);

		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetUserInfoByName(String userName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10004);
		params.put("userName", userName);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetUserInfoByUserId(long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10004);
		params.put("userId", userId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testShowTreasuryViewList() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 60001);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testMarkMailRead() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10118);
		params.put("msgId", 352);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDeleteMails() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10114);
		params.put("msgIds", new int[] { 349, 350, 351 });

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetMails() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10112);
		params.put("type", 0);
		params.put("status", 1);
		params.put("pageNo", 0);
		params.put("pageSize", 13);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSendMail() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10110);
		params.put("userId", 301);
		params.put("title", "accId=2发送的测试邮件111");
		params.put("content", "accId=2发送的测试邮件1231");
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetCombat(String combatId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70101);
		params.put("combatId", combatId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDeclareWar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70002);
		params.put("posX", 537);
		params.put("posY", 3);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDeclareWar(long casId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70002);
		params.put("casId", casId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testFindMapCellsByRange() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 20001);
		params.put("posX1", 2220);
		params.put("posY1", 1440);
		params.put("posX2", 2240);
		params.put("posY2", 1460);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetCastleInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 20005);
		params.put("posX", 2232);
		params.put("posY", 1452);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testAddTreasury(int entId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 60011);
		params.put("entId", entId);
		params.put("itemCount", num);

		params.put("sid", this.sid);
		this.request(params);
	}

	// 装备强化
	public void testTreasuryLvUp(long treasuryId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61001);
		params.put("tId", treasuryId);
		params.put("num", num);

		params.put("sid", this.sid);
		this.request(params);
	}

	// 装备分解
	public void testTreasuryDestroy(String treasuryIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61003);
		params.put("tIds", treasuryIds);

		params.put("sid", this.sid);
		this.request(params);
	}

	// 回炉
	public void testTreasuryRebirth(String treasuryIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61004);
		params.put("tIds", treasuryIds);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCreateGuild() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40001);
		params.put("guildName", "我的联盟1");
		params.put("guildFlag", "王");
		params.put("flagType", 1);
		params.put("color", 1);
		params.put("guildNotice", "测试");
		params.put("qq", "12345678");

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testAddCash() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 130400);
		params.put("cash", "100000");

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testBindQQGroup(String qqGroup, String groupOpenId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 45001);
		params.put("qqGroup", qqGroup);
		params.put("groupOpenId", groupOpenId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSyncQQGroupMessage(int sync) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 45002);
		params.put("sync", sync);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSyncQQGroupCard(String prefix) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 45003);
		params.put("prefix", prefix);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetTradeInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 34001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSetTradeInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 34003);
		params.put("casIds", "202");

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetTradeUsers() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 34002);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testStartTrade() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 34005);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSetTreasuryShortcut() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 60017);
		params.put("pos", 1);//
		params.put("entId", 10610101);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetTreasuryShortcut() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 60019);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCancelTrade() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 34006);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetGuildData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40003);
		params.put("guildId", 5702);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCreateGuildMergeInvite() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40040);
		params.put("guildId", 6201);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testAgreeGuildMergeInvite() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40043);
		params.put("id", 3);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetGuildMergeInvites() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40041);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCancelGuildMergeInvite() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40044);
		params.put("id", 1);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testRejectGuildMergeInvite() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40042);
		params.put("id", 2);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUpdateGuildNotice() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40003);
		params.put("notice", "");

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testArmyOutPvP() {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70001);
//		params.put("outType", CombatConstants.OUT_TYPE_DEFAULT);
		params.put("troopId", 1);
		params.put("posX", 537);
		params.put("posY", 3);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testArmyOutPvE(long troopId, int posX, int posY) {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70001);
//		params.put("outType", CombatConstants.OUT_TYPE_DEFAULT);
		params.put("troopId", troopId);
		params.put("posX", posX);
		params.put("posY", posY);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetLastArmyOutRecord() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70004);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHierHero() {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70001);
//		params.put("outType", CombatConstants.OUT_TYPE_DEFAULT);
		params.put("troopId", 1);
		params.put("posX", 100);
		params.put("posY", 100);
		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testUpgradeHeroArmy(long heroId, int armyEntId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31014);
		params.put("heroId", heroId);
		params.put("armyEntId", armyEntId);
		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testViewBuiBar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31019);

		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testArmyOutPve() {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70001);
//		params.put("outType", CombatConstants.OUT_TYPE_DEFAULT);
		params.put("troopId", 100000);
		params.put("posX", 100);
		params.put("posY", 100);
		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testArmyOutFight() {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70001);
//		params.put("outType", CombatConstants.OUT_TYPE_FIGHT);
		params.put("heroIds", "101");
		params.put("posX", 100);
		params.put("posY", 100);
		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testArmyOutBack() {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70003);
		params.put("outId", 13);
		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testBuyResource() {
		// user : 2389 ,10
		// castle: 1,5
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30100);
		params.put("entId", 10401002);
		params.put("cash", 49);
		params.put("sid", this.sid);
		this.request(params);
		System.out.println("=================");
	}

	public void testSendMessage() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10009);
		params.put("toUserId", 0L);// 101751L
		params.put("channel", "world");
		params.put("context", String.valueOf(this.accId));
		this.request(params);

	}

	public void testChangeHeroExp() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31005);
		params.put("heroId", 6104L);// 101751L
		params.put("exp", 100);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDoGainMoney() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 36010);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDoGainFood() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 36011);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testDoGainBronze() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 36012);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testFreshCastleData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30005);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testLoadTreasuryViewByUserId() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61022);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testUserGuide(int stageId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 13001);
		params.put("stageId", stageId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testFuncUnLocks() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 15001);

		params.put("sid", this.sid);
		this.request(params);

	}

	public void testUpgradeCastle() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30035);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testcreateBuilding(int casId, int entId, int posNo,
			int[] builders) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30002);
		params.put("casId", casId);
		params.put("entId", entId);
		params.put("pos", posNo);
		params.put("builders", builders);
		params.put("debug_time", System.currentTimeMillis());
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testUpgradeBuilding(int casId, int buiId, int[] builders) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30003);
		params.put("casId", casId);
		params.put("casBuiId", buiId);
		params.put("builders", builders);
		params.put("debug_time", System.currentTimeMillis());
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testCancelUpgradeBuilding(int casId, int buiId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30004);
		params.put("casId", casId);
		params.put("casBuiId", buiId);
		params.put("debug_time", System.currentTimeMillis());
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testFastUpgradeBuilding(int casId, int buiId, int cashType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30006);
		params.put("casId", casId);
		params.put("casBuiId", buiId);
		params.put("cashType", cashType);
		params.put("debug_time", System.currentTimeMillis());
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testListBuilding(int casId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30001);
		params.put("casId", casId);
		params.put("debug_time", System.currentTimeMillis());
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testListCastleBuilders() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30070);
		params.put("debug_time", System.currentTimeMillis());
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetUserNpcAnimus() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70006);
		params.put("reqId", 1);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetUserMissionsByType(String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 80001);
		params.put("missionType", type);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCommitMission(int missionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 80003);
		params.put("id", missionId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testMissionRead(int missionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 80004);
		params.put("id", missionId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildCastleApply(int gCasId, int honor) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 42002);
		params.put("id", gCasId);
		params.put("honor", honor);
		params.put("sid", this.sid);
		this.request(params);
	}

	/**
	 * 战斗录像
	 */
	public void testGetUserCombat() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71020);
		params.put("sid", this.sid);
		this.request(params);
	}

	/**
	 * 
	 */
	public void testDelUserCombat() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71020);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShowTower() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterTower() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71002);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSetTowerTroop(long troopId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71004);
		params.put("troopId", troopId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testFastTowerCombat() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71003);
		params.put("stageId", -1);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testStopAutoTowerCombat() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71006);
		params.put("treatHero", 1);
		params.put("supplyArmy", 1);
		params.put("stopStage", 5);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testExitTower() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71007);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testStartTowerCombat() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 71005);
		params.put("sid", this.sid);
		this.request(params);
	}

	// /建造城防
	public void testCreateTowerArmy(int entId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30025);
		params.put("entId", entId);
		params.put("num", num);

		params.put("sid", this.sid);
		this.request(params);
	}

	// /加速建造城防
	public void testFastCreateTowerArmy(int entId, int cashType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30034);
		params.put("entId", entId);
		params.put("cashType", cashType);

		params.put("sid", this.sid);
		this.request(params);
	}

	// /加速建造城防
	public void testCancelCreateTowerArmy(int entId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30033);
		params.put("entId", entId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetFriendListByGroupId(int groupId, int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51006);
		params.put("groupId", groupId);
		params.put("curPage", pageNo);
		params.put("sid", this.sid);
		this.request(params);
	}

	// 获取建筑工搜索信息-30073
	public void testGetCastleBuilderSearch() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30073);
		params.put("sid", this.sid);
		this.request(params);
	}

	// 寻访建筑工，增加好感度-30075
	public void testAddCastleBuilderSearchPoint() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30075);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testResetCastleBuilderSearch() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30074);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testBarRefresh() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31027);
		params.put("modeId", 0);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testBarHireHero(int type, int ten) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31020);
		params.put("type", type);
		params.put("ten", ten);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testRefreshUserTask(int mode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12001);
		params.put("mode", mode);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testExecUserTask(int taskId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12002);
		params.put("taskId", taskId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetUserTaskAward() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12003);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetUserDailyActivity() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12010);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetUserDailyActivityAwards(int awardId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12011);
		params.put("awardId", awardId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetCastleArmy() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30008);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testAddCastleArmy(int type, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30009);
		params.put("type", type);
		params.put("armyNum", num);

		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetHero() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31022);
//		Combat combat = new Combat();
//		CombatTeam team = new CombatTeam();
//		team.setTroopId(1111);
//		combat.setAttacker(team);
//		params.put("combat", combat);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testGetPlatformFriends() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51060);
		params.put("sid", this.sid);
		this.request(params);

	}
	public void testGetUserOfficialShop() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 19206);
		params.put("sid", this.sid);
		this.request(params);

	}
	
	public void testUseCdkey(String cdkey) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16110);
		params.put("cdkey", cdkey);		
		params.put("sid", this.sid);
		this.request(params);

	}	
	public void testDoOfficialShop(int shopId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 19207);
		params.put("shopId", shopId);		
		params.put("sid", this.sid);
		this.request(params);

	}
	

	public void testPlatformSendHp(long friUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51061);
		params.put("friUserId", friUserId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testPlatformReceiveHp(long friUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51062);
		params.put("friUserId", friUserId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testPlatformShareHp(long friUserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51063);
		params.put("friUserId", friUserId);
		params.put("sid", this.sid);
		this.request(params);

	}

	public void testPlatformShareTower(long friUserId, int stage) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51064);
		params.put("friUserId", friUserId);
		params.put("stage", stage);
		params.put("sid", this.sid);
		this.request(params);

	}

	/**
	 * 创建联盟基地
	 */
	public void testCreateGuildCastle(int posX, int posY) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 42001);
		params.put("posX", posX);
		params.put("posY", posY);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetGuildBuilding() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 42002);
		params.put("sid", this.sid);
		this.request(params);
	}

	/**
	 * 升级联盟建筑
	 */
	public void testUpgradeGuildCastleBuilding() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 42001);
		params.put("sid", this.sid);
		this.request(params);
	}

	/**
	 * 升级联盟建筑
	 */
	public void testLeagueOpen() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 92001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLeagueEnter() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 92002);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLeagueSetTroop(long troopId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 92004);
		params.put("troopId", troopId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLeagueCombat(long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 92005);
		params.put("userId", userId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterGuuidShop() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 43001);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterGuildTechnology() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 43020);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUpgradeGuildTech(int techId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 43021);
		params.put("entId", techId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroTaskEquip(long heroId, long treasuryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31010);
		params.put("heroId", heroId);
		if (treasuryId > 0) {
			params.put("treasuryId", treasuryId);
		}
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testRefreshHonorMissionStar(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 80011);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEquipRefining(long treasuryId, int type, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61030);
		params.put("tId", treasuryId);
		params.put("type", type);
		params.put("num", num);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroInherit(long heroId1, long heroId2) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31040);
		params.put("heroId1", heroId1);
		params.put("heroId2", heroId2);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSaveEquipRefining(long treasuryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61031);
		params.put("tId", treasuryId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildDonateData(int woodNum, int stoneNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40020);
		params.put("woodNum", woodNum);
		params.put("stoneNum", stoneNum);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetSigninAward(int index) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12013);
		params.put("index", index);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCancelEquipRefining(long treasuryId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61032);
		params.put("tId", treasuryId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetCanAttackedNPCS() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 70013);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetGuildTrans(int pageNo, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 41001);
		params.put("pageNo", pageNo);
		params.put("name", name);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetMyGuildTrans() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 41002);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSellItem(int itemId, int num, int money) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 41003);
		params.put("itemId", itemId);
		params.put("num", num);
		params.put("money", money);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCancelSellItem(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 41004);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGuildBuyItem(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 41005);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetGuildTransLogs(int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 41006);
		params.put("pageNo", pageNo);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testListQQGift(int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 14801);
		params.put("type", type);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testListUserLvGift() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 14800);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGainQQGift(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 14802);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHireNpcPlayerBuilder(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30077);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHireCastleBuilder(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30071);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	// 10206002

	public void testGetTransToken4FastBuilding(long casId, int casBuiId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 140000);
		Map<String, Object> subs = new HashMap<String, Object>();
		// 建筑加速
		subs.put("cmd", 30006);
		subs.put("type", 1);
		subs.put("casBuiId", casBuiId);
		subs.put("casId", casId);// 元宝
		subs.put("cashType", 1);// 元宝

		params.put("_token_data", subs);
		params.put("sid", this.sid);

		this.request(params);
	}

	public void testTransCallBack() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 140000);
		params.put("demoCallBack", 1);
		params.put("sid", this.sid);

		this.request(params);
	}

	public void testGetUserCashConsumes() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 18020);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGainUserCashConsumeGift(int id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 18021);
		params.put("id", id);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterOffical() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testOfficalFight(int countryId, int officalId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cmd", 17003);
			params.put("countryId", countryId);
			params.put("id", officalId);
			params.put("sid", this.sid);
			this.request(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetForbidChatUserList(int countryId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cmd", 17006);
			params.put("countryId", countryId);
			params.put("sid", this.sid);
			this.request(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDisableChat(long userId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cmd", 17007);
			params.put("userId", userId);
			params.put("reason", 1);
			params.put("sid", this.sid);
			this.request(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testEnableChat(long userId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cmd", 17008);
			params.put("userId", userId);
			params.put("sid", this.sid);
			this.request(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetCountryNotice(int countryId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17009);
		params.put("countryId", countryId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUpdateCountryNotice(String notice) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17010);
		params.put("notice", notice);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCountryBroadcast(String msg) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17011);
		params.put("msg", msg);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetCountryEvents(int countryId, int type, int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17013);
		params.put("countryId", countryId);
		params.put("type", type);
		params.put("pageNo", pageNo);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDropSpecialOffical() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17005);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSendFlowerEgg(int type, int countryId, int officalId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17012);
		params.put("type", type);
		params.put("countryId", countryId);
		params.put("officalId", officalId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testExitOffical() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 17002);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetOfficalAward() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cmd", 17004);
			params.put("sid", this.sid);
			this.request(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetUserPowerRank(int countryId, int pageNo,
			String userName, long userId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 91006);
		params.put("countryId", countryId);
		if (pageNo > 0) {
			params.put("pageNo", pageNo);
		}
		if (userName != null) {
			params.put("userName", userName);
		}
		if (userId > 0) {
			params.put("userId", userId);
		}
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGainUserBonus() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 18030);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLeaguerefreshEnemys() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 92007);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetRecommendGuilds(int countryId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 40075);
		params.put("countryId", countryId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetLuckyRanks() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 93001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLuckyRankFight(int rankId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 93002);
		params.put("rankId", rankId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLuckyRankFastCool() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 93003);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLuckyRankExit() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 93004);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetUserSign() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12014);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUserSign() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12015);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUserSignAward(int id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 12017);
		params.put("id", id);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetUserLvLottery() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 18040);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoUserLvLottery() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 18041);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetFriendRecommends() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51030);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoFriendRecommends(long[] userIds) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 51031);
		params.put("userIds", userIds);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetQQPrivilegeOpen(int type) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 14821);
		params.put("type", type);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoQQPrivilegeOpenAward(int type, int point) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 14822);
		params.put("type", type);
		params.put("point", point);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetUserAwardActivitys() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 14822);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEggActivityIsOpen() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEggActivityEnter() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16002);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoOpenEgg(int type, int isFree, int useItem) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16004);
		params.put("type", type);
		params.put("free", isFree);
		params.put("item", useItem);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEggActivityExit() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16003);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testConsumeActivityIsOpen() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16010);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterConsumeGift() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16011);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testEnterConsumeRank() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16012);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoConsumeAward(int cash) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16014);
		params.put("cash", cash);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testConsumeActivityExit() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16013);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetUserArmyAdviserInfos() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16801);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoArmyAdviserSearch(int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16802);
		params.put("type", type);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testSetDefaultArmyAdviser(long adviserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16803);
		params.put("adviserId", adviserId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoAdviserTrain(long adviserId, int itemId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16804);
		params.put("adviserId", adviserId);
		params.put("itemId", itemId);
		params.put("num", num);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetFriendArmyAdviserInfo(int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16805);
		params.put("pageNo", pageNo);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetGuildUserArmyAdviserInfo(int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16806);
		params.put("pageNo", pageNo);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testInviteUserArmyAdviser(long invUserId, int invType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16807);
		params.put("invUserId", invUserId);
		params.put("invType", invType);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoUserArmyAdviserDestroy(int adviserId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16808);
		params.put("adviserId", adviserId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDoUserArmyAdviserEquipDestroy(long tId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 16809);
		params.put("tId", tId);
		params.put("num", num);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testDemo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", -1);
		params.put("hello", "hello");
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetRiskInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 54001);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroStrength(long heroId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31073);
		params.put("heroId", heroId);
		params.put("num", num);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCollectionCmds() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 11000);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testRiskCombat(int pId, int sId, int dif) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 54002);
		params.put("pId", pId);
		params.put("sId", sId);
		params.put("dif", dif);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUpgradeTech(int techId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30030);
		params.put("entId", techId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testUseItem(long id, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 60009);
		params.put("id", id);
		params.put("itemCount", num);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testFastUpgradeTech(int techId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30031);
		params.put("entId", techId);

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testAutoRiskCombat(int pId, int sId, int dif) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 54003);
		params.put("pId", pId);
		params.put("sId", sId);
		params.put("dif", dif);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testClearRiskCD() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 54005);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetRiskStarAward(int pId, int idx) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 54004);
		params.put("pId", pId);
		params.put("idx", idx);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testClearJoinNum(int pId, int sId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 54006);
		params.put("pId", pId);
		params.put("sId", sId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShowBar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31027);
		params.put("sid", this.sid);
		this.request1(params);
	}

	public void testHireHero(int type, boolean ten, boolean discard) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31020);
		params.put("type", type);
		if (ten) {
			params.put("ten", ten);
		}
		if (discard) {
			params.put("discard", discard);
		}

		params.put("sid", this.sid);
		this.request(params);
	}

	public void testLevelUpHero(long heroId, int level) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31005);
		params.put("heroId", heroId);
		params.put("level", level);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testRelifeHero(long heroId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31006);
		params.put("heroId", heroId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroRebirth(long heroId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31003);
		params.put("heroId", heroId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testBuyItem(int shopItemId, int num) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 90110);
		params.put("shopId", shopItemId);
		params.put("num", num);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetDailyBuyItems() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 90111);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetHeroNextLevelProperties(long heroId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31070);
		params.put("heroId", heroId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testGetHeroNextRelifeProperties(long heroId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31071);
		params.put("heroId", heroId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroDiscard(long[] heroIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31004);
		params.put("heroIds", heroIds);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroCardDiscard(int[] cardIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31030);
		params.put("cardIds", cardIds);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testHeroSoulComposite(int soulId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 31031);
		params.put("soulId", soulId);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShowAltar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30046);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testResourceShelter(int cardId/* 锁定的卡片Id */) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30047);
		if (cardId > 0) {
			params.put("cardId", cardId);
		}
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testCashShelter(int cardId/* 锁定的卡片Id */) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30048);
		if (cardId > 0) {
			params.put("cardId", cardId);
		}
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testShelterAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 30057);
		params.put("sid", this.sid);
		this.request(params);
	}

	public void testViewArena() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 72001);
		params.put("sid", this.sid);
		this.requestAsync(params);
	}

	public void testArenaCombat(int myRankId, long toUserId, int toRankId,
			boolean isCash) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 72003);
		params.put("myRankId", myRankId);
		params.put("toUserId", toUserId);
		params.put("toRankId", toRankId);
		if (isCash) {
			params.put("isCash", isCash);
		}
		params.put("sid", this.sid);
		this.requestAsync(params);
	}

	public void testGetArenaRank(int curPage, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 72005);
		params.put("curPage", curPage);
		params.put("pageSize", pageSize);

		params.put("sid", this.sid);
		this.requestAsync(params);
	}

	public void testGetArenaAward() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 72004);
		params.put("sid", this.sid);
		this.requestAsync(params);
	}

	public DemoSocketClient() {
	}

	public void init(String ip, int port, IoHandlerAdapter handler) {
		// exector = Executors.newFixedThreadPool(4, new NamingThreadFactory(
		// "demoFlexClient"));
		this.ip = ip;
		this.port = port;
		processor = new SimpleIoProcessorPool<NioSession>(NioProcessor.class, 1);
		// connector = new NioSocketConnector((Executor) exector, processor);
		connector = new NioSocketConnector(processor);

		// connector.getSessionConfig().setTcpNoDelay(true);
		// connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();

		// chain.addLast("codec", new ProtocolCodecFilter(new
		// AMF3CodecFactory())); // 设置编码过滤器
		chain
				.addLast("codec", new ProtocolCodecFilter(
						new NewMutilCodecFactory())); // 设置编码过滤器
		// chain.addLast("codec", new ProtocolCodecFilter(new
		// AMF3CompressCodecFactory())); // 设置编码过滤器

		executor = new UnorderedThreadPoolExecutor(4, 8, 300, TimeUnit.SECONDS,
				new NamingThreadFactory("demoFlexClient"));
		// ThreadPoolExecutor executorWrite = new UnorderedThreadPoolExecutor(4,
		// 8, 300, TimeUnit.SECONDS,
		// new NamingThreadFactory("testwrite"));
		executor.prestartAllCoreThreads();
		// executorWrite.prestartAllCoreThreads();
		chain.addLast("exec", new ExecutorFilter(executor,
				IoEventType.EXCEPTION_CAUGHT, IoEventType.MESSAGE_RECEIVED,
				IoEventType.SESSION_CLOSED, IoEventType.SESSION_IDLE,
				IoEventType.SESSION_OPENED));
		//

		// chain.addLast("execWrite", new ExecutorFilter(executorWrite,
		// IoEventType.WRITE,IoEventType.MESSAGE_SENT));
		chain.addLast("logger", new LoggingFilter());
		// 心跳过滤器
		KeepAliveFilter filter = new KeepAliveFilter(
				new KeepAliveMessageFactoryProtoImpl(), IdleStatus.READER_IDLE,
				KeepAliveRequestTimeoutHandler.CLOSE, 600, 30);
		chain.addLast("ping", filter);

		connector.setHandler(handler);
		connector.getSessionConfig().setReuseAddress(true);
		connector.getSessionConfig().setTcpNoDelay(false);
		ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));// 建立连接

		cf.awaitUninterruptibly();

		if (cf.isConnected()) {
			session = cf.getSession();
			init = true;
		}
	}

	public Map<String, Object> request1(Map<String, Object> params) {
		params.put("debug_time", System.currentTimeMillis());

		if (session.isConnected()) {
			try {

				this.session.write(params);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("连接已经断开");
		}
		return null;
	}

	public Map<String, Object> request(Map<String, Object> params) {
		long now = System.currentTimeMillis();
		params.put("debug_time", now);
		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
		params.put(AMF3WolfService.SYNC_KEY, synstat);
		params.put("sid", this.sid);
		if (session.isConnected()) {
			try {
				// System.out.println("request, accId=" + accId + ", requestId="
				// + synstat.getId());
				TaskFuture<Object> future = ResultMgr.requestSent(this.session,
						synstat.getId());
				this.session.write(params);
				TEST_SENDCOUNT.incrementAndGet();

				Object o = null;
				try {
					o = future.get(100000, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					ResultMgr
							.requestCompleted(this.session, synstat.getId(), e);
					throw e;
				}

				if (o == null) {
					return null;
				}

				if (o instanceof Exception) {
					throw Exception.class.cast(o);
				}
				Map<String, Object> map = (Map<String, Object>) o;
				if (map.containsKey(IAMF3Action.ACTION_ERROR_CODE_KEY)) {
					if (((Integer) map.get(IAMF3Action.ACTION_ERROR_CODE_KEY)) < 0) {
						throw new RuntimeException(map.get(
								IAMF3Action.ACTION_ERROR_KEY).toString());
					}
				}
				System.out.println("one time="
						+ (System.currentTimeMillis() - now));
				return map;

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("连接已经断开");
		}

		// future.awaitUninterruptibly();
	}

	public void requestAsync(Map<String, Object> params) {
		params.put("debug_time", System.currentTimeMillis());
		if (session.isConnected()) {
			WriteFuture future = session.write(params);
			TEST_SENDCOUNT.incrementAndGet();
		} else {
			System.out.println("连接已经断开");
		}

		// future.awaitUninterruptibly();
	}

	public Object sendTask(String serviceName, String methodName,
			Object... params) throws Exception {
		long time = System.currentTimeMillis();
		SyncWolfTask task = new SyncWolfTask();
		task.setParams(params);
		task.setMethodName(methodName);
		task.setServiceName(serviceName);

		if (this.session == null || this.session.isClosing()
				|| !this.session.isConnected()) {
			throw new RuntimeException("连接已经断开，请重新建立连接！！");
		}
		TaskFuture<Object> future = ResultMgr.requestSent(this.session, task
				.getRequestId());
		this.session.write(task);
		TEST_SENDCOUNT.incrementAndGet();
		// if (logger.isDebugEnabled()) {
		// logger.debug("同步请求开始：{} -> {}", serviceName, methodName);
		// }
		Object o = null;
		try {
			o = future.get(200000, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			ResultMgr.requestCompleted(this.session, task.getRequestId(), e);
			throw e;
		}
		time = System.currentTimeMillis() - time;

		if (time > 500) {
			System.out.println("同步请求时间过长：" + serviceName + "." + methodName
					+ ",time=" + time);
		}

		// o = future.get();

		if (o == null) {
			return null;
		}

		if (o instanceof Exception) {
			throw Exception.class.cast(o);
		}
		return o;

	}

	public void sendTaskAsync(String serviceName, String methodName,
			Object... params) throws Exception {
		AsyncWolfTask task = new AsyncWolfTask();
		task.setParams(params);
		task.setMethodName(methodName);
		task.setServiceName(serviceName);

		this.session.write(task);

	}

	public Object login() throws Exception {
		session.write("tgw_l7_forward\r\nHost: " + this.ip + ":" + this.port
				+ "\r\n\r\n");
		long time = System.currentTimeMillis();
		NodeRegTask task = new NodeRegTask();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usr", DemoSocketClient.this.usr);
		params.put("pwd", DemoSocketClient.this.pwd);

		task.setParams(new Object[] { params });

		TaskFuture<Object> future = ResultMgr.requestSent(this.session, task
				.getRequestId());
		this.session.write(task);

		// if (logger.isDebugEnabled()) {
		// logger.debug("同步请求开始：{} -> {}", serviceName, methodName);
		// }
		Object o = null;
		try {
			o = future.get(20000, TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			ResultMgr.requestCompleted(this.session, task.getRequestId(), e);
			throw e;
		}

		if (o instanceof Exception) {
			throw Exception.class.cast(o);
		} else if (o instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) o;
			if (((Integer) map.get(IAMF3Action.ACTION_ERROR_CODE_KEY)) < 0) {
				throw new RuntimeException(map
						.get(IAMF3Action.ACTION_ERROR_KEY).toString());
			}
		}
		System.out.println("login time=" + (System.currentTimeMillis() - time));
		return o;

	}

	public void close() {
		if (timeBegin > 0) {
			int sendMsg = TEST_SENDCOUNT.get();
			int reciveMsg = TEST_COUNT.get();
			long time = System.currentTimeMillis() - timeBegin;
			System.out.println("用时：" + time / 1000 + "秒,发送请求数：" + sendMsg
					+ ",平均每秒发送：" + (sendMsg * 1000 / time) + ",接收请求数："
					+ reciveMsg + ",平均每秒接收：" + (reciveMsg * 1000 / time));
		}

		// + count
		// + ",每秒接受到的消息数量="
		// + (count * 1000 / (System
		// .currentTimeMillis() - timeBegin)));
		// }

		if (mainClient != null) {
			mainClient.close();
		}
		if (this.session != null) {
			CloseFuture future = this.session.close(false);
			try {
				future.wait();
			} catch (Exception e) {
			}
		}

		connector.dispose();

		if (this.processor != null) {
			this.processor.dispose();
		}

		if (executor != null) {
			executor.shutdown();
		}
	}
}
