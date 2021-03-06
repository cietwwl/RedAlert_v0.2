package com.youxigu.dynasty2.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static final String DATETIME_DATE = "yyyy-MM-dd";

	public static final String DATETIME_TIME = "yyyy-MM-dd HH:mm:ss";

	/**
	 * hours between first and second timestamp
	 * 
	 * @param bigDate
	 * @param smallDate
	 * @return
	 */
	public static double timeDistanceByHour(Timestamp bigDate,
			Timestamp smallDate) {
		double ldDis = 0;
		if (bigDate == null || smallDate == null)
			return ldDis;
		java.util.Calendar lCal = java.util.Calendar.getInstance();
		java.util.Calendar lCal01 = java.util.Calendar.getInstance();
		lCal01.setTime(bigDate);
		lCal.setTime(smallDate);
		double lll = lCal01.getTime().getTime() - lCal.getTime().getTime();
		ldDis = lll / 1000 / 60 / 60;
		return ldDis;
	}

	/**
	 * Return a Timestamp for right now
	 * 
	 * @return Timestamp for right now
	 */
	public static java.sql.Timestamp nowTimestamp() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * Return a Date for right now
	 * 
	 * @return Date for right now
	 */
	public static java.util.Date nowDate() {
		return new java.util.Date();
	}

	public static java.sql.Timestamp getDayStart(java.sql.Timestamp stamp) {
		return getDayStart(stamp, 0);
	}

	public static java.sql.Timestamp getDayStart(java.sql.Timestamp stamp,
			int daysLater) {
		Calendar tempCal = Calendar.getInstance();

		tempCal.setTime(new java.util.Date(stamp.getTime()));
		tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH),
				tempCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
		return new java.sql.Timestamp(tempCal.getTime().getTime());
	}

	public static java.sql.Timestamp getNextDayStart(java.sql.Timestamp stamp) {
		return getDayStart(stamp, 1);
	}

	public static java.sql.Timestamp getDayEnd(java.sql.Timestamp stamp) {
		return getDayEnd(stamp, 0);
	}

	public static java.sql.Timestamp getDayEnd(java.sql.Timestamp stamp,
			int daysLater) {
		Calendar tempCal = Calendar.getInstance();

		tempCal.setTime(new java.util.Date(stamp.getTime()));
		tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH),
				tempCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
		return new java.sql.Timestamp(tempCal.getTime().getTime());
	}

	/**
	 * Converts a date String into a java.sql.Date
	 * 
	 * @param date
	 *            The date String: MM/DD/YYYY
	 * @return A java.sql.Date made from the date String
	 */
	public static java.sql.Date toSqlDate(String date) {
		java.util.Date newDate = toDate(date, "00:00:00");

		if (newDate != null)
			return new java.sql.Date(newDate.getTime());
		else
			return null;
	}

	/**
	 * Makes a java.sql.Date from separate Strings for month, day, year
	 * 
	 * @param monthStr
	 *            The month String
	 * @param dayStr
	 *            The day String
	 * @param yearStr
	 *            The year String
	 * @return A java.sql.Date made from separate Strings for month, day, year
	 */
	public static java.sql.Date toSqlDate(String monthStr, String dayStr,
			String yearStr) {
		java.util.Date newDate = toDate(monthStr, dayStr, yearStr, "0", "0",
				"0");

		if (newDate != null)
			return new java.sql.Date(newDate.getTime());
		else
			return null;
	}

	/**
	 * Makes a java.sql.Date from separate ints for month, day, year
	 * 
	 * @param month
	 *            The month int
	 * @param day
	 *            The day int
	 * @param year
	 *            The year int
	 * @return A java.sql.Date made from separate ints for month, day, year
	 */
	public static java.sql.Date toSqlDate(int month, int day, int year) {
		java.util.Date newDate = toDate(month, day, year, 0, 0, 0);

		if (newDate != null)
			return new java.sql.Date(newDate.getTime());
		else
			return null;
	}

	/**
	 * Converts a time String into a java.sql.Time
	 * 
	 * @param time
	 *            The time String: either HH:MM or HH:MM:SS
	 * @return A java.sql.Time made from the time String
	 */
	public static java.sql.Time toSqlTime(String time) {
		java.util.Date newDate = toDate("1/1/1970", time);

		if (newDate != null)
			return new java.sql.Time(newDate.getTime());
		else
			return null;
	}

	/**
	 * Makes a java.sql.Time from separate Strings for hour, minute, and second.
	 * 
	 * @param hourStr
	 *            The hour String
	 * @param minuteStr
	 *            The minute String
	 * @param secondStr
	 *            The second String
	 * @return A java.sql.Time made from separate Strings for hour, minute, and
	 *         second.
	 */
	public static java.sql.Time toSqlTime(String hourStr, String minuteStr,
			String secondStr) {
		java.util.Date newDate = toDate("0", "0", "0", hourStr, minuteStr,
				secondStr);

		if (newDate != null)
			return new java.sql.Time(newDate.getTime());
		else
			return null;
	}

	/**
	 * Makes a java.sql.Time from separate ints for hour, minute, and second.
	 * 
	 * @param hour
	 *            The hour int
	 * @param minute
	 *            The minute int
	 * @param second
	 *            The second int
	 * @return A java.sql.Time made from separate ints for hour, minute, and
	 *         second.
	 */
	public static java.sql.Time toSqlTime(int hour, int minute, int second) {
		java.util.Date newDate = toDate(0, 0, 0, hour, minute, second);

		if (newDate != null)
			return new java.sql.Time(newDate.getTime());
		else
			return null;
	}

	/**
	 * Converts a date and time String into a Timestamp
	 * 
	 * @param dateTime
	 *            A combined data and time string in the format "MM/DD/YYYY
	 *            HH:MM:SS", the seconds are optional
	 * @return The corresponding Timestamp
	 */
	public static java.sql.Timestamp toTimestamp(String dateTime) {
		java.util.Date newDate = toDate(dateTime);

		if (newDate != null)
			return new java.sql.Timestamp(newDate.getTime());
		else
			return null;
	}

	/**
	 * Converts a date String and a time String into a Timestamp
	 * 
	 * @param date
	 *            The date String: MM/DD/YYYY
	 * @param time
	 *            The time String: either HH:MM or HH:MM:SS
	 * @return A Timestamp made from the date and time Strings
	 */
	public static java.sql.Timestamp toTimestamp(String date, String time) {
		java.util.Date newDate = toDate(date, time);

		if (newDate != null)
			return new java.sql.Timestamp(newDate.getTime());
		else
			return null;
	}

	/**
	 * Makes a Timestamp from separate Strings for month, day, year, hour,
	 * minute, and second.
	 * 
	 * @param monthStr
	 *            The month String
	 * @param dayStr
	 *            The day String
	 * @param yearStr
	 *            The year String
	 * @param hourStr
	 *            The hour String
	 * @param minuteStr
	 *            The minute String
	 * @param secondStr
	 *            The second String
	 * @return A Timestamp made from separate Strings for month, day, year,
	 *         hour, minute, and second.
	 */
	public static java.sql.Timestamp toTimestamp(String monthStr,
			String dayStr, String yearStr, String hourStr, String minuteStr,
			String secondStr) {
		java.util.Date newDate = toDate(monthStr, dayStr, yearStr, hourStr,
				minuteStr, secondStr);

		if (newDate != null)
			return new java.sql.Timestamp(newDate.getTime());
		else
			return null;
	}

	/**
	 * Makes a Timestamp from separate ints for month, day, year, hour, minute,
	 * and second.
	 * 
	 * @param month
	 *            The month int
	 * @param day
	 *            The day int
	 * @param year
	 *            The year int
	 * @param hour
	 *            The hour int
	 * @param minute
	 *            The minute int
	 * @param second
	 *            The second int
	 * @return A Timestamp made from separate ints for month, day, year, hour,
	 *         minute, and second.
	 */
	public static java.sql.Timestamp toTimestamp(int month, int day, int year,
			int hour, int minute, int second) {
		java.util.Date newDate = toDate(month, day, year, hour, minute, second);

		if (newDate != null)
			return new java.sql.Timestamp(newDate.getTime());
		else
			return null;
	}

	/**
	 * Converts a date and time String into a Date
	 * 
	 * @param dateTime
	 *            A combined data and time string in the format "MM/DD/YYYY
	 *            HH:MM:SS", the seconds are optional
	 * @return The corresponding Date
	 */
	public static java.util.Date toDate(String dateTime) {
		// dateTime must have one space between the date and time...
		String date = dateTime.substring(0, dateTime.indexOf(" "));
		String time = dateTime.substring(dateTime.indexOf(" ") + 1);

		return toDate(date, time);
	}

	public static java.util.Date toUtilDate(String dateTime, String inStr) {
		// dateTime must have one space between the date and time...
		String date = dateTime.substring(0, dateTime.indexOf(" "));
		String time = dateTime.substring(dateTime.indexOf(" ") + 1);

		return toDate(date, time, inStr);
	}

	/**
	 * Converts a date String and a time String into a Date
	 * 
	 * @param date
	 *            The date String: MM/DD/YYYY
	 * @param time
	 *            The time String: either HH:MM or HH:MM:SS
	 * @return A Date made from the date and time Strings
	 */
	public static java.util.Date toDate(String date, String time) {
		if (date == null || time == null)
			return null;
		String month;
		String day;
		String year;
		String hour;
		String minute;
		String second;

		int dateSlash1 = date.indexOf("/");
		int dateSlash2 = date.lastIndexOf("/");

		if (dateSlash1 <= 0 || dateSlash1 == dateSlash2)
			return null;
		int timeColon1 = time.indexOf(":");
		int timeColon2 = time.lastIndexOf(":");

		if (timeColon1 <= 0)
			return null;
		month = date.substring(0, dateSlash1);
		day = date.substring(dateSlash1 + 1, dateSlash2);
		year = date.substring(dateSlash2 + 1);
		hour = time.substring(0, timeColon1);

		if (timeColon1 == timeColon2) {
			minute = time.substring(timeColon1 + 1);
			second = "0";
		} else {
			minute = time.substring(timeColon1 + 1, timeColon2);
			second = time.substring(timeColon2 + 1);
		}

		return toDate(month, day, year, hour, minute, second);
	}

	public static java.util.Date toDate(String date, String time, String inStr) {
		if (date == null || time == null)
			return null;
		String month;
		String day;
		String year;
		String hour;
		String minute;
		String second;

		int dateSlash1 = date.indexOf(inStr);
		int dateSlash2 = date.lastIndexOf(inStr);

		if (dateSlash1 <= 0 || dateSlash1 == dateSlash2)
			return null;
		int timeColon1 = time.indexOf(":");
		int timeColon2 = time.lastIndexOf(":");

		if (timeColon1 <= 0)
			return null;
		month = date.substring(0, dateSlash1);
		day = date.substring(dateSlash1 + 1, dateSlash2);
		year = date.substring(dateSlash2 + 1);
		hour = time.substring(0, timeColon1);

		if (timeColon1 == timeColon2) {
			minute = time.substring(timeColon1 + 1);
			second = "0";
		} else {
			minute = time.substring(timeColon1 + 1, timeColon2);
			second = time.substring(timeColon2 + 1);
		}

		return toDate(month, day, year, hour, minute, second);
	}

	/**
	 * Makes a Date from separate Strings for month, day, year, hour, minute,
	 * and second.
	 * 
	 * @param monthStr
	 *            The month String
	 * @param dayStr
	 *            The day String
	 * @param yearStr
	 *            The year String
	 * @param hourStr
	 *            The hour String
	 * @param minuteStr
	 *            The minute String
	 * @param secondStr
	 *            The second String
	 * @return A Date made from separate Strings for month, day, year, hour,
	 *         minute, and second.
	 */
	public static java.util.Date toDate(String monthStr, String dayStr,
			String yearStr, String hourStr, String minuteStr, String secondStr) {
		int month, day, year, hour, minute, second;

		try {
			month = Integer.parseInt(monthStr);
			day = Integer.parseInt(dayStr);
			year = Integer.parseInt(yearStr);
			hour = Integer.parseInt(hourStr);
			minute = Integer.parseInt(minuteStr);
			second = Integer.parseInt(secondStr);
		} catch (Exception e) {
			return null;
		}
		return toDate(month, day, year, hour, minute, second);
	}

	/**
	 * Makes a Date from separate ints for month, day, year, hour, minute, and
	 * second.
	 * 
	 * @param month
	 *            The month int
	 * @param day
	 *            The day int
	 * @param year
	 *            The year int
	 * @param hour
	 *            The hour int
	 * @param minute
	 *            The minute int
	 * @param second
	 *            The second int
	 * @return A Date made from separate ints for month, day, year, hour,
	 *         minute, and second.
	 */
	public static java.util.Date toDate(int month, int day, int year, int hour,
			int minute, int second) {
		Calendar calendar = Calendar.getInstance();

		try {
			calendar.set(year - 1900, month - 1, day, hour, minute, second);
		} catch (Exception e) {
			return null;
		}
		java.util.Date dtTmp = calendar.getTime();
		return dtTmp;// new java.util.Date(calendar.getTime().getTime());
	}

	/**
	 * Makes a date String in the format MM/DD/YYYY from a Date
	 * 
	 * @param date
	 *            The Date
	 * @return A date String in the format MM/DD/YYYY
	 */
	public static String toDateString(java.util.Date date) {
		if (date == null)
			return "";
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		String monthStr;
		String dayStr;
		String yearStr;

		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}
		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = "" + day;
		}
		yearStr = "" + year;
		return monthStr + "/" + dayStr + "/" + yearStr;
	}

	/**
	 * 返回中文日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static String toCNDateString(java.util.Date date) {
		if (date == null)
			return "";
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);
		String monthStr;
		String dayStr;
		String yearStr;

		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}
		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = "" + day;
		}
		yearStr = "" + year;
		return yearStr + "年" + monthStr + "月" + dayStr + "日";
	}

	/**
	 * Makes a time String in the format HH:MM:SS from a Date. If the seconds
	 * are 0, then the output is in HH:MM.
	 * 
	 * @param date
	 *            The Date
	 * @return A time String in the format HH:MM:SS or HH:MM
	 */
	public static String toTimeString(java.util.Date date) {
		if (date == null)
			return "";
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		return (toTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar
				.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
	}

	/**
	 * Makes a time String in the format HH:MM:SS from a separate ints for hour,
	 * minute, and second. If the seconds are 0, then the output is in HH:MM.
	 * 
	 * @param hour
	 *            The hour int
	 * @param minute
	 *            The minute int
	 * @param second
	 *            The second int
	 * @return A time String in the format HH:MM:SS or HH:MM
	 */
	public static String toTimeString(int hour, int minute, int second) {
		String hourStr;
		String minuteStr;
		String secondStr;

		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = "" + hour;
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = "" + minute;
		}
		if (second < 10) {
			secondStr = "0" + second;
		} else {
			secondStr = "" + second;
		}
		if (second == 0)
			return hourStr + ":" + minuteStr;
		else
			return hourStr + ":" + minuteStr + ":" + secondStr;
	}

	/**
	 * Makes a combined data and time string in the format "MM/DD/YYYY HH:MM:SS"
	 * from a Date. If the seconds are 0 they are left off.
	 * 
	 * @param date
	 *            The Date
	 * @return A combined data and time string in the format "MM/DD/YYYY
	 *         HH:MM:SS" where the seconds are left off if they are 0.
	 */
	public static String toDateTimeString(java.util.Date date) {
		if (date == null)
			return "";
		String dateString = toDateString(date);
		String timeString = toTimeString(date);

		if (dateString != null && timeString != null)
			return dateString + " " + timeString;
		else
			return "";
	}

	/**
	 * Makes a Timestamp for the beginning of the month
	 * 
	 * @return A Timestamp of the beginning of the month
	 */
	public static java.sql.Timestamp monthBegin() {
		Calendar mth = Calendar.getInstance();

		mth.set(Calendar.DAY_OF_MONTH, 1);
		mth.set(Calendar.HOUR_OF_DAY, 0);
		mth.set(Calendar.MINUTE, 0);
		mth.set(Calendar.SECOND, 0);
		mth.set(Calendar.AM_PM, Calendar.AM);
		return new java.sql.Timestamp(mth.getTime().getTime());
	}

	/**
	 * This static method returns the number of days between two specified
	 * dates. If one of the dates in null, the difference is between system date
	 * and other date
	 * 
	 * @Method Name: DaysBetween(java.sql.Date dt1, java.sql.Date dt2)
	 * @version 1.0
	 * @author TCS
	 * @param adtDate1
	 *            Date 1
	 * @param adtDate2
	 *            Date 2
	 * @return returns number of days between Date 1 and Date 2
	 * @Version Author Date Change Description
	 */
	public static int daysBetween(java.util.Date adtDate1,
			java.util.Date adtDate2) {
		int liNumberOfDays = 0;
		int liMonth1 = 0;
		int liDay1 = 0;
		int liYear1 = 0;
		int liMonth2 = 0;
		int liDay2 = 0;
		int liYear2 = 0;

		GregorianCalendar lGregCalendar1 = (GregorianCalendar) GregorianCalendar
				.getInstance();
		if (adtDate1 != null)
			lGregCalendar1.setTime(adtDate1);

		GregorianCalendar lGregCalendar2 = (GregorianCalendar) GregorianCalendar
				.getInstance();
		if (adtDate2 != null)
			lGregCalendar2.setTime(adtDate2);

		liDay1 = lGregCalendar1.get(Calendar.DAY_OF_MONTH);
		liMonth1 = lGregCalendar1.get(Calendar.MONTH) + 1;
		liYear1 = lGregCalendar1.get(Calendar.YEAR);

		liDay2 = lGregCalendar2.get(Calendar.DAY_OF_MONTH);
		liMonth2 = lGregCalendar2.get(Calendar.MONTH) + 1;
		liYear2 = lGregCalendar2.get(Calendar.YEAR);

		liNumberOfDays = toJulian(liDay1, liMonth1, liYear1)
				- toJulian(liDay2, liMonth2, liYear2);
		liNumberOfDays = Math.abs(liNumberOfDays);

		return liNumberOfDays;
	}

	/**
	 * This static method returns the Julian day for interger values of day,
	 * month and year
	 * 
	 * @Method Name: ToJulian(int aiDay, int aiMonth, int aiYear)
	 * @version 1.0
	 * @author TCS
	 * @param aiDay
	 *            Day
	 * @param aiMonth
	 *            Month
	 * @param aiYear
	 *            Year
	 * @return The Julian day number that begins at noon of this day Positive
	 *         year signifies A.D., negative year B.C. Remember that the year
	 *         after 1 B.C. was 1 A.D. A convenient reference point is that May
	 *         23, 1968 noon is Julian day 2440000. Julian day 0 is a Monday.
	 *         This algorithm is from Press et al., Numerical Recipes in C, 2nd
	 *         ed., Cambridge University Press 1992
	 */

	private static int toJulian(int aiDay, int aiMonth, int aiYear) {
		int liJulianYear = aiYear;
		if (aiYear < 0)
			liJulianYear++;
		int liJulianMonth = aiMonth;
		if (aiMonth > 2)
			liJulianMonth++;
		else {
			liJulianYear--;
			liJulianMonth += 13;
		}
		int liJulian = (int) (java.lang.Math.floor(365.25 * liJulianYear)
				+ java.lang.Math.floor(30.6001 * liJulianMonth) + aiDay + 1720995.0);

		int IGREG = 15 + 31 * (10 + 12 * 1582);
		// Gregorian Calendar adopted Oct. 15, 1582

		if (aiDay + 31 * (aiMonth + 12 * aiYear) >= IGREG)
		// change over to Gregorian calendar
		{
			int ja = (int) (0.01 * liJulianYear);
			liJulian += 2 - ja + (int) (0.25 * ja);
		}
		return liJulian;
	}

	// the String data format must be yyyy-mm-dd hh:mm:ss.fffffffff
	public static Timestamp str2Date(String asDate, String asPattern) {
		java.sql.Timestamp lStamp = null;

		if (asDate != null && asDate.length() > 0) {
			if (asPattern == null || asPattern.length() == 0) {
				try {
					lStamp = java.sql.Timestamp.valueOf(asDate);
				} catch (Exception e) {
					lStamp = new java.sql.Timestamp(System.currentTimeMillis());
				}
			} else {
				try {
					SimpleDateFormat lFormat = new SimpleDateFormat(asPattern);
					lStamp = new java.sql.Timestamp(lFormat.parse(asDate)
							.getTime());
				} catch (Exception e) {
					lStamp = new java.sql.Timestamp(System.currentTimeMillis());
				}
			}
		}
		return lStamp;
	}

	public static String formatDateTime(java.util.Date adDate, String asPattern) {
		String result = null;
		if (adDate != null) {
			SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat();
			lSimpleDateFormat.applyPattern(asPattern);
			result = lSimpleDateFormat.format(adDate);
		}

		return result;
	}

	public static String date2Text(Date date) {
		return date2Text(date, DATETIME_DATE);
	}

	public static String datetime2Text(Date datetime) {
		return date2Text(datetime, DATETIME_TIME);
	}

	public static String date2Text(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String text = "";
		try {
			text = formatter.format(date);
		} catch (Exception e) {
		}
		return text;
	}

	public static java.util.Date text2Date(String text) {
		return text2Date(text, DATETIME_DATE);
	}

	public static java.util.Date text2Datetime(String text) {
		return text2Date(text, DATETIME_TIME);
	}

	public static java.util.Date text2Date(String text, String pattern) {
		if (text == null)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = formatter.parse(text);
		} catch (Exception e) {
			date = new Date(System.currentTimeMillis());
		}
		return date;
	}

	/**
	 * 西方的一周：周日开始，周六结束
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeek(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (subYear == 0) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			// //例子:cal1是"2005-1-1"，cal2是"2004-12-25"
			// java对"2004-12-25"处理成第52周
			// "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			// 例子:cal1是"2004-12-31"，cal2是"2005-1-1"
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 中国的一周：周一开始，周日结束
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeek_CN(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);

		if (subYear == 0 || (1 == subYear && 11 == cal2.get(Calendar.MONTH))
				|| (-1 == subYear && 11 == cal1.get(Calendar.MONTH))) {

			int week = cal1.get(Calendar.WEEK_OF_YEAR)
					- cal2.get(Calendar.WEEK_OF_YEAR);

			if (week == 0) {
				int weekday1 = cal1.get(Calendar.DAY_OF_WEEK);
				int weekday2 = cal2.get(Calendar.DAY_OF_WEEK);
				if ((weekday1 == Calendar.SUNDAY && weekday2 != Calendar.SUNDAY) || (weekday1 != Calendar.SUNDAY && weekday2 == Calendar.SUNDAY)) {
					return false;
				} else {
					return true;
				}
			} else if (week == 1) {
				int weekday1 = cal1.get(Calendar.DAY_OF_WEEK);
				int weekday2 = cal2.get(Calendar.DAY_OF_WEEK);
				if (weekday1 == Calendar.SUNDAY && weekday2 != Calendar.SUNDAY) {
					return true;
				}
			} else if (week == -1) {
				int weekday1 = cal1.get(Calendar.DAY_OF_WEEK);
				int weekday2 = cal2.get(Calendar.DAY_OF_WEEK);
				if (weekday1 != Calendar.SUNDAY && weekday2 == Calendar.SUNDAY) {
					return true;
				}
			}

		}
		return false;
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(date2);
		if (year == cal.get(Calendar.YEAR)
				&& day == cal.get(Calendar.DAY_OF_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBeforeDay(long dt1, long dt2) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dt1);

		int year1 = cal.get(Calendar.YEAR);
		int day1 = cal.get(Calendar.DAY_OF_YEAR);

		// /连续登陆奖励判断

		cal.setTimeInMillis(dt2);
		int year2 = cal.get(Calendar.YEAR);
		int day2 = cal.get(Calendar.DAY_OF_YEAR);

		if (year1 != year2 || day1 != day2) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			year2 = cal.get(Calendar.YEAR);
			day2 = cal.get(Calendar.DAY_OF_YEAR);

			if (year1 == year2 && day1 == day2) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 与当前时间是不是同一天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isSameDay(Date date) {
		if (date == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(date);
		if (year == cal.get(Calendar.YEAR)
				&& day == cal.get(Calendar.DAY_OF_YEAR)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSameMonth(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		cal.setTime(date2);
		if (year == cal.get(Calendar.YEAR) && month == cal.get(Calendar.MONTH)) {
			return true;
		} else {
			return false;
		}
	}

	// 当前时间点距当天0时的毫秒数
	public static long getMillisecondInADay() {
		Calendar cl = Calendar.getInstance();
		return (cl.get(Calendar.HOUR_OF_DAY) * 3600 + cl.get(Calendar.MINUTE)
				* 60 + cl.get(Calendar.SECOND))
				* 1000 + cl.get(Calendar.MILLISECOND);
	}

	public static long getMillisecondInADay(long time) {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(time);
		return (cl.get(Calendar.HOUR_OF_DAY) * 3600 + cl.get(Calendar.MINUTE)
				* 60 + cl.get(Calendar.SECOND))
				* 1000 + cl.get(Calendar.MILLISECOND);
	}

	public static String formatSeonds(int time) {
		int hour = (int) time / 3600;// 小时
		int tmp = time % 3600;
		int minute = tmp / 60; // 分
		int second = tmp % 60; // 秒

		StringBuilder sb = new StringBuilder(8);
		if (hour < 10) {
			sb.append("0");
		}
		sb.append(hour).append(":");
		if (minute < 10) {
			sb.append("0");
		}
		sb.append(minute).append(":");

		if (second < 10) {
			sb.append("0");
		}
		sb.append(second);
		return sb.toString();
	}

	public static Timestamp getNowZeroDttm() {
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		Timestamp now = new Timestamp(c.getTimeInMillis());
		return now;
	}
}
