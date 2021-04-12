package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义配置总配置
 * 
 * @author ParadiseWY
 * @date 2020-8-14 15:31:51
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

	private DataSource1Properties dataSource1 = new DataSource1Properties();
}