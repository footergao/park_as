package com.hdzx.tenement.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

/**
 * 时间格式化工具类
 * 
 * @Author zhw
 * @Version V1.0
 * @Createtime：2014年5月9日 下午6:24:09
 */
public class TimeFormatUtil {
	/**
	 * 格式化当前时间为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatChineseTimeStr() {
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		return simpleDateFormat.format(currentTime);
	}
	@SuppressLint("SimpleDateFormat")
	public static String formatChineseTimeStr(long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		
		return simpleDateFormat.format(time);
	}

	@SuppressLint("SimpleDateFormat")
	public static long timeTOMillis(String inTime) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.parse(inTime).getTime();
	}

	/**
	 * 格式化当前时间为 yyyy-MM-dd
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatChineseTimeDayStr() {
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return simpleDateFormat.format(currentTime);
	}

	/**
	 * 获取当前时间散落的区间
	 * 
	 * @author zhw
	 * 
	 * @return
	 */
	public static String formatTimeRange() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);

		return hour + "点 ~ " + (hour + 1) + "点";
	}

	/**
	 * 获取当前星期几
	 * 
	 * @author zhw
	 * 
	 * @return
	 */
	public static String formatWeekRange() {
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.DAY_OF_WEEK);

		String currentWeek = "";
		switch (week) {
		case 1:
			currentWeek = "星期天";
			break;
		case 2:
			currentWeek = "星期一";
			break;
		case 3:
			currentWeek = "星期二";
			break;
		case 4:
			currentWeek = "星期三";
			break;
		case 5:
			currentWeek = "星期四";
			break;
		case 6:
			currentWeek = "星期五";
			break;
		case 7:
			currentWeek = "星期六";
			break;
		default:
			break;
		}

		return currentWeek;
	}

	/**
	 * 格式化时间 MM月dd日 HH:mm
	 * 
	 * @param timeMills
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatTime(long timeMills) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(timeMills);
		return dateFormat.format(date);
	}

	/**
	 * 格式化时间 yyyy-MM-dd
	 * 
	 * @param timeMills
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatYeardayTime(long timeMills) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(timeMills);
		return dateFormat.format(date);
	}

	/**
	 * 格式化时间 MM月dd日
	 * 
	 * @param timeMills
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatdayTime(long timeMills) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		Date date = new Date(timeMills);
		return dateFormat.format(date);
	}

	public static String analyzeTime(long timeMills) {
		String time = "";
		String now = formatChineseTimeDayStr();
		String logtime = formatYeardayTime(timeMills);
		String yesterday = "";

		int currYear, currMonth, currDate, currHour;
		Calendar calendar = Calendar.getInstance();
		currYear = calendar.get(Calendar.YEAR);
		currMonth = calendar.get(Calendar.MONTH) + 1;
		currDate = calendar.get(Calendar.DATE);
		currHour = calendar.get(Calendar.HOUR_OF_DAY);

		// 判断昨天是不是上一年。
		if (currMonth == 1 && currDate == 1) {// 今天是一月一号
			yesterday = (currYear - 1) + "-" + 12 + "-" + 31;
		} else if (currMonth >= 1 && currDate >= 2) {// 今天是一个月的二号或2号之后的日期
			if (currMonth < 10) {// month
				yesterday = (currYear) + "-0" + currMonth;
			} else {
				yesterday = (currYear) + "-" + currMonth;
			}
			if (currDate < 10) {// day
				yesterday = yesterday + "-0" + (currDate - 1);
			} else {
				yesterday = yesterday + "-" + (currDate - 1);
			}
			yesterday = (currYear) + "-" + currMonth + "-" + (currDate - 1);
		} else if (currMonth > 1 && currDate == 1) {// 今天是除一月外的月份第一天
			if (currMonth < 10) {
				yesterday = +(currYear) + "-0" + (currMonth - 1);
			} else {
				yesterday = +(currYear) + "-" + (currMonth - 1);
			}
			if (currDate < 10) {
				yesterday = yesterday + "-0"
						+ getLastDayOfUpMonth(currYear, currMonth, currDate);
			} else {
				yesterday = yesterday + "-"
						+ getLastDayOfUpMonth(currYear, currMonth, currDate);
			}
		}

		if (now.equals(logtime)) {
			if (yesterday.equals(logtime)) {
				time = "昨天  " + formatHourTime(timeMills);
			} else {
				time = "今天  " + formatHourTime(timeMills);
			}
		} else {
			time = formatdayTime(timeMills);
		}
		return time;
	}

	public static String analyzeTimeDetail(long timeMills) {
		String time = "";
		String now = formatChineseTimeDayStr();
		String logtime = formatYeardayTime(timeMills);
		String yesterday = "";

		int currYear, currMonth, currDate, currHour;
		Calendar calendar = Calendar.getInstance();
		currYear = calendar.get(Calendar.YEAR);
		currMonth = calendar.get(Calendar.MONTH) + 1;
		currDate = calendar.get(Calendar.DATE);
		currHour = calendar.get(Calendar.HOUR_OF_DAY);

		// 判断昨天是不是上一年。
		if (currMonth == 1 && currDate == 1) {// 今天是一月一号
			yesterday = (currYear - 1) + "-" + 12 + "-" + 31;
		} else if (currMonth >= 1 && currDate >= 2) {// 今天是一个月的二号或2号之后的日期
			if (currMonth < 10) {// month
				yesterday = (currYear) + "-0" + currMonth;
			} else {
				yesterday = (currYear) + "-" + currMonth;
			}
			if (currDate < 10) {// day
				yesterday = yesterday + "-0" + (currDate - 1);
			} else {
				yesterday = yesterday + "-" + (currDate - 1);
			}
			yesterday = (currYear) + "-" + currMonth + "-" + (currDate - 1);
		} else if (currMonth > 1 && currDate == 1) {// 今天是除一月外的月份第一天
			if (currMonth < 10) {
				yesterday = +(currYear) + "-0" + (currMonth - 1);
			} else {
				yesterday = +(currYear) + "-" + (currMonth - 1);
			}
			if (currDate < 10) {
				yesterday = yesterday + "-0"
						+ getLastDayOfUpMonth(currYear, currMonth, currDate);
			} else {
				yesterday = yesterday + "-"
						+ getLastDayOfUpMonth(currYear, currMonth, currDate);
			}
		}

		if (now.equals(logtime)) {
			if (yesterday.equals(logtime)) {
				time = "昨天  " + formatHourTime(timeMills);
			} else {
				time = "今天  " + formatHourTime(timeMills);
			}
		} else {
			time = formatTime(timeMills);
		}
		return time;
	}

	/**
	 * 上个月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static int getLastDayOfUpMonth(int year, int month, int date) {
		Calendar calendar = new GregorianCalendar(year, month, date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		calendar.add(Calendar.MONTH, -1);// 月增减1天
		calendar.add(Calendar.DAY_OF_MONTH, -1);// 日期倒数一日,既得到本月最后一天
//		Logger.i("", "上个月的最后一天是：" + calendar.get(Calendar.DATE) + "号");
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 前天
	 * 
	 * @param lastDay
	 * @return
	 */
	public static String getLastDayComplete(String lastDay) {
		System.out.println("------lastDay ----" + lastDay);
		String year = lastDay.substring(0, lastDay.indexOf("-"));
		String month = lastDay.substring(lastDay.indexOf("-") + 1,
				lastDay.lastIndexOf("-"));
		String day = lastDay.substring(lastDay.lastIndexOf("-") + 1);
		System.out.println(year);
		System.out.println(day);
		System.out.println(month);
		if (Integer.parseInt(month) < 10) {
			month = "0" + month;
		}
		if (Integer.parseInt(day) < 10) {
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}

	/**
	 * 格式化时间 HH:mm
	 * 
	 * @param timeMills
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatHourTime(long timeMills) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date(timeMills);
		return dateFormat.format(date);
	}
	
	/**
	 * 
	 * @param time 
	 * @return
	 */
	public static String formatTime(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date=dateFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateFormat.format(date);
	}

	/**
	 * 毫秒转分秒工具方法
	 * 
	 * @author Zhang
	 */
//	@SuppressWarnings("unused")
//	private String formatMillis(Context context, long millis) {
//		int second = (int) (millis / 1000);
//		if (second == 0 && millis != 0) {
//			return "1"
//					+ context.getString(ResourceReader.read(context,
//							ResourceReader.STRING, "second"));
//		} else if (second == 0) {
//			return context.getString(ResourceReader.read(context,
//					ResourceReader.STRING, "unconnected"));
//		} else if (second < 60) {
//			return second
//					+ context.getString(ResourceReader.read(context,
//							ResourceReader.STRING, "second"));
//		} else {
//			return second
//					/ 60
//					+ context.getString(ResourceReader.read(context,
//							ResourceReader.STRING, "minute"))
//					+ (second - (second / 60) * 60)
//					+ context.getString(ResourceReader.read(context,
//							ResourceReader.STRING, "second"));
//		}
//	}
	
	public static long stringToLong(String strTime, String formatType)
 			throws ParseException {
 		Date date = stringToDate(strTime, formatType); // String类型转成date类型
 		if (date == null) {
 			return 0;
 		} else {
 			long currentTime = dateToLong(date); // date类型转成long类型
 			return currentTime;
 		}
 	}
	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		date = formatter.parse(strTime);
 		return date;
 	}
	
	// date类型转换为long类型
	 	// date要转换的date类型的时间
	 	public static long dateToLong(Date date) {
	 		return date.getTime();
	 	}
}
