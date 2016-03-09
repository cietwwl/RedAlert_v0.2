import com.youxigu.dynasty2.vip.domain.UserVip;

public class VipServiceTest extends TestCaseParent {
	private static final String SERVICE_NAME = "vipService";

	public VipServiceTest(long userId) {
		super(userId);
	}

	public void createVip() throws Exception {
		getClient().sendTask(SERVICE_NAME, "doCreateUserVip", userId);
	}

	public void updateUserVip(int level) throws Exception {
		UserVip vip = (UserVip) getClient().sendTask(SERVICE_NAME,
				"getUserVip", userId);
		vip.setVipLv(level);
		getClient().sendTask(SERVICE_NAME, "doUpdateUserVip", vip);
	}

	public static void main(String[] args) throws Exception {
		VipServiceTest test = new VipServiceTest(2);
		test.createVip();
		test.updateUserVip(1);
	}

}
