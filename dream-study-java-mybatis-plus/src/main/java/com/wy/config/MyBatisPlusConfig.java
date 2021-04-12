package com.wy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MyBatis-plus配置
 * 
 * @author 飞花梦影
 * @date 2021-04-08 16:16:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyBatisPlusConfig {

	/**
	 * MyBatis-plus分页插件
	 * 
	 * @return 分页拦截器
	 */
	@Bean
	public PaginationInnerInterceptor paginationInnerInterceptor() {
		return new PaginationInnerInterceptor();
	}
}