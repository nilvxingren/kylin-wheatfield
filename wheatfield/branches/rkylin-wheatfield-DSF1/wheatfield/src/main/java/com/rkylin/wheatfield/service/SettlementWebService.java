package com.rkylin.wheatfield.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface SettlementWebService {
	/**
	 * 通过画面上传分润文件 更新用户余额
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> updateSettleFile(String invoicedate,String fileName);
	
	@Transactional
	boolean fileUpload(MultipartFile fileUpload,String fileName);

	/**
	 * 通过画面上传分润文件 更新用户余额
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> correct(String newOrderNo,String oldOrderNo,String rootInstCd,String funcCode);
	
}
