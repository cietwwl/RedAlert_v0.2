
import com.youxigu.dynasty2.core.GMSocketClient;

public class ReloadLeagueRank {
	public static void main(String[] args) {
		// GMSocketClient client = new GMSocketClient("172.16.3.53", 8739,
		// "admin", "123456");

		 GMSocketClient client = new GMSocketClient(
		 "s7.app100656690.qqopenapp.com", 8001, "admin",
		 "");
//		GMSocketClient client = new GMSocketClient("172.16.3.53", 8740,
//				"admin", "123456");

		try {
			client.sendTask("leagueService", "reloadRank");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		System.out.println("===============");

	}
}
