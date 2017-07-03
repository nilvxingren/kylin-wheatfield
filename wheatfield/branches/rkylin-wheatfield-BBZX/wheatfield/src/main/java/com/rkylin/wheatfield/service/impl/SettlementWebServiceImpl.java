package com.rkylin.wheatfield.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.database.BaseDao;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.wheatfield.common.DateUtils;
import com.rkylin.wheatfield.common.PartyCodeUtil;
import com.rkylin.wheatfield.constant.Constants;
import com.rkylin.wheatfield.constant.SettleConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.exception.AccountException;
import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.manager.SettleBalanceEntryManager;
import com.rkylin.wheatfield.manager.SettleSplittingEntryManager;
import com.rkylin.wheatfield.manager.SettleTransTabManager;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.pojo.SettleBalanceEntry;
import com.rkylin.wheatfield.pojo.SettleBalanceEntryQuery;
import com.rkylin.wheatfield.pojo.SettleSplittingEntry;
import com.rkylin.wheatfield.pojo.SettleTransTab;
import com.rkylin.wheatfield.pojo.SettleTransTabQuery;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.response.ErrorResponse;
import com.rkylin.wheatfield.service.PaymentAccountService;
import com.rkylin.wheatfield.service.PaymentInternalService;
import com.rkylin.wheatfield.service.SettlementWebService;
import com.rkylin.wheatfield.utils.SettlementUtils;


@Service("settlementWebService")
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class SettlementWebServiceImpl extends BaseDao implements
		SettlementWebService {

	@Autowired
	HttpServletRequest request;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	PaymentInternalService paymentInternalService;
	@Autowired
	RedisIdGenerator redisIdGenerator;
	@Autowired
	SettleSplittingEntryManager settleSplittingEntryManager;
	@Autowired
	SettleBalanceEntryManager settleBalanceEntryManager;
	@Autowired
	PaymentAccountService paymentAccountService;
	@Autowired
	Properties userProperties;
	@Autowired
	SettleTransTabManager settleTransTabManager;
	//上传文件路径
	private static final String PATH = SettleConstants.FILE_UP_PATH +"webchannel";
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> updateSettleFile(String invoicedate,String fileName) {
		logger.info("通过分润结果更新用户余额 ————————————START————————————");
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		//RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
    	keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
		parameterInfo = parameterInfoManager.queryList(keyList);
    	SettlementUtils settlementUtils = new SettlementUtils();
    	String accountDate = "";;
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = settlementUtils.getAccountDate(parameterInfo.get(0).getParameterValue(), "yyyyMMdd",0);
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					//responseR = errorResponseService.getErrorResponse("S1", "账期无效");
					return null;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    	}
			logger.info("取得的账期为"+ accountDate);

			// 取得分润结果文件
			//logger.info("获取授信文件，调用P2P接口1");
	    	//String filename = "FN_FR_" + accountDate+"."+SettleConstants.FILE_CSV;
	    	File file = new File(PATH);
	    	if (!file.exists()) {
	    		file.mkdirs();
	    	}
			logger.info("读取本地上传分润结果文件");
			String path=PATH +"/"+ fileName;
			file = new File(path);
			if (!file.exists()) {
				logger.error("本地上传分润结果文件不存在!");
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "上传分润结果文件不存在!");
				return rtnMap;
			}
//			file = new File("C:\\test\\7e57a424-460f-416d-9877-15892abdbeed.csv");
			TxtReader txtReader = new TxtReader();
			List<Map> fileList = new ArrayList<Map>();
			try {
				txtReader.setEncode("UTF-8");
				fileList = txtReader.txtreader(file , SettleConstants.DEDT_SPLIT2);
			} catch(Exception e) {
				logger.error("丰年分润结果文件操作异常！" + e.getMessage());
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "丰年分润结果文件操作异常！");
				return rtnMap;
			}

			if (fileList.size() == 0) {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "丰年分润结果文件内容异常！");
				return rtnMap;
			}
			
			//第一行	帐期}总笔数|总金额
			String amountmax = (String)fileList.get(0).get("L_2");
			
			// 检查总金额
			Map paraMap = new HashMap();
