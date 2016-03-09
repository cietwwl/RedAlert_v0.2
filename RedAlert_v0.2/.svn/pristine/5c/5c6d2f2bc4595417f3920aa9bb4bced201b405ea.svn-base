import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.youxigu.dynasty2.core.GMSocketClient;
import com.youxigu.wolf.net.AsyncWolfTask;

public class LogClientTest {

	public static void main(String[] args) {
		final GMSocketClient client = new GMSocketClient("10.190.164.216", 8790,
				"admin", "123456");

		final Map<String, Object> columns = new HashMap<String, Object>();

		columns.put("areaId", 4);
		columns.put("dttm", new Date());
		columns.put("num", "3311");
		for (int j = 0; j < 4; j++) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < 400000; i++) {
						Map<String, Object> logdata = new HashMap<String, Object>();
						logdata.put("a", 1);
						logdata.put("t", "tlog_online");
						logdata.put("d", columns);
						AsyncWolfTask task = new AsyncWolfTask();
						task.setServiceName("logService");
						task.setMethodName("log");
						task.setParams(new Object[] { logdata });

						try {
							client.asyncSendTask(task);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}).start();

		}
		System.out.println("=====");
	}
}
