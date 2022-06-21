package com.wy.shiro.service;

import java.util.Map;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

import com.wy.result.Result;
import com.wy.shiro.entity.vo.LoginVo;

/**
 * 登录业务接口
 *
 * @author 飞花梦影
 * @date 2022-06-21 23:08:20
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface LoginService {

	/**
	 * 登陆路由
	 * 
	 * @param
	 * @return
	 */
	Map<String, String> route(LoginVo loginVo) throws UnknownAccountException, IncorrectCredentialsException;

	/**
	 * jwt方式登录
	 * 
	 * @param loginVo 登录参数
	 * @return
	 */
	Result<?> routeForJwt(LoginVo loginVo) throws UnknownAccountException, IncorrectCredentialsException;
}