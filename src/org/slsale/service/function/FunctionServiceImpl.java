package org.slsale.service.function;

import java.util.List;

import javax.annotation.Resource;

import org.slsale.dao.function.FunctionMapper;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;
import org.springframework.stereotype.Service;
@Service
public class FunctionServiceImpl implements FunctionService {

	@Resource
	private FunctionMapper functionMapper;
	
	/**
	 * 返回主菜单列表
	 */
	@Override
	public List<Function> getMainFunctionList(Authority authority)
			throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getMainFunctionList(authority);
	}

	/**
	 * 主菜单对应的子菜单列表
	 */
	@Override
	public List<Function> getSubFunctionList(Function function)
			throws Exception {
		// TODO Auto-generated method stub
		return functionMapper.getSubFunctionList(function);
	}

}
