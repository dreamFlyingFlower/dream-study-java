package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义配置类
 * 
 * @author 飞花梦影
 * @date 2021-01-07 12:07:04
 * @git {@link https://github.com/mygodness100}
 */
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class ConfigProperties {

	private CrawlerProperties crawler = new CrawlerProperties();
}