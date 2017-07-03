package com.rkylin.wheatfield.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;


public class SignUtils {

	/**
	 * 给ROP请求签名。
	 * 
	 * @param requestHolder
	 *            所有字符型的TOP请求参数
	 * @param secret
	 *            签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String signRopRequest(Map<String, String[]> requestParams,
			String secret) throws IOException {
		
		// 第一步：把字典按Key的字母顺序排序
		Map<String, String[]> map = new TreeMap<String, String[]>();
		map.putAll(requestParams);
		Set<Entry<String, String[]>> paramSet = map.entrySet();

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder(secret);
		for (Entry<String, String[]> param : paramSet) {
			if (StringUtils.areNotEmpty(param.getKey(), param.getValue()[0])) {
				query.append(param.getKey()).append(param.getValue()[0]);
			}
		}
		query.append(secret);
		// 第三步：使用MD5加密
		byte[] bytes = encryptMD5(query.toString());

		// 第四步：把二进制转化为大写的十六进制
		return byte2hex(bytes);
	}
	
	private static byte[] encryptMD5(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes(com.rkylin.wheatfield.constant.Constants.CHARSET_UTF8));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
		return bytes;
	}
	
	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}	
	public String getMD5Code(String data) throws IOException {
		byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(data.getBytes(com.rkylin.wheatfield.constant.Constants.CHARSET_UTF8));
			return byte2hex(bytes);
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		}
	}
}
