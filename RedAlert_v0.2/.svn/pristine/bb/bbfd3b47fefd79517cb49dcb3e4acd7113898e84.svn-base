
/**
 * 测试 jdk1.6.0u24版本在64位操作系统下使用-XX:+UseCompressedOops的crash现象<br>
 * 
 * UseCompressedOops是64位指针压缩开关,只有在64bit版本才有有，-server参数下，-mx在4G-32G是 默认是打开的（我测试的是在2G以上都是打开的）。
 * 
 * 
 * 测试环境：vm_info: Java HotSpot(TM) 64-Bit Server VM (19.1-b02) for linux-amd64 JRE (1.6.0_24-b07),<br>
 * -server (UseCompressedOops默认开始)，执行10多次基本就会出一次crash<br>
 * 模拟7雄的参数 -server -verbosegc -Xms100m -Xmx100m -Xmn20m -XX:PermSize=40m -XX:MaxPermSize=40m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection,<br>
 * 执行10多次基本就会出一次crash<br>
 * 
 * 强制加上-XX:-UseCompressedOops，关闭指针压缩，不出问题,但是会增加内存占用25%-50%
 * 
 * 另外测试了：
 * jdk1.6.0u20 (64bit)没问题
 * jdk1.6.0u25 (64bit)没问题
 * 32bit环境下各个版本都没问题  
 * 
 * @author Administrator
 *
 */
///java -server -verbosegc -Xms100m -Xmx100m -Xmn20m -XX:PermSize=40m -XX:MaxPermSize=40m -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection -cp .: CrashTest
public class CrashTest {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				int[] laArr = null;
				@Override
				public void run() {
					//for (int i = 0; i < 1000; i++) {
						//String aa= String.valueOf(i);
						laArr = new int[1024*1024];//这是为了占内存，使GC工作,4M*10=40M，占用40M
					//}
						for (int i = 0; i < 100000; i++) {
							Object tmp =  new CrashTest();//这是为了填充内存，使GC工作
							//sun 的 bug 7002666
							Object[] a = testArray(CrashTest.class);
							if (a[0] != null) {
								//居然有值，并且这个值还无法输出，纳闷
								//System.err.println("====================error:"+a[0]);
								System.err.println("====================error:");
								//System.exit(0);
							}
						}
						System.out.println("end");
						
				}

			}).start();
		}		
	}

	public static Object[] testArray(Class c) {
		Object[] a = (Object[]) java.lang.reflect.Array.newInstance(c, 1);
		return a;
	}
}
