package com.wy.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.wy.lang.StrTool;
import com.wy.result.Result;
import com.wy.shiro.constant.ShiroConstant;

/**
 * 自定义jwt的资源校验
 *
 * @author 飞花梦影
 * @date 2022-06-21 23:19:33
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class JwtPermsFilter extends PermissionsAuthorizationFilter {

	/**
	 * 访问拒绝时调用
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		// 判断当前请求头中是否带有jwtToken的字符串
		String jwtToken = WebUtils.toHttp(request).getHeader("jwtToken");
		// 如果有,返回json的应答
		if (StrTool.isNotBlank(jwtToken)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(
					JSONObject.toJSONString(Result.error(ShiroConstant.NO_AUTH_CODE, ShiroConstant.NO_AUTH_MESSAGE)));
			return false;
		}
		// 如果没有,走原始方式
		return super.onAccessDenied(request, response);
	}
}