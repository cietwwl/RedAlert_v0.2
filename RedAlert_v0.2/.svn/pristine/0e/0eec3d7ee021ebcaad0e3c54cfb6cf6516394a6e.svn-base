import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.youxigu.dynasty2.core.GMSocketClient;
import com.youxigu.dynasty2.core.JSONUtil;

public class GMSocketClientUSerSign {

	public static void main(String[] args) {
		// 封测2用户专属礼包
		// s7.app100656690.qqopenapp.com 8010 admin egistics6tjdr9
		// e:/sftp/gifts/fengce2-openId.txt e:/sftp/gifts/fengce2.vm

		String ip = args[0];// s7.app100656690.qqopenapp.com
		int port = Integer.parseInt(args[1]);// 8010
		String usr = args[2];// admin
		String pwd = args[3];
		boolean debug = false;
		if (args.length > 4) {
			debug = true;
		}
		StringBuilder sb = new StringBuilder();
		// GMSocketClient client = new GMSocketClient("172.16.3.53", 8739,
		// "admin", "123456");

		GMSocketClient client = new GMSocketClient(ip, port, usr, pwd);

		try {

			String jsons = (String) client
					.sendTask(
							"gmtoolService",
							"doSqlQuery",
							"select userId from usersign where (dttm>='2013-07-01' and currMthSign>3) or (dttm<'2013-07-01' and dttm>='2013-06-28')");
			Timestamp now = new Timestamp(System.currentTimeMillis()-24*3600L*1000);
			if (jsons != null) {
				List<Map<String, Object>> datas = (List<Map<String, Object>>) JSONUtil
						.toJavaBean(jsons, List.class);
				int i=1;
				System.out.println("num="+datas.size());
				for (Map<String, Object> d : datas) {
					Object obj = d.get("userId");
					long userId = Long.parseLong(obj.toString());
//					UserSign userSign = (UserSign) client.sendTask(
//							"userDailyActivityDao", "getUserSign", userId);
//					if (userSign.getCntSign() == 1) {
//						userSign.setPrevMthSign(userSign.getCurrMthSign());
//						userSign.setCurrMthSign(2);
//						userSign.setAward(0);
//						userSign.setCntSign(1);
//					} else {
//						userSign.setPrevMthSign(userSign.getCurrMthSign());
//						userSign.setCurrMthSign(0);
//						userSign.setAward(0);
//						userSign.setCntSign(0);
//						userSign.setDttm(now);
//					}
//					client.sendTask("userDailyActivityDao", "updateUserSign",
//							userSign);
					client.sendTask("memcachedManager", "delete", "userSign_"
							+ userId, 3);
					System.out.println("NO."+i+",userId:" + userId);
					i++;
					if (debug) {
						System.in.read();
					}
				}

			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(ip + ".log"));
			bw.write(sb.toString());
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			client.close();
		}
		System.out.println("==================");

	}
}
