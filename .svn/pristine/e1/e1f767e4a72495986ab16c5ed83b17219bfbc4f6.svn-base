package org.slsale.controller.user;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.slsale.common.Constants;
import org.slsale.pojo.User;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author anan
 * 只是一个基类，并不需要让spring容器知道
 * =============================
 * 比方说，每个controller都经常需要得到User，
 * 所以先把"当前用户"这个属性先给抽取出来，
 * 以后在 开发里，直接调用就可以了，就能取到当前用户的信息，
 * 就不需要在每个方法里面得它了
 * =============================
 */
public class BaseController {
	
	private Logger logger = Logger.getLogger(BaseController.class);
	
	private User currentUser;

	public User getCurrentUser() {
		
		if(null == this.currentUser){
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			/**
			 * httpServletRequest.getSession(false);
			 * 中的参数，默认为true，意思为，如果找不到，就创建一个
			 * 但这里要的是获取有没有，所以，有就返回有，没有就返回null，并不需要创建
			 * 所以用 false
			 */
			HttpSession httpSession = httpServletRequest.getSession(false);
			if(null != httpSession){
				currentUser = (User) httpSession.getAttribute(Constants.SESSION_USER);
			}else{
				currentUser = null;
			}
		}
		
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	
	/**
	 * 日期国际化
	 */
	@InitBinder
	public void InitBinder(WebDataBinder dateBinder){
		//初始化绑定
		dateBinder.registerCustomEditor(Date.class, new PropertyEditorSupport(){
			public void setAsText(String value){
				try {
					setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setValue(null);
				}
			}
			public String getAsText(){
				return new SimpleDateFormat("yyyy-MM-dd").format((Date)getValue());
			}
			
		});
	}
	
	
	
	
}
