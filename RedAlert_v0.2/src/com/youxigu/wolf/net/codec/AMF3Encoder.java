package com.youxigu.wolf.net.codec;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput; //import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class AMF3Encoder extends ProtocolEncoderAdapter {
	// private static Logger log = LoggerFactory
	// .getLogger(AMF3Encoder.class);

	private static Field bsBufField = null;
	static {
		try {
			bsBufField = ByteArrayOutputStream.class.getDeclaredField("buf");
			bsBufField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// long time = System.currentTimeMillis();
		IoBuffer buffer;
		if (message instanceof String) {// 安全沙箱不encode
			byte[] bytes = ((String) message).getBytes("UTF-8");
			buffer = IoBuffer.allocate(bytes.length + 1);
			buffer.put(bytes);
			buffer.put((byte) 0x0);
			buffer.flip();
			out.write(buffer);
		} else {
			Amf3Output amf3out = null;
			try {
				// 写object 到amf3out

				SerializationContext context = new SerializationContext();
				// //////////////////////////////////////////
				// 由于encoder要同时兼容java/flexx客户端,
				// 目前flex客户端为纯AS3,不支持ArrayCollection,需要将List转换成Array
				// 而java客户端需要保持为Collection
				// 这里假设所有Map类型的为flex客户端的数据
				// 其他类型为java客户端的数据
				context.legacyMap = true;
				if (message instanceof Map || message instanceof IAMF3Message) {
					context.legacyCollection = true;
				}
				// else{
				// context.legacyCollection=false;
				// context.legacyMap=false;
				// }

				amf3out = new Amf3Output(context);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				amf3out.setOutputStream(stream);
				amf3out.writeObject(message);
				amf3out.flush();
				// 减少一次内存copy
				byte[] bytes = (byte[]) bsBufField.get(stream);
				int byteLen = stream.size();
				// System.out.println("object:"+message+",encoder length:"+byteLen);
				// byte[] bytes = stream.toByteArray();
				// amf3out.close();

				buffer = IoBuffer.allocate(byteLen + 4, false);
				// System.out.println("send byte=" + bytes.length + "+4");
				// 加入长度字段//防止断包/粘包现象
				buffer.putInt(byteLen);
				buffer.put(bytes, 0, byteLen);
				buffer.flip();
				out.write(buffer);
				// out.flush();
				// buffer.free();
			} finally {
				if (amf3out != null) {
					amf3out.close();
				}
			}
		}
		// if (log.isDebugEnabled()){
		// log.debug("encode one message time:{}",(System.currentTimeMillis()-time));
		// }
	}

}
