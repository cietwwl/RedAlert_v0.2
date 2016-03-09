import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

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

public class SimpleSwingSocketClient extends JFrame implements ActionListener {

	public AtomicInteger TEST_COUNT = new AtomicInteger();
	public AtomicInteger TEST_SENDCOUNT = new AtomicInteger();
	public long timeBegin = 0;
	public static SimpleSwingSocketClient mainClient;
	public static SimpleSwingSocketClient client;
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
	public static SimpleDateFormat formatter = new SimpleDateFormat( "HH:mm:ss.SSS");
	
	
	JTextArea text_ip = new JTextArea(1,10);
	JTextArea text_port = new JTextArea(1,10);
	JTextArea text_acc = new JTextArea(1,10);//游戏账号
	JTextArea text_pro = new JTextArea(1,10);//协议编号
	JTextArea text3 = new JTextArea(20,50); //输出窗口
	String[] ss = {"数字", "字符串", "list", "map"};
	
	List<Map<String, JComponent>> paraList = new ArrayList<Map<String, JComponent>>();
	
	LineBorder blackBorder = new LineBorder(new Color(0x7A8A99));
	JPanel panel = new JPanel();
	int index = 8;//参数组建的位置
	
	static Map<String, String> xieyi = new HashMap<String, String>();
	
	public SimpleSwingSocketClient() {
		super("七雄Socket协议测试");
		super.setSize(600,550);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton button = new JButton("提交");
		JButton button3 = new JButton("登陆");
		JButton button4 = new JButton("添加参数");
		text_acc.setEditable(true);
		text_acc.setLineWrap(true);
		text_acc.setText("账号");
		text_acc.setBorder(blackBorder);
		text_ip.setEditable(true);
		text_ip.setLineWrap(true);
		text_ip.setText("192.168.0.33");
		text_ip.setBorder(blackBorder);
		text_port.setEditable(true);
		text_port.setLineWrap(true);
		text_port.setText("8739");
		text_port.setBorder(blackBorder);
		text_pro.setEditable(true);
		text_pro.setLineWrap(true);
		text_pro.setText("协议编号");
		text_pro.setToolTipText("协议编号");
		text_pro.setBorder(blackBorder);
		text3.setEditable(false);
		text3.setLineWrap(true);
//		text3.setToolTipText("adfasdf");
		panel.add(text_ip);
		panel.add(text_port);
		panel.add(text_acc);
		panel.add(button3);
		panel.add(text_pro);
		this.addParaComponents(5);
		panel.add(button);
		panel.add(button4);
		panel.add(new JScrollPane(text3));
		panel.setBackground(new Color(227, 237, 205));
		button.addActionListener(this);//添加鼠标监听
		button3.addActionListener(this);//添加鼠标监听
		button4.addActionListener(this);//添加鼠标监听
		this.setContentPane(panel);
		this.setResizable(false);
	}
	private void addParaComponents(int index){
		JComboBox box = new JComboBox(ss);
		box.setToolTipText("list格式为value1,value2,...，map格式为key=value;key=value;...");
		JTextArea text_para1 = new JTextArea(1,15);
		JTextArea text_para2 = new JTextArea(1,30);
		text_para1.setEditable(true);
		text_para1.setLineWrap(true);
		text_para1.setText("参数名");
		text_para1.setToolTipText("参数名");
		text_para1.setBorder(blackBorder);
		text_para2.setEditable(true);
		text_para2.setLineWrap(true);
		text_para2.setText("参数值");
		text_para2.setToolTipText("参数值");
		text_para2.setBorder(blackBorder);
		panel.add(text_para2, index);
		panel.add(text_para1, index);
		panel.add(box, index);
		Map<String, JComponent> map = new HashMap<String, JComponent>();
		map.put("type", box);
		map.put("key", text_para1);
		map.put("value", text_para2);
		paraList.add(map);
	}
	
	private void addParaDynamic(){
		addParaComponents(index);
		panel.updateUI();
		super.setSize(600,this.getHeight() + 30);
		index += 3;
	}
	public static void main(String[] args) {
		client = new SimpleSwingSocketClient();
//		startClient(client, "192.168.0.33", 8739, "fhx", "admin", "123456");
		client.setVisible(true);
		client.setResizable(false);
	}
	
