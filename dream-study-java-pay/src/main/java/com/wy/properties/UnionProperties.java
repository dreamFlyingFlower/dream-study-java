package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 银联配置参数
 *
 * @author 飞花梦影
 * @date 2021-12-30 15:36:29
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Configuration
@ConfigurationProperties(prefix = "config.union")
@Getter
@Setter
public class UnionProperties {

	/** 回调地址 */
	private String notifyUrl = "";
}