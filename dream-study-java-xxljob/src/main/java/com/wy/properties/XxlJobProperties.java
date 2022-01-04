package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * xxljob参数
 *
 * @author 飞花梦影
 * @date 2021-12-21 17:31:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@ConfigurationProperties(prefix = "xxl.job")
@Configuration
@Getter
@Setter
public class XxlJobProperties {

	/** http登录xxljob-admin的用户名和密码 */
	private String username = "admin";

	private String password = "123456";

	private String accessToken;

	private Integer logretentiondays = 30;

	private String adminAddresses;

	private String loginPath = "/login";

	private Executor executor = new Executor();

	@Getter
	@Setter
	public class Executor {

		private String appname;

		private String address;

		private String ip;

		private int port;

		private String logPath;

		private int logRetentionDays;
	}
}