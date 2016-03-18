package com.hdzx.tenement.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Jesley
 *
 */
public class CalendarUtils {

	/**
	 * 计算两个日期之间的所有日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<Date>
	 * @说明 该返回值的List会包含开始日期和结束日期
	 */
	public static List<Date> getDates(Date startDate, Date endDate) {
		List<Date> list = new ArrayList();
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		// 如果开始日期小于结束日期
		if (startCalendar.after(endCalendar)) {
			return null;
		}
		// 计算2个日期之间的日期
		while (startCalendar.before(endCalendar)) {
			list.add(startCalendar.getTime());
			startCalendar.add(Calendar.DATE, 1);
		}
		// 添加结束日期
		list.add(endDate);
		return list;
	}

	/**
	 * 计算两个日期之间的相隔天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return int
	 * 
	 */
	public static int getDayCounts(Date startDate, Date endDate) {
		int days = 0;
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		while (startCalendar.before(endCalendar)) {
			days++;
			startCalendar.add(Calendar.DATE, 1);
		}
		return days;
	}

	/**
	 * 获得当前日期的字符串
	 * 
	 * @return
	 */
	public static String getDateFormatString(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 解析日期字符串
	 * 
	 * @return
	 */
	public static Date getDateFromString(String dateStr, String pattern) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {

		}
		return date;
	}
	
	/**
	 * 获得当前月的第一天
	 */
	public static Date getFirstDayOfMonth(Date dateTime){
		if(dateTime == null) return dateTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		return calendar.getTime();
	}
	
	/**
	 * 获得该月的最后一天
	 * @param dateTime
	 * @return
	 */
	public static Date getLastDayOfMonth(Date dateTime){
		if(dateTime == null) return dateTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
}
