import com.youxigu.dynasty2.hero.proto.HeroMsg.ExpItem;
import com.youxigu.dynasty2.hero.proto.HeroMsg.HeroArmyInfo;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31021Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31025Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31027Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31029Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31031Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31035Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31037Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31039Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31041Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request31043Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33107Define;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class HeroActionTest extends ClientServerTestCase {

	public void loadHeroViewByUserId() {
		Request31021Define.Builder req = Request31021Define.newBuilder();
		req.setCmd(31021);
		request(req.build());
	}

	public void heroLevelUp(long heroId) {
		Request31025Define.Builder req = Request31025Define.newBuilder();
		req.setCmd(31025);
		req.setHeroId(heroId);
		ExpItem.Builder exp = ExpItem.newBuilder();
		exp.setEntId(1003001);
		exp.setNum(1);
		req.addExpItems(exp.build());

		// exp = ExpItem.newBuilder();
		// exp.setEntId(1002168);
		// exp.setNum(5);
		// req.addExpItems(exp.build());

		request(req.build());
	}

	/**
	 * 进阶
	 * 
	 * @param heroId
	 */
	public void relifeHero(long heroId) {

		Request31027Define.Builder req = Request31027Define.newBuilder();
		req.setCmd(31027);
		req.setHeroId(heroId);
		request(req.build());
	}

	/**
	 * 强化
	 * 
	 * @param heroId
	 * @param num
	 */
	public void doHeroStrength(long heroId, int num) {
		Request31029Define.Builder req = Request31029Define.newBuilder();
		req.setCmd(31029);
		req.setHeroId(heroId);
		request(req.build());
	}

	public void getHeroEffectValue(long heroId) {
		Request31043Define.Builder req = Request31043Define.newBuilder();
		req.setCmd(31043);
		req.setHeroId(heroId);
		request(req.build());
	}

	public void doHeroRebirth(long heroId, boolean retire) {
		Request31031Define.Builder req = Request31031Define.newBuilder();
		req.setCmd(31031);
		req.addHeroIds(heroId);
		// req.setHeroId(heroId);
		req.setRetire(retire);
		request(req.build());
	}

	public void changeHeroArmy(long heroId) {
		Request31039Define.Builder req = Request31039Define.newBuilder();
		req.setCmd(31039);
		req.setStatus(1);
		HeroArmyInfo.Builder info = HeroArmyInfo.newBuilder();
		info.setArmyNum(1);
		info.setHeroId(heroId);
		req.addHeroArmys(info.build());
		request(req.build());
	}

	public void autoHeroArmy() {
		Request31041Define.Builder req = Request31041Define.newBuilder();
		req.setCmd(31041);
		req.setStatus(1);
		request(req.build());
	}

	public void doHeroCardAndDebris() {
		Request33107Define.Builder req = Request33107Define.newBuilder();
		req.setCmd(33107);
		request(req.build());
	}

	public void doHeroCardDiscard(long heroId) {
		Request31037Define.Builder req = Request31037Define.newBuilder();
		req.setCmd(31037);
		req.addHeroIds(heroId);
		request(req.build());
	}

	public void doHeroSoulComposite(long heroId) {
		Request31035Define.Builder req = Request31035Define.newBuilder();
		req.setCmd(31035);
		req.setHeroId(heroId);
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		HeroActionTest test = new HeroActionTest();
		String accId = "111";
		HeroActionTest client = (HeroActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000025);
			}
			client.loadHeroViewByUserId();
			// client.getHeroEffectValue(2001);
			// client.heroLevelUp(7601);
			// client.relifeHero(7601);
			// client.doHeroStrength(7601, 1);
			// client.doHeroRebirth(7601, true);
			// client.changeHeroArmy(4202);
			// client.autoHeroArmy();
			// client.doHeroCardAndDebris();
			// client.doHeroCardDiscard(5401);
			// client.doHeroSoulComposite(6709);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
