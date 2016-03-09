import com.youxigu.dynasty2.hero.enums.TroopGridPosition;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33001Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33003Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33005Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33009Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33013Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class TroopActionTest extends ClientServerTestCase {

	public void getTroopList() {
		Request33003Define.Builder req = Request33003Define.newBuilder();
		req.setCmd(33003);
		request(req.build());
	}

	public void getTroop(long troopId) {
		Request33005Define.Builder req = Request33005Define.newBuilder();
		req.setCmd(33005);
		req.setTroopId(troopId);
		request(req.build());
	}

	public void getTroopGrid(long troopGridId) {
		Request33009Define.Builder req = Request33009Define.newBuilder();
		req.setCmd(33009);
		req.setTroopGridId(troopGridId);
		request(req.build());
	}

	public void doUpTroop(long troopId, long heroId, TroopGridPosition ps,
			long troopGridId1) {
		Request33001Define.Builder req = Request33001Define.newBuilder();
		req.setCmd(33001);
		req.setHeroId(heroId);
		req.setTroopId1(troopId);
		// req.setPosition(ps.getIndex());
		// req.setTroopId(troopId);
		req.setTroopGridId1(troopGridId1);
		request(req.build());
	}

	public void saveTroopGrid() {
		Request33013Define.Builder req = Request33013Define.newBuilder();
		req.setCmd(33013);
		req.setTroopId(1001);
		//
		req.addTroopGridIds(1601);
		req.addTroopGridIds(0);
		req.addTroopGridIds(5701);
		req.addTroopGridIds(0);
		req.addTroopGridIds(0);
		req.addTroopGridIds(1601);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		TroopActionTest test = new TroopActionTest();
		String accId = "603";
		TroopActionTest client = (TroopActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000021);
			}
			// client.getTroopList();
			// client.getTroop(501);
			// client.getTroopGrid(401);
			// client.doUpTroop(11701, 15201, TroopGridPosition.GRID_POSITION_2,
			// 7001);
			// client.saveTroopGrid();

			client.doUpTroop(11701, 15201, TroopGridPosition.GRID_POSITION_1, 0);
			client.doUpTroop(11701, 15202, TroopGridPosition.GRID_POSITION_2, 0);
			client.doUpTroop(11701, 15203, TroopGridPosition.GRID_POSITION_3, 0);
			client.doUpTroop(11701, 15204, TroopGridPosition.GRID_POSITION_4, 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
