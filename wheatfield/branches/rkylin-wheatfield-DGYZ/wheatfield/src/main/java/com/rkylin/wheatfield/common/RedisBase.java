package com.rkylin.wheatfield.common;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.rkylin.crps.pojo.OrderDetail;

@Component
public class RedisBase {
	private static Logger logger = LoggerFactory.getLogger(RedisBase.class);
	
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	
	@Autowired
	private RedisTemplate<String, Set<String>> redisTemplateSet;
	
	@Autowired
	private RedisTemplate<String, List> redisTemplateList;
	
	@Autowired
	private RedisTemplate<String, Set<String>> redisTemplateStrSet;
	
	@Autowired
	private RedisTemplate<String,String[]> redisTemplateArray;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplateMap;
	
	
	public List getList(String key){
		return redisTemplateList.opsForValue().get(key);
	}
	
	/**
	 * 根据传入的set缓存或修改原有缓存的set
	 * @param key  健
	 * @param orderNoSet 传入集合
	 * @param timeout  有效期时间
	 * @param unit	单位
	 * isDel 是否删除  true 是
	 */
	public void saveOrUpdateStrSet(String key,String str,long timeout,TimeUnit unit,boolean isDel){
		Set<String> allStrSet = redisTemplateStrSet.opsForValue().get(key);
		if (isDel) {
			if (allStrSet==null) {
				return;
			}else{
				allStrSet.remove(str);
			}
		}else{
			if (allStrSet==null) {
				allStrSet = new HashSet<String>();
				allStrSet.add(str);
			}else{
				allStrSet.add(str);
			}
		}
		redisTemplateStrSet.opsForValue().set(key,allStrSet, timeout,unit);
	}
	
	
	/**
	 * 根据传入的set缓存或修改原有缓存的set
	 * @param key  健
	 * @param orderNoSet 传入集合
	 * @param timeout  有效期时间
	 * @param unit	单位
	 */
	public void saveOrUpdateList(String key,List list,long timeout,TimeUnit unit){
		List orDeList = redisTemplateList.opsForValue().get(key);
		if (orDeList==null) {
			redisTemplateList.opsForValue().set(key,list, timeout,unit);
		}else{
			orDeList.addAll(list);
			redisTemplateList.opsForValue().set(key,orDeList, timeout,unit);
		}
	}
	
	/**
	 * 根据传入的set缓存或修改原有缓存的set
	 * @param key  健
	 * @param orderNoSet 传入集合
	 * @param timeout  有效期时间
	 * @param unit	单位
	 */
	public void saveOrUpdateSet(String key,Set<String> orderNoSet,long timeout,TimeUnit unit){
		Set<String> orderNoSetAll = redisTemplateSet.opsForValue().get(key);
		if (orderNoSetAll==null) {
			redisTemplateSet.opsForValue().set(key,orderNoSet, timeout,unit);
		}else{
			orderNoSetAll.addAll(orderNoSet);
			redisTemplateSet.opsForValue().set(key,orderNoSetAll, timeout,unit);
		}
	}
	
	/**
	 * 获取缓存的set
	 * @param key
	 * @return
	 */
	public Set<String> getSet(String key){
		return redisTemplateSet.opsForValue().get(key);
	}
	
	/**
	 * 删除缓存的set
	 * @param key
	 */
	public void delSet(String key){
		redisTemplateSet.delete(key);;
	}
	
	public void addArray(String key,String[] addArray){
		// M000001_ORDER_NO_ARRAY_20151106    FNPAYMENT_ST20151027028_ARRAY
		String[] orderaddArrayy = redisTemplateArray.opsForValue().get(key);
		logger.info("初始值======orderaddArrayy==="+orderaddArrayy);
		redisTemplateArray.opsForValue().set(key,addArray, 50, TimeUnit.SECONDS);
		logger.info("修改后值======addArray==="+addArray);
	}
	
	public void add(String key,Map<String,String> map){
		redisTemplateMap.opsForHash().putAll(key, map);
		redisTemplate.expire(key, 30, TimeUnit.SECONDS);
//		redisTemplateMap.boundValueOps(key).expire(30, TimeUnit.SECONDS);
	}
	
	public void addSetByOneValue(String key,String value){
		Set<String> orderNoSet = redisTemplateSet.opsForValue().get(key);
		logger.info("初始值======orderNoSet==="+orderNoSet);
		if (orderNoSet==null) {
			orderNoSet = new HashSet<String>();
		}
		orderNoSet.add(value);
		redisTemplateSet.opsForValue().set(key,orderNoSet, 120, TimeUnit.SECONDS);
		logger.info("修改后值======orderNoSet==="+orderNoSet);
	}
	
	public void delSet(String key,String value){
		Set<String> orderNoSet = redisTemplateSet.opsForValue().get(key);
		logger.info("初始值======orderNoSet==="+orderNoSet);
		if (orderNoSet!=null) {
			orderNoSet = new HashSet<String>();
		}
		orderNoSet.add(value);
		redisTemplateSet.opsForValue().set(key,orderNoSet, 120, TimeUnit.SECONDS);
		logger.info("修改后值======orderNoSet==="+orderNoSet);
	}
	
//	  public void saveUser(final User user) {
//    redisTemplate.execute(new RedisCallback<Object>() {
//
//        public Object doInRedis(RedisConnection connection) throws DataAccessException {
//            connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
//                           redisTemplate.getStringSerializer().serialize(user.getName()));
//            return null;
//        }
//    });
//}

}
