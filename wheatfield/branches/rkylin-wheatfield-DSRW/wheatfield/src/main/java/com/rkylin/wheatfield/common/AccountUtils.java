package com.rkylin.wheatfield.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

public class AccountUtils {
    
	/**
	 * 判断字符串是否为空
	 * @param str 字符串
	 * @return true ：为空 false 不为空
	 */
	public static boolean isEmpty(String str) {
		boolean b = true;
		if (str == null || "".equals(str)) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}
    
	/**
	 * 判断字符串是否是数字
	 * @param str 字符串
	 * @return true ： 是数字 false ：不是数字
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
    
	/**
	 * 判断字符串是否为金额
	 * @param str
	 * @return
	 */
	public static boolean isAmount(String str) {
		if (str.startsWith("0")) {
			return false;
		}
		return isNumeric(str);

	}
	
	/**
	 * 获取记账方向
	 * @param tradeType 交易类型
	 * @param flag 交易源与交易目标的标记          另一种情况：true=CREDIT_TYPE  flase=DEBIT_TYPE
	 * @return 返回不同交易类型中的DC标志
	 * */
	public static int getDorC(String transType , boolean flag){
		int rtn=0;
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
			rtn = BaseConstants.CREDIT_TYPE;
		}else if (transType.equals(TransCodeConst.FROZEN)) {//解冻
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			if(flag){
				rtn = BaseConstants.DEBIT_TYPE;
			}
			else{
				rtn = BaseConstants.CREDIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.FROZON)){//冻结
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.CREDIT_CONSUME)){//信用消费
			if(flag){
				rtn=BaseConstants.DEBIT_TYPE;
			}else {
				rtn=BaseConstants.CREDIT_TYPE;
			}
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			if(flag){
				rtn=BaseConstants.DEBIT_TYPE;
			}else {
				rtn=BaseConstants.CREDIT_TYPE;
			}
		}else{
			if(flag){
				rtn = BaseConstants.CREDIT_TYPE;
			}
			else{
				rtn = BaseConstants.DEBIT_TYPE;
			}
		}
		return rtn;
	}
	
	/**
	 * 获取交易相关方Id
	 * @param tradeAtom 原子交易
	 * @param flag 交易源与交易目标的标记
	 * @return 返回交易相关方Id
	 * */
	public  static String getThirdPartyId(TransOrderInfo transOrderInfo , boolean flag){
		String rtn = null;
		String transType=transOrderInfo.getFuncCode();
		if(transType.equals(TransCodeConst.ADJUST_ACCOUNT_AMOUNT)){//内部转账交易
			rtn="";
		}else if (transType.equals(TransCodeConst.CHARGE)) {//充值
			rtn=TransCodeConst.THIRDPARTYID_FNZZH;
		}else if (transType.equals(TransCodeConst.CREDIT_CONSUME)) {//信用消费
			rtn=TransCodeConst.THIRDPARTYID_FNYFXYZH;
		}else if(transType.equals(TransCodeConst.DEBIT_CONSUME)){//储蓄消费
			rtn=TransCodeConst.THIRDPARTYID_QTYFKZH;
		}else if (transType.equals(TransCodeConst.PAYMENT_COLLECTION)) {//代收付
			rtn=TransCodeConst.THIRDPARTYID_FNZZH;
		}else if (transType.equals(TransCodeConst.PAYMENT_WITHHOLD)) {//代扣
			rtn=TransCodeConst.THIRDPARTYID_FNZZH;
		}else if(transType.equals(TransCodeConst.WITHDROW)){//提现
			rtn=TransCodeConst.THIRDPARTYID_TXDQSZH;
		}
		return rtn;
	}	
	
	public static void main(String[] args) {
		System.out.println(isAmount("1.1"));
	}
	

}
