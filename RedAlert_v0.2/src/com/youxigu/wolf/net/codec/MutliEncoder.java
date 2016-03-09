package com.youxigu.wolf.net.codec;

import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.zip.Deflater;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Output;

/**
 * 
 * 支持java serial 与 AMF3的混合协议.<br>
 * 协议：5个字节协议头+协议体.<br>
 * 协议头1-4字节表示协议长度=协议体长度+协议头长度-4（去掉长度占的4字节） 协议头第5字节为标志字节：<br>
 * 该字节的最低位为压缩位：0=协议体未压缩 1=协议体已经压缩<br>
 * 该字节的低2-4位为协议位：000=基于AMF3的协议，001=基于java serial协议 <br>
 * 5-8位未用，作为以后扩展
 * 
 * 也就是说在AMF3协议下，java-->as或者as-->java的标志字节位只会是0或者1，表示压缩，未压缩
 * 
 * 
 * @author Administrator
 * 
 */
@Deprecated
public class MutliEncoder extends ProtocolEncoderAdapter {
	private static Logger log = LoggerFactory.getLogger(MutliEncoder.class);
	public static final byte BIT_COMPRESSED = 0x01;
	public static final byte BIT_UNCOMPRESSED = 0x00;

	public static final byte BIT_AMF3 = 0x00;
	public static final byte BIT_JAVA = 0x02;
	
