package test.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.bean.OrderQuery;
import com.rkylin.wheatfield.bean.TransOrderInfo;
import com.rkylin.wheatfield.bean.TransOrderStatusUpdate;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.constant.TransCodeConst;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.TransOrderInfosResponse;
import com.rkylin.wheatfield.pojo.TransOrderInfoQuery;
import com.rkylin.wheatfield.service.OrderService;
import com.rkylin.wheatfield.service.ParameterInfoService;
import com.rkylin.wheatfield.service.TransOrderService;

public class ParameterInfoServiceTest  extends BaseJUnit4Test{
    
    @Autowired
    private ParameterInfoService parameterInfoService;
    
    @Test
    public void getOrdersByPackageNoTest(){
        Map<String, String> map =parameterInfoService.getParaValAndProductIdByParamCode(RedisConstants.INSTCODE_TO_PRODUCT_KEY);
        System.out.println(map);
    }

}
