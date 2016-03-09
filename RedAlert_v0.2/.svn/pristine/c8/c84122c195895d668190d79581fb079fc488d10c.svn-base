package com.youxigu.dynasty2.asyncdb.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileID {
	public static Logger log = LoggerFactory.getLogger(FileID.class);
	private long initValue = 1;// 初始值

	/**
	 * 缓存的id个数，设置这个值避免每次都写文件
	 * 带来的问题是，服务器重启将导致断号，每次重启都会中断0-cacheNum个号
	 */
	private int cacheNum = 100;
	


	// 对应的存储文件
	private File file;

	// 计数器
	private long current=initValue;

	/**
	 * 计数器当前可增长到的最大值，超过此值则需要更新计数器文件
	 */
	private long cachedMax = current + cacheNum;

	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getInitValue() {
		return initValue;
	}

	public void setInitValue(long initValue) {
		this.initValue = initValue;
	}


	public int getCacheNum() {
		return cacheNum;
	}

	public void setCacheNum(int cacheNum) {
		this.cacheNum = cacheNum;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
		this.cachedMax  = current;//this.current+ cacheNum;
	}



	public synchronized long get() {
		 if (this.current==this.cachedMax){
			 long max = this.cachedMax+this.cacheNum;
			 try{
				 writeID(max);
			 }
			 catch (Exception e){
				 log.error("ID生成异常",e);
				 return -1;
			 }
			 this.cachedMax = max;
		 }
		 return this.current++;
	}

	public void writeID(long max) throws IOException {
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(this.getFile()));
			bw.write(max+"\n");
			bw.flush();
		}

		finally{
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
				}
			}
		}
		
	}
	

	public void loadIDFromFile() {
		if (this.getFile().exists()) {
			// 读入之前缓存的已经分配的ID值
			BufferedReader is = null;
			try {
				is = new BufferedReader(new FileReader(this.getFile()));
				String tmp = is.readLine();
				long current = Long.parseLong(tmp);
				this.setCurrent(current);
				
			} catch (Exception e) {
				log.error("ID生成异常",e);				
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
				}
			}
		}
		
		//writeID();

	}
}
