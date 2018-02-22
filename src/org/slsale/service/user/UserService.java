package org.slsale.service.user;

import java.util.List;

import org.slsale.pojo.User;
import org.springframework.stereotype.Service;

public interface UserService {
	/**
	 * 获得登录用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User getLoginUser(User user) throws Exception;
	
	/**
	 * 登录用户是否存在
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int loginCodeIsExist(User user)throws Exception;

	/**
	 * addUser
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int addUser(User user)throws Exception;
	
	/**
	 * modifyUser
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int modifyUser(User user)throws Exception;

	/**
	 * 获取用户总条数
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int count(User user)throws Exception;

	
	/**
	 * 获取用户列表
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserList(User user)throws Exception;
	
	/**
	 * 删除用户图片
	 * ===============
	 * 删除图片并不需要删掉某个字段，只是将这个字段置空字符串即可
	 * 所以sql中应该用 "update"
	 * ===================
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int delUserPic(User user)throws Exception;
	
	/**
	 * getUser by id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User getUserById(User user) throws Exception;


}
