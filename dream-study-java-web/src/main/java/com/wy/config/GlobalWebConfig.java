package com.wy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.filters.SecondFilter;
import com.wy.interceptor.FirstInterceptor;
import com.wy.interceptor.RateLimitInterceptor;
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

	@Autowired
	private FirstInterceptor firstInterceptor;

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
		ServletListenerRegistrationBean<SecondListener> bean =
		        new ServletListenerRegistrationBean<SecondListener>(new SecondListener());
		return bean;
	}

	/**
	 * 添加拦截器,可以添加多个
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RateLimitInterceptor());
		// 直接将对象注入或spring注入
		// registry.addInterceptor(new FirstInterceptor());
		registry.addInterceptor(firstInterceptor)
		        // 拦截的请求,只对单个拦截器有效,每个拦截都需要配置,不配置默认拦截所有请求
		        .addPathPatterns("/**")
		        // 不拦截的请求
		        .excludePathPatterns("/bigscreen/template1/js/jquery.easyui.min.js", "/static/**",
		                "/bigscreen/template1/js/*.js", "/bigscreen/tempate1/**", "/bigscreen/**",
		                "/bigscreen/template1/js/**");
		// 若interceptor没有加入到springboot上下文中,可以直接new一个,效果同上
		// registry.addInterceptor(new FirstInterceptor());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 不拦截的资源请求同时要添加到资源映射中
		registry.addResourceHandler("/bigscreen/**").addResourceLocations("classpath:/static/bigscreen/");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}