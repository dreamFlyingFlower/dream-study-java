package com.wy.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * 在启动的时候做一些上下文环境操作,例如加载程序外的配置文件.但是该文件必须注册到META-INF/spring.factories中
 * 
 * org.springframework.boot.env.EnvironmentPostProcessor=com.wy.config.MyEnvironmentPostProcessor
 * 
 * @auther 飞花梦影
 * @date 2021-05-14 23:00:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {

	/**
	 * 可以通过{@link ApplicationContext#getEnvironment()}得到ConfigurableEnvironment,从而得到其中的配置
	 * 
	 * @param environment 上下文环境
	 * @param application 启动类
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		try (InputStream is = new FileInputStream("/app/test.properties")) {
			Properties properties = new Properties();
			properties.load(is);
			PropertiesPropertySource propertySource = new PropertiesPropertySource("test", properties);
			environment.getPropertySources().addLast(propertySource);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}