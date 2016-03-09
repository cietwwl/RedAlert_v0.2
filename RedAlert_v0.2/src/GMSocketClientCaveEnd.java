import com.youxigu.dynasty2.core.GMSocketClient;

public class GMSocketClientCaveEnd {

	public static void main(String[] args) {
		// 封测2用户专属礼包
		// s7.app100656690.qqopenapp.com 8010 admin egistics6tjdr9
		// e:/sftp/gifts/fengce2-openId.txt e:/sftp/gifts/fengce2.vm

		String ip = args[0];// s7.app100656690.qqopenapp.com
		int port = Integer.parseInt(args[1]);// 8010
		String usr = args[2];// admin
		String pwd = args[3];

		long guildId = Long.parseLong(args[4]);
		int caveId = Integer.parseInt(args[5]);

		StringBuilder sb = new StringBuilder();
		// GMSocketClient client = new GMSocketClient("172.16.3.53", 8739,
		// "admin", "123456");

		GMSocketClient client = new GMSocketClient(ip, port, usr, pwd);

		try {

			client.sendTask("migicCaveClientService", "doJobEndMigicCave",
					guildId, caveId);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			client.close();
		}
		System.out.println("==================");

	}
}
