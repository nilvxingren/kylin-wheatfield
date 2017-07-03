package com.rkylin.wheatfield.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.model.PingAnParam;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;
import com.rkylin.wheatfield.service.ParameterInfoService;
@Service("parameterInfoService")
public class ParameterInfoServiceImpl implements ParameterInfoService {
    private static Logger logger = LoggerFactory
            .getLogger(ParameterInfoServiceImpl.class);
    
	@Autowired
	ParameterInfoManager parameterInfoManager;
	
    @Autowired
    private RedisTemplate<String, String> redisTemplateMap;
    
    @Autowired
    private RedisTemplate<String, Map<String, String>> redisTemplateStrToMap;    
	
	@Override
	public ParameterInfo getParameterInfo(String parameterCode) {
		ParameterInfoQuery query =new ParameterInfoQuery();
		query.setParameterCode(parameterCode);
		List<ParameterInfo> pList=parameterInfoManager.queryList(query);
		if(pList!=null && pList.size()>0){
			return pList.get(0);
		}
		return null;
	}
	
    public ParameterInfo getParameterInfoByParaCode(String parameterCode) {
        ParameterInfoQuery query =new ParameterInfoQuery();
        query.setParameterCode(parameterCode);
        query.setStatus(1);
        List<ParameterInfo> pList=parameterInfoManager.queryList(query);
        if(pList!=null && pList.size()>0){
            return pList.get(0);
        }
        return null;
     }
	
	public ParameterInfo getParaInfoByProductId(String productId) {
	    ParameterInfoQuery query =new ParameterInfoQuery();
        query.setProductId(productId);
        query.setStatus(1);
        List<ParameterInfo> pList=parameterInfoManager.queryList(query);
        if(pList.size()!=0){
            return pList.get(0);
        }
        return null;
	}
	
	  /**
     * 获取与平安银行外部转账有关的参数
     * Discription:
     * @return PingAnParam
     * @author Achilles
     * @since 2016年5月9日
     */
    public PingAnParam getPingAnOutTransferParam(){
        ParameterInfoQuery paraQuery = new ParameterInfoQuery();
        paraQuery.setParameterCode("PINGAN_DEAL_INST");
        paraQuery.setStatus(1);
        ParameterInfo parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置机构号");
            return null;
        }
        PingAnParam pingAnParam = new PingAnParam();
        pingAnParam.setDealInst(parameterInfo.getParameterValue());
                
