package com.wy.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 限制用户登录重试次数:15的代码,和16比,11,13,14已经删除
 * 
 * @author 飞花梦影
 * @date 2022-06-21 16:47:31
 */
@SpringBootApplication(scanBasePackages = { "com.wy.retry.interceptor", "com.wy.retry.service", "com.wy.retry.config",
        "com.wy.retry.intialize" })
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class SpringbootShiroConfigStart extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootShiroConfigStart.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootShiroConfigStart.class);
	}

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {

			@Override
			public void customize(ConfigurableServletWebServerFactory factory) {
				factory.setPort(80);
				factory.setContextPath("/shiro");
			}
		};
	}
}