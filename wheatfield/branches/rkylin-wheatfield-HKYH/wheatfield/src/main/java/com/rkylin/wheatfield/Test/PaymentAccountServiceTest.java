package com.rkylin.wheatfield.Test;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.utils.DateUtil;

public class PaymentAccountServiceTest {
	@Autowired
	PaymentAccountService paymentAccountService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	IErrorResponseService errorResponseService;
	
	private static Logger logger = LoggerFactory.getLogger(PaymentAccountServiceTest.class);	
	DateUtil dateUtil=new DateUtil();
	
	public void recharge(){//充值
//		Object object=new Object();
//		boolean result=true;
//		synchronized (object) {
//			ErrorResponse response=new ErrorResponse();
//			TransOrderInfo transOrderInfo=newTransOrder("4015", "充值");
//			try {
//				response=paymentAccountService.recharge(transOrderInfo, "", "dsf");
//				if(!response.isIs_success()){
//					result=false;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				result=false;
//			}
//		}
//		return result;
		
		TransOrderInfo transOrderInfo=newTransOrder("4015", "充值");
		try {
			paymentAccountService.recharge(transOrderInfo, "", "dsf");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deductXY(){//扣款
		TransOrderInfo transOrderInfo=newTransOrder("4011", "扣款");//信用消费
		
	}
	public void deductCX(){//扣款
		TransOrderInfo transOrderInfo=newTransOrder("4012", "扣款");//储蓄消费
	}
	
	public void withdrow(){//提现
		TransOrderInfo transOrderInfo=newTransOrder("4016", "提现");
		try {
			paymentAccountService.withdrow(transOrderInfo, "dsf",null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rechargeRun(){
		logger.info("账户充值多线程启动");
		int threadNum=10;
		 int trueNum=0;//成功数
		final int falseNum=0;//失败数
		for (int i = 0; i < threadNum; i++) {
			Thread thread=new Thread(new Runnable() {			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					recharge();
					withdrow();
				}
			});
			thread.start();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("账户充值多线程测试，测试总线程数:"+threadNum);
		logger.info("账户充值多线程结束");
	}
	
	
	private TransOrderInfo newTransOrder(String trandType,String remark){
		TransOrderInfo transOrderInfo=new TransOrderInfo();
		
		transOrderInfo.setRequestNo(redisIdGenerator.createRequestNo());
		transOrderInfo.setRequestTime(new Date());
		transOrderInfo.setTradeFlowNo(transOrderInfo.getRequestNo());
		transOrderInfo.setOrderPackageNo(redisIdGenerator.createRequestNo());
		transOrderInfo.setOrderNo(transOrderInfo.getOrderPackageNo());
		transOrderInfo.setOrderDate(new Date());
		transOrderInfo.setOrderAmount(100L);
		transOrderInfo.setOrderCount(1);
		transOrderInfo.setTransType(0);
		transOrderInfo.setFuncCode(trandType);
		transOrderInfo.setInterMerchantCode("1000");
		transOrderInfo.setMerchantCode("1000");
		transOrderInfo.setUserId("dsf");
		transOrderInfo.setAmount(100L);
		transOrderInfo.setFeeAmount(0L);
		transOrderInfo.setUserFee(0L);
		transOrderInfo.setProfit(0L);
		transOrderInfo.setBusiTypeId("9100");
		transOrderInfo.setPayChannelId("1");
		transOrderInfo.setBankCode("102");
		transOrderInfo.setStatus(1);
		transOrderInfo.setErrorCode("S0");
		transOrderInfo.setErrorMsg("正常");
		transOrderInfo.setRemark(remark);
		
		return transOrderInfo;
	}
}
