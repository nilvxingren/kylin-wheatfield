/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.wheatfield.api.AccountService;
import com.rkylin.wheatfield.common.RedisBase;
import com.rkylin.wheatfield.constant.RedisConstants;
import com.rkylin.wheatfield.dao.GenerationPaymentDao;
import com.rkylin.wheatfield.manager.GenerationPaymentManager;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.GenerationPayment;
import com.rkylin.wheatfield.pojo.GenerationPaymentQuery;
import com.rkylin.wheatfield.service.GenerationPaymentService;

import test.wheatfield.BaseJUnit4Test;

public class GenerationPaymentServiceTest extends BaseJUnit4Test {
	
    @Autowired
    private GenerationPaymentDao generationPaymentDao;
    
    @Autowired
    RedisBase redisBase;
    
    @Autowired
    GenerationPaymentService generationPaymentService;
    
    @Test
    public void getGenerationPaymentList() {
        List list = new ArrayList();
        OrderDetails orderDetails = new OrderDetails();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo("14779872000483625631");
//      orderDetail.setOrderNo("20150826171201001");
        orderDetail.setStatusId(15);
        list.add(orderDetail);
//      accountService.refund(orderDetail);
//        orderDetails.setOrderDetails(list);
//        logger.info("accountService=="+generationPaymentService.getGenPaymentList(list, 0));
//        CommonResponse res = new CommonResponse();
//        System.out.println(res.getCode());

    }
    
	@Test
	public void cachePushedGen() {
	    String key = "AchillesHonor";
	    GenerationPaymentQuery example = new GenerationPaymentQuery();
	    example.setRemark("AchillesTest");
	    example.setProcessResult(key);
	    List<GenerationPayment> list = generationPaymentDao.selectByExample(example);
	    OrderDetails orderDetails = new OrderDetails();
	    List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
	    for (GenerationPayment generationPayment : list) {
	        OrderDetail orderDetail = new OrderDetail();
	        orderDetail.setOrderNo(generationPayment.getOrderNo());
	        orderDetail.setStatusId(15);
	        orderDetail.setRequestNo(key);
	        orderDetailList.add(orderDetail);
        }
	    orderDetails.setOrderDetails(orderDetailList);
        redisBase.saveOrUpdateStrSet(RedisConstants.GENERATIONPAYMENT_PUSH, key, 7, TimeUnit.HOURS, false);
        redisBase.saveOrUpdateList(key, orderDetailList, 6, TimeUnit.HOURS);
	}

}
