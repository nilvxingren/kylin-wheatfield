package com.rkylin.wheatfield.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rkylin.wheatfield.api.BaseInforDubboService;
import com.rkylin.wheatfield.dao.DictionaryDao;
import com.rkylin.wheatfield.model.DictionaryResponse;
import com.rkylin.wheatfield.pojo.Dictionary;
import com.rkylin.wheatfield.pojo.DictionaryQuery;
import com.rkylin.wheatfield.service.BaseInforService;
import com.rkylin.wheatfield.utils.BeanUtil;
import com.rkylin.wheatfield.utils.CodeEnum;

@Service("baseInforService")
public class BaseInforServiceImpl implements BaseInforService,BaseInforDubboService{

	private static Logger logger = LoggerFactory.getLogger(BaseInforServiceImpl.class);	
	
	@Autowired
	@Qualifier("dictionaryDao")
	private DictionaryDao dictionaryDao;
	
	/**
	 * 查询字典信息
	 */
	@Override
	public DictionaryResponse getDicInfor(DictionaryQuery query) {
		logger.info("查询字典信息   入参所有字段值:"+BeanUtil.getBeanVal(query, null));
		DictionaryResponse res = new DictionaryResponse();
		if (query==null || BeanUtil.validateBeanProEmpty(query, new String[]{"tableName","dictionaryName"})!=null) {
			res.setCode(CodeEnum.ERR_PARAM_NULL.getCode());
			res.setMsg(CodeEnum.ERR_PARAM_NULL.getMessage());
			return res;
		}
		List<Dictionary> dicList =  dictionaryDao.selectByExample(query);
		logger.info("查询字典信息   查出的数据个数:"+dicList.size());
		if (dicList.size()==0) {
			res.setCode(CodeEnum.ERR_DATA_NO_RESULT.getCode());
			res.setMsg(CodeEnum.ERR_DATA_NO_RESULT.getMessage());
			return res;
		}
		res.setDicList(dicList);
		return res;
	}
}
