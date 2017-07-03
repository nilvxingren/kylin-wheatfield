/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.ParameterInfoManager;
import com.rkylin.wheatfield.pojo.ParameterInfo;
import com.rkylin.wheatfield.pojo.ParameterInfoQuery;

public class ParameterInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("parameterInfoManager")
	private ParameterInfoManager parameterInfoManager;


	public void testNewParameterInfo() {
		ParameterInfo ParameterInfo = new ParameterInfo();
		parameterInfoManager.saveParameterInfo(ParameterInfo);
	}


	public void testUpdateParameterInfo(){
		ParameterInfo ParameterInfo = new ParameterInfo();
//		ParameterInfo.setId(2l);
		parameterInfoManager.saveParameterInfo(ParameterInfo);
	}
	

	public void testDeleteParameterInfo(){
		parameterInfoManager.deleteParameterInfoById(99L);
	}
	

	public void testDeleteParameterInfoByQuery(){
		ParameterInfoQuery query = new ParameterInfoQuery();
		parameterInfoManager.deleteParameterInfo(query);
	}


	public void testFindParameterInfoById(){
		ParameterInfoQuery query = new ParameterInfoQuery();
		int size = parameterInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
