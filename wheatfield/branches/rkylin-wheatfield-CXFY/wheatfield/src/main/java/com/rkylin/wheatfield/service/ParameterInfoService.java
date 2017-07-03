package com.rkylin.wheatfield.service;

import java.util.Map;

import com.rkylin.wheatfield.model.PingAnParam;
import com.rkylin.wheatfield.pojo.ParameterInfo;

public interface ParameterInfoService {
    public ParameterInfo getParameterInfoByParaCode(String parameterCode);
    
	public ParameterInfo getParameterInfo(String ParameterCode);
	
	  /**
     * 获取与平安银行外部转账有关的参数
     * Discription:
     * @return PingAnParam
     * @author Achilles
     * @since 2016年5月9日
     */
    public PingAnParam getPingAnOutTransferParam();
    
    /**
      * 获取与平安银行开户有关的参数
      * Discription:
      * @return PingAnParam
      * @author Achilles
      * @since 2016年5月9日
  */
    public PingAnParam getPingAnOpenParam();
    
    /**
   * 获取与平安银行内部转账有关的参数
   * Discription:
   * @return PingAnParam
   * @author Achilles
   * @since 2016年5月9日
   */
  public PingAnParam getPingAnInteriorTransferParam();
  
  public ParameterInfo getParaInfoByProductId(String productId);
  
  /**
   * 获取并更新平安子账户序列号
   * Discription:
   * @param productId
   * @return ParameterInfo
   * @author Achilles
   * @since 2016年5月24日
   */
  public String getSubAccountSeq(String productId);
  
  public String getParameterValByParameterCode(String parameterCode);
  
  /**
   * Discription: 查找参数表，如果返回多个参数值，以逗号分隔 并 返回
   * @param parameterCode
   * @return String
   * @author liuhuan
   * @since 2016年11月16日
   */
  public String getParameterValue(String parameterCode);
  
  /**
   * Discription:根据code获取val及product_id,先从缓存获取,缓存没有去数据库,然后更新缓存
   * @param parameterCode
   * @return Map<String,String>
   * @author Achilles
   * @since 2016年10月25日
   */
  public Map<String, String> getParaValAndProductIdByParamCode(String parameterCode);
}
