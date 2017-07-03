package com.rkylin.wheatfield.utils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 常见的辅助类
 * 
 */
public class CommUtil {

	private static Log log = LogFactory.getLog(CommUtil.class);
	
	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public  boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证输入的字符串是否为全数字
	 * @param str 需要检查的字符串
	 * @return 如果是全数字返回true，否则返回false
	 */
	public boolean isNumeric(String str) {
		if(str.length()>1){
			Pattern pattern = Pattern.compile("[1-9][0-9]*");
			return pattern.matcher(str).matches();
		}else{
			Pattern pattern = Pattern.compile("[0-9]");
			return pattern.matcher(str).matches();
		}
	}
	
	/**
	 * 比较指定的两个数字字符串大小
	 * @param num1 指定的第一个数字字符串
	 * @param num2 指定的第二个数字字符串
	 * @return num1>=num2 返回true；num1<num2返回false
	 * */
	public boolean compareNum(String num1,String num2){
		if(Double.parseDouble(num1)<Double.parseDouble(num2)){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * 获取字符串小数点之前的子串
	 * @param org 原始字符串
	 * @return 小数点之前子串
	 * */
	public String getInt(String org){
		if(-1 == org.indexOf(".")){
			return org;
		}else{
			return org.substring(0, org.indexOf("."));
		}
	}

}