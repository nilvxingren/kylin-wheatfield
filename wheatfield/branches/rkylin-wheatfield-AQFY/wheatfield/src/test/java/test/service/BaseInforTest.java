package test.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wheatfield.BaseJUnit4Test;
import com.rkylin.wheatfield.model.DictionaryResponse;
import com.rkylin.wheatfield.pojo.DictionaryQuery;
import com.rkylin.wheatfield.service.BaseInforService;

public class BaseInforTest   extends BaseJUnit4Test{

	@Autowired
	private BaseInforService baseInforService;
	
	@Test
	public void getDicInforTest(){
		DictionaryQuery query = new DictionaryQuery();
		query.setDictionaryName("ROOT_INST_CD");
		query.setTableName("FINANACE_ACCOUNT");
		query.setDictionaryRemark("年");
		DictionaryResponse dicList = baseInforService.getDicInfor(null);
		System.out.println(dicList);
	}
}
