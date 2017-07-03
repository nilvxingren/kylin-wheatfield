package com.rkylin.wheatfield.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class PartyCodeUtil {
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMddHHmmsss");

	/**
	 * 按规则生成TcParty表里面的partyCode 前6位代表卡Bin 信息 现在固定为141224 7到10位代表是行业信息 现在固定为5045
	 */
	public static String getPartyCodeByRule() {

		String prePartyCode = "1412245045";
		prePartyCode += getRandomCode();
		Calendar calendar = Calendar.getInstance();
		String ym = format.format(calendar.getTime());
		prePartyCode += getPartyCode(ym + prePartyCode);
		prePartyCode += getPartyCode(prePartyCode);
		return prePartyCode;
	}

	/**
	 * 生成6位数字的随机码
	 * */
	public static String getRandomCode() {
		StringBuffer randomCode = new StringBuffer();
		Random random = new Random();
		String[] codeSequence = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
				"9" };
		for (int i = 0; i < 6; i++) {
			String strRand = String.valueOf(codeSequence[random
					.nextInt(codeSequence.length)]);
			randomCode.append(strRand);
		}
		System.out.println(randomCode);
		return randomCode.toString();
	}

	/**
	 * 根据传入的纯数字的串 ，根据Luhn 算法生成1 位的数字校验位
	 * 
	 * @param prePartyCode
	 *            未加校验位的数字串
	 * @return 返回校验位
	 */
	private static String getPartyCode(String prePartyCode) {
		int he = 0;
		for (int i = 0; i < prePartyCode.length(); i++) {
			String ch = prePartyCode.substring(i, i + 1);
			int c = Integer.valueOf(ch);

			if ((i + 1) % 2 == 0 && i > 1) {
				c = c * 2;
			}
			if (c > 10) {
				c = c - 9;
			}
			he += c;
		}

		he = (he * 9) % 10;
		return he + "";
	}

}
