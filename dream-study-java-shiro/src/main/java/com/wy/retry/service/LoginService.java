package com.wy.retry.service;

import java.util.Map;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

import com.wy.retry.vo.LoginVo;

/**
 * @Description 登陆业务接口
 */

public interface LoginService {

	/**
	 * @Description 登陆路由
	 * @param
	 * @return
	 */
	public Map<String, String> route(LoginVo loginVo) throws UnknownAccountException, IncorrectCredentialsException;
}