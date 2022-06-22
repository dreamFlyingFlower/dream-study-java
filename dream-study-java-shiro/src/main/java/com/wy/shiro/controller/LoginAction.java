package com.wy.shiro.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.result.Result;
import com.wy.shiro.constant.ShiroConstant;
import com.wy.shiro.entity.vo.LoginVo;
import com.wy.shiro.service.LoginService;
import com.wy.shiro.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/login")
@Slf4j
public class LoginAction {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginService loginService;

	/**
	 * 首页
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("/account/login");
	}

	/**
	 * 登录操作
	 * 
	 * @param loginVo 登录对象
	 * @return
	 */
	@RequestMapping(value = "/usersLongin", method = RequestMethod.POST)
	public ModelAndView usersLongin(LoginVo loginVo) {
		ModelAndView modelAndView = new ModelAndView("/account/login");
		String shiroLoginFailure = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			loginVo.setSystemCode(ShiroConstant.PLATFORM_MGT);
			loginService.route(loginVo);
		} catch (UnknownAccountException ex) {
			log.error("登陆异常:{}", ex);
			shiroLoginFailure = "登录失败 - 账号不存在！";
			map.put("loginName", loginVo.getLoginName());
			map.put("shiroLoginFailure", shiroLoginFailure);
			modelAndView.addAllObjects(map);
		} catch (IncorrectCredentialsException ex) {
			log.error("登陆异常:{}", ex);
			shiroLoginFailure = "登录失败 - 密码不正确！";
			map.put("shiroLoginFailure", shiroLoginFailure);
			map.put("loginName", loginVo.getLoginName());
			modelAndView.addAllObjects(map);
		} catch (ExcessiveAttemptsException ex) {
			log.error("登陆异常:{}", ex);
			shiroLoginFailure = ex.getMessage();
			map.put("shiroLoginFailure", shiroLoginFailure);
			map.put("loginName", loginVo.getLoginName());
			modelAndView.addAllObjects(map);
		} catch (Exception ex) {
			log.error("登陆异常:{}", ex);
			shiroLoginFailure = "登录失败 - 请联系平台管理员！";
			map.put("shiroLoginFailure", shiroLoginFailure);
			map.put("loginName", loginVo.getLoginName());
			modelAndView.addAllObjects(map);
		}
		if (shiroLoginFailure != null) {
			return modelAndView;
		}
		modelAndView.setViewName("redirect:/menus/system");
		return modelAndView;
	}

	/**
	 * jwt的json登录方式
	 * 
	 * @param loginVo
	 * @return
	 */
	@RequestMapping("login-jwt")
	@ResponseBody
	public Result<?> LoginForJwt(@RequestBody LoginVo loginVo) {
		return loginService.routeForJwt(loginVo);
	}

	/**
	 * 退出系统
	 */
	@RequestMapping(value = "/usersLongout")
	public String usersLongout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			subject.logout();
		}
		return "/account/login";
	}

	/**
	 * 编辑密码
	 */
	@RequestMapping(value = "/editorpassword")
	public ModelAndView editorPassword() {
		return new ModelAndView("/user/user-editor-password");
	}

	/**
	 * 保存新密码
	 */
	@RequestMapping(value = "/saveNewPassword")
	@ResponseBody
	public Boolean saveNewPassword(String oldPassword, String plainPassword)
	        throws InvocationTargetException, IllegalAccessException {
		return userService.saveNewPassword(oldPassword, plainPassword);
	}
}