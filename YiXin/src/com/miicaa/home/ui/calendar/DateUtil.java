package com.miicaa.home.ui.calendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

import com.miicaa.home.ui.report.WeekEntity;

//import com.miicaa.home.ui.report.WeekEntity;

public class DateUtil {

	// public static String[] weekName = { "周日", "周一", "周二", "周三", "周四",
	// "周五","周六" };

	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}

	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static CustomDate getNextSunday() {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 7 - getWeekDay() + 1);
		CustomDate date = new CustomDate(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
		return date;
	}

	public static int[] getWeekSunday(int year, int month, int day, int pervious) {
		int[] time = new int[3];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.DAY_OF_MONTH, pervious);
		time[0] = c.get(Calendar.YEAR);
		time[1] = c.get(Calendar.MONTH) + 1;
		time[2] = c.get(Calendar.DAY_OF_MONTH);
		return time;

	}

	public static int getWeekDayFromDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));
		int week_index = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromString(int year, int month) {
		String dateString = year + "-" + (month > 9 ? month : ("0" + month))
				+ "-01";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {

		}
		return date;
	}

	public static boolean isToday(CustomDate date) {
		return (date.year == DateUtil.getYear()
				&& date.month == DateUtil.getMonth() && date.day == DateUtil
					.getCurrentMonthDay());
	}

	public static boolean isCurrentMonth(CustomDate date) {

		return (date.year == DateUtil.getYear() && date.month == DateUtil
				.getMonth());
	}

	public static boolean isMiddleMonth(CustomDate date, int direc) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, date.year);
		c.set(Calendar.MONTH, date.month - 1);
		c.set(Calendar.DAY_OF_MONTH, date.day);
		int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (direc == -1) {
			if (date.day == lastDay)
				return false;
		}

		else {

			if (date.day == 1) {
				return false;
			}
		}
		return true;
	}

	/*
	 * 格式化时间字符串， param1 格式 param2 与当前时间的间隔，昨天为-1，明天为1，今天为0，以此类推
	 */
	public static String formatDate(String format, int delay) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ delay);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}

	/*
	 * 获取以周为单位的数据 startDate,endDate 例如：printfWeeks(2013,2017)
	 */
	public static ArrayList<WeekEntity> printfWeeks(int startDate, int endDate)
			throws Exception {
		ArrayList<WeekEntity> datas = new ArrayList<WeekEntity>();
		for (int j = startDate; j <= endDate; j++) {
			for (int m = 1; m <= 12; m++) {
				String str = j + "-" + m;
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
				Date date1 = dateFormat.parse(str);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date1);
				int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

				int count = 0;

				for (int i = 1; i <= days; i++) {
					DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
					Date date2 = dateFormat1.parse(str + "-" + i);
					calendar.clear();
					calendar.setTime(date2);
					int k = new Integer(calendar.get(Calendar.DAY_OF_WEEK));

					if (k == 1) {// 若当天是周日
						count++;

						WeekEntity week = new WeekEntity();

						week.setEndDate(calendar);
						week.setIndex(count);
						calendar.set(Calendar.DAY_OF_YEAR,
								calendar.get(Calendar.DAY_OF_YEAR) - 6);
						week.setStartDate(calendar);
						datas.add(week);
					}

				}
			}
		}
		return datas;
	}
}
