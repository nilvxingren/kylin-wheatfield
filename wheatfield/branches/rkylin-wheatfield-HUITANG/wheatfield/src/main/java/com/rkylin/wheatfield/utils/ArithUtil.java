package com.rkylin.wheatfield.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 精 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class ArithUtil {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    
    
    public String longAdd(String value1,String value2){
	Long longValue1=Long.valueOf(value1);
	Long longValue2=Long.valueOf(value2);
	return String.valueOf(longValue1+longValue2);
    }
    
    public String longSub(String value1,String value2){
	Long longValue1=Long.valueOf(value1);
	Long longValue2=Long.valueOf(value2);
	return String.valueOf(longValue1-longValue2);
    }
    
    public boolean longThan(String value1,String value2){
	Long longValue1=Long.valueOf(value1);
	Long longValue2=Long.valueOf(value2);
	return longValue1.longValue()>=longValue2.longValue();
    }

    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */
    public  double add(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
	BigDecimal b2 = new BigDecimal(Double.toString(v2));
	return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */
    public  double sub(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
	BigDecimal b2 = new BigDecimal(Double.toString(v2));
	return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     *            被乘数
     * @param v2
     *            乘数
     * @return 两个参数的积
     */
    public  double mul(double v1, double v2) {
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
	BigDecimal b2 = new BigDecimal(Double.toString(v2));
	return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */
    public  double div(double v1, double v2) {
	return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @param scale
     *            表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public  double div(double v1, double v2, int scale) {
	if (scale < 0) {
	    throw new IllegalArgumentException(
		    "The scale must be a positive integer or zero");
	}
	BigDecimal b1 = new BigDecimal(Double.toString(v1));
	BigDecimal b2 = new BigDecimal(Double.toString(v2));
	return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public  double round(double v, int scale) {
	if (scale < 0) {
	    throw new IllegalArgumentException(
		    "The scale must be a positive integer or zero");
	}
	BigDecimal b = new BigDecimal(Double.toString(v));
	BigDecimal one = new BigDecimal("1");
	return b.divide(one, scale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * 提供精确的小数位全进处理。
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 全进
     */
    public  double roundup(double v, int scale) {
	if (scale < 0) {
	    throw new IllegalArgumentException(
		    "The scale must be a positive integer or zero");
	}
	BigDecimal b = new BigDecimal(Double.toString(v));
	BigDecimal one = new BigDecimal("1");
	return b.divide(one, scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 提供精确的小数位全舍处理。
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 全舍后的结果
     */
    public double rounddown(double v, int scale) {
	if (scale < 0) {
	    throw new IllegalArgumentException(
		    "The scale must be a positive integer or zero");
	}
	BigDecimal b = new BigDecimal(Double.toString(v));
	BigDecimal one = new BigDecimal("1");
	return b.divide(one, scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 获取数据金额类型，小数点后3位四舍五入
     * 
     * @param str
     *            需要转化的数字
     * @return 转化后的数字
     */
    public  String getFormatter(String str) {
	NumberFormat n = NumberFormat.getNumberInstance();
	double d;
	String outStr = null;
	try {
	    d = Double.parseDouble(str);
	    outStr = n.format(d);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return outStr;
    }

    /**
     * 获取精确的数据金额类型，小数点后5位四舍五入
     * 
     * @param str
     *            需要转化的数字
     * @return 转化后的数字
     */
    public  String getDecimalFormat(String str) {
	DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00000");
	String outStr = null;
	double d;
	try {
	    d = Double.parseDouble(str);
	    outStr = fmt.format(d);
	} catch (Exception e) {
	}
	return outStr;
    }

    /**
     * 获取金额
     * 
     * @param str
     *            需要转化的数字
     * @return 转化后的数字
     */
    public  String getCurrency(String str) {
	NumberFormat n = NumberFormat.getCurrencyInstance();
	double d;
	String outStr = null;
	try {
	    d = Double.parseDouble(str);
	    outStr = n.format(d);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return outStr;
    }

     String HanDigiStr[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍",
	    "陆", "柒", "捌", "玖" };
     String HanDiviStr[] = new String[] { "", "拾", "佰", "仟", "万", "拾",
	    "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰",
	    "仟", "万", "拾", "佰", "仟" };

    public  String PositiveIntegerToHanStr(String NumStr) { // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
	String RMBStr = "";
	boolean lastzero = false;
	boolean hasvalue = false; // 亿、万进位前有数值标记
	int len, n;
	len = NumStr.length();
	if (len > 15) {
	    return "数值过大!";
	}
	for (int i = len - 1; i >= 0; i--) {
	    if (NumStr.charAt(len - i - 1) == ' ') {
		continue;
	    }
	    n = NumStr.charAt(len - i - 1) - '0';
	    if (n < 0 || n > 9) {
		return "输入含非数字字符!";
	    }

	    if (n != 0) {
		if (lastzero) {
		    RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
		    // 除了亿万前的零不带到后面
		    // if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
		    // 如十进位前有零也不发壹音用此行
		}
		if (!(n == 1 && (i % 4) == 1 && i == len - 1)) { // 十进位处于第一位不发壹音
		    RMBStr += HanDigiStr[n];
		}
		RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
		hasvalue = true; // 置万进位前有值标记

	    } else {
		if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) { // 亿万之间必须有非零值方显示万
		    RMBStr += HanDiviStr[i]; // “亿”或“万”
		}
	    }
	    if (i % 8 == 0) {
		hasvalue = false; // 万进位前有值标记逢亿复位
	    }
	    lastzero = (n == 0) && (i % 4 != 0);
	}

	if (RMBStr.length() == 0) {
	    return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
	}
	return RMBStr;
    }

    public  String toRMB(double val) {
	String SignStr = "";
	String TailStr = "";
	long fraction, integer;
	int jiao, fen;

	if (val < 0) {
	    val = -val;
	    SignStr = "负";
	}
	if (val > 99999999999999.999 || val < -99999999999999.999) {
	    return "数值位数过大!";
	}
	// 四舍五入到分
	long temp = Math.round(val * 100);
	integer = temp / 100;
	fraction = temp % 100;
	jiao = (int) fraction / 10;
	fen = (int) fraction % 10;
	if (jiao == 0 && fen == 0) {
	    TailStr = "整";
	} else {
	    TailStr = HanDigiStr[jiao];
	    if (jiao != 0) {
		TailStr += "角";
	    }
	    if (integer == 0 && jiao == 0) { // 零元后不写零几分
		TailStr = "";
	    }
	    if (fen != 0) {
		TailStr += HanDigiStr[fen] + "分";
	    }
	}

	// 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
	// if( !integer ) return SignStr+TailStr;

	return "￥" + SignStr + PositiveIntegerToHanStr(String.valueOf(integer))
		+ "元" + TailStr;
    }
    
    /**
     * 两个字符串数字相加
     * @param para1 参数1
     * @param para2 参数2
     * @return 两个字符串数字相加后再转化为字符串
     * */
    public String doubleAdd(String para1, String para2){
    	CommUtil commUtil = new CommUtil();
    	BigDecimal bdPara1 = new BigDecimal(para1);
    	bdPara1 = bdPara1.add(new BigDecimal(para2));
    	return commUtil.getInt(DecimalFormat.getInstance().format(bdPara1)).replaceAll(",", "");
    }
    
    /**
     * 两个字符串数字相减
     * @param para1 参数1
     * @param para2 参数2
     * @return 两个字符串数字相加后再转化为字符串
     * */
    public String doubleSub(String para1, String para2){
    	CommUtil commUtil = new CommUtil();
    	BigDecimal bdPara1 = new BigDecimal(para1);
    	bdPara1 = bdPara1.subtract(new BigDecimal(para2));
    	return commUtil.getInt(DecimalFormat.getInstance().format(bdPara1)).replaceAll(",", "");
    }

    public static void main(String args[]) {
	ArithUtil arith = new ArithUtil();
	System.out.println(arith.doubleAdd("123423451345.00000", "1.0"));
	System.out.println(arith.doubleSub("1234512341234889.00000" , "112"));
//	System.out.println(String.format("%20d",
//		arith.add(6865615615000.00, 48964455100.00)));
	// java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
	// nf.setGroupingUsed(false);
	// System.out.println(nf.format(arith
	// .add(800789998900.01, 855565646432.25)));
	// System.out.println(nf.format(Math.ceil(789654321.123456798)));
	// System.out.println(nf.format(Math.round(789654321.123456798)));
	// System.out.println(nf.format(arith.round(789654321.125456798, 2)));
	// System.out.println(nf.format(arith.roundup(789654321.125456798, 2)));
	// System.out.println(nf.format(arith.rounddown(789654321.125456798,
	// 2)));
	// System.out.println(nf.format(Math.floor(789654321.123456798)));
	// System.out.println(nf.format(arith.mul(789654321.123456798,
	// 1.0000)));
	// System.out.println(nf.format(arith.add(789654321.123456798,
	// 1.0000)));
	// String str = "0600450625465.562689";
	// System.out.println(arith.getFormatter(str));
	// System.out.println(arith.getCurrency(str));
	// System.out.println(arith.getDecimalFormat(str));
	//
	// System.out.println(-99999999999999.999 +
	// arith.toRMB(-99999999999999.999));
	// System.out.println(99999999999999.999 +
	// arith.toRMB(99999999999999.999));
	// System.out.println(arith.toRMB(100000000.00));
	// System.out.println(arith.toRMB(100000001.00));
	// System.out.println(arith.toRMB(236.21));
	// System.out.println(arith.toRMB(100210543.21));
	System.out.println(arith.longThan("100111111111", "-111"));
	
    }
}