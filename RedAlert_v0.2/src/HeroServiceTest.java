import com.youxigu.dynasty2.hero.domain.Hero;

public class HeroServiceTest extends TestCaseParent {

	public HeroServiceTest(long userId) {
		super(userId);
	}

//	/**
//	 * 加物品到用户的背包里面去
//	 * 
//	 * @param userId
//	 * @param itemId
//	 * @param itemNum
//	 * @throws Exception
//	 */
//	public void addItemToTreasury(int itemId, int itemNum) throws Exception {
//		boolean hero = (boolean) getClient().sendTask("treasuryService",
//				"doAddItemToTreasury", userId, itemId, itemNum);
//		System.out.println("添加 物品成功" + hero);
//	}

	/**
	 * 给武将穿装备
	 * 
	 * @param heroId
	 * @param treasuryId
	 * @throws Exception
	 */
	public void doTakeEquip(long heroId, long treasuryId) throws Exception {
		Hero hero = (Hero) getClient().sendTask("heroEquipService",
				"doTakeEquip", userId, heroId, treasuryId);
		System.out.println("给武将穿装备" + hero);
	}

	/**
	 * 脱下装备
	 * 
	 * @param heroId
	 * @param treasuryId
	 * @throws Exception
	 */
	public void doTakeOffEquip(long heroId, long treasuryId) throws Exception {
		Hero hero = (Hero) getClient().sendTask("heroEquipService",
				"doTakeOffEquip", userId, heroId, treasuryId);
		System.out.println("给武将穿装备" + hero);
	}

	/**
	 * 升级装备
	 * 
	 * @param treasuryId
	 * @param num
	 * @throws Exception
	 */
	public void doEquipLevelup(long treasuryId, int num) throws Exception {
		Hero hero = (Hero) getClient().sendTask("heroEquipService",
				"doEquipLevelup", userId, treasuryId, num);
		System.out.println("给武将升级穿装备" + hero);
	}

	public static void main(String[] args) {
		HeroServiceTest test = new HeroServiceTest(2);
		try {
			// test.addItemToTreasury(1001001, 1);
			// test.doTakeEquip(901, 301);
			// test.doTakeOffEquip(901, 301);
			test.doEquipLevelup(301, 1);
			// System.out.println(hero);
			// Hero hero = (Hero) test.getClient().sendTask("heroService",
			// "createAHero", test.getUserId(), 100000024,
			// LogHeroAct.Init_Hero_ADD);
			// System.out.println(hero);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			test.close();
		}
		System.out.println("==================");

	}
}
