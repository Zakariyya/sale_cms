package org.slsale.service.role;

import java.util.List;

import org.slsale.pojo.Role;

public interface RoleService {

	/**
	 * 获取角色列表
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleList() throws Exception;
	
}
