package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;
import com.rkylin.wheatfield.response.InterestRepaymentResponse;

/**
 * 
 * @author Yang
 *
 */
public interface InterestRepaymentService {
	/**
	 * 查询退款信息
	 * @param query
	 * @return
	 */
	@Transactional
	public List<InterestRepayment> getInterestRepayment(InterestRepaymentQuery query) throws Exception;
	/**
	 * 创建还款信息文件并上传服务器
	 * @param rootInstCd 管理机构代码,例丰年,会唐,课栈
	 * @param providerId 授信提供方ID,例JRD
	 * @return rtnMap
	 */
	@Transactional
	public Map<String, String> uploadIneterestRepaymentFile(String rootInstCd, String providerId);
	/***
	 * 查询是否可以提前还款
	 * @param query
	 * @return true可以; false不可以
	 */
	@Transactional
	public Map<String, String> canBeEarlyRepayment(String userId, String userOrderId, String rootInstId, InterestRepaymentResponse response);
}
