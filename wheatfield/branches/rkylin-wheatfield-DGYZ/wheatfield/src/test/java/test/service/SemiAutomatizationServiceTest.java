package test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.wheatfield.BaseJUnit4Test;

import com.rkylin.wheatfield.service.SemiAutomatizationService;

public class SemiAutomatizationServiceTest extends BaseJUnit4Test{
	@Autowired
	private SemiAutomatizationService semiAutomatizationService;
	
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
