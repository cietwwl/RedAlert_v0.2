import java.util.Map;

import com.manu.util.bean.UtilBean;


public class TestObject1 implements java.io.Serializable {

	private int aaa;
	private transient int bbb;
	private TestObject2 obj = new TestObject2();

	public String getCcc(){
		return "ccc";
	}
	public int getAaa() {
		return aaa;
	}

	public void setAaa(int aaa) {
		this.aaa = aaa;
	}

	public int getBbb() {
		return bbb;
	}

	public void setBbb(int bbb) {
		this.bbb = bbb;
	}

	public TestObject2 getObj() {
		return obj;
	}

	public void setObj(TestObject2 obj) {
		this.obj = obj;
	}

	public static class TestObject2 {
		private int aaa;
		private long bbb;
		private String ccc = "asdasdasd";
		public int getAaa() {
			return aaa;
		}
		public void setAaa(int aaa) {
			this.aaa = aaa;
		}
		public long getBbb() {
			return bbb;
		}
		public void setBbb(long bbb) {
			this.bbb = bbb;
		}
		public String getCcc() {
			return ccc;
		}
		public void setCcc(String ccc) {
			this.ccc = ccc;
		}
		
	}
	
	
	public static void main(String[] args){
		Map map = UtilBean.bean2map(new TestObject1());
		System.out.println("=====");
	}
}
