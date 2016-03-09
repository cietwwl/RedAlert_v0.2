package com.youxigu.wolf.net;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import com.manu.core.ServiceLocator;

public class MethodUtil {
	private static Logger logger = LoggerFactory.getLogger(MethodUtil.class);
	private static Map<String, Method> cache = new ConcurrentHashMap<String, Method>();

	public static Object call(String serviceName, String methodName,
			Object[] params) throws Exception {
		Object instance = null;
		try {
			instance = ServiceLocator.getSpringBean(serviceName);
		} catch (Exception e) {
			logger.error("error: ", e);
		}

		if (instance == null) {
			try {
				instance = Thread.currentThread().getContextClassLoader()
						.loadClass(serviceName).newInstance();
			} catch (Exception e) {
				logger.error("error: ", e);
			}
		}

		if (instance != null) {
			Method method = MethodUtil.getMethod(instance.getClass(),
					serviceName, methodName, params);

			if (method != null) {
				return method.invoke(instance, params);
			} else {
				throw new NoSuchMethodException(serviceName + "." + methodName);
			}

		} else {
			throw new NoSuchMethodException(serviceName + "." + methodName);
		}

	}

	// public static Method getMethod(Object instance, String serviceName,
	// String methodName, Object[] params) {
	// int paramCount = 0;
	// if (params != null)
	// paramCount = params.length;
	// String callKey = serviceName + "_" + methodName + "_" + paramCount;
	// Method method = cache.get(callKey);
	// if (method == null) {
	// if (params == null) {
	// try {
	// method = instance.getClass().getDeclaredMethod(methodName,
	// (Class[]) null);
	// cache.put(callKey, method);
	// } catch (NoSuchMethodException e) {
	// }
	// } else {
	// Class<?>[] paramz = new Class[paramCount];
	// for (int i = 0; i < paramCount; i++) {
	// if (params[i] == null) {
	// paramz[i] = Object.class;
	// } else {
	// paramz[i] = params[i].getClass();
	// }
	// }
	// try {
	// List possibleParamList = searchAllMatch(paramz);
	// for (Object param : possibleParamList) {
	// try {
	// method = instance.getClass().getDeclaredMethod(
	// methodName, (Class[]) param);
	// cache.put(callKey, method);
	// } catch (NoSuchMethodException e) {
	// continue;
	// }
	// break;
	// }
	//
	// } catch (Exception e) {
	// logger.error(e.toString(), e);
	// }
	// }
	// }
	// return method;
	// }

	public static Method getMethod(Class instClass, String serviceName,
			String methodName, Object[] params) {
		int argCount = 0;
		if (params != null)
			argCount = params.length;
		// TODO:if some method has same name and same count params,the key is
		// error
		String callKey = new StringBuilder().append(serviceName).append("_")
				.append(methodName).append("_").append(argCount).toString();
		Method method = cache.get(callKey);
		if (method == null) {
			if (params == null || params.length == 0) {
				try {
					method = instClass.getMethod(methodName, (Class[]) null);
				} catch (NoSuchMethodException e) {
				}
			} else {
				// Method[] candidates = instClass.getMethods();
				Method[] candidates = instClass.getDeclaredMethods();
				// First pass: look for method with directly assignable
				// arguments.
				for (int i = 0; i < candidates.length; i++) {
					if (candidates[i].getName().equals(methodName)) {
						// Check if the inspected method has the correct number
						// of
						// parameters.
						Class[] paramTypes = candidates[i].getParameterTypes();
						if (paramTypes.length == argCount) {
							int numberOfCorrectArguments = 0;
							for (int j = 0; j < argCount; j++) {
								if (ClassUtils.isAssignableValue(paramTypes[j],
										params[j])) {
									numberOfCorrectArguments++;
								} else {
									break;
								}
							}
							if (numberOfCorrectArguments == argCount) {
								method = candidates[i];
								break;
							}
						}
					}
				}
			}
			if (method != null) {
				cache.put(callKey, method);
			} else {
				logger.error("method not found:{}.{}", serviceName, method);
			}
		}
		return method;
	}

	public static void main(String[] args) {

		MethodUtil util = new MethodUtil();
		for (int i = 0; i < 10; i++) {
			long time = System.currentTimeMillis();
			Method method = MethodUtil.getMethod(util.getClass(), "MethodUtil",
					"getMethod", new Object[] { util, "asd", "sadas", null });
			// Method method = MethodUtil.getMethod(util, "MethodUtil",
			// "getMethod",
			// new Object[] { util, "asd", "sadas", null });

			System.out.println("time=" + (System.currentTimeMillis() - time));

			System.out.println(method);
		}
	}
}
