package com.wy.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2026-06-29 16:40:55
 */
@Data
@Configuration
@ConfigurationProperties("config.knowledge")
public class KnowledgeProperties {

	private String dirRoot;
}