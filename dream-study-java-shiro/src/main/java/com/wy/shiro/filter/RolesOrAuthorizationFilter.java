package com.wy.shiro.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

/**
 * 角色或 过滤器,Shiro默认的是和,见 {@link RolesAuthorizationFilter}
 * 
 * @author 飞花梦影
 * @date 2022-06-22 16:38:22
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class RolesOrAuthorizationFilter extends AuthorizationFilter {

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
	        throws IOException {

		Subject subject = getSubject(request, response);
		String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
			// no roles specified, so nothing to check - allow access.
			return true;
		}

		Set<String> roles = CollectionUtils.asSet(rolesArray);
		// 循环roles判断只要有角色则返回true
		for (String role : roles) {
			if (subject.hasRole(role)) {
				return true;
			}
		}
		return false;
	}
}