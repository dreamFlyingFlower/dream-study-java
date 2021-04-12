package com.wy.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.wy.utils.StrUtils;

/**
 * spring过滤器,对所有的请求都有效,拦截器(interceptor)是aop的实现,比filter更灵活
 * 
 * @author ParadiseWY
 * @date 2020-12-03 23:01:19
 * @git {@link https://github.com/mygodness100}
 */
public class AuthFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 请求校验,检验请求头中是否含有指定的校验信息
		String auths = request.getHeader("Authentication");
		if (StrUtils.isNotBlank(auths)) {
			// do something
		} else {
			authErrorPage(response, "验证信息为空");
		}
	}

	private void authErrorPage(HttpServletResponse resp, String message) {
		try {
			resp.setContentType("text/plain;charset=utf8");
			PrintWriter pw = resp.getWriter();
			pw.println(message);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}