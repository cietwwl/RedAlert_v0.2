import java.lang.reflect.Field;
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
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10009Define;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10011Define;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10013Define;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10015Define;
import com.youxigu.dynasty2.chat.proto.CommonHead;
import com.youxigu.dynasty2.chat.proto.CommonHead.ResponseHead;
import com.youxigu.dynasty2.combat.proto.CombatMsg.Request70101Define;
import com.youxigu.dynasty2.core.flex.amf.AMF3WolfService;
import com.youxigu.dynasty2.core.flex.amf.IAMF3Action;
import com.youxigu.dynasty2.develop.proto.DevelopMsg;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20027Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20029Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20041Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31021Define;
import com.youxigu.dynasty2.mail.proto.MailMsg.Request11001Define;
import com.youxigu.dynasty2.mail.proto.MailMsg.Request11003Define;
import com.youxigu.dynasty2.mail.proto.MailMsg.Request11005Define;
import com.youxigu.dynasty2.mail.proto.MailMsg.Request11007Define;
import com.youxigu.dynasty2.mail.proto.MailMsg.Request11009Define;
import com.youxigu.dynasty2.mission.domain.Mission;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80001Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80003Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80005Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80007Define;
import com.youxigu.dynasty2.mission.proto.MissionMsg.Request80009Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61001Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61003Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61005Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61007Define;
import com.youxigu.dynasty2.user.proto.UserMsg;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10003Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10005Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10034Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10036Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10038Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10002Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;
import com.youxigu.wolf.net.KeepAliveMessageFactoryProtoImpl;
import com.youxigu.wolf.net.NamingThreadFactory;
import com.youxigu.wolf.net.RequestTimeoutCloseProtoHandler;
import com.youxigu.wolf.net.ResultMgr;
import com.youxigu.wolf.net.ResultMgr.TaskFuture;
import com.youxigu.wolf.net.SyncWolfTask;
import com.youxigu.wolf.net.codec.NewMutilCodecFactory;
import com.youxigu.wolf.node.core.NodeRegTask;

public class TestTool4Action {

	public AtomicInteger TEST_COUNT = new AtomicInteger();
	public AtomicInteger TEST_SENDCOUNT = new AtomicInteger();
	public long timeBegin = 0;
	public TestTool4Action mainClient;
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
	public static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");

