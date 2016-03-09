import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;

import com.youxigu.dynasty2.core.GMSocketClient;

/**
 * 全区发放
 * 
 * @author Administrator
 * 
 */
public class GMSocketClientAnniversaryJob {

	public static void main(String[] args) {
		String fileName = null;
		//		int percent = 30;
		if (args.length > 0) {
			fileName = args[0];
			//			percent = Integer.parseInt(args[1]);
		} else {
			fileName = "F:/ftpdir/ipList2.txt";
			//			percent = 30;			
		}
		System.out.println("读取文件:" + fileName);
		StringBuilder sb = new StringBuilder();
		// GMSocketClient client = new GMSocketClient("172.16.3.53", 8739,
		// "admin", "123456");
		BufferedReader reader = null;
		BufferedWriter bw = null;
		GMSocketClient client = null;
		int port = 8010;
		String usr = "admin";
		String pwd = "egistics6tjdr9";
		
//		int port = 8739;
//		String usr = "admin";
//		String pwd = "123456";

		try {

//			String ip = null;
//			bw = new BufferedWriter(new FileWriter(fileName + "." + System.currentTimeMillis()));
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine();

			while (line != null) {
//				if (line.startsWith("#")) {
//					bw.write(line);
//					bw.write("\n");

//					ip = reader.readLine();
//					bw.write(line);
//					bw.write("\n");
					System.out.println("正在处理:" + line);
					//					int pos = line.indexOf("nodserIP: ")+10;
					//					int pos2 = line.indexOf(" ",pos);
					//					ip = line.substring(pos,pos2);
					System.out.println("mainserverIp:" + line);
					if (client != null) {
						client.close();
					}
					client = new GMSocketClient(line, port, usr, pwd);

//					line = reader.readLine();
//					bw.write(line);
//					bw.write("\n");
					line = reader.readLine();
//				}
//				String[] lineArr = line.split("\t");
				//				String openid = lineArr[0];
				//				String areaId = lineArr[1];
				//				int cash = Integer.parseInt(lineArr[3]);
				//				String userName = lineArr[4];
				//				
				//				int paypoint = (int)(1d*cash*percent/100d);
				//				if (paypoint>0){
				try {
					client.sendTask("commonService", "updateSysPara", new Object[] { "SYS_ANNIVERSARY_DRAW", "0" });
					client.sendTask("anniversaryActivityService", "sendAnniversaryLuckydrawRankBonus", (Object[]) null);
				} catch (Exception e) {
					e.printStackTrace();
//					bw.write("异常:" + line);
//					bw.write("\n");
					System.out.println("异常:" + line);
				}
				//				}
//
//				bw.write(line);
//				bw.write("\t");
//				//				bw.write(String.valueOf(paypoint));				
//				bw.write("\n");

//				line = reader.readLine();
			}

			//			client.sendTask("migicCaveClientService", "doJobEndMigicCave",
			//					guildId, caveId);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}

			try {
				reader.close();
//				bw.flush();
//				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//			client.close();
		}
		System.out.println("==================");

	}
}
