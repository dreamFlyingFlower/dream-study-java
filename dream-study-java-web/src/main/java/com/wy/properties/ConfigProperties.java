package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置文件总类
 * 
 * @author ParadiseWY
 * @date 2020-12-07 13:42:22
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

	private CommonProperties common = new CommonProperties();
}