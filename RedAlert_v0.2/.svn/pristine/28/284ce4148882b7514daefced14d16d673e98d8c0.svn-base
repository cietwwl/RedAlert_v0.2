import java.util.ArrayList;
import java.util.List;

import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33111Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33113Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33115Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33117Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33119Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33121Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33125Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33127Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.Request33131Define;
import com.youxigu.dynasty2.hero.proto.HeroMsg.SellEquipCardAndDebriInfo;
import com.youxigu.dynasty2.user.proto.UserMsg.Response10003Define;

public class HeroEquipActionTest extends ClientServerTestCase {

	/**
	 * 换装备
	 * 
	 * @param heroId
	 * @param treasuryId
	 */
	public void takeEquip(long heroId, long treasuryId) {
		Request33111Define.Builder req = Request33111Define.newBuilder();
		req.setCmd(33111);
		req.setHeroId(heroId);
		req.setTreasuryId(treasuryId);
		request(req.build());
	}

	/**
	 * 脱装备
	 * 
	 * @param heroId
	 * @param treasuryId
	 */
	public void takeOffEquip(long heroId, long treasuryId) {
		Request33113Define.Builder req = Request33113Define.newBuilder();
		req.setCmd(33113);
		req.setHeroId(heroId);
		req.setTreasuryId(treasuryId);
		request(req.build());
	}

	/**
	 * 装备强化
	 * 
	 * @param heroId
	 * @param treasuryId
	 */
	public void equipLvUp(long heroId, long treasuryId, int num) {
		Request33115Define.Builder req = Request33115Define.newBuilder();
		req.setCmd(33115);
		req.setNum(num);
		req.setTId(treasuryId);
		request(req.build());
	}

	/**
	 * 装备回炉
	 * 
	 * @param heroId
	 * @param treasuryId
	 */
	public void equipRebirth(long treasuryId) {
		Request33121Define.Builder req = Request33121Define.newBuilder();
		req.setCmd(33121);
		req.addTIds(treasuryId);
		request(req.build());
	}

	/**
	 * 分解
	 * 
	 * @param heroId
	 * @param treasuryId
	 */
	public void doEquipDestroy(long treasuryId) {
		Request33119Define.Builder req = Request33119Define.newBuilder();
		req.setCmd(33119);
		req.addTIds(treasuryId);
		request(req.build());
	}

	/**
	 * 装备生产
	 * 
	 * @param heroId
	 * @param treasuryId
	 */
	public void equipBuild(int entId) {
		Request33125Define.Builder req = Request33125Define.newBuilder();
		req.setCmd(33125);
		req.setEntId(entId);
		request(req.build());
	}

	public void equipCompose(int equId) {
		Request33117Define.Builder req = Request33117Define.newBuilder();
		req.setCmd(33117);
		req.setEntId(equId);
		request(req.build());
	}

	public void equipCardAndDebris() {
		Request33127Define.Builder req = Request33127Define.newBuilder();
		req.setCmd(33127);
		request(req.build());
	}

	/**
	 * 出售图纸或者碎片
	 * 
	 * @param infos
	 */
	public void sellItem(List<SellEquipCardAndDebriInfo> infos) {
		Request33131Define.Builder req = Request33131Define.newBuilder();
		req.setCmd(33131);
		for (SellEquipCardAndDebriInfo is : infos) {
			req.addInfos(is);
		}
		request(req.build());
	}

	public static void main(String[] args) throws Exception {
		HeroEquipActionTest test = new HeroEquipActionTest();
		String accId = "603";
		HeroEquipActionTest client = (HeroEquipActionTest) test
				.startClient("192.168.1.89", 8739, accId, "admin", "123456",
						test.getClass());
		try {
			Response10003Define response10003Define = (Response10003Define) client
					.requestgetUser(client.sid);
			if (response10003Define.getUserId() < 0) {
				client.requestCreateUser(accId, 100000021);
			}
			// client.takeEquip(10601, 8501);
			// client.takeEquip(403, 0);// 一键穿装备
			// client.takeOffEquip(4001, 3407);
			// client.equipLvUp(4001, 3407, 1);
			// client.equipRebirth(4001, 3407);
			// client.doEquipDestroy(3408);
			// client.equipBuild(1001064);
			// client.equipCompose(1001002);
			// client.equipCardAndDebris();

			List<SellEquipCardAndDebriInfo> infos = new ArrayList<SellEquipCardAndDebriInfo>();
			SellEquipCardAndDebriInfo.Builder i1 = SellEquipCardAndDebriInfo
					.newBuilder();
			i1.setEntId(1007001);
			i1.setNum(1);
			infos.add(i1.build());

			client.sellItem(infos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// client.close();
		}
	}
}
