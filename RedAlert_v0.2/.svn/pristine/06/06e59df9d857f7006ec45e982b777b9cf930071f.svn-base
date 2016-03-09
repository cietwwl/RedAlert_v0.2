package com.youxigu.dynasty2.util;

import java.util.HashSet;
import java.util.Random;

import com.manu.util.Util;

public class MathUtils {

	public static final int PROBABILITY = 1000;// 定义一个 千分比

	/**
	 * 判断千分比 是否中
	 * 
	 * @param src
	 * @return
	 */
	public static boolean randomProbability(int src) {
		int v = getRandom(PROBABILITY);
		if (v < src) {
			return true;
		}
		return false;
	}

	/**
	 * 取得指定的两个数之间的最大值
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int getMax(int a, int b) {
		return Math.max(a, b);
	}

	/**
	 * 取得指定的两个数之间的最小值
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int getMin(int a, int b) {
		return Math.min(a, b);
	}

	/**
	 * 取指定的3个数中>最小<最大的拿的数
	 * 
	 * @param produce
	 *            任意值
	 * @param max
	 *            最大
	 * @param min
	 *            最小
	 * @return
	 */
	public static int getBetweenMaxAndMin(int produce, int max, int min) {
		return Math.max(min, Math.min(produce, max));
	}

	private static Random random = new Random();

	static {
		random.setSeed(System.currentTimeMillis());
	}

	/**
	 * 随机一个 [0, max-1]的数
	 * 
	 * @param max
	 * @return
	 */
	public static int getRandom(int max) {
		return Math.abs(random.nextInt()) % max;
	}

	/**
	 * 在最大和最小值之间随机一个整数[min,max)不包含最大值
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomExMax(int min, int max) {
		if (min >= max) {
			return min;
		}
		return Math.abs(random.nextInt()) % (max - min) + min;
	}

	/**
	 * 在最大和最小值之间随机一个整数[min,max]包含最大值
	 * 
	 * @param max
	 * @param min
	 * @return
	 */
	public static int getRandomInMax(int min, int max) {
		if (min >= max) {
			return min;
		}
		return max > min ? Util.randInt(max - min + 1) + min : min;
	}

	/**
	 * return a random value in [0, range-1]
	 * 
	 * @param array
	 *            [n][m] n:对象id 例如某个道具的id, m:出现的几率 例如15% 1.5% 0.15%
	 * @param range
	 *            几率范围 例如100,1000,10000
	 * @return 返回对象id
	 */
	public static int getRandom(int[][] array, int range) {
		int rand = MathUtils.getRandom(range);
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i][1];
			if (rand < sum) {
				return array[i][0];
			}
		}
		return 0;
	}

	public static long getLong(Object obj) {
		long retu = 0;
		if (obj != null) {
			if (obj instanceof Double) {
				retu = ((Double) obj).longValue();
			} else if (obj instanceof Integer) {
				retu = ((Integer) obj).longValue();
			} else if (obj instanceof Long) {
				retu = (Long) obj;
			} else {
				retu = Long.parseLong(obj.toString());
			}
		}
		return retu;
	}

	public static int getInt(Object obj) {
		int retu = 0;
		if (obj != null) {
			if (obj instanceof Integer) {
				retu = ((Integer) obj).intValue();
			} else if (obj instanceof Double) {
				retu = ((Double) obj).intValue();
			} else if (obj instanceof Long) {
				retu = ((Long) obj).intValue();
			} else {
				retu = Integer.parseInt(obj.toString());
			}
		}
		return retu;
	}

	public static int[] getIntArray(Object[] params) {
		if (params == null) {
			return null;
		}
		int[] arr = new int[params.length];
		for (int i = 0; i < params.length; i++) {
			Object tmp = params[i];
			if (tmp instanceof Integer) {
				arr[i] = (Integer) tmp;
			} else {
				arr[i] = Integer.parseInt(tmp.toString());
			}
		}
		return arr;
	}

	public static String[] getStringArray(Object[] params) {
		if (params == null) {
			return null;
		}
		String[] arr = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			Object tmp = params[i];
			if (tmp instanceof String) {
				arr[i] = (String) tmp;
			} else {
				arr[i] = String.valueOf(tmp.toString());
			}
		}
		return arr;
	}

	/**
	 * 从数组中随机出一个
	 * 
	 * @param arr
	 * @param max
	 * 
	 * @return
	 */
	public static int randomOneFromArray(int[] arr, int max) {
		int l = arr.length;
		if (max == 0) {// 如果没有计算总值，这里进行计算
			for (int i = 0; i < l; i++) {
				max += arr[i];
			}
		}
		int ram = MathUtils.getRandom(max + 1);
		int temp = 0;
		int index = 0;
		for (int i = 0; i < l; i++) {
			temp += arr[i];
			if (temp > ram) {
				index = i;
				break;
			}

		}
		return index;
	}

	/**
	 * 从一个数组中随机出 m 个不重复索引
	 * 
	 * @param arr
	 * @param m
	 * @param max
	 *            数组中最大值，0需要程序算,(随机时可以达到max)
	 * @return
	 */
	public static HashSet<Integer> randomNoRepeatArray(int[] arr, int m, int max) {
		int l = arr.length;
		if (m > l) {
			m = l;
		}
		if (max == 0) {// 如果没有计算总值，这里进行计算
			for (int i = 0; i < l; i++) {
				max += arr[i];
			}
		}
		HashSet<Integer> hs = new HashSet<Integer>(m);
		while (true) {
			int ram = MathUtils.getRandom(max + 1);
			int temp = 0;
			int index = 0;
			for (int i = 0; i < l; i++) {
				temp += arr[i];
				if (temp > ram) {
					index = i;
					break;
				}

			}
			hs.add(index);
			if (hs.size() == m) {
				break;
			}
		}
		return hs;
	}

	/**
	 * 从一个数组中随机出 m 个可重复索引
	 * 
	 * @param arr
	 * @param m
	 * @param max
	 *            数组中最大值，0需要程序算,(随机时可以达到max)
	 * @return
	 */
	public static int[] randomRepeatArray(int[] arr, int m, int max) {
		int l = arr.length;
		if (m > l) {
			m = l;
		}
		if (max == 0) {// 如果没有计算总值，这里进行计算
			for (int i = 0; i < l; i++) {
				max += arr[i];
			}
		}
		int[] hs = new int[m];
		int j = 0;
		while (true) {
			int ram = MathUtils.getRandom(max + 1);
			int temp = 0;
			int index = 0;
			for (int i = 0; i < l; i++) {
				temp += arr[i];
				if (temp > ram) {
					index = i;
					break;
				}

			}
			hs[j] = index;
			if (++j == m) {
				break;
			}
		}
		return hs;
	}
}
