
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.youxigu.wolf.net.codec.MutilDecoder;

/**
 * 
 * TODO：可以用mina 的DemuxingProtocolCodecFactory来实现
 * 
 * 名字拼写错误，懒得修改了，就这样吧.<br>
 * 
 * 支持java serial 与 AMF3的混合协议.<br>
 * 协议：5个字节协议头+协议体.<br>
 * 协议头1-4字节表示协议长度=协议体长度+1（协议头的标志位） 协议头第5字节为标志字节：<br>
 * 该字节的最低位为压缩位：0=协议体未压缩 1=协议体已经压缩<br>
 * 该字节的低2-4位为协议位：000=基于AMF3的协议，001=基于java serial协议 <br>
 * 5-8位未用，作为以后扩展
 * 
 * @author Administrator
 * 
 */
public class TestCodecFactory implements ProtocolCodecFactory {

	private TestMutliEncoderNew encoder;
	private MutilDecoder decoder;

	public TestCodecFactory() {
		encoder = new TestMutliEncoderNew();
		decoder = new MutilDecoder();
	}

	public void setCompressLen(int compressLen) {
		encoder.setCompressLen(compressLen);
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

}
