package com.rkylin.wheatfield.service;

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
}
