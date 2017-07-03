package com.rkylin.wheatfield.service;

import java.util.List;

import com.rkylin.wheatfield.pojo.OpenBankCode;
import com.rkylin.wheatfield.pojo.TlBankCode;

public interface OpenBankCodeService {


	List<OpenBankCode> getBankList(OpenBankCode bankCode);
	/**
	 * 获取总行信息列表
	 * @return
	 */
	List<TlBankCode> getHeadBankCode();
	/**
	 * 获取总行信息列表
	 * @param status 查询总行信息状态
	 * @return
	 */
	List<TlBankCode> getHeadBankCode(Integer status);
	
}
