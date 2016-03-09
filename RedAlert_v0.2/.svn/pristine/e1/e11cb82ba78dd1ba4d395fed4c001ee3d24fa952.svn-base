package com.youxigu.wolf.net.codec;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 
 * @author Administrator
 *buffer由外部传入，这个类不负责buffer的清理
 */
public class IoBufferOutputStream extends OutputStream{

	private IoBuffer buffer;
	
//	public IoBufferOutputStream(int capacity, boolean direct){
//		buffer = IoBuffer.allocate(capacity, direct);
//	}

	public IoBufferOutputStream(IoBuffer buffer){
		this.buffer = buffer;
		//this.buffer.setAutoExpand(true);
	}

	@Override
	public void write(int b) throws IOException {
		buffer.put((byte)b);
		
	}
	@Override
	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}
	 
	@Override	
	public void write(byte[] b, int off, int len) throws IOException {
		buffer.put(b, off, len);
	}
	
	//TODO: other write method
}
