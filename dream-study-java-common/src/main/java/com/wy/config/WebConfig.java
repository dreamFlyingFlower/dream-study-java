package com.wy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wy.redis.idempotent.IdempotentInterceptor;

/**
 * Web拦截器,初始化等配置
 *
 * @author 飞花梦影
 * @date 2021-11-09 15:46:42
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private IdempotentInterceptor idempotentInterceptor;

	/**
	 * 添加拦截器
	 * 
	 * @param registry 注册拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(idempotentInterceptor);
	}
}