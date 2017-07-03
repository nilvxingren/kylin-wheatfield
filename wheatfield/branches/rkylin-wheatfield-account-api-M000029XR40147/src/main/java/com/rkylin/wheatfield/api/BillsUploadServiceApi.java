/**
 * @Creator : liuhuan
 * @CreateTime : 2015年12月29日 下午11:04:27
 * @Version : 1.0
 */
package com.rkylin.wheatfield.api;

import java.util.Map;

import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface BillsUploadServiceApi {
	/**
	 * @Description : TODO(单个上传支付凭证记账接口-支付)
	 * @CreateTime : 2015年12月29日 下午11:05:18
	 */
	Map<String,String> billsUpload(TransOrderInfo transOrderInfo);
}
