package org.slsale.service.datadictionary;

import java.util.List;

import javax.annotation.Resource;

import org.slsale.dao.datadictionary.DataDictionaryMapper;
import org.slsale.pojo.DataDictionary;
import org.springframework.stereotype.Service;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

	@Resource
	private DataDictionaryMapper dataDictionaryMapper;
	
	@Override
	public List<DataDictionary> getDataDictionary(DataDictionary dataDictionary) throws Exception {
		// TODO Auto-generated method stub
		return dataDictionaryMapper.getDataDictionary(dataDictionary);
	}


}
