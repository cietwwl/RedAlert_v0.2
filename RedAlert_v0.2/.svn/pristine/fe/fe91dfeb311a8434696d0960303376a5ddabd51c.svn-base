import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;

import com.ibatis.sqlmap.engine.cache.memcached.XSerializingTranscoderEx;

public class TranscoderTest {

	public static void main(String[] args) {
		test2();
	}

	public static void test1() {
		System.gc();
		long time = System.currentTimeMillis();
		XSerializingTranscoderEx transcoder = new XSerializingTranscoderEx();
		transcoder.setCompressionThreshold(1024);

		List<Object> datas = new ArrayList<Object>();
		for (int i = 0; i < 10000; i++) {
			datas.add("21321421354436457567865");
		}
		CachedData tmp = transcoder.encode(datas);
		for (int i = 0; i < 1000; i++) {
			
			Object obj = transcoder.decode(tmp);
		}
		System.out.println("time="+(System.currentTimeMillis()-time));
		System.gc();
		System.gc();
	}
	
	public static void test2() {
		
		System.gc();
		long time = System.currentTimeMillis();
		SerializingTranscoder transcoder = new SerializingTranscoder();
		transcoder.setCompressionThreshold(1024);

		List<Object> datas = new ArrayList<Object>();
		for (int i = 0; i < 10000; i++) {
			datas.add("21321421354436457567865");
		}
		CachedData tmp = transcoder.encode(datas);
		for (int i = 0; i < 1000; i++) {
			
			Object obj = transcoder.decode(tmp);
		}
		System.out.println("time="+(System.currentTimeMillis()-time));
		System.gc();
		System.gc();
		
		
	}
}
