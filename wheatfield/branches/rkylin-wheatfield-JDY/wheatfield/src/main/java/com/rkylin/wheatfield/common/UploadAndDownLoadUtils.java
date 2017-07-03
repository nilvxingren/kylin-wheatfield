package com.rkylin.wheatfield.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.domain.FileUrl;
import com.Rop.api.request.FsFileUploadRequest;
import com.Rop.api.request.FsFileurlGetRequest;
import com.Rop.api.request.FsUrlkeyGetRequest;
import com.Rop.api.response.FsFileUploadResponse;
import com.Rop.api.response.FsFileurlGetResponse;
import com.Rop.api.response.FsUrlkeyGetResponse;
import com.rkylin.wheatfield.constant.SettleConstants;

public class UploadAndDownLoadUtils {
	private static Logger logger = LoggerFactory.getLogger(UploadAndDownLoadUtils.class);	
//	static {
//		try {
//			Map<String, Object> keyMap = RSAUtils.genKeyPair();
//			publicKey = RSAUtils.getPublicKey(keyMap);
//			privateKey = RSAUtils.getPrivateKey(keyMap);
//			System.err.println("公钥:\n" + publicKey);
//			System.err.println("私钥:\n" + privateKey);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * 
	 * @param path本地文件路径
	 * @param type 参考rop平台注释
	 * @param invaiceDate 帐期 : 格式 yyyy-MM-dd
	 * @param batch 批次 --可选
	 * @param jsonOrXml 返回格式"json"   "xml"
	 * @param outFileName 写入的文件路径
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(String path, int type, Date invaiceDate,
			String batch, String jsonOrXml,String priOrPubKey,int flg,String appKey,String appSecret,String ropUrl)
			throws Exception {
//		if(type<=4){
//			publicKey=p2p_publicKey;
//		}else if (type == 11||type==12) {
//			privateKey=fn_privateKey;
//		}else{
//			publicKey=rky_publicKey;
//		}
		String path1 = SettleConstants.FILE_UP_PATH +"beifen";
		logger.info("-----备份地址-----"+path1);
		File filePath1 = new File(path1);
		if (!filePath1.exists()) {
			filePath1.mkdirs();
		}
//		path1=path1+File.separator + "FN_PANYMENT_"+DateUtils.getyyyyMMdd(Constants.DATE_FORMAT_YYYYMMDD)+PartyCodeUtil.getRandomCode()+".csv";
		path1=path1 + path.substring(path.lastIndexOf(File.separator),path.length());
		File fPath1=new File(path1);
		File fPath=new File(path);
		fileChannelCopy(fPath, fPath1);
		File inputFile = new File(path);
		byte[] data = readFromFile(inputFile);
		byte[] encodedData;
		if(flg==1){
			encodedData = RSAUtils.encryptByPrivateKey(data, priOrPubKey);
		}else{
			encodedData = RSAUtils.encryptByPublicKey(data, priOrPubKey);
		}
		writeToFile(encodedData, inputFile);
		String urlKey = "";
		UploadAndDownLoadUtils uploadAndDownLoadUtils = new UploadAndDownLoadUtils();
		try {
			urlKey = uploadAndDownLoadUtils.uploadLogic(path, type, invaiceDate,
				 batch, jsonOrXml, priOrPubKey, flg, appKey, appSecret, ropUrl);
		} catch (Exception e1) {
			try {
				urlKey = uploadAndDownLoadUtils.uploadLogic(path, type, invaiceDate,
						 batch, jsonOrXml, priOrPubKey, flg, appKey, appSecret, ropUrl);
				logger.info("-----上传失败---异常后第二次上传---msg:"+urlKey);
			} catch (Exception e2) {
				logger.error("-----上传异常两次失败---");
			}
		}
		return urlKey;
	}
	
