import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20019Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20021Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20023Define;
import com.youxigu.dynasty2.develop.proto.DevelopMsg.Request20025Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class CastleBarrackActionTest extends ClientServerTestCase {

	public void viewBuiBarrack() {
		Request20019Define.Builder req = Request20019Define.newBuilder();
		req.setCmd(20019);
		request(req.build());
	}
	

	public void buiBarrackAddArmy() {
		Request20021Define.Builder req = Request20021Define.newBuilder();
		req.setCmd(20021);
		req.setArmyNum(1);
		request(req.build());
	}

	public void exchangeItem() {
		Request20023Define.Builder req = Request20023Define.newBuilder();
		req.setCmd(20023);
		request(req.build());
	}

	public void speedUp() {
		Request20025Define.Builder req = Request20025Define.newBuilder();
		req.setCmd(20025);
		req.setDiamond(true);
		req.setEntId(0);
		req.setNum(0);
		request(req.build());
	}

	public void speedUpItem(int entId, int num) {
		Request20025Define.Builder req = Request20025Define.newBuilder();
		req.setCmd(20025);
		req.setDiamond(false);
		req.setEntId(entId);
		req.setNum(num);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		CastleBarrackActionTest test = new CastleBarrackActionTest();
		String accId = "178";
		CastleBarrackActionTest client = (CastleBarrackActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// client.viewBuiBarrack();
			// client.buiBarrackAddArmy();
			// client.exchangeItem();
			// client.speedUp(true);
			client.speedUpItem(1020131, 1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
