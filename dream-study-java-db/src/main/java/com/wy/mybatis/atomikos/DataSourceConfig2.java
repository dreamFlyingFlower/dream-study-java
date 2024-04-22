package com.wy.mybatis.atomikos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 数据库配置2
 *
 * @author 飞花梦影
 * @date 2024-04-22 17:49:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource.test2")
public class DataSourceConfig2 {

	private String jdbcurl;

	private String username;

	private String password;

	private int minPoolSize;

	private int maxPoolSize;

	private int maxLifetime;

	private int borrowConnectionTimeout;

	private int loginTimeout;

	private int maintenanceInterval;

	private int maxIdleTime;

	private String testQuery;
}