	private static SerializationContext context = new SerializationContext();
	private static Field bsBufField = null;
	static {
		context.legacyCollection = true;
		context.legacyMap = true;

		try {
			bsBufField = ByteArrayOutputStream.class.getDeclaredField("buf");
			bsBufField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int cachesize = 1024;
	/**
	 * 字节数》compressLen压缩，否则不压缩
	 */
	private int compressLen = 5120;// Integer.MAX_VALUE;// 4096;

	public void setCompressLen(int compressLen) {
		this.compressLen = compressLen;
	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		if (message instanceof String) {// flash 安全沙箱不encode
			byte[] bytes = ((String) message).getBytes("UTF-8");
			IoBuffer buffer = IoBuffer.allocate(bytes.length + 1);
			buffer.put(bytes);
			buffer.put((byte) 0x0);
			buffer.flip();
			out.write(buffer);
		} else {
			if (message instanceof Map || message instanceof IAMF3Message) {
				amf3Encode(session, message, out);
			} else {
				javaEncode(session, message, out);
			}
		}

	}

	/**
	 * amf3 encode
	 * 
	 * @param session
	 * @param message
	 * @param out
	 * @throws Exception
	 */
	private void amf3Encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer = null;
		Amf3Output amf3out = null;
		try {
			byte flag = BIT_AMF3;// 标志位
			// 写object 到amf3out
			amf3out = new Amf3Output(context);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			amf3out.setOutputStream(bos);
			amf3out.writeObject(message);
			amf3out.flush();

			// TODO:目前没有处理加密

			// 减少一次内存copy
			byte[] bytes = (byte[]) bsBufField.get(bos);
			int byteLen = bos.size();

			// 四字节长度位，+一字节标志位（第一位为压缩标志位，第二位为加密标志位）
			// 压缩
			if (byteLen > compressLen) {

				flag = (byte) (flag | BIT_COMPRESSED);
				// TODO:这里分配多大合适呢？
				buffer = IoBuffer.allocate(compressLen, false);
				buffer.setAutoExpand(true);
				buffer.position(5);
				compress(session, bytes, byteLen, buffer);
				int position = buffer.position();
				buffer.position(0);
				buffer.putInt(position - 4);
				buffer.put(flag);
				buffer.position(position);
				if (log.isDebugEnabled()) {
					log.debug("消息{}太长，被压缩,from {} byte to {} byte",
							new Object[] { message, byteLen, position });
				}
			} else {

				buffer = IoBuffer.allocate(byteLen + 5, false);

				// 加入长度字段,长度=数据长度+标志位长度
				buffer.putInt(byteLen + 1);
				buffer.put(flag);
				buffer.put(bytes, 0, byteLen);

				if (log.isDebugEnabled()) {
					if (byteLen > 1024) {
						log.debug("消息{}太长 {} bytes", message, byteLen);
					}
				}
			}
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

	/**
	 * java encode
	 * 
	 * @param session
	 * @param message
	 * @param out
	 * @throws Exception
	 */
	private void javaEncode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {

		// java 没有做压缩
		if (!(message instanceof Serializable)) {
			throw new NotSerializableException();
		}
		IoBuffer buffer = null;
		ObjectOutputStream oos = null;
		try {
			byte flag = BIT_JAVA;// 标志位

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(message);
			oos.flush();
			// oos.close();
			// 这里不是用toByteArray()是为了减少一次内存copy
			// newValue = bos.toByteArray();
			byte[] bytes = (byte[]) bsBufField.get(bos);
			int byteLen = bos.size();

			// 压缩
			if (byteLen > compressLen) {
				flag = (byte) (flag | BIT_COMPRESSED);
				// ///直接使用IOBuffer作为压缩的输出，可以节省一次内存拷贝
				// TODO:这里分配多大合适呢？
				buffer = IoBuffer.allocate(compressLen, false);
				buffer.setAutoExpand(true);
				buffer.position(5);
				compress(session, bytes, byteLen, buffer);
				int posion = buffer.position();
				buffer.position(0);
				buffer.putInt(posion - 4);
				buffer.put(flag);
				buffer.position(posion);

			} else {
				// 四字节长度位，+一字节标志位（第一位为压缩标志位，第二位为加密标志位）
				buffer = IoBuffer.allocate(byteLen + 5, false);
				// 加入长度字段,长度=数据长度+标志位长度
				buffer.putInt(byteLen + 1);
				buffer.put(flag);
				buffer.put(bytes, 0, byteLen);
			}
			buffer.flip();
			out.write(buffer);
		} finally {
			if (oos != null) {
				oos.close();
			}
		}
	}

	// //压缩结果使用为压缩前的byte[]
	// private int compress(IoSession session, byte[] inputs, int byteLen) {
	// // 同一个session使用同一个压缩对象,减少Deflater的构造次数，必须做同步,
	// //byte outputs[] = null;
	// int len = 0;
	// synchronized (session) {
	// Deflater deflater = (Deflater) session.getAttribute(DEFLATER);
	// if (deflater == null) {
	// deflater = new Deflater();
	// session.setAttribute(DEFLATER, deflater);
	// }
	// deflater.reset();
	// deflater.setInput(inputs, 0, byteLen);
	// deflater.finish();
	//
	// byte[] bytes = new byte[cachesize];
	// int value;
	// while (!deflater.finished()) {
	// value = deflater.deflate(bytes);
	// System.arraycopy(bytes, 0, inputs, len, value);
	// len = len +value;
	// }
	// }
	// return len;
	// }

	private ByteArrayOutputStream compress(IoSession session, byte[] inputs,
			int byteLen) {
		// 同一个session使用同一个压缩对象,减少Deflater的构造次数，必须做同步,
		// byte outputs[] = null;
		ByteArrayOutputStream stream = null;

		Deflater deflater = new Deflater();

		deflater.setInput(inputs, 0, byteLen);
		deflater.finish();
		// TODO:这里不用ByteArrayOutputStream是否可以减少内存占用？
		try {
			stream = new ByteArrayOutputStream(compressLen);
			byte[] bytes = new byte[cachesize];
			int value;
			while (!deflater.finished()) {
				value = deflater.deflate(bytes, 0, cachesize);
				stream.write(bytes, 0, value);
			}
			// return stream;//outputs = stream.toByteArray();

		} finally {
			deflater.end();
			// if (stream != null) {
			// try {
			// stream.close();
			// } catch (Exception e) {
			// }
			// }
		}

		return stream;
	}

	private void compress(IoSession session, byte[] inputs, int byteLen,
			IoBuffer buffer) {
		// 同一个session使用同一个压缩对象,减少Deflater的构造次数，必须做同步,
		// byte outputs[] = null;
		int total = 0;
		Deflater deflater = new Deflater();

		deflater.setInput(inputs, 0, byteLen);
		deflater.finish();
		try {
			byte[] bytes = new byte[cachesize];
			while (!deflater.finished()) {
				int value = deflater.deflate(bytes, 0, cachesize);
				buffer.put(bytes, 0, value);
				total = total + value;
			}

		} finally {
			deflater.end();
		}
	}

}
