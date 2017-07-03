package com.rkylin.wheatfield.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("settlementUtils")
public class SettlementUtils {

	/**
	 * 取得账期
	 * 
	 * @param 
	 * @return
	 */
	public String getAccountDate(String accountDate,String fromat,int step) throws Exception {
		Calendar c = Calendar.getInstance();
    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter = new SimpleDateFormat(fromat);
    	
		c.setTime(formatter1.parse(accountDate));
		c.add(Calendar.DAY_OF_MONTH, step);
		
		return formatter.format(c.getTime());
	}
	
	/** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public void copyFile(String oldPath, String newPath) throws Exception { 
       int bytesum = 0; 
       int byteread = 0; 
       File oldfile = new File(oldPath); 
       if (oldfile.exists()) { //文件存在时 
           InputStream inStream = new FileInputStream(oldPath); //读入原文件 
           OutputStream fs = new FileOutputStream(newPath); 
           byte[] buffer = new byte[1444]; 
           int length; 
           while ( (byteread = inStream.read(buffer)) != -1) { 
               bytesum += byteread; //字节数 文件大小 
               fs.write(buffer, 0, byteread); 
           } 
           inStream.close(); 
       }
   } 

   /** 
     * 复制整个文件夹内容 
     * @param oldPath String 原文件路径 如：c:/fqf 
     * @param newPath String 复制后路径 如：f:/fqf/ff 
     * @return boolean 
     */ 
   public void copyFolder(String oldPath, String newPath) { 

       try { 
           (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
           File a=new File(oldPath); 
           String[] file=a.list(); 
           File temp=null; 
           for (int i = 0; i < file.length; i++) { 
               if(oldPath.endsWith(File.separator)){ 
                   temp=new File(oldPath+file[i]); 
               } 
               else{ 
                   temp=new File(oldPath+File.separator+file[i]); 
               } 

               if(temp.isFile()){ 
                   FileInputStream input = new FileInputStream(temp); 
                   FileOutputStream output = new FileOutputStream(newPath + "/" + 
                           (temp.getName()).toString()); 
                   byte[] b = new byte[1024 * 5]; 
                   int len; 
                   while ( (len = input.read(b)) != -1) { 
                       output.write(b, 0, len); 
                   } 
                   output.flush(); 
                   output.close(); 
                   input.close(); 
               } 
               if(temp.isDirectory()){//如果是子文件夹 
                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
               } 
           } 
       } 
       catch (Exception e) { 
           System.out.println("复制整个文件夹内容操作出错"); 
           e.printStackTrace(); 

       }
   }
   
	/** 
    * 替换字符串 
    * @param str String 字符串 
    * @param restr String 替换字符串
    * @return String 
    */ 
  public String nvl(Object str, String restr) throws Exception { 
      if (str == null || "".equals(str)) {
    	  return restr;
      }
      return str.toString();
  }
  
	/** 
   * 补充字符串 
   * @param str String 字符串 
   * @param len String 长度
   * @param len int step
   * @return String 
   */ 
 public String pad(String str, String len,int i) throws Exception { 
	int srtint;
	int lenint;
	int strlenint;
	if (isValidInt(str)) {
		srtint = Integer.parseInt(str);
		srtint = srtint + i;
		str = String.valueOf(srtint);
		lenint = Integer.parseInt(len);
		strlenint=str.length();
		if (strlenint>lenint) {
			return str.substring(0,lenint);
		}else{
			for (int ii =0;ii<lenint-strlenint;ii++) {
				str = "0"+str;
			}
			return str;
		}
	} else {
		return str;
	}
 }
 
 /*** 
  * 判断 String 是否是 int 
  *  
  * @param input 
  * @return 
  */  
 public boolean isValidInt(String value) {  
     try {  
         Integer.parseInt(value);  
     } catch (NumberFormatException e) {  
         return false;  
     }  
     return true;  
 }  
}
