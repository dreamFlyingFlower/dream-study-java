package com.wy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 在application.yml中自定义配置文件
 * @attention 需要用到@ConfigurationProperties,其中prefix是在配置文件中的前缀,可自动提示
 *            使用该文件之后,配置文件将不会对该类中的属性报警
 * @attention 该文件只是一个配置文件,使用的时候可使用注解 ConditionalOnClass(UserConfig.class)
 *            和EnableConfigurationProperties(UserConfig.class)
 * @instruction Value注解和ConfigurationProperties的区别:value注解可直接找到yml文件中的key,
 *              而不需要如ConfigurationProperties一样事先在类中定义,但是value每次只能定义字段
 * @instruction Validated只能配置ConfigurationProperties使用,目的是校验类中的字段
 * @instruction PropertySource注解加载路径下的properties文件
 * @instruction ImportResource注解主要是导入spring的xml配置文件,可使用Bean注解代替
 * @author paradiseWy
 */
@Configuration
@ConfigurationProperties(prefix = "user.config")
public class UserConfig {
	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}