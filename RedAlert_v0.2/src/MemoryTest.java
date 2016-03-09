import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.youxigu.wolf.node.job.CronExpression;

public class MemoryTest {

	/**
	 * -server -XX:+PrintGCDetails -Xms40m -Xmx40m -Xmn10m -XX:PermSize=4m
	 * -XX:MaxPermSize=4m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC
	 * -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection
	 * -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		

			System.out.println(Math.ceil((double)1.1));


		try{
			Calendar adjustCal = Calendar.getInstance();
			adjustCal.set(Calendar.HOUR, 22);
			adjustCal.set(Calendar.MINUTE, 4);
			Date date = adjustCal.getTime();
			System.out.println("time="+date);
			CronExpression ce = new CronExpression("* * 2-23 ? * MON-SAT");
			long time = System.currentTimeMillis();
			boolean b = ce.isSatisfiedBy(date);
			System.out.println("time="+(System.currentTimeMillis()-time));
		}
		catch (Exception e){
			e.printStackTrace();
		}
		String[] keys = new String[0];
		for (String key : keys) {
			
		}
		System.out.println(111<<0);
		System.out.println(Math.pow(2d, 1d/2d));
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				int[] laArr = null;
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						String aa= String.valueOf(i);
						laArr = new int[1024*1024];
					}
				}

			}).start();
		}

		System.out.println("=============");
	}

	public static void main1(String[] args) {

		List cache = new ArrayList();
		// 总共36M内存(减去PERM4M)，产生36M的数据
		for (int i = 0; i < 1024 * 9; i++) {

			int[] laArr = new int[1024];

			cache.add(laArr);
		}
		System.out.println("生成数据完毕：堆内存全部填满");
		// 隔一个删除一个,使内存产生空隙
		int i = 0;
		Iterator lit = cache.iterator();
		while (lit.hasNext()) {
			Object obj = lit.next();
			if (i % 2 == 0) {
				lit.remove();
			}
			i++;
		}
		// System.gc();
		System.out.println("产生内存空隙结束,内存应该还剩16M");

		// for (int j=0;i<1024*10;i++){
		//			
		// int[] laArr= new int[1024];
		//
		// cache.add(laArr);
		// }
		//		
		System.out.println("分配，由于碎片导致无法分配，溢出了，为什么压缩不好用？");
		int[] laArr1 = new int[5 * 1024 * 1024];

		System.out.println("溢出了，");
	}
}
