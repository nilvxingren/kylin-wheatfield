package com.rkylin.wheatfield.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.database.BaseDao;
import com.rkylin.utils.RkylinMailUtil;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBatchResultManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBatchResult;
import com.rkylin.wheatfield.pojo.SettleBatchResultQuery;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.InterestRepaymentService;
import com.rkylin.wheatfield.service.SettlementService;
import com.rkylin.wheatfield.service.SettlementServiceSec;
import com.rkylin.wheatfield.service.SettlementServiceThr;
import com.rkylin.wheatfield.service.impl.GenerationPaymentServiceImpl;
import com.rkylin.wheatfield.settlement.ProfitLogic;
import com.rkylin.wheatfield.utils.SettlementUtils;

@Component("settlementTask")
public class SettlementTask extends BaseDao {

	@Autowired
	SettlementService settlementService;
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	Properties userProperties;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	GenerationPaymentServiceImpl generationPaymentService;
	@Autowired
	SettlementServiceSec settlementServiceSec;
	@Autowired
	SettlementServiceThr settlementServiceThr;
	@Autowired
	private InterestRepaymentService interestRepaymentService;
	@Autowired
	SettleBatchResultManager settleBatchResultManager;
	@Autowired
	ProfitLogic profitLogic;
	
	/** 日志对象 */
	private static final Logger logger = LoggerFactory.getLogger(SettlementTask.class);
	private String filetype;//文件类型
	private String batch;//批次号
	private String invoicedate;//账期
	private String batchType;//批次号类型
	private String rootinstcd;//机构号
	private String datetype;//当日标记
	public String getDatetype() {
		return datetype;
	}

