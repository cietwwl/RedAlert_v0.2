package com.youxigu.wolf.net.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

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
public class MutliEncoderNew extends ProtocolEncoderAdapter {
	private static Logger log = LoggerFactory.getLogger(MutliEncoderNew.class);
	public static final byte BIT_COMPRESSED = 0x01;
	public static final byte BIT_UNCOMPRESSED = 0x00;

	public static final int HEAD_LEN = 5;

	public static final byte BIT_AMF3 = 0x00;
	public static final byte BIT_JAVA = 0x02;

	private static SerializationContext context = new SerializationContext();
	protected static Field bsBufField = null;
	static {
		context.legacyCollection = true;
		context.legacyMap = false;

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
	private int compressLen = 5120;// 5120;// 5K Integer.MAX_VALUE;// ;

	private int maxSendLen = 0;// 统计的最大消息长度

	// private final AttributeKey DEFLATER = new AttributeKey(getClass(),
	// "deflater");

	public void setCompressLen(int compressLen) {
		this.compressLen = compressLen;
	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		// long time = System.currentTimeMillis();
		if (message instanceof String) {// flash 安全沙箱或者TGW头不encode,
			String str = (String) message;

			IoBuffer buffer = null;
			if (str.startsWith("tgw_")) {
				byte[] bytes = str.getBytes("GBK");
				buffer = IoBuffer.allocate(bytes.length);
				buffer.put(bytes);
			} else {
				byte[] bytes = str.getBytes("UTF-8");
				buffer = IoBuffer.allocate(bytes.length + 1);
				buffer.put(bytes);
				buffer.put((byte) 0x0);
			}
			buffer.flip();
			out.write(buffer);
		} else {
			byte flag = BIT_AMF3;// 标志位
			// 这里使用的buffer都是非direct的，不用free;
			// TODO:这里默认应该分配多大呢?
			IoBuffer buffer = IoBuffer.allocate(256, false);
			buffer.setAutoExpand(true);
			// 留出协议头位置
			buffer.position(HEAD_LEN);

			if (message instanceof Map || message instanceof IAMF3Message) {
				getAmf3Bytes(buffer, message);
			} else {
				flag = BIT_JAVA;
				getJavaBytes(buffer, message);
			}
			// _encode(session, buffer, flag, out);

			// TODO:目前没有处理加密
			// 协议体字节数
			int position = buffer.position();
			int byteLen = position - HEAD_LEN;
			// System.out.println("=====len"+byteLen);
			if (log.isInfoEnabled()) {// 由于多线程的原因，这里显示的不完全正确
				if (byteLen > maxSendLen) {
					log.info("当前发出的最大消息长度：{}byte", byteLen);
					maxSendLen = byteLen;
				}
			}
			// 四字节长度位，+一字节标志位（第一位为压缩标志位，第二位为加密标志位）
			// 压缩
			if (byteLen > compressLen) {
				// long time = System.currentTimeMillis();
				int oldLen = byteLen;
				flag = (byte) (flag | BIT_COMPRESSED);
				byte[] inputs = buffer.buf().array();
				// // TODO:这里分配多大合适呢？
				// buffer = IoBuffer.allocate(3072, false);
				// buffer.setAutoExpand(true);
				buffer.clear();
				buffer.position(HEAD_LEN);
				if ((flag & 0xE) == MutliEncoderNew.BIT_JAVA) {
					compressJava(session, inputs, HEAD_LEN, byteLen, buffer);
				} else {
					compress(session, inputs, HEAD_LEN, byteLen, buffer);
				}
				position = buffer.position();
				byteLen = position - HEAD_LEN;

				if (log.isDebugEnabled()) {
					log.debug("消息体太长，被压缩,from {} bytes to {} bytes.", oldLen,
							byteLen);
				}
				// System.out.println("压缩时间:"
				// + (System.currentTimeMillis() - time));
			}

			buffer.position(0);
			// 加入长度字段,长度=数据长度+标志位长度
			buffer.putInt(byteLen + 1);
			buffer.put(flag);
			buffer.position(position);

			buffer.flip();

			out.write(buffer);

		}

		// System.out.println("time="+(System.currentTimeMillis()-time));

	}

	protected void getAmf3Bytes(IoBuffer buffer, Object message)
			throws Exception {
		Amf3Output amf3out = null;
		try {
			amf3out = new Amf3Output(context);
			amf3out.setOutputStream(new IoBufferOutputStream(buffer));
			amf3out.writeObject(message);
			amf3out.flush();
		} finally {
			if (amf3out != null) {
				amf3out.close();
			}
		}
	}

	protected void getJavaBytes(IoBuffer buffer, Object message)
			throws Exception {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new IoBufferOutputStream(buffer));
			oos.writeObject(message);
			oos.flush();
		} finally {
			if (oos != null) {
				oos.close();
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
	// private void _encode(IoSession session, IoBuffer buffer, byte flag,
	// ProtocolEncoderOutput out) throws Exception {
	//
	// // TODO:目前没有处理加密
	// // 协议体字节数
	// int position = buffer.position();
	// int byteLen = position - HEAD_LEN;
	//
	// // 四字节长度位，+一字节标志位（第一位为压缩标志位，第二位为加密标志位）
	// // 压缩
	// if (byteLen > compressLen) {
	// flag = (byte) (flag | BIT_COMPRESSED);
	// byte[] inputs = buffer.buf().array();
	// // TODO:这里分配多大合适呢？
	// buffer = IoBuffer.allocate(8192, false);
	// buffer.setAutoExpand(true);
	// buffer.position(HEAD_LEN);
	// compress(session, inputs, HEAD_LEN, byteLen, buffer);
	// position = buffer.position();
	// byteLen = position - HEAD_LEN;
	//
	// }
	//
	// buffer.position(0);
	// // 加入长度字段,长度=数据长度+标志位长度
	// buffer.putInt(byteLen + 1);
	// buffer.put(flag);
	// buffer.position(position);
	//
	// buffer.flip();
	//
	// out.write(buffer);
	// // out.flush();
	// // buffer.free();
	//
	// }

	// private void compress(IoSession session, byte[] inputs, int offset,
	// int byteLen, IoBuffer buffer) {
	// // 同一个session使用同一个压缩对象,减少Deflater的构造次数，必须做同步,
	// // byte outputs[] = null;
	// // int total = 0;
	// synchronized (session) {
	// Deflater deflater = (Deflater) session.getAttribute(DEFLATER);
	// if (deflater == null) {
	// deflater = new Deflater();
	// session.setAttribute(DEFLATER, deflater);
	// }
	//				
	//			
	//
	// deflater.setInput(inputs, offset, byteLen);
	// deflater.finish();
	// try {
	// int cacheLen = cachesize;
	// if (byteLen > cacheLen * 4) {
	// cacheLen = byteLen / 4;
	// }
	// byte[] bytes = new byte[cacheLen];
	// while (!deflater.finished()) {
	// int value = deflater.deflate(bytes, 0, cacheLen);
	// buffer.put(bytes, 0, value);
	// // total = total + value;
	// }
	//
	// } finally {
	// deflater.end();
	// deflater.reset();
	// }
	// }
	// }

	private void compress(IoSession session, byte[] inputs, int offset,
			int byteLen, IoBuffer buffer) {

		GZIPOutputStream os = null;
		ByteArrayOutputStream bs = null;
		try {
			bs = new ByteArrayOutputStream(byteLen/2);
			os = new GZIPOutputStream(bs, cachesize);
			os.write(inputs, offset, byteLen);

			os.flush();
			os.close();
			byte[] buf = (byte[]) bsBufField.get(bs);
			int bufLen = bs.size();
			buffer.put(buf, 0, bufLen);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IO exception compress data");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("IllegalArgumentException compress data");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("IllegalAccessException compress data");			
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	private void compressJava(IoSession session, byte[] inputs, int offset,
			int byteLen, IoBuffer buffer) {

		Deflater deflater = null;
		try {
			deflater = new Deflater();

			byte[] buf = new byte[cachesize];

			for (int i = 0; i < byteLen; i += cachesize) {
				deflater.setInput(inputs, offset + i, Math.min(cachesize,
						byteLen - i));
				while (!deflater.needsInput()) {
					int len = deflater.deflate(buf, 0, cachesize);
					if (len > 0) {
						buffer.put(buf, 0, len);
					}
				}
			}
			if (!deflater.finished()) {
				deflater.finish();
				while (!deflater.finished()) {
					int len = deflater.deflate(buf, 0, cachesize);
					if (len > 0) {
						buffer.put(buf, 0, len);
					}
				}
			}

		} finally {
			deflater.end();
		}

	}

}
