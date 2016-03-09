import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.Transcoder;

import com.ibatis.sqlmap.engine.cache.memcached.SerializingTranscoderEx;

/**
 * 如果mcClient上的并发请求过多，影响mc性能有以下几个因素:<BR>
 * 1.Object的大小： 当然是越小的越好，但是在不考虑垃圾收集的影响时大于16K(16384,约等于16K)反倒会更快(默认>16K压缩)<BR>
 * 这里我们可以调整压缩参数，根据游戏中各个Object的大小，找一个合适的压缩值，例如改成10K压缩<BR>
 * 
 * 2.受mcClient 的bufferSize影响，默认是16384,如果我们的对象都比较大，>8K,把这个值改大，也能有好的效果<BR>
 * 
 * 3.受mcClient的个数影响,由于mCClient是用单线程+队列的方式来处理向服务器的请求(服务器返回信息是多线程并发处理的),因此,
 * 增大mcClient也会有好的效果,但是最大不要大于CPU核数.<BR>
 * 
 * 
 * 
 * 
 * 
 * {QA sa=/172.16.3.53:11211, #Rops=28, #Wops=19663, #iq=0,
 * topRop=net.spy.memcached.protocol.ascii.StoreOperationImpl@367c85,
 * topWop=net.spy.memcached.protocol.ascii.StoreOperationImpl@1291c98,
 * toWrite=13367, interested=5}
 * 
 * iq : inputQueue 我们的任何get set delete
 * ,都会先放到inputQueue中，inputQueue有个默认的长度16384，超过会出异常
 * 
 * Wops：WriteQueue 向服务器上的写请求队列，inputQueue中的请求会被循环不断地写到 WriteQueue
 * 中，WriteQueue没有长度限制 WriteQueue 中的数据会不断地向服务器端发送，每次发送bufferSize个字节，
 * bufferSize默认长度为16384
 * 
 * Rops:ReadQueue，收到的服务器端的响应，会放到ReadQueue中，
 * ReadQueue没有长度限制，循环处理readQueue中的所有数据，然后通知客户端请求的那个Future.
 * 
 * toWrite:当前已经写到buffer,准备向服务器段发送的字节数
 * 
 * @author Administrator
 * 
 */

public class MemCachedTest {
	private String serverlist;
	private long memTimeout = 20000L;
	private int memClientNum = Runtime.getRuntime().availableProcessors() * 2;

	private int currentIndex = 0;
	private MemcachedClient[] mcc = null;

	private ThreadLocal<MemcachedClient> threadLocalMC = new ThreadLocal<MemcachedClient>();

	public static void main(String[] args) {
		final String mcIp = args[0]; // "172.16.3.53:11211";
		final int num = Integer.parseInt(args[1]);
		// mc client 数量
		final int bufferSize = Integer.parseInt(args[2]);// 16480; // mc
															// client的buffer
															// size
		final int msgSize = Integer.parseInt(args[3]);// 1024 * 5;// 测试用的Object
														// 大小

		final int threadNum = Integer.parseInt(args[4]); // 测试用的发送请求的线程数

		// LinkedBlockingQueue<TestObject> queue = new
		// LinkedBlockingQueue<TestObject>();
		// for (int i=0;i<5000;i++){
		// queue.offer(new TestObject());
		// }
		//	
		// TestObject obj = new TestObject();
		// long now =System.currentTimeMillis();
		// for (int i=0;i<5000;i++){
		// queue.contains(obj);
		// queue.poll();
		// }
		// System.out.println("time="+(System.currentTimeMillis()-now));

		System.out.println("mc Client数量：" + num);
		System.out.println("mc Client buffer大小：" + bufferSize + " byte");
		System.out.println("测试Object大小：" + msgSize + " byte");

		final MemCachedTest cache = new MemCachedTest();
		cache.setMemClientNum(num);
		cache.setServerlist(mcIp);
		cache.init(bufferSize);

		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Future<Boolean> f = cache.getCache().set("1" ,0,"1");
		long time = 0;
		for (int i = 0; i < 20; i++) {
			time = time + cache.test(threadNum, msgSize);
		}
		System.out.println("avg time : "+(time/20d)+" ms");

		cache.close();
		System.exit(0);
	}

