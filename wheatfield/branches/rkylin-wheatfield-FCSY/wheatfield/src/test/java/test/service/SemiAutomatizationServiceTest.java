package test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.service.SemiAutomatizationService;

public class SemiAutomatizationServiceTest extends BaseJUnit4Test{
	@Autowired
	private SemiAutomatizationService semiAutomatizationService;
	
    @Autowired
    private SemiAutomatizationServiceApi semiAutomatizationServiceApi;
	
    @Test
    public void operateFinAccountsTest() {
        List<com.rkylin.wheatfield.bean.User> userList = new ArrayList<com.rkylin.wheatfield.bean.User>();
        com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
        user.setInstCode("M000001");
        user.setProductId("P000002");
        user.setUserId("18801438893");
        user.setAmount(10L);
        user.setStatus(0);
        userList.add(user);
        com.rkylin.wheatfield.bean.User user2 = new com.rkylin.wheatfield.bean.User();
        user2.setInstCode("M000001");
        user2.setProductId("P000010");
        user2.setUserId("18801438893");
        user2.setAmount(5L);
        user2.setStatus(1);
        userList.add(user2);
        CommonResponse res =semiAutomatizationServiceApi.operateFinAccounts(userList);
        System.out.println(res);
    }
    
    @Test
    public void operateFinAccountTest() {
        com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
        user.setInstCode("M000001");
        user.setProductId("P000002");
        user.setUserId("18701514648");
        user.setAmount(1L);
        user.setStatus(1);
        CommonResponse res =semiAutomatizationServiceApi.operateFinAccount(user);
        System.out.println(res);
    }
    
	@Test
	public void testForAccount() {
		List<Map<String,String>> paramsValueList = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
//		map.put("amount", "1998436");
//		map.put("userid", "141223100000001");
//		map.put("productid", "P000002");
//		map.put("consid", "M000001");
//		map.put("status", "0");
//		map.put("type", "1001");
		
		map.put("amount", "1998436");
		map.put("userid", "141223100000007");
		map.put("productid", "P000002");
		map.put("consid", "M000001");
		map.put("status", "0");
		map.put("type", "1001");
		
		paramsValueList.add(map);
		semiAutomatizationService.ForAccount(paramsValueList);
	}

}
