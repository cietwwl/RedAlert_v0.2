import com.youxigu.dynasty2.core.GMSocketClient;

/**
 * 测试抽象类
 * 
 * @author fengfeng
 *
 */
public abstract class TestCaseParent {
	protected GMSocketClient client;
	protected long userId;

	public TestCaseParent(long userId) {
		this.userId = userId;
		client = new GMSocketClient("127.0.0.1", 8739, "admin", "123456");
	}

	public void close() {
		client.close();
	}

	public GMSocketClient getClient() {
		return client;
	}

	public long getUserId() {
		return userId;
	}
}
