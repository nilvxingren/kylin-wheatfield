package com.rkylin.wheatfield.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.icu.util.Calendar;
public class DateUtils {
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
	public static Date getSysDate(String format){
		Date date=new Date();
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
}
