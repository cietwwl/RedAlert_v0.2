import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.manu.util.Util;
import com.youxigu.dynasty2.user.domain.Account;
import com.youxigu.dynasty2.user.domain.User;

public class LoginTest {

	public static void main(String[] args) {
		
		

		
		Date date = new Date(1329287376000L);
		
		System.out.println(Util.randInt(3));
		System.out.println(Util.randInt(3));
		System.out.println(Util.randInt(3));
		System.out.println(Util.randInt(3));		
		Map<String,Object> maps = new HashMap<String,Object>();
		maps.put("StateOfficialIdList_1", 000);	
		maps.put("StateOfficial_1", 111);
		
		
		Iterator lit = maps.keySet().iterator();
		while (lit.hasNext()){
			System.out.println("::::"+lit.next());
		}
		autoTest(900420435);

	}

	public static void autoTest(long accId) {
		DemoSocketClient client = DemoSocketClient.startClient("172.16.3.53",
				8739, String.valueOf(17691975),"admin","123456");

		Timestamp now = new Timestamp(System.currentTimeMillis());
		for (long i = accId; i < accId + 100000; i++) {
			try {

				Account account = new Account();
				account.setAccId(i);
				account.setAccName(String.valueOf(i));
				account.setLoginIp("127.0.0.1");
				account.setCreateDttm(now);
				account.setLastLoginDttm(now);

				client.sendTask("accountDao", "insertAccount", account);

				User user = new User();
				user.setAccId(account.getAccId());
				user.setUserName(String.valueOf(account.getAccId()));
				//user.setSex(Util.randInt(2));
				//user.setIcon("");
				user.setCountryId(Util.randInt(7) + 1);
				user.setCharId(2);

				client.sendTask("userService", "createUser", user);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		client.close();

	}
}
