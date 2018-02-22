package org.slsale.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.taglibs.standard.functions.Functions;
import org.slsale.common.Constants;
import org.slsale.common.RedisAPI;
import org.slsale.dao.function.FunctionMapper;
import org.slsale.pojo.Authority;
import org.slsale.pojo.Function;
import org.slsale.pojo.Menu;
import org.slsale.pojo.User;
import org.slsale.service.function.FunctionService;
import org.slsale.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController extends BaseController{
	
	private Logger logger = Logger.getLogger(LoginController.class);
	
	@Resource
	private UserService userService;
	
	@Resource
	private FunctionService functionService;
	
	@Resource 
	private RedisAPI redisAPI;

	/***********************    main start        *********************************************/
	/**
	 * main.html
	 * @param session
	 * @return
	 */
	@RequestMapping("/main.html")
	public ModelAndView main(HttpSession session){
		User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
		logger.debug("main==============");
		
		//menu list , 先得到当前用户，然后根据用户的权限来判断菜单
		User user = this.getCurrentUser(); //因为继承了BaseController
		List<Menu> mList = null;
		/**
		 * 判断当前session是否为空，如果为空就GG了
		 * 如果不为空就继续
		 */
		if(null != user){
			Map<String,Object> model = new HashMap<String, Object>();
			model.put("user", user);
			
			/**
			 * key:menuList+roleID ====eg:menuList2
			 * value:mList
			 */
			if(!redisAPI.exists("menuList"+user.getRoleId())){ //redis没数据
				//根据当前用户获取菜单列表mList
				mList = getFuncByCurrentUser(user.getRoleId());
				//json
				if(null != mList){
					JSONArray jsonArray = JSONArray.fromObject(mList);
					String jsonString =  jsonArray.toString();
					logger.debug("jsonArray===========>"+jsonString);
					model.put("mList", jsonString);
					
					//将用户数据丢到reids存起来
					redisAPI.set("menuList"+user.getRoleId(), jsonString);
				}
			}else{//redis里有数据，直接从redis取
				String redisMenuListkeyString = redisAPI.get("menuList"+user.getRoleId());
				logger.debug("menuList from reids ===========>"+redisMenuListkeyString);
				
				if(null != redisMenuListkeyString && !"".equals(redisMenuListkeyString)){//如果redis的数据不是空的或是""
					model.put("mList", redisMenuListkeyString);
				}else{//取不出来，说明里面的数据是有问题的，重来一遍吧少年
					return new ModelAndView("redirect:/");
				}
			}
			
			session.setAttribute(Constants.SESSION_BASE_MODEL, model);
			return new ModelAndView("main",model);
			
		}
		
		//当前用户不存在任何session，直接定向到: /
		return new ModelAndView("redirect:/");
	}
	/*****************************     main end          ***************************************/
	
	/*****************************     menu start          ***************************************/
	/**
	 * 根据当前用户角色Id来获取功能列表
	 * @param roldId
	 * @return
	 */
	protected List<Menu> getFuncByCurrentUser(int roleId){
		
		List<Menu> menuList = new ArrayList<Menu>();
		Authority authority = new Authority();
		authority.setRoleId(roleId);
		
		try {
			//主菜单
			 List<Function> mList = functionService.getMainFunctionList(authority);
			 
			 if(mList != null){
				 /**
				  * 循环取出子菜单，并加入到菜单列表中
				  * 数据结构逻辑分析：
				  * menuList 里面放的是一个个menu对象，
				  * menu对象里面由一个主菜单和子菜单构成，（子菜单也是一个List）
				  */
				 for(Function function:mList){
					 Menu menu = new Menu();
					 menu.setMainMenu(function);
					 function.setRoleId(roleId);
					 List<Function> subList = functionService.getSubFunctionList(function);
					 if(subList != null){
						 menu.setSubMenu(subList);
					 }
					 menuList.add(menu);
				 }
				 
			 }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return menuList;
	}
	/*****************************     menu end          ***************************************/
	
	
	/*****************************     login start          ***************************************/
	/**
	 * 一般ajax（异步数据）提交时（异步返回），一般使用"ResponseBody",
	 * Controller方法需要往页面返回一个对象
	 */
	@RequestMapping("/login.html")
	@ResponseBody
	public Object login(HttpSession session,@RequestParam String user){
		if( user == null || user == ""){
			return "nodata";
		}else{
			
			JSONObject userObject = JSONObject.fromObject(user);
			User userObj = (User)userObject.toBean(userObject, User.class);
			//不存在登录账号
			try {
				if(userService.loginCodeIsExist(userObj) == 0){
					return "userObj";
				}else{
					User _user = userService.getLoginUser(userObj);
					//判断登录
					if(null != user){
						//登录成功,存到session中
						session.setAttribute(Constants.SESSION_USER, _user);
						//更新当前用户登录的LastLoginTime
						User updateLoginTimeUser = new User();
						updateLoginTimeUser.setId(_user.getId());
						updateLoginTimeUser.setLastLoginTime(new Date());  //获取当前时间作为最后的登录时间
						userService.modifyUser(updateLoginTimeUser);
						updateLoginTimeUser = null;
						return "success";
					}else{
						return "pwderror";
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "failed";
			}
		}
		
	}
	/*****************************     login end          ***************************************/
	
	/*****************************     logout start          ***************************************/
	/**
	 * 注销
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout.html")
	public String logout(HttpSession session){
		//适用于清空指定的属性   
		session.removeAttribute(Constants.SESSION_USER);
		//清除当前session的所有相关信息
		session.invalidate();
		this.setCurrentUser(null);//BaseController中"CurrentUser"也需要置空
		return "index";
	}
	/*****************************     logout start          ***************************************/
	
	
	
	
}

























