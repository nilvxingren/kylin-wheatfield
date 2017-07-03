package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.model.CommonResponse;

public interface SettlementServiceThrDubboService {

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
     * 代收付汇总
     * 参数:
     *   generationType:代收付类型  代收，提现等
     *   merchantId:商户ID 会堂，丰年等
     *   orderType:订单类型  代收，代付等
     *   bussinessCoded：To通联业务代码
     *   dateType:汇总日期类型  例：0：当日数据汇总 1：t+1日数据汇总 2：t+2日数据汇总 ...
     * */
	public CommonResponse paymentGeneration(String generationType,String merchantId,int orderType,
                            String bussinessCoded,int dateType);
}