        paraQuery.setParameterCode("GATEROUTER_MD5_KEY");
        parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置加密key");
            return null;
        }
        pingAnParam.setMd5key(parameterInfo.getParameterValue());
        return pingAnParam;
    }
	
	/**
	 * 获取与平安银行内部转账有关的参数
	 * Discription:
	 * @return PingAnParam
	 * @author Achilles
	 * @since 2016年5月9日
	 */
	public PingAnParam getPingAnInteriorTransferParam(){
        ParameterInfoQuery paraQuery = new ParameterInfoQuery();
        paraQuery.setParameterCode("PINGAN_DEAL_INST");
        paraQuery.setStatus(1);
        ParameterInfo parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置机构号");
            return null;
        }
        PingAnParam pingAnParam = new PingAnParam();
        pingAnParam.setDealInst(parameterInfo.getParameterValue());
        
        paraQuery.setParameterCode(null);
        paraQuery.setProductId("PINGAN_MAIN_ACCOUNT");
        parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置主账户");
            return null;
        }
        pingAnParam.setMainAccountNo(parameterInfo.getParameterCode());
        pingAnParam.setMainAccountName(parameterInfo.getParameterValue());
        
        paraQuery.setProductId(null);
        paraQuery.setParameterCode("GATEROUTER_MD5_KEY");
        parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置加密key");
            return null;
        }
        pingAnParam.setMd5key(parameterInfo.getParameterValue());
        return pingAnParam;
	}

	   /**
     * 获取与平安银行开户有关的参数
     * Discription:
     * @return PingAnParam
     * @author Achilles
     * @since 2016年5月9日
     */
    public PingAnParam getPingAnOpenParam(){
        ParameterInfoQuery paraQuery = new ParameterInfoQuery();
        paraQuery.setParameterCode("PINGAN_OPEN_INST");
        paraQuery.setStatus(1);
        ParameterInfo parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置机构号");
            return null;
        }
        PingAnParam pingAnParam = new PingAnParam();
        pingAnParam.setOpenInst(parameterInfo.getParameterValue());
        
        paraQuery.setParameterCode(null);
        paraQuery.setProductId("PINGAN_MAIN_ACCOUNT");
        parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置主账户");
            return null;
        }
        pingAnParam.setMainAccountNo(parameterInfo.getParameterCode());
        pingAnParam.setMainAccountName(parameterInfo.getParameterValue());
        
        paraQuery.setProductId(null);
        paraQuery.setParameterCode("GATEROUTER_MD5_KEY");
        parameterInfo =parameterInfoManager.getParameterInfo(paraQuery);
        if (parameterInfo==null) {
            logger.info("系统异常,参数表未设置加密key");
            return null;
        }
        pingAnParam.setMd5key(parameterInfo.getParameterValue());
        return pingAnParam;
    }
	/**
	 * 获取并更新平安子账户序列号
	 * Discription:
	 * @param parameterCode
	 * @return ParameterInfo
	 * @author Achilles
	 * @since 2016年5月24日
	 */
    public String getSubAccountSeq(String productId) {
        String seq = null;
        ParameterInfoQuery query =new ParameterInfoQuery();
        query.setProductId(productId);
        query.setStatus(1);
        List<ParameterInfo> paramList=parameterInfoManager.queryList(query);
        if(paramList.size()==0){
            return null;
        }
        String paramType = paramList.get(0).getParameterType();
        Long subAccountSeq = 0L;
        if ("0".equals(paramType)) {//表示从PARAMETER_VALUE字段累加,(PARAMETER_VALUE是每次累加后的数字)
            seq = paramList.get(0).getParameterValue();
            subAccountSeq = Long.parseLong(seq)+1;
            seq = String.valueOf(subAccountSeq);
        }else if("1".equals(paramType)){//表示从PARAMETER_CODE字段累加,(PARAMETER_CODE是基准数)
            seq = paramList.get(0).getParameterCode();
            subAccountSeq = Long.parseLong(seq)+1;
            seq = String.valueOf(subAccountSeq);
        }
        if (seq!=null) {
            ParameterInfo paramInfo = new ParameterInfo();
            paramInfo.setParameterId(paramList.get(0).getParameterId());
            paramInfo.setParameterValue(seq);
            parameterInfoManager.saveParameterInfo(paramInfo);
        }
        return seq;
    }
    
  @Override   
  public String getParameterValByParameterCode(String parameterCode) {
      String parameterVal = redisTemplateMap.opsForValue().get(parameterCode);
      if (parameterVal!=null) {
          return parameterVal;
      }
      parameterVal = parameterInfoManager.getParameterValueByParameterCode(parameterCode);
      if ("".equals(parameterVal)) {
          parameterVal=null;
      }
      if (parameterVal!=null) {
          redisTemplateMap.opsForValue().set(parameterCode,parameterVal);
      }
      return parameterVal; 
  }

  @Override
  public Map<String, String> getParaValAndProductIdByParamCode(String parameterCode) {
      Map<String, String> instToProductMap = redisTemplateStrToMap.opsForValue().get(parameterCode);
      if (instToProductMap!=null) {
          return instToProductMap;
      }
      List<ParameterInfo> list = parameterInfoManager.getParameterInfoByParamCode(parameterCode);
      if (list.size()==0) {
          return null; 
      }
      instToProductMap = new HashMap<String, String>();
      for (ParameterInfo parameterInfo : list) {
          instToProductMap.put(parameterInfo.getParameterValue(), parameterInfo.getProductId());
      }
      redisTemplateStrToMap.opsForValue().set(parameterCode, instToProductMap);
      return instToProductMap; 
  }
}