	void testEquipLvUp(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61001);
		params.put("sid", this.sid);
		params.put("tId", 134);
		params.put("num", 10);
		this.request(params);
	}
	void testequipDestroy(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 61003);
		params.put("sid", this.sid);
		params.put("tIds", "134,157");
		this.request(params);
	}

	public static SimpleSwingSocketClient startClient(final SimpleSwingSocketClient gameClient, final String ip, final int port,
			final String accId, final String usr, final String pwd) {

		// mutiLoginTest();

		

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
					println("message:" + message);
				} else if (message instanceof SyncWolfTask) {
					SyncWolfTask task = SyncWolfTask.class.cast(message);

					if (task.getState() == SyncWolfTask.RESPONSE) {
						ResultMgr.requestCompleted(session,
								task.getRequestId(), task.getResult());
					}
				} else {
					println("unknow message:" + message);
				}

				gameClient.TEST_COUNT.incrementAndGet();
			}
		};
		println("start...");
		gameClient.mainClient = new SimpleSwingSocketClient();
		gameClient.mainClient.usr = usr;
		gameClient.mainClient.pwd = pwd;
		gameClient.mainClient.init(ip, port, new IoHandlerAdapter() {

			public void messageSent(IoSession session, Object message)
					throws Exception {
			}

			public void sessionOpened(final IoSession session) throws Exception {
			}

			public void messageReceived(IoSession session, Object message) {
//				 println("mainserver messageReceived,"+ message);
				if (message instanceof Map) {
					Map map = (Map) message;
					int cmd = (Integer) map.get("cmd");
					AMF3WolfService.SyncStat synstat = (AMF3WolfService.SyncStat) map
							.remove(AMF3WolfService.SYNC_KEY);
					if (synstat != null
							&& synstat.getStat() == AMF3WolfService.SyncStat.SYNC_STATUS_RESPONSE) {
						ResultMgr.requestCompleted(session, synstat.getId(),
								message);
					}
					println("message:" + message);
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
			println("login...");
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

			gameClient.login();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return gameClient;
	}

	public Map<String, Object> testLogin(String accId) {
		println(this.ip);
		println(this.port);
		session.write("tgw_l7_forward\r\nHost: " + this.ip + ":" + this.port
				+ "\r\n\r\n");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", 10001);// IAMF3Action.CMD_LOGIN
		params.put("aid", accId);
		params.put("openkey", String.valueOf(System.currentTimeMillis()));		
		params.put("debug_time", System.currentTimeMillis());
		return this.request(params);
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

		if (session.isConnected()) {
			try {

				TaskFuture<Object> future = ResultMgr.requestSent(this.session,
						synstat.getId());
				println("发送请求：" + params.get("cmd"));
				this.session.write(params);
				TEST_SENDCOUNT.incrementAndGet();

				Object o = null;
				try {
					o = future.get(100000, TimeUnit.MILLISECONDS);
					println("请求返回：" + params.get("cmd"));
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
				System.out
						.println("time=" + (System.currentTimeMillis() - now));
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
			println("连接已经断开");
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
			println("同步请求时间过长：" + serviceName + "." + methodName
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
		params.put("usr", SimpleSwingSocketClient.this.usr);
		params.put("pwd", SimpleSwingSocketClient.this.pwd);

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
		println("login time=" + (System.currentTimeMillis() - time));
		return o;

	}

	public void close() {
		if (timeBegin > 0) {
			int sendMsg = TEST_SENDCOUNT.get();
			int reciveMsg = TEST_COUNT.get();
			long time = System.currentTimeMillis() - timeBegin;
			println("用时：" + time / 1000 + "秒,发送请求数：" + sendMsg
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

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			println(e.getActionCommand());
			if(e.getActionCommand().equals("登陆")){
				initAll();
//				testFastRequest();
			}else if(e.getActionCommand().equals("提交")){
				requestSer();
			}else if(e.getActionCommand().equals("添加参数")){
				addParaDynamic();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			println("错误 : " + e1.getMessage());
		}
		
	}
	private void requestSer() {
		int cmd = Integer.parseInt(text_pro.getText());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cmd", cmd);
		params.put("sid", this.sid);
		for(Map<String, JComponent> paras : paraList){
			JComboBox box = (JComboBox) paras.get("type");
			JTextArea key = (JTextArea) paras.get("key");
			JTextArea value = (JTextArea) paras.get("value");
			Object oo = this.getParaValue(box, value);
			params.put(key.getText(), oo);
		}
		this.request(params);
		
	}
	private void initAll() {
		startClient(client, text_ip.getText(), Integer.parseInt(text_port.getText()), text_acc.getText(), "admin", "123456");
	}
	
	private Object getParaValue(JComboBox box, JTextArea value) {
		int type = box.getSelectedIndex();
		String valueStr = value.getText();
		if(valueStr == null || valueStr.length() == 0){
			return null;
		}
		String[] arr;
		switch (type) {
		case 0:
			return Integer.parseInt(valueStr);
		case 1:
			return valueStr;
		case 2:
			//list
			arr = valueStr.split(",");
			List list = new ArrayList();
			for(String str : arr){
				list.add(isNumeric(str) ? Integer.parseInt(str) :str);
			}
			return list;
		case 3:
			//map
			arr = valueStr.split(";");
			Map<String, Object> mapTmp = new HashMap<String, Object>();
			for(String s : arr){
				String[] tmp = s.split("=");
				mapTmp.put(tmp[0], isNumeric(tmp[1]) ? Integer.parseInt(tmp[1]) : tmp[1]);
			}
			return mapTmp;

		default:
			println("错误的参数类型");
			break;
		}
		return null;
	}

	/**
	 * 判断整数
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("-?\\d+");
		return pattern.matcher(str).matches();
	}
	public static void println(Object msg){
		client.text3.setText(client.text3.getText() + "\n" + msg);
		client.text3.setCaretPosition(client.text3.getText().length());
	}
}
