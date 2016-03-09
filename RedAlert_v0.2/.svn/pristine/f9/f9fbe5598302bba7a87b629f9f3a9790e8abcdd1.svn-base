import java.util.zip.Deflater;

/**
 * 测试 Deflater使用 reset 与 end的效果
 * @author Administrator
 *
 */
public class DefalterTest {

	public static void main(String[] args) {

		int type = Integer.parseInt(args[0]);
		test(type);
		//System.gc();
		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("================");
	}
	
	public static void test(int type){
		
		for (int i = 0; i < 1000; i++) {
			Deflater deflater = new Deflater();
			byte[] inputs = new byte[512 * 1024];
			for (int j = 0; j < inputs.length; j++) {
				inputs[j] = (byte) j;
			}
			
			deflater.setInput(inputs, 0, inputs.length);
			deflater.finish();
			try {
				int cacheLen = 1024;

				byte[] bytes = new byte[cacheLen];
				while (!deflater.finished()) {
					int value = deflater.deflate(bytes, 0, cacheLen);
					// System.out.println("value="+value);
				}

			} finally {
				// System.gc();
				if (type == 1) {
					deflater.reset();
				} else if (type == 2) {
					deflater.end();
				}

			}
		}
	}

}
