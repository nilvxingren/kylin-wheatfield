package com.rkylin.wheatfield.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.common.AccountUtils;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.service.FinanaceEntryService;
import com.rkylin.wheatfield.utils.DateUtil;


@Service("finanaceEntryServie")
public class FinanaceEntryServiceImpl implements FinanaceEntryService {
	private static Logger logger = LoggerFactory.getLogger(FinanaceEntryServiceImpl.class);
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	ParameterInfoManager parameterInfoManager;
    
	
	@Override
	/**
	 * quotaFlag  0 :额度  1 ：交易发生金额
	 */
	public FinanaceEntry getAccountFlow(TransOrderInfo transOrderInfo, Balance balance, String entryId, boolean flag, String quotaFlag) {
		FinanaceEntry rtnFlow = new FinanaceEntry();
		DateUtil dateUtil = new DateUtil();
		String finEntryId = redisIdGenerator.createFINEntryrRequestNo();
		if (null != finEntryId) {
			rtnFlow.setFinanaceEntryId(finEntryId);// 设置流水主键
			// 获取记账日期
			String accountDateString = this.getAccountDate();
			if (accountDateString == null || accountDateString.equals("")) {
				return null;
			}
			try {
				rtnFlow.setAccountDate(dateUtil.parse(accountDateString, "yyyy-MM-dd"));
			} catch (ParseException e) {
				e.printStackTrace();
			} // 设置记账日期
			String accountId = balance.getFinAccountId();// 获取账户ID

			if (null != accountId) {

				rtnFlow.setFinAccountId(accountId);// 设置账户ID
				rtnFlow.setReferEntryId(entryId);// 设置套录号

				String tradeType = transOrderInfo.getFuncCode();

				rtnFlow.setDirection(AccountUtils.getDorC(tradeType, flag));// 设置DC标志

				/* 账户余额 */
				rtnFlow.setAmount(balance.getAmount());

				/* 可用余额 */
				rtnFlow.setBalanceUsable(balance.getBalanceUsable());

				/* 清算余额 */
				rtnFlow.setBalanceSettle(balance.getBalanceSettle());

				/* 冻结余额 */
				rtnFlow.setBalanceFrozon(balance.getBalanceFrozon());

				/* 透支额度 */
				rtnFlow.setBalanceOverLimit(balance.getBalanceOverLimit());

				/* 贷记余额 */
				rtnFlow.setBalanceCredit(balance.getBalanceCredit());

				// ************************************************************
				if (Integer.valueOf(transOrderInfo.getFuncCode()) == BaseConstants.REVERSE_TYPE) {
					// 冲正交易
					rtnFlow.setReverseFlag(BaseConstants.REVERSE_TYPE);
				} else {
					// 非冲正交易
					rtnFlow.setReverseFlag(BaseConstants.NOREVERSE_TYPE);
				}

				// rtnFlow.setPartyIdFrom(transOrderInfo.getUserId());//设置交易发起方
				rtnFlow.setPartyIdFrom(balance.getFinAccountId());// 设置交易发起方
				rtnFlow.setReferId(transOrderInfo.getOrderNo());// 记账凭证号
				/* 暂时定义为订单来源 */
				rtnFlow.setReferFrom(String.valueOf(BaseConstants.REFER_TRADE));// 设置凭证来源
				if("0".equals(quotaFlag)){// 额度
					rtnFlow.setPaymentAmount(balance.getBalanceSettle());// 获取交易额度
				} if("1".equals(quotaFlag)) {// 非额度
					rtnFlow.setPaymentAmount(transOrderInfo.getAmount());// 获取交易发生额
				}
				
				rtnFlow.setTransDate(transOrderInfo.getRequestTime());// 设置交易日期
				rtnFlow.setThirdPartyId(AccountUtils.getThirdPartyId(transOrderInfo, flag));// 交易相关方
				if (TransCodeConst.FROZEN.equals(transOrderInfo.getFuncCode())) {
					rtnFlow.setMyNotes(TransCodeConst.FROZEN);// 我方摘要
				} else {

				}
				rtnFlow.setYourNotes("");// 你方摘要
				rtnFlow.setHisNote("");// 他方摘要
				if (transOrderInfo.getMerchantCode() == null) {
					rtnFlow.setRemark("");// 其他备注
				} else {
					rtnFlow.setRemark(transOrderInfo.getRemark());// 其他备注
				}
				rtnFlow.setRecordMap("");// 设置安全码
				rtnFlow.setPulseTime(String.valueOf(new Date().getTime()));// 设置账户心跳时间
				rtnFlow.setPulseDegree(balance.getPulseDegree());// 账户心跳计次
			} else {
				logger.error("获取用户[" + accountId + "的账户ID失败");
				rtnFlow = null;
			}
		}

		return rtnFlow;
	}

	@Override
	public String getAccountDate() {
		String accountDate = null;
		ParameterInfoQuery query = new ParameterInfoQuery();
		query.setParameterCode(TransCodeConst.ACCOUNTDATE_CODE);
		List<ParameterInfo> parameterInfos = parameterInfoManager.queryList(query);
		if (parameterInfos != null && parameterInfos.size() > 0) {
			accountDate = parameterInfos.get(0).getParameterValue();
		}
		return accountDate;
	}

}
