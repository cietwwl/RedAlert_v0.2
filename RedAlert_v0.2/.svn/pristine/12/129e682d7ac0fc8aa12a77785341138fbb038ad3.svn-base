package com.youxigu.wolf.net.codec;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.google.protobuf.Message;
import com.youxigu.dynasty2.util.PropertiesLoador;

/**
 * 
 * 9个字节协议头+协议体.
 *
 * 协议头1-4字节表示协议长度 =协议体长度+9-4（去掉长度占的4字节）
 * 
 * 协议头第5字节为标志字节：
 *------------------------
 * 该字节的最低位为压缩位：0=协议体未压缩 1=协议体已经压缩，
 * 该字节的低2-4位为协议位：
 * 000=基于AMF3的协议，
 * 001=基于java serial协议
 * 010=基于protobuf协议
 * 5-8位未用，作为以后扩展
 * ------------------------
 *
 * 6-9字节表示命令号
 *
 * 采用网络字节序的整数(高位在前，低位在后)
 * @author Administrator
 *
 */
public class NewMutilCodecFactory implements ProtocolCodecFactory {

	private NewMutliEncoder encoder;
	private NewMutilDecoder decoder;

	//双向cache
	private static Map<Integer, Class<? extends Message>> messages;
	private static Map<Class<? extends Message>, Integer> commands = new HashMap<Class<? extends Message>, Integer>();

	private static Map<Integer, Method> methods = new HashMap<Integer, Method>();
	private final static String conf = "cmd2proto.properties";
	private static Properties properties;

	//是否将protobuf转换成Map
	//	private boolean toMap = true;

	private void parseMessage(boolean findPath) {
		if (messages == null) {
			properties = PropertiesLoador.initCommandConfig(properties, conf, findPath);

			if (properties != null && messages == null) {
				messages = new HashMap<Integer, Class<? extends Message>>();

				Iterator it = properties.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					try {
						Class<Message> msgClass = (Class<Message>) Class.forName((String) value);
						int cmd = Integer.parseInt(key.toString());
						commands.put(msgClass, cmd);
						messages.put(cmd, msgClass);
						Class[] paramsTypes = { byte[].class };
						Method method = msgClass.getMethod("parseFrom", paramsTypes);
						this.methods.put(cmd, method);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public NewMutilCodecFactory() {
		this(false);
	}

	public NewMutilCodecFactory(boolean findPath) {
		if (encoder == null) {
			encoder = new NewMutliEncoder(this);
		}

		if (decoder == null) {
			decoder = new NewMutilDecoder(this);
		}

		if (messages == null) {
			this.parseMessage(findPath);
		}
	}

	public void setCompressLen(int compressLen) {
		encoder.setCompressLen(compressLen);
	}

	public Method getMessageDecodeMethod(int cmd) {

		return methods.get(cmd);
	}

	public Class<? extends Message> getMessageClass(int cmd) {

		return messages.get(cmd);
	}

	public int getMessageCommand(Class msgClass) {
		if (commands.containsKey(msgClass)) {
			return commands.get(msgClass);
		} else {
			return -1;
		}
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return encoder;
	}
}
