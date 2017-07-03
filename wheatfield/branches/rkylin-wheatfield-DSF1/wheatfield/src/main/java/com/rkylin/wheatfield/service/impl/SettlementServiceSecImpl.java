package com.rkylin.wheatfield.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.common.UploadAndDownLoadUtils;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.CreditApprovalInfoManager;
import com.rkylin.wheatfield.manager.CreditRateTemplateDetailManager;
import com.rkylin.wheatfield.manager.CreditRateTemplateManager;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettlementManager;
import com.rkylin.wheatfield.pojo.CreditRateTemplate;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetail;
import com.rkylin.wheatfield.pojo.CreditRateTemplateDetailQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplateQuery;
import com.rkylin.wheatfield.pojo.CreditRateTemplateRes;
import com.rkylin.wheatfield.pojo.InterestRepayment;
import com.rkylin.wheatfield.pojo.WrongOrder;
import com.rkylin.wheatfield.response.Response;
import com.rkylin.wheatfield.response.SettlementSecResponse;
import com.rkylin.wheatfield.service.IAPIService;
import com.rkylin.wheatfield.service.IErrorResponseService;
import com.rkylin.wheatfield.service.OperationServive;
import com.rkylin.wheatfield.service.SettlementServiceSec;
import com.rkylin.wheatfield.settlement.SettlementLogic;
import com.rkylin.wheatfield.utils.DateUtil;

