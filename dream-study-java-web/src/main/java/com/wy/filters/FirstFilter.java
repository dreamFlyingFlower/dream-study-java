package com.wy.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * servlet的拦截器,可直接使用{@link WebFilter},需要开启{@link ServletComponentScan}注解
 *
 * @author ParadiseWY
 * @date 2019-04-07 10:32:36
 * @git {@link https://github.com/mygodness100}
 */
@WebFilter(filterName = "testFilter", urlPatterns = "*")
public class FirstFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}