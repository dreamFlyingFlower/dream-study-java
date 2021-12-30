package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-30 15:33:38
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ConfigurationProperties(prefix = "config.common")
@Configuration
@Getter
@Setter
public class CommonProperties {

	/** 项目地址,主要用来回调 */
	private String serverUrl;
}