package com.wy.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 配置druid的管理页面
 * 
 * @author ParadiseWY
 * @date 2020-12-02 16:32:04
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class DruidConfig {

	/**
	 * 配置,初始化属性{@link com.alibaba.druid.support.http.ResourceServlet}
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> stateViewServlet() {
		ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<StatViewServlet>(
				new StatViewServlet(), "/druid/*");
		// 配置初始化属性
		Map<String, String> params = new HashMap<>();
		params.put("loginUsername", "root");
		params.put("loginPassword", "123456");
		// 允许访问的请求,空表示无限制
		params.put("allow", "");
		registrationBean.setInitParameters(params);
		return registrationBean;
	}

	/**
	 * 注册一个web监控的filter
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> stateViewFilter() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new WebStatFilter());
		return filterRegistrationBean;
	}
}