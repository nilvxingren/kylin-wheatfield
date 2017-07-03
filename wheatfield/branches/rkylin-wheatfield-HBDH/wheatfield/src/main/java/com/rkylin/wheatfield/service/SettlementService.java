package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.pojo.CreditInfo;
import com.rkylin.wheatfield.response.ErrorResponse;

/**
 * 清结算对内提供接口
 * @author  zhenpc
 * @version 2015年2月9日 上午11:00:12
 */

public interface SettlementService {
	/**
	 * 获取授信文件并更新指定用户的授信额度
	 * @return 返回已更新用户信息
	 * */
	@Transactional
	List<CreditInfo> getCreditInfo(String invoicedate);

	/**
	 * 生成给丰年的对账文件(不包含充值)
	 * @return 文件名
	 * */
	@Transactional
	String createDebtAccountFile(String invoicedate);

	/**
	 * 生成给丰年的对账文件(充值)
	 * @return 文件名
	 * */
	@Transactional
	String createDebtAccountFile2(String invoicedate);
	
	/**
	 * 生成给P2P的债权包的交易
	 * @return 文件名
	 * */
	@Transactional
	String createP2PDebtFile(String invoicedate);
	

	/**
	 * 根据债权包文件插入代收付表
	 * @return 文件名
	 * */
	@Transactional
	ErrorResponse readP2PDebtFile(String invoicedate);
	
	/**
	 * 每日定时任务生成用户还款文件
	 * @return 文件名
	 * */
	@Transactional
	String createPaymentFile(String invoicedate);

	/**
	 * 读取上游渠道的对账文件
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> readCollateFile(String invoicedate);
	
	/**
	 * 读取丰年的对账结果
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> readCollateResFile(String invoicedate,String batch);
	
	/**
	 * 通过代扣结果更新用户余额
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> updateCreditAccount();
	
	/**
	 * 通过分润文件更新用户余额
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> updateSettleFile(String invoicedate,String batch);

	/**
	 * 计息存储过程调用
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> proRepayment();

	/**
	 * 读取上游渠道的对账文件
	 * @return 返回码&信息
	 * */
	@Transactional
	Map<String,String> readCollateFileHT(String invoicedate,String rootInstCd,String fileType,String batch,String wyFlg);

	/**
	 * 生成给会唐的对账文件(充值)
	 * @return 文件名
	 * */
	@Transactional
	String createDebtAccountFileHT(String invoicedate,String rootInstCd,String fileType);
	
	/**
	 * 生成给会唐的对账文件(充值)
	 * @return 文件名
	 * */
	@Transactional
	String createDebtAccountFileForAll(String invoicedate,String rootInstCd,String fileType);
	
}
