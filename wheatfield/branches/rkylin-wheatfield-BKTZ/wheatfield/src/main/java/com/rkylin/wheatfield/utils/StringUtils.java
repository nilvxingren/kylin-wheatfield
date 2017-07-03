package com.rkylin.wheatfield.utils;

import java.util.Arrays;

/**
 * 字符串工具类。
 * 
 * @author Robin.huang
 * @since 1.0, Sep 12, 2009
 */
public abstract class StringUtils {

	// 换行符
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private StringUtils() {
	}
	
	/**
	 * 得到一个字符串指定的长度,一个汉字文长度为2,英文字符长度为1
	 * @param s  原始字符串
	 * @return maxLength 需要的最大长度
	 */
	public static String subStr(String s,int maxLength) {
		if(s==null || "".equals(s.trim())){
			return "";
		}
		int valueLength = 0;
		int cbit;
		int length = 0;
		String chinese = "[\u4e00-\u9fa5]";
		String rtnStr = "";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < s.length(); i++) {
			// 获取一个字符
			String temp = s.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为2
				cbit = 2;
			} else {
				// 其他字符长度为1
				cbit = 1;
			}
			valueLength += cbit;
			length++;
			if(valueLength > maxLength){
				length--;
				break;
			}
			if(valueLength == maxLength){
				break;
			}
		}
		rtnStr = s.substring(0, length);
		return rtnStr;
	}
	
	/**
	 * 数组转字符串
	 * @param obj
	 * @return
	 */
	public static String arrayToStr(Object[] obj){
		return Arrays.toString(obj).replace("[", "").replace("]", "");
	}
	/**
	 * 判断字符串是否在字符串数组中存在
	 * @param strs 字符串数组
	 * @param value 字符串
	 * @return 存在返回 false，不存在返回true;
	 */
	public static boolean stringIsHave(String[] strs,String value){
		boolean isHave=true;
		for (String str : strs) {
			if(value.equals(str)){
				isHave=false;
				break;
			}
		}
		return isHave;
	}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 * 
	 * @param value
	 *            待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查对象是否为数字型字符串。
	 */
	public static boolean isNumeric(Object obj) {
		if (obj == null) {
			return false;
		}
		String str = obj.toString();
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定的字符串列表是否不为空。
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * 把通用字符编码的字符串转化为汉字编码。
	 */
	public static String unicodeToChinese(String unicode) {
		StringBuilder out = new StringBuilder();
		if (!isEmpty(unicode)) {
			for (int i = 0; i < unicode.length(); i++) {
				out.append(unicode.charAt(i));
			}
		}
		return out.toString();
	}

	/**
	 * 过滤不可见字符
	 */
	public static String stripNonValidXMLCharacters(String input) {
		if (input == null || ("".equals(input)))
			return "";
		StringBuilder out = new StringBuilder();
		char current;
		for (int i = 0; i < input.length(); i++) {
			current = input.charAt(i);
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

}
