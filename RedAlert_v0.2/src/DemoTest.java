import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.youxigu.dynasty2.user.domain.User;

public class DemoTest {

	public static void main(String[] args) {

		try {
			String[] tmp = "economic-7310,qqq,".split(",");
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					AddrUtil.getAddresses("172.16.3.70:11211"));

			MemcachedClient mc = builder.build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		
		
		// // 测试副本
		new Thread(new Runnable() {

			@Override
			public void run() {
				autoTest(2101);
			}

		}).start();
		//		
		// new Thread(new Runnable() {
		//			
		// @Override
		// public void run() {
		// autoTest(123456);
		// }
		//
		// }).start();
		//		
	}

	/**
	 * @param accId
	 */
	public static void autoTest(long accId) {
		DemoSocketClient client = DemoSocketClient.startClient("172.16.0.62",
				8739, String.valueOf(accId), "admin", "123456");

		client.testgetFormations();

		try {
			User user = (User) client.sendTask("userService", "getUserByaccId",
					Long.parseLong(client.accId));
			// 付费开启
			client.sendTask("userFlowerService", "doOpenUserFlower", user
					.getUserId(), 10002l, 11700006);
			// 种花
			// client.sendTask("userFlowerService","doSeedFlower",user.getUserId(),
			// 1091,10635001);
			// 浇水
			// client.sendTask("userFlowerService","doWaterFlower",user.getUserId(),
			// user.getUserId(),1091);
			// 收获
			// client.sendTask("userFlowerService","doHarvestFlower",user.getUserId(),
			// 1091);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
