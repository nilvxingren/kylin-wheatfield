package test.wheatfield;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 执行完毕后保存数据库操作记录
 * @author zhenpc
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"classpath*:spring-web.xml"})
//@ContextConfiguration(locations={"classpath*:testContext.xml"})
public abstract class BaseJUnit4Test extends AbstractJUnit4SpringContextTests  {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		
		return jdbcTemplate;
	}
	
	public void save(String sql) {
		jdbcTemplate.execute(sql);
	}

	public void update(String sql) {
		jdbcTemplate.execute(sql);
	}

	public void delete(String sql) {
		jdbcTemplate.execute(sql);
	}
	
	public List<?> query(String sql) {
		return jdbcTemplate.queryForList(sql);
	}
	
	public Object queryForObject(String sql){
		return jdbcTemplate.queryForMap(sql);
	}
}
