import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61001Define;
import com.youxigu.dynasty2.treasury.proto.TreasuryMsg.Request61005Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class TreasuryActionTest extends ClientServerTestCase {

	public void loadTreasuryViewByUserId() {
		Request61001Define.Builder req = Request61001Define.newBuilder();
		req.setCmd(61001);
		request(req.build());
	}

	public void useItem(long id) {
		Request61005Define.Builder req = Request61005Define.newBuilder();
		req.setCmd(61005);
		req.setId(id);
		req.setItemCount(1);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		TreasuryActionTest test = new TreasuryActionTest();
		String accId = "120";
		TreasuryActionTest client = (TreasuryActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// client.loadTreasuryViewByUserId();
			// client.useItem(9601);
			// client.useItem(9602);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
