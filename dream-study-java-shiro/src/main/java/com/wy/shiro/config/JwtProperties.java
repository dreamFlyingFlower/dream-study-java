package com.wy.shiro.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * jwt配置文件
 */
@Data
@ConfigurationProperties(prefix = "shiro.framework.jwt")
public class JwtProperties implements Serializable {

	private static final long serialVersionUID = 3010756479445048197L;

	/**
	 * 签名密码
	 */
	private String base64EncodedSecretKey;
}