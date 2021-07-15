package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义配置类
 *
 * @author 飞花梦影
 * @date 2021-07-15 14:03:40
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class ConfigPropertes {

	private AlipayProperties alipay = new AlipayProperties();
}