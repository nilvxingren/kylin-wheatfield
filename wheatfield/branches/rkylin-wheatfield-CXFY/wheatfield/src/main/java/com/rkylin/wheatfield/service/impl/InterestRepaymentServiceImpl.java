package com.rkylin.wheatfield.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.InterestRepaymentManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.InterestRepaymentQuery;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.response.InterestRepaymentResponse;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.InterestRepaymentService;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.SettlementUtils;

@Service("interestRepaymentService")
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class InterestRepaymentServiceImpl extends BaseDao implements InterestRepaymentService, IAPIService {
	private static Logger logger = LoggerFactory.getLogger(SettlementServiceImpl.class);
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
	
	@Autowired
	private InterestRepaymentManager interestRepaymentManager;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	private IErrorResponseService errorResponseService;
	@Autowired
	Properties userProperties;
	@Autowired
	SettlementLogic settlementLogic;
	/**
	 *封装查询条件的query实体 
	 */
	private InterestRepaymentQuery query;
	private ParameterInfoQuery keyList;
	/**
	 * 相应对象
	 */
	private Response responseR = null;
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	/***
	 * 学生扣款查询
	 */
	public List<InterestRepayment> getInterestRepayment(InterestRepaymentQuery query) throws Exception {
		logger.info("进入   __________学生扣款查询方法___________");
		logger.info("query信息 : " + query);
		return interestRepaymentManager.queryInterestRepaymentList(query);
	}
	/**
	 * 创建还款信息文件并上传服务器
	 * @param rootInstCd 管理机构代码,例丰年,会唐,课栈
	 * @param providerId 授信提供方ID,例JRD
	 * @return rtnMap
	 */
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> uploadIneterestRepaymentFile(String rootInstCd, String providerId) {
		logger.info("进入   __________创建扣款信息文件并上传服务器___________" + rootInstCd);
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		
		InterestRepaymentQuery query = null;						//InterestRepaymentQuery 扣款条件对象
		String accountDate = "";									//账期
		String accountDate1 = "";
		List<InterestRepayment> interestRepaymentes = null;			//扣款信息
		SettlementUtils settlementUtils = null;						//清结算工具类
		String fileName = "";										//上传文件名称
		String rootInstCdStr = "";
		
		//实例化
		query = new InterestRepaymentQuery();
		keyList = new ParameterInfoQuery();
		settlementUtils = new SettlementUtils();
		Date createdTime = null;
		
		try {
			//查询账期的条件
			keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
			//查询当前账期
			accountDate = settlementUtils.getAccountDate(parameterInfoManager.queryList(keyList).get(0).getParameterValue(), "yyyy-MM-dd", -1);
			accountDate1 = accountDate.replace("-", "");
			createdTime = formatter.parse(accountDate);
			
			logger.info("获取账期成功, 账期信息 : " + accountDate);
		} catch (Exception e) {
			logger.error("计算账期异常！" + e.getMessage());
			rtnMap.put("errMsg", "账期获取失败");
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}
		
		
		if(rootInstCd == null || "".equals(rootInstCd.trim())) {//判断参数不为空
			rtnMap.put("errMsg", "传递的'管理机构代码'不正确");
			rtnMap.put("errCode", "P2");
			return rtnMap;
		} else {
			logger.info("获取管理机构代码成功:" + rootInstCd);
		}
		
		
		//封装扣款的查询条件
		query.setRootInstCd(rootInstCd);
		query.setProviderId(providerId);
		//query.setCreatedTime(createdTime);
		//查询扣款信息
		interestRepaymentes = interestRepaymentManager.queryListAndParam(query);

/*for(InterestRepayment ir : interestRepaymentes) {
	System.out.println(ir);
}
*/		
		
		if(interestRepaymentes == null || interestRepaymentes.size() <= 0) {
			rtnMap.put("errMsg", "获取到0条扣款信息");
			rtnMap.put("errCode", "P3");
			logger.info("获取到0条扣款信息");
			return rtnMap;
		} else {
			logger.info("获取 " + interestRepaymentes.size() + " 条扣款信息");
		}
		
		//获取商户编号
		rootInstCdStr = interestRepaymentes.get(0).getRootInstCd();
		//文件名称生成策略: IR:InterestRepayment缩写; rootInstCdStr:商户编号; accountDate:账期; 0~100随机数; 文件后缀
		fileName = "IR_" + rootInstCdStr + "_" + accountDate1 + "_" + PartyCodeUtil.getRandomCode() + "." + SettleConstants.FILE_CSV;
		//测试信息
		logger.info("文件名称 : " + fileName);
		
		//开始生成文件
		logger.info(">>> 开始生成文件");
		List txtList = new ArrayList();
		
		Map paraMap = null;
		Map configMap = new HashMap();
	
		
		for (InterestRepayment ir : interestRepaymentes) {
			paraMap = new HashMap();	
			int tdCount = 1;
			//还款账期
			accountDate = accountDate == null ? "" : accountDate;
			paraMap.put("F_" + (tdCount ++), accountDate);
			//还款时间
			paraMap.put("F_" + (tdCount ++), ir.getRepaidRepaymentDate()==null?"":formatter1.format(ir.getRepaidRepaymentDate()));
			//账户交易流水号
			paraMap.put("F_" + (tdCount ++), ir.getInterId());
			//机构码
			paraMap.put("F_" + (tdCount ++), ir.getProviderId());
			//商户号
			paraMap.put("F_" + (tdCount ++), ir.getRootInstCd());
			//商户用户
			paraMap.put("F_" + (tdCount ++), ir.getUserId());							
			//账户贷款订单号									
			paraMap.put("F_" + (tdCount ++), ir.getOrderId());
			//商户贷款订单号									
			paraMap.put("F_" + (tdCount ++), ir.getUserOrderId());
			//还款总期数									
			paraMap.put("F_" + (tdCount ++), ir.getPeriodSummary());
			//当前期数									
			paraMap.put("F_" + (tdCount ++), ir.getPeriodCurrent());
			//应还日期									
			paraMap.put("F_" + (tdCount ++), ir.getShouldRepaymentDate()==null?"":formatter.format(ir.getShouldRepaymentDate()));
			
			//应还本金									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getShouldCapital()) / 100);
			//应还利息									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getShouldInterest()) / 100);
			//减免利息									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getInterestFree()) / 100);
			//应还金额									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getShouldAmount()) / 100);
			
			//逾期天数								
			paraMap.put("F_" + (tdCount ++), ir.getOverdueDays());
			//逾期罚金									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getOverdueFine()) / 100);
			//逾期利息									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getOverdueInterest()) / 100);
			//提前还款申请订单号						
			paraMap.put("F_" + (tdCount ++), ir.getOverdueOrderId());
			//提前还款本金									
			paraMap.put("F_" + (tdCount ++), ir.getOrderType() != 2 ? "" : ((double) ir.getAdvanceAmount()) / 100);
			//提前还款利息									
			paraMap.put("F_" + (tdCount ++), ir.getOrderType() != 2 ? "" : ((double) ir.getAdvanceInterest()) / 100);
			//剩余应还本金							
			paraMap.put("F_" + (tdCount ++), ((double) ir.getOverplusAmount()) / 100);
			
			//实际还款金额									
			paraMap.put("F_" + (tdCount ++), ((double) ir.getRepaidAmount()) / 100);
			//扣款结果								
			paraMap.put("F_" + (tdCount ++), ir.getStatusId());
			//还款状态1									
			paraMap.put("F_" + (tdCount ++), ir.getStatusId());
			//还款状态2
			paraMap.put("F_" + (tdCount ++), ir.getOverdueFlag2());
			//扩展字段1
			paraMap.put("F_" + (tdCount ++), "");
			//扩展字段2
			paraMap.put("F_" + (tdCount ++), "");
			//扩展字段3
			paraMap.put("F_" + (tdCount ++), "");
			
			//测试信息
			logger.info("文件内容  >>> >>> >>> " + paraMap);					
			txtList.add(paraMap);
		}
		
		String path = SettleConstants.FILE_UP_PATH + accountDate1;
		
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		
		//文件却对路径
		path = path + File.separator + fileName;				
		configMap.put("FILE", path);

		String Key = userProperties.getProperty("P2P_PUBLIC_KEY");
		try {
			TxtWriter.WriteTxt(txtList, configMap, SettleConstants.DEDT_SPLIT2, "UTF-8");	// SettleConstants.DEDT_SPLIT2

			UploadAndDownLoadUtils.uploadFile(
					path,							//文件上传路径	 			String path
					4,								//文件类别					int type
					formatter2.parse(accountDate1),	//账期 格式 (yyyyMMdd)		Date invaiceDate
					settlementLogic.getBatchNo(formatter2.parse(accountDate1),
					SettleConstants.ROP_REPAYMENT_BATCH_CODE, rootInstCd),							//批次号 正在建设中 ... ...	String batch
					SettleConstants.FILE_XML,		//不明					String jsonOrXml
					Key,							//不明					String priOrPubKey  <- 他是null
					0,								//不明					int flg
					userProperties.getProperty("FSAPP_KEY"),		//不明	String appKey
					userProperties.getProperty("FSDAPP_SECRET"),	//不明	String appSecret
					userProperties.getProperty("FSROP_URL")			//不明	String ropUrl
				);
			logger.info("成功!生成还款明细文件");
		} catch (Exception e) {
			rtnMap.put("errMsg", "生成还款明细文件操作异常！");
			rtnMap.put("errCode", "P4");
			logger.error("生成还款明细文件操作异常！" + e.getMessage());
			return rtnMap;
		}
		rtnMap.put("errMsg", "还款信息文件,上传成功!");
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}
	
	/***
	 * 查询是否可以提前还款
	 * @param query
	 * @return true可以; false不可以
	 */
	public Map<String,String> canBeEarlyRepayment(String userId, String userOrderId, String rootInstId, InterestRepaymentResponse response) {
		logger.info(">>> >>> >>> 进入 canBeEarlyRepayment <<< <<< <<<");
		//提示信息
		Map<String,String> rtnMap = new HashMap<String,String>();
		
		logger.info(">>> >>> >>> 对 传入参数 进行校验 <<< <<< <<<");
		//输入参数校验
		repaymentPayCheck(userId, userOrderId, rootInstId, rtnMap);
		if(!"".equals(rtnMap.get("errCode")) && null != rtnMap.get("errCode") ){
			return rtnMap;
		}
		
		logger.info(">>> >>> 通过 商户ID 查询商户对应的 code");
		String rootInstCd = settlementLogic.getParameterInfoByCode(rootInstId, "1");
		
		//封装 传出/入 参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("iv_user_id", userId); 
		param.put("iv_user_order_id", userOrderId); 
        param.put("iv_root_inst_cd", rootInstCd); 
        
        logger.info(">>> >>> >>> 调用晶姐存储过工程");
		//创建session对象
		SqlSession session = super.getSqlSession();
		
		//mybatis 对应存储过工程的的id
		String statement = "MyBatisMap.interestPrepaymentQuery";
		session.selectList(statement, param);
		
		String out_principal = String.valueOf(param.get("out_principal"));
		String out_fees = String.valueOf(param.get("out_fees"));
		logger.info(">>> >>> 获取传出参数 out_principal: " + out_principal);
		logger.info(">>> >>> 获取传出参数 out_fees: " + out_fees);
		
		//错误提示信息
		String mess = "";
		if(!"null".equals(out_principal) && !"".equals(out_principal.trim())) {
			response.setOut_principal(out_principal);
		} else {
			mess += "未获取到 out_principal 信息; ";
		}
		
		if(!"null".equals(out_fees.trim()) && !"".equals(out_fees.trim())) {
			response.setOut_fees(out_fees);
		} else {
			mess += "未获取到 out_fees 信息; ";
		}
		
		if("".equals(mess)) {
			rtnMap.put("errMsg", "是否可以提前还款,查询成功!");
			rtnMap.put("errCode", "P0");
		} else {
			rtnMap.put("errMsg", mess);
			rtnMap.put("errCode", "P6");
		}
		
		return rtnMap;
	}
	
	private void repaymentPayCheck(String userId,String userOrderId, String rootInstId, Map<String,String>rtnMap){
		if (null == userId || "".equals(userId.trim())) {
			rtnMap.put("errMsg", "商户用户ID不能为空");
			rtnMap.put("errCode", "P2");
			return;
		}	
		if (null == userOrderId || "".equals(userOrderId.trim())) {
			rtnMap.put("errMsg", "商户贷款订单号不能为空");
			rtnMap.put("errCode", "P3");
			return;
		}	
		if (null == rootInstId || "".equals(rootInstId.trim())) {
			rtnMap.put("errMsg", "商户号不能为空");
			rtnMap.put("errCode", "P4");
			return;
		}	
	}
	/**
	 * 将 Map<String, String[]> paramMap 中的查询条件封装到 InterestRepaymentQuery 对象中
	 * @param paramMap
	 * @return  带有查询条件 的 InterestRepaymentQuery对象
	 */
	private InterestRepaymentQuery getParamQuery(Map<String, String[]> paramMap) throws Exception {
		logger.info("进入   __________获取查询条件方法___________");
		
		/*生命query对象*/
		InterestRepaymentQuery query = new InterestRepaymentQuery();
		/*学生扣款查询时'课栈'的特有功能*/
		//final String ROOTINSTCD = "M000004";
		/*目前只有 '课栈'调用此接口查询学生扣款信息*/
		//query.setRootInstCd(ROOTINSTCD);
		/*声明查询条件*/
		String rootInstCd = null;
		String userOrderId = null;							//用户编号
		Integer statusId = null;						//还款结果状态,0未还款,1还款成功,2还款失败,3还款中
		String providerId = null;
		if(paramMap != null && paramMap.size() > 0) {
			for(Object keyObj : paramMap.keySet().toArray()){
				String[] strs = paramMap.get(keyObj);
							
				for(String value : strs) {
					if("rootInstCd".equalsIgnoreCase(String.valueOf(keyObj))) {
						rootInstCd = value;
						logger.info("传递的参数 rootInstCd : " + rootInstCd);
						query.setRootInstCd(rootInstCd);
					}
					if("userOrderId".equalsIgnoreCase(String.valueOf(keyObj))) {
						userOrderId = value;
						logger.info("传递的参数 userOrderId : " + userOrderId);
						query.setUserOrderId(userOrderId);
					}
					if("statusId".equalsIgnoreCase(String.valueOf(keyObj))) {
						statusId = Integer.parseInt(value);
						logger.info("传递的参数 statusId : " + statusId);
						query.setStatusId(statusId);
					}
					if("providerId".equalsIgnoreCase(String.valueOf(keyObj))) {
						providerId = value;
						logger.info("传递的参数 statusId : " + providerId);
						query.setProviderId(providerId);
					}
				}
			}
			
			if(rootInstCd == null || rootInstCd.length() <= 0) {
				logger.error("管理机构代码,例丰年,会唐,课栈 不能为空");
				return null;
			}
		} else {
			logger.info("未向  InterestRepaymentService 的 doJob  传递参数 ... ...");
		}
		
		return query;
	}
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		logger.info("进入   __________doJob___________");
		InterestRepaymentResponse response = new InterestRepaymentResponse();
		String urlKey = null;
		boolean isSuccess = false;
		
		if("ruixue.wheatfield.interestrepayment.query".equals(methodName)){
			List<InterestRepayment> interestRepaymentes = null;
			
			try {
				query = this.getParamQuery(paramMap);
				interestRepaymentes = this.getInterestRepayment(query);
				if(interestRepaymentes != null && interestRepaymentes.size() > 0) {
					isSuccess = true;
				} else {
					isSuccess = false;
					logger.info("查询学生还款信息: 查询到 0 条学生还款信息的结果!");
				}
			} catch (Exception e) {
				logger.error("查询学生还款信息异常: " + e.getMessage());
				isSuccess = false;
			}
			
			response.setRepaymentes(interestRepaymentes);
			response.setUrlKey(urlKey);
			response.setIs_success(isSuccess);
		} else if("ruixue.wheatfield.earlyrepayment.query".equals(methodName)) {
			logger.info(">>> >>> >>>    在interestrepayment的doJob中调用了 earlyrepayment 是否可以提前还款的方法     <<< <<< <<<");
			
			String userId = "";
			String userOrderId ="";
			String rootInstId="";
			
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if(keyObj.equals("userid")) {
						userId = value;
					}else if(keyObj.equals("userorderid")) {
						userOrderId = value;
					}else if(keyObj.equals("rootinstid")) {
						rootInstId = value;
					}
				}
			}
			
			Map<String,String> rtnMap = this.canBeEarlyRepayment(userId, userOrderId, rootInstId, response);
			
			if ("P0".equals(rtnMap.get("errCode"))) {
				response.setIs_success(true);
				response.setRtn_msg(rtnMap.get("errMsg"));
			} else {
				response.setIs_success(false);
				response.setRtn_msg(rtnMap.get("errMsg"));
			}
		} else {
			return errorResponseService.getErrorResponse("S0");
		}
		
		return response;
	}
}