//			paraMap.put("ACCOUNT_DATE", accountDate + " 00:00:00");
//			paraMap.put("FUNC_CODE", TransCodeConst.CHARGE);
//			paraMap.put("STATUS", SettleConstants.SETTLE_STU_1);
			SettleBalanceEntryQuery settleBalanceEntryQuery =new  SettleBalanceEntryQuery();
			settleBalanceEntryQuery.setSettleTime(formatter3.parse(accountDate + " 00:00:00"));
			settleBalanceEntryQuery.setStatusId(SettleConstants.SETTLE_STU_1);
			List<SettleBalanceEntry> rtnList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
			List<SettleBalanceEntry> upList = new ArrayList<SettleBalanceEntry>();
			SettleBalanceEntry uosbe = new SettleBalanceEntry();
			Long amountmaxto = new Long(0);
			for (SettleBalanceEntry bean:rtnList) {
				uosbe = new SettleBalanceEntry();
				if (TransCodeConst.CREDIT_CONSUME.equals(bean.getRetriRefNo()) || TransCodeConst.DEBIT_CONSUME.equals(bean.getRetriRefNo())) {
					amountmaxto += bean.getAmount();
					uosbe.setSettleId(bean.getSettleId());
					uosbe.setStatusId(99);//分润完了转台
					upList.add(uosbe);
				}
			}
			
			if (amountmax.compareTo(amountmaxto.toString()) == 0) {
				
			} else {
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "分润文件金额与实际交易不符！文件内金额["+amountmax+"],DB中["+amountmaxto+"]");
				//RkylinMailUtil.sendMailThread("分润文件金额与实际交易不符","分润文件金额与实际交易不符,文件内金额["+amountmax+"],DB中["+amountmaxto+"]", "21401233@qq.com");
				return rtnMap;
			}
			
			//第一行	帐期}总笔数|总金额
			//明细行	帐期|机构号|台长号|用户号|金额|备注
			//明细行	帐期,机构号,用户号,台长号,金额,备注
			ErrorResponse errorResponse = null;
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			SettleSplittingEntry settleSplittingEntry = new SettleSplittingEntry();
			for (Map<String,String> subMap : fileList) {
				if (subMap.size() <4) {
					continue;
				}
				settleSplittingEntry.setSettleId(redisIdGenerator.settleEntryNo());
				settleSplittingEntry.setRootInstCd(subMap.get("L_1"));
				settleSplittingEntry.setAccountDate(formatter1.parse(subMap.get("L_0")));
				settleSplittingEntry.setAccountRelateId(subMap.get("L_3"));
				settleSplittingEntry.setUserId(subMap.get("L_2"));
				settleSplittingEntry.setSettleType(SettleConstants.SETTLE_TYPE_5);
				settleSplittingEntry.setAmount(Long.parseLong(subMap.get("L_4")));
				finanaceEntry = new FinanaceEntry();
				finanaceEntry.setPaymentAmount(Long.parseLong(subMap.get("L_4")));
				finanaceEntry.setReferId(settleSplittingEntry.getSettleId());
				if (subMap.get("L_3") != null && !"".equals(subMap.get("L_3"))) {
					errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
							settleSplittingEntry.getRootInstCd(),Constants.ADVANCE_ACCOUNT,settleSplittingEntry.getAccountRelateId());
				} else {
					errorResponse = paymentInternalService.fenrun(finanaceEntry, settleSplittingEntry.getUserId(),
							settleSplittingEntry.getRootInstCd(),Constants.FN_PRODUCT,settleSplittingEntry.getAccountRelateId());
				}
				
				if (errorResponse.isIs_success() == true) {
					settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_1);
				} else {
					settleSplittingEntry.setStatusId(SettleConstants.SETTLE_STU_0);
					settleSplittingEntry.setRemark("账户操作失败！"+errorResponse.getMsg());
				}
				settleSplittingEntryManager.saveSettleSplittingEntry(settleSplittingEntry);
			}
			
			if (upList.size() > 0) {
				for (SettleBalanceEntry bean:upList) {
					settleBalanceEntryManager.saveSettleBalanceEntry(bean);
				}
			}
			
			//分润结束，退款挂账开始
			SettleTransTabQuery settleTransTabQuery = new SettleTransTabQuery();
			settleTransTabQuery.setStatusId(0);
			List<SettleTransTab> settleTransTabList = settleTransTabManager.queryList(settleTransTabQuery);
			TransOrderInfo transOrderInfo = new TransOrderInfo();
			SettleTransTab settleTransTabbean = new SettleTransTab();
			errorResponse = new ErrorResponse();
			if (settleTransTabList.size() > 0) {
				Long balanceAmount = Long.parseLong("0");
				Long acAmount = Long.parseLong("0");
				for (SettleTransTab settleTransTab:settleTransTabList) {
					transOrderInfo = new TransOrderInfo();
					settleTransTabbean = new SettleTransTab();
					balanceAmount = paymentInternalService.getInternalBalance(settleTransTab.getUserId(), settleTransTab.getRootInstCd(), settleTransTab.getGroupManage(), settleTransTab.getReferUserId());
					if (balanceAmount == 0) {
						
					} else{
						transOrderInfo.setAmount(settleTransTab.getAmount());
						transOrderInfo.setFuncCode(TransCodeConst.ADJUST_ACCOUNT_AMOUNT);
						if (Constants.FN_ID.equals(settleTransTab.getRootInstCd())) {
							transOrderInfo.setInterMerchantCode(TransCodeConst.THIRDPARTYID_FNZZHQY);
						}
						transOrderInfo.setMerchantCode(settleTransTab.getRootInstCd());
						transOrderInfo.setOrderCount(1);
						transOrderInfo.setOrderDate(new Date());
						transOrderInfo.setOrderNo(redisIdGenerator.createRequestNo());
						transOrderInfo.setOrderPackageNo(settleTransTab.getOrderNo());
						transOrderInfo.setRemark("qjs_tuikuan");
						transOrderInfo.setRequestNo(settleTransTab.getBatchNo());
						transOrderInfo.setRequestTime(new Date());
						transOrderInfo.setStatus(1);
						transOrderInfo.setUserFee(Long.parseLong("0"));
						if ( balanceAmount >= settleTransTab.getAmount()) {
							transOrderInfo.setOrderAmount(settleTransTab.getAmount());
							settleTransTabbean.setTabId(settleTransTab.getTabId());
							settleTransTabbean.setStatusId(1);
						} else {
							transOrderInfo.setOrderAmount(balanceAmount);
							acAmount = settleTransTab.getAmount();
							settleTransTabbean = settleTransTab;
							settleTransTabbean.setAmount(acAmount - balanceAmount);
						}
						errorResponse = paymentAccountService.transfer(transOrderInfo, settleTransTab.getGroupManage(), settleTransTab.getUserId());
						if (errorResponse.isIs_success() == true) {
							settleTransTabManager.saveSettleTransTab(settleTransTabbean);
						} else {
							logger.info("分润结束，退款挂账:账户余额：["+balanceAmount+"]需要退款：["+settleTransTab.getAmount()+"],账户操作失败！");
				    		//RkylinMailUtil.sendMailThread("分润挂账退款","分润结束，退款挂账:账户余额：["+balanceAmount+"]需要退款：["+settleTransTab.getAmount()+"],账户操作失败！", "21401233@qq.com");
						}
					}
				}
			}
    	} catch (Exception z) {
			logger.error("分润结果更新用户余额失败，请联系相关负责人");
			rtnMap.put("errCode", "0003");
			rtnMap.put("errMsg", "通过分润结果更新用户余额！");
			return rtnMap;
    	}
		logger.info("通过分润结果更新用户余额 ————————————END————————————"); 
		return rtnMap;
	}
	
    public boolean fileUpload(MultipartFile fileUpload,String fileName){
		logger.info("文件上传 ————————————Start————————————"); 
		try{
			//用来限制用户上传文件大小的   
			File filePath = new File(PATH);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			writeToFile(PATH,fileName,fileUpload); 
	    }catch(Exception ex){
			logger.info("文件上传失败：" + ex);
			return false;
    	}
		logger.info("文件上传 ————————————End————————————");   	
		return true;
    }
	
	public static void writeToFile(String path,String fileName, MultipartFile fileUpload) {
		try {
            File uploadFile = new File(path,fileName);
            InputStream is = fileUpload.getInputStream();
            FileOutputStream fos = new FileOutputStream(uploadFile);
            byte[] tmp = new byte[1024];
            int len = -1;
            while ((len = is.read(tmp)) != -1) {
                fos.write(tmp, 0, len);
            }
            is.close();
            fos.flush();
            fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional(rollbackFor = AccountException.class, propagation = Propagation.NESTED)
	public Map<String,String> correct(String newOrderNo,String oldOrderNo,String rootInstCd,String funcCode) {
		logger.info("差错处理冲正&抹账 ————————————START————————————");
		HashMap<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
    	try {
			ErrorResponse errorResponse = null;
	    	TransOrderInfo transOrderInfo = new TransOrderInfo();
    		if (funcCode == null || "".equals(funcCode)) {
    			transOrderInfo.setOrderNo(newOrderNo);
				transOrderInfo.setOrderPackageNo(oldOrderNo);
				transOrderInfo.setMerchantCode(rootInstCd);
				errorResponse = paymentInternalService.wipeAccount(transOrderInfo);
				if (errorResponse.isIs_success() == true) {
				} else {
					rtnMap.put("errCode", errorResponse.getCode());
					rtnMap.put("errMsg", errorResponse.getMsg());
				}
    		} else {
    			transOrderInfo.setOrderNo(newOrderNo);
				transOrderInfo.setOrderPackageNo(oldOrderNo);
				transOrderInfo.setMerchantCode(rootInstCd);
				transOrderInfo.setFuncCode(funcCode);
				errorResponse = paymentAccountService.antiDeduct(transOrderInfo);
				if (errorResponse.isIs_success() == true) {
				} else {
					rtnMap.put("errCode", errorResponse.getCode());
					rtnMap.put("errMsg", errorResponse.getMsg());
				}
    		}
	    	
    	} catch (Exception z) {
			logger.error("错处理冲正&抹账失败，请联系相关负责人" + z.getMessage());
			rtnMap.put("errCode", "0003");
			rtnMap.put("errMsg", "通错处理冲正&抹账失败！");
			return rtnMap;
    	}
		logger.info("差错处理冲正&抹账 ————————————END————————————");
		return rtnMap;
	}

}