	public void init(String ip, int port, IoHandlerAdapter handler) throws NoSuchMethodException {
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
		//		Map<Integer, Class<? extends Message>> messages = new HashMap<Integer, Class<? extends Message>>();
		//		messages.put(10009, ChatActionReceive.class);
		//		fc.setMessages(null);
		chain.addLast("codec", new ProtocolCodecFilter(new NewMutilCodecFactory())); // 设置编码过滤器
		// chain.addLast("codec", new ProtocolCodecFilter(new
		// AMF3CompressCodecFactory())); // 设置编码过滤器

		executor = new UnorderedThreadPoolExecutor(4, 8, 300, TimeUnit.SECONDS, new NamingThreadFactory(
				"demoFlexClient"));
		// ThreadPoolExecutor executorWrite = new UnorderedThreadPoolExecutor(4,
		// 8, 300, TimeUnit.SECONDS,
		// new NamingThreadFactory("testwrite"));
		executor.prestartAllCoreThreads();
		// executorWrite.prestartAllCoreThreads();
		chain.addLast("exec", new ExecutorFilter(executor, IoEventType.EXCEPTION_CAUGHT, IoEventType.MESSAGE_RECEIVED,
				IoEventType.SESSION_CLOSED, IoEventType.SESSION_IDLE, IoEventType.SESSION_OPENED));
		//

		// chain.addLast("execWrite", new ExecutorFilter(executorWrite,
		// IoEventType.WRITE,IoEventType.MESSAGE_SENT));
		chain.addLast("logger", new LoggingFilter());
		// 心跳过滤器
		KeepAliveFilter filter = new KeepAliveFilter(new KeepAliveMessageFactoryProtoImpl(), IdleStatus.READER_IDLE,
				RequestTimeoutCloseProtoHandler.CLOSE, 600, 30);
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

	public void requestChat() {
		Request10009Define.Builder br2 = Request10009Define.newBuilder();
		br2.setCmd(10009);
		br2.setChannel("country");
		br2.setContext("hello world 哇哈哈");
		request(br2.build());
	}

	public Message requestgetUser(String sid) {
		Request10003Define.Builder br2 = Request10003Define.newBuilder();
		br2.setCmd(10003);
		br2.setSid(sid);

		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
		CommonHead.SyncStat.Builder synstatBr = CommonHead.SyncStat.newBuilder();
		synstatBr.setId(synstat.getId());
		synstatBr.setStat(synstat.getStat());
		br2.setSyncstat(synstatBr.build());

		return (Message) request(br2.build(), synstat);
	}

	public Message requestCreateUser() {
		Request10005Define.Builder br2 = Request10005Define.newBuilder();
		br2.setCmd(10005);
		br2.setUserName("test21");
		br2.setCountryId(1);
		// br2.setSysHeroId(100000001);
		//br2.setSex(1);
		// br2.setIcon("qin_male");
		br2.setCharId(2);
		
		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
		CommonHead.SyncStat.Builder synstatBr = CommonHead.SyncStat.newBuilder();
		synstatBr.setId(synstat.getId());
		synstatBr.setStat(synstat.getStat());
		br2.setSyncstat(synstatBr.build());

		return (Message) request(br2.build(), synstat);
	}

	public Message requestCreateUser(String userName) {
		Request10005Define.Builder br2 = Request10005Define.newBuilder();
		br2.setCmd(10005);
		br2.setUserName(userName);
		br2.setCountryId(1);
		// br2.setSysHeroId(100000001);
		//br2.setSex(1);
		// br2.setIcon("qin_male");
		br2.setCharId(2);
		
		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
		CommonHead.SyncStat.Builder synstatBr = CommonHead.SyncStat.newBuilder();
		synstatBr.setId(synstat.getId());
		synstatBr.setStat(synstat.getStat());
		br2.setSyncstat(synstatBr.build());

		return (Message) request(br2.build(), synstat);
	}

	public Message requestGetCombat() {
		Request70101Define.Builder br2 = Request70101Define.newBuilder();
		br2.setCmd(70101);
		br2.setCombatId("C_e9715c97-0e68-43e0-a7d8-01690f120688");
		br2.setFromDB(1);

		return (Message) request(br2.build());
	}

	public Message loadTreasuryViewByUserId() {
		Request61001Define.Builder br2 = Request61001Define.newBuilder();
		br2.setCmd(61001);
		return (Message) request(br2.build());
	}

	public Message delItem() {
		Request61003Define.Builder br2 = Request61003Define.newBuilder();
		br2.setCmd(61003);
		br2.setId(2804L);
		br2.setItemCount(5);
		return (Message) request(br2.build());
	}

	public Message useItem() {
		Request61005Define.Builder br2 = Request61005Define.newBuilder();
		br2.setCmd(61005);
		br2.setId(3001L);
		br2.setItemCount(2);
		return (Message) request(br2.build());
	}

	public Message getTreasury() {
		Request61007Define.Builder br2 = Request61007Define.newBuilder();
		br2.setCmd(61007);
		br2.setUName("test");
		br2.setTId(2416L);
		return (Message) request(br2.build());
	}

	public Message freshCastleData() {
		Request20027Define.Builder br2 = Request20027Define.newBuilder();
		br2.setCmd(20027);
		return (Message) request(br2.build());
	}

	public Message gainRes() {
		Request20029Define.Builder br2 = Request20029Define.newBuilder();
		br2.setCmd(20029);
		br2.setType(1);
		return (Message) request(br2.build());
	}

	public Message loadHeroViewByUserId() {
		Request31021Define.Builder br2 = Request31021Define.newBuilder();
		br2.setCmd(31021);
		return (Message) request(br2.build());
	}

	public Message sendMail() {
		Request11001Define.Builder br2 = Request11001Define.newBuilder();
		br2.setCmd(11001);
		br2.setTitle("测试标题");
		br2.setContent("测试内容");

		br2.addUserNames("test");
		br2.addUserNames("test1");
		return (Message) request(br2.build());
	}

	public Message getUserMails() {
		Request11003Define.Builder br2 = Request11003Define.newBuilder();
		br2.setCmd(11003);
		//		br2.setType(0);
		br2.setStatus(-1);
		br2.setPageNo(0);
		br2.setPageSize(10);
		return (Message) request(br2.build());
	}

	public Message setMailRead() {
		Request11005Define.Builder br2 = Request11005Define.newBuilder();
		br2.setCmd(11005);
		br2.setMsgId(1);
		return (Message) request(br2.build());
	}

	public Message getMailAppendix() {
		Request11007Define.Builder br2 = Request11007Define.newBuilder();
		br2.setCmd(11007);
		br2.setMsgId(1);
		return (Message) request(br2.build());
	}

	public Message deleteMessages() {
		Request11009Define.Builder br2 = Request11009Define.newBuilder();
		br2.setCmd(11009);
		br2.addMsgIds(1);
		return (Message) request(br2.build());
	}

    public Message requestGetCastleBuildings(){
        DevelopMsg.Request20001Define.Builder br2 = DevelopMsg.Request20001Define.newBuilder();
        br2.setCmd(20001);

        return (Message) request(br2.build());
    }

    public Message requestUpgradeCastleBuilding(long casId, long buildingId, int builderIndex){
        DevelopMsg.Request20003Define.Builder bdr = DevelopMsg.Request20003Define.newBuilder();
        bdr.setCmd(20003);
        bdr.setCasId(casId);
        bdr.setBuildingId(buildingId);
        bdr.setBuilder(builderIndex);

        return (Message) request((bdr.build()));
    }

    public Message requestCancelCastleBuilding(long casId, long buildingId){
        DevelopMsg.Request20005Define.Builder bdr = DevelopMsg.Request20005Define.newBuilder();
        bdr.setCmd(20005);
        bdr.setCasId(casId);
        bdr.setBuildingId(buildingId);

        return (Message) request((bdr.build()));
    }

    public Message requestAutoUpgradeCastleBuilding(){
        DevelopMsg.Request20015Define.Builder bdr = DevelopMsg.Request20015Define.newBuilder();
        bdr.setCmd(20015);
        bdr.addBuildingIds(125);
        bdr.addBuildingIds(126);

        return (Message) request((bdr.build()));
    }

    public Message requestCancelAutoUpgradeCastleBuilding(){
        DevelopMsg.Request20015Define.Builder bdr = DevelopMsg.Request20015Define.newBuilder();
        bdr.setCmd(20015);

        return (Message) request((bdr.build()));
    }

    public Message requestSpeedupCastleBuildingCash(long casId, long buildingId){
        DevelopMsg.Request20011Define.Builder bdr = DevelopMsg.Request20011Define.newBuilder();
        bdr.setCmd(20011);
        bdr.setCasId(casId);
        bdr.setBuildingId(buildingId);
        bdr.setSpeedUpType(DevelopMsg.Request20011Define.SpeedUpType.CASH);
//        bdr.setEntId(1020111);
//        bdr.setNum(2);

        return (Message) request((bdr.build()));
    }

    public Message requestSpeedupCastleBuildingItem(long casId, long buildingId, int num){
        DevelopMsg.Request20011Define.Builder bdr = DevelopMsg.Request20011Define.newBuilder();
        bdr.setCmd(20011);
        bdr.setCasId(casId);
        bdr.setBuildingId(buildingId);
        bdr.setSpeedUpType(DevelopMsg.Request20011Define.SpeedUpType.ITEM);
        bdr.setEntId(1020111);//5分钟道具
        bdr.setNum(num);

        return (Message) request((bdr.build()));
    }

    public Message requestSpeedupCastleBuildingItem2(long casId, long buildingId, int num){
        DevelopMsg.Request20011Define.Builder bdr = DevelopMsg.Request20011Define.newBuilder();
        bdr.setCmd(20011);
        bdr.setCasId(casId);
        bdr.setBuildingId(buildingId);
        bdr.setSpeedUpType(DevelopMsg.Request20011Define.SpeedUpType.ITEM);
        bdr.setEntId(1020112);//1小时道具
        bdr.setNum(num);

        return (Message) request((bdr.build()));
    }

    public Message requestSpeedupCastleBuildingFree(long casId, long buildingId){
        DevelopMsg.Request20011Define.Builder bdr = DevelopMsg.Request20011Define.newBuilder();
        bdr.setCmd(20011);
        bdr.setCasId(casId);
        bdr.setBuildingId(buildingId);
        bdr.setSpeedUpType(DevelopMsg.Request20011Define.SpeedUpType.FREE);
//        bdr.setEntId(1020111);
//        bdr.setNum(2);

        return (Message) request((bdr.build()));
    }

    public Message requestGetBuffTips(long casId){
        DevelopMsg.Request20031Define.Builder bdr = DevelopMsg.Request20031Define.newBuilder();
        bdr.setCmd(20031);
        bdr.setCastleId(casId);

        Message msg = (Message) request((bdr.build()));
        System.out.print(msg);
        return msg;
    }

    public Message requestActivateBuff(long casId, int buffId, int useCash){
        DevelopMsg.Request20033Define.Builder bdr = DevelopMsg.Request20033Define.newBuilder();
        bdr.setCmd(20033);
        bdr.setCastleId(casId);
        bdr.setBuffId(buffId);
        bdr.setUseCash(useCash);

//		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
//		CommonHead.SyncStat.Builder synstatBr = CommonHead.SyncStat.newBuilder();
//		synstatBr.setId(synstat.getId());
//		synstatBr.setStat(synstat.getStat());
//		bdr.setSyncstat(synstatBr.build());

		return (Message) request(bdr.build());
    }

    public Message requestCancelBuff(long casId, int buffId){
        DevelopMsg.Request20035Define.Builder bdr = DevelopMsg.Request20035Define.newBuilder();
        bdr.setCmd(20035);
        bdr.setCastleId(casId);
        bdr.setBuffId(buffId);

        return (Message) request((bdr.build()));
    }
    
    public Message receive(){
		Request10009Define.Builder bdr = Request10009Define.newBuilder();
        bdr.setCmd(10009);
        bdr.setToUserId(1L);
        bdr.setChannel(ChatChannelManager.CHANNEL_PRIVATE);
        bdr.setChannelId("");
        bdr.setContext("hello test555");
        return (Message) request((bdr.build()));
    }
    
    public Message getChanelConfig(){
    	Request10015Define.Builder bdr = Request10015Define.newBuilder();
        bdr.setCmd(10015);
        return (Message) request((bdr.build()));
    }
    
    public Message setChanelConfig(){
    	Request10011Define.Builder bdr = Request10011Define.newBuilder();
        bdr.setCmd(10011);
        bdr.setChannelConfig("1,1,0,0");
        return (Message) request((bdr.build()));
    }
    
    public Message getHisRecord(){
    	Request10013Define.Builder bdr = Request10013Define.newBuilder();
        bdr.setCmd(10013);
        return (Message) request((bdr.build()));
    }


	public Message commitWorldMission(int missionId) {
		Request80007Define.Builder bdr = Request80007Define.newBuilder();
		bdr.setCmd(80007);
		bdr.setMissionId(missionId);
		return (Message) request((bdr.build()));
	}

	public Message showMissionRedPoint() {
		Request80009Define.Builder bdr = Request80009Define.newBuilder();
		bdr.setCmd(80009);
		return (Message) request((bdr.build()));
	}

	public Message commitUserMission(int userMissionId) {
		Request80003Define.Builder bdr = Request80003Define.newBuilder();
		bdr.setCmd(80003);
		bdr.setId(userMissionId);
		return (Message) request((bdr.build()));
	}

	public Message showUserMissionViewList() {
		Request80001Define.Builder bdr = Request80001Define.newBuilder();
		bdr.setCmd(80001);
		bdr.setMissionType(Mission.MISSION_TYPE_MAIN);
		return (Message) request((bdr.build()));
	}

	public Message showWorldMissionViewList() {
		Request80005Define.Builder bdr = Request80005Define.newBuilder();
		bdr.setCmd(80005);
		bdr.setPageNo(0);
		return (Message) request((bdr.build()));
	}

	public Message getTechnology() {
		Request20041Define.Builder bdr = Request20041Define.newBuilder();
		bdr.setCmd(20041);
		return (Message) request((bdr.build()));
	}

	public Message getUserAchieveByType(int type) {
		Request10034Define.Builder bdr = Request10034Define.newBuilder();
		bdr.setCmd(10034);
		bdr.setType(type);
		return (Message) request((bdr.build()));
	}

	public Message showTitleInfo(int titleId) {
		Request10038Define.Builder bdr = Request10038Define.newBuilder();
		bdr.setCmd(10038);
		bdr.setTitleId(titleId);
		return (Message) request((bdr.build()));
	}

	public Message clickMedal(int awardId) {
		Request10036Define.Builder bdr = Request10036Define.newBuilder();
		bdr.setCmd(10036);
		bdr.setAwardId(awardId);
		return (Message) request((bdr.build()));
	}

	public static void main(String[] args) {
		//这里完成里登陆
//		final TestTool4Action client = startClient("192.168.0.49", 8739, "test", "admin", "123456");
		String accname = "test44";
		final TestTool4Action client = startClient("192.168.0.49", 8739,
				accname,
				"admin", "123456");
//		final TestTool4Action client = startClient("192.168.3.98", 8739, "test13", "admin", "123456");
		try {

			//取角色信息
			Response10003Define response10003Define = (Response10003Define) client.requestgetUser(client.sid);

			//调用mainserver的action的方式
//			client.mainClient.receive();
//			client.mainClient.getHisRecord();

			if (response10003Define.getUserId() < 0) {
				//没有角色注册新角色
				client.requestCreateUser(accname);
			}

			//调用nodeserver action的方式
//						client.requestGetCombat();
			//			client.loadTreasuryViewByUserId();
			//			client.delItem();
			//			client.getTreasury();
			//			client.useItem();
			//			client.freshCastleData();
			//			client.gainRes();
			//			client.loadHeroViewByUserId();
			//			client.sendMail();
			//			client.getUserMails();
			//			client.setMailRead();
			//			client.getMailAppendix();
			// client.getUserMails();
			// client.requestGetCombat();
			// client.commitWorldMission(1010130);
			// client.showMissionRedPoint();
			// client.showUserMissionViewList();
			// client.commitUserMission(6);
//			client.showWorldMissionViewList();
			// client.getTechnology();
			// client.getUserAchieveByType(1);
			// client.showTitleInfo(1);
			// client.clickMedal(1007);
			// client.loadHeroViewByUserId();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			client.mainClient.close();
//			client.close();
		}
	}

	public static TestTool4Action startClient(final String ip, final int port, final String accId, final String usr,
                                               final String pwd) {

		// mutiLoginTest();

		final TestTool4Action gameClient = new TestTool4Action();

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
					AMF3WolfService.SyncStat synstat = (AMF3WolfService.SyncStat) map.remove(AMF3WolfService.SYNC_KEY);
					if (synstat != null && synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
						ResultMgr.requestCompleted(session, synstat.getId(), message);
					}

				} else if (message instanceof SyncWolfTask) {
					SyncWolfTask task = SyncWolfTask.class.cast(message);

					if (task.getState() == SyncWolfTask.RESPONSE) {
						ResultMgr.requestCompleted(session, task.getRequestId(), task.getResult());
					}
				} else if (message instanceof Message) {
					try {
						Field synstatF_ = message.getClass().getDeclaredField("syncstat_");
						synstatF_.setAccessible(true);
						CommonHead.SyncStat synstat = (CommonHead.SyncStat) synstatF_.get(message);
						if (synstat != null && synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
							ResultMgr.requestCompleted(session, synstat.getId(), message);
						}
					} catch (Exception e) {
						//						e.printStackTrace();
					}
				} else {
					System.out.println("unknow message:" + message);
				}

				gameClient.TEST_COUNT.incrementAndGet();
			}
		};

		gameClient.mainClient = new TestTool4Action();
		gameClient.mainClient.usr = usr;
		gameClient.mainClient.pwd = pwd;
		try {
			gameClient.mainClient.init(ip, port, new IoHandlerAdapter() {

				public void messageSent(IoSession session, Object message) throws Exception {
				}

				public void sessionOpened(final IoSession session) throws Exception {
				}

				public void messageReceived(IoSession session, Object message) {
					System.out.println("mainserver messageReceived," + message);
					if (message instanceof Map) {
						Map map = (Map) message;
						int cmd = (Integer) map.get("cmd");
						AMF3WolfService.SyncStat synstat = (AMF3WolfService.SyncStat) map
								.remove(AMF3WolfService.SYNC_KEY);
						if (synstat != null && synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
							ResultMgr.requestCompleted(session, synstat.getId(), message);
							// System.out.println("Completed, accId=" + accId +
							// ", requestId=" + synstat.getId());
						}
						// System.out.println("message:" + message);
					} else if (message instanceof SyncWolfTask) {
						SyncWolfTask task = SyncWolfTask.class.cast(message);
						if (task.getState() == SyncWolfTask.RESPONSE) {
							ResultMgr.requestCompleted(session, task.getRequestId(), task.getResult());
						}
					} else if (message instanceof Message) {
						try {
							Field synstatF_ = message.getClass().getDeclaredField("syncstat_");
							synstatF_.setAccessible(true);
							CommonHead.SyncStat synstat = (CommonHead.SyncStat) synstatF_.get(message);
							if (synstat != null && synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
								ResultMgr.requestCompleted(session, synstat.getId(), message);
							}

						} catch (Exception e) {
							//							e.printStackTrace();
						}
					}
				}

			});
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			Object params = gameClient.mainClient.testLogin(accId);
			Response10002Define response = (Response10002Define) params;
			String sid = response.getSid();
			String aid = response.getAccId();

			String aName = response.getAId();
			String ip1 = response.getGip();
			int port1 = response.getGport();
			gameClient.sid = sid;
			gameClient.accId = aid;
			gameClient.accName = aName;
			gameClient.mainClient.accId = aid;
			gameClient.mainClient.sid = sid;
			gameClient.usr = usr;
			gameClient.pwd = pwd;
			gameClient.init(ip1, port1, gameHandler);

			System.out.println("accId=" + accId + ", sid=" + sid);
			gameClient.login();

			try {
				gameClient.mainClient.getChanelConfig();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return gameClient;
	}

	public Object testLogin(String accId) {
		System.out.println(this.ip);
		System.out.println(this.port);
		session.write("tgw_l7_forward\r\nHost: " + this.ip + ":" + this.port + "\r\n\r\n");

		//		Map<String, Object> params = new HashMap<String, Object>();
		//		params.put("cmd", 10001);// IAMF3Action.CMD_LOGIN
		//		params.put("aid", accId);
		//		params.put("vClientVersion", "1.0.20");
		//
		//		// params.put("openkey", String.valueOf(System.currentTimeMillis()));
		//		params.put("debug_time", System.currentTimeMillis());

		//		ProtoHead.Builder br = ProtoHead.newBuilder();
		//		br.setCmd(10001);
		//		br.setErrCode(0);
		//		br.setErr("test----------------------");
		//		br.setRequestCmd(9999999);

		UserMsg.Request10001Define.Builder br2 = UserMsg.Request10001Define.newBuilder();
		br2.setCmd(10001);
		br2.setAid(accId);
		br2.setVProtocalVersion("v0.1");
		//		br.setVClientVersion("1.0.20");

		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
		CommonHead.SyncStat.Builder synstatBr = CommonHead.SyncStat.newBuilder();
		synstatBr.setId(synstat.getId());
		synstatBr.setStat(synstat.getStat());
		br2.setSyncstat(synstatBr.build());

		return this.request(br2.build(), synstat);
	}

	public Object login() throws Exception {
		session.write("tgw_l7_forward\r\nHost: " + this.ip + ":" + this.port + "\r\n\r\n");
		long time = System.currentTimeMillis();
		NodeRegTask task = new NodeRegTask();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usr", usr);
		params.put("pwd", pwd);

		task.setParams(new Object[] { params });

		TaskFuture<Object> future = ResultMgr.requestSent(this.session, task.getRequestId());
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
				throw new RuntimeException(map.get(IAMF3Action.ACTION_ERROR_KEY).toString());
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
			System.out.println("用时：" + time / 1000 + "秒,发送请求数：" + sendMsg + ",平均每秒发送：" + (sendMsg * 1000 / time)
					+ ",接收请求数：" + reciveMsg + ",平均每秒接收：" + (reciveMsg * 1000 / time));
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

	public Message request(Message params, AMF3WolfService.SyncStat synstat) {
		long now = System.currentTimeMillis();
		//		params.put("debug_time", now);
		//		AMF3WolfService.SyncStat synstat = new AMF3WolfService.SyncStat();
		//		params.put(AMF3WolfService.SYNC_KEY, synstat);
		//		params.put("sid", this.sid);
		if (session.isConnected()) {
			try {
				// System.out.println("request, accId=" + accId + ", requestId="
				// + synstat.getId());
				TaskFuture<Object> future = ResultMgr.requestSent(this.session, synstat.getId());
				this.session.write(params);
				TEST_SENDCOUNT.incrementAndGet();

				Object o = null;
				try {
					o = future.get(100000, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					ResultMgr.requestCompleted(this.session, synstat.getId(), e);
					throw e;
				}

				if (o == null) {
					return null;
				}

				if (o instanceof Exception) {
					throw Exception.class.cast(o);
				}
				Message map = (Message) o;
				Field f_code = map.getClass().getDeclaredField("responseHead_");
				f_code.setAccessible(true);
				ResponseHead responseHead = (ResponseHead) f_code.get(map);
				if (responseHead.getErrCode() < 0) {
					throw new RuntimeException(responseHead.getErr());
				}

				System.out.println("one time=" + (System.currentTimeMillis() - now));
				return map;

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("连接已经断开");
		}

		// future.awaitUninterruptibly();
	}

	public Object request(Object obj) {
		if (session.isConnected()) {
			try {
				this.session.write(obj);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("连接已经断开");
		}
		return null;
	}
}
