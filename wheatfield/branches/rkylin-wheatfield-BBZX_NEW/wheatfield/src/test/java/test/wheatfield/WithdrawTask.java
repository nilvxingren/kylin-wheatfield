package test.wheatfield;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.constant.BaseConstants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.AccountInfoManager;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.manager.TransDaysSummaryManager;
import com.rkylin.wheatfield.manager.TransOrderInfoManager;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.TransDaysSummary;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.service.GenerationPaymentService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.utils.DateUtil;

public class WithdrawTask extends BaseJUnit4Test {

	@Autowired
	TransOrderInfoManager transOrderInfoManager;
	@Autowired
	GenerationPaymentService generationPaymentService;
//	@Autowired
//	GenerationPaymentManager generationPaymentManager;
	@Autowired
	OperationServive operationServive;
	@Autowired
	TransDaysSummaryManager transDaysSummaryManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	AccountInfoManager accountInfoManager;
	
	DateUtil dateUtil=new DateUtil();
	/** 日志对象 */
	private static final Logger logger = LoggerFactory.getLogger(WithdrawTask.class);
	/**
	 * 提现通知代收付
	 */	
	@Test
	public void withdraw(){
		logger.info("提现汇总TASK启动");
		if(null!=transOrderInfoManager){
			logger.info("transOrderInfoManager不为空");
		}else{
			logger.info("transOrderInfoManager为空");
		}
		
		//获取订单交易表中的用户提现数据
		 /*获取账单日期      日期减一*/
		try {
			Date accountDate=dateUtil.parse(operationServive.getAccountDate(), "yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(accountDate);
			cal.add(cal.DATE, -1);//设置账单日期减一
			accountDate=cal.getTime();
			//记录提现账单汇总
			List<TransDaysSummary> transDaysSummaries=new ArrayList<TransDaysSummary>();
			TransOrderInfoQuery query=new TransOrderInfoQuery();
			query.setAccountDate(accountDate);
			query.setFuncCode(TransCodeConst.WITHDROW);
			//获取订单记录查询结果
			List<TransOrderInfo> transOrderInfos=transOrderInfoManager.queryListGroup(query);
			if(null!=transOrderInfos&&0<transOrderInfos.size()){
				String userId="";//用户Id
				long amount=0;//用户金额
				StringBuilder sbStr=new StringBuilder();//记录订单号，以逗号分隔
				int i=0;
				//统计用户提现数据
				for (TransOrderInfo transOrderInfo : transOrderInfos) {
					if (userId.equals(transOrderInfo.getUserId())) {//
						amount+=amount;//金额累加
						sbStr.append(transOrderInfo.getOrderNo()+",");
					} else {
						if(0!=i){
							//提现汇总表中写数据
							TransDaysSummary transDaysSummary=new TransDaysSummary();
							transDaysSummary.setTransSumId(redisIdGenerator.createRequestNo());//汇总订单号
							transDaysSummary.setOrderType(TransCodeConst.WITHDROW);//订单类型--提现
							transDaysSummary.setAccountDate(accountDate);//账单日期
							transDaysSummary.setSummaryAmount(amount);//汇总金额
							transDaysSummary.setSummaryOrders(sbStr.toString());//订单Id（汇总）
							transDaysSummary.setUserId(userId);
							transDaysSummaries.add(transDaysSummary);
						}						
						userId=transOrderInfo.getUserId();
						amount=transOrderInfo.getAmount();
						i=1;
					}
				}
			}			
			//调用代收付接口写入代收付数据
			if(null!=transDaysSummaries&&0<transDaysSummaries.size()){
				for (TransDaysSummary transDaysSummary : transDaysSummaries) {
					//获取用户绑卡信息
					AccountInfoQuery accountInfoQuery=new AccountInfoQuery();
					accountInfoQuery.setAccountPurpose(BaseConstants.BANKCARD_TYPE_SETTLE);//清算卡类型
					accountInfoQuery.setFinAccountName(transDaysSummary.getUserId());//用户Id
					List<AccountInfo> accountInfos=accountInfoManager.queryViewByUserIdAndPurpose(accountInfoQuery);
					if(null!=accountInfos&&0<accountInfos.size()){
						AccountInfo accountInfo=accountInfos.get(0);
						//准备数据调用接口写入代收付
						GenerationPayment generationPayment=new GenerationPayment();
						generationPayment.setOrderNo(transDaysSummary.getTransSumId());//订单号
						generationPayment.setOrderType(SettleConstants.ORDER_WITHDRAW);//订单类型
						generationPayment.setUserId(transDaysSummary.getUserId());
						generationPayment.setBankCode(accountInfo.getBankHead());
						generationPayment.setAccountType(accountInfo.getAccountTypeId());
						generationPayment.setAccountNo(accountInfo.getAccountNumber());
						generationPayment.setAccountName(accountInfo.getAccountName());
						generationPayment.setAccountProperty(accountInfo.getAccountProperty());
						generationPayment.setProvince(accountInfo.getBankProvince());
						generationPayment.setCity(accountInfo.getBankCity());
						generationPayment.setOpenBankName(accountInfo.getBankHeadName());
						generationPayment.setPayBankCode(accountInfo.getBankBranch());
						generationPayment.setAmount(transDaysSummary.getSummaryAmount());
						generationPayment.setCurrency(accountInfo.getCurrency());
						generationPayment.setCertificateType(accountInfo.getCertificateType());
						generationPayment.setCertificateNumber(accountInfo.getCertificateNumber());
						generationPayment.setSendType(SettleConstants.SEND_NORMAL);
						generationPayment.setStatusId(SettleConstants.TAKE_EFFECT);
						generationPayment.setAccountDate(accountDate);						
						//String result= generationPaymentService.payMentResultTransform(generationPayment);
						generationPaymentService.payMentResultTransform(generationPayment);
						//判断返回结果，ok 结束  ，其他状态需要调用冲正接口
//						if(!"ok".equals(result)){
//							
//						}
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		logger.info("提现TASK结束");
	}
	
}