	public long test(final int threadNum, final int msgSize) {
		final CountDownLatch latch = new CountDownLatch(threadNum);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < threadNum; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					MemcachedClient cacheClient = MemCachedTest.this.getCache();

					// System.out.println(cacheClient.getId());

					StringBuilder sb = new StringBuilder(msgSize);
					for (int i = 0; i < msgSize; i++) {
						sb.append("1");
					}
					String data = sb.toString();
					for (int j = 0; j < 1000; j++) {
						TestObject obj = new TestObject();
						obj.setProp1(data);
						// 这里没有等每一个的返回，目的就是想向mc client的队列里塞大量数据
						Future<Boolean> f = cacheClient
								.set("test_" + j, 0, obj);
						// try {
						// f.get(10, TimeUnit.SECONDS);
						// } catch (Exception e) {
						// throw new RuntimeException(e.toString());
						// }
						// Object obj2 = cacheClient.get("test_"+j);
						// System.out.println("obj="+obj2);
					}
					cacheClient.waitForQueues(65535, TimeUnit.SECONDS);
					latch.countDown();
					// System.out.println("time="+(System.currentTimeMillis()-time));

				}
			}).start();
		}
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time = System.currentTimeMillis() - begin; 
		System.out
				.println("total time:" + time);
		
		return time;
	}

	public void setServerlist(String serverlist) {
		this.serverlist = serverlist;
	}

	public void setMemTimeout(long memTimeout) {
		this.memTimeout = memTimeout;
	}

	public void setMemClientNum(int memClientNum) {
		this.memClientNum = memClientNum;
	}

	public void init(int buffersize) {
		if (mcc != null)
			return;

		try {
			String[] serverlistName = serverlist.split(",");
			InetSocketAddress[] addresses = new InetSocketAddress[serverlistName.length];
			int i = 0;
			for (String addr : serverlistName) {
				String[] ipPort = addr.split(":");
				addresses[i] = new InetSocketAddress(ipPort[0], Integer
						.parseInt(ipPort[1]));

				i++;
			}
			ConnectionFactory factory = new DefaultConnectionFactory(16384,
					buffersize) {
				@Override
				public long getOperationTimeout() {

					return memTimeout;
				}

				@Override
				public Transcoder<Object> getDefaultTranscoder() {
					return new SerializingTranscoderEx();
				}
			};
			mcc = new MemcachedClient[memClientNum];
			for (i = 0; i < memClientNum; i++) {
				mcc[i] = new MemcachedClient(factory, Arrays.asList(addresses));
			}

			// ////这里是输出mc Client的队列信息
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// while (true) {
			// for (MemcachedClient client : mcc) {
			// NodeLocator locator = client.getNodeLocator();
			// Collection<MemcachedNode> nodes = locator.getAll();
			// for (MemcachedNode node : nodes) {
			// System.out.println(node.toString());
			// }
			// }
			// try {
			// Thread.sleep(1000);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// }
			// }
			//
			// }).start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public MemcachedClient getCache() {
		// 同一thread 使用一个MemcachedClient
		MemcachedClient c = threadLocalMC.get();
		if (c == null) {
			synchronized (MemCachedTest.class) {
				if (currentIndex >= mcc.length - 1) {
					currentIndex = 0;
				} else {
					currentIndex++;
				}
			}
			// System.out.println("currentIndex=" + currentIndex);
			c = mcc[currentIndex];
			threadLocalMC.set(c);
		}else{
			System.out.println("------------------");
		}
		return c;
	}

	// public void bindCache2Thread(long id){
	// MemcachedClient c = mcc[(int)(id%mcc.length)];
	// threadLocalMC.set(c);
	// }

	public void close() {
		for (int i = 0; i < mcc.length; i++) {
			mcc[i].shutdown();
		}

	}

	public Future<Boolean> set(String key, int exp, Object obj) {
		return this.getCache().set(key, exp, obj);
	}

	public Object get(String key) {
		return this.getCache().get(key);
	}

	public Map<String, Object> getBulk(String[] keys) {
		return this.getCache().getBulk(keys);
	}

	public Future<Boolean> deleteBulk(String[] keys) {
		MemcachedClient mc = this.getCache();
		for (int i = 0; i < keys.length; i++) {
			mc.delete(keys[i]);
		}
		return null;
	}

	public Future<Boolean> delete(String key) {

		return this.getCache().delete(key);
	}

	public long incr(String key, int by) {
		return this.getCache().incr(key, by);
	}

	public long decr(String key, int by) {
		return this.getCache().decr(key, by);
	}

	public Future<Boolean> flush() {
		for (int i = 0; i < mcc.length; i++) {
			mcc[i].flush();
		}
		return null;
	}

	public Future<Boolean> add(String key, int exp, Object obj) {
		return this.getCache().add(key, exp, obj);
	}

	public static class TestObject implements java.io.Serializable {
		private String prop1;

		public String getProp1() {
			return prop1;
		}

		public void setProp1(String prop1) {
			this.prop1 = prop1;
		}

	}

}