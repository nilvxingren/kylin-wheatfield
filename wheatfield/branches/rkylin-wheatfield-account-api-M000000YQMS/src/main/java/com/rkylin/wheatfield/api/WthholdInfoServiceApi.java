package com.rkylin.wheatfield.api;

import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

public interface WthholdInfoServiceApi {
	
	public CommonResponse  execute(TransOrderInfo transOrderInfo);

}
