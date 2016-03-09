package com.youxigu.dynasty2.util;

import java.util.Calendar;

/**
 * 时间的工具类
 * 
 * 
 */
public class TimeUtils {
	/**
	 * 返回一个唯一标识当天的int值<br>
	 * YEAR << 9[月份最大不过16] + MONTH << 5[日期最大不过32] + DAY ;
	 * 
	 * @param now
	 * @return
	 */
	public static int getVersionOfToday(long now) {
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeInMillis(now);
		return calcBaseDayVersion(_calendar);
	}

	public static String resoveDailyVersion(int version) {
		int year = version >> 9;
		version = version ^ (year << 9);
		int month = version >> 5;
		int day = version ^ (month << 5);
		return "" + (year) + (month) + (day);
	}

	public static int calcMonthVersion(long now) {
		Calendar _cal = Calendar.getInstance();
		_cal.setTimeInMillis(now);
		_cal.set(Calendar.DAY_OF_MONTH, 1);
		return calcBaseDayVersion(_cal);

	}

	protected static int calcBaseDayVersion(Calendar calendar) {
		return (calendar.get(Calendar.YEAR) << 9)
				| ((calendar.get(Calendar.MONTH) + 1) << 5)
				| calendar.get(Calendar.DATE);
	}

	public static void main(String[] args) {
		System.out.println(getVersionOfToday(System.currentTimeMillis()));
	}
}