	private String uploadLogic(String path, int type, Date invaiceDate,
			String batch, String jsonOrXml,String priOrPubKey,int flg,String appKey,String appSecret,String ropUrl) throws Exception {
		String urlKey = "";
		DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,
				appSecret, jsonOrXml);
		FsFileUploadRequest fileUpload = new FsFileUploadRequest();
		fileUpload.setBatch(batch);
		fileUpload.setInvoice_date(invaiceDate);
		fileUpload.setPath(path);
		fileUpload.setType(type);
		
		for (int i = 0; i < 3; i++) {
			urlKey = "";
			FsFileUploadResponse rsp = ropClient.execute(fileUpload,
					SessionUtils.sessionGet(ropUrl,appKey,appSecret));
			if (rsp != null) {
				if(rsp.isSuccess()){
					urlKey=rsp.getUrl_key();
					break;
				}else{
					urlKey="no-"+rsp.getErrorCode()+":"+rsp.getMsg();
					logger.info("-----上传失败---"+i+"---msg:"+urlKey);
				}
			} else {
				urlKey = "";
				rsp = ropClient.execute(fileUpload,
						SessionUtils.sessionGet(ropUrl,appKey,appSecret));
				if (rsp != null) {
					if(rsp.isSuccess()){
						urlKey=rsp.getUrl_key();
					}else{
						urlKey="no-"+rsp.getErrorCode()+":"+rsp.getMsg();
						logger.info("-----上传失败---NULL后第二次上传---msg:"+urlKey);
					}
				}
			}
		}
		return urlKey;
	}
	/**
	 * 
	 * @param filePath 本地路径
	 * @param jsonOrXml
	 * @param urlKey 用这个getUrlKey()方法获取urlList 
	 * @throws Exception
	 */
	public static String downloadFile(String filePath,String jsonOrXml,String urlKey,String nameAndSuffix,HttpServletRequest request,int type,String priOrPubKey,int flg
			,String appKey,String appSecret,String ropUrl)
			throws Exception {
//		if(type<=4){
//			publicKey=p2p_publicKey;
//		}else if (type == 13||type==14||type==15) {
//			privateKey=fn_privateKey;
//		}else{
//			publicKey=rky_publicKey;
//		}
		String resultUrl="";
		DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,
				appSecret, jsonOrXml);
		String fileUrl="";
		FsFileurlGetRequest fileurlRequest=new FsFileurlGetRequest();
		fileurlRequest.setUrl_key(urlKey);
		try {
			FsFileurlGetResponse filrUrlResponse=ropClient.execute(fileurlRequest, SessionUtils.sessionGet(ropUrl,appKey,appSecret));
			if(filrUrlResponse!=null){
				fileUrl=filrUrlResponse.getFile_url();
//				fileUrl=fileUrl.substring(0, fileUrl.lastIndexOf("?"));
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fos=null;
		InputStream inputStream=null;
		try{
			URL url = new URL(fileUrl);  
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	                //设置超时间为3秒
			conn.setConnectTimeout(3*1000);
			//防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			//得到输入流
			 inputStream = conn.getInputStream();  
			//获取自己数组
			byte[] encodedData = readInputStream(inputStream);    
			byte[] dencodedDate;
			if(flg==1){
				dencodedDate=RSAUtils.decryptByPrivateKey(encodedData, priOrPubKey);
			}else{
				dencodedDate=RSAUtils.decryptByPublicKey(encodedData, priOrPubKey);
			}
			//文件保存位置
			String path="";
			if (type!=16 && type!=8 && type!=9 && type!=26 && type!=28 && type!=20) {
				path=request.getSession().getServletContext().getRealPath("/");
				path=path+".."+File.separator+".."+File.separator+".."+File.separator+"download"+File.separator;
			} else {
				path = SettleConstants.FILE_PATH+"TLbeifen"+File.separator;
			}
			resultUrl=path+filePath;
			File saveDir = new File(resultUrl);
			if(!saveDir.exists()){
				saveDir.mkdirs();
			}
			File file = new File(saveDir+File.separator+nameAndSuffix);    
			writeToFile(dencodedDate, file);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fos!=null){
				fos.close();  
			}
			if(inputStream!=null){
				inputStream.close();
			}
		}
		return resultUrl;
	}
	public static void writeToFile(byte[] content, File outFileName) {
		// 创建输出流
		FileOutputStream outStream;
		try {
			outStream = new FileOutputStream(outFileName);
			// 写入数据
			outStream.write(content);
			// 关闭输出流
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static byte[] readFromFile(File fileName) throws Exception {

		FileInputStream fis = new FileInputStream(fileName);
		byte[] buf = new byte[(int) fileName.length()];
		fis.read(buf);
		fis.close();

		return buf;
	}
	/**
	 * 获取下载的key
	 * @param type
	 * @param invoiceDate
	 * @param batch
	 * @param jsonOrXml
	 * @return
	 */
	public static List<FileUrl> getUrlKeys(int type,Date invoiceDate,String batch,String jsonOrXml,String appKey,String appSecret,String ropUrl){
		List<FileUrl> urlList=new ArrayList<FileUrl>();
		DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey,
				appSecret, jsonOrXml);
		FsUrlkeyGetRequest urlKeyGet=new FsUrlkeyGetRequest();
		urlKeyGet.setBatch(batch);
		urlKeyGet.setInvoice_date(invoiceDate);
		urlKeyGet.setType(type);
		try {
			FsUrlkeyGetResponse urlKeyResponse=ropClient.execute(urlKeyGet, SessionUtils.sessionGet(ropUrl,appKey,appSecret));
			if (urlKeyResponse != null) {
				urlList=urlKeyResponse.getFileurls();
			}
		} catch (ApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return urlList;
		
	}
	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
		byte[] buffer = new byte[1024];  
		int len = 0;  
		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		while((len = inputStream.read(buffer)) != -1) {  
			bos.write(buffer, 0, len);  
		}  
		bos.close();  
		return bos.toByteArray();  
	}
	
	public static void main(String[] args) throws Exception {
//		File inputFile = new File("E:/fea66dd9-65ef-4e2c-af0a-e9b2358ec2c2.csv");
//    	File tmpFile = new File("d:/temp.rar");
//    	File outputFile = new File("E:/ss.csv");
//        byte[] data = readFromFile(inputFile);
////        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
////        writeToFile(encodedData, tmpFile);
//        byte[] decodedData = RSAUtils.decryptByPublicKey(data, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClE5O2ZxaU6RFcnTz1PX7Muo0v0dya01jiZh9CxLEO+1Xby0EBXA1Ycr5b9rO4eWG5hT44xMimRN66DVU5nz/M3pnAXIrppY7/UcOF29268HYs2Yc98Ad1pLwWTuN6nSGCXq/0nkCXzGwTIINnpGFYJ4oQV7168TlmsrEo8AePfQIDAQAB");
//        writeToFile(decodedData, outputFile);
//        String urlkey=uploadFile("G:/dd/333.csv", 8, DateUtils.getDate("2013-03-25", Constants.DATE_FORMAT_YYYYMMDD),PartyCodeUtil.getRandomCode() , "json");
//        System.err.println(urlkey);
//		Map<String, Object> keyMap = RSAUtils.genKeyPair();
//		publicKey = RSAUtils.getPublicKey(keyMap);
//		privateKey = RSAUtils.getPrivateKey(keyMap);
//		System.err.println("公钥:\n" + publicKey);
//		System.err.println("私钥:\n" + privateKey);
	}
	 public static void fileChannelCopy(File s, File t) {
	        FileInputStream fi = null;
	        FileOutputStream fo = null;
	        FileChannel in = null;
	        FileChannel out = null;
	        try {
	            fi = new FileInputStream(s);
	            fo = new FileOutputStream(t);
	            in = fi.getChannel();//得到对应的文件通道
	            out = fo.getChannel();//得到对应的文件通道
	            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                fi.close();
	                in.close();
	                fo.close();
	                out.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	
}
