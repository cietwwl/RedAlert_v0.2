import com.youxigu.dynasty2.hero.enums.HeroDrawType;
import com.youxigu.dynasty2.hero.proto.HeroDrawInfo;

public class HeroDrawServiceTest extends TestCaseParent {
	private static final String SERVICE_NAME = "heroDrawService";

	public HeroDrawServiceTest(long userId) {
		super(userId);
	}

	public void doHireHero(HeroDrawType type) throws Exception {
		HeroDrawInfo vip = (HeroDrawInfo) getClient().sendTask(SERVICE_NAME,
				"doHireHero", userId, type);
		System.out.println(vip);
	}

	public static void main(String[] args) throws Exception {
		HeroDrawServiceTest test = new HeroDrawServiceTest(2);
		test.doHireHero(HeroDrawType.HERO_DRAW_TYPE1);
	}

}
