package com.wy.shiro.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * redis配置文件
 */
@Data
@ConfigurationProperties(prefix = "shiro.framework.shiro.redis")
public class ShiroRedisProperties implements Serializable {

	private static final long serialVersionUID = 4445862462176859939L;

	/**
	 * redis连接地址
	 */
	private String nodes;

	/**
	 * 获取连接超时时间
	 */
	private int connectTimeout;

	/**
	 * 最小空闲连接数
	 */
	private int connectPoolSize;

	/**
	 * 最大连接数
	 */
	private int connectionMinimumidleSize;

	/**
	 * 等待数据返回超时时间
	 */
	private int timeout;

	/**
	 * 全局超时时间
	 */
	private long globalSessionTimeout;

}
