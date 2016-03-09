import com.youxigu.dynasty2.friend.proto.FriendMsg.GroupType;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51001Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51003Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51005Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51007Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51009Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51011Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51013Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51015Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51017Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51050Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51052Define;
import com.youxigu.dynasty2.friend.proto.FriendMsg.Request51054Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;
import com.youxigu.dynasty2.util.StringUtils;

public class FriendActionTest extends ClientServerTestCase {

	/**
	 * 获取列表
	 */
	public void getFriendListByGroupId(GroupType type) {
		Request51011Define.Builder req = Request51011Define.newBuilder();
		req.setCmd(51011);
		req.setGroup(type);
		request(req.build());
	}

	/**
	 * 推荐好友列表
	 * 
	 * @param type
	 */
	public void getFriendRecommends(String name) {
		Request51001Define.Builder req = Request51001Define.newBuilder();
		req.setCmd(51001);
		if (!StringUtils.isEmpty(name)) {
			req.setUserName(name);
		}
		request(req.build());
	}

	/**
	 * 申请添加好友
	 * 
	 * @param type
	 */
	public void appFriend(long userId) {
		Request51003Define.Builder req = Request51003Define.newBuilder();
		req.setCmd(51003);
		req.setFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 获取申请列表(收到的)
	 * 
	 * @param type
	 */
	public void getReceivedApp() {
		Request51005Define.Builder req = Request51005Define.newBuilder();
		req.setCmd(51005);
		request(req.build());
	}

	/**
	 * 同意或拒绝好友申请
	 * 
	 * @param type
	 */
	public void accFriend(long userId, boolean accpet) {
		Request51007Define.Builder req = Request51007Define.newBuilder();
		req.setCmd(51007);
		req.addFriendUserId(userId);
		req.setAgree(accpet);
		request(req.build());
	}

	/**
	 * 51009删除好友
	 * 
	 * @param type
	 */
	public void delFriend(long userId) {
		Request51009Define.Builder req = Request51009Define.newBuilder();
		req.setCmd(51009);
		req.setFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 51013移出列表(最近联系人)
	 * 
	 * @param type
	 */
	public void removeFromList(long userId) {
		Request51013Define.Builder req = Request51013Define.newBuilder();
		req.setCmd(51013);
		req.setFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 51015屏蔽此人(加入黑名单)
	 * 
	 * @param type
	 */
	public void addBlack(long userId) {
		Request51015Define.Builder req = Request51015Define.newBuilder();
		req.setCmd(51015);
		req.setFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 51017移出黑名单
	 * 
	 * @param type
	 */
	public void delBlack(long userId) {
		Request51017Define.Builder req = Request51017Define.newBuilder();
		req.setCmd(51017);
		req.setFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 51050赠送好友体力
	 * 
	 * @param type
	 */
	public void doSendFriendAll(long userId) {
		Request51050Define.Builder req = Request51050Define.newBuilder();
		req.setCmd(51050);
		req.addFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 51052领取体力(单个好友)
	 * 
	 * @param type
	 */
	public void doReceiveHp(long userId) {
		Request51052Define.Builder req = Request51052Define.newBuilder();
		req.setCmd(51052);
		req.setFriendUserId(userId);
		request(req.build());
	}

	/**
	 * 51054一键领取体力并且反赠
	 * 
	 * @param type
	 */
	public void doReceiveHpAll() {
		Request51054Define.Builder req = Request51054Define.newBuilder();
		req.setCmd(51054);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		FriendActionTest test = new FriendActionTest();
		String accId = "178";
		// String accId = "9999";
		// String accId = "320";
		// String accId = "315";
		FriendActionTest client = (FriendActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// client.getFriendListByGroupId(GroupType.FRIEND);
			// client.getFriendListByGroupId(GroupType.FRIEND_BLACK);

			// ((FriendActionTest) client.mainClient).getChanelConfig();
			client.getFriendListByGroupId(GroupType.FRIEND);
			// client.getFriendRecommends("");
			// client.getFriendRecommends("131");
			// client.appFriend(7901);
			// client.getReceivedApp();
			// client.accFriend(4601, true);
			// client.accFriend(4601, false);
			// client.delFriend(3701);
			// client.removeFromList(7901);
			// client.addBlack(7901);
			// client.delBlack(7901);
			// client.doSendFriendAll(7901);
			// client.doReceiveHp(4601);
			// client.doReceiveHpAll();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