@Service("settlementServiceSec")
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class SettlementServiceSecImpl extends BaseDao implements
		SettlementServiceSec, IAPIService {
	private static Logger logger = LoggerFactory
			.getLogger(SettlementServiceImpl.class);
	@Autowired
	IErrorResponseService errorResponseService;
	@Autowired
	CreditRateTemplateManager creditRateTemplateManager;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired	
	CreditRateTemplateDetailManager creditRateTemplateDetailManager;
	@Autowired
	SettlementLogic settlementLogic;
	@Autowired	
	CreditApprovalInfoManager creditApprovalInfoManager;
	@Autowired	
	OperationServive operationServive;
	@Autowired	
	SettlementManager settlementManager;
	@Autowired
	Properties userProperties;
	
	DateUtil dateUtil=new DateUtil();
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		// TODO Auto-generated method stub
		SettlementSecResponse response = new SettlementSecResponse();
		String type = "";
		CreditRateTemplateRes rateTemp = new CreditRateTemplateRes();
		try{
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("type")) {
						type = value;
					}else if(keyObj.equals("rateid")) {
						rateTemp.setRateId(value);
					}else if(keyObj.equals("rootinstcd")) {
						rateTemp.setRootInstCd(value);
					}else if(keyObj.equals("ratetype")) {
						rateTemp.setRateType(value);
					}else if(keyObj.equals("billday")) {
						rateTemp.setBillDay(value);
					}else if(keyObj.equals("repaymentday")) {
						rateTemp.setRepaymentDay(value);
					}else if(keyObj.equals("ratex")) {
						rateTemp.setRateX(value);
					}else if(keyObj.equals("ratey")) {
						rateTemp.setRateY(value);
					}else if(keyObj.equals("rateproperty")) {
						rateTemp.setRateProperty(value);
					}else if(keyObj.equals("ratetimex")) {
						rateTemp.setRateTimeX(value);
					}else if(keyObj.equals("ratetimey")) {
						rateTemp.setRateTimeY(value);
					}else if(keyObj.equals("rateinterestform")) {
						rateTemp.setRateInterestForm(value);
					}else if(keyObj.equals("rateinteresttype")) {
						rateTemp.setRateInterestType(value);
					} else if (keyObj.equals("rateinterestover")) {
						rateTemp.setRateInterestOver(value);
					} else if (keyObj.equals("rateinterestoverunit")) {
						rateTemp.setRateInterestOverUnit(value);
					} else if (keyObj.equals("overduefees")) {
						rateTemp.setOverdueFees((long)Double.parseDouble(value));
					} else if (keyObj.equals("overduefeesunit")) {
						rateTemp.setOverdueFeesUnit(value);
					} else if (keyObj.equals("rateadvaoneoff")) {
						rateTemp.setRateAdvaOneoff(value);
					} else if (keyObj.equals("rateadvaoneoffunit")) {
						rateTemp.setRateAdvaOneoffUnit(value);
					} else if (keyObj.equals("advancefeesoneoff")) {
						rateTemp.setAdvanceFeesOneoff((long)Double.parseDouble(value));
					} else if (keyObj.equals("advancefeesoneoffunit")) {
						rateTemp.setAdvanceFeesOneoffUnit(value);
					} else if (keyObj.equals("rateadvasect")) {
						rateTemp.setRateAdvaSect(value);
					} else if (keyObj.equals("rateadvasectunit")) {
						rateTemp.setRateAdvaSectUnit(value);
					} else if (keyObj.equals("advancefeessect")) {
						rateTemp.setAdvanceFeesSect((long)Double.parseDouble(value));
					} else if (keyObj.equals("advancefeessectunit")) {
						rateTemp.setAdvanceFeesSectUnit(value);
					} else if (keyObj.equals("productid")) {
						rateTemp.setProductId(value);
					}else if(keyObj.equals("providerid")) {
						rateTemp.setProviderId(value);
					}else if(keyObj.equals("rateunit")) {
						rateTemp.setRateUnit(value);
					}else if(keyObj.equals("ratetimeunit")) {
						rateTemp.setRateTimeUnit(value);
					}
				}
			}
		}catch (Exception ex){
			return errorResponseService.getErrorResponse(
					"P9", "参数输入转换错误：" + ex.toString());
		}
		if ("ruixue.wheatfield.ratetemplate.operate".equals(methodName)) {
			//取得输入参数
			if(!"1".equals(type) && !"2".equals(type) && !"3".equals(type)){
				return errorResponseService.getErrorResponse("P1","操作类型不能为空，且必须为1或2或3");
			}
			if("".equals(rateTemp.getProviderId()) || null==rateTemp.getProviderId()){
				return errorResponseService.getErrorResponse("P5","授信提供方ID不能为空");
			}
			try{
				Map<String, String> rtnMap = operateRateTemplate(type,rateTemp);
				if ("P0".equals(rtnMap.get("errCode"))) {
					response.setIs_success(true);
					response.setRtn_msg(rtnMap.get("errMsg"));
					String rateId = rtnMap.get("rateId");
					if(!"".equals(rateId) && null != rateId){
						response.setRateId(rateId);					
					}
				} else {
					response.setIs_success(false);
					return errorResponseService.getErrorResponse(
							rtnMap.get("errCode"), rtnMap.get("errMsg"));
				}
			}catch (Exception ex){
				logger.error(ex.getMessage());
				throw new AccountException(ex.getMessage());
				//return errorResponseService.getErrorResponse(
				//		"P4", "费率模版操作失败：" + ex.toString());
			}
		}
		if ("ruixue.wheatfield.ratetemplate.query".equals(methodName)) {
			logger.info("查询费率模版接口-----------Start----------------");
			if("".equals(rateTemp.getProviderId()) || null==rateTemp.getProviderId()){
				return errorResponseService.getErrorResponse("P5","授信提供方ID不能为空");
			}
			try{
				List<CreditRateTemplateRes> rtnRateList = this
						.queryRateTemplate(rateTemp);
				if (rtnRateList == null || rtnRateList.size() == 0) {
					response.setRtn_msg("查询结果为空");
					response.setIs_success(true);
				} else {
					response.setCreditRateTemplates(rtnRateList);
					response.setIs_success(true);
				}
				logger.info("查询费率模版接口-----------END----------------");
			}catch (Exception ex){
				return errorResponseService.getErrorResponse(
						"P4", "费率模版操作失败：" + ex.toString());
			}
		}
		//更新计息开始日
		if ("ruixue.wheatfield.nofity.service.kzhloaninteresttoaccount".equals(methodName)) {
			String interestDate = "";
			String orderNoList = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("interestdate")) {
						interestDate = value;
					}else if(keyObj.equals("ordernolist")) {
						orderNoList = value;
					}
				}
			}
			Map<String,String> rtnMap = this.updateInterestDate(orderNoList,interestDate,response);
			if ("P0".equals(rtnMap.get("errCode"))) {
				response.setIs_success(true);
				response.setRtn_msg(rtnMap.get("errMsg"));
			} else {
				response.setIs_success(false);
				response.setRtn_msg(rtnMap.get("errMsg"));
			}
		}
		//查询还款计划
		if ("ruixue.wheatfield.repaymentplan.query".equals(methodName)) {
			String typePlan = "";
			String userId = "";
			String userOrderId ="";
			String rootInstId="";
			String orderId = "";
			for (Object keyObj : paramMap.keySet().toArray()) {
				String[] strs = paramMap.get(keyObj);
				for (String value : strs) {
					if (keyObj.equals("type")) {
						typePlan = value;
					}else if(keyObj.equals("userid")) {
						userId = value;
					}else if(keyObj.equals("userorderid")) {
						userOrderId = value;
					}else if(keyObj.equals("rootinstid")) {
						rootInstId = value;
					}else if(keyObj.equals("orderid")) {
						orderId = value;
					}
				}
			}
			Map<String,String> rtnMap = this.queryRepaymentPlan(typePlan, userId,
					                                            userOrderId, rootInstId, 
					                                            orderId,response);
			
			if ("P0".equals(rtnMap.get("errCode"))) {
				response.setIs_success(true);
				response.setRtn_msg(rtnMap.get("errMsg"));
			} else {
				response.setIs_success(false);
				response.setRtn_msg(rtnMap.get("errMsg"));
			}
		}
		return response;
	}

	//日批上传还款明细
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> uploadWithholdFile(String rootInstId,String providerId) {
		// TODO Auto-generated method stub
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		String accountDateS = "";
		//获取账期
		try {	
			//获取T日账期
			accountDateS = operationServive.getAccountDate().replace("-",""); 
			//accountDateLast = dateUtil.parse(accountDateS , "eye-MM-dd");
		} catch (Exception e) {
			rtnMap.put("errMsg", "账期取得失败");
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}
		if(null == accountDateS ){
			rtnMap.put("errMsg", "账期取得为0件");
			rtnMap.put("errCode", "P2");
			return rtnMap;			
		}
		//上传明细取得
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("accountDate", accountDateS);
		paraMap.put("merchantCode", rootInstId);
		paraMap.put("funcCode", TransCodeConst.PAYMENT_COLLECTION);
		paraMap.put("providerId", providerId);
//paraMap.put("status", "4");
		List<Map<String,Object>> rtnList = settlementManager.selectWithhold(paraMap);
		logger.debug("取得代扣明细件数:" + rtnList.size());
		if(rtnList.size() > 0){
			List txtList = new ArrayList();
			for (Map<String,Object> tmpbean:rtnList) {
				paraMap = new HashMap();
				//上传文件的列数为14，列数改变时须修改
				for(int i = 1; i < 15; i ++){
					paraMap.put("F_" + i, tmpbean.get("colum" + i));//账期
				}
				txtList.add(paraMap);
			}
			//上传文件名和路径的设定
			String path = SettleConstants.FILE_UP_PATH + accountDateS + File.separator;
			File filePath = new File(path);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			//rtnList.get(0).get("colum6") ： 转换后的商户号 ，文件中的第6列
			path=path + rtnList.get(0).get("colum6") + "_WITHHOLD_"+accountDateS+ "_" + PartyCodeUtil.getRandomCode()+".csv";
			//上传文件的配置设定
			Map configMap = new HashMap();
			configMap.put("FILE", path);
			String Key = userProperties.getProperty("P2P_PUBLIC_KEY");
			try {
				SimpleDateFormat f1= new SimpleDateFormat("yyyyMMdd"); 
				TxtWriter.WriteTxt(txtList, configMap, SettleConstants.DEDT_SPLIT2,"UTF-8");
				UploadAndDownLoadUtils.uploadFile(path, 3, 
												  f1.parse(accountDateS), 
						                          settlementLogic.getBatchNo(f1.parse(accountDateS), SettleConstants.ROP_COLLECTION_BATCH_CODE, rootInstId), 
						                          SettleConstants.FILE_XML,Key,0,
						                          userProperties.getProperty("FSAPP_KEY"),
						                          userProperties.getProperty("FSDAPP_SECRET"),
						                          userProperties.getProperty("FSROP_URL"));
			} catch (Exception e) {
				rtnMap.put("errMsg", "上传代扣明细文件发生异常：" + e.getMessage());
				rtnMap.put("errCode", "P3");
				return rtnMap;
			}
		}else{
			rtnMap.put("errMsg", "当日代扣明细件数为0件");
			rtnMap.put("errCode", "P0");
			return rtnMap;
		}
		rtnMap.put("errMsg", "当日代扣明细文件上传成功");
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}
	
	//查询还款计划
	@Override
	public Map<String,String> queryRepaymentPlan(String type, String userId,
			String userOrderId, String rootInstId, String orderId,
			SettlementSecResponse response) {
		logger.info(">>> >>> >>>  进入 查询还款计划 方法    <<< <<< <<<");
		// TODO Auto-generated method stub
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		//输入参数校验
		
		logger.info(">>> >>> 验证 传入参数");
		/*验证参数*/
		repaymentPayCheck(type,userId,userOrderId,rootInstId,orderId,rtnMap);
		if(!"".equals(rtnMap.get("errCode")) && null !=rtnMap.get("errCode") ){
			return rtnMap;
		}
		
		logger.info(">>> >>> 通过 商户ID 查询商户对应的 code");
		String rootInstCd = settlementLogic.getParameterInfoByCode(rootInstId, "1");
		
		if(rootInstCd == null || "".equals(rootInstCd)) {
			rtnMap.put("errMsg", "传入的 商户ID 不正确!");
			rtnMap.put("errCode", "P9");
			return rtnMap;
		}
		
		logger.info(">>> >>> iv_user_id: " + userId);
		logger.info(">>> >>> iv_user_order_id: " + userOrderId);
		logger.info(">>> >>> iv_root_inst_cd: " + rootInstCd);
		logger.info(">>> >>> iv_order_id: " + orderId);
		
        Map<String, Object> param = new HashMap<String, Object>();  
        param.put("iv_user_id", userId); 
        param.put("iv_user_order_id", userOrderId);
        param.put("iv_root_inst_cd", rootInstCd); 
        param.put("iv_order_id", orderId);

        //声明 mybatis的selectList方法调用存储过程返回的List
        List<Object> resultList = null;
        
        //调用晶姐存储过程
        logger.info(">>> >>> >>> 调用存储过工程");
        resultList = super.getSqlSession().selectList("MyBatisMap.queryRepayPlan", param);
        
        //如果selectList方法 返回的 resultList对象 为 null或者 长度为0 表示 查询失败
        //如果selectList方法 返回的 resultList对象 长度<2 表示 查询失败, 因为存储过程 return 2个结果集
        if(resultList == null || resultList.size() < 2) {
        	rtnMap.put("errMsg", "查询结果出现异常");
			rtnMap.put("errCode", "P6");
        	return rtnMap;
        }
        
        //声明对象 接受  储过程 return 的2个结果集
        //费率模板
        CreditRateTemplateRes creditRateTemplateRes = null;
        //还款信息
        List<InterestRepayment> interestRepaymentList = null;
        
        //错误提示信息字符串
        String mess = "";
        //如果第一个结果集为null,或者里面的对象为null, 表明 费率模板 查询失败
        if(resultList.get(0) != null && ((List<CreditRateTemplateRes>)resultList.get(0)).get(0) != null) {
        	//费率模板
	        creditRateTemplateRes = ((List<CreditRateTemplateRes>)resultList.get(0)).get(0);
        } else {
        	mess = "未查询结果异常, 费率模板查询失败; ";
        	rtnMap.put("errCode", "P8");
        }
        
        //如果第二个结果集为null,或者里面的对象为null, 表明 还款信息 查询失败
        if(resultList.get(1) != null) {
        	//还款信息
	        interestRepaymentList = (List<InterestRepayment>) resultList.get(1);
	        
	        if(interestRepaymentList == null) {
	        	mess += " 还款信息查询失败";
	        	rtnMap.put("errMsg", mess);
	        	rtnMap.put("errCode", "P8");
	        } else if(interestRepaymentList.size() <= 0) {
	        	mess += " 还款信息查询记录为0";
	        	rtnMap.put("errMsg", mess);
	        	rtnMap.put("errCode", "P8");
	        }
        } else {
        	mess += " 还款信息查询记录为0";
        	rtnMap.put("errMsg", mess);
			rtnMap.put("errCode", "P8");
        }
        
        logger.info(">>> >>> 把查询结果 封装到 response 对象中");
        response.setCalculateDate(String.valueOf(param.get("out_calculate_date")));
        response.setInterestDate(String.valueOf(param.get("out_interest_date")));
        response.setCreditRateTemplate(creditRateTemplateRes);
        response.setRepayments(interestRepaymentList);
        
        if("".equals(rtnMap.get("errCode")) || null ==rtnMap.get("errCode") ){
        	rtnMap.put("errMsg", "查询成功");
    		rtnMap.put("errCode", "P0");
		}
		
        logger.info(">>> >>> >>> 方法结束");
		return rtnMap;
	}
	

	//更新计息开始日
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String, String> updateInterestDate(String orderOnList,String interestDate,SettlementSecResponse response) {
		// TODO Auto-generated method stub
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		if ("".equals(orderOnList) || null == orderOnList) {
			rtnMap.put("errMsg", "账户贷款订单号不能为空");
			rtnMap.put("errCode", "P1");
			return rtnMap;
		}
		if ("".equals(interestDate) || null == interestDate) {
			rtnMap.put("errMsg", "计息开始日不能为空");
			rtnMap.put("errCode", "P2");
			return rtnMap;
		}
		
		HashMap<String,String> paraMap = new HashMap<String,String>();
		String[] orderOns = orderOnList.split(",");
		String orderOnListUp = "";
		
		for (int i = 0; i < orderOns.length; i++) {
			orderOns[i] = "'" + orderOns[i] + "'";
			if (i != orderOns.length - 1) {
				orderOnListUp = orderOnListUp + orderOns[i] + ",";
			} else {
				orderOnListUp = orderOnListUp + orderOns[i];
			}
		}
		
		paraMap.put("orderOnList", orderOnListUp);
		paraMap.put("interestDate", interestDate);

		int record = 0;
		
		try{
		  record = creditApprovalInfoManager.updateInterestDate(paraMap);
		  if(record == 0){
				rtnMap.put("errMsg", "0件数据被更新");
				rtnMap.put("errCode", "P5");
				return rtnMap;			    
		  }
		} catch (Exception ex){
			rtnMap.put("errMsg", ex.getMessage().toString());
			rtnMap.put("errCode", "P3");
			return rtnMap;
		}

		List<String> errOrderList = new ArrayList<String>();		
		if(record < orderOns.length) {//当manager影响的记录数 小于 传入的参数时			
			//验证orderId数据库中是否存在
			List<String> list = creditApprovalInfoManager.validateOrderList(paraMap);

			if(list == null || list.size() <= 0) {
				rtnMap.put("errMsg", "0件数据被更新");
				rtnMap.put("errCode", "P5");
				return rtnMap;
			}
			
			//承装错误orderId的容器
			for(String orderId : orderOnList.split(",")) {
				errOrderList.add(orderId);
			}
			errOrderList.removeAll(list);
		}
		
		if( errOrderList.size() == 0) {
			rtnMap.put("errMsg", "计息开始日更新成功,传入" + orderOns.length + "件数据," + record + "件数据被更新");
		} else {
			List<WrongOrder> wrongOrders = new ArrayList<WrongOrder>();
			for(String errOrder: errOrderList) {
				WrongOrder wrongOrder =new WrongOrder();
				wrongOrder.setOrderid(errOrder);
				wrongOrder.setWrong_msg("订单号不存在");
				wrongOrders.add(wrongOrder);
			}
			response.setWrongOrder(wrongOrders);
			rtnMap.put("errMsg", "计息开始日更新成功,传入" + orderOns.length + "件数据," + record + "件数据被更新");
		}
		rtnMap.put("errCode", "P0");		
		return rtnMap;
	}
    //费率模版查询
	@Override
	public List<CreditRateTemplateRes> queryRateTemplate(CreditRateTemplateRes rateTemp) throws Exception  {
		// TODO Auto-generated method stub	
		CreditRateTemplateQuery query = new CreditRateTemplateQuery();
		String providerId = settlementLogic.getParameterInfoByCode(rateTemp.getProviderId(),"1");
		if("".equals(providerId) || null==providerId){
			throw new Exception("授信提供方ID不正确");
		}
		query.setStatusId(1);
		query.setProviderId(providerId);
		//输入参数的机构id不为空时
		String rootInstCd ="";
		if(!"".equals(rateTemp.getRootInstCd()) && null !=rateTemp.getRootInstCd()){
			rootInstCd = settlementLogic.getParameterInfoByCode(rateTemp.getRootInstCd(),"1");
			if("".equals(rootInstCd) || null==rootInstCd){
				throw new Exception("输入的管理机构代码不正确");		
			}
			query.setRootInstCd(rootInstCd);
		}
		List<CreditRateTemplateRes> creditRateTemplates = creditRateTemplateManager.queryListWithJoin(query);
		if( creditRateTemplates!=null && creditRateTemplates.size() >0){
			Map<String,String> parameterCodes = settlementLogic.getParameterCodeByType("1",1);
			if(parameterCodes != null && parameterCodes.size() >0){
				for (CreditRateTemplateRes tmpbean : creditRateTemplates) {
					if (!parameterCodes.get(tmpbean.getRootInstCd()).isEmpty()) {
						tmpbean.setRootInstCd(parameterCodes.get(tmpbean
								.getRootInstCd()));
					}
					if (!parameterCodes.get(tmpbean.getProviderId()).isEmpty()) {
						tmpbean.setProviderId(parameterCodes.get(tmpbean
								.getProviderId()));
					}
				}
				logger.info("查询件数：" + creditRateTemplates.size());
				return creditRateTemplates;
			}else{
				throw new Exception("管理机构代码不正确");
			}
		}		
		return null;
	}
	
    //费率模版 增删改
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> operateRateTemplate(String type,CreditRateTemplateRes rateTemp) {
		// TODO Auto-generated method stub
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errMsg", "数据更新成功");
		entryCheck(type,rateTemp,rtnMap);
		if(!"".equals(rtnMap.get("errCode")) && null !=rtnMap.get("errCode") ){
			return rtnMap;
		}
		CreditRateTemplate rateInsert = new CreditRateTemplate();
		rateInsert = setRateTemp(type,rateTemp);
		CreditRateTemplateDetail rateDetailInsertX = new CreditRateTemplateDetail();
		rateDetailInsertX = setRateDetailTemp("X",type,rateInsert.getRateId(),rateTemp);
		//插入的场合
		if ("1".equals(type)) {
			// 插入费率模版表
			creditRateTemplateManager.insertCreditRateTemplate(rateInsert);
			// 插入费率模版明细表
			creditRateTemplateDetailManager.saveCreditRateTemplateDetail(rateDetailInsertX);
			if (!"".equals(rateTemp.getRateY()) && null != rateTemp.getRateY()) {
				CreditRateTemplateDetail rateDetailInsertY = new CreditRateTemplateDetail();
				rateDetailInsertY = setRateDetailTemp("Y", type,rateInsert.getRateId(),rateTemp);
				creditRateTemplateDetailManager.saveCreditRateTemplateDetail(rateDetailInsertY);
			}
			rtnMap.put("rateId",rateInsert.getRateId());
		} else {
			// 更新费率模版的存在验证
			CreditRateTemplateQuery queryExist = new CreditRateTemplateQuery();
			queryExist.setRateId(rateTemp.getRateId());
			queryExist.setStatusId(1);
			if(creditRateTemplateManager.countByExample(queryExist) > 0){
			// 更新费率模版明细表
				creditRateTemplateManager.saveCreditRateTemplate(rateInsert);
				if ("2".equals(type)) {
					// 删除原明细数据
					CreditRateTemplateDetailQuery query = new CreditRateTemplateDetailQuery();
					query.setRateId(rateInsert.getRateId());
					query.setStatusId(1);
					creditRateTemplateDetailManager
							.deleteCreditRateTemplateDetail(query);
					// 插入新明细数据
					creditRateTemplateDetailManager
							.saveCreditRateTemplateDetail(rateDetailInsertX);
					if (!"".equals(rateTemp.getRateY())
							&& null != rateTemp.getRateY()) {
						CreditRateTemplateDetail rateDetailInsertY = new CreditRateTemplateDetail();
						rateDetailInsertY = setRateDetailTemp("Y", type,
								rateInsert.getRateId(), rateTemp);
						creditRateTemplateDetailManager
								.saveCreditRateTemplateDetail(rateDetailInsertY);
					}
				} else {
					creditRateTemplateDetailManager
							.updateStatusByRateId(rateDetailInsertX);
				}
			}else{
				rtnMap.put("errMsg", "被更新的数据不存在");
			}
		}
		rtnMap.put("errCode", "P0");
		return rtnMap;
	}
	//输入参数验证
	private void entryCheck(String type, CreditRateTemplateRes rateTemp,HashMap<String,String> rtnMap){	
		//插入和更新时，必须输入管理机构代码
		if("1".equals(type) || "2".equals(type)){
			if ("".equals(rateTemp.getRootInstCd())
					|| null == rateTemp.getRootInstCd()) {
				rtnMap.put("errMsg", "管理机构代码不能为空");
				rtnMap.put("errCode", "P2");
				return;
			}
			String rootInstCd = settlementLogic.getParameterInfoByCode(rateTemp.getRootInstCd(),"1");
			if ("".equals(rootInstCd)
					|| null == rootInstCd) {
				rtnMap.put("errMsg", "管理机构代码不正确");
				rtnMap.put("errCode", "P6");
				return;
			}else{			
				rateTemp.setRootInstCd(rootInstCd);
			}
			String providerId = settlementLogic.getParameterInfoByCode(rateTemp.getProviderId(),"1");
			if ("".equals(providerId)
					|| null == providerId) {
				rtnMap.put("errMsg", "授信提供方ID不正确");
				rtnMap.put("errCode", "P7");
				return;
			}else{			
				rateTemp.setProviderId(providerId);
			}			
		}
		//删除和更新时，必须输入费率协议号
		if("2".equals(type) || "3".equals(type)){
			if ("".equals(rateTemp.getRateId())
					|| null == rateTemp.getRateId()) {
				rtnMap.put("errMsg", "费率协议号不能为空");
				rtnMap.put("errCode", "P3");
				return;
			}
		}
		if( "3".equals(type)){
			rateTemp.setProviderId(settlementLogic.getParameterInfoByCode(rateTemp.getProviderId(),"1"));
			rateTemp.setRootInstCd(settlementLogic.getParameterInfoByCode(rateTemp.getRootInstCd(),"1"));
		}
	}
	private void repaymentPayCheck(String type,String userId,String userOrderId,
			                       String rootInstId,String orderId,
			                       Map<String,String>rtnMap){
		/*
		if ("".equals(type) || null == type) {
			rtnMap.put("errMsg", "查询类型不能为空");
			rtnMap.put("errCode", "P1");
			return;
		}*/		
		if ("".equals(userId) || null == userId) {
			rtnMap.put("errMsg", "商户用户ID不能为空");
			rtnMap.put("errCode", "P2");
			return;
		}	
		if ("".equals(userOrderId) || null == userOrderId) {
			rtnMap.put("errMsg", "商户贷款订单号不能为空");
			rtnMap.put("errCode", "P3");
			return;
		}	
		if ("".equals(rootInstId) || null == rootInstId) {
			rtnMap.put("errMsg", "商户号不能为空");
			rtnMap.put("errCode", "P4");
			return;
		}	
		if ("".equals(orderId) || null == orderId) {
			rtnMap.put("errMsg", "账户贷款订单号不能为空");
			rtnMap.put("errCode", "P5");
			return;
		}	
	}
	private CreditRateTemplate setRateTemp(String type ,CreditRateTemplateRes rateTemp){
		CreditRateTemplate rateInsert = new CreditRateTemplate();
		if( "1".equals(type)){
			rateInsert.setRateId(redisIdGenerator.createRateId());
		}else{
			rateInsert.setRateId(rateTemp.getRateId());
		}
		if( "3".equals(type)){
			rateInsert.setStatusId(0);
		}else{
			rateInsert.setStatusId(1);	
		}
		rateInsert.setBillDay(rateTemp.getBillDay());
		rateInsert.setRootInstCd(rateTemp.getRootInstCd());
		rateInsert.setRateType(rateTemp.getRateType());
		rateInsert.setRepaymentDay(rateTemp.getRepaymentDay());
		rateInsert.setRateProperty(rateTemp.getRateProperty());
		rateInsert.setRateInterestForm(rateTemp.getRateInterestForm());
		rateInsert.setRateInterestType(rateTemp.getRateInterestType());
		rateInsert.setRateInterestOver(rateTemp.getRateInterestOver());
		rateInsert.setRateInterestOverUnit(rateTemp.getRateInterestOverUnit());
		rateInsert.setOverdueFees(rateTemp.getOverdueFees());
		rateInsert.setOverdueFeesUnit(rateTemp.getOverdueFeesUnit());
		rateInsert.setRateAdvaOneoff(rateTemp.getRateAdvaOneoff());
		rateInsert.setRateAdvaOneoffUnit(rateTemp.getRateAdvaOneoffUnit());
		rateInsert.setAdvanceFeesOneoff(rateTemp.getAdvanceFeesOneoff());
		rateInsert.setAdvanceFeesOneoffUnit(rateTemp.getAdvanceFeesOneoffUnit());
		rateInsert.setRateAdvaSect(rateTemp.getRateAdvaSect());
		rateInsert.setRateAdvaSectUnit(rateTemp.getRateAdvaSectUnit());
		rateInsert.setAdvanceFeesSect(rateTemp.getAdvanceFeesSect());
		rateInsert.setAdvanceFeesSectUnit(rateTemp.getAdvanceFeesSectUnit());
		rateInsert.setProductId(rateTemp.getProductId());
		rateInsert.setProviderId(rateTemp.getProviderId());
		return rateInsert;
	}
	
	private CreditRateTemplateDetail setRateDetailTemp(String typeX,String type,String rateId, CreditRateTemplateRes rateTemp){
		CreditRateTemplateDetail rateDetailInsert = new CreditRateTemplateDetail();		
		rateDetailInsert.setRateId(rateId);
		if("X".equals(typeX)){
			rateDetailInsert.setRateLevel("X");
			rateDetailInsert.setRate(rateTemp.getRateX());
			rateDetailInsert.setRateTime(rateTemp.getRateTimeX());
		}else if("Y".equals(typeX)){
			rateDetailInsert.setRateLevel("Y");
			rateDetailInsert.setRate(rateTemp.getRateY());
			rateDetailInsert.setRateTime(rateTemp.getRateTimeY());
		}
		rateDetailInsert.setRateTimeUnit(rateTemp.getRateTimeUnit());
		rateDetailInsert.setRateUnit(rateTemp.getRateUnit());
		if( "3".equals(type)){
			rateDetailInsert.setStatusId(0);
		}else {
			rateDetailInsert.setStatusId(1);
		}
		return rateDetailInsert;
	}


}
