package com.rkylin.wheatfield.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.GenerationPayment;


public interface SettlementServiceThr {
	
	/**
	 * 审核结果/授信结果读入/通知接口(dubbo)
	 * @param fileTpye ROP上文件类型
	 * @param accountDate	账期
	 * @param batch	批次号
	 * @param type 通知类型：1：审核/授信果  3:通知分润
	 * @return
	 */
	public CommonResponse notifyCreditReslts(String fileTpye,String accountDate,String batch,String type);
	/**
	 * 查询费率模版
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	 Map<String,String> getCreditInfo(String type,String accountDate,String batch);
	
	/**
	 * 代收付汇总
	 * 参数:
	 *   generationType:代收付类型  代收，提现等
	 *   merchantId:商户ID 会堂，丰年等
	 *   orderType:订单类型  代收，代付等
	 *   bussinessCoded：To通联业务代码
	 *   dateType:汇总日期类型  例：0：当日数据汇总 1：t+1日数据汇总 2：t+2日数据汇总 ...
	 * */
	@Transactional
	CommonResponse paymentGeneration(String generationType,String merchantId,int orderType,
			        		String bussinessCoded,int dateType);
	
	/**
	 * 通过代收（课栈向学生）  发起代付（课栈向JRD）
	 * 参数：
	 * @return 状态代码 状态信息
	 * */ 
	@Transactional
	 Map<String,String> withholdToP2P();
	
	/**
	 * 把代收付结果更新订单系统
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map<String,String> updateOrder();
	
	/**
	 * 把代收付结果更新
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map updateCreditAccountSec();
	/**
	 * 上传债券包文件至P2P
	 * @return 状态代码 状态信息
	 * */
	@Transactional
	Map<String, String> uploadDeductFile(String rootInstId, String providerId);
	
	/**
	 * 日终是否正常结束
	 * @return  true:正常       false:结束
	 */
	public boolean isDayEndOk();
	
	/**
	 * 修改代收付结果
	 * @param generationList
	 * @return
	 */
	@Transactional
	public Map<String,String> updateRecAndPayResults(List<GenerationPayment> generationList);
	
	/**
	 * 代收成功后 向P2P发起代付
	 * @param genList
	 * @return
	 */
	public Map<String, String> withholdToP2P(List<GenerationPayment> genList);
	
	/**
	 * 获取发送一批数据的上限
	 * @return
	 */
	public int getBatchLimit();
	
	   /**
     * 清结算汇总的代付结果返回后,调账,将代付的钱分到40142交易的每一个用户
     * Discription:
     * @param transOrderInfo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年4月28日
     */
    public CommonResponse adjustmentWithhold40142(String accountDate,String verify);
    
    /**
     * 汇总清结算发送的40142汇总的代付
     * Discription:
     * @return CommonResponse
     * @author Achilles
     * @since 2016年5月4日
     */
    public CommonResponse summarizing40142();
    
    /**
     * 处理订单状态为11的40142交易
     * Discription:
     * @param orderNo
     * @return CommonResponse
     * @author Achilles
     * @since 2016年5月9日
     */
    public CommonResponse manageBackFail40142(String orderNo);
    
    /**
     * 根据当日提现交易：保理主账户－；每日晚上11：30进行操作，根据当前账期(T-1)的账期进行汇总提现交易且提现状态=4金额，
     * 需要判断保理主账户金额是否足够，不足时，需要发送短信提醒
     * Discription:
     * @return CommonResponse
     * @author Achilles
     * @since 2016年7月7日
     */
    public CommonResponse summaryCreditWithdrawToUpdateFactoring();
}
	
