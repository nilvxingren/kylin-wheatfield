package com.rkylin.wheatfield.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据校验
 * 
 * @author Achilles
 *
 */
public class VerifyUtil {

	/**
	 * 校验ip合法性
	 * @param ipStr
	 * @return
	 */
	public static boolean isIP(String ipStr) {
		if (ipStr == null) {
			return false;
		}
		String eL = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(ipStr);
		return m.matches();

	}

}
