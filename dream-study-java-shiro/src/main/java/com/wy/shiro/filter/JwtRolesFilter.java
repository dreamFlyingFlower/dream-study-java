package com.wy.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.wy.lang.StrTool;
import com.wy.result.Result;
import com.wy.shiro.constant.ShiroConstant;

/**
 * 自定义jwt角色校验
 *
 * @author 飞花梦影
 * @date 2022-06-21 23:20:01
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class JwtRolesFilter extends RolesAuthorizationFilter {

	/**
	 * 访问拒绝时调用
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		// 判断当前请求头中是否带有jwtToken的字符串
		String jwtToken = WebUtils.toHttp(request).getHeader("jwtToken");
		// 如果有：返回json的应答
		if (StrTool.isNotBlank(jwtToken)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			response.getWriter().write(
					JSONObject.toJSONString(Result.error(ShiroConstant.NO_ROLE_CODE, ShiroConstant.NO_ROLE_MESSAGE)));
			return false;
		}
		// 如果没有,则走原始方式
		return super.onAccessDenied(request, response);
	}
}