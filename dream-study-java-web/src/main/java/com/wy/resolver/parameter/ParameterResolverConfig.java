package com.wy.resolver.parameter;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注解参数解析器
 *
 * @author 飞花梦影
 * @date 2024-11-23 11:07:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class ParameterResolverConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(parameterResolver());
	}

	@Bean
	ParameterResolver parameterResolver() {
		return new ParameterResolver();
	}
}