import com.youxigu.dynasty2.chat.ChatChannelManager;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10009Define;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10017Define;
import com.youxigu.dynasty2.chat.proto.ChatMsg.Request10019Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class ChatActionTest extends ClientServerTestCase {

	public void receive(long userId) {
		Request10009Define.Builder bdr = Request10009Define.newBuilder();
		bdr.setCmd(10009);
		bdr.setToUserId(userId);
		bdr.setChannel(ChatChannelManager.CHANNEL_PRIVATE);
		bdr.setChannelId("");
		bdr.setContext("hello test555");
		request(bdr.build());
	}

	public void setChatStrangeMsg() {
		Request10019Define.Builder req = Request10019Define.newBuilder();
		req.setCmd(10019);
		req.setStatu(true);
		request(req.build());

	}

	/**
	 * 读取那个用户发过来的离线消息
	 * 
	 * @param userId
	 */
	public void readOfflineMsg(long userId) {
		Request10017Define.Builder req = Request10017Define.newBuilder();
		req.setCmd(10017);
		req.setUserId(userId);
		request(req.build());

	}

	public static void main(String[] args) throws Exception {
		ChatActionTest test = new ChatActionTest();
		// String accId = "178";
		String accId = "320";
		ChatActionTest client = (ChatActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// ((ChatActionTest) client.mainClient).getChanelConfig();
			// ((ChatActionTest) client.mainClient).receive(7901);
			// ((ChatActionTest) client.mainClient).setChatStrangeMsg();
			((ChatActionTest) client.mainClient).readOfflineMsg(4601);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
