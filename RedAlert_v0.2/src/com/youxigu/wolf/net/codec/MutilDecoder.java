package com.youxigu.wolf.net.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.mina.core.buffer.BufferDataException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.wolf.net.NodeSessionMgr;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

public class MutilDecoder extends CumulativeProtocolDecoder {
	private static Logger log = LoggerFactory.getLogger(MutilDecoder.class);

	// FLASH安全沙箱标志
	private final AttributeKey POLICY = new AttributeKey(this.getClass(),
			"policy");
	// 腾讯TGW协议头
	private final AttributeKey TGWHEAD = new AttributeKey(this.getClass(),
			"TGW");
	private final String securityReq = "<policy-file-request/>";
	private final String security = "<cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\0";
	// private final String security =
	// "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
	// +
	// "	<!DOCTYPE cross-domain-policy SYSTEM \"http://www.adobe.com/xml/dtds/cross-domain-policy.dtd\">\n"
	// + "	<cross-domain-policy>\n"
	// + "		<site-control permitted-cross-domain-policies=\"all\"/>\n"
	// + "		<allow-access-from domain=\"*\" to-ports=\"9000-12000\" />\n"
	// + "	</cross-domain-policy>\n";
	private static SerializationContext context = new SerializationContext();

	protected static Field bsBufField = null;
	static {
		context.createASObjectForMissingType = true;
		try {
			bsBufField = ByteArrayOutputStream.class.getDeclaredField("buf");
			bsBufField.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * decoder最大长度
	 */
	private int maxDecodeLen = 5 * 1024 * 1024;

	public void setMaxDecodeLen(int maxDecodeLen) {
		this.maxDecodeLen = maxDecodeLen;
	}

	// static{
	// context.legacyMap=true;
	// }
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// long time = System.currentTimeMillis();
		if (this.isSecurityRequest(session, in)) {
			// 安全策略请求
			// ASObject asObject = new ASObject();
			// asObject.put("policy", securityReq);
			// out.write(asObject);
			session.write(security);
		} else {
			// 去掉TGW包头
			skipTGWRequest(session, in);
			try {
				while (in.remaining() > 0) {// /这层循环实际上不需要,CumulativeProtocolDecoder已经处理了
					boolean dataAvai = false;
					if (session
							.containsAttribute(NodeSessionMgr.SERVER_TYPE_KEY)) {
						dataAvai = in.prefixedDataAvailable(4,
								Integer.MAX_VALUE);
					} else {
						dataAvai = in.prefixedDataAvailable(4, maxDecodeLen);
					}
					if (dataAvai) {
						//System.out.println("*****"+in.getInt(in.position()));
						// 正常Encoder中写入的包头制定长度数据
						int len = in.getInt();

						byte flag = in.get();// 标志位

						// 是否压缩
						boolean compressed = ((flag & 0x1) == MutliEncoderNew.BIT_COMPRESSED);

						// //先把需要的字节数读到数组中，防止decode出错后有剩余的字节保留在IoBuffer,使下一个请求解析不了
						byte bytes[] = new byte[len - 1];
						in.get(bytes, 0, len - 1);
						if ((flag & 0xE) == MutliEncoderNew.BIT_JAVA) {
							javaDecode(out, bytes, compressed);
						} else {
							amf3Decode(out, bytes, compressed);
						}
						//System.out.println("========1");
					} else {
						// 包长度不正确，等待后续包
						if (log.isDebugEnabled()) {
							log.debug("包长度不正确，等待后续包.......");
						}
						//System.out.println(":::总长度"+in.getInt(in.position())+"，本次接收长度:"+in.remaining());
						// System.out.println("length is error");
						return false;
					}

				}
			} catch (BufferDataException e) {
				log.error("解码数据长度不在限制范围内,丢弃并关闭session.{}", session);
				session.close(true);
				throw e;
			} catch (Exception e) {
				log.error(e.getMessage());
				throw e;
			}
		}
		return true;

	}

	public void amf3Decode(ProtocolDecoderOutput out, byte[] bytes,
			boolean compressed) throws Exception {

		int len = bytes.length;

		ByteArrayInputStream bios = new ByteArrayInputStream(bytes, 0, len);

		Amf3Input amf3in = null;
		try {
			amf3in = new Amf3Input(context);
			if (compressed) {
				amf3in.setInputStream(new GZIPInputStream(bios));
			} else {
				amf3in.setInputStream(bios);
			}
			Object message = amf3in.readObject();
			if (message != null) {
				out.write(message);
			}

		} finally {
			if (amf3in != null) {
				amf3in.close();
			}
		}

	}

	public void javaDecode(ProtocolDecoderOutput out, byte[] bytes,
			boolean compressed) throws Exception {
		int len = bytes.length;

		ByteArrayInputStream bios = new ByteArrayInputStream(bytes, 0, len);

		ObjectInputStream ois = null;

		try {
			if (compressed) {
				ois = new ObjectInputStream(new InflaterInputStream(bios));
			} else {
				ois = new ObjectInputStream(bios);
			}
			Object message = ois.readObject();
			if (message != null) {
				out.write(message);
			}

		} finally {
			if (ois != null) {
				ois.close();
			}
		}

		// in.close();

	}

	private boolean isSecurityRequest(IoSession session, IoBuffer in) {
		Boolean policy = (Boolean) session.getAttribute(POLICY);
		if (policy != null) {
			return false;
		}
		int len = in.limit();
		if (len < 23) {
			// 不够安全沙箱的长度，不设置
			return false;
		}
		int start = in.position();
		byte[] bytes = new byte[len];
		in.get(bytes);// 从IoBuffer中获取数据并放入bytes中
		String request = null;
		try {
			request = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			request = null;
		}

		boolean result = false;
		if (request != null) {
			result = request.startsWith(securityReq);
		}
		session.setAttribute(POLICY, new Boolean(result));
		if (!result) {
			in.position(start);
		}
		return result;
	}

	/**
	 * 腾讯TGW协议 ：TGW包头格式为如下:tgw_l7_forward\r\nHost:
	 * app12345-54321.qzoneapp.com:8002\r\n\r\n
	 * 
	 * @param session
	 * @param in
	 * @return
	 */
	private void skipTGWRequest(IoSession session, IoBuffer in) {
		Boolean tgw = (Boolean) session.getAttribute(TGWHEAD);
		if (tgw != null) {
			return;
		}
		int start = in.position();
		int len = in.limit();

		boolean result = false;
		// byte[] bytes = new byte[len];

		int pos = 0;
		if (len > 25) {
			// tgw开始
			if (in.get() == 't' && in.get() == 'g' && in.get() == 'w') {
				while (pos < len - 4) {
					if (in.get() == '\r') {
						if (in.get() == '\n') {
							if (in.get() == '\r') {
								if (in.get() == '\n') {
									result = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		session.setAttribute(TGWHEAD, new Boolean(result));
		if (!result) {
			in.position(start);
		}

	}

	// private String getRequest(IoBuffer in) {
	// byte[] bytes = new byte[in.limit()];
	// in.get(bytes);// 从IoBuffer中获取数据并放入bytes中
	// String request = null;
	// try {
	// request = new String(bytes, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// request = null;
	// }
	// return request;
	// }

}
