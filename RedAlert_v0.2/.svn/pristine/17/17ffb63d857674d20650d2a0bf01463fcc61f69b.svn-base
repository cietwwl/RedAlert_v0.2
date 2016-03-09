import com.youxigu.dynasty2.user.proto.UserMsg.Request10030Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10032Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Request10042Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class UserActionTest extends ClientServerTestCase {

	public void changeSignature(String sign) {
		Request10030Define.Builder req = Request10030Define.newBuilder();
		req.setCmd(10030);
		req.setSelfSignature(sign);
		request(req.build());
	}

	public void changeUserName(String newName) {
		Request10032Define.Builder req = Request10032Define.newBuilder();
		req.setCmd(10032);
		req.setNewName(newName);
		request(req.build());
	}

	public void changeCountry(int countryId) {
		Request10042Define.Builder req = Request10042Define.newBuilder();
		req.setCmd(10042);
		req.setCountryId(countryId);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		UserActionTest test = new UserActionTest();
		String accId = "660";
		UserActionTest client = (UserActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// client.changeSignature("哈哈四的发送到发送到");
			// client.changeUserName("3200");
			// client.changeCountry(3);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
