import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

public class Protocol2Xml {
	private static SerializationContext context = new SerializationContext();
	private static XStream xstream;
	// 00 00 00 29 00 0A 0B 01 07 6E 75 6D 04 01 09 74 79 70 65 04 01 15 73 68
	// 6F 70 49 74 65 6D 49 64 04 C3 99 49 07 63 6D 64 04 85 BF 7E 01

	static {

		//context.legacyCollection = true;
		//context.legacyMap = true;

		// xstream = new XStream(new DomDriver("utf-8"));// dom
		xstream = new XStream(new DomDriver("utf-8") {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out, new String("  ")
						.toCharArray(), "\n", xmlFriendlyReplacer());
			}
		});// dom

		xstream.setMode(XStream.NO_REFERENCES);
		xstream.aliasSystemAttribute(null, "class");
		xstream.autodetectAnnotations(true);

	}

	public static void main(String[] args) {
		//String source = "00 00 00 29 00 0A 0B 01 07 6E 75 6D 04 01 09 74 79 70 65 04 01 15 73 68 6F 70 49 74 65 6D 49 64 04 C3 99 49 07 63 6D 64 04 85 BF 7E 01";
	    String source = "00 00 00 28 00 0A 43 01 07 6E 75 6D 07 63 6D 64 09 74 79 70 65 15 73 68 6F 70 49 74 65 6D 49 64 04 01 04 85 BF 7E 04 01 04 C3 99 49";					  

		String xml = toXML(source);
		System.out.println(xml);
		String hex = fromXML(xml);
		System.out.println(hex);		
	}

	public static String toXML(String para) {
		para.replace("\n", " ");
		para = para.trim();

		String[] tmpArr = para.split(" ");
		byte[] data = new byte[tmpArr.length];

		for (int i = 0; i < tmpArr.length; i++) {
			char[] charArr = tmpArr[i].toCharArray();
			data[i] = (byte) (((byte) charToByte(charArr[0])) << 4 | (byte) charToByte(charArr[1]));
		}

		boolean compressed = ((data[4] & 0x1) == 0x01);

		byte[] data2 = new byte[tmpArr.length - 5];
		System.arraycopy(data, 5, data2, 0, data2.length);
		try {
			Map obj = (Map) decode(data2, compressed);
			if (compressed) {
				obj.put("_compressed", true);
			}

			StringWriter sw = new StringWriter(512);
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(sw);
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				xstream.toXML(obj, bw);
				return sw.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (Exception e) {
					}
				}
			}

			// Object obj1 = xstream.fromXML(xml);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String fromXML(String xml) {
		Object obj = xstream.fromXML(xml);
		try{
			byte[] data =encode(obj);
			return byetArrayToHexString(data);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static Object decode(byte[] bytes, boolean compressed)
			throws Exception {

		Amf3Input amf3in = null;
		amf3in = new Amf3Input(context);
		if (compressed) {
			// TODO:这里用InflaterInputStream 还是Inflater更好呢?
			amf3in.setInputStream(new InflaterInputStream(
					new ByteArrayInputStream(bytes)));
		} else {
			amf3in.setInputStream(new ByteArrayInputStream(bytes));

		}
		Object message = amf3in.readObject();
		amf3in.close();
		return message;
	}

	public static byte[] encode(Object message) throws Exception {
		boolean compressed = false;
		if (message instanceof Map) {
			Object tmp = ((Map) message).remove("_compressed");
			if (tmp != null) {
				compressed = true;
			}

		}
		Amf3Output amf3out = null;
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			amf3out = new Amf3Output(context);
			amf3out.setOutputStream(bo);
			amf3out.writeObject(message);
			amf3out.flush();
			byte[] data = bo.toByteArray();
			byte flag = 0;
			if (compressed) {
				flag = (byte) (flag | 0x01);
				data = compress(data);
			}
			int len = data.length;
			byte[] result = new byte[len + 5];
			System.arraycopy(data, 0, result, 5, len);
			result[4] = flag;
			byte[] head4 = intToByteArray(len + 1);
			System.arraycopy(head4, 0, result, 0, 4);

			return result;
		} finally {
			if (amf3out != null) {
				amf3out.close();
			}
		}
	}

	private static byte[] compress(byte[] inputs) {
		// 同一个session使用同一个压缩对象,减少Deflater的构造次数，必须做同步,
		// byte outputs[] = null;
		// int total = 0;
		Deflater deflater = new Deflater();
		deflater.setInput(inputs, 0, inputs.length);
		deflater.finish();
		try {
			byte[] bytes = new byte[inputs.length];
			int value = deflater.deflate(bytes, 0, inputs.length);

			byte[] bytes1 = new byte[value];
			System.arraycopy(bytes, 0, bytes1, 0, value);
			return bytes1;
		} finally {
			deflater.reset();
			deflater.end();
		}

	}

	private static byte charToByte(char c) {
		return (byte) ("0123456789ABCDEF".indexOf(c));
	}

	public static final String byteToHex(byte b) {
		  return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xf));
		 }
	
	private static byte[] intToByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer
				: integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}

	private static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	private static String byetArrayToHexString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(byteToHex(b[i])).append(" ");
		}
		return sb.toString();
	}
}
