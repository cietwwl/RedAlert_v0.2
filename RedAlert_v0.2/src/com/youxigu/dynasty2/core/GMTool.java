package com.youxigu.dynasty2.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;

public class GMTool {
	private static DateFormat DF_LONG = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private static DateFormat DF_SHORT = new SimpleDateFormat("yyyy-MM-dd");
	private static Properties properties;
	private static Gson gson = new Gson();
	
	public static final String SERVICENAME=".serviceName";  
	public static final String METHODNAME=".methodName";
	public static final String PARAMTYPE=".paramType";	
	
	// private static JsonGenerator jsonGenerator = null;
	// private static ObjectMapper objectMapper;

	public static void main(String[] args) {
		// Gson gson = new
		// GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

		System.out.println("usage GMTool -h127.0.1.133 -P8739 -uroot -proot");
		if (args.length != 4) {
			System.out.println("参数错误");
			System.exit(0);
		}

		String ip = null;
		int port = 0;
		String userName = null;
		String password = null;
		String cmdFile = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-h")) {
				ip = args[i].substring(2);
			} else if (args[i].startsWith("-P")) {
				port = Integer.parseInt(args[i].substring(2));
			} else if (args[i].startsWith("-u")) {
				userName = args[i].substring(2);
			} else if (args[i].startsWith("-p")) {
				password = args[i].substring(2);
			} else if (args[i].startsWith("-f")) {
				cmdFile = args[i].substring(2);
			}
		}

		if (ip == null) {
			System.out.println("必须提供ip参数 : -h");
			System.exit(0);
		}
		if (port == 0) {
			System.out.println("必须提供port参数 : -P");
			System.exit(0);
		}
		if (userName == null) {
			System.out.println("必须提供userName参数 : -u");
			System.exit(0);
		}
		if (password == null) {
			System.out.println("必须提供password参数 : -p");
			System.exit(0);
		}

		initCommandConfig(cmdFile);

		GMSocketClient client = new GMSocketClient(ip, port, userName, password);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		while (true) {
			try {
				String command = reader.readLine();
				command = command.trim();
				if ("quit".equalsIgnoreCase(command)) {
					client.close();
					System.exit(0);
				} else {
					GMCommand gmCommand = parseCommand(command);
					if (gmCommand != null) {
						try {
							// long time = System.currentTimeMillis();
							// for (int i=0;i<10000;i++){
							Object obj = client.sendTask(gmCommand
									.getServiceName(), gmCommand
									.getMethodName(), gmCommand.getParams());
							System.out.println("OK," + toJsonString(obj));
							// }
							// System.out.println("time1="+(System.currentTimeMillis()-time));

						} catch (Exception e) {
							System.err.println("ERR," + e.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static GMCommand parseCommand(String commandStr) {
		if (commandStr == null) {
			return null;
		}
		commandStr = commandStr.trim();
		if (commandStr.length() == 0) {
			return null;
		}
		String[] tmps = commandStr.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		// String[] tmps = commandStr.split("");
		List<String> paramValues = new ArrayList<String>();
		String cmd = null;
		for (String value : tmps) {
			value = value.trim();
			if (cmd == null) {
				cmd = value;
			} else {
				if (value.startsWith("\"") && value.endsWith("\"")) {
					value = value.substring(1);
					value = value.substring(0, value.length() - 1);
				}
				paramValues.add(value);
			}
		}

		if (cmd == null) {
			System.err.println("错误的命令：" + commandStr);
			return null;
		}
		String serviceName = properties.getProperty(cmd + SERVICENAME);
		String methodName = properties.getProperty(cmd + METHODNAME);
		if (serviceName == null || methodName == null) {
			System.err.println("错误的命令：" + commandStr);
			return null;
		}

		String paramTypes = properties.getProperty(cmd + PARAMTYPE);

		Object[] paramObjectArry = null;
		if (paramTypes != null) {
			paramTypes = paramTypes.trim();
			if (!"".equals(paramTypes)) {
				String[] paramTypeArr = paramTypes.split(",");
				if (paramTypeArr.length != paramValues.size()) {
					System.err.println("错误的命令参数，需要" + paramTypeArr.length
							+ "个参数");
					return null;
				}
				paramObjectArry = new Object[paramValues.size()];
				for (int i = 0; i < paramObjectArry.length; i++) {
					try {
						paramObjectArry[i] = parseParam(paramTypeArr[i],
								paramValues.get(i));
					} catch (Exception e) {
						System.err.println("参数" + i + "错误，应该是"
								+ paramTypeArr[i] + "类型");
						return null;
					}
				}

			}
		}

		GMCommand command = new GMCommand();
		command.setServiceName(serviceName);
		command.setMethodName(methodName);
		command.setParams(paramObjectArry);
		return command;
	}

	public static Object parseParam(String typeStr, String valueStr)
			throws Exception {
		typeStr = typeStr.trim();
		valueStr = valueStr.trim();
		Object value = null;
		if ("byte".equalsIgnoreCase(typeStr)) {
			value = Byte.parseByte(valueStr);
		} else if ("short".equalsIgnoreCase(typeStr)) {
			value = Short.parseShort(valueStr);
		} else if ("int".equalsIgnoreCase(typeStr)) {
			value = Integer.parseInt(valueStr);
		} else if ("long".equalsIgnoreCase(typeStr)) {
			value = Long.parseLong(valueStr);
		} else if ("boolean".equalsIgnoreCase(typeStr)) {
			value = Boolean.parseBoolean(valueStr);
		} else if ("double".equalsIgnoreCase(typeStr)) {
			value = Double.parseDouble(valueStr);
		} else if ("date".equalsIgnoreCase(typeStr)
				|| "timestamp".equalsIgnoreCase(typeStr)) {
			try {
				value = DF_LONG.parse(valueStr);
			} catch (Exception e) {
				value = DF_SHORT.parse(valueStr);
			}
			if ("timestamp".equalsIgnoreCase(typeStr)) {
				value = new Timestamp(((Date) value).getTime());
			}
		} else {
			value = valueStr;
		}

		return value;
	}

	public static void initCommandConfig(String cmdFile) {
		// 初始化命令配置文件
		if (properties == null) {
			boolean initCommand = false;
			properties = new Properties();
			if (cmdFile != null) {
				Reader reader = null;
				File file = new File(cmdFile);
				if (file.exists() && file.isFile()) {
					try {
						reader = new InputStreamReader(
								new FileInputStream(file));
						properties.load(reader);
						initCommand = true;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (Exception e) {
							}
						}
					}
				}
			}
			if (!initCommand) {
				initCommandConfigByDefault();
			}
		}

	}

	public static void initCommandConfigByDefault() {

		try {
			properties.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("command.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		String str = null;
		// 这里没有用jackson,因为jackson采用javaBean模式,不忽略transient 属性
		if (gson != null) {
			try {
				str = gson.toJson(obj);
			} catch (Exception e) {
				e.printStackTrace();
				str = obj.toString();
			}
		} else {
			str = obj.toString();
		}
		return str;

	}
	
	public static String get(String key){
		return properties.getProperty(key);
	}
}
