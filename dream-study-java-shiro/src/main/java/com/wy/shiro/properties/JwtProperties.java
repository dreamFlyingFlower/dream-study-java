package com.wy.shiro.properties;

import java.io.Serializable;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * JWT配置
 * 
 * @author 飞花梦影
 * @date 2022-06-22 11:31:17
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@ConfigurationProperties(prefix = "shiro.framework.jwt")
@Configuration
@ConditionalOnMissingClass
public class JwtProperties implements Serializable {

	private static final long serialVersionUID = 3010756479445048197L;

	/**
	 * 密码
	 */
	private String base64EncodedSecretKey;
}