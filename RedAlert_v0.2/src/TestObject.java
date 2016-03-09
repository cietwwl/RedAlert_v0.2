import java.io.IOException;
import java.io.ObjectInputStream;

public class TestObject implements java.io.Serializable {
	private int aaa;
	private long bbb;
	private String ccc = "asdasdasd";

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		if (in != null) {
			in.defaultReadObject();
		}
		aaa = -1;
	}

	public int getAaa() {
		return aaa;
	}

	public void setAaa(int aaa) {
		this.aaa = aaa;
	}

	public TestObject() {
		// aaa = -1;
	}
}
