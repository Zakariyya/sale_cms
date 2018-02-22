package org.slsale.service.datadictionary;

import java.util.List;

import org.slsale.pojo.DataDictionary;

public interface DataDictionaryService {

	
	/**
	 * 根据TypeCode查找对应的List
	 */
	public List<DataDictionary> getDataDictionary(DataDictionary dataDictionary)throws Exception;
	
}
