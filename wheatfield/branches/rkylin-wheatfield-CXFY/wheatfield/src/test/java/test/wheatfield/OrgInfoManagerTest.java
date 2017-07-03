/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.OrgInfoManager;
import com.rkylin.wheatfield.pojo.OrgInfo;
import com.rkylin.wheatfield.pojo.OrgInfoQuery;

public class OrgInfoManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("orgInfoManager")
	private OrgInfoManager orgInfoManager;


	public void testNewOrgInfo() {
		OrgInfo OrgInfo = new OrgInfo();
		orgInfoManager.saveOrgInfo(OrgInfo);
	}


	public void testUpdateOrgInfo(){
		OrgInfo OrgInfo = new OrgInfo();
//		OrgInfo.setId(2l);
		orgInfoManager.saveOrgInfo(OrgInfo);
	}
	

	public void testDeleteOrgInfo(){
		orgInfoManager.deleteOrgInfoById(99L);
	}
	

	public void testDeleteOrgInfoByQuery(){
		OrgInfoQuery query = new OrgInfoQuery();
		orgInfoManager.deleteOrgInfo(query);
	}


	public void testFindOrgInfoById(){
		OrgInfoQuery query = new OrgInfoQuery();
		int size = orgInfoManager.queryList(query).size();
		System.out.println(size);
	}
}
