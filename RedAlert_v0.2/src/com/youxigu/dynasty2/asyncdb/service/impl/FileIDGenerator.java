package com.youxigu.dynasty2.asyncdb.service.impl;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.youxigu.dynasty2.asyncdb.service.IIDGenerator;

/**
 * 基于文件持久化的ID生成器
 * @author Administrator
 *
 */
public class FileIDGenerator implements IIDGenerator {
	public static Logger log = LoggerFactory.getLogger(FileIDGenerator.class);
	
	public static final String TYPE_DEFAULT="DEF";
	private static final String PERSISTENCE_PATH = "ID";

	private String path;

	/**
	 * 按类型缓存的ID的当前值
	 */
	private Map<String, FileID> idCaches;

	public void setPath(String path) {
		this.path = path;
	}

	public void setIdCaches(Map<String, FileID> idCaches) {
		this.idCaches = idCaches;
	}

	public void init() {
		if (path == null) {
			path = System.getProperty("user.dir") + File.separator
					+ PERSISTENCE_PATH + File.separator;
		}

		if (!this.path.endsWith(File.separator)) {
			this.path = this.path + File.separator;
		}
		
		File tmp = new File(path);
		if (!tmp.exists()) {
			tmp.mkdir();
		} else if (!tmp.isDirectory()) {
			tmp.delete();
			tmp.mkdir();
		}
		
		

		if (idCaches == null) {
			idCaches = new HashMap<String, FileID>();
		}

		Iterator<Entry<String, FileID>> lit = idCaches.entrySet().iterator();
		while (lit.hasNext()) {
			Entry<String, FileID> entry = lit.next();
			FileID id = entry.getValue();
			id.setCurrent(id.getInitValue());
			id.setFile(new File(this.path + entry.getKey() + ".id"));
		}
	}


	@Override
	public long getID() {
		return getID(TYPE_DEFAULT);
	}

	@Override
	public long getID(String type) {
		if (type==null || type.length()==0)
			type=TYPE_DEFAULT;
		
		FileID id = idCaches.get(type);
		if (id==null){
			synchronized (idCaches){
				id = idCaches.get(type);
				if (id==null){
					id = new FileID();
					id.setCurrent(id.getInitValue());
					id.setFile(new File(this.path + type + ".id"));
					id.loadIDFromFile();
					idCaches.put(type, id);
				}
			}
			
		}
		
		return id.get();
	}


	
	
	public static void main(String[] args){
		final FileIDGenerator g = new FileIDGenerator();
		g.init();
		//g.getID(null);
		final long time = System.currentTimeMillis();
		for (int i=0;i<100000;i++){
			g.getID(null);
			//'System.out.println(g.getID(null));
			//						
		}
		System.out.println("time ======"+(System.currentTimeMillis()-time));	
		System.out.println("time ======");
		for (int i=0;i<200;i++){
			

			new Thread(new Runnable(){

				@Override
				public void run() {
					for (int i=0;i<10000;i++){
						g.getID(null);
						//'System.out.println(g.getID(null));
						//						
					}
					System.out.println("time ======"+(System.currentTimeMillis()-time));					
					
				}}).start();
			
		}

		System.out.println("time ======");
	}
}
