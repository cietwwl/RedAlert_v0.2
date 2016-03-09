package com.youxigu.wolf.net.codec;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.zip.Deflater;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

public class AMF3CompressEncoder extends ProtocolEncoderAdapter {

	private static int cachesize = 1024;
	/**
	 * 字节数》compressLen压缩，否则不压缩
	 */
	private int compressLen = 4096;

	// private final AttributeKey DEFLATER = new AttributeKey(getClass(),
	// "deflater");

	public void setCompressLen(int compressLen) {
		this.compressLen = compressLen;
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
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

				if (message instanceof Map || message instanceof IAMF3Message) {
					context.legacyCollection = true;
					context.legacyMap = true;
				} else {
					context.legacyCollection = false;
				}

				amf3out = new Amf3Output(context);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				amf3out.setOutputStream(stream);
				amf3out.writeObject(message);
				amf3out.flush();

				// TODO:目前没有处理加密

				// 压缩
				byte compressed = 0;
				byte[] bytes = stream.toByteArray();
				if (bytes.length > compressLen) {
					bytes = compress(session, bytes);
					compressed = 1;
				}

				// 四字节长度位，+一字节标志位（第一位为压缩标志位，第二位为加密标志位）

				buffer = IoBuffer.allocate(bytes.length + 5, false);

				// 加入长度字段,长度=数据长度+标志位长度
				buffer.putInt(bytes.length + 1);
				buffer.put(compressed);
				buffer.put(bytes);
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

	}

	private byte[] compress(IoSession session, byte[] inputs) {
		// 同一个session使用同一个压缩对象,减少Deflater的构造次数，必须做同步,
		byte outputs[] = null;

		Deflater deflater = null;

		ByteArrayOutputStream stream = null;
		try {
			stream = new ByteArrayOutputStream(inputs.length);			
			deflater = new Deflater();
			deflater.setInput(inputs);
			deflater.finish();

			byte[] bytes = new byte[cachesize];
			int value;
			while (!deflater.finished()) {
				value = deflater.deflate(bytes);
				stream.write(bytes, 0, value);
			}
			outputs = stream.toByteArray();

		} finally {
			deflater.end();
			// if (stream != null) {
			// try {
			// stream.close();
			// } catch (Exception e) {
			// }
			// }
		}

		return outputs;
	}

}
