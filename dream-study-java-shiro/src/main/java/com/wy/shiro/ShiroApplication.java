package com.wy.shiro;

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
 * Shiro完整代码
 * 
 * @author 飞花梦影
 * @date 2022-06-21 16:47:31
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SpringBootApplication(scanBasePackages = { "com.wy.shiro.interceptor", "com.wy.shiro.service", "com.wy.shiro.config",
		"com.wy.shiro.intialize" })
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class ShiroApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ShiroApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShiroApplication.class);
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