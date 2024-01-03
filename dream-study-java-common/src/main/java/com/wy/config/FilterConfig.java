package com.wy.config;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * http拦截器
 *
 * @author 飞花梦影
 * @date 2024-01-03 17:26:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class FilterConfig {

	/**
	 * http,https接口调用信息拦截
	 */
	@Bean
	RemoteIpFilter remoteIpFilter() {
		return new RemoteIpFilter();
	}
}