package com.youxigu.dynasty2.core.flex.amf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import net.spy.memcached.CachedData;
import net.spy.memcached.compat.CloseUtil;

import com.ibatis.sqlmap.engine.cache.memcached.SerializingTranscoderEx;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;
@Deprecated
public class SerializingTranscoderAmf3 extends SerializingTranscoderEx{
	private static SerializationContext context = new SerializationContext();

	static {
		context.legacyCollection = true;
		context.legacyMap = true;
	}

	
	protected CachedData serializeAndCompressEx(Object o, int flags) {
		if (o == null) {
			throw new NullPointerException("Can't serialize null");
		}
		
		byte[] rv = null;
		byte[] inBytes = null;
		int byteLen = 0;
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream(64);
			Amf3Output os = new Amf3Output(context);
			os.setOutputStream(bos);
			os.writeObject(o);
			os.close();
			bos.close();

			byteLen = bos.size();
			// rv=bos.toByteArray();
			if (statistic){
				doStatic(byteLen);
			}

			if (byteLen > compressionThreshold) {
				inBytes = (byte[]) bsBufField.get(bos);
				flags |= COMPRESSED;
				bos = new ByteArrayOutputStream(1024);
				DeflaterOutputStream def = null;
				// deflater.finish();
				try {
					def = new DeflaterOutputStream(bos);
					def.write(inBytes, 0, byteLen);
				} catch (IOException e) {
					throw new RuntimeException("IO exception compressing data",
							e);
				} finally {
					CloseUtil.close(def);
					CloseUtil.close(bos);
				}
				rv = bos.toByteArray();
				if (log.isDebugEnabled()) {
					log.debug("Compressed " + o.getClass().getName() + " from "
							+ byteLen + " to " + rv.length);
				}

			} else {
				rv = bos.toByteArray();
				if (log.isDebugEnabled() && rv.length > 10240) {
					log.debug("Serial " + o.getClass().getName() + " to "
							+ rv.length);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("serializeAndCompressEx error",
					e);
		}
		return new CachedData(flags, rv, getMaxSize());
	}

	protected Object decompressAndDeserialize(byte[] in) {
		
		Amf3Input ois = null;
		Object rv = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(in);
		try {
			ois = new Amf3Input(context);
			ois.setInputStream(new InflaterInputStream(bis));
			rv = ois.readObject();

		} catch (Exception e) {
			log.error("Failed to decompress data", e);
		} finally {
			if (ois != null) {
				try {
					bis.close();
					ois.close();
				} catch (Exception e) {
				}
			}
		}

		return rv;
	}
	
	protected Object deserialize(byte[] in) {
		Object rv=null;
		ByteArrayInputStream bis = null;
		Amf3Input ois = null;
		try {
			if(in != null) {
				bis=new ByteArrayInputStream(in);
				ois = new Amf3Input(context);
				ois.setInputStream(bis);
				rv=ois.readObject();
			}
		} catch(IOException e) {
			getLogger().warn("Caught IOException decoding %d bytes of data",
					in == null ? 0 : in.length, e);
		} catch (ClassNotFoundException e) {
			getLogger().warn("Caught CNFE decoding %d bytes of data",
					in == null ? 0 : in.length, e);
		} finally {
			if (ois != null) {
				try {
					bis.close();
					ois.close();
				} catch (Exception e) {
				}
			}
		}
		return rv;
	}
}
