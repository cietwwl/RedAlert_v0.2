import com.youxigu.dynasty2.activity.proto.ActivityMsg.Request11021Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Request11023Define;
import com.youxigu.dynasty2.activity.proto.ActivityMsg.Request11025Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class MysticShopActionTest extends ClientServerTestCase {

	private void getShopItemView() {
		Request11021Define.Builder req = Request11021Define.newBuilder();
		req.setCmd(11021);
		req.setShopId(1);
		request(req.build());
	}

	private void freashShopIems() {
		Request11023Define.Builder req = Request11023Define.newBuilder();
		req.setCmd(11023);
		req.setShopId(1);
		request(req.build());
	}

	private void buyShopIem(int p) {
		Request11025Define.Builder req = Request11025Define.newBuilder();
		req.setCmd(11025);
		req.setShopId(1);
		req.setPos(p);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		MysticShopActionTest test = new MysticShopActionTest();
		String accId = "171";
		MysticShopActionTest client = (MysticShopActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// client.getShopItemView();
			// client.freashShopIems();
			client.buyShopIem(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
