package com.wy.idempotent;

import javax.servlet.http.HttpServletRequest;

/**
 * Token业务接口
 *
 * @author 飞花梦影
 * @date 2021-11-09 16:20:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface TokenService {

	/**
	 * 创建token
	 * 
	 * @return token
	 */
	String createToken();

	/**
	 * 检验token
	 * 
	 * @param request 请求
	 * @param tokenKey 存储在redis中的token key
	 * @return true->校验成功;false->校验失败
	 */
	boolean checkToken(HttpServletRequest request, String tokenKey);

	/**
	 * 从请求头中获得token
	 * 
	 * @param request 请求
	 * @param tokenKey 存储在redis中的token key
	 * @return token值
	 */
	Object getToken(HttpServletRequest request, String tokenKey);
}