package com.youxigu.wolf.net.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class AMF3CodecFactory implements ProtocolCodecFactory {
	private ProtocolEncoder encoder;
	private ProtocolDecoder decoder;

//	private ProtocolEncoder javaEncoder;
//	private ProtocolDecoder javaDecoder;
	
	public AMF3CodecFactory() {
		// TODO Auto-generated constructor stub
		encoder = new AMF3Encoder();
		decoder = new AMF3Decoder();
//		
//		javaEncoder = new ObjectSerializationEncoder();
//		javaDecoder = new ObjectSerializationDecoder();		
		
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
//		if (session.containsAttribute(NodeSessionMgr.SERVER_TYPE_KEY)){
//			System.out.println("java decoder");
//			return javaDecoder;
//		}
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
//		if (session.containsAttribute(NodeSessionMgr.SERVER_TYPE_KEY)){
//			System.out.println("java encoder");			
//			return javaEncoder;
//		}
		return encoder;
	}
}
