package com.wy.shiro.core.impl;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.wy.lang.StrTool;

import io.jsonwebtoken.Claims;

/**
 * 自定义会话管理器
 * 
 * @author 飞花梦影
 * @date 2022-06-21 23:07:01
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class ShiroSessionManager extends DefaultWebSessionManager {

	// 从请求中获得sessionId的key
	private static final String AUTHORIZATION = "jwtToken";

	// 自定义注入的资源类型名称
	private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

	@Autowired
	JwtTokenManager jwtTokenManager;

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		// 判断request请求中是否带有jwtToken的key
		String jwtToken = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
		// 如果没有走默认的cookie获得sessionId的方式
		if (StrTool.isBlank(jwtToken)) {
			return super.getSessionId(request, response);
			// 如果有走jwtToken获得sessionI的的方式

		} else {
			Claims claims = jwtTokenManager.decodeToken(jwtToken);
			String id = (String) claims.get("jti");
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
			return id;
		}
	}
}