package com.rkylin.wheatfield.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 时间日期类处理
 * 
 * */
public class DateUtil {

	/**
	 * 缺省的日期显示格式： yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 得到系统当前日期时间
	 * 
	 * @return 当前日期时间
	 */
	public Date getNow() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 得到用缺省方式格式化的当前日期
	 * 
	 * @return 当前日期
	 */
	public String getDate() {
		return getDateTime(DEFAULT_DATE_FORMAT);
	}

	/**
	 * 按照毫秒显示当前时间
	 * 
	 * @return 当前时间毫秒数
	 * */
	public String getTime() {
		Date dt = new Date();
		return dt.getTime() + "";
	}

	/**
	 * 得到用缺省方式格式化的当前日期及时间
	 * 
	 * @return 当前日期及时间
	 */
	public String getDateTime() {
		return getDateTime(DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * 得到系统当前日期及时间，并用指定的方式格式化
	 * 
	 * @param pattern
	 *            显示格式
	 * @return 当前日期及时间
	 */
	public String getDateTime(String pattern) {
		Date datetime = Calendar.getInstance().getTime();
		return getDateTime(datetime, pattern);
	}

	/**
	 * 得到用指定方式格式化的日期
	 * 
	 * @param date
	 *            需要进行格式化的日期
	 * @param pattern
	 *            显示格式
	 * @return 日期时间字符串
	 */
	public String getDateTime(Date date, String pattern) {
		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATETIME_FORMAT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 得到当前年份
	 * 
	 * @return 当前年份
	 */
	public int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份
	 * 
	 * @return 当前月份
	 */
	public int getCurrentMonth() {
		// 用get得到的月份数比实际的小1，需要加上
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到当前日
	 * 
	 * @return 当前日
	 */
	public int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
	 * 
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public Date addDays(int days) {
		return add(getNow(), days, Calendar.DATE);
	}

	/**
	 * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public Date addDays(Date date, int days) {
		return add(date, days, Calendar.DATE);
	}

	/**
	 * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
	 * 
	 * @param months
	 *            增加的月份数
	 * @return 增加以后的日期
	 */
	public Date addMonths(int months) {
		return add(getNow(), months, Calendar.MONTH);
	}

	/**
	 * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
	 * 
	 * @param date
	 *            基准日期
	 * @param months
	 *            增加的月份数
	 * 
	 * @return 增加以后的日期
	 */
	public Date addMonths(Date date, int months) {
		return add(date, months, Calendar.MONTH);
	}

	/**
	 * 内部方法。为指定日期增加相应的天数或月数
	 * 
	 * @param date
	 *            基准日期
	 * @param amount
	 *            增加的数量
	 * @param field
	 *            增加的单位，年，月或者日
	 * @return 增加以后的日期
	 */
	private Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差月份数
	 */
	public int diffMonths(Date one, Date two) {
		Calendar calendar = Calendar.getInstance();
		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);
		int monthOne = calendar.get(Calendar.MONDAY);
		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);
		int monthTwo = calendar.get(Calendar.MONDAY);
		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。<br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 * @throws ParseException
	 */
	public Date parse(String datestr, String pattern) throws ParseException {
		Date rtDate = new SimpleDateFormat(pattern).parse(datestr);
		return rtDate;
	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。<br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 * @throws ParseException
	 */
	public static Date strToDate(String datestr, String pattern) {
		Date rtDate = null;
		try {
			rtDate = new SimpleDateFormat(pattern).parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rtDate;
	}

	public static boolean isYYYYMMDD(String dateStr) {
	        if (dateStr == null) {
	            return false;
	        }
	        String eL = "^(20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))$";
	        Pattern p = Pattern.compile(eL);
	        Matcher m = p.matcher(dateStr);
	        boolean dateFlag = m.matches();
	        return dateFlag;

	    }
	
//   public static void main(String[] args) {
//        System.out.println(DateUtil.isYYYY_MM_DDHHMMSS("2015-05-2"));
//    }
	   
	   
	/**
	 * 校验日期格式是否是：yyyy-MM-dd hh:mm:ss
	 * @param dateStr
	 * @return
	 */
	public static boolean isYYYYMMDDHHMMSS(String dateStr) {
		if (dateStr == null) {
			return false;
		}
		String eL = "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(dateStr);
		boolean dateFlag = m.matches();
		return dateFlag;

	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。<br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 * @throws ParseException
	 */
	public static Date parseDate(String datestr, String pattern) {
		Date rtDate = null;
		try {
			rtDate = new SimpleDateFormat(pattern).parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rtDate;
	}

	/**
	 * 返回本月的最后一天
	 * 
	 * @return 本月最后一天的日期
	 */
	public Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	/**
	 * 返回给定日期中的月份中的最后一天
	 * 
	 * @param date
	 *            基准日期
	 * @return 该月最后一天的日期
	 */
	public Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1, 1);
		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 根据日期获取星期
	 * 
	 * @param strdate
	 * @return
	 */
	public String getWeekDayByDate(String strdate) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	/**
	 * 返回TimeStamp对象
	 * 
	 * @return
	 */
	public Timestamp getCurrentTimestamp() {
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp;
	}



}
