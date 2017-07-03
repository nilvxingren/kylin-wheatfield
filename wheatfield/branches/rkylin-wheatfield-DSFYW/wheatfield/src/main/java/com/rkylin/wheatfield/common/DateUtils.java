package com.rkylin.wheatfield.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.icu.util.Calendar;
public class DateUtils {
    
    /**
     * 显示格式： yyyy-MM-dd
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	
	public static Date getDate(String date ,String format) {
		Date dateForm = null;
		try{
		SimpleDateFormat sdf =   new SimpleDateFormat(format);
		dateForm= sdf.parse(date);
		}catch(Exception e){
			e.getMessage();
		}
		return dateForm;
	}
	public static String getyyyyMMdd(String format){
		String dateStr="";
		try{
			SimpleDateFormat sdf =   new SimpleDateFormat(format);
			dateStr= sdf.format(new Date());
		}catch(Exception e){
			e.getMessage();
		}
		return dateStr;
	}
	
	public static String getSysDateStr(String format){
		String sysDate=null;
		try{
			SimpleDateFormat sdf =   new SimpleDateFormat(format);
			sysDate=sdf.format(new Date());
		}catch(Exception e){
			e.getMessage();
		}
		return sysDate;
	}
	
	public static Date getSysDate(String format){
		Date date=null;
		try{
			SimpleDateFormat sdf =   new SimpleDateFormat(format);
			String sysDate=sdf.format(new Date());
			date=sdf.parse(sysDate);
		}catch(Exception e){
			e.getMessage();
		}
		return date;
	}
	public static String getDateFormat(String format,Date date){
		SimpleDateFormat sdf =   new SimpleDateFormat(format);
		String sysDate="";
		try{
		sysDate=sdf.format(date);
		}catch(Exception e){
			
		}
		return sysDate;
	}
	public static Date getAccountDate(String format,String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date beginDate=sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			Date endDate = sdf.parse(sdf.format(calendar.getTime()));
			return endDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取今天之前或之后的日期
	 * @param format 格式
	 * @param back 以当前为基准，向前或先后推几天，向前为正数，向后为负数
	 * @return
	 */
	public static String getTheDayBefore(String format,int back) {
	   Calendar cal=Calendar.getInstance();
       cal.add(Calendar.DATE,back);
       Date d=cal.getTime();
	   SimpleDateFormat sp=new SimpleDateFormat(format);
	   String before=sp.format(d);
	return before;
	}
	
	/**
     * 获取今天之前或之后的日期
     * @param format 格式
     * @param back 以当前为基准，向前或先后推几天，向前为正数，向后为负数
     * @return
     */
    public static Date getTheDayBeforeOrAfter(String format,int back) {
       Calendar cal=Calendar.getInstance();
       cal.add(Calendar.DATE,back);
       Date d=cal.getTime();
       SimpleDateFormat sdf=new SimpleDateFormat(format);
       Date date = null;
        try {
            date = sdf.parse(sdf.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return date;
    }
	
	public static Date getDateFormatDate(Date date ,String format) {
		Date dateForm=new Date();
		try{
		SimpleDateFormat sdf =   new SimpleDateFormat(format);
		dateForm= sdf.parse(date.toString());
		}catch(Exception e){
			e.getMessage();
		}
		return dateForm;
	}
	
	public static void main(String[] args) {
        System.out.println(getTheDayBeforeOrAfter(DATE_FORMAT_YYYYMMDD, 1));
    }
}
