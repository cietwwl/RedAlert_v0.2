package com.youxigu.wolf.net.codec;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.InflaterInputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

public class AMF3CompressDecoder extends CumulativeProtocolDecoder {
	private static Logger log = LoggerFactory
			.getLogger(AMF3CompressDecoder.class);

	private final AttributeKey POLICY = new AttributeKey(this.getClass(),
			"policy");
	private final String securityReq = "<policy-file-request/>";

	private final String security = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ "	<!DOCTYPE cross-domain-policy SYSTEM \"http://www.adobe.com/xml/dtds/cross-domain-policy.dtd\">\n"
			+ "	<cross-domain-policy>\n"
			+ "		<site-control permitted-cross-domain-policies=\"all\"/>\n"
			+ "		<allow-access-from domain=\"*\" to-ports=\"9000-12000\" />\n"
			+ "	</cross-domain-policy>\n";

	private static SerializationContext context = new SerializationContext();
//	static{
//		context.legacyMap=true;	
//	}	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (this.isSecurityRequest(session, in)) {
			// 安全策略请求
			// ASObject asObject = new ASObject();
			// asObject.put("policy", securityReq);
			// out.write(asObject);
			if (log.isDebugEnabled())
				log.debug("amf3 安全策略请求.......");
			session.write(security);
		} else {
			Amf3Input amf3in = null;
			//int start = 0;
			try {
				while (in.remaining() > 0) {///这个循环实际上可以不用，CumulativeProtocolDecoder已经处理了
					if (in.prefixedDataAvailable(4)) {// 必须大于一个int长度
						//start = in.position();
						// System.out.println("start:" + start);
						// 正常Encoder中写入的包头制定长度数据
						int len = in.getInt();
						//读标志位
						byte compressed = in.get();// =0未压缩=1压缩
						
						len=len-1;//减掉标志位长度
						byte bytes[] = new byte[len];
						in.get(bytes, 0, len);

						// 获取flash socket传入的信息.此时客户端传入的是一个ASObject(HashMap的子类)对象
						amf3in = new Amf3Input(context);
						if (compressed == 1) {
							//TODO:这里用InflaterInputStream 还是Inflater更好呢?
							amf3in.setInputStream(new InflaterInputStream(
									new ByteArrayInputStream(bytes)));
						} else {
							amf3in.setInputStream(new ByteArrayInputStream(
									bytes));

						}
						Object message = amf3in.readObject();
						amf3in.close();
						if (message != null) {
							out.write(message);
						}
						// in.free();
					} else {
						// 包长度不正确，等待后续包
						if (log.isDebugEnabled())
							log.debug("包长度不正确，等待后续包.......");
						// System.out.println("length is error");
						return false;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				// in.position(start);
				return false;
			} finally {
				if (amf3in != null)
					amf3in.close();
			}
		}
		//in.free();
		return true;
		// if (message instanceof ASObject) {
		// out.write(message);
		// in.free();
		// return true;
		// } else {
		// in.free();
		// return false;
		// }

	}

	private boolean isSecurityRequest(IoSession session, IoBuffer in) {
		Boolean policy = (Boolean) session.getAttribute(POLICY);
		if (policy != null) {
			return false;
		}
		int start = in.position();
		String request = this.getRequest(in);
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

	private String getRequest(IoBuffer in) {
		byte[] bytes = new byte[in.limit()];
		in.get(bytes);// 从IoBuffer中获取数据并放入bytes中
		String request = null;
		try {
			request = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			request = null;
		}
		return request;
	}

}
