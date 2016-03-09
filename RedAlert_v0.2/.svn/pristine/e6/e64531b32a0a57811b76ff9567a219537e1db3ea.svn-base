import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54001Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54003Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54005Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54007Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54009Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54011Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54013Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.Request54015Define;
import com.youxigu.dynasty2.risk.proto.RiskMsg.StarType;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

/**
 * 战役测试类
 * 
 * @author fengfeng
 *
 */
public class RiskActionTest extends ClientServerTestCase {

	public void getQuickIds(int id) {
		Request54015Define.Builder req = Request54015Define.newBuilder();
		req.setCmd(54015);
		req.setRiskId(id);
		request(req.build());
	}

	public void getUserRiskScenes(
			com.youxigu.dynasty2.risk.proto.RiskMsg.RiskType type) {
		Request54013Define.Builder req = Request54013Define.newBuilder();
		req.setCmd(54013);
		req.setType(type);
		request(req.build());
	}

	public void getOneRiskScene(int id) {
		Request54001Define.Builder req = Request54001Define.newBuilder();
		req.setCmd(54001);
		req.setId(id);
		request(req.build());
	}

	public void doCombat(int id) {
		Request54003Define.Builder req = Request54003Define.newBuilder();
		req.setCmd(54003);
		req.setId(id);
		request(req.build());
	}

	public void doAutoCombat(List<Integer> ids, int num) {
		Request54005Define.Builder req = Request54005Define.newBuilder();
		req.setCmd(54005);
		for (int id : ids) {
			req.addId(id);
		}
		req.setNum(num);
		request(req.build());
	}

	public void gainStarAward(int id, StarType type) {
		Request54007Define.Builder req = Request54007Define.newBuilder();
		req.setCmd(54007);
		req.setId(id);
		req.setStarType(type);
		request(req.build());
	}

	public void firstBonus(int id) {
		Request54009Define.Builder req = Request54009Define.newBuilder();
		req.setCmd(54009);
		req.setId(id);
		request(req.build());
	}

	public void clearJoinNum(int id) {
		Request54011Define.Builder req = Request54011Define.newBuilder();
		req.setCmd(54011);
		req.setId(id);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		RiskActionTest test = new RiskActionTest();
		// String accId = "600";
		// String accId = "601";
		// String accId = "602";
		// String accId = "603";
		String accId = "633";
		RiskActionTest client = (RiskActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			// client.getUserRiskScenes(com.youxigu.dynasty2.risk.proto.RiskMsg.RiskType.NORMAL);
			// client.getOneRiskScene(1);

			// client.doCombat(1001);
			// client.doCombat(101);
			// client.doCombat(101);
			// client.doCombat(101);
			// client.doCombat(101);
			// client.doCombat(101);
			// client.doCombat(101);
			// client.doCombat(102);
			// client.doCombat(103);
			// client.doCombat(104);
			// client.doCombat(105);
			// client.doCombat(106);
			// client.doCombat(107);
			// client.doCombat(108);

			// client.doCombat(109);
			// client.doCombat(110);

			// client.doCombat(104);

			// 测试精英关卡
			// client.doCombat(401);

			// List<Integer> ids = new ArrayList<Integer>();
			// // ids.add(20);
			// ids.add(101);
			// ids.add(104);
			// // ids.add(110);
			// client.doAutoCombat(ids, 1);

			// client.gainStarAward(1, StarType.STAR_1);

			// client.firstBonus(101);
			// client.clearJoinNum(101);

			client.getQuickIds(1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
