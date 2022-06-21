package com.wy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.wy.lang.StrTool;
import com.wy.result.Result;
import com.wy.shiro.constant.ShiroConstant;
import com.wy.shiro.core.impl.JwtTokenManager;

/**
 * 自定义登录验证过滤器
 *
 * @author 飞花梦影
 * @date 2022-06-21 23:19:19
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class JwtAuthcFilter extends FormAuthenticationFilter {

	private JwtTokenManager jwtTokenManager;

	public JwtAuthcFilter(JwtTokenManager jwtTokenManager) {
		this.jwtTokenManager = jwtTokenManager;
	}

	/**
	 * @Description 是否允许访问
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 判断当前请求头中是否带有jwtToken的字符串
		String jwtToken = WebUtils.toHttp(request).getHeader("jwtToken");
		// 如果有,走jwt校验
		if (StrTool.isNotBlank(jwtToken)) {
			boolean verifyToken = jwtTokenManager.isVerifyToken(jwtToken);
			if (verifyToken) {
				return super.isAccessAllowed(request, response, mappedValue);
			} else {
				return false;
			}
		}
		// 没有没有：走原始校验
		return super.isAccessAllowed(request, response, mappedValue);
	}

	/**
	 * @Description 访问拒绝时调用
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 判断当前请求头中是否带有jwtToken的字符串
		String jwtToken = WebUtils.toHttp(request).getHeader("jwtToken");
		// 如果有,返回json的应答
		if (StrTool.isNotBlank(jwtToken)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(
					JSONObject.toJSONString(Result.error(ShiroConstant.NO_LOGIN_CODE, ShiroConstant.NO_LOGIN_MESSAGE)));
			return false;
		}
		// 如果没有,走原始方式
		return super.onAccessDenied(request, response);
	}
}