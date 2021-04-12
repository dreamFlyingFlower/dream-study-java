package com.wy.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.filters.SecondFilter;
import com.wy.intercepter.FirstInterceptor;
import com.wy.servlet.listener.SecondListener;

/**
 * Filter,Intercpetor,Listener等初始化,最好实现WebMvcConfigurer而不继承{@link WebMvcConfigurationSupport},有可能会改变json序列化方式
 *
 * @author 飞花梦影
 * @date 2021-03-26 15:36:50
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class GlobalWebConfig implements WebMvcConfigurer {

	/**
	 * 添加过滤器.若有多个过滤器,可以写多个类似方法
	 * 
	 * @return 过滤器注册对象
	 */
	@Bean
	public FilterRegistrationBean<?> secondFilterRegistration() {
		FilterRegistrationBean<SecondFilter> bean = new FilterRegistrationBean<>(new SecondFilter());
		bean.addUrlPatterns("/second");
		return bean;
	}

	/**
	 * 添加监听器,可以添加多个
	 * 
	 * @return 监听器对象
	 */
	@Bean
	public ServletListenerRegistrationBean<SecondListener> getServletListenerRegistrationBean() {
		ServletListenerRegistrationBean<SecondListener> bean = new ServletListenerRegistrationBean<SecondListener>(
				new SecondListener());
		return bean;
	}

	/**
	 * 添加拦截器,可以添加多个
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new FirstInterceptor());
	}
}