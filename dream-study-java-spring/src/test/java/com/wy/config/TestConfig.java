package com.wy.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

/**
 * 测试配置类:不带{@link Configuration}和{@link ConfigurationProperties}注解类属性的注入<br>
 * 直接使用{@link Value}注解注入
 * 
 * @author ParadiseWY
 * @date 2020-12-02 13:56:32
 * @git {@link https://github.com/mygodness100}
 */
@SpringBootTest
public class TestConfig {

	@Autowired
	private ConfigProperties config;

	/** 不需要在ConfigProperties中配置,可以直接获取 */
	@Value("${test}")
	private String test;

	/** 可以获取,必须和配置文件中相同,不能和配置类中相同,config.userId无法获取 */
	@Value("${config.user-id}")
	private String userId;

	/** 可以直接使用SpEL表达式赋值 */
	@Value("#{10*2}")
	private int age;

	@Value("${uuid}")
	private String uuid;

	@Test
	public void testConfig() {
		System.out.println(config.getUserId()); // 1
		System.out.println(config.getAge());
		System.out.println(test); // test 换行
		System.out.println(userId); // 1
		System.out.println(age);
		System.out.println(config.getPlaceholder());
		System.out.println(uuid);
	}
}