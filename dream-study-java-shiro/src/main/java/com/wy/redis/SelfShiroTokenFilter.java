package com.wy.redis;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.MediaType;

import com.wy.lang.StrTool;
import com.wy.result.Result;

/**
 * Shiro拦截器
 *
 * @author 飞花梦影
 * @date 2022-06-09 11:26:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SelfShiroTokenFilter extends AuthenticatingFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		// 获取请求token
		String token = getRequestToken((HttpServletRequest) request);
		return StrTool.isBlank(token) ? null : new SelfShiroToken(token);
	}

	/**
	 * 是否有权限调用接口,如果返回false,则调用{@link #onAccessDenied(ServletRequest, ServletResponse)}
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		String token = getRequestToken((HttpServletRequest) request);
		if (StrTool.isNotBlank(token)) {
			// 提交给realm进行登入,如果错误会抛出异常并被捕获
			getSubject(request, response).login(new SelfShiroToken(token));
			// 如果没有抛出异常则代表登入成功,返回true
			return true;
		}
		return false;
	}

	/**
	 * 登录检验失败
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpResponse.getWriter().print(Result.error("invalid token"));
		// return executeLogin(request, response);
		return false;
	}

	/**
	 * 登录失败
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
	        ServletResponse response) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		try {
			Throwable throwable = e.getCause() == null ? e : e.getCause();
			httpResponse.getWriter().print(Result.error(throwable.getMessage()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取请求的token
	 */
	private String getRequestToken(HttpServletRequest httpRequest) {
		// 从header中获取token,如果header中不存在token,则从参数中获取token
		String token = httpRequest.getHeader("Authorization");
		return StrTool.isNotBlank(token) ? token : httpRequest.getParameter("Authorization");
	}
}