/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package test.wheatfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.wheatfield.manager.GenerationPaymentHistoryManager;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistory;
import com.rkylin.wheatfield.pojo.GenerationPaymentHistoryQuery;

public class GenerationPaymentHistoryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("generationPaymentHistoryManager")
	private GenerationPaymentHistoryManager generationPaymentHistoryManager;


	public void testNewGenerationPaymentHistory() {
		GenerationPaymentHistory GenerationPaymentHistory = new GenerationPaymentHistory();
		generationPaymentHistoryManager.saveGenerationPaymentHistory(GenerationPaymentHistory);
	}


	public void testUpdateGenerationPaymentHistory(){
		GenerationPaymentHistory GenerationPaymentHistory = new GenerationPaymentHistory();
		GenerationPaymentHistory.setGeneId(2l);
		generationPaymentHistoryManager.saveGenerationPaymentHistory(GenerationPaymentHistory);
	}
	

	public void testDeleteGenerationPaymentHistory(){
		generationPaymentHistoryManager.deleteGenerationPaymentHistoryById(99L);
	}
	

	public void testDeleteGenerationPaymentHistoryByQuery(){
		GenerationPaymentHistoryQuery query = new GenerationPaymentHistoryQuery();
		generationPaymentHistoryManager.deleteGenerationPaymentHistory(query);
	}


	public void testFindGenerationPaymentHistoryById(){
		GenerationPaymentHistoryQuery query = new GenerationPaymentHistoryQuery();
		int size = generationPaymentHistoryManager.queryList(query).size();
		System.out.println(size);
	}
}
