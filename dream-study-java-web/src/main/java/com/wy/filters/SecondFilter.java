package com.wy.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wy.config.GlobalWebConfig;

/**
 * servlet的拦截器,该方式需要当前类被注入到{@link FilterRegistrationBean}中,不需要添加{@link Configuration}
 *
 * @author ParadiseWY
 * @date 2019-04-07 10:32:36
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class SecondFilter implements Filter {

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

	/**
	 * 该方法可以写在{@link GlobalWebConfig}中,也可以写这里
	 * 
	 * @return 过滤器对象
	 */
	@Bean
	FilterRegistrationBean<SecondFilter> secondFilter() {
		FilterRegistrationBean<SecondFilter> bean = new FilterRegistrationBean<>(new SecondFilter());
		bean.addUrlPatterns("/secondFilter");
		return bean;
	}
}