	public void setDatetype(String datetype) {
		this.datetype = datetype;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getRootinstcd() {
		return rootinstcd;
	}

	public void setRootinstcd(String rootInstCd) {
		this.rootinstcd = rootInstCd;
	}

	private static Object lock = new Object();

	
	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getInvoicedate() {
		return invoicedate;
	}

	public void setInvoicedate(String invoicedate) {
		this.invoicedate = invoicedate;
	}

	public void collate_FN_FC() {
		logger.info("丰年（非充值）对账计划任务-----------START----------------"+new Date());
		settlementService.createDebtAccountFile(null);
		logger.info("丰年（非充值）对账计划任务-----------END----------------"+new Date());
	}
	
	public void collate_JRD_FC() {
		logger.info("君融贷（非充值）对账计划任务-----------START----------------"+new Date());
		settlementService.createDebtAccountFileForAll(null,Constants.JRD_ID,"33");
		logger.info("君融贷（非充值）对账计划任务-----------END----------------"+new Date());
	}
	
	public void collate_TLHT_C() {
		logger.info("通联HT对账计划任务-----------START----------------"+new Date());
		Map<String,String> rtnMap = settlementService.readCollateFileHT(null,"'"+Constants.HT_ID+"','"+Constants.KZ_ID+"'","26","","B1");
		if (rtnMap !=null && "0000".equals(rtnMap.get("errCode"))) {
			settlementService.createDebtAccountFileHT(null,Constants.HT_ID,"27");
		}else {
			if (rtnMap !=null) {
				RkylinMailUtil.sendMailThread("通联HT对账计划任务","通联HT对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]", "21401233@qq.com");
				logger.error("通联HT对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]");
			} else {
				RkylinMailUtil.sendMailThread("通联HT对账计划任务","通联HT对账计划任务失败！", "21401233@qq.com");
				logger.error("通联HT对账计划任务失败！");
			}
		}
		logger.info("通联HT对账计划任务-----------END----------------"+new Date());
	}
	
	public void collate_TLJRD_C() {
		//UAT和生产环境逻辑
		logger.info("通联JRD对账计划任务-----------START----------------"+new Date());
		boolean flg = true;
		Map<String,String>  rtnMap = settlementService.readCollateFileHT(null,"'"+Constants.JRD_ID+"'","28","109164111502001","B1");
		if (rtnMap !=null && "0000".equals(rtnMap.get("errCode"))) {
		}else {
			flg = false;
			if (rtnMap !=null) {
				if ("0999".equals(rtnMap.get("errCode"))) {
					flg = true;
				}
				RkylinMailUtil.sendMailThread("通联HT对账计划任务","通联JRD对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]", "21401233@qq.com");
				logger.error("通联网关支付JRD对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]");
			} else {
				RkylinMailUtil.sendMailThread("通联JRD对账计划任务","通联JRD对账计划任务失败！", "21401233@qq.com");
				logger.error("通联网关支付HT对账计划任务失败！");
			}
		}
		rtnMap = settlementService.readCollateFileHT(null,"'"+Constants.JRD_ID+"'","28","109164111502002","B11");
		if (rtnMap !=null && "0000".equals(rtnMap.get("errCode"))) {
		}else {
			flg = false;
			if (rtnMap !=null) {
				if ("0999".equals(rtnMap.get("errCode"))) {
					flg = true;
				}
				RkylinMailUtil.sendMailThread("通联HT对账计划任务","通联JRD对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]", "21401233@qq.com");
				logger.error("通联移动支付JRD对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]");
			} else {
				RkylinMailUtil.sendMailThread("通联JRD对账计划任务","通联JRD对账计划任务失败！", "21401233@qq.com");
				logger.error("通联移动支付HT对账计划任务失败！");
			}
		}
		if (flg) {
			String rtnStr = settlementService.createDebtAccountFileHT(null,Constants.JRD_ID,"32");
			logger.info("生成对账文件的返回值："+rtnStr);
		}
		//ST上的代码逻辑不经过上游对账
//		String rtnStr = settlementService.createDebtAccountFileHT(null,Constants.JRD_ID,"32");
//		logger.info("生成对账文件的返回值："+rtnStr);
		
		logger.info("通联JRD对账计划任务-----------END----------------"+new Date());
	}
	
	public void collate_TL_C() {
		logger.info("通联对账计划任务-----------START----------------"+new Date());
		Map<String,String> rtnMap = settlementService.readCollateFile(null);
		if (rtnMap !=null && "0000".equals(rtnMap.get("errCode"))) {
			settlementService.createDebtAccountFile2(null);
		}else {
			if (rtnMap !=null) {
				RkylinMailUtil.sendMailThread("通联对账计划任务","通联对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]", "21401233@qq.com");
				logger.error("通联对账计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]");
			} else {
				RkylinMailUtil.sendMailThread("通联对账计划任务","通联对账计划任务失败！", "21401233@qq.com");
				logger.error("通联对账计划任务失败！");
			}
		}
		logger.info("通联对账计划任务-----------END----------------"+new Date());
	}

	public void createDebtFile() {
		logger.info("生成债权包文件计划任务-----------START----------------"+new Date());
		settlementService.createP2PDebtFile(null);

		// 调用晶晶的存储过程完成计息
    	Map<String, String> rtnMap = settlementService.proRepayment();

        if(null==rtnMap||!String.valueOf(rtnMap.get("on_err_code")).equals("0")){
//    		RkylinMailUtil.sendMailThread("债权包计划任务","计息计划任务:["+rtnMap.get("on_err_code")+"]-["+rtnMap.get("ov_err_text")+"]", "21401233@qq.com");
//			logger.error("计息计划任务:["+rtnMap.get("on_err_code")+"]-["+rtnMap.get("ov_err_text")+"]");
        	logger.error("计息出错了");
        }
		logger.info("生成债权包文件计划任务-----------END----------------"+new Date());
	}
	
	public void createPaymentFile() {
		logger.info("生成还款文件计划任务-----------START----------------"+new Date());
		settlementService.createPaymentFile(null);
		logger.info("生成还款文件计划任务-----------END----------------"+new Date());
	}
	
	public void readPaymentFile() {
		synchronized (lock) {
			logger.info("接受代收付结果计划任务-----------START----------------"+new Date());
	
			logger.info("输入参数：" + filetype);
			logger.info("输入参数：" + batch);
			logger.info("输入参数：" + invoicedate);
	    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
	    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
	    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
	    	SettlementUtils settlementUtils = new SettlementUtils();
	    	String accountDate = "";
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyy-MM-dd",-1);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					return;
				}
	    	} else {
	    		accountDate = invoicedate;
	    	}
			logger.info("取得的账期为"+ accountDate);
			
			String msg=generationPaymentService.paymentFile(accountDate,batch,filetype,userProperties.getProperty("RKYLIN_PUBLIC_KEY"));
			if("ok".equals(msg)){
				Map<String,String> rtnMap=settlementServiceThr.updateCreditAccountSec();
				if("0000".equals(rtnMap.get("errCode"))){
					
				}else{
					RkylinMailUtil.sendMail("代收付计划任务", "代收付更新错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"), "21401233@qq.com");
					logger.error("代收付更新错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
				}
			}else{
				RkylinMailUtil.sendMail("代收付计划任务", "代收付结果读入错误！", "21401233@qq.com");
				logger.error("代收付结果读入错误！");
			}
			logger.info("接受代收付结果计划任务-----------END----------------"+new Date());
		}
	}
	
	public void readPaymentFileForTask() {
		synchronized (lock) {
			logger.info("接受代收付结果计划任务-----------START----------------"+new Date());
	
			logger.info("输入参数：" + filetype);
			logger.info("输入参数：" + invoicedate);
			logger.info("输入参数：" + rootinstcd);
			logger.info("输入参数：" + datetype);
			if ("8".equals(filetype)) {
				this.batchType = SettleConstants.ROP_RECEIVE_BATCH_CODE;
			} else if ("9".equals(filetype)) {
				this.batchType = SettleConstants.ROP_PAYMENT_BATCH_CODE;
			}
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
	    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
	    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
	    	SettlementUtils settlementUtils = new SettlementUtils();
	    	String accountDate = "";
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					int dateFlag = 1;
					if(!"".equals(datetype) && null != datetype){
						dateFlag = Integer.parseInt(datetype);
					}
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyy-MM-dd",-dateFlag);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					return;
				}
	    	} else {
	    		accountDate = invoicedate;
	    	}
			logger.info("取得的账期为"+ accountDate);
			
			SettleBatchResultQuery settleBatchResultQuery = new SettleBatchResultQuery();
			SettleBatchResult SettleBatch = new SettleBatchResult();
			try {
				settleBatchResultQuery.setAccountDate(formatter.parse(accountDate + " 00:00:00"));
				settleBatchResultQuery.setBatchType(batchType);
				settleBatchResultQuery.setRootInstCd(rootinstcd);
			} catch (Exception e) {
				logger.error("时期转换异常！"+ e.getMessage());
				e.printStackTrace();
			}
			
			List<SettleBatchResult> settleBatchResultList = settleBatchResultManager.queryList(settleBatchResultQuery);
			
			if (settleBatchResultList.size() == 0) {
				logger.error("账期内["+accountDate+"]没有对应的批次号");
				return;
			}
			
			String msg = "";
			Map<String,String> rtnMap = new HashMap<String,String>();
			for (SettleBatchResult settleBatchResult:settleBatchResultList) {
				SettleBatch = new SettleBatchResult();
				msg=generationPaymentService.paymentFile(accountDate,settleBatchResult.getBatchNo(),filetype,userProperties.getProperty("RKYLIN_PUBLIC_KEY"));
				if("ok".equals(msg)){
					rtnMap = new HashMap<String,String>();
					rtnMap=settlementServiceThr.updateCreditAccountSec();		
					if("0000".equals(rtnMap.get("errCode"))){
						SettleBatch.setBatchResult("成功");
						//********特殊处理：学生还款给课栈 成功后 ， 发起课栈代付交易给P2P 20150603 start
						Map<String, String> rtnMapP2P = settlementServiceThr.withholdToP2P();
						if (!"P0".equals(rtnMapP2P.get("errCode"))) {
							logger.error("学生还款给课栈 成功后，发起课栈代付交易给P2P错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
						}	
					}else{
						SettleBatch.setBatchResult("结果读入失败");
						RkylinMailUtil.sendMail("代收付计划任务", "代收付更新错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"), "21401233@qq.com");
						logger.error("代收付更新错误："+rtnMap.get("errCode")+":"+rtnMap.get("errMsg"));
					}
					// 调用晶晶的存储过程把成功的代收付过渡至历史表
		            Map<String, String> param = new HashMap<String, String>();
			    	super.getSqlSession().selectList("MyBatisMap.setgeneration", param);
		            if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
		    			logger.error("维护代付表历史数据失败！");
		            }
				}else{
					SettleBatch.setBatchResult("文件下载失败");
					RkylinMailUtil.sendMail("代收付计划任务", "代收付结果读入错误！", "21401233@qq.com");
					logger.error("代收付结果读入错误！");
				}
				SettleBatch.setResultId(settleBatchResult.getResultId());
				settleBatchResultManager.saveSettleBatchResult(SettleBatch);
			}
			logger.info("接受代收付结果计划任务-----------END----------------"+new Date());
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void uploadWithholdFile() {
		logger.info("日批上传代扣明细文件 TO ROP 计划任务-----------START----------------" + new Date());
		Map<String,String> rtnMap = new HashMap<String, String>();
/*		logger.info("*****1.上传丰年代扣明细文件");
		Map<String,String> rtnMap = settlementServiceSec.uploadWithholdFile(Constants.FN_ID,Constants.P2P_ID);
		logger.info("*****1.结果:" + rtnMap.get("errCode") + ":" + rtnMap.get("errMsg"));		
		logger.info("*****2.上传会堂代扣明细文件");
		rtnMap = settlementServiceSec.uploadWithholdFile(Constants.HT_ID,Constants.P2P_ID);
		logger.info("*****2.结果：" + rtnMap.get("errCode") + ":" + rtnMap.get("errMsg"));	
*/		logger.info("*****3.上传课栈代扣明细文件");
		rtnMap = settlementServiceSec.uploadWithholdFile(Constants.KZ_ID,Constants.P2P_ID);
		logger.info("*****3.结果：" + rtnMap.get("errCode") + ":" + rtnMap.get("errMsg"));	
		logger.info("日批上传代扣明细文件 TO ROP 计划任务-----------END----------------" + new Date());
		
		logger.info("课栈计息 计划任务-----------START----------------" + new Date());	
		Map<String, String> param = new HashMap<String, String>();
    	super.getSqlSession().selectList("MyBatisMap.prorepayment_kz", param);

        if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
        	logger.error("prorepayment_kz 出错了");
        }
		logger.info("课栈计息 TO ROP 计划任务-----------END----------------" + new Date());
	}

	public void uploadInterRepaymentFile() {
		logger.info("上传  课栈 每日还款明细 文件  -----------START----------------" + new Date());
		Map<String,String> rtnMap = new HashMap<String, String>();
		try {
			//课栈调用 每日上传还款明细文件
			rtnMap = interestRepaymentService.uploadIneterestRepaymentFile(Constants.KZ_ID,Constants.P2P_ID);
			logger.info("*****结果：" + rtnMap.get("errCode") + ":" + rtnMap.get("errMsg"));	

		} catch (Exception e) {
			logger.error("上传 课栈 每日还款明细 文件时出现异常 !!!!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("上传  课栈 每日还款明细 文件   -----------END----------------" + new Date());

		logger.info("课栈过渡 计划任务-----------START----------------" + new Date());	
		//调用晶晶的存储过程把interest的数据过渡至历史表 start
        Map<String, String> paramInterest = new HashMap<String, String>();
    	super.getSqlSession().selectList("MyBatisMap.setinterest", paramInterest);
        if(null==paramInterest||!String.valueOf(paramInterest.get("on_err_code")).equals("0")){
        	logger.error("setinterest 出错了");
        }
		logger.info("课栈过渡计划任务-----------END----------------" + new Date());	
	}
	
	public void porfitTask() {
			logger.info("分润计划任务-----------START----------------"+new Date());
			logger.info("输入参数：" + rootinstcd);
			
			Map<String,String> rtnMap = profitLogic.profitLogic(null, rootinstcd);
			if (rtnMap !=null && "0000".equals(rtnMap.get("errCode"))) {
				
			} else {
				logger.error("分润计划任务:["+rtnMap.get("errCode")+"]-["+rtnMap.get("errMsg")+"]");
			}
			
			logger.info("分润计划任务-----------END----------------"+new Date());
		}
	
	public void uploadDeductFile() {
		logger.info("上传棉庄债券包明细文件 TO ROP 计划任务-----------START----------------" + new Date());
		Map<String,String> rtnMap = new HashMap<String, String>();
		rtnMap = settlementServiceThr.uploadDeductFile(Constants.MZ_ID,Constants.P2P_ID);
		logger.info("*****结果：" + rtnMap.get("errCode") + ":" + rtnMap.get("errMsg"));	
		logger.info("上传棉庄债券包明细文件  TO ROP 计划任务-----------END----------------" + new Date());
		
//		logger.info("棉庄计息 计划任务-----------START----------------" + new Date());	
//		Map<String, String> param = new HashMap<String, String>();
//    	super.getSqlSession().selectList("MyBatisMap.prorepayment_kz", param);
//
//        if(null==param||!String.valueOf(param.get("on_err_code")).equals("0")){
//        	logger.error("棉庄prorepayment_kz 出错了");
//        }
//		logger.info("棉庄计息 TO ROP 计划任务-----------END----------------" + new Date());
	}
}
