package org.slsale.controller.user;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.slsale.common.Constants;
import org.slsale.common.JsonDateValueProcessor;
import org.slsale.common.PageSupport;
import org.slsale.common.SQLTools;
import org.slsale.pojo.DataDictionary;
import org.slsale.pojo.Role;
import org.slsale.pojo.User;
import org.slsale.service.datadictionary.DataDictionaryService;
import org.slsale.service.role.RoleService;
import org.slsale.service.user.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class UserController extends BaseController {
	
	public Logger logger = Logger.getLogger(UserController.class);
	
	@Resource
	private UserService userService;
	
	@Resource 
	private RoleService roleService;
	
	@Resource
	private DataDictionaryService dataDictionaryService;
	
	@RequestMapping("/backend/modifyPwd.html")
	@ResponseBody
	public Object modifyPwd(@RequestParam String userJson){
		logger.debug("modifypwd==================>");
		System.out.println("------------------------------------------------------modifypwd");
		//获取当前用户
		User currentUser = this.getCurrentUser();
		
		if(userJson == null || userJson == ""){
			return "nodata"; //无数据处理
		}else{
			JSONObject userObject = JSONObject.fromObject(userJson);//前台js传来的user对象的json格式
			User user = (User) JSONObject.toBean(userObject, User.class);//转成user对象
			user.setId(currentUser.getId());
			user.setLoginCode(currentUser.getLoginCode());
			try {
				User loginUser = userService.getLoginUser(user);
				if(loginUser != null){
					//将新密码set到现有的user中
					/**
					 * 小细节：
					 * password2：是支付密码，但在这里前台用来储存“新的登录密码”
					 * 所以，password2的资源是被“污染”了的。
					 * ====================================
					 * 注意：user.setPassword2(null);
					 * 设置为null之后，在UserMapper.xml的modifyUser方法中
					 *-----------------------------
					 * 有<if test="password2 != null">password2= #{password2},</if>
					 *-----------------------------
					 * 因此，不会对password2进行任何数据操作
					 */
					user.setPassword(user.getPassword2());
					user.setPassword2(null);//因为设定值为null，并password2并不会被重写
					userService.modifyUser(user);
				}else{
					return "oldpasswrong";
				}
			} catch (Exception e) {
				// TODO: handle exception
				return "failed";
			}
		}
		return "success";
	}
	
	/**
	 * 用户管理：
	 * 		显示用户列表，并进行分页
	 * @param model
	 * @param session
	 * @param s_loginCode
	 * @param s_referCode
	 * @param s_roleId
	 * @param s_isStart
	 * @return
	 */
	@RequestMapping("/backend/userlist.html")
	public ModelAndView userList(Model model,HttpSession session,
			@RequestParam(value="currentpage", required=false) Integer currentPage,//当前页
			@RequestParam(value="s_loginCode",required=false) String s_loginCode,
			@RequestParam(value="s_referCode",required=false) String s_referCode,
			@RequestParam(value="s_roleId",required=false) String s_roleId,
			@RequestParam(value="s_isStart",required=false) String s_isStart){
		
		Map<String,Object>baseModel = (Map<String, Object>) session.getAttribute(Constants.SESSION_BASE_MODEL);
		if(baseModel == null){
			return new ModelAndView("redirect:/");
		}else{
			//获取roleList and cardTypeList
			List<Role> roleList = null;
			DataDictionary dataDictionary = new DataDictionary();
			dataDictionary.setTypeCode("CARD_TYPE");
			List<DataDictionary> cardTypeList = null;
			
			try {
				roleList = roleService.getRoleList();
				cardTypeList = dataDictionaryService.getDataDictionary(dataDictionary);
				logger.debug("cardTypeList.size()===================>>>>>>>>>"+cardTypeList.size());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			//条件的模糊查询
			User user  = new User();
			if(null != s_loginCode){
				user.setLoginCode("%"+SQLTools.transfer(s_loginCode)+"%");
			}
			if(null != s_referCode){
				user.setReferCode("%"+SQLTools.transfer(s_referCode)+"%");
			}
//			if(!StringUtils.isNullOrEmpty(s_isStart)){//是否选择了"启用/未启用"   //很奇怪，老是报错
			if(s_isStart != null && s_isStart != "")      //所以改成这个
				user.setIsStart(Integer.valueOf(s_isStart));
			else
				user.setIsStart(null);
//			if(!StringUtils.isNullOrEmpty(s_roleId)){//是否"角色"  		//很奇怪，老是报错
			if(s_isStart != null && s_isStart != "")      //所以改成这个
				user.setIsStart(Integer.valueOf(s_roleId));
			else
				user.setRoleId(null);
			
			//page分页列表
			PageSupport pageSupport = new PageSupport();
			try {
				logger.debug("currentPage  >>>>>>>>>>>>>>>>>>>   "+currentPage);
				pageSupport.setTotalCount(userService.count(user));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(">>>>>>>>>>>>>>>页面异常");
				pageSupport.setTotalCount(0);
			}
			logger.debug("page.getTotalCount()====================== " + pageSupport.getTotalCount());
			if(pageSupport.getTotalCount() > 0){
				if(currentPage != null)//当前页不为空时
					pageSupport.setPage(currentPage);
				/**
				 * currentPage网页传来的当前页面值为    null 时，通常是第一次进入该页面
				 * 那么  pageSupport.getPage()  就当成"当前页" 来看待
				 */
				if(pageSupport.getPage() <= 0)//当前页小于，则设定跳转至首页
					pageSupport.setPage(1);
				if(pageSupport.getPage() > pageSupport.getPageCount())//当前页大于总页数，则设定跳转到尾页
					pageSupport.setPage(pageSupport.getPageCount());
				logger.debug("pageSupport.getPageCount() >>>>>>>>>>>>>>>> "+pageSupport.getPageCount());
				logger.debug("pageSupport.getTotalCount() >>>>>>>>>>>>>>>> "+pageSupport.getTotalCount());
				
				//MySQL--分页查询，limit ?, ?  (第一个参数下标从0开始)
				user.setStartNum((pageSupport.getPage()-1)*pageSupport.getPageSize());//下标
				user.setPageSize(pageSupport.getPageSize());//每个页面显示的条数
				
				logger.debug("user.getPageSize()====================== " + user.getPageSize());
				logger.debug("user.getStarNum()====================== " + user.getStartNum());
		
				List<User> userList = null;
				try {
					userList = userService.getUserList(user);
					logger.debug("userList====================== " + userList.toString());

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					userList = null;
					if(pageSupport == null){
						pageSupport = new PageSupport();
						pageSupport.setItems(null);//设定当前页面集合为空，相当于置空页面内容
					}
				}
				logger.debug("userList====================== " + userList.size());
				pageSupport.setItems(userList);
			}else{
				pageSupport.setItems(null);
			}
			
			model.addAllAttributes(baseModel);
			model.addAttribute("page",pageSupport);
			model.addAttribute("roleList",roleList);
			model.addAttribute("cardTypeList",cardTypeList);
			model.addAttribute("s_loginCode", s_loginCode);
			model.addAttribute("s_referCode", s_referCode);
			model.addAttribute("s_roleId", s_roleId);
			model.addAttribute("s_isStart", s_isStart);
			return new ModelAndView("/backend/userlist");
		}
	}/*userList END*/
	
	/**
	 *  通过选择"角色"来异步加载"会员类型"列表
	 *  
	 *  
	 *  ===============
	 *  提交过来的数据当中有"中文",为防止出现中文乱码：produces={"text/html;charset=UTF-8"}
	 * @param s_roleId
	 * @return
	 */
	@RequestMapping(value="/backend/loadUserTypeList.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object loadUserTypeList(@RequestParam(value="s_roleId",required=false) String s_roleId){
		String cjson = "";
		try {
			DataDictionary dataDictionary = new DataDictionary();
			dataDictionary.setTypeCode("USER_TYPE");
			//从数据库中获取DataDictionary的"UserType"的数据，并放入到list中
			List<DataDictionary>userTypeList =  dataDictionaryService.getDataDictionary(dataDictionary);
			//将userTypeList转换成json字符串
			JSONArray jo = JSONArray.fromObject(userTypeList);
			cjson = jo.toString();
			logger.debug("cjson==========>"+cjson);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return cjson;
	}/*loadUserTypeList*/
	
	
	@RequestMapping(value="/backend/logincodeisexist.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String loginCodeIsExist(@RequestParam(value="loginCode",required=false) String loginCode,
			@RequestParam(value="id",required=false) String id){
		String result = "failed";
		logger.debug("loginCode=============>>>>>>>"+loginCode);
		logger.debug("id=============>>>>>>>"+id);
		User _user = new User();
		_user.setLoginCode(loginCode);
		
		logger.debug("id=>"+id);
		if(!id.equals("-1")){//修改操作进行同名判断
			_user.setId(Integer.valueOf(id));
			logger.debug("_user.getId()=====>"+_user.getId());
		}
		
		try {
			if(userService.loginCodeIsExist(_user) == 0)
				result="only";
			else
				result="repeat";
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return result;
		}
		return result;
	}/*loginCodeIsExist END*/
	
	
	@RequestMapping(value="/backend/adduser.html", method = RequestMethod.POST)
	public ModelAndView addUser(HttpSession session,@ModelAttribute("addUser") User addUser){
		
		if(session.getAttribute(Constants.SESSION_BASE_MODEL) == null)
			return new ModelAndView("redirect:/");
		else{
			try {
				/**
				 * 设置密码
				 */
				//密码,支付密码   :   默认是为"身份证后6位", 因此进行String截取
				String idCard = addUser.getIdCard();
				String ps = idCard.substring(idCard.length()-6);
				addUser.setPassword(ps);
				addUser.setPassword2(ps);
				
				
				addUser.setCreateTime(new Date());
				addUser.setReferId(this.getCurrentUser().getId());
				addUser.setReferCode(this.getCurrentUser().getLoginCode());
				addUser.setLastLoginTime(new Date());
				
				userService.addUser(addUser);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/backend/userlist.html");
		}
		
	}
	
	/**
	 * 表单    文件上传(不是提交表达，只是上传表单)
	 * @param cardFile
	 * @return
	 * ============
	 * HttpServletRequest  httpRequest 在获取url,path时候用到
	 * 
	 */
	@RequestMapping(value="/backend/upload.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object upload(@RequestParam(value="a_fileInputID",required=false) MultipartFile cardFile,
			@RequestParam(value="a_fileInputBank",required=false) MultipartFile bankFile,
			@RequestParam(value="m_fileInputID",required=false) MultipartFile mCardFile,
			@RequestParam(value="m_fileInputBank",required=false) MultipartFile mBankFile,
			@RequestParam(value="loginCode",required=false) String loginCode,
			HttpSession seesion,HttpServletRequest httpRequest ){
			
		logger.debug("upload===============");
		/**
		 *	根据服务器的操作系统，自动获取物理路径，自适应
		 * 	File.separator:  根据操作系统路径自适应(处理斜杠、反斜杠)
		 */
		String path = httpRequest.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		logger.debug("path============="+path);
		
		DataDictionary dataDictionary = new DataDictionary();
		dataDictionary.setTypeCode("PERSONALFILE_SIZE");
		List<DataDictionary> dataDictionaryList = null;
		try {
			dataDictionaryList = dataDictionaryService.getDataDictionary(dataDictionary);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		int filesize = 5000;
		if(dataDictionaryList != null){
			if(dataDictionaryList.size() ==1){
				/**
				 * DataDictionary中"PERSONALFILE_SIZE"
				 * 记录了上传个人资料附件大小，取出数据库中PERSONALFILE_SIZE的value值
				 * 并设定
				 */
				logger.debug("dataDictionaryList============>"+dataDictionaryList);
				filesize = Integer.valueOf(dataDictionaryList.get(0).getValueName());
			}
		}
		if(cardFile != null){
			/**
			 * 不能让原文件名进行上传，用户传来的文件名有可能是中文的
			 */
			//获取原文件名
			String oldFileName = cardFile.getOriginalFilename();
			//取到文件的后缀
			String prefix = FilenameUtils.getExtension(oldFileName);
			logger.debug("cardFile prefix ===================>  "+prefix);
		
			if(cardFile.getSize() > filesize){
				return "1";
			}else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
					|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//通过查看后缀名，判断文件格式是否正确
				//给文件进行重命名:系统毫秒数+100W以内的随机数
				String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_IDcard.jpg";
				logger.debug("new fileName==============>  "+ fileName);
				
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//保存，上传
				try {
					cardFile.transferTo(targetFile);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				String url =httpRequest.getContextPath()+"/statics/uploadfiles/"+fileName; 
				return url;
			}/*else if   END*/
			else{
				return "2";
			}
		}/*if(cardFile != null)  END */
		
		if(mCardFile != null){
			String oldFileName = mCardFile.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(oldFileName);
			if(mCardFile.getSize() > filesize){
				return '1';
			}else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
					|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//通过查看后缀名，判断文件格式是否正确
				//给文件进行重命名:系统毫秒数+100W以内的随机数
				String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"IDcard.jpg";
				logger.debug("fileName=========> "+fileName.toString());
				File targetFile = new File(fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					mCardFile.transferTo(targetFile);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				String url =httpRequest.getContextPath()+"/statics/uploadfiles/"+fileName; 
				return url;
			}else{
				return "2";
			}
		}
		
		if(bankFile != null){
			/**
			 * 不能让原文件名进行上传，用户传来的文件名有可能是中文的
			 */
			//获取原文件名
			String oldFileName = bankFile.getOriginalFilename();
			//取到文件的后缀
			String prefix = FilenameUtils.getExtension(oldFileName);
			logger.debug("bankFile prefix ===================>  "+prefix);
			
			if(bankFile.getSize() > filesize){
				return "1";
			}else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png") 
					|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//通过查看后缀名，判断文件格式是否正确
				//给文件进行重命名:系统毫秒数+100W以内的随机数
				String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_bank.jpg";
				logger.debug("new bankFileName==============>  "+ fileName);
				
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				//保存，上传
				try {
					bankFile.transferTo(targetFile);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				String url =httpRequest.getContextPath()+"/statics/uploadfiles/"+fileName; 
				return url;
			}/*else if   END*/
			else{
				return "2";
			}
		}/*if(bankFile != null)  END */
		
		
		return null;
	}
	
	/**
	 * 删除图片
	 * @param picPath
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/backend/delpic.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String delPic(@RequestParam(value="picPath",required=false) String picPath,
			@RequestParam(value="id",required=false) String id,
			HttpSession session){
		
		String result = "failed";
		if(picPath == null || picPath.equals("")){
			result ="success";
		}else{
			/**
			 * picPath解析成物理路径
			 */
			String[] paths = picPath.split("/");//进行截取分段
			//真实路径  & 进行File服务器兼容匹配.最后解析出来的文件名
			String path = session.getServletContext().getRealPath(paths[1]+File.separator+paths[2]+File.separator+paths[3]);
			File file = new File(path);
			
			if(file.exists()){
				if(file.delete()){
					if(id.equals("0"))
						result = "success";
					/**
					 * 修改用户时，删除上传图片
					 */
					else{//修改用户时，删除上传图片
						User _user = new User();
						_user.setId(Integer.valueOf(id));
						
						if(picPath.indexOf("_IDCard.jpg") != -1){//验证后缀名,详细查看本类upload()中filename变量
							_user.setIdCardPicPath(picPath);
							logger.debug("_user.getIdCardPicPath() ============"+_user.getIdCardPicPath());
						}else if(picPath.indexOf("_bank.jpg") != -1){
							_user.setBankPicPath(picPath);
							logger.debug("_user.getBankPicPath() ============"+_user.getBankPicPath());
						}
						try {
							if(userService.delUserPic(_user) > 0){
								result = "success";
								logger.debug("modify---delPic ============");
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							return result;
						}
					}
				}
			}else{//文件不存在.(很可能是服务器文件丢失，数据库有数据，服务器没有文件),这时候将数据库idCardPicPath字段清空
				
				
				User _user = new User();
				_user.setId(Integer.valueOf(id));
				logger.debug("idpicPath.indexOf===============>"+picPath.indexOf("_IDCard.jpg"));
				logger.debug("bankpicPath.indexOf===============>"+picPath.indexOf("_bank.jpg"));

//				if(picPath.indexOf("_IDCard.jpg") != -1){//验证后缀名,详细查看本类upload()中filename变量
					_user.setIdCardPicPath("");
//				}else if(picPath.indexOf("_bank.jpg") != -1){//从数据库中取出(数据库有数据不为空既 != -1)，可是服务器却没有文件
					_user.setBankPicPath("");
//				}
				logger.debug("_user.getIdCardPicPath() ============"+_user.getIdCardPicPath());
				logger.debug("_user.getBankPicPath() ============"+_user.getBankPicPath());
				try {
					if(userService.delUserPic(_user) > 0){
						result = "success";
						logger.debug("modify---delPic ============");
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return result;
				}
				
				
			}
			
		}
		
		return result;
	}
	
	@RequestMapping(value="/backend/getuser.html",produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public Object getUser(@RequestParam(value="id",required=false) String m_id){
		
		String cjson= "";
		
		if(m_id == "" || m_id == null){
			return "nodata";
		}else{
			try {
				User user = new User();
				user.setId(Integer.valueOf(m_id));
				user = userService.getUserById(user);
				
				/**
				 * springMVC中，需要将"时间格式"的属性转换成json对象
				 */
				JsonConfig jsonConfig = new JsonConfig();
				/**
				 * user内所有日期属性都会按照此日期格式进行json转换, (对象转json)
				 * ==================================================
				 * 注册json值的处理器
 				 * 
 				 * jsonConfig.registerJsonValueProcessor(java.lang.Class propertyType,JsonValueProcessor jsonValueProcessor)
                    
                    - propertyType : 要用作关键字的属性类型, 在这里需要转换的是Date属性(the property type to use as key)
                    - jsonValueProcessor :  the processor to register(因为已经有自定义的DateValueProcessor, 所以用自定义的)
                    
				 */
				jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
				JSONObject jo = JSONObject.fromObject(user, jsonConfig);
				cjson = jo.toString();
				logger.debug("cjson========>"+cjson.toString());
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "failed";
			}
			
			return cjson;
		}
	}
	
	
	
	@RequestMapping(value="/backend/modifyuser.html",method=RequestMethod.POST)
	public ModelAndView modifyUser(HttpSession httpSession, @ModelAttribute("modifyUser") User modifyUser){
		
		if(httpSession.getAttribute(Constants.SESSION_BASE_MODEL) == null){
			return new ModelAndView("redirect:/");
		}else{
			try {
				modifyUser.setLastUpdateTime(new Date());
				userService.modifyUser(modifyUser);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return new ModelAndView("redirect:/backend/userlist.html");
		}
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}















