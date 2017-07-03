package com.rkylin.wheatfield.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rkylin.wheatfield.common.DateUtils;


/**
 * 常见的辅助类
 * 
 */
public class CommUtil {

	private static Log log = LogFactory.getLog(CommUtil.class);
	
	
	public static void main(String[] args) {
        System.out.println(getGenerateId(new HashSet<String>()).length());
    }
	
	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public boolean isEmpty(Object pObj) {
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

   public static boolean isEmp(Object pObj) {
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
	
    public static <T> T getArrayOneVal(T[] array){
        if (array==null||array.length==0) {
            return null; 
        }
        return array[0];
    }
	
    public static boolean isNum(String str) {
        if (str==null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
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
	
//	public static void main(String[] args) {
//		Set set = new HashSet();
//		for (int i = 0; i < 1000; i++) {
//			System.out.println(getGenerateId(set));
//		}
//	}

	/**  (不要再使用此方法，使用此类其他有maxLength 参数的方法)
	 * 生成20位以内唯一数    毫秒数+线程数+随机数  在一个循环中调用的时候使用
	 * @param idSet(定义在循环体外面，new一个) 在同一个循环中使用的时候，把每次生成的id放入Set,如果本次生成的已经在set中存在，则会重新生成，直到原set中不存在
	 * @return
	 */
	public static String getGenerateId(Set<String> idSet){
		Random random = new Random();
		int randomNumber =  random.nextInt(99999)%(99999-1) + 1;
		String id = ""+ System.currentTimeMillis()+Thread.currentThread().getId()+randomNumber;
		if (id.length()>20) {
		    id=id.substring(0, 20); 
        }
		if (!idSet.contains(id)) {
			idSet.add(id);
		}else{
			return getGenerateId(idSet);
		}
		return id;
	}
	
	/**
	 * 生成唯一数    毫秒数+线程数+随机数  在一个循环中调用的时候使用
	 * @param idSet(定义在循环体外面，new一个) 在同一个循环中使用的时候，把每次生成的id放入Set,如果本次生成的已经在set中存在，则会重新生成，直到原set中不存在
	 * @return
	 */
	public static String getGenerateNum(Set<String> numSet,int maxLength){
		String num = getGenerateNum(maxLength);
		if (!numSet.contains(num)) {
			numSet.add(num);
		}else{
			getGenerateNum(numSet,maxLength);
		}
		return num;
	}
	
	/**
	 * 	生成唯一数
	 * @param maxLength  随机数的长度
	 * @return	毫秒数+线程数+随机数
	 */
	public static String getGenerateNum(int maxLength){
		String id = ""+ System.currentTimeMillis()+Thread.currentThread().getId()+getRandomNum(maxLength);
		return id;
	}
	
	/**
	 * 根据传入的数字获取这个值长度内的随机数
	 * @param maxLength
	 * @return
	 */
	public static int getRandomNum(int maxLength){
		int max = getMaxInt(maxLength);
		Random random = new Random();
		int randomNumber =  random.nextInt(max)%(max-1) + 1;
		return randomNumber;
	}
	
	/**
	 * 根据传入的位数获取这个位数最大的整数
	 * @param maxLength
	 * @return
	 */
	public static int getMaxInt(int maxLength){
		int max = 0;
		for (int i = maxLength; i>0; i--) {
			max += Math.pow(10, i-1)*9;
		}
		return max;
	}
	
	/**(不要再使用此方法，使用此类其他有maxLength 参数的方法)
	 * 生成20位以内唯一数(非循环中使用)    毫秒数+线程数+随机数
	 * @return
	 */
	public static String getGenerateId(){
		Random random = new Random();
		int randomNumber =  random.nextInt(99999)%(99999-1) + 1;
		String id = ""+ System.currentTimeMillis()+Thread.currentThread().getId()+randomNumber;
		return id;
	}
	
    /**
     * 获取map的键值(rop输入参数的打印)
     * @param paramMap
     * @return
     */
    public static String getMapKeyVal(Map<String, String[]> paramMap){
        if (paramMap==null||paramMap.size()==0) {
            return "";
        }
        StringBuffer paramsBuffer = new StringBuffer();
        for (Object keyObj : paramMap.keySet().toArray()) {
            String[] strs = paramMap.get(keyObj);
            if (strs == null) {
                continue;
            }
            for (String value : strs) {
                paramsBuffer.append(keyObj + "=" + value + ", ");
            }
        }
        return paramsBuffer.toString();
    }
    
    /**
     * 获取一个数组的第一位的值
     * Discription:
     * @param array
     * @return String
     * @author Achilles
     * @since 2016年8月29日
     */
    public static String getArrayOneVal(String[] array){
        if (array==null||array.length==0) {
            return null; 
        }
        return array[0];
    }
}