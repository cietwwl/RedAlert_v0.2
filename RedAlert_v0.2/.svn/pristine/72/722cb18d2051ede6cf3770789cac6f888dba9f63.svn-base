package com.youxigu.dynasty2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties 加载工具
 * @author Dagangzi
 *
 */
public class PropertiesLoador {
	/**
	 * 绝对路径
	 * @param properties
	 * @param cmdFile
	 */
	public static Properties initCommandConfig(Properties properties, String fileName, boolean findPath) {
		// 初始化命令配置文件
		if (properties == null || properties.isEmpty()) {
			boolean initCommand = false;
			FileInputStream in = null;
			properties = new Properties();
			if (findPath) {
				String dir = System.getProperty("user.dir") + File.separator + "conf" + File.separator + fileName;
				try {
					in = new FileInputStream(dir);
					properties.load(in);
					initCommand = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (!initCommand) {
				//相对路径查找
				initCommandConfigByDefault(properties, fileName);
			}
		}
        return properties;
	}

	/**
	 * 相对路径
	 * @param properties
	 */
	public static void initCommandConfigByDefault(Properties properties, String fileName) {
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (in != null) {
				properties.load(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
